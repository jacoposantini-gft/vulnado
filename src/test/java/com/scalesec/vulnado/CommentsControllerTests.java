package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentsControllerTests {

    @Value("${app.secret}")
    private String secret;

    @Test
    public void comments_ShouldReturnListOfComments_WhenAuthenticated() {
        // Arrange
        String token = "valid-token";
        CommentsController controller = new CommentsController();
        controller.secret = secret;

        List<Comment> mockComments = new ArrayList<>();
        mockComments.add(new Comment("user1", "comment1"));
        mockComments.add(new Comment("user2", "comment2"));

        mockStatic(User.class);
        mockStatic(Comment.class);

        when(User.assertAuth(secret, token)).thenReturn(true);
        when(Comment.fetch_all()).thenReturn(mockComments);

        // Act
        List<Comment> result = controller.comments(token);

        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Result size should match", 2, result.size());
        assertEquals("First comment username should match", "user1", result.get(0).getUsername());
        assertEquals("Second comment body should match", "comment2", result.get(1).getBody());
    }

    @Test(expected = ResponseStatusException.class)
    public void comments_ShouldThrowException_WhenNotAuthenticated() {
        // Arrange
        String token = "invalid-token";
        CommentsController controller = new CommentsController();
        controller.secret = secret;

        mockStatic(User.class);
        when(User.assertAuth(secret, token)).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // Act
        controller.comments(token);
    }

    @Test
    public void createComment_ShouldCreateComment_WhenValidInput() {
        // Arrange
        String token = "valid-token";
        CommentRequest input = new CommentRequest();
        input.username = "user1";
        input.body = "comment1";

        CommentsController controller = new CommentsController();
        controller.secret = secret;

        Comment mockComment = new Comment("user1", "comment1");

        mockStatic(User.class);
        mockStatic(Comment.class);

        when(User.assertAuth(secret, token)).thenReturn(true);
        when(Comment.create(input.username, input.body)).thenReturn(mockComment);

        // Act
        Comment result = controller.createComment(token, input);

        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Username should match", "user1", result.getUsername());
        assertEquals("Body should match", "comment1", result.getBody());
    }

    @Test(expected = ResponseStatusException.class)
    public void createComment_ShouldThrowException_WhenNotAuthenticated() {
        // Arrange
        String token = "invalid-token";
        CommentRequest input = new CommentRequest();
        input.username = "user1";
        input.body = "comment1";

        CommentsController controller = new CommentsController();
        controller.secret = secret;

        mockStatic(User.class);
        when(User.assertAuth(secret, token)).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // Act
        controller.createComment(token, input);
    }

    @Test
    public void deleteComment_ShouldReturnTrue_WhenCommentDeleted() {
        // Arrange
        String token = "valid-token";
        String commentId = "123";

        CommentsController controller = new CommentsController();
        controller.secret = secret;

        mockStatic(User.class);
        mockStatic(Comment.class);

        when(User.assertAuth(secret, token)).thenReturn(true);
        when(Comment.delete(commentId)).thenReturn(true);

        // Act
        Boolean result = controller.deleteComment(token, commentId);

        // Assert
        assertTrue("Result should be true", result);
    }

    @Test(expected = ResponseStatusException.class)
    public void deleteComment_ShouldThrowException_WhenNotAuthenticated() {
        // Arrange
        String token = "invalid-token";
        String commentId = "123";

        CommentsController controller = new CommentsController();
        controller.secret = secret;

        mockStatic(User.class);
        when(User.assertAuth(secret, token)).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // Act
        controller.deleteComment(token, commentId);
    }
}
