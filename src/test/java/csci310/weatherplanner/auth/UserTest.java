package csci310.weatherplanner.auth;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserTest {
	private static User user1;
	private static User user2;

	@BeforeClass
	public static void setUpBeforeClass() {
		user1 = new User(1, "drew", "PASSHASH");
		user2 = new User(1, "drew", "PASSHASH");
	}
	
	@Test
	public void testHashCodeEqual() {
		assertEquals(user1.hashCode(), user2.hashCode());
	}
	
	@Test
	public void testHashCodeNotEqual() {
		assertNotEquals(user1.hashCode(), new User(2, "drew", "PASSHASH").hashCode());
	}

	@Test
	public void testEqualsEqual() {
		assertEquals(user1, user2);
	}
	
	@Test
	public void testEqualsNotEqual() {
		assertNotEquals(user1, new User(2, "drew", "PASSHASH"));
		assertNotEquals(user1, new User(1, "drew2", "PASSHASH"));
	}

	@Test
	public void testEqualsNull() {
		assertNotEquals(user1, null);
	}
	
	@Test
	public void testEqualsWrongType() {
		assertNotEquals(user1, "hello");
	}
}
