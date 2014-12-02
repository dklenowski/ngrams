package com.orbious.google.ngrams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import gnu.getopt.Getopt;
import org.apache.log4j.Logger;

public class Finder {

  private static Logger logger = null;

  private static void usage() {
    System.out.println(
        "Usage: Finder [-h] -i <inputdir> -w <word>\n" +
        "    -h               Print this help message and exit.\n" +
        "    -i <inputdir>    The input directory to parse.\n" +
        "    -w <word>        The word to search for.\n");
    System.exit(1);
  }

  public static void main(String[] args) {
    Utils.setuplogger();
    logger = Logger.getLogger(NgramConfig.logrealm);

    String inputname = null;
    String wd = null;
    
    Getopt opts = new Getopt("Parser", args, "hi:w:");
    int c;
    while ( (c = opts.getopt()) != -1 ) {
      switch ( c ) {
      case 'i':
        inputname = opts.getOptarg();
        break;
      case 'w':
        wd = opts.getOptarg();
        break;
      case 'h':
        usage();
        break;
      }
    }

    if ( inputname == null ) {
      System.err.println("you must specify a <inputdir>?");
      usage();
    } else if ( wd == null ) {
      System.err.println("you must specify a <word>?");
      usage();
    }
    
    File inputpath = new File(inputname);
    if ( !inputpath.exists() ) {
      System.err.println("input (" + inputpath + ") does not exist?");
      System.exit(1);
    }
    
    parse(inputpath, wd);
  }

  public static void parse(File inputpath, String wd) {
    File[] files;
    if ( inputpath.isDirectory() ) 
      files = inputpath.listFiles();
    else
      files = new File[] { inputpath };
    
    for (File file : files) {
      try{ 
        search(file, wd);
      } catch ( FileNotFoundException fnfe ) {
        logger.warn("failed to open " + file, fnfe);
      } catch ( IOException ioe ) {
        logger.warn("ioerror searching " + file, ioe);
      }
    }
  }
  
  public static void search(File file, String wd) throws IOException, FileNotFoundException {
    String wdlower = wd.toLowerCase();
    BufferedReader br = new BufferedReader(new FileReader(file));

    logger.info("searching for " + wd + " in " + file);
    
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
      
      if ( wdlower.toLowerCase().equals(pr.wd()) ) {
        logger.info("found word in line " + str);
        logger.info(wd + "(" + wdlower + 
            ") is a " + WordStoreType.toString(pr.type()) + " in " + file);
      }
    }

    br.close();
  }
}
