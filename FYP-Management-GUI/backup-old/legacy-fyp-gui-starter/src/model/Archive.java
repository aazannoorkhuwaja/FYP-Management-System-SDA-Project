package model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Archive implements Serializable {
    private String archiveId;
    private String domain;
    private int year;

    public Archive() {}

    public Archive(String archiveId, String domain, int year) {
        this.archiveId = archiveId;
        this.domain = domain;
        this.year = year;
    }

    public static List<Archive> search(List<Archive> all, String keyword) {
        List<Archive> results = new ArrayList<>();
        for (Archive a : all) {
            if (a.getDomain().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(a);
            }
        }
        return results;
    }

    public boolean checkExists(List<Archive> all, String title) {
        for (Archive a : all) {
            if (a.getArchiveId().equalsIgnoreCase(title)) {
                return true;
            }
        }
        return false;
    }

    public String getArchiveId() { return archiveId; }
    public void setArchiveId(String archiveId) { this.archiveId = archiveId; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
}
