package com.projectlibre1.theme;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class CardSurfacePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public CardSurfacePanel() {
        this(null);
    }

    public CardSurfacePanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
        int shadowInset = NomadPlanThemeTokens.cardShadowInset();
        int shadowBottom = shadowInset + NomadPlanThemeTokens.cardShadowOffsetY();
        setBorder(BorderFactory.createEmptyBorder(shadowInset, shadowInset, shadowBottom, shadowInset));
    }

    private Shape createCardShape() {
        java.awt.Insets insets = getInsets();
        float x = insets.left;
        float y = insets.top;
        float width = Math.max(0, getWidth() - insets.left - insets.right);
        float height = Math.max(0, getHeight() - insets.top - insets.bottom);
        float arc = NomadPlanThemeTokens.cardArc();
        return new RoundRectangle2D.Float(x, y, width, height, arc, arc);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        java.awt.Insets insets = getInsets();
        float x = insets.left;
        float y = insets.top;
        float width = Math.max(0, getWidth() - insets.left - insets.right);
        float height = Math.max(0, getHeight() - insets.top - insets.bottom);
        float arc = NomadPlanThemeTokens.cardArc();

        for (int i = 2; i >= 1; i--) {
            float shadowY = y + NomadPlanThemeTokens.cardShadowOffsetY() - (i - 1);
            g2.setColor(NomadPlanThemeTokens.alpha(
                NomadPlanThemeTokens.cardShadowColor(),
                NomadPlanThemeTokens.cardShadowAlpha() / (i + 1)));
            g2.fill(new RoundRectangle2D.Float(
                x + 1 - i,
                shadowY,
                Math.max(0, width + (i * 2) - 2),
                Math.max(0, height + (i * 2) - 2),
                arc + i,
                arc + i));
        }

        RoundRectangle2D.Float card = new RoundRectangle2D.Float(x, y, width, height, arc, arc);
        g2.setColor(NomadPlanColors.background());
        g2.fill(card);
        g2.setColor(NomadPlanThemeTokens.cardBorderColor());
        g2.draw(card);
        g2.dispose();
    }

    @Override
    protected void paintChildren(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setClip(createCardShape());
        super.paintChildren(g2);
        g2.dispose();
    }
}
