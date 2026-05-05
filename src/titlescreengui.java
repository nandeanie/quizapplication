package quizapplication.src;

import javax.swing.*;

import quizapplication.src.components.RoundedPanel;
import quizapplication.src.components.StyledButton;
import quizapplication.src.components.StyledTextField;
import quizapplication.src.constants.commonConstants;
import quizapplication.src.database.JDBC;
import quizapplication.src.database.category;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class titlescreengui extends JFrame {

    private JComboBox<String> categoriesMenu;
    private StyledTextField   numofquestionstTextField;

    public titlescreengui() {
        super("Quiz Game — General Knowledge");
        setSize(440, 620);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(commonConstants.SURFACE);
        addGuicomponents();
    }

    private void addGuicomponents() {

        // ── Header bar ────────────────────────────────────────
        JPanel header = new JPanel(null);
        header.setBounds(0, 0, 440, 72);
        header.setBackground(commonConstants.PRIMARY);
        add(header);

        JLabel appIcon = new JLabel("🧠");
        appIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        appIcon.setBounds(20, 18, 40, 36);
        header.add(appIcon);

        JLabel appTitle = new JLabel("QuizMaster");
        appTitle.setFont(commonConstants.FONT_HEADING);
        appTitle.setForeground(Color.WHITE);
        appTitle.setBounds(64, 22, 200, 28);
        header.add(appTitle);

        JLabel tagline = new JLabel("General Knowledge Edition");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tagline.setForeground(new Color(255, 255, 255, 180));
        tagline.setBounds(64, 46, 220, 16);
        header.add(tagline);

        // ── Main card ─────────────────────────────────────────
        RoundedPanel card = new RoundedPanel(16, commonConstants.CARD, true);
        card.setBounds(24, 96, 392, 440);
        add(card);

        JLabel welcomeLabel = new JLabel("Ready to play?");
        welcomeLabel.setFont(commonConstants.FONT_TITLE);
        welcomeLabel.setForeground(commonConstants.TEXT_PRIMARY);
        welcomeLabel.setBounds(24, 24, 340, 34);
        card.add(welcomeLabel);

        JLabel subLabel = new JLabel("Choose a category and start your quiz.");
        subLabel.setFont(commonConstants.FONT_LABEL);
        subLabel.setForeground(commonConstants.TEXT_SECONDARY);
        subLabel.setBounds(24, 62, 340, 18);
        card.add(subLabel);

        JSeparator sep1 = new JSeparator();
        sep1.setBounds(24, 90, 344, 1);
        sep1.setForeground(commonConstants.BORDER);
        card.add(sep1);

        JLabel catLabel = new JLabel("CATEGORY");
        catLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        catLabel.setForeground(commonConstants.TEXT_SECONDARY);
        catLabel.setBounds(24, 108, 340, 16);
        card.add(catLabel);

        ArrayList<String> categoryList = JDBC.getcategories();
        categoriesMenu = new JComboBox<>(categoryList != null ? categoryList.toArray(new String[0]) : new String[0]);
        categoriesMenu.setFont(commonConstants.FONT_BODY);
        categoriesMenu.setForeground(commonConstants.TEXT_PRIMARY);
        categoriesMenu.setBackground(Color.WHITE);
        categoriesMenu.setBounds(24, 130, 344, 40);
        categoriesMenu.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(commonConstants.BORDER, 1, true),
            BorderFactory.createEmptyBorder(0, 8, 0, 8)
        ));
        card.add(categoriesMenu);

        JLabel numLabel = new JLabel("NUMBER OF QUESTIONS");
        numLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        numLabel.setForeground(commonConstants.TEXT_SECONDARY);
        numLabel.setBounds(24, 188, 340, 16);
        card.add(numLabel);

        numofquestionstTextField = new StyledTextField("10");
        numofquestionstTextField.setHorizontalAlignment(SwingConstants.CENTER);
        numofquestionstTextField.setBounds(24, 210, 344, 42);
        card.add(numofquestionstTextField);

        JLabel hintLabel = new JLabel("Enter any number — we'll cap it to available questions.");
        hintLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hintLabel.setForeground(commonConstants.TEXT_HINT);
        hintLabel.setBounds(24, 258, 344, 16);
        card.add(hintLabel);

        JSeparator sep2 = new JSeparator();
        sep2.setBounds(24, 284, 344, 1);
        sep2.setForeground(commonConstants.BORDER);
        card.add(sep2);

        StyledButton startButton = new StyledButton("▶   Start Quiz", StyledButton.Variant.PRIMARY);
        startButton.setFont(commonConstants.FONT_BUTTON);
        startButton.setHorizontalAlignment(SwingConstants.CENTER);
        startButton.setBounds(24, 300, 344, 48);
        startButton.addActionListener(e -> {
            if (validateInput()) {
                category cat = JDBC.getCategory(categoriesMenu.getSelectedItem().toString());
                if (cat == null) {
                    JOptionPane.showMessageDialog(this, "Category not found in database.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int num = Integer.parseInt(numofquestionstTextField.getText().trim());
                qiuzscreengui quizScreen = new qiuzscreengui(cat, num);
                quizScreen.setLocationRelativeTo(this);
                this.dispose();
                quizScreen.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields correctly.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            }
        });
        card.add(startButton);

        StyledButton createBtn = new StyledButton("✏   Create a Question", StyledButton.Variant.OUTLINE);
        createBtn.setFont(commonConstants.FONT_BUTTON);
        createBtn.setHorizontalAlignment(SwingConstants.CENTER);
        createBtn.setBounds(24, 360, 344, 44);
        createBtn.addActionListener(e -> {
            createquestionscreengui cqs = new createquestionscreengui();
            cqs.setLocationRelativeTo(this);
            this.dispose();
            cqs.setVisible(true);
        });
        card.add(createBtn);

        StyledButton exitBtn = new StyledButton("✕   Exit", StyledButton.Variant.DANGER);
        exitBtn.setFont(commonConstants.FONT_BUTTON);
        exitBtn.setHorizontalAlignment(SwingConstants.CENTER);
        exitBtn.setBounds(24, 416, 344, 44);
        exitBtn.addActionListener(e -> this.dispose());
        card.add(exitBtn);

        JLabel footer = new JLabel("QuizMaster v1.0  •  General Knowledge", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(commonConstants.TEXT_HINT);
        footer.setBounds(0, 560, 440, 20);
        add(footer);
    }

    private boolean validateInput() {
        String txt = numofquestionstTextField.getText().trim();
        if (txt.isEmpty()) return false;
        try { if (Integer.parseInt(txt) <= 0) return false; }
        catch (NumberFormatException e) { return false; }
        return categoriesMenu.getSelectedItem() != null;
    }
}
