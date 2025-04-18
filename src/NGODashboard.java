import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NGODashboard extends JPanel {
    private final int ngoId;
    private final DonationDAO donationDAO;
    private JTable donationTable;

    public NGODashboard(int ngoId) {
        this.ngoId = ngoId;
        this.donationDAO = new DonationDAO();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Create BackgroundPanel to show the background image
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());

        // Create tabs
        JTabbedPane tabbedPane = new JTabbedPane();

        // Available Donations Tab
        JPanel donationsPanel = createDonationsPanel();
        tabbedPane.addTab("Available Donations", donationsPanel);

        // Search Tab
        JPanel searchPanel = createSearchPanel();
        tabbedPane.addTab("Search Donations", searchPanel);

        // Add tabbedPane to the backgroundPanel
        backgroundPanel.add(tabbedPane, BorderLayout.CENTER);

        // Add the backgroundPanel to the NGODashboard
        add(backgroundPanel, BorderLayout.CENTER);
    }

    private JPanel createDonationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Get donations from DAO
        List<Object[]> donations = donationDAO.getAvailableDonations();

        // Column names
        String[] columnNames = {"ID", "Food Type", "Quantity", "Expiry Date", "Location"};

        // Table model (non-editable)
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Add data to model
        for (Object[] donation : donations) {
            model.addRow(donation);
        }

        // Create table with model
        donationTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(donationTable);

        // Enquire Button
        JButton enquireButton = new JButton("Enquire");
        enquireButton.addActionListener(e -> {
            int selectedRow = donationTable.getSelectedRow();
            if (selectedRow != -1) {
                String location = donationTable.getValueAt(selectedRow, 4).toString();
                try {
                    String query = "https://www.google.com/maps/search/?api=1&query=" +
                            java.net.URLEncoder.encode(location, "UTF-8");
                    java.awt.Desktop.getDesktop().browse(java.net.URI.create(query));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Failed to open Maps.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a donation first.");
            }
        });

        // Accept Button
        JButton acceptButton = new JButton("Accept");
        acceptButton.addActionListener(e -> {
            int selectedRow = donationTable.getSelectedRow();
            if (selectedRow != -1) {
                int donationId = Integer.parseInt(donationTable.getValueAt(selectedRow, 0).toString());

                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to accept this donation?",
                        "Confirm", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = donationDAO.deleteDonation(donationId);
                    if (success) {
                        ((DefaultTableModel) donationTable.getModel()).removeRow(selectedRow);
                        JOptionPane.showMessageDialog(this, "Donation accepted and removed.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to accept the donation.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a donation first.");
            }
        });

        // Bottom Panel with buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(enquireButton);
        bottomPanel.add(acceptButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Search");
        JTextArea resultsArea = new JTextArea();
        resultsArea.setEditable(false);

        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            List<Object[]> results = donationDAO.searchDonations(keyword);
            StringBuilder sb = new StringBuilder();
            for (Object[] donation : results) {
                sb.append(String.format(
                        "ID: %s, Food: %s, Qty: %s, Expiry: %s, Location: %s\n\n",
                        donation[0], donation[1], donation[2], donation[3], donation[4]
                ));
            }
            resultsArea.setText(sb.toString());
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("Search:"), BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(resultsArea), BorderLayout.CENTER);

        return panel;
    }
}
