# 🎬 Media Streaming App

[![Java](https://img.shields.io/badge/Java-24-blue)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-green)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

A simple **Spring Boot** application for managing and streaming media assets (video/audio) with secure 10-minute streaming links.

---

## ⚡ Features

- JWT-based user **signup/login**
- Add and manage media metadata (title, type, file URL)
- Generate secure 10-min streaming links
- Media streaming for **video/mp4** and **audio/mpeg**
- Optional logging of media views by IP

---

## 🛠 Technology Stack

- **Backend**: Java 24, Spring Boot 3.5.3
- **Database**: PostgreSQL
- **Security**: JWT (JSON Web Tokens)
- **Repository**: Spring Data JPA
- **Testing**: Postman

---

## 🗄 Database Schema

### MediaAsset
| Column     | Type   | Description               |
|------------|--------|---------------------------|
| id         | Long   | Primary key               |
| title      | String | Media title               |
| type       | String | Media type (video/audio)  |
| file_url   | String | Absolute path to file     |
| created_at | Instant| Creation timestamp        |

### AdminUser
| Column        | Type   | Description         |
|---------------|--------|-------------------|
| id            | Long   | Primary key        |
| email         | String | User email         |
| hashed_password | String | Hashed password  |
| created_at    | Instant| Creation timestamp |

### MediaViewLog
| Column      | Type   | Description                 |
|-------------|--------|----------------------------|
| media_id    | Long   | Foreign key to MediaAsset  |
| viewed_by_ip| String | IP address of viewer       |
| timestamp   | Instant| View timestamp             |

---

## 🚀 API Endpoints

### Authentication
- **POST /auth/signup** → Register a new user
- **POST /auth/login** → Login and receive JWT token

### Media
- **POST /media** (Authenticated) → Add media metadata
- **GET /media/{id}/stream-url** → Get secure 10-min streaming URL
- **GET /stream?token=…** → Stream media by token

---

## 📝 Postman Testing

1. **Sign up user**
POST http://localhost:8080/auth/signup
Body (JSON):
{
"email": "user@example.com",
"password": "password123"
}



2. **Login**
POST http://localhost:8080/auth/login
Body (JSON):
{
"email": "user@example.com",
"password": "password123"
}
Response: JWT token



3. **Add media**
POST http://localhost:8080/media
Headers: Authorization: Bearer <JWT>
Body (JSON):
{
"title": "Sample Video",
"type": "video",
"fileUrl": "/absolute/path/to/file.mp4"
}



4. **Get stream link**
GET http://localhost:8080/media/1/stream-url
Headers: Authorization: Bearer <JWT>
Response: URL with token



5. **Stream media**
GET <stream-url from previous step>

6. **Log a Media view** POST /media/{id}/view Headers: Authorization: Bearer <token>

7. **Media Analytics** GET /media/{id}/analytics Headers: Authorization: Bearer <token>
   

---
🐳 Running with Docker
1. Build & Start Containers
   docker-compose up --build

2. Application Profiles

dev → Local DB + Redis

docker → Dockerized DB + Redis

prod → Production-ready (no ddl-auto, optimized logging, etc.)

🧪 Running Tests
JUnit (Java Tests)
./mvnw test

Example Test Classes

MediaServiceTest.java

AnalyticsServiceTest.java

RateLimiterTest.java

📡 API Endpoints
Media Analytics

POST /media/:id/view → Record a view (Rate Limited)

GET /media/:id/analytics → Fetch analytics (Cached with Redis)

📂 Project Structure
src/main/java/com/example/media
├── controller      # REST controllers
├── service         # Business logic
├── repository      # Data access layer
├── model           # Entities
├── config          # Security, Redis, Rate Limiting
└── MediaApplication.java

src/test/java/com/example/media
├── service         # Unit tests
├── controller      # API tests

## ⚙ Configuration

- Configure PostgreSQL in `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/media_app
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update
Ensure absolute paths in fileUrl exist on your server.