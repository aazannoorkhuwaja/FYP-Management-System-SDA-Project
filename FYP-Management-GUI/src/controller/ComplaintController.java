package controller;

import model.Database;
import model.Complaint;
import model.Student;
import model.User;
import java.util.List;

public class ComplaintController {
    private Database db;

    public ComplaintController() {
        this.db = Database.getInstance();
    }

    public String submitComplaint(String studentId, String description) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return "Error: Student ID is required.";
        }
        if (description == null || description.trim().isEmpty()) {
            return "Error: Description is required.";
        }

        User user = db.findUserById(studentId);
        if (user == null || !(user instanceof Student)) {
            return "Error: Student not found.";
        }

        String complaintId = "C" + System.currentTimeMillis();
        Complaint complaint = new Complaint(complaintId, studentId, description.trim());
        complaint.submit();

        db.getComplaints().add(complaint);
        db.saveToFile();
        return "Success";
    }

    public String resolveComplaint(User caller, String complaintId) {
        if (!(caller instanceof model.Admin)) {
            return "Error: Unauthorized. Only admins can resolve complaints.";
        }
        if (complaintId == null || complaintId.trim().isEmpty()) {
            return "Error: Complaint ID is required.";
        }

        for (Complaint c : db.getComplaints()) {
            if (c.getComplaintId().equals(complaintId)) {
                c.setStatus("Resolved");
                db.saveToFile();
                return "Success";
            }
        }
        return "Error: Complaint not found.";
    }

    public List<Complaint> getPendingComplaints(User caller) {
        if (!(caller instanceof model.Admin)) {
            return new java.util.ArrayList<>();
        }
        return db.getPendingComplaints();
    }
}
