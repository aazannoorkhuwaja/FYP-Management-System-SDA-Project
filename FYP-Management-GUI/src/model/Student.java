package model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Student extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<String> skills;
    private String interests;
    private ProjectGroup group;
    private float contributionScore;

    public Student() {
        setRole("Student");
    }

    public Student(String userId, String name, String email, String password,
                   List<String> skills, String interests, ProjectGroup group, float contributionScore) {
        super(userId, name, email, password, "Student");
        this.skills = skills != null ? skills : new ArrayList<>();
        this.interests = interests;
        this.group = group;
        this.contributionScore = contributionScore;
    }

    public void submitProposal() {
        // Delegated to ProposalController in practice
    }

    public void uploadDocument() {
        // Stub for document upload use case
    }

    public void submitWeeklyLog() {
        // Stub for progress log use case
    }

    public void submitPeerReview() {
        // Delegated to ReviewController in practice
    }

    // Getters and Setters
    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }
    public String getInterests() { return interests; }
    public void setInterests(String interests) { this.interests = interests; }
    public ProjectGroup getGroup() { return group; }
    public void setGroup(ProjectGroup group) { this.group = group; }
    public float getContributionScore() { return contributionScore; }
    public void setContributionScore(float contributionScore) { this.contributionScore = contributionScore; }
}
