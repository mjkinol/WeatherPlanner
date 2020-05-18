package csci310.weatherplanner.auth;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import csci310.weatherplanner.db.Database;

public class UserManager {
	private final static String selectUserSQL = "SELECT id, username, password_hash FROM users WHERE username = ?";
	private final static String createUserSQL = "INSERT INTO users (username, password_hash) VALUES (?, ?);";
	
	private final Map<HttpSession, User> loggedIn;
	
	private static UserManager globalManager = null;
	
	public static UserManager getGlobalUserManager() {
		if(globalManager == null)
			globalManager = new UserManager();
		
		return globalManager;
	}
	
	public UserManager() {
		loggedIn = new HashMap<HttpSession, User>();
	}
	
	public CreateUserResult CreateUser(String username, String password) {
		try {
			User user = getUser(username);
			
			if(user != null) {
				return CreateUserResult.AlreadyExists;
			}
			
			PreparedStatement insertUser = Database.getPreparedStatement(createUserSQL);
			insertUser.setString(1, username);
			insertUser.setString(2, new Hasher().hashPassword(password));
			
			if(insertUser.executeUpdate() != 1)
				return CreateUserResult.Error;
			
			return CreateUserResult.Success;
		} catch (SQLException e) {
			e.printStackTrace();
			return CreateUserResult.Error;
		}
	}
	
	public LogInResult LogIn(String username, String password, HttpSession session) {
		try {			
			
			if(!isAlphaNumeric(username)) {
				return LogInResult.InvalidUsername;
			}
			
			if(!isAlphaNumeric(password)) {
				return LogInResult.InvalidPassword;
			}
			
			User user = getUser(username);
			
			if(user == null)
				return LogInResult.DoesNotExist;
			
			if(!new Hasher().checkPassword(password, user.getPasswordHash())) {
				return LogInResult.IncorrectPassword;
			}
			
			loggedIn.put(session, user);
			
			return LogInResult.Success;
		} catch (SQLException e) {
			e.printStackTrace();
			return LogInResult.Error;
		}
	}
	
	public User getCurrentLogIn(HttpSession session) {
		if(!loggedIn.containsKey(session))
			return null;
		
		return loggedIn.get(session);
	}
	
	private User getUser(String username) throws SQLException {
		PreparedStatement selectUser = Database.getPreparedStatement(selectUserSQL);

		selectUser.setString(1, username);
		ResultSet user = selectUser.executeQuery();
		
		if(user.isClosed()) {
			return null;
		}

		return new User(user.getInt(1), user.getString(2), user.getString(3));
	}
	
	public boolean isAlphaNumeric(String query) {
	    return query.matches("^[a-zA-Z0-9]*$");
	}
}
