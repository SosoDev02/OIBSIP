package OnlineExaminationSystem.gui;

import OnlineExaminationSystem.ExamApp;
import OnlineExaminationSystem.model.Question;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ResultPanel extends JPanel {

    private final ExamApp app;
    private final JLabel scoreLabel = new JLabel();
    private final JLabel timeLabel = new JLabel();
    private final JTextArea breakdownArea = new JTextArea();

    public ResultPanel(ExamApp app) {
        this.app = app;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Exam Results");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        scoreLabel.setFont(scoreLabel.getFont().deriveFont(Font.BOLD, 16f));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(title);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(scoreLabel);
        topPanel.add(timeLabel);
        add(topPanel, BorderLayout.NORTH);

        breakdownArea.setEditable(false);
        breakdownArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        add(new JScrollPane(breakdownArea), BorderLayout.CENTER);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> app.onLogout());
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(logoutButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void showResult(String username, int correctCount, int totalCount, long timeTakenSeconds,
                            List<Boolean> perQuestionCorrect, List<Question> questions) {
        scoreLabel.setText("Score: " + correctCount + " out of " + totalCount);
        long minutes = timeTakenSeconds / 60;
        long seconds = timeTakenSeconds % 60;
        timeLabel.setText(String.format("Time Taken: %d min %d sec", minutes, seconds));

        StringBuilder sb = new StringBuilder();
        sb.append("Breakdown:\n\n");
        for (int i = 0; i < questions.size(); i++) {
            String status = perQuestionCorrect.get(i) ? "Correct" : "Incorrect";
            sb.append(String.format("Q%d: %s - %s%n", i + 1, status, questions.get(i).getText()));
        }
        breakdownArea.setText(sb.toString());
        breakdownArea.setCaretPosition(0);
    }
}
