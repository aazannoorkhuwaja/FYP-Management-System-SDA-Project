# serialVersionUID Stabilization Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Add `serialVersionUID` to all serializable model classes and delete the outdated `database.dat` files to prevent `local class incompatible` runtime crashes.

**Architecture:** We will inject `private static final long serialVersionUID = 1L;` into all `model/` classes that implement `Serializable` to stabilize the serialization format. Then we will purge the old `data/database.dat` files to ensure the application generates a fresh, stable database file upon next run.

**Tech Stack:** Java SE, Java Serialization

---

### Task 1: Add serialVersionUID to Base Models

**Files:**
- Modify: `src/model/User.java`
- Modify: `src/model/FYPProposal.java`
- Modify: `src/model/SupervisionRequest.java`

**Step 1: Add serialization ID to User.java**
Add `private static final long serialVersionUID = 1L;` inside the class definition.

**Step 2: Add serialization ID to FYPProposal.java**
Add `private static final long serialVersionUID = 1L;` inside the class definition.

**Step 3: Add serialization ID to SupervisionRequest.java**
Add `private static final long serialVersionUID = 1L;` inside the class definition.

---

### Task 2: Add serialVersionUID to Subclass Models

**Files:**
- Modify: `src/model/Student.java`
- Modify: `src/model/Supervisor.java`
- Modify: `src/model/Admin.java`
- Modify: `src/model/Group.java`
- Modify: `src/model/Notification.java`

**Step 1: Add serialization ID to Student.java**
Add `private static final long serialVersionUID = 1L;` inside the class definition.

**Step 2: Add serialization ID to Supervisor.java**
Add `private static final long serialVersionUID = 1L;` inside the class definition.

**Step 3: Add serialization ID to Admin.java**
Add `private static final long serialVersionUID = 1L;` inside the class definition.

**Step 4: Add serialization ID to Group.java**
Add `private static final long serialVersionUID = 1L;` inside the class definition.

**Step 5: Add serialization ID to Notification.java**
Add `private static final long serialVersionUID = 1L;` inside the class definition.

---

### Task 3: Add serialVersionUID to Database

**Files:**
- Modify: `src/model/Database.java`

**Step 1: Add serialization ID to Database.java**
Add `private static final long serialVersionUID = 1L;` inside the class definition.

---

### Task 4: Clean Old Database Files and Verify

**Files:**
- Delete: `data/database.dat`
- Delete: `data/database.dat.bak`
- Delete: `data/database.dat.tmp`

**Step 1: Delete outdated files**
Run `rm -f data/database.dat*` in the project root.

**Step 2: Recompile project**
Run `cd src && javac Main.java model/*.java view/*.java controller/*.java`
Expected: Exit code 0.

**Step 3: Run project to generate fresh DB**
Run `cd src && java Main`
Expected: Application launches without `local class incompatible` errors, and a new `data/database.dat` file is created.
