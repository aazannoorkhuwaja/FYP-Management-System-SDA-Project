package view;

import controller.PhaseController;
import model.Database;
import model.ProjectPhase;
import model.ProjectGroup;
import model.Student;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

        // FR-12: Update Deadline button — enables updating a selected phase deadline
        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        south.setOpaque(false);

        JButton refreshBtn = Theme.createOutlineButton("↻ Refresh");
        refreshBtn.addActionListener(e -> loadPhases());

        JButton updateDeadlineBtn = Theme.createPrimaryButton("Update Deadline");
        updateDeadlineBtn.setToolTipText("Select a milestone above, then click to update its deadline.");
        updateDeadlineBtn.addActionListener(e -> updateSelectedDeadline());

        JButton closeBtn = Theme.createOutlineButton("Close");
        closeBtn.addActionListener(e -> dispose());

        south.add(refreshBtn);
        south.add(updateDeadlineBtn);
        south.add(closeBtn);
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

    /**
     * FR-12: Update selected milestone deadline.
     * Note: In a full deployment, this action would require supervisor role verification.
     * Students can propose a new deadline which the supervisor must approve.
     */
    private void updateSelectedDeadline() {
        int index = phaseList.getSelectedIndex();
        if (index < 0 || loadedPhases == null || index >= loadedPhases.size()) {
            JOptionPane.showMessageDialog(this, "Please select a milestone first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ProjectPhase selected = loadedPhases.get(index);
        String currentDeadline = selected.getDeadline() != null ? SDF.format(selected.getDeadline()) : "";
        String newDeadlineStr = (String) JOptionPane.showInputDialog(
                this,
                "Enter new deadline for \"" + selected.getPhaseName() + "\":",
                "Update Deadline",
                JOptionPane.PLAIN_MESSAGE,
                null, null,
                currentDeadline);

        if (newDeadlineStr == null || newDeadlineStr.trim().isEmpty()) return;

        Date newDeadline;
        try {
            newDeadline = SDF.parse(newDeadlineStr.trim());
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.", "Format Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PhaseController pc = new PhaseController();
        String result = pc.updatePhase(selected.getPhaseId(), newDeadline);
        if ("Success".equals(result)) {
            JOptionPane.showMessageDialog(this, "Deadline updated successfully!");
            loadPhases();
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
