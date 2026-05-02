package view;

import controller.ComplaintController;
import model.Database;
import model.Student;

import javax.swing.*;
import java.awt.*;

/**
 * Complaint Frame refined with Monolithic Serenity theme.
 */
public class ComplaintFrame extends JFrame {
    private Student student;
    private JTextArea complaintArea;

    public ComplaintFrame(Student student) {
        this.student = student;
        setTitle("Submit Complaint");
        setSize(500, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        Theme.applyTheme(panel);

        JLabel head = new JLabel("Describe your complaint");
        Theme.styleLabel(head, true);
        panel.add(head, BorderLayout.NORTH);

        complaintArea = new JTextArea(10, 30);
        complaintArea.setBackground(Theme.SURFACE);
        complaintArea.setForeground(Theme.TEXT);
        complaintArea.setCaretColor(Theme.ACCENT);
        complaintArea.setFont(Theme.MAIN_FONT);
        
        JScrollPane scroll = new JScrollPane(complaintArea);
        Theme.styleScrollPane(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        panel.add(scroll, BorderLayout.CENTER);

        JButton submitBtn = Theme.createPrimaryButton("Submit Formal Complaint");
        submitBtn.addActionListener(e -> submitComplaint());
        panel.add(submitBtn, BorderLayout.SOUTH);

        add(panel);
    }

    private void submitComplaint() {
        String desc = complaintArea.getText().trim();
        if (desc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complaint description cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ComplaintController cc = new ComplaintController();
        String result = cc.submitComplaint(student.getUserId(), desc);
        if ("Success".equals(result)) {
            JOptionPane.showMessageDialog(this, "Complaint submitted!");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
