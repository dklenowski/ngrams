package com.orbious.google.ngrams;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class WordStore {

  private HashMap<String, RedisSet> stores;

  protected static final Logger logger = Logger.getLogger(Config.logrealm);

  public WordStore() {
    stores = new HashMap<String, RedisSet>();
  }

  public void put(String wd, WordStoreType type) throws RedisSetException {
    String typestr = WordStoreType.toString(type);
    if ( typestr == null ) {
      logger.fatal("failed to add word " + wd + " invalid type (" + type + ")");
      return;
    }

    RedisSet store = stores.get(typestr);
    if ( store == null ) {
      store = new RedisSet(Config.redis_ip, Config.redis_port, typestr);
      store.connect();
      stores.put(typestr, store);
    }

    logger.debug("adding word " + wd +  " to " + typestr);
    store.put(wd);
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
