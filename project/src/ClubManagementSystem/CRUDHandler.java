package ClubManagementSystem;

import java.sql.Connection;
import java.sql.SQLException;

public interface CRUDHandler {
    void handleRead(Connection con, String menuName) throws SQLException;
    void handleInsert(Connection con, String menuName) throws SQLException;
    void handleUpdate(Connection con, String menuName) throws SQLException;
    void handleDelete(Connection con, String menuName) throws SQLException;
}
