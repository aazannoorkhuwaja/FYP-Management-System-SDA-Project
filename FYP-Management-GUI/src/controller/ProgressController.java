package controller;

import model.Database;
import model.Feedback;
import model.ProgressLog;
import model.ProjectGroup;
import model.Student;
import util.DiagnosticService;
import java.util.Date;
import java.util.List;

public class ProgressController {
    private Database db;
    private DiagnosticService diag;

    public ProgressController() {
        this.db = Database.getInstance();
        this.diag = DiagnosticService.getInstance();
    }

    public String submitLog(String groupId, int week, String content, String individualContributions) {
        diag.info("ProgressController", "Submitting log for group " + groupId + ", week " + week);
        
        if (groupId == null || groupId.trim().isEmpty()) {
            return "Error: Group ID is required.";
        }
        if (week < 1 || week > 20) {
            return "Error: Week number must be between 1 and 20.";
        }
        if (content == null || content.trim().isEmpty()) {
            return "Error: Log content is required.";
        }

        ProjectGroup group = db.findGroupById(groupId);
        if (group == null) {
            diag.error("ProgressController", "Group not found: " + groupId);
            return "Error: Group not found.";
        }

        String logId = "LOG" + System.currentTimeMillis();
        ProgressLog log = new ProgressLog(logId, group, week, content.trim(),
                individualContributions != null ? individualContributions.trim() : "");
        log.submit();

        db.getLogs().add(log);

        // Gap #4 — Auto-update contributionScore
        if (group.getMembers() != null && !group.getMembers().isEmpty()) {
            int totalLogs = 0;
            for (ProgressLog l : db.getLogs()) {
                if (l.getGroup() != null && l.getGroup().getGroupId().equals(groupId)) {
                    totalLogs++;
                }
            }
            float scorePerLog = 100.0f / Math.max(totalLogs, 1);
            for (Student member : group.getMembers()) {
                member.setContributionScore(Math.min(100f, member.getContributionScore() + scorePerLog));
            }
        }

        db.saveToFile();
        diag.info("ProgressController", "Successfully submitted log: " + logId);
        return "Success";
    }

    public List<ProgressLog> getLogsForGroup(String groupId) {
        return db.getLogsForGroup(groupId);
    }

    public String provideFeedback(String logId, String feedbackContent) {
        diag.info("ProgressController", "Providing feedback for log " + logId);
        
        if (logId == null || logId.trim().isEmpty()) {
            return "Error: Log ID is required.";
        }
        if (feedbackContent == null || feedbackContent.trim().isEmpty()) {
            return "Error: Feedback content is required.";
        }

        ProgressLog targetLog = null;
        for (ProgressLog log : db.getLogs()) {
            if (log.getLogId().equals(logId)) {
                targetLog = log;
                break;
            }
        }

        if (targetLog == null) {
            diag.error("ProgressController", "Log not found: " + logId);
            return "Error: Progress log not found.";
        }

        targetLog.setSupervisorFeedback(feedbackContent.trim());
        targetLog.setFeedbackDate(new Date());

        String feedbackId = "FB-" + System.currentTimeMillis();
        String supervisorId = (targetLog.getGroup() != null && targetLog.getGroup().getSupervisor() != null) 
                             ? targetLog.getGroup().getSupervisor().getUserId() : "System";
                             
        Feedback feedbackObj = new Feedback(feedbackId, logId, supervisorId, feedbackContent.trim(), new Date());
        db.getFeedbacks().add(feedbackObj);

        db.saveToFile();
        diag.info("ProgressController", "Successfully provided feedback for log: " + logId);
        return "Success";
    }
}
