package controller;

import model.Database;
import model.MemberListing;
import java.util.List;
import java.util.ArrayList;

public class MemberListingController {
    private Database db;

    public MemberListingController() {
        this.db = Database.getInstance();
    }

    public String createListing(String posterId, String requiredSkills, String description) {
        if (posterId == null || posterId.trim().isEmpty()) {
            return "Error: Poster ID is required.";
        }
        if (requiredSkills == null || requiredSkills.trim().isEmpty()) {
            return "Error: Required skills are required.";
        }
        if (description == null || description.trim().isEmpty()) {
            return "Error: Description is required.";
        }

        String listingId = "ML" + System.currentTimeMillis();
        MemberListing listing = new MemberListing(listingId, posterId.trim(), requiredSkills.trim(), description.trim());
        listing.createListing();
        db.getListings().add(listing);
        db.saveToFile();
        return "Success";
    }

    public List<MemberListing> getAllListings() {
        return db.getListings();
    }

    public List<MemberListing> searchListings(String keyword) {
        List<MemberListing> result = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllListings();
        }
        keyword = keyword.toLowerCase();
        for (MemberListing listing : db.getListings()) {
            if ((listing.getRequiredSkills() != null && listing.getRequiredSkills().toLowerCase().contains(keyword)) ||
                (listing.getDescription() != null && listing.getDescription().toLowerCase().contains(keyword))) {
                result.add(listing);
            }
        }
        return result;
    }
}
