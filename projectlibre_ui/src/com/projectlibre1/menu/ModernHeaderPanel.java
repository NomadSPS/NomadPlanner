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

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.ImageIcon;

import com.projectlibre1.pm.graphic.IconManager;
import com.projectlibre1.theme.NomadPlanColors;
import com.projectlibre1.theme.NomadPlanThemeTokens;
import com.projectlibre1.theme.NomadPlanUi;
import com.projectlibre1.util.BrowserControl;

/**
 * Two-row modern shell header that keeps the required ProjectLibre logo adjacent
 * to the File menu while visually integrating the menu row and toolbar row.
 */
public class ModernHeaderPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final String PROJECTLIBRE_URL = "http://www.projectlibre.com/";
    private static final int BRAND_LOGO_WIDTH = 144;
    private static final int BRAND_LOGO_HEIGHT = 31;

    public ModernHeaderPanel(JMenuBar menuBar, JToolBar toolBar) {
        super(new BorderLayout());
        setOpaque(true);
        setBackground(NomadPlanColors.headerBackground());
        add(createTopRow(menuBar), BorderLayout.NORTH);
        add(createBottomRow(toolBar), BorderLayout.CENTER);
    }

    private JComponent createTopRow(JMenuBar menuBar) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(true);
        row.setBackground(NomadPlanColors.headerBackground());
        row.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        row.setPreferredSize(new Dimension(0, NomadPlanThemeTokens.headerMenuRowHeight()));
        row.add(createBrandBlock(), BorderLayout.WEST);
        NomadPlanUi.applyHeaderMenuBarStyle(menuBar);
        row.add(menuBar, BorderLayout.CENTER);
        return row;
    }

    private JComponent createBottomRow(JToolBar toolBar) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(true);
        row.setBackground(NomadPlanColors.headerBackground());
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, NomadPlanUi.subtleHeaderDivider()));
        row.setPreferredSize(new Dimension(0, NomadPlanThemeTokens.headerToolbarRowHeight()));
        row.add(createToolbarSpacer(), BorderLayout.WEST);
        NomadPlanUi.applyEmbeddedToolbarStyle(toolBar);
        row.add(toolBar, BorderLayout.CENTER);
        return row;
    }

    private Component createToolbarSpacer() {
        return Box.createRigidArea(new Dimension(
            NomadPlanThemeTokens.headerToolbarInsetWidth(),
            NomadPlanThemeTokens.headerToolbarRowHeight()));
    }

    private JComponent createBrandBlock() {
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        container.setPreferredSize(new Dimension(
            NomadPlanThemeTokens.headerBrandWidth(),
            NomadPlanThemeTokens.headerMenuRowHeight()));

        JButton brandButton = new JButton();
        configureBrandButton(brandButton);
        container.add(brandButton, BorderLayout.CENTER);
        return container;
    }

    private void configureBrandButton(AbstractButton button) {
        button.setBorder(BorderFactory.createEmptyBorder(
            1,
            2,
            1,
            2));
        button.setOpaque(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setToolTipText("ProjectLibre");
        button.setHorizontalAlignment(JButton.LEFT);
        button.setPreferredSize(new Dimension(
            NomadPlanThemeTokens.headerBrandWidth(),
            NomadPlanThemeTokens.headerMenuRowHeight()));

        Icon logo = IconManager.getIcon("logo.ProjectLibre");
        if (logo != null) {
            button.setIcon(scaleBrandIcon(logo));
        } else {
            button.setText("ProjectLibre");
        }

        button.addActionListener(event -> BrowserControl.displayURL(PROJECTLIBRE_URL));
    }

    private Icon scaleBrandIcon(Icon icon) {
        if (icon instanceof ImageIcon) {
            BufferedImage scaled = new BufferedImage(BRAND_LOGO_WIDTH, BRAND_LOGO_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = scaled.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Image source = ((ImageIcon) icon).getImage();
            g2.drawImage(source, 0, 0, BRAND_LOGO_WIDTH, BRAND_LOGO_HEIGHT, null);
            g2.dispose();
            return new ImageIcon(scaled);
        }
        return icon;
    }
}
