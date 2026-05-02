package view;

import controller.ReviewController;
import model.Database;
import model.PeerReview;
import model.ProjectGroup;
import model.Student;
import model.Supervisor;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Aggregated Reviews Frame refined with Monolithic Serenity theme.
 */
public class AggregatedReviewsFrame extends JFrame {
    private Supervisor supervisor; 
    private JComboBox<String> groupCombo;
    private JTextArea resultsArea;

    public AggregatedReviewsFrame(Supervisor supervisor) {
        this.supervisor = supervisor;
        setTitle(supervisor == null ? "Admin — Insights" : "Team Performance Review");
        setSize(700, 600);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        Theme.applyTheme(panel);

        JLabel head = new JLabel("Aggregated Peer Analytics");
        Theme.styleLabel(head, true);

        // Control Panel
        JPanel controls = new JPanel(new BorderLayout(15, 0));
        controls.setOpaque(false);
        
        JLabel l1 = new JLabel("Select Target Group:");
        Theme.styleLabel(l1, false);
        controls.add(l1, BorderLayout.WEST);
        
        groupCombo = new JComboBox<>();
        groupCombo.setBackground(Theme.SURFACE);
        groupCombo.setForeground(Theme.TEXT);
        groupCombo.setFont(Theme.MAIN_FONT);
        loadGroups();
        groupCombo.addActionListener(e -> loadReviewsForSelectedGroup());
        controls.add(groupCombo, BorderLayout.CENTER);
        
        JButton refreshBtn = Theme.createOutlineButton("Refresh Data");
        refreshBtn.addActionListener(e -> loadReviewsForSelectedGroup());
        controls.add(refreshBtn, BorderLayout.EAST);

        JPanel northPanel = new JPanel(new BorderLayout(15, 15));
        northPanel.setOpaque(false);
        northPanel.add(head, BorderLayout.NORTH);
        northPanel.add(controls, BorderLayout.CENTER);
        panel.add(northPanel, BorderLayout.NORTH);

        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setBackground(Theme.SURFACE);
        resultsArea.setForeground(Theme.TEXT);
        resultsArea.setFont(Theme.MONO_FONT);
        resultsArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JScrollPane scroll = new JScrollPane(resultsArea);
        Theme.styleScrollPane(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        panel.add(scroll, BorderLayout.CENTER);

        JButton closeBtn = Theme.createOutlineButton("Close Analytics");
        closeBtn.addActionListener(e -> dispose());
        panel.add(closeBtn, BorderLayout.SOUTH);

        add(panel);

        if (groupCombo.getItemCount() > 0) {
            loadReviewsForSelectedGroup();
        }
    }

    private void loadGroups() {
        groupCombo.removeAllItems();
        Database db = Database.getInstance();
        for (ProjectGroup g : db.getGroups()) {
            boolean include = supervisor == null
                || (g.getSupervisor() != null && g.getSupervisor().getUserId().equals(supervisor.getUserId()));
            if (include) {
                groupCombo.addItem(g.getGroupId() + " — " + g.getProjectTitle());
            }
        }
    }

    private void loadReviewsForSelectedGroup() {
        String selected = (String) groupCombo.getSelectedItem();
        if (selected == null) {
            resultsArea.setText("No groups available.");
            return;
        }
        String groupId = selected.split(" — ")[0].trim();

        ReviewController rc = new ReviewController();
        List<PeerReview> reviews = rc.getReviewsForGroup(groupId);

        if (reviews.isEmpty()) {
            resultsArea.setText("No peer reviews submitted yet for group " + groupId + ".");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("DATA SUMMARY FOR GROUP: ").append(groupId).append("\n");
        sb.append("--------------------------------------------------\n\n");

        Database db = Database.getInstance();
        ProjectGroup group = db.findGroupById(groupId);
        if (group != null) {
            for (Student member : group.getMembers()) {
                sb.append("→ MEMBER: ").append(member.getName().toUpperCase()).append("\n");
                float totalAvg = 0;
                int count = 0;
                for (PeerReview pr : reviews) {
                    if (pr.getReviewee() != null && pr.getReviewee().getUserId().equals(member.getUserId())) {
                        Map<String, Integer> ratings = pr.getRatings();
                        float avg = pr.getAggregated();
                        sb.append("  Reviewed by: ")
                          .append(pr.getReviewer() != null ? pr.getReviewer().getName() : "Anonymous")
                          .append("\n");
                        for (Map.Entry<String, Integer> entry : ratings.entrySet()) {
                            sb.append("    ").append(String.format("%-20s", entry.getKey())).append(": ").append(entry.getValue()).append("/5\n");
                        }
                        sb.append("  SCORE: ").append(String.format("%.2f", avg)).append("/5\n\n");
                        totalAvg += avg;
                        count++;
                    }
                }
                if (count > 0) {
                    sb.append("  ★ GROUP CONTRIBUTION AVG: ")
                      .append(String.format("%.2f", totalAvg / count)).append("/5\n");
                } else {
                    sb.append("  (Insufficient Data)\n");
                }
                sb.append("-".repeat(50)).append("\n");
            }
        }

        resultsArea.setText(sb.toString());
        resultsArea.setCaretPosition(0);
    }
}
