# FYP Management Platform — Design Sprint Plan

**Goal:** Apply all 7 design disciplines to the FYP Management Platform (Java Swing SDA Assignment-3) to produce: a design system, a production HTML/CSS dashboard UI, a theme spec, brand copy guidelines, generative art, and a refined poster.

**Architecture:** The app is a Java Swing MVC application with 3 roles (Student, Supervisor, Admin), 3 core UCs (Proposal, Supervision Request, Peer Review), and ~25 screens. The design sprint will produce a parallel HTML/CSS prototype + brand identity system.

**Tech Stack:** HTML · Vanilla CSS · Vanilla JS (no build tool) · Python Pillow (canvas) · p5.js (generative)

---

## Phase Summary

| # | Deliverable | Workflow | Status |
|---|-------------|----------|--------|
| 1 | Brainstorm & Design System Spec | ui-ux-pro-max | ✅ done (this file) |
| 2 | Brand Identity & Copy Guidelines | brand-guidelines | ⬜ pending |
| 3 | Theme Spec (color tokens + fonts) | theme-factory | ⬜ pending |
| 4 | Production Dashboard HTML | frontend-design | ⬜ pending |
| 5 | Interactive HTML Artifact (shadcn style) | web-artifacts-builder | ⬜ pending |
| 6 | Generative / Algorithmic Art (p5.js HTML) | algorithmic-art | ⬜ pending |
| 7 | Poster PNG (refined) | canvas-design | ✅ done (fyp_management_poster.png) |

---

## Brainstorm Output (Phase 1)

### What is this product?
An **academic project management platform** for engineering universities. Three roles:
- **Student** — submit proposals, request supervisors, track milestones, peer-review teammates
- **Supervisor** — accept/reject supervision requests, review progress, grade, view peer reviews
- **Admin/FYP Coordinator** — manage groups, approve proposals, define rubrics, archive projects

### Who are the users, emotionally?
- Students: **anxious, deadline-driven, want clarity** on where they stand
- Supervisors: **time-poor, need efficiency**, want signal not noise
- Admin: **process-oriented, institutional**, need audit trail and overview

### What should users feel in 3 seconds?
**Clarity + Authority.** This is not a startup product. It should feel like a rigorous, trustworthy institutional system — but modern, not bureaucratic. Like a well-designed academic journal meets a Notion-style workspace.

### Design Direction Chosen
**"Institutional Modernism"** — clean Swiss-grid authority + warm editorial accents.
- Not SaaS purple gradients
- Not corporate bland
- Not playful/startup
- *Yes*: constrained grid, editorial typography, status-driven color logic, deliberate data density

### DFII Score
| Dimension | Score |
|-----------|-------|
| Aesthetic Impact | 4 |
| Context Fit | 5 |
| Implementation Feasibility | 5 |
| Performance Safety | 5 |
| Consistency Risk (subtract) | 2 |
| **DFII Total** | **17 → capped at 15 → Excellent** |

### Design System Decisions

**Fonts:**
- Display/Headings: `DM Serif Display` — editorial, authoritative, warm
- Body/Labels/Data: `DM Sans` — modern grotesque, highly legible at small sizes
- Code/IDs/Tags: `JetBrains Mono` — monospace for student IDs, timestamps

**Color Tokens:**
```
--bg:          #0D0F14   (near-black)
--surface:     #13161E   (card surface)
--surface-2:   #1C2030   (elevated)
--border:      #252A3A   (subtle boundary)
--text:        #E8EAF0   (primary text)
--text-muted:  #7A8099   (secondary text)
--accent:      #D94F2E   (vermillion — action, alert)
--accent-2:    #3B8FCC   (steel-blue — info, links)
--success:     #2EA86B   (milestone complete)
--warning:     #D98C2E   (pending)
--danger:      #D94F2E   (rejected/overdue)
```

**Spacing Rhythm:** 4px base unit. Scale: 4, 8, 12, 16, 24, 32, 48, 64, 96
**Border Radius:** 4px (sharp, institutional). No pill shapes.
**Z-Index:** sidebar 10, modal 30, toast 50

**Motion:** 200ms ease-out for hovers; 300ms for panels; no decorative animation.

**Differentiation Anchor:**
The left sidebar uses a **monolithic slab** design (matching the poster's Monolithic Serenity philosophy) — full-height, dark, with thin colored status bars that act as role indicators. This visual motif ties all deliverables together.

### Screen Inventory (for HTML prototype)
1. Login screen
2. Student Dashboard
3. Submit Proposal form
4. Supervisor Dashboard
5. Peer Review screen

### Non-Functional Assumptions
- Desktop-first (university lab computers) — target 1280px+
- No backend needed for prototype (mock data inline)
- Accessible: min 4.5:1 contrast, keyboard nav, focus rings

---

## Errors Log
| Error | Attempt | Resolution |
|-------|---------|------------|
| — | — | — |
