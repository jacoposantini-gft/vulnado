package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostgresTests {

    @Test
    public void connection_ShouldEstablishConnection() {
        try {
            Connection connection = Postgres.connection();
            assertNotNull("Connection should not be null", connection);
            assertFalse("Connection should be open", connection.isClosed());
            connection.close();
        } catch (Exception e) {
            fail("Exception occurred while testing connection: " + e.getMessage());
        }
    }

    @Test
    public void setup_ShouldCreateTablesAndInsertSeedData() {
        try {
            Postgres.setup();
            Connection connection = Postgres.connection();
            Statement stmt = connection.createStatement();

            // Verify users table
            ResultSet rsUsers = stmt.executeQuery("SELECT COUNT(*) AS count FROM users");
            rsUsers.next();
            int userCount = rsUsers.getInt("count");
            assertEquals("Users table should have 5 seed entries", 5, userCount);

            // Verify comments table
            ResultSet rsComments = stmt.executeQuery("SELECT COUNT(*) AS count FROM comments");
            rsComments.next();
            int commentCount = rsComments.getInt("count");
            assertEquals("Comments table should have 2 seed entries", 2, commentCount);

            connection.close();
        } catch (Exception e) {
            fail("Exception occurred while testing setup: " + e.getMessage());
        }
    }

    @Test
    public void md5_ShouldReturnCorrectHash() {
        String input = "test";
        String expectedHash = "098f6bcd4621d373cade4e832627b4f6"; // Precomputed MD5 hash for "test"
        String actualHash = Postgres.md5(input);
        assertEquals("MD5 hash should match expected value", expectedHash, actualHash);
    }

    @Test
    public void insertUser_ShouldInsertUserIntoDatabase() {
        try {
            String username = "test_user_" + UUID.randomUUID();
            String password = "TestPassword123!";
            Postgres.insertUser(username, password);

            Connection connection = Postgres.connection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT username, password FROM users WHERE username = '" + username + "'");
            assertTrue("User should exist in the database", rs.next());
            assertEquals("Username should match", username, rs.getString("username"));
            assertEquals("Password hash should match", Postgres.md5(password), rs.getString("password"));

            connection.close();
        } catch (Exception e) {
            fail("Exception occurred while testing insertUser: " + e.getMessage());
        }
    }

    @Test
    public void insertComment_ShouldInsertCommentIntoDatabase() {
        try {
            String username = "test_user_" + UUID.randomUUID();
            String body = "This is a test comment!";
            Postgres.insertComment(username, body);

            Connection connection = Postgres.connection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT username, body FROM comments WHERE username = '" + username + "'");
            assertTrue("Comment should exist in the database", rs.next());
            assertEquals("Username should match", username, rs.getString("username"));
            assertEquals("Comment body should match", body, rs.getString("body"));

            connection.close();
        } catch (Exception e) {
            fail("Exception occurred while testing insertComment: " + e.getMessage());
        }
    }
}
