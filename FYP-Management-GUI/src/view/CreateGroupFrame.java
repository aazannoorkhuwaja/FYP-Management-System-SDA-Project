package view;

import controller.GroupController;
import model.Database;
import model.Student;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Frame allowing a Student to create their own group.
 * Max 3 members enforced. Refined with Monolithic Serenity theme.
 */
public class CreateGroupFrame extends JFrame {
    private Student currentStudent;
    private JTextField groupIdField;
    private JTextField projectTitleField;
    private JList<String> memberList;
    private DefaultListModel<String> memberListModel;

    public CreateGroupFrame(Student student) {
        this.currentStudent = student;
        setTitle("Formation: Create Group");
        setSize(550, 500);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        Theme.applyTheme(panel);

        JLabel head = new JLabel("Form Your Project Group");
        Theme.styleLabel(head, true);
        panel.add(head, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);

        JLabel l1 = new JLabel("Unique Group Identifier");
        Theme.styleLabel(l1, false);
        body.add(l1);
        groupIdField = new JTextField();
        Theme.styleTextField(groupIdField);
        body.add(groupIdField);
        body.add(Box.createVerticalStrut(15));

        JLabel l2 = new JLabel("Proposed Project Title");
        Theme.styleLabel(l2, false);
        body.add(l2);
        projectTitleField = new JTextField();
        Theme.styleTextField(projectTitleField);
        body.add(projectTitleField);
        body.add(Box.createVerticalStrut(15));

        JLabel l3 = new JLabel("Invite Team Members (Hold Ctrl/Cmd)");
        Theme.styleLabel(l3, false);
        body.add(l3);
        
        memberListModel = new DefaultListModel<>();
        Database db = Database.getInstance();
        for (User u : db.getUsers()) {
            if (u instanceof Student) {
                Student s = (Student) u;
                if (!s.getUserId().equals(student.getUserId()) && s.getGroup() == null) {
                    memberListModel.addElement(s.getUserId() + " - " + s.getName());
                }
            }
        }
        memberList = new JList<>(memberListModel);
        memberList.setBackground(Theme.SURFACE);
        memberList.setForeground(Theme.TEXT);
        memberList.setSelectionBackground(Theme.SURFACE_2);
        memberList.setFont(Theme.MONO_FONT);
        
        JScrollPane scroll = new JScrollPane(memberList);
        Theme.styleScrollPane(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        body.add(scroll);

        panel.add(body, BorderLayout.CENTER);

        JPanel btnArea = new JPanel(new GridLayout(1, 2, 10, 0));
        btnArea.setOpaque(false);
        
        JButton cancelBtn = Theme.createOutlineButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        
        JButton createBtn = Theme.createPrimaryButton("Initialize Group");
        createBtn.addActionListener(e -> createGroup());
        
        btnArea.add(cancelBtn);
        btnArea.add(createBtn);
        panel.add(btnArea, BorderLayout.SOUTH);

        add(panel);
    }

    private void createGroup() {
        String groupId = groupIdField.getText().trim();
        String projectTitle = projectTitleField.getText().trim();

        if (groupId.isEmpty() || projectTitle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Group ID and Project Title are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> memberIds = new ArrayList<>();
        memberIds.add(currentStudent.getUserId());
        for (String item : memberList.getSelectedValuesList()) {
            memberIds.add(item.split(" - ")[0]);
        }

        if (memberIds.size() > 3) {
            JOptionPane.showMessageDialog(this, "A group can have a maximum of 3 members.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Database db = Database.getInstance();
        if (db.findGroupById(groupId) != null) {
            JOptionPane.showMessageDialog(this, "A group with this ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        model.ProjectGroup group = new model.ProjectGroup(groupId, projectTitle, 0, null);
        for (String id : memberIds) {
            User u = db.findUserById(id);
            if (u instanceof Student) {
                Student s = (Student) u;
                if (s.getGroup() != null) {
                    JOptionPane.showMessageDialog(this, "Student " + s.getName() + " is already in a group.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                group.addMember(s);
                s.setGroup(group);
            }
        }

        db.getGroups().add(group);
        db.saveToFile();
        JOptionPane.showMessageDialog(this, "Group '" + groupId + "' created successfully!");
        dispose();
    }
}
