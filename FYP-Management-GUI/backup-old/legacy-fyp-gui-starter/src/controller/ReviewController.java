package controller;

import model.Database;
import model.PeerReview;
import model.Group;
import model.Student;
import java.util.List;
import java.util.ArrayList;

public class ReviewController {
    private Database db;

    public ReviewController() {
        this.db = Database.getInstance();
    }

    public String submitReview(String reviewerId, String revieweeId, int rating) {
        if (rating < 1 || rating > 5) {
            return "Error: Rating must be between 1 and 5.";
        }
        if (reviewerId.equals(revieweeId)) {
            return "Error: Cannot review yourself.";
        }

        String reviewId = "PR" + System.currentTimeMillis();
        PeerReview review = new PeerReview(reviewId, reviewerId, revieweeId, rating);
        review.submit();
        db.getPeerReviews().add(review);
        db.saveToFile();
        return "Success";
    }

    public List<PeerReview> getReviewsForGroup(String groupId) {
        return db.findReviewsForGroup(groupId);
    }

    public List<Student> getGroupMembersExcept(String groupId, String studentId) {
        List<Student> members = new ArrayList<>();
        Group group = db.findGroupById(groupId);
        if (group == null) return members;
        for (String memberId : group.getMemberIds()) {
            if (!memberId.equals(studentId)) {
                for (model.User u : db.getUsers()) {
                    if (u instanceof Student && u.getUserId().equals(memberId)) {
                        members.add((Student) u);
                        break;
                    }
                }
            }
        }
        return members;
    }
}
