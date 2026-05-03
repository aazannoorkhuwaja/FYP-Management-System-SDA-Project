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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));
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
        Theme.styleSpinner(weekSpinner);
        Theme.constrainSpinnerWidth(weekSpinner, 120);
        form.add(weekSpinner);
        form.add(Box.createVerticalStrut(20));

        JLabel l2 = new JLabel("Progress Content");
        Theme.styleLabel(l2, false);
        form.add(l2);
        form.add(Box.createVerticalStrut(8));
        
        contentArea = new JTextArea(4, 20);
        Theme.styleTextArea(contentArea);
        JScrollPane s1 = new JScrollPane(contentArea);
        Theme.styleScrollPane(s1);
        s1.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        s1.setPreferredSize(new Dimension(440, 100));
        form.add(s1);
        form.add(Box.createVerticalStrut(20));

        JLabel l3 = new JLabel("Individual Contributions");
        Theme.styleLabel(l3, false);
        form.add(l3);
        form.add(Box.createVerticalStrut(8));
        
        individualContribArea = new JTextArea(4, 20);
        Theme.styleTextArea(individualContribArea);
        JScrollPane s2 = new JScrollPane(individualContribArea);
        Theme.styleScrollPane(s2);
        s2.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        s2.setPreferredSize(new Dimension(440, 100));
        form.add(s2);

        panel.add(Theme.wrapCentered(form), BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        south.setOpaque(false);
        JButton submitBtn = Theme.createPrimaryButton("Submit Weekly Log");
        submitBtn.addActionListener(e -> submitLog());
        south.add(submitBtn);
        panel.add(south, BorderLayout.SOUTH);

        add(panel);
        pack();
        setMinimumSize(new Dimension(520, 480));
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
