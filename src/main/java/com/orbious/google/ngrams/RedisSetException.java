package com.orbious.google.ngrams;

public class RedisSetException extends Exception {

  private static final long serialVersionUID = 1L;

  public RedisSetException(String msg) {
    super(msg);
  }

  public RedisSetException(String msg, Throwable cause) {
    super(msg, cause);
  }
}


