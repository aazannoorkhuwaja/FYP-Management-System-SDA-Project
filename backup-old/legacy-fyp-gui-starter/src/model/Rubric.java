package model;

import java.io.Serializable;
import java.util.Date;

public class Rubric implements Serializable {
    private String rubricId;
    private String criteria;
    private Date publishedDate;

    public Rubric() {
        this.publishedDate = new Date();
    }

    public Rubric(String rubricId, String criteria) {
        this.rubricId = rubricId;
        this.criteria = criteria;
        this.publishedDate = new Date();
    }

    public void publish() {
        this.publishedDate = new Date();
    }

    public String getRubricId() { return rubricId; }
    public void setRubricId(String rubricId) { this.rubricId = rubricId; }
    public String getCriteria() { return criteria; }
    public void setCriteria(String criteria) { this.criteria = criteria; }
    public Date getPublishedDate() { return publishedDate; }
    public void setPublishedDate(Date publishedDate) { this.publishedDate = publishedDate; }
}
