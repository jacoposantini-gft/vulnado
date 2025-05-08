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
  private String id; // Make id non-public
  private String username; // Make username non-public

  private String hashedPassword; // Make hashedPassword non-public
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
      logger.severe(e.getMessage());
      throw new Unauthorized(e.getMessage());
    }
  }

  public static User fetch(String un) {
    Statement stmt = null;
    User user = null;
    try {
      Connection cxn = Postgres.connection();
      stmt = cxn.createStatement();
      Logger logger = Logger.getLogger(User.class.getName());
      logger.info(\"Opened database successfully\");

      String query = "select * from users where username = '" + un + "' limit 1";
      logger.info(query);
      PreparedStatement pstmt = cxn.prepareStatement(\"SELECT * FROM users WHERE username = ? LIMIT 1\");
      pstmt.setString(1, un);
      if (rs.next()) {
      String userId = rs.getString(\"user_id\");
      String userName = rs.getString(\"username\");
      String hashedPassword = rs.getString(\"password\");
      user = new User(userId, userName, hashedPassword);
      }
      cxn.close();
    } catch (Exception e) {
      e.printStackTrace();
      logger.severe(e.getClass().getName() + \": \" + e.getMessage());
    } finally {
    }
  }
}
