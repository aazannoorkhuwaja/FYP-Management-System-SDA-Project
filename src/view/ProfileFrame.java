package view;

import model.Database;
import model.User;
import model.Student;
import model.Supervisor;

import javax.swing.*;
import java.awt.*;

/**
 * Profile Frame refined with Monolithic Serenity theme.
 */
public class ProfileFrame extends JFrame {
    private User user;

    public ProfileFrame(User user) {
        this.user = user;
        setTitle("Identity: Profile");
        setSize(450, 450);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        Theme.applyTheme(panel);

        JLabel head = new JLabel("User Identity Profile");
        Theme.styleLabel(head, true);
        panel.add(head, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 15));
        infoPanel.setOpaque(false);
        
        addInfoRow(infoPanel, "User ID", user.getUserId());
        addInfoRow(infoPanel, "Full Name", user.getName());
        addInfoRow(infoPanel, "Email Address", user.getEmail());
        addInfoRow(infoPanel, "Account Role", user.getClass().getSimpleName());

        if (user instanceof Student) {
            Student s = (Student) user;
            addInfoRow(infoPanel, "Current Skills", String.join(", ", s.getSkills()));
            addInfoRow(infoPanel, "Research Interests", s.getInterests());
            addInfoRow(infoPanel, "Group Status", s.getGroup() != null ? s.getGroup().getGroupId() : "Unassigned");
        } else if (user instanceof Supervisor) {
            Supervisor sv = (Supervisor) user;
            addInfoRow(infoPanel, "Research Domain", String.join(", ", sv.getResearchAreas()));
            addInfoRow(infoPanel, "Active Slots", sv.getCurrentGroups() + " / " + sv.getMaxGroups());
        }

        panel.add(infoPanel, BorderLayout.CENTER);

        JButton closeBtn = Theme.createOutlineButton("Close Profile");
        closeBtn.addActionListener(e -> this.dispose());
        panel.add(closeBtn, BorderLayout.SOUTH);

        add(panel);
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
}
