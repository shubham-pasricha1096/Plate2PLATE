import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RestaurantDashboard extends JPanel {
    private final int restaurantId;
    private JTable donationsTable;
    private DefaultTableModel tableModel;
    private Image backgroundImage;

    public RestaurantDashboard(int restaurantId) {
        this.restaurantId = restaurantId;
        this.backgroundImage = new ImageIcon("C:\\Users\\shubh\\Downloads\\BACKGROUND.png").getImage(); // Set your image path here
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel headerLabel = new JLabel("Restaurant Dashboard - ID: " + restaurantId, SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(headerLabel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel donationsPanel = createDonationsPanel();
        tabbedPane.addTab("My Donations", donationsPanel);

        // "New Donation" tab removed

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createDonationsPanel() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // Draw the background image
            }
        };

        // Top panel with Refresh button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshButton = new JButton("Refresh");
        topPanel.add(refreshButton);
        panel.add(topPanel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"ID", "Food Type", "Quantity", "Expiry Date", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        donationsTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(donationsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Load initial data
        loadDonations();

        // Refresh button action
        refreshButton.addActionListener(e -> loadDonations());

        return panel;
    }

    private JPanel createNewDonationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel foodTypeLabel = new JLabel("Food Type:");
        JTextField foodTypeField = new JTextField(20);

        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField(10);

        JLabel expiryLabel = new JLabel("Expiry Date (dd/MM/yyyy):");
        JTextField expiryField = new JTextField(10);

        JButton submitButton = new JButton("Donate");

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(foodTypeLabel, gbc);
        gbc.gridx = 1;
        panel.add(foodTypeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(quantityLabel, gbc);
        gbc.gridx = 1;
        panel.add(quantityField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(expiryLabel, gbc);
        gbc.gridx = 1;
        panel.add(expiryField, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(submitButton, gbc);

        submitButton.addActionListener(e -> {
            String foodType = foodTypeField.getText();
            String quantityText = quantityField.getText();
            String expiryDate = expiryField.getText();

            if (foodType.isEmpty() || quantityText.isEmpty() || expiryDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }

            try {
                int quantity = Integer.parseInt(quantityText);
                DonationDAO dao = new DonationDAO();
                boolean success = dao.postDonation(restaurantId, foodType, quantity, expiryDate, "Not Provided");

                if (success) {
                    JOptionPane.showMessageDialog(this, "Donation added successfully!");

                    // Clear fields
                    foodTypeField.setText("");
                    quantityField.setText("");
                    expiryField.setText("");

                    // Refresh table
                    loadDonations();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add donation. Please check the inputs.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantity must be a number.");
            }
        });

        return panel;
    }

    public void loadDonations() {
        DonationDAO dao = new DonationDAO();
        List<Object[]> donations = dao.getDonationsByRestaurant(restaurantId, "Available");

        // Clear old data
        tableModel.setRowCount(0);

        // Add new data
        for (Object[] row : donations) {
            tableModel.addRow(row);
        }
    }
}
