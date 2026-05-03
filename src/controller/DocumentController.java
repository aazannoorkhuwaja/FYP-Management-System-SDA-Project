package controller;

import model.Database;
import model.Document;
import model.Notification;
import java.util.List;
import java.util.ArrayList;

public class DocumentController {
    private Database db;

    public DocumentController() {
        this.db = Database.getInstance();
    }

    public String uploadDocument(String groupId, String fileName, String description, String filePath) {
        if (groupId == null || groupId.trim().isEmpty()) {
            return "Error: Group ID is required.";
        }
        if (fileName == null || fileName.trim().isEmpty()) {
            return "Error: File name is required.";
        }
        if (description == null || description.trim().isEmpty()) {
            return "Error: Description is required.";
        }
        if (filePath == null || filePath.trim().isEmpty()) {
            return "Error: File path is required.";
        }

        String lower = filePath.toLowerCase();
        if (!(lower.endsWith(".pdf") || lower.endsWith(".docx") || lower.endsWith(".pptx") || lower.endsWith(".zip") || lower.endsWith(".txt"))) {
            return "Error: Unsupported file type. Accepted types: pdf, docx, pptx, zip, txt.";
        }

        model.ProjectGroup group = db.findGroupById(groupId);
        if (group == null) {
            return "Error: Group not found.";
        }

        String normalizedId = groupId + "-" + fileName.trim().replaceAll("\\s+", "_");
        int version = 1;
        for (Document existing : db.getDocuments()) {
            if (normalizedId.equals(existing.getDocumentId())) {
                version = Math.max(version, existing.getVersion() + 1);
            }
        }

        Document doc = new Document(normalizedId, groupId, fileName.trim(), description.trim(), filePath.trim());
        doc.setVersion(version);
        doc.upload();

        db.getDocuments().add(doc);

        if (group.getSupervisor() != null) {
            String notifId = "NOTIF-DOC-" + System.currentTimeMillis();
            Notification notif = new Notification(notifId, group.getSupervisor().getUserId(), "Document Upload", "New document uploaded for group " + groupId + ": " + fileName.trim());
            db.getNotifications().add(notif);
        }

        db.saveToFile();
        return "Success";
    }

    public List<Document> getDocumentsForGroup(String groupId) {
        return db.getDocumentsForGroup(groupId);
    }

    /**
     * FR-13: Returns ALL document versions for a group (no deduplication).
     * Use this for version history display.
     */
    public List<Document> getAllDocumentsForGroup(String groupId) {
        List<Document> result = new ArrayList<>();
        for (Document d : db.getDocuments()) {
            if (groupId != null && groupId.equals(d.getGroupId())) {
                result.add(d);
            }
        }
        return result;
    }

    public List<Document> getVersionHistory(String documentId) {
        return db.getDocumentVersions(documentId);
    }
}
