package org.infinispan;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FloatPoint;
import org.apache.lucene.document.LatLonDocValuesField;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

public class Person {

   String name;
   String email;
   Integer age;
   String company;
   String metadata;
   Float height;
   Double latitude;
   Double longitude;
   Double eyeDistance;

   public static Analyzer getAnalyzerPerField() throws IOException {
      Map<String, Analyzer> analyzerMap = new HashMap<>();
      Analyzer custom = CustomAnalyzer.builder().withTokenizer(StandardTokenizerFactory.class)
            .addTokenFilter(LowerCaseFilterFactory.class)
            .addTokenFilter(StopFilterFactory.class, "ignoreCase", "false")
            .build();
      analyzerMap.put("name", custom);
      analyzerMap.put("metadata", new KeywordAnalyzer());
      return new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzerMap);
   }

   public Document toDocument() {
      Document document = new Document();
      if (name != null) {
         document.add(new TextField("name", name, Store.YES));
      }
      if (email != null) {
         document.add(new StringField("email", email, Store.YES));
      }
      if (age != null) {
         document.add(new NumericDocValuesField("age", age));
      }
      if (company != null) {
         document.add(new StringField("company", company, Store.YES));
      }
      if (metadata != null) {
         document.add(new StoredField("metadata", metadata));
      }
      if (height != null) {
         document.add(new FloatPoint("height", height));
      }
      if (latitude != null && longitude != null) {
         document.add(new LatLonDocValuesField("location", latitude, longitude));
      }
      if (eyeDistance != null) {
         document.add(new DoublePoint("eyeDistance", eyeDistance));
      }
      System.out.println("Created document " + document);
      return document;
   }
}
