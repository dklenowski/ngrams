package com.orbious.google.ngrams;

import gnu.getopt.Getopt;
import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

public class Dumper {
  private static Logger logger = null;

  private static void usage() {
    System.out.println(
        "Usage: Dumper [-h] -t <type> -o <outputfile>\n" +
        "    -h               Print this help message and exit.\n" +
        "    -t <type>        The WordStoreType string <type> to dump.\n" + 
        "    -o <outputfile>  The <outputfile> to dump to.\n");
    System.exit(1);
  }

  public static void main(String[] args) {
    Utils.setuplogger();
    logger = Logger.getLogger(NgramConfig.logrealm);

    String outputname = null;
    String typestr = null;
    
    Getopt opts = new Getopt("Parser", args, "ht:o:");
    int c;
    while ( (c = opts.getopt()) != -1 ) {
      switch ( c ) {
      case 'o':
        outputname = opts.getOptarg();
        break;
      case 't':
        typestr = opts.getOptarg();
        break;
      case 'h':
        usage();
        break;
      }
    }

    if ( outputname == null ) {
      System.err.println("you must specify a <outputfile>?");
      usage();
    } else if ( typestr == null ) {
      System.err.println("you must specify a <typestr>?");
      usage();
    }
    
    File outputfile = new File(outputname);
    if ( outputfile.exists() ) {
      logger.fatal("outputfile " + outputfile + " already exists?");
      System.exit(1);
    }
    
    WordStoreType type =  WordStoreType.fromString(typestr);
    if ( type == null ) {
      logger.fatal("invalid type (" + typestr + ") specified?");
      System.exit(1);
    }
    
    WordStore wordstore = new WordStore();
    try {
      wordstore.dump(outputfile, type);
    } catch ( RedisException re ) {
      logger.warn("redis error dumping " + typestr + " to " + outputfile, re);
    } catch ( IOException ioe ) {
      logger.warn("io error dumping " + typestr + " to " + outputfile, ioe);
    }
  }
}
