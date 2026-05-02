package model;

import java.io.Serializable;
import java.util.Date;

public class ProgressLog implements Serializable {
    private String logId;
    private int weekNumber;
    private String content;
    private Date submittedDate;

    public ProgressLog() {
        this.submittedDate = new Date();
    }

    public ProgressLog(String logId, int weekNumber, String content) {
        this.logId = logId;
        this.weekNumber = weekNumber;
        this.content = content;
        this.submittedDate = new Date();
    }

    public void submit() {
        this.submittedDate = new Date();
    }

    public boolean checkInactivity(Date lastSubmit, int thresholdDays) {
        long diff = new Date().getTime() - lastSubmit.getTime();
        long days = diff / (1000 * 60 * 60 * 24);
        return days > thresholdDays;
    }

    // Getters and Setters
    public String getLogId() { return logId; }
    public void setLogId(String logId) { this.logId = logId; }
    public int getWeekNumber() { return weekNumber; }
    public void setWeekNumber(int weekNumber) { this.weekNumber = weekNumber; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Date getSubmittedDate() { return submittedDate; }
    public void setSubmittedDate(Date submittedDate) { this.submittedDate = submittedDate; }
}
