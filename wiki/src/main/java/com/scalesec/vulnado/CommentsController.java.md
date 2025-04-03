# Documentation: CommentsController.java

## Overview
The `CommentsController` class is a RESTful controller implemented using Spring Boot. It provides endpoints for managing comments, including fetching, creating, and deleting comments. The controller also includes mechanisms for authentication and error handling.

---

## Class Details

### **CommentsController**
The main controller class that handles HTTP requests related to comments.

#### **Fields**
| Field Name | Type   | Description                                                                 |
|------------|--------|-----------------------------------------------------------------------------|
| `secret`   | String | A secret value injected from application properties, used for authentication.|

#### **Endpoints**
| HTTP Method | Endpoint         | Description                                                                 |
|-------------|------------------|-----------------------------------------------------------------------------|
| `GET`       | `/comments`      | Fetches all comments. Requires an authentication token in the request header.|
| `POST`      | `/comments`      | Creates a new comment. Requires an authentication token and a JSON payload. |
| `DELETE`    | `/comments/{id}` | Deletes a comment by its ID. Requires an authentication token.              |

#### **Methods**
1. **`comments(String token)`**
   - **Description**: Fetches all comments.
   - **Parameters**:
     - `token`: Authentication token passed in the `x-auth-token` header.
   - **Returns**: A list of `Comment` objects.
   - **Authentication**: Validates the token using `User.assertAuth`.

2. **`createComment(String token, CommentRequest input)`**
   - **Description**: Creates a new comment.
   - **Parameters**:
     - `token`: Authentication token passed in the `x-auth-token` header.
     - `input`: A `CommentRequest` object containing the username and body of the comment.
   - **Returns**: The created `Comment` object.

3. **`deleteComment(String token, String id)`**
   - **Description**: Deletes a comment by its ID.
   - **Parameters**:
     - `token`: Authentication token passed in the `x-auth-token` header.
     - `id`: The ID of the comment to be deleted.
   - **Returns**: A boolean indicating whether the deletion was successful.

---

### **CommentRequest**
A data structure representing the request payload for creating a comment.

#### **Fields**
| Field Name | Type   | Description                     |
|------------|--------|---------------------------------|
| `username` | String | The username of the commenter. |
| `body`     | String | The content of the comment.    |

---

### **Error Handling**
Custom exceptions are defined to handle specific error scenarios.

#### **BadRequest**
- **Description**: Represents a `400 Bad Request` error.
- **Constructor**: Accepts an error message.

#### **ServerError**
- **Description**: Represents a `500 Internal Server Error`.
- **Constructor**: Accepts an error message.

---

## Insights
1. **Authentication**: The controller relies on a secret value (`app.secret`) and the `User.assertAuth` method to validate authentication tokens. This ensures that only authorized users can access the endpoints.
2. **Cross-Origin Resource Sharing (CORS)**: All endpoints are configured to allow requests from any origin (`@CrossOrigin(origins = "*")`), which is useful for enabling client-side applications to interact with the API but may pose security risks if not properly managed.
3. **Error Handling**: Custom exceptions (`BadRequest` and `ServerError`) are used to provide meaningful HTTP status codes and error messages, improving the API's usability and debugging experience.
4. **Serialization**: The `CommentRequest` class implements `Serializable`, ensuring compatibility with Java's serialization mechanism, which is useful for transferring data between systems.
5. **Dependency Injection**: The `@Value` annotation is used to inject the `app.secret` property, demonstrating the use of Spring's dependency injection for configuration management.

---

## Dependencies
The following dependencies are utilized:
- **Spring Boot**: Provides annotations like `@RestController`, `@EnableAutoConfiguration`, and `@RequestMapping` for building RESTful APIs.
- **Spring Web**: Handles HTTP requests and responses.
- **Java Serialization**: Used for the `CommentRequest` class.

---

## Security Considerations
1. **Token Validation**: Ensure that the `User.assertAuth` method is robust and secure to prevent unauthorized access.
2. **CORS Configuration**: Allowing all origins (`*`) may expose the API to security vulnerabilities. Consider restricting origins to trusted domains.
3. **Error Messages**: Avoid exposing sensitive information in error messages to prevent leaking details about the application's internals.
