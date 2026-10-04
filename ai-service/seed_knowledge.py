import hashlib
import uuid
from datetime import datetime, timezone

import psycopg2
from psycopg2.extras import register_uuid

from config import Settings
from gemini_client import GeminiClient


# Register Python UUID objects with psycopg2
register_uuid()


KNOWLEDGE_CHUNKS = [
    {
        "title": "Budgeting Basics: 50-30-20 Rule",
        "content": (
            "The 50/30/20 rule is a simple and effective budgeting strategy "
            "designed to help individuals manage their finances and save for "
            "the future. It divides after-tax income into three categories: "
            "Needs, Wants, and Savings/Debt Repayment. 50% goes to Needs: "
            "These are essential expenses required for survival and basic "
            "living. Examples include rent or mortgage payments, groceries, "
            "utilities (electricity, water, internet), minimum debt payments, "
            "and basic insurance. 30% goes to Wants: These are non-essential "
            "expenses that enhance your lifestyle but aren't strictly "
            "necessary. Examples include dining out, entertainment, travel, "
            "subscriptions, and luxury items. 20% goes to Savings and Debt "
            "Repayment: This portion is crucial for building financial "
            "security. It should be directed towards building an emergency "
            "fund, saving for retirement, investing, or making extra payments "
            "on high-interest debt beyond the minimum required. By sticking "
            "to this framework, individuals can ensure they are covering "
            "their bases while still enjoying life and securing their "
            "financial future."
        ),
    },
    {
        "title": "Emergency Fund Importance",
        "content": (
            "An emergency fund is a stash of money set aside to cover "
            "unexpected financial shocks. Its primary purpose is to provide "
            "a financial safety net, preventing you from going into debt or "
            "liquidating long-term investments when emergencies arise. Common "
            "emergencies include sudden job loss, unexpected medical bills, "
            "major car repairs, or urgent home maintenance. Financial experts "
            "generally recommend saving three to six months' worth of "
            "essential living expenses. Essential living expenses include "
            "rent/mortgage, groceries, utilities, insurance premiums, and "
            "minimum debt payments. For individuals with less stable income "
            "(e.g., freelancers) or high medical risks, a larger fund of "
            "six to twelve months may be advisable. The fund should be highly "
            "liquid and easily accessible, typically kept in a high-yield "
            "savings account where it can earn some interest while remaining "
            "instantly available without penalties for withdrawal."
        ),
    },
    {
        "title": "Compound Interest Explained",
        "content": (
            "Compound interest is often referred to as the 'eighth wonder "
            "of the world' because of its powerful effect on wealth "
            "accumulation over time. It is the interest calculated on the "
            "initial principal, which also includes all of the accumulated "
            "interest from previous periods. In simple terms, it's 'interest "
            "on interest.' The formula for compound interest is "
            "A = P (1 + r/n)^(nt), where A is the future value of the "
            "investment, P is the principal investment amount, r is the "
            "annual interest rate (decimal), n is the number of times that "
            "interest is compounded per unit t, and t is the time the money "
            "is invested for in years. The key takeaway is that the earlier "
            "you start investing, the more time your money has to compound, "
            "leading to exponential growth. Even small regular contributions "
            "can grow significantly over decades due to the compounding effect."
        ),
    },
    {
        "title": "SIP (Systematic Investment Plan) Basics",
        "content": (
            "A Systematic Investment Plan (SIP) is a method of investing a "
            "fixed sum of money regularly (e.g., monthly, quarterly) in a "
            "mutual fund. SIPs encourage financial discipline and regular "
            "saving habits. One of the primary benefits of an SIP is Rupee "
            "Cost Averaging. Since you invest a fixed amount regularly, you "
            "buy more units when the market is low and fewer units when the "
            "market is high. Over time, this averages out the cost of your "
            "investments and mitigates the impact of market volatility. SIPs "
            "also leverage the power of compounding, as returns generated on "
            "the investment are reinvested to generate further returns. They "
            "are an accessible way for individuals to start investing in "
            "equity markets without needing a large lump sum, making wealth "
            "creation accessible to a broader demographic."
        ),
    },
    {
        "title": "Mutual Funds Types (Equity, Debt, Hybrid)",
        "content": (
            "Mutual funds pool money from multiple investors to purchase a "
            "diversified portfolio of securities. They generally fall into "
            "three main categories. Equity Funds invest primarily in stocks "
            "(shares) of companies. They offer the potential for high returns "
            "but also carry higher risk and market volatility. They are "
            "suitable for long-term financial goals. Debt Funds invest in "
            "fixed-income securities like government bonds, corporate bonds, "
            "and treasury bills. They aim to provide regular income and "
            "capital preservation, carrying lower risk than equity funds but "
            "also lower potential returns. Hybrid Funds invest in a mix of "
            "both equities and debt. They aim to balance risk and return by "
            "offering the growth potential of equities and the stability of "
            "debt. The specific allocation between equity and debt dictates "
            "the fund's risk profile."
        ),
    },
    {
        "title": "Tax Saving under Section 80C",
        "content": (
            "Section 80C of the Income Tax Act provides provisions for tax "
            "deductions on specific investments and expenses, encouraging "
            "individuals to save for the future while reducing their taxable "
            "income. The maximum deduction allowed under this section is "
            "₹1.5 lakh per financial year. Popular investment options "
            "eligible for 80C deductions include the Public Provident Fund "
            "(PPF), Employees' Provident Fund (EPF), Equity Linked Savings "
            "Scheme (ELSS) mutual funds, National Savings Certificate (NSC), "
            "5-year tax-saving Fixed Deposits (FDs), and life insurance "
            "premiums. Additionally, certain expenses like the principal "
            "repayment of a home loan and tuition fees for children's "
            "education are also eligible. By strategically investing in "
            "these instruments, taxpayers can significantly lower their tax "
            "liability while building long-term wealth."
        ),
    },
    {
        "title": "Importance of Health Insurance",
        "content": (
            "Health insurance is a vital component of financial planning "
            "because it protects individuals and families from severe "
            "financial hardship due to medical emergencies. Medical costs, "
            "including hospitalization, surgeries, and critical illness "
            "treatments, are rising rapidly due to medical inflation. A "
            "comprehensive health insurance policy covers these expenses, "
            "preventing the depletion of savings or the need to take on "
            "burdensome debt to pay hospital bills. Beyond hospitalization, "
            "many policies also cover pre and post-hospitalization expenses, "
            "daycare procedures, and sometimes even annual health check-ups. "
            "Having adequate health insurance ensures access to quality "
            "healthcare without financial stress, allowing individuals to "
            "focus on recovery rather than worrying about the bills. It is "
            "fundamentally a tool for risk transfer."
        ),
    },
    {
        "title": "Debt Management Strategies",
        "content": (
            "Effective debt management is crucial for financial stability. "
            "Two popular strategies for paying off multiple debts are the "
            "Snowball Method and the Avalanche Method. The Debt Snowball "
            "Method involves paying off debts from smallest balance to "
            "largest, regardless of interest rate. This provides psychological "
            "'wins' and motivation as smaller debts are eliminated quickly. "
            "The Debt Avalanche Method involves paying off debts with the "
            "highest interest rates first. This is mathematically the most "
            "efficient method, as it minimizes the total amount of interest "
            "paid over time. Beyond these methods, general debt management "
            "principles include always making at least the minimum payments "
            "on all debts to avoid penalties, avoiding taking on new "
            "high-interest debt, and considering debt consolidation if it "
            "results in a lower overall interest rate."
        ),
    },
    {
        "title": "Risk vs Return Relationship",
        "content": (
            "The risk-return tradeoff is a fundamental principle in finance "
            "stating that potential return rises with an increase in risk. "
            "Individuals associate low levels of uncertainty (low risk) with "
            "low potential returns, and high levels of uncertainty (high "
            "risk) with high potential returns. For example, a government "
            "bond is considered very low risk, and therefore offers a "
            "relatively low return. Conversely, investing in a startup "
            "company is very high risk, but offers the potential for very "
            "high returns if the company succeeds. Investors must assess "
            "their own risk tolerance—their ability and willingness to lose "
            "some or all of their original investment in exchange for "
            "greater potential returns—when constructing their investment "
            "portfolio. A well-diversified portfolio aims to balance this "
            "tradeoff according to the investor's specific goals and timeline."
        ),
    },
    {
        "title": "Power of Starting Early (Time Value of Money)",
        "content": (
            "The Time Value of Money (TVM) is the concept that a sum of money "
            "is worth more now than the same sum will be at a future date due "
            "to its earning potential in the interim. This principle "
            "underscores the immense power of starting to invest early. "
            "Because of compound interest, money invested earlier has more "
            "time to grow. For instance, an individual who starts investing "
            "a small amount in their 20s can often accumulate a larger "
            "retirement nest egg than someone who starts investing a much "
            "larger amount in their 40s. Delaying investment not only means "
            "missing out on potential returns but also requires significantly "
            "higher contributions later in life to achieve the same financial "
            "goals. Time is the most valuable asset an investor possesses."
        ),
    },
]


SOURCE_NAME = "TREZI Internal Knowledge Base"
SOURCE_ORGANIZATION = "TREZI"
SOURCE_TYPE = "INTERNAL"
SOURCE_BASE_URL = "https://trezi.local"
SOURCE_AUTHORITY_LEVEL = "INTERNAL"

DOCUMENT_SOURCE_URL = "https://trezi.local/knowledge"
DOCUMENT_TYPE = "INTERNAL"


def get_connection(settings):
    return psycopg2.connect(
        host=settings.TREZI_AI_DB_HOST,
        port=settings.TREZI_AI_DB_PORT,
        dbname=settings.TREZI_AI_DB_NAME,
        user=settings.TREZI_AI_DB_USERNAME,
        password=settings.TREZI_AI_DB_PASSWORD,
    )


def get_or_create_source(cur):
    """
    Get the TREZI internal source if it already exists.
    Otherwise create it.
    """

    cur.execute(
        """
        SELECT id
        FROM knowledge_sources
        WHERE name = %s
        LIMIT 1
        """,
        (SOURCE_NAME,),
    )

    row = cur.fetchone()

    if row:
        return row[0]

    source_id = uuid.uuid4()

    cur.execute(
        """
        INSERT INTO knowledge_sources (
            id,
            name,
            organization,
            source_type,
            base_url,
            authority_level,
            is_active
        )
        VALUES (%s, %s, %s, %s, %s, %s, %s)
        RETURNING id
        """,
        (
            source_id,
            SOURCE_NAME,
            SOURCE_ORGANIZATION,
            SOURCE_TYPE,
            SOURCE_BASE_URL,
            SOURCE_AUTHORITY_LEVEL,
            True,
        ),
    )

    return cur.fetchone()[0]


def get_or_create_document(cur, source_id, title, content_hash):
    """
    Find an existing document using source + title + content hash.
    Create it if it does not exist.
    """

    cur.execute(
        """
        SELECT id
        FROM knowledge_documents
        WHERE source_id = %s
          AND title = %s
          AND content_hash = %s
        LIMIT 1
        """,
        (
            source_id,
            title,
            content_hash,
        ),
    )

    row = cur.fetchone()

    if row:
        return row[0]

    document_id = uuid.uuid4()
    now = datetime.now(timezone.utc)

    cur.execute(
        """
        INSERT INTO knowledge_documents (
            id,
            source_id,
            title,
            document_type,
            source_url,
            document_identifier,
            publication_date,
            effective_date,
            retrieved_at,
            content_hash,
            status
        )
        VALUES (
            %s,
            %s,
            %s,
            %s,
            %s,
            %s,
            %s,
            %s,
            %s,
            %s,
            %s
        )
        RETURNING id
        """,
        (
            document_id,
            source_id,
            title,
            DOCUMENT_TYPE,
            DOCUMENT_SOURCE_URL,
            f"trezi-internal-{document_id}",
            None,
            None,
            now,
            content_hash,
            "ACTIVE",
        ),
    )

    return cur.fetchone()[0]


def get_or_create_version(
    cur,
    document_id,
    content_hash,
    content,
):
    """
    Find an existing document version or create one.

    Matches the actual database schema:
        version_label
        publication_date
        effective_date
        source_url
        content_hash
        retrieved_at
        is_current
        raw_content
    """

    cur.execute(
        """
        SELECT id
        FROM knowledge_document_versions
        WHERE document_id = %s
          AND content_hash = %s
        LIMIT 1
        """,
        (
            document_id,
            content_hash,
        ),
    )

    row = cur.fetchone()

    if row:
        return row[0]

    version_id = uuid.uuid4()
    now = datetime.now(timezone.utc)

    cur.execute(
        """
        INSERT INTO knowledge_document_versions (
            id,
            document_id,
            version_label,
            publication_date,
            effective_date,
            source_url,
            content_hash,
            retrieved_at,
            is_current,
            raw_content
        )
        VALUES (
            %s,
            %s,
            %s,
            %s,
            %s,
            %s,
            %s,
            %s,
            %s,
            %s
        )
        RETURNING id
        """,
        (
            version_id,
            document_id,
            "1.0",
            None,
            None,
            DOCUMENT_SOURCE_URL,
            content_hash,
            now,
            True,
            content,
        ),
    )

    return cur.fetchone()[0]


def chunk_exists(cur, document_version_id):
    """
    Check whether chunk 0 already exists for this version.
    """

    cur.execute(
        """
        SELECT id
        FROM knowledge_chunks
        WHERE document_version_id = %s
          AND chunk_index = 0
        LIMIT 1
        """,
        (document_version_id,),
    )

    row = cur.fetchone()

    return row[0] if row else None


def insert_chunk(
    cur,
    document_version_id,
    section_title,
    content,
    embedding,
):
    """
    Insert one knowledge chunk.

    The database requires vector(1536).
    """

    if not embedding:
        raise ValueError(
            f"Embedding generation returned an empty embedding "
            f"for '{section_title}'."
        )

    if len(embedding) != 1536:
        raise ValueError(
            f"Invalid embedding dimension for '{section_title}'. "
            f"Expected 1536, received {len(embedding)}."
        )

    embedding_str = "[" + ",".join(
        str(float(value)) for value in embedding
    ) + "]"

    chunk_id = uuid.uuid4()

    token_count = len(content.split())

    if token_count <= 0:
        raise ValueError(
            f"Content for '{section_title}' is empty."
        )

    cur.execute(
        """
        INSERT INTO knowledge_chunks (
            id,
            document_version_id,
            chunk_index,
            section_title,
            content,
            embedding,
            token_count
        )
        VALUES (
            %s,
            %s,
            %s,
            %s,
            %s,
            %s::vector,
            %s
        )
        RETURNING id
        """,
        (
            chunk_id,
            document_version_id,
            0,
            section_title,
            content,
            embedding_str,
            token_count,
        ),
    )

    return cur.fetchone()[0]


def seed():
    settings = Settings()

    if not settings.GEMINI_API_KEY:
        raise ValueError("GEMINI_API_KEY is missing.")

    gemini = GeminiClient(
        api_key=settings.GEMINI_API_KEY,
        model=settings.GEMINI_MODEL,
    )

    conn = None

    try:
        conn = get_connection(settings)

        print("Connected to PostgreSQL.")
        print("Starting TREZI knowledge base seeding...")
        print(f"Knowledge items: {len(KNOWLEDGE_CHUNKS)}")
        print()

        with conn.cursor() as cur:

            # ---------------------------------------------------------
            # 1. Source
            # ---------------------------------------------------------

            source_id = get_or_create_source(cur)

            print(f"Knowledge source ready: {source_id}")
            print()

            # ---------------------------------------------------------
            # 2. Documents / Versions / Embeddings / Chunks
            # ---------------------------------------------------------

            created_chunks = 0
            existing_chunks = 0

            for index, chunk_data in enumerate(KNOWLEDGE_CHUNKS, start=1):

                title = chunk_data["title"]
                content = chunk_data["content"]

                print(
                    f"[{index}/{len(KNOWLEDGE_CHUNKS)}] {title}"
                )

                # SHA-256 hash of the knowledge content
                content_hash = hashlib.sha256(
                    content.encode("utf-8")
                ).hexdigest()

                # -----------------------------------------------------
                # Document
                # -----------------------------------------------------

                document_id = get_or_create_document(
                    cur=cur,
                    source_id=source_id,
                    title=title,
                    content_hash=content_hash,
                )

                # -----------------------------------------------------
                # Document version
                # -----------------------------------------------------

                version_id = get_or_create_version(
                    cur=cur,
                    document_id=document_id,
                    content_hash=content_hash,
                    content=content,
                )

                # -----------------------------------------------------
                # Chunk
                # -----------------------------------------------------

                existing_chunk_id = chunk_exists(
                    cur,
                    version_id,
                )

                if existing_chunk_id:

                    print(
                        f"    Chunk already exists: "
                        f"{existing_chunk_id}"
                    )

                    existing_chunks += 1
                    continue

                # -----------------------------------------------------
                # Gemini embedding
                # -----------------------------------------------------

                print("    Generating embedding...")

                embedding = gemini.embed([content])[0]

                print(
                    f"    Embedding dimension: {len(embedding)}"
                )

                # -----------------------------------------------------
                # Insert vector chunk
                # -----------------------------------------------------

                chunk_id = insert_chunk(
                    cur=cur,
                    document_version_id=version_id,
                    section_title=title,
                    content=content,
                    embedding=embedding,
                )

                print(
                    f"    Chunk inserted: {chunk_id}"
                )

                created_chunks += 1
                print()

            # ---------------------------------------------------------
            # Commit everything
            # ---------------------------------------------------------

            conn.commit()

            print()
            print("=" * 60)
            print("SUCCESS: TREZI RAG knowledge base seeded.")
            print("=" * 60)
            print(f"Documents processed: {len(KNOWLEDGE_CHUNKS)}")
            print(f"New chunks created:  {created_chunks}")
            print(f"Existing chunks:     {existing_chunks}")
            print()

    except Exception as e:

        if conn:
            conn.rollback()

        print()
        print("=" * 60)
        print("ERROR: Knowledge seeding failed.")
        print("=" * 60)
        print(type(e).__name__)
        print(str(e))
        print()
        raise

    finally:

        if conn:
            conn.close()
            print("Database connection closed.")


if __name__ == "__main__":
    seed()