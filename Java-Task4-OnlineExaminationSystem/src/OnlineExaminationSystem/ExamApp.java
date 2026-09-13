package OnlineExaminationSystem;

import OnlineExaminationSystem.model.Question;
import OnlineExaminationSystem.model.UserStore;
import OnlineExaminationSystem.gui.ExamPanel;
import OnlineExaminationSystem.gui.LoginPanel;
import OnlineExaminationSystem.gui.ProfileUpdatePanel;
import OnlineExaminationSystem.gui.ResultPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class ExamApp extends JFrame {

    public static final String CARD_LOGIN = "login";
    public static final String CARD_PROFILE = "profile";
    public static final String CARD_EXAM = "exam";
    public static final String CARD_RESULT = "result";

    public static final int EXAM_DURATION_SECONDS = 30 * 60;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    private final UserStore userStore = new UserStore();
    private final List<Question> questions = buildQuestionBank();

    private final LoginPanel loginPanel;
    private final ProfileUpdatePanel profilePanel;
    private final ExamPanel examPanel;
    private final ResultPanel resultPanel;

    private String currentUsername;
    private boolean examInProgress = false;

    public ExamApp() {
        super("Online Examination System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(800, 600);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);

        loginPanel = new LoginPanel(this, userStore);
        profilePanel = new ProfileUpdatePanel(this, userStore);
        examPanel = new ExamPanel(this, questions);
        resultPanel = new ResultPanel(this);

        cardPanel.add(loginPanel, CARD_LOGIN);
        cardPanel.add(profilePanel, CARD_PROFILE);
        cardPanel.add(examPanel, CARD_EXAM);
        cardPanel.add(resultPanel, CARD_RESULT);

        add(cardPanel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClose();
            }
        });

        showCard(CARD_LOGIN);
    }

    private void handleWindowClose() {
        if (examInProgress) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "An exam is in progress. Are you sure you want to quit?",
                    "Confirm Quit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        } else {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to exit?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }

    public void showCard(String name) {
        cardLayout.show(cardPanel, name);
    }

    public void onLoginSuccess(String username) {
        this.currentUsername = username;
        profilePanel.loadUser(username);
        showCard(CARD_PROFILE);
    }

    public void onProfileContinue() {
        examPanel.startExam(currentUsername);
        examInProgress = true;
        showCard(CARD_EXAM);
    }

    public void onExamFinished(int correctCount, int totalCount, long timeTakenSeconds,
                                List<Boolean> perQuestionCorrect) {
        examInProgress = false;
        resultPanel.showResult(currentUsername, correctCount, totalCount, timeTakenSeconds,
                perQuestionCorrect, questions);
        showCard(CARD_RESULT);
    }

    public void onLogout() {
        currentUsername = null;
        loginPanel.reset();
        showCard(CARD_LOGIN);
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public UserStore getUserStore() {
        return userStore;
    }

    private List<Question> buildQuestionBank() {
        List<Question> list = new ArrayList<>();
        list.add(new Question("What does JVM stand for?",
                new String[]{"Java Virtual Machine", "Java Verified Method", "Java Variable Manager", "Joint Virtual Machine"}, 0));
        list.add(new Question("Which keyword is used to inherit a class in Java?",
                new String[]{"implements", "extends", "inherits", "super"}, 1));
        list.add(new Question("Which collection class allows duplicate elements and maintains insertion order?",
                new String[]{"HashSet", "TreeSet", "ArrayList", "HashMap"}, 2));
        list.add(new Question("What is the default value of a boolean instance variable in Java?",
                new String[]{"true", "false", "0", "null"}, 1));
        list.add(new Question("Which package contains the Swing GUI classes?",
                new String[]{"java.awt", "javax.swing", "java.gui", "javax.awt"}, 1));
        list.add(new Question("Which layout manager arranges components in a grid of rows and columns?",
                new String[]{"BorderLayout", "FlowLayout", "GridLayout", "CardLayout"}, 2));
        list.add(new Question("What does the 'final' keyword do when applied to a variable?",
                new String[]{"Makes it static", "Prevents reassignment", "Makes it private", "Deletes it after use"}, 1));
        list.add(new Question("Which exception is thrown when dividing an integer by zero?",
                new String[]{"NullPointerException", "ArithmeticException", "NumberFormatException", "ClassCastException"}, 1));
        list.add(new Question("What is the size of an int in Java?",
                new String[]{"16 bits", "32 bits", "64 bits", "8 bits"}, 1));
        list.add(new Question("Which method is the entry point of a Java application?",
                new String[]{"start()", "run()", "main()", "init()"}, 2));
        return list;
    }
}
