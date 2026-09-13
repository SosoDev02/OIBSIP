package OnlineExaminationSystem.gui;

import OnlineExaminationSystem.ExamApp;
import OnlineExaminationSystem.model.Question;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ExamPanel extends JPanel {

    private final ExamApp app;
    private final List<Question> questions;
    private final int[] selectedAnswers;

    private int currentIndex = 0;
    private long examStartMillis;
    private int remainingSeconds;
    private Timer countdownTimer;

    private final JLabel timerLabel = new JLabel();
    private final JLabel questionNumberLabel = new JLabel();
    private final JTextArea questionTextArea = new JTextArea();
    private final JRadioButton[] optionButtons = new JRadioButton[4];
    private final ButtonGroup buttonGroup = new ButtonGroup();

    private final JButton previousButton = new JButton("Previous");
    private final JButton nextButton = new JButton("Next");
    private final JButton submitButton = new JButton("Submit Exam");

    public ExamPanel(ExamApp app, List<Question> questions) {
        this.app = app;
        this.questions = questions;
        this.selectedAnswers = new int[questions.size()];
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        questionNumberLabel.setFont(questionNumberLabel.getFont().deriveFont(Font.BOLD, 14f));
        timerLabel.setFont(timerLabel.getFont().deriveFont(Font.BOLD, 16f));
        timerLabel.setForeground(new Color(180, 0, 0));
        topPanel.add(questionNumberLabel, BorderLayout.WEST);
        topPanel.add(timerLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        questionTextArea.setEditable(false);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setFont(questionTextArea.getFont().deriveFont(Font.PLAIN, 16f));
        questionTextArea.setOpaque(false);
        questionTextArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        questionTextArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        centerPanel.add(questionTextArea);
        centerPanel.add(Box.createVerticalStrut(15));

        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(optionButtons[i].getFont().deriveFont(15f));
            optionButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            buttonGroup.add(optionButtons[i]);
            centerPanel.add(optionButtons[i]);
            centerPanel.add(Box.createVerticalStrut(8));

            final int optionIndex = i;
            optionButtons[i].addActionListener(e -> selectedAnswers[currentIndex] = optionIndex);
        }

        centerPanel.add(Box.createVerticalGlue());

        add(new JScrollPane(centerPanel), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        previousButton.addActionListener(e -> goToQuestion(currentIndex - 1));
        nextButton.addActionListener(e -> goToQuestion(currentIndex + 1));
        submitButton.addActionListener(e -> confirmAndSubmit());

        bottomPanel.add(previousButton);
        bottomPanel.add(nextButton);
        bottomPanel.add(submitButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void startExam(String username) {
        for (int i = 0; i < selectedAnswers.length; i++) {
            selectedAnswers[i] = -1;
        }
        currentIndex = 0;
        examStartMillis = System.currentTimeMillis();
        remainingSeconds = ExamApp.EXAM_DURATION_SECONDS;
        updateTimerLabel();
        displayQuestion();

        if (countdownTimer != null) {
            countdownTimer.stop();
        }
        countdownTimer = new Timer(1000, e -> tick());
        countdownTimer.start();
    }

    private void tick() {
        remainingSeconds--;
        updateTimerLabel();
        if (remainingSeconds <= 0) {
            countdownTimer.stop();
            JOptionPane.showMessageDialog(this,
                    "Time is up! Your exam is being submitted automatically.",
                    "Time's Up",
                    JOptionPane.WARNING_MESSAGE);
            finishExam();
        }
    }

    private void updateTimerLabel() {
        int minutes = Math.max(remainingSeconds, 0) / 60;
        int seconds = Math.max(remainingSeconds, 0) % 60;
        timerLabel.setText(String.format("Time Remaining: %02d:%02d", minutes, seconds));
    }

    private void displayQuestion() {
        Question q = questions.get(currentIndex);
        questionNumberLabel.setText("Question " + (currentIndex + 1) + " of " + questions.size());
        questionTextArea.setText(q.getText());

        buttonGroup.clearSelection();
        String[] options = q.getOptions();
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(options[i]);
        }
        if (selectedAnswers[currentIndex] != -1) {
            optionButtons[selectedAnswers[currentIndex]].setSelected(true);
        }

        previousButton.setEnabled(currentIndex > 0);
        nextButton.setEnabled(currentIndex < questions.size() - 1);
    }

    private void goToQuestion(int index) {
        if (index < 0 || index >= questions.size()) return;
        currentIndex = index;
        displayQuestion();
    }

    private void confirmAndSubmit() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to submit the exam?",
                "Confirm Submit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (choice == JOptionPane.YES_OPTION) {
            countdownTimer.stop();
            finishExam();
        }
    }

    private void finishExam() {
        long timeTakenSeconds = (System.currentTimeMillis() - examStartMillis) / 1000;
        int correctCount = 0;
        List<Boolean> perQuestionCorrect = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            boolean correct = selectedAnswers[i] == questions.get(i).getCorrectIndex();
            perQuestionCorrect.add(correct);
            if (correct) correctCount++;
        }
        app.onExamFinished(correctCount, questions.size(), timeTakenSeconds, perQuestionCorrect);
    }
}
