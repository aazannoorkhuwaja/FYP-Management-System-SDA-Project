package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.DiagnosticService;

public class Database implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Database instance;
    private static final String FILE_PATH = "data/database.dat";
    private int maxGroupSize = 3;

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

    private Database() {
    }

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
        long start = DiagnosticService.getInstance().startSpan("Database", "saveToFile");
        try {
            File dir = new File("data");
            if (!dir.exists())
                dir.mkdirs();

            File dbFile = new File(FILE_PATH);
            File bakFile = new File(FILE_PATH + ".bak");
            File tmpFile = new File(FILE_PATH + ".tmp");

            // Write to temporary file
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tmpFile));
            oos.writeObject(this);
            oos.close();

            // Backup existing database if it exists
            if (dbFile.exists()) {
                if (bakFile.exists())
                    bakFile.delete();
                java.nio.file.Files.copy(dbFile.toPath(), bakFile.toPath());
            }

            // Replace database file with temporary file
            if (dbFile.exists())
                dbFile.delete();
            tmpFile.renameTo(dbFile);
            DiagnosticService.getInstance().endSpan("Database", "saveToFile", start);

        } catch (IOException e) {
            DiagnosticService.getInstance().logException("Database", "saveToFile failure", e);
        }
    }

    private static Database loadFromFile() {
        DiagnosticService diag = DiagnosticService.getInstance();
        long start = diag.startSpan("Database", "loadFromFile");
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            diag.warning("Database", "Persistence file not found: " + FILE_PATH);
            return null;
        }
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
                        "java.lang.Integer", "java.lang.String", "java.lang.Float", "java.lang.Double",
                        "java.lang.Boolean",
                        "java.util.CollSer", "java.util.ImmutableCollections$List12", "java.util.ImmutableCollections$ListN",
                        "[Ljava.lang.String;");
                if (!whitelist.contains(name) && !name.startsWith("java.lang.")) {
                    throw new InvalidClassException("Unauthorized deserialization attempt", name);
                }
                return super.resolveClass(desc);
            }
        }) {
            Database db = (Database) ois.readObject();
            diag.endSpan("Database", "loadFromFile", start);
            return db;
        } catch (Exception e) {
            diag.logException("Database", "loadFromFile failure", e);
            return null;
        }
    }

    public void seedDemoData() {
        // Force re-seed if the current data is old, missing key elements, or passwords
        // are not hashed
        boolean hasSV06 = false;
        boolean allHashed = true;
        for (User u : users) {
            if (u.getUserId().equals("SV06"))
                hasSV06 = true;
            if (u.getHashedPassword() == null || u.getHashedPassword().length() != 64)
                allHashed = false;
        }

        User student = findUserByEmail("munesh@fast.edu");
        String expectedStudentHash = User.hashPassword("student123");
        boolean correctStudentPass = student != null && expectedStudentHash.equals(student.getHashedPassword());

        // Aggressive re-seed check: less than 15 users, missing SV06, no phases,
        // unhashed passwords, or missing rich demo data (listings)
        boolean needsReseed = users.size() < 15 || !hasSV06 || phases.isEmpty() || !allHashed || !correctStudentPass
                || listings.isEmpty();
        if (!needsReseed)
            return;

        DiagnosticService diag = DiagnosticService.getInstance();
        long start = diag.startSpan("Database", "seedDemoData (FORCED)");

        // Clear all to ensure consistency
        users.clear();
        groups.clear();
        proposals.clear();
        requests.clear();
        peerReviews.clear();
        complaints.clear();
        notifications.clear();
        ideas.clear();
        documents.clear();
        logs.clear();
        feedbacks.clear();
        grades.clear();
        rubrics.clear();
        archives.clear();
        listings.clear();
        phases.clear();

        String studentPass = "student123";
        String supervisorPass = "supervisor123";
        String adminPass = "admin123";

        // Supervisors
        Supervisor sv1 = new Supervisor("SV01", "Dr. Umer Haroon", "umer@fast.edu", supervisorPass,
                Arrays.asList("Machine Learning", "AI"), 2.5f, 2, 5);
        Supervisor sv2 = new Supervisor("SV02", "Shoiab M. Khan", "shoiab@fast.edu", supervisorPass,
                Arrays.asList("Cybersecurity", "Networks"), 3.0f, 1, 4);
        Supervisor sv3 = new Supervisor("SV03", "Engr. Bilal Jan", "bilal@fast.edu", supervisorPass,
                Arrays.asList("Embedded Systems", "IOT"), 2.8f, 1, 3);
        Supervisor sv4 = new Supervisor("SV04", "Usman Ali Shah", "usman@fast.edu", supervisorPass,
                Arrays.asList("Image Processing", "CV"), 2.7f, 1, 3);
        Supervisor sv5 = new Supervisor("SV05", "Samin Ahmed", "samin@fast.edu", supervisorPass,
                Arrays.asList("Data Science", "Big Data"), 2.9f, 1, 4);
        Supervisor sv6 = new Supervisor("SV06", "Fazl-e-Basit", "fazl@fast.edu", supervisorPass,
                Arrays.asList("Software Engineering", "Agile"), 2.6f, 1, 3);

        // Groups
        ProjectGroup g1 = new ProjectGroup("G01", "AI Chatbot for Healthcare", 0, sv1);
        ProjectGroup g2 = new ProjectGroup("G02", "Blockchain Security", 0, sv2);
        ProjectGroup g3 = new ProjectGroup("G03", "SDN Traffic Analysis", 0, sv3);
        ProjectGroup g4 = new ProjectGroup("G04", "Facial Recognition System", 0, sv4);
        ProjectGroup g5 = new ProjectGroup("G05", "Big Data Analytics", 0, sv5);
        ProjectGroup g6 = new ProjectGroup("G06", "Agile Management Tool", 0, sv6);

        // Students
        Student s1 = new Student("S101", "Aazan Noor Khuwaja", "aazan@fast.edu", studentPass, Arrays.asList("Java", "ML"),
                "AI", g1, 0);
        Student s2 = new Student("S102", "Munesh Kumar", "munesh@fast.edu", studentPass,
                Arrays.asList("Java", "Python"), "AI", g1, 0);
        Student s3 = new Student("S103", "Ahmed Asim", "ahmed@fast.edu", studentPass, Arrays.asList("UI/UX", "JS"),
                "Web", g2, 0);
        Student s4 = new Student("S104", "Osama Tariq", "osama@fast.edu", studentPass, Arrays.asList("Python", "R"),
                "Security", g2, 0);
        Student s5 = new Student("S105", "Awaiz Junaid", "awaiz@fast.edu", studentPass, Arrays.asList("Java", "Kotlin"),
                "Networks", g3, 0);
        Student s6 = new Student("S106", "Hania Shahid", "hania@fast.edu", studentPass, Arrays.asList("Figma", "React"),
                "Web", g3, 0);
        Student s7 = new Student("S107", "Zaid Khurram", "zaid@fast.edu", studentPass, Arrays.asList("C#", "Unity"),
                "Images", g4, 0);
        Student s8 = new Student("S108", "Ayesha Khan", "ayesha@fast.edu", studentPass, Arrays.asList("Python", "SQL"),
                "Data", g5, 0);

        g1.addMember(s1);
        g1.addMember(s2);
        g2.addMember(s3);
        // s4, s5, s6, s7, s8 left unassigned for demo purposes

        Admin a1 = new Admin("A01", "Admin User", "admin@fast.edu", adminPass, "A01");

        // Add to lists
        users.addAll(Arrays.asList(s1, s2, s3, s4, s5, s6, s7, s8, sv1, sv2, sv3, sv4, sv5, sv6, a1));
        groups.addAll(Arrays.asList(g1, g2, g3, g4, g5, g6));

        // Phases for G01 (Munesh's group)
        java.util.Date now = new java.util.Date();
        java.util.Date later = new java.util.Date(now.getTime() + 864000000L); // +10 days
        ProjectPhase ph1 = new ProjectPhase("PH01", "G01", "Inception", now, "Initial project kickoff");
        ph1.setCompleted(true);
        ProjectPhase ph2 = new ProjectPhase("PH02", "G01", "Proposal", later, "Submit FYP-1 Proposal");
        phases.addAll(Arrays.asList(ph1, ph2));

        // Ideas
        ideas.add(new IdeaBank("ID01", "AI Diagnosis", "Health", "Deep learning for medical imaging", "SV01"));
        ideas.add(new IdeaBank("ID02", "Smart Firewall", "Security", "ML based intrusion detection", "SV02"));
        ideas.add(new IdeaBank("ID03", "5G Optimization", "Networks", "Resource allocation in 5G", "SV03"));

        // Proposals
        proposals.add(new FYPProposal("P01", "AI Chatbot", "AI", "Abstract...", "Approved", g1));
        proposals.add(new FYPProposal("P02", "Blockchain Security", "Security", "Abstract...", "Approved", g2));
        proposals.add(new FYPProposal("P03", "SDN Analyzer", "Networks", "Real-time traffic monitor", "Pending", g3));

        // Requests
        requests.add(new SupervisionRequest("R01", g3, sv3));
        requests.add(new SupervisionRequest("R02", g4, sv4));

        // Notifications
        notifications.add(new Notification("N01", "S102", "System", "Welcome to FYP Management Portal!"));
        notifications.add(new Notification("N02", "S102", "Alert", "Proposal 'AI Chatbot' has been Approved."));
        notifications.add(new Notification("N03", "SV01", "New Request", "Group G01 sent a supervision request."));

        // Complaints
        complaints.add(new Complaint("C01", "S101", "Requesting hardware resources for ML training", now));

        // Peer Reviews for G01
        java.util.Map<String, Integer> prMap = new java.util.HashMap<>();
        prMap.put("Teamwork", 5);
        prMap.put("Quality", 4);
        peerReviews.add(new PeerReview("PR01", s1, s2, prMap));

        // Documents
        documents.add(new Document("D01", "G01", "Project_Charter.pdf", "Main project plan", "data/docs/charter.pdf"));
        documents.add(new Document("D02", "G01", "Literature_Review.docx", "Initial research", "data/docs/lit_rev.docx"));

        // Progress Logs
        logs.add(new ProgressLog("L01", g1, 1, "Completed UI design", "Aazan worked on Backend, Munesh on Frontend", now));
        logs.add(new ProgressLog("L02", g1, 2, "Integrated Database", "Both worked on SQL schemas", now));

        // Feedbacks
        feedbacks.add(new Feedback("F01", "L01", "SV01", "Great progress on UI, focus on responsiveness.", now));

        // Member Listings
        listings.add(
                new MemberListing("ML01", "S104", "Looking for React dev for Blockchain project.", "React, Web3"));
        listings.add(new MemberListing("ML02", "S105", "Need a UI/UX designer for our group.", "Figma, Tailwind"));

        diag.endSpan("Database", "seedDemoData (FORCED)", start);
        saveToFile();
    }

    // Getters for lists
    public List<User> getUsers() {
        return users;
    }

    public List<ProjectGroup> getGroups() {
        return groups;
    }

    public List<FYPProposal> getProposals() {
        return proposals;
    }

    public List<SupervisionRequest> getRequests() {
        return requests;
    }

    public List<PeerReview> getPeerReviews() {
        return peerReviews;
    }

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public List<IdeaBank> getIdeas() {
        return ideas;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public List<ProgressLog> getLogs() {
        return logs;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public int getMaxGroupSize() {
        return maxGroupSize;
    }

    public void setMaxGroupSize(int maxGroupSize) {
        this.maxGroupSize = maxGroupSize;
    }

    public List<Rubric> getRubrics() {
        return rubrics;
    }

    public List<Archive> getArchives() {
        return archives;
    }

    public List<MemberListing> getListings() {
        return listings;
    }

    public List<ProjectPhase> getPhases() {
        return phases;
    }

    public List<GroupMember> getGroupMembers() {
        return groupMembers;
    }

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
            if (u.getUserId().equals(userId))
                return u;
        }
        return null;
    }

    // Helper: find group by ID
    public ProjectGroup findGroupById(String groupId) {
        for (ProjectGroup g : groups) {
            if (g.getGroupId().equals(groupId))
                return g;
        }
        return null;
    }

    // Helper: find proposals by group
    public List<FYPProposal> findProposalsByGroup(String groupId) {
        List<FYPProposal> result = new ArrayList<>();
        for (FYPProposal p : proposals) {
            if (p.getGroup() != null && p.getGroup().getGroupId().equals(groupId))
                result.add(p);
        }
        return result;
    }

    // Helper: find reviews for a group (private to supervisor/admin)
    public List<PeerReview> findReviewsForGroup(String groupId) {
        List<PeerReview> result = new ArrayList<>();
        ProjectGroup g = findGroupById(groupId);
        if (g == null)
            return result;
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
        if (groupLogs.isEmpty())
            return null;

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
        if (group == null)
            return result;

        for (Student member : group.getMembers()) {
            Grade g = getGradeForStudent(member.getUserId());
            if (g != null)
                result.add(g);
        }
        return result;
    }

    // FEEDBACK MANAGEMENT
    public List<Feedback> getFeedbackForLog(String logId) {
        List<Feedback> result = new ArrayList<>();
        for (Feedback f : feedbacks) {
            if (f.getLogId() != null && f.getLogId().equals(logId)) {
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
                    (idea.getDescription() != null
                            && idea.getDescription().toLowerCase().contains(keyword.toLowerCase()))) {
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
                if (notificationExists(notifId))
                    continue;
                Notification alert = new Notification(
                        notifId,
                        group.getGroupId(),
                        "Progress Overdue",
                        "Group " + groupId + " has not submitted a progress log for " + daysPassed + " days.");
                notifications.add(alert);
                if (group.getSupervisor() != null) {
                    Notification supervisorAlert = new Notification(
                            notifId + "-SUP",
                            group.getSupervisor().getUserId(),
                            "Progress Overdue",
                            "Your group " + groupId + " has not submitted a progress log for " + daysPassed + " days.");
                    notifications.add(supervisorAlert);
                }
            }
        }
    }

    // FR-16: Get list of inactive groups for UI display
    public List<ProjectGroup> getInactiveGroups(int thresholdDays) {
        List<ProjectGroup> inactive = new ArrayList<>();
        for (ProjectGroup g : groups) {
            ProgressLog latest = getLatestLogForGroup(g.getGroupId());
            long daysPassed = 0;
            if (latest == null || latest.getSubmittedDate() == null) {
                daysPassed = thresholdDays + 1; // assume overdue if no logs submitted yet
            } else {
                long diffMs = System.currentTimeMillis() - latest.getSubmittedDate().getTime();
                daysPassed = diffMs / (1000 * 60 * 60 * 24);
            }
            if (daysPassed > thresholdDays) {
                inactive.add(g);
            }
        }
        return inactive;
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
                                        " has been pending for " + daysPassed
                                        + " days. Manual assignment may be needed.");
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
        if (group == null)
            return new HashMap<>();

        for (PeerReview review : peerReviews) {
            if (review.getReviewee() != null && group.getMembers().contains(review.getReviewee())) {
                String revieweeId = review.getReviewee().getUserId();
                scores.computeIfAbsent(revieweeId, id -> new ArrayList<>()).add(review.getAggregated());
            }
        }

        Map<String, Float> averageMap = new HashMap<>();
        for (Map.Entry<String, List<Float>> entry : scores.entrySet()) {
            float total = 0f;
            for (Float value : entry.getValue())
                total += value;
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
        if (t1.contains(t2) || t2.contains(t1))
            return true;

        // Check word overlap
        String[] words1 = t1.split("\\s+");
        String[] words2 = t2.split("\\s+");
        int matchCount = 0;

        for (String w1 : words1) {
            for (String w2 : words2) {
                if (w1.equalsIgnoreCase(w2))
                    matchCount++;
            }
        }

        int maxWords = Math.max(words1.length, words2.length);
        return (double) matchCount / maxWords > 0.6; // 60% word match
    }
}
