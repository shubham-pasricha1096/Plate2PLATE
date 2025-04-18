import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SearchDonationsPage extends JFrame {
    private JTextField searchField;
    private JTextArea resultsArea;
    private DonationDAO donationDAO;

    public SearchDonationsPage(int ngoId) {
        this.donationDAO = new DonationDAO();
        setTitle("Search Donations");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search components
        JLabel searchLabel = new JLabel("Search by Location or Food Type:");
        searchField = new JTextField();
        JButton searchButton = new JButton("Search");

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Results area
        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultsArea);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Search action
        searchButton.addActionListener(e -> performSearch());

        add(panel);
    }

    private void performSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term");
            return;
        }

        // In SearchDonationsPage.java's performSearch() method:
        List<Object[]> results = donationDAO.searchDonations(keyword);
        StringBuilder sb = new StringBuilder();
        for (Object[] donation : results) {
            sb.append("ID: ").append(donation[0])
                    .append(", Food: ").append(donation[1])
                    .append(", Qty: ").append(donation[2])
                    .append(", Expiry: ").append(donation[3])
                    .append(", Location: ").append(donation[4])
                    .append("\n\n");
        }
        resultsArea.setText(sb.toString());
    }
}