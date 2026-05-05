package quizapplication.src.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StyledButton extends JButton {

    public enum Variant { PRIMARY, OUTLINE, DANGER, ANSWER }

    private final Variant variant;
    private boolean hovered = false;
    private boolean pressed = false;
    private Color overrideBackground = null;

    public StyledButton(String text, Variant variant) {
        super(text);
        this.variant = variant;
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFont(new Font("Segoe UI", Font.BOLD, 15));
        setHorizontalAlignment(SwingConstants.LEFT);

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e)  { hovered = true;  repaint(); }
            @Override public void mouseExited(MouseEvent e)   { hovered = false; repaint(); }
            @Override public void mousePressed(MouseEvent e)  { pressed = true;  repaint(); }
            @Override public void mouseReleased(MouseEvent e) { pressed = false; repaint(); }
        });
    }

    public void setFeedbackColor(Color bg) {
        this.overrideBackground = bg;
        repaint();
    }

    public void clearFeedbackColor() {
        this.overrideBackground = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();
        int arc = 10;
        Color bg, fg;

        if (overrideBackground != null) {
            bg = overrideBackground;
            fg = Color.WHITE;
        } else {
            switch (variant) {
                case PRIMARY -> {
                    bg = pressed ? Color.decode("#1D4ED8") : hovered ? Color.decode("#3B82F6") : Color.decode("#2563EB");
                    fg = Color.WHITE;
                }
                case OUTLINE -> {
                    bg = hovered ? Color.decode("#EFF6FF") : Color.WHITE;
                    fg = Color.decode("#2563EB");
                }
                case DANGER -> {
                    bg = hovered ? Color.decode("#DC2626") : Color.decode("#EF4444");
                    fg = Color.WHITE;
                }
                default -> {
                    bg = hovered ? Color.decode("#EFF6FF") : Color.WHITE;
                    fg = Color.decode("#0F172A");
                }
            }
        }

        if (variant == Variant.ANSWER && overrideBackground == null) {
            g2.setColor(new Color(0, 0, 0, 20));
            g2.fillRoundRect(1, 2, w - 2, h - 2, arc, arc);
        }

        g2.setColor(bg);
        g2.fillRoundRect(0, 0, w, h, arc, arc);

        if (variant == Variant.OUTLINE || (variant == Variant.ANSWER && overrideBackground == null)) {
            g2.setColor(Color.decode("#E2E8F0"));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);
        }

        g2.dispose();
        setForeground(fg);
        super.paintComponent(g);
    }
}
