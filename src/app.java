package quizapplication.src;

import javax.swing.SwingUtilities;
import quizapplication.src.database.category;


public class app {
    public static void main(String[] args) {
        // ensure Swing GUI tasks are executed on the event dispatch thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // create and display the title screen
                new titlescreengui().setVisible(true);

                
                // new createquestionscreengui().setVisible(true);
                // new qiuzscreengui(new category(1, "General Knowledge"), 10).setVisible(true);
            }
        });
    }
}
