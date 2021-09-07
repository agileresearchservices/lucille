package com.kmwllc.lucille.stage;

import com.kmwllc.lucille.core.Document;
import com.kmwllc.lucille.core.Stage;
import com.kmwllc.lucille.core.StageException;
import com.kmwllc.lucille.util.StageUtils;
import com.typesafe.config.Config;

import java.util.List;

/**
 * This stage copies values from a given set of source fields to a given set of destination fields. If the same number
 * of fields are supplied for both sources and destinations, the fields will be copied from source_1 to dest_1 and
 * source_2 to dest_2. If either source or dest has only one field value, and the other has several, all of the
 * fields will be copied to/from the same field.
 */
public class CopyFields extends Stage {

  private final List<String> sourceFields;
  private final List<String> destFields;

  public CopyFields(Config config) {
    super(config);
    this.sourceFields = config.getStringList("source");
    this.destFields = config.getStringList("dest");
  }

  @Override
  public void start() throws StageException {
    StageUtils.validateFieldNumNotZero(sourceFields, "Copy Fields");
    StageUtils.validateFieldNumNotZero(destFields, "Copy Fields");
    StageUtils.validateFieldNumsSeveralToOne(sourceFields, destFields, "Copy Fields");
  }

  @Override
  public List<Document> processDocument(Document doc) throws StageException {
    for (int i = 0; i < sourceFields.size(); i++) {
      // If there is only one source or dest, use it. Otherwise, use the current source/dest.
      String sourceField = sourceFields.get(i);
      String destField = destFields.size() == 1 ? destFields.get(0) : destFields.get(i);

      if (!doc.has(sourceField))
        continue;

      for (String value : doc.getStringList(sourceField)) {
        doc.addToField(destField, value);
      }
    }

    return null;
  }
}