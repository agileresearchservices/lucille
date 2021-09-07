package com.kmwllc.lucille.stage;

import com.kmwllc.lucille.core.Document;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ApplyRegexTest {

  @Test
  public void testApplyRegex() throws Exception {
    Config config = ConfigFactory.load("ApplyRegexTest/config.conf");
    ApplyRegex stage = new ApplyRegex(config);
    stage.start();

    Document doc = new Document("doc");
    doc.setField("input1", "here is a number 12");
    stage.processDocument(doc);
    assertEquals("Regex pattern should extract numbers from the input", "12",
        doc.getStringList("output1").get(0));

    Document doc2 = new Document("doc2");
    doc2.setField("input1", "here are some numbers: 1, 2, 3, 4, 5");
    stage.processDocument(doc2);
    List<String> tokens = doc2.getStringList("output1");
    for (int i = 1; i <= 5; i++) {
      assertEquals("Regex should extract all the numbers in order", String.valueOf(i), tokens.get(i - 1));
    }

    Document doc3 = new Document("doc3");
    doc3.setField("input3", "this is field #3");
    stage.processDocument(doc3);
    assertEquals("Field output3 should contain the extracted values from input3",
        doc3.getStringList("output3").get(0), "3");

    Document doc4 = new Document("doc4");
    doc4.setField("input1", "this is field input 1");
    doc4.setField("input2", "this is field input 2");
    doc4.setField("input3", "this is field input 3");
    stage.processDocument(doc4);
    assertEquals("output1 should contain values from input", "1", doc4.getStringList("output1").get(0));
    assertEquals("output2 should contain values from input2", "2", doc4.getStringList("output2").get(0));
    assertEquals("output3 should contain values from input3", "3", doc4.getStringList("output3").get(0));
  }

}