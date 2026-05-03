package view;

import controller.IdeaBankController;
import model.Database;
import model.IdeaBank;
import model.Supervisor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Idea Bank Frame refined with Monolithic Serenity theme.
 */
public class IdeaBankFrame extends JFrame {
    private Supervisor supervisor;
    private JTextField titleField;
    private JTextField domainField;
    private JTextArea descArea;

    public IdeaBankFrame(Supervisor supervisor) {
        this.supervisor = supervisor;
        setTitle("Contribute: Idea Bank");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));
        Theme.applyTheme(panel);

        JLabel head = new JLabel("Post New Research Idea");
        Theme.styleLabel(head, true);
        panel.add(head, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        form.add(createField("Idea Title", titleField = new JTextField()));
        form.add(Box.createVerticalStrut(15));
        form.add(createField("Research Domain", domainField = new JTextField()));
        form.add(Box.createVerticalStrut(15));
        
        JLabel lDesc = new JLabel("Problem Description");
        Theme.styleLabel(lDesc, false);
        form.add(lDesc);
        form.add(Box.createVerticalStrut(8));
        
        descArea = new JTextArea(5, 20);
        Theme.styleTextArea(descArea);
        JScrollPane scroll = new JScrollPane(descArea);
        Theme.styleScrollPane(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        scroll.setPreferredSize(new Dimension(Theme.FORM_FIELD_WIDTH, 120));
        form.add(scroll);

        panel.add(Theme.wrapCentered(form), BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        south.setOpaque(false);
        JButton postBtn = Theme.createPrimaryButton("Publish to Idea Bank");
        postBtn.addActionListener(e -> postIdea());
        south.add(postBtn);
        panel.add(south, BorderLayout.SOUTH);

        add(panel);
        pack();
        setMinimumSize(new Dimension(520, 460));
    }

    private JPanel createField(String labelText, JTextField field) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setOpaque(false);
        JLabel l = new JLabel(labelText);
        Theme.styleLabel(l, false);
        p.add(l, BorderLayout.NORTH);
        Theme.styleTextField(field);
        Theme.constrainFormFieldWidth(field, Theme.FORM_FIELD_WIDTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private void postIdea() {
        String title = titleField.getText().trim();
        String domain = domainField.getText().trim();
        String desc = descArea.getText().trim();

        if (title.isEmpty() || domain.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title and domain are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        IdeaBankController ibc = new IdeaBankController();
        String result = ibc.postIdea(supervisor.getUserId(), title, domain, desc);
        if ("Success".equals(result)) {
            JOptionPane.showMessageDialog(this, "Idea published to global bank!");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
