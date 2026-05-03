package view;

import model.Database;
import model.Student;
import model.PeerReview;
import model.ProjectGroup;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Peer review — centered form with aligned spinners.
 */
public class PeerReviewFrame extends JFrame {
    private Student student;
    private JComboBox<String> memberCombo;
    private JSpinner commSpinner;
    private JSpinner qualitySpinner;
    private JSpinner timeSpinner;

    public PeerReviewFrame(Student student) {
        this.student = student;
        setTitle("Submit Peer Review");
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

        JLabel head = new JLabel("Member Peer Review");
        head.setFont(Theme.HEADING_FONT);
        head.setForeground(Theme.TEXT);
        g.gridy = 0;
        g.insets = new Insets(0, 0, 20, 0);
        card.add(head, g);

        g.gridy = 1;
        g.insets = new Insets(0, 0, 8, 0);
        card.add(createLabel("Team member"), g);

        memberCombo = new JComboBox<>();
        Theme.styleComboBox(memberCombo);
        memberCombo.setFont(Theme.MAIN_FONT);
        Theme.constrainComboWidth(memberCombo, Theme.FORM_FIELD_WIDTH);

        Database db = Database.getInstance();
        ProjectGroup group = student.getGroup();
        if (group != null) {
            for (Student member : group.getMembers()) {
                if (!member.getUserId().equals(student.getUserId())) {
                    memberCombo.addItem(member.getUserId() + " - " + member.getName());
                }
            }
        }

        g.gridy = 2;
        g.insets = new Insets(0, 0, 18, 0);
        card.add(memberCombo, g);

        int spinW = 120;
        g.gridy = 3;
        g.insets = new Insets(0, 0, 8, 0);
        card.add(createLabel("Communication (1–5)"), g);
        commSpinner = createStyledSpinner();
        Theme.constrainSpinnerWidth(commSpinner, spinW);
        g.gridy = 4;
        g.insets = new Insets(0, 0, 14, 0);
        card.add(commSpinner, g);

        g.gridy = 5;
        g.insets = new Insets(0, 0, 8, 0);
        card.add(createLabel("Work Quality (1–5)"), g);
        qualitySpinner = createStyledSpinner();
        Theme.constrainSpinnerWidth(qualitySpinner, spinW);
        g.gridy = 6;
        g.insets = new Insets(0, 0, 14, 0);
        card.add(qualitySpinner, g);

        g.gridy = 7;
        g.insets = new Insets(0, 0, 8, 0);
        card.add(createLabel("Timeliness (1–5)"), g);
        timeSpinner = createStyledSpinner();
        Theme.constrainSpinnerWidth(timeSpinner, spinW);
        g.gridy = 8;
        g.insets = new Insets(0, 0, 0, 0);
        card.add(timeSpinner, g);

        root.add(Theme.wrapCentered(card), BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        south.setOpaque(false);
        JButton submitBtn = Theme.createPrimaryButton("Submit Anonymous Review");
        submitBtn.addActionListener(e -> submitReview());
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

    private JSpinner createStyledSpinner() {
        JSpinner s = new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
        Theme.styleSpinner(s);
        return s;
    }

    private void submitReview() {
        String selected = (String) memberCombo.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "No member selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String revieweeId = selected.split(" - ")[0];
        int comm = (Integer) commSpinner.getValue();
        int quality = (Integer) qualitySpinner.getValue();
        int time = (Integer) timeSpinner.getValue();
        int rating = (comm + quality + time) / 3;

        Database db = Database.getInstance();
        String reviewId = "PR" + System.currentTimeMillis();
        Student reviewee = (Student) db.findUserById(revieweeId);
        if (reviewee == null) {
            JOptionPane.showMessageDialog(this, "Selected student not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        PeerReview review = new PeerReview(reviewId, student, reviewee, Map.of(
            "communication", comm,
            "quality", quality,
            "timeliness", time,
            "rating", rating
        ));
        review.submit();
        db.getPeerReviews().add(review);
        db.saveToFile();

        JOptionPane.showMessageDialog(this, "Peer review submitted anonymously!");
        this.dispose();
    }
}
