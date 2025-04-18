import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.jdatepicker.impl.*;
import java.util.Properties;

public class DonateFood extends JPanel {
    private final int userId;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public DonateFood(int userId) {
        this.userId = userId;
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(450, 350));
        add(formPanel);

        initializeForm(formPanel);
    }

    private void initializeForm(JPanel formPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        Dimension fieldDim = new Dimension(300, 30);

        // Food Type
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Food Type:"), gbc);
        gbc.gridx = 1;
        JTextField foodTypeField = new JTextField();
        foodTypeField.setPreferredSize(fieldDim);
        foodTypeField.setFont(fieldFont);
        foodTypeField.setMargin(new Insets(5, 10, 5, 10));
        foodTypeField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(foodTypeField, gbc);

        // Quantity
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Quantity(kg):"), gbc);
        gbc.gridx = 1;
        JSpinner quantityField = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        quantityField.setFont(fieldFont);
        JComponent editor = quantityField.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
            tf.setFont(fieldFont);
            tf.setPreferredSize(fieldDim);
            tf.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            tf.setMargin(new Insets(5, 10, 5, 10));
        }
        formPanel.add(quantityField, gbc);

        // Expiry Date
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Expiry Date:"), gbc);
        gbc.gridx = 1;
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.setPreferredSize(fieldDim);
        datePicker.getJFormattedTextField().setFont(fieldFont);
        datePicker.getJFormattedTextField().setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(datePicker, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        JTextArea descField = new JTextArea(3, 20);
        descField.setFont(fieldFont);
        descField.setLineWrap(true);
        descField.setWrapStyleWord(true);
        descField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        descField.setMargin(new Insets(5, 10, 5, 10));
        JScrollPane scrollPane = new JScrollPane(descField);
        scrollPane.setPreferredSize(new Dimension(300, 60));
        formPanel.add(scrollPane, gbc);

        // Submit Button
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton submitBtn = new JButton("Submit Donation");
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitBtn.setPreferredSize(new Dimension(200, 35));
        submitBtn.setBackground(new Color(220, 220, 220));
        submitBtn.setForeground(Color.BLACK);
        submitBtn.setFocusPainted(false);
        submitBtn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        formPanel.add(submitBtn, gbc);

        // Action
        submitBtn.addActionListener(e -> {
            String foodType = foodTypeField.getText().trim();
            String description = descField.getText().trim();
            java.util.Date selectedDate = (java.util.Date) datePicker.getModel().getValue();

            if (!validateForm(foodType, selectedDate)) return;

            String expiry = DATE_FORMATTER.format(
                    selectedDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
            );

            submitDonation(
                    foodType,
                    (Integer) quantityField.getValue(),
                    expiry,
                    description
            );
        });
    }

    private boolean validateForm(String foodType, java.util.Date expiryDate) {
        if (foodType.isEmpty()) {
            showError("Food type is required");
            return false;
        }
        if (expiryDate == null) {
            showError("Please select a valid expiry date");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    private void submitDonation(String foodType, int quantity, String expiry, String description) {
        DonationDAO donationDAO = new DonationDAO();
        boolean success = donationDAO.postDonation(userId, foodType, quantity, expiry, description);
        // After successful donation
        JOptionPane.showMessageDialog(null, "Donation submitted successfully!");

// Refresh the dashboard (if it's still open)
        if (MainApp.restaurantDashboard != null) {
            MainApp.restaurantDashboard.loadDonations();
        }


        if (success) {
            JOptionPane.showMessageDialog(this, "Donation submitted successfully!");
            // TODO: Refresh donations list in dashboard and FoodDonation class if needed
        } else {
            JOptionPane.showMessageDialog(this, "Failed to submit donation. Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Custom formatter for JDatePicker
    public static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        @Override
        public Object stringToValue(String text) throws java.text.ParseException {
            try {
                return java.sql.Date.valueOf(LocalDate.parse(text, dateFormatter));
            } catch (DateTimeParseException e) {
                throw new java.text.ParseException("Invalid date format", 0);
            }
        }

        @Override
        public String valueToString(Object value) {
            if (value != null) {
                java.util.Calendar cal = (java.util.Calendar) value;
                return dateFormatter.format(cal.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
            }
            return "";
        }
    }
}
