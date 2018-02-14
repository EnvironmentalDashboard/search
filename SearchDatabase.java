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
			Query query = parser.parse(args.length > 0 ? args[0] : "");
			TopDocs results = searcher.search(query, args.length > 1 ? Integer.parseInt(args[1]) : 20);
			ScoreDoc[] hits = results.scoreDocs;
			for (int i = 0; i < hits.length; i++) {
				if (i != 0) {
					System.out.print(",");
				}
				int doc_id = hits[i].doc;
				Document d = searcher.doc(doc_id);
				System.out.print(d.get("pid"));
			}
			reader.close();
		} catch (Exception e) {
			//
		}
	}
}