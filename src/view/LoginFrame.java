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
 * Login screen — centered form card with fixed field widths (no full-width stretch).
 */
public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private DiagnosticService diag;

    public LoginFrame() {
        this.diag = DiagnosticService.getInstance();
        diag.trace("UI-Login", "Initializing LoginFrame");

        setTitle("FYP Management Platform - Login");
        setMinimumSize(new Dimension(840, 480));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(Theme.BG);
        root.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel brandPanel = new JPanel(new BorderLayout());
        brandPanel.setPreferredSize(new Dimension(280, 0));
        brandPanel.setBackground(Theme.FAST_BLUE_DARK);
        brandPanel.setBorder(BorderFactory.createEmptyBorder(38, 32, 38, 32));

        JPanel brandText = new JPanel();
        brandText.setOpaque(false);
        brandText.setLayout(new BoxLayout(brandText, BoxLayout.Y_AXIS));

        JLabel campusLabel = new JLabel("FAST-NUCES");
        campusLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        campusLabel.setForeground(new Color(166, 196, 230));
        campusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        brandText.add(campusLabel);
        brandText.add(Box.createVerticalStrut(18));

        JLabel logoLabel = new JLabel("FYP.MP");
        logoLabel.setFont(Theme.LOGO_FONT);
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        brandText.add(logoLabel);
        brandText.add(Box.createVerticalStrut(12));

        JLabel subtitleLabel = new JLabel("<html>Final Year Project<br>Management Platform</html>");
        subtitleLabel.setFont(Theme.SUBHEADING_FONT);
        subtitleLabel.setForeground(new Color(223, 235, 250));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        brandText.add(subtitleLabel);
        brandText.add(Box.createVerticalGlue());

        JLabel footLabel = new JLabel("Blue and white academic workspace");
        footLabel.setFont(Theme.MONO_FONT);
        footLabel.setForeground(new Color(166, 196, 230));
        footLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        brandText.add(footLabel);

        brandPanel.add(brandText, BorderLayout.CENTER);
        root.add(brandPanel, BorderLayout.WEST);

        JPanel centerHost = new JPanel(new GridBagLayout());
        centerHost.setOpaque(false);
        GridBagConstraints cx = new GridBagConstraints();
        cx.gridx = 0;
        cx.gridy = 0;
        cx.weightx = 1;
        cx.weighty = 1;
        cx.anchor = GridBagConstraints.CENTER;
        cx.fill = GridBagConstraints.NONE;

        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(Theme.SURFACE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                BorderFactory.createEmptyBorder(36, 40, 36, 40)));

        GridBagConstraints g = new GridBagConstraints();
        int row = 0;

        JLabel headerLabel = new JLabel("Sign in");
        headerLabel.setFont(Theme.HEADING_FONT);
        headerLabel.setForeground(Theme.FAST_BLUE_DARK);
        g.gridx = 0;
        g.gridy = row++;
        g.gridwidth = 1;
        g.anchor = GridBagConstraints.CENTER;
        g.fill = GridBagConstraints.NONE;
        g.weightx = 0;
        g.insets = new Insets(0, 0, 22, 0);
        formCard.add(headerLabel, g);

        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;

        JLabel emailLbl = createLabel("Email Address");
        g.gridy = row++;
        g.insets = new Insets(0, 0, 8, 0);
        formCard.add(emailLbl, g);

        emailField = new JTextField();
        Theme.styleTextField(emailField);
        Theme.constrainFormFieldWidth(emailField, Theme.FORM_FIELD_WIDTH);
        g.gridy = row++;
        g.insets = new Insets(0, 0, 14, 0);
        formCard.add(emailField, g);

        JLabel pwdLbl = createLabel("Password");
        g.gridy = row++;
        g.insets = new Insets(0, 0, 8, 0);
        formCard.add(pwdLbl, g);

        passwordField = new JPasswordField();
        Theme.styleTextField(passwordField);
        Theme.constrainFormFieldWidth(passwordField, Theme.FORM_FIELD_WIDTH);
        g.gridy = row++;
        g.insets = new Insets(0, 0, 22, 0);
        formCard.add(passwordField, g);

        JButton loginBtn = Theme.createPrimaryButton("Sign In");
        Dimension bd = loginBtn.getPreferredSize();
        loginBtn.setPreferredSize(new Dimension(Theme.FORM_FIELD_WIDTH, Math.max(bd.height, 44)));
        loginBtn.setMinimumSize(loginBtn.getPreferredSize());
        loginBtn.addActionListener(e -> attemptLogin());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        btnRow.setOpaque(false);
        btnRow.add(loginBtn);
        g.gridy = row++;
        g.fill = GridBagConstraints.NONE;
        g.anchor = GridBagConstraints.CENTER;
        g.weightx = 0;
        g.insets = new Insets(0, 0, 16, 0);
        formCard.add(btnRow, g);

        JLabel hintLabel = new JLabel("<html><body style='width:" + Theme.FORM_FIELD_WIDTH + "px;text-align:center'>"
                + "Demo: aazan@fast.edu / pass123, umer@fast.edu / pass123, admin@fast.edu / admin123"
                + "</body></html>");
        hintLabel.setForeground(Theme.TEXT_MUTED);
        hintLabel.setFont(Theme.MONO_FONT);
        g.gridy = row++;
        g.anchor = GridBagConstraints.CENTER;
        g.fill = GridBagConstraints.NONE;
        g.insets = new Insets(0, 0, 0, 0);
        formCard.add(hintLabel, g);

        centerHost.add(formCard, cx);
        root.add(centerHost, BorderLayout.CENTER);

        add(root);
        pack();
        Theme.maximizeFrame(this);
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Theme.TEXT_MUTED);
        l.setFont(Theme.BOLD_FONT);
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
