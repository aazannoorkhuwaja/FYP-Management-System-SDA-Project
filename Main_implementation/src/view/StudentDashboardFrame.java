package view;

import model.Database;
import model.Student;
import model.ProjectGroup;
import model.ProjectPhase;
import model.Notification;
import util.DiagnosticService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Student Dashboard refined with Monolithic Serenity theme.
 */
public class StudentDashboardFrame extends JFrame {
    private Student student;
    private DiagnosticService diag;
    private WorkspacePanel workspace;
    private JPanel bodyPanel;
    private JPanel statsPanel;

    public StudentDashboardFrame(Student student) {
        this.student = student;
        this.diag = DiagnosticService.getInstance();
        diag.trace("UI-Student", "Loading dashboard for: " + student.getUserId());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("FYP.MP | Student Dashboard");

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

        Theme.addSidebarButton(sidebar, "Dashboard",
                Theme.traceActionListener("StudentDashboard", "Dashboard", e -> workspace.showHome()));
        Theme.addSidebarSectionLabel(sidebar, "Project");
        Theme.addSidebarButton(sidebar, "Proposals", Theme.traceActionListener("StudentDashboard", "Proposals",
                e -> openModule("Proposals", () -> new SubmitProposalFrame(student))));
        Theme.addSidebarButton(sidebar, "Supervision",
                Theme.traceActionListener("StudentDashboard", "Supervision", e -> {
                    if (student.getGroup() != null && student.getGroup().getSupervisor() != null) {
                        openModule("Current Supervision", () -> new SupervisorProfilesFrame());
                    } else {
                        openModule("Supervision", () -> new SupervisionRequestFrame(student));
                    }
                }));
        Theme.addSidebarButton(sidebar, "Milestones", Theme.traceActionListener("StudentDashboard", "Milestones",
                e -> openModule("Milestones", () -> new MilestoneTrackingFrame(student))));
        Theme.addSidebarButton(sidebar, "Documents", Theme.traceActionListener("StudentDashboard", "Documents",
                e -> openModule("Documents", () -> new DocumentListFrame(student))));
        Theme.addSidebarSectionLabel(sidebar, "People");
        Theme.addSidebarButton(sidebar, "Peer Review", Theme.traceActionListener("StudentDashboard", "PeerReview",
                e -> openModule("Peer Review", () -> new PeerReviewFrame(student))));
        Theme.addSidebarButton(sidebar, "Notifications", Theme.traceActionListener("StudentDashboard", "Notifications",
                e -> openModule("Notifications", () -> new NotificationFrame(student))));

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(Box.createVerticalStrut(10));

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
        contentPanel.setBorder(BorderFactory.createEmptyBorder(32, 36, 36, 36));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.BG);
        headerPanel.setBorder(Theme.dashboardHeaderSeparator());

        JLabel welcomeLabel = new JLabel("Welcome, " + student.getName());
        Theme.styleLabel(welcomeLabel, true);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JLabel groupLabel = new JLabel(
                student.getGroup() != null ? "Group: " + student.getGroup().getGroupId() : "No Group");
        groupLabel.setFont(Theme.MONO_FONT);
        groupLabel.setForeground(Theme.FAST_BLUE);
        headerPanel.add(groupLabel, BorderLayout.EAST);

        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Header Action: Refresh
        JButton refreshBtn = Theme.createOutlineButton("↻ Refresh");
        refreshBtn.addActionListener(e -> refreshStats());
        headerPanel.add(refreshBtn, BorderLayout.CENTER);

        // Scrollable Body
        bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        Theme.applyTheme(bodyPanel);
        bodyPanel.setBackground(Theme.BG);
        bodyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        statsPanel = new JPanel(new GridLayout(1, 5, 16, 0));
        statsPanel.setBackground(Theme.BG);
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(12, 0, 20, 0));
        bodyPanel.add(Theme.wrapNaturalHeight(statsPanel));

        refreshStats();

        JPanel quickLinks = Theme.createDashboardQuickLinksShell();
        addActionSection(quickLinks, "Project Work", new Object[][] {
                { "Browse Ideas",
                        (java.awt.event.ActionListener) e -> openModule("Idea Browsing",
                                () -> new IdeaBrowsingFrame()) },
                { "Progress Log",
                        (java.awt.event.ActionListener) e -> openModule("Progress Log",
                                () -> new ProgressLogFrame(student)) },
                { "Documents",
                        (java.awt.event.ActionListener) e -> openModule("Documents",
                                () -> new DocumentListFrame(student)) }
        });
        addActionSection(quickLinks, "Team & Supervision", new Object[][] {
                { "Team Finder",
                        (java.awt.event.ActionListener) e -> openModule("Team Finder",
                                () -> new MemberListingFrame(student)) },
                { "Available Students",
                        (java.awt.event.ActionListener) e -> openModule("Available Students",
                                () -> new AvailableStudentsFrame(student)) },
                { "Group Management",
                        (java.awt.event.ActionListener) e -> openModule("Group Management",
                                () -> new CreateGroupFrame(student)) },
                { "Supervisor Profiles",
                        (java.awt.event.ActionListener) e -> openModule("Supervisor Profiles",
                                () -> new SupervisorProfilesFrame()) }
        });
        addActionSection(quickLinks, "Assessment & Support", new Object[][] {
                { "Feedback",
                        (java.awt.event.ActionListener) e -> openModule("Feedback",
                                () -> new ProgressReviewFrame(student)) },
                { "Grades",
                        (java.awt.event.ActionListener) e -> openModule("Student Grades",
                                () -> new StudentGradesFrame(student)) },
                { "Rubrics",
                        (java.awt.event.ActionListener) e -> openModule("Grading Rubrics",
                                () -> new RubricViewerFrame()) },
                { "Complaint",
                        (java.awt.event.ActionListener) e -> openModule("Submit Complaint",
                                () -> new ComplaintFrame(student)) },
                { "Archive",
                        (java.awt.event.ActionListener) e -> openModule("Archive Search",
                                () -> new ArchiveSearchFrame()) },
                { "My Profile",
                        (java.awt.event.ActionListener) e -> openModule("My Profile", () -> new ProfileFrame(student)) }
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
    }

    private void addActionSection(JPanel host, String title, Object[][] actions) {
        JPanel section = Theme.createActionSection(title, true);
        JButton[] row = new JButton[actions.length];
        for (int i = 0; i < actions.length; i++) {
            String label = (String) actions[i][0];
            java.awt.event.ActionListener listener = (java.awt.event.ActionListener) actions[i][1];
            JButton btn = Theme.createOutlineButton(label);
            btn.addActionListener(Theme.traceActionListener("StudentDashboard", label, listener));
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
                BorderFactory.createEmptyBorder(16, 18, 16, 18)));

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
    private void refreshStats() {
        statsPanel.removeAll();
        Database db = Database.getInstance();
        ProjectGroup group = student.getGroup();

        int completedPhases = 0;
        int totalPhases = 0;
        String activePhase = "None";
        java.util.Date nextDate = null;
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd");

        if (group != null) {
            List<ProjectPhase> groupPhases = db.getPhasesForGroup(group.getGroupId());
            totalPhases = groupPhases.size();
            for (ProjectPhase p : groupPhases) {
                if (p.isCompleted()) {
                    completedPhases++;
                } else {
                    if (activePhase.equals("None"))
                        activePhase = p.getPhaseName();
                    if (p.getDeadline() != null) {
                        if (nextDate == null || p.getDeadline().before(nextDate)) {
                            nextDate = p.getDeadline();
                        }
                    }
                }
            }
        }

        statsPanel.add(createStatCard("Phases", completedPhases + "/" + totalPhases));
        statsPanel.add(createStatCard("Active", activePhase));
        statsPanel.add(createStatCard("Deadline", nextDate != null ? sdf.format(nextDate) : "TBD"));
        statsPanel.add(createStatCard("Notifications",
                String.valueOf(db.getNotificationsForRecipient(student.getUserId()).size())));
        statsPanel.add(createStatCard("Group", group != null ? group.getGroupId() : "SOLO"));

        statsPanel.revalidate();
        statsPanel.repaint();
    }
}
