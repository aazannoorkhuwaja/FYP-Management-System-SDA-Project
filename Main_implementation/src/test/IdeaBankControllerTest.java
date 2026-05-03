package test;

import controller.IdeaBankController;
import model.Database;

public class IdeaBankControllerTest {
    public static void main(String[] args) {
        testPostIdeaValidation();
        testDuplicateTitle();
    }

    public static void testPostIdeaValidation() {
        System.out.println("Running testPostIdeaValidation...");
        IdeaBankController controller = new IdeaBankController();
        
        String result = controller.postIdea("", "Test Title", "Test Domain", "Test Desc");
        if (result.contains("Error: Supervisor ID is required")) {
            System.out.println("PASS: Validation caught empty supervisor ID.");
        } else {
            System.out.println("FAIL: Expected validation error, got: " + result);
            System.exit(1);
        }
    }

    public static void testDuplicateTitle() {
        System.out.println("Running testDuplicateTitle...");
        IdeaBankController controller = new IdeaBankController();
        String supervisorId = "S100";
        String title = "Unique Idea " + System.currentTimeMillis();
        
        controller.postIdea(supervisorId, title, "Domain", "Desc");
        String result = controller.postIdea(supervisorId, title, "Domain", "Desc");
        
        if (result.contains("Error: Duplicate idea title")) {
            System.out.println("PASS: Duplicate title rejected.");
        } else {
            System.out.println("FAIL: Expected duplicate title error, got: " + result);
            System.exit(1);
        }
    }
}
