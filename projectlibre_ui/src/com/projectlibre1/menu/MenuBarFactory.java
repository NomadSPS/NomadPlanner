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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.ImageIcon;

import com.projectlibre1.pm.graphic.IconManager;
import com.projectlibre1.theme.NomadPlanUi;

/**
 * Builds a standard JMenuBar to replace the Flamingo ribbon menu system.
 * All actions are looked up from MenuManager by their MenuActionConstants IDs.
 */
public class MenuBarFactory implements MenuActionConstants {

    private final MenuManager menuManager;

    public MenuBarFactory(MenuManager menuManager) {
        this.menuManager = menuManager;
    }

    /**
     * Creates and returns the fully populated menu bar.
     */
    public JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        NomadPlanUi.applyHeaderMenuBarStyle(menuBar);

        menuBar.add(createFileMenu());
        menuBar.add(createEditMenu());
        menuBar.add(createViewMenu());
        menuBar.add(createTaskMenu());
        menuBar.add(createResourceMenu());
        menuBar.add(createProjectMenu());
        menuBar.add(createHelpMenu());

        return menuBar;
    }

    // ---- File menu ----

    private JMenu createFileMenu() {
        JMenu menu = createTopLevelMenu("File", KeyEvent.VK_F);

        addItem(menu, ACTION_NEW_PROJECT, "New Project", KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK);
        addItem(menu, ACTION_OPEN_PROJECT, "Open", KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK);
        addItem(menu, ACTION_SAVE_PROJECT, "Save", KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);
        addItem(menu, ACTION_SAVE_PROJECT_AS, "Save As...", KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);
        addItem(menu, ACTION_CLOSE_PROJECT, "Close", KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK);
        menu.addSeparator();
        addItem(menu, ACTION_PRINT, "Print", KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK);
        addItem(menu, ACTION_PRINT_PREVIEW, "Print Preview", 0, 0);
        addItem(menu, ACTION_PDF, "PDF", 0, 0);
        menu.addSeparator();
        addItem(menu, ACTION_EXIT, "Exit", KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK);

        return menu;
    }

    // ---- Edit menu ----

    private JMenu createEditMenu() {
        JMenu menu = createTopLevelMenu("Edit", KeyEvent.VK_E);

        addItem(menu, ACTION_UNDO, "Undo", KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK);
        addItem(menu, ACTION_REDO, "Redo", KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK);
        menu.addSeparator();
        addItem(menu, ACTION_CUT, "Cut", KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK);
        addItem(menu, ACTION_COPY, "Copy", KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK);
        addItem(menu, ACTION_PASTE, "Paste", KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK);
        menu.addSeparator();
        addItem(menu, ACTION_FIND, "Find", KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK);
        menu.addSeparator();
        addItem(menu, ACTION_ASSIGN_RESOURCES, "Assign Resources", 0, 0);

        return menu;
    }

    // ---- View menu ----

    private JMenu createViewMenu() {
        JMenu menu = createTopLevelMenu("View", KeyEvent.VK_V);

        addItem(menu, ACTION_GANTT, "Gantt", 0, 0);
        addItem(menu, ACTION_NETWORK, "Network", 0, 0);
        addItem(menu, ACTION_WBS, "WBS", 0, 0);
        menu.addSeparator();
        addItem(menu, ACTION_RESOURCES, "Resources", 0, 0);
        addItem(menu, ACTION_RBS, "RBS", 0, 0);
        menu.addSeparator();
        addItem(menu, ACTION_TASK_USAGE_DETAIL, "Task Usage Detail", 0, 0);
        addItem(menu, ACTION_RESOURCE_USAGE_DETAIL, "Resource Usage Detail", 0, 0);
        menu.addSeparator();
        addItem(menu, ACTION_PROJECTS, "Projects", 0, 0);
        addItem(menu, ACTION_REPORT, "Report", 0, 0);
        menu.addSeparator();
        addItem(menu, ACTION_HISTOGRAM, "Histogram", 0, 0);
        addItem(menu, ACTION_CHARTS, "Charts", 0, 0);
        addItem(menu, ACTION_TASK_DETAILS, "Task Details", 0, 0);
        addItem(menu, ACTION_NO_SUB_WINDOW, "No Sub Window", 0, 0);
        menu.addSeparator();
        addItem(menu, ACTION_ZOOM_IN, "Zoom In", KeyEvent.VK_EQUALS, InputEvent.CTRL_DOWN_MASK);
        addItem(menu, ACTION_ZOOM_OUT, "Zoom Out", KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK);
        addItem(menu, ACTION_SCROLL_TO_TASK, "Scroll to Task", 0, 0);
        menu.addSeparator();
        addItem(menu, ACTION_REFRESH, "Refresh", KeyEvent.VK_F5, 0);
        menu.addSeparator();
        JMenuItem darkToggle = new JMenuItem("Toggle Dark Mode");
        darkToggle.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        darkToggle.addActionListener(e -> {
            com.projectlibre1.pm.graphic.frames.GraphicManager gm =
                com.projectlibre1.pm.graphic.frames.GraphicManager.getInstance();
            if (gm != null) {
                gm.getLafManager().toggleTheme();
            }
        });
        menu.add(darkToggle);

        return menu;
    }

    // ---- Task menu ----

    private JMenu createTaskMenu() {
        JMenu menu = createTopLevelMenu("Task", KeyEvent.VK_T);

        addItem(menu, ACTION_INSERT_TASK, "Insert Task", KeyEvent.VK_INSERT, 0);
        addItem(menu, ACTION_DELETE, "Delete", KeyEvent.VK_DELETE, 0);
        menu.addSeparator();
        addItem(menu, ACTION_INDENT, "Indent", 0, 0);
        addItem(menu, ACTION_OUTDENT, "Outdent", 0, 0);
        menu.addSeparator();
        addItem(menu, ACTION_LINK, "Link", 0, 0);
        addItem(menu, ACTION_UNLINK, "Unlink", 0, 0);
        menu.addSeparator();
        addItem(menu, ACTION_INFORMATION, "Information", 0, 0);
        addItem(menu, ACTION_NOTES, "Notes", 0, 0);
        menu.addSeparator();
        addItem(menu, ACTION_UPDATE_TASKS, "Update Tasks", 0, 0);

        return menu;
    }

    // ---- Resource menu ----

    private JMenu createResourceMenu() {
        JMenu menu = createTopLevelMenu("Resource", KeyEvent.VK_R);

        addItem(menu, ACTION_INFORMATION, "Information", 0, 0);
        addItem(menu, ACTION_NOTES, "Notes", 0, 0);
        menu.addSeparator();
        addItem(menu, ACTION_CHANGE_WORKING_TIME, "Calendar", 0, 0);

        return menu;
    }

    // ---- Project menu ----

    private JMenu createProjectMenu() {
        JMenu menu = createTopLevelMenu("Project", KeyEvent.VK_P);

        addItem(menu, ACTION_PROJECT_INFORMATION, "Project Information", 0, 0);
        addItem(menu, ACTION_CHANGE_WORKING_TIME, "Calendar", 0, 0);
        addItem(menu, ACTION_CALENDAR_OPTIONS, "Working Time", 0, 0);
        addItem(menu, ACTION_PROJECTS_DIALOG, "Projects Dialog", 0, 0);
        addItem(menu, ACTION_WBS_SUMMARY_COLORS, "WBS Summary Colors...", 0, 0);
        menu.addSeparator();
        addItem(menu, ACTION_SAVE_BASELINE, "Save Baseline", 0, 0);
        addItem(menu, ACTION_CLEAR_BASELINE, "Clear Baseline", 0, 0);
        menu.addSeparator();
        addItem(menu, ACTION_UPDATE_PROJECT, "Update Project", 0, 0);

        return menu;
    }

    // ---- Help menu ----

    private JMenu createHelpMenu() {
        JMenu menu = createTopLevelMenu("Help", KeyEvent.VK_H);

        addItem(menu, ACTION_PROJECTLIBRE_DOCUMENTATION, "Help", KeyEvent.VK_F1, 0);
        addItem(menu, ACTION_ACTIVATION, "Activation...", 0, 0);
        addItem(menu, ACTION_ABOUT_PROJECTLIBRE, "About", 0, 0);

        return menu;
    }

    // ---- Helper: create a menu item wired to an action ----

    private void addItem(JMenu menu, String actionId, String fallbackText, int keyCode, int modifiers) {
        Action action = null;
        try {
            action = menuManager.getActionFromId(actionId);
        } catch (Exception e) {
            // Action not found — will create disabled menu item
        }
        JMenuItem item;
        if (action != null) {
            item = new JMenuItem(action);
            // Override the text if the action does not provide one
            if (item.getText() == null || item.getText().isEmpty()) {
                item.setText(fallbackText);
            }
        } else {
            item = new JMenuItem(fallbackText);
            item.setEnabled(false);
        }
        // Try to set an icon from IconManager
        ImageIcon icon = IconManager.getIcon(actionId);
        if (icon != null) {
            item.setIcon(icon);
        }
        // Keyboard accelerator
        if (keyCode != 0) {
            item.setAccelerator(KeyStroke.getKeyStroke(keyCode, modifiers));
        }
        menu.add(item);
    }

    private void configureMenu(JMenu menu) {
        NomadPlanUi.configureTopLevelMenu(menu);
    }

    private JMenu createTopLevelMenu(String text, int mnemonic) {
        JMenu menu = new HeaderMenu(text);
        configureMenu(menu);
        menu.setMnemonic(mnemonic);
        return menu;
    }

    private static final class HeaderMenu extends JMenu {
        private static final long serialVersionUID = 1L;

        private HeaderMenu(String text) {
            super(text);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            boolean selected = isSelected() || getModel().isArmed();
            boolean rollover = !selected && getModel().isRollover();
            java.awt.Color previous = getForeground();
            setForeground((selected || rollover)
                ? com.projectlibre1.theme.NomadPlanColors.accent()
                : com.projectlibre1.theme.NomadPlanColors.textPrimary());
            super.paintComponent(graphics);
            setForeground(previous);
            if (selected) {
                Graphics2D g2 = (Graphics2D) graphics.create();
                g2.setColor(com.projectlibre1.theme.NomadPlanColors.alpha(
                    com.projectlibre1.theme.NomadPlanColors.accent(),
                    180));
                g2.fillRoundRect(10, getHeight() - 2, Math.max(12, getWidth() - 20), 1, 1, 1);
                g2.dispose();
            }
        }
    }
}
