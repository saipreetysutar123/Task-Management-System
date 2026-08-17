````markdown
# 📋 Task Management System

A secure **RESTful Task Management System** built using **Java 21 and Spring Boot**. The application provides user, project, and task management with **JWT authentication, role-based authorization, validation, exception handling, pagination, sorting, filtering, search, Swagger/OpenAPI documentation, and automated testing**.

---

## 🚀 Features

### 👤 User Management
- Create a new user
- Get all users
- Get user by ID
- Update user
- Delete user
- Input validation
- Role management (`USER` / `ADMIN`)
- Password encryption using BCrypt

### 📁 Project Management
- Create a project
- Get all projects
- Get project by ID
- Update a project
- Delete a project

### ✅ Task Management
- Create a task
- Get all tasks
- Get task by ID
- Update a task
- Delete a task
- Pagination
- Sorting
- Filter tasks by status
- Filter tasks by priority
- Filter tasks by project
- Filter tasks by user
- Search tasks using keywords

### 🔐 Authentication & Authorization
- Login using email and password
- JWT-based authentication
- Bearer token authentication
- Role-based authorization
- `ADMIN`-only access to `/admin/**`
- Custom `401 Unauthorized` response
- Custom `403 Forbidden` response

### 🛡️ Validation & Exception Handling
- Jakarta Bean Validation
- Request validation using `@Valid`
- Global exception handling
- Custom `ProjectNotFoundException`
- Validation error responses
- Proper HTTP status codes

### 📖 API Documentation
- Swagger/OpenAPI documentation
- Bearer authentication support in Swagger UI

### 🧪 Automated Testing
The project includes automated tests for controllers, services, JWT, authentication, authorization, and security components.

**Current test result:**

```text
78 Tests
0 Failures
0 Errors
BUILD SUCCESS
````

---

## 🛠️ Tech Stack

* **Java 21**
* **Spring Boot 4.1.0**
* **Spring MVC**
* **Spring Security**
* **Spring Data JPA**
* **Hibernate**
* **MySQL**
* **JWT**
* **Jakarta Bean Validation**
* **Swagger / OpenAPI**
* **JUnit 5**
* **Mockito**
* **MockMvc**
* **Maven**
* **Git & GitHub**
* **Postman**

---

## 🏗️ Architecture

The project follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Security flow:

```text
Client
   ↓
JWT Bearer Token
   ↓
JwtAuthenticationFilter
   ↓
JwtService
   ↓
CustomUserDetailsService
   ↓
SecurityContext
   ↓
Controller
```

---

## 📁 Project Structure

```text
src
├── main
│   ├── java
│   │   └── com.saipreety.taskmanagement
│   │       ├── config
│   │       │   └── SecurityConfig.java
│   │       ├── controller
│   │       │   ├── AdminController.java
│   │       │   ├── AuthController.java
│   │       │   ├── ProjectController.java
│   │       │   ├── TaskController.java
│   │       │   └── UserController.java
│   │       ├── dto
│   │       ├── entity
│   │       ├── exception
│   │       ├── repository
│   │       ├── security
│   │       ├── service
│   │       └── TaskManagementSystemApplication.java
│   │
│   └── resources
│       └── application.properties
│
└── test
    └── java
        └── com.saipreety.taskmanagement
            ├── config
            ├── controller
            ├── security
            └── service
```

---

## 🔐 Authentication

### Login

```http
POST /auth/login
```

Example request:

```json
{
  "email": "user@gmail.com",
  "password": "password123"
}
```

A successful login returns a JWT token.

Use the token in protected requests:

```http
Authorization: Bearer <JWT_TOKEN>
```

---

## 📌 API Endpoints

### Authentication

| Method | Endpoint      | Description            | Access |
| ------ | ------------- | ---------------------- | ------ |
| POST   | `/auth/login` | Login and generate JWT | Public |

---

### User APIs

| Method | Endpoint            | Description    |
| ------ | ------------------- | -------------- |
| POST   | `/user/create`      | Create a user  |
| GET    | `/user/fetchAll`    | Get all users  |
| GET    | `/user/fetch/{id}`  | Get user by ID |
| PUT    | `/user/update/{id}` | Update user    |
| DELETE | `/user/delete/{id}` | Delete user    |

Protected endpoints require authentication.

---

### Project APIs

| Method | Endpoint               | Description       |
| ------ | ---------------------- | ----------------- |
| POST   | `/project/create`      | Create a project  |
| GET    | `/project/fetchAll`    | Get all projects  |
| GET    | `/project/fetch/{id}`  | Get project by ID |
| PUT    | `/project/update/{id}` | Update a project  |
| DELETE | `/project/delete/{id}` | Delete a project  |

---

### Task APIs

| Method | Endpoint                          | Description             |
| ------ | --------------------------------- | ----------------------- |
| POST   | `/task/create`                    | Create a task           |
| GET    | `/task/fetchAll`                  | Get paginated tasks     |
| GET    | `/task/fetch/{id}`                | Get task by ID          |
| PUT    | `/task/update/{id}`               | Update a task           |
| DELETE | `/task/delete/{id}`               | Delete a task           |
| GET    | `/task/fetch/status/{status}`     | Filter by status        |
| GET    | `/task/fetch/priority/{priority}` | Filter by priority      |
| GET    | `/task/fetch/project/{projectId}` | Filter by project       |
| GET    | `/task/fetch/user/{userId}`       | Filter by user          |
| GET    | `/task/search`                    | Search tasks by keyword |

### Task Pagination and Sorting

Example:

```http
GET /task/fetchAll?page=0&size=5&sortBy=id&direction=asc
```

Parameters:

* `page` — page number
* `size` — number of records per page
* `sortBy` — field used for sorting
* `direction` — `asc` or `desc`

---

### Admin API

| Method | Endpoint           | Description     | Access       |
| ------ | ------------------ | --------------- | ------------ |
| GET    | `/admin/dashboard` | Admin dashboard | `ADMIN` only |

Authorization behavior:

```text
No authentication  → 401 Unauthorized
USER role          → 403 Forbidden
ADMIN role         → 200 OK
```

---

## 🗄️ Database Configuration

The application uses **MySQL** with Spring Data JPA and Hibernate.

Sensitive configuration is loaded through environment variables.

```properties
spring.application.name=TaskManagementSystem

spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

jwt.secret=${JWT_SECRET}
```

Configure the following environment variables before running the application:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
```

Example database URL:

```text
jdbc:mysql://localhost:3306/task_management
```

> Do not commit database passwords or JWT secrets to GitHub.

---

## 📖 Swagger / OpenAPI

Once the application is running, Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

Use the **Authorize** button to provide your JWT:

```text
Bearer <JWT_TOKEN>
```

---

## ▶️ Running the Project

### Clone the repository

```bash
git clone https://github.com/saipreetysutar123/Task-Management-System.git
```

### Navigate to the project

```bash
cd Task-Management-System
```

### Run the application

Using Maven Wrapper:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

Or run:

```text
TaskManagementSystemApplication.java
```

directly from IntelliJ IDEA.

---

## 🧪 Testing

The project contains automated tests using **JUnit 5, Mockito, and MockMvc**.

Test coverage includes:

* User controller
* Project controller
* Task controller
* Authentication controller
* User service
* Project service
* Task service
* JWT service
* JWT authentication filter
* Custom user details service
* Authentication entry point
* Access denied handler
* Security authorization flow

### Test Result

```text
Tests run: 78
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

Run all tests with:

```bash
mvn clean test
```

---

## 🔒 Security

The application uses:

* JWT authentication
* BCrypt password hashing
* Bearer token authorization
* Role-based access control
* Custom authentication entry point
* Custom access denied handler

Protected requests require:

```http
Authorization: Bearer <JWT_TOKEN>
```

---

## 🔮 Future Enhancements

- Frontend application using React
- Refresh token support
- Docker containerization
- CI/CD pipeline
- Task notifications and reminders
- Cloud deployment

---

## 👩‍💻 Author

**Saipreety Sutar**

GitHub:
https://github.com/saipreetysutar123

---

## ⭐ Support

If you found this project useful, consider giving the repository a ⭐ on GitHub.

```
```
