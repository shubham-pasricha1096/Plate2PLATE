import javax.swing.*;
import java.awt.*;

public class ProfileScreen extends JPanel {
    public ProfileScreen(int userId, String role) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header
        JLabel titleLabel = new JLabel("Profile", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Profile Panel
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new GridBagLayout());
        profilePanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Profile Icon
        ImageIcon profileIcon = new ImageIcon(new ImageIcon("profile.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
        JLabel profilePic = new JLabel(profileIcon);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        profilePanel.add(profilePic, gbc);

        gbc.gridheight = 1;
        gbc.gridx = 1;

        // Fetch user from DB
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByIdAndRole(userId, role);

        if (user != null) {
            // Labels
            JLabel nameLabel = new JLabel("Name: " + user.getName());
            JLabel emailLabel = new JLabel("Email: " + user.getEmail());
            JLabel locationLabel = new JLabel("Location: " + user.getLocation());
            JLabel roleLabel = new JLabel("Role: " + role);
            JLabel certLabel = null;

            if (role.equalsIgnoreCase("NGO")) {
                certLabel = new JLabel("Govt. Certificate: " + user.getCertificate());
            }

            // Add Labels to Grid
            gbc.gridx = 1;
            gbc.gridy = 0;
            profilePanel.add(nameLabel, gbc);
            gbc.gridy++;
            profilePanel.add(emailLabel, gbc);
            gbc.gridy++;
            profilePanel.add(locationLabel, gbc);
            gbc.gridy++;
            profilePanel.add(roleLabel, gbc);

            if (certLabel != null) {
                gbc.gridy++;
                profilePanel.add(certLabel, gbc);
            }

        } else {
            profilePanel.add(new JLabel("User information could not be loaded."));
        }

        // Edit Button
        JButton editButton = new JButton("Edit Profile");
        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 2;
        profilePanel.add(editButton, gbc);

        // Add to main layout
        add(titleLabel, BorderLayout.NORTH);
        add(profilePanel, BorderLayout.CENTER);
    }
}
