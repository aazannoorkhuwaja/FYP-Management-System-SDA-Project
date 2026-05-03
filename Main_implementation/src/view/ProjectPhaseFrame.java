package view;

import controller.PhaseController;
import model.Database;
import model.ProjectGroup;
import model.ProjectPhase;
import model.Student;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Project Phase Frame refined with Monolithic Serenity theme.
 */
public class ProjectPhaseFrame extends JFrame {
    private model.Supervisor supervisor;
    private JComboBox<String> groupCombo;
    private JTextField phaseNameField;
    private JTextField deadlineField;
    private JTextArea descArea;

    public ProjectPhaseFrame(model.Supervisor supervisor) {
        this.supervisor = supervisor;
        setTitle("Lifecycle: Define Milestone");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));
        Theme.applyTheme(panel);

        JLabel head = new JLabel("Schedule New Phase");
        Theme.styleLabel(head, true);
        panel.add(head, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        form.add(createLabel("Assigned Group"));
        groupCombo = new JComboBox<>();
        styleCombo(groupCombo);
        Theme.constrainComboWidth(groupCombo, Theme.FORM_FIELD_WIDTH);
        loadGroups();
        form.add(groupCombo);
        form.add(Box.createVerticalStrut(15));

        form.add(createLabel("Milestone Name"));
        phaseNameField = new JTextField();
        Theme.styleTextField(phaseNameField);
        Theme.constrainFormFieldWidth(phaseNameField, Theme.FORM_FIELD_WIDTH);
        form.add(phaseNameField);
        form.add(Box.createVerticalStrut(15));

        form.add(createLabel("Completion Deadline (YYYY-MM-DD)"));
        deadlineField = new JTextField("2024-06-30");
        Theme.styleTextField(deadlineField);
        Theme.constrainFormFieldWidth(deadlineField, Theme.FORM_FIELD_WIDTH);
        form.add(deadlineField);
        form.add(Box.createVerticalStrut(15));

        form.add(createLabel("Phase Description"));
        descArea = new JTextArea(3, 20);
        Theme.styleTextArea(descArea);
        JScrollPane scroll = new JScrollPane(descArea);
        Theme.styleScrollPane(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        scroll.setPreferredSize(new Dimension(Theme.FORM_FIELD_WIDTH, 88));
        form.add(scroll);

        panel.add(Theme.wrapCentered(form), BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        south.setOpaque(false);
        JButton addBtn = Theme.createPrimaryButton("Initialize Milestone");
        addBtn.addActionListener(e -> addPhase());
        south.add(addBtn);
        panel.add(south, BorderLayout.SOUTH);

        add(panel);
        pack();
        setMinimumSize(new Dimension(520, 460));
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        Theme.styleLabel(l, false);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void styleCombo(JComboBox<String> combo) {
        Theme.styleComboBox(combo);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void loadGroups() {
        Database db = Database.getInstance();
        for (ProjectGroup g : db.getGroups()) {
            if (g.getSupervisor() != null && g.getSupervisor().getUserId().equals(supervisor.getUserId())) {
                groupCombo.addItem(g.getGroupId() + " - " + g.getProjectTitle());
            }
        }
    }

    private void addPhase() {
        String phaseName = phaseNameField.getText().trim();
        String deadlineStr = deadlineField.getText().trim();
        String desc = descArea.getText().trim();

        if (groupCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Select a group first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (phaseName.isEmpty() || deadlineStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phase name and deadline are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date deadline;
        try {
            deadline = new SimpleDateFormat("yyyy-MM-dd").parse(deadlineStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String selected = (String) groupCombo.getSelectedItem();
        String groupId = selected.split(" - ")[0];
        PhaseController pc = new PhaseController();
        String result = pc.addPhase(supervisor, groupId, phaseName, deadline, desc);
        if ("Success".equals(result)) {
            JOptionPane.showMessageDialog(this, "Milestone scheduled successfully!");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
