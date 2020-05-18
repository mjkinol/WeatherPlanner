package csci310.weatherplanner.auth;

import org.mindrot.jbcrypt.BCrypt;

public class Hasher {
    public String hashPassword(String password)
    {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    public boolean checkPassword(String password, String hash)
    {
        return BCrypt.checkpw(password, hash);
    }
}