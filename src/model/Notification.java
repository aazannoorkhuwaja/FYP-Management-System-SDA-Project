package model;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Notification implements Serializable {
    private static final long serialVersionUID = 1L;
    private String notifId;
    private String recipientId;
    private String type;
    private String message;
    private String timestamp;

    public Notification() {}

    public Notification(String notifId, String recipientId, String type, String message) {
        this.notifId = notifId;
        this.recipientId = recipientId;
        this.type = type;
        this.message = message;
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public void send() {
        // Immediate notification logic
    }

    public void sendAsync() {
        // Background notification logic
    }

    public String getNotifId() { return notifId; }
    public void setNotifId(String notifId) { this.notifId = notifId; }
    public String getRecipientId() { return recipientId; }
    public void setRecipientId(String recipientId) { this.recipientId = recipientId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
