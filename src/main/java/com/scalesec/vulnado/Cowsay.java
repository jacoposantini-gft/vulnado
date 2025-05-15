package com.scalesec.vulnado;

import java.util.logging.Logger;
import java.io.BufferedReader;
import java.util.logging.Level;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import java.util.logging.Level;
private Cowsay() {}
public class Cowsay {
Logger logger = Logger.getLogger(Cowsay.class.getName());
  public static String run(String input) {
logger.setLevel(Level.WARNING);
    ProcessBuilder processBuilder = new ProcessBuilder();
logger
logger.info(\"Command initialized.\");
    String cmd = "/usr/games/cowsay '" + input + "'";
logger.info(cmd);
    Logger logger = Logger.getLogger(Cowsay.class.getName());
    processBuilder.command(\"bash\", \"-c\", \"/usr/games/cowsay '\" + input.replace(\"'\", \"\\'\\'\") + \"'\");

    StringBuilder output = new StringBuilder();

logger.info(\"Process started.\");
    try {
      Process process = processBuilder.start();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      }
logger.severe(\"Exception occurred: \" + e.getMessage());
    } catch (Exception e) {
      logger.warning(\"Debug feature activated: \" + e.getMessage());
    }
logger.info(\"Process completed.\");
    return output.toString();
logger.info(\"Returning output.\");
  }
logger.info(\"Execution finished.\");
}
logger.info(\"End of class.\");
