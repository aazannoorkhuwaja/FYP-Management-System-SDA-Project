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
        setSize(700, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Browse Supervisor Profiles");
        title.setFont(new Font("Arial", Font.BOLD, 18));
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
        table.setFillsViewportHeight(true);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        panel.add(closeBtn, BorderLayout.SOUTH);

        add(panel);
    }
}
