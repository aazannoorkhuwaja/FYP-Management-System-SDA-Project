package model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class ProjectGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    private String groupId;
    private String projectTitle;
    private int memberCount;
    private Supervisor supervisor;
    private List<Student> members;

    public ProjectGroup() {
        this.members = new ArrayList<>();
        this.memberCount = 0;
    }

    public ProjectGroup(String groupId, String projectTitle, int memberCount, Supervisor supervisor) {
        this.groupId = groupId;
        this.projectTitle = projectTitle;
        this.memberCount = memberCount;
        this.supervisor = supervisor;
        this.members = new ArrayList<>();
    }

    public void addMember(Student student) {
        if (!members.contains(student)) {
            members.add(student);
            memberCount = members.size();
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
    public Supervisor getSupervisor() { return supervisor; }
    public void setSupervisor(Supervisor supervisor) { this.supervisor = supervisor; }
    public List<Student> getMembers() { return members; }
    public void setMembers(List<Student> members) {
        this.members = members;
        this.memberCount = members.size();
    }
}
