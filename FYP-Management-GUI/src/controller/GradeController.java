package controller;

import model.Database;
import model.Rubric;
import model.Grade;
import model.Admin;
import model.Student;
import model.User;
import java.util.List;
import java.util.ArrayList;

public class GradeController {
    private Database db;

    public GradeController() {
        this.db = Database.getInstance();
    }

    public String defineRubric(String supervisorId, String criteria) {
        if (supervisorId == null || supervisorId.trim().isEmpty()) {
            return "Error: Supervisor ID is required.";
        }
        if (criteria == null || criteria.trim().isEmpty()) {
            return "Error: Criteria description is required.";
        }

        User user = db.findUserById(supervisorId);
        if (user == null || (!(user instanceof model.Supervisor) && !(user instanceof model.Admin))) {
            return "Error: Only Supervisors or Admins can define rubrics.";
        }

        String rubricId = "RB" + System.currentTimeMillis();
        Rubric rubric = new Rubric(rubricId, supervisorId.trim(), criteria.trim());
        rubric.publish();

        // Remove existing rubric for this supervisor if any
        db.getRubrics().removeIf(r -> r.getSupervisorId().equals(supervisorId));
        db.getRubrics().add(rubric);
        db.saveToFile();
        return "Success";
    }

    public String enterGrade(User caller, String studentId, float marks) {
        if (!(caller instanceof model.Supervisor)) {
            return "Error: Unauthorized. Only supervisors can award grades.";
        }
        if (studentId == null || studentId.trim().isEmpty()) {
            return "Error: Student ID is required.";
        }
        if (marks < 0 || marks > 100) {
            return "Error: Marks must be between 0 and 100.";
        }

        Student student = (Student) db.findUserById(studentId);
        if (student == null) {
            return "Error: Student not found.";
        }

        String gradeId = "GR" + System.currentTimeMillis();
        String groupId = student.getGroup() != null ? student.getGroup().getGroupId() : "";
        Grade grade = new Grade(gradeId, marks, groupId, studentId);
        grade.enter();

        db.getGrades().add(grade);
        db.saveToFile();
        return "Success";
    }

    public Grade getGradeForStudent(String studentId) {
        return db.getGradeForStudent(studentId);
    }

    public List<Grade> getGradesForGroup(String groupId) {
        return db.getGradesForGroup(groupId);
    }

    public Rubric getRubricBySupervisor(String supervisorId) {
        return db.getRubricBySupervisor(supervisorId);
    }
}
