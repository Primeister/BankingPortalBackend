# Banking Portal Backend

A Spring Boot banking application that manages customers, accounts, transactions, and recurring payments with JWT-based authentication and role-based authorization.

## Overview

This backend provides the core API for a banking portal and supports:

- Customer registration and management
- User authentication and JWT login
- Account creation and lookup
- Deposits, withdrawals, and transfers
- Recurring payment management
- Admin access to customer and account data

## Tech Stack

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- PostgreSQL
- Spring Security
- JWT (jjwt)
- Maven

## Project Structure

```text
src/
├── main/
│   ├── java/
│   │   └── za/co/standardbank/banking_portal/
│   │       ├── account/
│   │       ├── admin/
│   │       ├── customer/
│   │       ├── recurringpayment/
│   │       ├── security/
│   │       ├── transaction/
│   │       ├── user/
│   │       ├── BankingPortalApplication.java
│   │       ├── DataInitializer.java
│   │       └── GlobalExceptionHandler.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/
```

## Requirements

Before running this application, make sure you have:

- Java 21 installed
- Maven installed
- PostgreSQL running locally
- A database named `ppb_banking`

## Database Configuration

The application is configured in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ppb_banking
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

If your local PostgreSQL setup differs, update the values above before starting the app.

## Default Admin User

On application startup, the app automatically creates an admin user if it does not already exist.

- Email: `admin@standardbank.local`
- Password: `admin123`

## Run the Application

From the project root:

```bash
./mvnw spring-boot:run
```

The app runs on:

```text
http://localhost:8080
```

## Security

This project uses JWT authentication with stateless sessions.

### Public endpoints
- `/api/auth/register`
- `/api/auth/login`
- `/api/auth/create-for-customer`

### Admin-only endpoints
- `/api/admin/**`
- `/api/customers/**`

All other endpoints require authentication.

## API Summary

### Authentication

#### Register a user
```http
POST /api/auth/register
Content-Type: application/x-www-form-urlencoded

firstName=John
&lastName=Doe
&email=john@example.com
&password=secret123
```

#### Login
```http
POST /api/auth/login
Content-Type: application/x-www-form-urlencoded

email=admin@standardbank.local
&password=admin123
```

### Customers

```http
GET /api/customers
GET /api/customers/{id}
POST /api/customers
DELETE /api/customers/{id}
```

### Accounts

```http
GET /api/accounts
GET /api/accounts/{id}
POST /api/accounts
DELETE /api/accounts/{id}
```

### Transactions

```http
GET /api/transactions
POST /api/transactions/deposit?accountId=1&amount=100.00
POST /api/transactions/withdraw?accountId=1&amount=50.00
POST /api/transactions/transfer?sourceAccountId=1&destinationAccountNumber=1234567890&amount=25.00
```

### Recurring Payments

```http
GET /api/recurring-payments
POST /api/recurring-payments?accountId=1
DELETE /api/recurring-payments/{id}
```

### Admin

```http
GET /api/admin/customers
GET /api/admin/accounts
```

## Notes

- The backend is designed for local development and demo usage.
- `ddl-auto=update` will automatically create or update database tables.
- Validation and access protection are enforced in service logic and security configuration.

## Useful Commands

```bash
# Run application
./mvnw spring-boot:run

# Build project
./mvnw clean package

# Run tests
./mvnw test
```

## License

This project does not currently declare a license in the Maven configuration.
