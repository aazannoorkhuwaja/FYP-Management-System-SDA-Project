package model;

import java.io.Serializable;
import java.util.Date;

/**
 * Progress Log model with full support for getWeekNumber and other required methods.
 */
public class ProgressLog implements Serializable {
    private static final long serialVersionUID = 1L;
    private String logId;
    private ProjectGroup group;
    private int weekNumber;
    private String content;
    private String individualContributions;
    private String supervisorFeedback;
    private Date submittedDate;
    private Date feedbackDate;
    private boolean isSubmitted;

    public ProgressLog(String logId, ProjectGroup group, int weekNumber, String content, String individualContributions) {
        this.logId = logId;
        this.group = group;
        this.weekNumber = weekNumber;
        this.content = content;
        this.individualContributions = individualContributions;
        this.isSubmitted = false;
    }

    // Secondary constructor for controller flexibility
    public ProgressLog(String logId, ProjectGroup group, int weekNumber, String content, String individualContributions, Date date) {
        this(logId, group, weekNumber, content, individualContributions);
        this.submittedDate = date;
        this.isSubmitted = (date != null);
    }

    public void submit() {
        this.isSubmitted = true;
        this.submittedDate = new Date();
    }

    // Getters and Setters
    public String getLogId() { return logId; }
    public ProjectGroup getGroup() { return group; }
    public int getWeekNumber() { return weekNumber; }
    public String getContent() { return content; }
    public String getIndividualContributions() { return individualContributions; }
    public String getSupervisorFeedback() { return supervisorFeedback; }
    public Date getSubmittedDate() { return submittedDate; }
    public Date getFeedbackDate() { return feedbackDate; }
    public boolean isSubmitted() { return isSubmitted; }

    public void setSupervisorFeedback(String supervisorFeedback) { this.supervisorFeedback = supervisorFeedback; }
    public void setFeedbackDate(Date feedbackDate) { this.feedbackDate = feedbackDate; }
}
