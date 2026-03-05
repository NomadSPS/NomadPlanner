package com.projectlibre1.theme;

import java.awt.Color;

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
    private static final Color L_GANTT_TASK      = new Color(0x0D9488);
    private static final Color L_GANTT_SUMMARY   = new Color(0x334155);
    private static final Color L_GANTT_MILESTONE = new Color(0x0D9488);
    private static final Color L_GANTT_PROGRESS  = new Color(0x0F766E);
    private static final Color L_GANTT_CRITICAL  = new Color(0xDC2626);
    private static final Color L_GANTT_BASELINE  = new Color(0xE2E8F0);
    private static final Color L_GANTT_LINK      = new Color(0x64748B);
    private static final Color L_GANTT_NONWORK   = new Color(0xF1F5F9);
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
