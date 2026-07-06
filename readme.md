# Spring Boot CRUD Application

## 📌 Overview

This is a **Spring Boot CRUD (Create, Read, Update, Delete) application** built as part of my learning journey in Spring Boot development.
The project demonstrates the fundamentals of building RESTful APIs using Spring Boot, Spring Data JPA, and an in-memory database.

It is a beginner-level project focused on understanding backend development concepts such as layering, database interaction, and REST API design.

---

## 🚀 Features

* Create new records
* Retrieve all records
* Retrieve a record by ID
* Update existing records
* Delete records
* RESTful API architecture
* In-memory database support (H2)
* H2 Console for database inspection

---

## 🛠️ Tech Stack

* Java
* Spring Boot
* Spring Data JPA
* Hibernate
* H2 Database
* Maven

---

## 📂 Project Purpose

This project is built purely for **learning purposes** to understand:

* How Spring Boot applications are structured
* How REST APIs are created
* How JPA handles database operations
* How CRUD operations work in backend systems
* How to use H2 in-memory database for testing

---

## ⚙️ Setup & Run Instructions

### 1. Clone the repository

```bash
git clone https://github.com/KabileshwaranKabil/springboot-crud-app.git
```

### 2. Open in IDE

Open the project in IntelliJ IDEA or any Java-supported IDE.

### 3. Build the project

```bash
mvn clean install
```

### 4. Run the application

```bash
mvn spring-boot:run
```

---

## 🌐 H2 Database Console

Once the application is running, access the H2 console:

```
http://localhost:1896/h2-console
```

### Default settings (if unchanged):

* JDBC URL: `jdbc:h2:mem:tododb`
* Username: `admin`
* Password: ``

---

## 📈 Future Improvements

* Add validation layer (Spring Validation)
* Add Swagger/OpenAPI documentation
* Connect to MySQL/PostgreSQL
* Add DTO layer for better architecture
* Add exception handling (Global Exception Handler)

---

## 👨‍💻 Author

Built by **Kabileshwaran Kabil** as part of Spring Boot learning practice.