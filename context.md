# Context For Next Codex Session

Last updated: 2026-05-03

This file exists so a future Codex session can read it first and recover the current project picture without rediscovering everything from scratch.

## User Goal

The user has a Java Swing Final Year Project Management System. The implementation mostly satisfies the assignment/reference requirements, but the user wants the UI improved and any broken behavior fixed.

Current UI direction:
- Keep the existing Java Swing/MVC implementation.
- Improve usability without rewriting the whole app.
- Avoid separate top-level windows for every menu option.
- Use a FAST-NU/NUCES inspired theme: blue shades and white.
- Organize dashboard actions into clear categories.
- Continue polishing remaining UI issues in later sessions.

## Repository Layout

Root directory:

```text
/home/mahmedasim/Desktop/test-sda-proj
```

Important directories:

```text
Refrence_Material/
  Contains original assignment/reference materials:
  - Assignment.pdf
  - two .docx files with functional/non-functional requirements, use cases, diagrams, etc.

Main_implementation/
  Real Java Swing implementation.
  Important subfolders:
  - src/model
  - src/controller
  - src/view
  - src/api
  - src/util
  - src/test
  - data
  - logs
  - scripts
```

The app is a Java Swing desktop app using models/controllers/views. Entry point:

```text
Main_implementation/src/Main.java
```

Demo credentials from README:

```text
Student:     aazan@fast.edu / pass123
Supervisor:  umer@fast.edu / pass123
Admin:       admin@fast.edu / admin123
```

## What Was There At The Start Of This Work

When Codex started on this UI-betterment task:

- The app already compiled.
- Many assignment-required screens existed in `src/view`.
- The app used a dark "Monolithic Serenity" theme with orange/vermillion accent.
- Student, supervisor, and admin dashboards used sidebars and quick-action grids.
- The major UX problem: clicking almost any option created a separate `JFrame`, so the app felt like many separate programs/windows instead of one integrated system.
- The project had `DiagnosticService` in `src/util`, and many views/controllers import `util.DiagnosticService`.
- Existing tests:
  - `src/test/DashboardControllerTest.java`
  - `src/test/IdeaBankControllerTest.java`

## Changes Already Made

### 1. Embedded Workspace Navigation

New file:

```text
Main_implementation/src/view/WorkspacePanel.java
```

Purpose:
- Provides single-window dashboard navigation.
- Uses `CardLayout`.
- Embeds existing feature `JFrame` content inside the dashboard instead of showing a separate top-level frame.
- Adds a "Back to Dashboard" toolbar.
- Wires existing buttons named `Close`, `Cancel`, `Back`, or `Done` to return to dashboard when embedded.

Important behavior:
- It does not rewrite every feature screen.
- It creates the existing frame, extracts its content pane, and inserts that content into the dashboard workspace.
- This is a pragmatic compatibility layer over the existing Swing code.

### 2. Dashboards Rewired To Use WorkspacePanel

Modified files:

```text
Main_implementation/src/view/StudentDashboardFrame.java
Main_implementation/src/view/SupervisorDashboardFrame.java
Main_implementation/src/view/AdminDashboardFrame.java
```

Changes:
- Sidebar and quick-action buttons now call `openModule(...)` instead of `new SomeFrame(...).setVisible(true)`.
- Each dashboard owns a `WorkspacePanel workspace`.
- Clicking a module opens it inside the dashboard area.
- Dashboard/home sidebar item calls `workspace.showHome()`.

### 3. Student Dashboard Data Improvement

In `StudentDashboardFrame.java`, the milestone/stat cards were changed from hardcoded values like:

```text
Active Phase: Development
Milestones: 2/4
Next Deadline: Dec 15
```

to data-driven values pulled from:

```java
Database.getInstance().getPhasesForGroup(student.getGroup().getGroupId())
```

### 4. FAST-NU Inspired Theme Started

Modified file:

```text
Main_implementation/src/view/Theme.java
```

Old theme:
- Dark background
- Orange/vermillion accent

New direction:
- Light blue/white UI.
- Dark blue sidebars.
- Blue primary buttons.
- White cards/surfaces.

Important color constants currently in `Theme.java`:

```java
BG = #F4F8FC
SURFACE = #FFFFFF
SURFACE_2 = #E2EDF9
BORDER = #C2D3E8
TEXT = #162740
TEXT_MUTED = #5B6F89
FAST_BLUE = #004C99
FAST_BLUE_DARK = #002A5C
FAST_BLUE_LIGHT = #127ACF
ACCENT = #F5B31F
```

The user specifically asked for FAST-NU colors, "blue diff shades and white." Codex searched official FAST/NUCES pages and found:
- FAST-NUCES CFD brand guideline page: `https://cfd.nu.edu.pk/brand-guideline/`
- FAST-NUCES Peshawar official site: `https://pwr.nu.edu.pk/`

No exact color tokens were extracted from the brand PDF yet. Current palette is an approximation based on the official blue/white visual direction.

### 5. Dashboard Options Categorized

Modified files:

```text
StudentDashboardFrame.java
SupervisorDashboardFrame.java
AdminDashboardFrame.java
Theme.java
```

New helper methods in `Theme.java`:

```java
addSidebarSectionLabel(...)
createActionSection(...)
createActionGrid(...)
```

Student dashboard categories:
- Project Work
- Team & Supervision
- Assessment & Support

Supervisor dashboard categories:
- Project Setup
- Review & Assessment

Admin dashboard categories:
- System Management
- Oversight & Controls

Sidebar labels also group items, for example Project/People, Supervision/Assessment/Account, System/Governance/Account.

### 6. Smoke Test Script Added

New file:

```text
Main_implementation/scripts/smoke-tests.sh
```

Purpose:
- Avoids a command-line mistake where `javac` was split across lines and `src/util/*.java` was not included.
- Runs compile and both smoke tests.

Contents:

```sh
#!/usr/bin/env sh
set -eu

cd "$(dirname "$0")/.."

javac -d bin src/Main.java src/model/*.java src/view/*.java src/controller/*.java src/api/*.java src/util/*.java src/test/*.java
java -cp bin test.DashboardControllerTest
java -cp bin test.IdeaBankControllerTest
```

This script was made executable with:

```bash
chmod +x scripts/smoke-tests.sh
```

### 7. Continuation UI Polish

Modified files:

```text
Main_implementation/src/view/Theme.java
Main_implementation/src/view/LoginFrame.java
Main_implementation/src/view/SubmitProposalFrame.java
Main_implementation/src/view/DocumentListFrame.java
Main_implementation/src/view/ProgressReviewFrame.java
```

Changes:
- Removed fixed maximum heights from `Theme.createActionSection(...)` and `Theme.createActionGrid(...)`. The previous caps could clip dashboard quick-action sections with two rows of buttons.
- Reduced shared radius token from 12 to 8 for a tighter dashboard/tool UI.
- Added shared helpers:

```java
Theme.styleTextArea(...)
Theme.styleComboBox(...)
Theme.styleList(...)
```

- Updated proposal submission, document listing/version-history, and progress/document feedback screens to use the new shared helpers for more consistent blue/white controls.
- Reworked `LoginFrame` into a wider institutional entry screen with a dark FAST-blue brand panel and a white sign-in form card. Demo credentials now list student, supervisor, and admin accounts.

## Compile/Test Notes

Correct manual compile command:

```bash
cd /home/mahmedasim/Desktop/test-sda-proj/Main_implementation
javac -d bin src/Main.java src/model/*.java src/view/*.java src/controller/*.java src/api/*.java src/util/*.java
java -cp bin Main
```

Correct smoke test command:

```bash
cd /home/mahmedasim/Desktop/test-sda-proj/Main_implementation
./scripts/smoke-tests.sh
```

The user previously ran this incorrectly:

```bash
javac -d bin src/Main.java src/model/*.java src/view/*.java src/controller/*.java src/api/*.java
  src/util/*.java src/test/*.java
```

Because the newline had no trailing `\`, zsh treated `src/util/*.java` as a separate command. That caused:

```text
package util does not exist
zsh: permission denied: src/util/DiagnosticService.java
```

The tests still appeared to pass afterward because they were running against older compiled classes in `bin`.

Verified after changes:

```text
./scripts/smoke-tests.sh
Running testAdminStats...
PASS: totalGroups count is correct.
Running testPostIdeaValidation...
PASS: Validation caught empty supervisor ID.
Running testDuplicateTitle...
PASS: Duplicate title rejected.
```

Verified again after continuation UI polish:

```text
javac -d bin src/Main.java src/model/*.java src/view/*.java src/controller/*.java src/api/*.java src/util/*.java src/test/*.java
./scripts/smoke-tests.sh

Running testAdminStats...
PASS: totalGroups count is correct.
Running testPostIdeaValidation...
PASS: Validation caught empty supervisor ID.
Running testDuplicateTitle...
PASS: Duplicate title rejected.
```

Manual app compile also passed:

```bash
javac -d bin src/Main.java src/model/*.java src/view/*.java src/controller/*.java src/api/*.java src/util/*.java
```

## Current Working Tree / Git State Caveat

The git status is noisy and shows many files as renamed into `Main_implementation/`. This appears to be pre-existing repository state, not caused by the recent UI edits.

Do not assume all status entries are Codex's changes. Recent intentional source changes are mainly:

```text
Main_implementation/src/view/WorkspacePanel.java
Main_implementation/src/view/Theme.java
Main_implementation/src/view/StudentDashboardFrame.java
Main_implementation/src/view/SupervisorDashboardFrame.java
Main_implementation/src/view/AdminDashboardFrame.java
Main_implementation/scripts/smoke-tests.sh
```

There are also untracked files that existed/appeared in the broader project state, including:

```text
Main_implementation/src/view/ArchiveManagementFrame.java
Main_implementation/src/view/NotificationFrame.java
```

Do not delete or revert user/generated files unless the user explicitly asks.

Smoke tests modify serialized database/log files:

```text
Main_implementation/data/database.dat
Main_implementation/data/database.dat.bak
Main_implementation/logs/app_trace.log
```

Codex restored these after running tests, but git status still reports them as `A` because of broader repo rename/staging state. Treat data/log status carefully.

## Known Remaining UI Issues / Likely Next Work

The user said the colors are better but there are still issues. The next session should inspect the app visually and keep improving UI.

Likely issues to check:

1. Embedded frame compatibility
   - Some feature screens may still look awkward when embedded because they were designed as full `JFrame`s.
   - Some screens may have their own padding/header that duplicates dashboard UI.
   - Some screens may still call `dispose()` on submit, close, or cancel. `WorkspacePanel` handles common close buttons, but submit actions that call `dispose()` may not always return exactly as desired.

2. Theme consistency
   - `Theme.java` changed shared colors, but many individual frames may still hardcode old dark colors or use defaults.
   - Need search for `new Color(` and `setBackground`/`setForeground` in `src/view`.
   - Some lists/tables/text areas may still have poor contrast.

3. Dashboard layout
   - Categories are improved, but card heights and grid sizing may need polish.
   - `Theme.createActionSection` currently has `maximumSize` around 190 and grids around 110. This may need adjustment if text clips or sections feel cramped.

4. Login page
   - It now inherits the blue/white theme but may need a more polished FAST-NU look.
   - Consider a clean institutional header, better card layout, and clearer demo credentials.

5. Module screens
   - Important screens to visually test:
     - `SubmitProposalFrame`
     - `SupervisionRequestFrame`
     - `DocumentListFrame`
     - `ProgressLogFrame`
     - `ProgressReviewFrame`
     - `CreateGroupFrame`
     - `IdeaBrowsingFrame`
     - `ArchiveSearchFrame`
     - `ProfileFrame`
     - `NotificationFrame`
   - Admin/supervisor screens:
     - `ProjectPhaseFrame`
     - `RubricDefinitionFrame`
     - `GradingFrame`
     - `AggregatedReviewsFrame`
     - `ComplaintReviewFrame`
     - `ArchiveManagementFrame`

6. Functional checks
   - Smoke tests are very shallow.
   - Need manual workflow testing by logging in as student, supervisor, admin.
   - Because data is serialized, test runs can mutate `data/database.dat`.

## Suggested Next Session Plan

If the user says "read context.md and continue":

1. Read this file.
2. Inspect current diffs for the key files:

```bash
git diff -- Main_implementation/src/view/Theme.java Main_implementation/src/view/StudentDashboardFrame.java Main_implementation/src/view/SupervisorDashboardFrame.java Main_implementation/src/view/AdminDashboardFrame.java Main_implementation/src/view/WorkspacePanel.java Main_implementation/scripts/smoke-tests.sh
```

3. Compile:

```bash
cd Main_implementation
javac -d bin src/Main.java src/model/*.java src/view/*.java src/controller/*.java src/api/*.java src/util/*.java src/test/*.java
```

4. Run smoke tests:

```bash
./scripts/smoke-tests.sh
```

5. Search for remaining hardcoded dark theme usage:

```bash
rg "new Color|setBackground|setForeground|Theme\\.ACCENT|Theme\\.BG|Theme\\.SURFACE" src/view
```

6. Continue making targeted UI improvements. Prefer improving `Theme.java` helpers and reusing them rather than restyling every screen independently.

## Important Development Constraints

- Use `apply_patch` for manual edits.
- Do not use destructive git commands.
- Do not revert unrelated changes or files not touched by the current task.
- The repo has a dirty/noisy worktree. Be careful with `git status`.
- If a command fails with sandbox `bwrap: loopback: Failed RTM_NEWADDR`, rerun with escalation if it is needed.
- Network access is restricted, but web search is available through the browser tool if current sources are needed.

## Useful Commands

Run app:

```bash
cd /home/mahmedasim/Desktop/test-sda-proj/Main_implementation
javac -d bin src/Main.java src/model/*.java src/view/*.java src/controller/*.java src/api/*.java src/util/*.java
java -cp bin Main
```

Run tests:

```bash
cd /home/mahmedasim/Desktop/test-sda-proj/Main_implementation
./scripts/smoke-tests.sh
```

Clean-ish compile with tests:

```bash
cd /home/mahmedasim/Desktop/test-sda-proj/Main_implementation
javac -d bin src/Main.java src/model/*.java src/view/*.java src/controller/*.java src/api/*.java src/util/*.java src/test/*.java
```

Search view code:

```bash
cd /home/mahmedasim/Desktop/test-sda-proj/Main_implementation
rg "new .*Frame|setVisible\\(true\\)|dispose\\(|setBackground|setForeground|new Color" src/view
```

## Summary

Current state: the app has moved from separate-window navigation toward an integrated single-window dashboard, and the theme has shifted from dark/orange to FAST-NU inspired blue/white. The next work should focus on visual consistency across individual embedded modules, resolving any remaining awkward embedded-frame behavior, and continuing user-friendly layout polish.
