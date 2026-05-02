package model;

import java.io.Serializable;
import java.util.Date;

public class Document implements Serializable {
    private static final long serialVersionUID = 1L;
    private String documentId;
    private String groupId;
    private String fileName;
    private String description;
    private String filePath;
    private int version;
    private Date uploadDate;
    private boolean isUploaded;

    public Document(String documentId, String groupId, String fileName, String description, String filePath) {
        this.documentId = documentId;
        this.groupId = groupId;
        this.fileName = fileName;
        this.description = description;
        this.filePath = filePath;
        this.version = 1;
        this.isUploaded = false;
    }

    public void upload() {
        this.isUploaded = true;
        this.uploadDate = new Date();
    }

    // Getters and Setters
    public String getDocumentId() { return documentId; }
    public String getGroupId() { return groupId; }
    public String getFileName() { return fileName; }
    public String getDescription() { return description; }
    public String getFilePath() { return filePath; }
    public int getVersion() { return version; }
    public Date getUploadDate() { return uploadDate; }
    public boolean isUploaded() { return isUploaded; }

    public void setVersion(int version) { this.version = version; }
}
