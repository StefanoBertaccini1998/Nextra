# Nextra — ERP per PMI (MVP)

Nextra è un ERP leggero e **personalizzabile** per piccole/medie aziende di divani artigianali.
MVP: **Ordini** di modelli configurabili → **calcolo materiali** (tessuto/fusto/piedi) → **magazzino light** → **piano settimanale** → **checklist QC**.

## Stack
- **Backend**: Spring Boot 3 (Java 21), PostgreSQL 16, Flyway, JPA/Hibernate
- **Frontend**: React + TypeScript (Vite), MUI, AG Grid, RHF + Zod
- **Auth (dev)**: Keycloak
- **Dev**: Docker Compose (Postgres, Keycloak), GitHub Actions CI

## Avvio rapido (sviluppo)
```bash
# servizi base
docker compose up -d postgres keycloak

# backend (porta 8080)
cd backend && ./gradlew bootRun

# frontend (porta 5173)
cd ../web && pnpm install && pnpm dev
