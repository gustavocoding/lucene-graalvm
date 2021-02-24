package org.infinispan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SimpleFSDirectory;

/**
 * Helper class to exercise Lucene API
 */
public class LuceneBackend {

    Directory createDirectory(Class<? extends Directory> directory, String folder) throws IOException {
        if (directory == RAMDirectory.class) {
            return new RAMDirectory();
        }
        if (directory == ByteBuffersDirectory.class) {
            return new ByteBuffersDirectory();
        }
        if (FSDirectory.class.isAssignableFrom(directory)) {
            Path path = Paths.get(folder);
            if (directory == SimpleFSDirectory.class) {
                return new SimpleFSDirectory(path.resolve("simple"));
            }
            if (directory == NIOFSDirectory.class) {
                return new NIOFSDirectory(path.resolve("nio"));
            }
            if (directory == MMapDirectory.class) {
                return new MMapDirectory(path.resolve("mmap"));
            }
        }
        throw new IllegalArgumentException("Director class " + directory + " not found");
    }

    public void basicIndexAndSearch() throws IOException {
        basicIndexAndSearch(RAMDirectory.class);
        basicIndexAndSearch(ByteBuffersDirectory.class);
        basicIndexAndSearch(SimpleFSDirectory.class);
        basicIndexAndSearch(NIOFSDirectory.class);
        basicIndexAndSearch(MMapDirectory.class);
    }

    private void basicIndexAndSearch(Class<? extends Directory> directoryClass) throws IOException {
        Analyzer standardAnalyzer = Person.getAnalyzerPerField();
        Path luceneDir = Files.createTempDirectory("lucene");
        Directory directory = createDirectory(directoryClass, luceneDir.toString());
        IndexWriterConfig config = new IndexWriterConfig(standardAnalyzer);

        // Write two documents and close the index writer
        IndexWriter writer = new IndexWriter(directory, config);

        Person p = new PersonBuilder().withName("Bruce Wayne").withAge(54).withMetadata("553d26b58f840").withHeight(1.8f)
                .withLatitude(54.2).withLongitude(0.02).getPerson();
        writer.addDocument(p.toDocument());

        p = new PersonBuilder().withName("Clark Kent").withAge(35).withMetadata("f5db284a30247d0").withHeight(1.7f)
                .withEmail("a@b.com").withEyeDistance(0.06d)
                .getPerson();
        writer.addDocument(p.toDocument());

        writer.close();

        // Open index reader
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);

        // Search by term with results
        Query query = new TermQuery(new Term("name", "bruce"));
        TopDocs results = searcher.search(query, 5);
        assertSingleResult(reader, results, "name", "Bruce Wayne");

        // Empty result
        query = new TermQuery(new Term("name", "Diana"));
        results = searcher.search(query, 5);
        assertEmptyResults(results);
    }

    private void assertEmptyResults(TopDocs results) {
        assert results.totalHits.value == 0;
    }

    private void assertSingleResult(IndexReader reader, TopDocs results, String field, String value)
            throws IOException {
        assert results.totalHits.value == 1;
        Document result = reader.document(results.scoreDocs[0].doc);
        assert result.get(field).equals(value);
    }
}
