package org.infinispan;

import java.util.Map;

import org.apache.lucene.analysis.charfilter.MappingCharFilterFactory;
import org.apache.lucene.analysis.core.KeywordTokenizerFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.core.UpperCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenizerFactory;
import org.apache.lucene.analysis.ngram.NGramFilterFactory;
import org.apache.lucene.analysis.path.PathHierarchyTokenizerFactory;
import org.apache.lucene.analysis.pattern.PatternReplaceCharFilterFactory;
import org.apache.lucene.analysis.pattern.PatternTokenizerFactory;
import org.apache.lucene.analysis.shingle.ShingleFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.apache.lucene.analysis.synonym.SynonymGraphFilterFactory;
import org.apache.lucene.analysis.util.CharFilterFactory;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeReflection;

import com.oracle.svm.core.annotate.AutomaticFeature;
import com.oracle.svm.util.ReflectionUtil;

@AutomaticFeature
class ReflectionRegistration implements Feature {

   public void beforeAnalysis(BeforeAnalysisAccess access) {
      try {
         RuntimeReflection.register(ReflectionUtil.lookupConstructor(StandardTokenizerFactory.class, Map.class));
         RuntimeReflection.register(ReflectionUtil.lookupConstructor(LowerCaseFilterFactory.class, Map.class));
         RuntimeReflection.register(ReflectionUtil.lookupConstructor(EdgeNGramTokenizerFactory.class, Map.class));
         RuntimeReflection.register(ReflectionUtil.lookupConstructor(PathHierarchyTokenizerFactory.class, Map.class));
         RuntimeReflection.register(ReflectionUtil.lookupConstructor(WhitespaceTokenizerFactory.class, Map.class));
         RuntimeReflection.register(ReflectionUtil.lookupConstructor(PatternTokenizerFactory.class, Map.class));
         RuntimeReflection.register(ReflectionUtil.lookupConstructor(KeywordTokenizerFactory.class, Map.class));
         RuntimeReflection.register(ReflectionUtil.lookupConstructor(NGramFilterFactory.class, Map.class));
         RuntimeReflection.register(ReflectionUtil.lookupConstructor(EdgeNGramFilterFactory.class, Map.class));
         RuntimeReflection.register(ReflectionUtil.lookupConstructor(ShingleFilterFactory.class, Map.class));
         RuntimeReflection.register(ReflectionUtil.lookupConstructor(StopFilterFactory.class, Map.class));
         RuntimeReflection.register(ReflectionUtil.lookupConstructor(SynonymGraphFilterFactory.class, Map.class));
         RuntimeReflection.register(ReflectionUtil.lookupConstructor(UpperCaseFilterFactory.class, Map.class));
         RuntimeReflection.register(ReflectionUtil.lookupConstructor(CharFilterFactory.class, Map.class));
         RuntimeReflection.register(ReflectionUtil.lookupConstructor(MappingCharFilterFactory.class, Map.class));
         RuntimeReflection.register(ReflectionUtil.lookupConstructor(PatternReplaceCharFilterFactory.class, Map.class));
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}