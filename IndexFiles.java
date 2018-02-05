package search;

import static search.Database.*;
import java.sql.*;


public class IndexFiles {
	private IndexFiles() {}
	public static void main(String[] args) {
		

		System.out.println("Connecting database...");

		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
		    System.out.println("Database connected!");
		} catch (SQLException e) {
		    throw new IllegalStateException("Cannot connect the database!", e);
		}
	}
}