package controller;

import model.Database;
import model.Notification;
import model.ProjectGroup;
import model.ProgressLog;
import model.Student;
import model.User;
import model.Admin;
import util.DiagnosticService;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class AdminController {
    private Database db;
    private DiagnosticService diag;

    public AdminController() {
        this.db = Database.getInstance();
        this.diag = DiagnosticService.getInstance();
    }

    /**
     * Gap #2 — Sends inactivity alerts to groups that haven't submitted a log in over 7 days.
     * Also checks groups with NO logs at all.
     * 
     * [SECURITY] Only authorized Admins can trigger this method.
     */
    public List<String> sendInactivityAlerts(User caller) {
        diag.info("AdminController", "Inactivity alert process triggered by: " + (caller != null ? caller.getUserId() : "unknown"));
        
        if (!(caller instanceof Admin)) {
            diag.error("AdminController", "Unauthorized access attempt for inactivity alerts");
            throw new SecurityException("Unauthorized access: Administrative privileges required.");
        }
        
        List<String> alertedGroups = new ArrayList<>();
        long sevenDaysMs = 7L * 24 * 60 * 60 * 1000;
        Date now = new Date();

        for (ProjectGroup group : db.getGroups()) {
            List<ProgressLog> logs = db.getLogsForGroup(group.getGroupId());
            boolean inactive = false;

            if (logs.isEmpty()) {
                diag.trace("AdminController", "Group " + group.getGroupId() + " has no logs.");
                inactive = true;
            } else {
                // Find most recent log
                Date mostRecent = logs.get(0).getSubmittedDate();
                for (ProgressLog log : logs) {
                    if (log.getSubmittedDate().after(mostRecent)) {
                        mostRecent = log.getSubmittedDate();
                    }
                }
                if (now.getTime() - mostRecent.getTime() > sevenDaysMs) {
                    diag.trace("AdminController", "Group " + group.getGroupId() + " is inactive for >7 days.");
                    inactive = true;
                }
            }

            if (inactive) {
                alertedGroups.add(group.getGroupId() + " (" + group.getProjectTitle() + ")");
                // Send notification to all members
                for (Student student : group.getMembers()) {
                    String notifId = "ALERT-" + System.currentTimeMillis() + "-" + student.getUserId();
                    Notification notif = new Notification(notifId, student.getUserId(), "Inactivity Alert", 
                        "Your group has been flagged for inactivity. Please submit a progress log immediately.");
                    db.getNotifications().add(notif);
                }
            }
        }

        if (!alertedGroups.isEmpty()) {
            diag.info("AdminController", "Sent alerts to " + alertedGroups.size() + " groups.");
            db.saveToFile();
        } else {
            diag.info("AdminController", "No inactive groups found.");
        }
        return alertedGroups;
    }
}
