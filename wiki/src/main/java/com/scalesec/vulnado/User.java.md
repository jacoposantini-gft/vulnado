# Documentation: `User.java`

## Overview
The `User` class is part of the `com.scalesec.vulnado` package and provides functionality for user management, including token generation, authentication, and database interaction. It encapsulates user-related data and operations, such as fetching user details from a database and validating authentication tokens.

---

## Class: `User`

### Fields
| Field Name      | Type   | Description                                      |
|------------------|--------|--------------------------------------------------|
| `id`            | String | Unique identifier for the user.                  |
| `username`      | String | Username of the user.                            |
| `hashedPassword`| String | Hashed password of the user.                     |

### Constructor
#### `User(String id, String username, String hashedPassword)`
Initializes a new `User` object with the provided `id`, `username`, and `hashedPassword`.

---

### Methods

#### `String token(String secret)`
Generates a JSON Web Token (JWT) for the user using the provided secret key.

- **Parameters**:
  - `secret`: A string used as the secret key for signing the JWT.
- **Returns**:
  - A signed JWT containing the `username` as the subject.
- **Implementation Details**:
  - Uses the `io.jsonwebtoken` library to create and sign the token.
  - The secret key is derived using `Keys.hmacShaKeyFor`.

---

#### `static void assertAuth(String secret, String token)`
Validates the provided JWT token using the given secret key.

- **Parameters**:
  - `secret`: A string used as the secret key for verifying the JWT.
  - `token`: The JWT to be validated.
- **Throws**:
  - `Unauthorized`: If the token validation fails.
- **Implementation Details**:
  - Parses the token using the `JwtParser` from the `io.jsonwebtoken` library.
  - If validation fails, an exception is thrown with the error message.

---

#### `static User fetch(String un)`
Fetches a user from the database based on the provided username.

- **Parameters**:
  - `un`: The username of the user to fetch.
- **Returns**:
  - A `User` object populated with data from the database, or `null` if no user is found.
- **Implementation Details**:
  - Establishes a connection to the database using `Postgres.connection()`.
  - Executes a SQL query to retrieve user details.
  - Constructs a `User` object using the retrieved data.
  - Closes the database connection after execution.
- **Potential Issues**:
  - The SQL query is vulnerable to SQL injection due to direct concatenation of the `username` parameter.
  - Exception handling is limited to printing stack traces and error messages.

---

## Insights

### Security Concerns
1. **SQL Injection Vulnerability**:
   - The `fetch` method constructs SQL queries by directly concatenating user input (`username`). This approach is highly vulnerable to SQL injection attacks. Using prepared statements is recommended to mitigate this risk.

2. **Hardcoded Secret Key Handling**:
   - The `token` and `assertAuth` methods rely on a secret key provided as a string. Ensure the secret key is securely stored and managed to prevent unauthorized access.

3. **Exception Handling**:
   - The `fetch` method only prints stack traces and error messages without proper error propagation or logging mechanisms. This can lead to silent failures in production environments.

### Usage of External Libraries
- The `io.jsonwebtoken` library is used for JWT creation and validation. It provides robust functionality for token-based authentication but requires careful handling of secret keys.

### Database Interaction
- The `fetch` method interacts with a PostgreSQL database using the `Postgres.connection()` method. Ensure the `Postgres` class is properly implemented and configured for secure database connections.

### Recommendations
- Replace string concatenation in SQL queries with prepared statements to prevent SQL injection.
- Implement proper logging and error handling mechanisms for better observability and debugging.
- Use environment variables or secure vaults to manage sensitive data like secret keys.

---

## Dependencies
| Dependency                  | Purpose                                      |
|-----------------------------|----------------------------------------------|
| `io.jsonwebtoken`           | JWT creation and validation.                |
| `javax.crypto.SecretKey`    | Secret key generation for HMAC signing.     |
| `java.sql.Connection`       | Database connection management.             |
| `java.sql.Statement`        | Execution of SQL queries.                   |
| `java.sql.ResultSet`        | Retrieval of query results.                 |

---

## Potential Enhancements
1. **Input Validation**:
   - Validate user input (e.g., `username`) to ensure it adheres to expected formats and constraints.

2. **Token Expiry**:
   - Add expiration claims to the JWT to enhance security and prevent token reuse.

3. **Error Propagation**:
   - Replace `System.out.println` and `e.printStackTrace` with structured logging and proper exception handling.

4. **Database Connection Pooling**:
   - Use a connection pool to optimize database interactions and reduce overhead.
