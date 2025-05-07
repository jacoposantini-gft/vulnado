package com.scalesec.vulnado;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Cowsay {
  private static String sanitizeInput(String input) {
  public static String run(String input) {
    Pattern pattern = Pattern.compile("[a-zA-Z0-9 ]*");
    ProcessBuilder processBuilder = new ProcessBuilder();
    Matcher matcher = pattern.matcher(input);
    String cmd = "/usr/games/cowsay '" + input + "'";
    if (matcher.matches()) {
    System.out.println(cmd);
      return input;
    processBuilder.command("bash", "-c", "/usr/games/cowsay '" + sanitizeInput(input) + "'");
    } else {

      throw new IllegalArgumentException("Invalid input detected");
    StringBuilder output = new StringBuilder();
    }

  }
    try {
      Process process = processBuilder.start();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      }
    } catch (Exception e) {
      System.err.println("Debugging information: " + e.getMessage());
    }
    return output.toString();
  }
}
