# Replace Flamingo Ribbon with Modern Toolbar — Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Replace the Flamingo Ribbon with a standard Swing JMenuBar + JToolBar, letting FlatLaf handle all rendering natively for a professional appearance.

**Architecture:** The app already has a `MainFrameFactory` that branches on `Environment.isRibbonUI()` between `MainRibbonFrame` (JRibbonFrame) and `MainFrame` (JFrame). We'll create a new `ModernMainFrame` extending JFrame with JMenuBar + JToolBar, update the factory to use it, and wire all existing Actions unchanged. The existing `MenuManager`, `ExtMenuFactory`, and action registry stay intact.

**Tech Stack:** Java Swing (JMenuBar, JToolBar, JButton, JToggleButton), FlatLaf L&F, existing Action classes.

---

## Task 1: Create ModernMainFrame

**Files:**
- Create: `projectlibre_ui/src/com/projectlibre1/pm/graphic/frames/ModernMainFrame.java`
- Reference: `projectlibre_ui/src/com/projectlibre1/pm/graphic/frames/MainRibbonFrame.java`

**Step 1: Create the new frame class**

Model it after `MainRibbonFrame` but extend `JFrame` instead of `JRibbonFrame`. Keep the same `FrameHolder` interface.

```java
package com.projectlibre1.pm.graphic.frames;

import java.awt.*;
import javax.swing.*;
import com.projectlibre1.pm.graphic.IconManager;
import com.projectlibre1.util.Environment;

public class ModernMainFrame extends JFrame implements FrameHolder {
    private static final long serialVersionUID = 1L;
    protected GraphicManager graphicManager;

    public ModernMainFrame(String name, String projectUrl, String server)
            throws HeadlessException {
        super(name);
        setIconImage(IconManager.getImage("application.icon"));
        init();
    }

    public GraphicManager getGraphicManager() { return graphicManager; }
    public void setGraphicManager(GraphicManager graphicManager) {
        this.graphicManager = graphicManager;
    }
    public Container getContainer() { return this; }

    public void init() {
        if (Environment.isWindows()) setSize(1024, 768);
        if (Environment.isMac()) setPreferredSize(new Dimension(1280, 768));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                graphicManager.closeApplication();
            }
        });
    }
}
```

**Step 2: Verify it compiles**

Run: `ant compile` from `projectlibre_build/`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add projectlibre_ui/src/com/projectlibre1/pm/graphic/frames/ModernMainFrame.java
git commit -m "feat: add ModernMainFrame (JFrame-based, no ribbon)"
```

---

## Task 2: Create MenuBarFactory

**Files:**
- Create: `projectlibre_ui/src/com/projectlibre1/menu/MenuBarFactory.java`

**Step 1: Create the factory**

This class builds a `JMenuBar` with all menus, using the existing `MenuManager` action registry to look up and wire actions. Each `JMenuItem` gets the Action directly so icons, text, accelerators, and enabled state all work automatically.

```java
package com.projectlibre1.menu;

import java.awt.event.*;
import javax.swing.*;
import com.projectlibre1.pm.graphic.IconManager;
import com.projectlibre1.theme.NomadPlanColors;

public class MenuBarFactory {

    public static JMenuBar create(MenuManager menuManager) {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(createFileMenu(menuManager));
        menuBar.add(createEditMenu(menuManager));
        menuBar.add(createViewMenu(menuManager));
        menuBar.add(createTaskMenu(menuManager));
        menuBar.add(createResourceMenu(menuManager));
        menuBar.add(createProjectMenu(menuManager));
        menuBar.add(createHelpMenu(menuManager));

        // Right-aligned glue + theme toggle
        menuBar.add(Box.createHorizontalGlue());
        JButton themeToggle = new JButton(
            NomadPlanColors.isDarkMode() ? "\u263E" : "\u2600");
        themeToggle.setToolTipText("Toggle dark mode");
        themeToggle.setBorderPainted(false);
        themeToggle.setFocusable(false);
        themeToggle.addActionListener(e -> {
            com.projectlibre1.pm.graphic.frames.GraphicManager.getInstance()
                .getLafManager().toggleTheme();
            themeToggle.setText(NomadPlanColors.isDarkMode() ? "\u263E" : "\u2600");
        });
        menuBar.add(themeToggle);

        return menuBar;
    }

    private static JMenu createFileMenu(MenuManager mm) {
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        addItem(menu, mm, "NewProject", "NewProjectAction", KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        addItem(menu, mm, "OpenProject", "OpenProjectAction", KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        addItem(menu, mm, "SaveProject", "SaveProjectAction", KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        addItem(menu, mm, "SaveProjectAs", "SaveProjectAsAction", null);
        addItem(menu, mm, "CloseProject", "CloseProjectAction", KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
        menu.addSeparator();
        addItem(menu, mm, "Print", "PrintAction", KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
        addItem(menu, mm, "PrintPreview", "PrintPreviewAction", null);
        addItem(menu, mm, "PDF", "PDFAction", null);
        menu.addSeparator();
        addItem(menu, mm, "Exit", "ExitAction", null);
        return menu;
    }

    private static JMenu createEditMenu(MenuManager mm) {
        JMenu menu = new JMenu("Edit");
        menu.setMnemonic(KeyEvent.VK_E);
        addItem(menu, mm, "Undo", "UndoAction", KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        addItem(menu, mm, "Redo", "RedoAction", KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
        menu.addSeparator();
        addItem(menu, mm, "Cut", "CutAction", KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        addItem(menu, mm, "Copy", "CopyAction", KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        addItem(menu, mm, "Paste", "PasteAction", KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
        menu.addSeparator();
        addItem(menu, mm, "Find", "FindAction", KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
        menu.addSeparator();
        addItem(menu, mm, "AssignResources", "AssignResourcesAction", null);
        return menu;
    }

    private static JMenu createViewMenu(MenuManager mm) {
        JMenu menu = new JMenu("View");
        menu.setMnemonic(KeyEvent.VK_V);
        // Task views
        addItem(menu, mm, "Gantt", "GanttAction", null);
        addItem(menu, mm, "Network", "NetworkAction", null);
        addItem(menu, mm, "WBS", "WBSAction", null);
        menu.addSeparator();
        // Resource views
        addItem(menu, mm, "Resources", "ResourcesAction", null);
        addItem(menu, mm, "RBS", "RBSAction", null);
        menu.addSeparator();
        // Usage views
        addItem(menu, mm, "TaskUsageDetail", "TaskUsageDetailAction", null);
        addItem(menu, mm, "ResourceUsageDetail", "ResourceUsageDetailAction", null);
        menu.addSeparator();
        // Other views
        addItem(menu, mm, "Projects", "ProjectsAction", null);
        addItem(menu, mm, "Report", "ReportAction", null);
        menu.addSeparator();
        // Sub views
        addItem(menu, mm, "Histogram", "HistogramAction", null);
        addItem(menu, mm, "Charts", "ChartsAction", null);
        addItem(menu, mm, "NoSubWindow", "NoSubWindowAction", null);
        menu.addSeparator();
        addItem(menu, mm, "ZoomIn", "ZoomInAction", KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, InputEvent.CTRL_DOWN_MASK));
        addItem(menu, mm, "ZoomOut", "ZoomOutAction", KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK));
        addItem(menu, mm, "ScrollToTask", "ScrollToTaskAction", null);
        return menu;
    }

    private static JMenu createTaskMenu(MenuManager mm) {
        JMenu menu = new JMenu("Task");
        menu.setMnemonic(KeyEvent.VK_T);
        addItem(menu, mm, "InsertTask", "InsertTaskAction", KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0));
        addItem(menu, mm, "Delete", "DeleteAction", KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        menu.addSeparator();
        addItem(menu, mm, "Indent", "IndentAction", null);
        addItem(menu, mm, "Outdent", "OutdentAction", null);
        menu.addSeparator();
        addItem(menu, mm, "Link", "LinkAction", null);
        addItem(menu, mm, "Unlink", "UnlinkAction", null);
        menu.addSeparator();
        addItem(menu, mm, "TaskInformation", "InformationAction", null);
        addItem(menu, mm, "Notes", "NotesAction", null);
        menu.addSeparator();
        addItem(menu, mm, "UpdateTasks", "UpdateTasksAction", null);
        return menu;
    }

    private static JMenu createResourceMenu(MenuManager mm) {
        JMenu menu = new JMenu("Resource");
        menu.setMnemonic(KeyEvent.VK_R);
        addItem(menu, mm, "ResourceInformation", "InformationAction", null);
        addItem(menu, mm, "Notes", "NotesAction", null);
        menu.addSeparator();
        addItem(menu, mm, "ChangeWorkingTime", "ChangeWorkingTimeAction", null);
        return menu;
    }

    private static JMenu createProjectMenu(MenuManager mm) {
        JMenu menu = new JMenu("Project");
        menu.setMnemonic(KeyEvent.VK_P);
        addItem(menu, mm, "ProjectInformation", "ProjectInformationAction", null);
        addItem(menu, mm, "Calendar", "ChangeWorkingTimeAction", null);
        addItem(menu, mm, "ProjectsDialog", "ProjectsDialogAction", null);
        menu.addSeparator();
        addItem(menu, mm, "SaveBaseline", "SaveBaselineAction", null);
        addItem(menu, mm, "ClearBaseline", "ClearBaselineAction", null);
        menu.addSeparator();
        addItem(menu, mm, "UpdateProject", "UpdateProjectAction", null);
        return menu;
    }

    private static JMenu createHelpMenu(MenuManager mm) {
        JMenu menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        addItem(menu, mm, "Help", "HelpAction", KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        addItem(menu, mm, "About", "AboutAction", null);
        return menu;
    }

    /**
     * Add a menu item wired to an action from the MenuManager registry.
     * If the action doesn't exist yet, creates a disabled placeholder.
     */
    private static void addItem(JMenu menu, MenuManager mm,
            String label, String actionId, KeyStroke accelerator) {
        Action action = mm.getActionFromId(actionId);
        JMenuItem item;
        if (action != null) {
            item = new JMenuItem(action);
            item.setText(label);
        } else {
            item = new JMenuItem(label);
            item.setEnabled(false);
        }
        if (accelerator != null) {
            item.setAccelerator(accelerator);
        }
        // Try to set icon from ModernIcons
        String iconKey = "menu." + actionId.replace("Action", "").toLowerCase();
        javax.swing.ImageIcon icon = IconManager.getIcon(iconKey);
        if (icon != null) {
            item.setIcon(icon);
        }
        menu.add(item);
    }
}
```

**Step 2: Verify it compiles**

Run: `ant compile`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add projectlibre_ui/src/com/projectlibre1/menu/MenuBarFactory.java
git commit -m "feat: add MenuBarFactory for JMenuBar-based menu system"
```

---

## Task 3: Create ModernToolBar

**Files:**
- Create: `projectlibre_ui/src/com/projectlibre1/menu/ModernToolBar.java`

**Step 1: Create the toolbar**

A single JToolBar with grouped icon buttons, view segmented control, and filter dropdowns.

```java
package com.projectlibre1.menu;

import java.awt.*;
import javax.swing.*;
import com.projectlibre1.pm.graphic.IconManager;
import com.projectlibre1.theme.NomadPlanColors;
import com.projectlibre1.toolbar.FilterToolBarManager;

public class ModernToolBar {

    public static JToolBar create(MenuManager menuManager,
            FilterToolBarManager filterToolBarManager) {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, NomadPlanColors.border()),
            BorderFactory.createEmptyBorder(2, 4, 2, 4)
        ));

        // Group 1: File ops
        addButton(toolbar, menuManager, "SaveProjectAction", "menu24.save", "Save");
        addButton(toolbar, menuManager, "UndoAction", "menu24.undo", "Undo");
        addButton(toolbar, menuManager, "RedoAction", "menu24.redo", "Redo");
        toolbar.addSeparator(new Dimension(8, 24));

        // Group 2: Edit ops
        addButton(toolbar, menuManager, "InsertTaskAction", "menu24.insert", "Insert Task");
        addButton(toolbar, menuManager, "DeleteAction", "menu24.delete", "Delete");
        addButton(toolbar, menuManager, "IndentAction", "menu24.indent", "Indent");
        addButton(toolbar, menuManager, "OutdentAction", "menu24.outdent", "Outdent");
        toolbar.addSeparator(new Dimension(8, 24));

        // Group 3: Links
        addButton(toolbar, menuManager, "LinkAction", "menu24.link", "Link");
        addButton(toolbar, menuManager, "UnlinkAction", "menu24.unlink", "Unlink");
        toolbar.addSeparator(new Dimension(8, 24));

        // Group 4: Info
        addButton(toolbar, menuManager, "InformationAction", "menu24.taskInformation", "Information");
        addButton(toolbar, menuManager, "NotesAction", "menu24.notes", "Notes");
        toolbar.addSeparator(new Dimension(8, 24));

        // Group 5: View switcher (segmented toggle buttons)
        ButtonGroup viewGroup = new ButtonGroup();
        addToggle(toolbar, menuManager, "GanttAction", "view.gantt", "Gantt", viewGroup, true);
        addToggle(toolbar, menuManager, "NetworkAction", "view.network", "Network", viewGroup, false);
        addToggle(toolbar, menuManager, "ResourcesAction", "view.resources", "Resources", viewGroup, false);
        toolbar.addSeparator(new Dimension(8, 24));

        // Group 6: Search
        addButton(toolbar, menuManager, "FindAction", "menu24.find", "Find");
        toolbar.addSeparator(new Dimension(8, 24));

        // Group 7: Filter dropdowns
        if (filterToolBarManager != null) {
            JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
            filterPanel.setOpaque(false);
            filterToolBarManager.addButtonsInRibbonBand(filterPanel);
            toolbar.add(filterPanel);
        }

        return toolbar;
    }

    private static void addButton(JToolBar toolbar, MenuManager mm,
            String actionId, String iconKey, String tooltip) {
        Action action = mm.getActionFromId(actionId);
        JButton btn = new JButton();
        if (action != null) {
            btn.setAction(action);
        }
        btn.setText(null); // icon only
        btn.setToolTipText(tooltip);
        btn.setFocusable(false);

        ImageIcon icon = IconManager.getIcon(iconKey);
        if (icon != null) {
            btn.setIcon(icon);
        }
        btn.putClientProperty("JButton.buttonType", "toolBarButton");
        toolbar.add(btn);
    }

    private static void addToggle(JToolBar toolbar, MenuManager mm,
            String actionId, String iconKey, String tooltip,
            ButtonGroup group, boolean selected) {
        Action action = mm.getActionFromId(actionId);
        JToggleButton btn = new JToggleButton();
        if (action != null) {
            btn.setAction(action);
        }
        btn.setText(null); // icon only
        btn.setToolTipText(tooltip);
        btn.setFocusable(false);
        btn.setSelected(selected);

        ImageIcon icon = IconManager.getIcon(iconKey);
        if (icon != null) {
            btn.setIcon(icon);
        }
        btn.putClientProperty("JButton.buttonType", "roundRect");
        group.add(btn);
        toolbar.add(btn);
    }
}
```

**Step 2: Verify it compiles**

Run: `ant compile`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add projectlibre_ui/src/com/projectlibre1/menu/ModernToolBar.java
git commit -m "feat: add ModernToolBar with grouped icon buttons and view switcher"
```

---

## Task 4: Wire GraphicManager to use the new toolbar

**Files:**
- Modify: `projectlibre_ui/src/com/projectlibre1/pm/graphic/frames/GraphicManager.java`

**Step 1: Add a new `setModernToolbar()` method**

Add this method near `setRibbon()` (around line 2370). It creates the menu bar and toolbar and adds them to the frame.

```java
public void setModernToolbar(ModernMainFrame frame, MenuManager menuManager) {
    // Create filter toolbar
    filterToolBarManager = FilterToolBarManager.create(getMenuManager());

    // Create and set menu bar
    JMenuBar menuBar = MenuBarFactory.create(menuManager);
    frame.setJMenuBar(menuBar);

    // Create and add toolbar
    JToolBar toolbar = ModernToolBar.create(menuManager, filterToolBarManager);
    frame.getContentPane().add(toolbar, BorderLayout.NORTH);
}
```

**Step 2: Modify `setToolBarAndMenus()` to branch**

Find the existing `setToolBarAndMenus()` method (around line 2540). Currently it calls `setRibbon()` when `isRibbonUI()`. Add an else branch for the modern toolbar:

```java
public void setToolBarAndMenus(final Container contentPane) {
    if (Environment.isRibbonUI()) {
        setRibbon((JRibbonFrame)container, getMenuManager());
    } else {
        setModernToolbar((ModernMainFrame)container, getMenuManager());
    }
}
```

Add required imports at the top of GraphicManager.java:
```java
import com.projectlibre1.menu.MenuBarFactory;
import com.projectlibre1.menu.ModernToolBar;
```

**Step 3: Verify it compiles**

Run: `ant compile`
Expected: BUILD SUCCESSFUL

**Step 4: Commit**

```bash
git add projectlibre_ui/src/com/projectlibre1/pm/graphic/frames/GraphicManager.java
git commit -m "feat: wire GraphicManager to use modern toolbar when not in ribbon mode"
```

---

## Task 5: Update MainFrameFactory to use ModernMainFrame

**Files:**
- Modify: `projectlibre_ui/src/com/projectlibre1/pm/graphic/frames/MainFrameFactory.java`

**Step 1: Change the factory to create ModernMainFrame**

Currently line 71 does:
```java
mainFrame = Environment.isRibbonUI() ?
    new MainRibbonFrame(name, projectUrl, server) :
    new MainFrame(name, projectUrl, server);
```

Change to:
```java
mainFrame = Environment.isRibbonUI() ?
    new MainRibbonFrame(name, projectUrl, server) :
    new ModernMainFrame(name, projectUrl, server);
```

**Step 2: Commit**

```bash
git add projectlibre_ui/src/com/projectlibre1/pm/graphic/frames/MainFrameFactory.java
git commit -m "feat: wire MainFrameFactory to use ModernMainFrame"
```

---

## Task 6: Disable ribbon mode and test

**Files:**
- Modify: `projectlibre_ui/src/com/projectlibre1/util/Environment.java`

**Step 1: Find and change `isRibbonUI()`**

Search for `isRibbonUI` in `Environment.java`. It likely returns `true`. Change it to return `false`.

**Step 2: Build and launch**

```bash
export JAVA_HOME="/c/Program Files/Java/jdk-25.0.2"
export PATH="$JAVA_HOME/bin:/c/tools/apache-ant-1.10.15/bin:$PATH"
cd projectlibre_build
ant dist fatjar
java -jar packages/nomadplan-1.9.8.jar
```

**Step 3: Verify**

Expected:
- App opens with JMenuBar at top (File, Edit, View, Task, Resource, Project, Help)
- Toolbar below with icon buttons
- Theme toggle button on far right of menu bar
- All menu items clickable and wired to correct actions
- Content area (Gantt/spreadsheet) renders correctly below toolbar
- No Flamingo ribbon visible

**Step 4: Debug and fix**

Common issues to watch for:
- Actions returning null from `getActionFromId()` — action IDs may not match. Check `menuInternal.properties` for exact action string names and adjust `MenuBarFactory` accordingly.
- Content panel not showing — `GraphicManager` may need adjustment in how it adds the content pane vs toolbar.
- Icon lookup failures — icon keys in `ModernToolBar` may not match `images.properties`. Check and adjust.
- `FrameHolder` interface methods missing — implement any missing methods from the interface in `ModernMainFrame`.
- Cast exceptions — other code may cast the frame to `JRibbonFrame`. Search for `(JRibbonFrame)` casts and add null checks or instanceof guards.

**Step 5: Commit**

```bash
git add -A
git commit -m "feat: switch to modern toolbar mode (disable ribbon)"
```

---

## Task 7: Fix action wiring

**Files:**
- Modify: `projectlibre_ui/src/com/projectlibre1/menu/MenuBarFactory.java`
- Modify: `projectlibre_ui/src/com/projectlibre1/menu/ModernToolBar.java`

**Step 1: Verify action ID mapping**

The action IDs in `MenuBarFactory` and `ModernToolBar` must match exactly what `MenuManager.getActionFromId()` expects. The action IDs come from `menuInternal.properties`:

```properties
RibbonSaveProject.action = SaveProjectAction
RibbonOpenProject.action = OpenProjectAction
```

But `getActionFromId()` in `ExtMenuFactory` strips the suffix and looks up via the action map. The actual action names registered in `GraphicManager.initActions()` use constants from `MenuActionConstants`:

```java
public static final String ACTION_SAVE_PROJECT = "SaveProject";
```

So the action IDs passed to `getActionFromId()` should be the action constants (e.g., `"SaveProject"`) NOT the full `"SaveProjectAction"` string. **Fix all action IDs in both files.**

Key mappings (from MenuActionConstants.java):
- `SaveProject`, `OpenProject`, `NewProject`, `SaveProjectAs`, `CloseProject`
- `Print`, `PrintPreview`, `PDF`
- `Undo`, `Redo`, `Cut`, `Copy`, `Paste`
- `InsertTask`, `Delete`, `Indent`, `Outdent`
- `Link`, `Unlink`
- `Find`, `Information`, `Notes`, `ChangeWorkingTime`, `AssignResources`
- `SaveBaseline`, `ClearBaseline`, `UpdateProject`, `UpdateTasks`
- `ProjectInformation`, `ProjectsDialog`
- `Gantt`, `Network`, `WBS`, `Resources`, `RBS`
- `TaskUsageDetail`, `ResourceUsageDetail`
- `Histogram`, `Charts`, `TaskUsage`, `ResourceUsage`, `NoSubWindow`
- `Projects`, `Report`
- `ScrollToTask`, `ZoomIn`, `ZoomOut`
- `Exit`, `Help`, `About`

**Step 2: Test each menu and toolbar button**

Launch the app, click every menu item and toolbar button. Note which ones work and which are dead (no action).

**Step 3: Fix any broken wiring and commit**

```bash
git add -A
git commit -m "fix: correct action ID mapping for menu and toolbar buttons"
```

---

## Task 8: Handle JRibbonFrame casts throughout codebase

**Files:**
- Search and modify: All files that cast to `JRibbonFrame`

**Step 1: Find all JRibbonFrame references**

```bash
grep -rn "JRibbonFrame" projectlibre_ui/src/ --include="*.java" | grep -v "import"
```

**Step 2: Guard each cast with instanceof**

For each occurrence like:
```java
JRibbonFrame frame = (JRibbonFrame) SwingUtilities.getWindowAncestor(component);
```

Change to:
```java
Window window = SwingUtilities.getWindowAncestor(component);
if (window instanceof JRibbonFrame) {
    JRibbonFrame frame = (JRibbonFrame) window;
    // ribbon-specific code
}
```

Or if the code just needs a JFrame:
```java
JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(component);
```

**Step 3: Build, test, commit**

```bash
ant dist fatjar
java -jar packages/nomadplan-1.9.8.jar
git add -A
git commit -m "fix: guard JRibbonFrame casts for non-ribbon mode"
```

---

## Task 9: Add project combo box to toolbar area

**Files:**
- Modify: `projectlibre_ui/src/com/projectlibre1/menu/ModernToolBar.java`
- Modify: `projectlibre_ui/src/com/projectlibre1/pm/graphic/frames/GraphicManager.java`

**Step 1: Add the project combo panel**

In `GraphicManager.setModernToolbar()`, after creating the toolbar, add the project combo box to the right side:

```java
// Add project file selector to right side of toolbar
JComponent filesComponent = ((DefaultFrameManager)getFrameManager()).getProjectComboPanel();
toolbar.add(Box.createHorizontalGlue());
toolbar.add(filesComponent);
```

**Step 2: Build, test, commit**

---

## Task 10: Polish and visual refinement

**Files:**
- Modify: `projectlibre_ui/src/com/projectlibre1/pm/graphic/laf/LafManagerImpl.java`
- Modify: `projectlibre_ui/src/com/projectlibre1/menu/MenuBarFactory.java`
- Modify: `projectlibre_ui/src/com/projectlibre1/menu/ModernToolBar.java`

**Step 1: Add FlatLaf toolbar styling defaults**

In `LafManagerImpl.applyNomadPlanDefaults()`:
```java
// Modern toolbar styling
UIManager.put("MenuBar.borderColor", NomadPlanColors.border());
UIManager.put("MenuBar.underlineSelectionColor", NomadPlanColors.accent());
UIManager.put("MenuItem.selectionBackground", NomadPlanColors.accent());
UIManager.put("ToolBar.background", NomadPlanColors.surface());
```

**Step 2: Add NomadPlan logo to menu bar**

In `MenuBarFactory.create()`, add a logo label before the first menu:
```java
JLabel logo = new JLabel("NomadPlan");
logo.setFont(logo.getFont().deriveFont(Font.BOLD, 14f));
logo.setForeground(NomadPlanColors.accent());
logo.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 12));
menuBar.add(logo);
```

**Step 3: Set toolbar button size**

In `ModernToolBar.addButton()`:
```java
btn.setPreferredSize(new Dimension(32, 32));
```

**Step 4: Build, test, take screenshot, commit**

```bash
ant dist fatjar
java -jar packages/nomadplan-1.9.8.jar
git add -A
git commit -m "feat: polish modern toolbar appearance with FlatLaf styling"
```

---

## Task 11: Clean up ribbon references (optional, after stable)

**Files:**
- Modify: Various files that import/reference Flamingo

**Step 1: Remove `isRibbonUI()` branching**

Once the modern toolbar is stable, remove the ribbon code path:
- Remove `setRibbon()` method from GraphicManager
- Remove `MainRibbonFrame.java`
- Remove `ProjectLibreRibbonUI.java` (2300+ lines)
- Remove ribbon-specific modifications in `BasicRibbonBandUI`, `BasicRibbonTaskToggleButtonUI`, `BasicRibbonApplicationMenuButtonUI`
- Update `Environment.isRibbonUI()` to always return false, or remove it

**Step 2: Build, test full regression, commit**

```bash
ant clean dist fatjar
java -jar packages/nomadplan-1.9.8.jar
git add -A
git commit -m "chore: remove Flamingo ribbon code (replaced by modern toolbar)"
```

---

## Verification Checklist

After all tasks complete:

- [ ] App launches with menu bar + toolbar (no ribbon)
- [ ] All File menu items work (New, Open, Save, Save As, Close, Print, Preview, PDF)
- [ ] All Edit menu items work (Undo, Redo, Cut, Copy, Paste, Find)
- [ ] All View menu items switch views correctly (Gantt, Network, WBS, Resources, etc.)
- [ ] All Task menu items work (Insert, Delete, Indent, Outdent, Link, Unlink, Info, Notes)
- [ ] All Project menu items work (Information, Calendar, Baselines, Update)
- [ ] Toolbar buttons trigger correct actions
- [ ] View toggle buttons switch views and show selected state
- [ ] Filter dropdowns work
- [ ] Theme toggle (light/dark) works
- [ ] Project combo box shows open projects
- [ ] Keyboard shortcuts work (Ctrl+S, Ctrl+Z, Ctrl+N, etc.)
- [ ] Gantt chart renders correctly with more vertical space
- [ ] Both light and dark themes look correct
- [ ] No ClassCastExceptions or null pointer errors in console
