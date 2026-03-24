package com.projectlibre1.theme;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public final class NomadPlanUi {
    private NomadPlanUi() {
    }

    public static void applyToolbarStyle(JToolBar toolBar) {
        toolBar.setOpaque(true);
        toolBar.setBackground(NomadPlanThemeTokens.toolbarBackground());
        toolBar.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, subtleHeaderDivider()),
            new EmptyBorder(4, 6, 4, 6)));
        toolBar.setMargin(new Insets(0, 0, 0, 0));
    }

    public static void applyEmbeddedToolbarStyle(JToolBar toolBar) {
        toolBar.setOpaque(true);
        toolBar.setBackground(NomadPlanThemeTokens.headerBackground());
        toolBar.setBorder(BorderFactory.createEmptyBorder(3, 4, 3, 6));
        toolBar.setMargin(new Insets(0, 0, 0, 0));
    }

    public static void applyHeaderMenuBarStyle(JMenuBar menuBar) {
        menuBar.setOpaque(false);
        menuBar.setBackground(NomadPlanThemeTokens.headerBackground());
        menuBar.setForeground(NomadPlanThemeTokens.textPrimary());
        menuBar.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 4));
    }

    public static void configureTopLevelMenu(JMenu menu) {
        menu.setOpaque(false);
        menu.setBackground(NomadPlanThemeTokens.headerBackground());
        menu.setForeground(NomadPlanThemeTokens.textPrimary());
        menu.setBorder(BorderFactory.createEmptyBorder(
            NomadPlanThemeTokens.menuVerticalPadding(),
            NomadPlanThemeTokens.menuHorizontalPadding(),
            NomadPlanThemeTokens.menuVerticalPadding(),
            NomadPlanThemeTokens.menuHorizontalPadding()));
        menu.setMargin(new Insets(
            NomadPlanThemeTokens.menuVerticalPadding(),
            NomadPlanThemeTokens.menuHorizontalPadding(),
            NomadPlanThemeTokens.menuVerticalPadding(),
            NomadPlanThemeTokens.menuHorizontalPadding()));
        menu.setFont(menu.getFont().deriveFont(Font.PLAIN, 13.0f));
        menu.setFocusable(false);
        menu.setRolloverEnabled(true);
    }

    public static void configureToolbarButton(AbstractButton button) {
        int size = NomadPlanThemeTokens.toolbarButtonSize();
        button.setFocusable(false);
        button.setOpaque(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setBorder(createToolbarButtonBorder());
        button.setPreferredSize(new Dimension(size, size));
        button.setMinimumSize(new Dimension(size, size));
        button.setMaximumSize(new Dimension(size, size));
    }

    public static void configureToolbarTextButton(AbstractButton button) {
        int height = NomadPlanThemeTokens.toolbarButtonSize();
        button.setFocusable(false);
        button.setOpaque(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setBorder(new CompoundBorder(
            new EmptyBorder(0, 0, 0, 0),
            new EmptyBorder(0, NomadPlanThemeTokens.spacingSm(), 0, NomadPlanThemeTokens.spacingSm())));
        button.setForeground(NomadPlanThemeTokens.textPrimary());
        button.setIconTextGap(6);
        button.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Dimension preferred = button.getPreferredSize();
        int width = Math.max(preferred.width, height + 26);
        button.setPreferredSize(new Dimension(width, height));
        button.setMinimumSize(new Dimension(width, height));
        button.setMaximumSize(new Dimension(width, height));
    }

    public static JLabel createToolbarCaption(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 12.5f));
        label.setForeground(NomadPlanThemeTokens.textSecondary());
        label.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 4));
        return label;
    }

    public static void configureDialogButton(AbstractButton button, boolean primary) {
        button.setFocusable(false);
        button.setOpaque(true);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setForeground(primary ? NomadPlanThemeTokens.textInverse() : NomadPlanThemeTokens.textPrimary());
        button.setBackground(primary ? NomadPlanThemeTokens.accent() : NomadPlanThemeTokens.surfaceRaised());
        button.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(primary ? NomadPlanThemeTokens.accent() : NomadPlanThemeTokens.border()),
            new EmptyBorder(0, 12, 0, 12)));
        button.setIconTextGap(8);
        Dimension size = new Dimension(Math.max(96, button.getPreferredSize().width + 16), 36);
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);
    }

    public static void configureDialogActionButton(AbstractButton button, boolean primary) {
        configureDialogButton(button, primary);
        button.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        button.setIconTextGap(10);
        button.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(primary ? NomadPlanThemeTokens.accent() : NomadPlanThemeTokens.border()),
            new EmptyBorder(0, 14, 0, 14)));
        Dimension preferred = button.getPreferredSize();
        Dimension size = new Dimension(Math.max(236, preferred.width + 18), 38);
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, size.height));
    }

    public static Color subtleHeaderDivider() {
        return NomadPlanThemeTokens.alpha(NomadPlanThemeTokens.divider(), 112);
    }

    public static void applySplitPaneStyle(JSplitPane splitPane) {
        splitPane.setOpaque(false);
        splitPane.setBackground(NomadPlanThemeTokens.appBackground());
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setContinuousLayout(true);
        splitPane.setDividerSize(NomadPlanThemeTokens.splitDividerSize());
    }

    public static void applyTabbedPaneStyle(JTabbedPane tabbedPane) {
        tabbedPane.setOpaque(false);
        tabbedPane.setBackground(NomadPlanThemeTokens.canvas());
        tabbedPane.setBorder(BorderFactory.createEmptyBorder());
    }

    public static void applyWorkspaceBackground(JComponent component) {
        component.setOpaque(true);
        component.setBackground(NomadPlanThemeTokens.appBackground());
        component.setForeground(NomadPlanThemeTokens.textPrimary());
    }

    public static void applySurface(JComponent component) {
        component.setOpaque(true);
        component.setBackground(NomadPlanThemeTokens.canvas());
        component.setForeground(NomadPlanThemeTokens.textPrimary());
    }

    public static void applySubtleSurface(JComponent component) {
        component.setOpaque(true);
        component.setBackground(NomadPlanThemeTokens.surface());
        component.setForeground(NomadPlanThemeTokens.textPrimary());
    }

    public static void applyTableStyle(JTable table) {
        table.setBackground(NomadPlanThemeTokens.canvas());
        table.setForeground(NomadPlanThemeTokens.textPrimary());
        table.setSelectionBackground(NomadPlanThemeTokens.selectionFill());
        table.setSelectionForeground(NomadPlanThemeTokens.selectionText());
        table.setGridColor(NomadPlanThemeTokens.alpha(NomadPlanThemeTokens.border(), 96));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFillsViewportHeight(true);
        table.setRowMargin(0);
        if (table.getTableHeader() != null) {
            table.getTableHeader().setOpaque(true);
            table.getTableHeader().setBackground(NomadPlanThemeTokens.surfaceMuted());
            table.getTableHeader().setForeground(NomadPlanThemeTokens.textPrimary());
            table.getTableHeader().setBorder(new MatteBorder(0, 0, 1, 0, NomadPlanThemeTokens.divider()));
        }
    }

    public static void prepareScrollPaneForCard(JScrollPane scrollPane) {
        scrollPane.setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setOpaque(true);
        scrollPane.getViewport().setBackground(NomadPlanThemeTokens.canvas());

        JViewport columnHeader = scrollPane.getColumnHeader();
        if (columnHeader != null) {
            columnHeader.setOpaque(true);
            columnHeader.setBackground(NomadPlanThemeTokens.surfaceMuted());
        }
        JViewport rowHeader = scrollPane.getRowHeader();
        if (rowHeader != null) {
            rowHeader.setOpaque(true);
            rowHeader.setBackground(NomadPlanThemeTokens.surfaceMuted());
        }
    }

    public static CardSurfacePanel createCardPanel() {
        return new CardSurfacePanel(new BorderLayout());
    }

    public static CardSurfacePanel wrapInCard(JComponent component) {
        CardSurfacePanel panel = createCardPanel();
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    public static JPanel createWorkspacePane() {
        JPanel panel = new JPanel(new BorderLayout());
        applyWorkspaceBackground(panel);
        panel.setBorder(BorderFactory.createEmptyBorder(
            NomadPlanThemeTokens.workspacePadding(),
            NomadPlanThemeTokens.workspacePadding(),
            NomadPlanThemeTokens.workspacePadding(),
            NomadPlanThemeTokens.workspacePadding()));
        return panel;
    }

    public static Color groupedRowBackground(int level) {
        return (level & 1) == 0 ? NomadPlanThemeTokens.groupTintA() : NomadPlanThemeTokens.groupTintB();
    }

    public static Border createToolbarButtonBorder() {
        return new CompoundBorder(
            new EmptyBorder(0, 0, 0, 0),
            new EmptyBorder(
                3,
                3,
                3,
                3));
    }

    public static Border createHeaderBorder() {
        return new CompoundBorder(
            new MatteBorder(0, 0, 1, 1, NomadPlanThemeTokens.alpha(NomadPlanThemeTokens.divider(), 92)),
            new EmptyBorder(0, NomadPlanThemeTokens.spacingSm(), 0, NomadPlanThemeTokens.spacingSm()));
    }

    public static Border createHeaderCellBorder(boolean lastColumn) {
        return new CompoundBorder(
            new MatteBorder(0, 0, 1, lastColumn ? 0 : 1, NomadPlanThemeTokens.alpha(NomadPlanThemeTokens.divider(), lastColumn ? 92 : 72)),
            new EmptyBorder(0, NomadPlanThemeTokens.spacingSm(), 0, NomadPlanThemeTokens.spacingSm()));
    }

    public static Border createCellPaddingBorder() {
        return new EmptyBorder(0, NomadPlanThemeTokens.spacingSm(), 0, NomadPlanThemeTokens.spacingSm());
    }

    public static Border createTreeCellPaddingBorder() {
        return new EmptyBorder(0, 2, 0, NomadPlanThemeTokens.spacingSm());
    }

    public static Border createFocusBorder() {
        return new CompoundBorder(
            new MatteBorder(1, 1, 1, 1, NomadPlanThemeTokens.focusRing()),
            new EmptyBorder(0, NomadPlanThemeTokens.spacingSm() - 1, 0, NomadPlanThemeTokens.spacingSm() - 1));
    }

    public static Border createTreeCellFocusBorder() {
        return new CompoundBorder(
            new MatteBorder(1, 1, 1, 1, NomadPlanThemeTokens.focusRing()),
            new EmptyBorder(0, 1, 0, NomadPlanThemeTokens.spacingSm() - 1));
    }

    public static Border createSectionBorder() {
        return new CompoundBorder(
            new MatteBorder(1, 1, 1, 1, NomadPlanThemeTokens.border()),
            new EmptyBorder(
                NomadPlanThemeTokens.spacingMd(),
                NomadPlanThemeTokens.spacingMd(),
                NomadPlanThemeTokens.spacingMd(),
                NomadPlanThemeTokens.spacingMd()));
    }

    public static Border createFlatDialogBodyBorder() {
        return BorderFactory.createEmptyBorder(0, 0, 0, 0);
    }

    public static Border createDialogSectionBorder() {
        return BorderFactory.createEmptyBorder(
            NomadPlanThemeTokens.spacingXs(),
            0,
            0,
            0);
    }

    public static Border createPopupBorder() {
        return new CompoundBorder(
            BorderFactory.createLineBorder(NomadPlanThemeTokens.alpha(NomadPlanThemeTokens.borderStrong(), 180)),
            new EmptyBorder(4, 4, 4, 4));
    }

    public static Border createDialogChromeBorder() {
        return BorderFactory.createEmptyBorder(
            NomadPlanThemeTokens.dialogOuterPadding(),
            NomadPlanThemeTokens.dialogOuterPadding(),
            NomadPlanThemeTokens.dialogOuterPadding(),
            NomadPlanThemeTokens.dialogOuterPadding());
    }

    public static Border createDialogInnerBorder() {
        return BorderFactory.createEmptyBorder(
            NomadPlanThemeTokens.dialogInnerPadding(),
            NomadPlanThemeTokens.dialogInnerPadding(),
            NomadPlanThemeTokens.dialogInnerPadding(),
            NomadPlanThemeTokens.dialogInnerPadding());
    }

    public static JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(NomadPlanThemeTokens.textSecondary());
        label.setFont(label.getFont().deriveFont(Font.BOLD, 13f));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, NomadPlanThemeTokens.spacingXs(), 0));
        return label;
    }

    public static JComponent createSectionDivider() {
        JPanel divider = new JPanel();
        divider.setOpaque(true);
        divider.setBackground(NomadPlanThemeTokens.divider());
        divider.setPreferredSize(new Dimension(1, 1));
        divider.setMinimumSize(new Dimension(1, 1));
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return divider;
    }

    public static JPanel wrapDialogSection(String title, JComponent content) {
        JPanel panel = new JPanel(new BorderLayout(0, NomadPlanThemeTokens.spacingXs()));
        panel.setOpaque(false);
        panel.setBorder(createDialogSectionBorder());
        if (title != null && !title.isEmpty()) {
            panel.add(createSectionLabel(title), BorderLayout.NORTH);
        }
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    public static void prepareDialogScrollPane(JScrollPane scrollPane) {
        prepareScrollPaneForCard(scrollPane);
        scrollPane.setBorder(BorderFactory.createLineBorder(NomadPlanThemeTokens.alpha(NomadPlanThemeTokens.divider(), 140)));
    }

    public static void applyPopupSurface(JPopupMenu popupMenu) {
        popupMenu.setOpaque(true);
        popupMenu.setBackground(NomadPlanThemeTokens.canvas());
        popupMenu.setForeground(NomadPlanThemeTokens.textPrimary());
        popupMenu.setBorder(createPopupBorder());
    }

    public static void applyFlatDialogBody(JComponent component) {
        if (component == null) {
            return;
        }
        flattenDialogBody(component);
    }

    public static void configurePopupList(JList list) {
        list.setBackground(NomadPlanThemeTokens.canvas());
        list.setForeground(NomadPlanThemeTokens.textPrimary());
        list.setSelectionBackground(NomadPlanThemeTokens.selectionFill());
        list.setSelectionForeground(NomadPlanThemeTokens.selectionText());
    }

    private static void flattenDialogBody(Component component) {
        if (component instanceof JScrollPane) {
            prepareDialogScrollPane((JScrollPane) component);
        } else if (component instanceof JTabbedPane) {
            applyTabbedPaneStyle((JTabbedPane) component);
        } else if (component instanceof JSplitPane) {
            applySplitPaneStyle((JSplitPane) component);
        } else if (component instanceof JPanel) {
            ((JPanel) component).setOpaque(false);
        } else if (component instanceof AbstractButton) {
            ((AbstractButton) component).setOpaque(false);
        }

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                flattenDialogBody(child);
            }
        }
    }
}
