package controller;

import model.Database;
import model.FYPProposal;
import model.ProjectGroup;
import java.util.List;

public class ProposalController {
    private Database db;

    public ProposalController() {
        this.db = Database.getInstance();
    }

    public boolean validateFields(String title, String domain, String abstractText) {
        return title != null && !title.trim().isEmpty()
            && domain != null && !domain.trim().isEmpty()
            && abstractText != null && !abstractText.trim().isEmpty();
    }

    public String submitProposal(String groupId, String title, String domain, String abstractText) {
        if (!validateFields(title, domain, abstractText)) {
            return "Error: All fields are required.";
        }

        ProjectGroup group = db.findGroupById(groupId);
        if (group == null) {
            return "Error: ProjectGroup not found.";
        }

        String proposalId = "P" + System.currentTimeMillis();
        FYPProposal proposal = new FYPProposal(proposalId, title, domain, abstractText, "Pending Review", group);

        if (db.isDuplicateProposal(title, null)) {
            return "Error: A proposal with this title already exists.";
        }

        proposal.submit();
        db.getProposals().add(proposal);
        group.setProjectTitle(title);
        db.saveToFile();
        return "Success";
    }

    public List<FYPProposal> getProposalsByGroup(String groupId) {
        return db.findProposalsByGroup(groupId);
    }
}
