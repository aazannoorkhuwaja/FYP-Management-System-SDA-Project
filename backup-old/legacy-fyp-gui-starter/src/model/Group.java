package model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Group implements Serializable {
    private String groupId;
    private String projectTitle;
    private int memberCount;
    private String supervisorId;
    private List<String> memberIds;

    public Group() {
        this.memberIds = new ArrayList<>();
    }

    public Group(String groupId, String projectTitle, int memberCount, String supervisorId) {
        this.groupId = groupId;
        this.projectTitle = projectTitle;
        this.memberCount = memberCount;
        this.supervisorId = supervisorId;
        this.memberIds = new ArrayList<>();
    }

    public void addMember(String studentId) {
        if (!memberIds.contains(studentId)) {
            memberIds.add(studentId);
            memberCount = memberIds.size();
        }
    }

    public void sendRequest() {
        // Delegated to RequestController
    }

    // Getters and Setters
    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }
    public String getProjectTitle() { return projectTitle; }
    public void setProjectTitle(String projectTitle) { this.projectTitle = projectTitle; }
    public int getMemberCount() { return memberCount; }
    public void setMemberCount(int memberCount) { this.memberCount = memberCount; }
    public String getSupervisorId() { return supervisorId; }
    public void setSupervisorId(String supervisorId) { this.supervisorId = supervisorId; }
    public List<String> getMemberIds() { return memberIds; }
    public void setMemberIds(List<String> memberIds) {
        this.memberIds = memberIds;
        this.memberCount = memberIds.size();
    }
}
