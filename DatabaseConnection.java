import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection {
    private static Connection connection;
    private DatabaseConnection() {
    }
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = "jdbc:sqlite:identifier.sqlite";
            connection = DriverManager.getConnection(url);
        }
        return connection;
    }
}
