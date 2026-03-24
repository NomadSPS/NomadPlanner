package com.projectlibre1.pm.graphic.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

import com.projectlibre1.association.AssociationList;
import com.projectlibre1.configuration.Configuration;
import com.projectlibre1.document.ObjectEvent;
import com.projectlibre1.field.Field;
import com.projectlibre1.graphic.configuration.SpreadSheetCategories;
import com.projectlibre1.grouping.core.Node;
import com.projectlibre1.menu.MenuActionConstants;
import com.projectlibre1.pm.assignment.Assignment;
import com.projectlibre1.pm.dependency.Dependency;
import com.projectlibre1.pm.dependency.DependencyNodeModelDataFactory;
import com.projectlibre1.pm.graphic.IconManager;
import com.projectlibre1.pm.graphic.frames.DocumentFrame;
import com.projectlibre1.pm.graphic.spreadsheet.SpreadSheet;
import com.projectlibre1.pm.graphic.spreadsheet.SpreadSheetModel;
import com.projectlibre1.pm.graphic.spreadsheet.SpreadSheetUtils;
import com.projectlibre1.pm.graphic.spreadsheet.selection.event.SelectionNodeEvent;
import com.projectlibre1.pm.graphic.spreadsheet.selection.event.SelectionNodeListener;
import com.projectlibre1.pm.resource.Resource;
import com.projectlibre1.pm.task.NormalTask;
import com.projectlibre1.pm.task.Project;
import com.projectlibre1.pm.task.Task;
import com.projectlibre1.strings.Messages;
import com.projectlibre1.theme.CardSurfacePanel;
import com.projectlibre1.theme.NomadPlanColors;
import com.projectlibre1.theme.NomadPlanUi;
import com.projectlibre1.undo.UndoController;
import com.projectlibre1.workspace.WorkspaceSetting;

public class TaskDetailsView extends JPanel implements BaseView, SelectionNodeListener, ObjectEvent.Listener {
	private static final long serialVersionUID = 8184229393232462715L;
	private static final String EMPTY_CARD = "empty";
	private static final String DETAILS_CARD = "details";
	private static final String DEPENDENCY_SPREADSHEET = SpreadSheetCategories.dependencySpreadsheetCategory;

	private final DocumentFrame documentFrame;
	private final Project project;
	private final CardLayout cardLayout = new CardLayout();
	private final JPanel contentPanel = new JPanel(cardLayout);
	private final CardSurfacePanel cardPanel = NomadPlanUi.createCardPanel();
	private final JLabel emptyLabel = new JLabel(Messages.getString("TaskDetailsView.EmptyState"), JLabel.CENTER); //$NON-NLS-1$
	private final JTabbedPane tabbedPane = new JTabbedPane();
	private final SpreadSheet resourcesSpreadSheet;
	private final DependencySpreadSheet predecessorsSpreadSheet;
	private final DependencySpreadSheet successorsSpreadSheet;

	private Task selectedTask;
	private boolean active;

	public TaskDetailsView(DocumentFrame documentFrame) {
		super(new BorderLayout());
		this.documentFrame = documentFrame;
		this.project = documentFrame.getProject();
		resourcesSpreadSheet = createResourcesSpreadsheet();
		predecessorsSpreadSheet = createDependencySpreadsheet(true);
		successorsSpreadSheet = createDependencySpreadsheet(false);
		initUi();
		showEmptyState();
	}

	private void initUi() {
		NomadPlanUi.applyWorkspaceBackground(this);
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		contentPanel.setOpaque(false);
		cardPanel.setBorder(BorderFactory.createCompoundBorder(
			cardPanel.getBorder(),
			BorderFactory.createEmptyBorder(4, 4, 4, 4)));
		emptyLabel.setForeground(NomadPlanColors.textSecondary());
		emptyLabel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
		emptyLabel.setHorizontalTextPosition(JLabel.CENTER);
		contentPanel.add(emptyLabel, EMPTY_CARD);
		tabbedPane.addTab(Messages.getString("TaskInformationDialog.Resources"), SpreadSheetUtils.makeSpreadsheetScrollPane(resourcesSpreadSheet)); //$NON-NLS-1$
		tabbedPane.addTab(Messages.getString("TaskInformationDialog.Predecessors"), SpreadSheetUtils.makeSpreadsheetScrollPane(predecessorsSpreadSheet)); //$NON-NLS-1$
		tabbedPane.addTab(Messages.getString("TaskInformationDialog.Successors"), SpreadSheetUtils.makeSpreadsheetScrollPane(successorsSpreadSheet)); //$NON-NLS-1$
		NomadPlanUi.applyTabbedPaneStyle(tabbedPane);
		contentPanel.add(tabbedPane, DETAILS_CARD);
		cardPanel.add(contentPanel, BorderLayout.CENTER);
		add(cardPanel, BorderLayout.CENTER);
	}

	private SpreadSheet createResourcesSpreadsheet() {
		SpreadSheet spreadSheet = SpreadSheetUtils.createFilteredSpreadsheet(documentFrame,
			false,
			"View.TaskInformation.Assignments", //$NON-NLS-1$
			SpreadSheetCategories.resourceAssignmentSpreadsheetCategory,
			"Spreadsheet.Assignment.resourceUsage", //$NON-NLS-1$
			true,
			new String[0]);
		configureReadOnlySpreadsheet(spreadSheet);
		return spreadSheet;
	}

	private DependencySpreadSheet createDependencySpreadsheet(boolean predecessor) {
		DependencySpreadSheet spreadSheet = new DependencySpreadSheet(predecessor);
		spreadSheet.setSpreadSheetCategory(DEPENDENCY_SPREADSHEET);
		spreadSheet.setCanModifyColumns(false);
		spreadSheet.setCanSelectFieldArray(false);
		SpreadSheetUtils.createCollectionSpreadSheet(
			spreadSheet,
			new AssociationList(),
			predecessor ? "View.TaskInformation.Predecessors" : "View.TaskInformation.Successors", //$NON-NLS-1$ //$NON-NLS-2$
			DEPENDENCY_SPREADSHEET,
			predecessor ? "Spreadsheet.TaskDetails.predecessors" : "Spreadsheet.TaskDetails.successors", //$NON-NLS-1$ //$NON-NLS-2$
			predecessor,
			new DependencyNodeModelDataFactory(),
			0);
		configureReadOnlySpreadsheet(spreadSheet);
		return spreadSheet;
	}

	private final class DependencySpreadSheet extends SpreadSheet {
		private static final long serialVersionUID = 513697991757440522L;
		private final boolean predecessor;
		private final Field jumpField = Configuration.getFieldFromId("Field.jumpToTask"); //$NON-NLS-1$
		private final Icon jumpIcon = IconManager.getIcon("menu.scrollToTask"); //$NON-NLS-1$
		private final JButton jumpButton = createJumpButton();

		private DependencySpreadSheet(boolean predecessor) {
			this.predecessor = predecessor;
		}

		private JButton createJumpButton() {
			JButton button = new JButton();
			button.setHorizontalAlignment(SwingConstants.CENTER);
			button.setVerticalAlignment(SwingConstants.CENTER);
			button.setIcon(jumpIcon);
			button.setFocusable(false);
			button.setRolloverEnabled(false);
			button.setMargin(new java.awt.Insets(0, 0, 0, 0));
			button.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(UIManager.getColor("Button.shadow")),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));
			button.setToolTipText(Messages.getString("Field.jumpToTask")); //$NON-NLS-1$
			return button;
		}

		private Dependency getDependencyInRow(int row) {
			if (row < 0) {
				return null;
			}
			com.projectlibre1.pm.graphic.model.cache.GraphicNode graphicNode = ((SpreadSheetModel) getModel()).getNode(row);
			if (graphicNode == null || graphicNode.getNode() == null || graphicNode.getNode().isVoid()) {
				return null;
			}
			Object object = graphicNode.getNode().getImpl();
			return object instanceof Dependency ? (Dependency) object : null;
		}

		private Task resolveTargetTask(Dependency dependency) {
			if (dependency == null) {
				return null;
			}
			Object target = predecessor ? dependency.getLeft() : dependency.getRight();
			return target instanceof Task ? (Task) target : null;
		}

		public void doDoubleClick(int row, int col) {
		}

		public void doClick(int row, int col) {
			Field field = ((SpreadSheetModel) getModel()).getFieldInColumn(col + 1);
			if (field != jumpField) {
				return;
			}
			Task targetTask = resolveTargetTask(getDependencyInRow(row));
			if (targetTask == null || targetTask.getDocument() == null) {
				return;
			}
			targetTask.getDocument().getObjectSelectionEventManager().fire(this, targetTask);
		}

		public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
			Field field = ((SpreadSheetModel) getModel()).getFieldInColumn(column + 1);
			if (field == jumpField) {
				boolean selected = isCellSelected(row, column);
				jumpButton.setBackground(selected ? getSelectionBackground() : UIManager.getColor("Button.background"));
				jumpButton.setForeground(selected ? getSelectionForeground() : UIManager.getColor("Button.foreground"));
				jumpButton.setIcon(jumpIcon);
				return jumpButton;
			}
			return super.prepareRenderer(renderer, row, column);
		}
	}

	private void configureReadOnlySpreadsheet(SpreadSheet spreadSheet) {
		spreadSheet.setCanModifyColumns(false);
		spreadSheet.setCanSelectFieldArray(false);
		spreadSheet.clearActions();
		spreadSheet.setReadOnly(true);
	}

	public void selectionChanged(SelectionNodeEvent e) {
		setSelectedTask(extractSelectedTask(e));
	}

	public void objectChanged(ObjectEvent objectEvent) {
		if (!active || selectedTask == null || objectEvent == null) {
			return;
		}
		Object object = objectEvent.getObject();
		if (object == null) {
			return;
		}
		if (objectEvent.isDelete() && object == selectedTask) {
			setSelectedTask(null);
			return;
		}
		if (object instanceof Task || object instanceof Assignment || object instanceof Dependency || object instanceof Resource) {
			refreshSelectedTask();
		}
	}

	private Task extractSelectedTask(SelectionNodeEvent event) {
		if (event == null) {
			return null;
		}
		Task task = taskFromNode(event.getCurrentNode());
		if (task != null) {
			return task;
		}
		List nodes = event.getNodes();
		if (nodes == null) {
			return null;
		}
		for (int i = 0; i < nodes.size(); i++) {
			task = taskFromNode((Node) nodes.get(i));
			if (task != null) {
				return task;
			}
		}
		return null;
	}

	private Task taskFromNode(Node node) {
		return node == null ? null : taskFromObject(node.getImpl());
	}

	private Task taskFromObject(Object object) {
		if (object instanceof Assignment) {
			return ((Assignment) object).getTask();
		}
		return object instanceof Task ? (Task) object : null;
	}

	private void setSelectedTask(Task task) {
		selectedTask = task;
		if (task == null) {
			showEmptyState();
			updateResourcesSpreadsheet(new AssociationList());
			updateDependencySpreadsheet(predecessorsSpreadSheet, new AssociationList());
			updateDependencySpreadsheet(successorsSpreadSheet, new AssociationList());
			return;
		}
		showDetails();
		updateResourcesSpreadsheet(task instanceof NormalTask ? ((NormalTask) task).getAssignments() : new AssociationList());
		updateDependencySpreadsheet(predecessorsSpreadSheet, task.getPredecessorList());
		updateDependencySpreadsheet(successorsSpreadSheet, task.getSuccessorList());
	}

	private void refreshSelectedTask() {
		if (selectedTask != null) {
			setSelectedTask(selectedTask);
		}
	}

	private void updateResourcesSpreadsheet(AssociationList assignments) {
		SpreadSheetUtils.updateFilteredSpreadsheet(resourcesSpreadSheet, assignments);
		((SpreadSheetModel) resourcesSpreadSheet.getModel()).fireUpdateAll();
	}

	private void updateDependencySpreadsheet(SpreadSheet spreadSheet, AssociationList dependencies) {
		SpreadSheetUtils.updateCollectionSpreadSheet(spreadSheet, dependencies, new DependencyNodeModelDataFactory(), 0);
		((SpreadSheetModel) spreadSheet.getModel()).fireUpdateAll();
	}

	private void showEmptyState() {
		cardLayout.show(contentPanel, EMPTY_CARD);
	}

	private void showDetails() {
		cardLayout.show(contentPanel, DETAILS_CARD);
	}

	public UndoController getUndoController() {
		return null;
	}

	public void zoomIn() {
	}

	public void zoomOut() {
	}

	public void scrollToTask() {
	}

	public boolean canZoomIn() {
		return false;
	}

	public boolean canZoomOut() {
		return false;
	}

	public boolean canScrollToTask() {
		return false;
	}

	public int getScale() {
		return -1;
	}

	public SpreadSheet getSpreadSheet() {
		Component component = tabbedPane.getSelectedComponent();
		if (component == null) {
			return null;
		}
		if (component == tabbedPane.getComponentAt(0)) {
			return resourcesSpreadSheet;
		}
		if (component == tabbedPane.getComponentAt(1)) {
			return predecessorsSpreadSheet;
		}
		if (component == tabbedPane.getComponentAt(2)) {
			return successorsSpreadSheet;
		}
		return null;
	}

	public boolean hasNormalMinWidth() {
		return true;
	}

	public String getViewName() {
		return MenuActionConstants.ACTION_TASK_DETAILS;
	}

	public boolean showsTasks() {
		return true;
	}

	public boolean showsResources() {
		return false;
	}

	public void onActivate(boolean activate) {
		if (active == activate) {
			return;
		}
		active = activate;
		if (activate) {
			project.addObjectListener(this);
		} else {
			project.removeObjectListener(this);
		}
	}

	public boolean isPrintable() {
		return false;
	}

	public void cleanUp() {
		if (active) {
			project.removeObjectListener(this);
			active = false;
		}
		resourcesSpreadSheet.cleanUp();
		predecessorsSpreadSheet.cleanUp();
		successorsSpreadSheet.cleanUp();
	}

	public void restoreWorkspace(WorkspaceSetting w, int context) {
		if (!(w instanceof Workspace)) {
			return;
		}
		Workspace ws = (Workspace) w;
		if (ws.selectedTab >= 0 && ws.selectedTab < tabbedPane.getTabCount()) {
			tabbedPane.setSelectedIndex(ws.selectedTab);
		}
	}

	public WorkspaceSetting createWorkspace(int context) {
		Workspace ws = new Workspace();
		ws.selectedTab = tabbedPane.getSelectedIndex();
		return ws;
	}

	public com.projectlibre1.pm.graphic.model.cache.NodeModelCache getCache() {
		SpreadSheet spreadSheet = getSpreadSheet();
		return spreadSheet == null ? null : spreadSheet.getCache();
	}

	public static class Workspace implements WorkspaceSetting {
		private static final long serialVersionUID = 8602237375288312211L;
		int selectedTab;
	}
}
