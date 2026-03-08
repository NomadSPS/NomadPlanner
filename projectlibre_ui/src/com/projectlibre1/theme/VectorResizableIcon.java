package com.projectlibre1.theme;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;

/**
 * A ResizableIcon backed by a Java2D vector painter from ModernIcons.
 * Icons are painted on-demand and cached at each requested size.
 */
public class VectorResizableIcon implements ResizableIcon {

    private final ModernIcons.IconPainter painter;
    private int width;
    private int height;
    private BufferedImage cache;
    private int cacheW, cacheH;

    public VectorResizableIcon(ModernIcons.IconPainter painter, int width, int height) {
        this.painter = painter;
        this.width = width;
        this.height = height;
    }

    @Override
    public void setDimension(Dimension newDimension) {
        this.width = newDimension.width;
        this.height = newDimension.height;
        this.cache = null;
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (width <= 0 || height <= 0) return;

        // Paint fresh each time to respect theme changes (dark/light toggle)
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        painter.paint(g2d, width, height);
        g2d.dispose();
        g.drawImage(img, x, y, null);
    }
}
