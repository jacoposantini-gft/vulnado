package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentTests {

    // Test for Comment.create method
    @Test
    public void create_ValidComment_ShouldReturnComment() throws Exception {
        // Mock Postgres connection
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        Postgres.setMockConnection(mockConnection);

        // Create a comment
        Comment comment = Comment.create("testUser", "testBody");

        // Assert the comment is created successfully
        assertNotNull(comment);
        assertEquals("testUser", comment.username);
        assertEquals("testBody", comment.body);
        assertNotNull(comment.created_on);
    }

    @Test(expected = BadRequest.class)
    public void create_InvalidComment_ShouldThrowBadRequest() throws Exception {
        // Mock Postgres connection
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        Postgres.setMockConnection(mockConnection);

        // Attempt to create a comment
        Comment.create("testUser", "testBody");
    }

    @Test(expected = ServerError.class)
    public void create_Exception_ShouldThrowServerError() throws Exception {
        // Mock Postgres connection
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));
        Postgres.setMockConnection(mockConnection);

        // Attempt to create a comment
        Comment.create("testUser", "testBody");
    }

    // Test for Comment.fetch_all method
    @Test
    public void fetchAll_ValidComments_ShouldReturnList() throws Exception {
        // Mock Postgres connection
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("id")).thenReturn("1");
        when(mockResultSet.getString("username")).thenReturn("testUser");
        when(mockResultSet.getString("body")).thenReturn("testBody");
        when(mockResultSet.getTimestamp("created_on")).thenReturn(new Timestamp(System.currentTimeMillis()));
        Postgres.setMockConnection(mockConnection);

        // Fetch all comments
        List<Comment> comments = Comment.fetch_all();

        // Assert the comments are fetched successfully
        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals("testUser", comments.get(0).username);
        assertEquals("testBody", comments.get(0).body);
    }

    @Test
    public void fetchAll_NoComments_ShouldReturnEmptyList() throws Exception {
        // Mock Postgres connection
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        Postgres.setMockConnection(mockConnection);

        // Fetch all comments
        List<Comment> comments = Comment.fetch_all();

        // Assert the comments list is empty
        assertNotNull(comments);
        assertTrue(comments.isEmpty());
    }

    // Test for Comment.delete method
    @Test
    public void delete_ValidComment_ShouldReturnTrue() throws Exception {
        // Mock Postgres connection
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        Postgres.setMockConnection(mockConnection);

        // Delete a comment
        boolean result = Comment.delete("1");

        // Assert the comment is deleted successfully
        assertTrue(result);
    }

    @Test
    public void delete_InvalidComment_ShouldReturnFalse() throws Exception {
        // Mock Postgres connection
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        Postgres.setMockConnection(mockConnection);

        // Attempt to delete a comment
        boolean result = Comment.delete("1");

        // Assert the comment is not deleted
        assertFalse(result);
    }

    @Test
    public void delete_Exception_ShouldReturnFalse() throws Exception {
        // Mock Postgres connection
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));
        Postgres.setMockConnection(mockConnection);

        // Attempt to delete a comment
        boolean result = Comment.delete("1");

        // Assert the comment is not deleted
        assertFalse(result);
    }
}
