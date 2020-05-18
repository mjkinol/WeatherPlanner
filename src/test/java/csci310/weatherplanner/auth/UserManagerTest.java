package csci310.weatherplanner.auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import csci310.weatherplanner.db.Database;
import csci310.weatherplanner.weathersource.mock.MockSession;

public class UserManagerTest {
	private UserManager testManager;

	@BeforeClass
	public static void beforeAll() {
		Database.resetDB();
	}
	
	@Before
	public void setUp() throws Exception {
		testManager = new UserManager();
	}

	@Test
	public void testCreateUserSuccess() {
		HttpSession session = new MockSession("abc");
		
		CreateUserResult result = testManager.CreateUser("drewc", "pass123");
		assertEquals(CreateUserResult.Success, result);
	}
	
	@Test
	public void testCreateUserDuplicate() {
		HttpSession session = new MockSession("abc");
		
		testManager.CreateUser("drew1", "pass123");
		CreateUserResult result = testManager.CreateUser("drew1", "pass456");
		assertEquals(CreateUserResult.AlreadyExists, result);
	}
	
	@Test
	public void testCreateUserMultiple() {
		HttpSession session = new MockSession("abc");
		
		testManager.CreateUser("mark", "pass123");
		testManager.CreateUser("mike", "pass123");
		CreateUserResult result = testManager.CreateUser("brighton", "pass123");
		assertEquals(CreateUserResult.Success, result);
	}

	@Test
	public void testLogInDoesNotExist() {
		HttpSession session = new MockSession("abc");
		
		LogInResult result = testManager.LogIn("NOTEXIST", "pass", session);
		
		assertEquals(LogInResult.DoesNotExist, result);
	}
	
	@Test
	public void testLogInIncorrectPassword() {
		HttpSession session = new MockSession("abc");
		
		testManager.CreateUser("drew", "pass123");
		
		LogInResult result = testManager.LogIn("drew", "WRONGPASSWORD", session);
		
		assertEquals(LogInResult.IncorrectPassword, result);
	}
	
	@Test
	public void testLogInSuccess() {
		HttpSession session = new MockSession("abc");
		
		testManager.CreateUser("drew", "pass123");
		
		LogInResult result = testManager.LogIn("drew", "pass123", session);
		
		assertEquals(LogInResult.Success, result);
	}

	@Test
	public void testGetCurrentLogInNoLogIn() {
		HttpSession session = new MockSession("abc");
		
		User user = testManager.getCurrentLogIn(session);
		
		assertNull(user);
	}
	
	@Test
	public void testGetCurrentLogInSuccess() {
		HttpSession session = new MockSession("abc");
		
		testManager.CreateUser("drew", "pass123");
		testManager.LogIn("drew", "pass123", session);
		
		User user = testManager.getCurrentLogIn(session);
		
		assertNotNull(user);
		assertEquals("drew", user.getUsername());
	}

}
