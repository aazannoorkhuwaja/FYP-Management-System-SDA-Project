package controller;

import model.Database;
import model.ProjectGroup;
import model.Student;
import model.Supervisor;
import model.User;
import util.DiagnosticService;
import java.util.List;
import java.util.ArrayList;

public class GroupController {
    private Database db;
    private DiagnosticService diag;

    public GroupController() {
        this.db = Database.getInstance();
        this.diag = DiagnosticService.getInstance();
    }

    public String formGroup(String groupId, String projectTitle, String supervisorId, List<String> memberIds) {
        diag.info("GroupController", "Attempting to form group: " + groupId);
        
        if (groupId == null || groupId.trim().isEmpty()) {
            diag.error("GroupController", "Group ID is missing");
            return "Error: Group ID is required.";
        }
        if (db.findGroupById(groupId) != null) {
            diag.error("GroupController", "Group ID already exists: " + groupId);
            return "Error: Group with this ID already exists.";
        }
        if (projectTitle == null || projectTitle.trim().isEmpty()) {
            return "Error: Project title is required.";
        }
        if (supervisorId == null || supervisorId.trim().isEmpty()) {
            return "Error: Supervisor ID is required.";
        }

        Supervisor supervisor = findSupervisor(supervisorId);
        if (supervisor == null) {
            diag.error("GroupController", "Supervisor not found: " + supervisorId);
            return "Error: Supervisor not found.";
        }

        if (supervisor.getCurrentGroups() >= supervisor.getMaxGroups()) {
            diag.error("GroupController", "Supervisor limit reached: " + supervisorId);
            return "Error: Supervisor has reached maximum group limit.";
        }

        if (memberIds == null || memberIds.isEmpty()) {
            return "Error: At least one member is required.";
        }
        int limit = db.getMaxGroupSize();
        if (memberIds.size() > limit) {
            return "Error: A group can have a maximum of " + limit + " members.";
        }

        List<Student> members = new ArrayList<>();
        for (String studentId : memberIds) {
            User user = db.findUserById(studentId);
            if (user == null || !(user instanceof Student)) {
                diag.error("GroupController", "Student not found: " + studentId);
                return "Error: Student not found for ID: " + studentId;
            }
            Student student = (Student) user;
            if (student.getGroup() != null) {
                diag.error("GroupController", "Student already in group: " + studentId);
                return "Error: Student " + studentId + " is already in another group.";
            }
            members.add(student);
        }

        ProjectGroup group = new ProjectGroup(groupId.trim(), projectTitle.trim(), members.size(), supervisor);
        for (Student member : members) {
            group.addMember(member);
            member.setGroup(group);
        }
        supervisor.setCurrentGroups(supervisor.getCurrentGroups() + 1);
        db.getGroups().add(group);
        db.saveToFile();
        diag.info("GroupController", "Successfully formed group: " + groupId);
        return "Success";
    }

    public String addMemberToGroup(String groupId, String studentId) {
        diag.trace("GroupController", "Adding member " + studentId + " to group " + groupId);
        if (groupId == null || groupId.trim().isEmpty()) {
            return "Error: Group ID is required.";
        }
        if (studentId == null || studentId.trim().isEmpty()) {
            return "Error: Student ID is required.";
        }

        ProjectGroup group = db.findGroupById(groupId);
        if (group == null) {
            return "Error: Group not found.";
        }

        User user = db.findUserById(studentId);
        if (user == null || !(user instanceof Student)) {
            return "Error: Student not found.";
        }
        Student student = (Student) user;

        if (student.getGroup() != null) {
            return "Error: Student is already in another group.";
        }

        if (group.getMembers().size() >= db.getMaxGroupSize()) {
            return "Error: Group has already reached the maximum member limit.";
        }

        if (group.getMembers().contains(student)) {
            return "Error: Student is already a member of this group.";
        }

        group.addMember(student);
        student.setGroup(group);
        db.saveToFile();
        diag.info("GroupController", "Successfully added member " + studentId + " to group " + groupId);
        return "Success";
    }

    public ProjectGroup findGroupById(String groupId) {
        return db.findGroupById(groupId);
    }

    public String updateGroupInfo(String groupId, String projectTitle) {
        diag.trace("GroupController", "Updating group info for: " + groupId);
        if (groupId == null || groupId.trim().isEmpty()) {
            return "Error: Group ID is required.";
        }
        if (projectTitle == null || projectTitle.trim().isEmpty()) {
            return "Error: Project title is required.";
        }

        ProjectGroup group = db.findGroupById(groupId);
        if (group == null) {
            return "Error: Group not found.";
        }

        group.setProjectTitle(projectTitle.trim());
        db.saveToFile();
        diag.info("GroupController", "Successfully updated title for group: " + groupId);
        return "Success";
    }

    public List<ProjectGroup> getAllGroups() {
        return db.getGroups();
    }

    private Supervisor findSupervisor(String supervisorId) {
        for (User u : db.getUsers()) {
            if (u instanceof Supervisor && u.getUserId().equals(supervisorId)) {
                return (Supervisor) u;
            }
        }
        return null;
    }
}
