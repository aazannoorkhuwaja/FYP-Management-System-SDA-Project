package view;

import model.Admin;
import model.Database;
import model.User;
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
    private WorkspacePanel workspace;
    private JPanel statsPanel;

    public AdminDashboardFrame(Admin admin) {
        this.admin = admin;
        this.diag = DiagnosticService.getInstance();
        diag.trace("UI-Admin", "Loading dashboard for: " + admin.getUserId());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("FYP.MP | Admin Dashboard");

        JPanel mainPanel = new JPanel(new BorderLayout());
        Theme.applyTheme(mainPanel);

        // --- Sidebar ---
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBackground(Theme.FAST_BLUE_DARK);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(28, 20, 32, 24));

        JLabel logoLabel = new JLabel("FYP.MP");
        logoLabel.setFont(Theme.LOGO_FONT);
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logoLabel);
        sidebar.add(Box.createVerticalStrut(40));

        Theme.addSidebarButton(sidebar, "Overview", Theme.traceActionListener("AdminDashboard", "Overview", e -> workspace.showHome()));
        Theme.addSidebarSectionLabel(sidebar, "System");
        Theme.addSidebarButton(sidebar, "User Management", Theme.traceActionListener("AdminDashboard", "UserMgmt", e -> viewAllUsers()));
        Theme.addSidebarButton(sidebar, "Group Management", Theme.traceActionListener("AdminDashboard", "GroupMgmt", e -> openModule("Group Management", () -> new GroupManagementFrame())));
        Theme.addSidebarSectionLabel(sidebar, "Governance");
        Theme.addSidebarButton(sidebar, "Pending Complaints", Theme.traceActionListener("AdminDashboard", "Complaints", e -> viewPendingComplaints()));
        Theme.addSidebarButton(sidebar, "Global Rubric", Theme.traceActionListener("AdminDashboard", "GlobalRubric", e -> publishGlobalRubric()));
        Theme.addSidebarButton(sidebar, "Inactivity Alerts", Theme.traceActionListener("AdminDashboard", "InactivityAlerts", e -> sendInactivityAlerts()));
        Theme.addSidebarSectionLabel(sidebar, "Account");
        Theme.addSidebarButton(sidebar, "Notifications", Theme.traceActionListener("AdminDashboard", "Notifications", e -> openModule("Notifications", () -> new NotificationFrame(admin))));
        
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(Box.createVerticalStrut(10));

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
        contentPanel.setBorder(BorderFactory.createEmptyBorder(32, 36, 36, 36));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.BG);
        headerPanel.setBorder(Theme.dashboardHeaderSeparator());
        
        JLabel welcomeLabel = new JLabel("Admin Control Center");
        Theme.styleLabel(welcomeLabel, true);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JLabel adminLabel = new JLabel("System Administrator");
        adminLabel.setFont(Theme.MONO_FONT);
        adminLabel.setForeground(Theme.FAST_BLUE);
        headerPanel.add(adminLabel, BorderLayout.EAST);

        // Header Action: Refresh
        JButton refreshBtn = Theme.createOutlineButton("↻ Refresh");
        refreshBtn.addActionListener(e -> refreshStats());
        headerPanel.add(refreshBtn, BorderLayout.CENTER);

        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Scrollable Body
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        Theme.applyTheme(bodyPanel);
        bodyPanel.setBackground(Theme.BG);
        bodyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Stats Row
        statsPanel = new JPanel(new GridLayout(1, 4, 16, 0));
        statsPanel.setBackground(Theme.BG);
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(12, 0, 20, 0));
        
        refreshStats();

        bodyPanel.add(Theme.wrapNaturalHeight(statsPanel));

        JPanel quickLinks = Theme.createDashboardQuickLinksShell();
        addActionSection(quickLinks, "System Management", new Object[][] {
            {"Archive Projects", (java.awt.event.ActionListener) e -> openModule("Archive Projects", () -> new ArchiveManagementFrame())},
            {"Group Size Limit", (java.awt.event.ActionListener) e -> setGlobalGroupLimit()},
            {"Browse Members", (java.awt.event.ActionListener) e -> openModule("Browse Members", () -> new MemberListingFrame())}
        });
        addActionSection(quickLinks, "Oversight & Controls", new Object[][] {
            {"Supervisor Limits", (java.awt.event.ActionListener) e -> setSupervisorGroupLimit()},
            {"Peer Reviews", (java.awt.event.ActionListener) e -> openModule("Peer Reviews", () -> new AggregatedReviewsFrame(null))},
            {"Pending Complaints", (java.awt.event.ActionListener) e -> viewPendingComplaints()},
            {"View Profile", (java.awt.event.ActionListener) e -> openModule("View Profile", () -> new ProfileFrame(admin))},
            {"Notifications", (java.awt.event.ActionListener) e -> openModule("Notifications", () -> new NotificationFrame(admin))}
        });

        bodyPanel.add(Theme.wrapNaturalHeight(quickLinks));
        bodyPanel.add(Box.createVerticalGlue());

        JScrollPane bodyScroll = new JScrollPane(bodyPanel);
        bodyScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        Theme.styleScrollPane(bodyScroll, true);
        workspace = new WorkspacePanel(bodyScroll);
        contentPanel.add(workspace, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
        pack();
        Theme.maximizeFrame(this);

        // FR-16: Auto-check inactivity on load
        checkInactivityAlerts();
    }

    private void addActionSection(JPanel host, String title, Object[][] actions) {
        JPanel section = Theme.createActionSection(title, true);
        JButton[] row = new JButton[actions.length];
        for (int i = 0; i < actions.length; i++) {
            String label = (String) actions[i][0];
            java.awt.event.ActionListener listener = (java.awt.event.ActionListener) actions[i][1];
            JButton btn = Theme.createOutlineButton(label);
            btn.addActionListener(Theme.traceActionListener("AdminDashboard", label, listener));
            row[i] = btn;
        }
        section.add(Theme.buildActionButtonGrid(3, row));
        host.add(section);
    }

    private void openModule(String title, java.util.function.Supplier<JFrame> frameFactory) {
        workspace.openFrame(title, frameFactory);
    }

    private JPanel createStatCard(String label, String value) {
        JPanel card = new JPanel(new BorderLayout());
        Theme.applySurface(card);
        card.setBorder(BorderFactory.createCompoundBorder(
            card.getBorder(),
            BorderFactory.createEmptyBorder(16, 18, 16, 18)
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

    private void checkInactivityAlerts() {
        Database db = Database.getInstance();
        java.util.List<model.ProjectGroup> inactive = db.getInactiveGroups(7);
        if (!inactive.isEmpty()) {
            StringBuilder sb = new StringBuilder("The following groups have not submitted logs in 7+ days:\n\n");
            for (model.ProjectGroup g : inactive) {
                sb.append("- ").append(g.getGroupId()).append("\n");
            }
            sb.append("\nConsider triggering Inactivity Alerts from the sidebar.");
            JOptionPane.showMessageDialog(this, sb.toString(), "FR-16: Inactive Groups Detected", JOptionPane.WARNING_MESSAGE);
        }
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
        openModule("Pending Complaints", () -> new ComplaintReviewFrame(admin));
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

    private void setGlobalGroupLimit() {
        Database db = Database.getInstance();
        String current = String.valueOf(db.getMaxGroupSize());
        String input = JOptionPane.showInputDialog(this, "Set Maximum Students per Group:", current);
        if (input == null || input.trim().isEmpty()) return;

        try {
            int limit = Integer.parseInt(input.trim());
            if (limit < 1 || limit > 10) {
                JOptionPane.showMessageDialog(this, "Limit must be between 1 and 10.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            db.setMaxGroupSize(limit);
            db.saveToFile();
            diag.info("Admin", "Global group size limit set to: " + limit);
            JOptionPane.showMessageDialog(this, "Global group limit updated to " + limit);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void publishGlobalRubric() {
        JTextArea criteriaArea = new JTextArea(8, 35);
        Theme.styleTextArea(criteriaArea);
        criteriaArea.setText("Example:\n1. Communication Skills (20%)\n2. Work Quality (40%)\n3. Timeliness (20%)\n4. Innovation (20%)");
        JScrollPane scroll = new JScrollPane(criteriaArea);
        Theme.styleScrollPane(scroll);
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

    private void refreshStats() {
        statsPanel.removeAll();
        Database db = Database.getInstance();
        statsPanel.add(createStatCard("Users", String.valueOf(db.getUsers().size())));
        statsPanel.add(createStatCard("Groups", String.valueOf(db.getGroups().size())));
        statsPanel.add(createStatCard("Proposals", String.valueOf(db.getProposals().size())));
        statsPanel.add(createStatCard("Complaints", String.valueOf(db.getComplaints().size())));
        statsPanel.revalidate();
        statsPanel.repaint();
    }
}
