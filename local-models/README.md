# Local Models (Ollama)

This folder contains Docker Compose for a local Ollama server with an OpenAI-compatible API.

## Start

```bash
docker compose -f /data/models/docker-compose.yml up -d
```

The first start will pull:
- `llama3:8b`
- `nomic-embed-text`

## OpenAI-Compatible API

Base URL:

```
http://localhost:11434/v1
```

Most OpenAI client SDKs require an API key field. Ollama does not enforce one; use any placeholder string.

## Chat Example (curl)

```bash
curl http://localhost:11434/v1/chat/completions \
  -H "Content-Type: application/json" \
  -d '{
    "model": "llama3:8b",
    "messages": [
      {"role": "user", "content": "Write a 2 sentence summary of Docker Compose."}
    ]
  }'
```

## Embeddings Example (curl)

```bash
curl http://localhost:11434/v1/embeddings \
  -H "Content-Type: application/json" \
  -d '{
    "model": "nomic-embed-text",
    "input": "Text you want embedded"
  }'
```
