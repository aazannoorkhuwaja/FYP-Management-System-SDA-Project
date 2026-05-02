package view;

import controller.GradeController;
import model.Database;
import model.Grade;
import model.ProjectGroup;
import model.Rubric;
import model.Student;
import model.Supervisor;

import javax.swing.*;
import java.awt.*;

/**
 * Student Grades Frame refined with Monolithic Serenity theme.
 */
public class StudentGradesFrame extends JFrame {
    private Student student;
    private JTextArea gradesArea;

    public StudentGradesFrame(Student student) {
        this.student = student;
        setTitle("Grades & Assessment");
        setSize(550, 450);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        Theme.applyTheme(panel);

        JLabel head = new JLabel("Performance Review");
        Theme.styleLabel(head, true);
        panel.add(head, BorderLayout.NORTH);

        gradesArea = new JTextArea();
        gradesArea.setEditable(false);
        gradesArea.setBackground(Theme.SURFACE);
        gradesArea.setForeground(Theme.TEXT);
        gradesArea.setFont(Theme.MONO_FONT);
        gradesArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        loadGrades();

        JScrollPane scroll = new JScrollPane(gradesArea);
        Theme.styleScrollPane(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        panel.add(scroll, BorderLayout.CENTER);

        JButton closeBtn = Theme.createOutlineButton("Close Performance Window");
        closeBtn.addActionListener(e -> dispose());
        panel.add(closeBtn, BorderLayout.SOUTH);

        add(panel);
    }

    private void loadGrades() {
        StringBuilder sb = new StringBuilder();
        GradeController gc = new GradeController();
        ProjectGroup group = student.getGroup();

        sb.append("=== FINAL ASSESSMENT ===\n\n");
        if (group == null) {
            sb.append("Status: Unassigned\nNo project group detected.\n");
        } else {
            Grade grade = gc.getGradeForStudent(student.getUserId());
            if (grade != null) {
                sb.append("Student:  ").append(student.getName()).append("\n");
                sb.append("Project:  ").append(group.getProjectTitle()).append("\n");
                sb.append("Score:    ").append(String.format("%.1f", grade.getMarks())).append(" / 100\n");
            } else {
                sb.append("Status: Pending\nNo marks recorded yet.\n");
            }
        }

        sb.append("\n=== GRADING RUBRIC ===\n\n");
        if (group != null && group.getSupervisor() != null) {
            Supervisor sv = group.getSupervisor();
            Rubric rubric = gc.getRubricBySupervisor(sv.getUserId());
            if (rubric != null) {
                sb.append("Assessor: ").append(sv.getName()).append("\n");
                sb.append("\nCriteria:\n");
                sb.append("------------------------------------------\n");
                sb.append(rubric.getCriteria()).append("\n");
            } else {
                sb.append("Supervisor has not yet published a rubric.\n");
            }
        } else {
            Database db = Database.getInstance();
            if (!db.getRubrics().isEmpty()) {
                Rubric rubric = db.getRubrics().get(0);
                sb.append("General Criteria:\n");
                sb.append("------------------------------------------\n");
                sb.append(rubric.getCriteria()).append("\n");
            } else {
                sb.append("No institutional rubric available.\n");
            }
        }

        gradesArea.setText(sb.toString());
        gradesArea.setCaretPosition(0);
    }
}
