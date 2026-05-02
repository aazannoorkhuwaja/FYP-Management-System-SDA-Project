package view;

import model.Supervisor;
import model.Database;
import model.SupervisionRequest;
import model.Student;
import model.ProjectGroup;
import util.DiagnosticService;
import java.util.List;

import javax.swing.*;
import java.awt.*;

/**
 * Supervisor Dashboard refined with Monolithic Serenity theme.
 */
public class SupervisorDashboardFrame extends JFrame {
    private Supervisor supervisor;
    private DiagnosticService diag;

    public SupervisorDashboardFrame(Supervisor supervisor) {
        this.supervisor = supervisor;
        this.diag = DiagnosticService.getInstance();
        diag.trace("UI-Supervisor", "Loading dashboard for: " + supervisor.getUserId());

        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("FYP.MP | Supervisor Dashboard");

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
        Theme.addSidebarButton(sidebar, "Define Phase", Theme.traceActionListener("SupervisorDashboard", "DefinePhase", e -> new ProjectPhaseFrame(supervisor).setVisible(true)));
        Theme.addSidebarButton(sidebar, "Rubric Definition", Theme.traceActionListener("SupervisorDashboard", "RubricDef", e -> new RubricDefinitionFrame(supervisor).setVisible(true)));
        Theme.addSidebarButton(sidebar, "Grade Students", Theme.traceActionListener("SupervisorDashboard", "Grading", e -> new GradingFrame(supervisor).setVisible(true)));
        Theme.addSidebarButton(sidebar, "Progress Review", Theme.traceActionListener("SupervisorDashboard", "ProgressReview", e -> new ProgressReviewFrame(supervisor).setVisible(true)));
        Theme.addSidebarButton(sidebar, "View Peer Reviews", Theme.traceActionListener("SupervisorDashboard", "PeerReviews", e -> new AggregatedReviewsFrame(supervisor).setVisible(true)));
        
        sidebar.add(Box.createVerticalGlue());

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
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.BG);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + supervisor.getName());
        Theme.styleLabel(welcomeLabel, true);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JLabel capLabel = new JLabel("Capacity: " + supervisor.getCurrentGroups() + "/" + supervisor.getMaxGroups());
        capLabel.setFont(Theme.MONO_FONT);
        capLabel.setForeground(Theme.ACCENT);
        headerPanel.add(capLabel, BorderLayout.EAST);

        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Scrollable Body
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        Theme.applyTheme(bodyPanel);
        bodyPanel.setBackground(Theme.BG);

        // Pending Requests Section
        JLabel reqHeader = new JLabel("Pending Supervision Requests");
        Theme.styleLabel(reqHeader, false);
        reqHeader.setFont(Theme.BOLD_FONT);
        reqHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        bodyPanel.add(Box.createVerticalStrut(40));
        bodyPanel.add(reqHeader);
        bodyPanel.add(Box.createVerticalStrut(15));

        JPanel requestCard = new JPanel(new BorderLayout());
        requestCard.setBackground(Theme.SURFACE);
        requestCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        requestCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        requestCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> requestList = new JList<>(listModel);
        requestList.setBackground(Theme.SURFACE);
        requestList.setForeground(Theme.TEXT);
        requestList.setFont(Theme.MONO_FONT);
        
        Database db = Database.getInstance();
        for (SupervisionRequest r : db.getRequests()) {
            if (r.getSupervisor() != null && r.getSupervisor().getUserId().equals(supervisor.getUserId()) && r.getStatus().equals("Pending")) {
                listModel.addElement(r.getRequestId() + " | Group: " + (r.getGroup() != null ? r.getGroup().getGroupId() : "Unknown"));
            }
        }

        JScrollPane requestScroll = new JScrollPane(requestList);
        Theme.styleScrollPane(requestScroll);
        requestCard.add(requestScroll, BorderLayout.CENTER);

        JPanel requestActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        requestActionPanel.setBackground(Theme.SURFACE);
        JButton acceptBtn = Theme.createPrimaryButton("Accept");
        JButton declineBtn = Theme.createOutlineButton("Decline");
        
        acceptBtn.addActionListener(Theme.traceActionListener("SupervisorDashboard", "AcceptRequest", e -> {
            String selected = requestList.getSelectedValue();
            if (selected == null) return;
            String reqId = selected.split(" \\| ")[0];
            for (SupervisionRequest r : db.getRequests()) {
                if (r.getRequestId().equals(reqId)) {
                    diag.info("UI-Supervisor", "Accepting request: " + reqId);
                    r.accept();
                    db.saveToFile();
                    listModel.removeElement(selected);
                    break;
                }
            }
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
                    listModel.removeElement(selected);
                    break;
                }
            }
        }));

        requestActionPanel.add(declineBtn);
        requestActionPanel.add(acceptBtn);
        requestCard.add(requestActionPanel, BorderLayout.SOUTH);
        bodyPanel.add(requestCard);

        // Quick Actions Section
        JLabel actionHeader = new JLabel("Supervisor Utilities");
        Theme.styleLabel(actionHeader, false);
        actionHeader.setFont(Theme.BOLD_FONT);
        actionHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        bodyPanel.add(Box.createVerticalStrut(40));
        bodyPanel.add(actionHeader);
        bodyPanel.add(Box.createVerticalStrut(15));

        JPanel actionsGrid = new JPanel(new GridLayout(0, 3, 20, 20));
        actionsGrid.setBackground(Theme.BG);
        actionsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        addActionButton(actionsGrid, "Idea Bank", e -> new IdeaBankFrame(supervisor).setVisible(true));
        addActionButton(actionsGrid, "Archive Search", e -> new ArchiveSearchFrame().setVisible(true));
        addActionButton(actionsGrid, "Complaints", e -> new ComplaintReviewFrame(supervisor).setVisible(true));

        bodyPanel.add(actionsGrid);

        JScrollPane bodyScroll = new JScrollPane(bodyPanel);
        Theme.styleScrollPane(bodyScroll);
        contentPanel.add(bodyScroll, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void addActionButton(JPanel panel, String text, java.awt.event.ActionListener listener) {
        JButton btn = Theme.createOutlineButton(text);
        btn.addActionListener(Theme.traceActionListener("SupervisorDashboard", text, listener));
        panel.add(btn);
    }
}
