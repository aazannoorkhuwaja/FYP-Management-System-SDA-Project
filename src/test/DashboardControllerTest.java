package test;

import controller.DashboardController;
import model.Database;
import model.ProjectGroup;
import java.util.Map;

public class DashboardControllerTest {
    public static void main(String[] args) {
        testAdminStats();
    }

    public static void testAdminStats() {
        System.out.println("Running testAdminStats...");
        Database db = Database.getInstance();
        // Clear or setup mock data if needed, but we'll use existing DB state
        
        DashboardController controller = new DashboardController();
        Map<String, Integer> stats = controller.getAdminStats();
        
        int groupCount = db.getGroups().size();
        if (stats.get("totalGroups") == groupCount) {
            System.out.println("PASS: totalGroups count is correct.");
        } else {
            System.out.println("FAIL: Expected " + groupCount + ", but got " + stats.get("totalGroups"));
            System.exit(1);
        }
    }
}
