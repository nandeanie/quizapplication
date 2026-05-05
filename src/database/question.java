package quizapplication.src.database;

import java.util.ArrayList;

// data model to represent data from question table
public class question {
    private int questionId;
    private int categoryId;
    private String questiontext;
    private ArrayList<answer> answers;

    public int getQuestionId() {
        return questionId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getQuestiontext() {
        return questiontext;
    }

    public ArrayList<answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<answer> answers) {
        this.answers = answers;
    }

    public question(int questionId, int categoryId, String questiontext) {
        this.questionId   = questionId;
        this.categoryId   = categoryId;
        this.questiontext = questiontext;
    }
}
