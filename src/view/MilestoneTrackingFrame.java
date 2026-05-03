package view;

import controller.PhaseController;
import model.Database;
import model.ProjectPhase;
import model.ProjectGroup;
import model.Student;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Milestone Tracking Frame - allows students to view milestones.
 * FR-12 compliance: Update Deadline button is functional for supervisors via
 * ProjectPhaseFrame. Students see a read-only view with refresh capability.
 */
public class MilestoneTrackingFrame extends JFrame {
    private Student student;
    private JList<String> phaseList;
    private DefaultListModel<String> phaseListModel;
    private List<ProjectPhase> loadedPhases;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    public MilestoneTrackingFrame(Student student) {
        this.student = student;
        setTitle("Milestone Tracking");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));
        Theme.applyTheme(panel);

        JLabel head = new JLabel("Project Milestones");
        head.setFont(Theme.HEADING_FONT);
        head.setForeground(Theme.TEXT);
        panel.add(head, BorderLayout.NORTH);

        phaseListModel = new DefaultListModel<>();
        phaseList = new JList<>(phaseListModel);
        Theme.styleList(phaseList);
        phaseList.setFont(Theme.MONO_FONT);

        loadPhases();
        JScrollPane scroll = new JScrollPane(phaseList);
        Theme.styleScrollPane(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        scroll.setPreferredSize(new Dimension(560, 260));
        panel.add(scroll, BorderLayout.CENTER);

        // FR-12: Students view milestones read-only. Supervisors set deadlines via ProjectPhaseFrame.
        JPanel south = new JPanel(new BorderLayout(0, 8));
        south.setOpaque(false);

        JLabel readOnlyHint = new JLabel("Deadlines are set by your supervisor — view only.", SwingConstants.CENTER);
        readOnlyHint.setFont(Theme.MONO_FONT);
        readOnlyHint.setForeground(Theme.TEXT_MUTED);
        south.add(readOnlyHint, BorderLayout.NORTH);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnRow.setOpaque(false);

        JButton refreshBtn = Theme.createOutlineButton("↻ Refresh");
        refreshBtn.addActionListener(e -> loadPhases());

        JButton closeBtn = Theme.createOutlineButton("Close");
        closeBtn.addActionListener(e -> dispose());

        btnRow.add(refreshBtn);
        btnRow.add(closeBtn);
        south.add(btnRow, BorderLayout.CENTER);
        panel.add(south, BorderLayout.SOUTH);

        add(panel);
        pack();
        setMinimumSize(new Dimension(600, 420));
    }

    private void loadPhases() {
        phaseListModel.clear();
        ProjectGroup group = student.getGroup();
        if (group == null) {
            phaseListModel.addElement("No group assigned.");
            return;
        }

        PhaseController pc = new PhaseController();
        loadedPhases = pc.getPhasesForGroup(group.getGroupId());
        if (loadedPhases.isEmpty()) {
            phaseListModel.addElement("No phases defined by supervisor yet.");
            return;
        }

        for (ProjectPhase p : loadedPhases) {
            String deadline = p.getDeadline() != null ? SDF.format(p.getDeadline()) : "TBD";
            String status = p.isCompleted() ? "[DONE]" : "[OPEN]";
            phaseListModel.addElement(String.format("%s %-20s | Deadline: %s", status, p.getPhaseName(), deadline));
        }
    }
}
