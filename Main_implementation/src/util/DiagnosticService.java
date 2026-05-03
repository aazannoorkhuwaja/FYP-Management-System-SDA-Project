package util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * DiagnosticService for structured tracing and debugging.
 */
public class DiagnosticService {
    private static DiagnosticService instance;
    private static final String LOG_FILE = "logs/app_trace.log";
    private String traceId;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private DiagnosticService() {
        this.traceId = UUID.randomUUID().toString().substring(0, 8);
        ensureLogDir();
    }

    public static synchronized DiagnosticService getInstance() {
        if (instance == null) {
            instance = new DiagnosticService();
        }
        return instance;
    }

    private void ensureLogDir() {
        java.io.File dir = new java.io.File("logs");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Start a new operation span to measure execution time.
     */
    public long startSpan(String component, String operation) {
        log("TRACE", component, "START: " + operation);
        return System.currentTimeMillis();
    }

    /**
     * End an operation span and log the duration.
     */
    public void endSpan(String component, String operation, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        log("TRACE", component, String.format("END: %s (Duration: %dms)", operation, duration));
    }

    public void log(String level, String component, String message) {
        String timestamp = dateFormat.format(new Date());
        String logLine = String.format("[%s] [%-7s] [%s] [%s] - %s", timestamp, level, traceId, component, message);
        
        System.out.println(logLine);
        
        try (PrintWriter out = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            out.println(logLine);
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }

    public void logException(String component, String context, Throwable e) {
        error(component, context + " - Exception: " + e.getMessage());
        try (PrintWriter out = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            e.printStackTrace(out);
        } catch (IOException ex) {
            System.err.println("Failed to log exception stack trace: " + ex.getMessage());
        }
    }

    public void info(String component, String message) { log("INFO", component, message); }
    public void warning(String component, String message) { log("WARNING", component, message); }
    public void debug(String component, String message) { log("DEBUG", component, message); }
    public void error(String component, String message) { log("ERROR", component, message); }
    public void trace(String component, String message) { log("TRACE", component, message); }

    public String getTraceId() { return traceId; }
}

