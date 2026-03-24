package com.projectlibre1.theme;

import java.awt.*;
import java.awt.geom.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;

/**
 * Modern flat vector icons painted with Java2D.
 * All icons use NomadPlanColors for automatic theme support.
 * Design: 2px stroke, rounded caps/joins, monoline style.
 */
public class ModernIcons {

    @FunctionalInterface
    public interface IconPainter {
        void paint(Graphics2D g, int w, int h);
    }

    private static final Map<String, IconPainter> PAINTERS = new HashMap<>();

    static {
        // File band
        PAINTERS.put("ribbon.save", ModernIcons::paintSave);
        PAINTERS.put("ribbon.open", ModernIcons::paintOpen);
        PAINTERS.put("ribbon.new", ModernIcons::paintNew);
        PAINTERS.put("ribbon.saveAs", ModernIcons::paintSaveAs);
        PAINTERS.put("ribbon.close", ModernIcons::paintClose);

        // Print band
        PAINTERS.put("ribbon.print", ModernIcons::paintPrint);
        PAINTERS.put("ribbon.printPreview", ModernIcons::paintPreview);
        PAINTERS.put("ribbon.pdf", ModernIcons::paintPdf);

        // Edit band
        PAINTERS.put("ribbon.paste", ModernIcons::paintPaste);
        PAINTERS.put("ribbon.cut", ModernIcons::paintCut);
        PAINTERS.put("ribbon.copy", ModernIcons::paintCopy);
        PAINTERS.put("ribbon.insert", ModernIcons::paintInsert);
        PAINTERS.put("ribbon.delete", ModernIcons::paintDelete);
        PAINTERS.put("ribbon.indent", ModernIcons::paintIndent);
        PAINTERS.put("ribbon.outdent", ModernIcons::paintOutdent);
        PAINTERS.put("ribbon.link", ModernIcons::paintLink);
        PAINTERS.put("ribbon.unlink", ModernIcons::paintUnlink);
        PAINTERS.put("ribbon.find", ModernIcons::paintFind);
        PAINTERS.put("ribbon.notes", ModernIcons::paintNotes);

        // Project band
        PAINTERS.put("ribbon.information", ModernIcons::paintInfo);
        PAINTERS.put("ribbon.calendar", ModernIcons::paintCalendar);
        PAINTERS.put("ribbon.multiproject", ModernIcons::paintMultiProject);
        PAINTERS.put("dialog.projects", ModernIcons::paintProjects);

        // Zoom
        PAINTERS.put("ribbon.zoomIn", ModernIcons::paintZoomIn);
        PAINTERS.put("ribbon.zoomOut", ModernIcons::paintZoomOut);

        // Help
        PAINTERS.put("ribbon.help", ModernIcons::paintHelp);
        PAINTERS.put("ribbon.hint", ModernIcons::paintHelp);

        // View icons
        PAINTERS.put("view.gantt", ModernIcons::paintViewGantt);
        PAINTERS.put("view.trackingGantt", ModernIcons::paintViewGantt);
        PAINTERS.put("view.network", ModernIcons::paintViewNetwork);
        PAINTERS.put("view.resources", ModernIcons::paintViewResources);
        PAINTERS.put("view.projects", ModernIcons::paintViewProjects);
        PAINTERS.put("view.WBS", ModernIcons::paintViewWbs);
        PAINTERS.put("view.RBS", ModernIcons::paintViewWbs);
        PAINTERS.put("view.histogram", ModernIcons::paintViewHistogram);
        PAINTERS.put("view.charts", ModernIcons::paintViewCharts);
        PAINTERS.put("view.taskDetails", ModernIcons::paintViewTaskDetails);
        PAINTERS.put("view.taskUsage", ModernIcons::paintViewTaskUsage);
        PAINTERS.put("view.resourceUsage", ModernIcons::paintViewResourceUsage);
        PAINTERS.put("view.report", ModernIcons::paintViewReport);
        PAINTERS.put("view.noSubWindow", ModernIcons::paintViewNoSub);

        // Ribbon locale
        PAINTERS.put("ribbon.locale", ModernIcons::paintGlobe);

        // Save all
        PAINTERS.put("ribbon.saveAll", ModernIcons::paintSaveAll);

        // Taskbar / menu24 icons (quick access toolbar)
        PAINTERS.put("menu24.save", ModernIcons::paintSave);
        PAINTERS.put("menu24.open", ModernIcons::paintOpen);
        PAINTERS.put("menu24.new", ModernIcons::paintNew);
        PAINTERS.put("menu24.saveAs", ModernIcons::paintSaveAs);
        PAINTERS.put("menu24.print", ModernIcons::paintPrint);
        PAINTERS.put("menu24.printPreview", ModernIcons::paintPreview);
        PAINTERS.put("menu24.undo", ModernIcons::paintUndo);
        PAINTERS.put("menu24.redo", ModernIcons::paintRedo);
        PAINTERS.put("menu24.cut", ModernIcons::paintCut);
        PAINTERS.put("menu24.copy", ModernIcons::paintCopy);
        PAINTERS.put("menu24.paste", ModernIcons::paintPaste);
        PAINTERS.put("menu24.delete", ModernIcons::paintDelete);
        PAINTERS.put("menu24.find", ModernIcons::paintFind);
        PAINTERS.put("menu24.zoomin", ModernIcons::paintZoomIn);
        PAINTERS.put("menu24.zoomout", ModernIcons::paintZoomOut);
        PAINTERS.put("menu24.link", ModernIcons::paintLink);
        PAINTERS.put("menu24.unlink", ModernIcons::paintUnlink);
        PAINTERS.put("menu24.indent", ModernIcons::paintIndent);
        PAINTERS.put("menu24.outdent", ModernIcons::paintOutdent);
        PAINTERS.put("menu24.help", ModernIcons::paintHelp);
        PAINTERS.put("menu24.taskInformation", ModernIcons::paintInfo);
        PAINTERS.put("menu24.resourceInformation", ModernIcons::paintViewResources);
        PAINTERS.put("menu24.projectInformation", ModernIcons::paintInfo);
        PAINTERS.put("menu24.notes", ModernIcons::paintNotes);
        PAINTERS.put("menu24.changeWorkingTime", ModernIcons::paintCalendar);
        PAINTERS.put("menu24.insertTask", ModernIcons::paintInsert);
        PAINTERS.put("menu24.insertResource", ModernIcons::paintInsert);
        PAINTERS.put("menu24.insertProject", ModernIcons::paintInsert);
        PAINTERS.put("menu24.closeProject", ModernIcons::paintClose);
        PAINTERS.put("menu24.PDF", ModernIcons::paintPdf);
        PAINTERS.put("menu24.locale", ModernIcons::paintGlobe);
        PAINTERS.put("menu24.assignResources", ModernIcons::paintViewResources);
        PAINTERS.put("menu24.scrollToTask", ModernIcons::paintScrollToTask);
        PAINTERS.put("menu24.refresh", ModernIcons::paintRefresh);
        PAINTERS.put("menu24.calendarManager", ModernIcons::paintCalendar);
        PAINTERS.put("menu24.workingTime", ModernIcons::paintWorkingTime);
        PAINTERS.put("menu24.taskDetails", ModernIcons::paintViewTaskDetails);
        PAINTERS.put("menu24.wbsSummaryColors", ModernIcons::paintWbsSummaryColors);
        PAINTERS.put("menu24.drivingPathBackward", ModernIcons::paintDrivingPathBackward);
        PAINTERS.put("menu24.drivingPathForward", ModernIcons::paintDrivingPathForward);
        PAINTERS.put("menu24.drivingPathBoth", ModernIcons::paintDrivingPathBoth);

        // Small menu icons (16px)
        PAINTERS.put("menu.save", ModernIcons::paintSave);
        PAINTERS.put("menu.open", ModernIcons::paintOpen);
        PAINTERS.put("menu.new", ModernIcons::paintNew);
        PAINTERS.put("menu.print", ModernIcons::paintPrint);
        PAINTERS.put("menu.printPreview", ModernIcons::paintPreview);
        PAINTERS.put("menu.undo", ModernIcons::paintUndo);
        PAINTERS.put("menu.redo", ModernIcons::paintRedo);
        PAINTERS.put("menu.cut", ModernIcons::paintCut);
        PAINTERS.put("menu.copy", ModernIcons::paintCopy);
        PAINTERS.put("menu.paste", ModernIcons::paintPaste);
        PAINTERS.put("menu.delete", ModernIcons::paintDelete);
        PAINTERS.put("menu.find", ModernIcons::paintFind);
        PAINTERS.put("menu.zoomin", ModernIcons::paintZoomIn);
        PAINTERS.put("menu.zoomout", ModernIcons::paintZoomOut);
        PAINTERS.put("menu.Help", ModernIcons::paintHelp);
        PAINTERS.put("menu.Information", ModernIcons::paintInfo);
        PAINTERS.put("menu.taskInformation", ModernIcons::paintInfo);
        PAINTERS.put("menu.notes", ModernIcons::paintNotes);
        PAINTERS.put("menu.link", ModernIcons::paintLink);
        PAINTERS.put("menu.unlink", ModernIcons::paintUnlink);
        PAINTERS.put("menu.calendarManager", ModernIcons::paintCalendar);
        PAINTERS.put("menu.workingTime", ModernIcons::paintWorkingTime);
        PAINTERS.put("menu.changeWorkingTime", ModernIcons::paintCalendar);
        PAINTERS.put("menu.assignResources", ModernIcons::paintViewResources);
        PAINTERS.put("WbsSummaryColors", ModernIcons::paintWbsSummaryColors);
        PAINTERS.put("DrivingPathBackward", ModernIcons::paintDrivingPathBackward);
        PAINTERS.put("DrivingPathForward", ModernIcons::paintDrivingPathForward);
        PAINTERS.put("DrivingPathBoth", ModernIcons::paintDrivingPathBoth);

        // --- Stage 2: Task Indicators ---
        PAINTERS.put("indicator.completed", ModernIcons::paintIndicatorCompleted);
        PAINTERS.put("indicator.note", ModernIcons::paintIndicatorNote);
        PAINTERS.put("indicator.constraint", ModernIcons::paintIndicatorConstraint);
        PAINTERS.put("indicator.calendar", ModernIcons::paintIndicatorCalendar);
        PAINTERS.put("indicator.missedDeadline", ModernIcons::paintIndicatorMissedDeadline);
        PAINTERS.put("indicator.delegated", ModernIcons::paintIndicatorDelegated);
        PAINTERS.put("indicator.delegatedMe", ModernIcons::paintIndicatorDelegatedMe);
        PAINTERS.put("indicator.subproject", ModernIcons::paintIndicatorSubproject);
        PAINTERS.put("indicator.invalidCalendar", ModernIcons::paintIndicatorInvalidCalendar);
        PAINTERS.put("indicator.invalidProject", ModernIcons::paintIndicatorInvalidProject);
        PAINTERS.put("indicator.parentAssignment", ModernIcons::paintIndicatorParentAssignment);
        PAINTERS.put("indicator.team", ModernIcons::paintIndicatorTeam);
        PAINTERS.put("indicator.user.admin", ModernIcons::paintIndicatorUserAdmin);
        PAINTERS.put("indicator.user.pm", ModernIcons::paintIndicatorUserPm);
        PAINTERS.put("indicator.user.tm", ModernIcons::paintIndicatorUserTm);
        PAINTERS.put("indicator.user.external_pm", ModernIcons::paintIndicatorUserPm);
        PAINTERS.put("indicator.user.external_tm", ModernIcons::paintIndicatorUserTm);
        PAINTERS.put("indicator.user.inactive", ModernIcons::paintIndicatorUserInactive);

        // --- Stage 2: Color Circle Indicators ---
        PAINTERS.put("greenCircle", (g, w, h) -> paintCircle(g, w, h, new Color(0x4CAF50)));
        PAINTERS.put("yellowCircle", (g, w, h) -> paintCircle(g, w, h, new Color(0xFFC107)));
        PAINTERS.put("redCircle", (g, w, h) -> paintCircle(g, w, h, new Color(0xF44336)));
        PAINTERS.put("grayCircle", (g, w, h) -> paintCircle(g, w, h, secondary()));

        // --- Stage 2: Expand/Collapse ---
        PAINTERS.put("menu.expand", ModernIcons::paintExpand);
        PAINTERS.put("menu.collapse", ModernIcons::paintCollapse);
        PAINTERS.put("menu24.expand", ModernIcons::paintExpand);
        PAINTERS.put("menu24.collapse", ModernIcons::paintCollapse);

        // --- Stage 2: Table/Column Operations ---
        PAINTERS.put("menu.insertTask", ModernIcons::paintInsert);
        PAINTERS.put("menu.insertColumn", ModernIcons::paintInsertColumn);
        PAINTERS.put("menu.hideColumn", ModernIcons::paintHideColumn);
        PAINTERS.put("menu.properties", ModernIcons::paintProperties);

        // --- Stage 2: Navigation Arrows ---
        PAINTERS.put("menu.leftArrow", ModernIcons::paintLeftArrow);
        PAINTERS.put("menu.rightArrow", ModernIcons::paintRightArrow);
        PAINTERS.put("menu.showAllResourcesSmall", ModernIcons::paintViewResources);
        PAINTERS.put("menu.showTeamResourcesSmall", ModernIcons::paintViewResources);

        // --- Stage 2: Calendar Navigation ---
        PAINTERS.put("calendar.back", ModernIcons::paintCalendarBack);
        PAINTERS.put("calendar.forward", ModernIcons::paintCalendarForward);
        PAINTERS.put("calendar.today", ModernIcons::paintCalendarToday);

        // --- Stage 2: Print Preview ---
        PAINTERS.put("print.down", ModernIcons::paintPrintDown);
        PAINTERS.put("print.format", ModernIcons::paintPrintFormat);
        PAINTERS.put("print.leftView", ModernIcons::paintPrintLeftView);
        PAINTERS.put("print.leftViewHidden", ModernIcons::paintPrintLeftViewHidden);
        PAINTERS.put("print.rightView", ModernIcons::paintPrintRightView);
        PAINTERS.put("print.rightViewHidden", ModernIcons::paintPrintRightViewHidden);

        // --- Stage 2: Resource Toggles ---
        PAINTERS.put("menu24.showAllResources", ModernIcons::paintViewResources);
        PAINTERS.put("menu24.showTeamResources", ModernIcons::paintIndicatorTeam);

        // --- Stage 2: Dialog Icons ---
        PAINTERS.put("dialog.ok", ModernIcons::paintDialogOk);
        PAINTERS.put("dialog.cancel", ModernIcons::paintDialogCancel);

        // --- Stage 2: System/Misc ---
        PAINTERS.put("application.icon", ModernIcons::paintAppGlyph);
        PAINTERS.put("menu16.locale", ModernIcons::paintGlobe);
        PAINTERS.put("image.down", ModernIcons::paintArrowDown);
        PAINTERS.put("image.up", ModernIcons::paintArrowUp);
        PAINTERS.put("man", ModernIcons::paintPerson);

        // --- Stage 3: Spreadsheet tree icons ---
        PAINTERS.put("spreadsheet.leaf.icon", ModernIcons::paintLeaf);
        PAINTERS.put("spreadsheet.emptyleaf.icon", ModernIcons::paintLeaf);
        PAINTERS.put("spreadsheet.collapsed.icon", ModernIcons::paintCollapse);
        PAINTERS.put("spreadsheet.expanded.icon", ModernIcons::paintExpand);

        // --- Stage 3: Timescale zoom ---
        PAINTERS.put("timescale.zoomIn.icon", ModernIcons::paintZoomIn);
        PAINTERS.put("timescale.zoomOut.icon", ModernIcons::paintZoomOut);

        // --- Stage 3: Menu items missing vector ---
        PAINTERS.put("menu.scrollToTask", ModernIcons::paintScrollToTask);
        PAINTERS.put("menu.import", ModernIcons::paintImport);
        PAINTERS.put("menu.export", ModernIcons::paintExport);
        PAINTERS.put("menu.replace", ModernIcons::paintReplace);
        PAINTERS.put("menu.PDF", ModernIcons::paintPdf);

        // --- Stage 3: Print preview extras ---
        PAINTERS.put("print.print", ModernIcons::paintPrint);
        PAINTERS.put("print.zoomIn", ModernIcons::paintZoomIn);
        PAINTERS.put("print.zoomOut", ModernIcons::paintZoomOut);
        PAINTERS.put("print.zoomReset", ModernIcons::paintZoomReset);
        PAINTERS.put("print.back", ModernIcons::paintLeftArrow);
        PAINTERS.put("print.forward", ModernIcons::paintRightArrow);
        PAINTERS.put("print.up", ModernIcons::paintArrowUp);
        PAINTERS.put("print.first", ModernIcons::paintStepBack);
        PAINTERS.put("print.last", ModernIcons::paintStepForward);
        PAINTERS.put("print.PDF", ModernIcons::paintPdf);

        // --- Stage 3: Menu24 extras ---
        PAINTERS.put("menu24.delegateTasks", ModernIcons::paintIndicatorDelegated);
        PAINTERS.put("menu24.import", ModernIcons::paintImport);
        PAINTERS.put("menu24.export", ModernIcons::paintExport);

        // --- Stage 3: Misc ---
        PAINTERS.put("format.other", ModernIcons::paintFormatOther);

        // --- Stage 4: Remaining bitmap replacements ---
        PAINTERS.put("infomation.icon", ModernIcons::paintInfo);
        PAINTERS.put("network.icon", ModernIcons::paintNetworkView);
        PAINTERS.put("spreadsheet.fetchedLazyCollapsed.icon", ModernIcons::paintCollapse);
        PAINTERS.put("spreadsheet.fetchedLazyExpanded.icon", ModernIcons::paintExpand);
        PAINTERS.put("spreadsheet.unfetchedLazy.icon", ModernIcons::paintUnfetchedLazy);
        PAINTERS.put("view.calendar", ModernIcons::paintCalendar);
    }

    public static IconPainter getPainter(String key) {
        return PAINTERS.get(key);
    }

    public static boolean hasIcon(String key) {
        return PAINTERS.containsKey(key);
    }

    // --- Shared helpers ---

    private static void setupStroke(Graphics2D g, float w, float strokeScale) {
        float sw = Math.max(1.5f, w * strokeScale);
        g.setStroke(new BasicStroke(sw, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }

    private static void setup(Graphics2D g, int w) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        setupStroke(g, w, 0.045f);
    }

    private static Color fg() { return NomadPlanColors.textPrimary(); }
    private static Color accent() { return NomadPlanColors.accent(); }
    private static Color secondary() { return NomadPlanColors.textSecondary(); }
    private static Color surface() { return NomadPlanColors.surface(); }

    // --- Document base shape (used by several icons) ---

    private static void paintDocOutline(Graphics2D g, int w, int h, float inset) {
        float m = w * inset;
        float dw = w - 2 * m;
        float dh = h - 2 * m;
        float fold = dw * 0.25f;
        GeneralPath doc = new GeneralPath();
        doc.moveTo(m, m + 2);
        doc.lineTo(m + dw - fold, m);
        doc.lineTo(m + dw, m + fold);
        doc.lineTo(m + dw, m + dh - 2);
        doc.quadTo(m + dw, m + dh, m + dw - 2, m + dh);
        doc.lineTo(m + 2, m + dh);
        doc.quadTo(m, m + dh, m, m + dh - 2);
        doc.closePath();
        g.draw(doc);
        // fold line
        g.draw(new Line2D.Float(m + dw - fold, m, m + dw - fold, m + fold));
        g.draw(new Line2D.Float(m + dw - fold, m + fold, m + dw, m + fold));
    }

    // =========== FILE BAND ===========

    private static void paintSave(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(accent());
        float m = w * 0.15f;
        float s = w - 2 * m;
        RoundRectangle2D body = new RoundRectangle2D.Float(m, m, s, s, s * 0.15f, s * 0.15f);
        g.draw(body);
        // slot at top
        float slotW = s * 0.45f;
        float slotH = s * 0.2f;
        float slotX = m + (s - slotW) / 2;
        g.draw(new RoundRectangle2D.Float(slotX, m, slotW, slotH, 2, 2));
        // label area at bottom
        float labelM = s * 0.15f;
        float labelY = m + s * 0.55f;
        g.draw(new RoundRectangle2D.Float(m + labelM, labelY, s - 2 * labelM, s * 0.32f, 3, 3));
    }

    private static void paintOpen(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(accent());
        float m = w * 0.12f;
        float fw = w - 2 * m;
        float fh = h - 2 * m;
        // folder back
        GeneralPath folder = new GeneralPath();
        folder.moveTo(m, m + fh * 0.2f);
        folder.lineTo(m, m + fh);
        folder.lineTo(m + fw, m + fh);
        folder.lineTo(m + fw, m + fh * 0.35f);
        folder.lineTo(m + fw * 0.5f, m + fh * 0.35f);
        folder.lineTo(m + fw * 0.4f, m + fh * 0.2f);
        folder.closePath();
        g.draw(folder);
        // folder tab
        g.draw(new Line2D.Float(m, m + fh * 0.2f, m + fw * 0.4f, m + fh * 0.2f));
        // arrow up
        float ax = m + fw * 0.5f;
        float ay1 = m + fh * 0.5f;
        float ay2 = m + fh * 0.85f;
        g.draw(new Line2D.Float(ax, ay1, ax, ay2));
        float arr = fw * 0.12f;
        g.draw(new Line2D.Float(ax - arr, ay1 + arr, ax, ay1));
        g.draw(new Line2D.Float(ax + arr, ay1 + arr, ax, ay1));
    }

    private static void paintNew(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        paintDocOutline(g, w, h, 0.18f);
        // plus sign
        g.setColor(accent());
        float cx = w * 0.5f;
        float cy = h * 0.58f;
        float ps = w * 0.15f;
        g.draw(new Line2D.Float(cx - ps, cy, cx + ps, cy));
        g.draw(new Line2D.Float(cx, cy - ps, cx, cy + ps));
    }

    private static void paintSaveAs(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(accent());
        // smaller floppy
        float m = w * 0.12f;
        float s = w * 0.55f;
        RoundRectangle2D body = new RoundRectangle2D.Float(m, m, s, s, s * 0.15f, s * 0.15f);
        g.draw(body);
        float slotW = s * 0.4f;
        float slotH = s * 0.18f;
        float slotX = m + (s - slotW) / 2;
        g.draw(new RoundRectangle2D.Float(slotX, m, slotW, slotH, 2, 2));
        // pencil
        g.setColor(fg());
        float px = w * 0.55f;
        float py = h * 0.45f;
        float px2 = w * 0.85f;
        float py2 = h * 0.85f;
        g.draw(new Line2D.Float(px, py, px2, py2));
        float tipS = w * 0.06f;
        g.draw(new Line2D.Float(px - tipS, py + tipS, px + tipS, py - tipS));
    }

    private static void paintClose(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        paintDocOutline(g, w, h, 0.18f);
        // X mark
        g.setColor(NomadPlanColors.error());
        float cx = w * 0.5f;
        float cy = h * 0.58f;
        float xs = w * 0.12f;
        g.draw(new Line2D.Float(cx - xs, cy - xs, cx + xs, cy + xs));
        g.draw(new Line2D.Float(cx + xs, cy - xs, cx - xs, cy + xs));
    }

    // =========== PRINT BAND ===========

    private static void paintPrint(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.15f;
        // printer body
        float bodyY = h * 0.35f;
        float bodyH = h * 0.3f;
        g.draw(new RoundRectangle2D.Float(m, bodyY, w - 2 * m, bodyH, 4, 4));
        // paper top (input tray)
        float paperM = w * 0.25f;
        g.draw(new Rectangle2D.Float(paperM, m, w - 2 * paperM, bodyY - m + 2));
        // paper bottom (output)
        float outY = bodyY + bodyH - 2;
        g.draw(new Rectangle2D.Float(paperM, outY, w - 2 * paperM, h - m - outY));
        // lines on output paper
        g.setColor(secondary());
        float lineM = w * 0.32f;
        float lineY1 = outY + (h - m - outY) * 0.35f;
        float lineY2 = outY + (h - m - outY) * 0.6f;
        g.draw(new Line2D.Float(lineM, lineY1, w - lineM, lineY1));
        g.draw(new Line2D.Float(lineM, lineY2, w - lineM, lineY2));
    }

    private static void paintPreview(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        paintDocOutline(g, w, h, 0.18f);
        // magnifying glass
        g.setColor(accent());
        float cx = w * 0.55f;
        float cy = h * 0.55f;
        float r = w * 0.14f;
        g.draw(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));
        float handleLen = w * 0.1f;
        float hx = cx + r * 0.7f;
        float hy = cy + r * 0.7f;
        g.draw(new Line2D.Float(hx, hy, hx + handleLen, hy + handleLen));
    }

    private static void paintPdf(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(NomadPlanColors.error());
        paintDocOutline(g, w, h, 0.18f);
        // "PDF" text
        float fontSize = w * 0.22f;
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, Math.round(fontSize)));
        FontMetrics fm = g.getFontMetrics();
        String text = "PDF";
        int tw = fm.stringWidth(text);
        g.drawString(text, (w - tw) / 2f, h * 0.65f);
    }

    // =========== EDIT BAND ===========

    private static void paintPaste(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.15f;
        // clipboard
        g.draw(new RoundRectangle2D.Float(m, m + w * 0.1f, w - 2 * m, h - 2 * m - w * 0.05f, 4, 4));
        // clip at top
        float clipW = w * 0.25f;
        float clipH = w * 0.12f;
        g.draw(new RoundRectangle2D.Float((w - clipW) / 2, m, clipW, clipH + w * 0.1f, 3, 3));
        // lines on board
        g.setColor(secondary());
        float lineM = w * 0.28f;
        for (int i = 0; i < 3; i++) {
            float ly = h * 0.42f + i * w * 0.13f;
            g.draw(new Line2D.Float(lineM, ly, w - lineM, ly));
        }
    }

    private static void paintCut(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float cx = w * 0.5f;
        // scissors: two circles and crossing lines
        float r = w * 0.12f;
        float cy1 = h * 0.78f;
        float cy2 = h * 0.78f;
        float cx1 = w * 0.32f;
        float cx2 = w * 0.68f;
        g.draw(new Ellipse2D.Float(cx1 - r, cy1 - r, r * 2, r * 2));
        g.draw(new Ellipse2D.Float(cx2 - r, cy2 - r, r * 2, r * 2));
        // blades
        g.draw(new Line2D.Float(cx1, cy1 - r, cx2, h * 0.2f));
        g.draw(new Line2D.Float(cx2, cy2 - r, cx1, h * 0.2f));
    }

    private static void paintCopy(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.12f;
        float offset = w * 0.15f;
        // back doc
        g.draw(new RoundRectangle2D.Float(m + offset, m, w - 2 * m - offset, h - 2 * m - offset, 3, 3));
        // front doc
        g.setColor(accent());
        g.draw(new RoundRectangle2D.Float(m, m + offset, w - 2 * m - offset, h - 2 * m - offset, 3, 3));
    }

    private static void paintInsert(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(accent());
        float cx = w * 0.5f;
        float cy = h * 0.5f;
        float r = w * 0.32f;
        g.draw(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));
        float ps = r * 0.55f;
        g.draw(new Line2D.Float(cx - ps, cy, cx + ps, cy));
        g.draw(new Line2D.Float(cx, cy - ps, cx, cy + ps));
    }

    private static void paintDelete(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(NomadPlanColors.error());
        float cx = w * 0.5f;
        float cy = h * 0.5f;
        float r = w * 0.32f;
        g.draw(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));
        float ps = r * 0.55f;
        g.draw(new Line2D.Float(cx - ps, cy, cx + ps, cy));
    }

    private static void paintIndent(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.18f;
        // horizontal lines
        for (int i = 0; i < 3; i++) {
            float ly = m + i * w * 0.2f + w * 0.1f;
            g.draw(new Line2D.Float(w * 0.45f, ly, w - m, ly));
        }
        // right arrow
        g.setColor(accent());
        float ay = h * 0.5f;
        float ax1 = m;
        float ax2 = w * 0.35f;
        g.draw(new Line2D.Float(ax1, ay, ax2, ay));
        float arr = w * 0.08f;
        g.draw(new Line2D.Float(ax2 - arr, ay - arr, ax2, ay));
        g.draw(new Line2D.Float(ax2 - arr, ay + arr, ax2, ay));
    }

    private static void paintOutdent(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.18f;
        for (int i = 0; i < 3; i++) {
            float ly = m + i * w * 0.2f + w * 0.1f;
            g.draw(new Line2D.Float(w * 0.45f, ly, w - m, ly));
        }
        // left arrow
        g.setColor(accent());
        float ay = h * 0.5f;
        float ax1 = w * 0.35f;
        float ax2 = m;
        g.draw(new Line2D.Float(ax1, ay, ax2, ay));
        float arr = w * 0.08f;
        g.draw(new Line2D.Float(ax2 + arr, ay - arr, ax2, ay));
        g.draw(new Line2D.Float(ax2 + arr, ay + arr, ax2, ay));
    }

    private static void paintLink(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(accent());
        float m = w * 0.18f;
        float linkW = w * 0.22f;
        float linkH = w * 0.12f;
        // two chain links
        g.draw(new RoundRectangle2D.Float(m, h * 0.35f, linkW, linkH, linkH, linkH));
        g.draw(new RoundRectangle2D.Float(w - m - linkW, h * 0.53f, linkW, linkH, linkH, linkH));
        // connecting line
        g.draw(new Line2D.Float(m + linkW * 0.7f, h * 0.41f + linkH / 2, w - m - linkW * 0.7f, h * 0.53f + linkH / 2));
    }

    private static void paintUnlink(Graphics2D g, int w, int h) {
        paintLink(g, w, h);
        // break line (X)
        g.setColor(NomadPlanColors.error());
        float cx = w * 0.5f;
        float cy = h * 0.5f;
        float xs = w * 0.08f;
        setupStroke(g, w, 0.055f);
        g.draw(new Line2D.Float(cx - xs, cy - xs, cx + xs, cy + xs));
        g.draw(new Line2D.Float(cx + xs, cy - xs, cx - xs, cy + xs));
    }

    private static void paintFind(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float cx = w * 0.42f;
        float cy = h * 0.42f;
        float r = w * 0.22f;
        g.draw(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));
        // handle
        float hx = cx + r * 0.7f;
        float hy = cy + r * 0.7f;
        float handleLen = w * 0.22f;
        g.draw(new Line2D.Float(hx, hy, hx + handleLen, hy + handleLen));
    }

    private static void paintNotes(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.18f;
        g.draw(new RoundRectangle2D.Float(m, m, w - 2 * m, h - 2 * m, 4, 4));
        // lines
        g.setColor(secondary());
        float lineM = w * 0.28f;
        for (int i = 0; i < 4; i++) {
            float ly = m + w * 0.15f + i * w * 0.14f;
            float endX = (i == 3) ? w * 0.55f : w - lineM;
            g.draw(new Line2D.Float(lineM, ly, endX, ly));
        }
    }

    // =========== PROJECT BAND ===========

    private static void paintInfo(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(accent());
        float cx = w * 0.5f;
        float cy = h * 0.5f;
        float r = w * 0.32f;
        g.draw(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));
        // "i" letter
        float fontSize = w * 0.35f;
        g.setFont(new Font(Font.SERIF, Font.BOLD | Font.ITALIC, Math.round(fontSize)));
        FontMetrics fm = g.getFontMetrics();
        String text = "i";
        int tw = fm.stringWidth(text);
        g.drawString(text, cx - tw / 2f, cy + fm.getAscent() * 0.38f);
    }

    private static void paintCalendar(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.15f;
        float calW = w - 2 * m;
        float calH = h - 2 * m;
        // body
        g.draw(new RoundRectangle2D.Float(m, m + calH * 0.12f, calW, calH * 0.88f, 4, 4));
        // header bar
        g.setColor(accent());
        g.fill(new RoundRectangle2D.Float(m, m + calH * 0.12f, calW, calH * 0.2f, 4, 4));
        g.fill(new Rectangle2D.Float(m, m + calH * 0.22f, calW, calH * 0.1f));
        // rings
        g.setColor(fg());
        float ringY1 = m;
        float ringY2 = m + calH * 0.2f;
        g.draw(new Line2D.Float(m + calW * 0.3f, ringY1, m + calW * 0.3f, ringY2));
        g.draw(new Line2D.Float(m + calW * 0.7f, ringY1, m + calW * 0.7f, ringY2));
        // grid dots (days)
        g.setColor(secondary());
        float dotR = w * 0.025f;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 4; col++) {
                float dx = m + calW * (0.2f + col * 0.2f);
                float dy = m + calH * (0.45f + row * 0.17f);
                g.fill(new Ellipse2D.Float(dx - dotR, dy - dotR, dotR * 2, dotR * 2));
            }
        }
    }

    private static void paintWorkingTime(Graphics2D g, int w, int h) {
        setup(g, w);
        float cx = w * 0.42f;
        float cy = h * 0.52f;
        float r = w * 0.24f;

        g.setColor(fg());
        g.draw(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));

        g.setColor(accent());
        g.draw(new Line2D.Float(cx, cy, cx, cy - r * 0.58f));
        g.draw(new Line2D.Float(cx, cy, cx + r * 0.48f, cy));

        g.setColor(secondary());
        g.draw(new Line2D.Float(cx, cy - r * 0.92f, cx, cy - r * 0.72f));
        g.draw(new Line2D.Float(cx + r * 0.92f, cy, cx + r * 0.72f, cy));
        g.draw(new Line2D.Float(cx, cy + r * 0.92f, cx, cy + r * 0.72f));
        g.draw(new Line2D.Float(cx - r * 0.92f, cy, cx - r * 0.72f, cy));

        float panelX = w * 0.62f;
        float panelY = h * 0.28f;
        float panelW = w * 0.2f;
        float panelH = h * 0.46f;
        g.setColor(fg());
        g.draw(new RoundRectangle2D.Float(panelX, panelY, panelW, panelH, 3, 3));
        g.setColor(accent());
        g.draw(new Line2D.Float(panelX + panelW * 0.22f, panelY + panelH * 0.28f, panelX + panelW * 0.78f, panelY + panelH * 0.28f));
        g.draw(new Line2D.Float(panelX + panelW * 0.22f, panelY + panelH * 0.5f, panelX + panelW * 0.62f, panelY + panelH * 0.5f));
        g.draw(new Line2D.Float(panelX + panelW * 0.22f, panelY + panelH * 0.72f, panelX + panelW * 0.7f, panelY + panelH * 0.72f));
    }

    private static void paintMultiProject(Graphics2D g, int w, int h) {
        setup(g, w);
        float offset = w * 0.12f;
        // back doc
        g.setColor(secondary());
        g.draw(new RoundRectangle2D.Float(w * 0.22f + offset, w * 0.12f, w * 0.55f, h * 0.65f, 3, 3));
        // front doc
        g.setColor(fg());
        g.draw(new RoundRectangle2D.Float(w * 0.12f, w * 0.22f, w * 0.55f, h * 0.65f, 3, 3));
        // lines on front
        g.setColor(secondary());
        float lineM = w * 0.2f;
        for (int i = 0; i < 3; i++) {
            float ly = w * 0.38f + i * w * 0.13f;
            g.draw(new Line2D.Float(lineM, ly, w * 0.6f, ly));
        }
    }

    private static void paintProjects(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.15f;
        // grid
        float gw = w - 2 * m;
        float gh = h - 2 * m;
        g.draw(new RoundRectangle2D.Float(m, m, gw, gh, 3, 3));
        // vertical lines
        g.setColor(secondary());
        g.draw(new Line2D.Float(m + gw * 0.33f, m, m + gw * 0.33f, m + gh));
        g.draw(new Line2D.Float(m + gw * 0.66f, m, m + gw * 0.66f, m + gh));
        // horizontal lines
        g.draw(new Line2D.Float(m, m + gh * 0.33f, m + gw, m + gh * 0.33f));
        g.draw(new Line2D.Float(m, m + gh * 0.66f, m + gw, m + gh * 0.66f));
    }

    // =========== ZOOM ===========

    private static void paintZoomIn(Graphics2D g, int w, int h) {
        paintFind(g, w, h);
        g.setColor(accent());
        float cx = w * 0.42f;
        float cy = h * 0.42f;
        float ps = w * 0.1f;
        g.draw(new Line2D.Float(cx - ps, cy, cx + ps, cy));
        g.draw(new Line2D.Float(cx, cy - ps, cx, cy + ps));
    }

    private static void paintZoomOut(Graphics2D g, int w, int h) {
        paintFind(g, w, h);
        g.setColor(accent());
        float cx = w * 0.42f;
        float cy = h * 0.42f;
        float ps = w * 0.1f;
        g.draw(new Line2D.Float(cx - ps, cy, cx + ps, cy));
    }

    // =========== HELP ===========

    private static void paintHelp(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(accent());
        float cx = w * 0.5f;
        float cy = h * 0.5f;
        float r = w * 0.32f;
        g.draw(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));
        // "?"
        float fontSize = w * 0.35f;
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, Math.round(fontSize)));
        FontMetrics fm = g.getFontMetrics();
        String text = "?";
        int tw = fm.stringWidth(text);
        g.drawString(text, cx - tw / 2f, cy + fm.getAscent() * 0.38f);
    }

    // =========== VIEW ICONS (smaller, simpler) ===========

    private static void paintViewGantt(Graphics2D g, int w, int h) {
        setup(g, w);
        float m = w * 0.15f;
        float barH = h * 0.12f;
        float gap = h * 0.06f;
        Color[] colors = { accent(), fg(), accent(), secondary() };
        float[] widths = { 0.6f, 0.4f, 0.7f, 0.35f };
        float[] offsets = { 0f, 0.2f, 0.1f, 0.35f };
        for (int i = 0; i < 4; i++) {
            float by = m + i * (barH + gap);
            float bx = m + (w - 2 * m) * offsets[i];
            float bw = (w - 2 * m) * widths[i];
            g.setColor(colors[i]);
            g.fill(new RoundRectangle2D.Float(bx, by, bw, barH, barH * 0.5f, barH * 0.5f));
        }
    }

    private static void paintViewNetwork(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float bw = w * 0.25f;
        float bh = h * 0.2f;
        // three boxes in a tree
        float cx = w * 0.5f;
        // top
        g.draw(new RoundRectangle2D.Float(cx - bw / 2, h * 0.12f, bw, bh, 2, 2));
        // bottom left
        g.draw(new RoundRectangle2D.Float(w * 0.12f, h * 0.62f, bw, bh, 2, 2));
        // bottom right
        g.draw(new RoundRectangle2D.Float(w * 0.63f, h * 0.62f, bw, bh, 2, 2));
        // lines
        g.setColor(secondary());
        g.draw(new Line2D.Float(cx, h * 0.12f + bh, w * 0.12f + bw / 2, h * 0.62f));
        g.draw(new Line2D.Float(cx, h * 0.12f + bh, w * 0.63f + bw / 2, h * 0.62f));
    }

    private static void paintViewResources(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float cx = w * 0.5f;
        // head
        float headR = w * 0.15f;
        g.draw(new Ellipse2D.Float(cx - headR, h * 0.15f, headR * 2, headR * 2));
        // body
        g.draw(new Arc2D.Float(cx - w * 0.28f, h * 0.45f, w * 0.56f, h * 0.5f, 0, 180, Arc2D.OPEN));
    }

    private static void paintViewProjects(Graphics2D g, int w, int h) {
        paintProjects(g, w, h);
    }

    private static void paintViewWbs(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.15f;
        // tree structure: lines with dots
        float x1 = m + w * 0.1f;
        float x2 = m + w * 0.3f;
        float dotR = w * 0.04f;
        for (int i = 0; i < 4; i++) {
            float y = m + i * h * 0.18f + h * 0.05f;
            float indent = (i > 0) ? x2 : x1;
            g.setColor((i == 0) ? accent() : fg());
            g.fill(new Ellipse2D.Float(indent - dotR, y - dotR, dotR * 2, dotR * 2));
            g.draw(new Line2D.Float(indent + dotR * 2, y, w - m, y));
        }
        // vertical connector
        g.setColor(secondary());
        g.draw(new Line2D.Float(x2 - w * 0.05f, m + h * 0.05f + h * 0.18f - dotR, x2 - w * 0.05f, m + h * 0.05f + h * 0.54f));
    }

    private static void paintViewHistogram(Graphics2D g, int w, int h) {
        setup(g, w);
        float m = w * 0.15f;
        float baseY = h - m;
        float barW = (w - 2 * m) * 0.2f;
        float gap = barW * 0.3f;
        float[] heights = { 0.5f, 0.75f, 0.35f, 0.9f };
        for (int i = 0; i < 4; i++) {
            float bx = m + i * (barW + gap);
            float bh = (baseY - m) * heights[i];
            g.setColor(i == 3 ? accent() : fg());
            g.fill(new RoundRectangle2D.Float(bx, baseY - bh, barW, bh, 2, 2));
        }
        // baseline
        g.setColor(secondary());
        g.draw(new Line2D.Float(m, baseY, w - m, baseY));
    }

    private static void paintViewCharts(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(accent());
        float m = w * 0.18f;
        // pie chart (simple)
        float r = w * 0.3f;
        float cx = w * 0.5f;
        float cy = h * 0.5f;
        g.draw(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));
        // slice
        g.draw(new Line2D.Float(cx, cy, cx, cy - r));
        g.draw(new Line2D.Float(cx, cy, cx + r * 0.7f, cy + r * 0.7f));
        g.fill(new Arc2D.Float(cx - r, cy - r, r * 2, r * 2, 45, 45, Arc2D.PIE));
    }

    private static void paintViewTaskUsage(Graphics2D g, int w, int h) {
        setup(g, w);
        float m = w * 0.15f;
        // table-like: rows with mini bars
        g.setColor(fg());
        g.draw(new RoundRectangle2D.Float(m, m, w - 2 * m, h - 2 * m, 3, 3));
        // vertical divider
        float divX = m + (w - 2 * m) * 0.4f;
        g.setColor(secondary());
        g.draw(new Line2D.Float(divX, m, divX, h - m));
        // rows
        for (int i = 1; i < 4; i++) {
            float ly = m + i * (h - 2 * m) * 0.25f;
            g.draw(new Line2D.Float(m, ly, w - m, ly));
        }
        // mini bars on right side
        g.setColor(accent());
        float barH = (h - 2 * m) * 0.12f;
        float[] barWidths = { 0.8f, 0.5f, 0.65f };
        for (int i = 0; i < 3; i++) {
            float by = m + (i + 0.35f) * (h - 2 * m) * 0.25f;
            float bw = (w - m - divX - w * 0.05f) * barWidths[i];
            g.fill(new RoundRectangle2D.Float(divX + w * 0.03f, by, bw, barH, 2, 2));
        }
    }

    private static void paintViewTaskDetails(Graphics2D g, int w, int h) {
        setup(g, w);
        float m = w * 0.12f;
        float tabH = h * 0.2f;
        float bodyY = m + tabH * 0.65f;

        g.setColor(fg());
        g.draw(new RoundRectangle2D.Float(m, bodyY, w - 2 * m, h - bodyY - m, 4, 4));

        float tabGap = w * 0.03f;
        float tabW = (w - 2 * m - 2 * tabGap) / 3f;
        for (int i = 0; i < 3; i++) {
            float x = m + i * (tabW + tabGap);
            g.setColor(i == 0 ? accent() : secondary());
            g.draw(new RoundRectangle2D.Float(x, m, tabW, tabH, 3, 3));
        }

        g.setColor(secondary());
        float lineLeft = m + w * 0.08f;
        float lineRight = w - lineLeft;
        for (int i = 0; i < 3; i++) {
            float y = bodyY + h * 0.13f + i * h * 0.14f;
            g.draw(new Line2D.Float(lineLeft, y, lineRight, y));
        }
    }

    private static void paintViewResourceUsage(Graphics2D g, int w, int h) {
        paintViewTaskUsage(g, w, h);
    }

    private static void paintViewReport(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        paintDocOutline(g, w, h, 0.15f);
        // chart inside
        g.setColor(accent());
        float m = w * 0.25f;
        float baseY = h * 0.75f;
        float barW = w * 0.1f;
        float gap = w * 0.04f;
        for (int i = 0; i < 3; i++) {
            float bx = m + i * (barW + gap);
            float bh = (i == 1) ? h * 0.3f : h * 0.18f;
            g.fill(new Rectangle2D.Float(bx, baseY - bh, barW, bh));
        }
    }

    private static void paintViewNoSub(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.15f;
        g.draw(new RoundRectangle2D.Float(m, m, w - 2 * m, h - 2 * m, 4, 4));
        g.setColor(secondary());
        float lineM = w * 0.25f;
        for (int i = 0; i < 3; i++) {
            float ly = h * 0.35f + i * h * 0.12f;
            g.draw(new Line2D.Float(lineM, ly, w - lineM, ly));
        }
    }

    private static void paintGlobe(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float cx = w * 0.5f;
        float cy = h * 0.5f;
        float r = w * 0.32f;
        g.draw(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));
        // meridian (vertical ellipse)
        g.draw(new Ellipse2D.Float(cx - r * 0.4f, cy - r, r * 0.8f, r * 2));
        // equator
        g.draw(new Line2D.Float(cx - r, cy, cx + r, cy));
        // tropics
        g.setColor(secondary());
        g.draw(new Line2D.Float(cx - r * 0.85f, cy - r * 0.45f, cx + r * 0.85f, cy - r * 0.45f));
        g.draw(new Line2D.Float(cx - r * 0.85f, cy + r * 0.45f, cx + r * 0.85f, cy + r * 0.45f));
    }

    private static void paintUndo(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.15f;
        // arrow pointing left at top
        float arrowTip = m;
        float arrowY = h * 0.35f;
        float arrowLen = w * 0.22f;
        g.draw(new Line2D.Float(arrowTip, arrowY, arrowTip + arrowLen, arrowY - arrowLen * 0.7f));
        g.draw(new Line2D.Float(arrowTip, arrowY, arrowTip + arrowLen, arrowY + arrowLen * 0.7f));
        // curved path from arrow tip going right and down
        GeneralPath path = new GeneralPath();
        path.moveTo(arrowTip, arrowY);
        path.lineTo(w * 0.55f, arrowY);
        path.quadTo(w - m, arrowY, w - m, h * 0.55f);
        path.quadTo(w - m, h - m, w * 0.45f, h - m);
        g.draw(path);
    }

    private static void paintRedo(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.15f;
        // arrow pointing right at top
        float arrowTip = w - m;
        float arrowY = h * 0.35f;
        float arrowLen = w * 0.22f;
        g.draw(new Line2D.Float(arrowTip, arrowY, arrowTip - arrowLen, arrowY - arrowLen * 0.7f));
        g.draw(new Line2D.Float(arrowTip, arrowY, arrowTip - arrowLen, arrowY + arrowLen * 0.7f));
        // curved path from arrow tip going left and down
        GeneralPath path = new GeneralPath();
        path.moveTo(arrowTip, arrowY);
        path.lineTo(w * 0.45f, arrowY);
        path.quadTo(m, arrowY, m, h * 0.55f);
        path.quadTo(m, h - m, w * 0.55f, h - m);
        g.draw(path);
    }

    private static void paintScrollToTask(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.18f;
        // target/crosshair
        float cx = w * 0.5f;
        float cy = h * 0.5f;
        float r = w * 0.25f;
        g.draw(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));
        float inner = r * 0.5f;
        g.draw(new Ellipse2D.Float(cx - inner, cy - inner, inner * 2, inner * 2));
        // crosshair lines
        g.draw(new Line2D.Float(cx, cy - r - w * 0.05f, cx, cy - inner));
        g.draw(new Line2D.Float(cx, cy + inner, cx, cy + r + w * 0.05f));
        g.draw(new Line2D.Float(cx - r - w * 0.05f, cy, cx - inner, cy));
        g.draw(new Line2D.Float(cx + inner, cy, cx + r + w * 0.05f, cy));
    }

    private static void paintRefresh(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float cx = w * 0.5f;
        float cy = h * 0.5f;
        float r = w * 0.28f;
        // circular arrow
        g.draw(new Arc2D.Float(cx - r, cy - r, r * 2, r * 2, 30, 300, Arc2D.OPEN));
        // arrowhead at the end
        float arr = w * 0.08f;
        float ax = cx + r * 0.87f;
        float ay = cy - r * 0.5f;
        g.draw(new Line2D.Float(ax, ay, ax + arr, ay - arr * 0.3f));
        g.draw(new Line2D.Float(ax, ay, ax - arr * 0.3f, ay - arr));
    }

    private static void paintWbsSummaryColors(Graphics2D g, int w, int h) {
        setup(g, w);
        float left = w * 0.14f;
        float startY = h * 0.18f;
        float rowHeight = h * 0.14f;
        float rowGap = h * 0.1f;
        float indent = w * 0.06f;
        float chipWidth = w * 0.14f;
        Color[] fills = {
            accent(),
            new Color(0xF59E0B),
            new Color(0x60A5FA)
        };

        for (int i = 0; i < fills.length; i++) {
            float y = startY + i * (rowHeight + rowGap);
            float chipX = left + i * indent;
            g.setColor(fills[i]);
            g.fill(new RoundRectangle2D.Float(chipX, y, chipWidth, rowHeight, rowHeight * 0.45f, rowHeight * 0.45f));

            g.setColor(fg());
            float lineX = chipX + chipWidth + w * 0.08f;
            float lineY = y + rowHeight * 0.5f;
            float lineWidth = w * (0.42f - i * 0.05f);
            g.draw(new Line2D.Float(lineX, lineY, lineX + lineWidth, lineY));
        }

        float paletteSize = w * 0.22f;
        float paletteX = w - paletteSize - w * 0.14f;
        float paletteY = h - paletteSize - h * 0.14f;
        g.setColor(fg());
        g.draw(new Ellipse2D.Float(paletteX, paletteY, paletteSize, paletteSize));

        float swatch = paletteSize * 0.22f;
        g.setColor(accent());
        g.fill(new Rectangle2D.Float(paletteX + swatch * 0.55f, paletteY + swatch * 0.5f, swatch, swatch));
        g.setColor(new Color(0xF59E0B));
        g.fill(new Rectangle2D.Float(paletteX + swatch * 1.75f, paletteY + swatch * 0.55f, swatch, swatch));
        g.setColor(new Color(0x60A5FA));
        g.fill(new Rectangle2D.Float(paletteX + swatch * 1.15f, paletteY + swatch * 1.8f, swatch, swatch));
    }

    private static void paintDrivingPathBackward(Graphics2D g, int w, int h) {
        paintDrivingPath(g, w, h, -1);
    }

    private static void paintDrivingPathForward(Graphics2D g, int w, int h) {
        paintDrivingPath(g, w, h, 1);
    }

    private static void paintDrivingPathBoth(Graphics2D g, int w, int h) {
        setup(g, w);
        paintPathNodes(g, w, h);

        float cy = h * 0.5f;
        float left = w * 0.22f;
        float right = w * 0.78f;
        g.setColor(accent());
        drawArrow(g, right, cy - h * 0.18f, left, cy - h * 0.18f);
        g.setColor(new Color(0xF59E0B));
        drawArrow(g, left, cy + h * 0.18f, right, cy + h * 0.18f);
    }

    private static void paintDrivingPath(Graphics2D g, int w, int h, int direction) {
        setup(g, w);
        paintPathNodes(g, w, h);

        float cy = h * 0.5f;
        float left = w * 0.2f;
        float right = w * 0.8f;
        g.setColor(accent());
        if (direction < 0) {
            drawArrow(g, right, cy, left, cy);
        } else {
            drawArrow(g, left, cy, right, cy);
        }
    }

    private static void paintPathNodes(Graphics2D g, int w, int h) {
        float cy = h * 0.5f;
        float radius = w * 0.09f;
        float[] centers = { w * 0.22f, w * 0.5f, w * 0.78f };

        g.setColor(secondary());
        g.draw(new Line2D.Float(centers[0], cy, centers[2], cy));

        g.setColor(fg());
        for (int i = 0; i < centers.length; i++) {
            float cx = centers[i];
            if (i == 1) {
                g.setColor(accent());
                g.fill(new Ellipse2D.Float(cx - radius, cy - radius, radius * 2, radius * 2));
                g.setColor(surface());
                g.fill(new Ellipse2D.Float(cx - radius * 0.32f, cy - radius * 0.32f, radius * 0.64f, radius * 0.64f));
                g.setColor(accent());
            } else {
                g.setColor(fg());
                g.draw(new Ellipse2D.Float(cx - radius, cy - radius, radius * 2, radius * 2));
            }
        }
    }

    private static void drawArrow(Graphics2D g, float x1, float y1, float x2, float y2) {
        g.draw(new Line2D.Float(x1, y1, x2, y2));
        float dx = x2 - x1;
        float dy = y2 - y1;
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        if (length == 0f) {
            return;
        }
        float ux = dx / length;
        float uy = dy / length;
        float arrow = length * 0.16f;
        float wingX = -uy * arrow * 0.55f;
        float wingY = ux * arrow * 0.55f;
        g.draw(new Line2D.Float(x2, y2, x2 - ux * arrow + wingX, y2 - uy * arrow + wingY));
        g.draw(new Line2D.Float(x2, y2, x2 - ux * arrow - wingX, y2 - uy * arrow - wingY));
    }

    private static void paintSaveAll(Graphics2D g, int w, int h) {
        setup(g, w);
        // Two floppy disks stacked
        g.setColor(secondary());
        float m = w * 0.2f;
        float s = w * 0.55f;
        g.draw(new RoundRectangle2D.Float(m + w * 0.1f, m, s, s, s * 0.12f, s * 0.12f));
        g.setColor(accent());
        g.draw(new RoundRectangle2D.Float(m, m + w * 0.12f, s, s, s * 0.12f, s * 0.12f));
        float slotW = s * 0.4f;
        float slotH = s * 0.18f;
        float slotX = m + (s - slotW) / 2;
        g.draw(new RoundRectangle2D.Float(slotX, m + w * 0.12f, slotW, slotH, 2, 2));
    }

    private static void paintLogo(Graphics2D g, int w, int h) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        // "NomadPlan" in bold accent color, sized to fill the space
        g.setColor(accent());
        float fontSize = h * 0.72f;
        Font font = new Font("SansSerif", Font.BOLD, (int) fontSize);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        String text = "NomadPlan";
        int textW = fm.stringWidth(text);
        int x = (w - textW) / 2;
        int y = (h - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(text, x, y);
    }

    private static void paintAppGlyph(Graphics2D g, int w, int h) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        float inset = Math.max(1.5f, w * 0.12f);
        float size = Math.min(w, h) - (inset * 2);
        RoundRectangle2D body = new RoundRectangle2D.Float(
            (w - size) / 2f,
            (h - size) / 2f,
            size,
            size,
            size * 0.28f,
            size * 0.28f);
        g.setColor(NomadPlanColors.alpha(accent(), 56));
        g.fill(body);
        g.setColor(accent());
        g.setStroke(new BasicStroke(Math.max(1.6f, w * 0.06f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(body);

        float left = w * 0.3f;
        float center = w * 0.5f;
        float right = w * 0.7f;
        float top = h * 0.28f;
        float bottom = h * 0.72f;
        g.setColor(fg());
        g.draw(new Line2D.Float(left, top, left, bottom));
        g.draw(new Line2D.Float(left, top, center, bottom));
        g.draw(new Line2D.Float(center, bottom, right, top));
        g.draw(new Line2D.Float(right, top, right, bottom));
    }

    // =========== STAGE 2: TASK INDICATORS ===========

    private static void paintIndicatorCompleted(Graphics2D g, int w, int h) {
        setup(g, w);
        Color green = new Color(0x4CAF50);
        g.setColor(green);
        float cx = w * 0.5f, cy = h * 0.5f, r = w * 0.35f;
        g.fill(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));
        // white checkmark
        g.setColor(surface());
        setupStroke(g, w, 0.06f);
        g.draw(new Line2D.Float(w * 0.3f, h * 0.5f, w * 0.45f, h * 0.65f));
        g.draw(new Line2D.Float(w * 0.45f, h * 0.65f, w * 0.7f, h * 0.35f));
    }

    private static void paintIndicatorNote(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(accent());
        float m = w * 0.18f;
        // sticky note shape
        GeneralPath note = new GeneralPath();
        note.moveTo(m, m);
        note.lineTo(w - m, m);
        note.lineTo(w - m, h - m - w * 0.15f);
        note.lineTo(w - m - w * 0.15f, h - m);
        note.lineTo(m, h - m);
        note.closePath();
        g.draw(note);
        // fold
        g.draw(new Line2D.Float(w - m - w * 0.15f, h - m, w - m - w * 0.15f, h - m - w * 0.15f));
        g.draw(new Line2D.Float(w - m - w * 0.15f, h - m - w * 0.15f, w - m, h - m - w * 0.15f));
        // lines
        g.setColor(secondary());
        g.draw(new Line2D.Float(w * 0.3f, h * 0.38f, w * 0.7f, h * 0.38f));
        g.draw(new Line2D.Float(w * 0.3f, h * 0.55f, w * 0.6f, h * 0.55f));
    }

    private static void paintIndicatorConstraint(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float cx = w * 0.5f, cy = h * 0.5f;
        // lock body
        float bw = w * 0.4f, bh = h * 0.3f;
        g.draw(new RoundRectangle2D.Float(cx - bw / 2, cy, bw, bh, 3, 3));
        // shackle
        float sr = bw * 0.35f;
        g.draw(new Arc2D.Float(cx - sr, cy - sr * 1.2f, sr * 2, sr * 2, 0, 180, Arc2D.OPEN));
    }

    private static void paintIndicatorCalendar(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.15f;
        float cw = w - 2 * m, ch = h - 2 * m;
        g.draw(new RoundRectangle2D.Float(m, m + ch * 0.15f, cw, ch * 0.85f, 2, 2));
        // header
        g.setColor(accent());
        g.fill(new Rectangle2D.Float(m, m + ch * 0.15f, cw, ch * 0.2f));
        // rings
        g.setColor(fg());
        g.draw(new Line2D.Float(m + cw * 0.3f, m, m + cw * 0.3f, m + ch * 0.25f));
        g.draw(new Line2D.Float(m + cw * 0.7f, m, m + cw * 0.7f, m + ch * 0.25f));
        // dots
        g.setColor(secondary());
        float dotR = w * 0.03f;
        for (int row = 0; row < 2; row++)
            for (int col = 0; col < 3; col++) {
                float dx = m + cw * (0.25f + col * 0.25f);
                float dy = m + ch * (0.5f + row * 0.2f);
                g.fill(new Ellipse2D.Float(dx - dotR, dy - dotR, dotR * 2, dotR * 2));
            }
    }

    private static void paintIndicatorMissedDeadline(Graphics2D g, int w, int h) {
        setup(g, w);
        Color red = new Color(0xF44336);
        g.setColor(red);
        float m = w * 0.12f;
        // triangle
        GeneralPath tri = new GeneralPath();
        tri.moveTo(w * 0.5f, m);
        tri.lineTo(w - m, h - m);
        tri.lineTo(m, h - m);
        tri.closePath();
        g.draw(tri);
        // exclamation
        float cx = w * 0.5f;
        g.draw(new Line2D.Float(cx, h * 0.32f, cx, h * 0.6f));
        float dotR = w * 0.03f;
        g.fill(new Ellipse2D.Float(cx - dotR, h * 0.72f - dotR, dotR * 2, dotR * 2));
    }

    private static void paintIndicatorDelegated(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        // person on left
        float px = w * 0.32f, py = h * 0.28f, hr = w * 0.1f;
        g.draw(new Ellipse2D.Float(px - hr, py - hr, hr * 2, hr * 2));
        g.draw(new Arc2D.Float(px - w * 0.15f, h * 0.42f, w * 0.3f, h * 0.35f, 0, 180, Arc2D.OPEN));
        // arrow pointing right
        g.setColor(accent());
        float ax1 = w * 0.55f, ax2 = w * 0.82f, ay = h * 0.5f;
        g.draw(new Line2D.Float(ax1, ay, ax2, ay));
        float arr = w * 0.08f;
        g.draw(new Line2D.Float(ax2 - arr, ay - arr, ax2, ay));
        g.draw(new Line2D.Float(ax2 - arr, ay + arr, ax2, ay));
    }

    private static void paintIndicatorDelegatedMe(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        // person on right
        float px = w * 0.68f, py = h * 0.28f, hr = w * 0.1f;
        g.draw(new Ellipse2D.Float(px - hr, py - hr, hr * 2, hr * 2));
        g.draw(new Arc2D.Float(px - w * 0.15f, h * 0.42f, w * 0.3f, h * 0.35f, 0, 180, Arc2D.OPEN));
        // arrow pointing left
        g.setColor(accent());
        float ax1 = w * 0.45f, ax2 = w * 0.18f, ay = h * 0.5f;
        g.draw(new Line2D.Float(ax1, ay, ax2, ay));
        float arr = w * 0.08f;
        g.draw(new Line2D.Float(ax2 + arr, ay - arr, ax2, ay));
        g.draw(new Line2D.Float(ax2 + arr, ay + arr, ax2, ay));
    }

    private static void paintIndicatorSubproject(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.15f;
        // outer rectangle
        g.draw(new RoundRectangle2D.Float(m, m, w - 2 * m, h - 2 * m, 3, 3));
        // inner rectangle
        g.setColor(accent());
        float im = w * 0.3f;
        g.draw(new RoundRectangle2D.Float(im, im, w - 2 * im, h - 2 * im, 2, 2));
    }

    private static void paintIndicatorInvalidCalendar(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.15f;
        float cw = w - 2 * m, ch = h - 2 * m;
        g.draw(new RoundRectangle2D.Float(m, m + ch * 0.15f, cw, ch * 0.85f, 2, 2));
        g.draw(new Line2D.Float(m + cw * 0.3f, m, m + cw * 0.3f, m + ch * 0.25f));
        g.draw(new Line2D.Float(m + cw * 0.7f, m, m + cw * 0.7f, m + ch * 0.25f));
        // X overlay
        g.setColor(NomadPlanColors.error());
        setupStroke(g, w, 0.06f);
        g.draw(new Line2D.Float(w * 0.25f, h * 0.45f, w * 0.75f, h * 0.85f));
        g.draw(new Line2D.Float(w * 0.75f, h * 0.45f, w * 0.25f, h * 0.85f));
    }

    private static void paintIndicatorInvalidProject(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        paintDocOutline(g, w, h, 0.18f);
        // X overlay
        g.setColor(NomadPlanColors.error());
        setupStroke(g, w, 0.06f);
        float cx = w * 0.5f, cy = h * 0.58f, xs = w * 0.15f;
        g.draw(new Line2D.Float(cx - xs, cy - xs, cx + xs, cy + xs));
        g.draw(new Line2D.Float(cx + xs, cy - xs, cx - xs, cy + xs));
    }

    private static void paintIndicatorParentAssignment(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float cx = w * 0.5f;
        // head
        float hr = w * 0.12f;
        g.draw(new Ellipse2D.Float(cx - hr, h * 0.12f, hr * 2, hr * 2));
        // body
        g.draw(new Arc2D.Float(cx - w * 0.2f, h * 0.36f, w * 0.4f, h * 0.3f, 0, 180, Arc2D.OPEN));
        // down arrow
        g.setColor(accent());
        float ay1 = h * 0.62f, ay2 = h * 0.85f;
        g.draw(new Line2D.Float(cx, ay1, cx, ay2));
        float arr = w * 0.07f;
        g.draw(new Line2D.Float(cx - arr, ay2 - arr, cx, ay2));
        g.draw(new Line2D.Float(cx + arr, ay2 - arr, cx, ay2));
    }

    private static void paintIndicatorTeam(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        // left person
        float lx = w * 0.35f, hr = w * 0.09f;
        g.draw(new Ellipse2D.Float(lx - hr, h * 0.18f, hr * 2, hr * 2));
        g.draw(new Arc2D.Float(lx - w * 0.14f, h * 0.4f, w * 0.28f, h * 0.35f, 0, 180, Arc2D.OPEN));
        // right person
        float rx = w * 0.65f;
        g.draw(new Ellipse2D.Float(rx - hr, h * 0.18f, hr * 2, hr * 2));
        g.draw(new Arc2D.Float(rx - w * 0.14f, h * 0.4f, w * 0.28f, h * 0.35f, 0, 180, Arc2D.OPEN));
    }

    private static void paintPersonWithBadge(Graphics2D g, int w, int h, String badge) {
        setup(g, w);
        g.setColor(fg());
        float cx = w * 0.42f;
        float hr = w * 0.12f;
        g.draw(new Ellipse2D.Float(cx - hr, h * 0.15f, hr * 2, hr * 2));
        g.draw(new Arc2D.Float(cx - w * 0.2f, h * 0.42f, w * 0.4f, h * 0.4f, 0, 180, Arc2D.OPEN));
        // badge
        g.setColor(accent());
        float fontSize = w * 0.28f;
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, Math.round(fontSize)));
        g.drawString(badge, w * 0.65f, h * 0.42f);
    }

    private static void paintIndicatorUserAdmin(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float cx = w * 0.42f, hr = w * 0.12f;
        g.draw(new Ellipse2D.Float(cx - hr, h * 0.15f, hr * 2, hr * 2));
        g.draw(new Arc2D.Float(cx - w * 0.2f, h * 0.42f, w * 0.4f, h * 0.4f, 0, 180, Arc2D.OPEN));
        // star badge
        g.setColor(new Color(0xFFC107));
        float sx = w * 0.75f, sy = h * 0.3f, sr = w * 0.1f;
        GeneralPath star = new GeneralPath();
        for (int i = 0; i < 5; i++) {
            double angle = Math.PI / 2 + i * 2 * Math.PI / 5;
            float px = sx + (float)(sr * Math.cos(angle));
            float py = sy - (float)(sr * Math.sin(angle));
            if (i == 0) star.moveTo(px, py);
            else star.lineTo(px, py);
            angle += Math.PI / 5;
            float ix = sx + (float)(sr * 0.4 * Math.cos(angle));
            float iy = sy - (float)(sr * 0.4 * Math.sin(angle));
            star.lineTo(ix, iy);
        }
        star.closePath();
        g.fill(star);
    }

    private static void paintIndicatorUserPm(Graphics2D g, int w, int h) {
        paintPersonWithBadge(g, w, h, "P");
    }

    private static void paintIndicatorUserTm(Graphics2D g, int w, int h) {
        paintPersonWithBadge(g, w, h, "T");
    }

    private static void paintIndicatorUserInactive(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(secondary());
        float cx = w * 0.5f, hr = w * 0.13f;
        g.draw(new Ellipse2D.Float(cx - hr, h * 0.15f, hr * 2, hr * 2));
        g.draw(new Arc2D.Float(cx - w * 0.2f, h * 0.42f, w * 0.4f, h * 0.4f, 0, 180, Arc2D.OPEN));
    }

    // =========== STAGE 2: COLOR CIRCLES ===========

    private static void paintCircle(Graphics2D g, int w, int h, Color color) {
        setup(g, w);
        g.setColor(color);
        float m = w * 0.2f;
        g.fill(new Ellipse2D.Float(m, m, w - 2 * m, h - 2 * m));
    }

    // =========== STAGE 2: EXPAND/COLLAPSE ===========

    private static void paintExpand(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        // downward chevron
        float m = w * 0.25f;
        float cy = h * 0.42f;
        g.draw(new Line2D.Float(m, cy, w * 0.5f, h - m));
        g.draw(new Line2D.Float(w * 0.5f, h - m, w - m, cy));
    }

    private static void paintCollapse(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        // right chevron
        float m = w * 0.32f;
        float cx = w * 0.38f;
        g.draw(new Line2D.Float(cx, m, w - m, h * 0.5f));
        g.draw(new Line2D.Float(w - m, h * 0.5f, cx, h - m));
    }

    // =========== STAGE 2: TABLE/COLUMN OPERATIONS ===========

    private static void paintInsertColumn(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.15f;
        // table grid
        g.draw(new RoundRectangle2D.Float(m, m, w - 2 * m, h - 2 * m, 3, 3));
        float mid = w * 0.5f;
        g.draw(new Line2D.Float(mid, m, mid, h - m));
        g.draw(new Line2D.Float(m, h * 0.35f, w - m, h * 0.35f));
        // plus sign
        g.setColor(accent());
        float cx = w * 0.75f, cy = h * 0.68f, ps = w * 0.08f;
        g.draw(new Line2D.Float(cx - ps, cy, cx + ps, cy));
        g.draw(new Line2D.Float(cx, cy - ps, cx, cy + ps));
    }

    private static void paintHideColumn(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.15f;
        // table grid
        g.draw(new RoundRectangle2D.Float(m, m, w - 2 * m, h - 2 * m, 3, 3));
        float mid = w * 0.5f;
        g.draw(new Line2D.Float(mid, m, mid, h - m));
        g.draw(new Line2D.Float(m, h * 0.35f, w - m, h * 0.35f));
        // eye-slash (simplified: line through)
        g.setColor(NomadPlanColors.error());
        setupStroke(g, w, 0.05f);
        g.draw(new Line2D.Float(w * 0.2f, h * 0.2f, w * 0.8f, h * 0.8f));
    }

    private static void paintProperties(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float cx = w * 0.5f, cy = h * 0.5f, r = w * 0.3f;
        // gear outer circle
        g.draw(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));
        // inner circle
        float ir = r * 0.4f;
        g.draw(new Ellipse2D.Float(cx - ir, cy - ir, ir * 2, ir * 2));
        // teeth (6 lines radiating outward)
        for (int i = 0; i < 6; i++) {
            double angle = i * Math.PI / 3;
            float x1 = cx + (float)(r * 0.8 * Math.cos(angle));
            float y1 = cy + (float)(r * 0.8 * Math.sin(angle));
            float x2 = cx + (float)((r + w * 0.06f) * Math.cos(angle));
            float y2 = cy + (float)((r + w * 0.06f) * Math.sin(angle));
            g.draw(new Line2D.Float(x1, y1, x2, y2));
        }
    }

    // =========== STAGE 2: NAVIGATION ARROWS ===========

    private static void paintLeftArrow(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.3f, cy = h * 0.5f;
        g.draw(new Line2D.Float(w - m, m, m, cy));
        g.draw(new Line2D.Float(m, cy, w - m, h - m));
    }

    private static void paintRightArrow(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.3f, cy = h * 0.5f;
        g.draw(new Line2D.Float(m, m, w - m, cy));
        g.draw(new Line2D.Float(w - m, cy, m, h - m));
    }

    // =========== STAGE 2: CALENDAR NAVIGATION ===========

    private static void paintCalendarBack(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.3f, cy = h * 0.5f;
        g.draw(new Line2D.Float(w - m, m, m, cy));
        g.draw(new Line2D.Float(m, cy, w - m, h - m));
    }

    private static void paintCalendarForward(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.3f, cy = h * 0.5f;
        g.draw(new Line2D.Float(m, m, w - m, cy));
        g.draw(new Line2D.Float(w - m, cy, m, h - m));
    }

    private static void paintCalendarToday(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.15f;
        float cw = w - 2 * m, ch = h - 2 * m;
        g.draw(new RoundRectangle2D.Float(m, m + ch * 0.15f, cw, ch * 0.85f, 2, 2));
        g.setColor(accent());
        g.fill(new Rectangle2D.Float(m, m + ch * 0.15f, cw, ch * 0.2f));
        g.setColor(fg());
        g.draw(new Line2D.Float(m + cw * 0.3f, m, m + cw * 0.3f, m + ch * 0.25f));
        g.draw(new Line2D.Float(m + cw * 0.7f, m, m + cw * 0.7f, m + ch * 0.25f));
        // today dot
        g.setColor(accent());
        float dotR = w * 0.08f;
        float dx = w * 0.5f, dy = h * 0.65f;
        g.fill(new Ellipse2D.Float(dx - dotR, dy - dotR, dotR * 2, dotR * 2));
    }

    // =========== STAGE 2: PRINT PREVIEW ===========

    private static void paintPrintDown(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float cx = w * 0.5f, m = w * 0.25f;
        g.draw(new Line2D.Float(cx, m, cx, h - m));
        float arr = w * 0.12f;
        g.draw(new Line2D.Float(cx - arr, h - m - arr, cx, h - m));
        g.draw(new Line2D.Float(cx + arr, h - m - arr, cx, h - m));
    }

    private static void paintPrintFormat(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.15f;
        // page
        g.draw(new RoundRectangle2D.Float(m, m, w - 2 * m, h - 2 * m, 3, 3));
        // rulers on edges
        g.setColor(secondary());
        // top ruler ticks
        for (int i = 1; i <= 3; i++) {
            float tx = m + i * (w - 2 * m) * 0.25f;
            g.draw(new Line2D.Float(tx, m, tx, m + h * 0.06f));
        }
        // left ruler ticks
        for (int i = 1; i <= 3; i++) {
            float ty = m + i * (h - 2 * m) * 0.25f;
            g.draw(new Line2D.Float(m, ty, m + w * 0.06f, ty));
        }
        // content area
        g.setColor(accent());
        float cm = w * 0.28f;
        g.draw(new Rectangle2D.Float(cm, cm, w - 2 * cm, h - 2 * cm));
    }

    private static void paintPrintLeftView(Graphics2D g, int w, int h) {
        setup(g, w);
        float m = w * 0.12f;
        // left panel (active)
        g.setColor(accent());
        g.fill(new RoundRectangle2D.Float(m, m, (w - 2 * m) * 0.45f, h - 2 * m, 3, 3));
        // right panel (inactive)
        g.setColor(secondary());
        g.draw(new RoundRectangle2D.Float(m + (w - 2 * m) * 0.55f, m, (w - 2 * m) * 0.45f, h - 2 * m, 3, 3));
    }

    private static void paintPrintLeftViewHidden(Graphics2D g, int w, int h) {
        setup(g, w);
        float m = w * 0.12f;
        // left panel (dimmed)
        g.setColor(secondary());
        g.draw(new RoundRectangle2D.Float(m, m, (w - 2 * m) * 0.45f, h - 2 * m, 3, 3));
        // right panel (inactive)
        g.draw(new RoundRectangle2D.Float(m + (w - 2 * m) * 0.55f, m, (w - 2 * m) * 0.45f, h - 2 * m, 3, 3));
    }

    private static void paintPrintRightView(Graphics2D g, int w, int h) {
        setup(g, w);
        float m = w * 0.12f;
        // left panel (inactive)
        g.setColor(secondary());
        g.draw(new RoundRectangle2D.Float(m, m, (w - 2 * m) * 0.45f, h - 2 * m, 3, 3));
        // right panel (active)
        g.setColor(accent());
        g.fill(new RoundRectangle2D.Float(m + (w - 2 * m) * 0.55f, m, (w - 2 * m) * 0.45f, h - 2 * m, 3, 3));
    }

    private static void paintPrintRightViewHidden(Graphics2D g, int w, int h) {
        setup(g, w);
        float m = w * 0.12f;
        // both panels dimmed
        g.setColor(secondary());
        g.draw(new RoundRectangle2D.Float(m, m, (w - 2 * m) * 0.45f, h - 2 * m, 3, 3));
        g.draw(new RoundRectangle2D.Float(m + (w - 2 * m) * 0.55f, m, (w - 2 * m) * 0.45f, h - 2 * m, 3, 3));
    }

    // =========== STAGE 2: DIALOG ICONS ===========

    private static void paintDialogOk(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(new Color(0x4CAF50));
        setupStroke(g, w, 0.06f);
        g.draw(new Line2D.Float(w * 0.2f, h * 0.5f, w * 0.42f, h * 0.72f));
        g.draw(new Line2D.Float(w * 0.42f, h * 0.72f, w * 0.8f, h * 0.28f));
    }

    private static void paintDialogCancel(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(NomadPlanColors.error());
        setupStroke(g, w, 0.06f);
        float m = w * 0.22f;
        g.draw(new Line2D.Float(m, m, w - m, h - m));
        g.draw(new Line2D.Float(w - m, m, m, h - m));
    }

    // =========== STAGE 2: SYSTEM/MISC ===========

    private static void paintArrowDown(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float cx = w * 0.5f, m = w * 0.25f;
        g.draw(new Line2D.Float(cx, m, cx, h - m));
        float arr = w * 0.12f;
        g.draw(new Line2D.Float(cx - arr, h - m - arr, cx, h - m));
        g.draw(new Line2D.Float(cx + arr, h - m - arr, cx, h - m));
    }

    private static void paintArrowUp(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float cx = w * 0.5f, m = w * 0.25f;
        g.draw(new Line2D.Float(cx, h - m, cx, m));
        float arr = w * 0.12f;
        g.draw(new Line2D.Float(cx - arr, m + arr, cx, m));
        g.draw(new Line2D.Float(cx + arr, m + arr, cx, m));
    }

    private static void paintPerson(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float cx = w * 0.5f;
        float headR = w * 0.15f;
        g.draw(new Ellipse2D.Float(cx - headR, h * 0.12f, headR * 2, headR * 2));
        g.draw(new Arc2D.Float(cx - w * 0.28f, h * 0.42f, w * 0.56f, h * 0.5f, 0, 180, Arc2D.OPEN));
    }

    // =========== STAGE 3: REMAINING ICONS ===========

    private static void paintLeaf(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(secondary());
        float cx = w * 0.5f, cy = h * 0.5f, r = w * 0.12f;
        g.fill(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));
    }

    private static void paintImport(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(accent());
        float m = w * 0.12f;
        float fw = w - 2 * m, fh = h - 2 * m;
        // folder
        GeneralPath folder = new GeneralPath();
        folder.moveTo(m, m + fh * 0.2f);
        folder.lineTo(m, m + fh);
        folder.lineTo(m + fw, m + fh);
        folder.lineTo(m + fw, m + fh * 0.35f);
        folder.lineTo(m + fw * 0.5f, m + fh * 0.35f);
        folder.lineTo(m + fw * 0.4f, m + fh * 0.2f);
        folder.closePath();
        g.draw(folder);
        // arrow down into folder
        g.setColor(fg());
        float ax = w * 0.5f, ay1 = m, ay2 = m + fh * 0.55f;
        g.draw(new Line2D.Float(ax, ay1, ax, ay2));
        float arr = w * 0.08f;
        g.draw(new Line2D.Float(ax - arr, ay2 - arr, ax, ay2));
        g.draw(new Line2D.Float(ax + arr, ay2 - arr, ax, ay2));
    }

    private static void paintExport(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(accent());
        float m = w * 0.12f;
        float fw = w - 2 * m, fh = h - 2 * m;
        // folder
        GeneralPath folder = new GeneralPath();
        folder.moveTo(m, m + fh * 0.2f);
        folder.lineTo(m, m + fh);
        folder.lineTo(m + fw, m + fh);
        folder.lineTo(m + fw, m + fh * 0.35f);
        folder.lineTo(m + fw * 0.5f, m + fh * 0.35f);
        folder.lineTo(m + fw * 0.4f, m + fh * 0.2f);
        folder.closePath();
        g.draw(folder);
        // arrow up out of folder
        g.setColor(fg());
        float ax = w * 0.5f, ay1 = m + fh * 0.55f, ay2 = m;
        g.draw(new Line2D.Float(ax, ay1, ax, ay2));
        float arr = w * 0.08f;
        g.draw(new Line2D.Float(ax - arr, ay2 + arr, ax, ay2));
        g.draw(new Line2D.Float(ax + arr, ay2 + arr, ax, ay2));
    }

    private static void paintReplace(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.2f;
        // two curved arrows forming a cycle
        float r = w * 0.22f;
        // top arrow (left to right)
        g.draw(new Arc2D.Float(m, h * 0.2f, w - 2 * m, r * 2, 0, 180, Arc2D.OPEN));
        float arr = w * 0.06f;
        g.draw(new Line2D.Float(w - m, h * 0.2f + r, w - m - arr, h * 0.2f + r - arr));
        g.draw(new Line2D.Float(w - m, h * 0.2f + r, w - m + arr, h * 0.2f + r - arr));
        // bottom arrow (right to left)
        g.draw(new Arc2D.Float(m, h * 0.5f, w - 2 * m, r * 2, 180, 180, Arc2D.OPEN));
        g.draw(new Line2D.Float(m, h * 0.5f + r, m - arr, h * 0.5f + r + arr));
        g.draw(new Line2D.Float(m, h * 0.5f + r, m + arr, h * 0.5f + r + arr));
    }

    private static void paintZoomReset(Graphics2D g, int w, int h) {
        paintFind(g, w, h);
        g.setColor(accent());
        float fontSize = w * 0.18f;
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, Math.round(fontSize)));
        FontMetrics fm = g.getFontMetrics();
        String text = "1:1";
        int tw = fm.stringWidth(text);
        g.drawString(text, w * 0.42f - tw / 2f, h * 0.42f + fm.getAscent() * 0.35f);
    }

    private static void paintStepBack(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.2f;
        // vertical bar on left
        g.draw(new Line2D.Float(m, m, m, h - m));
        // double left chevron
        float cx = w * 0.55f;
        g.draw(new Line2D.Float(cx, m, cx - w * 0.2f, h * 0.5f));
        g.draw(new Line2D.Float(cx - w * 0.2f, h * 0.5f, cx, h - m));
        float cx2 = cx + w * 0.18f;
        g.draw(new Line2D.Float(cx2, m, cx2 - w * 0.2f, h * 0.5f));
        g.draw(new Line2D.Float(cx2 - w * 0.2f, h * 0.5f, cx2, h - m));
    }

    private static void paintStepForward(Graphics2D g, int w, int h) {
        setup(g, w);
        g.setColor(fg());
        float m = w * 0.2f;
        // vertical bar on right
        g.draw(new Line2D.Float(w - m, m, w - m, h - m));
        // double right chevron
        float cx = w * 0.45f;
        g.draw(new Line2D.Float(cx, m, cx + w * 0.2f, h * 0.5f));
        g.draw(new Line2D.Float(cx + w * 0.2f, h * 0.5f, cx, h - m));
        float cx2 = cx - w * 0.18f;
        g.draw(new Line2D.Float(cx2, m, cx2 + w * 0.2f, h * 0.5f));
        g.draw(new Line2D.Float(cx2 + w * 0.2f, h * 0.5f, cx2, h - m));
    }

    // --- Stage 4 painters ---

    private static void paintNetworkView(Graphics2D g, int w, int h) {
        setup(g, w);
        float m = w * 0.15f;
        // Three connected boxes (PERT/network diagram)
        float bw = w * 0.22f, bh = h * 0.18f;
        // top center box
        float tx = w * 0.5f - bw / 2, ty = m;
        g.setColor(fg());
        g.draw(new RoundRectangle2D.Float(tx, ty, bw, bh, 2, 2));
        // bottom-left box
        float lx = m, ly = h - m - bh;
        g.draw(new RoundRectangle2D.Float(lx, ly, bw, bh, 2, 2));
        // bottom-right box
        float rx = w - m - bw, ry = ly;
        g.draw(new RoundRectangle2D.Float(rx, ry, bw, bh, 2, 2));
        // lines: top box to bottom boxes
        g.setColor(secondary());
        g.draw(new Line2D.Float(tx + bw * 0.3f, ty + bh, lx + bw * 0.7f, ly));
        g.draw(new Line2D.Float(tx + bw * 0.7f, ty + bh, rx + bw * 0.3f, ry));
    }

    private static void paintUnfetchedLazy(Graphics2D g, int w, int h) {
        setup(g, w);
        // Hollow circle with ellipsis (loading indicator)
        float cx = w * 0.5f, cy = h * 0.5f;
        float r = w * 0.3f;
        g.setColor(secondary());
        g.draw(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));
        // three dots inside
        float dotR = w * 0.04f;
        g.setColor(fg());
        g.fill(new Ellipse2D.Float(cx - w * 0.14f - dotR, cy - dotR, dotR * 2, dotR * 2));
        g.fill(new Ellipse2D.Float(cx - dotR, cy - dotR, dotR * 2, dotR * 2));
        g.fill(new Ellipse2D.Float(cx + w * 0.14f - dotR, cy - dotR, dotR * 2, dotR * 2));
    }

    private static void paintFormatOther(Graphics2D g, int w, int h) {
        setup(g, w);
        float m = w * 0.2f;
        // three horizontal slider tracks with handles
        for (int i = 0; i < 3; i++) {
            float y = m + i * (h - 2 * m) * 0.4f + (h - 2 * m) * 0.1f;
            g.setColor(secondary());
            g.draw(new Line2D.Float(m, y, w - m, y));
            // handle at different positions
            float[] handlePos = {0.3f, 0.6f, 0.45f};
            float hx = m + (w - 2 * m) * handlePos[i];
            float hr = w * 0.06f;
            g.setColor(accent());
            g.fill(new Ellipse2D.Float(hx - hr, y - hr, hr * 2, hr * 2));
        }
    }
}
