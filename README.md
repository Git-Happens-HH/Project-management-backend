# Prokress - Project Management App

[![Java](https://img.shields.io/badge/Java-21-blue)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.14-green)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-336791)](https://www.postgresql.org)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED)](https://www.docker.com)
[![License](https://img.shields.io/badge/License-MIT-yellow)](https://github.com/Git-Happens-HH/Project-management-backend/blob/main/LICENSE)
[![Build](https://img.shields.io/badge/Build-Passing-brightgreen)](https://github.com)
[![Build & Test](https://github.com/Git-Happens-HH/Project-management-backend/workflows/PR%20Check/badge.svg)](https://github.com/Git-Happens-HH/Project-management-backend/actions)
[![Deploy Staging](https://github.com/Git-Happens-HH/Project-management-backend/workflows/Deploy%20Staging/badge.svg)](https://github.com/Git-Happens-HH/Project-management-backend/actions)
[![Deploy Production](https://github.com/Git-Happens-HH/Project-management-backend/workflows/Deploy%20Production/badge.svg)](https://github.com/Git-Happens-HH/Project-management-backend/actions)

## Project Name and Description

Prokress is a project management application designed to help teams manage projects, tasks, user roles, and work progress in one place. The application supports project creation, task lists, task creation, drag and drop task handling, commenting, user login, and real-time task tracking.

## Backlog

- GitHub Projects backlog: [Git-Happens-HH projects](https://github.com/orgs/Git-Happens-HH/projects)

## Production Deployments

- Backend: [https://project-management-backend-prokress-backend.2.rahtiapp.fi/](https://project-management-backend-prokress-backend.2.rahtiapp.fi/)
- Backend Swagger UI: [https://project-management-backend-prokress-backend.2.rahtiapp.fi/swagger-ui/index.html](https://project-management-backend-prokress-backend.2.rahtiapp.fi/swagger-ui/index.html)
- Frontend: [https://yellow-mud-05a9abf03.1.azurestaticapps.net](https://yellow-mud-05a9abf03.1.azurestaticapps.net)

## License

This project is licensed under the MIT License - see the [LICENSE](https://github.com/Git-Happens-HH/Project-management-backend/blob/main/LICENSE) file for details.

## Technologies

- Java 21
- Spring Boot 3.5.14
- Spring Web
- Spring Data JPA
- Spring Data REST
- Spring Security
- JWT (JJWT 0.11.5)
- Spring WebSocket
- Spring Validation
- Springdoc OpenAPI / Swagger UI
- PostgreSQL 15
- H2 Database
- Maven
- Testcontainers 1.20.4
- JUnit 5
- Mockito

## Technical Instructions

The application is located in the Maven module [project-management-app](project-management-app).

Run locally on Windows:

```powershell
cd project-management-app
./mvnw.cmd spring-boot:run
```

Run tests from the command line:

```powershell
cd project-management-app
./mvnw.cmd test
```

Run the full verification build:

```powershell
cd project-management-app
./mvnw.cmd clean verify
```

## Data Model

The core domain consists of the following entities:

```mermaid
erDiagram
  APP_USER ||--o{ TASK : creates
  APP_USER ||--o{ TASK : assigned_to
  APP_USER ||--o{ COMMENT : writes
  APP_USER ||--o{ USER_PROJECT : linked_via
  PROJECT ||--o{ USER_PROJECT : linked_via
  PROJECT ||--o{ TASK_LIST : contains
  TASK_LIST ||--o{ TASK : contains
  TASK ||--o{ COMMENT : has

  APP_USER {
    long app_user_id
    string username
    string first_name
    string last_name
    string email
  }

  PROJECT {
    long project_id
    string title
    string description
    datetime created_at
    boolean is_shared
  }

  TASK_LIST {
    long task_list_id
    string title
    datetime created_at
  }

  TASK {
    long task_id
    string title
    string description
    datetime deadline
  }

  COMMENT {
    long comment_id
    string content
    datetime created_at
  }

  USER_PROJECT {
    long app_user_id
    long project_id
    boolean shared_to_this_user
  }
```

## Attachments

- CI/CD documentation: [docs/ci-cd-document.md](docs/ci-cd-document.md)
- Testcontainers documentation: [docs/testcontainers.md](docs/testcontainers.md)
