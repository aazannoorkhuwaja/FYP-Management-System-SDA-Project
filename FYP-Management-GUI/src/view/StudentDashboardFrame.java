package view;

import model.Database;
import model.Student;
import model.FYPProposal;
import model.ProjectGroup;
import util.DiagnosticService;

import javax.swing.*;
import java.awt.*;

/**
 * Student Dashboard refined with Monolithic Serenity theme.
 */
public class StudentDashboardFrame extends JFrame {
    private Student student;
    private DiagnosticService diag;

    public StudentDashboardFrame(Student student) {
        this.student = student;
        this.diag = DiagnosticService.getInstance();
        diag.trace("UI-Student", "Loading dashboard for: " + student.getUserId());

        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("FYP.MP | Student Dashboard");

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

        Theme.addSidebarButton(sidebar, "Dashboard", null);
        Theme.addSidebarButton(sidebar, "Proposals", Theme.traceActionListener("StudentDashboard", "Proposals", e -> new SubmitProposalFrame(student).setVisible(true)));
        Theme.addSidebarButton(sidebar, "Supervision", Theme.traceActionListener("StudentDashboard", "Supervision", e -> new SupervisionRequestFrame(student).setVisible(true)));
        Theme.addSidebarButton(sidebar, "Milestones", Theme.traceActionListener("StudentDashboard", "Milestones", e -> new MilestoneTrackingFrame(student).setVisible(true)));
        Theme.addSidebarButton(sidebar, "Peer Review", Theme.traceActionListener("StudentDashboard", "PeerReview", e -> new PeerReviewFrame(student).setVisible(true)));
        Theme.addSidebarButton(sidebar, "Documents", Theme.traceActionListener("StudentDashboard", "Documents", e -> new DocumentListFrame(student).setVisible(true)));
        
        sidebar.add(Box.createVerticalGlue());

        JButton logoutBtn = Theme.createOutlineButton("Logout");
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutBtn.addActionListener(Theme.traceActionListener("StudentDashboard", "Logout", e -> {
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
        
        JLabel welcomeLabel = new JLabel("Welcome, " + student.getName());
        Theme.styleLabel(welcomeLabel, true);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JLabel groupLabel = new JLabel(student.getGroup() != null ? "Group: " + student.getGroup().getGroupId() : "No Group");
        groupLabel.setFont(Theme.MONO_FONT);
        groupLabel.setForeground(Theme.ACCENT);
        headerPanel.add(groupLabel, BorderLayout.EAST);

        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Scrollable Body
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        Theme.applyTheme(bodyPanel);
        bodyPanel.setBackground(Theme.BG);

        // Stats Grid
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(Theme.BG);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));

        statsPanel.add(createStatCard("Active Phase", "Development"));
        statsPanel.add(createStatCard("Milestones", "2/4"));
        statsPanel.add(createStatCard("Next Deadline", "Dec 15"));

        bodyPanel.add(statsPanel);
        bodyPanel.add(Box.createVerticalStrut(20));

        // Action Buttons Grid
        JLabel actionTitle = new JLabel("Quick Actions");
        Theme.styleLabel(actionTitle, false);
        actionTitle.setFont(Theme.BOLD_FONT);
        actionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        bodyPanel.add(actionTitle);
        bodyPanel.add(Box.createVerticalStrut(15));

        JPanel actionsGrid = new JPanel(new GridLayout(0, 3, 10, 10));
        actionsGrid.setBackground(Theme.BG);
        actionsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        addActionButton(actionsGrid, "Idea Browsing", e -> new IdeaBrowsingFrame().setVisible(true));
        addActionButton(actionsGrid, "Browse Members", e -> new MemberListingFrame(student).setVisible(true));
        addActionButton(actionsGrid, "Progress Log", e -> new ProgressLogFrame(student).setVisible(true));
        addActionButton(actionsGrid, "View Feedback", e -> new ProgressReviewFrame(student).setVisible(true));
        addActionButton(actionsGrid, "Student Grades", e -> new StudentGradesFrame(student).setVisible(true));
        addActionButton(actionsGrid, "Grading Rubrics", e -> new RubricViewerFrame().setVisible(true));
        addActionButton(actionsGrid, "Group Mgmt", e -> new CreateGroupFrame(student).setVisible(true));
        addActionButton(actionsGrid, "Submit Complaint", e -> new ComplaintFrame(student).setVisible(true));

        bodyPanel.add(actionsGrid);

        JScrollPane bodyScroll = new JScrollPane(bodyPanel);
        Theme.styleScrollPane(bodyScroll);
        contentPanel.add(bodyScroll, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void addActionButton(JPanel panel, String text, java.awt.event.ActionListener listener) {
        JButton btn = Theme.createOutlineButton(text);
        btn.addActionListener(Theme.traceActionListener("StudentDashboard", text, listener));
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
}
