package csci310.weatherplanner.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import csci310.weatherplanner.auth.UserManager;
import csci310.weatherplanner.db.Database;

public class HistoryManager {
    protected Connection conn;
    protected String db_url = "jdbc:sqlite:src/main/webapp/weatherplanner.db";
    private String getHistoryNoUser = "SELECT * FROM searches;";
    private String getHistoryUser = "SELECT * FROM searches WHERE user_id = ?;";
    private String insertHistoryNoUser = "INSERT INTO searches(search_type, user_input, user_output) values(?, ?, ?);";
    private String insertHistoryUser = "INSERT INTO searches(search_type, user_input, user_output, user_id) values(?, ?, ?, ?);";

    public HistoryManager()
    {
        //try {
        //    conn = DriverManager.getConnection(db_url);
        //} catch (SQLException e) {
        //    System.out.println(e.getMessage());
        //}

    }
    
	private static HistoryManager globalHM = null;
		
	public static HistoryManager getGlobalHM() {
		if(globalHM == null)
			globalHM = new HistoryManager();
		
		return globalHM;
	}

    public void addHistory(int user_id, String request_body, String response_body, String search_type) {
        try {
            PreparedStatement ps;
            if (user_id == -1) {
                ps = Database.getPreparedStatement(insertHistoryNoUser);
                ps.setString(1, search_type);
                ps.setString(2, request_body);
                ps.setString(3, response_body);
            }
            else {
                ps = Database.getPreparedStatement(insertHistoryUser);
                ps.setString(1, search_type);
                ps.setString(2, request_body);
                ps.setString(3, response_body);
                ps.setInt(4, user_id);
            }
            ps.executeUpdate();
            System.out.println("Good");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<History> getHistory(int user_id)
    {
        PreparedStatement ps;
        ArrayList<History> res = new ArrayList<History>();
        try {
            if (user_id == -1) {
                res = null;
                return res;
            }
            else {
                ps = Database.getPreparedStatement(getHistoryUser);
                ps.setInt(1, user_id);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                History h = new History(rs.getString("user_input"), rs.getString("user_output"));
                //System.out.println(h.requestBody);
                //System.out.println(h.responseBody);
                res.add(h);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return res;
    }
}
