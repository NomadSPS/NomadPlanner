/*******************************************************************************
 * The contents of this file are subject to the Common Public Attribution License
 * Version 1.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.projectlibre.com/license . The License is based on the Mozilla Public
 * License Version 1.1 but Sections 14 and 15 have been added to cover use of
 * software over a computer network and provide for limited attribution for the
 * Original Developer. In addition, Exhibit A has been modified to be consistent
 * with Exhibit B.
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for the
 * specific language governing rights and limitations under the License. The
 * Original Code is ProjectLibre. The Original Developer is the Initial Developer
 * and is ProjectLibre Inc. All portions of the code written by ProjectLibre are
 * Copyright (c) 2012-2019. All Rights Reserved. Contributor ProjectLibre, Inc.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * ProjectLibre End-User License Agreement (the ProjectLibre License) in which case
 * the provisions of the ProjectLibre License are applicable instead of those above.
 * If you wish to allow use of your version of this file only under the terms of the
 * ProjectLibre License and not to allow others to use your version of this file
 * under the CPAL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the ProjectLibre
 * License. If you do not delete the provisions above, a recipient may use your
 * version of this file under either the CPAL or the ProjectLibre Licenses.
 *
 *
 * [NOTE: The text of this Exhibit A may differ slightly from the text of the notices
 * in the Source Code files of the Original Code. You should use the text of this
 * Exhibit A rather than the text found in the Original Code Source Code for Your
 * Modifications.]
 *
 * EXHIBIT B. Attribution Information for ProjectLibre required
 *
 * Attribution Copyright Notice: Copyright (c) 2012-2019, ProjectLibre, Inc.
 * Attribution Phrase (not exceeding 10 words):
 * ProjectLibre, open source project management software.
 * Attribution URL: http://www.projectlibre.com
 * Graphic Image as provided in the Covered Code as file: projectlibre-logo.png with
 * alternatives listed on http://www.projectlibre.com/logo
 *
 * Display of Attribution Information is required in Larger Works which are defined
 * in the CPAL as a work which combines Covered Code or portions thereof with code
 * not governed by the terms of the CPAL. However, in addition to the other notice
 * obligations, all copies of the Covered Code in Executable and Source Code form
 * distributed must, as a form of attribution of the original author, include on
 * each user interface screen the "ProjectLibre" logo visible to all users.
 * The ProjectLibre logo should be located horizontally aligned with the menu bar
 * and left justified on the top left of the screen adjacent to the File menu. The
 * logo must be at least 144 x 31 pixels. When users click on the "ProjectLibre"
 * logo it must direct them back to http://www.projectlibre.com.
 *******************************************************************************/
package com.projectlibre1.menu;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import com.projectlibre1.pm.graphic.IconManager;
import com.projectlibre1.theme.ModernIcons;
import com.projectlibre1.toolbar.FilterToolBarManager;

/**
 * Modern single-row toolbar replacing the Flamingo ribbon command buttons.
 * Uses standard Swing JToolBar with FlatLaf styling.
 */
public class ModernToolBar implements MenuActionConstants {

    private final MenuManager menuManager;
    private final FilterToolBarManager filterToolBarManager;
    private JToolBar toolBar;
    private JToggleButton themeToggle;

    public ModernToolBar(MenuManager menuManager, FilterToolBarManager filterToolBarManager) {
        this.menuManager = menuManager;
        this.filterToolBarManager = filterToolBarManager;
    }

    /**
     * Creates and returns the fully populated toolbar.
     */
    public JToolBar createToolBar() {
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setRollover(true);

        // -- File ops --
        addButton(ACTION_SAVE_PROJECT, "menu24.save", "Save");
        addButton(ACTION_UNDO, "menu24.undo", "Undo");
        addButton(ACTION_REDO, "menu24.redo", "Redo");

        toolBar.addSeparator();

        // -- Edit ops --
        addButton(ACTION_INSERT_TASK, "menu24.insertTask", "Insert Task");
        addButton(ACTION_DELETE, "menu24.delete", "Delete");
        addButton(ACTION_INDENT, "menu24.indent", "Indent");
        addButton(ACTION_OUTDENT, "menu24.outdent", "Outdent");

        toolBar.addSeparator();

        // -- Links --
        addButton(ACTION_LINK, "menu24.link", "Link");
        addButton(ACTION_UNLINK, "menu24.unlink", "Unlink");

        toolBar.addSeparator();

        // -- Info --
        addButton(ACTION_INFORMATION, "menu24.taskInformation", "Information");
        addButton(ACTION_NOTES, "menu24.notes", "Notes");
        addButton(ACTION_CHANGE_WORKING_TIME, "menu24.calendarManager", "Calendar");
        addButton(ACTION_CALENDAR_OPTIONS, "menu24.workingTime", "Working Time");

        toolBar.addSeparator();

        // -- View switcher (toggle buttons in a ButtonGroup) --
        ButtonGroup viewGroup = new ButtonGroup();
        addToggleButton(ACTION_GANTT, "view.gantt", "Gantt", viewGroup, true);
        addToggleButton(ACTION_NETWORK, "view.network", "Network", viewGroup, false);
        addToggleButton(ACTION_RESOURCES, "view.resources", "Resources", viewGroup, false);

        toolBar.addSeparator();

        // -- Zoom --
        addButton(ACTION_ZOOM_IN, "menu24.zoomin", "Zoom In");
        addButton(ACTION_ZOOM_OUT, "menu24.zoomout", "Zoom Out");
        addButton(ACTION_SCROLL_TO_TASK, "menu24.scrollToTask", "Scroll to Task");

        toolBar.addSeparator();

        // -- Summary colors --
        addButton(ACTION_WBS_SUMMARY_COLORS, "menu24.wbsSummaryColors", "WBS Summary Colors");

        toolBar.addSeparator();

        // -- Driving path --
        addStandaloneToggleButton(ACTION_DRIVING_PATH_BACKWARD, "menu24.drivingPathBackward", "Driving Path Backward");
        addStandaloneToggleButton(ACTION_DRIVING_PATH_FORWARD, "menu24.drivingPathForward", "Driving Path Forward");
        addStandaloneToggleButton(ACTION_DRIVING_PATH_BOTH, "menu24.drivingPathBoth", "Driving Path Backward + Forward");

        toolBar.addSeparator();

        // -- Search --
        addButton(ACTION_FIND, "menu24.find", "Find");

        toolBar.addSeparator();

        // -- Filter toolbar (combo boxes for filter/sort/group) --
        if (filterToolBarManager != null) {
            javax.swing.JPanel filterPanel = new javax.swing.JPanel(
                new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 0));
            filterPanel.setOpaque(false);
            filterToolBarManager.addButtonsInRibbonBand(filterPanel);
            toolBar.add(filterPanel);
        }

        toolBar.addSeparator();

        // -- Theme toggle (Dark/Light) --
        themeToggle = createThemeToggle();
        toolBar.add(themeToggle);

        return toolBar;
    }

    /**
     * Returns the underlying JToolBar (only valid after createToolBar()).
     */
    public JToolBar getToolBar() {
        return toolBar;
    }

    // ---- Helper: icon-only action button ----

    private static final int ICON_SIZE = 20;

    private void addButton(String actionId, String iconKey, String tooltip) {
        Action action = menuManager.getActionFromId(actionId);
        JButton btn;
        if (action != null) {
            btn = new JButton(action);
        } else {
            btn = new JButton();
            btn.setEnabled(false);
        }
        btn.setText(null);
        btn.setToolTipText(tooltip);
        btn.setFocusable(false);
        btn.putClientProperty("JButton.buttonType", "toolBarButton");
        btn.setMargin(new java.awt.Insets(4, 4, 4, 4));

        javax.swing.Icon icon = getScaledIcon(iconKey);
        if (icon != null) {
            btn.setIcon(icon);
        } else {
            btn.setText(tooltip.length() > 2 ? tooltip.substring(0, 2) : tooltip);
        }

        toolBar.add(btn);
        menuManager.registerToolButton(actionId, btn);
    }

    private void addToggleButton(String actionId, String iconKey, String tooltip,
            ButtonGroup group, boolean selected) {
        Action action = menuManager.getActionFromId(actionId);
        JToggleButton btn;
        if (action != null) {
            btn = new JToggleButton(action);
        } else {
            btn = new JToggleButton();
            btn.setEnabled(false);
        }
        btn.setText(null);
        btn.setToolTipText(tooltip);
        btn.setFocusable(false);
        btn.setSelected(selected);
        btn.putClientProperty("JButton.buttonType", "roundRect");
        btn.setMargin(new java.awt.Insets(4, 4, 4, 4));

        javax.swing.Icon icon = getScaledIcon(iconKey);
        if (icon != null) {
            btn.setIcon(icon);
        } else {
            btn.setText(tooltip.length() > 2 ? tooltip.substring(0, 2) : tooltip);
        }

        group.add(btn);
        toolBar.add(btn);
        menuManager.registerToolButton(actionId, btn);
    }

    private void addStandaloneToggleButton(String actionId, String iconKey, String tooltip) {
        Action action = menuManager.getActionFromId(actionId);
        JToggleButton btn;
        if (action != null) {
            btn = new JToggleButton(action);
        } else {
            btn = new JToggleButton();
            btn.setEnabled(false);
        }
        btn.setText(null);
        btn.setToolTipText(tooltip);
        btn.setFocusable(false);
        btn.putClientProperty("JButton.buttonType", "roundRect");
        btn.setMargin(new java.awt.Insets(4, 4, 4, 4));

        javax.swing.Icon icon = getScaledIcon(iconKey);
        if (icon != null) {
            btn.setIcon(icon);
        } else {
            btn.setText(tooltip.length() > 2 ? tooltip.substring(0, 2) : tooltip);
        }

        toolBar.add(btn);
        menuManager.registerToolButton(actionId, btn);
    }

    /**
     * Returns a vector Icon that paints directly in the Graphics context,
     * or falls back to a scaled bitmap. Direct painting ensures crisp
     * rendering on HiDPI displays since the Graphics2D already carries
     * the display scale transform.
     */
    private static javax.swing.Icon getScaledIcon(String iconKey) {
        if (ModernIcons.hasIcon(iconKey)) {
            return new VectorToolBarIcon(ModernIcons.getPainter(iconKey), ICON_SIZE);
        }
        // Fall back to bitmap icons, scaling if needed
        ImageIcon icon = IconManager.getIcon(iconKey);
        if (icon != null && (icon.getIconWidth() != ICON_SIZE || icon.getIconHeight() != ICON_SIZE)) {
            java.awt.Image scaled = icon.getImage().getScaledInstance(
                    ICON_SIZE, ICON_SIZE, java.awt.Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        }
        return icon;
    }

    /** Paints a ModernIcons painter directly — no intermediate BufferedImage. */
    private static class VectorToolBarIcon implements javax.swing.Icon {
        private final ModernIcons.IconPainter painter;
        private final int size;

        VectorToolBarIcon(ModernIcons.IconPainter painter, int size) {
            this.painter = painter;
            this.size = size;
        }

        @Override
        public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
            java.awt.Graphics2D g2d = (java.awt.Graphics2D) g.create();
            g2d.translate(x, y);
            painter.paint(g2d, size, size);
            g2d.dispose();
        }

        @Override public int getIconWidth() { return size; }
        @Override public int getIconHeight() { return size; }
    }

    private static JToggleButton createThemeToggle() {
        boolean dark = com.projectlibre1.theme.NomadPlanColors.isDarkMode();
        JToggleButton toggle = new JToggleButton(dark ? "Light" : "Dark");
        toggle.setSelected(dark);
        toggle.setToolTipText("Toggle light/dark theme");
        toggle.setFocusable(false);
        toggle.setFont(toggle.getFont().deriveFont(11f));
        toggle.setMargin(new java.awt.Insets(2, 8, 2, 8));
        toggle.addActionListener(e -> {
            com.projectlibre1.pm.graphic.frames.GraphicManager gm =
                com.projectlibre1.pm.graphic.frames.GraphicManager.getInstance();
            if (gm != null) {
                gm.getLafManager().toggleTheme();
            }
            toggle.setText(com.projectlibre1.theme.NomadPlanColors.isDarkMode() ? "Light" : "Dark");
            toggle.setSelected(com.projectlibre1.theme.NomadPlanColors.isDarkMode());
        });
        return toggle;
    }
}
