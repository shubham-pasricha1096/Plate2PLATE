import javax.swing.*;
import java.awt.*;

public class Profile extends JFrame {
    public Profile() {
        setTitle("Profile");
        setSize(400, 300);
        setLayout(new GridLayout(6, 1));

        JTextField txtNgoName = new JTextField();
        JTextField txtContactPerson = new JTextField();
        JTextField txtPhone = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtAddress = new JTextField();

        JButton btnUpdate = new JButton("Update Profile");
        btnUpdate.addActionListener(e -> JOptionPane.showMessageDialog(this, "Profile Updated!"));

        add(new JLabel("NGO Name:"));
        add(txtNgoName);
        add(new JLabel("Contact Person:"));
        add(txtContactPerson);
        add(new JLabel("Phone Number:"));
        add(txtPhone);
        add(new JLabel("Email:"));
        add(txtEmail);
        add(new JLabel("Address:"));
        add(txtAddress);
        add(btnUpdate);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}