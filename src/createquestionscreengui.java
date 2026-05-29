package quizapplication.src;

import javax.swing.*;

import quizapplication.src.components.RoundedPanel;
import quizapplication.src.components.StyledButton;
import quizapplication.src.components.StyledTextArea;
import quizapplication.src.components.StyledTextField;
import quizapplication.src.constants.commonConstants;
import quizapplication.src.database.JDBC;

import java.awt.*;
import java.awt.event.*;

public class createquestionscreengui extends JFrame {

    private StyledTextArea    questiontextArea;
    private StyledTextField   categorytextfield;
    private StyledTextField[] answerTextFields;
    private ButtonGroup       buttonGroup;
    private JRadioButton[]    answerRadioButtons;

    public createquestionscreengui() {
        super("Create a Question");
        setSize(900, 620);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(commonConstants.SURFACE);

        answerRadioButtons = new JRadioButton[4];
        answerTextFields   = new StyledTextField[4];
        buttonGroup        = new ButtonGroup();

        addGuicomponents();
    }

    private void addGuicomponents() {

        // ── Header ────────────────────────────────────────────
        JPanel header = new JPanel(null);
        header.setBounds(0, 0, 900, 68);
        header.setBackground(commonConstants.PRIMARY);
        add(header);

        JLabel icon = new JLabel("✏");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        icon.setBounds(20, 16, 40, 36);
        header.add(icon);

        JLabel title = new JLabel("Create a New Question");
        title.setFont(commonConstants.FONT_HEADING);
        title.setForeground(Color.WHITE);
        title.setBounds(62, 20, 300, 28);
        header.add(title);

        JLabel back = new JLabel("← Back to Title");
        back.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        back.setForeground(new Color(255, 255, 255, 200));
        back.setBounds(730, 22, 148, 24);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                titlescreengui ts = new titlescreengui();
                ts.setLocationRelativeTo(createquestionscreengui.this);
                createquestionscreengui.this.dispose();
                ts.setVisible(true);
            }
            @Override public void mouseEntered(MouseEvent e) { back.setForeground(Color.WHITE); }
            @Override public void mouseExited(MouseEvent e)  { back.setForeground(new Color(255, 255, 255, 200)); }
        });
        header.add(back);

        // ── Left column: question + category ─────────────────
        RoundedPanel leftCard = new RoundedPanel(14, commonConstants.CARD, true);
        leftCard.setBounds(20, 88, 400, 480);
        add(leftCard);

        JLabel qSectionLabel = new JLabel("QUESTION");
        qSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        qSectionLabel.setForeground(commonConstants.TEXT_SECONDARY);
        qSectionLabel.setBounds(20, 20, 360, 16);
        leftCard.add(qSectionLabel);

        questiontextArea = new StyledTextArea();
        questiontextArea.setBounds(20, 42, 360, 120);
        leftCard.add(questiontextArea);

        JLabel catSectionLabel = new JLabel("CATEGORY");
        catSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        catSectionLabel.setForeground(commonConstants.TEXT_SECONDARY);
        catSectionLabel.setBounds(20, 180, 360, 16);
        leftCard.add(catSectionLabel);

        JLabel catHint = new JLabel("Type a new category or reuse an existing one.");
        catHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        catHint.setForeground(commonConstants.TEXT_HINT);
        catHint.setBounds(20, 200, 360, 16);
        leftCard.add(catHint);

        categorytextfield = new StyledTextField();
        categorytextfield.setBounds(20, 222, 360, 42);
        leftCard.add(categorytextfield);

        // Tip box
        RoundedPanel tipBox = new RoundedPanel(8, commonConstants.PRIMARY_LIGHT, false);
        tipBox.setBounds(20, 290, 360, 80);
        leftCard.add(tipBox);

        JLabel tipIcon = new JLabel("💡");
        tipIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        tipIcon.setBounds(12, 12, 28, 28);
        tipBox.add(tipIcon);

        JLabel tipText = new JLabel("<html><b>Tips:</b> Select the radio button next to the<br>"
                + "correct answer. All four answer fields<br>must be filled in before submitting.</html>");
        tipText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tipText.setForeground(commonConstants.PRIMARY);
        tipText.setBounds(44, 8, 306, 64);
        tipBox.add(tipText);

        // Submit button
        StyledButton submitButton = new StyledButton("✔   Submit Question", StyledButton.Variant.PRIMARY);
        submitButton.setFont(commonConstants.FONT_BUTTON);
        submitButton.setHorizontalAlignment(SwingConstants.CENTER);
        submitButton.setBounds(20, 396, 360, 50);
        submitButton.addActionListener(e -> {
            if (validateInput()) {
                String question  = questiontextArea.getText();
                String category  = categorytextfield.getText();
                String[] answers = new String[answerTextFields.length];
                int correctindex = 0;
                for (int i = 0; i < answerTextFields.length; i++) {
                    answers[i] = answerTextFields[i].getText();
                    if (answerRadioButtons[i].isSelected()) correctindex = i;
                }
                if (JDBC.savequestioncategoryandanswerstodb(question, category, answers, correctindex)) {
                    JOptionPane.showMessageDialog(this, "Question added successfully! ✅", "Success", JOptionPane.INFORMATION_MESSAGE);
                    resetFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to save question. Check your database connection.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields before submitting.", "Incomplete Form", JOptionPane.WARNING_MESSAGE);
            }
        });
        leftCard.add(submitButton);

        // ── Right column: answers ─────────────────────────────
        RoundedPanel rightCard = new RoundedPanel(14, commonConstants.CARD, true);
        rightCard.setBounds(440, 88, 440, 480);
        add(rightCard);

        JLabel answersSectionLabel = new JLabel("ANSWER OPTIONS  ( ● = correct )");
        answersSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        answersSectionLabel.setForeground(commonConstants.TEXT_SECONDARY);
        answersSectionLabel.setBounds(20, 20, 400, 16);
        rightCard.add(answersSectionLabel);

        addAnswercomponents(rightCard);
    }

    private void addAnswercomponents(RoundedPanel parent) {
        int startY  = 48;
        int spacing = 98;
        char[] letters = {'A', 'B', 'C', 'D'};

        for (int i = 0; i < 4; i++) {
            JLabel badge = new JLabel(String.valueOf(letters[i]));
            badge.setFont(new Font("Segoe UI", Font.BOLD, 13));
            badge.setForeground(commonConstants.PRIMARY);
            badge.setHorizontalAlignment(SwingConstants.CENTER);
            badge.setOpaque(true);
            badge.setBackground(commonConstants.PRIMARY_LIGHT);
            badge.setBounds(20, startY + i * spacing + 16, 28, 28);
            parent.add(badge);

            JLabel answerLabel = new JLabel("Answer " + letters[i]);
            answerLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
            answerLabel.setForeground(commonConstants.TEXT_SECONDARY);
            answerLabel.setBounds(56, startY + i * spacing, 300, 16);
            parent.add(answerLabel);

            answerTextFields[i] = new StyledTextField();
            answerTextFields[i].setBounds(56, startY + i * spacing + 22, 290, 40);
            parent.add(answerTextFields[i]);

            answerRadioButtons[i] = new JRadioButton("Correct");
            answerRadioButtons[i].setFont(new Font("Segoe UI", Font.PLAIN, 12));
            answerRadioButtons[i].setForeground(commonConstants.SUCCESS);
            answerRadioButtons[i].setBackground(commonConstants.CARD);
            answerRadioButtons[i].setBounds(354, startY + i * spacing + 28, 76, 24);
            buttonGroup.add(answerRadioButtons[i]);
            parent.add(answerRadioButtons[i]);
        }

        answerRadioButtons[0].setSelected(true);
    }

    private boolean validateInput() {
        if (questiontextArea.getText().trim().isEmpty())  return false;
        if (categorytextfield.getText().trim().isEmpty()) return false;
        for (StyledTextField f : answerTextFields) {
            if (f.getText().trim().isEmpty()) return false;
        }
        return true;
    }

    private void resetFields() {
        questiontextArea.setText("");
        categorytextfield.setText("");
        for (StyledTextField f : answerTextFields) f.setText("");
        answerRadioButtons[0].setSelected(true);
    }
}

