import javax.swing.*;
import java.awt.*;

public class UIFactory {
    public static void setModernLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            UIManager.put("controlFont", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        } catch (Exception e) {
            System.err.println("Failed to set modern look and feel.");
        }
    }
}
