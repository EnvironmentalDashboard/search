package search;

import static search.Database.*; // database constants
import java.sql.*;
import java.util.*;
import java.nio.file.Paths;
// lucene imports
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.store.*;


public class IndexDatabase {
	private IndexDatabase() {}

	public static void main(String[] args) {
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		try {
			Directory dir = FSDirectory.open(Paths.get("index/"));
			IndexWriter writer = new IndexWriter(dir, iwc);
			try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
				Statement stmt = conn.createStatement();
				try {
					ResultSet rs = stmt.executeQuery("SELECT pid, value FROM cv_image_meta WHERE `key` = 'Message Text' AND value != ''");
					while (rs.next()) {
						Document document = new Document();
						document.add(new StoredField("pid", rs.getString("pid")));
						document.add(new TextField("description", rs.getString("value"), Field.Store.NO)); // Field.Store.NO b/c we dont need to return the description
						writer.addDocument(document);
					}
				} finally {
					stmt.close();
					conn.close();
					writer.close();
				}
			} catch (SQLException e) {
				throw new IllegalStateException("Cannot connect the database!", e);
			}
		} catch (Exception e) {
			//
		}
	}
}