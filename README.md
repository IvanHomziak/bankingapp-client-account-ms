# Client Manager Service

The **Client Manager Service** is a Spring Boot-based application designed for managing client and account data efficiently. It supports CRUD operations for clients and accounts, implements Kafka integration for handling transaction events, and uses MySQL for database persistence.

---

## Features

- **Client Management**: Add, update, retrieve, and delete client information.
- **Account Management**: Manage checking accounts with support for creation, update, deletion, and retrieval of account details.
- **Kafka Integration**: Consume and produce messages for transaction processing.
- **Database Integration**: Persistent storage using MySQL with JPA and Hibernate.
- **Exception Handling**: Centralized handling of application-specific exceptions.

---

## Technologies Used

- **Java**: Version 17.
- **Spring Boot**: REST APIs, validation, and dependency injection.
- **Kafka**: Message streaming for transaction processing.
- **MySQL**: Relational database management.
- **Hibernate/JPA**: ORM for database interactions.
- **Docker**: Containerization for application and database.

---

## Prerequisites

1. **Java**: Install JDK 17 or higher.
2. **Maven**: For building the application.
3. **Docker**: For running the application in a containerized environment.
4. **Kafka**: Kafka setup with appropriate configurations.

---

## Setup and Installation

### 1. Clone the Repository
```bash
git clone https://github.com/IvanHomziak/client-manager-service.git
cd client-manager-service
```

### 2. Configure Environment Variables
Set up the required environment variables in an `.env` file or in the system:

```plaintext
DB_HOST=mysqldb-cms-c
DB_NAME=clients_db
DB_USER=root
DB_PASS=admin1234
```

### 3. Build the Application
Build the project using Maven:
```bash
mvn clean install
```

### 4. Run the Application
Run the application locally:
```bash
java -jar target/client-manager-service.jar
```

---

## Running with Docker

### 1. Start MySQL with Docker
Start the MySQL container using the provided `docker-compose.yml` file:
```bash
docker-compose up -d mysqldb
```

### 2. Build and Run the Service
Uncomment the service in `docker-compose.yml` and then:
```bash
docker-compose up --build
```

---

## REST API Endpoints

### Client Endpoints
| Method | Endpoint               | Description                   |
|--------|------------------------|-------------------------------|
| GET    | `/api/clients`         | Get all clients               |
| GET    | `/api/clients/{uuid}`  | Get client by UUID            |
| POST   | `/api/clients`         | Add a new client              |
| PATCH  | `/api/clients/update`  | Update client details         |
| DELETE | `/api/clients/{uuid}`  | Delete client by UUID         |

### Account Endpoints
| Method | Endpoint               | Description                   |
|--------|------------------------|-------------------------------|
| GET    | `/api/account`         | Get all accounts              |
| GET    | `/api/account/{uuid}`  | Get account by UUID           |
| POST   | `/api/account`         | Create a new account          |
| PATCH  | `/api/account`         | Update account details        |
| DELETE | `/api/account/{uuid}`  | Delete account by UUID        |

---

## Kafka Integration

### Topics Used
- **`transfer-transactions-topic`**: Consumed by the service to process transaction events.
- **`transaction-results-topic`**: Produced by the service to communicate transaction results.

---

## Database Schema

### **Clients Table**
| Column Name      | Type        | Description          |
|------------------|-------------|----------------------|
| `client_id`      | BIGINT      | Primary key          |
| `first_name`     | VARCHAR     | Client's first name  |
| `last_name`      | VARCHAR     | Client's last name   |
| `email`          | VARCHAR     | Email address        |
| `phone_number`   | VARCHAR     | Phone number         |
| `uuid`           | VARCHAR     | Unique identifier    |

### **Accounts Table**
| Column Name      | Type        | Description          |
|------------------|-------------|----------------------|
| `account_id`     | BIGINT      | Primary key          |
| `account_number` | VARCHAR     | Unique account number|
| `balance`        | DOUBLE      | Account balance      |
| `uuid`           | VARCHAR     | Unique identifier    |

---

## Exception Handling

The application has centralized exception handling for:
- ClientNotFoundException
- AccountNotFoundException
- ClientAlreadyExistException
- NonSufficientFundsException

These are handled by a global exception handler to provide user-friendly error messages.

---

## Contributions

Feel free to fork the repository and create a pull request to contribute.

---

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

---

