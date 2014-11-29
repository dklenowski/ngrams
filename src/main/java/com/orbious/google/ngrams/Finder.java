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
    logger.info("searching file " + file);
    
    BufferedReader br = new BufferedReader(new FileReader(file));
    
    String str;
    String[] flds;
    String[] flds2;
    String wdtype;
    String wdlower = wd.toLowerCase();
    String fndwd;
    while ( (str = br.readLine()) != null ) {
      if ( str.equals("^$") ) continue;

      flds = str.trim().split("\\s+");
      if ( !flds[0].contains("_") ) continue;

      flds2 = flds[0].split("_", 2);
      fndwd = flds2[0];
      wdtype = flds2[1];
      
      if ( fndwd.toLowerCase().equals(wdlower) ) 
        logger.info("Found word " + fndwd + "(" + wdlower + ") as a " + wdtype + " in " + file);
    }
    
    br.close();
  }
}
