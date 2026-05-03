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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));
        Theme.applyTheme(panel);

        JLabel head = new JLabel("Define Assessment Criteria");
        Theme.styleLabel(head, true);
        panel.add(head, BorderLayout.NORTH);

        criteriaArea = new JTextArea(10, 30);
        Theme.styleTextArea(criteriaArea);
        criteriaArea.setFont(Theme.MONO_FONT);
        
        JScrollPane scroll = new JScrollPane(criteriaArea);
        Theme.styleScrollPane(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        scroll.setPreferredSize(new Dimension(480, 260));
        panel.add(scroll, BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        south.setOpaque(false);
        JButton saveBtn = Theme.createPrimaryButton("Publish Rubric");
        saveBtn.addActionListener(e -> saveRubric());
        south.add(saveBtn);
        panel.add(south, BorderLayout.SOUTH);

        add(panel);
        pack();
        setMinimumSize(new Dimension(520, 420));
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
