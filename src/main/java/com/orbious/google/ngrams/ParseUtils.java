package com.orbious.google.ngrams;

import java.util.HashSet;
import org.apache.log4j.Logger;

public class ParseUtils {

  private static HashSet<Character> punctuation;
  static {
    punctuation = new HashSet<Character>();
    punctuation.add('.');
    punctuation.add('-');
    punctuation.add('\'');
    punctuation.add('`');
  }
  
  protected static final Logger logger = Logger.getLogger(NgramConfig.logrealm);
  
  private ParseUtils() { }
  
  public static ParseResult parse(String line) {
    String[] flds = line.trim().split("\\s+");
    
    if ( !flds[0].contains("_") ) return null;
    
    String[] categories = flds[0].split("_", 2);
    String wd = categories[0];
    String wdtype =categories[1];
    
    if ( wd == null ) {
      logger.warn("failed to extract word from line: " + line);
      return null;
    } else if ( wdtype == null ) {
      logger.warn("failed to extract word-type from line:" + line);
      return null;
    }
    
    if ( !validword(wd) ) {
      logger.info("invalid word (" + wd + ") found on line: " + line);
      return null;
    } else if ( capitalized(wd) ) {
      logger.info("all caps word (" + wd + ") found on line: " + line);
      return null;
    }
    
    WordStoreType storetype = WordStoreType.fromString(wdtype);
    if ( storetype == null ) {
      logger.warn("unknow type (" + wdtype + ") found on line:" + line);
      return null;
    }
    
    if ( NgramConfig.lowercase ) 
      wd = wd.toLowerCase();
    
    return new ParseResult(wd, storetype);
  }

  public static boolean validword(String wd) {
    char[] buf = wd.toCharArray();
    for ( int i = 0; i < buf.length; i++ ) {
      if ( !Character.isLetter(buf[i]) && !punctuation.contains(buf[i]) ) 
        return false;
    }

    return true;
  }
  
  public static boolean capitalized(String wd) {
    char[] buf = wd.toCharArray();
    for ( int i = 0; i < buf.length; i++ ) { 
      if ( Character.isLowerCase(buf[i]) || punctuation.contains(buf[i]) )
        return false;
    }

    return true;
  }
}
