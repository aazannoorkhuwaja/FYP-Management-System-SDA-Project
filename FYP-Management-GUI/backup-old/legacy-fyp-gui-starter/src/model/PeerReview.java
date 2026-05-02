package model;

import java.io.Serializable;

public class PeerReview implements Serializable {
    private String reviewId;
    private String revieweeId;
    private int rating;
    private boolean isPrivate;
    private String reviewerId;

    public PeerReview() {
        this.isPrivate = true;
    }

    public PeerReview(String reviewId, String reviewerId, String revieweeId, int rating) {
        this.reviewId = reviewId;
        this.reviewerId = reviewerId;
        this.revieweeId = revieweeId;
        this.rating = rating;
        this.isPrivate = true;
    }

    public void submit() {
        // Saved to Database by controller
    }

    public float getAggregated() {
        // Stub: aggregation done at controller/service layer
        return rating;
    }

    // Getters and Setters
    public String getReviewId() { return reviewId; }
    public void setReviewId(String reviewId) { this.reviewId = reviewId; }
    public String getRevieweeId() { return revieweeId; }
    public void setRevieweeId(String revieweeId) { this.revieweeId = revieweeId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public boolean isPrivate() { return isPrivate; }
    public void setPrivate(boolean isPrivate) { this.isPrivate = isPrivate; }
    public String getReviewerId() { return reviewerId; }
    public void setReviewerId(String reviewerId) { this.reviewerId = reviewerId; }
}
