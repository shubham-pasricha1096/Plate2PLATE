import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

public class RegistrationScreen extends JFrame {
    private static final Logger logger = Logger.getLogger(RegistrationScreen.class.getName());
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private JTextField nameField, emailField, locationField, certificateField;
    private JPasswordField passwordField, confirmPasswordField;
    private JComboBox<String> roleComboBox;
    private JLabel locationLabel, certificateLabel;
    private JButton refreshLocationBtn;

    public RegistrationScreen() {
        setTitle("Food Donation App - Register");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeComponents();
    }

    private void initializeComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Components
        JLabel nameLabel = new JLabel("Name:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        JLabel roleLabel = new JLabel("Role:");
        locationLabel = new JLabel("Location:");
        certificateLabel = new JLabel("Govt. Certificate:");

        nameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        roleComboBox = new JComboBox<>(new String[]{"Restaurant", "NGO"});
        locationField = new JTextField(20);
        certificateField = new JTextField(20);
        refreshLocationBtn = new JButton("Refresh");

        JButton registerButton = new JButton("Register");
        JButton cancelButton = new JButton("Cancel");

        // Layout
        gbc.gridx = 0; gbc.gridy = 0; panel.add(nameLabel, gbc);
        gbc.gridx = 1; panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(emailLabel, gbc);
        gbc.gridx = 1; panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(passwordLabel, gbc);
        gbc.gridx = 1; panel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(confirmPasswordLabel, gbc);
        gbc.gridx = 1; panel.add(confirmPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panel.add(roleLabel, gbc);
        gbc.gridx = 1; panel.add(roleComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 5; panel.add(locationLabel, gbc);
        gbc.gridx = 1;
        JPanel locationPanel = new JPanel(new BorderLayout());
        locationPanel.add(locationField, BorderLayout.CENTER);
        locationPanel.add(refreshLocationBtn, BorderLayout.EAST);
        panel.add(locationPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 6; panel.add(certificateLabel, gbc);
        gbc.gridx = 1; panel.add(certificateField, gbc);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, gbc);

        // Event Listeners
        roleComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                updateFieldVisibility();
            }
        });

        refreshLocationBtn.addActionListener(e -> fetchAndSetLocation());
        registerButton.addActionListener(this::handleRegistration);
        cancelButton.addActionListener(e -> dispose());

        add(panel);
        updateFieldVisibility();
    }

    private void updateFieldVisibility() {
        String selectedRole = (String) roleComboBox.getSelectedItem();
        boolean isRestaurant = "Restaurant".equals(selectedRole);

        locationLabel.setVisible(isRestaurant);
        locationField.setVisible(isRestaurant);
        refreshLocationBtn.setVisible(isRestaurant);
        certificateLabel.setVisible(!isRestaurant);
        certificateField.setVisible(!isRestaurant);

        if (isRestaurant) {
            fetchAndSetLocation();
        }
    }

    private void fetchAndSetLocation() {
        try {
            String locationJson = LocationFetcher.fetchCityFromIP();
            if (locationJson.startsWith("{")) {
                JSONObject json = new JSONObject(locationJson);
                String city = json.optString("city", "Unknown Location");
                String country = json.optString("country", "");
                locationField.setText(city + (!country.isEmpty() ? ", " + country : ""));
            } else {
                locationField.setText(locationJson);
            }
            locationField.setEditable(false);
        } catch (Exception ex) {
            locationField.setText("Unknown Location");
            locationField.setEditable(true);
            JOptionPane.showMessageDialog(this,
                    "Could not automatically detect location. Please enter manually.",
                    "Location Service",
                    JOptionPane.WARNING_MESSAGE);
            logger.log(Level.WARNING, "Location fetch failed", ex);
        }
    }

    private void handleRegistration(ActionEvent e) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
        String role = (String) roleComboBox.getSelectedItem();
        String location = locationField.getText().trim();
        String certificate = certificateField.getText().trim();

        if (!validateInput(name, email, password, confirmPassword, location, certificate, role)) {
            return;
        }

        UserDAO userDAO = new UserDAO();
        if (userDAO.registerUser(name, email, password, role, location, certificate)) {
            JOptionPane.showMessageDialog(this, "Registration successful!");
            dispose();
            new LoginScreen().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Registration failed! Email might be already in use.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInput(String name, String email, String password,
                                  String confirmPassword, String location,
                                  String certificate, String role) {
        if (name.isEmpty() || name.length() > 100) {
            showError("Name must be between 1-100 characters");
            return false;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            showError("Invalid email format");
            return false;
        }

        if (password.length() < 8) {
            showError("Password must be at least 8 characters");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return false;
        }

        if ("Restaurant".equals(role) && location.isEmpty()) {
            showError("Location is required for restaurants");
            return false;
        }

        if ("NGO".equals(role) && certificate.isEmpty()) {
            showError("Government certificate is required for NGOs");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistrationScreen().setVisible(true));
    }
}