package view;

import controller.ComplaintController;
import model.Student;

import javax.swing.*;
import java.awt.*;

/**
 * Complaint submission — centered form card.
 */
public class ComplaintFrame extends JFrame {
    private Student student;
    private JTextArea complaintArea;

    public ComplaintFrame(Student student) {
        this.student = student;
        setTitle("Submit Complaint");
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

        JLabel head = new JLabel("Describe your complaint");
        Theme.styleLabel(head, true);
        g.gridy = 0;
        g.insets = new Insets(0, 0, 14, 0);
        card.add(head, g);

        JLabel sub = new JLabel("Provide enough detail for the coordinator to investigate.");
        sub.setFont(Theme.MAIN_FONT);
        sub.setForeground(Theme.TEXT_MUTED);
        g.gridy = 1;
        g.insets = new Insets(0, 0, 12, 0);
        card.add(sub, g);

        complaintArea = new JTextArea(8, 32);
        Theme.styleTextArea(complaintArea);
        JScrollPane scroll = new JScrollPane(complaintArea);
        Theme.styleScrollPane(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        scroll.setPreferredSize(new Dimension(Theme.FORM_FIELD_WIDTH, 180));
        g.gridy = 2;
        g.fill = GridBagConstraints.BOTH;
        g.weighty = 1;
        g.insets = new Insets(0, 0, 0, 0);
        card.add(scroll, g);

        root.add(Theme.wrapCentered(card), BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        south.setOpaque(false);
        JButton submitBtn = Theme.createPrimaryButton("Submit Formal Complaint");
        submitBtn.addActionListener(e -> submitComplaint());
        south.add(submitBtn);
        root.add(south, BorderLayout.SOUTH);

        add(root);
        pack();
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
