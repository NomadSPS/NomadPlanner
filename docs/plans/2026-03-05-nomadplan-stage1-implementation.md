# NomadPlan Stage 1 Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Transform ProjectLibre into NomadPlan with modern FlatLaf theme (light + dark), full rebrand, modernized Gantt/component colors, and ribbon color extraction.

**Architecture:** Replace the native platform LAF with FlatLaf, create a `NomadPlanColors` singleton as the single source of truth for all custom-painted components, update Gantt bar colors via `view.xml`, and rebrand all user-visible strings/assets from "ProjectLibre" to "NomadPlan".

**Tech Stack:** Java 21, Swing, FlatLaf 3.5.4, Flamingo Ribbon, Apache Ant, JFreeChart

---

## Prerequisites

Before starting, download FlatLaf JAR:

```bash
cd projectlibre_contrib/lib
curl -L -O https://repo1.maven.org/maven2/com/formdev/flatlaf/3.5.4/flatlaf-3.5.4.jar
```

Verify the build works before any changes:

```bash
cd projectlibre_build
ant clean dist
```

---

## Task 1: Add FlatLaf JAR and Verify Build

**Files:**
- Add: `projectlibre_contrib/lib/flatlaf-3.5.4.jar`

**Step 1: Download FlatLaf**

```bash
cd projectlibre_contrib/lib
curl -L -O https://repo1.maven.org/maven2/com/formdev/flatlaf/3.5.4/flatlaf-3.5.4.jar
```

**Step 2: Verify build still compiles**

```bash
cd projectlibre_build
ant clean dist
```

Expected: BUILD SUCCESSFUL. The JAR is auto-included via `compile-no_contrib_built.class.path` which globs `../projectlibre_contrib/lib/**/*.jar`.

**Step 3: Commit**

```bash
git add projectlibre_contrib/lib/flatlaf-3.5.4.jar
git commit -m "deps: add FlatLaf 3.5.4 for modern Look & Feel"
```

---

## Task 2: Create NomadPlanColors Singleton

**Files:**
- Create: `projectlibre_ui/src/com/projectlibre1/theme/NomadPlanColors.java`

**Step 1: Create the theme directory**

```bash
mkdir -p projectlibre_ui/src/com/projectlibre1/theme
```

**Step 2: Write NomadPlanColors.java**

```java
package com.projectlibre1.theme;

import java.awt.Color;

/**
 * Single source of truth for all NomadPlan UI colors.
 * Custom-painted components (Gantt, Ribbon) read from here.
 * Standard Swing components get colors via FlatLaf properties.
 */
public class NomadPlanColors {
    private static boolean darkMode = false;

    // --- Light palette ---
    private static final Color L_BACKGROUND     = Color.WHITE;
    private static final Color L_SURFACE         = new Color(0xF8F9FA);
    private static final Color L_BORDER          = new Color(0xE2E8F0);
    private static final Color L_TEXT_PRIMARY     = new Color(0x1A202C);
    private static final Color L_TEXT_SECONDARY   = new Color(0x64748B);
    private static final Color L_ACCENT          = new Color(0x0D9488);
    private static final Color L_ACCENT_HOVER    = new Color(0x0F766E);
    private static final Color L_SUCCESS         = new Color(0x16A34A);
    private static final Color L_WARNING         = new Color(0xD97706);
    private static final Color L_ERROR           = new Color(0xDC2626);

    // --- Dark palette ---
    private static final Color D_BACKGROUND     = new Color(0x1E1E2E);
    private static final Color D_SURFACE         = new Color(0x282A36);
    private static final Color D_BORDER          = new Color(0x3B3F51);
    private static final Color D_TEXT_PRIMARY     = new Color(0xCDD6F4);
    private static final Color D_TEXT_SECONDARY   = new Color(0x6C7086);
    private static final Color D_ACCENT          = new Color(0x2DD4BF);
    private static final Color D_ACCENT_HOVER    = new Color(0x14B8A6);
    private static final Color D_SUCCESS         = new Color(0x4ADE80);
    private static final Color D_WARNING         = new Color(0xFBBF24);
    private static final Color D_ERROR           = new Color(0xF87171);

    // --- Gantt-specific colors ---
    // Light
    private static final Color L_GANTT_TASK      = new Color(0x0D9488);  // teal
    private static final Color L_GANTT_SUMMARY   = new Color(0x334155);  // slate
    private static final Color L_GANTT_MILESTONE = new Color(0x0D9488);  // teal
    private static final Color L_GANTT_PROGRESS  = new Color(0x0F766E);  // dark teal
    private static final Color L_GANTT_CRITICAL  = new Color(0xDC2626);  // red
    private static final Color L_GANTT_BASELINE  = new Color(0xE2E8F0);  // border gray
    private static final Color L_GANTT_LINK      = new Color(0x64748B);  // secondary
    private static final Color L_GANTT_NONWORK   = new Color(0xF1F5F9);  // very light
    // Dark
    private static final Color D_GANTT_TASK      = new Color(0x2DD4BF);
    private static final Color D_GANTT_SUMMARY   = new Color(0x94A3B8);
    private static final Color D_GANTT_MILESTONE = new Color(0x2DD4BF);
    private static final Color D_GANTT_PROGRESS  = new Color(0x14B8A6);
    private static final Color D_GANTT_CRITICAL  = new Color(0xF87171);
    private static final Color D_GANTT_BASELINE  = new Color(0x3B3F51);
    private static final Color D_GANTT_LINK      = new Color(0x6C7086);
    private static final Color D_GANTT_NONWORK   = new Color(0x252537);

    public static boolean isDarkMode() { return darkMode; }
    public static void setDarkMode(boolean dark) { darkMode = dark; }

    public static Color background()    { return darkMode ? D_BACKGROUND : L_BACKGROUND; }
    public static Color surface()       { return darkMode ? D_SURFACE : L_SURFACE; }
    public static Color border()        { return darkMode ? D_BORDER : L_BORDER; }
    public static Color textPrimary()   { return darkMode ? D_TEXT_PRIMARY : L_TEXT_PRIMARY; }
    public static Color textSecondary() { return darkMode ? D_TEXT_SECONDARY : L_TEXT_SECONDARY; }
    public static Color accent()        { return darkMode ? D_ACCENT : L_ACCENT; }
    public static Color accentHover()   { return darkMode ? D_ACCENT_HOVER : L_ACCENT_HOVER; }
    public static Color success()       { return darkMode ? D_SUCCESS : L_SUCCESS; }
    public static Color warning()       { return darkMode ? D_WARNING : L_WARNING; }
    public static Color error()         { return darkMode ? D_ERROR : L_ERROR; }

    public static Color ganttTask()      { return darkMode ? D_GANTT_TASK : L_GANTT_TASK; }
    public static Color ganttSummary()   { return darkMode ? D_GANTT_SUMMARY : L_GANTT_SUMMARY; }
    public static Color ganttMilestone() { return darkMode ? D_GANTT_MILESTONE : L_GANTT_MILESTONE; }
    public static Color ganttProgress()  { return darkMode ? D_GANTT_PROGRESS : L_GANTT_PROGRESS; }
    public static Color ganttCritical()  { return darkMode ? D_GANTT_CRITICAL : L_GANTT_CRITICAL; }
    public static Color ganttBaseline()  { return darkMode ? D_GANTT_BASELINE : L_GANTT_BASELINE; }
    public static Color ganttLink()      { return darkMode ? D_GANTT_LINK : L_GANTT_LINK; }
    public static Color ganttNonWork()   { return darkMode ? D_GANTT_NONWORK : L_GANTT_NONWORK; }
}
```

**Step 3: Verify build**

```bash
cd projectlibre_build && ant clean dist
```

Expected: BUILD SUCCESSFUL

**Step 4: Commit**

```bash
git add projectlibre_ui/src/com/projectlibre1/theme/NomadPlanColors.java
git commit -m "feat: add NomadPlanColors singleton with light/dark palettes"
```

---

## Task 3: Integrate FlatLaf into LafManagerImpl (Stage 1a)

**Files:**
- Modify: `projectlibre_ui/src/com/projectlibre1/pm/graphic/laf/LafManagerImpl.java`

**Step 1: Update LafManagerImpl to use FlatLaf**

Replace the `getPlaf()` method and add FlatLaf imports. The key changes:
- Import `com.formdev.flatlaf.FlatLightLaf` and `com.formdev.flatlaf.FlatDarkLaf`
- Import `com.projectlibre1.theme.NomadPlanColors`
- Import `java.util.prefs.Preferences`
- Replace platform-native LAF selection with FlatLaf setup
- Add `toggleTheme()` method
- Update `getSelectedBackgroundColor()` and `getUnselectedBackgroundColor()` to use NomadPlanColors

Replace the entire `getPlaf()` method (lines ~68-82) with:

```java
private static final String PREF_DARK_MODE = "nomadplan.darkMode";

public LookAndFeel getPlaf() {
    if (plaf == null) {
        try {
            Preferences prefs = Preferences.userNodeForPackage(LafManagerImpl.class);
            boolean dark = prefs.getBoolean(PREF_DARK_MODE, false);
            NomadPlanColors.setDarkMode(dark);
            if (dark) {
                FlatDarkLaf.setup();
            } else {
                FlatLightLaf.setup();
            }
            applyNomadPlanDefaults();
            plaf = UIManager.getLookAndFeel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (graphicManager != null) SwingUtilities.updateComponentTreeUI(graphicManager.getContainer());
    }
    return plaf;
}

public void toggleTheme() {
    boolean dark = !NomadPlanColors.isDarkMode();
    NomadPlanColors.setDarkMode(dark);
    Preferences prefs = Preferences.userNodeForPackage(LafManagerImpl.class);
    prefs.putBoolean(PREF_DARK_MODE, dark);
    try {
        if (dark) {
            FlatDarkLaf.setup();
        } else {
            FlatLightLaf.setup();
        }
        applyNomadPlanDefaults();
        plaf = UIManager.getLookAndFeel();
    } catch (Exception e) {
        e.printStackTrace();
    }
    if (graphicManager != null) {
        SwingUtilities.updateComponentTreeUI(graphicManager.getContainer());
        graphicManager.getContainer().repaint();
    }
}

private void applyNomadPlanDefaults() {
    UIManager.put("Component.focusColor", NomadPlanColors.accent());
    UIManager.put("Component.focusWidth", 1);
    UIManager.put("Button.arc", 6);
    UIManager.put("Component.arc", 6);
    UIManager.put("TextComponent.arc", 6);
    UIManager.put("ScrollBar.thumbArc", 999);
    UIManager.put("ScrollBar.thumbInsets", new javax.swing.plaf.InsetsUIResource(2, 2, 2, 2));
    UIManager.put("TabbedPane.selectedBackground", NomadPlanColors.background());
    UIManager.put("TabbedPane.underlineColor", NomadPlanColors.accent());
    UIManager.put("Table.alternateRowColor", NomadPlanColors.surface());
}
```

Update `getSelectedBackgroundColor()` and `getUnselectedBackgroundColor()`:

```java
public Color getSelectedBackgroundColor() {
    return NomadPlanColors.accent();
}
public Color getUnselectedBackgroundColor() {
    return NomadPlanColors.surface();
}
```

Add imports at top of file:

```java
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.projectlibre1.theme.NomadPlanColors;
import java.util.prefs.Preferences;
```

**Step 2: Add toggleTheme to LafManager interface**

In `projectlibre_ui/src/com/projectlibre1/pm/graphic/laf/LafManager.java`, add:

```java
public abstract void toggleTheme();
```

**Step 3: Verify build**

```bash
cd projectlibre_build && ant clean dist
```

Expected: BUILD SUCCESSFUL

**Step 4: Manual test — launch the app**

```bash
java -jar projectlibre_build/packages/projectlibre-1.9.8.jar
```

Expected: App launches with FlatLaf light theme. Buttons, menus, dialogs should look flat/modern instead of native Windows/Metal.

**Step 5: Commit**

```bash
git add projectlibre_ui/src/com/projectlibre1/pm/graphic/laf/LafManagerImpl.java
git add projectlibre_ui/src/com/projectlibre1/pm/graphic/laf/LafManager.java
git commit -m "feat(stage-1a): integrate FlatLaf with NomadPlan light/dark theme support"
```

---

## Task 4: Add Theme Toggle to View Menu (Stage 1b)

**Files:**
- Modify: `projectlibre_ui/src/com/projectlibre1/pm/graphic/frames/GraphicManager.java`
- Modify: `projectlibre_core/src/com/projectlibre1/strings/client.properties`

**Step 1: Add menu string**

In `client.properties`, add:

```properties
Menu.toggleDarkMode=Toggle Dark Mode
```

**Step 2: Add toggle action in GraphicManager**

Locate the menu initialization section in `GraphicManager.java`. Search for where View menu items are added (look for `addViewButtons` or similar). Add a menu item that calls `getLafManager().toggleTheme()`.

The exact insertion point requires reading GraphicManager.java's menu setup. The toggle should be wired as:

```java
// In the method that builds the View menu/ribbon band
JCheckBoxMenuItem darkModeItem = new JCheckBoxMenuItem(Messages.getString("Menu.toggleDarkMode"));
darkModeItem.setSelected(NomadPlanColors.isDarkMode());
darkModeItem.addActionListener(e -> {
    ((LafManagerImpl) getLafManager()).toggleTheme();
    darkModeItem.setSelected(NomadPlanColors.isDarkMode());
});
```

If adding to the ribbon is complex, an alternative is adding it to the File/application menu which is simpler. Search for `addApplicationMenuEntries` or the ribbon application menu setup in GraphicManager.java.

**Step 3: Verify build and test toggle**

```bash
cd projectlibre_build && ant clean dist
java -jar packages/projectlibre-1.9.8.jar
```

Expected: Toggle Dark Mode menu item appears. Clicking it switches between light and dark themes. Preference persists across restarts.

**Step 4: Commit**

```bash
git add -A
git commit -m "feat(stage-1b): add dark mode toggle to view menu"
```

---

## Task 5: Rebrand Strings — client.properties (Stage 1c)

**Files:**
- Modify: `projectlibre_core/src/com/projectlibre1/strings/client.properties`
- Modify: All `client_*.properties` locale files (30+)

**Step 1: Update English client.properties**

Find and replace these specific keys:

```
Text.ApplicationTitle=NomadPlan
Text.ShortTitle=NomadPlan
Open_Text.ApplicationTitle=NomadPlan
Open_Text.ShortTitle=NomadPlan
AboutDialog.copyright=Copyright 2025-2026 NomadPlan. All Rights Reserved
```

**Step 2: Update all localized client_*.properties**

For each `client_*.properties` file, update any overrides of `Text.ApplicationTitle`, `Text.ShortTitle`, `Open_Text.ApplicationTitle`, `Open_Text.ShortTitle`. Most locale files may NOT override these keys (they fall back to the English default), but check each one.

Use this command to find which locale files override these keys:

```bash
grep -rl "Text.ApplicationTitle\|Text.ShortTitle\|Open_Text.ApplicationTitle\|Open_Text.ShortTitle" projectlibre_core/src/com/projectlibre1/strings/
```

Update any found files.

**Step 3: Update build.properties**

In `projectlibre_build/build.properties`, the version_name and version stay as-is (1.9.8), but if there's a product name property, update it. Currently there is no explicit product name property — the JAR is named `projectlibre.jar` and `projectlibre-${version}.jar` via build.xml. These should be updated:

In `build.xml`, search for `projectlibre` in jar filenames:
- `${dist}/projectlibre.jar` → `${dist}/nomadplan.jar`
- `${packages}/projectlibre-${version}.jar` → `${packages}/nomadplan-${version}.jar`
- Main-Class stays `com.projectlibre1.main.Main` (package name unchanged)

**Step 4: Verify build**

```bash
cd projectlibre_build && ant clean dist
java -jar packages/nomadplan-1.9.8.jar
```

Expected: Window title shows "NomadPlan". About dialog shows "NomadPlan".

**Step 5: Commit**

```bash
git add -A
git commit -m "feat(stage-1c): rebrand user-visible strings from ProjectLibre to NomadPlan"
```

---

## Task 6: Rebrand Image Assets (Stage 1c continued)

**Files:**
- Create: `projectlibre_ui/src/com/projectlibre1/pm/graphic/images/nomadplan-logo.png`
- Create: `projectlibre_ui/src/com/projectlibre1/pm/graphic/images/nomadplan-logo-whitebg.png`
- Create: `projectlibre_ui/src/com/projectlibre1/pm/graphic/images/nomadplan-application.png`
- Modify: `projectlibre_ui/src/com/projectlibre1/pm/graphic/images.properties`
- Modify: `projectlibre_ui/src/com/projectlibre1/dialog/AboutDialog.java`
- Modify: `projectlibre_ui/src/com/projectlibre1/pm/graphic/frames/MainRibbonFrame.java`

**Step 1: Create placeholder logo assets**

Create simple text-based placeholder PNGs. Use Java or a script to generate them. Minimum sizes:
- `nomadplan-logo.png` — 144x31px (CPAL minimum, now our standard)
- `nomadplan-logo-whitebg.png` — 144x31px (white background variant)
- `nomadplan-application.png` — 128x128px (application icon)

For a quick placeholder, create a Java utility that writes the PNGs:

```java
// Run as a one-off script or manually create with any image editor
// The logo should be text "NomadPlan" in teal (#0D9488) on transparent/white background
```

Alternatively, create them with any image editor. The key requirement is that the files exist at the right sizes.

**Step 2: Update images.properties**

Change these keys:

```properties
application.icon=nomadplan-application.png
logo.NomadPlan=nomadplan-logo.png
```

Keep the old `logo.ProjectLibre` key pointing to the new file for backwards compatibility:

```properties
logo.ProjectLibre=nomadplan-logo.png
```

**Step 3: Update AboutDialog.java**

In `projectlibre_ui/src/com/projectlibre1/dialog/AboutDialog.java`:

Change line referencing `logo.ProjectLibre`:
```java
// Old:
JLabel logo = new JLabel(IconManager.getIcon("logo.ProjectLibre"));
// New:
JLabel logo = new JLabel(IconManager.getIcon("logo.NomadPlan"));
```

Change the URL from `http://www.projectlibre.com` to the NomadPlan URL (or remove the click handler for now).

**Step 4: Update MainRibbonFrame.java**

In `projectlibre_ui/src/com/projectlibre1/pm/graphic/frames/MainRibbonFrame.java`:

Change line ~31:
```java
// Old:
appMenuIcon=IconManager.getRibbonIcon("logo.ProjectLibre",144,31);
// New:
appMenuIcon=IconManager.getRibbonIcon("logo.NomadPlan",144,31);
```

**Step 5: Verify build and test**

```bash
cd projectlibre_build && ant clean dist
java -jar packages/nomadplan-1.9.8.jar
```

Expected: NomadPlan logo appears in ribbon and About dialog. Application icon shows NomadPlan icon.

**Step 6: Commit**

```bash
git add -A
git commit -m "feat(stage-1c): rebrand image assets and icon references to NomadPlan"
```

---

## Task 7: Remove CPAL License Dialog (Stage 1c continued)

**Files:**
- Modify: `projectlibre_ui/src/com/projectlibre1/dialog/LicenseDialog.java`

**Step 1: Disable forced license acceptance**

The simplest approach: make `LicenseDialog.showDialog()` return immediately without showing.

In `LicenseDialog.java`, find the `showDialog()` method (line ~86). Change it to:

```java
public static void showDialog(Frame frame, boolean force) {
    // License acceptance no longer required
    return;
}
```

This preserves the class for future use but disables the blocking dialog on first launch.

**Step 2: Verify — app launches without license dialog**

```bash
cd projectlibre_build && ant clean dist
java -jar packages/nomadplan-1.9.8.jar
```

Expected: App launches directly without showing "I Accept" license dialog.

**Step 3: Commit**

```bash
git add projectlibre_ui/src/com/projectlibre1/dialog/LicenseDialog.java
git commit -m "feat(stage-1c): disable CPAL license acceptance dialog"
```

---

## Task 8: Update Gantt Bar Colors via view.xml (Stage 1d)

**Files:**
- Modify: `projectlibre_core/src/com/projectlibre1/configuration/view.xml`
- Modify: `projectlibre_core/src/com/projectlibre1/graphic/configuration/shape/Colors.java`

**Step 1: Add NomadPlan colors to Colors.java**

Add new named colors that can be referenced from view.xml. In `Colors.java`, after the existing custom colors (line ~206), add:

```java
// NomadPlan palette
public static final Color NOMAD_TEAL = new Color(0x0D9488);
public static final Color NOMAD_TEAL_DARK = new Color(0x0F766E);
public static final Color NOMAD_SLATE = new Color(0x334155);
public static final Color NOMAD_ERROR = new Color(0xDC2626);
public static final Color NOMAD_BORDER = new Color(0xE2E8F0);
public static final Color NOMAD_SECONDARY = new Color(0x64748B);
public static final Color NOMAD_SURFACE = new Color(0xF1F5F9);
```

Also add these to the `data` array so they can be looked up by name from view.xml:

```java
// Add to the data array (between existing entries, maintaining alphabetical order or at end)
"NOMAD_TEAL", NOMAD_TEAL,
"NOMAD_TEAL_DARK", NOMAD_TEAL_DARK,
"NOMAD_SLATE", NOMAD_SLATE,
"NOMAD_ERROR", NOMAD_ERROR,
"NOMAD_BORDER", NOMAD_BORDER,
"NOMAD_SECONDARY", NOMAD_SECONDARY,
"NOMAD_SURFACE", NOMAD_SURFACE,
```

**Step 2: Update view.xml bar colors**

In `view.xml`, update the `<format>` elements for Gantt bars. Find and update:

```xml
<!-- Bar.task: BLUE -> NOMAD_TEAL -->
<format id="Bar.task" start="" middle="NOMAD_TEAL FULL_HEIGHT DEFAULT" end="NOMAD_TEAL FULL_HEIGHT FRAMED" layer="200"/>

<!-- Bar.milestone: BLACK -> NOMAD_TEAL -->
<format id="Bar.milestone" start="" middle="NOMAD_TEAL DIAMOND SOLID" end="" layer="200"/>

<!-- Bar.summary: BLACK -> NOMAD_SLATE -->
<format id="Bar.summary" start="NOMAD_SLATE PENTAGON_DOWN SOLID" middle="NOMAD_SLATE HALF_HEIGHT_TOP SOLID" end="NOMAD_SLATE PENTAGON_DOWN SOLID" layer="200"/>

<!-- Bar.critical: RED -> NOMAD_ERROR -->
<format id="Bar.critical" start="" middle="NOMAD_ERROR FULL_HEIGHT DEFAULT" end="NOMAD_ERROR FULL_HEIGHT FRAMED" layer="200"/>

<!-- Bar.baseline: DARK_SLATE_GRAY -> NOMAD_BORDER -->
<format id="Bar.baseline" start="" middle="NOMAD_BORDER FULL_HEIGHT DEFAULT" end="NOMAD_BORDER FULL_HEIGHT SOLID" layer="1001"/>

<!-- Bar.assignment: ORANGE -> NOMAD_TEAL -->
<format id="Bar.assignment" start="" middle="NOMAD_TEAL HALF_HEIGHT_CENTER DEFAULT" end="NOMAD_TEAL HALF_HEIGHT_CENTER FRAMED" layer="200"/>
```

Note: The exact XML attribute format may differ — check the actual view.xml structure. The color name is the first token in the `middle`/`start`/`end` attribute value.

**Step 3: Verify build and test Gantt rendering**

```bash
cd projectlibre_build && ant clean dist
java -jar packages/nomadplan-1.9.8.jar
```

Expected: Open/create a project with tasks. Gantt bars should appear in teal instead of blue/yellow. Summary bars in slate. Critical path in red (error color). Milestones as teal diamonds.

**Step 4: Commit**

```bash
git add -A
git commit -m "feat(stage-1d): update Gantt bar colors to NomadPlan palette"
```

---

## Task 9: Update Spreadsheet Row Colors (Stage 1d continued)

**Files:**
- Modify: `projectlibre_core/src/com/projectlibre1/graphic/configuration/shape/Colors.java`

**Step 1: Update default row colors**

The NORMAL_YELLOW color is used as the default task row background. Update it:

```java
// Old:
public static final Color NORMAL_YELLOW = new Color(0xffffb0);
public static final Color NORMAL_LIGHT_YELLOW = new Color(0xffffe0);
// New:
public static final Color NORMAL_YELLOW = new Color(0xFFFFFF);      // white (FlatLaf handles alternation)
public static final Color NORMAL_LIGHT_YELLOW = new Color(0xF8F9FA); // NomadPlan surface
```

Update multi-project alternating colors:

```java
// Old:
public static final Color MULTIPROJET1 = new Color(0xececec);
public static final Color MULTIPROJET0 = new Color(0xdcecec);
// New:
public static final Color MULTIPROJET1 = new Color(0xF8F9FA);  // surface
public static final Color MULTIPROJET0 = new Color(0xF0FDFA);  // very light teal tint
```

**Step 2: Verify build and test**

```bash
cd projectlibre_build && ant clean dist
java -jar packages/nomadplan-1.9.8.jar
```

Expected: Spreadsheet rows use clean white/light gray alternation instead of yellow.

**Step 3: Commit**

```bash
git add projectlibre_core/src/com/projectlibre1/graphic/configuration/shape/Colors.java
git commit -m "feat(stage-1d): update spreadsheet row colors to clean white/gray palette"
```

---

## Task 10: Extract Hardcoded Colors from ProjectLibreRibbonUI (Stage 1e)

**Files:**
- Modify: `projectlibre_ui/src/com/projectlibre/ui/ribbon/ProjectLibreRibbonUI.java`

This is the largest task. The file is 2,384 lines with ~20 hardcoded color values in custom Graphics2D painting code.

**Step 1: Identify all hardcoded colors**

Search for color patterns in the file:

```bash
grep -n "new Color\|Color\.\|0x[0-9a-fA-F]\|getColor" projectlibre_ui/src/com/projectlibre/ui/ribbon/ProjectLibreRibbonUI.java
```

**Step 2: Replace hardcoded colors with NomadPlanColors references**

Add import at top:
```java
import com.projectlibre1.theme.NomadPlanColors;
```

For each hardcoded color found, replace with the appropriate NomadPlanColors method:

| Pattern | Replacement |
|---------|-------------|
| `new Color(...)` for backgrounds | `NomadPlanColors.background()` or `NomadPlanColors.surface()` |
| `Color.WHITE` in backgrounds | `NomadPlanColors.background()` |
| `Color.BLACK` in text | `NomadPlanColors.textPrimary()` |
| `Color.GRAY` / `Color.LIGHT_GRAY` in borders | `NomadPlanColors.border()` |
| `Color.DARK_GRAY` in text | `NomadPlanColors.textSecondary()` |
| `GradientPaint` fills | Replace with flat fills using `NomadPlanColors.surface()` |
| Selection/active colors | `NomadPlanColors.accent()` |

The exact replacements depend on context. Each `GradientPaint` should be simplified to a flat fill:

```java
// Old:
GradientPaint gp = new GradientPaint(x, y, startColor, x, y+h, endColor);
g2.setPaint(gp);
// New:
g2.setColor(NomadPlanColors.surface());
```

**Step 3: Verify build and test ribbon appearance**

```bash
cd projectlibre_build && ant clean dist
java -jar packages/nomadplan-1.9.8.jar
```

Expected: Ribbon toolbar matches the NomadPlan palette. No hardcoded colors visible. Tab underlines in teal. Flat, clean appearance.

**Step 4: Test dark mode toggle**

Toggle to dark mode. Ribbon should update to dark palette colors.

**Step 5: Commit**

```bash
git add projectlibre_ui/src/com/projectlibre/ui/ribbon/ProjectLibreRibbonUI.java
git commit -m "feat(stage-1e): extract hardcoded ribbon colors to NomadPlanColors"
```

---

## Task 11: Update Installer Configs (Stage 1c cleanup)

**Files:**
- Modify: `projectlibre_build/resources/projectlibre.desktop`
- Modify: `projectlibre_build/resources/wix/projectlibre.wxs` (and variants)
- Modify: `projectlibre_build/build.xml` (jpackage targets)

**Step 1: Update Linux .desktop file**

In `projectlibre.desktop`, change:
- `Name=ProjectLibre` → `Name=NomadPlan`
- `Comment=...ProjectLibre...` → `Comment=...NomadPlan...` (all 30+ language variants)
- `GenericName` entries remain as "Project Management" (generic)
- `Icon=projectlibre` → `Icon=nomadplan`

**Step 2: Update Windows MSI config**

In the `.wxs` files, update Product Name, Description, and icon references from `projectlibre` to `nomadplan`.

**Step 3: Update build.xml jpackage targets**

Search for `jpackage` in build.xml. Update:
- `--name ProjectLibre` → `--name NomadPlan`
- `--vendor "ProjectLibre"` → `--vendor "NomadPlan"`
- `--description` text

**Step 4: Verify build**

```bash
cd projectlibre_build && ant clean dist
```

Expected: BUILD SUCCESSFUL

**Step 5: Commit**

```bash
git add -A
git commit -m "feat(stage-1c): update installer configs with NomadPlan branding"
```

---

## Task 12: Final Stage 1 Verification

**Step 1: Clean build**

```bash
cd projectlibre_build && ant clean fatjar
```

Expected: BUILD SUCCESSFUL, produces `packages/nomadplan-1.9.8.jar`

**Step 2: Run through manual test checklist**

Launch: `java -jar packages/nomadplan-1.9.8.jar`

Verify each item from the design doc Stage 1 checklist:

- [ ] App launches in light theme
- [ ] Window title shows "NomadPlan"
- [ ] About dialog shows NomadPlan branding and logo
- [ ] No "ProjectLibre" text visible anywhere in UI
- [ ] No CPAL license dialog on first launch
- [ ] Gantt bars are teal (not yellow/blue)
- [ ] Summary bars are slate
- [ ] Critical path is red
- [ ] Milestones are teal diamonds
- [ ] Spreadsheet rows are clean white/gray (not yellow)
- [ ] Ribbon toolbar uses NomadPlan colors
- [ ] Toggle to dark mode — entire UI switches
- [ ] All views render in dark mode (Gantt, Resources, Calendar, Network)
- [ ] Gantt bars visible in dark mode
- [ ] Text readable in dark mode
- [ ] Toggle back to light mode — UI switches back
- [ ] Theme preference persists after restart
- [ ] File Open/Save dialogs work
- [ ] Task Info dialog renders
- [ ] Print preview renders

**Step 3: Fix any issues found, commit fixes**

**Step 4: Final commit**

```bash
git add -A
git commit -m "feat: complete NomadPlan Stage 1 - FlatLaf theme, dark mode, rebrand, modernized colors"
```
