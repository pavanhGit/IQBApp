# Interview Question Bank API

Spring Boot REST API for saving interview questions and answers, auto-categorizing them, generating embeddings, and detecting semantically similar questions.

## Features

- Save interview questions and answers.
- Auto-categorize questions into `JAVA`, `SPRING`, `DSA`, `DATABASE`, or `SYSTEM_DESIGN`.
- Generate deterministic local embeddings.
- Check semantic similarity using cosine similarity.
- Increment `frequencyCount` when a similar question already exists.
- Store data in PostgreSQL.
- REST APIs only, with no authentication.

## Tech Stack

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- PostgreSQL
- Maven

## Database

Create a PostgreSQL database:

```sql
CREATE DATABASE interview_questions;
```

Default connection:

```text
url: jdbc:postgresql://db.oryshinfkqticdhvrntg.supabase.co:5432/postgres?sslmode=require
username: postgres
password: set through DB_PASSWORD
```

Override with environment variables:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
SIMILARITY_THRESHOLD
EMBEDDING_DIMENSIONS
```

For Supabase, set:

```text
DB_URL=jdbc:postgresql://db.oryshinfkqticdhvrntg.supabase.co:5432/postgres?sslmode=require
DB_USERNAME=postgres
DB_PASSWORD=your_supabase_database_password
```

## Run

```bash
mvn spring-boot:run
```

The API starts on `http://localhost:8080`.

## Run With Docker

Build the image:

```bash
docker build -t iqb-app .
```

Run the container:

```bash
docker run --rm -p 8080:8080 \
  -e DB_PASSWORD=your_supabase_database_password \
  iqb-app
```

Or run with Docker Compose:

```bash
DB_PASSWORD=your_supabase_database_password docker compose up --build
```

On Windows PowerShell:

```powershell
$env:DB_PASSWORD="your_supabase_database_password"
docker compose up --build
```

## APIs

### Save Question

```http
POST /api/questions
Content-Type: application/json
```

```json
{
  "question": "What is dependency injection in Spring?",
  "answer": "Dependency injection is a pattern where Spring provides required dependencies to a class instead of the class creating them."
}
```

If a similar question already exists, the API returns `duplicate: true` and increments `frequencyCount`.

### List Questions

```http
GET /api/questions
GET /api/questions?category=SPRING
```

### Get Question By ID

```http
GET /api/questions/1
```

### Check Similarity

```http
GET /api/questions/similarity?question=Explain DI in Spring Boot
```

## Notes

The embedding implementation is local and deterministic, so the project runs without external AI APIs. It can later be replaced with OpenAI, Hugging Face, or pgvector-based embeddings without changing the REST contract.
