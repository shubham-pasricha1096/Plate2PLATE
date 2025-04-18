import java.sql.*;

public class UserDAO {

    public String[] loginUser(String email, String password, String role) {
        String table = role.equalsIgnoreCase("Restaurant") ? "Restaurants" : "NGOs";
        String query = "SELECT id, email, password FROM " + table + " WHERE email = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String dbPassword = rs.getString("password");
                if (dbPassword.equals(password)) {
                    return new String[]{email, role, String.valueOf(rs.getInt("id"))};
                }
            }

        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
        }

        return null;
    }

    public User getUserByIdAndRole(int userId, String role) {
        String table = role.equalsIgnoreCase("Restaurant") ? "Restaurants" : "NGOs";
        String query = "SELECT id, name, email, location, certificate FROM " + table + " WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("location"),
                        rs.getString("certificate")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error fetching user profile: " + e.getMessage());
        }

        return null;
    }

    public boolean registerUser(String name, String email, String password, String role, String location, String certificate) {
        String table = role.equalsIgnoreCase("Restaurant") ? "Restaurants" : "NGOs";
        String sql = "INSERT INTO " + table + " (name, email, password, location, certificate) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, location);
            stmt.setString(5, certificate);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Registration error: " + e.getMessage());
            return false;
        }
    }
}
