import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainApp extends JFrame {
    private static final Color NAV_BG_COLOR = new Color(51, 51, 51);
    private static final Color NAV_BUTTON_COLOR = new Color(220, 220, 220);
    private static final Font NAV_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    public static RestaurantDashboard restaurantDashboard;

    private final int userId;
    private final String userRole;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);




    public MainApp(int userId, String userRole) {
        this.userId = userId;
        this.userRole = userRole;
        initializeUI();
        setupComponents();

    }

    private void initializeUI() {
        setTitle("FoodBridge - " + userRole + " Dashboard");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1024, 600));

    }

    private void setupComponents() {
        setLayout(new BorderLayout());

        // Navigation Panel
        add(createNavigationPanel(), BorderLayout.WEST);

        // Main Content Panel
        initializeContentPanels();
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createNavigationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(NAV_BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        String[] menuItems = {"Dashboard", "Donate", "Centers", "Profile", "Logout"};
        for (String item : menuItems) {
            JButton btn = createNavigationButton(item);
            panel.add(btn);
            panel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        return panel;
    }

    private JButton createNavigationButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setPreferredSize(new Dimension(150, 45));
        btn.setMaximumSize(new Dimension(150, 45));
        btn.setFont(NAV_FONT);
        btn.setBackground(NAV_BUTTON_COLOR);
        btn.setFocusPainted(false);
        btn.addActionListener(this::handleNavigation);
        return btn;
    }

    private void initializeContentPanels() {
        JPanel dashboardPanel = userRole.equalsIgnoreCase("Restaurant")
                ? new RestaurantDashboard(userId)
                : new NGODashboard(userId);

        mainPanel.add(dashboardPanel, "Dashboard");
        mainPanel.add(new DonateFood(userId), "Donate");

        // ✅ DEBUG: Check if user is loading correctly
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByIdAndRole(userId, userRole);
        System.out.println("Loaded user: " + user);  // Should not be null

        mainPanel.add(new ProfileScreen(userId, userRole), "Profile");

    }

    private void handleNavigation(ActionEvent e) {
        String command = ((JButton) e.getSource()).getText();
        switch (command) {
            case "Logout" -> confirmLogout();
            case "Centers" -> OpenNearbyCenters.openMapWindow();  // ✅ open map window
            default -> cardLayout.show(mainPanel, command);

        }
    }

    private void confirmLogout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            EventQueue.invokeLater(() -> new LoginScreen().setVisible(true));
        }
    }
}
