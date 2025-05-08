import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentsControllerTest {

    @Value("${app.secret}")
    private String secret = "test-secret";

    @Test
    void comments_ShouldReturnAllComments_WhenAuthenticated() {
        // Arrange
        String token = "valid-token";
        List<Comment> mockComments = new ArrayList<>();
        mockComments.add(new Comment("user1", "comment1"));
        mockComments.add(new Comment("user2", "comment2"));

        User mockUser = mock(User.class);
        Comment mockComment = mock(Comment.class);

        Mockito.doNothing().when(mockUser).assertAuth(secret, token);
        when(mockComment.fetch_all()).thenReturn(mockComments);

        CommentsController controller = new CommentsController();
        controller.secret = secret;

        // Act
        List<Comment> result = controller.comments(token);

        // Assert
        assertEquals(mockComments, result, "Should return all comments");
    }

    @Test
    void comments_ShouldThrowException_WhenNotAuthenticated() {
        // Arrange
        String token = "invalid-token";

        User mockUser = mock(User.class);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED)).when(mockUser).assertAuth(secret, token);

        CommentsController controller = new CommentsController();
        controller.secret = secret;

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> controller.comments(token), "Should throw exception for invalid token");
    }

    @Test
    void createComment_ShouldCreateComment_WhenValidInput() {
        // Arrange
        String token = "valid-token";
        CommentRequest input = new CommentRequest();
        input.username = "user1";
        input.body = "comment1";

        Comment mockComment = mock(Comment.class);
        when(mockComment.create(input.username, input.body)).thenReturn(new Comment(input.username, input.body));

        CommentsController controller = new CommentsController();
        controller.secret = secret;

        // Act
        Comment result = controller.createComment(token, input);

        // Assert
        assertNotNull(result, "Should create a comment");
        assertEquals(input.username, result.getUsername(), "Username should match");
        assertEquals(input.body, result.getBody(), "Body should match");
    }

    @Test
    void createComment_ShouldThrowException_WhenInvalidInput() {
        // Arrange
        String token = "valid-token";
        CommentRequest input = new CommentRequest();
        input.username = null; // Invalid input
        input.body = "comment1";

        Comment mockComment = mock(Comment.class);
        when(mockComment.create(input.username, input.body)).thenThrow(new BadRequest("Invalid input"));

        CommentsController controller = new CommentsController();
        controller.secret = secret;

        // Act & Assert
        assertThrows(BadRequest.class, () -> controller.createComment(token, input), "Should throw exception for invalid input");
    }

    @Test
    void deleteComment_ShouldDeleteComment_WhenValidId() {
        // Arrange
        String token = "valid-token";
        String id = "comment-id";

        Comment mockComment = mock(Comment.class);
        when(mockComment.delete(id)).thenReturn(true);

        CommentsController controller = new CommentsController();
        controller.secret = secret;

        // Act
        Boolean result = controller.deleteComment(token, id);

        // Assert
        assertTrue(result, "Should delete the comment");
    }

    @Test
    void deleteComment_ShouldThrowException_WhenInvalidId() {
        // Arrange
        String token = "valid-token";
        String id = "invalid-id";

        Comment mockComment = mock(Comment.class);
        when(mockComment.delete(id)).thenThrow(new BadRequest("Invalid ID"));

        CommentsController controller = new CommentsController();
        controller.secret = secret;

        // Act & Assert
        assertThrows(BadRequest.class, () -> controller.deleteComment(token, id), "Should throw exception for invalid ID");
    }
}
