package view;

import model.Database;
import model.User;
import model.Student;
import model.Supervisor;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * Profile Frame with edit mode.
 * FR-02: Students add technical skills to their profile. Supervisors add research areas.
 * Now supports editing for both roles.
 */
public class ProfileFrame extends JFrame {
    private User user;

    // Editable fields
    private JTextField skillsField;
    private JTextField interestsField;
    private JTextField researchField;
    private JSpinner maxGroupsSpinner;
    private boolean editMode = false;

    // Display labels (for view mode)
    private JPanel infoPanel;

    public ProfileFrame(User user) {
        this.user = user;
        setTitle("Identity: Profile");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));
        Theme.applyTheme(mainPanel);

        JLabel head = new JLabel("User Identity Profile");
        Theme.styleLabel(head, true);
        mainPanel.add(head, BorderLayout.NORTH);

        // Info panel — will be rebuilt on toggle
        infoPanel = new JPanel(new GridLayout(0, 2, 10, 15));
        infoPanel.setOpaque(false);
        buildViewMode();

        JScrollPane scrollPane = new JScrollPane(infoPanel);
        Theme.styleScrollPane(scrollPane);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(480, 340));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottomPanel.setOpaque(false);

        JButton editBtn = Theme.createPrimaryButton("Edit Profile");
        JButton saveBtn = Theme.createPrimaryButton("Save Changes");
        saveBtn.setVisible(false);

        JButton cancelBtn = Theme.createOutlineButton("Cancel");
        cancelBtn.setVisible(false);

        editBtn.addActionListener(e -> {
            editMode = true;
            buildEditMode();
            editBtn.setVisible(false);
            saveBtn.setVisible(true);
            cancelBtn.setVisible(true);
            revalidate();
            repaint();
        });

        saveBtn.addActionListener(e -> {
            saveChanges();
            editMode = false;
            buildViewMode();
            editBtn.setVisible(true);
            saveBtn.setVisible(false);
            cancelBtn.setVisible(false);
            revalidate();
            repaint();
        });

        cancelBtn.addActionListener(e -> {
            editMode = false;
            buildViewMode();
            editBtn.setVisible(true);
            saveBtn.setVisible(false);
            cancelBtn.setVisible(false);
            revalidate();
            repaint();
        });

        JButton closeBtn = Theme.createOutlineButton("Close Profile");
        closeBtn.addActionListener(e -> this.dispose());

        bottomPanel.add(editBtn);
        bottomPanel.add(saveBtn);
        bottomPanel.add(cancelBtn);
        bottomPanel.add(closeBtn);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);
        pack();
        setMinimumSize(new Dimension(520, 480));
    }

    private void buildViewMode() {
        infoPanel.removeAll();

        addInfoRow("User ID", user.getUserId());
        addInfoRow("Full Name", user.getName());
        addInfoRow("Email Address", user.getEmail());
        addInfoRow("Account Role", user.getClass().getSimpleName());

        if (user instanceof Student) {
            Student s = (Student) user;
            addInfoRow("Current Skills", s.getSkills() != null ? String.join(", ", s.getSkills()) : "None");
            addInfoRow("Research Interests", s.getInterests());
            addInfoRow("Group Status", s.getGroup() != null ? s.getGroup().getGroupId() : "Unassigned");
        } else if (user instanceof Supervisor) {
            Supervisor sv = (Supervisor) user;
            addInfoRow("Research Domain", sv.getResearchAreas() != null ? String.join(", ", sv.getResearchAreas()) : "None");
            addInfoRow("Avg Response Time", String.format("%.1f hours", sv.getAvgResponseTime()));
            addInfoRow("Active Slots", sv.getCurrentGroups() + " / " + sv.getMaxGroups());
        }

        infoPanel.revalidate();
        infoPanel.repaint();
    }

    private void buildEditMode() {
        infoPanel.removeAll();

        // Non-editable fields shown as labels
        addInfoRow("User ID", user.getUserId());
        addInfoRow("Full Name", user.getName());
        addInfoRow("Email Address", user.getEmail());
        addInfoRow("Account Role", user.getClass().getSimpleName());

        if (user instanceof Student) {
            Student s = (Student) user;

            JLabel skillLabel = new JLabel("Current Skills");
            skillLabel.setFont(Theme.BOLD_FONT);
            skillLabel.setForeground(Theme.TEXT_MUTED);
            infoPanel.add(skillLabel);
            skillsField = new JTextField(s.getSkills() != null ? String.join(", ", s.getSkills()) : "");
            Theme.styleTextField(skillsField);
            infoPanel.add(skillsField);

            JLabel intLabel = new JLabel("Research Interests");
            intLabel.setFont(Theme.BOLD_FONT);
            intLabel.setForeground(Theme.TEXT_MUTED);
            infoPanel.add(intLabel);
            interestsField = new JTextField(s.getInterests() != null ? s.getInterests() : "");
            Theme.styleTextField(interestsField);
            infoPanel.add(interestsField);

            addInfoRow("Group Status", s.getGroup() != null ? s.getGroup().getGroupId() : "Unassigned");

        } else if (user instanceof Supervisor) {
            Supervisor sv = (Supervisor) user;

            JLabel resLabel = new JLabel("Research Domain");
            resLabel.setFont(Theme.BOLD_FONT);
            resLabel.setForeground(Theme.TEXT_MUTED);
            infoPanel.add(resLabel);
            researchField = new JTextField(sv.getResearchAreas() != null ? String.join(", ", sv.getResearchAreas()) : "");
            Theme.styleTextField(researchField);
            infoPanel.add(researchField);

            JLabel maxLabel = new JLabel("Max Groups");
            maxLabel.setFont(Theme.BOLD_FONT);
            maxLabel.setForeground(Theme.TEXT_MUTED);
            infoPanel.add(maxLabel);
            maxGroupsSpinner = new JSpinner(new SpinnerNumberModel(sv.getMaxGroups(), 1, 20, 1));
            Theme.styleSpinner(maxGroupsSpinner);
            infoPanel.add(maxGroupsSpinner);

            addInfoRow("Active Slots", sv.getCurrentGroups() + " / " + sv.getMaxGroups());
        }

        infoPanel.revalidate();
        infoPanel.repaint();
    }

    private void saveChanges() {
        Database db = Database.getInstance();

        if (user instanceof Student) {
            Student s = (Student) user;
            if (skillsField != null && !skillsField.getText().trim().isEmpty()) {
                String[] skills = skillsField.getText().trim().split("\\s*,\\s*");
                s.setSkills(Arrays.asList(skills));
            }
            if (interestsField != null) {
                s.setInterests(interestsField.getText().trim());
            }
        } else if (user instanceof Supervisor) {
            Supervisor sv = (Supervisor) user;
            if (researchField != null && !researchField.getText().trim().isEmpty()) {
                String[] areas = researchField.getText().trim().split("\\s*,\\s*");
                sv.setResearchAreas(Arrays.asList(areas));
            }
            if (maxGroupsSpinner != null) {
                sv.setMaxGroups((Integer) maxGroupsSpinner.getValue());
            }
        }

        db.saveToFile();
        JOptionPane.showMessageDialog(this, "Profile updated successfully.");
    }

    private void addInfoRow(JPanel p, String key, String val) {
        JLabel k = new JLabel(key);
        k.setFont(Theme.BOLD_FONT);
        k.setForeground(Theme.TEXT_MUTED);
        p.add(k);

        JLabel v = new JLabel(val != null && !val.isEmpty() ? val : "N/A");
        v.setFont(Theme.MAIN_FONT);
        v.setForeground(Theme.TEXT);
        p.add(v);
    }

    private void addInfoRow(String key, String val) {
        addInfoRow(infoPanel, key, val);
    }
}
