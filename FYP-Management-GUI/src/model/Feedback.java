package model;

import java.io.Serializable;
import java.util.Date;

/**
 * Feedback model expanded for compliance.
 */
public class Feedback implements Serializable {
    private static final long serialVersionUID = 1L;
    private String feedbackId;
    private String logId;
    private String supervisorId;
    private String comments;
    private Date timestamp;

    public Feedback(String feedbackId, String logId, String supervisorId, String comments, Date timestamp) {
        this.feedbackId = feedbackId;
        this.logId = logId;
        this.supervisorId = supervisorId;
        this.comments = comments;
        this.timestamp = timestamp;
    }

    // Constructor for Controller compatibility
    public Feedback(String feedbackId, String comments) {
        this(feedbackId, "LOG-UNSPECIFIED", "SUP-UNSPECIFIED", comments, new Date());
    }

    public void write() {
        // Mock write logic
    }

    // Getters and Setters
    public String getFeedbackId() { return feedbackId; }
    public String getLogId() { return logId; }
    public String getSupervisorId() { return supervisorId; }
    public String getComments() { return comments; }
    public Date getTimestamp() { return timestamp; }
}
