package view;

import model.Database;
import model.ProjectGroup;
import model.Student;
import model.Supervisor;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Group Management Frame refined with Monolithic Serenity theme.
 */
public class GroupManagementFrame extends JFrame {
    private JTabbedPane tabs;

    // Create Group tab
    private JTextField groupIdField;
    private JTextField projectTitleField;
    private JComboBox<String> supervisorCombo;
    private JList<String> studentList;
    private DefaultListModel<String> studentListModel;

    // Assign Supervisor tab
    private JComboBox<String> existingGroupCombo;
    private JComboBox<String> newSupervisorCombo;

    public GroupManagementFrame() {
        setTitle("Structural: Group Governance");
        setSize(650, 600);
        setLocationRelativeTo(null);

        Theme.applyTheme((JComponent) getContentPane());
        
        tabs = new JTabbedPane();
        tabs.setBackground(Theme.BG);
        tabs.setForeground(Theme.TEXT_MUTED);
        tabs.setFont(Theme.BOLD_FONT);
        
        tabs.addTab("Create New Group", buildCreatePanel());
        tabs.addTab("Supervisor Allocation", buildAssignSupervisorPanel());

        add(tabs);
    }

    private JPanel buildCreatePanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        Theme.applyTheme(panel);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        form.add(createLabel("Internal Group ID"));
        groupIdField = new JTextField();
        Theme.styleTextField(groupIdField);
        form.add(groupIdField);
        form.add(Box.createVerticalStrut(15));

        form.add(createLabel("Approved Project Title"));
        projectTitleField = new JTextField();
        Theme.styleTextField(projectTitleField);
        form.add(projectTitleField);
        form.add(Box.createVerticalStrut(15));

        form.add(createLabel("Designated Supervisor"));
        supervisorCombo = new JComboBox<>();
        styleCombo(supervisorCombo);
        Database db = Database.getInstance();
        for (User u : db.getUsers()) {
            if (u instanceof Supervisor) {
                Supervisor s = (Supervisor) u;
                supervisorCombo.addItem(s.getUserId() + " - " + s.getName() + " (" + s.getCurrentGroups() + "/" + s.getMaxGroups() + ")");
            }
        }
        form.add(supervisorCombo);
        form.add(Box.createVerticalStrut(15));

        form.add(createLabel("Enrolled Members (Select Multiple)"));
        studentListModel = new DefaultListModel<>();
        for (User u : db.getUsers()) {
            if (u instanceof Student) {
                Student s = (Student) u;
                if (s.getGroup() == null) {
                    studentListModel.addElement(s.getUserId() + " - " + s.getName());
                }
            }
        }
        studentList = new JList<>(studentListModel);
        studentList.setBackground(Theme.SURFACE);
        studentList.setForeground(Theme.TEXT);
        studentList.setFont(Theme.MONO_FONT);
        studentList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scroll = new JScrollPane(studentList);
        Theme.styleScrollPane(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        form.add(scroll);

        panel.add(form, BorderLayout.CENTER);

        JButton createBtn = Theme.createPrimaryButton("Initialize Group Architecture");
        createBtn.addActionListener(e -> createGroup());
        panel.add(createBtn, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildAssignSupervisorPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        Theme.applyTheme(panel);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);

        Database db = Database.getInstance();

        body.add(createLabel("Target Group"));
        existingGroupCombo = new JComboBox<>();
        styleCombo(existingGroupCombo);
        for (ProjectGroup g : db.getGroups()) {
            String svName = g.getSupervisor() != null ? g.getSupervisor().getName() : "None";
            existingGroupCombo.addItem(g.getGroupId() + " — " + g.getProjectTitle() + " [" + svName + "]");
        }
        body.add(existingGroupCombo);
        body.add(Box.createVerticalStrut(20));

        body.add(createLabel("Assign New Supervisor"));
        newSupervisorCombo = new JComboBox<>();
        styleCombo(newSupervisorCombo);
        for (User u : db.getUsers()) {
            if (u instanceof Supervisor) {
                Supervisor sv = (Supervisor) u;
                newSupervisorCombo.addItem(sv.getUserId() + " - " + sv.getName() + " (" + sv.getCurrentGroups() + "/" + sv.getMaxGroups() + ")");
            }
        }
        body.add(newSupervisorCombo);
        body.add(Box.createVerticalStrut(25));

        JTextArea infoArea = new JTextArea(5, 30);
        infoArea.setEditable(false);
        infoArea.setBackground(Theme.SURFACE);
        infoArea.setForeground(Theme.TEXT_MUTED);
        infoArea.setFont(Theme.MONO_FONT);
        infoArea.setText("PROTOCOL:\n1. Select unassigned or assigned group.\n2. Select available supervisor.\n3. Assignment will recalculate capacities.");
        JScrollPane infoScroll = new JScrollPane(infoArea);
        Theme.styleScrollPane(infoScroll);
        infoScroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        body.add(infoScroll);

        panel.add(body, BorderLayout.CENTER);

        JButton assignBtn = Theme.createPrimaryButton("Finalize Supervisor Reallocation");
        assignBtn.addActionListener(e -> assignSupervisor());
        panel.add(assignBtn, BorderLayout.SOUTH);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        Theme.styleLabel(l, false);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void styleCombo(JComboBox<String> combo) {
        combo.setBackground(Theme.SURFACE);
        combo.setForeground(Theme.TEXT);
        combo.setFont(Theme.MAIN_FONT);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void createGroup() {
        String groupId = groupIdField.getText().trim();
        String projectTitle = projectTitleField.getText().trim();
        String selectedSupervisor = (String) supervisorCombo.getSelectedItem();

        if (groupId.isEmpty() || projectTitle.isEmpty() || selectedSupervisor == null) {
            JOptionPane.showMessageDialog(this, "All fields are mandatory for group creation.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String supervisorId = selectedSupervisor.split(" - ")[0];
        List<String> memberIds = new ArrayList<>();
        for (String item : studentList.getSelectedValuesList()) {
            memberIds.add(item.split(" - ")[0]);
        }

        if (memberIds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "A group must have at least one member.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        controller.GroupController gc = new controller.GroupController();
        String result = gc.formGroup(groupId, projectTitle, supervisorId, memberIds);

        if ("Success".equals(result)) {
            JOptionPane.showMessageDialog(this, "Group registered successfully!");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, result, "Creation Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void assignSupervisor() {
        String groupSel = (String) existingGroupCombo.getSelectedItem();
        String svSel = (String) newSupervisorCombo.getSelectedItem();

        if (groupSel == null || svSel == null) {
            JOptionPane.showMessageDialog(this, "Incomplete selection for reallocation.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String groupId = groupSel.split(" — ")[0].trim();
        String supervisorId = svSel.split(" - ")[0].trim();

        Database db = Database.getInstance();
        ProjectGroup group = db.findGroupById(groupId);
        Supervisor newSv = null;
        for (User u : db.getUsers()) {
            if (u instanceof Supervisor && u.getUserId().equals(supervisorId)) {
                newSv = (Supervisor) u;
                break;
            }
        }

        if (group == null || newSv == null) {
            JOptionPane.showMessageDialog(this, "Target entity resolution failed.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (newSv.getCurrentGroups() >= newSv.getMaxGroups()) {
            JOptionPane.showMessageDialog(this, "Supervisor capacity exceeded.", "Governance Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (group.getSupervisor() != null) {
            Supervisor oldSv = group.getSupervisor();
            if (oldSv.getCurrentGroups() > 0) {
                oldSv.setCurrentGroups(oldSv.getCurrentGroups() - 1);
            }
        }

        group.setSupervisor(newSv);
        newSv.setCurrentGroups(newSv.getCurrentGroups() + 1);
        db.saveToFile();

        JOptionPane.showMessageDialog(this, "Allocation finalized for: " + newSv.getName());
        dispose();
    }
}
