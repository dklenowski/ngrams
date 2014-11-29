package com.orbious.google.ngrams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.orbious.jedisutil.RedisException;

import gnu.getopt.Getopt;

import org.apache.log4j.Logger;

public class Parser {

  private static Logger logger = null;

  private static void usage() {
    System.out.println(
        "Usage: Parser [-h] -i <inputfile>\n" +
        "    -h               Print this help message and exit.\n" +
        "    -i <inputfile>   The inputfile to process.\n");
    System.exit(1);
  }

  public static void main(String[] args) {
    Utils.setuplogger();
    logger = Logger.getLogger(NgramConfig.logrealm);

    String inputname = null;

    Getopt opts = new Getopt("Parser", args, "hi:");
    int c;
    while ( (c = opts.getopt()) != -1 ) {
      switch ( c ) {
      case 'i':
        inputname = opts.getOptarg();
        break;
      case 'h':
        usage();
        break;
      }
    }

    if ( inputname == null ) {
      System.err.println("you must specify a <inputfile>?");
      usage();
    }

    WordStore store = new WordStore();
    File inputfile = new File(inputname);
    try {
      parse(inputfile, store);
    } catch ( FileNotFoundException fnfe ) {
      logger.fatal("failed to find file " + inputname, fnfe);
    } catch ( IOException ioe ) {
      logger.fatal("io error parsing file " + inputname, ioe);
    }
  }

  public static void parse(File inputfile, WordStore store) throws FileNotFoundException, IOException {
    logger.debug("parsing " + inputfile);

    BufferedReader br = new BufferedReader(new FileReader(inputfile));

    String str;
    ParseResult pr;
    String prev = null;
    String[] flds;
    
    while ( (str = br.readLine()) != null ) {
      if ( str.equals("^$") ) continue;

      // try to "continue" fast !
      flds = str.trim().split("\\s+");
      if ( !flds[0].contains("_") ) continue;
      
      // there are allot of duplicates in the ngrams ..
      if ( flds[0].equals(prev) ) continue;
      
      prev = flds[0];
      pr = ParseUtils.parse(str);
      if ( pr == null ) continue;
      
      try {
        logger.info("adding result: " + pr);
        store.put(pr.wd(), pr.type());;
      } catch ( RedisException rse ) {
        logger.fatal("failed to add result: " + pr, rse);
        break;
      }
    }

    br.close();
  }
}
