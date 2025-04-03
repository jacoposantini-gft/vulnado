# Documentation: `Comment.java`

## Overview
The `Comment` class is part of the `com.scalesec.vulnado` package and provides functionality for managing comments in a database. It includes methods for creating, fetching, deleting, and committing comments to a PostgreSQL database. The class interacts with the database using JDBC and relies on a `Postgres` utility class for database connections.

---

## Class: `Comment`

### Fields
| Field Name   | Type       | Description                                      |
|--------------|------------|--------------------------------------------------|
| `id`         | `String`   | Unique identifier for the comment.               |
| `username`   | `String`   | Username of the user who created the comment.    |
| `body`       | `String`   | Content of the comment.                          |
| `created_on` | `Timestamp`| Timestamp indicating when the comment was created.|

---

### Constructor
#### `Comment(String id, String username, String body, Timestamp created_on)`
Initializes a new `Comment` object with the provided values.

**Parameters:**
- `id`: Unique identifier for the comment.
- `username`: Username of the user who created the comment.
- `body`: Content of the comment.
- `created_on`: Timestamp indicating when the comment was created.

---

### Methods

#### `static Comment create(String username, String body)`
Creates a new comment and saves it to the database.

**Parameters:**
- `username`: Username of the user creating the comment.
- `body`: Content of the comment.

**Returns:**
- A `Comment` object representing the newly created comment.

**Throws:**
- `BadRequest`: If an error occurs while saving the comment.
- `ServerError`: If a server-side error occurs.

---

#### `static List<Comment> fetch_all()`
Fetches all comments from the database.

**Returns:**
- A `List<Comment>` containing all comments retrieved from the database.

**Behavior:**
- Executes a SQL query to fetch all rows from the `comments` table.
- Maps each row to a `Comment` object and adds it to the list.

---

#### `static Boolean delete(String id)`
Deletes a comment from the database based on its ID.

**Parameters:**
- `id`: The unique identifier of the comment to be deleted.

**Returns:**
- `true` if the comment was successfully deleted.
- `false` otherwise.

**Behavior:**
- Executes a SQL `DELETE` statement with the provided ID.

---

#### `private Boolean commit()`
Commits the current `Comment` object to the database.

**Returns:**
- `true` if the comment was successfully saved.
- `false` otherwise.

**Throws:**
- `SQLException`: If an error occurs during the database operation.

**Behavior:**
- Executes a SQL `INSERT` statement to save the comment's data.

---

## Insights

### Security Concerns
1. **SQL Injection Risk**: 
   - The `fetch_all` method uses raw SQL queries (`Statement`) instead of prepared statements, which can expose the application to SQL injection attacks. It is recommended to use `PreparedStatement` for all database queries.
   
2. **Error Handling**:
   - The `delete` method always returns `false` in the `finally` block, even if the operation succeeds. This can lead to incorrect behavior. Proper error handling and return logic should be implemented.

3. **Exception Management**:
   - Exceptions are printed to the console (`e.printStackTrace()`), which is not ideal for production environments. Consider using a logging framework for better error tracking.

---

### Database Dependency
The class relies on a `Postgres` utility class for database connections. Ensure that the `Postgres.connection()` method is properly implemented and handles connection pooling efficiently.

---

### UUID for Comment IDs
The `create` method generates unique IDs for comments using `UUID.randomUUID()`. This ensures that each comment has a globally unique identifier, which is suitable for distributed systems.

---

### Timestamp Usage
The `created_on` field uses `Timestamp` to store the creation time of comments. This is useful for tracking and sorting comments based on their creation time.

---

### Potential Improvements
1. **Validation**:
   - Add input validation for `username` and `body` in the `create` method to prevent invalid or malicious data from being stored in the database.

2. **Pagination**:
   - The `fetch_all` method retrieves all comments without any limit. For large datasets, consider implementing pagination to improve performance.

3. **Error Messages**:
   - Provide more descriptive error messages in exceptions to help with debugging and user feedback.

---

## Metadata
| Key         | Value          |
|-------------|----------------|
| File Name   | `Comment.java` |
| Package     | `com.scalesec.vulnado` |
