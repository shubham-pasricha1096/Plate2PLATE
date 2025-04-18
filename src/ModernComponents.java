import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ModernComponents {

    public static class ModernButton extends JButton {
        public ModernButton(String text) {
            super(text);
            setBackground(new Color(0, 150, 136));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }
    }

    public static class RoundBorder implements Border {
        private final int radius;
        private final Color color;

        public RoundBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
        }

        public boolean isBorderOpaque() {
            return true;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(color);
            g.drawRoundRect(x, y, width-1, height-1, radius, radius);
        }
    }
}