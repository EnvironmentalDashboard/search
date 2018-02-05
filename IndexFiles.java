package search;

import static search.Database.*;
import java.sql.*;
import java.util.*;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public class IndexFiles {
	private IndexFiles() {}

	public static void main(String[] args) {
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		try {
			Directory dir = FSDirectory.open(Paths.get("indices/"));
			IndexWriter writer = new IndexWriter(dir, iwc);
			try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
				Statement stmt = conn.createStatement();
				try {
					ResultSet rs = stmt.executeQuery("SELECT pid, value FROM cv_image_meta WHERE `key` = 'Message Text'");
					while (rs.next()) {
						Document document = new Document();
						document.add(new StringField("pid", rs.getString("pid"), Field.Store.YES));
						document.add(new TextField("description", rs.getString("value"), Field.Store.NO));
						writer.updateDocument(new Term("pid", rs.getString("pid")), document);
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