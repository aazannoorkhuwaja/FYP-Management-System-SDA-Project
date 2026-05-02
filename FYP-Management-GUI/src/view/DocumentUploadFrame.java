package view;

import controller.DocumentController;
import model.Database;
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
        setSize(450, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        fileNameField = new JTextField();
        descriptionField = new JTextField();
        filePathField = new JTextField();

        form.add(new JLabel("File Name:"));
        form.add(fileNameField);
        form.add(new JLabel("Description:"));
        form.add(descriptionField);
        form.add(new JLabel("File Path (simulated):"));
        form.add(filePathField);

        panel.add(form, BorderLayout.CENTER);

        JButton uploadBtn = new JButton("Upload");
        uploadBtn.addActionListener(e -> uploadDoc());
        panel.add(uploadBtn, BorderLayout.SOUTH);

        add(panel);
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
