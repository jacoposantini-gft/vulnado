# Documentation: `LoginController.java`

## Overview
The `LoginController` class is part of a Spring Boot application and provides functionality for user authentication. It exposes an endpoint for login requests, validates user credentials, and returns a token upon successful authentication. The class also handles unauthorized access scenarios.

---

## File Metadata
- **File Name**: `LoginController.java`

---

## Components

### 1. **LoginController**
#### Description
The `LoginController` is a REST controller that handles login requests. It uses Spring Boot annotations to define its behavior and configuration.

#### Key Features
- **Endpoint**: `/login`
  - **HTTP Method**: POST
  - **Consumes**: JSON
  - **Produces**: JSON
  - **Cross-Origin**: Allows requests from all origins (`@CrossOrigin(origins = "*")`).
- **Authentication Logic**:
  - Fetches user details using `User.fetch(input.username)`.
  - Compares the hashed password from the request with the stored hashed password.
  - Generates a token using a secret value if authentication is successful.
  - Throws an `Unauthorized` exception if authentication fails.

#### Dependencies
- **Spring Boot**:
  - `@RestController`
  - `@EnableAutoConfiguration`
  - `@RequestMapping`
  - `@CrossOrigin`
  - `@Value`
- **HTTP Status Handling**:
  - `HttpStatus.UNAUTHORIZED`

#### Fields
| Field Name | Type   | Description                          |
|------------|--------|--------------------------------------|
| `secret`   | String | Application secret used for token generation. |

---

### 2. **LoginRequest**
#### Description
A data structure representing the login request payload. It implements `Serializable` for potential serialization needs.

#### Fields
| Field Name | Type   | Description                     |
|------------|--------|---------------------------------|
| `username` | String | Username provided by the user. |
| `password` | String | Password provided by the user. |

---

### 3. **LoginResponse**
#### Description
A data structure representing the login response payload. It contains the token generated upon successful authentication. Implements `Serializable`.

#### Fields
| Field Name | Type   | Description                     |
|------------|--------|---------------------------------|
| `token`    | String | Authentication token.          |

#### Constructor
| Parameter | Type   | Description                     |
|-----------|--------|---------------------------------|
| `msg`     | String | Token message to be assigned.  |

---

### 4. **Unauthorized**
#### Description
A custom exception class that represents unauthorized access. It is annotated with `@ResponseStatus(HttpStatus.UNAUTHORIZED)` to automatically return a 401 status code when thrown.

#### Constructor
| Parameter   | Type   | Description                     |
|-------------|--------|---------------------------------|
| `exception` | String | Exception message.             |

---

## Insights

### Security Considerations
- **Hardcoded Secret**: The `secret` field is injected using `@Value("${app.secret}")`. Ensure that the secret is securely stored and managed (e.g., environment variables or a secrets manager).
- **Password Hashing**: The password comparison uses `Postgres.md5`. Verify that this hashing mechanism is secure and up-to-date with modern cryptographic standards.
- **Cross-Origin Requests**: The `@CrossOrigin(origins = "*")` annotation allows requests from all origins. This could expose the endpoint to potential security risks. Consider restricting origins to trusted domains.

### Error Handling
- Unauthorized access is handled using a custom exception (`Unauthorized`) with a clear HTTP status code (`401 Unauthorized`).

### Scalability
- The `User.fetch` method and token generation logic are not detailed in this file. Ensure these methods are optimized for performance and scalability, especially in high-traffic scenarios.

### Serialization
Both `LoginRequest` and `LoginResponse` implement `Serializable`, which is useful for object serialization. However, ensure that serialization is necessary for the application's requirements.

### Dependency Management
The class relies on Spring Boot annotations and features. Ensure that the dependencies are properly managed in the project's `pom.xml` or `build.gradle` file.

---

## Endpoint Summary

| Endpoint | HTTP Method | Request Type | Response Type | Description                     |
|----------|-------------|--------------|---------------|---------------------------------|
| `/login` | POST         | JSON         | JSON          | Authenticates user credentials and returns a token. |
