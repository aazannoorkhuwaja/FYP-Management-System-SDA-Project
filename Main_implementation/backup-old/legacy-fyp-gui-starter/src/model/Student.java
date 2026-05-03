package model;

import java.io.Serializable;

public class Student extends User implements Serializable {
    private String skills;
    private String interests;
    private String groupId;
    private float contributionScore;

    public Student() {
        setRole("Student");
    }

    public Student(String userId, String name, String email, String password,
                   String skills, String interests, String groupId, float contributionScore) {
        super(userId, name, email, password, "Student");
        this.skills = skills;
        this.interests = interests;
        this.groupId = groupId;
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
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    public String getInterests() { return interests; }
    public void setInterests(String interests) { this.interests = interests; }
    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }
    public float getContributionScore() { return contributionScore; }
    public void setContributionScore(float contributionScore) { this.contributionScore = contributionScore; }
}
