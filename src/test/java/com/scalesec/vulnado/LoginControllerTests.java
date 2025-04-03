package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class LoginControllerTests {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private User mockUser;

    @Mock
    private Postgres mockPostgres;

    @Value("${app.secret}")
    private String secret;

    // Helper method to create a LoginRequest object
    private LoginRequest createLoginRequest(String username, String password) {
        LoginRequest request = new LoginRequest();
        request.username = username;
        request.password = password;
        return request;
    }

    @Test
    public void login_ValidCredentials_ShouldReturnToken() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";
        String hashedPassword = "hashedPassword";
        String expectedToken = "validToken";

        LoginRequest request = createLoginRequest(username, password);

        when(User.fetch(username)).thenReturn(mockUser);
        when(mockUser.hashedPassword).thenReturn(hashedPassword);
        when(Postgres.md5(password)).thenReturn(hashedPassword);
        when(mockUser.token(secret)).thenReturn(expectedToken);

        // Act
        LoginResponse response = loginController.login(request);

        // Assert
        assertNotNull("Response should not be null", response);
        assertEquals("Token should match expected value", expectedToken, response.token);
    }

    @Test
    public void login_InvalidCredentials_ShouldThrowUnauthorizedException() {
        // Arrange
        String username = "testUser";
        String password = "wrongPassword";
        String hashedPassword = "hashedPassword";

        LoginRequest request = createLoginRequest(username, password);

        when(User.fetch(username)).thenReturn(mockUser);
        when(mockUser.hashedPassword).thenReturn(hashedPassword);
        when(Postgres.md5(password)).thenReturn("differentHashedPassword");

        // Act & Assert
        try {
            loginController.login(request);
            fail("Expected Unauthorized exception to be thrown");
        } catch (Unauthorized ex) {
            assertEquals("Exception message should match", "Access Denied", ex.getMessage());
        }
    }

    @Test
    public void login_NonExistentUser_ShouldThrowUnauthorizedException() {
        // Arrange
        String username = "nonExistentUser";
        String password = "testPassword";

        LoginRequest request = createLoginRequest(username, password);

        when(User.fetch(username)).thenReturn(null);

        // Act & Assert
        try {
            loginController.login(request);
            fail("Expected Unauthorized exception to be thrown");
        } catch (Unauthorized ex) {
            assertEquals("Exception message should match", "Access Denied", ex.getMessage());
        }
    }
}
