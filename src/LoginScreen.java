import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.regex.Pattern;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;

public class LoginScreen extends JFrame {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private Image backgroundImage;

    public LoginScreen() {
        initializeUI();
        setupComponents();
    }

    private void initializeUI() {
        setTitle("Food Donation App - Login");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Load the background image
        try {
            backgroundImage = ImageIO.read(new File("C:\\Users\\shubh\\Downloads\\BACKGROUND.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupComponents() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Email
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        panel.add(emailField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);

        // Role
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        roleComboBox = new JComboBox<>(new String[]{"Restaurant", "NGO"});
        panel.add(roleComboBox, gbc);

        // Login Button
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        panel.add(loginButton, gbc);

        // Register Button
        gbc.gridy = 4;
        JButton registerButton = new JButton("Register");
        panel.add(registerButton, gbc);

        loginButton.addActionListener(this::handleLogin);
        registerButton.addActionListener(e -> {
            new RegistrationScreen().setVisible(true);
            dispose();
        });

        add(panel);
    }

    private void handleLogin(ActionEvent e) {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = (String) roleComboBox.getSelectedItem();

        if (!validateInput(email, password)) {
            return;
        }

        UserDAO userDAO = new UserDAO();
        String[] userInfo = userDAO.loginUser(email, password, role);

        if (userInfo != null) {
            try {
                int userId = Integer.parseInt(userInfo[2]);
                SwingUtilities.invokeLater(() -> {
                    if (role.equalsIgnoreCase("NGO")) {
                        new MainApp2(userId).setVisible(true); // NGO Dashboard
                    } else {
                        new MainApp(userId, role).setVisible(true); // Restaurant Dashboard
                    }
                    dispose();
                });
            } catch (NumberFormatException ex) {
                showError("Invalid user ID format.");
            }
        } else {
            showError("Invalid credentials. Please try again.");
        }
    }

    private boolean validateInput(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            showError("Please fill all fields.");
            return false;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            showError("Invalid email format.");
            return false;
        }

        if (password.length() < 6) {
            showError("Password must be at least 6 characters.");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Login Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UIFactory.setModernLookAndFeel(); // Optional
            new LoginScreen().setVisible(true);
        });
    }
}
