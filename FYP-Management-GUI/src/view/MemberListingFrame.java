package view;

import controller.MemberListingController;
import model.Database;
import model.MemberListing;
import model.Student;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Member Listing Frame refined with Monolithic Serenity theme.
 */
public class MemberListingFrame extends JFrame {
    private Student student;
    private JTextField skillsField;
    private JTextField requiredSkillsField;
    private JTextArea descriptionArea;
    private DefaultListModel<String> listingListModel;

    public MemberListingFrame() {
        this(null);
    }

    public MemberListingFrame(Student student) {
        this.student = student;
        setTitle("Group Member Listings");
        setSize(700, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        Theme.applyTheme(mainPanel);

        // Header
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);
        
        JLabel head = new JLabel("Collaboration Board");
        Theme.styleLabel(head, true);
        topPanel.add(head, BorderLayout.NORTH);

        JPanel searchBar = new JPanel(new BorderLayout(10, 0));
        searchBar.setOpaque(false);
        skillsField = new JTextField();
        Theme.styleTextField(skillsField);
        searchBar.add(skillsField, BorderLayout.CENTER);
        
        JButton searchBtn = Theme.createOutlineButton("Search Skills");
        searchBtn.addActionListener(e -> searchListings());
        searchBar.add(searchBtn, BorderLayout.EAST);
        topPanel.add(searchBar, BorderLayout.SOUTH);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center: List
        listingListModel = new DefaultListModel<>();
        JList<String> listingList = new JList<>(listingListModel);
        listingList.setBackground(Theme.SURFACE);
        listingList.setForeground(Theme.TEXT);
        listingList.setFont(Theme.MONO_FONT);
        loadListings();
        
        JScrollPane scroll = new JScrollPane(listingList);
        Theme.styleScrollPane(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        mainPanel.add(scroll, BorderLayout.CENTER);

        // Footer: Create Listing (if student)
        if (student != null) {
            JPanel createPanel = new JPanel();
            createPanel.setLayout(new BoxLayout(createPanel, BoxLayout.Y_AXIS));
            createPanel.setOpaque(false);
            createPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Theme.BORDER), "Post a New Listing", 0, 0, Theme.BOLD_FONT, Theme.ACCENT));

            JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));
            form.setOpaque(false);
            
            JLabel l1 = new JLabel("Required Skills:");
            Theme.styleLabel(l1, false);
            form.add(l1);
            requiredSkillsField = new JTextField();
            Theme.styleTextField(requiredSkillsField);
            form.add(requiredSkillsField);

            JLabel l2 = new JLabel("Project Description:");
            Theme.styleLabel(l2, false);
            form.add(l2);
            descriptionArea = new JTextArea(3, 20);
            descriptionArea.setBackground(Theme.SURFACE);
            descriptionArea.setForeground(Theme.TEXT);
            form.add(new JScrollPane(descriptionArea));
            
            createPanel.add(form);
            createPanel.add(Box.createVerticalStrut(15));
            
            JButton postBtn = Theme.createPrimaryButton("Post Listing");
            postBtn.addActionListener(e -> {
                MemberListingController mlc = new MemberListingController();
                String required = requiredSkillsField.getText().trim();
                String desc = descriptionArea.getText().trim();
                String result = mlc.createListing(student.getUserId(), required, desc);
                if ("Success".equals(result)) {
                    JOptionPane.showMessageDialog(this, "Listing posted!");
                    loadListings();
                } else {
                    JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            createPanel.add(postBtn);
            mainPanel.add(createPanel, BorderLayout.SOUTH);
        } else {
            JButton closeBtn = Theme.createOutlineButton("Close");
            closeBtn.addActionListener(e -> this.dispose());
            mainPanel.add(closeBtn, BorderLayout.SOUTH);
        }

        add(mainPanel);
    }

    private void loadListings() {
        listingListModel.clear();
        MemberListingController mlc = new MemberListingController();
        List<MemberListing> listings = mlc.getAllListings();
        if (listings.isEmpty()) {
            listingListModel.addElement("No active member listings.");
        }
        for (MemberListing listing : listings) {
            listingListModel.addElement(listing.getListingId() + " | Needs: " + listing.getRequiredSkills());
        }
    }

    private void searchListings() {
        String keyword = skillsField.getText().trim();
        listingListModel.clear();
        MemberListingController mlc = new MemberListingController();
        for (MemberListing listing : mlc.searchListings(keyword)) {
            listingListModel.addElement(listing.getListingId() + " | Needs: " + listing.getRequiredSkills());
        }
    }
}
