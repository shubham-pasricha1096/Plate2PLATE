import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/food_donation";
    private static final String USER = "root";  // Change if needed
    private static final String PASSWORD = "Shubh1096";  // Your actual MySQL password

    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("✅ Database connection successful!");
        } else {
            System.out.println("❌ Failed to connect to the database.");
        }
    }

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  // ✅ Ensure driver is loaded
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found! " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Connection failed! " + e.getMessage());
        }
        return null;
    }
}
