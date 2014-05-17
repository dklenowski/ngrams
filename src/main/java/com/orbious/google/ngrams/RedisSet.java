package com.orbious.google.ngrams;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

public class RedisSet {

  private JedisPool pool;

  private String ip;
  private int port;
  private String setname;

  protected static final Logger logger = Logger.getLogger(Config.logrealm);

  public RedisSet(String ip, int port, String setname) { 
    this.ip = ip;
    this.port = port;
    this.setname = setname;
  }

  public synchronized void connect() throws RedisException {
    if ( pool != null ) return;

    logger.info("connecting to redis on " + ip + ": " + port);

    JedisPoolConfig config = new JedisPoolConfig();
    config.setTestOnBorrow(true);
    pool = new JedisPool(config, ip, port, Config.redistimeout);

    if ( !isConnected() ) 
      throw new RedisException("Failed to connect to redis at " + ip + ": " +port);
  }

  public boolean isConnected() {
    Jedis jedis = pool.getResource();
    boolean connected = jedis.isConnected();
    pool.returnResource(jedis);

    return connected;
  }

  public synchronized void disconnect()  {
    try {
      pool.destroy();
    } catch ( JedisException je ) { 
      logger.warn("failed to destroy jedis pool");
    } finally {
      pool = null;
    }
  }

  public synchronized boolean contains(String key) {
    logger.debug("searching for key " + key);
    Jedis jedis = pool.getResource();
    boolean ismember = jedis.sismember(setname, key);
    pool.returnResource(jedis);

    return ismember;
  }

  public synchronized void put(String key) {
    logger.debug("adding " + key + " to set " + setname);
    Jedis jedis = pool.getResource();
    jedis.sadd(setname, key);
    pool.returnResource(jedis);
  }

  public synchronized String connectStr() {
    return ip + ": " + port + " (" + setname + ")";
  }
}
