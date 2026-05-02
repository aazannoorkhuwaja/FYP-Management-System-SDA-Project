package model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class FYPProposal implements Serializable {
    private static final long serialVersionUID = 1L;
    private String proposalId;
    private String title;
    private String domain;
    private String abstractText;
    private String status;
    private ProjectGroup group;

    public FYPProposal() {}

    public FYPProposal(String proposalId, String title, String domain, String abstractText, String status, ProjectGroup group) {
        this.proposalId = proposalId;
        this.title = title;
        this.domain = domain;
        this.abstractText = abstractText;
        this.status = status;
        this.group = group;
    }

    public void submit() {
        this.status = "Pending Review";
    }

    public boolean checkDuplicate(List<FYPProposal> existingProposals) {
        for (FYPProposal p : existingProposals) {
            if (!p.getProposalId().equals(this.proposalId) &&
                p.getTitle().equalsIgnoreCase(this.title)) {
                return true;
            }
        }
        return false;
    }

    // Getters and Setters
    public String getProposalId() { return proposalId; }
    public void setProposalId(String proposalId) { this.proposalId = proposalId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public String getAbstractText() { return abstractText; }
    public void setAbstractText(String abstractText) { this.abstractText = abstractText; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public ProjectGroup getGroup() { return group; }
    public void setGroup(ProjectGroup group) { this.group = group; }
}
