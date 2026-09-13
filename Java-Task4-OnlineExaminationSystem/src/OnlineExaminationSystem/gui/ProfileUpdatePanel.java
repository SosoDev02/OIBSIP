package OnlineExaminationSystem.gui;

import OnlineExaminationSystem.ExamApp;
import OnlineExaminationSystem.model.UserStore;

import javax.swing.*;
import java.awt.*;

public class ProfileUpdatePanel extends JPanel {

    private final ExamApp app;
    private final UserStore userStore;

    private String username;
    private final JTextField displayNameField = new JTextField(15);
    private final JPasswordField newPasswordField = new JPasswordField(15);
    private final JPasswordField confirmPasswordField = new JPasswordField(15);
    private final JLabel statusLabel = new JLabel(" ");

    public ProfileUpdatePanel(ExamApp app, UserStore userStore) {
        this.app = app;
        this.userStore = userStore;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JLabel title = new JLabel("Update Your Profile (optional)", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(6, 1, 10, 10));

        JPanel nameRow = new JPanel(new GridLayout(1, 2, 10, 10));
        nameRow.add(new JLabel("Display Name:"));
        nameRow.add(displayNameField);

        JPanel newPasswordRow = new JPanel(new GridLayout(1, 2, 10, 10));
        newPasswordRow.add(new JLabel("New Password:"));
        newPasswordRow.add(newPasswordField);

        JPanel confirmPasswordRow = new JPanel(new GridLayout(1, 2, 10, 10));
        confirmPasswordRow.add(new JLabel("Confirm Password:"));
        confirmPasswordRow.add(confirmPasswordField);

        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> saveChanges());

        JButton continueButton = new JButton("Continue to Exam");
        continueButton.addActionListener(e -> app.onProfileContinue());

        JPanel buttonRow = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonRow.add(saveButton);
        buttonRow.add(continueButton);

        statusLabel.setForeground(new Color(0, 128, 0));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        formPanel.add(nameRow);
        formPanel.add(newPasswordRow);
        formPanel.add(confirmPasswordRow);
        formPanel.add(buttonRow);
        formPanel.add(statusLabel);
        formPanel.add(new JLabel());

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.add(formPanel);
        add(wrapper, BorderLayout.CENTER);
    }

    public void loadUser(String username) {
        this.username = username;
        displayNameField.setText(userStore.getDisplayName(username));
        newPasswordField.setText("");
        confirmPasswordField.setText("");
        statusLabel.setText(" ");
    }

    private void saveChanges() {
        String newDisplayName = displayNameField.getText().trim();
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (!newPassword.equals(confirmPassword)) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Passwords do not match.");
            return;
        }

        userStore.updateProfile(username, newDisplayName, newPassword.isEmpty() ? null : newPassword);
        statusLabel.setForeground(new Color(0, 128, 0));
        statusLabel.setText("Profile updated.");
    }
}
