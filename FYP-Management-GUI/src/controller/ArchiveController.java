package controller;

import model.Database;
import model.Archive;
import java.util.List;
import java.util.ArrayList;

public class ArchiveController {
    private Database db;

    public ArchiveController() {
        this.db = Database.getInstance();
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
