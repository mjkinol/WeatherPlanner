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

import csci310.weatherplanner.auth.LogInResult;
import csci310.weatherplanner.auth.UserManager;

public class LogInServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private final UserManager userManager;
	private final JsonParser parser;
	
	public LogInServlet() {
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
		
		LogInResult result = userManager.LogIn(username, password, session);
		
		switch(result) {
		case DoesNotExist:
			response.setStatus(404); // 404: Does not exist
			break;
		case InvalidUsername:
			response.setStatus(405);
			break;
		case InvalidPassword:
			response.setStatus(406);
			break;
		case IncorrectPassword:
			response.setStatus(401); // 401: Unauthorized
			break;
		case Success:
			response.setStatus(201); // 201: Log in Created
			break;
		case Error:
		default:
			response.setStatus(500); // 500: ServerError
			break;
		
		}
	}
}