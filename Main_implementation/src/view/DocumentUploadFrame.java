package view;

import controller.DocumentController;
import model.ProjectGroup;
import model.Student;

import javax.swing.*;
import java.awt.*;

public class DocumentUploadFrame extends JFrame {
    private Student student;
    private JTextField fileNameField;
    private JTextField filePathField;
    private JTextField descriptionField;

    public DocumentUploadFrame(Student student) {
        this.student = student;
        setTitle("Upload Document");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));
        Theme.applyTheme(root);

        JPanel card = Theme.createFormCardShell();
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.weightx = 1;
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;

        JLabel head = new JLabel("Upload Project Document");
        head.setFont(Theme.HEADING_FONT);
        head.setForeground(Theme.TEXT);
        g.gridy = row++;
        g.insets = new Insets(0, 0, 22, 0);
        card.add(head, g);

        g.insets = new Insets(0, 0, 8, 0);
        g.gridy = row++;
        card.add(label("File name (e.g. SRS_v1)"), g);
        fileNameField = new JTextField();
        Theme.styleTextField(fileNameField);
        Theme.constrainFormFieldWidth(fileNameField, Theme.FORM_FIELD_WIDTH);
        g.gridy = row++;
        g.insets = new Insets(0, 0, 14, 0);
        card.add(fileNameField, g);

        g.gridy = row++;
        g.insets = new Insets(0, 0, 8, 0);
        card.add(label("Description"), g);
        descriptionField = new JTextField();
        Theme.styleTextField(descriptionField);
        Theme.constrainFormFieldWidth(descriptionField, Theme.FORM_FIELD_WIDTH);
        g.gridy = row++;
        g.insets = new Insets(0, 0, 14, 0);
        card.add(descriptionField, g);

        g.gridy = row++;
        g.insets = new Insets(0, 0, 8, 0);
        card.add(label("File path"), g);

        filePathField = new JTextField();
        Theme.styleTextField(filePathField);
        Theme.constrainFormFieldWidth(filePathField, Theme.FORM_FIELD_WIDTH);
        filePathField.setEditable(false);
        g.gridy = row++;
        g.insets = new Insets(0, 0, 10, 0);
        card.add(filePathField, g);

        JButton browseBtn = Theme.createOutlineButton("Browse…");
        browseBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                filePathField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        JPanel browseRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        browseRow.setOpaque(false);
        browseRow.add(browseBtn);
        g.gridy = row++;
        g.insets = new Insets(0, 0, 0, 0);
        card.add(browseRow, g);

        root.add(Theme.wrapCentered(card), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);
        JButton cancelBtn = Theme.createOutlineButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        btnPanel.add(cancelBtn);
        JButton uploadBtn = Theme.createPrimaryButton("Upload");
        uploadBtn.addActionListener(e -> uploadDoc());
        btnPanel.add(uploadBtn);
        root.add(btnPanel, BorderLayout.SOUTH);

        add(root);
        pack();
        setMinimumSize(new Dimension(520, 380));
    }

    private JLabel label(String t) {
        JLabel l = new JLabel(t);
        Theme.styleLabel(l, false);
        return l;
    }

    private void uploadDoc() {
        String fileName = fileNameField.getText().trim();
        String description = descriptionField.getText().trim();
        String filePath = filePathField.getText().trim();

        if (fileName.isEmpty() || filePath.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ProjectGroup group = student.getGroup();
        if (group == null) {
            JOptionPane.showMessageDialog(this, "You have no group.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DocumentController dc = new DocumentController();
        String result = dc.uploadDocument(group.getGroupId(), fileName, description, filePath);
        if ("Success".equals(result)) {
            JOptionPane.showMessageDialog(this, "Document uploaded!");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
