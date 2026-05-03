package model;

import java.io.Serializable;
import java.util.Date;

public class Grade implements Serializable {
    private static final long serialVersionUID = 1L;
    private String gradeId;
    private float marks;
    private String groupId;
    private String studentId;
    private Date awardedDate;
    private boolean isAwarded;

    public Grade(String gradeId, float marks, String groupId, String studentId) {
        this.gradeId = gradeId;
        this.marks = marks;
        this.groupId = groupId;
        this.studentId = studentId;
        this.isAwarded = false;
    }

    public void enter() {
        this.isAwarded = true;
        this.awardedDate = new Date();
    }

    // Getters
    public String getGradeId() { return gradeId; }
    public float getMarks() { return marks; }
    public String getGroupId() { return groupId; }
    public String getStudentId() { return studentId; }
    public Date getAwardedDate() { return awardedDate; }
    public boolean isAwarded() { return isAwarded; }

    public String getLetterGrade() {
        if (marks >= 90) return "A+";
        if (marks >= 80) return "A";
        if (marks >= 70) return "B";
        if (marks >= 60) return "C";
        if (marks >= 50) return "D";
        return "F";
    }
}
