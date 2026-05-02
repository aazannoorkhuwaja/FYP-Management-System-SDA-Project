package model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class MemberListing implements Serializable {
    private String listingId;
    private String posterId;
    private String requiredSkills;
    private String description;
    private String status;

    public MemberListing() {
        this.status = "Open";
    }

    public MemberListing(String listingId, String posterId, String requiredSkills, String description) {
        this.listingId = listingId;
        this.posterId = posterId;
        this.requiredSkills = requiredSkills;
        this.description = description;
        this.status = "Open";
    }

    public void createListing() {
        this.status = "Open";
    }

    public static List<MemberListing> browseListings(List<MemberListing> all) {
        return new ArrayList<>(all);
    }

    public String getListingId() { return listingId; }
    public void setListingId(String listingId) { this.listingId = listingId; }
    public String getPosterId() { return posterId; }
    public void setPosterId(String posterId) { this.posterId = posterId; }
    public String getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(String requiredSkills) { this.requiredSkills = requiredSkills; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
