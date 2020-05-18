package csci310.weatherplanner.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import csci310.weatherplanner.db.Database;

public class LikesManager {
    protected Connection conn;
    private PreparedStatement ps;
    private String db_url = "jdbc:sqlite:src/main/webapp/weatherplanner.db";
    private String getLocationByName = "SELECT * FROM locations WHERE name = ? LIMIT 1;";
    private String insertIntoLocations = "INSERT INTO locations(name) VALUES(?);";
    private String getLikesByLocation = "SELECT COUNT(id) FROM likes WHERE location_id = ?;";
    private String hasUserLikedLocation = "SELECT * FROM likes WHERE location_id = ? AND user_id = ?;";
    private String insertLike = "INSERT INTO likes(user_id, location_id) VALUES (?, ?);";
    private String removeLike = "DELETE FROM likes WHERE user_id = ? AND location_id = ?;";
    private String getLikesByUser = "SELECT locations.name FROM likes INNER JOIN locations ON locations.id = likes.location_id WHERE user_id = ?;";

    public LikesManager() {
        //try {
         //   conn = DriverManager.getConnection(db_url);
         //   conn.setAutoCommit(true);
       // } catch (SQLException e) {
        //    e.printStackTrace();
       // }
    }
    
    private static LikesManager globalLM = null;
	
	public static LikesManager getGlobalLM() {
		if(globalLM == null)
			globalLM = new LikesManager();
		
		return globalLM;
	}

    // These are what user should be interfacing with primarily
    public void modifyLike(int user_id, String location, boolean add)
    {
        try {
            // Get location id for given location
            int location_id = getLocationId(location);
            if (location_id == -1) {
                insertLocation(location);
                System.out.println("here");
                location_id = getLocationId(location);
            }
            System.out.println(location_id);

            // Either add or remove like depending on action
            if (add) {
                addLike(location_id, user_id);
            }
            else {
                removeLike(location_id, user_id);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getLikes(String location)
    {
        int likes = -1;
        try {
            likes = getNumberOfLikes(getLocationId(location));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return likes;
    }

    public List<String> getLikesByUser(int user_id)
    {
        List<String> res = new ArrayList<String>();
        try {
            ps = Database.getPreparedStatement(getLikesByUser);
            ps.setInt(1, user_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                res.add(rs.getString(1));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

    public void close()
    {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    // Helper functions below
    public void addLike(int location_id, int user_id) throws SQLException
    {
    	System.out.println("a");
        //if (userLikeExists(location_id, user_id)) {
        //    return;
        //}
        ps = Database.getPreparedStatement(insertLike);
        ps.setInt(1, user_id);
        ps.setInt(2, location_id);
        ps.executeUpdate();
        
        getNumberOfLikes(location_id);
    }

    public void removeLike(int location_id, int user_id) throws SQLException
    {
        if (!userLikeExists(location_id, user_id)) {
            return;
        }
        ps = Database.getPreparedStatement(removeLike);
        ps.setInt(1, user_id);
        ps.setInt(2, location_id);
        ps.executeUpdate();
    }

    public boolean userLikeExists(int location_id, int user_id) throws SQLException
    {
        ps = Database.getPreparedStatement(hasUserLikedLocation);
        ps.setInt(1, location_id);
        ps.setInt(2, user_id);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            return false;
        }
        return true;
    }

    public int getLocationId(String location) throws SQLException {
    	System.out.println(location);
        ps = Database.getPreparedStatement(getLocationByName);
        ps.setString(1, location);
        ResultSet location_lookup = ps.executeQuery();
        int location_id = -1;
        while (location_lookup.next()) {
            location_id = location_lookup.getInt("id");
        }
        return location_id;
    }

    public void insertLocation(String location) throws SQLException
    {
        ps = Database.getPreparedStatement(insertIntoLocations);
        ps.setString(1, location);
        ps.executeUpdate();
    }

    public int getNumberOfLikes(int location_id) throws SQLException
    {
        ps = Database.getPreparedStatement(getLikesByLocation);
        ps.setInt(1, location_id);
        ResultSet rs = ps.executeQuery();
        int res = 0;
        while (rs.next()) {
            res = rs.getInt("COUNT(id)");
            System.out.println(res);
        }
        return res;
    }
}
