# 📋 Task Management System

A RESTful Task Management System built using **Spring Boot** that allows users to manage tasks efficiently through CRUD operations. The project follows a clean layered architecture and implements validation, exception handling, and database integration using MySQL.

---

## 🚀 Features

- ✅ Create a new task
- ✅ View all tasks
- ✅ View a task by ID
- ✅ Update an existing task
- ✅ Delete a task
- ✅ Input validation using Bean Validation
- ✅ Global exception handling
- ✅ MySQL database integration
- ✅ RESTful API design

---

## 🛠️ Tech Stack

- Java 21
- Spring Boot
- Spring Data JPA (Hibernate)
- MySQL
- Maven
- Postman
- Git & GitHub

---

## 📁 Project Structure

```
src
├── main
│   ├── java
│   │   └── com.saipreety.taskmanagement
│   │       ├── controller
│   │       ├── entity
│   │       ├── repository
│   │       ├── service
│   │       ├── exception
│   │       └── TaskManagementSystemApplication.java
│   └── resources
│       └── application.properties
```

---

## 📌 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/users` | Create a new user |
| GET | `/users` | Get all users |
| GET | `/users/{id}` | Get user by ID |
| PUT | `/users/{id}` | Update user |
| DELETE | `/users/{id}` | Delete user |

---

## 🗄️ Database

- MySQL
- Spring Data JPA
- Hibernate ORM

Configure your database in:

```
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/task_management
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
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

```bash
./mvnw spring-boot:run
```

or run the `TaskManagementSystemApplication.java` class from IntelliJ IDEA.

---

## 🧪 Testing

The APIs were tested using **Postman**.

---

## 📈 Future Enhancements

- DTO (Data Transfer Objects)
- Password Encryption using BCrypt
- User Authentication
- JWT Security
- Swagger/OpenAPI Documentation
- Pagination & Sorting
- Search & Filter
- Unit Testing
- Docker Deployment

---

## 👩‍💻 Author

**Saipreety Sutar**

- GitHub: https://github.com/saipreetysutar123

---

## ⭐ If you like this project

If you found this project useful, consider giving it a ⭐ on GitHub!
