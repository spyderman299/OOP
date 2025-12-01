import main.java.config.DatabaseConnection;
import main.java.service.ServiceFactory;
import main.java.view.LoginView;
import main.java.controller.LoginController;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Initialize database connection
        try {
            DatabaseConnection.initialize();
            System.out.println("Database connection initialized successfully.");
        } catch (Exception e) {
            System.err.println("Failed to initialize database connection: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Initialize services
        try {
            ServiceFactory.init();
            System.out.println("Services initialized successfully.");
        } catch (Exception e) {
            System.err.println("Failed to initialize services: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Start UI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            new LoginController(loginView);
            loginView.setVisible(true);
        });
    }
}

