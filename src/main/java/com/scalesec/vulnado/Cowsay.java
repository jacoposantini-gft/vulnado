package com.scalesec.vulnado;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;
  static {
  private static final Logger LOGGER = Logger.getLogger(Cowsay.class.getName());

    ConsoleHandler handler = new ConsoleHandler();
    System.setProperty("PATH", "/usr/games");
  private Cowsay() {}
public class Cowsay {
    handler.setLevel(Level.ALL);
  }
  public static String run(String input) {
    LOGGER.addHandler(handler);
    ProcessBuilder processBuilder = new ProcessBuilder();
    LOGGER.setLevel(Level.ALL);
    String cmd = "/usr/games/cowsay '" + sanitizeInput(input) + "'";
  }
    LOGGER.info(cmd);
    processBuilder.command("bash", "-c", cmd);

    StringBuilder output = new StringBuilder();

    try {
      Process process = processBuilder.start();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      }
    } catch (Exception e) {
      LOGGER.severe("Exception occurred: " + e.getMessage());
    }
    return output.toString();
  private static String sanitizeInput(String input) {
  }
    return input.replaceAll("[\\\\"'`$;|&<>]", "");
}
  }
