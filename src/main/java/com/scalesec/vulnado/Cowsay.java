package com.scalesec.vulnado;

import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;

private Cowsay() {}
public class Cowsay {
  public static String run(String input) {
    ProcessBuilder processBuilder = new ProcessBuilder();
// Ensure proper validation of input and PATH
// Validate input to prevent command injection
    String cmd = "/usr/games/cowsay '" + input + "'";
    Logger logger = Logger.getLogger(Cowsay.class.getName());
// Ensure PATH is properly sanitized
    processBuilder.command("bash", "-c", cmd); // Ensure proper validation of input and PATH

    StringBuilder output = new StringBuilder();

// Use try-with-resources for better resource management
    try {
      Process process = processBuilder.start();
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
// Avoid concatenating strings directly in append
        output.append(line).append("\n");
      }
} catch (Exception e) {
logger.severe("Exception occurred: " + e.getMessage());
// Remove debug features before production
      logger.warning("Debug feature activated: " + e.getMessage());
}
    return output.toString();
  }
}
