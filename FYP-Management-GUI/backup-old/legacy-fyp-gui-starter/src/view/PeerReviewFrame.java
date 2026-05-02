package view;

import model.Database;
import model.Student;
import model.PeerReview;
import model.Group;

import javax.swing.*;
import java.awt.*;

public class PeerReviewFrame extends JFrame {
    private Student student;
    private JComboBox<String> memberCombo;
    private JSpinner ratingSpinner;

    public PeerReviewFrame(Student student) {
        this.student = student;
        setTitle("Submit Peer Review");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Rate a Group Member (1-5):"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.add(new JLabel("Select Member:"));
        memberCombo = new JComboBox<>();
        Database db = Database.getInstance();
        Group group = db.findGroupById(student.getGroupId());
        if (group != null) {
            for (String memberId : group.getMemberIds()) {
                if (!memberId.equals(student.getUserId())) {
                    for (model.User u : db.getUsers()) {
                        if (u.getUserId().equals(memberId)) {
                            memberCombo.addItem(memberId + " - " + u.getName());
                            break;
                        }
                    }
                }
            }
        }
        form.add(memberCombo);

        form.add(new JLabel("Rating (1-5):"));
        ratingSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
        form.add(ratingSpinner);

        panel.add(form, BorderLayout.CENTER);

        JButton submitBtn = new JButton("Submit Review");
        submitBtn.addActionListener(e -> submitReview());
        panel.add(submitBtn, BorderLayout.SOUTH);

        add(panel);
    }

    private void submitReview() {
        String selected = (String) memberCombo.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "No member selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String revieweeId = selected.split(" - ")[0];
        int rating = (Integer) ratingSpinner.getValue();

        Database db = Database.getInstance();
        String reviewId = "PR" + System.currentTimeMillis();
        PeerReview review = new PeerReview(reviewId, student.getUserId(), revieweeId, rating);
        review.submit();
        db.getPeerReviews().add(review);
        db.saveToFile();

        JOptionPane.showMessageDialog(this, "Peer review submitted anonymously!");
        this.dispose();
    }
}
