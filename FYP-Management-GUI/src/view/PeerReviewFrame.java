package view;

import model.Database;
import model.Student;
import model.PeerReview;
import model.ProjectGroup;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Peer Review Frame refined with Monolithic Serenity theme.
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
        setSize(500, 450);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        Theme.applyTheme(panel);

        JLabel head = new JLabel("Member Peer Review");
        head.setFont(Theme.HEADING_FONT);
        head.setForeground(Theme.TEXT);
        panel.add(head, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Theme.BG);

        form.add(createLabel("Select Member to Review"));
        memberCombo = new JComboBox<>();
        memberCombo.setBackground(Theme.SURFACE);
        memberCombo.setForeground(Theme.TEXT);
        memberCombo.setFont(Theme.MAIN_FONT);
        
        Database db = Database.getInstance();
        ProjectGroup group = student.getGroup();
        if (group != null) {
            for (Student member : group.getMembers()) {
                if (!member.getUserId().equals(student.getUserId())) {
                    memberCombo.addItem(member.getUserId() + " - " + member.getName());
                }
            }
        }
        form.add(memberCombo);
        form.add(Box.createVerticalStrut(20));

        form.add(createLabel("Communication (1-5)"));
        commSpinner = createStyledSpinner();
        form.add(commSpinner);
        form.add(Box.createVerticalStrut(15));

        form.add(createLabel("Work Quality (1-5)"));
        qualitySpinner = createStyledSpinner();
        form.add(qualitySpinner);
        form.add(Box.createVerticalStrut(15));

        form.add(createLabel("Timeliness (1-5)"));
        timeSpinner = createStyledSpinner();
        form.add(timeSpinner);

        panel.add(form, BorderLayout.CENTER);

        JButton submitBtn = Theme.createPrimaryButton("Submit Anonymous Review");
        submitBtn.addActionListener(e -> submitReview());
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

    private JSpinner createStyledSpinner() {
        JSpinner s = new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
        s.setBackground(Theme.SURFACE);
        s.setForeground(Theme.TEXT);
        s.setFont(Theme.MAIN_FONT);
        JComponent editor = s.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField txt = ((JSpinner.DefaultEditor) editor).getTextField();
            txt.setBackground(Theme.SURFACE);
            txt.setForeground(Theme.TEXT);
            txt.setCaretColor(Theme.ACCENT);
        }
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
