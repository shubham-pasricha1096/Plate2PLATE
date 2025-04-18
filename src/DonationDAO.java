import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DonationDAO {
    private static final Logger logger = Logger.getLogger(DonationDAO.class.getName());

    public List<Object[]> getDonationsByRestaurant(int restaurantId, String status) {
        List<Object[]> donations = new ArrayList<>();
        String query = "SELECT donation_id, food_type, quantity, expiry_date, status " +
                "FROM Food_Donations WHERE restaurant_id = ? AND status = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, restaurantId);
            pstmt.setString(2, status);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] donation = new Object[5];
                    donation[0] = rs.getInt("donation_id");
                    donation[1] = rs.getString("food_type");
                    donation[2] = rs.getInt("quantity");
                    donation[3] = rs.getDate("expiry_date");
                    donation[4] = rs.getString("status");
                    donations.add(donation);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching donations for restaurant: " + restaurantId, e);
        }
        return donations;
    }

    public boolean postDonation(int restaurantId, String foodType, int quantity,
                                String expiry, String location) {
        String query = "INSERT INTO Food_Donations " +
                "(restaurant_id, food_type, quantity, expiry_date, pickup_location, status) " +
                "VALUES (?, ?, ?, ?, ?, 'Available')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            LocalDate expiryDate = LocalDate.parse(expiry, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            pstmt.setInt(1, restaurantId);
            pstmt.setString(2, foodType);
            pstmt.setInt(3, quantity);
            pstmt.setDate(4, Date.valueOf(expiryDate));
            pstmt.setString(5, location);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (DateTimeParseException e) {
            logger.log(Level.WARNING, "Invalid date format: " + expiry, e);
            return false;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error posting donation", e);
            return false;
        }
    }

    public List<Object[]> searchDonations(String keyword) {
        List<Object[]> donations = new ArrayList<>();
        String query = "SELECT donation_id, food_type, quantity, expiry_date, pickup_location " +
                "FROM Food_Donations WHERE status = 'Available' AND food_type LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, "%" + keyword + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] donation = new Object[5];
                    donation[0] = rs.getInt("donation_id");
                    donation[1] = rs.getString("food_type");
                    donation[2] = rs.getInt("quantity");
                    donation[3] = rs.getDate("expiry_date");
                    donation[4] = rs.getString("pickup_location");
                    donations.add(donation);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error searching donations with keyword: " + keyword, e);
        }

        return donations;
    }

    public List<Object[]> getAvailableDonations() {
        List<Object[]> donations = new ArrayList<>();
        String query = "SELECT donation_id, food_type, quantity, expiry_date, pickup_location " +
                "FROM Food_Donations WHERE status = 'Available'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Object[] donation = new Object[5];
                donation[0] = rs.getInt("donation_id");
                donation[1] = rs.getString("food_type");
                donation[2] = rs.getInt("quantity");
                donation[3] = rs.getDate("expiry_date");
                donation[4] = rs.getString("pickup_location");
                donations.add(donation);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching available donations", e);
        }

        return donations;
    }

    public String getLocationByDonationId(int donationId) {
        String location = "";
        String query = "SELECT pickup_location FROM Food_Donations WHERE donation_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, donationId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    location = rs.getString("pickup_location");
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching location for donation ID: " + donationId, e);
        }

        return location;
    }

    public boolean deleteDonation(int donationId) {
        String query = "DELETE FROM Food_Donations WHERE donation_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, donationId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting donation ID: " + donationId, e);
            return false;
        }
    }
}
