package model;

import java.io.Serializable;

public class Complaint implements Serializable {
    private String complaintId;
    private String studentId;
    private String description;
    private String status;

    public Complaint() {
        this.status = "Submitted";
    }

    public Complaint(String complaintId, String studentId, String description) {
        this.complaintId = complaintId;
        this.studentId = studentId;
        this.description = description;
        this.status = "Submitted";
    }

    public void submit() {
        this.status = "Submitted";
    }

    public void escalate() {
        this.status = "Escalated";
    }

    // Getters and Setters
    public String getComplaintId() { return complaintId; }
    public void setComplaintId(String complaintId) { this.complaintId = complaintId; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
