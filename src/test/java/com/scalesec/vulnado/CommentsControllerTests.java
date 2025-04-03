package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentsControllerTests {

    @Autowired
    private CommentsController commentsController;

    @Value("${app.secret}")
    private String secret;

    // Mock dependencies
    private User mockUser = mock(User.class);
    private Comment mockComment = mock(Comment.class);

    // Helper method to create a mock CommentRequest
    private CommentRequest createMockCommentRequest(String username, String body) {
        CommentRequest request = new CommentRequest();
        request.username = username;
        request.body = body;
        return request;
    }

    @Test
    public void comments_ShouldReturnListOfComments_WhenAuthenticated() {
        // Arrange
        String token = "valid-token";
        List<Comment> expectedComments = Arrays.asList(new Comment(), new Comment());
        Mockito.doNothing().when(mockUser).assertAuth(secret, token);
        Mockito.when(mockComment.fetch_all()).thenReturn(expectedComments);

        // Act
        List<Comment> actualComments = commentsController.comments(token);

        // Assert
        assertEquals("Expected comments list should match actual comments list", expectedComments, actualComments);
    }

    @Test(expected = ResponseStatusException.class)
    public void comments_ShouldThrowException_WhenNotAuthenticated() {
        // Arrange
        String token = "invalid-token";
        Mockito.doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED)).when(mockUser).assertAuth(secret, token);

        // Act
        commentsController.comments(token);
    }

    @Test
    public void createComment_ShouldReturnCreatedComment_WhenValidInput() {
        // Arrange
        String token = "valid-token";
        CommentRequest input = createMockCommentRequest("testUser", "testBody");
        Comment expectedComment = new Comment();
        Mockito.when(mockComment.create(input.username, input.body)).thenReturn(expectedComment);

        // Act
        Comment actualComment = commentsController.createComment(token, input);

        // Assert
        assertEquals("Expected comment should match actual comment", expectedComment, actualComment);
    }

    @Test(expected = ResponseStatusException.class)
    public void createComment_ShouldThrowException_WhenInvalidInput() {
        // Arrange
        String token = "valid-token";
        CommentRequest input = createMockCommentRequest("", ""); // Invalid input
        Mockito.doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST)).when(mockComment).create(input.username, input.body);

        // Act
        commentsController.createComment(token, input);
    }

    @Test
    public void deleteComment_ShouldReturnTrue_WhenCommentDeletedSuccessfully() {
        // Arrange
        String token = "valid-token";
        String commentId = "123";
        Mockito.when(mockComment.delete(commentId)).thenReturn(true);

        // Act
        Boolean result = commentsController.deleteComment(token, commentId);

        // Assert
        assertTrue("Expected result should be true when comment is deleted successfully", result);
    }

    @Test
    public void deleteComment_ShouldReturnFalse_WhenCommentDeletionFails() {
        // Arrange
        String token = "valid-token";
        String commentId = "123";
        Mockito.when(mockComment.delete(commentId)).thenReturn(false);

        // Act
        Boolean result = commentsController.deleteComment(token, commentId);

        // Assert
        assertFalse("Expected result should be false when comment deletion fails", result);
    }

    @Test(expected = ResponseStatusException.class)
    public void deleteComment_ShouldThrowException_WhenInvalidCommentId() {
        // Arrange
        String token = "valid-token";
        String commentId = "invalid-id";
        Mockito.doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST)).when(mockComment).delete(commentId);

        // Act
        commentsController.deleteComment(token, commentId);
    }
}
