import model.*;
import view.LoginFrame;

import javax.swing.UIManager;
import java.awt.Color;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Database db = Database.getInstance(); util.DiagnosticService diag = util.DiagnosticService.getInstance(); diag.info("Core", "Application initializing. OS: " + System.getProperty("os.name"));
        db.seedDemoData();
        db.saveToFile();

        // FR-16 & UC-02 Alt 3b: Auto-alert for inactive groups and pending requests
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            db.checkInactiveGroups(7);
            db.checkAndAlertPendingRequests(3); // 3 days coordinator-defined period
            db.saveToFile();
        }, 0, 1, TimeUnit.DAYS);

        // NFR-10: Accessibility & Institutional Branding (Monolithic Serenity)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        UIManager.put("Label.foreground", view.Theme.TEXT);
        UIManager.put("Panel.background", view.Theme.BG);
        UIManager.put("Button.font", view.Theme.MAIN_FONT);

        // Launch GUI
        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
