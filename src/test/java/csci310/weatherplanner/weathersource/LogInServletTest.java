package csci310.weatherplanner.weathersource;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;

import org.junit.BeforeClass;
import org.junit.Test;

import csci310.weatherplanner.auth.UserManager;
import csci310.weatherplanner.endpoints.LogInServlet;
import csci310.weatherplanner.endpoints.mock.MockRequest;
import csci310.weatherplanner.endpoints.mock.MockResponse;
import csci310.weatherplanner.weathersource.mock.MockSession;

public class LogInServletTest {

	private static LogInServlet servlet;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		UserManager globalManager = UserManager.getGlobalUserManager();
		globalManager.CreateUser("drew", "pass123");
		servlet = new LogInServlet();
	}
	
	@Test
	public void successfulLogIn() {
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
		
		assertEquals(201, response.getStatus());
	}
	
	@Test
	public void invalidPost() {
		MockRequest request = new MockRequest(new MockSession("mock"), 
				new HashMap<String, Object>(), 
				new HashMap<String, String[]>(), 
				"{username: \"drew\", WRONG_KEY: \"pass123\"}");
		
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
	
	@Test
	public void userDoesNotExist() {
		MockRequest request = new MockRequest(new MockSession("mock"), 
				new HashMap<String, Object>(), 
				new HashMap<String, String[]>(), 
				"{username: \"NOTEXIST\", password: \"pass123\"}");
		
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
		
		assertEquals(404, response.getStatus());
	}
	
	@Test
	public void incorrectPassword() {
		MockRequest request = new MockRequest(new MockSession("mock"), 
				new HashMap<String, Object>(), 
				new HashMap<String, String[]>(), 
				"{username: \"drew\", password: \"WRONGPASS\"}");
		
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
		
		assertEquals(401, response.getStatus());
	}
}
