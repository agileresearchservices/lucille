package com.kmwllc.lucille.joiningDbConnector;

import com.kmwllc.lucille.test.ConfigValidationUtils;
import org.junit.Test;

public class ConfigValidationTest {

  @Test
  public void testConf() throws Exception {
    ConfigValidationUtils.validateConfigs();
  }
}