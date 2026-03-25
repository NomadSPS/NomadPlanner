package com.projectlibre1.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.projectlibre1.activation.ActivationService;
import com.projectlibre1.activation.ActivationSummary;
import com.projectlibre1.strings.Messages;
import com.projectlibre1.theme.NomadPlanColors;
import com.projectlibre1.theme.NomadPlanUi;

public final class ActivationDialog extends AbstractDialog {
	private static final long serialVersionUID = 1L;

	private final ActivationService activationService = ActivationService.getInstance();
	private final boolean startupGate;

	private JTextField serialField;
	private JTextField installationField;
	private JTextArea activationCodeArea;
	private JLabel summaryLabel;
	private JLabel statusLabel;
	private JButton copyInstallationIdButton;

	public static boolean ensureActivated(Frame owner) {
		ActivationService activationService = ActivationService.getInstance();
		ActivationSummary summary = activationService.validateForStartup();
		if (!summary.getStatus().blocksStartup()) {
			return true;
		}
		ActivationDialog dialog = new ActivationDialog(owner, true);
		return dialog.doModal();
	}

	public static boolean showDialog(Frame owner) {
		ActivationDialog dialog = new ActivationDialog(owner, false);
		return dialog.doModal();
	}

	private ActivationDialog(Frame owner, boolean startupGate) {
		super(owner, Messages.getContextString("Text.ApplicationTitle") + " " + Messages.getString("ActivationDialog.Title"), true);
		this.startupGate = startupGate;
	}

	protected void initComponents() {
		if (serialField != null) {
			return;
		}
		serialField = new JTextField(36);
		installationField = new JTextField(36);
		installationField.setEditable(false);
		activationCodeArea = new JTextArea(6, 36);
		activationCodeArea.setLineWrap(true);
		activationCodeArea.setWrapStyleWord(true);
		summaryLabel = new JLabel();
		statusLabel = new JLabel(" ");
		copyInstallationIdButton = new JButton(Messages.getString("ActivationDialog.Copy"));
		NomadPlanUi.configureDialogButton(copyInstallationIdButton, false);
		copyInstallationIdButton.addActionListener(e -> copyInstallationId());
		super.initComponents();
	}

	public JComponent createContentPanel() {
		initComponents();

		JPanel body = new JPanel();
		body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
		body.setBorder(NomadPlanUi.createFlatDialogBodyBorder());

		JLabel intro = new JLabel(startupGate
			? Messages.getString("ActivationDialog.StartupPrompt")
			: Messages.getString("ActivationDialog.ManagePrompt"));
		intro.setAlignmentX(JComponent.LEFT_ALIGNMENT);

		JPanel summaryPanel = new JPanel(new BorderLayout(0, 4));
		summaryPanel.setOpaque(false);
		summaryPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		summaryPanel.add(NomadPlanUi.createSectionLabel(Messages.getString("ActivationDialog.StatusSection")), BorderLayout.NORTH);
		summaryPanel.add(summaryLabel, BorderLayout.CENTER);

		JPanel installRow = new JPanel(new BorderLayout(8, 0));
		installRow.setOpaque(false);
		installRow.add(installationField, BorderLayout.CENTER);
		installRow.add(copyInstallationIdButton, BorderLayout.EAST);

		JPanel installSection = new JPanel(new BorderLayout(0, 6));
		installSection.setOpaque(false);
		installSection.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		installSection.add(NomadPlanUi.createSectionLabel(Messages.getString("ActivationDialog.InstallationId")), BorderLayout.NORTH);
		installSection.add(installRow, BorderLayout.CENTER);

		JPanel serialSection = new JPanel(new BorderLayout(0, 6));
		serialSection.setOpaque(false);
		serialSection.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		serialSection.add(NomadPlanUi.createSectionLabel(Messages.getString("ActivationDialog.SerialNumber")), BorderLayout.NORTH);
		serialSection.add(serialField, BorderLayout.CENTER);

		JScrollPane activationScroll = new JScrollPane(activationCodeArea);
		NomadPlanUi.prepareDialogScrollPane(activationScroll);
		JPanel activationSection = new JPanel(new BorderLayout(0, 6));
		activationSection.setOpaque(false);
		activationSection.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		activationSection.add(NomadPlanUi.createSectionLabel(Messages.getString("ActivationDialog.ActivationCode")), BorderLayout.NORTH);
		activationSection.add(activationScroll, BorderLayout.CENTER);

		JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		statusPanel.setOpaque(false);
		statusPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		statusPanel.add(statusLabel);

		body.add(intro);
		body.add(Box.createVerticalStrut(10));
		body.add(summaryPanel);
		body.add(Box.createVerticalStrut(10));
		body.add(installSection);
		body.add(Box.createVerticalStrut(10));
		body.add(serialSection);
		body.add(Box.createVerticalStrut(10));
		body.add(activationSection);
		if (activationService.isDeveloperModeEnabled()) {
			JLabel developerMode = new JLabel(Messages.getString("ActivationDialog.DeveloperModeEnabled"));
			developerMode.setForeground(NomadPlanColors.warning());
			developerMode.setAlignmentX(JComponent.LEFT_ALIGNMENT);
			body.add(Box.createVerticalStrut(10));
			body.add(developerMode);
		}
		body.add(Box.createVerticalStrut(10));
		body.add(statusPanel);

		NomadPlanUi.applyFlatDialogBody(body);
		refreshFromCurrentState();
		return body;
	}

	@Override
	protected void createOkCancelButtons() {
		createOkCancelButtons(Messages.getString("ActivationDialog.Activate"), Messages.getString("ButtonText.Cancel"));
	}

	@Override
	public void onOk() {
		ActivationSummary summary;
		String serialText = serialField.getText() == null ? "" : serialField.getText().trim();
		if (activationService.isDeveloperModeEnabled() && serialText.startsWith("DEV1.")) {
			summary = activationService.activateDeveloperOverride(serialText);
		} else {
			summary = activationService.activate(serialText, activationCodeArea.getText());
		}
		if (!summary.isActive()) {
			showStatus(summary, true);
			return;
		}
		showStatus(summary, false);
		super.onOk();
	}

	private void refreshFromCurrentState() {
		installationField.setText(activationService.getInstallationId());
		ActivationSummary summary = activationService.getCurrentSummary();
		if ((summary.getSerialNumber() != null) && !summary.isDeveloperOverride()) {
			serialField.setText(summary.getSerialNumber());
		}
		showStatus(summary, !summary.isActive());
	}

	private void showStatus(ActivationSummary summary, boolean error) {
		summaryLabel.setText(ActivationUiSupport.formatSummary(summary));
		statusLabel.setForeground(error ? NomadPlanColors.error() : NomadPlanColors.success());
		statusLabel.setText(summary.getMessage() == null ? " " : summary.getMessage());
	}

	private void copyInstallationId() {
		String value = installationField.getText();
		if ((value == null) || (value.length() == 0)) {
			return;
		}
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(value), null);
		statusLabel.setForeground(NomadPlanColors.textMuted());
		statusLabel.setText(Messages.getString("ActivationDialog.InstallationIdCopied"));
	}

	@Override
	protected boolean initialOkEnabledState() {
		return true;
	}
}
