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
package com.projectlibre1.preference;

import java.awt.Color;
import java.util.prefs.Preferences;

import com.projectlibre1.document.ObjectEvent;
import com.projectlibre1.document.ObjectEventManager;

public class GlobalPreferences {
	public static final int WBS_COLOR_LEVEL_COUNT = 8;
	private static final String KEY_SHOW_ALL_RESOURCES = "showAllResources";
	private static final String KEY_HIDE_LEAF_OUTLINE_DOTS = "hideLeafOutlineDots";
	private static final String KEY_WBS_BACKGROUND_PREFIX = "wbsLevelBackground.";
	private static final String KEY_WBS_FOREGROUND_PREFIX = "wbsLevelForeground.";

	private final transient Preferences storedPreferences = Preferences.userNodeForPackage(GlobalPreferences.class);
	protected transient boolean showAllResources = true;
	protected transient boolean hideLeafOutlineDots = true;
	private final transient String[] wbsLevelBackgrounds = new String[WBS_COLOR_LEVEL_COUNT];
	private final transient String[] wbsLevelForegrounds = new String[WBS_COLOR_LEVEL_COUNT];

	public GlobalPreferences() {
		load();
	}

	private void load() {
		showAllResources = storedPreferences.getBoolean(KEY_SHOW_ALL_RESOURCES, true);
		hideLeafOutlineDots = storedPreferences.getBoolean(KEY_HIDE_LEAF_OUTLINE_DOTS, true);
		for (int i = 0; i < WBS_COLOR_LEVEL_COUNT; i++) {
			wbsLevelBackgrounds[i] = normalizeColorHex(storedPreferences.get(KEY_WBS_BACKGROUND_PREFIX + (i + 1), null));
			wbsLevelForegrounds[i] = normalizeColorHex(storedPreferences.get(KEY_WBS_FOREGROUND_PREFIX + (i + 1), null));
		}
	}

	public boolean isShowProjectResourcesOnly() {
		return !showAllResources;
	}

	public void setShowProjectResourcesOnly(boolean showProjectResourcesOnly) {
		if (showProjectResourcesOnly!=showAllResources) return;
		this.showAllResources = !showProjectResourcesOnly;
		storedPreferences.putBoolean(KEY_SHOW_ALL_RESOURCES, this.showAllResources);
		fireUpdateEvent(this, this);
	}

	public boolean isHideLeafOutlineDots() {
		return hideLeafOutlineDots;
	}

	public void setHideLeafOutlineDots(boolean hideLeafOutlineDots) {
		if (this.hideLeafOutlineDots == hideLeafOutlineDots) {
			return;
		}
		this.hideLeafOutlineDots = hideLeafOutlineDots;
		storedPreferences.putBoolean(KEY_HIDE_LEAF_OUTLINE_DOTS, hideLeafOutlineDots);
		fireUpdateEvent(this, this);
	}

	public String getWbsLevelBackground(int level) {
		return getLevelValue(wbsLevelBackgrounds, level);
	}

	public String getWbsLevelForeground(int level) {
		return getLevelValue(wbsLevelForegrounds, level);
	}

	public Color getWbsLevelBackgroundColor(int level) {
		return decodeColor(getWbsLevelBackground(level));
	}

	public Color getWbsLevelForegroundColor(int level) {
		return decodeColor(getWbsLevelForeground(level));
	}

	public String[] getWbsLevelBackgrounds() {
		return wbsLevelBackgrounds.clone();
	}

	public String[] getWbsLevelForegrounds() {
		return wbsLevelForegrounds.clone();
	}

	public void setWbsLevelColors(String[] backgrounds, String[] foregrounds) {
		if ((backgrounds == null) || (foregrounds == null)
			|| (backgrounds.length != WBS_COLOR_LEVEL_COUNT)
			|| (foregrounds.length != WBS_COLOR_LEVEL_COUNT)) {
			throw new IllegalArgumentException("Expected " + WBS_COLOR_LEVEL_COUNT + " WBS color levels");
		}
		boolean changed = false;
		for (int i = 0; i < WBS_COLOR_LEVEL_COUNT; i++) {
			String normalizedBackground = normalizeColorHex(backgrounds[i]);
			String normalizedForeground = normalizeColorHex(foregrounds[i]);
			if (!equals(wbsLevelBackgrounds[i], normalizedBackground)) {
				wbsLevelBackgrounds[i] = normalizedBackground;
				storeColor(KEY_WBS_BACKGROUND_PREFIX + (i + 1), normalizedBackground);
				changed = true;
			}
			if (!equals(wbsLevelForegrounds[i], normalizedForeground)) {
				wbsLevelForegrounds[i] = normalizedForeground;
				storeColor(KEY_WBS_FOREGROUND_PREFIX + (i + 1), normalizedForeground);
				changed = true;
			}
		}
		if (changed) {
			fireUpdateEvent(this, this);
		}
	}

	public void clearAllWbsLevelColors() {
		setWbsLevelColors(new String[WBS_COLOR_LEVEL_COUNT], new String[WBS_COLOR_LEVEL_COUNT]);
	}

	private void storeColor(String key, String value) {
		if (value == null) {
			storedPreferences.remove(key);
		} else {
			storedPreferences.put(key, value);
		}
	}

	private String getLevelValue(String[] values, int level) {
		if ((level < 1) || (level > WBS_COLOR_LEVEL_COUNT)) {
			return null;
		}
		return values[level - 1];
	}

	private static boolean equals(String left, String right) {
		return (left == null) ? (right == null) : left.equals(right);
	}

	public static String normalizeColorHex(String value) {
		if (value == null) {
			return null;
		}
		String trimmed = value.trim();
		if (trimmed.length() == 0) {
			return null;
		}
		Color color = decodeColor(trimmed);
		return (color == null) ? null : toColorHex(color);
	}

	public static Color decodeColor(String value) {
		String normalized = normalizeColorCandidate(value);
		if (normalized == null) {
			return null;
		}
		try {
			return Color.decode(normalized);
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	public static String toColorHex(Color color) {
		if (color == null) {
			return null;
		}
		return String.format("#%06X", color.getRGB() & 0xFFFFFF);
	}

	private static String normalizeColorCandidate(String value) {
		if (value == null) {
			return null;
		}
		String trimmed = value.trim();
		if (trimmed.length() == 0) {
			return null;
		}
		if (trimmed.startsWith("#") || trimmed.startsWith("0x") || trimmed.startsWith("0X")) {
			return trimmed;
		}
		if (trimmed.matches("[0-9A-Fa-f]{6}")) {
			return "#" + trimmed;
		}
		return trimmed;
	}
	
	private transient ObjectEventManager objectEventManager = new ObjectEventManager();
	/**
	 * @param listener
	 */
	public void addObjectListener(ObjectEvent.Listener listener) {
		objectEventManager.addListener(listener);
	}
	/**
	 * @param listener
	 */
	public void removeObjectListener(ObjectEvent.Listener listener) {
		objectEventManager.removeListener(listener);
	}	

	public ObjectEventManager getObjectEventManager() {
		return objectEventManager;
	}

	public void fireUpdateEvent(Object source, Object object) {
		objectEventManager.fireUpdateEvent(source,object);
	}

}
