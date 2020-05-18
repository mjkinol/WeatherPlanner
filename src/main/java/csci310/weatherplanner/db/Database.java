package csci310.weatherplanner.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import csci310.weatherplanner.auth.Hasher;

public class Database {
	private static String url = "jdbc:sqlite:src/main/webapp/weatherplanner.db";
	private static Connection conn = null;
	
//	query strings for drops 
	private static String dropFavorites = "DROP TABLE IF EXISTS favorites;";
	private static String dropSearches = "DROP TABLE IF EXISTS searches;";
	private static String dropLikes = "DROP TABLE IF EXISTS likes;";
	private static String dropLocations = "DROP TABLE IF EXISTS locations;";
	private static String dropUsers = "DROP TABLE IF EXISTS users;";
	
//	query strings for creates
	private static String createFavorites = "CREATE TABLE IF NOT EXISTS favorites (" + 
											"	id INTEGER PRIMARY KEY AUTOINCREMENT," + 
											"	user_id INTEGER NOT NULL," + 
											"	location_id INTEGER NOT NULL," + 
											//"	search_id INTEGER," + 
											"	FOREIGN KEY(location_id) REFERENCES locations(id)" + 
											//"	FOREIGN KEY(search_id) REFERENCES searches(id)" + 
											");";
	private static String createSearches = "CREATE TABLE IF NOT EXISTS searches (" + 
											"	id INTEGER PRIMARY KEY AUTOINCREMENT," + 
											"	search_type VARCHAR(255) NOT NULL," + 
											"	user_input TEXT NOT NULL," + 
											"	user_output TEXT NOT NULL," + 
											"	user_id INTEGER," + 
											"	FOREIGN KEY(user_id) REFERENCES users(id)" + 
											");";
	private static String createLikes = "CREATE TABLE IF NOT EXISTS likes (" + 
										"	id INTEGER PRIMARY KEY AUTOINCREMENT," + 
										"	user_id INTEGER," +
										"	location_id INTEGER NOT NULL," + 
										"	FOREIGN KEY(user_id) REFERENCES users(id)," + 
										"	FOREIGN KEY(location_id) REFERENCES locations(id)" + 
										");";
	private static String createLocations = "CREATE TABLE IF NOT EXISTS locations (" + 
											"	id INTEGER PRIMARY KEY AUTOINCREMENT," + 
											"	name VARCHAR(255) NOT NULL" + 
											");";
	private static String createUsers = "CREATE TABLE IF NOT EXISTS users (" + 
										"	id INTEGER PRIMARY KEY AUTOINCREMENT," + 
										"	username VARCHAR(255) NOT NULL," + 
										"   password_hash VARCHAR(255) NOT NULL" + 
										");";
	
//	query strings for inserts
	private static String insertUsers = "INSERT INTO users (username, password_hash) VALUES (?, ?);";
	
	
	public static PreparedStatement getPreparedStatement(String statement) throws SQLException {
		if(conn == null)
			getConnection();
		
		return conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
	}
	
	public static void dropAll() {
		if(conn != null)
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		try {
			Connection dropConn = DriverManager.getConnection(url);
			
			dropConn.prepareStatement(dropFavorites).executeUpdate();
			dropConn.prepareStatement(dropSearches).executeUpdate();
			dropConn.prepareStatement(dropLikes).executeUpdate();
			dropConn.prepareStatement(dropLocations).executeUpdate();
			dropConn.prepareStatement(dropUsers).executeUpdate();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static void initDB() {
		getConnection();
		
		try {
			conn.prepareStatement(createUsers).executeUpdate();
			
			conn.prepareStatement(createSearches).executeUpdate(); 
			
			conn.prepareStatement(createFavorites).executeUpdate();
			
			conn.prepareStatement(createLocations).executeUpdate(); 
			
			conn.prepareStatement(createLikes).executeUpdate();
			
			PreparedStatement createDummyAccount = conn.prepareStatement(insertUsers);
			createDummyAccount.setString(1, "account1");
			createDummyAccount.setString(2, new Hasher().hashPassword("pass123"));
			createDummyAccount.executeUpdate();
						
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void getConnection(){
		if(conn != null)
			return;
		
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void resetDB() {
		dropAll();
		initDB();
	}
	
	public static void main(String[] args) {
		resetDB();
	}
}
