package controller;

import model.Database;
import model.Proposal;
import model.Group;
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

        Group group = db.findGroupById(groupId);
        if (group == null) {
            return "Error: Group not found.";
        }

        String proposalId = "P" + System.currentTimeMillis();
        Proposal proposal = new Proposal(proposalId, title, domain, abstractText, "Pending Review", groupId);

        if (proposal.checkDuplicate(db.getProposals())) {
            return "Error: A proposal with this title already exists.";
        }

        proposal.submit();
        db.getProposals().add(proposal);
        group.setProjectTitle(title);
        db.saveToFile();
        return "Success";
    }

    public List<Proposal> getProposalsByGroup(String groupId) {
        return db.findProposalsByGroup(groupId);
    }
}
