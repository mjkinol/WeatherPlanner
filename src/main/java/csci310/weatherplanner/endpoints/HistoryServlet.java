package csci310.weatherplanner.endpoints;

import csci310.weatherplanner.auth.User;
import csci310.weatherplanner.auth.UserManager;
import csci310.weatherplanner.util.DbInit;
import csci310.weatherplanner.util.History;
import csci310.weatherplanner.util.HistoryManager;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HistoryServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
    {
        HistoryManager hm = HistoryManager.getGlobalHM();
        PrintWriter out;
        int user_id;
        UserManager um = UserManager.getGlobalUserManager();
        User user = um.getCurrentLogIn(request.getSession());
        user_id = -1;
        if(user != null) {
        	user_id = user.getUserId();
        	//System.out.println(user.getUsername());
        }
        else {
        	//System.out.println("null");
        }
        List<History> res = hm.getHistory(user_id);
        if(res == null) {
        	try {
				out = response.getWriter();  
				out.print("NoUser");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        ArrayList<JSONObject> responseArray;
        try {
            responseArray = new ArrayList<JSONObject>();
            JSONObject row;
            for (int i = 0 ; i < res.size(); i+=1) {
                row = new JSONObject();
                String resp = res.get(i).requestBody;
                String[] respSplit = resp.split(",");
                row.append("location", respSplit[0]);
                row.append("unit", respSplit[1]);
                responseArray.add(row);
            }
            out = response.getWriter();
            //System.out.println((new JSONArray(responseArray)).toString());
            out.print((new JSONArray(responseArray)).toString());
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
    {
        try {
        	HistoryManager hm = HistoryManager.getGlobalHM();
            UserManager um = UserManager.getGlobalUserManager();
            User user = um.getCurrentLogIn(request.getSession());
            if (user == null) {
                response.setStatus(404);
                return;
            }
            int user_id = user.getUserId();
            if (request.getParameter("location") == null) {
                response.setStatus(404);
                return;
            }
            hm.addHistory(user_id, request.getParameter("location"), " ", "WEATHER");
            return;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
