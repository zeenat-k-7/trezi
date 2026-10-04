from fastapi import FastAPI, HTTPException, Header
from fastapi.middleware.cors import CORSMiddleware
from config import Settings
from models import ChatRequest, ChatResponse
from gemini_client import GeminiClient
from rag import RAGRetriever
from orchestrator import Orchestrator
from fastapi.exceptions import RequestValidationError
from fastapi.responses import JSONResponse

settings = Settings()
app = FastAPI(title="TREZI AI Service", version="0.1.0")

# CORS — only Spring Boot backend should call this, but allow for dev
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:8080"],
    allow_methods=["*"],
    allow_headers=["*"],
)

# Initialize components
gemini_client = GeminiClient(api_key=settings.GEMINI_API_KEY, model=settings.GEMINI_MODEL)

rag_retriever = RAGRetriever(
    db_config={
        'host': settings.TREZI_AI_DB_HOST,
        'port': settings.TREZI_AI_DB_PORT,
        'dbname': settings.TREZI_AI_DB_NAME,
        'user': settings.TREZI_AI_DB_USERNAME,
        'password': settings.TREZI_AI_DB_PASSWORD,
    },
    gemini_client=gemini_client,
)

orchestrator = Orchestrator(gemini_client, rag_retriever, settings)

@app.get("/health")
def health():
    return {"status": "ok", "service": "trezi-ai-service"}

from fastapi import Request

@app.exception_handler(RequestValidationError)
async def validation_exception_handler(request, exc):
    print("========== VALIDATION ERROR ==========")
    print("URL:", request.url)
    print("Errors:", exc.errors())

    try:
        body = await request.body()
        print("BODY:", body.decode("utf-8"))
    except Exception as e:
        print("Could not read body:", e)

    print("======================================")

    return JSONResponse(
        status_code=422,
        content={
            "detail": exc.errors()
        }
    )

@app.post("/api/chat", response_model=ChatResponse)
async def chat(
    request: ChatRequest,
    authorization: str | None = Header(default=None)
):
    try:
        return await orchestrator.process_chat(
            request,
            auth_header=authorization
        )
    except Exception as e:
        raise HTTPException(
            status_code=500,
            detail=f"AI processing failed: {str(e)}"
        )