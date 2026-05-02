package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Document implements Serializable {
    private String documentId;
    private String fileName;
    private int version;
    private Date uploadDate;

    public Document() {
        this.version = 1;
        this.uploadDate = new Date();
    }

    public Document(String documentId, String fileName) {
        this.documentId = documentId;
        this.fileName = fileName;
        this.version = 1;
        this.uploadDate = new Date();
    }

    public void upload() {
        this.uploadDate = new Date();
    }

    public static List<Document> getVersionHistory(List<Document> allDocs, String groupId) {
        List<Document> history = new ArrayList<>();
        for (Document d : allDocs) {
            if (d.getDocumentId().startsWith(groupId)) {
                history.add(d);
            }
        }
        return history;
    }

    // Getters and Setters
    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }
    public Date getUploadDate() { return uploadDate; }
    public void setUploadDate(Date uploadDate) { this.uploadDate = uploadDate; }
}
