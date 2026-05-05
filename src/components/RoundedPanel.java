package quizapplication.src.components;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {

    private final int radius;
    private final Color background;
    private final boolean shadow;

    public RoundedPanel(int radius, Color background, boolean shadow) {
        this.radius     = radius;
        this.background = background;
        this.shadow     = shadow;
        setOpaque(false);
        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        if (shadow) {
            for (int i = 4; i >= 1; i--) {
                g2.setColor(new Color(0, 0, 0, 8 * i));
                g2.fillRoundRect(i, i + 1, w - i * 2, h - i * 2, radius, radius);
            }
        }

        g2.setColor(background);
        g2.fillRoundRect(0, 0, w - 1, h - 1, radius, radius);
        g2.dispose();
    }
}
