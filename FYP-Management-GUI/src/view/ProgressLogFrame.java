package view;

import controller.ProgressController;
import model.Database;
import model.ProjectGroup;
import model.Student;

import javax.swing.*;
import java.awt.*;

/**
 * Progress Log Frame refined with Monolithic Serenity theme.
 */
public class ProgressLogFrame extends JFrame {
    private Student student;
    private JSpinner weekSpinner;
    private JTextArea contentArea;
    private JTextArea individualContribArea;

    public ProgressLogFrame(Student student) {
        this.student = student;
        setTitle("Weekly Progress Log");
        setSize(500, 450);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        Theme.applyTheme(panel);

        JLabel head = new JLabel("Log Weekly Progress");
        Theme.styleLabel(head, true);
        panel.add(head, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        JLabel l1 = new JLabel("Week Number");
        Theme.styleLabel(l1, false);
        form.add(l1);
        form.add(Box.createVerticalStrut(8));
        
        weekSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        weekSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        form.add(weekSpinner);
        form.add(Box.createVerticalStrut(20));

        JLabel l2 = new JLabel("Progress Content");
        Theme.styleLabel(l2, false);
        form.add(l2);
        form.add(Box.createVerticalStrut(8));
        
        contentArea = new JTextArea(4, 20);
        contentArea.setBackground(Theme.SURFACE);
        contentArea.setForeground(Theme.TEXT);
        contentArea.setCaretColor(Theme.ACCENT);
        JScrollPane s1 = new JScrollPane(contentArea);
        Theme.styleScrollPane(s1);
        s1.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        form.add(s1);
        form.add(Box.createVerticalStrut(20));

        JLabel l3 = new JLabel("Individual Contributions");
        Theme.styleLabel(l3, false);
        form.add(l3);
        form.add(Box.createVerticalStrut(8));
        
        individualContribArea = new JTextArea(4, 20);
        individualContribArea.setBackground(Theme.SURFACE);
        individualContribArea.setForeground(Theme.TEXT);
        individualContribArea.setCaretColor(Theme.ACCENT);
        JScrollPane s2 = new JScrollPane(individualContribArea);
        Theme.styleScrollPane(s2);
        s2.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        form.add(s2);

        panel.add(form, BorderLayout.CENTER);

        JButton submitBtn = Theme.createPrimaryButton("Submit Weekly Log");
        submitBtn.addActionListener(e -> submitLog());
        panel.add(submitBtn, BorderLayout.SOUTH);

        add(panel);
    }

    private void submitLog() {
        int week = (Integer) weekSpinner.getValue();
        String content = contentArea.getText().trim();
        String indivContribs = individualContribArea.getText().trim();

        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter progress content.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ProjectGroup group = student.getGroup();
        if (group == null) {
            JOptionPane.showMessageDialog(this, "You are not in a group.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ProgressController pc = new ProgressController();
        String result = pc.submitLog(group.getGroupId(), week, content, indivContribs);

        if ("Success".equals(result)) {
            JOptionPane.showMessageDialog(this, "Progress log submitted!");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
