package controller;

import model.Database;
import model.PeerReview;
import model.ProjectGroup;
import model.Student;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

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

        model.User reviewer = db.findUserById(reviewerId);
        model.User reviewee = db.findUserById(revieweeId);

        if (!(reviewer instanceof Student)) {
            return "Error: Reviewer is not a student.";
        }
        if (!(reviewee instanceof Student)) {
            return "Error: Reviewee is not a student.";
        }

        String reviewId = "PR" + System.currentTimeMillis();
        PeerReview review = new PeerReview(reviewId, (Student) reviewer, (Student) reviewee, Map.of("rating", rating));
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
        ProjectGroup group = db.findGroupById(groupId);
        if (group == null) return members;
        for (Student member : group.getMembers()) {
            if (!member.getUserId().equals(studentId)) {
                members.add(member);
            }
        }
        return members;
    }
}
