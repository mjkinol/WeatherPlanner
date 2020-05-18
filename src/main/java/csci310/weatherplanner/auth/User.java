package csci310.weatherplanner.auth;


public class User {
	private final int userId;
	private final String username;
	private final String passwordHash;
	
    public User(int userId, String username, String passwordHash) {
    	this.userId = userId;
    	this.username = username;
    	this.passwordHash = passwordHash;
    }
    
    @Override
    public boolean equals(Object other) {
    	if(!(other instanceof User))
    		return false;
    	
    	User otherUser = (User) other;
    	
    	return userId == otherUser.userId && username == otherUser.username;
    }
    
    @Override
    public int hashCode() {
    	return userId;
    }
    
    public int getUserId() {
    	return userId;
    }

    public String getUsername() {
    	return username;
    }

	public String getPasswordHash() {
		return passwordHash;
	}
}
