import javax.swing.*;
import java.awt.*;

public class LogoutPage extends JFrame {
    public LogoutPage() {
        setTitle("Logout");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create components
        JLabel messageLabel = new JLabel("Are you sure you want to logout?", SwingConstants.CENTER);
        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        // Add action listeners
        confirmButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(LogoutPage.this, "Logged out successfully!");
            SwingUtilities.invokeLater(() -> {
                new LoginScreen().setVisible(true);
            });
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        // Assemble layout
        add(messageLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
