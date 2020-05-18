package csci310.weatherplanner.weathersource;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import csci310.weatherplanner.auth.UserManager;
import csci310.weatherplanner.db.Database;
import csci310.weatherplanner.endpoints.UserServlet;
import csci310.weatherplanner.endpoints.mock.MockRequest;
import csci310.weatherplanner.endpoints.mock.MockResponse;
import csci310.weatherplanner.weathersource.mock.MockSession;

public class UserServletTest {
	private static UserServlet servlet;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		servlet = new UserServlet();
	}

	
	@Before
	public void setUp() {
		Database.resetDB();
		UserManager globalManager = UserManager.getGlobalUserManager();
		globalManager.CreateUser("drew", "pass123");
	}

	@Test
	public void successfulCreate() {
		MockRequest request = new MockRequest(new MockSession("mock"), 
				new HashMap<String, Object>(), 
				new HashMap<String, String[]>(), 
				"{username: \"mike\", password: \"pass123\"}");
		
		MockResponse response = new MockResponse();
		
		try {
			servlet.doPost(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
			fail("Servlet exception!");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO exception!");
		}
		
		assertEquals(201, response.getStatus());
	}
	
	@Test
	public void userAlreadyExists() {
		MockRequest request = new MockRequest(new MockSession("mock"), 
				new HashMap<String, Object>(), 
				new HashMap<String, String[]>(), 
				"{username: \"drew\", password: \"pass123\"}");
		
		MockResponse response = new MockResponse();
		
		try {
			servlet.doPost(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
			fail("Servlet exception!");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO exception!");
		}
		
		assertEquals(409, response.getStatus());
	}
	
	@Test
	public void invalidPost() {
		MockRequest request = new MockRequest(new MockSession("mock"), 
				new HashMap<String, Object>(), 
				new HashMap<String, String[]>(), 
				"{username: \"mike\", WRONG_KEY: \"pass123\"}");
		
		MockResponse response = new MockResponse();
		
		try {
			servlet.doPost(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
			fail("Servlet exception!");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO exception!");
		}
		
		assertEquals(400, response.getStatus());
	}

}
