package model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Supervisor extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<String> researchAreas;
    private float avgResponseTime;
    private int currentGroups;
    private int maxGroups;

    public Supervisor() {
        setRole("Supervisor");
    }

    public Supervisor(String userId, String name, String email, String password,
                      List<String> researchAreas, float avgResponseTime, int currentGroups, int maxGroups) {
        super(userId, name, email, password, "Supervisor");
        this.researchAreas = researchAreas != null ? researchAreas : new ArrayList<>();
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
    public List<String> getResearchAreas() { return researchAreas; }
    public void setResearchAreas(List<String> researchAreas) { this.researchAreas = researchAreas; }
    public float getAvgResponseTime() { return avgResponseTime; }
    public void setAvgResponseTime(float avgResponseTime) { this.avgResponseTime = avgResponseTime; }
    public int getCurrentGroups() { return currentGroups; }
    public void setCurrentGroups(int currentGroups) { this.currentGroups = currentGroups; }
    public int getMaxGroups() { return maxGroups; }
    public void setMaxGroups(int maxGroups) { this.maxGroups = maxGroups; }
}
