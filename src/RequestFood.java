import javax.swing.*;
import java.awt.*;

public class RequestFood extends JFrame {
    public RequestFood() {
        setTitle("Request Food");
        setSize(400, 300);
        setLayout(new GridLayout(5, 1));

        JTextField txtFoodType = new JTextField();
        JTextField txtQuantity = new JTextField();
        JTextField txtLocation = new JTextField();
        JTextArea txtNotes = new JTextArea();

        JButton btnSubmit = new JButton("Submit Request");
        btnSubmit.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Food Request Submitted!");
            dispose();
        });

        add(new JLabel("Food Type:"));
        add(txtFoodType);
        add(new JLabel("Quantity:"));
        add(txtQuantity);
        add(new JLabel("Pickup Location:"));
        add(txtLocation);
        add(new JLabel("Additional Notes:"));
        add(txtNotes);
        add(btnSubmit);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}