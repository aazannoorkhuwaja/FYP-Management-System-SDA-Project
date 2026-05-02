package controller;

import model.Database;
import model.User;
import model.Student;
import model.Supervisor;
import model.Admin;

public class AuthController {
    private Database db;

    public AuthController() {
        this.db = Database.getInstance();
    }

    public User login(String email, String password) {
        User user = db.findUserByEmail(email);
        if (user != null && user.login(email, password)) {
            return user;
        }
        return null;
    }

    public String getDashboardRole(User user) {
        if (user instanceof Student) return "Student";
        if (user instanceof Supervisor) return "Supervisor";
        if (user instanceof Admin) return "Admin";
        return "Unknown";
    }
}
