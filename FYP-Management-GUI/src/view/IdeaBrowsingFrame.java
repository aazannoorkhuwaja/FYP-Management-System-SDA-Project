package view;

import controller.IdeaBankController;
import model.IdeaBank;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Idea Browsing Frame refined with Monolithic Serenity theme.
 */
public class IdeaBrowsingFrame extends JFrame {
    private JTextField searchField;
    private DefaultListModel<String> ideaListModel;
    private JList<String> ideaList;

    public IdeaBrowsingFrame() {
        setTitle("Browse Idea Bank");
        setSize(600, 500);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        Theme.applyTheme(panel);

        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBackground(Theme.BG);
        
        JLabel searchLabel = new JLabel("Search Ideas:");
        searchLabel.setFont(Theme.BOLD_FONT);
        searchLabel.setForeground(Theme.TEXT);
        topPanel.add(searchLabel, BorderLayout.NORTH);

        searchField = new JTextField();
        Theme.styleTextField(searchField);
        topPanel.add(searchField, BorderLayout.CENTER);

        JButton searchBtn = Theme.createOutlineButton("Search");
        searchBtn.addActionListener(e -> searchIdeas());
        topPanel.add(searchBtn, BorderLayout.EAST);
        
        panel.add(topPanel, BorderLayout.NORTH);

        ideaListModel = new DefaultListModel<>();
        ideaList = new JList<>(ideaListModel);
        ideaList.setBackground(Theme.SURFACE);
        ideaList.setForeground(Theme.TEXT);
        ideaList.setSelectionBackground(Theme.SURFACE_2);
        ideaList.setFont(Theme.MAIN_FONT);
        
        loadAllIdeas();
        JScrollPane scrollPane = new JScrollPane(ideaList);
        Theme.styleScrollPane(scrollPane);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton viewDetailsBtn = Theme.createPrimaryButton("View Details");
        viewDetailsBtn.addActionListener(e -> viewDetails());
        panel.add(viewDetailsBtn, BorderLayout.SOUTH);

        add(panel);
    }

    private void loadAllIdeas() {
        ideaListModel.clear();
        IdeaBankController ibc = new IdeaBankController();
        List<IdeaBank> ideas = ibc.getIdeasBySupervisor("");
        for (IdeaBank idea : ideas) {
            ideaListModel.addElement(idea.getTitle() + " - " + idea.getDomain());
        }
    }

    private void searchIdeas() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadAllIdeas();
            return;
        }

        IdeaBankController ibc = new IdeaBankController();
        List<IdeaBank> results = ibc.searchIdeas(keyword);
        ideaListModel.clear();
        for (IdeaBank idea : results) {
            ideaListModel.addElement(idea.getTitle() + " - " + idea.getDomain());
        }
    }

    private void viewDetails() {
        String selected = ideaList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Select an idea first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        IdeaBankController ibc = new IdeaBankController();
        List<IdeaBank> ideas = ibc.searchIdeas("");
        for (IdeaBank idea : ideas) {
            if ((idea.getTitle() + " - " + idea.getDomain()).equals(selected)) {
                JOptionPane.showMessageDialog(this,
                    "Title: " + idea.getTitle() + "\nDomain: " + idea.getDomain() + "\nDescription: " + idea.getDescription(),
                    "Idea Details", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }
    }
}
