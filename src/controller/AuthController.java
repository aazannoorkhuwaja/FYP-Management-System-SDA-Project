package controller;

import model.Database;
import model.User;
import model.Student;
import model.Supervisor;
import model.Admin;
import util.DiagnosticService;

public class AuthController {
    private Database db;
    private DiagnosticService diag;

    public AuthController() {
        this.db = Database.getInstance();
        this.diag = DiagnosticService.getInstance();
    }

    public User login(String email, String password) {
        long start = diag.startSpan("AuthController", "User Login: " + email);
        User user = db.findUserByEmail(email);
        if (user != null && user.login(email, password)) {
            diag.info("AuthController", "Login successful for user: " + user.getUserId() + " (" + getDashboardRole(user) + ")");
            diag.endSpan("AuthController", "User Login: " + email, start);
            return user;
        }
        diag.error("AuthController", "Login failed for: " + email);
        diag.endSpan("AuthController", "User Login: " + email, start);
        return null;
    }

    public String getDashboardRole(User user) {
        if (user instanceof Student) return "Student";
        if (user instanceof Supervisor) return "Supervisor";
        if (user instanceof Admin) return "Admin";
        return "Unknown";
    }
}
