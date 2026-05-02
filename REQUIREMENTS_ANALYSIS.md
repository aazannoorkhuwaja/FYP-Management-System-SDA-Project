# FYP Management Platform - Requirements Analysis

## From Previous Assignments Documentation

### Key Functional Requirements (FR):

#### Currently Implemented ✅
- **FR-01:** Role-based dashboard (Student, Supervisor, Admin)
- **FR-07:** Submit FYP Proposal (title, domain, abstract)
- **FR-08:** Duplicate title checking before proposal submission
- **FR-09:** Send Supervision Request to supervisor
- **FR-10:** Supervisor accept/decline supervision requests with reason
- **FR-17:** Private peer reviews with hidden ratings (only visible to supervisor/admin)

#### NOT Fully Implemented ❌

**User Management & Profile:**
- **FR-02:** User profiles with skills (Student) and research areas (Supervisor)
- **FR-03:** Supervisor average response time tracking

**Group Management:**
- **FR-04:** Project group formation with up to 3 members
- **FR-05:** Member listing feature to find group members with specific skills

**Idea Bank & Resources:**
- **FR-06:** Supervisors posting project ideas in Idea Bank
- **FR-06:** Students browsing Idea Bank to select topics

**Project Phases & Deadlines:**
- **FR-12:** Supervisor-defined deadlines for project phases (mid-defense, final submission)

**Document Management:**
- **FR-13:** Upload project files with version history tracking

**Progress Tracking:**
- **FR-14:** Weekly progress log submission
- **FR-15:** Supervisor feedback on files and logs with timestamp
- **FR-16:** Auto-alert if no progress log submitted within defined period

**Grading & Archive:**
- **FR-18:** Final grading using rubric with criteria visible to students
- **FR-19:** Searchable archive of completed projects

**Complaints & Support:**
- **FR-20:** Formal complaint submission to admin for unresponsive supervisors

---

## Missing Components Summary

### High Priority (Core to Assignment):
1. **Project Group Management** - Form groups, manage members
2. **Progress Tracking System** - Weekly logs and supervisor feedback
3. **Document/File Management** - Upload with version history
4. **Project Phases & Deadlines** - Phase-based milestone tracking
5. **Grading Rubric** - Define and apply grading criteria
6. **Project Archive** - Searchable repository of completed projects

### Medium Priority:
7. **Idea Bank** - Supervisor ideas, student browsing
8. **User Skills/Research Areas** - Profile enhancement
9. **Member Listing** - Find group members by skills
10. **Supervisor Response Time Tracking** - Performance metrics

### Lower Priority (Nice-to-have):
11. **Complaint System** - Formal complaint escalation
12. **Automated Alerts** - Auto-notify on overdue progress

---

## Current Implementation Status

### Fully Implemented Model Classes:
- `User`, `Student`, `Supervisor`, `Admin`
- `ProjectGroup`, `GroupMember`
- `FYPProposal`, `SupervisionRequest`
- `PeerReview`

### Partially Implemented:
- `Database.java` - Basic persistence, needs archive support
- Controllers - Basic use cases only

### Missing Model Classes (Need Implementation):
- Document/FileUpload handling
- ProgressLog
- Feedback
- Rubric/Grade
- IdeaBank
- Archive (for completed projects)
- Complaint
- ProjectPhase

### Missing View Components:
- Group Management UI
- Document Upload UI
- Progress Log UI
- Grading UI
- Archive Browsing UI
- Idea Bank UI

### Missing Controllers:
- Document management
- Progress tracking
- Grading & rubric
- Archive search
- Idea bank management
- Complaint handling

---

## Immediate Next Steps for Assignment 3:

1. **Implement Group Management UI** - Allow students to form/manage groups
2. **Add Progress Log Feature** - Weekly submission with supervisor feedback
3. **Document/File Upload** - Store with version history
4. **Project Phases & Deadlines** - Define milestone tracking
5. **Grading System** - Rubric-based grading interface
6. **Archive System** - Search completed projects
7. **Complete all 6 core use cases** (currently only 3/6 implemented)

---

## Non-Functional Requirements Status

| NFR | Requirement | Status |
|-----|-------------|--------|
| NFR-01 | Performance (Dashboard load time) | Partial - Basic UI works but needs optimization |
| NFR-02 | Security (Password hashing) | ❌ Missing - Using plain text (security issue!) |
| NFR-03 | Reliability | ⚠️ Depends on deployment environment |
| NFR-04 | Robustness (Backup recovery) | ⚠️ Basic serialization only |
| NFR-05 | Usability (Self-explanatory UI) | Partial |
| NFR-06 | Compatibility (Chrome/Firefox/Mobile) | ❌ Swing GUI not mobile-friendly |
| NFR-07 | Scalability | ⚠️ Limited by serialization approach |
| NFR-08 | Maintainability | Partial - Modular structure present |
| NFR-09 | Privacy (Peer review hiding) | ✅ Implemented |
| NFR-10 | Accessibility (Color contrast) | ⚠️ Needs verification |

### Critical Issues to Address:
- **Security:** Implement proper password hashing instead of plain text
- **Mobile Compatibility:** Consider web-based approach instead of Swing GUI
