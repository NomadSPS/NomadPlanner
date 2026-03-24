package com.projectlibre1.theme;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.plaf.FontUIResource;

import com.projectlibre1.util.Environment;

public final class NomadPlanThemeTokens {
    private NomadPlanThemeTokens() {
    }

    private static boolean dark() {
        return NomadPlanColors.isDarkMode();
    }

    private static Set<String> availableFonts() {
        return new HashSet<String>(
            Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
    }

    public static Color appBackground() {
        return dark() ? new Color(0x171C24) : new Color(0xF3F5F7);
    }

    public static Color canvas() {
        return dark() ? new Color(0x1B2230) : Color.WHITE;
    }

    public static Color surface() {
        return dark() ? new Color(0x1E2634) : Color.WHITE;
    }

    public static Color surfaceMuted() {
        return dark() ? new Color(0x232D3D) : new Color(0xF7F9FB);
    }

    public static Color surfaceRaised() {
        return dark() ? new Color(0x20293A) : Color.WHITE;
    }

    public static Color toolbarBackground() {
        return dark() ? new Color(0x1E2634) : Color.WHITE;
    }

    public static Color headerBackground() {
        return toolbarBackground();
    }

    public static Color headerSelectedBackground() {
        return dark() ? new Color(0x143D38) : new Color(0xEEF7F5);
    }

    public static Color border() {
        return dark() ? new Color(0x364152) : new Color(0xE1E7EB);
    }

    public static Color borderStrong() {
        return dark() ? new Color(0x4A566A) : new Color(0xCDD7DD);
    }

    public static Color divider() {
        return dark() ? new Color(0x303A4B) : new Color(0xE7ECEF);
    }

    public static Color textPrimary() {
        return dark() ? new Color(0xE5EEF2) : new Color(0x1D3B47);
    }

    public static Color textSecondary() {
        return dark() ? new Color(0xA9BAC3) : new Color(0x536C76);
    }

    public static Color textMuted() {
        return dark() ? new Color(0x8395A0) : new Color(0x6E8088);
    }

    public static Color textInverse() {
        return dark() ? new Color(0x12232B) : new Color(0xF8FAFC);
    }

    public static Color accent() {
        return dark() ? new Color(0x38B9AC) : new Color(0x00897B);
    }

    public static Color accentHover() {
        return dark() ? new Color(0x46C7BA) : new Color(0x00796B);
    }

    public static Color focusRing() {
        return dark() ? new Color(0x5CD8CB) : new Color(0x1AA79A);
    }

    public static Color selectionFill() {
        return dark() ? new Color(0x17443F) : new Color(0xE2F3EF);
    }

    public static Color selectionText() {
        return textPrimary();
    }

    public static Color success() {
        return dark() ? new Color(0x58C777) : new Color(0x2E7D32);
    }

    public static Color warning() {
        return dark() ? new Color(0xFFAD58) : new Color(0xF57C00);
    }

    public static Color error() {
        return dark() ? new Color(0xF07972) : new Color(0xC84C42);
    }

    public static Color disabledText() {
        return dark() ? new Color(0x7F8E99) : new Color(0x95A4AC);
    }

    public static Color ganttCanvas() {
        return canvas();
    }

    public static Color ganttRowStripe() {
        return dark() ? new Color(0x202837) : new Color(0xF7FAFB);
    }

    public static Color ganttTask() {
        return dark() ? new Color(0x60C8BF) : new Color(0x4DB6AC);
    }

    public static Color ganttSummary() {
        return dark() ? new Color(0x9FB3BC) : new Color(0x2F6470);
    }

    public static Color ganttMilestone() {
        return dark() ? new Color(0xFFD86A) : new Color(0xFBC02D);
    }

    public static Color ganttProgress() {
        return dark() ? new Color(0x2FB2A6) : new Color(0x00897B);
    }

    public static Color ganttCritical() {
        return dark() ? new Color(0xF06D68) : new Color(0xE53935);
    }

    public static Color ganttBaseline() {
        return dark() ? new Color(0x334150) : new Color(0xD8E1E5);
    }

    public static Color ganttLink() {
        return dark() ? new Color(0x94A6AE) : new Color(0x6B7D86);
    }

    public static Color ganttNonWork() {
        return dark() ? new Color(0x202736) : new Color(0xF1F4F6);
    }

    public static Color ganttProjectBoundary() {
        return dark() ? new Color(0x9FB0B8) : new Color(0x90A2AB);
    }

    public static Color ganttStatusLine() {
        return accent();
    }

    public static Color groupTintA() {
        return dark() ? alpha(accent(), 24) : new Color(0xF5FBFA);
    }

    public static Color groupTintB() {
        return dark() ? alpha(ganttMilestone(), 20) : new Color(0xFFFBF1);
    }

    public static Color cardShadowColor() {
        return dark() ? Color.BLACK : new Color(0x182229);
    }

    public static Color cardBorderColor() {
        return dark() ? alpha(borderStrong(), 180) : alpha(border(), 220);
    }

    public static Color alpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static int spacingXs() {
        return 4;
    }

    public static int spacingSm() {
        return 8;
    }

    public static int spacingMd() {
        return 12;
    }

    public static int spacingLg() {
        return 16;
    }

    public static int denseRowHeight() {
        return 26;
    }

    public static int denseHeaderHeight() {
        return 30;
    }

    public static int workspacePadding() {
        return 10;
    }

    public static int workspaceGutter() {
        return 10;
    }

    public static int splitDividerSize() {
        return workspaceGutter();
    }

    public static int headerMenuRowHeight() {
        return 36;
    }

    public static int headerToolbarRowHeight() {
        return 40;
    }

    public static int headerBrandWidth() {
        return 152;
    }

    public static int headerToolbarInsetWidth() {
        return 8;
    }

    public static int menuHorizontalPadding() {
        return 10;
    }

    public static int menuVerticalPadding() {
        return 5;
    }

    public static int inputArc() {
        return 8;
    }

    public static int cardArc() {
        return 14;
    }

    public static int surfaceArc() {
        return cardArc();
    }

    public static int toolbarButtonSize() {
        return 32;
    }

    public static int cardShadowInset() {
        return 4;
    }

    public static int cardShadowOffsetY() {
        return 2;
    }

    public static int cardShadowAlpha() {
        return dark() ? 58 : 16;
    }

    public static int dialogOuterPadding() {
        return 12;
    }

    public static int dialogInnerPadding() {
        return 16;
    }

    public static FontUIResource uiFont(float size, int style) {
        Font inter = InterFontLoader.deriveFont(size, style);
        if (inter != null) {
            return new FontUIResource(inter);
        }
        return new FontUIResource(resolveFontFamily(), style, Math.round(size));
    }

    private static String resolveFontFamily() {
        InterFontLoader.ensureRegistered();
        if (InterFontLoader.isAvailable()) {
            return InterFontLoader.getFamilyName();
        }

        Set<String> fonts = availableFonts();
        if (Environment.isWindows()) {
            if (fonts.contains("Segoe UI Variable Text")) {
                return "Segoe UI Variable Text";
            }
            if (fonts.contains("Segoe UI")) {
                return "Segoe UI";
            }
        }
        if (Environment.isMac()) {
            if (fonts.contains("SF Pro Text")) {
                return "SF Pro Text";
            }
            if (fonts.contains("Helvetica Neue")) {
                return "Helvetica Neue";
            }
        }
        if (fonts.contains("Noto Sans")) {
            return "Noto Sans";
        }
        if (fonts.contains("Liberation Sans")) {
            return "Liberation Sans";
        }
        return "Dialog";
    }
}
