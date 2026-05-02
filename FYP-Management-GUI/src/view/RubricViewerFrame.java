package view;

import model.Database;
import model.Rubric;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Rubric Viewer Frame refined with Monolithic Serenity theme.
 */
public class RubricViewerFrame extends JFrame {
    public RubricViewerFrame() {
        setTitle("Grading Rubrics");
        setSize(550, 450);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        Theme.applyTheme(panel);

        JLabel head = new JLabel("Institutional Grading Rubrics");
        Theme.styleLabel(head, true);
        panel.add(head, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(Theme.SURFACE);
        textArea.setForeground(Theme.TEXT);
        textArea.setFont(Theme.MONO_FONT);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        StringBuilder sb = new StringBuilder();
        Database db = Database.getInstance();
        List<Rubric> rubrics = db.getRubrics();

        if (rubrics.isEmpty()) {
            sb.append("No institutional rubrics have been defined yet.");
        } else {
            for (Rubric r : rubrics) {
                sb.append("--------------------------------------------------\n");
                sb.append("RUBRIC ID: ").append(r.getRubricId()).append("\n");
                sb.append("--------------------------------------------------\n");
                sb.append(r.getCriteria()).append("\n\n");
            }
        }
        textArea.setText(sb.toString());

        JScrollPane scrollPane = new JScrollPane(textArea);
        Theme.styleScrollPane(scrollPane);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton closeBtn = Theme.createOutlineButton("Close Viewer");
        closeBtn.addActionListener(e -> this.dispose());
        panel.add(closeBtn, BorderLayout.SOUTH);

        add(panel);
    }
}
