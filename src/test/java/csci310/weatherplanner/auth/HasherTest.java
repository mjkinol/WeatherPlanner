package csci310.weatherplanner.auth;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class HasherTest {
    private Hasher hasher;
    private String example_pw;

    @Before
    public void setUp() throws Exception {
        hasher = new Hasher();
        example_pw = "Hellopw123_";
    }

    @Test
    public void testHashPassword() {
        // Given: a pw in plain text
        // Expect: pw to be hashed without existence of original pw in hash
        String hash = hasher.hashPassword(example_pw);
        assertFalse(hash.contains(example_pw));
        assertTrue(hash.length() >= 1);
    }

    @Test
    public void testCheckPassword() {
        // Given: a plain text password
        // Expect: when hashed using hashPassword method, the hashed password and original plain text password
        //         return true when checked against each other
        String hash = hasher.hashPassword(example_pw);
        assertTrue(hasher.checkPassword(example_pw, hash));
    }
}
