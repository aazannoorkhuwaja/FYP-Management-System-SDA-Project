# Theme Specification: Monolithic Serenity

This document defines the visual tokens for the FYP Management Platform, derived from the "Monolithic Serenity" philosophy.

## Typography
We use a high-contrast pairing of editorial serifs and clinical sans-serifs.

- **Display Headings**: `DM Serif Display`
  - *Usage*: Page titles, card numbers, large hero stats.
- **Body & Interface**: `DM Sans`
  - *Usage*: Navigation, labels, descriptions, buttons.
- **Data & System**: `JetBrains Mono`
  - *Usage*: Timestamps, IDs, status codes, terminal logs.

## Color Palette (The "Void & Spark" System)
The palette is dominated by deep neutrals with sharp, monochromatic sparks for status and interaction.

### Base Neutrals
- `--bg`: `#0D0F14` (Deep onyx)
- `--surface`: `#13161E` (Elevated panel)
- `--surface-2`: `#1C2030` (Interactive surface)
- `--border`: `#252A3A` (Subtle divider)
- `--text`: `#E8EAF0` (High readability)
- `--text-muted`: `#7A8099` (Secondary guidance)

### Semantic Sparks
- `--accent`: `#D94F2E` (Vermillion — Action, Primary)
- `--info`: `#3B8FCC` (Steel Blue — Information, Neutral Links)
- `--success`: `#2EA86B` (Emerald — Completed, Approved)
- `--warning`: `#D98C2E` (Ochre — Pending, Attention)
- `--danger`: `#D94F2E` (Vermillion — Overdue, Rejected)

## Layout & Geometry
- **Slab Sidebar**: A monolithic 240px slab with no borders, using role-based top-accent bars.
- **Grid**: 4px base unit. Gaps are typically 16px or 24px.
- **Border Radius**: 4px (Strict, architectural).
- **Shadows**: None. We use depth through border colors and surface shifts.
- **Glassmorphism**: Minimal. Only used for floating modals (`blur(8px)` with semi-opaque surface).

## Component Rhythm
- **Buttons**: Square corners, 1px borders, bold labels in `DM Sans`.
- **Inputs**: Solid backgrounds, no focus shadows, only a bottom border shift to `--accent`.
- **Cards**: Flat surface shifts, no lifted effects.
