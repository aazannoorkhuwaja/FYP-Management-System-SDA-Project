package controller;

import model.Database;
import java.util.HashMap;
import java.util.Map;

public class DashboardController {
    private Database db;

    public DashboardController() {
        this.db = Database.getInstance();
    }

    public Map<String, Integer> getAdminStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalGroups", db.getGroups().size());
        return stats;
    }
}
