# Context2 — Latest Implementation & UI Work

Last updated: 2026-05-03  

This file summarizes **recent changes** made to the FYP Management Platform (Java Swing). For full project background, requirements, and architecture, see **`context.md`**.

---

## What Was Done Recently

### Layout & dashboard UX
- **Student, Admin, Supervisor dashboards**: Left-aligned content in scroll areas; stats row no longer clipped (`wrapNaturalHeight`, removed bad `MaximumSize` caps on KPI cards).
- **Quick actions** grouped in a **white card** (`Theme.createDashboardQuickLinksShell`) with section titles and `GridBagLayout` button grids (`buildActionButtonGrid`) so buttons don’t stretch to one huge row height.
- **Header** underline via `Theme.dashboardHeaderSeparator()`; **`Box.createVerticalGlue()`** at bottom so empty space doesn’t sit between sections.
- **`WorkspacePanel`**: Embeds module `JFrame` content; dashboard home scroll uses `HORIZONTAL_SCROLLBAR_NEVER` where applied.
- **Login**: Centered **GridBagLayout** form card; fixed field width (`FORM_FIELD_WIDTH`); `pack()` + maximize on open.
- **Many module frames**: Moved from fixed `setSize` to **`pack()`** + **`setMinimumSize`** + **`DISPOSE_ON_CLOSE`**; centered form shells where appropriate (`createFormCardShell`, `wrapCentered`).

### Notifications screen
- **`NotificationFrame`**: Compact table viewport (`syncViewportHeight`), corrected **row indexing** when marking read (newest-first table vs list order).
- Table chrome aligned with flat white header + readable body (see Theme / `COLOR_*` below).

### New / fixed screen
- **`IdeaBrowsingFrame`**: Browse/search ideas for students (`StudentDashboard` → “Browse Ideas”); **`Database.getIdeas()`** backed list.

---

## Theme & Readability (High Contrast)

### Look & feel (`Main.java`)
- Default LAF is **Metal** (`UIManager.getCrossPlatformLookAndFeelClassName()`), with fallback to **system** LAF if Metal fails — reduces GTK/Qt ignoring custom input colours.

### Colour tokens (`Theme.java`)
| Constant | Role |
|----------|------|
| `COLOR_BG_INPUT` | White surfaces for editable inputs |
| `COLOR_BG_INPUT_DISABLED` | Light grey when disabled |
| `COLOR_TEXT_PRIMARY` | Near-black `(30,30,30)` on light backgrounds |
| `COLOR_TEXT_DISABLED` | Muted but readable grey |
| `COLOR_SELECTION_LIGHT` | Pale selection tint (lists/combos/tables); text stays primary dark |

Legacy palette kept: **`BG`**, **`SURFACE`**, **`TEXT`**, **`TEXT_MUTED`**, **`FAST_BLUE`**, sidebar colours, etc.

### Component styling
- **`styleTextField` / `styleTextArea`**: `Basic*UI`; sync **enabled/disabled** bg/fg/caret; selection highlight uses `COLOR_SELECTION_LIGHT`.
- **`styleComboBox`**: Custom **`ListCellRenderer`**; **`applyComboEditableColors`** on enabled; optional editor **`JTextField`** gets `styleTextField` (max-height rule skipped under **`JComboBox`**).
- **`styleSpinner`**: **`polishSpinnerButtons`** — **`BasicButtonUI`** + light bg + **`COLOR_TEXT_PRIMARY`** on +/- buttons.
- **`styleList`**: Same contrast rules + selected row styling.
- **`styleTable`** / **`applyFlatTableHeader`**: White-style header strip, **`fillsViewportHeight(false)`**, selection uses **`COLOR_SELECTION_LIGHT`**.

### Global `UIManager` defaults
- **`installGlobalDefaults()`** maps text fields, password, textarea, spinner, combo, table, buttons to **`COLOR_*`** where relevant.

---

## Useful Theme Helpers

- `wrapNaturalHeight(JComponent)` — prevents vertical stretching in **`BoxLayout`**
- `wrapCentered(JComponent)` — centers a form card in **`GridBagLayout`**
- `createFormCardShell()` — bordered white panel for forms
- `constrainFormFieldWidth`, `constrainComboWidth`, `constrainSpinnerWidth`
- `dashboardHeaderSeparator`, `createDashboardQuickLinksShell`
- `buildActionButtonGrid`, `normalizeDashboardActionButtons`

---

## Files Commonly Touched in This Stretch

- **`Main_implementation/src/Main.java`** — LAF + `Theme.installGlobalDefaults()`
- **`Main_implementation/src/view/Theme.java`** — full design tokens + styling API
- **Dashboards**: `StudentDashboardFrame`, `AdminDashboardFrame`, `SupervisorDashboardFrame`
- **`LoginFrame`, `NotificationFrame`, `WorkspacePanel`**
- **Forms / modules**: e.g. `SubmitProposalFrame`, `ComplaintFrame`, `PeerReviewFrame`, `SupervisionRequestFrame`, `DocumentUploadFrame`, `ProgressLogFrame`, `ProjectPhaseFrame`, `GradingFrame`, `GroupManagementFrame`, … (most use `Theme.*` helpers)
- **`IdeaBrowsingFrame.java`** (new)

---

## Constraints Preserved

- **Java Swing only** (no JavaFX).
- Business logic and controllers/models largely **unchanged**; work was **UI/layout/theming**.
- No reliance on **`null`** layout / **`setBounds`** for main screens.

---

## Quick Build / Run

```bash
cd /home/mahmedasim/Desktop/test-sda-proj/Main_implementation/src
javac Main.java model/*.java controller/*.java util/*.java view/*.java
java Main
```
