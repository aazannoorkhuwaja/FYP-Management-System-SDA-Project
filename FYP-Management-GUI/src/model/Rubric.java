package model;

import java.io.Serializable;
import java.util.Date;

public class Rubric implements Serializable {
    private static final long serialVersionUID = 1L;
    private String rubricId;
    private String supervisorId;
    private String criteria;
    private Date publishedDate;
    private boolean isPublished;

    public Rubric(String rubricId, String supervisorId, String criteria) {
        this.rubricId = rubricId;
        this.supervisorId = supervisorId;
        this.criteria = criteria;
        this.isPublished = false;
    }

    public void publish() {
        this.isPublished = true;
        this.publishedDate = new Date();
    }

    // Getters
    public String getRubricId() { return rubricId; }
    public String getSupervisorId() { return supervisorId; }
    public String getCriteria() { return criteria; }
    public Date getPublishedDate() { return publishedDate; }
    public boolean isPublished() { return isPublished; }
}
