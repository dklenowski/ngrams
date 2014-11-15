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
        "Usage: Parser [-h]\n" +
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

    int linect = 0;
    String str;
    String[] flds;
    String[] flds2;
    String wd;
    String wdtype;
    WordStoreType storetype;

    while ( (str = br.readLine()) != null ) {
      linect++;

      if ( str.equals("^$") ) continue;

      flds = str.trim().split("\\s+");
      if ( !flds[0].contains("_") ) {
        logger.debug("failed to find type on line " + linect + ": " + str);
        continue;
      }

      flds2 = flds[0].split("_", 2);
      wd = flds2[0];
      wdtype = flds2[1];

      if ( wd == null ) {
        logger.fatal("failed to extract word from line " + linect + ": " + str);
        continue;
      } else if ( wdtype == null ) {
        logger.fatal("failed to extract word type from line " + linect + ": " + str);
        continue;
      }

      if ( !Utils.validword(wd) ) {
        logger.debug("word " + wd + " from line " + linect + " is not valid");
        continue;
      }

      storetype = WordStoreType.fromString(wdtype);
      if ( storetype == null ) {
        logger.warn("unknown type (" + wdtype + ") found on line " + linect + ": " + str);
        continue;
      }

      try {
        store.put(wd, storetype);
      } catch ( RedisException rse ) {
        logger.fatal("failed to add wd " + wd + " as " + wdtype, rse);
        break;
      }
    }

    br.close();
  }
}
