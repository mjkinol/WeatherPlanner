package csci310.weatherplanner.util.mock;

import csci310.weatherplanner.util.LikesManager;

import java.sql.Connection;
import java.sql.SQLException;

public class LikesManagerMock extends LikesManager {
    public LikesManagerMock(Connection input_conn)
    {
        super();
        try {
        	if(conn != null)
        		conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn = input_conn;
    }
}
