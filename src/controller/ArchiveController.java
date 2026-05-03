package controller;

import model.Database;
import model.Archive;
import model.ProjectGroup;
import model.Student;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

public class ArchiveController {
    private Database db;

    public ArchiveController() {
        this.db = Database.getInstance();
    }

    public String archiveProject(String groupId) {
        if (groupId == null || groupId.trim().isEmpty()) {
            return "Error: Group ID is required.";
        }

        ProjectGroup group = db.findGroupById(groupId);
        if (group == null) {
            return "Error: Group not found.";
        }

        // Check if already archived
        for (Archive a : db.getArchives()) {
            if (a.getArchiveId() != null && a.getArchiveId().equals("ARC-" + groupId)) {
                return "Error: This project is already archived.";
            }
        }

        // Build student names string
        StringBuilder studentNames = new StringBuilder();
        for (Student s : group.getMembers()) {
            if (studentNames.length() > 0) studentNames.append(", ");
            studentNames.append(s.getName());
        }

        String supervisorName = group.getSupervisor() != null ? group.getSupervisor().getName() : "Unassigned";
        String title = group.getProjectTitle() != null ? group.getProjectTitle() : "Untitled";
        int year = Calendar.getInstance().get(Calendar.YEAR);

        // Determine grade if available
        String grade = "N/A";
        for (Student s : group.getMembers()) {
            model.Grade g = db.getGradeForStudent(s.getUserId());
            if (g != null && g.getLetterGrade() != null) {
                grade = g.getLetterGrade();
                break;
            }
        }

        Archive archive = new Archive("ARC-" + groupId, title, "", studentNames.toString(), supervisorName, year, grade);
        db.getArchives().add(archive);
        db.saveToFile();
        return "Success";
    }

    public List<Archive> getAllArchives() {
        return db.getArchives();
    }

    public List<Archive> searchByTitle(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return db.searchArchiveByTitle(keyword.trim());
    }

    public List<Archive> searchByDomain(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return db.searchArchiveByDomain(keyword.trim());
    }

    public List<Archive> searchByStudent(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return db.searchArchiveByStudent(name.trim());
    }
}

