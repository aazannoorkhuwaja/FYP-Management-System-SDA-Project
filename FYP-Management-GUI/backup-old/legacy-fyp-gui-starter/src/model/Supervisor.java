package model;

import java.io.Serializable;

public class Supervisor extends User implements Serializable {
    private String expertise;
    private float avgResponseTime;
    private int currentGroups;
    private int maxGroups;

    public Supervisor() {
        setRole("Supervisor");
    }

    public Supervisor(String userId, String name, String email, String password,
                      String expertise, float avgResponseTime, int currentGroups, int maxGroups) {
        super(userId, name, email, password, "Supervisor");
        this.expertise = expertise;
        this.avgResponseTime = avgResponseTime;
        this.currentGroups = currentGroups;
        this.maxGroups = maxGroups;
    }

    public void reviewProposal() {
        // Stub: supervisor reviews proposal
    }

    public void provideFeedback() {
        // Stub: supervisor writes feedback
    }

    public void postIdeaBank() {
        // Stub: post idea to idea bank
    }

    public void enterMarks() {
        // Stub: enter marks via Grade
    }

    // Getters and Setters
    public String getExpertise() { return expertise; }
    public void setExpertise(String expertise) { this.expertise = expertise; }
    public float getAvgResponseTime() { return avgResponseTime; }
    public void setAvgResponseTime(float avgResponseTime) { this.avgResponseTime = avgResponseTime; }
    public int getCurrentGroups() { return currentGroups; }
    public void setCurrentGroups(int currentGroups) { this.currentGroups = currentGroups; }
    public int getMaxGroups() { return maxGroups; }
    public void setMaxGroups(int maxGroups) { this.maxGroups = maxGroups; }
}
