package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Database instance;
    private static final String FILE_PATH = "data/database.dat";

    private List<User> users = new ArrayList<>();
    private List<ProjectGroup> groups = new ArrayList<>();
    private List<FYPProposal> proposals = new ArrayList<>();
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

            File dbFile = new File(FILE_PATH);
            File bakFile = new File(FILE_PATH + ".bak");
            File tmpFile = new File(FILE_PATH + ".tmp");

            // Write to temporary file
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tmpFile));
            oos.writeObject(this);
            oos.close();

            // Backup existing database if it exists
            if (dbFile.exists()) {
                if (bakFile.exists()) bakFile.delete();
                java.nio.file.Files.copy(dbFile.toPath(), bakFile.toPath());
            }

            // Replace database file with temporary file
            if (dbFile.exists()) dbFile.delete();
            tmpFile.renameTo(dbFile);

        } catch (IOException e) {
            System.err.println("Save error: " + e.getMessage());
        }
    }

    private static Database loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH)) {
            @Override
            protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                String name = desc.getName();
                // [SECURITY] Whitelist of allowed classes for deserialization
                List<String> whitelist = Arrays.asList(
                    "model.Database", "model.User", "model.Student", "model.Supervisor", "model.Admin",
                    "model.ProjectGroup", "model.FYPProposal", "model.SupervisionRequest", "model.PeerReview",
                    "model.Complaint", "model.Notification", "model.IdeaBank", "model.Document",
                    "model.ProgressLog", "model.Feedback", "model.Grade", "model.Rubric", "model.Archive",
                    "model.MemberListing", "model.ProjectPhase", "model.GroupMember",
                    "java.util.ArrayList", "java.util.Arrays$ArrayList", "java.util.HashMap", "java.util.Date", 
                    "java.lang.Integer", "java.lang.String", "java.lang.Float", "java.lang.Double", "java.lang.Boolean",
                    "[Ljava.lang.String;"
                );
                if (!whitelist.contains(name) && !name.startsWith("java.lang.")) {
                    throw new InvalidClassException("Unauthorized deserialization attempt", name);
                }
                return super.resolveClass(desc);
            }
        }) {
            Database db = (Database) ois.readObject();
            return db;
        } catch (Exception e) {
            System.err.println("Load error: " + e.getMessage());
            return null;
        }
    }

    // Pre-populate demo data for video recording
    public void seedDemoData() {
        if (!users.isEmpty()) return; // already seeded

        // [SECURITY] Passwords are now hashed via User constructor. 
        // In production, these should be loaded from a secure vault or env.
        String defaultPass = "pass123"; 
        String adminPass = "admin123";
        Supervisor sv1 = new Supervisor("SV01", "Dr. Umer Haroon", "umer@fast.edu", defaultPass, Arrays.asList("Machine Learning"), 2.5f, 2, 5);

        ProjectGroup g1 = new ProjectGroup("G01", "AI Chatbot for Healthcare", 0, sv1);
        ProjectGroup g2 = new ProjectGroup("G02", "Game Development Platform", 0, sv1);

        Student s1 = new Student("S101", "Aazan Noor", "aazan@fast.edu", defaultPass, Arrays.asList("Java", "Python"), "AI, Web", g1, 0);
        Student s2 = new Student("S102", "Munesh Kumar", "munesh@fast.edu", defaultPass, Arrays.asList("C++", "React"), "Mobile", g1, 0);
        Student s3 = new Student("S103", "Ahmed Asim", "ahmed@fast.edu", defaultPass, Arrays.asList("UI/UX", "JS"), "Game Dev", g2, 0);

        g1.addMember(s1);
        g1.addMember(s2);
        g2.addMember(s3);

        Admin a1 = new Admin("A01", "Admin User", "admin@fast.edu", adminPass, "A01");

        users.add(s1);
        users.add(s2);
        users.add(s3);
        users.add(sv1);
        users.add(a1);
        groups.add(g1);
        groups.add(g2);
    }

    // Getters for lists
    public List<User> getUsers() { return users; }
    public List<ProjectGroup> getGroups() { return groups; }
    public List<FYPProposal> getProposals() { return proposals; }
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
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    // Helper: find user by ID
    public User findUserById(String userId) {
        for (User u : users) {
            if (u.getUserId().equals(userId)) return u;
        }
        return null;
    }

    // Helper: find group by ID
    public ProjectGroup findGroupById(String groupId) {
        for (ProjectGroup g : groups) {
            if (g.getGroupId().equals(groupId)) return g;
        }
        return null;
    }

    // Helper: find proposals by group
    public List<FYPProposal> findProposalsByGroup(String groupId) {
        List<FYPProposal> result = new ArrayList<>();
        for (FYPProposal p : proposals) {
            if (p.getGroup() != null && p.getGroup().getGroupId().equals(groupId)) result.add(p);
        }
        return result;
    }

    // Helper: find reviews for a group (private to supervisor/admin)
    public List<PeerReview> findReviewsForGroup(String groupId) {
        List<PeerReview> result = new ArrayList<>();
        ProjectGroup g = findGroupById(groupId);
        if (g == null) return result;
        for (Student member : g.getMembers()) {
            for (PeerReview r : peerReviews) {
                if (r.getReviewee() != null && r.getReviewee().getUserId().equals(member.getUserId())) {
                    result.add(r);
                }
            }
        }
        return result;
    }

    // ARCHIVE SEARCH & RETRIEVAL
    public List<Archive> searchArchiveByTitle(String titleKeyword) {
        List<Archive> result = new ArrayList<>();
        for (Archive a : archives) {
            if (a.getProjectTitle() != null && 
                a.getProjectTitle().toLowerCase().contains(titleKeyword.toLowerCase())) {
                result.add(a);
            }
        }
        return result;
    }

    public List<Archive> searchArchiveByDomain(String domainKeyword) {
        List<Archive> result = new ArrayList<>();
        for (Archive a : archives) {
            if (a.getDomain() != null && 
                a.getDomain().toLowerCase().contains(domainKeyword.toLowerCase())) {
                result.add(a);
            }
        }
        return result;
    }

    public List<Archive> searchArchiveByStudent(String studentName) {
        List<Archive> result = new ArrayList<>();
        for (Archive a : archives) {
            if (a.getStudentNames() != null && 
                a.getStudentNames().toLowerCase().contains(studentName.toLowerCase())) {
                result.add(a);
            }
        }
        return result;
    }

    public List<Archive> searchArchiveByYear(int year) {
        List<Archive> result = new ArrayList<>();
        for (Archive a : archives) {
            if (a.getYear() == year) {
                result.add(a);
            }
        }
        return result;
    }

    // PROGRESS LOG MANAGEMENT
    public List<ProgressLog> getLogsForGroup(String groupId) {
        List<ProgressLog> result = new ArrayList<>();
        for (ProgressLog log : logs) {
            if (log.getGroup() != null && log.getGroup().getGroupId().equals(groupId)) {
                result.add(log);
            }
        }
        return result;
    }

    public ProgressLog getLatestLogForGroup(String groupId) {
        List<ProgressLog> groupLogs = getLogsForGroup(groupId);
        if (groupLogs.isEmpty()) return null;
        
        ProgressLog latest = groupLogs.get(0);
        for (ProgressLog log : groupLogs) {
            if (log.getSubmittedDate() != null && 
                (latest.getSubmittedDate() == null || 
                 log.getSubmittedDate().after(latest.getSubmittedDate()))) {
                latest = log;
            }
        }
        return latest;
    }

    // DOCUMENT MANAGEMENT
    public List<Document> getDocumentsForGroup(String groupId) {
        List<Document> result = new ArrayList<>();
        for (Document doc : documents) {
            if (doc.getGroupId() != null && doc.getGroupId().equals(groupId)) {
                result.add(doc);
            }
        }
        return result;
    }

    public List<Document> getDocumentVersions(String documentId) {
        List<Document> versions = new ArrayList<>();
        for (Document doc : documents) {
            if (doc.getDocumentId() != null && doc.getDocumentId().equals(documentId)) {
                versions.add(doc);
            }
        }
        return versions;
    }

    // GRADE MANAGEMENT
    public Grade getGradeForStudent(String studentId) {
        for (Grade g : grades) {
            if (g.getStudentId() != null && g.getStudentId().equals(studentId)) {
                return g;
            }
        }
        return null;
    }

    public List<Grade> getGradesForGroup(String groupId) {
        List<Grade> result = new ArrayList<>();
        ProjectGroup group = findGroupById(groupId);
        if (group == null) return result;
        
        for (Student member : group.getMembers()) {
            Grade g = getGradeForStudent(member.getUserId());
            if (g != null) result.add(g);
        }
        return result;
    }

    // FEEDBACK MANAGEMENT
    public List<Feedback> getFeedbackForLog(String logId) {
        List<Feedback> result = new ArrayList<>();
        for (Feedback f : feedbacks) {
            if (f.getFeedbackId() != null && f.getFeedbackId().startsWith("LOG-" + logId)) {
                result.add(f);
            }
        }
        return result;
    }

    // IDEA BANK
    public List<IdeaBank> getIdeasBySupervisor(String supervisorId) {
        List<IdeaBank> result = new ArrayList<>();
        for (IdeaBank idea : ideas) {
            if (idea.getSupervisorId() != null && idea.getSupervisorId().equals(supervisorId)) {
                result.add(idea);
            }
        }
        return result;
    }

    public List<IdeaBank> searchIdeasByKeyword(String keyword) {
        List<IdeaBank> result = new ArrayList<>();
        for (IdeaBank idea : ideas) {
            if ((idea.getTitle() != null && idea.getTitle().toLowerCase().contains(keyword.toLowerCase())) ||
                (idea.getDescription() != null && idea.getDescription().toLowerCase().contains(keyword.toLowerCase()))) {
                result.add(idea);
            }
        }
        return result;
    }

    // PROJECT PHASES
    public List<ProjectPhase> getPhasesForGroup(String groupId) {
        List<ProjectPhase> result = new ArrayList<>();
        for (ProjectPhase phase : phases) {
            if (phase.getGroupId() != null && phase.getGroupId().equals(groupId)) {
                result.add(phase);
            }
        }
        return result;
    }

    // RUBRIC
    public Rubric getRubricBySupervisor(String supervisorId) {
        for (Rubric r : rubrics) {
            if (r.getSupervisorId() != null && r.getSupervisorId().equals(supervisorId)) {
                return r;
            }
        }
        return null;
    }

    // COMPLAINTS
    public List<Complaint> getComplaintsByStudent(String studentId) {
        List<Complaint> result = new ArrayList<>();
        for (Complaint c : complaints) {
            if (c.getStudentId() != null && c.getStudentId().equals(studentId)) {
                result.add(c);
            }
        }
        return result;
    }

    public List<Complaint> getPendingComplaints() {
        List<Complaint> result = new ArrayList<>();
        for (Complaint c : complaints) {
            if (!c.isResolved()) {
                result.add(c);
            }
        }
        return result;
    }

    // NOTIFICATION & ALERTS
    public void checkInactiveGroups(int thresholdDays) {
        for (ProjectGroup group : groups) {
            String groupId = group.getGroupId();
            ProgressLog latest = getLatestLogForGroup(groupId);
            
            long daysPassed = 0;
            if (latest == null || latest.getSubmittedDate() == null) {
                daysPassed = thresholdDays + 1; // assume overdue if no logs submitted yet
            } else {
                long diffMs = System.currentTimeMillis() - latest.getSubmittedDate().getTime();
                daysPassed = diffMs / (1000 * 60 * 60 * 24);
            }
            
            if (daysPassed > thresholdDays) { // More than threshold days without progress log
                String notifId = "ALERT-" + groupId + "-" + daysPassed;
                if (notificationExists(notifId)) continue;
                Notification alert = new Notification(
                    notifId,
                    group.getGroupId(),
                    "Progress Overdue",
                    "Group " + groupId + " has not submitted a progress log for " + daysPassed + " days."
                );
                notifications.add(alert);
                if (group.getSupervisor() != null) {
                    Notification supervisorAlert = new Notification(
                        notifId + "-SUP",
                        group.getSupervisor().getUserId(),
                        "Progress Overdue",
                        "Your group " + groupId + " has not submitted a progress log for " + daysPassed + " days."
                    );
                    notifications.add(supervisorAlert);
                }
            }
        }
    }

    public void checkAndAlertPendingRequests(int thresholdDays) {
        for (SupervisionRequest r : requests) {
            if ("Pending".equals(r.getStatus())) {
                long diffMs = System.currentTimeMillis() - r.getRequestDate();
                long daysPassed = diffMs / (1000 * 60 * 60 * 24);
                
                if (daysPassed > thresholdDays) {
                    String notifId = "REQ-ALERT-" + r.getRequestId();
                    if (!notificationExists(notifId)) {
                        Notification alert = new Notification(
                            notifId,
                            "A01", // Assuming A01 is the Admin/Coordinator
                            "Pending Request Alert",
                            "Request " + r.getRequestId() + " from Group " + r.getGroup().getGroupId() + 
                            " has been pending for " + daysPassed + " days. Manual assignment may be needed."
                        );
                        notifications.add(alert);
                    }
                }
            }
        }
    }

    public boolean notificationExists(String notifId) {
        for (Notification n : notifications) {
            if (n.getNotifId() != null && n.getNotifId().equals(notifId)) {
                return true;
            }
        }
        return false;
    }

    public List<Notification> getNotificationsForRecipient(String recipientId) {
        List<Notification> result = new ArrayList<>();
        for (Notification n : notifications) {
            if (n.getRecipientId() != null && n.getRecipientId().equals(recipientId)) {
                result.add(n);
            }
        }
        return result;
    }

    public Map<String, Float> getPeerReviewAveragesForGroup(String groupId) {
        Map<String, List<Float>> scores = new HashMap<>();
        ProjectGroup group = findGroupById(groupId);
        if (group == null) return new HashMap<>();

        for (PeerReview review : peerReviews) {
            if (review.getReviewee() != null && group.getMembers().contains(review.getReviewee())) {
                String revieweeId = review.getReviewee().getUserId();
                scores.computeIfAbsent(revieweeId, id -> new ArrayList<>()).add(review.getAggregated());
            }
        }

        Map<String, Float> averageMap = new HashMap<>();
        for (Map.Entry<String, List<Float>> entry : scores.entrySet()) {
            float total = 0f;
            for (Float value : entry.getValue()) total += value;
            averageMap.put(entry.getKey(), total / entry.getValue().size());
        }
        return averageMap;
    }

    // DUPLICATE PROPOSAL CHECK
    public boolean isDuplicateProposal(String title, String excludeProposalId) {
        for (FYPProposal p : proposals) {
            if (p.getProposalId() != null && !p.getProposalId().equals(excludeProposalId)) {
                if (p.getTitle() != null && 
                    (p.getTitle().equalsIgnoreCase(title) || 
                     isSimilarTitle(p.getTitle(), title))) {
                    return true;
                }
            }
        }

        for (Archive archive : archives) {
            if (archive.getProjectTitle() != null && 
                (archive.getProjectTitle().equalsIgnoreCase(title) || 
                 isSimilarTitle(archive.getProjectTitle(), title))) {
                return true;
            }
        }

        return false;
    }

    private boolean isSimilarTitle(String title1, String title2) {
        // Simple similarity check - can be enhanced with Levenshtein distance
        String t1 = title1.toLowerCase().trim();
        String t2 = title2.toLowerCase().trim();
        
        // Check if one contains the other (80% similarity)
        if (t1.contains(t2) || t2.contains(t1)) return true;
        
        // Check word overlap
        String[] words1 = t1.split("\\s+");
        String[] words2 = t2.split("\\s+");
        int matchCount = 0;
        
        for (String w1 : words1) {
            for (String w2 : words2) {
                if (w1.equalsIgnoreCase(w2)) matchCount++;
            }
        }
        
        int maxWords = Math.max(words1.length, words2.length);
        return (double) matchCount / maxWords > 0.6; // 60% word match
    }
}
