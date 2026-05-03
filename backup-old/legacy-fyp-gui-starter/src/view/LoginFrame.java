package view;

import model.Database;
import model.User;
import model.Student;
import model.Supervisor;
import model.Admin;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("FYP Management Platform - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(e -> attemptLogin());
        panel.add(loginBtn);

        // Demo accounts hint
        panel.add(new JLabel("<html><small>Demo: aazan@fast.edu / pass123</small></html>"));

        add(panel);
    }

    private void attemptLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Database db = Database.getInstance();
        User user = db.findUserByEmail(email);

        if (user != null && user.login(email, password)) {
            JOptionPane.showMessageDialog(this, "Welcome " + user.getName() + "!");
            this.dispose();

            if (user instanceof Student) {
                new StudentDashboardFrame((Student) user).setVisible(true);
            } else if (user instanceof Supervisor) {
                new SupervisorDashboardFrame((Supervisor) user).setVisible(true);
            } else if (user instanceof Admin) {
                JOptionPane.showMessageDialog(this, "Admin dashboard not implemented in demo scope.");
                new LoginFrame().setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
