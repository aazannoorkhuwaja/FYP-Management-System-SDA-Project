package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class ProjectPhase implements Serializable {
    private String phaseId;
    private String groupId;
    private String phaseName;
    private Date deadline;
    private String description;

    public ProjectPhase() {}

    public ProjectPhase(String phaseId, String groupId, String phaseName, Date deadline, String description) {
        this.phaseId = phaseId;
        this.groupId = groupId;
        this.phaseName = phaseName;
        this.deadline = deadline;
        this.description = description;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public static List<ProjectPhase> getUpcomingDeadlines(List<ProjectPhase> all, String groupId) {
        List<ProjectPhase> upcoming = new ArrayList<>();
        Date now = new Date();
        for (ProjectPhase p : all) {
            if (p.getGroupId().equals(groupId) && p.getDeadline() != null && p.getDeadline().after(now)) {
                upcoming.add(p);
            }
        }
        return upcoming;
    }

    public String getPhaseId() { return phaseId; }
    public void setPhaseId(String phaseId) { this.phaseId = phaseId; }
    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }
    public String getPhaseName() { return phaseName; }
    public void setPhaseName(String phaseName) { this.phaseName = phaseName; }
    public Date getDeadline() { return deadline; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
