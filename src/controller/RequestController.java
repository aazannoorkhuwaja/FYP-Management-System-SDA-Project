package controller;

import model.Database;
import model.SupervisionRequest;
import model.Supervisor;
import model.ProjectGroup;

public class RequestController {
    private Database db;

    public RequestController() {
        this.db = Database.getInstance();
    }

    public String sendRequest(String groupId, String supervisorId) {
        ProjectGroup group = db.findGroupById(groupId);
        if (group == null) return "Error: ProjectGroup not found.";

        Supervisor supervisor = findSupervisor(supervisorId);
        if (supervisor == null) return "Error: Supervisor not found.";

        if (supervisor.getCurrentGroups() >= supervisor.getMaxGroups()) {
            return "Error: Supervisor has reached maximum group limit.";
        }

        for (SupervisionRequest r : db.getRequests()) {
            if (r.getGroup() != null && r.getGroup().getGroupId().equals(groupId) && r.getSupervisor() != null && r.getSupervisor().getUserId().equals(supervisorId) && r.getStatus().equals("Pending")) {
                return "Error: Request already pending.";
            }
        }

        String requestId = "R" + System.currentTimeMillis();
        SupervisionRequest request = new SupervisionRequest(requestId, group, supervisor);
        db.getRequests().add(request);
        db.saveToFile();
        return "Success";
    }

    public String acceptRequest(String requestId) {
        for (SupervisionRequest r : db.getRequests()) {
            if (r.getRequestId().equals(requestId) && r.getStatus().equals("Pending")) {
                r.accept();
                // Update group supervisor
                ProjectGroup g = db.findGroupById(r.getGroup().getGroupId());
                if (g != null) g.setSupervisor(r.getSupervisor());
                // Update supervisor count & response time
                Supervisor s = findSupervisor(r.getSupervisor().getUserId());
                if (s != null) {
                    s.setCurrentGroups(s.getCurrentGroups() + 1);
                    
                    // FR-10 / UC-02: Notify student
                    if (g != null && !g.getMembers().isEmpty()) {
                        String msg = "Your request to " + s.getName() + " was accepted!";
                        model.Notification notif = new model.Notification("N" + System.currentTimeMillis(),
                                g.getMembers().get(0).getUserId(), "RequestUpdate", msg);
                        db.getNotifications().add(notif);
                    }
                }
                db.saveToFile();
                return "Success";
            }
        }
        return "Error: Request not found.";
    }

    public String declineRequest(String requestId, String reason) {
        if (reason == null || reason.trim().isEmpty()) return "Error: Reason is required.";
        for (SupervisionRequest r : db.getRequests()) {
            if (r.getRequestId().equals(requestId) && r.getStatus().equals("Pending")) {
                r.decline(reason);
                Supervisor s = findSupervisor(r.getSupervisor().getUserId());
                if (s != null) {
                    // FR-10 / UC-02: Notify student
                    ProjectGroup g = r.getGroup();
                    if (g != null && g.getMembers() != null && !g.getMembers().isEmpty()) {
                        String msg = "Request declined by " + s.getName() + ": " + reason;
                        model.Notification notif = new model.Notification("N" + System.currentTimeMillis(),
                                g.getMembers().get(0).getUserId(), "RequestUpdate", msg);
                        db.getNotifications().add(notif);
                    }
                }
                db.saveToFile();
                return "Success";
            }
        }
        return "Error: Request not found.";
    }

    // FR-03: Static calculation of average response time
    public static float getAverageResponseTime(String supervisorId) {
        Database db = Database.getInstance();
        long totalMillis = 0;
        int count = 0;
        for (SupervisionRequest r : db.getRequests()) {
            if (r.getSupervisor() != null && r.getSupervisor().getUserId().equals(supervisorId)) {
                if (!"Pending".equals(r.getStatus()) && r.getResponseDate() > 0 && r.getRequestDate() > 0) {
                    totalMillis += (r.getResponseDate() - r.getRequestDate());
                    count++;
                }
            }
        }
        if (count == 0) return 0f;
        return (float) (totalMillis / (1000.0 * 60 * 60 * count)); // in hours
    }

    private Supervisor findSupervisor(String supervisorId) {
        for (model.User u : db.getUsers()) {
            if (u instanceof Supervisor && u.getUserId().equals(supervisorId)) {
                return (Supervisor) u;
            }
        }
        return null;
    }
}
