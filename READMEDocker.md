# URL Shortener (Spring Boot + MySQL + Docker)

A simple **URL Shortener service** built using **Spring Boot** and **MySQL**.

This project demonstrates how to move **from code → production-ready setup** using:

* Spring Boot REST APIs
* MySQL persistence
* Optional in-memory cache
* Environment variables
* Docker
* Docker Compose

Anyone can run the entire application with **one command**.

---

# 1. Prerequisites

Make sure the following are installed:

* Java 17+ (optional if using Docker)
* Docker
* Docker Compose

Verify installation:

```bash
docker --version
docker compose version
```

---

# 2. Start MySQL (via Docker)

The database runs **separately from the application container**.

Start MySQL:

```bash
docker run -d \
--name mysql-url-shortener \
-e MYSQL_ROOT_PASSWORD=password \
-e MYSQL_DATABASE=url_shortener \
-p 3306:3306 \
mysql:8
```

Verify container:

```bash
docker ps
```

---

# 3. Connect to MySQL

Open MySQL shell:

```bash
docker exec -it mysql-url-shortener mysql -uroot -p
```

Password:

```
password
```

Switch database:

```sql
USE url_shortener;
```

---

# 4. Create Table

Run this SQL:

```sql
CREATE TABLE url_mapping (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    short_code VARCHAR(20) UNIQUE,
    original_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

Verify:

```sql
SHOW TABLES;
```

---

# 5. Environment Variables

The application reads configuration from **environment variables**.

| Variable       | Description            |
| -------------- | ---------------------- |
| MYSQL_HOST     | MySQL server hostname  |
| MYSQL_PORT     | MySQL port             |
| MYSQL_DB       | Database name          |
| MYSQL_USER     | Database user          |
| MYSQL_PASSWORD | Database password      |
| CACHE_ENABLED  | Enable in-memory cache |

Example values:

```
MYSQL_HOST=host.docker.internal
MYSQL_PORT=3306
MYSQL_DB=url_shortener
MYSQL_USER=root
MYSQL_PASSWORD=password
CACHE_ENABLED=true
```

---

# 6. Dockerfile

Create a file named:

```
Dockerfile
```

Add the following **multi-stage build**:

```dockerfile
# ---------- Build Stage ----------
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /build

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests


# ---------- Runtime Stage ----------
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
```

Why multi-stage builds?

* First stage builds the application
* Final image contains **only the JAR + Java runtime**
* Smaller production image

---

# 7. Docker Compose

Create file:

```
docker-compose.yml
```

Add:

```yaml
version: "3.8"

services:
  app:
    build: .
    container_name: url-shortener-app
    ports:
      - "8080:8080"
    environment:
      MYSQL_HOST: host.docker.internal
      MYSQL_PORT: 3306
      MYSQL_DB: url_shortener
      MYSQL_USER: root
      MYSQL_PASSWORD: password
      CACHE_ENABLED: true
```

Important:

```
MYSQL_HOST=host.docker.internal
```

This allows the container to connect to **MySQL running on the host machine**.

---

# 8. Start the Application

Run:

```bash
docker compose up
```

Docker will:

1. Build the application image
2. Compile the project using Maven
3. Start the Spring Boot container

Run in background:

```bash
docker compose up -d
```

---

# 9. Verify Containers

Check running containers:

```bash
docker compose ps
```

Check logs:

```bash
docker compose logs -f
```

---

# 10. Test APIs

### Create Short URL

```bash
curl -X POST http://localhost:8080/url/shorten \
-H "Content-Type: application/json" \
-d '{"url":"https://google.com"}'
```

Example response:

```json
{
  "shortCode": "a1b2c3",
  "shortUrl": "http://localhost:8080/url/a1b2c3"
}
```

---

### Fetch Original URL

```bash
curl http://localhost:8080/url/a1b2c3
```

Example response:

```json
{
  "shortCode": "a1b2c3",
  "originalUrl": "https://google.com"
}
```

---

# 11. Cache Feature Flag

The application supports **optional in-memory caching**.

Enable cache:

```
CACHE_ENABLED=true
```

Disable cache:

```
CACHE_ENABLED=false
```

When enabled:

* URLs are stored in a **ConcurrentHashMap**
* Reduces database reads.

---

# 12. Stop the Application

Stop containers:

```bash
docker compose down
```

Stop MySQL:

```bash
docker stop mysql-url-shortener
```

Remove MySQL container:

```bash
docker rm mysql-url-shortener
```

---

# 13. Architecture

```
Client
   ↓
Spring Boot Container
   ↓
In-Memory Cache (optional)
   ↓
MySQL (External Container / Local)
```

---

# 14. Tech Stack

* Java 17
* Spring Boot
* Spring Data JPA
* MySQL
* Docker
* Docker Compose

---

# 15. Run the Entire System

Start MySQL:

```bash
docker run -d \
--name mysql-url-shortener \
-e MYSQL_ROOT_PASSWORD=password \
-e MYSQL_DATABASE=url_shortener \
-p 3306:3306 \
mysql:8
```

Start application:

```bash
docker compose up
```

The API will be available at:

```
http://localhost:8080
```

---
