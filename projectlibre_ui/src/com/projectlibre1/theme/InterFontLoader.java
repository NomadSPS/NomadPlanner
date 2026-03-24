package com.projectlibre1.theme;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.font.TextAttribute;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class InterFontLoader {
    private static final String RESOURCE_PATH = "/com/projectlibre1/theme/fonts/Inter-Variable.ttf";

    private static volatile boolean attempted;
    private static volatile Font baseFont;
    private static volatile String familyName;

    private InterFontLoader() {
    }

    public static void ensureRegistered() {
        if (attempted) {
            return;
        }
        synchronized (InterFontLoader.class) {
            if (attempted) {
                return;
            }
            attempted = true;
            try (InputStream stream = InterFontLoader.class.getResourceAsStream(RESOURCE_PATH)) {
                if (stream == null) {
                    return;
                }
                Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
                baseFont = font;
                familyName = font.getFamily();
            } catch (Exception ignored) {
                baseFont = null;
                familyName = null;
            }
        }
    }

    public static boolean isAvailable() {
        ensureRegistered();
        return baseFont != null && familyName != null;
    }

    public static String getFamilyName() {
        ensureRegistered();
        return familyName;
    }

    public static Font deriveFont(float size, int style) {
        ensureRegistered();
        if (baseFont == null) {
            return null;
        }
        Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>(baseFont.getAttributes());
        attributes.put(TextAttribute.SIZE, Float.valueOf(size));
        attributes.put(TextAttribute.WEIGHT,
            (style & Font.BOLD) != 0 ? TextAttribute.WEIGHT_SEMIBOLD : TextAttribute.WEIGHT_REGULAR);
        attributes.put(TextAttribute.POSTURE,
            (style & Font.ITALIC) != 0 ? TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR);
        return baseFont.deriveFont(attributes);
    }
}
