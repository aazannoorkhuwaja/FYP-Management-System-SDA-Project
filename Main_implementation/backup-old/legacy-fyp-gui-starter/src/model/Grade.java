package model;

import java.io.Serializable;

public class Grade implements Serializable {
    private String gradeId;
    private float marks;
    private String groupId;

    public Grade() {}

    public Grade(String gradeId, float marks, String groupId) {
        this.gradeId = gradeId;
        this.marks = marks;
        this.groupId = groupId;
    }

    public void enter() {
        // Saved to Database by controller
    }

    public float getResult() {
        return marks;
    }

    public String getGradeId() { return gradeId; }
    public void setGradeId(String gradeId) { this.gradeId = gradeId; }
    public float getMarks() { return marks; }
    public void setMarks(float marks) { this.marks = marks; }
    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }
}
