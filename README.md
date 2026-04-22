# ✈️ Airport Admin – Backend

Spring Boot REST API for the Airport Administration Web App. Handles authentication, role-based access control, leave and shift management, staffing requests, and roster generation.

> Part of COMPX576 | [View Frontend Repo](https://github.com/chauhanarvind/airportadmin-frontend)

---

## 🛠️ Tech Stack

- **Framework:** Spring Boot
- **Security:** Spring Security + Stateless JWT (via `JwtAuthFilter`)
- **ORM:** JPA / Hibernate
- **Database:** PostgreSQL (hosted on Render)
- **Containerization:** Docker (multi-stage build)
- **Build Tool:** Maven

---

## 📁 Project Structure

```
src/main/java/com/airportadmin/
  controller/     → HTTP endpoints (REST controllers)
  service/        → Business logic
  dto/            → Request/response models
  entity/         → JPA entities (mapped to DB tables)
  repository/     → JpaRepository interfaces
  security/       → JwtAuthFilter, SecurityService
```

---

## ⚙️ Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL (local or remote)
- Docker (optional, for containerized run)

### Clone & Configure

```bash
git clone https://github.com/chauhanarvind/airportadmin.git
cd airportadmin
```

Create an `application.properties` or set environment variables:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/airportadmin
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
jwt.secret=your_jwt_secret
```

### Run Locally

```bash
./mvnw spring-boot:run
```

API runs at `http://localhost:8080`.

### Run with Docker

```bash
docker build -t airportadmin .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://... \
  -e SPRING_DATASOURCE_USERNAME=... \
  -e SPRING_DATASOURCE_PASSWORD=... \
  airportadmin
```

---

## 🔐 Security

- Stateless JWT authentication — no server-side sessions
- Token contains: user ID, role, expiry
- Every request validated by `JwtAuthFilter`
- Method-level access control via `@PreAuthorize`
- Ownership validation via `SecurityService.isOwner(userId, auth)`

---

## 📡 Key API Endpoints

| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/auth/login` | Public | Login and receive JWT |
| GET | `/auth/me` | Authenticated | Verify token, return user |
| GET/POST | `/api/admin/users` | Admin | Manage users |
| GET/PUT | `/api/admin/leaves` | Admin | Review leave requests |
| POST | `/api/my/my-staffing-requests` | Manager+ | Submit staffing request |
| GET/POST | `/api/my/my-shift/{id}` | Crew+ | Submit/cancel shift cover |
| POST | `/api/my/my-staff-availability` | Crew+ | Save availability |
| POST | `/api/admin/constraints` | Admin | Define constraint profiles |
| POST | `/api/roster/generate` | Admin | Generate roster |

---

## 🗃️ Database Schema (Key Entities)

```
User
 ├── LeaveRequest       (One-to-Many)
 ├── Availability       (One-to-One per date)
 └── ConstraintProfile  (Many-to-One)

StaffingRequest → JobRole, Location, User (creator)
ShiftAssignment → User, StaffingRequest, date/time
```

Custom queries handle overlap checks and date-based filtering beyond standard JPA CRUD.

---

## 🚀 Deployment

Deployed on **Render** via a Docker container connected to this repository.

**Dockerfile summary:**
- Stage 1: Build JAR using Maven
- Stage 2: Run JAR with a lightweight JRE image

```dockerfile
# Stage 1 – Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2 – Run
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/airport-admin-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Database credentials are injected securely via Render environment variables.
