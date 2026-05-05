package quizapplication.src.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class StyledTextArea extends JTextArea {

    private boolean focused = false;

    public StyledTextArea() {
        setOpaque(false);
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(Color.decode("#0F172A"));
        setLineWrap(true);
        setWrapStyleWord(true);
        setBorder(new EmptyBorder(10, 14, 10, 14));
        setBackground(Color.WHITE);

        addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { focused = true;  repaint(); }
            @Override public void focusLost(FocusEvent e)   { focused = false; repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();
        int arc = 8;

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, w, h, arc, arc);

        g2.setColor(focused ? Color.decode("#2563EB") : Color.decode("#CBD5E1"));
        g2.setStroke(new BasicStroke(focused ? 2f : 1.5f));
        g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);

        g2.dispose();
        super.paintComponent(g);
    }
}
