package view;

import model.Database;
import model.Student;
import model.Supervisor;
import model.SupervisionRequest;
import model.Group;

import javax.swing.*;
import java.awt.*;

public class SupervisionRequestFrame extends JFrame {
    private Student student;
    private JComboBox<String> supervisorCombo;

    public SupervisionRequestFrame(Student student) {
        this.student = student;
        setTitle("Send Supervision Request");
        setSize(400, 250);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Select Supervisor:"), BorderLayout.NORTH);

        supervisorCombo = new JComboBox<>();
        Database db = Database.getInstance();
        for (model.User u : db.getUsers()) {
            if (u instanceof Supervisor) {
                Supervisor s = (Supervisor) u;
                supervisorCombo.addItem(s.getUserId() + " - " + s.getName() + " (" + s.getCurrentGroups() + "/" + s.getMaxGroups() + ")");
            }
        }
        panel.add(supervisorCombo, BorderLayout.CENTER);

        JButton sendBtn = new JButton("Send Request");
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
        Group group = db.findGroupById(student.getGroupId());
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
            if (r.getGroupId().equals(group.getGroupId()) && r.getSupervisorId().equals(supervisorId) && r.getStatus().equals("Pending")) {
                JOptionPane.showMessageDialog(this, "Request already pending.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String requestId = "R" + System.currentTimeMillis();
        SupervisionRequest request = new SupervisionRequest(requestId, group.getGroupId(), supervisorId);
        db.getRequests().add(request);
        db.saveToFile();

        JOptionPane.showMessageDialog(this, "Request sent to " + supervisor.getName() + "!");
        this.dispose();
    }
}
