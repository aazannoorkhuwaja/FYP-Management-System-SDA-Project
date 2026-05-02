package model;

import java.io.Serializable;
import java.util.Date;

public class ProjectPhase implements Serializable {
    private static final long serialVersionUID = 1L;
    private String phaseId;
    private String groupId;
    private String phaseName;
    private Date deadline;
    private String description;
    private boolean isCompleted;

    public ProjectPhase(String phaseId, String groupId, String phaseName, Date deadline, String description) {
        this.phaseId = phaseId;
        this.groupId = groupId;
        this.phaseName = phaseName;
        this.deadline = deadline;
        this.description = description;
        this.isCompleted = false;
    }

    // Getters and Setters
    public String getPhaseId() { return phaseId; }
    public String getGroupId() { return groupId; }
    public String getPhaseName() { return phaseName; }
    public Date getDeadline() { return deadline; }
    public String getDescription() { return description; }
    public boolean isCompleted() { return isCompleted; }

    public void setDeadline(Date deadline) { this.deadline = deadline; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}
