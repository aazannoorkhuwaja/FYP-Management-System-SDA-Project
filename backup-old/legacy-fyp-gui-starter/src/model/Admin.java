package model;

import java.io.Serializable;

public class Admin extends User implements Serializable {
    private String adminId;

    public Admin() {
        setRole("Admin");
    }

    public Admin(String userId, String name, String email, String password, String adminId) {
        super(userId, name, email, password, "Admin");
        this.adminId = adminId;
    }

    public void setGroupLimit() {
        // Stub: set supervisor group limit
    }

    public void publishRubric() {
        // Stub: publish grading rubric
    }

    public void manageComplaint() {
        // Stub: manage student complaints
    }

    public void sendInactivityAlert() {
        // Stub: send alert for inactive groups
    }

    public void manageUsers() {
        // Stub: add/remove users
    }

    public void viewAggregatedReviews() {
        // Stub: view peer review aggregates
    }

    public String getAdminId() { return adminId; }
    public void setAdminId(String adminId) { this.adminId = adminId; }
}
