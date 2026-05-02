import model.*;
import view.LoginFrame;

public class Main {
    public static void main(String[] args) {
        Database db = Database.getInstance();
        db.seedDemoData();
        db.saveToFile();

        // Launch GUI
        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
