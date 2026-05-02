# Assignment Compliance Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Implement the final missing software features (Student Rubric Visibility, Individual Contribution, Peer Review Rubric) to achieve 100% compliance with the FYP Management Project requirements.

**Architecture:** We will extend the existing Swing GUI frames and models, avoiding complex database schema shifts. We will rely on simple extra string fields and calculated averages.

**Tech Stack:** Java, Swing, Java Serialization.

---

### Task 1: Student Rubric Visibility (FR-18)

**Files:**
- Create: `src/view/RubricViewerFrame.java`
- Modify: `src/view/StudentDashboardFrame.java`

**Step 1: Create RubricViewerFrame**
Create a new file `src/view/RubricViewerFrame.java` that extends `JFrame`. It should fetch rubrics from `Database.getInstance().getRubrics()` and display them in a `JTextArea` or `JList`. 

**Step 2: Add Button to StudentDashboardFrame**
Modify `src/view/StudentDashboardFrame.java` to add a "View Rubrics" button in the side panel that instantiates and shows `RubricViewerFrame`.

---

### Task 2: Individual Contribution Tracking (FR-04)

**Files:**
- Modify: `src/model/ProgressLog.java`
- Modify: `src/controller/ProgressController.java`
- Modify: `src/view/ProgressLogFrame.java`

**Step 1: Update Model**
Modify `src/model/ProgressLog.java` to add `private String individualContributions;`, update the constructor, and add getters/setters.

**Step 2: Update Controller**
Modify `src/controller/ProgressController.java` -> `submitLog` to accept `String individualContributions`, pass it to the `ProgressLog` constructor, and handle empty validation.

**Step 3: Update GUI**
Modify `src/view/ProgressLogFrame.java` to add a new `JTextArea` for "Individual Contributions (Who did what?)" and pass its text to the controller.

---

### Task 3: Peer Review Rubric (UC-04)

**Files:**
- Modify: `src/view/PeerReviewFrame.java`

**Step 1: Replace Spinner**
Modify `src/view/PeerReviewFrame.java`. Remove `ratingSpinner`. Add three spinners (1-5): `commSpinner` (Communication), `qualitySpinner` (Work Quality), `timeSpinner` (Timeliness).

**Step 2: Update Submission**
In `submitReview()`, read all three values. Calculate the average. Store the breakdown in the map: `Map.of("communication", comm, "quality", qual, "timeliness", time, "rating", average)`. Ensure the `PeerReview` constructor receives this expanded map.
