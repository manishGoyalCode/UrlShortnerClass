# URL Shortener (Spring Boot + MySQL)

A simple URL shortener service built using **Spring Boot** and **MySQL**.

Features:

* Create short URLs
* Fetch original URL using short code
* MySQL persistence
* Optional in-memory cache controlled via **feature flag**

---

# 1. Prerequisites

Make sure the following are installed:

* Java 17+
* Maven
* Docker

Check versions:

```bash
java -version
mvn -version
docker --version
```

---

# 2. Start MySQL using Docker

Run MySQL container:

```bash
docker run -d \
--name mysql-url-shortener \
-e MYSQL_ROOT_PASSWORD=password \
-e MYSQL_DATABASE=url_shortener \
-p 3306:3306 \
mysql:8
```

Check container:

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

Verify table:

```sql
SHOW TABLES;
```

---

# 5. Environment Variables

The application reads configuration from **environment variables**.

Required variables:

```
MYSQL_HOST
MYSQL_PORT
MYSQL_DB
MYSQL_USER
MYSQL_PASSWORD
CACHE_ENABLED
```

Example values:

```
MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_DB=url_shortener
MYSQL_USER=root
MYSQL_PASSWORD=password
CACHE_ENABLED=true
```
or keep this in .env file which is similar to .env.example

---

# 6. Export Environment Variables (Mac / Linux)

Run:

```bash
export MYSQL_HOST=localhost
export MYSQL_PORT=3306
export MYSQL_DB=url_shortener
export MYSQL_USER=root
export MYSQL_PASSWORD=password
export CACHE_ENABLED=true
```
or with file :
```bash

export $(grep -v '^#' .env | xargs)
```

Verify:

```bash
echo $MYSQL_HOST
```

---

# 7. Run the Project

From the project root:

```bash
./mvnw spring-boot:run
```

Server will start at:

```
http://localhost:8080
```

---

# 8. Test the APIs

### Create Short URL

```
POST /url/shorten
```

Example:

```bash
curl -X POST http://localhost:8080/url/shorten \
-H "Content-Type: application/json" \
-d '{"url":"https://google.com"}'
```

Response:

```json
{
  "shortCode": "a1b2c3",
  "shortUrl": "http://localhost:8080/url/a1b2c3"
}
```

---

### Fetch Original URL

```
GET /url/{shortCode}
```

Example:

```bash
curl http://localhost:8080/url/a1b2c3
```

Response:

```json
{
  "shortCode": "a1b2c3",
  "originalUrl": "https://google.com"
}
```

---

# 9. Cache Feature Flag

The service supports **optional in-memory caching**.

Enable cache:

```
CACHE_ENABLED=true
```

Disable cache:

```
CACHE_ENABLED=false
```

When enabled:

* URLs are cached in a **ConcurrentHashMap**
* Reduces DB lookups.

---

# 10. Stop MySQL Container

```bash
docker stop mysql-url-shortener
```

Remove container:

```bash
docker rm mysql-url-shortener
```

---

# 11. Architecture

```
Client
  ↓
Spring Boot API
  ↓
In-memory Cache (optional)
  ↓
MySQL
```

---

# 12. Tech Stack

* Java 17
* Spring Boot
* Spring Data JPA
* MySQL
* Docker

---
