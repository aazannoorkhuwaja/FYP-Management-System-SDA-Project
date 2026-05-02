package view;

import model.Database;
import model.User;
import model.Student;
import model.Supervisor;
import model.Admin;
import util.DiagnosticService;

import javax.swing.*;
import java.awt.*;

/**
 * Login screen refined with Monolithic Serenity theme.
 * Refined layout to prevent "too small" input fields.
 */
public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private DiagnosticService diag;

    public LoginFrame() {
        this.diag = DiagnosticService.getInstance();
        diag.trace("UI-Login", "Initializing LoginFrame");
        
        setTitle("FYP Management Platform - Login");
        setSize(450, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        Theme.applyTheme(mainPanel);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Header
        JLabel headerLabel = new JLabel("FYP.MP");
        headerLabel.setFont(Theme.LOGO_FONT);
        headerLabel.setForeground(Theme.TEXT);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Form fields with vertical spacing
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setOpaque(false);

        formContainer.add(createLabel("Email Address"));
        formContainer.add(Box.createVerticalStrut(8));
        emailField = new JTextField(20);
        Theme.styleTextField(emailField);
        formContainer.add(emailField);

        formContainer.add(Box.createVerticalStrut(20));

        formContainer.add(createLabel("Password"));
        formContainer.add(Box.createVerticalStrut(8));
        passwordField = new JPasswordField(20);
        Theme.styleTextField(passwordField);
        formContainer.add(passwordField);

        mainPanel.add(formContainer, BorderLayout.CENTER);

        // Action area
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setOpaque(false);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        JButton loginBtn = Theme.createPrimaryButton("Sign In");
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> attemptLogin());
        actionPanel.add(loginBtn);

        actionPanel.add(Box.createVerticalStrut(20));

        JLabel hintLabel = new JLabel("Demo: aazan@fast.edu / pass123");
        hintLabel.setForeground(Theme.TEXT_MUTED);
        hintLabel.setFont(Theme.MONO_FONT);
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionPanel.add(hintLabel);

        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Theme.TEXT_MUTED);
        l.setFont(Theme.BOLD_FONT);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void attemptLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        diag.info("UI-Login", "Login attempt for: " + email);

        if (email.isEmpty() || password.isEmpty()) {
            diag.warning("UI-Login", "Empty fields in login attempt");
            JOptionPane.showMessageDialog(this, "Security Requirement: Both fields must be populated.", "Access Denied",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Database db = Database.getInstance();
        User user = db.findUserByEmail(email);

        if (user != null && user.login(email, password)) {
            diag.info("UI-Login", "Login successful, redirecting to dashboard");
            Theme.showNotification(this, "Welcome back, " + user.getName());
            this.dispose();
            if (user instanceof Student)
                new StudentDashboardFrame((Student) user).setVisible(true);
            else if (user instanceof Supervisor)
                new SupervisorDashboardFrame((Supervisor) user).setVisible(true);
            else if (user instanceof Admin)
                new AdminDashboardFrame((Admin) user).setVisible(true);
        } else {
            diag.error("UI-Login", "Authentication failed for: " + email);
            JOptionPane.showMessageDialog(this, "Authentication Failed: Invalid credentials provided.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
