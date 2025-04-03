# Documentation: `Postgres.java`

## Overview
The `Postgres` class provides functionality for interacting with a PostgreSQL database. It includes methods for establishing a database connection, setting up the database schema, inserting seed data, and hashing passwords using the MD5 algorithm. This class is designed to initialize and manage a database for a user and comment system.

---

## Features
### 1. **Database Connection**
The `connection()` method establishes a connection to a PostgreSQL database using credentials and connection details provided via environment variables:
- `PGHOST`: Hostname of the PostgreSQL server.
- `PGDATABASE`: Name of the database.
- `PGUSER`: Username for authentication.
- `PGPASSWORD`: Password for authentication.

### 2. **Database Setup**
The `setup()` method:
- Creates two tables (`users` and `comments`) if they do not already exist.
- Clears any existing data in these tables.
- Inserts seed data into the tables for testing purposes.

### 3. **Password Hashing**
The `md5(String input)` method generates an MD5 hash for a given input string. This is used to securely store user passwords in the database.

### 4. **Data Insertion**
The class provides two private methods for inserting data into the database:
- `insertUser(String username, String password)`: Inserts a new user into the `users` table with a hashed password.
- `insertComment(String username, String body)`: Inserts a new comment into the `comments` table.

---

## Database Schema
The `setup()` method defines the following schema:

### `users` Table
| Column Name   | Data Type      | Constraints                          |
|---------------|----------------|---------------------------------------|
| `user_id`     | `VARCHAR(36)`  | Primary Key                          |
| `username`    | `VARCHAR(50)`  | Unique, Not Null                     |
| `password`    | `VARCHAR(50)`  | Not Null                             |
| `created_on`  | `TIMESTAMP`    | Not Null                             |
| `last_login`  | `TIMESTAMP`    | Optional                             |

### `comments` Table
| Column Name   | Data Type      | Constraints                          |
|---------------|----------------|---------------------------------------|
| `id`          | `VARCHAR(36)`  | Primary Key                          |
| `username`    | `VARCHAR(36)`  | Foreign Key (linked to `users.username`) |
| `body`        | `VARCHAR(500)` | Not Null                             |
| `created_on`  | `TIMESTAMP`    | Not Null                             |

---

## Seed Data
The `setup()` method inserts the following seed data:

### Users
| Username | Password              |
|----------|-----------------------|
| `admin`  | `!!SuperSecretAdmin!!`|
| `alice`  | `AlicePassword!`      |
| `bob`    | `BobPassword!`        |
| `eve`    | `$EVELknev^l`         |
| `rick`   | `!GetSchwifty!`       |

### Comments
| Username | Comment              |
|----------|-----------------------|
| `rick`   | `cool dog m8`         |
| `alice`  | `OMG so cute!`        |

---

## Insights
### Security Considerations
1. **Password Hashing**: The MD5 algorithm is used for hashing passwords. While MD5 is fast and easy to implement, it is considered cryptographically insecure for password storage due to vulnerabilities such as collision attacks. A more secure hashing algorithm like bcrypt or Argon2 should be used for production systems.
2. **Environment Variables**: Database credentials are retrieved from environment variables, which is a good practice for securing sensitive information.

### Scalability
1. **Schema Design**: The schema supports basic user and comment functionality. However, additional constraints (e.g., foreign key relationships) and indexes may be required for scalability and data integrity.
2. **Seed Data**: The hardcoded seed data is suitable for testing but should be removed or replaced with dynamic data for production environments.

### Error Handling
1. **Connection Failures**: The `connection()` method exits the program if a connection cannot be established. This approach may not be ideal for long-running applications, as it prevents recovery or retries.
2. **SQL Execution**: Errors during SQL execution are logged but not propagated, which may make debugging difficult in complex systems.

### Code Design
1. **Encapsulation**: The `insertUser` and `insertComment` methods are private, ensuring they are only used internally by the class.
2. **Hardcoded SQL Statements**: SQL statements are hardcoded, which may lead to maintenance challenges if the schema changes. Consider using an ORM (e.g., Hibernate) for dynamic query generation.

---

## Dependencies
- **PostgreSQL JDBC Driver**: The class requires the PostgreSQL JDBC driver (`org.postgresql.Driver`) to establish database connections.
- **Java Standard Libraries**: Utilizes standard Java libraries for database operations (`java.sql`), cryptography (`java.security`), and UUID generation (`java.util.UUID`).
