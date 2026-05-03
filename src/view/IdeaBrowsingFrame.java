package view;

import model.Database;
import model.IdeaBank;
import model.User;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

/**
 * Read-only idea bank for students (browse supervisor-posted ideas).
 */
public class IdeaBrowsingFrame extends JFrame {

    public IdeaBrowsingFrame() {
        setTitle("Idea Bank — Browse");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));
        Theme.applyTheme(root);

        JLabel head = new JLabel("Browse research ideas");
        Theme.styleLabel(head, true);
        root.add(head, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.weightx = 1;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, 8, 0);

        JLabel searchLbl = new JLabel("Search");
        searchLbl.setFont(Theme.BOLD_FONT);
        searchLbl.setForeground(Theme.TEXT_MUTED);
        gc.gridy = 0;
        center.add(searchLbl, gc);

        JTextField searchField = new JTextField();
        Theme.styleTextField(searchField);
        Theme.constrainFormFieldWidth(searchField, Theme.FORM_FIELD_WIDTH);
        gc.gridy = 1;
        gc.insets = new Insets(0, 0, 12, 0);
        center.add(searchField, gc);

        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> list = new JList<>(model);
        Theme.styleList(list);
        list.setVisibleRowCount(10);

        Runnable refresh = () -> {
            model.clear();
            String q = searchField.getText().trim().toLowerCase();
            List<IdeaBank> ideas = IdeaBank.browse(Database.getInstance().getIdeas());
            for (IdeaBank idea : ideas) {
                if (matches(idea, q)) {
                    String sv = resolveSupervisorName(idea.getSupervisorId());
                    model.addElement(String.format("[%s] %s — %s  (%s)",
                            idea.getIdeaId(), idea.getTitle(), idea.getDomain(), sv));
                }
            }
        };
        refresh.run();

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refresh.run(); }
            public void removeUpdate(DocumentEvent e) { refresh.run(); }
            public void changedUpdate(DocumentEvent e) { refresh.run(); }
        });

        JScrollPane scroll = new JScrollPane(list);
        Theme.styleScrollPane(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        gc.gridy = 2;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(0, 0, 0, 0);
        center.add(scroll, gc);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        south.setOpaque(false);
        JButton closeBtn = Theme.createOutlineButton("Close");
        closeBtn.addActionListener(e -> dispose());
        south.add(closeBtn);

        root.add(center, BorderLayout.CENTER);
        root.add(south, BorderLayout.SOUTH);

        add(root);
        pack();
    }

    private static boolean matches(IdeaBank idea, String q) {
        if (q.isEmpty()) return true;
        String t = idea.getTitle() != null ? idea.getTitle().toLowerCase() : "";
        String d = idea.getDomain() != null ? idea.getDomain().toLowerCase() : "";
        String desc = idea.getDescription() != null ? idea.getDescription().toLowerCase() : "";
        return t.contains(q) || d.contains(q) || desc.contains(q);
    }

    private static String resolveSupervisorName(String supervisorId) {
        if (supervisorId == null || supervisorId.isEmpty()) return "Staff";
        for (User u : Database.getInstance().getUsers()) {
            if (u.getUserId().equals(supervisorId)) {
                return u.getName();
            }
        }
        return supervisorId;
    }
}
