package model;

import java.io.Serializable;
import java.util.Date;

public class GroupMember implements Serializable {
    private String studentId;
    private String groupId;
    private String role;
    private Date joinedDate;

    public GroupMember() {
        this.joinedDate = new Date();
    }

    public GroupMember(String studentId, String groupId, String role) {
        this.studentId = studentId;
        this.groupId = groupId;
        this.role = role;
        this.joinedDate = new Date();
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Date getJoinedDate() { return joinedDate; }
    public void setJoinedDate(Date joinedDate) { this.joinedDate = joinedDate; }
}
