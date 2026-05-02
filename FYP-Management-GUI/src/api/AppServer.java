package api;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import controller.AdminController;
import model.Database;
import model.User;
import model.Admin;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * [SECURITY READY] Lightweight HTTP Server to enable Burp Suite testing.
 * This exposes core controllers via HTTP for vulnerability assessment.
 */
public class AppServer {
    public static void start(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
        // Endpoint for Admin Alerts
        server.createContext("/api/admin/alerts", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if ("POST".equals(exchange.getRequestMethod())) {
                    // Simulating a session/caller from headers for Burp Testing
                    // In a real app, this would be a JWT or Session Cookie
                    String callerId = exchange.getRequestHeaders().getFirst("X-Caller-ID");
                    User caller = Database.getInstance().findUserById(callerId);

                    AdminController ac = new AdminController();
                    try {
                        List<String> alerted = ac.sendInactivityAlerts(caller);
                        String response = "Alerts sent to: " + String.join(", ", alerted);
                        sendResponse(exchange, 200, response);
                    } catch (SecurityException e) {
                        sendResponse(exchange, 403, "Access Denied: " + e.getMessage());
                    } catch (Exception e) {
                        sendResponse(exchange, 500, "Server Error: " + e.getMessage());
                    }
                } else {
                    sendResponse(exchange, 405, "Method Not Allowed");
                }
            }
        });

        System.out.println("Security Test Server started on port " + port);
        System.out.println("Ready for Burp Suite Interception at http://localhost:" + port + "/api/admin/alerts");
        server.setExecutor(null);
        server.start();
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public static void main(String[] args) throws IOException {
        Database.getInstance().seedDemoData();
        start(8081);
    }
}
