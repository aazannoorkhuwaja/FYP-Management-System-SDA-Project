package view;

import model.Supervisor;
import model.Database;
import model.SupervisionRequest;

import javax.swing.*;
import java.awt.*;

public class SupervisorDashboardFrame extends JFrame {
    private Supervisor supervisor;

    public SupervisorDashboardFrame(Supervisor supervisor) {
        this.supervisor = supervisor;
        setTitle("Supervisor Dashboard - " + supervisor.getName());
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcome = new JLabel("Welcome, " + supervisor.getName() + " | Groups: " + supervisor.getCurrentGroups() + "/" + supervisor.getMaxGroups());
        welcome.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(welcome, BorderLayout.NORTH);

        // List of pending requests
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> requestList = new JList<>(listModel);
        JScrollPane scroll = new JScrollPane(requestList);

        Database db = Database.getInstance();
        for (SupervisionRequest r : db.getRequests()) {
            if (r.getSupervisorId().equals(supervisor.getUserId()) && r.getStatus().equals("Pending")) {
                listModel.addElement(r.getRequestId() + " | Group: " + r.getGroupId());
            }
        }

        panel.add(new JLabel("Pending Supervision Requests:"), BorderLayout.WEST);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton acceptBtn = new JButton("Accept Selected");
        JButton declineBtn = new JButton("Decline Selected");

        acceptBtn.addActionListener(e -> {
            String selected = requestList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a request first.");
                return;
            }
            String reqId = selected.split(" ")[0];
            for (SupervisionRequest r : db.getRequests()) {
                if (r.getRequestId().equals(reqId)) {
                    r.accept();
                    db.saveToFile();
                    JOptionPane.showMessageDialog(this, "Request accepted!");
                    listModel.removeElement(selected);
                    break;
                }
            }
        });

        declineBtn.addActionListener(e -> {
            String selected = requestList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a request first.");
                return;
            }
            String reason = JOptionPane.showInputDialog(this, "Enter rejection reason:");
            if (reason == null || reason.trim().isEmpty()) return;
            String reqId = selected.split(" ")[0];
            for (SupervisionRequest r : db.getRequests()) {
                if (r.getRequestId().equals(reqId)) {
                    r.decline(reason);
                    db.saveToFile();
                    JOptionPane.showMessageDialog(this, "Request declined.");
                    listModel.removeElement(selected);
                    break;
                }
            }
        });

        actionPanel.add(acceptBtn);
        actionPanel.add(declineBtn);
        panel.add(actionPanel, BorderLayout.SOUTH);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });
        panel.add(logoutBtn, BorderLayout.EAST);

        add(panel);
    }
}
