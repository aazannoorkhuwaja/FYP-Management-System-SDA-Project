package model;

import java.io.Serializable;

public class Archive implements Serializable {
    private static final long serialVersionUID = 1L;
    private String archiveId;
    private String projectTitle;
    private String domain;
    private String studentNames;
    private String supervisorName;
    private int year;
    private String grade;

    public Archive(String archiveId, String projectTitle, String domain, String studentNames, String supervisorName, int year, String grade) {
        this.archiveId = archiveId;
        this.projectTitle = projectTitle;
        this.domain = domain;
        this.studentNames = studentNames;
        this.supervisorName = supervisorName;
        this.year = year;
        this.grade = grade;
    }

    // Getters and Setters
    public String getArchiveId() { return archiveId; }
    public String getProjectTitle() { return projectTitle; }
    public String getDomain() { return domain; }
    public String getStudentNames() { return studentNames; }
    public String getSupervisorName() { return supervisorName; }
    public int getYear() { return year; }
    public String getGrade() { return grade; }
}
