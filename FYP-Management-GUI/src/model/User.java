package model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String userId;
    protected String name;
    protected String email;
    protected String hashedPassword; // Store hashed password, not plain text
    protected String role;

    public User() {}

    public User(String userId, String name, String email, String password, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.hashedPassword = hashPassword(password); // Hash on creation
        this.role = role;
    }

    /**
     * Hash password using SHA-256
     * @param password Plain text password
     * @return Hashed password
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Verify login credentials
     * @param email User email
     * @param password Plain text password
     * @return true if credentials match
     */
    public boolean login(String email, String password) {
        return this.email.equalsIgnoreCase(email) && this.hashedPassword.equals(hashPassword(password));
    }

    /**
     * Check if password is correct (for password change)
     * @param plainPassword Plain text password to verify
     * @return true if password matches
     */
    public boolean verifyPassword(String plainPassword) {
        return this.hashedPassword.equals(hashPassword(plainPassword));
    }

    public void logout() {
        System.out.println(name + " logged out.");
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    /**
     * Set password - will be automatically hashed
     * @param password Plain text password
     */
    public void setPassword(String password) { 
        this.hashedPassword = hashPassword(password); 
    }
    
    /**
     * Get hashed password (for display only, should rarely be used)
     * @return Hashed password
     */
    public String getHashedPassword() { 
        return hashedPassword; 
    }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
