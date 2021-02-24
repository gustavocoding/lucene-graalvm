package org.infinispan;

import java.io.IOException;

public class App {
   public static void main(String[] args) throws IOException {
      System.out.println("Starting app");
      LuceneBackend luceneBackend = new LuceneBackend();
      luceneBackend.basicIndexAndSearch();
   }
}
