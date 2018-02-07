package search;

import java.util.*;
import java.nio.file.Paths;
import java.io.IOException;
// lucene imports
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;


public class SearchDatabase {
	private SearchDatabase() {}

	public static void main(String[] args) throws IOException {
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("index/")));
    IndexSearcher searcher = new IndexSearcher(reader);
    Analyzer analyzer = new StandardAnalyzer();
    QueryParser parser = new QueryParser("description", analyzer);
    try {
    	Query query = parser.parse("oberlin");
    	TopDocs results = searcher.search(query, 5);
	    ScoreDoc[] hits = results.scoreDocs;
	    for (int i = 0; i < 5; i++) {
	    	System.out.println(hits[i]);
	    }
    } catch (Exception e) {
    	//
    }
	}
}