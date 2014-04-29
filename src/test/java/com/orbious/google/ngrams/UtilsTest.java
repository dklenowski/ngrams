package com.orbious.google.ngrams;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UtilsTest {

  @BeforeClass
  public void before() { 
    Utils.setuplogger();
  }

  @Test
  public void invalidword() {
    Assert.assertFalse(Utils.validword("\\xc3\\xa1ngel"));
  }

  @Test
  public void validword() {
    Assert.assertTrue(Utils.validword("awakeners"));
    Assert.assertTrue(Utils.validword("avam"));
    Assert.assertTrue(Utils.validword("2"));
    Assert.assertTrue(Utils.validword("I.B.M"));
  }
}

