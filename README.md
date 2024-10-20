Here's a README file for your Rule Engine with AST (Abstract Syntax Tree) project:

---

# Rule Engine with AST

## Overview

The **Rule Engine with Abstract Syntax Tree (AST)** is a Java-based application that allows users to define, store, and evaluate business rules in the form of logical expressions. These rules are represented as ASTs, which are created by parsing rule strings. The engine supports the combination of multiple rules, and it can evaluate them against user-provided attributes.

This project uses **Spring Boot** for REST APIs, **JPA** for database operations, and **AST** for parsing and evaluating the rules.

## Features

- **Create Rules**: Users can create rules using logical expressions (`AND`, `OR`, `>`, `<`, `=`).
- **Retrieve Rules**: Retrieve all stored rules from the database.
- **Combine Rules**: Multiple rules can be combined into a single AST.
- **Evaluate Rules**: Evaluate a rule against user attributes, determining if the rule conditions are satisfied.
- **AST Representation**: Rules are internally represented using AST (Abstract Syntax Tree) nodes.

## Technologies

- **Spring Boot**: To handle the REST API and dependency injection.
- **JPA (Java Persistence API)**: For managing rules storage in a relational database.
- **AST (Abstract Syntax Tree)**: To represent and process rules as tree structures.
- **Lombok**: To reduce boilerplate code.

## Prerequisites

- Java 17+
- Maven
- Database MySQL

## Project Structure

├── src/main/java
│   ├── com.Rule_Engine_with_AST
│   │   ├── Controller       # REST controllers handling API requests
│   │   ├── Models           # Entity classes (ASTNode, Rule)
│   │   ├── Repository       # Rule repository interface for JPA
│   │   └── Service          # Business logic for rules management and evaluation
└── src/main/resources
    └── application.yml      # Configuration file for database and other properties
```

## API Endpoints

### 1. Create a Rule

**POST** `/rules/create`

- Creates a new rule by saving the rule string to the database.
- **Request Body**: 
  ```json
  {
    "name": "Department Rule",
    "ruleString": "department = 'Sales'"
}
  ```
- **Response**: Returns the created rule with an ID.

### 2. Get All Rules

**GET** `/rules`

- Retrieves all stored rules from the database.
- **Response**: List of rules.

### 3. Combine Multiple Rules into a Single AST

**POST** `/rules/combine`

- Combines multiple rules into a single AST based on a main operator (`AND` or `OR`).
- **Request Body**: 
  ```json
  [
    "age > 30",
    "department = 'Sales'",
    "salary > 50000"
  ]
  ```
- **Response**: The combined AST structure.

### 4. Evaluate a Rule

**POST** `/rules/evaluate`

- Evaluates a rule against user attributes.
- **Request Body**:
  ```json
  TRUE CASE :
  {
    "ruleString": "age > 30 AND department = 'Sales'",
    "attributes": {
        "age": 35,
        "department": "Sales",
        "salary": 60000
    }
  }

```json
FALSE CASE :
{
    "ruleString": "age > 30 AND department = 'Marketing'",
    "userAttributes": {
        "age": 28,
        "department": "Sales"
    }
}


  ```
- **Response**: Returns `true` or `false` depending on whether the rule conditions are satisfied.

## Setup and Installation

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/rule-engine-ast.git
cd rule-engine-ast
```

### 2. Build the Project

Ensure you have **Maven** installed, then run the following command to build the project:

```bash
mvn clean install
```

### 3. Configure the Database

By default, the project is set up to use an in-memory **H2** database. If you wish to use a different database, update the `application.yml` file in the `src/main/resources` directory.

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

### 4. Run the Application

Run the Spring Boot application using Maven:

```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`.
