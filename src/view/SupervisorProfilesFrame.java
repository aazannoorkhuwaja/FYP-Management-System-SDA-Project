package view;

import model.Database;
import model.Supervisor;
import model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Gap #10 — Dedicated supervisor profile browser for students.
 * Allows students to view detailed information about supervisors, 
 * including response time ratings and research areas.
 */
public class SupervisorProfilesFrame extends JFrame {

    public SupervisorProfilesFrame() {
        setTitle("Supervisor Profiles & Performance");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));
        Theme.applyTheme(panel);

        JLabel title = new JLabel("Browse Supervisor Profiles");
        title.setFont(Theme.SUBHEADING_FONT);
        title.setForeground(Theme.TEXT);
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Research Areas", "Avg Response (h)", "Capacity"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        Database db = Database.getInstance();
        for (User u : db.getUsers()) {
            if (u instanceof Supervisor) {
                Supervisor s = (Supervisor) u;
                Object[] row = {
                    s.getUserId(),
                    s.getName(),
                    String.join(", ", s.getResearchAreas()),
                    String.format("%.1f", s.getAvgResponseTime()),
                    s.getCurrentGroups() + " / " + s.getMaxGroups()
                };
                model.addRow(row);
            }
        }

        JTable table = new JTable(model);
        Theme.styleTable(table);
        JScrollPane scroll = new JScrollPane(table);
        Theme.styleScrollPane(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        scroll.setPreferredSize(new Dimension(720, 280));
        panel.add(scroll, BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        south.setOpaque(false);
        JButton closeBtn = Theme.createOutlineButton("Close");
        closeBtn.addActionListener(e -> dispose());
        south.add(closeBtn);
        panel.add(south, BorderLayout.SOUTH);

        add(panel);
        pack();
        setMinimumSize(new Dimension(640, 380));
    }
}
