package com.orbious.google.ngrams;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class Utils {

  private Utils() { }

  public static void setuplogger() {
    Logger logger = Logger.getLogger(NgramConfig.logrealm);
    logger.setLevel(NgramConfig.loglevel);
    logger.addAppender(new ConsoleAppender(
        new PatternLayout("%d{ISO8601} %-5p  %C{2} (%M:%L) - %m%n") ));
  }
  
}
