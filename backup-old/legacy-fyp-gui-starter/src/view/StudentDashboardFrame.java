package view;

import model.Database;
import model.Student;
import model.Proposal;
import model.Group;

import javax.swing.*;
import java.awt.*;

public class StudentDashboardFrame extends JFrame {
    private Student student;

    public StudentDashboardFrame(Student student) {
        this.student = student;
        setTitle("Student Dashboard - " + student.getName());
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcome = new JLabel("Welcome, " + student.getName() + " (" + student.getGroupId() + ")");
        welcome.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(welcome, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        JButton proposalBtn = new JButton("Submit FYP Proposal");
        JButton requestBtn = new JButton("Send Supervision Request");
        JButton reviewBtn = new JButton("Submit Peer Review");
        JButton logoutBtn = new JButton("Logout");

        proposalBtn.addActionListener(e -> new SubmitProposalFrame(student).setVisible(true));
        requestBtn.addActionListener(e -> new SupervisionRequestFrame(student).setVisible(true));
        reviewBtn.addActionListener(e -> new PeerReviewFrame(student).setVisible(true));
        logoutBtn.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });

        btnPanel.add(proposalBtn);
        btnPanel.add(requestBtn);
        btnPanel.add(reviewBtn);
        btnPanel.add(logoutBtn);
        panel.add(btnPanel, BorderLayout.CENTER);

        add(panel);
    }
}
