package view;

import model.Database;
import model.Student;
import model.FYPProposal;
import model.ProjectGroup;

import javax.swing.*;
import java.awt.*;

/**
 * Submit proposal — centered form card, constrained field widths.
 */
public class SubmitProposalFrame extends JFrame {
    private Student student;
    private JTextField titleField;
    private JTextField domainField;
    private JTextArea abstractArea;

    public SubmitProposalFrame(Student student) {
        this.student = student;
        setTitle("Submit FYP Proposal");
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
        int row = 0;

        JLabel titleHead = new JLabel("Create New Proposal");
        titleHead.setFont(Theme.HEADING_FONT);
        titleHead.setForeground(Theme.TEXT);
        g.gridy = row++;
        g.insets = new Insets(0, 0, 22, 0);
        card.add(titleHead, g);

        g.insets = new Insets(0, 0, 8, 0);
        g.gridy = row++;
        card.add(createLabel("Project Title"), g);

        titleField = new JTextField();
        Theme.styleTextField(titleField);
        Theme.constrainFormFieldWidth(titleField, Theme.FORM_FIELD_WIDTH);
        g.gridy = row++;
        g.insets = new Insets(0, 0, 14, 0);
        card.add(titleField, g);

        g.gridy = row++;
        g.insets = new Insets(0, 0, 8, 0);
        card.add(createLabel("Domain"), g);

        domainField = new JTextField();
        Theme.styleTextField(domainField);
        Theme.constrainFormFieldWidth(domainField, Theme.FORM_FIELD_WIDTH);
        g.gridy = row++;
        g.insets = new Insets(0, 0, 14, 0);
        card.add(domainField, g);

        g.gridy = row++;
        g.insets = new Insets(0, 0, 8, 0);
        card.add(createLabel("Abstract / Description"), g);

        abstractArea = new JTextArea(5, 32);
        Theme.styleTextArea(abstractArea);
        JScrollPane scroll = new JScrollPane(abstractArea);
        Theme.styleScrollPane(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        scroll.setPreferredSize(new Dimension(Theme.FORM_FIELD_WIDTH, 140));
        g.gridy = row++;
        g.fill = GridBagConstraints.BOTH;
        g.weighty = 1;
        g.insets = new Insets(0, 0, 0, 0);
        card.add(scroll, g);

        root.add(Theme.wrapCentered(card), BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        south.setOpaque(false);
        JButton submitBtn = Theme.createPrimaryButton("Submit Proposal");
        submitBtn.addActionListener(e -> submitProposal());
        south.add(submitBtn);
        root.add(south, BorderLayout.SOUTH);

        add(root);
        pack();
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.BOLD_FONT);
        l.setForeground(Theme.TEXT_MUTED);
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
