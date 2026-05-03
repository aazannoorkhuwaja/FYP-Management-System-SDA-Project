package view;

import controller.ArchiveController;
import model.Database;
import model.ProjectGroup;
import model.Student;
import model.Archive;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Admin-only frame to archive completed projects.
 * Completes FR-19 end-to-end: completed projects are saved in a searchable archive.
 */
public class ArchiveManagementFrame extends JFrame {
    private JTable groupTable;
    private DefaultTableModel groupModel;
    private JTable archiveTable;
    private DefaultTableModel archiveModel;

    public ArchiveManagementFrame() {
        setTitle("Project Archive Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));
        Theme.applyTheme(mainPanel);

        JLabel head = new JLabel("Archive Management");
        head.setFont(Theme.HEADING_FONT);
        head.setForeground(Theme.TEXT);
        mainPanel.add(head, BorderLayout.NORTH);

        // Tabbed Pane
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(Theme.SURFACE);
        tabs.setForeground(Theme.TEXT);
        tabs.setFont(Theme.BOLD_FONT);
        tabs.setOpaque(true);

        // Tab 1: Active Groups (can be archived)
        tabs.addTab("Active Groups", buildGroupPanel());

        // Tab 2: Archived Projects
        tabs.addTab("Archived Projects", buildArchivePanel());

        mainPanel.add(tabs, BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        south.setOpaque(false);
        JButton closeBtn = Theme.createOutlineButton("Close");
        closeBtn.addActionListener(e -> dispose());
        south.add(closeBtn);
        mainPanel.add(south, BorderLayout.SOUTH);

        add(mainPanel);
        pack();
        setMinimumSize(new Dimension(840, 560));
    }

    private JPanel buildGroupPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        Theme.applyTheme(panel);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] columns = {"Group ID", "Project Title", "Supervisor", "Members"};
        groupModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        Database db = Database.getInstance();
        for (ProjectGroup g : db.getGroups()) {
            StringBuilder members = new StringBuilder();
            for (Student s : g.getMembers()) {
                if (members.length() > 0) members.append(", ");
                members.append(s.getName());
            }
            groupModel.addRow(new Object[]{
                g.getGroupId(),
                g.getProjectTitle() != null ? g.getProjectTitle() : "Untitled",
                g.getSupervisor() != null ? g.getSupervisor().getName() : "None",
                members.toString()
            });
        }

        groupTable = new JTable(groupModel);
        Theme.styleTable(groupTable);

        JScrollPane scroll = new JScrollPane(groupTable);
        Theme.styleScrollPane(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        scroll.setPreferredSize(new Dimension(780, 280));
        panel.add(scroll, BorderLayout.CENTER);

        JButton archiveBtn = Theme.createPrimaryButton("Archive Selected Project");
        archiveBtn.addActionListener(e -> archiveSelected());
        panel.add(archiveBtn, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildArchivePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        Theme.applyTheme(panel);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] columns = {"Archive ID", "Title", "Students", "Supervisor", "Year", "Grade"};
        archiveModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        loadArchives();

        archiveTable = new JTable(archiveModel);
        Theme.styleTable(archiveTable);

        JScrollPane scroll = new JScrollPane(archiveTable);
        Theme.styleScrollPane(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        scroll.setPreferredSize(new Dimension(780, 280));
        panel.add(scroll, BorderLayout.CENTER);

        JButton refreshBtn = Theme.createOutlineButton("Refresh");
        refreshBtn.addActionListener(e -> loadArchives());
        panel.add(refreshBtn, BorderLayout.SOUTH);

        return panel;
    }

    private void loadArchives() {
        archiveModel.setRowCount(0);
        ArchiveController ac = new ArchiveController();
        List<Archive> archives = ac.getAllArchives();
        if (archives.isEmpty()) {
            archiveModel.addRow(new Object[]{"—", "No archived projects yet.", "—", "—", "—", "—"});
            return;
        }
        for (Archive a : archives) {
            archiveModel.addRow(new Object[]{
                a.getArchiveId(),
                a.getProjectTitle(),
                a.getStudentNames(),
                a.getSupervisorName(),
                a.getYear(),
                a.getGrade()
            });
        }
    }

    private void archiveSelected() {
        int row = groupTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a group to archive.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String groupId = (String) groupModel.getValueAt(row, 0);
        String title = (String) groupModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Archive project \"" + title + "\" (Group " + groupId + ")?\nThis will add it to the permanent archive.",
            "Confirm Archive", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        ArchiveController ac = new ArchiveController();
        String result = ac.archiveProject(groupId);
        if ("Success".equals(result)) {
            JOptionPane.showMessageDialog(this, "Project archived successfully.");
            loadArchives();
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
