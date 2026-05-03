package view;

import model.Database;
import model.Student;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Frame to view and contact students who are not currently in a project group.
 */
public class AvailableStudentsFrame extends JFrame {
    private Student currentStudent;
    private DefaultListModel<String> studentListModel;

    public AvailableStudentsFrame(Student student) {
        this.currentStudent = student;
        setTitle("Browse Available Students");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));
        Theme.applyTheme(panel);

        JLabel head = new JLabel("Potential Teammates");
        Theme.styleLabel(head, true);
        panel.add(head, BorderLayout.NORTH);

        studentListModel = new DefaultListModel<>();
        loadAvailableStudents();
        
        JList<String> studentList = new JList<>(studentListModel);
        Theme.styleList(studentList);
        studentList.setFont(Theme.MONO_FONT);

        JScrollPane scroll = new JScrollPane(studentList);
        Theme.styleScrollPane(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        scroll.setPreferredSize(new Dimension(500, 300));
        panel.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottom.setOpaque(false);
        
        JButton refreshBtn = Theme.createOutlineButton("Refresh");
        refreshBtn.addActionListener(e -> loadAvailableStudents());
        bottom.add(refreshBtn);

        JButton inviteBtn = Theme.createPrimaryButton("Send Group Invite");
        inviteBtn.addActionListener(e -> {
            String selected = studentList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Please select a student.");
                return;
            }
            JOptionPane.showMessageDialog(this, "Invite sent to " + selected.split(" - ")[1]);
        });
        bottom.add(inviteBtn);
        
        panel.add(bottom, BorderLayout.SOUTH);

        add(panel);
        pack();
        setMinimumSize(new Dimension(560, 450));
    }

    private void loadAvailableStudents() {
        studentListModel.clear();
        Database db = Database.getInstance();
        int count = 0;
        for (User u : db.getUsers()) {
            if (u instanceof Student) {
                Student s = (Student) u;
                if (s.getGroup() == null && !s.getUserId().equals(currentStudent.getUserId())) {
                    studentListModel.addElement(s.getUserId() + " - " + s.getName() + " | Skills: " + String.join(", ", s.getSkills()));
                    count++;
                }
            }
        }
        if (count == 0) {
            studentListModel.addElement("No unassigned students found.");
        }
    }
}
