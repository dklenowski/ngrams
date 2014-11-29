package com.orbious.google.ngrams;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.orbious.jedisutil.RedisException;
import com.orbious.jedisutil.RedisSet;

public class WordStore {

  private HashMap<WordStoreType, RedisSet> stores;
 
  private String ip;
  private int port;
  
  private boolean initialized = false;
  
  protected static final Logger logger = Logger.getLogger(NgramConfig.logrealm);

  public WordStore() {
    stores = new HashMap<WordStoreType, RedisSet>();
    this.ip = NgramConfig.redis_ip;
    this.port = NgramConfig.redis_port;
  }
  
  public synchronized void connect() throws RedisException {
    initAllStores();
  }
  
  public synchronized String connectStr() {
    return ip + ":" + port;
  }
  
  public synchronized void put(String wd, WordStoreType type) throws RedisException {
    String typestr = WordStoreType.toString(type);
    if ( typestr == null ) {
      logger.fatal("failed to add word " + wd + " invalid type (" + type + ")");
      return;
    }

    RedisSet store = store(type);
    if ( store.contains(wd) ) return;

    logger.debug("adding word " + wd +  " to " + typestr);
    store.put(wd);
  }
  
  public synchronized boolean is(String wd, WordStoreType type) throws RedisException {
    String typestr = WordStoreType.toString(type);
    if ( typestr == null ) {
      logger.fatal("invalid type (" + type + ") searching for " + wd);
      return false;
    }
   
    RedisSet store = store(type);
    if ( NgramConfig.lowercase ) 
      return store.contains(wd.toLowerCase());
    else
      return store.contains(wd);
  }

  private RedisSet store(WordStoreType type) throws RedisException { 
    String typestr = WordStoreType.toString(type);
    RedisSet store = stores.get(type);
    if ( store != null ) return store;
    
    logger.info("initializing word store " + typestr);
    store = new RedisSet(ip, port, typestr);
    store.connect();
    stores.put(type, store);
    
    return store;
  }
  
  
  public synchronized WordStoreType type(String wd) throws RedisException {
    if ( !initialized ) initAllStores();

    for ( WordStoreType type : WordStoreType.values() ) {
      if ( stores.get(type).contains(wd) ) {
        if ( logger.isInfoEnabled() ) 
          logger.info("word " + wd + " is a " + WordStoreType.toString(type));
        return type;
      }
    }

    if ( logger.isInfoEnabled() ) 
      logger.info("failed to find type for word " + wd);
    
    return null;
  }
  
  private void initAllStores() throws RedisException {
    for ( WordStoreType type : WordStoreType.values() ) 
      store(type);
    
    initialized = true;
  }
  
  public void dump(File outputfile, WordStoreType type) throws RedisException, IOException {
    RedisSet store = store(type);
    store.dump(outputfile);
  }
}
