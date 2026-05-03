package view;

import model.Supervisor;
import model.Database;
import model.SupervisionRequest;
import controller.RequestController;
import util.DiagnosticService;

import javax.swing.*;
import java.awt.*;

/**
 * Supervisor Dashboard refined with Monolithic Serenity theme.
 */
public class SupervisorDashboardFrame extends JFrame {
    private Supervisor supervisor;
    private DiagnosticService diag;
    private Database db;
    private WorkspacePanel workspace;
    private JLabel capLabel;
    private DefaultListModel<String> requestListModel;

    public SupervisorDashboardFrame(Supervisor supervisor) {
        this.supervisor = supervisor;
        this.diag = DiagnosticService.getInstance();
        this.db = Database.getInstance();
        diag.trace("UI-Supervisor", "Loading dashboard for: " + supervisor.getUserId());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("FYP.MP | Supervisor Dashboard");

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

        Theme.addSidebarButton(sidebar, "Dashboard", Theme.traceActionListener("SupervisorDashboard", "Dashboard", e -> workspace.showHome()));
        Theme.addSidebarSectionLabel(sidebar, "Supervision");
        Theme.addSidebarButton(sidebar, "Define Phase", Theme.traceActionListener("SupervisorDashboard", "DefinePhase", e -> openModule("Define Phase", () -> new ProjectPhaseFrame(supervisor))));
        Theme.addSidebarButton(sidebar, "Progress Review", Theme.traceActionListener("SupervisorDashboard", "ProgressReview", e -> openModule("Progress Review", () -> new ProgressReviewFrame(supervisor))));
        Theme.addSidebarButton(sidebar, "View Peer Reviews", Theme.traceActionListener("SupervisorDashboard", "PeerReviews", e -> openModule("Peer Reviews", () -> new AggregatedReviewsFrame(supervisor))));
        Theme.addSidebarSectionLabel(sidebar, "Assessment");
        Theme.addSidebarButton(sidebar, "Rubric Definition", Theme.traceActionListener("SupervisorDashboard", "RubricDef", e -> openModule("Rubric Definition", () -> new RubricDefinitionFrame(supervisor))));
        Theme.addSidebarButton(sidebar, "Grade Students", Theme.traceActionListener("SupervisorDashboard", "Grading", e -> openModule("Grade Students", () -> new GradingFrame(supervisor))));
        Theme.addSidebarSectionLabel(sidebar, "Account");
        Theme.addSidebarButton(sidebar, "Notifications", Theme.traceActionListener("SupervisorDashboard", "Notifications", e -> openModule("Notifications", () -> new NotificationFrame(supervisor))));
        Theme.addSidebarButton(sidebar, "My Profile", Theme.traceActionListener("SupervisorDashboard", "MyProfile", e -> openModule("My Profile", () -> new ProfileFrame(supervisor))));
        
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(Box.createVerticalStrut(10));

        JButton logoutBtn = Theme.createOutlineButton("Logout");
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutBtn.addActionListener(Theme.traceActionListener("SupervisorDashboard", "Logout", e -> {
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
        
        JLabel welcomeLabel = new JLabel("Welcome, " + supervisor.getName());
        Theme.styleLabel(welcomeLabel, true);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        capLabel = new JLabel("Capacity: " + supervisor.getCurrentGroups() + "/" + supervisor.getMaxGroups());
        capLabel.setFont(Theme.MONO_FONT);
        capLabel.setForeground(Theme.FAST_BLUE);
        headerPanel.add(capLabel, BorderLayout.EAST);
        
        JButton refreshBtn = Theme.createOutlineButton("↻ Refresh");
        refreshBtn.addActionListener(e -> refreshData());
        headerPanel.add(refreshBtn, BorderLayout.CENTER);

        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Scrollable Body
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        Theme.applyTheme(bodyPanel);
        bodyPanel.setBackground(Theme.BG);
        bodyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel pendingBlock = new JPanel();
        pendingBlock.setLayout(new BoxLayout(pendingBlock, BoxLayout.Y_AXIS));
        pendingBlock.setOpaque(false);
        pendingBlock.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel reqHeader = new JLabel("Pending Supervision Requests");
        Theme.styleLabel(reqHeader, false);
        reqHeader.setFont(Theme.BOLD_FONT);
        reqHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        pendingBlock.add(Box.createVerticalStrut(8));
        pendingBlock.add(reqHeader);
        pendingBlock.add(Box.createVerticalStrut(12));

        JPanel requestCard = new JPanel(new BorderLayout());
        requestCard.setBackground(Theme.SURFACE);
        requestCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        requestCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        requestCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        requestListModel = new DefaultListModel<>();
        JList<String> requestList = new JList<>(requestListModel);
        Theme.styleList(requestList);
        requestList.setFont(Theme.MONO_FONT);
        
        refreshData();

        JScrollPane requestScroll = new JScrollPane(requestList);
        Theme.styleScrollPane(requestScroll, false);
        requestCard.add(requestScroll, BorderLayout.CENTER);

        JPanel requestActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        requestActionPanel.setBackground(Theme.SURFACE);
        JButton acceptBtn = Theme.createPrimaryButton("Accept");
        JButton declineBtn = Theme.createOutlineButton("Decline");
        
        acceptBtn.addActionListener(Theme.traceActionListener("SupervisorDashboard", "AcceptRequest", e -> {
            String selected = requestList.getSelectedValue();
            if (selected == null) return;
            String reqId = selected.split(" \\| ")[0];
            diag.info("UI-Supervisor", "Accepting request via controller: " + reqId);
            String result = new RequestController().acceptRequest(reqId);
            if ("Success".equals(result)) {
                // Refresh in-memory supervisor capacity label
                supervisor.setCurrentGroups(db.findGroupById(
                    db.getRequests().stream()
                        .filter(r -> r.getRequestId().equals(reqId))
                        .findFirst().map(r -> r.getGroup().getGroupId()).orElse(""))
                    == null ? supervisor.getCurrentGroups() : supervisor.getCurrentGroups());
            }
            refreshData();
        }));

        declineBtn.addActionListener(Theme.traceActionListener("SupervisorDashboard", "DeclineRequest", e -> {
            String selected = requestList.getSelectedValue();
            if (selected == null) return;
            String reason = JOptionPane.showInputDialog(this, "Enter rejection reason:");
            if (reason == null) return;
            String reqId = selected.split(" \\| ")[0];
            for (SupervisionRequest r : db.getRequests()) {
                if (r.getRequestId().equals(reqId)) {
                    diag.info("UI-Supervisor", "Declining request: " + reqId + " Reason: " + reason);
                    r.decline(reason);
                    db.saveToFile();
                    refreshData();
                    break;
                }
            }
        }));

        requestActionPanel.add(declineBtn);
        requestActionPanel.add(acceptBtn);
        requestCard.add(requestActionPanel, BorderLayout.SOUTH);
        pendingBlock.add(requestCard);

        bodyPanel.add(Theme.wrapNaturalHeight(pendingBlock));

        JPanel quickLinks = Theme.createDashboardQuickLinksShell();
        addActionSection(quickLinks, "Project Setup", new Object[][] {
            {"Define Phase", (java.awt.event.ActionListener) e -> openModule("Define Phase", () -> new ProjectPhaseFrame(supervisor))},
            {"Idea Bank", (java.awt.event.ActionListener) e -> openModule("Idea Bank", () -> new IdeaBankFrame(supervisor))},
            {"Archive Search", (java.awt.event.ActionListener) e -> openModule("Archive Search", () -> new ArchiveSearchFrame())}
        });
        addActionSection(quickLinks, "Review & Assessment", new Object[][] {
            {"Progress Review", (java.awt.event.ActionListener) e -> openModule("Progress Review", () -> new ProgressReviewFrame(supervisor))},
            {"Peer Reviews", (java.awt.event.ActionListener) e -> openModule("Peer Reviews", () -> new AggregatedReviewsFrame(supervisor))},
            {"Grade Students", (java.awt.event.ActionListener) e -> openModule("Grade Students", () -> new GradingFrame(supervisor))},
            {"Rubrics", (java.awt.event.ActionListener) e -> openModule("Rubric Definition", () -> new RubricDefinitionFrame(supervisor))},
            {"Complaints", (java.awt.event.ActionListener) e -> openModule("Complaints", () -> new ComplaintReviewFrame(supervisor))},
            {"My Profile", (java.awt.event.ActionListener) e -> openModule("My Profile", () -> new ProfileFrame(supervisor))}
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
            btn.addActionListener(Theme.traceActionListener("SupervisorDashboard", label, listener));
            row[i] = btn;
        }
        section.add(Theme.buildActionButtonGrid(3, row));
        host.add(section);
    }

    private void openModule(String title, java.util.function.Supplier<JFrame> frameFactory) {
        workspace.openFrame(title, frameFactory);
    }

    private void refreshData() {
        capLabel.setText("Capacity: " + supervisor.getCurrentGroups() + "/" + supervisor.getMaxGroups());
        requestListModel.clear();
        Database db = Database.getInstance();
        for (SupervisionRequest r : db.getRequests()) {
            if (r.getSupervisor() != null && r.getSupervisor().getUserId().equals(supervisor.getUserId())
                    && r.getStatus().equals("Pending")) {
                requestListModel.addElement(
                        r.getRequestId() + " | Group: " + (r.getGroup() != null ? r.getGroup().getGroupId() : "Unknown"));
            }
        }
    }
}
