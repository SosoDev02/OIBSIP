package OnlineExaminationSystem.gui;

import OnlineExaminationSystem.ExamApp;
import OnlineExaminationSystem.model.UserStore;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    private final ExamApp app;
    private final UserStore userStore;

    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JLabel statusLabel = new JLabel(" ");

    public LoginPanel(ExamApp app, UserStore userStore) {
        this.app = app;
        this.userStore = userStore;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JLabel title = new JLabel("Online Examination System", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 1, 10, 10));

        JPanel usernameRow = new JPanel(new GridLayout(1, 2, 10, 10));
        usernameRow.add(new JLabel("Username:"));
        usernameRow.add(usernameField);

        JPanel passwordRow = new JPanel(new GridLayout(1, 2, 10, 10));
        passwordRow.add(new JLabel("Password:"));
        passwordRow.add(passwordField);

        JButton loginButton = new JButton("Log In");
        loginButton.addActionListener(e -> attemptLogin());

        statusLabel.setForeground(Color.RED);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        formPanel.add(new JLabel());
        formPanel.add(usernameRow);
        formPanel.add(passwordRow);
        formPanel.add(loginButton);
        formPanel.add(statusLabel);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.add(formPanel);
        add(wrapper, BorderLayout.CENTER);

        passwordField.addActionListener(e -> attemptLogin());
        usernameField.addActionListener(e -> passwordField.requestFocusInWindow());
    }

    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both username and password.");
            return;
        }

        if (userStore.authenticate(username, password)) {
            statusLabel.setText(" ");
            app.onLoginSuccess(username);
        } else {
            statusLabel.setText("Invalid username or password.");
        }
    }

    public void reset() {
        usernameField.setText("");
        passwordField.setText("");
        statusLabel.setText(" ");
    }
}
