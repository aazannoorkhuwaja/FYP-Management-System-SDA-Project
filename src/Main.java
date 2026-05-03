import model.*;
import view.LoginFrame;
import util.DiagnosticService;

import javax.swing.UIManager;
import java.awt.Color;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        DiagnosticService diag = DiagnosticService.getInstance();
        long appStart = diag.startSpan("Core", "Application Startup");
        
        diag.info("Core", "Initializing system. OS: " + System.getProperty("os.name") + 
                  ", Java: " + System.getProperty("java.version") + 
                  ", TraceID: " + diag.getTraceId());

        Database db = Database.getInstance();
        db.seedDemoData();
        db.saveToFile();

        // FR-16 & UC-02 Alt 3b: Auto-alert for inactive groups and pending requests
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            db.checkInactiveGroups(7);
            db.checkAndAlertPendingRequests(3); // 3 days coordinator-defined period
            db.saveToFile();
        }, 0, 1, TimeUnit.DAYS);

        // NFR-10: Metal LAF aligns with Theme's explicit colors; GTK/Qt system LAF often ignores TextField/ComboBox colors.
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
        }
        view.Theme.installGlobalDefaults();
        UIManager.put("Label.foreground", view.Theme.TEXT);
        UIManager.put("Panel.background", view.Theme.BG);
        UIManager.put("Button.font", view.Theme.MAIN_FONT);

        // Launch GUI
        javax.swing.SwingUtilities.invokeLater(() -> {
            diag.endSpan("Core", "Application Startup", appStart);
            new LoginFrame().setVisible(true);
        });
    }
}
