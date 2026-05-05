package quizapplication.src.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class JDBC {

    // ── Database Configuration — apni details yahan bharo ──
    private static final String DB_URL      = "jdbc:mysql://127.0.0.1:3306/quiz_gui_db";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "nandeanie"; // 

    /*
     * question   - the question to be inserted
     * category   - category of the question (inserted if new)
     * answers    - answer options to be inserted
     * correctIndex - index of the correct answer
     */
    public static boolean savequestioncategoryandanswerstodb(String question, String category, String[] answers, int correctIndex) {
        try {
            category categoryobj = getCategory(category);
            if (categoryobj == null) {
                categoryobj = insertCategory(category);
            }

            question questionobj = insertQuestion(categoryobj, question);
            return insertAnswers(questionobj, answers, correctIndex);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ── Question methods ──────────────────────────────────────

    public static ArrayList<question> getQuestions(category category) {
        ArrayList<question> questions = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement getQuestionsQuery = connection.prepareStatement(
                "SELECT * FROM QUESTION JOIN CATEGORY " +
                "ON QUESTION.CATEGORY_ID = CATEGORY.CATEGORY_ID " +
                "WHERE CATEGORY.CATEGORY_NAME = ? ORDER BY RAND()"
            );
            getQuestionsQuery.setString(1, category.getCategoryName());

            ResultSet resultSet = getQuestionsQuery.executeQuery();
            while (resultSet.next()) {
                int    questionId  = resultSet.getInt("question_id");
                int    categoryId  = resultSet.getInt("category_id");
                String questionTxt = resultSet.getString("question_text");
                questions.add(new question(questionId, categoryId, questionTxt));
            }
            return questions;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static question insertQuestion(category categoryobj, String questiontext) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement insertQuestionQuery = connection.prepareStatement(
                "INSERT INTO QUESTION(CATEGORY_ID, QUESTION_TEXT) VALUES(?,?)",
                Statement.RETURN_GENERATED_KEYS
            );
            insertQuestionQuery.setInt(1, categoryobj.getCategoryId());
            insertQuestionQuery.setString(2, questiontext);
            insertQuestionQuery.executeUpdate();

            ResultSet resultSet = insertQuestionQuery.getGeneratedKeys();
            if (resultSet.next()) {
                int questionId = resultSet.getInt(1);
                return new question(questionId, categoryobj.getCategoryId(), questiontext);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ── Category methods ──────────────────────────────────────

    public static category getCategory(String category) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement getCategoryQuery = connection.prepareStatement(
                "SELECT * FROM CATEGORY WHERE CATEGORY_NAME = ?"
            );
            getCategoryQuery.setString(1, category);

            ResultSet resultset = getCategoryQuery.executeQuery();
            if (resultset.next()) {
                int categoryId = resultset.getInt("category_id");
                return new category(categoryId, category);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> getcategories() {
        ArrayList<String> categoryList = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            Statement getCategoriesQuery = connection.createStatement();
            ResultSet resultSet = getCategoriesQuery.executeQuery("SELECT * FROM CATEGORY");

            while (resultSet.next()) {
                String categoryName = resultSet.getString("category_name");
                categoryList.add(categoryName);
            }
            return categoryList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static category insertCategory(String category) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement insertCategoryQuery = connection.prepareStatement(
                "INSERT INTO CATEGORY(CATEGORY_NAME) VALUES(?)",
                Statement.RETURN_GENERATED_KEYS
            );
            insertCategoryQuery.setString(1, category);
            insertCategoryQuery.executeUpdate();

            ResultSet resultSet = insertCategoryQuery.getGeneratedKeys();
            if (resultSet.next()) {
                int category_id = resultSet.getInt(1);
                return new category(category_id, category);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ── Answer methods ────────────────────────────────────────

    public static ArrayList<answer> getAnswers(question question) {
        ArrayList<answer> answers = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement getAnswersQuery = connection.prepareStatement(
                "SELECT * FROM QUESTION JOIN ANSWER " +
                "ON QUESTION.QUESTION_ID = ANSWER.QUESTION_ID " +
                "WHERE QUESTION.QUESTION_ID = ? ORDER BY RAND()"
            );
            getAnswersQuery.setInt(1, question.getQuestionId());

            ResultSet resultSet = getAnswersQuery.executeQuery();
            while (resultSet.next()) {
                int     answerId   = resultSet.getInt("idanswer");
                String  answerText = resultSet.getString("answer_text");
                boolean iscorrect  = resultSet.getBoolean("is_correct");
                answers.add(new answer(answerId, question.getQuestionId(), answerText, iscorrect));
            }
            return answers;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // true  - successfully inserted answers
    // false - failed to insert answers
    private static boolean insertAnswers(question question, String[] answers, int correctIndex) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement insertAnswerQuery = connection.prepareStatement(
                "INSERT INTO ANSWER(QUESTION_ID, ANSWER_TEXT, IS_CORRECT) VALUES(?,?,?)"
            );
            insertAnswerQuery.setInt(1, question.getQuestionId());

            for (int i = 0; i < answers.length; i++) {
                insertAnswerQuery.setString(2, answers[i]);
                insertAnswerQuery.setBoolean(3, i == correctIndex);
                insertAnswerQuery.executeUpdate();
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}