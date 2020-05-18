package csci310.weatherplanner.util.mock;

import csci310.weatherplanner.util.HistoryManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HistoryManagerMock extends HistoryManager {
    public HistoryManagerMock()
    {
        try {
            conn = DriverManager.getConnection(db_url);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public HistoryManagerMock(Connection input_conn)
    {
        super();
        conn = input_conn;
    }

    public void revertChanges() throws Exception {
        conn.rollback();
    }
}
