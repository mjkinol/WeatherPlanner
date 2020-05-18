package csci310.weatherplanner.endpoints;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import csci310.weatherplanner.auth.UserManager;
import csci310.weatherplanner.weathersource.FavoritesManager;
import csci310.weatherplanner.weathersource.IFavoritesManager;
import csci310.weatherplanner.weathersource.UserFavoritesManager;

public class FavoritesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IFavoritesManager favoritesManager;
	private final Gson gson;

	public FavoritesServlet() {
		super();
		favoritesManager = new UserFavoritesManager(UserManager.getGlobalUserManager());
        gson = new Gson();        
	}  
    
    public FavoritesServlet(IFavoritesManager fm) {
    	super();
        gson = new Gson();        
    	favoritesManager = fm;
    }
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		List<String> currFavoritesList = favoritesManager.getFavorites(session);
		out.print(gson.toJson(currFavoritesList));
	    out.flush();
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		
		String city = request.getReader().readLine(); 
		if (!(city==null)){ 
			favoritesManager.addFavorite(session, city.replace('+', ' '));
			out.print(gson.toJson(favoritesManager.getFavorites(session)));
			out.flush();
		}
		else {
			response.setStatus(400);
			return;
		}
	}
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		String city = request.getReader().readLine().replace('+', ' ');
		if (!(city==null)) {
			favoritesManager.removeFavorite(session,city);
			out.print(gson.toJson(favoritesManager.getFavorites(session)));
			out.flush();
		}
		else {
			response.setStatus(400);
			return;
		}
	}

}
