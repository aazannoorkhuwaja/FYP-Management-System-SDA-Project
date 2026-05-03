package test;

import controller.AuthController;
import model.Admin;
import model.Database;
import model.Student;
import model.Supervisor;
import model.User;

public class AuthControllerTest {

    public static void main(String[] args) {
        System.out.println("--- Starting AuthController Tests ---");
        
        // Ensure clean slate or valid initial DB state
        Database db = Database.getInstance();
        
        // 1. Setup Test Data
        setupTestData(db);
        
        // 2. Run Tests
        testStudentLogin();
        testSupervisorLogin();
        testAdminLogin();
        testInvalidLogin();
        testRoleIdentification();
        
        System.out.println("--- All AuthController Tests Finished ---");
    }

    private static void setupTestData(Database db) {
        // Create dummy users for testing
        // Only add if they don't exist to avoid duplicates
        if (db.findUserByEmail("teststudent@example.com") == null) {
            db.getUsers().add(new Student("STU-TEST", "Test Student", "teststudent@example.com", "password123", new java.util.ArrayList<>(), "CS", null, 0.0f));
        }
        if (db.findUserByEmail("testsupervisor@example.com") == null) {
            db.getUsers().add(new Supervisor("SUP-TEST", "Test Supervisor", "testsupervisor@example.com", "password123", new java.util.ArrayList<>(), 0.0f, 0, 5));
        }
        if (db.findUserByEmail("testadmin@example.com") == null) {
            db.getUsers().add(new Admin("ADM-TEST", "Test Admin", "testadmin@example.com", "password123", "ADM-001"));
        }
    }

    private static void testStudentLogin() {
        System.out.println("Running testStudentLogin...");
        AuthController authController = new AuthController();
        User loggedInUser = authController.login("teststudent@example.com", "password123");
        
        if (loggedInUser != null && loggedInUser instanceof Student) {
            System.out.println("PASS: Student login successful.");
        } else {
            System.err.println("FAIL: Student login failed. User: " + loggedInUser);
        }
    }

    private static void testSupervisorLogin() {
        System.out.println("Running testSupervisorLogin...");
        AuthController authController = new AuthController();
        User loggedInUser = authController.login("testsupervisor@example.com", "password123");
        
        if (loggedInUser != null && loggedInUser instanceof Supervisor) {
            System.out.println("PASS: Supervisor login successful.");
        } else {
            System.err.println("FAIL: Supervisor login failed. User: " + loggedInUser);
        }
    }

    private static void testAdminLogin() {
        System.out.println("Running testAdminLogin...");
        AuthController authController = new AuthController();
        User loggedInUser = authController.login("testadmin@example.com", "password123");
        
        if (loggedInUser != null && loggedInUser instanceof Admin) {
            System.out.println("PASS: Admin login successful.");
        } else {
            System.err.println("FAIL: Admin login failed. User: " + loggedInUser);
        }
    }

    private static void testInvalidLogin() {
        System.out.println("Running testInvalidLogin...");
        AuthController authController = new AuthController();
        
        // Wrong password
        User wrongPassUser = authController.login("teststudent@example.com", "wrongpassword");
        if (wrongPassUser == null) {
            System.out.println("PASS: Invalid password correctly rejected.");
        } else {
            System.err.println("FAIL: Invalid password was accepted.");
        }

        // Non-existent user
        User noUser = authController.login("nobody@example.com", "password123");
        if (noUser == null) {
            System.out.println("PASS: Non-existent user correctly rejected.");
        } else {
            System.err.println("FAIL: Non-existent user was accepted.");
        }
    }
    
    private static void testRoleIdentification() {
        System.out.println("Running testRoleIdentification...");
        AuthController authController = new AuthController();
        
        User student = new Student("1", "S", "s@s.com", "pass", new java.util.ArrayList<>(), "CS", null, 0.0f);
        User supervisor = new Supervisor("2", "Sup", "sup@sup.com", "pass", new java.util.ArrayList<>(), 0.0f, 0, 5);
        User admin = new Admin("3", "A", "a@a.com", "pass", "A-001");
        
        boolean passed = true;
        
        if (!authController.getDashboardRole(student).equals("Student")) {
            System.err.println("FAIL: Incorrect role for Student.");
            passed = false;
        }
        if (!authController.getDashboardRole(supervisor).equals("Supervisor")) {
            System.err.println("FAIL: Incorrect role for Supervisor.");
            passed = false;
        }
        if (!authController.getDashboardRole(admin).equals("Admin")) {
            System.err.println("FAIL: Incorrect role for Admin.");
            passed = false;
        }
        
        if (passed) {
            System.out.println("PASS: All role identifications are correct.");
        }
    }
}
