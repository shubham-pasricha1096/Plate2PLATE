import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainApp2 extends JFrame {
    private static final Color NAV_BG_COLOR = new Color(51, 51, 51);
    private static final Color NAV_BUTTON_COLOR = new Color(220, 220, 220);
    private static final Font NAV_FONT = new Font("Segoe UI", Font.PLAIN, 16);

    private final int userId;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);

    public MainApp2(int userId) {
        this.userId = userId;
        initializeUI();
        setupComponents();
    }

    private void initializeUI() {
        setTitle("FoodBridge - NGO Dashboard");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1024, 600));
    }

    private void setupComponents() {
        setLayout(new BorderLayout());

        // Navigation Panel on the left side
        add(createNavigationPanel(), BorderLayout.WEST);

        // Main Content Panels: we include NGODashboard and ProfileScreen
        // For NGO, NGODashboard is the default dashboard
        JPanel dashboardPanel = new NGODashboard(userId);
        mainPanel.add(dashboardPanel, "Dashboard");

        // NGO Profile panel
        mainPanel.add(new ProfileScreen(userId, "NGO"), "Profile");

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createNavigationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(NAV_BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        // For NGO, the navigation items are Dashboard, Centers, Profile, and Logout.
        String[] menuItems = {"Dashboard", "Centers", "Profile", "Logout"};
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

    private void handleNavigation(ActionEvent e) {
        String command = ((JButton) e.getSource()).getText();
        switch (command) {
            case "Logout":
                confirmLogout();
                break;
            case "Centers":
                OpenNearbyCenters.openMapWindow();
                break;
            default:
                cardLayout.show(mainPanel, command);
                break;
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
            SwingUtilities.invokeLater(() -> new LoginScreen().setVisible(true));
        }
    }

    public static void main(String[] args) {
        // For testing, launch the NGO dashboard with a sample userId (e.g., 100).
        SwingUtilities.invokeLater(() -> new MainApp2(100).setVisible(true));
    }
}
