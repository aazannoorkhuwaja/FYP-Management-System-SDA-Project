package model;

import java.io.Serializable;

public class SupervisionRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String requestId;
    private ProjectGroup group;
    private Supervisor supervisor;
    private String status;
    private String reason;
    private long requestDate;
    private long responseDate;

    public SupervisionRequest() {
        this.status = "Pending";
        this.requestDate = System.currentTimeMillis();
    }

    public SupervisionRequest(String requestId, ProjectGroup group, Supervisor supervisor) {
        this.requestId = requestId;
        this.group = group;
        this.supervisor = supervisor;
        this.status = "Pending";
        this.reason = "";
        this.requestDate = System.currentTimeMillis();
    }

    public void accept() {
        this.status = "Accepted";
        this.reason = "";
        this.responseDate = System.currentTimeMillis();
    }

    public void decline(String reason) {
        this.status = "Declined";
        this.reason = reason;
        this.responseDate = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public ProjectGroup getGroup() { return group; }
    public void setGroup(ProjectGroup group) { this.group = group; }
    public Supervisor getSupervisor() { return supervisor; }
    public void setSupervisor(Supervisor supervisor) { this.supervisor = supervisor; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public long getRequestDate() { return requestDate; }
    public void setRequestDate(long requestDate) { this.requestDate = requestDate; }
    public long getResponseDate() { return responseDate; }
    public void setResponseDate(long responseDate) { this.responseDate = responseDate; }
}
