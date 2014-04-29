package com.orbious.google.ngrams;

import java.util.HashSet;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class Utils {
  
  private static HashSet<Character> punctuation;
  
  static {
  	punctuation = new HashSet<Character>();
  	punctuation.add('.');
  	punctuation.add('-');
  	punctuation.add('\'');
  	punctuation.add('`');
  }
  
	private Utils() { }
	
	public static void setuplogger() {
		Logger logger = Logger.getLogger(Config.logrealm);
		logger.setLevel(Config.loglevel);
		logger.addAppender(new ConsoleAppender(
    		new PatternLayout("%d{ISO8601} %-5p  %C{2} (%M:%L) - %m%n") ));
	}
	
	public static boolean validword(String wd) {
		char[] buf = wd.toCharArray();
		for ( int i = 0; i < buf.length; i++ ) {
			if ( !Character.isLetterOrDigit(buf[i]) && !punctuation.contains(buf[i]) ) 
				return false;
		}
		
		return true;
	}
}
