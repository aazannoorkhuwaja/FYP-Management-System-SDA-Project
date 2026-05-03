package view;

import controller.PhaseController;
import model.Database;
import model.ProjectPhase;
import model.ProjectGroup;
import model.Student;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Milestone Tracking Frame refined with Monolithic Serenity theme.
 */
public class MilestoneTrackingFrame extends JFrame {
    private Student student;
    private JList<String> phaseList;
    private DefaultListModel<String> phaseListModel;

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

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        south.setOpaque(false);
        JButton closeBtn = Theme.createOutlineButton("Close");
        closeBtn.addActionListener(e -> dispose());
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
        List<ProjectPhase> phases = pc.getPhasesForGroup(group.getGroupId());
        if (phases.isEmpty()) {
            phaseListModel.addElement("No phases defined by supervisor yet.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (ProjectPhase p : phases) {
            String deadline = p.getDeadline() != null ? sdf.format(p.getDeadline()) : "TBD";
            phaseListModel.addElement(String.format("%-20s | Deadline: %s", p.getPhaseName(), deadline));
        }
    }
}
