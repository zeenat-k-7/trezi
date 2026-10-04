# TREZI — Engineering Instructions

## Project

TREZI is a final-year engineering project focused on Financial Literacy using Agentic AI.

The system provides financial education and personalized financial guidance while emphasizing:
- knowledge grounding
- explainability
- compliance
- privacy
- security
- auditability

## Architecture

Frontend:
React + Vite

Primary backend:
Spring Boot

AI service:
Separate Python service

Database:
PostgreSQL + pgvector

LLM:
Gemini

Request flow:

React
  -> Spring Boot
  -> Python AI Service
  -> Gemini

Spring Boot owns:
- authentication and authorization
- user/profile management
- financial data
- assessments
- conversations
- business logic
- AI gateway
- audit logging
- API security

Python owns:
- AI orchestration
- Diagnostic Agent
- Pedagogical Agent
- Compliance Agent
- RAG
- Multi-HyDE
- structured financial-data tools
- verification
- reasoning traces
- Gemini integration

## Database Authority

The authoritative database schema is:

database/flyway/

All Flyway migrations must be inspected before implementing database-dependent code.

Flyway is the single source of truth for the database schema.

Never:
- invent database columns
- invent tables
- remove tables
- modify existing schema for convenience
- manually create production schema outside Flyway
- use Hibernate schema generation

Hibernate/JPA must validate the existing schema rather than create or modify it.

Use:

spring.jpa.hibernate.ddl-auto=validate

when appropriate.

## Database Conventions

- PostgreSQL
- pgcrypto
- pgvector
- UUID primary keys
- gen_random_uuid()
- TIMESTAMPTZ for timestamps
- NUMERIC(15,2) for monetary values
- NUMERIC(20,8) for investment quantities
- VARCHAR + CHECK constraints for database enum-like fields
- vector(1536) for embeddings
- BigDecimal for monetary values
- UUID for UUID fields
- appropriate Java time types for timestamps

Never use float/double for financial values.

## Security

Follow defense-in-depth and least privilege.

Never:
- disable authentication for convenience
- change PostgreSQL authentication to trust
- expose PostgreSQL publicly
- expose Gemini credentials
- commit secrets
- log passwords, tokens, API keys, or sensitive financial data
- give the AI service unrestricted database access
- allow autonomous financial transactions

Do not modify pg_hba.conf unless explicitly approved.

Do not weaken security configuration merely to make development easier.

## AI Architecture

Gemini is not the financial source of truth.

Authoritative information comes from:
- verified RAG documents
- structured financial data
- deterministic calculators
- explicit application rules

LLMs are used primarily for:
- language understanding
- explanation
- reasoning over verified context
- personalization of explanations

Do not use an LLM for deterministic calculations when normal application logic is appropriate.

Do not expose private chain-of-thought.

TREZI uses structured reasoning traces for auditability.

## Development Rules

Before implementing a task:

1. Inspect the existing repository.
2. Inspect relevant source files.
3. Inspect relevant Flyway migrations.
4. Understand existing architecture.
5. Identify conflicts before making changes.

Implement only the requested task.

Do not perform unrelated refactors.

Do not introduce unnecessary dependencies.

Prefer production-style, maintainable implementations.

Keep the architecture extensible without adding unnecessary complexity.

After implementation:

1. Compile.
2. Run relevant tests.
3. Validate database mappings where applicable.
4. Report files changed.
5. Report validation results.
6. Report unresolved issues.

If an architectural or schema conflict is discovered:

STOP and report it instead of silently changing the architecture.

## Current Development Strategy

Build the working foundation first.

Then progressively add:

1. persistence
2. REST APIs
3. authentication/security
4. financial functionality
5. assessment
6. conversation system
7. Python AI service
8. RAG
9. structured financial-data ingestion
10. AI orchestration
11. agents
12. verification
13. compliance
14. reasoning traces
15. frontend integration
16. testing
17. deployment

Do not implement future stages unless explicitly requested.

## DEMO DEADLINE MODE

The project has an immediate academic demonstration deadline.

Prioritize a working end-to-end MVP over exhaustive production implementation.

When implementing a task:

1. Preserve the established architecture and database schema.
2. Prefer completing an end-to-end demonstrable flow over isolated infrastructure.
3. Reuse existing implementations whenever possible.
4. Do not introduce unnecessary abstractions.
5. Do not implement research/future-scope features unless explicitly requested.
6. Do not spend excessive effort on speculative production optimizations.
7. Do not modify Flyway migrations unless explicitly approved.
8. Never weaken security to make development easier.
9. If a feature cannot be fully implemented within the current task, implement the smallest honest working version and clearly report the limitation.
10. Never fabricate functionality or claim an unimplemented feature is complete.

DEMO PRIORITY:

P0:
- application starts
- database works
- authentication
- user profile
- financial data
- literacy assessment
- AI assistant
- RAG grounding
- Diagnostic Agent
- Pedagogical Agent
- Compliance Agent
- sources/evidence
- basic reasoning trace
- frontend integration

P1:
- budgets
- financial goals
- investment holdings
- dashboard visualizations
- audit logs

P2:
- advanced ingestion automation
- advanced security hardening
- optimization
- deployment refinements
- research extensions

For demo purposes, deterministic mock/reference data may be used ONLY where the architecture explicitly permits it and it must be clearly identifiable as demo/reference data. Never fabricate regulatory or financial facts.