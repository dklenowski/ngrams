package com.orbious.google.ngrams;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ParseUtilsTest {

  @BeforeClass
  public void before() { 
    Utils.setuplogger();
  }

  @Test
  public void invalidword() {
    Assert.assertFalse(ParseUtils.validword("\\xc3\\xa1ngel"));
  }

  @Test
  public void validword() {
    Assert.assertTrue(ParseUtils.validword("awakeners"));
    Assert.assertTrue(ParseUtils.validword("avam"));
    Assert.assertFalse(ParseUtils.validword("2"));
    Assert.assertTrue(ParseUtils.validword("I.B.M"));
  }
  
  @Test
  public void parse() {
    String line;
    ParseResult pr;
    
    line = "C.A.S.H._NOUN   2005    38      5";
    pr = ParseUtils.parse(line);
    Assert.assertEquals(pr.wd(), "c.a.s.h.");
    Assert.assertEquals(pr.type(), WordStoreType.NOUN);
  }

  @Test
  public void nullparse() {
    String line;
    ParseResult pr;
    
    line = "CAEPIO_NOUN     1995    3       2";
    pr = ParseUtils.parse(line);
    Assert.assertNull(pr);
  }
  
  @Test
  public void capitalized() {
    Assert.assertFalse(ParseUtils.capitalized("Caepio"));
    Assert.assertFalse(ParseUtils.capitalized("I.B.M"));
    Assert.assertTrue(ParseUtils.capitalized("CAEPIO"));
  }
}

