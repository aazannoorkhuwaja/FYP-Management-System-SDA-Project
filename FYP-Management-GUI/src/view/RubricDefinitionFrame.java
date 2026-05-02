package view;

import controller.GradeController;
import model.Database;
import model.Supervisor;

import javax.swing.*;
import java.awt.*;

/**
 * Rubric Definition Frame refined with Monolithic Serenity theme.
 */
public class RubricDefinitionFrame extends JFrame {
    private Supervisor supervisor;
    private JTextArea criteriaArea;

    public RubricDefinitionFrame(Supervisor supervisor) {
        this.supervisor = supervisor;
        setTitle("Rubric Formulation");
        setSize(500, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        Theme.applyTheme(panel);

        JLabel head = new JLabel("Define Assessment Criteria");
        Theme.styleLabel(head, true);
        panel.add(head, BorderLayout.NORTH);

        criteriaArea = new JTextArea(10, 30);
        criteriaArea.setBackground(Theme.SURFACE);
        criteriaArea.setForeground(Theme.TEXT);
        criteriaArea.setFont(Theme.MONO_FONT);
        criteriaArea.setLineWrap(true);
        criteriaArea.setWrapStyleWord(true);
        
        JScrollPane scroll = new JScrollPane(criteriaArea);
        Theme.styleScrollPane(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        panel.add(scroll, BorderLayout.CENTER);

        JButton saveBtn = Theme.createPrimaryButton("Publish Rubric");
        saveBtn.addActionListener(e -> saveRubric());
        panel.add(saveBtn, BorderLayout.SOUTH);

        add(panel);
    }

    private void saveRubric() {
        String criteria = criteriaArea.getText().trim();
        if (criteria.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Criteria description is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        GradeController gc = new GradeController();
        String result = gc.defineRubric(supervisor.getUserId(), criteria);
        if ("Success".equals(result)) {
            JOptionPane.showMessageDialog(this, "Grading rubric successfully published!");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
