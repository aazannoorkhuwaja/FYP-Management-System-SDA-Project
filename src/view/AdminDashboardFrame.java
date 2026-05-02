package view;

import model.Admin;
import model.Database;
import model.ProjectGroup;
import model.Complaint;
import model.User;
import controller.ComplaintController;
import util.DiagnosticService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Admin Dashboard refined with Monolithic Serenity theme.
 */
public class AdminDashboardFrame extends JFrame {
    private Admin admin;
    private DiagnosticService diag;

    public AdminDashboardFrame(Admin admin) {
        this.admin = admin;
        this.diag = DiagnosticService.getInstance();
        diag.trace("UI-Admin", "Loading dashboard for: " + admin.getUserId());

        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("FYP.MP | Admin Dashboard");

        JPanel mainPanel = new JPanel(new BorderLayout());
        Theme.applyTheme(mainPanel);

        // --- Sidebar ---
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBackground(Theme.SURFACE);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        JLabel logoLabel = new JLabel("FYP.MP");
        logoLabel.setFont(Theme.LOGO_FONT);
        logoLabel.setForeground(Theme.TEXT);
        logoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logoLabel);
        sidebar.add(Box.createVerticalStrut(40));

        Theme.addSidebarButton(sidebar, "Overview", null);
        Theme.addSidebarButton(sidebar, "User Management", Theme.traceActionListener("AdminDashboard", "UserMgmt", e -> viewAllUsers()));
        Theme.addSidebarButton(sidebar, "Group Management", Theme.traceActionListener("AdminDashboard", "GroupMgmt", e -> new GroupManagementFrame().setVisible(true)));
        Theme.addSidebarButton(sidebar, "Pending Complaints", Theme.traceActionListener("AdminDashboard", "Complaints", e -> viewPendingComplaints()));
        Theme.addSidebarButton(sidebar, "Global Rubric", Theme.traceActionListener("AdminDashboard", "GlobalRubric", e -> publishGlobalRubric()));
        Theme.addSidebarButton(sidebar, "Inactivity Alerts", Theme.traceActionListener("AdminDashboard", "InactivityAlerts", e -> sendInactivityAlerts()));
        
        sidebar.add(Box.createVerticalGlue());

        JButton logoutBtn = Theme.createOutlineButton("Logout");
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutBtn.addActionListener(Theme.traceActionListener("AdminDashboard", "Logout", e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        }));
        sidebar.add(logoutBtn);

        mainPanel.add(sidebar, BorderLayout.WEST);

        // --- Content Area ---
        JPanel contentPanel = new JPanel(new BorderLayout());
        Theme.applyTheme(contentPanel);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.BG);
        
        JLabel welcomeLabel = new JLabel("Admin Control Center");
        Theme.styleLabel(welcomeLabel, true);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JLabel adminLabel = new JLabel("System Administrator");
        adminLabel.setFont(Theme.MONO_FONT);
        adminLabel.setForeground(Theme.ACCENT);
        headerPanel.add(adminLabel, BorderLayout.EAST);

        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Scrollable Body
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        Theme.applyTheme(bodyPanel);
        bodyPanel.setBackground(Theme.BG);

        // Stats Row
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setBackground(Theme.BG);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        Database db = Database.getInstance();
        
        statsPanel.add(createStatCard("Users", String.valueOf(db.getUsers().size())));
        statsPanel.add(createStatCard("Groups", String.valueOf(db.getGroups().size())));
        statsPanel.add(createStatCard("Proposals", String.valueOf(db.getProposals().size())));
        statsPanel.add(createStatCard("Complaints", String.valueOf(db.getComplaints().size())));

        bodyPanel.add(statsPanel);
        bodyPanel.add(Box.createVerticalStrut(30));

        // Quick Actions Section
        JLabel actionTitle = new JLabel("Management Utilities");
        Theme.styleLabel(actionTitle, false);
        actionTitle.setFont(Theme.BOLD_FONT);
        actionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        bodyPanel.add(actionTitle);
        bodyPanel.add(Box.createVerticalStrut(15));

        JPanel actionsGrid = new JPanel(new GridLayout(0, 3, 20, 20));
        actionsGrid.setBackground(Theme.BG);
        actionsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        addActionButton(actionsGrid, "Archive Search", e -> new ArchiveSearchFrame().setVisible(true));
        addActionButton(actionsGrid, "Browse Members", e -> new MemberListingFrame().setVisible(true));
        addActionButton(actionsGrid, "View Profile", e -> new ProfileFrame(admin).setVisible(true));
        addActionButton(actionsGrid, "Supervisor Limits", e -> setSupervisorGroupLimit());
        addActionButton(actionsGrid, "Peer Reviews", e -> new AggregatedReviewsFrame(null).setVisible(true));

        bodyPanel.add(actionsGrid);

        JScrollPane bodyScroll = new JScrollPane(bodyPanel);
        Theme.styleScrollPane(bodyScroll);
        contentPanel.add(bodyScroll, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }


    private void addActionButton(JPanel panel, String text, java.awt.event.ActionListener listener) {
        JButton btn = Theme.createOutlineButton(text);
        btn.addActionListener(Theme.traceActionListener("AdminDashboard", text, listener));
        panel.add(btn);
    }

    private JPanel createStatCard(String label, String value) {
        JPanel card = new JPanel(new BorderLayout());
        Theme.applySurface(card);
        card.setBorder(BorderFactory.createCompoundBorder(
            card.getBorder(),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel l = new JLabel(label);
        l.setFont(Theme.MAIN_FONT);
        l.setForeground(Theme.TEXT_MUTED);
        card.add(l, BorderLayout.NORTH);

        JLabel v = new JLabel(value);
        v.setFont(Theme.SUBHEADING_FONT);
        v.setForeground(Theme.TEXT);
        card.add(v, BorderLayout.CENTER);

        return card;
    }

    // Logic Methods (Preserved from original)
    private void viewAllUsers() {
        StringBuilder sb = new StringBuilder();
        Database db = Database.getInstance();
        for (User u : db.getUsers()) {
            sb.append(u.getUserId()).append(" - ").append(u.getName()).append(" (").append(u.getClass().getSimpleName()).append(")\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "All Users", JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewPendingComplaints() {
        new ComplaintReviewFrame(admin).setVisible(true);
    }

    private void setSupervisorGroupLimit() {
        Database db = Database.getInstance();
        java.util.List<model.Supervisor> supervisors = new java.util.ArrayList<>();
        for (User u : db.getUsers()) {
            if (u instanceof model.Supervisor) {
                supervisors.add((model.Supervisor) u);
            }
        }
        if (supervisors.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No supervisors registered.");
            return;
        }
        String[] supervisorNames = new String[supervisors.size()];
        for (int i = 0; i < supervisors.size(); i++) {
            model.Supervisor sv = supervisors.get(i);
            supervisorNames[i] = sv.getUserId() + " - " + sv.getName() + "  (current limit: " + sv.getMaxGroups() + ")";
        }
        String chosen = (String) JOptionPane.showInputDialog(this, "Select Supervisor:", "Set Group Limit",
                JOptionPane.QUESTION_MESSAGE, null, supervisorNames, supervisorNames[0]);
        if (chosen == null) return;
        String newLimitStr = JOptionPane.showInputDialog(this, "Enter new maximum group limit:");
        if (newLimitStr == null || newLimitStr.trim().isEmpty()) return;
        try {
            int newLimit = Integer.parseInt(newLimitStr.trim());
            if (newLimit < 1) throw new NumberFormatException();
            String supervisorId = chosen.split(" - ")[0];
            for (model.Supervisor sv : supervisors) {
                if (sv.getUserId().equals(supervisorId)) {
                    diag.info("UI-Admin", "Updating group limit for " + sv.getUserId() + " to " + newLimit);
                    sv.setMaxGroups(newLimit);
                    db.saveToFile();
                    JOptionPane.showMessageDialog(this, "Group limit updated to " + newLimit + " for " + sv.getName() + ".");
                    break;
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid positive integer.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void publishGlobalRubric() {
        JTextArea criteriaArea = new JTextArea(8, 35);
        criteriaArea.setText("Example:\n1. Communication Skills (20%)\n2. Work Quality (40%)\n3. Timeliness (20%)\n4. Innovation (20%)");
        criteriaArea.setLineWrap(true);
        criteriaArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(criteriaArea);
        scroll.setBorder(BorderFactory.createTitledBorder("Enter Semester Rubric Criteria:"));

        int result = JOptionPane.showConfirmDialog(this, scroll,
                "Publish Global Grading Rubric", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        String criteria = criteriaArea.getText().trim();
        if (criteria.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Criteria cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        diag.info("UI-Admin", "Publishing global rubric");
        controller.GradeController gc = new controller.GradeController();
        String pubResult = gc.defineRubric(admin.getUserId(), criteria);
        if ("Success".equals(pubResult)) {
            JOptionPane.showMessageDialog(this, "Global rubric published successfully!");
        } else {
            JOptionPane.showMessageDialog(this, pubResult, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sendInactivityAlerts() {
        diag.info("UI-Admin", "Triggering inactivity alerts");
        controller.AdminController ac = new controller.AdminController();
        List<String> alerted = ac.sendInactivityAlerts(admin);
        if (alerted.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No inactive groups found.");
        } else {
            StringBuilder sb = new StringBuilder("Inactivity alerts sent to the following groups:\n\n");
            for (String group : alerted) {
                sb.append("- ").append(group).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Inactivity Alerts Sent", JOptionPane.WARNING_MESSAGE);
        }
    }
}
