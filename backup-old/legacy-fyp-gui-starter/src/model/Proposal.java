package model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Proposal implements Serializable {
    private String proposalId;
    private String title;
    private String domain;
    private String abstractText;
    private String status;
    private String groupId;

    public Proposal() {}

    public Proposal(String proposalId, String title, String domain, String abstractText, String status, String groupId) {
        this.proposalId = proposalId;
        this.title = title;
        this.domain = domain;
        this.abstractText = abstractText;
        this.status = status;
        this.groupId = groupId;
    }

    public void submit() {
        this.status = "Pending Review";
    }

    public boolean checkDuplicate(List<Proposal> existingProposals) {
        for (Proposal p : existingProposals) {
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
    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }
}
