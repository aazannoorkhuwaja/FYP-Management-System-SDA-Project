package model;

import java.io.Serializable;

public class SupervisionRequest implements Serializable {
    private String requestId;
    private String groupId;
    private String supervisorId;
    private String status;
    private String reason;

    public SupervisionRequest() {
        this.status = "Pending";
    }

    public SupervisionRequest(String requestId, String groupId, String supervisorId) {
        this.requestId = requestId;
        this.groupId = groupId;
        this.supervisorId = supervisorId;
        this.status = "Pending";
        this.reason = "";
    }

    public void accept() {
        this.status = "Accepted";
        this.reason = "";
    }

    public void decline(String reason) {
        this.status = "Declined";
        this.reason = reason;
    }

    // Getters and Setters
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }
    public String getSupervisorId() { return supervisorId; }
    public void setSupervisorId(String supervisorId) { this.supervisorId = supervisorId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
