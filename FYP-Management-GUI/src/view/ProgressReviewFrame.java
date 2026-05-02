package view;

import controller.DocumentController;
import controller.ProgressController;
import model.Database;
import model.Document;
import model.ProjectGroup;
import model.ProgressLog;
import model.Supervisor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Gap #16 — ProgressReviewFrame now has a second tab "Document Feedback"
 * so supervisors can view uploaded documents and provide document-level
 * feedback, satisfying the Supervisor Activity Diagram:
 * "Provide Feedback on Files & Logs".
 */
public class ProgressReviewFrame extends JFrame {
    private Supervisor supervisor;

    // Tab 1: Log Review
    private JComboBox<String> groupCombo;
    private JList<String> logList;
    private DefaultListModel<String> logListModel;
    private JTextArea feedbackArea;
    private ProgressLog selectedLog;

    // Tab 2: Document Feedback
    private JComboBox<String> docGroupCombo;
    private JList<String> docList;
    private DefaultListModel<String> docListModel;
    private JTextArea docFeedbackArea;

    private model.Student studentCaller;

    public ProgressReviewFrame(Supervisor supervisor) {
        this.supervisor = supervisor;
        initUI();
    }

    public ProgressReviewFrame(model.Student student) {
        this.studentCaller = student;
        initUI();
    }

    private void initUI() {
        setTitle(supervisor != null ? "Progress & Document Review (Supervisor)" : "Project Feedback (Student)");
        setSize(700, 560);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Progress Logs", buildLogTab());
        tabs.addTab("Document Feedback", buildDocumentTab());
        
        Theme.applyTheme(tabs);
        add(tabs);
    }

    // ── TAB 1: Progress Log Feedback ──────────────────────────────────────────
    private JPanel buildLogTab() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        Theme.applyTheme(panel);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        JLabel groupLabel = new JLabel("Select Group:");
        groupLabel.setForeground(Theme.TEXT);
        topPanel.add(groupLabel);
        
        groupCombo = new JComboBox<>();
        loadGroups(groupCombo);
        topPanel.add(groupCombo);
        panel.add(topPanel, BorderLayout.NORTH);

        logListModel = new DefaultListModel<>();
        logList = new JList<>(logListModel);
        logList.setBackground(Theme.SURFACE);
        logList.setForeground(Theme.TEXT);
        logList.setFont(Theme.MONO_FONT);
        logList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedLog();
        });

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JScrollPane listScroll = new JScrollPane(logList);
        Theme.styleScrollPane(listScroll);
        split.setTopComponent(listScroll);

        JPanel feedPanel = new JPanel(new BorderLayout(5, 5));
        feedPanel.setOpaque(false);
        JLabel feedLabel = new JLabel("Feedback:");
        feedLabel.setForeground(Theme.TEXT);
        feedPanel.add(feedLabel, BorderLayout.NORTH);
        
        feedbackArea = new JTextArea(6, 40);
        feedbackArea.setBackground(Theme.SURFACE_2);
        feedbackArea.setForeground(Theme.TEXT);
        feedbackArea.setCaretColor(Theme.ACCENT);
        feedbackArea.setEditable(supervisor != null);
        
        JScrollPane feedScroll = new JScrollPane(feedbackArea);
        Theme.styleScrollPane(feedScroll);
        feedPanel.add(feedScroll, BorderLayout.CENTER);
        split.setBottomComponent(feedPanel);
        split.setDividerLocation(200);

        panel.add(split, BorderLayout.CENTER);

        if (supervisor != null) {
            JButton submitBtn = Theme.createPrimaryButton("Submit Feedback");
            submitBtn.addActionListener(e -> submitFeedback());
            panel.add(submitBtn, BorderLayout.SOUTH);
        }

        groupCombo.addActionListener(e -> loadLogsForSelectedGroup());
        if (groupCombo.getItemCount() > 0) loadLogsForSelectedGroup();

        return panel;
    }

    // ── TAB 2: Document Feedback ──────────────────────────────────────────────
    private JPanel buildDocumentTab() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        Theme.applyTheme(panel);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        JLabel groupLabel = new JLabel("Select Group:");
        groupLabel.setForeground(Theme.TEXT);
        topPanel.add(groupLabel);
        
        docGroupCombo = new JComboBox<>();
        loadGroups(docGroupCombo);
        topPanel.add(docGroupCombo);
        panel.add(topPanel, BorderLayout.NORTH);

        docListModel = new DefaultListModel<>();
        docList = new JList<>(docListModel);
        docList.setBackground(Theme.SURFACE);
        docList.setForeground(Theme.TEXT);
        docList.setFont(Theme.MONO_FONT);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JScrollPane listScroll = new JScrollPane(docList);
        Theme.styleScrollPane(listScroll);
        split.setTopComponent(listScroll);

        JPanel feedPanel = new JPanel(new BorderLayout(5, 5));
        feedPanel.setOpaque(false);
        JLabel feedLabel = new JLabel("Document Feedback:");
        feedLabel.setForeground(Theme.TEXT);
        feedPanel.add(feedLabel, BorderLayout.NORTH);
        
        docFeedbackArea = new JTextArea(6, 40);
        docFeedbackArea.setBackground(Theme.SURFACE_2);
        docFeedbackArea.setForeground(Theme.TEXT);
        docFeedbackArea.setEditable(supervisor != null);
        
        JScrollPane feedScroll = new JScrollPane(docFeedbackArea);
        Theme.styleScrollPane(feedScroll);
        feedPanel.add(feedScroll, BorderLayout.CENTER);
        split.setBottomComponent(feedPanel);
        split.setDividerLocation(200);

        panel.add(split, BorderLayout.CENTER);

        if (supervisor != null) {
            JButton submitDocBtn = Theme.createPrimaryButton("Submit Document Feedback");
            submitDocBtn.addActionListener(e -> submitDocumentFeedback());
            panel.add(submitDocBtn, BorderLayout.SOUTH);
        }

        docGroupCombo.addActionListener(e -> loadDocumentsForGroup());
        if (docGroupCombo.getItemCount() > 0) loadDocumentsForGroup();

        return panel;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private void loadGroups(JComboBox<String> combo) {
        Database db = Database.getInstance();
        if (supervisor != null) {
            for (ProjectGroup g : db.getGroups()) {
                if (g.getSupervisor() != null && g.getSupervisor().getUserId().equals(supervisor.getUserId())) {
                    combo.addItem(g.getGroupId() + " - " + g.getProjectTitle());
                }
            }
        } else if (studentCaller != null && studentCaller.getGroup() != null) {
            ProjectGroup g = studentCaller.getGroup();
            combo.addItem(g.getGroupId() + " - " + g.getProjectTitle());
        } else {
            // Admin or unassigned student view (show all for now or empty)
            for (ProjectGroup g : db.getGroups()) {
                combo.addItem(g.getGroupId() + " - " + g.getProjectTitle());
            }
        }
    }

    private void loadLogsForSelectedGroup() {
        logListModel.clear();
        String selected = (String) groupCombo.getSelectedItem();
        if (selected == null) return;
        String groupId = selected.split(" - ")[0];

        ProgressController pc = new ProgressController();
        for (ProgressLog log : pc.getLogsForGroup(groupId)) {
            String content = log.getContent();
            String preview = content.length() > 30 ? content.substring(0, 27) + "..." : content;
            logListModel.addElement(log.getLogId() + " | Week " + log.getWeekNumber() + " | " + preview);
        }
    }

    private void loadDocumentsForGroup() {
        docListModel.clear();
        String selected = (String) docGroupCombo.getSelectedItem();
        if (selected == null) return;
        String groupId = selected.split(" - ")[0];

        DocumentController dc = new DocumentController();
        for (Document doc : dc.getDocumentsForGroup(groupId)) {
            docListModel.addElement(doc.getDocumentId() + " | v" + doc.getVersion() + " | " + doc.getFileName());
        }
    }

    private void loadSelectedLog() {
        String selected = logList.getSelectedValue();
        if (selected == null) return;
        String logId = selected.split(" \\| ")[0];

        for (ProgressLog log : Database.getInstance().getLogs()) {
            if (log.getLogId().equals(logId)) {
                selectedLog = log;
                feedbackArea.setText(log.getSupervisorFeedback() != null ? log.getSupervisorFeedback() : "No feedback yet.");
                break;
            }
        }
    }

    private void submitFeedback() {
        if (selectedLog == null) {
            JOptionPane.showMessageDialog(this, "Select a log first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String feedback = feedbackArea.getText().trim();
        ProgressController pc = new ProgressController();
        String result = pc.provideFeedback(selectedLog.getLogId(), feedback);
        if ("Success".equals(result)) {
            JOptionPane.showMessageDialog(this, "Feedback submitted!");
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void submitDocumentFeedback() {
        String selected = docList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Select a document first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String feedback = docFeedbackArea.getText().trim();
        String docId = selected.split(" \\| ")[0];
        String feedbackId = "DOC-" + docId + "-" + System.currentTimeMillis();
        model.Feedback feedbackObj = new model.Feedback(feedbackId, feedback);
        Database db = Database.getInstance();
        db.getFeedbacks().add(feedbackObj);
        db.saveToFile();

        JOptionPane.showMessageDialog(this, "Document feedback saved!");
        docFeedbackArea.setText("");
    }
}
