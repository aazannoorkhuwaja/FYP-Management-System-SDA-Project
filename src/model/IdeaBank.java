package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class IdeaBank implements Serializable {
    private String ideaId;
    private String title;
    private String domain;
    private String description;
    private String supervisorId;

    public IdeaBank() {}

    public IdeaBank(String ideaId, String title, String domain, String description, String supervisorId) {
        this.ideaId = ideaId;
        this.title = title;
        this.domain = domain;
        this.description = description;
        this.supervisorId = supervisorId;
    }

    public void post() {
        // Saved to Database by controller
    }

    public static List<IdeaBank> browse(List<IdeaBank> all) {
        return new ArrayList<>(all);
    }

    // Getters and Setters
    public String getIdeaId() { return ideaId; }
    public void setIdeaId(String ideaId) { this.ideaId = ideaId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getSupervisorId() { return supervisorId; }
    public void setSupervisorId(String supervisorId) { this.supervisorId = supervisorId; }
}
