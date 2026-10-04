from pydantic_settings import BaseSettings, SettingsConfigDict

class Settings(BaseSettings):
    TREZI_AI_DB_HOST: str = "localhost"
    TREZI_AI_DB_PORT: int = 5432
    TREZI_AI_DB_NAME: str = "trezi"
    TREZI_AI_DB_USERNAME: str
    TREZI_AI_DB_PASSWORD: str
    GEMINI_API_KEY: str
    GEMINI_MODEL: str = "gemini-3.6-flash"
    EMBEDDING_MODEL: str = "text-embedding-004"
    EMBEDDING_DIMENSIONS: int = 1536

    model_config = SettingsConfigDict(env_file='.env', env_file_encoding='utf-8')
