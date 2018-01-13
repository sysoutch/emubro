package ch.sysout.emubro.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.commons.io.FilenameUtils;

import com.jgoodies.forms.factories.Paddings;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import ch.sysout.emubro.api.model.Emulator;
import ch.sysout.emubro.api.model.Explorer;
import ch.sysout.emubro.api.model.Game;
import ch.sysout.emubro.api.model.Platform;
import ch.sysout.emubro.util.MessageConstants;
import ch.sysout.ui.ImageUtil;
import ch.sysout.util.Icons;
import ch.sysout.util.Messages;
import ch.sysout.util.ScreenSizeUtil;
import ch.sysout.util.UIUtil;

public class GamePropertiesDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private JTabbedPane tpMain = new JTabbedPane();
	private JPanel pnlMain = new ScrollablePanel();
	private JTextArea txtGamePath;
	private JLabel txtGameFilename;
	private Font fontUnderline;
	private Font fontNotUnderline;
	private Map<TextAttribute, Integer> fontAttributesNotUnderlined = new HashMap<>();
	private Map<TextAttribute, Integer> fontAttributes = new HashMap<>();
	private JButton btnOk;
	private JButton btnCancel;
	private JButton btnApply;
	private JTextArea txtGameName;
	private Explorer explorer;
	private Game game;

	private JToggleButton btnModify;

	private JComponent pnlSpEmulators;

	public GamePropertiesDialog(Explorer explorer) {
		super();
		this.explorer = explorer;
		game = explorer.getCurrentGame();
		setTitle("Eigenschaften von " + game.getName());
		setLayout(new BorderLayout());
		setIconImage(ImageUtil.getImageIconFrom(Icons.get("gameProperties", 24, 24)).getImage());
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setModalityType(ModalityType.APPLICATION_MODAL);
		initComponents();
		addListeners();
		createUI();
		pack();
		setMinimumSize(getPreferredSize());
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				setSize(getPreferredSize());
			}
		});
		// int width = ScreenSizeUtil.adjusight));
	}

	private void addListeners() {
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	private void initComponents() {
		btnOk = new JButton(Messages.get(MessageConstants.OK));
		btnCancel = new JButton(Messages.get(MessageConstants.CANCEL));

		createMainPanel();
		FormLayout layout = new FormLayout("min:grow", "fill:min:grow");
		JPanel pnl = new JPanel();
		pnl.setLayout(layout);
		pnl.setMinimumSize(new Dimension(0, 0));
		CellConstraints cc = new CellConstraints();
		pnl.add(pnlMain, cc.xy(1, 1));
		JScrollPane sp = new JScrollPane(pnl);
		sp.setBorder(BorderFactory.createEmptyBorder());
		sp.getVerticalScrollBar().setUnitIncrement(16);
		// JScrollPane spMain = new JScrollPane(pnl);
		// spMain.setBorder(BorderFactory.createEmptyBorder());
		// spMain.getVerticalScrollBar().setUnitIncrement(16);
		tpMain.addTab(Messages.get(MessageConstants.GENERAL), sp);

		int oldPreferredHeight = txtGameName.getPreferredSize().height;
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				int newPreferredHeight = txtGameName.getPreferredSize().height;
				int difference = newPreferredHeight - oldPreferredHeight;
				if (difference > 0) {
					GamePropertiesDialog diss = GamePropertiesDialog.this;
					diss.setPreferredSize(new Dimension(diss.getWidth(), diss.getHeight()+difference));
				}
			}
		});
	}

	private void createUI() {
		getRootPane().setBorder(Paddings.TABBED_DIALOG);
		add(tpMain, BorderLayout.CENTER);

		FormLayout layout = new FormLayout("24dlu:grow, $button, $rgap, $button, $rgap, $button", "$rgap, fill:pref");
		layout.setColumnGroup(2, 4, 6);
		JPanel pnlFooter = new JPanel(layout);
		CellConstraints cc = new CellConstraints();
		pnlFooter.add(btnOk, cc.xy(2, 2));
		pnlFooter.add(btnCancel, cc.xy(4, 2));
		pnlFooter.add(btnApply = new JButton(Messages.get(MessageConstants.APPLY)), cc.xy(6, 2));
		btnApply.setEnabled(false);
		layout.setColumnGroup(2, 4, 6);
		add(pnlFooter, BorderLayout.SOUTH);
	}

	private void createMainPanel() {
		FormLayout layout = new FormLayout("default, $lcgap, default, $ugap:grow, $button, min, min",
				"fill:default, fill:default:grow, $ugap, fill:pref, $ugap, fill:pref, $rgap, fill:pref, fill:$ugap, fill:pref, $ugap,"
						+ "fill:pref, $rgap, top:pref, $ugap, fill:pref, $ugap, fill:pref, $rgap, fill:pref, $rgap, fill:pref, fill:min");
		pnlMain.setLayout(layout);
		pnlMain.setBorder(Paddings.TABBED_DIALOG);
		CellConstraints cc = new CellConstraints();
		JLabel lblIcon;
		pnlMain.add(lblIcon = new JLabel(ImageUtil.getImageIconFrom("/images/emulators/desmume.png")), cc.xy(1, 1));
		lblIcon.setHorizontalAlignment(SwingConstants.LEFT);
		txtGameName = new JTextArea(game.getName());
		JScrollPane spGameName = new JScrollPane(txtGameName);
		pnlMain.add(spGameName, cc.xywh(3, 1, layout.getColumnCount() - 2, 2));
		txtGameName.setCaretPosition(0);
		txtGameName.setLineWrap(true);
		txtGameName.setWrapStyleWord(true);
		txtGameName.setMinimumSize(new Dimension(0, 0));
		pnlMain.add(new JSeparator(), cc.xyw(1, 4, layout.getColumnCount()));
		pnlMain.add(new JLabel(Messages.get(MessageConstants.COLUMN_PLATFORM) + ":"), cc.xy(1, 6));
		Platform platform = explorer.getPlatform(game.getPlatformId());
		pnlMain.add(new JLabel(platform.getName()), cc.xyw(3, 6, layout.getColumnCount() - 2));
		pnlMain.add(new JLabel(Messages.get(MessageConstants.RUN_WITH) + ":"), cc.xy(1, 8));
		Emulator emulator = explorer.getEmulatorFromPlatform(platform.getId());
		String emulatorName = (emulator != null) ? emulator.getName() : "-";
		pnlMain.add(new JLabel(emulatorName), cc.xy(3, 8));
		pnlMain.add(btnModify = new JToggleButton(Messages.get(MessageConstants.MODIFY)), cc.xy(5, 8));

		int rowHeight = ScreenSizeUtil.adjustValueToResolution(32);
		EmulatorTableModel model = new EmulatorTableModel(platform.getEmulators());
		JTable tblEmulators = new JTableDoubleClickOnHeaderFix();
		tblEmulators.setPreferredScrollableViewportSize(tblEmulators.getPreferredSize());
		tblEmulators.setRowHeight(rowHeight);
		tblEmulators.setAutoscrolls(false);
		tblEmulators.getTableHeader().setReorderingAllowed(false);
		tblEmulators.setIntercellSpacing(new Dimension(0, 0));
		tblEmulators.setFillsViewportHeight(true);
		tblEmulators.setShowGrid(false);
		tblEmulators.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblEmulators.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblEmulators.setModel(model);
		JScrollPane spEmulators = new JScrollPane(tblEmulators);
		pnlSpEmulators = new JPanel(new BorderLayout());
		pnlSpEmulators.setBorder(Paddings.DLU4);
		pnlSpEmulators.add(spEmulators);
		btnModify.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (btnModify.isSelected()) {
					layout.setRowSpec(2, RowSpec.decode("fill:pref"));
					layout.setRowSpec(9, RowSpec.decode("fill:$ugap:grow"));
					pnlMain.add(pnlSpEmulators, cc.xyw(1, 9, layout.getColumnCount()));
					GamePropertiesDialog diss = GamePropertiesDialog.this;
					int superHeight = (int) (diss.getPreferredSize().getHeight());
					//					if (diss.getHeight() < diss.getPreferredSize().getHeight() + 128)) {
					if (diss.getHeight() < superHeight+128) {
						diss.setSize(new Dimension(diss.getWidth(), superHeight+128));
					} else {
						if (diss.getHeight() < superHeight+128) {
							diss.setSize(new Dimension(diss.getWidth(), superHeight + (128 - diss.getHeight() - superHeight)));
						}
					}
					UIUtil.revalidateAndRepaint(pnlMain);
					for (int i = 0; i < model.getRowCount(); i++) {
						if (platform.getDefaultEmulator() == model.getEmulator(i)) {
							tblEmulators.setRowSelectionInterval(i, i);
							break;
						}
					}
					tblEmulators.scrollRectToVisible(tblEmulators.getCellRect(tblEmulators.getSelectedRow(), 0, true));
				} else {
					pnlMain.remove(pnlSpEmulators);
					layout.setRowSpec(2, RowSpec.decode("fill:pref:grow"));
					layout.setRowSpec(9, RowSpec.decode("fill:$ugap"));
					GamePropertiesDialog diss = GamePropertiesDialog.this;
					int superHeight = (int) diss.getPreferredSize().getHeight();
					diss.setSize(new Dimension(diss.getWidth(), superHeight));
					UIUtil.revalidateAndRepaint(pnlMain);
				}
			}
		});
		pnlMain.add(new JSeparator(), cc.xyw(1, 10, layout.getColumnCount()));

		String name = FilenameUtils.getName(explorer.getFiles(game).get(0));
		String parent = FilenameUtils.getFullPathNoEndSeparator(explorer.getFiles(game).get(0));

		pnlMain.add(new JLabel(Messages.get(MessageConstants.FILE_NAME) + ":"), cc.xy(1, 12));
		pnlMain.add(txtGameFilename = new JLabel(name), cc.xyw(3, 12, layout.getColumnCount() - 2));
		pnlMain.add(new JLabel(Messages.get(MessageConstants.COLUMN_FILE_PATH) + ":"), cc.xy(1, 14	));
		pnlMain.add(txtGamePath = new JTextArea(parent), cc.xyw(3, 14, layout.getColumnCount() - 2));

		txtGameFilename.setMinimumSize(new Dimension(0, 0));
		txtGamePath.setMinimumSize(new Dimension(0, 0));
		txtGamePath.setOpaque(false);
		txtGamePath.setEditable(false);
		txtGamePath.setForeground(new Color(0, 0, 224));
		txtGamePath.setLineWrap(true);
		txtGamePath.setWrapStyleWord(false);
		txtGamePath.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				setGamePathUnderlined(true);
				txtGamePath.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setGamePathUnderlined(false);
				txtGamePath.setCursor(null);
			}
		});

		txtGamePath.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				setGamePathUnderlined(true);
			}

			@Override
			public void focusLost(FocusEvent e) {
				setGamePathUnderlined(false);
			}
		});
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date lastPlayed = game.getLastPlayed();
		if (lastPlayed == null) {

		} else {
			String formattedLastPlayed = dateFormat.format(lastPlayed);
			Date now = new Date();
			long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - lastPlayed.getTime());
			long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - lastPlayed.getTime());
			long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - lastPlayed.getTime());
			long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - lastPlayed.getTime());

			String sPlayCount = "";
			int playCount = game.getPlayCount();
			switch (playCount) {
			case 0:
				sPlayCount = Messages.get(MessageConstants.NEVER_PLAYED);
				break;
			case 1:
				sPlayCount = Messages.get(MessageConstants.PLAY_COUNT3, playCount);
				break;
			default:
				sPlayCount = Messages.get(MessageConstants.PLAY_COUNT2, playCount);
			}

			String ago = "";
			if (days > 0) {
				ago = days + " " + ((days == 1) ? Messages.get(MessageConstants.DAY) : Messages.get(MessageConstants.DAYS));
			} else if (hours > 0) {
				ago = hours + " " + ((hours == 1) ? Messages.get(MessageConstants.HOUR) : Messages.get(MessageConstants.HOURS));
			} else if (minutes > 0) {
				ago = minutes + " " + ((minutes == 1) ? Messages.get(MessageConstants.MINUTE) : Messages.get(MessageConstants.MINUTES));
			} else {
				ago = ((seconds == 0) ? Messages.get(MessageConstants.JUST_NOW)
						: (seconds + " "
								+ ((seconds == 1) ? Messages.get(MessageConstants.SECOND) : Messages.get(MessageConstants.SECONDS))));
			}
			if (Locale.getDefault().equals(Locale.GERMAN)) {
				ago = "Vor " + ago;
			}
			if (Locale.getDefault().equals(Locale.ENGLISH)) {
				ago += " ago";
			}
			if (Locale.getDefault().equals(Locale.FRENCH)) {
				ago = "Avant " + ago;
			}
			formattedLastPlayed = ago;
			pnlMain.add(new JLabel(sPlayCount), cc.xyw(3, 20, layout.getColumnCount() - 2));
			pnlMain.add(new JLabel(formattedLastPlayed), cc.xyw(3, 22, layout.getColumnCount() - 2));
		}
		Date dateAdded = game.getDateAdded();
		String formattedDateAdded = dateFormat.format(dateAdded);

		pnlMain.add(new JSeparator(), cc.xyw(1, 16, layout.getColumnCount()));
		pnlMain.add(new JLabel(Messages.get(MessageConstants.DATE_ADDED) + ":"), cc.xy(1, 18));
		pnlMain.add(new JLabel(formattedDateAdded), cc.xyw(3, 18, layout.getColumnCount() - 2));
		pnlMain.add(new JLabel(Messages.get(MessageConstants.PLAY_COUNT) + ":"), cc.xy(1, 20));
		pnlMain.add(new JLabel(Messages.get(MessageConstants.LAST_PLAYED) + ":"), cc.xy(1, 22));

		TableColumnModel tcm = tblEmulators.getColumnModel();
		DefaultTableCellRenderer renderer = new EmulatorTableCellRenderer(platform);
		tcm.getColumn(1).setCellRenderer(renderer);
		TableCellRenderer renderer2 = tblEmulators.getTableHeader().getDefaultRenderer();
		((JLabel) renderer2).setHorizontalAlignment(SwingConstants.LEFT);
		tblEmulators.getTableHeader().setDefaultRenderer(renderer2);
		TableColumn column0 = tcm.getColumn(0);
		TableColumnAdjuster adjuster = new TableColumnAdjuster(tblEmulators);
		adjuster.adjustColumn(0);
		adjuster.adjustColumn(1);
		column0.setResizable(false);
		tblEmulators.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
	}

	private void setGamePathUnderlined(boolean underlined) {
		if (fontUnderline == null) {
			fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		}
		if (fontNotUnderline == null) {
			fontAttributesNotUnderlined.put(TextAttribute.UNDERLINE, -1);
		}

		if (underlined) {
			fontUnderline = txtGamePath.getFont().deriveFont(fontAttributes);
			txtGamePath.setFont(fontUnderline);
		} else {
			fontNotUnderline = txtGamePath.getFont().deriveFont(fontAttributesNotUnderlined);
			txtGamePath.setFont(fontNotUnderline);
		}
	}

	public void languageChanged() {
		tpMain.getTabComponentAt(0).setName(Messages.get(MessageConstants.GENERAL));
	}
}