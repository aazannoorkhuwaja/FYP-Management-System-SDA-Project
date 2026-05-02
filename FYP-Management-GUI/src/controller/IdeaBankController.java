package controller;

import model.Database;
import model.IdeaBank;
import java.util.List;
import java.util.ArrayList;

public class IdeaBankController {
    private Database db;

    public IdeaBankController() {
        this.db = Database.getInstance();
    }

    public String postIdea(String supervisorId, String title, String domain, String description) {
        if (supervisorId == null || supervisorId.trim().isEmpty()) {
            return "Error: Supervisor ID is required.";
        }
        if (title == null || title.trim().isEmpty()) {
            return "Error: Title is required.";
        }
        if (domain == null || domain.trim().isEmpty()) {
            return "Error: Domain is required.";
        }

        // TDD requirement: Duplicate title check
        for (IdeaBank idea : db.getIdeas()) {
            if (idea.getSupervisorId().equals(supervisorId) && idea.getTitle().equalsIgnoreCase(title.trim())) {
                return "Error: Duplicate idea title for this supervisor.";
            }
        }

        String ideaId = "IDEA" + System.currentTimeMillis();
        IdeaBank idea = new IdeaBank(ideaId, title.trim(), domain.trim(), description != null ? description.trim() : "", supervisorId);
        idea.post();

        db.getIdeas().add(idea);
        db.saveToFile();
        return "Success";
    }

    public List<IdeaBank> searchIdeas(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return db.getIdeas();
        }
        return db.searchIdeasByKeyword(keyword.trim());
    }

    public List<IdeaBank> getIdeasBySupervisor(String supervisorId) {
        if (supervisorId == null || supervisorId.trim().isEmpty()) {
            return db.getIdeas();
        }
        return db.getIdeasBySupervisor(supervisorId);
    }
}
