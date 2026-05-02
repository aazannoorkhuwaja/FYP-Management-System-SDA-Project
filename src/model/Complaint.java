package model;

import java.io.Serializable;
import java.util.Date;

/**
 * Complaint model expanded for compliance.
 */
public class Complaint implements Serializable {
    private static final long serialVersionUID = 1L;
    private String complaintId;
    private String studentId;
    private String description;
    private String status;
    private Date submittedDate;
    private String resolutionNotes;

    public Complaint(String complaintId, String studentId, String description, Date submittedDate) {
        this.complaintId = complaintId;
        this.studentId = studentId;
        this.description = description;
        this.submittedDate = submittedDate;
        this.status = "Pending";
    }

    // Constructor for Controller compatibility
    public Complaint(String complaintId, String studentId, String description) {
        this(complaintId, studentId, description, new Date());
    }

    public void submit() {
        // Logic for submission
    }

    // Getters and Setters
    public String getComplaintId() { return complaintId; }
    public String getStudentId() { return studentId; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getSubmittedDate() { return submittedDate; }
    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String notes) { this.resolutionNotes = notes; } public boolean isResolved() { return "Resolved".equalsIgnoreCase(status); }
}
