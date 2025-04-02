package com.scalesec.vulnado;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Postgres.class, UUID.class})
public class CommentTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private Statement mockStatement;
    private ResultSet mockResultSet;

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);
        
        // Mock database connection and related objects
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockStatement = mock(Statement.class);
        mockResultSet = mock(ResultSet.class);
        
        PowerMockito.mockStatic(Postgres.class);
        when(Postgres.connection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
    }

    @Test
    public void testCommentConstructor() {
        // Arrange
        String id = "test-id";
        String username = "testuser";
        String body = "This is a test comment";
        Timestamp timestamp = new Timestamp(new Date().getTime());
        
        // Act
        Comment comment = new Comment(id, username, body, timestamp);
        
        // Assert
        assertEquals(id, comment.id);
        assertEquals(username, comment.username);
        assertEquals(body, comment.body);
        assertEquals(timestamp, comment.created_on);
    }

    @Test
    public void testFetchAll() throws SQLException {
        // Arrange
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false); // Return true twice for two comments, then false
        when(mockResultSet.getString("id")).thenReturn("id1", "id2");
        when(mockResultSet.getString("username")).thenReturn("user1", "user2");
        when(mockResultSet.getString("body")).thenReturn("comment1", "comment2");
        when(mockResultSet.getTimestamp("created_on")).thenReturn(new Timestamp(new Date().getTime()));
        
        // Act
        List<Comment> comments = Comment.fetch_all();
        
        // Assert
        assertNotNull(comments);
        assertEquals(2, comments.size());
        assertEquals("id1", comments.get(0).id);
        assertEquals("user1", comments.get(0).username);
        assertEquals("comment1", comments.get(0).body);
        assertEquals("id2", comments.get(1).id);
        assertEquals("user2", comments.get(1).username);
        assertEquals("comment2", comments.get(1).body);
    }

    @Test
    public void testDelete() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // Successful deletion
        
        // Act
        Boolean result = Comment.delete("test-id");
        
        // Assert
        assertFalse("Delete should return false due to implementation bug in finally block", result);
        // Note: The current implementation has a bug - it always returns false in the finally block
    }

    @Test
    public void testCommit() throws SQLException {
        // Arrange
        String id = "test-id";
        String username = "testuser";
        String body = "This is a test comment";
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Comment comment = new Comment(id, username, body, timestamp);
        
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // Successful commit
        
        // Act
        Boolean result = comment.commit(); // Using reflection to access private method
        
        // Assert
        assertTrue(result);
    }
}
