# EasyRailJourney
A Spring Boot-based railway reservation backend with train &amp; schedule management, user authentication, role-based authorization, seat booking, ticketing, fare rules, and waitlist management.


EasyRailJourney is a backend railway reservation system built using Java and Spring Boot. It provides APIs for railway management, user authentication and authorization, train scheduling, seat management, booking, ticketing, fare rules, refund rule and passenger waitlist and cancellation management.

## Technologies Used

* Java
* Spring Boot
* Spring Security
* JWT
* Spring Data JPA
* Hibernate
* MySQL
* Maven
* REST APIs

## Main Features

* User and passenger management
* Role-based access control
* JWT-based authentication
* Train and station management
* Schedule management
* Coach and seat management
* Train class management
* Fare rule management
* Ticket booking
* Waitlist management
* Soft delete support
* DTO-based REST APIs

## Project Architecture

The project follows a layered backend architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

The project is currently implemented as a modular monolith with domain-oriented packages, keeping the structure suitable for future migration toward microservices.

## Getting Started

### Prerequisites

* Java
* Maven
* MySQL

### Configuration

Create your local configuration with the required database credentials and secret values.

Do not commit passwords, API keys, JWT secrets, or other sensitive information to GitHub.

### Run the Application

```bash
mvn spring-boot:run
```

The application will start as a Spring Boot backend service.

## API

The application exposes REST APIs for authentication, users, roles, trains, schedules, coaches, seats, bookings, tickets, fares, and waitlist operations.

API documentation can be added using Swagger/OpenAPI.

## Future Improvements

* Microservice migration
* Improved observability and monitoring
* Automated testing
* Production deployment
* Caching and performance optimization
