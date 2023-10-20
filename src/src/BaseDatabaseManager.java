import java.sql.*;

/**
 *
 * @author oguzy
 */
public abstract class BaseDatabaseManager {
    public abstract Connection connect(String username, String password);
    public abstract String[] getList(Connection conn);
    public abstract String[] getList(Connection conn, String listName);
    public abstract void saveList(Connection conn, String listName, String[] items);
    public abstract void removeList(Connection conn, String listName);
}
