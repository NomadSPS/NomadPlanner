# Replace Flamingo Ribbon with Modern Menu Bar + Toolbar

**Date**: 2026-03-06
**Status**: Approved

## Problem

The Flamingo Ribbon framework is an abandoned 2010-era library that does its own custom painting, fighting against FlatLaf at every turn. Despite extensive custom painting work, the ribbon still looks amateur because Flamingo's architecture prevents FlatLaf from rendering components natively.

## Decision

Replace the Flamingo Ribbon with a standard Swing `JMenuBar` + `JToolBar`. This lets FlatLaf handle all rendering natively, producing a professional appearance with zero custom painting.

## Layout

```
+------------------------------------------------------------------+
| [NP]  File  Edit  View  Task  Resource  Project  Help      [sun] |  <- Menu bar (25px)
+------------------------------------------------------------------+
| Save Undo Redo | Insert Del Indent Outdent | Link Unlink |       |  <- Toolbar (36px)
| Info Notes | [Gantt | Network | Resources] | Find | [Filter v]   |
+------------------------------------------------------------------+
|                                                                    |
|                  Content area (70px MORE than before)              |
|                                                                    |
+------------------------------------------------------------------+
```

Total chrome height: ~61px (vs ~130px with ribbon). Gains 70px for Gantt charts.

## Menu Structure

### File
New, Open, Save, Save As, Close, ---, Print, Print Preview, Export PDF, ---, Exit

### Edit
Undo, Redo, ---, Cut, Copy, Paste, ---, Find, ---, Assign Resources

### View
Gantt, Network Diagram, WBS, ---, Resources, RBS, ---, Task Usage, Resource Usage, ---, Projects, Report, ---, Histogram, Charts, No Sub Window, ---, Zoom In, Zoom Out, Scroll to Task

### Task
Insert Task, Delete, ---, Indent, Outdent, ---, Link, Unlink, ---, Task Information, Notes, ---, Update Tasks

### Resource
Resource Information, Notes, ---, Change Working Time

### Project
Project Information, Calendar, Projects Dialog, ---, Save Baseline, Clear Baseline, ---, Update Project

### Help
Help, About

## Toolbar Groups

Separated by visual dividers:

1. **File ops**: Save, Undo, Redo
2. **Edit ops**: Insert, Delete, Indent, Outdent
3. **Links**: Link, Unlink
4. **Info**: Information, Notes
5. **View switcher**: Segmented toggle buttons (Gantt / Network / WBS / Resources)
6. **Search**: Find
7. **Filter**: Filter dropdown (FilterToolBarManager)

## Technical Changes

### Component Mapping

| Current (Flamingo) | New (Swing) |
|---|---|
| JRibbonFrame | JFrame |
| JRibbon | JMenuBar + JToolBar |
| JRibbonTask (tabs) | JMenu |
| JRibbonBand | Toolbar button groups |
| JCommandButton | JButton / JToggleButton |
| JRibbonApplicationMenuButton | Logo label in menu bar |
| RibbonFactory | New MenuBarFactory + ToolBarFactory |

### Key Files to Modify

- `MainRibbonFrame.java` -> `MainFrame.java` (new, extends JFrame)
- `GraphicManager.java` - Switch from ribbon setup to menu+toolbar setup
- `RibbonFactory.java` - Replace with `MenuBarFactory` + `ToolBarFactory`
- `menuInternal.properties` - Restructure for menu bar format
- `ProjectLibreRibbonUI.java` - Remove entirely

### Action Reuse

All existing Action classes (SaveProjectAction, InsertTaskAction, etc.) are standard Swing Actions. They wire directly to JMenuItem and JButton with no changes needed.

### FlatLaf Handles Rendering

- Menus: native FlatLaf styling (hover, selection, icons)
- Toolbar buttons: FlatLaf button styling (rounded, hover states)
- Separators: FlatLaf separator rendering
- View toggle buttons: FlatLaf `buttonType=roundRect` segmented control

### Filter Toolbar

`FilterToolBarManager` already produces a JPanel with filter controls. It slots directly into the JToolBar.

### Theme Toggle

The sun/moon toggle button moves to the right side of the menu bar.

## What Gets Removed

- `ProjectLibreRibbonUI.java` (2300+ lines of custom ribbon painting)
- `BasicRibbonBandUI.java` modifications (no more bands)
- `BasicRibbonTaskToggleButtonUI.java` modifications (no more tab buttons)
- `BasicRibbonApplicationMenuButtonUI.java` modifications (no more app menu button)
- `FlamingoUtilities.java` modifications
- All custom ribbon painting code

## Migration Strategy

Phase approach to avoid breaking the app:

1. Create new `MainFrame` class alongside existing `MainRibbonFrame`
2. Create `MenuBarFactory` that builds menus from action registry
3. Create `ToolBarFactory` that builds toolbar from action registry
4. Wire `GraphicManager` to use new frame class
5. Test all actions work
6. Remove old Flamingo ribbon code
