package view;

import controller.ArchiveController;
import model.Archive;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Archive Search Frame refined with Monolithic Serenity theme.
 */
public class ArchiveSearchFrame extends JFrame {
    private JTextField titleField;
    private JTextField domainField;
    private JTextField studentField;
    private JTextArea resultArea;

    public ArchiveSearchFrame() {
        setTitle("Project Archive Search");
        setSize(600, 500);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        Theme.applyTheme(panel);

        JLabel head = new JLabel("Search Historical Archives");
        Theme.styleLabel(head, true);
        panel.add(head, BorderLayout.NORTH);

        JPanel searchBody = new JPanel();
        searchBody.setLayout(new BoxLayout(searchBody, BoxLayout.Y_AXIS));
        searchBody.setOpaque(false);

        searchBody.add(createSearchField("Title Keyword:", titleField = new JTextField()));
        searchBody.add(Box.createVerticalStrut(10));
        searchBody.add(createSearchField("Domain Keyword:", domainField = new JTextField()));
        searchBody.add(Box.createVerticalStrut(10));
        searchBody.add(createSearchField("Student Name:", studentField = new JTextField()));
        searchBody.add(Box.createVerticalStrut(20));

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setOpaque(false);
        JButton searchBtn = Theme.createPrimaryButton("Search Archives");
        searchBtn.addActionListener(e -> searchArchive());
        JButton clearBtn = Theme.createOutlineButton("Clear All");
        clearBtn.addActionListener(e -> clearFields());
        btnPanel.add(clearBtn);
        btnPanel.add(searchBtn);
        searchBody.add(btnPanel);
        searchBody.add(Box.createVerticalStrut(20));

        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        resultArea.setBackground(Theme.SURFACE);
        resultArea.setForeground(Theme.TEXT);
        resultArea.setFont(Theme.MONO_FONT);
        resultArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scroll = new JScrollPane(resultArea);
        Theme.styleScrollPane(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        searchBody.add(scroll);

        panel.add(searchBody, BorderLayout.CENTER);
        add(panel);
    }

    private JPanel createSearchField(String labelText, JTextField field) {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        JLabel l = new JLabel(labelText);
        l.setPreferredSize(new Dimension(120, 30));
        Theme.styleLabel(l, false);
        p.add(l, BorderLayout.WEST);
        Theme.styleTextField(field);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private void searchArchive() {
        ArchiveController ac = new ArchiveController();
        StringBuilder sb = new StringBuilder();

        boolean found = false;
        if (!titleField.getText().trim().isEmpty()) {
            List<Archive> results = ac.searchByTitle(titleField.getText().trim());
            sb.append("TITLE RESULTS:\n");
            for (Archive a : results) {
                sb.append("• ").append(a.getProjectTitle()).append(" [").append(a.getDomain()).append("]\n");
                found = true;
            }
            sb.append("\n");
        }

        if (!domainField.getText().trim().isEmpty()) {
            List<Archive> results = ac.searchByDomain(domainField.getText().trim());
            sb.append("DOMAIN RESULTS:\n");
            for (Archive a : results) {
                sb.append("• ").append(a.getProjectTitle()).append(" — ").append(a.getStudentNames()).append("\n");
                found = true;
            }
            sb.append("\n");
        }

        if (!studentField.getText().trim().isEmpty()) {
            List<Archive> results = ac.searchByStudent(studentField.getText().trim());
            sb.append("STUDENT RESULTS:\n");
            for (Archive a : results) {
                sb.append("• ").append(a.getProjectTitle()).append(" (").append(a.getYear()).append(")\n");
                found = true;
            }
        }

        if (!found && sb.length() == 0) {
            resultArea.setText("No results found. Try broader keywords.");
        } else {
            resultArea.setText(sb.toString());
        }
    }

    private void clearFields() {
        titleField.setText("");
        domainField.setText("");
        studentField.setText("");
        resultArea.setText("");
    }
}
