package quizapplication.src;

import javax.swing.*;

import quizapplication.src.components.RoundedPanel;
import quizapplication.src.components.StyledButton;
import quizapplication.src.constants.commonConstants;
import quizapplication.src.database.JDBC;
import quizapplication.src.database.answer;
import quizapplication.src.database.category;
import quizapplication.src.database.question;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class qiuzscreengui extends JFrame implements ActionListener {

    // ── State ─────────────────────────────────────────────────
    private category            category;
    private ArrayList<question> questions;
    private question            currentquestion;
    private int                 currentquestionnumber = 0;
    private int                 numberofquestions;
    private int                 score                 = 0;
    private boolean             firstchoicemade       = false;

    // ── UI references ──────────────────────────────────────────
    private JLabel       scoreLabel;
    private JLabel       questionCounterLabel;
    private JTextArea    questiontextarea;
    private StyledButton[] answerbutton;
    private StyledButton nextButton;
    private JProgressBar progressBar;

    public qiuzscreengui(category category, int numofquestions) {
        super("Quiz Game — " + category.getCategoryName());
        setSize(460, 640);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(commonConstants.SURFACE);

        answerbutton  = new StyledButton[4];
        this.category = category;

        questions = JDBC.getQuestions(category);
        this.numberofquestions = Math.min(numofquestions, questions.size());

        for (question q : questions) {
            q.setAnswers(JDBC.getAnswers(q));
        }

        currentquestion = questions.get(currentquestionnumber);
        addGuicomponents();
    }

    private void addGuicomponents() {

        // ── Header bar ────────────────────────────────────────
        JPanel header = new JPanel(null);
        header.setBounds(0, 0, 460, 68);
        header.setBackground(commonConstants.PRIMARY);
        add(header);

        JLabel topicLabel = new JLabel("📚  " + category.getCategoryName());
        topicLabel.setFont(commonConstants.FONT_SUBHEADING);
        topicLabel.setForeground(Color.WHITE);
        topicLabel.setBounds(16, 10, 280, 24);
        header.add(topicLabel);

        scoreLabel = new JLabel("Score: 0/" + numberofquestions);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        scoreLabel.setForeground(new Color(255, 255, 255, 220));
        scoreLabel.setBounds(320, 10, 120, 24);
        scoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        header.add(scoreLabel);

        progressBar = new JProgressBar(0, numberofquestions);
        progressBar.setValue(0);
        progressBar.setBounds(16, 44, 428, 8);
        progressBar.setBackground(new Color(255, 255, 255, 60));
        progressBar.setForeground(commonConstants.ACCENT);
        progressBar.setBorderPainted(false);
        header.add(progressBar);

        // ── Question card ─────────────────────────────────────
        RoundedPanel questionCard = new RoundedPanel(14, commonConstants.CARD, true);
        questionCard.setBounds(20, 84, 420, 140);
        add(questionCard);

        questionCounterLabel = new JLabel("Question " + (currentquestionnumber + 1) + " of " + numberofquestions);
        questionCounterLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        questionCounterLabel.setForeground(commonConstants.PRIMARY);
        questionCounterLabel.setBounds(16, 14, 388, 16);
        questionCard.add(questionCounterLabel);

        questiontextarea = new JTextArea(currentquestion.getQuestiontext());
        questiontextarea.setFont(commonConstants.FONT_QUESTION);
        questiontextarea.setBounds(16, 36, 388, 88);
        questiontextarea.setLineWrap(true);
        questiontextarea.setWrapStyleWord(true);
        questiontextarea.setEditable(false);
        questiontextarea.setForeground(commonConstants.TEXT_PRIMARY);
        questiontextarea.setBackground(commonConstants.CARD);
        questiontextarea.setOpaque(false);
        questionCard.add(questiontextarea);

        // ── Answer buttons ────────────────────────────────────
        addanswercomponents();

        // ── Next button ───────────────────────────────────────
        nextButton = new StyledButton("Next Question  →", StyledButton.Variant.PRIMARY);
        nextButton.setFont(commonConstants.FONT_BUTTON);
        nextButton.setHorizontalAlignment(SwingConstants.CENTER);
        nextButton.setBounds(20, 540, 420, 46);
        nextButton.setVisible(false);
        nextButton.addActionListener(e -> {
            nextButton.setVisible(false);
            firstchoicemade = false;

            currentquestion = questions.get(++currentquestionnumber);
            questiontextarea.setText(currentquestion.getQuestiontext());
            questionCounterLabel.setText("Question " + (currentquestionnumber + 1) + " of " + numberofquestions);
            progressBar.setValue(currentquestionnumber);

            for (int i = 0; i < currentquestion.getAnswers().size(); i++) {
                answerbutton[i].clearFeedbackColor();
                answerbutton[i].setText(currentquestion.getAnswers().get(i).getAnswerText());
                answerbutton[i].setEnabled(true);
            }
        });
        add(nextButton);

        // ── Return to title ───────────────────────────────────
        StyledButton returnBtn = new StyledButton("← Return to Title", StyledButton.Variant.OUTLINE);
        returnBtn.setFont(commonConstants.FONT_LABEL);
        returnBtn.setHorizontalAlignment(SwingConstants.CENTER);
        returnBtn.setBounds(20, 594, 420, 34);
        returnBtn.addActionListener(e -> {
            titlescreengui ts = new titlescreengui();
            ts.setLocationRelativeTo(this);
            this.dispose();
            ts.setVisible(true);
        });
        add(returnBtn);
    }

    private void addanswercomponents() {
        int startY       = 240;
        int buttonHeight = 52;
        int gap          = 12;

        for (int i = 0; i < currentquestion.getAnswers().size(); i++) {
            answer ans  = currentquestion.getAnswers().get(i);
            char letter = (char) ('A' + i);

            StyledButton btn = new StyledButton("  " + letter + ".  " + ans.getAnswerText(), StyledButton.Variant.ANSWER);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setBounds(20, startY + i * (buttonHeight + gap), 420, buttonHeight);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.addActionListener(this);
            answerbutton[i] = btn;
            add(btn);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        StyledButton clicked = (StyledButton) e.getSource();

        answer correctAnswer = currentquestion.getAnswers()
            .stream()
            .filter(answer::iscorrect)
            .findFirst()
            .orElse(null);

        if (correctAnswer == null) return;

        if (clicked.getText().contains(correctAnswer.getAnswerText())) {
            clicked.setFeedbackColor(commonConstants.SUCCESS);

            if (!firstchoicemade) {
                score++;
                scoreLabel.setText("Score: " + score + "/" + numberofquestions);
            }

            if (currentquestionnumber == numberofquestions - 1) {
                progressBar.setValue(numberofquestions);
                showFinalScore();
            } else {
                nextButton.setVisible(true);
            }
        } else {
            clicked.setFeedbackColor(commonConstants.ERROR);
            clicked.setEnabled(false);
        }

        firstchoicemade = true;
    }

    private void showFinalScore() {
        String msg = String.format(
            "<html><center><b>Quiz Complete! 🎉</b><br><br>" +
            "You scored <b>%d out of %d</b>.<br>" +
            "That's <b>%.0f%%</b>!</center></html>",
            score, numberofquestions, (score * 100.0 / numberofquestions)
        );
        int choice = JOptionPane.showOptionDialog(
            this, msg, "Final Score",
            JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
            null, new String[]{"Play Again", "Return to Title"}, "Return to Title"
        );
        if (choice == 0) {
            qiuzscreengui replay = new qiuzscreengui(category, numberofquestions);
            replay.setLocationRelativeTo(this);
            this.dispose();
            replay.setVisible(true);
        } else {
            titlescreengui ts = new titlescreengui();
            ts.setLocationRelativeTo(this);
            this.dispose();
            ts.setVisible(true);
        }
    }
}
