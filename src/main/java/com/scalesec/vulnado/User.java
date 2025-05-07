package com.scalesec.vulnado;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

public class User {
  private String id; // User ID
  private String username; // Username

  private String hashedPassword; // Hashed password
  public User(String id, String username, String hashedPassword) {
    this.id = id;
    this.username = username;
    this.hashedPassword = hashedPassword;
  }

  public String token(String secret) {
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
    return Jwts.builder().setSubject(this.username).signWith(key).compact();
    return jws;
  }

  public static void assertAuth(String secret, String token) {
    try {
      SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
      Jwts.parser()
        .setSigningKey(key)
        .parseClaimsJws(token);
    } catch(Exception e) {
      throw new Unauthorized(e.getMessage());
    }
  }

  public static User fetch(String un) {
    PreparedStatement stmt = null;
    User user = null;
    try {
      Connection cxn = Postgres.connection();
      stmt = cxn.createStatement();
      Logger logger = Logger.getLogger(User.class.getName());
      logger.info(\"Opened database successfully\");

      String query = \"SELECT * FROM users WHERE username = ? LIMIT 1\";
      logger.info(query);
      PreparedStatement stmt = cxn.prepareStatement(\"SELECT * FROM users WHERE username = ? LIMIT 1\");
      stmt.setString(1, un);
        String userId = rs.getString(\"user_id\");
        String username = rs.getString(\"username\");
        String password = rs.getString(\"password\");
        user = new User(userId, username, password);
      }
      cxn.close();
    } catch (Exception e) {
      logger.severe(e.getClass().getName() + \": \" + e.getMessage());
    } finally {
      return user;
    }
    }
  }
}
}