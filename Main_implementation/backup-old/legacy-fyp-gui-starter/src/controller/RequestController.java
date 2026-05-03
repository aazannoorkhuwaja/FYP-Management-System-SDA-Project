package controller;

import model.Database;
import model.SupervisionRequest;
import model.Supervisor;
import model.Group;

public class RequestController {
    private Database db;

    public RequestController() {
        this.db = Database.getInstance();
    }

    public String sendRequest(String groupId, String supervisorId) {
        Group group = db.findGroupById(groupId);
        if (group == null) return "Error: Group not found.";

        Supervisor supervisor = findSupervisor(supervisorId);
        if (supervisor == null) return "Error: Supervisor not found.";

        if (supervisor.getCurrentGroups() >= supervisor.getMaxGroups()) {
            return "Error: Supervisor has reached maximum group limit.";
        }

        for (SupervisionRequest r : db.getRequests()) {
            if (r.getGroupId().equals(groupId) && r.getSupervisorId().equals(supervisorId) && r.getStatus().equals("Pending")) {
                return "Error: Request already pending.";
            }
        }

        String requestId = "R" + System.currentTimeMillis();
        SupervisionRequest request = new SupervisionRequest(requestId, groupId, supervisorId);
        db.getRequests().add(request);
        db.saveToFile();
        return "Success";
    }

    public String acceptRequest(String requestId) {
        for (SupervisionRequest r : db.getRequests()) {
            if (r.getRequestId().equals(requestId) && r.getStatus().equals("Pending")) {
                r.accept();
                // Update group supervisor
                Group g = db.findGroupById(r.getGroupId());
                if (g != null) g.setSupervisorId(r.getSupervisorId());
                // Update supervisor count
                Supervisor s = findSupervisor(r.getSupervisorId());
                if (s != null) s.setCurrentGroups(s.getCurrentGroups() + 1);
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
                db.saveToFile();
                return "Success";
            }
        }
        return "Error: Request not found.";
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
