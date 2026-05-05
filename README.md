# Prokress

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

Prokress is a project management application designed to help teams manage projects, tasks, user roles, and work progress in one place.

### Current Features

- User registration and authentication with JWT-based login
- Create personal and shared projects
- Organize tasks with task lists within projects
- Real-time task management - add and remove tasks instantly
- Drag and drop functionality for task management
- Real-time task tracking with WebSocket support
- User-friendly interface with responsive design

### Upcoming Features

- Task editing capability
- Task commenting
- Task assignment to specific team members
- User role management and permissions
- Custom background image selection
- Advanced search functionality for tasks
- Calendar and backlog view navigation
- Team member management for project managers

## Backlog

- GitHub Projects backlog: [Git-Happens-HH project 1](https://github.com/orgs/Git-Happens-HH/projects/1)

## Production Deployments

- Backend: [https://project-management-backend-prokress-backend.2.rahtiapp.fi/](https://project-management-backend-prokress-backend.2.rahtiapp.fi/)
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

## REST API Documentation

All REST API endpoints are automatically documented in Swagger UI:

[https://project-management-backend-prokress-backend.2.rahtiapp.fi/swagger-ui/index.html](https://project-management-backend-prokress-backend.2.rahtiapp.fi/swagger-ui/index.html)

The API includes endpoints for managing users, projects, task lists and tasks with full CRUD operations and WebSocket support for real-time updates.

## Deployment & CI/CD

The application uses GitHub Actions for continuous integration and deployment.

### GitHub Actions Workflows

- **[PR Check](https://github.com/Git-Happens-HH/Project-management-backend/actions/workflows/pr-check.yml)** - Builds and tests every pull request with Maven clean verify
- **[Security Scan](https://github.com/Git-Happens-HH/Project-management-backend/actions/workflows/security-scan.yml)** - Scans dependencies for vulnerabilities using Trivy and OWASP Dependency-Check
- **[Deploy Staging](https://github.com/Git-Happens-HH/Project-management-backend/actions/workflows/deploy-staging.yml)** - Automatically deploys to staging environment on main branch
- **[Deploy Production](https://github.com/Git-Happens-HH/Project-management-backend/actions/workflows/deploy-production.yml)** - Manually triggered production deployment with approval gate

### Deployment Infrastructure

- Backend deployed on [Rahti (OpenShift)](https://rahti.csc.fi/) with PostgreSQL database
- Frontend deployed on [Azure Static Web Apps](https://azure.microsoft.com/services/app-service/static/)
- Docker images stored in [GitHub Container Registry (GHCR)](https://github.com/Git-Happens-HH/Project-management-backend/pkgs/container)
- OpenShift manifests in [ops/openshift/](ops/openshift/) - Kubernetes deployment configurations, services, and routes

