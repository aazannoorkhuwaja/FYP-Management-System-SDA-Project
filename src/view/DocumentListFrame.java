package view;

import controller.DocumentController;
import model.Database;
import model.Document;
import model.ProjectGroup;
import model.Student;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Document List Frame refined with the shared FAST-NU theme.
 */
public class DocumentListFrame extends JFrame {
    private Student student;
    private JList<String> docList;
    private DefaultListModel<String> docListModel;

    public DocumentListFrame(Student student) {
        this.student = student;
        setTitle("Document Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));
        Theme.applyTheme(panel);

        JLabel head = new JLabel("Project Documents");
        head.setFont(Theme.HEADING_FONT);
        head.setForeground(Theme.TEXT);
        panel.add(head, BorderLayout.NORTH);

        docListModel = new DefaultListModel<>();
        docList = new JList<>(docListModel);
        Theme.styleList(docList);
        docList.setFont(Theme.MONO_FONT);
        
        loadDocuments();
        JScrollPane scroll = new JScrollPane(docList);
        Theme.styleScrollPane(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        scroll.setPreferredSize(new Dimension(640, 280));
        panel.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottom.setOpaque(false);
        
        JButton refreshBtn = Theme.createOutlineButton("Refresh");
        refreshBtn.addActionListener(e -> loadDocuments());
        bottom.add(refreshBtn);

        JButton viewVersionsBtn = Theme.createPrimaryButton("View Version History");
        viewVersionsBtn.addActionListener(e -> viewVersions());
        bottom.add(viewVersionsBtn);
        
        panel.add(bottom, BorderLayout.SOUTH);

        add(panel);
        pack();
        setMinimumSize(new Dimension(560, 400));
    }

    private void loadDocuments() {
        docListModel.clear();
        ProjectGroup group = student.getGroup();
        if (group == null) return;

        DocumentController dc = new DocumentController();
        List<Document> docs = dc.getDocumentsForGroup(group.getGroupId());
        if (docs.isEmpty()) {
            docListModel.addElement("No documents uploaded yet.");
            return;
        }
        for (Document doc : docs) {
            docListModel.addElement(String.format("%-25s | v%-3d | %s", doc.getFileName(), doc.getVersion(), doc.getUploadDate()));
        }
    }

    private void viewVersions() {
        String selected = docList.getSelectedValue();
        if (selected == null || selected.contains("No documents")) {
            JOptionPane.showMessageDialog(this, "Select a valid document first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String fileName = selected.split(" \\| ")[0].trim();
        ProjectGroup group = student.getGroup();
        if (group == null) return;

        DocumentController dc = new DocumentController();
        // FR-13: use getAllDocumentsForGroup to find ALL versions, not just latest
        String documentId = null;
        for (Document d : dc.getAllDocumentsForGroup(group.getGroupId())) {
            if (d.getFileName().equals(fileName)) {
                documentId = d.getDocumentId();
                break;
            }
        }

        if (documentId == null) {
            JOptionPane.showMessageDialog(this, "Document history not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Document> versions = dc.getVersionHistory(documentId);
        StringBuilder history = new StringBuilder("Version History for " + fileName + ":\n\n");
        for (Document d : versions) {
            history.append(String.format("v%d - Uploaded: %s\nPath: %s\n\n", d.getVersion(), d.getUploadDate(), d.getFilePath()));
        }

        JTextArea area = new JTextArea(history.toString());
        Theme.styleTextArea(area);
        area.setFont(Theme.MONO_FONT);
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(450, 300));
        Theme.styleScrollPane(scroll);
        
        JOptionPane.showMessageDialog(this, scroll, "History: " + fileName + " (" + versions.size() + " version(s))", JOptionPane.PLAIN_MESSAGE);
    }
}
