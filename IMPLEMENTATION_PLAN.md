# FYP Management Platform - Complete Implementation Plan

**Assignment 3 Goal:** Implement ALL functional requirements with working GUI

---

## Phase 1: Foundation & Security (Critical - Do First)

### 1.1 Fix Security - Password Hashing ⚠️ CRITICAL
- [ ] Add `MessageDigest` for SHA-256 hashing or use `BCrypt`
- [ ] Update `User.java` to store hashed passwords
- [ ] Update `AuthController.java` to hash on login/registration
- **Files to modify:** `model/User.java`, `controller/AuthController.java`

### 1.2 Complete Missing Model Classes
- [ ] `ProgressLog.java` - Weekly progress tracking
- [ ] `Feedback.java` - Supervisor feedback on logs/documents
- [ ] `ProjectPhase.java` - Milestone/deadline management
- [ ] `Rubric.java` - Grading criteria
- [ ] `Grade.java` - Final grades for students
- [ ] `Document.java` - File uploads with version history
- [ ] `IdeaBank.java` - Supervisor-posted project ideas
- [ ] `Archive.java` - Completed project repository
- [ ] `Complaint.java` - Formal complaint submission
- **New folder:** `model/` (add all above)

### 1.3 Update Database.java
- [ ] Add storage for ProgressLog, Feedback, Document, Grade, Archive, Complaint, IdeaBank, Rubric
- [ ] Add helper methods to fetch/filter by type
- [ ] Implement archive search functionality
- **File to modify:** `model/Database.java`

---

## Phase 2: Core Features Implementation

### 2.1 Group Management & Members (FR-04, FR-05)
- [ ] Update `StudentDashboardFrame.java` with "Form Group" button
- [ ] Create new `GroupManagementFrame.java` - form group, add members
- [ ] Create new `MemberListingFrame.java` - browse members by skills
- [ ] Create controller `GroupController.java`
- **NEW files:** `view/GroupManagementFrame.java`, `view/MemberListingFrame.java`, `controller/GroupController.java`

### 2.2 Progress Tracking (FR-14, FR-15, FR-16)
- [ ] Create new `ProgressLogFrame.java` - submit weekly logs
- [ ] Create new `ProgressReviewFrame.java` - supervisor feedback
- [ ] Create controller `ProgressController.java`
- [ ] Add auto-alert logic in `Database.java` when no log submitted in X days
- **NEW files:** `view/ProgressLogFrame.java`, `view/ProgressReviewFrame.java`, `controller/ProgressController.java`

### 2.3 Document/File Management (FR-13)
- [ ] Create new `DocumentUploadFrame.java` - upload files with versioning
- [ ] Create new `DocumentListFrame.java` - view files and versions
- [ ] Create controller `DocumentController.java`
- [ ] Store files in `data/documents/` directory with version tracking
- **NEW files:** `view/DocumentUploadFrame.java`, `view/DocumentListFrame.java`, `controller/DocumentController.java`

### 2.4 Project Phases & Deadlines (FR-12)
- [ ] Create new `ProjectPhaseFrame.java` - supervisor defines milestones
- [ ] Create new `MilestoneTrackingFrame.java` - students view deadlines
- [ ] Create controller `PhaseController.java`
- **NEW files:** `view/ProjectPhaseFrame.java`, `view/MilestoneTrackingFrame.java`, `controller/PhaseController.java`

### 2.5 Grading System (FR-18)
- [ ] Create new `RubricDefinitionFrame.java` - define grading criteria
- [ ] Create new `GradingFrame.java` - supervisor assigns grades
- [ ] Create new `StudentGradesFrame.java` - students view their grades
- [ ] Create controller `GradeController.java`
- **NEW files:** `view/RubricDefinitionFrame.java`, `view/GradingFrame.java`, `view/StudentGradesFrame.java`, `controller/GradeController.java`

### 2.6 Project Archive (FR-19)
- [ ] Create new `ArchiveSearchFrame.java` - searchable archive of completed projects
- [ ] Add search by title, domain, student name, year
- [ ] Create controller `ArchiveController.java`
- **NEW files:** `view/ArchiveSearchFrame.java`, `controller/ArchiveController.java`

### 2.7 Idea Bank (FR-06)
- [ ] Create new `IdeaBankFrame.java` - supervisor posts ideas
- [ ] Create new `IdeaBrowsingFrame.java` - students browse and select ideas
- [ ] Create controller `IdeaBankController.java`
- **NEW files:** `view/IdeaBankFrame.java`, `view/IdeaBrowsingFrame.java`, `controller/IdeaBankController.java`

### 2.8 Complaint System (FR-20)
- [ ] Create new `ComplaintFrame.java` - students submit complaints
- [ ] Create new `ComplaintReviewFrame.java` - admin reviews complaints
- [ ] Create controller `ComplaintController.java`
- **NEW files:** `view/ComplaintFrame.java`, `view/ComplaintReviewFrame.java`, `controller/ComplaintController.java`

### 2.9 User Profiles Enhancement (FR-02, FR-03)
- [ ] Create new `ProfileFrame.java` - students add skills, supervisors add research areas
- [ ] Add supervisor response time tracking logic
- [ ] Update `Database.java` to compute supervisor metrics
- **NEW files:** `view/ProfileFrame.java`

---

## Phase 3: Dashboard Updates

### 3.1 Enhanced Student Dashboard
- [ ] Add buttons for: Form Group, View Progress, Upload Documents, View Milestones, View Grades, Browse Archive
- [ ] Display notifications for alerts (overdue logs, supervisor feedback)
- **File to modify:** `view/StudentDashboardFrame.java`

### 3.2 Enhanced Supervisor Dashboard
- [ ] Add buttons for: Review Proposals, Review Progress, Post Ideas, Define Rubric, Grade Students
- [ ] Show pending requests and submissions
- **File to modify:** `view/SupervisorDashboardFrame.java`

### 3.3 Admin Dashboard (NEW)
- [ ] Create `AdminDashboardFrame.java` - review complaints, manage users, view system stats
- **NEW file:** `view/AdminDashboardFrame.java`

---

## Phase 4: Error Handling & Validation

### 4.1 Input Validation
- [ ] Add validation to all text inputs (not empty, valid format, etc.)
- [ ] Show user-friendly error dialogs
- [ ] Validate file uploads (size, type)

### 4.2 Database Error Handling
- [ ] Handle file corruption gracefully
- [ ] Add backup/recovery logic
- [ ] Ensure atomicity of operations

---

## Implementation Priority Order

**HIGH PRIORITY (Do First):**
1. Password hashing (security issue)
2. Complete model classes
3. Update Database.java
4. Group management UI
5. Progress tracking
6. Document management

**MEDIUM PRIORITY:**
7. Project phases & deadlines
8. Grading system
9. Archive system
10. Idea bank

**LOWER PRIORITY:**
11. Complaint system
12. Profile enhancements
13. Admin dashboard

---

## Testing Checklist - All Functional Requirements

**UC-01: Submit FYP Proposal** ✅ (Already done)
- [ ] Student submits title, domain, abstract
- [ ] Duplicate title check works
- [ ] Proposal saved as Pending

**UC-02: Send Supervision Request** ✅ (Already done)
- [ ] Student selects supervisor
- [ ] Supervisor gets request notification
- [ ] Supervisor can accept/decline with reason

**UC-03: Upload Project Document** ❌ (Missing)
- [ ] Student uploads files
- [ ] Previous versions accessible
- [ ] Timestamps recorded

**UC-04: Submit Peer Review** ✅ (Already done)
- [ ] Members rate each other privately
- [ ] Hidden from other members
- [ ] Visible to supervisor/admin

**UC-05: Submit Weekly Progress Log** ❌ (Missing)
- [ ] Student submits progress description
- [ ] Supervisor provides feedback
- [ ] Auto-alert if overdue

**UC-06: Define Project Phases** ❌ (Missing)
- [ ] Supervisor sets deadlines
- [ ] Visible to all members
- [ ] Tracks completion

**UC-07: Browse Idea Bank** ❌ (Missing)
- [ ] Supervisors post ideas
- [ ] Students search and select
- [ ] Assigned as proposal topic

**UC-08: Grade with Rubric** ❌ (Missing)
- [ ] Admin/Supervisor defines rubric
- [ ] Visible to students from start
- [ ] Grades recorded and visible

**UC-09: Search Project Archive** ❌ (Missing)
- [ ] Search by title, domain, student
- [ ] Download project details
- [ ] Year filtering

**UC-10: Submit Complaint** ❌ (Missing)
- [ ] Student files formal complaint
- [ ] Admin reviews
- [ ] Actions tracked

**Additional Requirements (FR-02, FR-03, FR-05, etc.):**
- [ ] User profiles with skills/research areas
- [ ] Response time metrics
- [ ] Member listing feature
- [ ] Alert system

---

## Key Implementation Notes

### Aazan's Responsibility (MODEL/DATABASE LAYER):
1. **All new model classes** - ProgressLog, Feedback, Document, Rubric, Grade, Archive, Complaint, IdeaBank, ProjectPhase
2. **Database.java enhancements** - Storage, retrieval, search, alerts
3. **Security** - Password hashing implementation
4. **Persistence logic** - File versioning, serialization

### Team Members (UI/CONTROLLERS):
- Views/Frames for each feature
- Controllers to wire model to UI
- User interaction handling
- Validation and error display

---

## Estimated Effort

| Phase | Time | Priority |
|-------|------|----------|
| Security + Model Classes | 3-4 hours | CRITICAL |
| Group Management | 2-3 hours | HIGH |
| Progress Tracking | 2-3 hours | HIGH |
| Document Management | 2-3 hours | HIGH |
| Grading System | 2-3 hours | MEDIUM |
| Archive | 2 hours | MEDIUM |
| Idea Bank | 1-2 hours | MEDIUM |
| Complaint System | 1-2 hours | LOW |
| Testing & Polish | 2 hours | ALL |
| **TOTAL** | **17-23 hours** | **Complete Implementation** |

---

## GitHub & Submission

**Final Deliverables:**
1. Complete GitHub repo (public)
   - All source code
   - README.md with compile & run instructions
   - .gitignore for build artifacts
2. Working compiled JAR or instructions to compile
3. Demo video (3 minutes) showing all major features
4. Reflection video (3 minutes) on LLM usage

