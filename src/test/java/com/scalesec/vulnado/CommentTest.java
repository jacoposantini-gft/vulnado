import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentTest {

    @Test
    public void create_ValidInput_ShouldCreateComment() {
        String username = "testUser";
        String body = "testBody";

        Comment comment = Comment.create(username, body);

        assertNotNull(comment, "Comment should not be null");
        assertEquals(username, comment.username, "Username should match the input");
        assertEquals(body, comment.body, "Body should match the input");
        assertNotNull(comment.id, "Comment ID should be generated");
        assertNotNull(comment.created_on, "Timestamp should be generated");
    }

    @Test
    public void create_InvalidCommit_ShouldThrowBadRequest() {
        String username = "testUser";
        String body = "testBody";

        Comment mockComment = mock(Comment.class);
        when(mockComment.commit()).thenReturn(false);

        assertThrows(BadRequest.class, () -> Comment.create(username, body), "Should throw BadRequest when commit fails");
    }

    @Test
    public void fetchAll_ShouldReturnComments() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(Postgres.connection()).thenReturn(mockConnection);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery("select * from comments;")).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("id")).thenReturn("1");
        when(mockResultSet.getString("username")).thenReturn("testUser");
        when(mockResultSet.getString("body")).thenReturn("testBody");
        when(mockResultSet.getTimestamp("created_on")).thenReturn(new Timestamp(System.currentTimeMillis()));

        List<Comment> comments = Comment.fetch_all();

        assertNotNull(comments, "Comments list should not be null");
        assertEquals(1, comments.size(), "Should return one comment");
        assertEquals("1", comments.get(0).id, "Comment ID should match");
        assertEquals("testUser", comments.get(0).username, "Username should match");
        assertEquals("testBody", comments.get(0).body, "Body should match");
        assertNotNull(comments.get(0).created_on, "Timestamp should not be null");
    }

    @Test
    public void fetchAll_EmptyResultSet_ShouldReturnEmptyList() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(Postgres.connection()).thenReturn(mockConnection);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery("select * from comments;")).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(false);

        List<Comment> comments = Comment.fetch_all();

        assertNotNull(comments, "Comments list should not be null");
        assertTrue(comments.isEmpty(), "Should return an empty list");
    }

    @Test
    public void delete_ValidId_ShouldReturnTrue() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(Postgres.connection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement("DELETE FROM comments where id = ?")).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = Comment.delete("1");

        assertTrue(result, "Should return true for successful deletion");
    }

    @Test
    public void delete_InvalidId_ShouldReturnFalse() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(Postgres.connection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement("DELETE FROM comments where id = ?")).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        boolean result = Comment.delete("invalidId");

        assertFalse(result, "Should return false for unsuccessful deletion");
    }

    @Test
    public void commit_ValidComment_ShouldReturnTrue() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(Postgres.connection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement("INSERT INTO comments (id, username, body, created_on) VALUES (?,?,?,?)")).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        Comment comment = new Comment("1", "testUser", "testBody", new Timestamp(System.currentTimeMillis()));
        boolean result = comment.commit();

        assertTrue(result, "Should return true for successful commit");
    }

    @Test
    public void commit_InvalidComment_ShouldReturnFalse() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(Postgres.connection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement("INSERT INTO comments (id, username, body, created_on) VALUES (?,?,?,?)")).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        Comment comment = new Comment("1", "testUser", "testBody", new Timestamp(System.currentTimeMillis()));
        boolean result = comment.commit();

        assertFalse(result, "Should return false for unsuccessful commit");
    }
}
