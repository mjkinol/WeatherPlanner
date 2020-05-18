package csci310.weatherplanner.util;

import csci310.weatherplanner.db.Database;
import csci310.weatherplanner.util.mock.LikesManagerMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LikesManagerTest {
    LikesManagerMock lm;
    Connection conn;
    String db_url = "jdbc:sqlite:src/main/webapp/weatherplanner.db";
    int test_user_id;
    int user_2;

    @Before
    public void setUp() throws Exception {
        //conn = DriverManager.getConnection(db_url);
        //conn.setAutoCommit(false);
        lm = new LikesManagerMock(LikesManager.getGlobalLM().conn);
    	Database.getPreparedStatement("INSERT INTO users(username, password_hash) values(\"test_user\", \"test_pw\");").executeUpdate();
        ResultSet resultSet = Database.getPreparedStatement("SELECT id FROM users WHERE username=\"test_user\";").executeQuery();
        while (resultSet.next()) {
            test_user_id = resultSet.getInt("id");
        }
    }

    @After
    public void tearDown() throws Exception
    {
        //conn.rollback();
        //conn.close();
    }

    @Test
    public void testModifyLike() throws Exception
    {
        lm.modifyLike(test_user_id, "San Jose TEST123", true);
        List<String> user_likes = lm.getLikesByUser(test_user_id);
        assertEquals(1, user_likes.size());
    }

    @Test
    public void testGetLikesByUser() throws Exception
    {
        lm.modifyLike(test_user_id,"San Jose TEST123", true );
        assertEquals(1, lm.getLikesByUser(test_user_id).size());
        assertEquals("San Jose TEST123", lm.getLikesByUser(test_user_id).get(0));
    }

    @Test
    public void testUserLikeExists() throws Exception
    {
        lm.modifyLike(test_user_id, "San Jose TEST123", false);
        assertEquals(false, lm.userLikeExists(lm.getLocationId("San Jose TEST123"), test_user_id));
    }
}
