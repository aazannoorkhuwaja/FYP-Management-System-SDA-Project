package view;

import controller.ComplaintController;
import model.Database;
import model.Complaint;
import model.Admin;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Complaint Review Frame refined with Monolithic Serenity theme.
 */
public class ComplaintReviewFrame extends JFrame {
    private model.User user;
    private JList<String> complaintList;
    private DefaultListModel<String> complaintListModel;

    public ComplaintReviewFrame(model.User user) {
        this.user = user;
        setTitle("Review Complaints");
        setSize(600, 500);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        Theme.applyTheme(panel);

        JLabel head = new JLabel("Pending Student Complaints");
        head.setFont(Theme.HEADING_FONT);
        head.setForeground(Theme.TEXT);
        panel.add(head, BorderLayout.NORTH);

        complaintListModel = new DefaultListModel<>();
        complaintList = new JList<>(complaintListModel);
        complaintList.setBackground(Theme.SURFACE);
        complaintList.setForeground(Theme.TEXT);
        complaintList.setSelectionBackground(Theme.SURFACE_2);
        complaintList.setFont(Theme.MONO_FONT);
        
        loadPendingComplaints();
        JScrollPane scroll = new JScrollPane(complaintList);
        Theme.styleScrollPane(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        panel.add(scroll, BorderLayout.CENTER);

        JButton resolveBtn = Theme.createPrimaryButton("Resolve Selected Complaint");
        resolveBtn.addActionListener(e -> resolveComplaint());
        panel.add(resolveBtn, BorderLayout.SOUTH);

        add(panel);
    }

    private void loadPendingComplaints() {
        complaintListModel.clear();
        ComplaintController cc = new ComplaintController();
        List<Complaint> complaints = cc.getPendingComplaints(user);
        if (complaints.isEmpty()) {
            complaintListModel.addElement("No pending complaints.");
            return;
        }
        for (Complaint c : complaints) {
            Database db = Database.getInstance();
            model.User student = db.findUserById(c.getStudentId());
            String name = student != null ? student.getName() : "Unknown";
            String preview = c.getDescription();
            if (preview.length() > 40) preview = preview.substring(0, 37) + "...";
            complaintListModel.addElement(c.getComplaintId() + " | " + name + ": " + preview);
        }
    }

    private void resolveComplaint() {
        String selected = complaintList.getSelectedValue();
        if (selected == null || selected.contains("No pending")) {
            JOptionPane.showMessageDialog(this, "Select a valid complaint first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String complaintId = selected.split(" \\| ")[0];
        ComplaintController cc = new ComplaintController();
        String result = cc.resolveComplaint(user, complaintId);
        if ("Success".equals(result)) {
            JOptionPane.showMessageDialog(this, "Complaint resolved!");
            loadPendingComplaints();
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
