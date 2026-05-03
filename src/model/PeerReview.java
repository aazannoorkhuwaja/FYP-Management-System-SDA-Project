package model;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

public class PeerReview implements Serializable {
    private String reviewId;
    private Student reviewer;
    private Student reviewee;
    private Map<String, Integer> ratings;
    private boolean visibleToGroup;

    public PeerReview() {
        this.visibleToGroup = false;
        this.ratings = new HashMap<>();
    }

    public PeerReview(String reviewId, Student reviewer, Student reviewee, Map<String, Integer> ratings) {
        this.reviewId = reviewId;
        this.reviewer = reviewer;
        this.reviewee = reviewee;
        this.ratings = ratings != null ? ratings : new HashMap<>();
        this.visibleToGroup = false; // private by default
    }

    public void submit() {
        // Saved to Database by controller
    }

    public float getAggregated() {
        if (ratings == null || ratings.isEmpty()) return 0f;
        int sum = 0;
        for (int value : ratings.values()) {
            sum += value;
        }
        return (float) sum / ratings.size();
    }

    // Getters and Setters
    public String getReviewId() { return reviewId; }
    public void setReviewId(String reviewId) { this.reviewId = reviewId; }
    public Student getReviewer() { return reviewer; }
    public void setReviewer(Student reviewer) { this.reviewer = reviewer; }
    public Student getReviewee() { return reviewee; }
    public void setReviewee(Student reviewee) { this.reviewee = reviewee; }
    public Map<String, Integer> getRatings() { return ratings; }
    public void setRatings(Map<String, Integer> ratings) { this.ratings = ratings; }
    public boolean isVisibleToGroup() { return visibleToGroup; }
    public void setVisibleToGroup(boolean visibleToGroup) { this.visibleToGroup = visibleToGroup; }
}
