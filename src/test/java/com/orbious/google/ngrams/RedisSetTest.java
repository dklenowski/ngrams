package com.orbious.google.ngrams;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RedisSetTest {

  private String testsetname = "redisset";
  
	@BeforeClass
	public void before() { 
		Utils.setuplogger();
	}
	
  @Test
  public void init() throws Exception { 
  	RedisSet rs = new RedisSet(TestConfig.redis_ip, TestConfig.redis_port, testsetname);
  	rs.connect();
  	Assert.assertTrue(rs.isConnected());
  }
  
  @Test
  public void put() throws Exception {
  	RedisSet rs = new RedisSet(TestConfig.redis_ip, TestConfig.redis_port, testsetname);
  	rs.connect();
  	
  	rs.put("1");
  	Assert.assertTrue(rs.contains("1"));

  	rs.put("2");
  	rs.put("3");
  	Assert.assertTrue(rs.contains("2"));
  	Assert.assertTrue(rs.contains("3"));
  	
  	Assert.assertFalse(rs.contains("5"));
  }
}

