# NomadPlan UI Modernization Design

**Date:** 2026-03-05
**Status:** Approved
**Approach:** Staged FlatLaf-First (Approach A)

## Overview

Modernize ProjectLibre's Java Swing UI from its current dated appearance to a clean, modern 2026 aesthetic, rebranded as **NomadPlan**. Staged approach: theme-level refresh MVP first, then iterative component-level improvements.

**Brand identity:** Modern & Minimal — clean whites/grays, teal accent, sans-serif fonts, flat icons. Think Notion/Linear aesthetic.

**License:** CPAL removed. Full rebrand to NomadPlan with no ProjectLibre attribution requirements.

## Stages

| Stage | Deliverable | Gate |
|-------|------------|------|
| **1a** | FlatLaf integration + NomadPlan light theme | App compiles, launches, all views render in light theme |
| **1b** | Dark theme + toggle | Toggle works, both themes pass visual review |
| **1c** | Rebrand (strings + assets) | "ProjectLibre" not visible anywhere in UI |
| **1d** | Gantt bar + component color updates | Gantt, tables, charts use new palette in both themes |
| **1e** | Ribbon color updates | Ribbon matches new palette, no hardcoded colors remain |
| **2** | Icon refresh, dialog layout polish, Gantt bar style refinement | Consistent modern icon set, improved dialog spacing |
| **3** | Structural UI overhaul (ribbon replacement, docking, animations) | TBD |

## Section 1: FlatLaf Integration & Theme System

### Changes

- Remove `LafManagerImpl.java`'s platform-native LAF logic (Windows/Mac native, Metal on Linux)
- Add FlatLaf 3.x JAR to `projectlibre_contrib/lib/`
- Create `NomadPlanTheme.java` with light and dark variants
- Create `NomadPlanColors.java` singleton — single source of truth for all custom-painted components

### NomadPlan Light Palette

| Role | Color | Usage |
|------|-------|-------|
| Background | `#FFFFFF` | Main panels, dialogs |
| Surface | `#F8F9FA` | Sidebar, headers, table alternating rows |
| Border | `#E2E8F0` | Separators, panel borders |
| Text Primary | `#1A202C` | Headings, labels |
| Text Secondary | `#64748B` | Descriptions, inactive text |
| Accent | `#0D9488` (teal-600) | Selected tabs, active buttons, links |
| Accent Hover | `#0F766E` (teal-700) | Hover states |
| Success | `#16A34A` | On-track indicators |
| Warning | `#D97706` | At-risk indicators |
| Error | `#DC2626` | Critical path, overdue |

### NomadPlan Dark Palette

| Role | Color | Usage |
|------|-------|-------|
| Background | `#1E1E2E` | Main panels |
| Surface | `#282A36` | Headers, sidebars |
| Border | `#3B3F51` | Separators |
| Text Primary | `#CDD6F4` | Headings |
| Text Secondary | `#6C7086` | Secondary text |
| Accent | `#2DD4BF` (teal-400) | Active elements |
| Success | `#4ADE80` | On-track |
| Warning | `#FBBF24` | At-risk |
| Error | `#F87171` | Critical/overdue |

### Architecture

1. `LafManagerImpl.initLookAndFeel()` → calls `FlatLaf.setup()` with NomadPlan theme
2. Theme preference stored via `java.util.prefs.Preferences` — persists across sessions
3. `FlatLaf.setGlobalExtraDefaults()` applies NomadPlan colors to all standard Swing components
4. Custom-painted components (Gantt, Ribbon) read from `NomadPlanColors` singleton
5. Theme toggle calls `SwingUtilities.updateComponentTreeUI()` + repaints custom components

### Flamingo Ribbon Impact

`ProjectLibreRibbonUI.java` (2,384 lines) does heavy custom Graphics2D painting with ~20 hardcoded color values. These must be refactored to read from `NomadPlanColors`. Gradient fills simplified to flat fills matching the modern aesthetic.

## Section 2: Rebrand — ProjectLibre to NomadPlan

### Strategy

~15,385 occurrences of "ProjectLibre" in source. NOT all need changing:

| Category | Action | Count (est.) |
|----------|--------|-------------|
| User-visible strings | Replace → "NomadPlan" | ~200 |
| Package/class names (`com.projectlibre1.*`) | Leave as-is | ~14,000+ |
| Comments/license headers | Update copyright | ~1,000 |

### Key Files

1. **`client.properties`** + 30+ localized variants
   - `Text.ApplicationTitle=NomadPlan`
   - `Text.ShortTitle=NomadPlan`
   - `AboutDialog.copyright=Copyright 2025-2026 NomadPlan. All Rights Reserved`

2. **`build.properties`** — version name, JAR name, installer product name

3. **`AboutDialog.java`** — new logo, new copyright, new URL

4. **Image assets to replace:**
   - `projectlibre-logo.png` → `nomadplan-logo.png`
   - `projectlibre-logo-whitebg.png` → `nomadplan-logo-whitebg.png`
   - `projectlibre-application.png` → `nomadplan-application.png`
   - `projectlibre.ico` → `nomadplan.ico`
   - `images.properties` — update icon key mappings

5. **Installer configs:**
   - `projectlibre.desktop` (Linux)
   - `projectlibre.wxs` (Windows MSI)
   - `build.xml` jpackage targets

6. **Window title** — `GraphicManager.java`, `MainFrame`, `MainRibbonFrame`

7. **License dialog** — remove CPAL acceptance, update to new license text

### Logo

Placeholder text-based "NomadPlan" in brand teal initially. Proper logo design is a separate creative task.

## Section 3: Gantt Chart & Component Visual Updates

### Gantt Bar Colors

| Element | Current | New (Light) | New (Dark) |
|---------|---------|-------------|------------|
| Normal task | `#FFFFB0` yellow | `#0D9488` teal | `#2DD4BF` |
| Summary bar | Black | `#334155` slate | `#94A3B8` |
| Milestone | Black diamond | `#0D9488` teal | `#2DD4BF` |
| Progress fill | Black | `#0F766E` dark teal | `#14B8A6` |
| Critical path | Red | `#DC2626` | `#F87171` |
| Baseline bars | Gray | `#E2E8F0` | `#3B3F51` |
| Non-working days | Dot pattern | `#F1F5F9` subtle | Subtle stripe |
| Dependency links | Black | `#64748B` secondary | `#6C7086` |

All colors sourced from `NomadPlanColors` singleton. `BarFormat`/`BarStyles` already support color config.

### Spreadsheet/Table Views

- FlatLaf handles selection color, alternating rows, headers automatically
- Custom cell renderers updated to read from UIManager defaults
- Row height increased to ~28px for breathing room

### Dialogs

- FlatLaf modernizes all standard components automatically
- `AbstractDialog` base class: updated padding/margins
- No structural redesign in Stage 1

### Charts (JFreeChart)

- Create `NomadPlanChartTheme` using JFreeChart's theme API
- Match chart colors to new palette

## Section 4: Testing Strategy

### Layer 1: Build Verification (Automated)

- `ant dist` must succeed after every change
- New `ant verify` target: headless smoke test
  - Instantiates key UI components in headless mode
  - Verifies no NPE from missing icons/resources
  - Checks all resource bundles load
  - Validates `NomadPlanColors` returns non-null for all roles in both themes

### Layer 2: Visual Regression Screenshots (Semi-automated)

- `ScreenshotTestHarness` utility:
  - Launches app in both themes
  - Opens each major view (Gantt, Resource Sheet, Calendar, Network, WBS)
  - Opens key dialogs (About, Task Info, Resource Info, Project Properties)
  - Captures screenshots via component painting to `BufferedImage`
  - Saves to `test-output/screenshots/{theme}/{viewname}.png`
- Human reviews before/after diffs

### Layer 3: Manual Test Checklist (Stage 1)

- [ ] App launches in light theme
- [ ] App launches in dark theme
- [ ] Theme toggle works mid-session without restart
- [ ] All 4 main views render correctly (Gantt, Resources, Calendar, Network)
- [ ] Gantt bars visible and correctly colored in both themes
- [ ] All toolbar/ribbon buttons visible and legible
- [ ] File Open/Save dialogs work
- [ ] Task Info dialog opens, all tabs render
- [ ] Resource Info dialog opens, all tabs render
- [ ] About dialog shows NomadPlan branding
- [ ] Window title shows "NomadPlan"
- [ ] Application icon is NomadPlan
- [ ] Print preview renders correctly
- [ ] No yellow/outdated colors visible anywhere
- [ ] Dependency lines visible in both themes
- [ ] Critical path highlighting works
- [ ] Text readable in both themes (contrast check)
- [ ] Linux: app launches and renders
- [ ] macOS: app launches and renders

## Key Files Reference

### Files to Create
- `projectlibre_ui/src/com/projectlibre1/theme/NomadPlanTheme.java`
- `projectlibre_ui/src/com/projectlibre1/theme/NomadPlanColors.java`
- `projectlibre_ui/src/com/projectlibre1/theme/NomadPlanChartTheme.java`
- NomadPlan logo/icon assets (placeholder initially)
- `ScreenshotTestHarness.java` (test utility)

### Files to Modify
- `projectlibre_ui/src/com/projectlibre1/pm/graphic/laf/LafManagerImpl.java` — FlatLaf init
- `projectlibre_ui/src/com/projectlibre/ui/ribbon/ProjectLibreRibbonUI.java` — extract colors
- `projectlibre_ui/src/com/projectlibre1/pm/graphic/frames/GraphicManager.java` — theme toggle, title
- `projectlibre_ui/src/com/projectlibre1/dialog/AboutDialog.java` — rebrand
- `projectlibre_ui/src/com/projectlibre1/dialog/LicenseDialog.java` — remove CPAL
- `projectlibre_core/src/com/projectlibre1/graphic/configuration/shape/Colors.java` — default updates
- `projectlibre_core/src/com/projectlibre1/strings/client.properties` + 30 localized variants
- `projectlibre_ui/src/com/projectlibre1/pm/graphic/images/images.properties` — icon mappings
- `projectlibre_build/build.properties` — product name, version
- `projectlibre_build/build.xml` — add verify target, update jpackage names
- Installer configs (`.desktop`, `.wxs`, jpackage args)

### Dependencies to Add
- `projectlibre_contrib/lib/flatlaf-3.x.jar`
