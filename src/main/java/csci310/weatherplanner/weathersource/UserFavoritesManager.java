package csci310.weatherplanner.weathersource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import csci310.weatherplanner.auth.User;
import csci310.weatherplanner.auth.UserManager;
import csci310.weatherplanner.db.Database;

public class UserFavoritesManager implements IFavoritesManager {
	private final static String selectFavoritesQuery = 
			"SELECT locations.name "
			+ "FROM locations "
			+ "INNER JOIN favorites ON favorites.location_id = locations.id "
			+ "INNER JOIN users ON users.id = favorites.user_id AND "
			+ "users.id = ?";
	
	private final static String selectFavoriteQuery = 
			"SELECT locations.name "
			+ "FROM locations "
			+ "INNER JOIN favorites ON favorites.location_id = locations.id AND locations.name = ? "
			+ "INNER JOIN users ON users.id = favorites.user_id AND "
			+ "users.id = ?";
	
	private final static String insertFavSQL = "INSERT INTO favorites (user_id, location_id) VALUES (?, ?);";
	
	private final static String insertLocSQL = "INSERT INTO locations (name) VALUES (?);";
	
	private final static String selectLocSQL = "SELECT locations.id "
			+ "FROM locations "
			+ "WHERE locations.name = ?;";
	
	private final static String removeFavSQL = "DELETE FROM favorites "
			+ "WHERE user_id = ? AND  location_id = ?;";
	
	private final UserManager userManager;

	public UserFavoritesManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	@Override
	public List<String> getFavorites(HttpSession session) {
		User user = userManager.getCurrentLogIn(session);
		List<String> favorites = new ArrayList<String>();
		
		
		if(user == null)
			return favorites;
		
		try {
			PreparedStatement getFavorites = Database.getPreparedStatement(selectFavoritesQuery);
			getFavorites.setInt(1, user.getUserId());
			ResultSet results = getFavorites.executeQuery();
			
			while(results.next()) {
				favorites.add(results.getString(1));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return favorites;
	}

	@Override
	public void labelFavorites(HttpSession session, List<WeatherLocation> locations) {
		for(WeatherLocation loc: locations) {
			loc.setFavorite(isFavorite(session, loc.getCity() + "," +loc.getCountry()));
		}
		
	}

	@Override
	public void labelFavorites(HttpSession session, WeatherLocation... locations) {
		labelFavorites(session, Arrays.asList(locations));
	}

	@Override
	public boolean isFavorite(HttpSession session, String loc) {
		User user = userManager.getCurrentLogIn(session);
		String normalLoc = loc.split("/")[0];
		
		if(user == null)
			return false;
		
		try {
			PreparedStatement getFavorites = Database.getPreparedStatement(selectFavoriteQuery);
			getFavorites.setString(1, normalLoc);
			getFavorites.setInt(2, user.getUserId());
			ResultSet results = getFavorites.executeQuery();
			
			return results.next();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean addFavorite(HttpSession session, String loc) {
		User user = userManager.getCurrentLogIn(session);
		if(user == null)
			return false;
		
		int locId = -1;
		try {
			locId = getLocationId(loc);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(locId == -1)
			return false;
		
		try {
			PreparedStatement insertFavorite = Database.getPreparedStatement(insertFavSQL);
			insertFavorite.setInt(1, user.getUserId());
			insertFavorite.setInt(2, locId);
			
			if(insertFavorite.executeUpdate() != 1)
				return false;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public boolean removeFavorite(HttpSession session, String loc) {
		User user = userManager.getCurrentLogIn(session);
		if(user == null)
			return false;
		
		int locId = -1;
		try {
			locId = getLocationId(loc);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(locId == -1)
			return false;
		
		try {
			PreparedStatement removeFavorite = Database.getPreparedStatement(removeFavSQL);
			removeFavorite.setInt(1, user.getUserId());
			removeFavorite.setInt(2, locId);
			
			removeFavorite.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	private int getLocationId(String location) throws SQLException {
		PreparedStatement selectLoc = Database.getPreparedStatement(selectLocSQL);

		selectLoc.setString(1, location);
		ResultSet loc = selectLoc.executeQuery();
		
		if(!loc.isClosed()) {
			return loc.getInt(1);
		}
		
		PreparedStatement createLoc = Database.getPreparedStatement(insertLocSQL);
		createLoc.setString(1, location);
		createLoc.executeUpdate();
		
		ResultSet genId = createLoc.getGeneratedKeys();
		
		if(genId.next())
			return genId.getInt(1);
		
		return -1;
	}
}
