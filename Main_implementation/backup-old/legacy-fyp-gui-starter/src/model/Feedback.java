package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Feedback implements Serializable {
    private String feedbackId;
    private String content;
    private Date timestamp;

    public Feedback() {
        this.timestamp = new Date();
    }

    public Feedback(String feedbackId, String content) {
        this.feedbackId = feedbackId;
        this.content = content;
        this.timestamp = new Date();
    }

    public void write() {
        this.timestamp = new Date();
    }

    public static List<Feedback> getHistory(List<Feedback> allFeedback, String relatedId) {
        List<Feedback> history = new ArrayList<>();
        for (Feedback f : allFeedback) {
            if (f.getFeedbackId().startsWith(relatedId)) {
                history.add(f);
            }
        }
        return history;
    }

    public String getFeedbackId() { return feedbackId; }
    public void setFeedbackId(String feedbackId) { this.feedbackId = feedbackId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}
