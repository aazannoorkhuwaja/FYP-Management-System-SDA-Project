package view;

import model.Database;
import model.Student;
import model.Supervisor;
import model.SupervisionRequest;
import model.ProjectGroup;

import javax.swing.*;
import java.awt.*;

/**
 * Supervision Request Frame refined with Monolithic Serenity theme.
 */
public class SupervisionRequestFrame extends JFrame {
    private Student student;
    private JComboBox<String> supervisorCombo;

    public SupervisionRequestFrame(Student student) {
        this.student = student;
        setTitle("Send Supervision Request");
        setSize(550, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        Theme.applyTheme(panel);

        JLabel head = new JLabel("Request Supervision");
        head.setFont(Theme.HEADING_FONT);
        head.setForeground(Theme.TEXT);
        panel.add(head, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG);

        JLabel label = new JLabel("Select Supervisor:");
        label.setFont(Theme.BOLD_FONT);
        label.setForeground(Theme.TEXT_MUTED);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        content.add(label);

        supervisorCombo = new JComboBox<>();
        supervisorCombo.setBackground(Theme.SURFACE);
        supervisorCombo.setForeground(Theme.TEXT);
        supervisorCombo.setFont(Theme.MAIN_FONT);
        
        Database db = Database.getInstance();
        for (model.User u : db.getUsers()) {
            if (u instanceof Supervisor) {
                Supervisor s = (Supervisor) u;
                supervisorCombo.addItem(s.getUserId() + " - " + s.getName() + " | Response: " + String.format("%.1f", s.getAvgResponseTime()) + "h | Capacity: " + s.getCurrentGroups() + "/" + s.getMaxGroups());
            }
        }
        content.add(supervisorCombo);
        content.add(Box.createVerticalStrut(10));
        
        JLabel hint = new JLabel("Tip: Browse profiles to see research areas.");
        hint.setFont(Theme.MONO_FONT);
        hint.setForeground(Theme.ACCENT);
        content.add(hint);

        panel.add(content, BorderLayout.CENTER);

        JButton sendBtn = Theme.createPrimaryButton("Send Request");
        sendBtn.addActionListener(e -> sendRequest());
        panel.add(sendBtn, BorderLayout.SOUTH);

        add(panel);
    }

    private void sendRequest() {
        String selected = (String) supervisorCombo.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "No supervisor selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String supervisorId = selected.split(" - ")[0];
        Database db = Database.getInstance();
        ProjectGroup group = student.getGroup();
        if (group == null) {
            JOptionPane.showMessageDialog(this, "You have no group.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Supervisor supervisor = null;
        for (model.User u : db.getUsers()) {
            if (u instanceof Supervisor && u.getUserId().equals(supervisorId)) {
                supervisor = (Supervisor) u;
                break;
            }
        }

        if (supervisor == null) {
            JOptionPane.showMessageDialog(this, "Supervisor not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (supervisor.getCurrentGroups() >= supervisor.getMaxGroups()) {
            JOptionPane.showMessageDialog(this, "Supervisor has reached maximum group limit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if request already exists
        for (SupervisionRequest r : db.getRequests()) {
            if (r.getGroup().getGroupId().equals(group.getGroupId()) && r.getSupervisor().getUserId().equals(supervisor.getUserId()) && r.getStatus().equals("Pending")) {
                JOptionPane.showMessageDialog(this, "Request already pending.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String requestId = "R" + System.currentTimeMillis();
        SupervisionRequest request = new SupervisionRequest(requestId, group, supervisor);
        db.getRequests().add(request);
        db.saveToFile();

        JOptionPane.showMessageDialog(this, "Request sent to " + supervisor.getName() + "!");
        this.dispose();
    }
}
