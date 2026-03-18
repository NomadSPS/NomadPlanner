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
 * Copyright (c) 2012-2019. All Rights Reserved. All portions of the code written by 
 * ProjectLibre are Copyright (c) 2012-2019. All Rights Reserved. Contributor 
 * ProjectLibre, Inc.
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
package com.projectlibre1.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.projectlibre1.pm.graphic.frames.GraphicManager;
import com.projectlibre1.preference.GlobalPreferences;

public final class WbsSummaryColorsDialog extends AbstractDialog {
	private static final long serialVersionUID = 1L;
	private static final Dimension SWATCH_SIZE = new Dimension(26, 18);

	private final GlobalPreferences preferences;
	private final Color[] backgroundColors = new Color[GlobalPreferences.WBS_COLOR_LEVEL_COUNT];
	private final Color[] foregroundColors = new Color[GlobalPreferences.WBS_COLOR_LEVEL_COUNT];
	private final JPanel[] backgroundSwatches = new JPanel[GlobalPreferences.WBS_COLOR_LEVEL_COUNT];
	private final JPanel[] foregroundSwatches = new JPanel[GlobalPreferences.WBS_COLOR_LEVEL_COUNT];

	public static void showDialog(GraphicManager graphicManager) {
		if (graphicManager == null) {
			return;
		}
		WbsSummaryColorsDialog dialog = new WbsSummaryColorsDialog(graphicManager.getFrame(), graphicManager.getPreferences());
		dialog.doModal();
	}

	private WbsSummaryColorsDialog(Frame owner, GlobalPreferences preferences) {
		super(owner, "WBS Summary Colors", true);
		this.preferences = preferences;
		loadFromPreferences();
	}

	private void loadFromPreferences() {
		for (int i = 0; i < GlobalPreferences.WBS_COLOR_LEVEL_COUNT; i++) {
			backgroundColors[i] = preferences.getWbsLevelBackgroundColor(i + 1);
			foregroundColors[i] = preferences.getWbsLevelForegroundColor(i + 1);
		}
	}

	public JComponent createContentPanel() {
		JPanel root = new JPanel(new BorderLayout(0, 12));
		root.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));

		JLabel description = new JLabel("Apply background and font colors to summary rows for WBS levels 1-8.");
		root.add(description, BorderLayout.NORTH);

		JPanel table = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(4, 4, 4, 4);
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;

		addHeader(table, constraints, 1, "Background");
		addHeader(table, constraints, 3, "Font");
		addHeader(table, constraints, 5, "Reset");

		for (int i = 0; i < GlobalPreferences.WBS_COLOR_LEVEL_COUNT; i++) {
			constraints.gridy = i + 1;
			constraints.gridx = 0;
			table.add(new JLabel("Level " + (i + 1)), constraints);

			constraints.gridx = 1;
			table.add(createColorButton("Background...", i, true), constraints);

			constraints.gridx = 2;
			backgroundSwatches[i] = createSwatch();
			table.add(backgroundSwatches[i], constraints);

			constraints.gridx = 3;
			table.add(createColorButton("Font...", i, false), constraints);

			constraints.gridx = 4;
			foregroundSwatches[i] = createSwatch();
			table.add(foregroundSwatches[i], constraints);

			constraints.gridx = 5;
			table.add(createClearButton(i), constraints);
		}

		root.add(table, BorderLayout.CENTER);
		refreshSwatches();
		return root;
	}

	private void addHeader(JPanel panel, GridBagConstraints constraints, int column, String text) {
		constraints.gridx = column;
		JLabel label = new JLabel(text);
		label.setFont(label.getFont().deriveFont(java.awt.Font.BOLD));
		panel.add(label, constraints);
	}

	private JButton createColorButton(String label, final int index, final boolean background) {
		JButton button = new JButton(label);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color initial = background ? backgroundColors[index] : foregroundColors[index];
				String title = "Level " + (index + 1) + (background ? " Background" : " Font");
				Color selected = JColorChooser.showDialog(WbsSummaryColorsDialog.this, title, initial);
				if (selected != null) {
					if (background) {
						backgroundColors[index] = selected;
					} else {
						foregroundColors[index] = selected;
					}
					refreshSwatches();
				}
			}
		});
		return button;
	}

	private JButton createClearButton(final int index) {
		JButton button = new JButton("Clear");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				backgroundColors[index] = null;
				foregroundColors[index] = null;
				refreshSwatches();
			}
		});
		return button;
	}

	private JPanel createSwatch() {
		JPanel swatch = new JPanel();
		swatch.setPreferredSize(SWATCH_SIZE);
		swatch.setMinimumSize(SWATCH_SIZE);
		swatch.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		return swatch;
	}

	private void refreshSwatches() {
		for (int i = 0; i < GlobalPreferences.WBS_COLOR_LEVEL_COUNT; i++) {
			updateSwatch(backgroundSwatches[i], backgroundColors[i]);
			updateSwatch(foregroundSwatches[i], foregroundColors[i]);
		}
	}

	private void updateSwatch(JPanel swatch, Color color) {
		if (swatch == null) {
			return;
		}
		swatch.setOpaque(true);
		swatch.setBackground((color == null) ? Color.WHITE : color);
		swatch.setToolTipText((color == null) ? "Default" : GlobalPreferences.toColorHex(color));
		swatch.repaint();
	}

	protected boolean bind(boolean get) {
		if (get) {
			loadFromPreferences();
			refreshSwatches();
			return true;
		}

		String[] backgrounds = new String[GlobalPreferences.WBS_COLOR_LEVEL_COUNT];
		String[] foregrounds = new String[GlobalPreferences.WBS_COLOR_LEVEL_COUNT];
		for (int i = 0; i < GlobalPreferences.WBS_COLOR_LEVEL_COUNT; i++) {
			backgrounds[i] = GlobalPreferences.toColorHex(backgroundColors[i]);
			foregrounds[i] = GlobalPreferences.toColorHex(foregroundColors[i]);
		}
		preferences.setWbsLevelColors(backgrounds, foregrounds);
		return true;
	}

	public ButtonPanel createButtonPanel() {
		createOkCancelButtons();
		ButtonPanel panel = new ButtonPanel();
		JButton clearAll = new JButton("Clear All");
		clearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < GlobalPreferences.WBS_COLOR_LEVEL_COUNT; i++) {
					backgroundColors[i] = null;
					foregroundColors[i] = null;
				}
				refreshSwatches();
			}
		});
		panel.addButton(clearAll);
		panel.addButton(ok);
		panel.addButton(cancel);
		return panel;
	}
}
