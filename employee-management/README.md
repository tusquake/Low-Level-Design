# Employee Management CRUD System

A production-ready Employee Management backend built using Spring Boot 3, Java 21, Spring Security with JWT, PostgreSQL, Spring Data JPA, MapStruct, Lombok, and validation.

---

## 1. Project Directory Structure

```text
employee-management/
├── pom.xml
├── Dockerfile
├── docker-compose.yml
├── README.md
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── management/
    │   │           └── employee/
    │   │               ├── EmployeeManagementApplication.java
    │   │               ├── config/
    │   │               │   ├── JpaAuditingConfig.java
    │   │               │   ├── JwtAuthenticationFilter.java
    │   │               │   ├── JwtService.java
    │   │               │   ├── OpenApiConfig.java
    │   │               │   └── SecurityConfig.java
    │   │               ├── controller/
    │   │               │   ├── AuthController.java
    │   │               │   └── EmployeeController.java
    │   │               ├── dto/
    │   │               │   ├── ApiResponse.java
    │   │               │   ├── AuthRequest.java
    │   │               │   ├── AuthResponse.java
    │   │               │   ├── EmployeeRequestDTO.java
    │   │               │   └── EmployeeResponseDTO.java
    │   │               ├── entity/
    │   │               │   └── Employee.java
    │   │               ├── exception/
    │   │               │   ├── DuplicateEmailException.java
    │   │               │   ├── GlobalExceptionHandler.java
    │   │               │   └── ResourceNotFoundException.java
    │   │               ├── mapper/
    │   │               │   └── EmployeeMapper.java
    │   │               ├── repository/
    │   │               │   └── EmployeeRepository.java
    │   │               └── service/
    │   │                   ├── EmployeeService.java
    │   │                   └── impl/
    │   │                       └── EmployeeServiceImpl.java
    │   └── resources/
    │       └── application.yml
    └── test/
        └── java/
            └── com/
                └── management/
                    └── employee/
                        ├── controller/
                        │   └── EmployeeControllerTest.java
                        └── service/
                            └── EmployeeServiceTest.java
```

---

## 2. Database Schema (PostgreSQL)

If you configure the database manually, the equivalent DDL schema for the `employees` table is:

```sql
CREATE TABLE employees (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    department VARCHAR(255) NOT NULL,
    salary NUMERIC(19, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    version BIGINT NOT NULL
);
```

---

## 3. Class-by-Class Architecture Walkthrough

### **Configuration layer (`config`)**
- `JpaAuditingConfig`: Enables JPA Auditing via `@EnableJpaAuditing` to automate timestamps on insert/update.
- `JwtService`: Handles creating JWTs, reading claims (including user roles), and verifying token expiration.
- `JwtAuthenticationFilter`: Custom filter extending `OncePerRequestFilter`. Parses bearer tokens and configures spring authentication contexts.
- `SecurityConfig`: Declares rules (stateless sessions, API routes authorization, password encoder, and standard mock user credentials). Preconfigured logins:
  - **Admin**: `admin` / `admin123` (Full CRUD authorization)
  - **User**: `user` / `user123` (Read-only access)
- `OpenApiConfig`: Customizes OpenAPI schemas and injects a global Bearer security option into Swagger UI.

### **DTO layer (`dto`)**
- `EmployeeRequestDTO`: Implemented as a **Java 21 Record**. Enforces field validation rules (`@NotBlank`, `@Email`, `@Positive`).
- `EmployeeResponseDTO`: Implemented as a **Java 21 Record**. Outputs employee details, creation/updation timestamps, and version tags.
- `ApiResponse`: Standardizes response layouts (with `success`, `message`, and nested generic payload `data`).
- `AuthRequest` & `AuthResponse`: Handles standard authentication data transfer.

### **Entity layer (`entity`)**
- `Employee`: Implements standard JPA properties, audit markers (`@CreatedDate`, `@LastModifiedDate`), and a `@Version` tag for optimistic locking.

### **Repository layer (`repository`)**
- `EmployeeRepository`: Manages database transactions and query filters (like checking for emails).

### **Mapper layer (`mapper`)**
- `EmployeeMapper`: Built using MapStruct, translating objects efficiently without boilerplate boilerplate.

### **Exception layer (`exception`)**
- `ResourceNotFoundException`: Thrown when querying nonexistent employee records.
- `DuplicateEmailException`: Thrown when email conflicts occur.
- `GlobalExceptionHandler`: Intercepts and transforms service/validation/auth errors into clean JSON response structures.

---

## 4. API Request/Response Examples

### **A. Authentication (Login)**

* **Request**:
  `POST /api/v1/auth/login`
  ```json
  {
    "username": "admin",
    "password": "admin123"
  }
  ```
* **Response (200 OK)**:
  ```json
  {
    "success": true,
    "message": "Authentication successful",
    "data": {
      "token": "eyJhbGciOiJIUzI1NiJ9.ey...",
      "username": "admin",
      "roles": [
        "ROLE_ADMIN"
      ]
    }
  }
  ```

---

### **B. Create Employee (Admin Role Required)**

* **Request**:
  `POST /api/v1/employees`
  `Header: Authorization: Bearer <ADMIN_JWT_TOKEN>`
  ```json
  {
    "name": "Sarah Miller",
    "email": "sarah.miller@example.com",
    "department": "Finance",
    "salary": 85000.00
  }
  ```
* **Response (201 Created)**:
  ```json
  {
    "success": true,
    "message": "Employee created successfully",
    "data": {
      "id": 1,
      "name": "Sarah Miller",
      "email": "sarah.miller@example.com",
      "department": "Finance",
      "salary": 85000.00,
      "createdAt": "2026-06-29T06:40:00",
      "updatedAt": "2026-06-29T06:40:00",
      "version": 0
    }
  }
  ```

---

### **C. Get Employee by ID (Admin or User Role)**

* **Request**:
  `GET /api/v1/employees/1`
  `Header: Authorization: Bearer <USER_JWT_TOKEN>`
* **Response (200 OK)**:
  ```json
  {
    "success": true,
    "message": "Employee retrieved successfully",
    "data": {
      "id": 1,
      "name": "Sarah Miller",
      "email": "sarah.miller@example.com",
      "department": "Finance",
      "salary": 85000.00,
      "createdAt": "2026-06-29T06:40:00",
      "updatedAt": "2026-06-29T06:40:00",
      "version": 0
    }
  }
  ```

---

### **D. Get All Employees with Pagination (Admin or User Role)**

* **Request**:
  `GET /api/v1/employees?page=0&size=10&sort=name`
  `Header: Authorization: Bearer <USER_JWT_TOKEN>`
* **Response (200 OK)**:
  ```json
  {
    "success": true,
    "message": "Employees retrieved successfully",
    "data": {
      "content": [
        {
          "id": 1,
          "name": "Sarah Miller",
          "email": "sarah.miller@example.com",
          "department": "Finance",
          "salary": 85000.00,
          "createdAt": "2026-06-29T06:40:00",
          "updatedAt": "2026-06-29T06:40:00",
          "version": 0
        }
      ],
      "pageable": {
        "pageNumber": 0,
        "pageSize": 10,
        "sort": {
          "empty": false,
          "sorted": true,
          "unsorted": false
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
      },
      "last": true,
      "totalElements": 1,
      "totalPages": 1,
      "first": true,
      "size": 10,
      "number": 0,
      "numberOfElements": 1,
      "empty": false
    }
  }
  ```

---

### **E. Update Employee (Admin Role Required)**

* **Request**:
  `PUT /api/v1/employees/1`
  `Header: Authorization: Bearer <ADMIN_JWT_TOKEN>`
  ```json
  {
    "name": "Sarah Miller",
    "email": "sarah.miller@example.com",
    "department": "Finance & Ops",
    "salary": 92000.00
  }
  ```
* **Response (200 OK)**:
  ```json
  {
    "success": true,
    "message": "Employee updated successfully",
    "data": {
      "id": 1,
      "name": "Sarah Miller",
      "email": "sarah.miller@example.com",
      "department": "Finance & Ops",
      "salary": 92000.00,
      "createdAt": "2026-06-29T06:40:00",
      "updatedAt": "2026-06-29T06:45:00",
      "version": 1
    }
  }
  ```

---

### **F. Delete Employee (Admin Role Required)**

* **Request**:
  `DELETE /api/v1/employees/1`
  `Header: Authorization: Bearer <ADMIN_JWT_TOKEN>`
* **Response (200 OK)**:
  ```json
  {
    "success": true,
    "message": "Employee deleted successfully",
    "data": null
  }
  ```

---

### **G. Validation Error Example (400 Bad Request)**

* **Request**:
  `POST /api/v1/employees`
  `Header: Authorization: Bearer <ADMIN_JWT_TOKEN>`
  ```json
  {
    "name": "",
    "email": "invalid-email",
    "department": "Finance",
    "salary": -500.00
  }
  ```
* **Response (400 Bad Request)**:
  ```json
  {
    "success": false,
    "message": "Validation failed: salary: Salary must be positive, name: Name cannot be blank, email: Email must be a valid email address",
    "data": null
  }
  ```

---

## 5. Postman Collection

Copy the JSON contents below, save them as `employee-management.postman_collection.json`, and import directly into Postman.

```json
{
  "info": {
    "_postman_id": "f5169a91-db67-4c46-bfca-451e704043b4",
    "name": "Employee Management API",
    "description": "API requests to test login and CRUD employee operations with ADMIN / USER privileges.",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Authentication",
      "item": [
        {
          "name": "Login as Admin",
          "event": [
            {
              "listen": "test",
              "script": {
                "exec": [
                  "var jsonData = pm.response.json();",
                  "if (jsonData.success) {",
                  "    pm.environment.set(\"adminToken\", jsonData.data.token);",
                  "}"
                ],
                "type": "text/javascript"
              }
            }
          ],
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n    \"username\": \"admin\",\n    \"password\": \"admin123\"\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/api/v1/auth/login",
              "host": [
                "{{baseUrl}}"
              ],
              "path": [
                "api",
                "v1",
                "auth",
                "login"
              ]
            }
          },
          "response": []
        },
        {
          "name": "Login as User",
          "event": [
            {
              "listen": "test",
              "script": {
                "exec": [
                  "var jsonData = pm.response.json();",
                  "if (jsonData.success) {",
                  "    pm.environment.set(\"userToken\", jsonData.data.token);",
                  "}"
                ],
                "type": "text/javascript"
              }
            }
          ],
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n    \"username\": \"user\",\n    \"password\": \"user123\"\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/api/v1/auth/login",
              "host": [
                "{{baseUrl}}"
              ],
              "path": [
                "api",
                "v1",
                "auth",
                "login"
              ]
            }
          },
          "response": []
        }
      ]
    },
    {
      "name": "Employees",
      "item": [
        {
          "name": "Create Employee (Admin)",
          "request": {
            "auth": {
              "type": "bearer",
              "bearer": [
                {
                  "key": "token",
                  "value": "{{adminToken}}",
                  "type": "string"
                }
              ]
            },
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n    \"name\": \"Alice Cooper\",\n    \"email\": \"alice.cooper@example.com\",\n    \"department\": \"Marketing\",\n    \"salary\": 72000.00\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/api/v1/employees",
              "host": [
                "{{baseUrl}}"
              ],
              "path": [
                "api",
                "v1",
                "employees"
              ]
            }
          },
          "response": []
        },
        {
          "name": "Get Employee by ID (User or Admin)",
          "request": {
            "auth": {
              "type": "bearer",
              "bearer": [
                {
                  "key": "token",
                  "value": "{{userToken}}",
                  "type": "string"
                }
              ]
            },
            "method": "GET",
            "header": [],
            "url": {
              "raw": "{{baseUrl}}/api/v1/employees/1",
              "host": [
                "{{baseUrl}}"
              ],
              "path": [
                "api",
                "v1",
                "employees",
                "1"
              ]
            }
          },
          "response": []
        },
        {
          "name": "Get All Employees (User or Admin)",
          "request": {
            "auth": {
              "type": "bearer",
              "bearer": [
                {
                  "key": "token",
                  "value": "{{userToken}}",
                  "type": "string"
                }
              ]
            },
            "method": "GET",
            "header": [],
            "url": {
              "raw": "{{baseUrl}}/api/v1/employees?page=0&size=10&sort=name",
              "host": [
                "{{baseUrl}}"
              ],
              "path": [
                "api",
                "v1",
                "employees"
              ],
              "query": [
                {
                  "key": "page",
                  "value": "0"
                },
                {
                  "key": "size",
                  "value": "10"
                },
                {
                  "key": "sort",
                  "value": "name"
                }
              ]
            }
          },
          "response": []
        },
        {
          "name": "Update Employee (Admin)",
          "request": {
            "auth": {
              "type": "bearer",
              "bearer": [
                {
                  "key": "token",
                  "value": "{{adminToken}}",
                  "type": "string"
                }
              ]
            },
            "method": "PUT",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n    \"name\": \"Alice Cooper\",\n    \"email\": \"alice.cooper@example.com\",\n    \"department\": \"Marketing & Sales\",\n    \"salary\": 79000.00\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/api/v1/employees/1",
              "host": [
                "{{baseUrl}}"
              ],
              "path": [
                "api",
                "v1",
                "employees",
                "1"
              ]
            }
          },
          "response": []
        },
        {
          "name": "Delete Employee (Admin)",
          "request": {
            "auth": {
              "type": "bearer",
              "bearer": [
                {
                  "key": "token",
                  "value": "{{adminToken}}",
                  "type": "string"
                }
              ]
            },
            "method": "DELETE",
            "header": [],
            "url": {
              "raw": "{{baseUrl}}/api/v1/employees/1",
              "host": [
                "{{baseUrl}}"
              ],
              "path": [
                "api",
                "v1",
                "employees",
                "1"
              ]
            }
          },
          "response": []
        }
      ]
    }
  ],
  "event": [
    {
      "listen": "prerequest",
      "script": {
        "type": "text/javascript",
        "exec": [
          ""
        ]
      }
    },
    {
      "listen": "test",
      "script": {
        "type": "text/javascript",
        "exec": [
          ""
        ]
      }
    }
  ],
  "variable": [
    {
      "key": "baseUrl",
      "value": "http://localhost:8080",
      "type": "string"
    },
    {
      "key": "adminToken",
      "value": "",
      "type": "string"
    },
    {
      "key": "userToken",
      "value": "",
      "type": "string"
    }
  ]
}
```

---

## 6. How to Build, Run, and Test

### **Build local jar**
Ensure you have Maven and JDK 21 installed on your system. Run:
```bash
mvn clean package
```

### **Run JUnit/Integration Tests**
To execute the tests (which use an in-memory H2 database simulation for database queries):
```bash
mvn test
```

### **Run with Docker Compose (Recommmended)**
Make sure you have Docker and Docker Compose installed, then execute:
```bash
docker-compose up --build -d
```
This command compiles the Spring Boot project inside a container, sets up a PostgreSQL 16 database, links them together, and runs the application on port `8080`.

---

## 7. Interactive API Documentation
Once running, you can explore the Swagger OpenAPI UI at:
- **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**
- OpenAPI raw docs: **[http://localhost:8080/api-docs](http://localhost:8080/api-docs)**
