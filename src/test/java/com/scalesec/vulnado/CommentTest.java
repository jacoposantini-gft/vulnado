import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentTest {

    private Connection mockConnection() throws SQLException {
        return mock(Connection.class);
    }

    private PreparedStatement mockPreparedStatement() throws SQLException {
        return mock(PreparedStatement.class);
    }

    private ResultSet mockResultSet() throws SQLException {
        return mock(ResultSet.class);
    }

    @Test
    public void create_ValidInput_ShouldCreateComment() throws SQLException {
        Connection connection = mockConnection();
        PreparedStatement preparedStatement = mockPreparedStatement();

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        Postgres.setConnection(connection);

        Comment comment = Comment.create("testUser", "testBody");

        assertNotNull(comment, "Comment should not be null");
        assertEquals("testUser", comment.username, "Username should match");
    }

    @Test
    public void create_InvalidInput_ShouldThrowBadRequest() throws SQLException {
        Connection connection = mockConnection();
        PreparedStatement preparedStatement = mockPreparedStatement();

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        Postgres.setConnection(connection);

        assertThrows(BadRequest.class, () -> Comment.create("testUser", "testBody"), "Should throw BadRequest exception");
    }

    @Test
    public void fetch_all_ShouldReturnComments() throws SQLException {
        Connection connection = mockConnection();
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mockResultSet();

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("id")).thenReturn("1");
        when(resultSet.getString("username")).thenReturn("testUser");
        when(resultSet.getString("body")).thenReturn("testBody");
        when(resultSet.getTimestamp("created_on")).thenReturn(new Timestamp(System.currentTimeMillis()));

        Postgres.setConnection(connection);

        List<Comment> comments = Comment.fetch_all();

        assertNotNull(comments, "Comments list should not be null");
        assertEquals(1, comments.size(), "Comments list size should be 1");
        assertEquals("testUser", comments.get(0).username, "Username should match");
        assertEquals("testBody", comments.get(0).body, "Body should match");
    }

    @Test
    public void delete_ValidId_ShouldReturnTrue() throws SQLException {
        Connection connection = mockConnection();
        PreparedStatement preparedStatement = mockPreparedStatement();

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        Postgres.setConnection(connection);

        boolean result = Comment.delete("1");

        assertTrue(result, "Delete should return true");
    }

    @Test
    public void delete_InvalidId_ShouldReturnFalse() throws SQLException {
        Connection connection = mockConnection();
        PreparedStatement preparedStatement = mockPreparedStatement();

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        Postgres.setConnection(connection);

        boolean result = Comment.delete("invalidId");

        assertFalse(result, "Delete should return false");
    }

    @Test
    public void commit_ValidComment_ShouldReturnTrue() throws SQLException {
        Connection connection = mockConnection();
        PreparedStatement preparedStatement = mockPreparedStatement();

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        Postgres.setConnection(connection);

        Comment comment = new Comment("1", "testUser", "testBody", new Timestamp(System.currentTimeMillis()));
        boolean result = comment.commit();

        assertTrue(result, "Commit should return true");
    }

    @Test
    public void commit_InvalidComment_ShouldThrowSQLException() throws SQLException {
        Connection connection = mockConnection();
        PreparedStatement preparedStatement = mockPreparedStatement();

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("SQL Error"));

        Postgres.setConnection(connection);

        Comment comment = new Comment("1", "testUser", "testBody", new Timestamp(System.currentTimeMillis()));

        assertThrows(SQLException.class, comment::commit, "Commit should throw SQLException");
    }
}
