# Stage 3: Visual Polish Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Fix hardcoded colors for dark mode, add rounded Gantt bar corners, and complete vector icon coverage.

**Architecture:** Replace ~8 hardcoded `Color.*` references with `NomadPlanColors.*` calls. Add rounded-corner support to `PredefinedShape.toGeneralPath()` for rectangle-based Gantt bars. Add ~20 `PAINTERS.put()` entries and ~8 new painter methods to `ModernIcons.java`.

**Tech Stack:** Java Swing, Java2D, FlatLaf, NomadPlanColors theme system

---

## Task 1: Fix Hardcoded Colors in Spreadsheet Renderers

**Files:**
- Modify: `projectlibre_ui/src/com/projectlibre1/pm/graphic/spreadsheet/renderer/SpreadSheetColumnHeaderRenderer.java:106-120`
- Modify: `projectlibre_ui/src/com/projectlibre1/pm/graphic/spreadsheet/renderer/CellUtility.java:105-106`
- Modify: `projectlibre_ui/src/com/projectlibre1/pm/graphic/spreadsheet/common/SpreadSheetCorner.java:95-96`

**Step 1: SpreadSheetColumnHeaderRenderer — replace 4 Color.BLACK/LIGHT_GRAY**

Add import: `import com.projectlibre1.theme.NomadPlanColors;`

Lines 106-120 become:
```java
if (Environment.isNewLaf()) {
    component.setForeground (NomadPlanColors.textPrimary());
    component.setBackground(isSelected ? GraphicManager.getInstance().getLafManager().getSelectedBackgroundColor() : GraphicManager.getInstance().getLafManager().getUnselectedBackgroundColor());
} if (Environment.isMac()){
    component.setForeground (isSelected ? NomadPlanColors.textPrimary() : table.getTableHeader().getForeground());
    component.setBackground(isSelected ? GraphicManager.getInstance().getLafManager().getSelectedBackgroundColor() : GraphicManager.getInstance().getLafManager().getUnselectedBackgroundColor());
} else {
    component.setForeground (isSelected ? NomadPlanColors.textPrimary() : table.getTableHeader().getForeground());
    component.setBackground(isSelected ? GraphicManager.getInstance().getLafManager().getSelectedBackgroundColor() : table.getTableHeader ().getBackground());
}
```

Line 120: `new LineBorder(Color.LIGHT_GRAY)` → `new LineBorder(NomadPlanColors.border())`

**Step 2: CellUtility — replace Color.GRAY for disabled cells**

Line 106: `component.setForeground(Color.GRAY);` → `component.setForeground(com.projectlibre1.theme.NomadPlanColors.textSecondary());`

**Step 3: SpreadSheetCorner — replace Color.LIGHT_GRAY border**

Line 96: `new LineBorder(Color.LIGHT_GRAY)` → `new LineBorder(com.projectlibre1.theme.NomadPlanColors.border())`

**Step 4: Build and verify**

```bash
cd projectlibre_build && ant clean dist && ant fatjar
```

---

## Task 2: Fix Hardcoded Colors in GanttRenderer

**Files:**
- Modify: `projectlibre_ui/src/com/projectlibre1/pm/graphic/gantt/GanttRenderer.java:602,611`

**Step 1: Replace project start/status date line colors**

Line 602: `Color.GRAY` → `NomadPlanColors.textSecondary()` (project start dashed line)
Line 611: `Color.GREEN` → `NomadPlanColors.accent()` (status date dotted line)

**Step 2: Build and verify**

---

## Task 3: Add Rounded Corners to Gantt Bars

**Files:**
- Modify: `projectlibre_core/src/com/projectlibre1/graphic/configuration/shape/PredefinedShape.java:71-100`

**Step 1: Add rounded-corner support to toGeneralPath()**

The `toGeneralPath()` method uses `points` (simple coordinate array) for rectangular shapes. We add a `rounded` flag to rect-based shapes and generate a `RoundRectangle2D`-based path when set.

Add field and constructor parameter:
```java
private boolean rounded = false;
```

Modify `toGeneralPath()` — when `rounded && points != null`, detect that it's a rectangle and use `java.awt.geom.RoundRectangle2D` to produce a rounded shape instead of straight `lineTo()` calls. Arc radius = `Math.min(3.0, vScale * 0.15)` for subtle rounding.

Mark all rect-based shapes as rounded: `FULL_HEIGHT`, `HALF_HEIGHT_*`, `QUARTER_HEIGHT_CENTER`.

**Step 2: Build and verify Gantt bars have subtle rounded corners**

---

## Task 4: Complete Vector Icon Coverage

**Files:**
- Modify: `projectlibre_ui/src/com/projectlibre1/theme/ModernIcons.java`

**Step 1: Add PAINTERS.put() entries for remaining bitmap-only keys**

```java
// Spreadsheet tree icons
PAINTERS.put("spreadsheet.leaf.icon", ModernIcons::paintLeaf);
PAINTERS.put("spreadsheet.emptyleaf.icon", ModernIcons::paintLeaf);
PAINTERS.put("spreadsheet.collapsed.icon", ModernIcons::paintCollapse);
PAINTERS.put("spreadsheet.expanded.icon", ModernIcons::paintExpand);

// Timescale zoom
PAINTERS.put("timescale.zoomIn.icon", ModernIcons::paintZoomIn);
PAINTERS.put("timescale.zoomOut.icon", ModernIcons::paintZoomOut);

// Menu items missing vector
PAINTERS.put("menu.scrollToTask", ModernIcons::paintScrollToTask);
PAINTERS.put("menu.import", ModernIcons::paintImport);
PAINTERS.put("menu.export", ModernIcons::paintExport);
PAINTERS.put("menu.replace", ModernIcons::paintReplace);
PAINTERS.put("menu.PDF", ModernIcons::paintPdf);

// Print preview extras
PAINTERS.put("print.print", ModernIcons::paintPrint);
PAINTERS.put("print.zoomIn", ModernIcons::paintZoomIn);
PAINTERS.put("print.zoomOut", ModernIcons::paintZoomOut);
PAINTERS.put("print.zoomReset", ModernIcons::paintZoomReset);
PAINTERS.put("print.back", ModernIcons::paintLeftArrow);
PAINTERS.put("print.forward", ModernIcons::paintRightArrow);
PAINTERS.put("print.up", ModernIcons::paintArrowUp);
PAINTERS.put("print.first", ModernIcons::paintStepBack);
PAINTERS.put("print.last", ModernIcons::paintStepForward);
PAINTERS.put("print.PDF", ModernIcons::paintPdf);

// Menu24 extras
PAINTERS.put("menu24.delegateTasks", ModernIcons::paintIndicatorDelegated);
PAINTERS.put("menu24.import", ModernIcons::paintImport);
PAINTERS.put("menu24.export", ModernIcons::paintExport);

// Misc
PAINTERS.put("format.other", ModernIcons::paintFormatOther);
```

**Step 2: Add ~8 new painter methods**

- `paintLeaf` — small filled circle (tree leaf node)
- `paintImport` — arrow pointing into folder
- `paintExport` — arrow pointing out of folder
- `paintReplace` — two curved arrows (swap)
- `paintZoomReset` — magnifier with "1:1"
- `paintStepBack` — double left chevron (|<<)
- `paintStepForward` — double right chevron (>>|)
- `paintFormatOther` — three horizontal sliders

**Step 3: Build and verify**

---

## Task 5: Build, Test, Verify

```bash
export JAVA_HOME="/c/Program Files/Java/jdk-25.0.2"
export PATH="$JAVA_HOME/bin:/c/tools/apache-ant-1.10.15/bin:$PATH"
cd projectlibre_build
ant clean dist && ant fatjar
java -jar packages/nomadplan-1.9.8.jar
```

Visual checks:
- [ ] Spreadsheet column headers readable in dark mode
- [ ] Disabled cells have visible (but muted) text in dark mode
- [ ] Gantt bars have subtle rounded corners
- [ ] Gantt project start/status date lines use theme colors
- [ ] All toolbar/menu icons render as vectors
- [ ] Tree expand/collapse icons in spreadsheet are vectors
- [ ] Print preview icons are vectors
- [ ] Run: `powershell -File screenshots/full-test.ps1` — 19/19 pass
