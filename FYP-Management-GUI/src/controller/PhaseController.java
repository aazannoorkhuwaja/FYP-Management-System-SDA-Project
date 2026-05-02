package controller;

import model.Database;
import model.ProjectPhase;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class PhaseController {
    private Database db;

    public PhaseController() {
        this.db = Database.getInstance();
    }

    public String addPhase(model.User caller, String groupId, String phaseName, Date deadline, String description) {
        if (!(caller instanceof model.Supervisor)) {
            return "Error: Unauthorized. Only supervisors can add project phases.";
        }
        if (groupId == null || groupId.trim().isEmpty()) {
            return "Error: Group ID is required.";
        }
        if (phaseName == null || phaseName.trim().isEmpty()) {
            return "Error: Phase name is required.";
        }
        if (deadline == null) {
            return "Error: Deadline is required.";
        }

        model.ProjectGroup group = db.findGroupById(groupId);
        if (group == null) {
            return "Error: Group not found.";
        }

        String phaseId = "PH" + System.currentTimeMillis();
        ProjectPhase phase = new ProjectPhase(phaseId, groupId, phaseName.trim(), deadline, description != null ? description.trim() : "");
        db.getPhases().add(phase);
        db.saveToFile();
        return "Success";
    }

    public List<ProjectPhase> getPhasesForGroup(String groupId) {
        return db.getPhasesForGroup(groupId);
    }

    public String updatePhase(String phaseId, Date newDeadline) {
        if (phaseId == null || phaseId.trim().isEmpty()) {
            return "Error: Phase ID is required.";
        }
        if (newDeadline == null) {
            return "Error: New deadline is required.";
        }

        for (ProjectPhase phase : db.getPhases()) {
            if (phase.getPhaseId().equals(phaseId)) {
                phase.setDeadline(newDeadline);
                db.saveToFile();
                return "Success";
            }
        }
        return "Error: Phase not found.";
    }
}
