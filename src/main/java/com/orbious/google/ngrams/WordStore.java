package com.orbious.google.ngrams;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class WordStore {

  private HashMap<String, RedisSet> stores;
 
  private String ip;
  private int port;
  
  private boolean initialized = false;
  
  protected static final Logger logger = Logger.getLogger(NgramConfig.logrealm);

  public WordStore() {
    stores = new HashMap<String, RedisSet>();
    this.ip = NgramConfig.redis_ip;
    this.port = NgramConfig.redis_port;
  }
  
  public String connectStr() {
    return ip + ":" + port;
  }
  
  public void put(String wd, WordStoreType type) throws RedisException {
    String typestr = WordStoreType.toString(type);
    if ( typestr == null ) {
      logger.fatal("failed to add word " + wd + " invalid type (" + type + ")");
      return;
    }

    logger.debug("adding word " + wd +  " to " + typestr);
    
    RedisSet store = store(typestr);
    store.put(wd);
  }
  
  public boolean is(String wd, WordStoreType type) throws RedisException {
    String typestr = WordStoreType.toString(type);
    if ( typestr == null ) {
      logger.fatal("invalid type (" + type + ") searching for " + wd);
      return false;
    }
   
    RedisSet store = store(typestr);
    return store.contains(wd);
  }

  private RedisSet store(String typestr) throws RedisException { 
    logger.info("initializing word store " + typestr);
    RedisSet store = stores.get(typestr);
    if ( store != null ) return store;
    
    store = new RedisSet(ip, port, typestr);
    store.connect();
    stores.put(typestr, store);
    
    return store;
  }
  
  
  public WordStoreType type(String wd) throws RedisException {
    if ( !initialized ) initAllStores();
    
    Iterator<String> iter = stores.keySet().iterator();
    String key;
    RedisSet store;
    while ( iter.hasNext() ) {
      key = iter.next();
      store = stores.get(key);
      if ( store.contains(wd) ) {
        if ( logger.isDebugEnabled() ) 
          logger.debug(wd + " is a " +  key);
        return WordStoreType.fromString(key);
      }
    }

    if ( logger.isDebugEnabled() )
      logger.debug("failed to find word " + wd);
    return null;
  }
  
  private void initAllStores() throws RedisException {
    String typestr;
    for ( WordStoreType type : WordStoreType.values() ) {
      typestr = WordStoreType.toString(type);
      logger.info("initializing wordstore " + typestr);
      store(typestr);
    }
    
    initialized = true;
  }

}
