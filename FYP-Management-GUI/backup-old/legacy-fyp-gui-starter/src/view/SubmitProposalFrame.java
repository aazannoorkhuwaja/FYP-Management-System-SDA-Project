package view;

import model.Database;
import model.Student;
import model.Proposal;
import model.Group;

import javax.swing.*;
import java.awt.*;

public class SubmitProposalFrame extends JFrame {
    private Student student;
    private JTextField titleField;
    private JTextField domainField;
    private JTextArea abstractArea;

    public SubmitProposalFrame(Student student) {
        this.student = student;
        setTitle("Submit FYP Proposal");
        setSize(450, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.add(new JLabel("Title:"));
        titleField = new JTextField();
        form.add(titleField);

        form.add(new JLabel("Domain:"));
        domainField = new JTextField();
        form.add(domainField);

        form.add(new JLabel("Abstract:"));
        abstractArea = new JTextArea(5, 20);
        form.add(new JScrollPane(abstractArea));

        panel.add(form, BorderLayout.CENTER);

        JButton submitBtn = new JButton("Submit Proposal");
        submitBtn.addActionListener(e -> submitProposal());
        panel.add(submitBtn, BorderLayout.SOUTH);

        add(panel);
    }

    private void submitProposal() {
        String title = titleField.getText().trim();
        String domain = domainField.getText().trim();
        String abstractText = abstractArea.getText().trim();

        if (title.isEmpty() || domain.isEmpty() || abstractText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Database db = Database.getInstance();
        Group group = db.findGroupById(student.getGroupId());
        if (group == null) {
            JOptionPane.showMessageDialog(this, "You are not assigned to a group.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String proposalId = "P" + System.currentTimeMillis();
        Proposal proposal = new Proposal(proposalId, title, domain, abstractText, "Pending Review", group.getGroupId());

        if (proposal.checkDuplicate(db.getProposals())) {
            JOptionPane.showMessageDialog(this, "A proposal with this title already exists. Please revise.", "Duplicate", JOptionPane.WARNING_MESSAGE);
            return;
        }

        proposal.submit();
        db.getProposals().add(proposal);
        group.setProjectTitle(title);
        db.saveToFile();

        JOptionPane.showMessageDialog(this, "Proposal submitted successfully!");
        this.dispose();
    }
}
