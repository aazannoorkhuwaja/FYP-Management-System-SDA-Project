package model;

import java.io.Serializable;

public class Notification implements Serializable {
    private String notifId;
    private String type;
    private String message;

    public Notification() {}

    public Notification(String notifId, String type, String message) {
        this.notifId = notifId;
        this.type = type;
        this.message = message;
    }

    public void send() {
        // Immediate notification logic
    }

    public void sendAsync() {
        // Background notification logic
    }

    public String getNotifId() { return notifId; }
    public void setNotifId(String notifId) { this.notifId = notifId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
