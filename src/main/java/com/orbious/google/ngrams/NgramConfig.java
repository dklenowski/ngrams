package com.orbious.google.ngrams;

import org.apache.log4j.Level;

public class NgramConfig {

  private NgramConfig() { }
  
  // redis stuff
  public static final String redis_ip = "127.0.0.1";
  public static final int redis_port = 7000;
  public static final int redistimeout = 1000;
  
  // parse stuff
  public static boolean lowercase = true;   // convert everything to lowercase
  
  // logging stuff
  public static final String logrealm = "ngrams";
  public static final Level loglevel = Level.INFO; 

}
