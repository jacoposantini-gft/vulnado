package com.scalesec.vulnado;

import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;

private static final Logger LOGGER = Logger.getLogger(Cowsay.class.getName());
public class Cowsay {
  public static String run(String input) {
    ProcessBuilder processBuilder = new ProcessBuilder();
    String sanitizedInput = input.replaceAll("[^a-zA-Z0-9 ]", ""); // Sanitize input
    System.out.println(cmd);
    processBuilder.command("/usr/games/cowsay", sanitizedInput); // Use arguments directly

    StringBuilder output = new StringBuilder();

    try {
      Process process = processBuilder.start();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      }
    } catch (Exception e) {
      LOGGER.severe("An error occurred while executing the command: " + e.getMessage());
    }
    return output.toString();
  }
}
