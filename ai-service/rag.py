import psycopg2
import numpy as np

class RAGRetriever:
    def __init__(self, db_config: dict, gemini_client):
        self.db_config = db_config
        self.gemini_client = gemini_client

    def _get_connection(self):
        return psycopg2.connect(
            host=self.db_config['host'],
            port=self.db_config['port'],
            dbname=self.db_config['dbname'],
            user=self.db_config['user'],
            password=self.db_config['password'],
        )

    def search(self, query: str, top_k: int = 5) -> list[dict]:
        """Search for relevant knowledge chunks using cosine similarity."""
        # 1. Embed the query
        query_embedding = self.gemini_client.embed([query])[0]
        embedding_str = '[' + ','.join(str(x) for x in query_embedding) + ']'

        # 2. Search using cosine distance (pgvector operator: <=>)
        sql = """
            SELECT
                kc.id,
                kc.section_title,
                kc.content,
                kd.title AS document_title,
                ks.name AS source_name,
                1 - (kc.embedding <=> %s::vector) AS relevance_score
            FROM knowledge_chunks kc
            JOIN knowledge_document_versions kdv ON kdv.id = kc.document_version_id
            JOIN knowledge_documents kd ON kd.id = kdv.document_id
            JOIN knowledge_sources ks ON ks.id = kd.source_id
            WHERE kdv.is_current = TRUE
            ORDER BY kc.embedding <=> %s::vector
            LIMIT %s
        """

        conn = self._get_connection()
        try:
            with conn.cursor() as cur:
                cur.execute(sql, (embedding_str, embedding_str, top_k))
                rows = cur.fetchall()
                results = []
                for row in rows:
                    results.append({
                        'chunk_id': str(row[0]),
                        'section_title': row[1],
                        'content': row[2],
                        'document_title': row[3],
                        'source_name': row[4],
                        'relevance_score': float(row[5]) if row[5] else 0.0,
                    })
                return results
        finally:
            conn.close()
