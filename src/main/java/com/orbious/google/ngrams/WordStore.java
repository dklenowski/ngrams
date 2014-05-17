package com.orbious.google.ngrams;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class WordStore {

  private HashMap<String, RedisSet> stores;
 
  private String ip;
  private int port;
  
  protected static final Logger logger = Logger.getLogger(Config.logrealm);

  public WordStore() {
    stores = new HashMap<String, RedisSet>();
    ip = Config.redis_ip;
    port = Config.redis_port;
  }

  public WordStore(String ip, int port) { 
    this();
    this.ip = ip;
    this.port = port;
  }
  
  public boolean isAvailable() throws RedisException {
    RedisSet rs = new RedisSet(ip, port, "test");
    rs.connect();
    boolean connected = rs.isConnected();
    rs.disconnect();
    
    return connected;
  }
  
  public String connectStr() {
    return ip + ": " + port;
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
    RedisSet store = stores.get(typestr);
    if ( store != null ) return store;
    
    store = new RedisSet(typestr, port, typestr);
    store.connect();
    stores.put(typestr, store);
    
    return store;
  }
  
  
  public WordStoreType type(String wd) {
    Iterator<String> iter = stores.keySet().iterator();
    String key;
    RedisSet store;
    while ( iter.hasNext() ) {
      key = iter.next();
      store = stores.get(key);
      if ( store.contains(key) ) 
        return WordStoreType.fromString(key);
    }

    return null;
  }

}
