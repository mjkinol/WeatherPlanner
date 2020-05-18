package csci310.weatherplanner.endpoints;

import csci310.weatherplanner.auth.User;
import csci310.weatherplanner.auth.UserManager;
import csci310.weatherplanner.util.DbInit;
import csci310.weatherplanner.util.LikesManager;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class LikesServlet extends HttpServlet {
    private Connection conn;
    private String db_url = "jdbc:sqlite:./weather.db";
    private String insertNewLikeString = "";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
    {
        LikesManager lm = new LikesManager();
        UserManager um = UserManager.getGlobalUserManager();
        User user = um.getCurrentLogIn(request.getSession());
        if (user == null) {
            response.setStatus(404);
            return;
        }
        try {
            PrintWriter out = response.getWriter();
            String request_type = request.getParameter("req_type");
            if (request_type.equalsIgnoreCase("hasUserLikedLocation")) {
                int user_id = Integer.parseInt(request.getParameter("user_id"));
                String location = request.getParameter("location").replaceAll("\\+", " ");
                boolean hasLiked = lm.userLikeExists(lm.getLocationId(location), user_id);
                JSONObject output = new JSONObject();
                output.append("location", location);
                output.append("hasLiked", hasLiked);
                out.print(output.toString());
            }
            else if (request_type.equalsIgnoreCase("getLocationLikes")) {
                String location = request.getParameter("location").replaceAll("\\+", " ");
                int num_likes = lm.getNumberOfLikes(lm.getLocationId(location));
                JSONObject output = new JSONObject();
                output.append("location", location);
                output.append("numLikes", num_likes);
                out.print(output.toString());
            }
            else if (request_type.equalsIgnoreCase("getLocationsUserHasLiked")) {
                int user_id = user.getUserId();
                List<String> result = lm.getLikesByUser(user_id);
                JSONObject output = new JSONObject();
                output.append("user_id", user_id);
                output.append("locations", new JSONArray(result));
                out.print(output.toString());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        lm.close();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
    {
        StringBuilder jsonBuff = new StringBuilder();
        String line = null;
        UserManager um = UserManager.getGlobalUserManager();
        User u = um.getCurrentLogIn(request.getSession());
        if (u == null) {
            response.setStatus(404);
            return;
        }
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jsonBuff.append(line);
            }
        } catch (Exception e) { /*error*/ }

        System.out.println("Request JSON string :" + jsonBuff.toString());

        try {
            JSONObject jsonObject = new JSONObject();
            int user_id = u.getUserId();
            String location = jsonBuff.toString();
            System.out.println(location);
            boolean add = true;
            LikesManager lm = LikesManager.getGlobalLM();
            lm.modifyLike(user_id, location, add);
            //lm.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
