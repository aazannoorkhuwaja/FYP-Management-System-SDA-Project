package view;

import controller.GradeController;
import model.Database;
import model.ProjectGroup;
import model.Rubric;
import model.Student;
import model.Supervisor;

import javax.swing.*;
import java.awt.*;

/**
 * Grading Frame refined with Monolithic Serenity theme.
 */
public class GradingFrame extends JFrame {
    private Supervisor supervisor;
    private JComboBox<String> groupCombo;
    private JComboBox<String> studentCombo;
    private JSpinner marksSpinner;
    private JTextArea rubricArea;

    public GradingFrame(Supervisor supervisor) {
        this.supervisor = supervisor;
        setTitle("Academic Assessment");
        setSize(600, 500);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        Theme.applyTheme(panel);

        JLabel head = new JLabel("Assign Final Grades");
        Theme.styleLabel(head, true);
        panel.add(head, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(0, 20));
        body.setOpaque(false);

        // Rubric Context
        rubricArea = new JTextArea(6, 40);
        rubricArea.setEditable(false);
        rubricArea.setBackground(Theme.SURFACE);
        rubricArea.setForeground(Theme.TEXT_MUTED);
        rubricArea.setFont(Theme.MONO_FONT);
        loadRubric();
        JScrollPane rubricScroll = new JScrollPane(rubricArea);
        Theme.styleScrollPane(rubricScroll);
        rubricScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Theme.BORDER), "Reference Rubric", 0, 0, Theme.BOLD_FONT, Theme.ACCENT));
        body.add(rubricScroll, BorderLayout.NORTH);

        // Entry Form
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        form.add(createLabel("Target Group"));
        groupCombo = new JComboBox<>();
        styleCombo(groupCombo);
        Database db = Database.getInstance();
        for (ProjectGroup g : db.getGroups()) {
            if (g.getSupervisor() != null && g.getSupervisor().getUserId().equals(supervisor.getUserId())) {
                groupCombo.addItem(g.getGroupId() + " - " + g.getProjectTitle());
            }
        }
        form.add(groupCombo);
        form.add(Box.createVerticalStrut(15));

        form.add(createLabel("Individual Student"));
        studentCombo = new JComboBox<>();
        styleCombo(studentCombo);
        groupCombo.addActionListener(e -> loadStudents());
        form.add(studentCombo);
        form.add(Box.createVerticalStrut(15));

        form.add(createLabel("Final Marks (0-100)"));
        marksSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        marksSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        form.add(marksSpinner);

        body.add(form, BorderLayout.CENTER);
        panel.add(body, BorderLayout.CENTER);

        JPanel footer = new JPanel(new GridLayout(1, 2, 10, 0));
        footer.setOpaque(false);
        JButton closeBtn = Theme.createOutlineButton("Cancel");
        closeBtn.addActionListener(e -> dispose());
        JButton assignBtn = Theme.createPrimaryButton("Post Official Grade");
        assignBtn.addActionListener(e -> assignGrade());
        footer.add(closeBtn);
        footer.add(assignBtn);
        panel.add(footer, BorderLayout.SOUTH);

        add(panel);

        if (groupCombo.getItemCount() > 0) loadStudents();
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        Theme.styleLabel(l, false);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void styleCombo(JComboBox<String> combo) {
        combo.setBackground(Theme.SURFACE);
        combo.setForeground(Theme.TEXT);
        combo.setFont(Theme.MAIN_FONT);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void loadRubric() {
        GradeController gc = new GradeController();
        Rubric rubric = gc.getRubricBySupervisor(supervisor.getUserId());
        if (rubric != null) {
            rubricArea.setText(rubric.getCriteria());
        } else {
            rubricArea.setText("No rubric defined yet. Please define criteria before grading.");
        }
        rubricArea.setCaretPosition(0);
    }

    private void loadStudents() {
        studentCombo.removeAllItems();
        String selected = (String) groupCombo.getSelectedItem();
        if (selected == null) return;
        String groupId = selected.split(" - ")[0];

        Database db = Database.getInstance();
        ProjectGroup group = db.findGroupById(groupId);
        if (group != null) {
            for (Student s : group.getMembers()) {
                studentCombo.addItem(s.getUserId() + " - " + s.getName());
            }
        }
    }

    private void assignGrade() {
        String groupSel = (String) groupCombo.getSelectedItem();
        String studentSel = (String) studentCombo.getSelectedItem();
        int marks = (Integer) marksSpinner.getValue();

        if (groupSel == null || studentSel == null) {
            JOptionPane.showMessageDialog(this, "Select group and student.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        GradeController gc = new GradeController();
        String studentId = studentSel.split(" - ")[0];
        String result = gc.enterGrade(supervisor, studentId, marks);
        if ("Success".equals(result)) {
            JOptionPane.showMessageDialog(this, "Grade successfully posted!");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
