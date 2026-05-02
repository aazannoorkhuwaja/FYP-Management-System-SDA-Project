package view;

import model.Database;
import model.Student;
import model.FYPProposal;
import model.ProjectGroup;

import javax.swing.*;
import java.awt.*;

/**
 * Submit Proposal Frame refined with Monolithic Serenity theme.
 */
public class SubmitProposalFrame extends JFrame {
    private Student student;
    private JTextField titleField;
    private JTextField domainField;
    private JTextArea abstractArea;

    public SubmitProposalFrame(Student student) {
        this.student = student;
        setTitle("Submit FYP Proposal");
        setSize(550, 450);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        Theme.applyTheme(panel);

        JLabel head = new JLabel("Create New Proposal");
        head.setFont(Theme.HEADING_FONT);
        head.setForeground(Theme.TEXT);
        panel.add(head, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Theme.BG);

        form.add(createLabel("Project Title"));
        titleField = new JTextField();
        Theme.styleTextField(titleField);
        form.add(titleField);
        form.add(Box.createVerticalStrut(15));

        form.add(createLabel("Domain"));
        domainField = new JTextField();
        Theme.styleTextField(domainField);
        form.add(domainField);
        form.add(Box.createVerticalStrut(15));

        form.add(createLabel("Abstract / Description"));
        abstractArea = new JTextArea(6, 20);
        abstractArea.setBackground(Theme.SURFACE);
        abstractArea.setForeground(Theme.TEXT);
        abstractArea.setCaretColor(Theme.ACCENT);
        abstractArea.setFont(Theme.MAIN_FONT);
        abstractArea.setLineWrap(true);
        abstractArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(abstractArea);
        Theme.styleScrollPane(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        form.add(scroll);

        panel.add(form, BorderLayout.CENTER);

        JButton submitBtn = Theme.createPrimaryButton("Submit Proposal");
        submitBtn.addActionListener(e -> submitProposal());
        panel.add(submitBtn, BorderLayout.SOUTH);

        add(panel);
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.BOLD_FONT);
        l.setForeground(Theme.TEXT_MUTED);
        l.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        return l;
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
        ProjectGroup group = student.getGroup();
        if (group == null) {
            JOptionPane.showMessageDialog(this, "You are not assigned to a group.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String proposalId = "P" + System.currentTimeMillis();
        FYPProposal proposal = new FYPProposal(proposalId, title, domain, abstractText, "Pending Review", group);

        if (proposal.checkDuplicate(db.getProposals()) || db.isDuplicateProposal(title, null)) {
            JOptionPane.showMessageDialog(this, "A proposal with this title already exists in submitted proposals or archive. Please revise.", "Duplicate", JOptionPane.WARNING_MESSAGE);
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
