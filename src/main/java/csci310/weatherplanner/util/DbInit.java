package csci310.weatherplanner.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbInit {
    private Connection conn;
    private String db_url = "jdbc:sqlite:src/main/webapp/weatherplanner.db";
    public DbInit()
    {
        try {
            conn = DriverManager.getConnection(db_url);
            conn.setAutoCommit(true);
            conn.prepareStatement("CREATE TABLE IF NOT EXISTS users (" +
                    "	id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "	username VARCHAR(255) NOT NULL," +
                    "   password_hash VARCHAR(255) NOT NULL" +
                    ");").executeUpdate();
            conn.prepareStatement("CREATE TABLE IF NOT EXISTS searches (" +
                    "	id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "	search_type VARCHAR(255) NOT NULL," +
                    "	user_input TEXT NOT NULL," +
                    "	user_output TEXT NOT NULL," +
                    "	user_id INTEGER," +
                    "	FOREIGN KEY(user_id) REFERENCES users(id)" +
                    ");").executeUpdate();
            conn.prepareStatement("CREATE TABLE IF NOT EXISTS favorites (" +
                    "	id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "	user_id INTEGER NOT NULL," +
                    "	location_id INTEGER NOT NULL," +
                    "	search_id INTEGER," +
                    "	FOREIGN KEY(location_id) REFERENCES locations(id)," +
                    "	FOREIGN KEY(search_id) REFERENCES searches(id)" +
                    ");").executeUpdate();
            conn.prepareStatement("CREATE TABLE IF NOT EXISTS locations (" +
                    "	id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "	name VARCHAR(255) NOT NULL" +
                    ");").executeUpdate();
            conn.prepareStatement("CREATE TABLE IF NOT EXISTS likes (" +
                    "	id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "	user_id INTEGER NOT NULL," +
                    "	location_id INTEGER NOT NULL," +
                    "	FOREIGN KEY(user_id) REFERENCES users(id)," +
                    "	FOREIGN KEY(location_id) REFERENCES locations(id)" +
                    ");").executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
