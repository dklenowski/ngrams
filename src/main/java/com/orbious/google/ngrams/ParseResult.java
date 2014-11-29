package com.orbious.google.ngrams;

public class ParseResult {
  private String wd;
  private WordStoreType type;
  
  public ParseResult(String wd, WordStoreType type) {
    this.wd = wd;
    this.type = type;
  }
  
  public String wd() { return wd; }
  public WordStoreType type() { return type; }
  
  @Override
  public String toString() { 
    return wd + " (" + WordStoreType.toString(type) + ")";
  }
}