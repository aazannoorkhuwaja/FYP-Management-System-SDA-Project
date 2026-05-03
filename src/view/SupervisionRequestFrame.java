package view;

import model.Database;
import model.Student;
import model.Supervisor;
import model.SupervisionRequest;
import model.ProjectGroup;

import javax.swing.*;
import java.awt.*;

/**
 * Supervision request — centered narrow form.
 */
public class SupervisionRequestFrame extends JFrame {
    private Student student;
    private JComboBox<String> supervisorCombo;

    public SupervisionRequestFrame(Student student) {
        this.student = student;
        setTitle("Send Supervision Request");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));
        Theme.applyTheme(root);

        JPanel card = Theme.createFormCardShell();
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.weightx = 1;
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;

        JLabel head = new JLabel("Request Supervision");
        head.setFont(Theme.HEADING_FONT);
        head.setForeground(Theme.TEXT);
        g.gridy = 0;
        g.insets = new Insets(0, 0, 20, 0);
        card.add(head, g);

        JLabel label = new JLabel("Supervisor");
        label.setFont(Theme.BOLD_FONT);
        label.setForeground(Theme.TEXT_MUTED);
        g.gridy = 1;
        g.insets = new Insets(0, 0, 8, 0);
        card.add(label, g);

        supervisorCombo = new JComboBox<>();
        Theme.styleComboBox(supervisorCombo);
        supervisorCombo.setFont(Theme.MAIN_FONT);
        Theme.constrainComboWidth(supervisorCombo, Theme.FORM_FIELD_WIDTH);

        Database db = Database.getInstance();
        for (model.User u : db.getUsers()) {
            if (u instanceof Supervisor) {
                Supervisor s = (Supervisor) u;
                supervisorCombo.addItem(s.getUserId() + " - " + s.getName() + " | Response: " + String.format("%.1f", controller.RequestController.getAverageResponseTime(s.getUserId())) + "h | Capacity: " + s.getCurrentGroups() + "/" + s.getMaxGroups());
            }
        }

        g.gridy = 2;
        g.insets = new Insets(0, 0, 14, 0);
        card.add(supervisorCombo, g);

        JLabel hint = new JLabel("Tip: Open Supervisor Profiles to compare research areas.");
        hint.setFont(Theme.MONO_FONT);
        hint.setForeground(Theme.TEXT_MUTED);
        g.gridy = 3;
        g.insets = new Insets(0, 0, 0, 0);
        card.add(hint, g);

        root.add(Theme.wrapCentered(card), BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        south.setOpaque(false);
        JButton sendBtn = Theme.createPrimaryButton("Send Request");
        sendBtn.addActionListener(e -> sendRequest());
        south.add(sendBtn);
        root.add(south, BorderLayout.SOUTH);

        add(root);
        pack();
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

        for (SupervisionRequest r : db.getRequests()) {
            if (r.getGroup() != null && r.getSupervisor() != null
                    && r.getGroup().getGroupId().equals(group.getGroupId())
                    && r.getSupervisor().getUserId().equals(supervisor.getUserId())
                    && "Pending".equals(r.getStatus())) {
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
