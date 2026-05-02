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
                    updateResponseTime(s, r);
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
                    updateResponseTime(s, r);
                }
                db.saveToFile();
                return "Success";
            }
        }
        return "Error: Request not found.";
    }

    private void updateResponseTime(Supervisor s, SupervisionRequest r) {
        long deltaMillis = r.getResponseDate() - r.getRequestDate();
        float deltaHours = deltaMillis / (1000.0f * 60 * 60);
        float currentAvg = s.getAvgResponseTime();
        s.setAvgResponseTime(currentAvg == 0 ? deltaHours : (currentAvg + deltaHours) / 2.0f);
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
