import javax.swing.*;

public class TrackRequests extends JFrame {
    public TrackRequests() {
        setTitle("Track Requests");
        setSize(400, 300);

        String[] requests = {"Request 1 - Pending", "Request 2 - Completed", "Request 3 - Processing"};
        JList<String> requestList = new JList<>(requests);
        JScrollPane scrollPane = new JScrollPane(requestList);

        add(scrollPane);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}