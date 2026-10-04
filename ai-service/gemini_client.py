from google import genai
from google.genai import types


class GeminiClient:
    def __init__(self, api_key: str, model: str):
        self.client = genai.Client(api_key=api_key)
        self.model = model

    def generate(
        self,
        prompt: str,
        system_instruction: str | None = None
    ) -> str:

        config = types.GenerateContentConfig(
            system_instruction=system_instruction,
            temperature=0.3,
            max_output_tokens=2048,
        )

        response = self.client.models.generate_content(
            model=self.model,
            contents=prompt,
            config=config,
        )

        return response.text

    def embed(self, texts: list[str]) -> list[list[float]]:
        """Generate embeddings for semantic retrieval."""

        result = []

        for text in texts:
            response = self.client.models.embed_content(
                model="gemini-embedding-2",
                contents=text,
                config=types.EmbedContentConfig(
                    output_dimensionality=1536,
                ),
            )

            result.append(response.embeddings[0].values)

        return result