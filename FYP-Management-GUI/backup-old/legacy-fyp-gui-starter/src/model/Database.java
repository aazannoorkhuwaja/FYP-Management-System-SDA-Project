package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Database implements Serializable {
    private static Database instance;
    private static final String FILE_PATH = "data/database.dat";

    private List<User> users = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();
    private List<Proposal> proposals = new ArrayList<>();
    private List<SupervisionRequest> requests = new ArrayList<>();
    private List<PeerReview> peerReviews = new ArrayList<>();
    private List<Complaint> complaints = new ArrayList<>();
    private List<Notification> notifications = new ArrayList<>();
    private List<IdeaBank> ideas = new ArrayList<>();
    private List<Document> documents = new ArrayList<>();
    private List<ProgressLog> logs = new ArrayList<>();
    private List<Feedback> feedbacks = new ArrayList<>();
    private List<Grade> grades = new ArrayList<>();
    private List<Rubric> rubrics = new ArrayList<>();
    private List<Archive> archives = new ArrayList<>();
    private List<MemberListing> listings = new ArrayList<>();
    private List<ProjectPhase> phases = new ArrayList<>();
    private List<GroupMember> groupMembers = new ArrayList<>();

    private Database() {}

    public static Database getInstance() {
        if (instance == null) {
            instance = loadFromFile();
            if (instance == null) {
                instance = new Database();
            }
        }
        return instance;
    }

    public void saveToFile() {
        try {
            File dir = new File("data");
            if (!dir.exists()) dir.mkdirs();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH));
            oos.writeObject(this);
            oos.close();
        } catch (IOException e) {
            System.err.println("Save error: " + e.getMessage());
        }
    }

    private static Database loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH));
            Database db = (Database) ois.readObject();
            ois.close();
            return db;
        } catch (Exception e) {
            System.err.println("Load error: " + e.getMessage());
            return null;
        }
    }

    // Pre-populate demo data for video recording
    public void seedDemoData() {
        if (!users.isEmpty()) return; // already seeded

        Student s1 = new Student("S101", "Aazan Noor", "aazan@fast.edu", "pass123", "Java, Python", "AI, Web", "G01", 0);
        Student s2 = new Student("S102", "Munesh Kumar", "munesh@fast.edu", "pass123", "C++, React", "Mobile", "G01", 0);
        Student s3 = new Student("S103", "Ahmed Asim", "ahmed@fast.edu", "pass123", "UI/UX, JS", "Game Dev", "G02", 0);

        Supervisor sv1 = new Supervisor("SV01", "Dr. Umer Haroon", "umer@fast.edu", "pass123", "Machine Learning", 2.5f, 2, 5);
        Admin a1 = new Admin("A01", "Admin User", "admin@fast.edu", "admin123", "A01");

        Group g1 = new Group("G01", "AI Chatbot for Healthcare", 2, null);
        g1.addMember("S101");
        g1.addMember("S102");

        users.add(s1);
        users.add(s2);
        users.add(s3);
        users.add(sv1);
        users.add(a1);
        groups.add(g1);
    }

    // Getters for lists
    public List<User> getUsers() { return users; }
    public List<Group> getGroups() { return groups; }
    public List<Proposal> getProposals() { return proposals; }
    public List<SupervisionRequest> getRequests() { return requests; }
    public List<PeerReview> getPeerReviews() { return peerReviews; }
    public List<Complaint> getComplaints() { return complaints; }
    public List<Notification> getNotifications() { return notifications; }
    public List<IdeaBank> getIdeas() { return ideas; }
    public List<Document> getDocuments() { return documents; }
    public List<ProgressLog> getLogs() { return logs; }
    public List<Feedback> getFeedbacks() { return feedbacks; }
    public List<Grade> getGrades() { return grades; }
    public List<Rubric> getRubrics() { return rubrics; }
    public List<Archive> getArchives() { return archives; }
    public List<MemberListing> getListings() { return listings; }
    public List<ProjectPhase> getPhases() { return phases; }
    public List<GroupMember> getGroupMembers() { return groupMembers; }

    // Helper: find user by email
    public User findUserByEmail(String email) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) return u;
        }
        return null;
    }

    // Helper: find group by ID
    public Group findGroupById(String groupId) {
        for (Group g : groups) {
            if (g.getGroupId().equals(groupId)) return g;
        }
        return null;
    }

    // Helper: find proposals by group
    public List<Proposal> findProposalsByGroup(String groupId) {
        List<Proposal> result = new ArrayList<>();
        for (Proposal p : proposals) {
            if (p.getGroupId().equals(groupId)) result.add(p);
        }
        return result;
    }

    // Helper: find reviews for a group (private to supervisor/admin)
    public List<PeerReview> findReviewsForGroup(String groupId) {
        List<PeerReview> result = new ArrayList<>();
        Group g = findGroupById(groupId);
        if (g == null) return result;
        for (String memberId : g.getMemberIds()) {
            for (PeerReview r : peerReviews) {
                if (r.getRevieweeId().equals(memberId)) result.add(r);
            }
        }
        return result;
    }
}
