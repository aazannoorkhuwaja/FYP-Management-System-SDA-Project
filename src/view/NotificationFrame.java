package view;

import model.Database;
import model.Notification;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * Notification Center — flat white table chrome (no grey header strip), compact viewport height.
 */
public class NotificationFrame extends JFrame {
    private User user;
    private JTable notifTable;
    private DefaultTableModel tableModel;
    private JLabel countLabel;

    public NotificationFrame(User user) {
        this.user = user;
        setTitle("Notification Center");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 16));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));
        Theme.applyTheme(mainPanel);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel head = new JLabel("Notifications");
        head.setFont(Theme.HEADING_FONT);
        head.setForeground(Theme.TEXT);
        headerPanel.add(head, BorderLayout.WEST);

        countLabel = new JLabel();
        countLabel.setFont(Theme.MONO_FONT);
        countLabel.setForeground(Theme.WARNING);
        headerPanel.add(countLabel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        String[] columns = {"Status", "Time", "Type", "Message"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        notifTable = new JTable(tableModel);
        Theme.styleTable(notifTable);

        notifTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                l.setOpaque(true);
                l.setForeground(Theme.COLOR_TEXT_PRIMARY);
                String status = (String) table.getValueAt(row, 0);
                l.setFont("NEW".equals(status) ? Theme.BOLD_FONT : Theme.MAIN_FONT);
                if (isSelected) {
                    l.setBackground(Theme.COLOR_SELECTION_LIGHT);
                    l.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Theme.FAST_BLUE, 1),
                            BorderFactory.createEmptyBorder(0, 0, 0, 0)));
                } else {
                    l.setBackground(Theme.COLOR_BG_INPUT);
                    l.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER));
                }
                return l;
            }
        });

        notifTable.getColumnModel().getColumn(0).setPreferredWidth(72);
        notifTable.getColumnModel().getColumn(0).setMaxWidth(88);
        notifTable.getColumnModel().getColumn(1).setPreferredWidth(160);
        notifTable.getColumnModel().getColumn(1).setMaxWidth(200);
        notifTable.getColumnModel().getColumn(2).setPreferredWidth(140);
        notifTable.getColumnModel().getColumn(2).setMaxWidth(220);

        loadNotifications();
        refreshUnreadLabel();
        syncViewportHeight();

        JScrollPane scroll = new JScrollPane(notifTable);
        Theme.styleScrollPane(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(scroll, BorderLayout.NORTH);

        mainPanel.add(center, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottomPanel.setOpaque(false);

        JButton markReadBtn = Theme.createPrimaryButton("Mark Selected Read");
        markReadBtn.addActionListener(e -> {
            int[] rows = notifTable.getSelectedRows();
            if (rows.length == 0) {
                JOptionPane.showMessageDialog(this, "Select notifications first.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Database db = Database.getInstance();
            List<Notification> userNotifs = db.getNotificationsForRecipient(user.getUserId());
            if (userNotifs.isEmpty()) return;
            for (int row : rows) {
                int idx = userNotifs.size() - 1 - row;
                if (idx >= 0 && idx < userNotifs.size()) {
                    userNotifs.get(idx).markAsRead();
                }
            }
            db.saveToFile();
            loadNotifications();
            refreshUnreadLabel();
            syncViewportHeight();
        });
        bottomPanel.add(markReadBtn);

        JButton markAllBtn = Theme.createOutlineButton("Mark All Read");
        markAllBtn.addActionListener(e -> {
            Database db = Database.getInstance();
            for (Notification n : db.getNotificationsForRecipient(user.getUserId())) {
                n.markAsRead();
            }
            db.saveToFile();
            loadNotifications();
            refreshUnreadLabel();
            syncViewportHeight();
        });
        bottomPanel.add(markAllBtn);

        JButton closeBtn = Theme.createOutlineButton("Close");
        closeBtn.addActionListener(e -> dispose());
        bottomPanel.add(closeBtn);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);
        pack();
        setMinimumSize(new Dimension(640, 420));
    }

    private void refreshUnreadLabel() {
        countLabel.setText(getUnreadCount() + " unread");
    }

    /** Body-only viewport height so empty grey filler rows do not dominate the module shell. */
    private void syncViewportHeight() {
        int rows = notifTable.getRowCount();
        int rowH = notifTable.getRowHeight();
        int body = rows * rowH + 8;
        body = Math.min(Math.max(body, rowH + 8), 320);
        notifTable.setPreferredScrollableViewportSize(new Dimension(680, body));
        notifTable.revalidate();
    }

    private void loadNotifications() {
        tableModel.setRowCount(0);
        Database db = Database.getInstance();
        List<Notification> notifications = db.getNotificationsForRecipient(user.getUserId());

        if (notifications.isEmpty()) {
            tableModel.addRow(new Object[]{"—", "—", "—", "No notifications."});
            return;
        }

        for (int i = notifications.size() - 1; i >= 0; i--) {
            Notification n = notifications.get(i);
            String status = n.isRead() ? "READ" : "NEW";
            tableModel.addRow(new Object[]{
                    status,
                    n.getTimestamp() != null ? n.getTimestamp() : "—",
                    n.getType() != null ? n.getType() : "—",
                    n.getMessage() != null ? n.getMessage() : "—"
            });
        }
    }

    private long getUnreadCount() {
        Database db = Database.getInstance();
        long count = 0;
        for (Notification n : db.getNotificationsForRecipient(user.getUserId())) {
            if (!n.isRead()) count++;
        }
        return count;
    }
}
