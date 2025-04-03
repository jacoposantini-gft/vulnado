package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.DriverManager;
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
            assertFalse("Connection should be valid", connection.isClosed());
            connection.close();
        } catch (Exception e) {
            fail("Exception occurred while establishing connection: " + e.getMessage());
        }
    }

    @Test
    public void setup_ShouldCreateTablesAndInsertSeedData() {
        try {
            Postgres.setup();
            Connection connection = Postgres.connection();
            Statement stmt = connection.createStatement();

            // Verify users table exists and contains seed data
            ResultSet rsUsers = stmt.executeQuery("SELECT COUNT(*) AS count FROM users");
            rsUsers.next();
            int userCount = rsUsers.getInt("count");
            assertTrue("Users table should contain seed data", userCount > 0);

            // Verify comments table exists and contains seed data
            ResultSet rsComments = stmt.executeQuery("SELECT COUNT(*) AS count FROM comments");
            rsComments.next();
            int commentCount = rsComments.getInt("count");
            assertTrue("Comments table should contain seed data", commentCount > 0);

            connection.close();
        } catch (Exception e) {
            fail("Exception occurred during setup: " + e.getMessage());
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
            String username = "testUser_" + UUID.randomUUID();
            String password = "testPassword";

            Postgres.setup(); // Ensure clean database
            Postgres.insertUser(username, password);

            Connection connection = Postgres.connection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT username FROM users WHERE username = '" + username + "'");
            assertTrue("User should be inserted into database", rs.next());
            assertEquals("Inserted username should match", username, rs.getString("username"));

            connection.close();
        } catch (Exception e) {
            fail("Exception occurred while inserting user: " + e.getMessage());
        }
    }

    @Test
    public void insertComment_ShouldInsertCommentIntoDatabase() {
        try {
            String username = "testUser_" + UUID.randomUUID();
            String password = "testPassword";
            String commentBody = "This is a test comment";

            Postgres.setup(); // Ensure clean database
            Postgres.insertUser(username, password); // Insert user first
            Postgres.insertComment(username, commentBody);

            Connection connection = Postgres.connection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT body FROM comments WHERE username = '" + username + "'");
            assertTrue("Comment should be inserted into database", rs.next());
            assertEquals("Inserted comment body should match", commentBody, rs.getString("body"));

            connection.close();
        } catch (Exception e) {
            fail("Exception occurred while inserting comment: " + e.getMessage());
        }
    }
}
