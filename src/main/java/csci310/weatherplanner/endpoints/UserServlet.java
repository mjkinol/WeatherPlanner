package csci310.weatherplanner.endpoints;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import csci310.weatherplanner.auth.CreateUserResult;
import csci310.weatherplanner.auth.UserManager;

public class UserServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private final UserManager userManager;
	private final JsonParser parser;
	
	public UserServlet() {
		super();
		userManager = UserManager.getGlobalUserManager();
        parser = new JsonParser();
	}  
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		
		JsonObject postJson = parser.parse(request.getReader()).getAsJsonObject();
		
		if(!postJson.has("username") || !postJson.has("password")) {
			response.setStatus(400);
			return;
		}
		
		String username = postJson.get("username").getAsString();
		String password = postJson.get("password").getAsString();
		
		CreateUserResult result = userManager.CreateUser(username, password);
		
		if(result == CreateUserResult.Success) {
			userManager.LogIn(username, password, session);
			response.setStatus(201); // 201: Created
			return;
		}
		else if(result == CreateUserResult.AlreadyExists) {
			response.setStatus(409); // 409: Conflict
			return;
		}
		
		response.setStatus(500); //500:Server Error
	}
}
