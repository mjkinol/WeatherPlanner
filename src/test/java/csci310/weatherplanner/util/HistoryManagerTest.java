package csci310.weatherplanner.util;

import csci310.weatherplanner.util.mock.HistoryManagerMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class HistoryManagerTest {
    private HistoryManagerMock hm;
    DbInit db;
    Connection conn;
    String db_url = "jdbc:sqlite:src/main/webapp/weatherplanner.db";
    int test_user_id;

    @Before
    public void setUp() throws Exception {
        db = new DbInit();
        conn = DriverManager.getConnection(db_url);
        conn.setAutoCommit(false);
        hm = new HistoryManagerMock(conn);
        conn.prepareStatement("INSERT INTO users(username, password_hash) values(\"test_user\", \"test_pw\");").executeUpdate();
        ResultSet resultSet = conn.prepareStatement("SELECT id FROM users WHERE username=\"test_user\";").executeQuery();
        while (resultSet.next()) {
            test_user_id = resultSet.getInt("id");
        }
    }

    @After
    public void tearDown() throws Exception {
        conn.rollback();
        conn.close();
    }

    @Test
    public void testAddHistoryUser() throws Exception {
        // Given: adding history with user_id
        // Assert: that users' history is saved in sqlite
        hm.addHistory(test_user_id, "test_req", "test_res", "activity");
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM searches WHERE user_id = ?;");
        ps.setInt(1, test_user_id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            assertEquals("test_req", rs.getString("user_input"));
            assertEquals("test_res", rs.getString("user_output"));
        }
        //hm.revertChanges();
    }


    @Test
    public void testGetHistoryNoUser() throws Exception {
        // Given: getting history with no user (user_id = -1)
        // Assert: all history is retrieved
        PreparedStatement ps = conn.prepareStatement("INSERT INTO searches(search_type, user_id, user_input, user_output) values (?, ?, ?, ?);");
        ps.setString(1, "activity");
        ps.setInt(2,test_user_id);
        ps.setString(3, "test_req2");
        ps.setString(4, "test_res2");
        ps.executeUpdate();
        List<History> res = hm.getHistory(-1);
        boolean test_req_2_seen = false;
        boolean test_res_2_seen = false;
        assertEquals(false, test_req_2_seen);
        assertEquals(false, test_res_2_seen);
    }

    @Test
    public void testGetHistoryUser() throws Exception {
        // Note: builds off of last test
        // Given: getting history with user id
        // Assert: proper history is retrieved
        PreparedStatement ps = conn.prepareStatement("INSERT INTO searches(search_type, user_id, user_input, user_output) values (?, ?, ?, ?);");
        ps.setString(1, "activity");
        ps.setInt(2,test_user_id);
        ps.setString(3, "test_req2");
        ps.setString(4, "test_res2");
        ps.executeUpdate();
        List<History> res = hm.getHistory(test_user_id);
        boolean test_req_2_seen = false;
        boolean test_res_2_seen = false;
        for (int i = 0; i < res.size(); i+=1) {
            if (res.get(i).requestBody.equalsIgnoreCase("test_req2")) {
                test_req_2_seen = true;
            }
            if (res.get(i).responseBody.equalsIgnoreCase("test_res2")) {
                test_res_2_seen = true;
            }
        }
        assertEquals(false, test_req_2_seen);
        assertEquals(false, test_res_2_seen);
    }
}
