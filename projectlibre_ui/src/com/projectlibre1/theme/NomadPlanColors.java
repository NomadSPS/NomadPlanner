package com.projectlibre1.theme;

import java.awt.Color;

public class NomadPlanColors {
    private static boolean darkMode = false;

    public static boolean isDarkMode() { return darkMode; }
    public static void setDarkMode(boolean dark) { darkMode = dark; }

    public static Color appBackground()  { return NomadPlanThemeTokens.appBackground(); }
    public static Color background()     { return NomadPlanThemeTokens.canvas(); }
    public static Color surface()        { return NomadPlanThemeTokens.surface(); }
    public static Color surfaceMuted()   { return NomadPlanThemeTokens.surfaceMuted(); }
    public static Color surfaceRaised()  { return NomadPlanThemeTokens.surfaceRaised(); }
    public static Color toolbarBackground() { return NomadPlanThemeTokens.toolbarBackground(); }
    public static Color headerBackground()  { return NomadPlanThemeTokens.headerBackground(); }
    public static Color headerSelectedBackground() { return NomadPlanThemeTokens.headerSelectedBackground(); }
    public static Color border()         { return NomadPlanThemeTokens.border(); }
    public static Color borderStrong()   { return NomadPlanThemeTokens.borderStrong(); }
    public static Color divider()        { return NomadPlanThemeTokens.divider(); }
    public static Color textPrimary()    { return NomadPlanThemeTokens.textPrimary(); }
    public static Color textSecondary()  { return NomadPlanThemeTokens.textSecondary(); }
    public static Color textMuted()      { return NomadPlanThemeTokens.textMuted(); }
    public static Color textInverse()    { return NomadPlanThemeTokens.textInverse(); }
    public static Color accent()         { return NomadPlanThemeTokens.accent(); }
    public static Color accentHover()    { return NomadPlanThemeTokens.accentHover(); }
    public static Color focusRing()      { return NomadPlanThemeTokens.focusRing(); }
    public static Color selectionFill()  { return NomadPlanThemeTokens.selectionFill(); }
    public static Color selectionText()  { return NomadPlanThemeTokens.selectionText(); }
    public static Color success()        { return NomadPlanThemeTokens.success(); }
    public static Color warning()        { return NomadPlanThemeTokens.warning(); }
    public static Color error()          { return NomadPlanThemeTokens.error(); }
    public static Color disabledText()   { return NomadPlanThemeTokens.disabledText(); }

    public static Color ganttCanvas()    { return NomadPlanThemeTokens.ganttCanvas(); }
    public static Color ganttRowStripe() { return NomadPlanThemeTokens.ganttRowStripe(); }
    public static Color ganttTask()      { return NomadPlanThemeTokens.ganttTask(); }
    public static Color ganttSummary()   { return NomadPlanThemeTokens.ganttSummary(); }
    public static Color ganttMilestone() { return NomadPlanThemeTokens.ganttMilestone(); }
    public static Color ganttProgress()  { return NomadPlanThemeTokens.ganttProgress(); }
    public static Color ganttCritical()  { return NomadPlanThemeTokens.ganttCritical(); }
    public static Color ganttBaseline()  { return NomadPlanThemeTokens.ganttBaseline(); }
    public static Color ganttLink()      { return NomadPlanThemeTokens.ganttLink(); }
    public static Color ganttNonWork()   { return NomadPlanThemeTokens.ganttNonWork(); }
    public static Color ganttProjectBoundary() { return NomadPlanThemeTokens.ganttProjectBoundary(); }
    public static Color ganttStatusLine()      { return NomadPlanThemeTokens.ganttStatusLine(); }

    public static Color alpha(Color color, int alpha) {
        return NomadPlanThemeTokens.alpha(color, alpha);
    }
}
