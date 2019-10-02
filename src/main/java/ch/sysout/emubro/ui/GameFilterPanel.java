package ch.sysout.emubro.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

import com.jgoodies.forms.factories.Paddings;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import ch.sysout.emubro.api.FilterListener;
import ch.sysout.emubro.api.GameListener;
import ch.sysout.emubro.api.PlatformFromGameListener;
import ch.sysout.emubro.api.TagsFromGamesListener;
import ch.sysout.emubro.api.event.FilterEvent;
import ch.sysout.emubro.api.event.GameAddedEvent;
import ch.sysout.emubro.api.event.GameRemovedEvent;
import ch.sysout.emubro.api.event.TagEvent;
import ch.sysout.emubro.api.filter.Criteria;
import ch.sysout.emubro.api.filter.FilterGroup;
import ch.sysout.emubro.api.model.Explorer;
import ch.sysout.emubro.api.model.Platform;
import ch.sysout.emubro.api.model.Tag;
import ch.sysout.emubro.impl.event.BroFilterEvent;
import ch.sysout.emubro.impl.filter.BroCriteria;
import ch.sysout.emubro.impl.model.PlatformConstants;
import ch.sysout.emubro.util.MessageConstants;
import ch.sysout.ui.util.JCustomButton;
import ch.sysout.util.Icons;
import ch.sysout.util.ImageUtil;
import ch.sysout.util.Messages;
import ch.sysout.util.ScreenSizeUtil;
import ch.sysout.util.UIUtil;

public class GameFilterPanel extends JPanel implements GameListener, TagsFromGamesListener, PlatformFromGameListener {
	private static final long serialVersionUID = 1L;

	private Icon iconFilter = ImageUtil.getImageIconFrom(Icons.get("setFilter", 16, 16));

	private JComboBox<Platform> cmbPlatforms;
	private JPanel pnlSearchField = new JPanel(new BorderLayout());
	private JPanel pnlSearchFieldInner = new JPanel(new BorderLayout());
	private JExtendedTextField txtSearchGame = new JExtendedTextField(Messages.get(MessageConstants.SEARCH_GAME) + " (Ctrl+F)");
	private ImageIcon icoSearch;
	private ImageIcon icoClose;
	private ImageIcon icoAdvancedSearch;
	private ImageIcon icoFilterGroupsSettings;
	private ImageIcon icoSaveFilterGroup;
	private ImageIcon icoFilterGroups;
	private ImageIcon iconRemove;
	private JButton btnClose;
	private JButton btnTags;
	private JButton btnFilterGroups;

	private int size = ScreenSizeUtil.is3k() ? 24 : 16;

	private List<FilterListener> filterListeners = new ArrayList<>();

	private boolean fireFilterEvent;

	private Explorer explorer;

	private Component requestFocusInWindowListener;

	private JPopupMenu mnuTags = new JPopupMenu();
	private JPopupMenu mnuFilterGroups = new JPopupMenu();

	private Map<String, Tag> tags = new HashMap<>();

	private JMenuItem itmNoTagsAvailable = new JMenuItem(Messages.get(MessageConstants.NO_TAGS_AVAILABLE));
	private JMenuItem itmNoFilterGroupsAvailable = new JMenuItem(Messages.get(MessageConstants.NO_FILTERGROUPS_AVAILABLE));

	protected boolean dontFireEvents;

	private JMenuItem itmSaveCurrentFilters;

	private JMenuItem itmClearTagFilter;

	protected Color colorSearchEmpty = Color.LIGHT_GRAY;

	private JButton btnResizeFilter = new JCustomButton();

	public GameFilterPanel(Explorer explorer) {
		super(new BorderLayout());
		this.explorer = explorer;
		icoSearch = ImageUtil.getImageIconFrom(Icons.get("search", size, size));
		icoAdvancedSearch = ImageUtil.getImageIconFrom(Icons.get("tags", size, size));
		icoFilterGroupsSettings = ImageUtil.getImageIconFrom(Icons.get("filter", size, size));
		icoSaveFilterGroup = ImageUtil.getImageIconFrom(Icons.get("add", size, size));
		icoFilterGroups = ImageUtil.getImageIconFrom(Icons.get("setFilter", size, size));
		iconRemove = ImageUtil.getImageIconFrom(Icons.get("remove", size, size));

		itmSaveCurrentFilters = new JMenuItem("Save current filters...", icoSaveFilterGroup);
		itmClearTagFilter = new JMenuItem("clear filter", iconRemove);
		itmClearTagFilter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				unselectTags();
				fireEvent(new BroFilterEvent(getSelectedPlatformId(), getCriteria()));
			}
		});
		btnClose = new JButton(icoSearch);
		btnTags = new JCustomButton("Tags", icoAdvancedSearch);
		btnTags.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showAdvancedSearchSettingsPopupMenu(btnTags);
			}
		});
		btnFilterGroups = new JButton("", icoFilterGroupsSettings);
		btnFilterGroups.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showFilterGroupsSettingsPopupMenu(btnFilterGroups);
			}
		});

		icoClose = ImageUtil.getImageIconFrom(Icons.get("remove", size, size));
		// txtSearchGame.setFont(ScreenSizeUtil.defaultFont());
		initComponents();
		createUI();
	}

	private void initComponents() {
		cmbPlatforms = new JComboBox<Platform>();
		cmbPlatforms.setEditable(true);
		cmbPlatforms.setRenderer(new CustomComboBoxRenderer());
		cmbPlatforms.setEditor(new CustomComboBoxEditor());
		cmbPlatforms.setUI(new BasicComboBoxUI() {
			@Override
			protected ComboPopup createPopup() {
				popup = new BasicComboPopup(comboBox) {

					@Override
					public void setBorder(Border border) {
						super.setBorder(BorderFactory.createEmptyBorder());
					}

					@Override
					protected JScrollPane createScroller() {
						JScrollPane sp = new JCustomScrollPane(list,
								ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
								ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
						sp.setHorizontalScrollBar(null);
						return sp;
					}
				};
				return popup;
			}

			@Override
			protected JButton createArrowButton() {
				return new JButton() {
					private static final long serialVersionUID = 1L;

					@Override
					public int getWidth() {
						return 0;
					}
				};
			}
		});
		cmbPlatforms.remove(cmbPlatforms.getComponent(0));
		cmbPlatforms.setBorder(BorderFactory.createEmptyBorder());
		cmbPlatforms.addItem(new EmptyPlatform());
		cmbPlatforms.getEditor().getEditorComponent().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				if (cmbPlatforms.isPopupVisible()) {
					cmbPlatforms.hidePopup();
				} else {
					cmbPlatforms.showPopup();
					cmbPlatforms.getEditor().selectAll();
				}
			}
		});
		cmbPlatforms.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				super.focusGained(e);
				cmbPlatforms.showPopup();
			}
		});
		cmbPlatforms.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!dontFireEvents) {
					Object selectedItem = cmbPlatforms.getSelectedItem();
					if (selectedItem instanceof Platform) {
						int platformId = ((Platform) selectedItem).getId();
						fireEvent(new BroFilterEvent(platformId, getCriteria()));
					}
				}
			}
		});

		btnResizeFilter.setFocusable(false);
		btnResizeFilter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				super.mouseEntered(e);
				btnResizeFilter.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR | Cursor.W_RESIZE_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				super.mouseExited(e);
				btnResizeFilter.setCursor(null);
			}
		});

		int size = ScreenSizeUtil.is3k() ? 16 : 12;
		btnResizeFilter.setIcon(ImageUtil.getImageIconFrom(Icons.get("barsWhiteVertical", size, size)));
	}

	private void createUI() {
		setOpaque(false);

		Border textFieldBorder = txtSearchGame.getBorder();
		txtSearchGame.setPreferredSize(new Dimension(ScreenSizeUtil.adjustValueToResolution(256), 0));
		txtSearchGame.setBorder(BorderFactory.createEmptyBorder());
		txtSearchGame.setOpaque(false);
		pnlSearchField.setOpaque(false);
		pnlSearchFieldInner.setOpaque(false);
		//		pnlSearchFieldInner.setBorder(textFieldBorder);

		// pnlSearchFieldInner.setBackground(getBackground());
		// ConstantSize spaceTopBottom = new ConstantSize(1, Unit.DIALOG_UNITS);
		// ConstantSize spaceLeftRight = new ConstantSize(2, Unit.DIALOG_UNITS);
		// pnlSearchFieldInner.setBorder(Paddings.createPadding(spaceTopBottom,
		// spaceLeftRight, spaceTopBottom, spaceLeftRight));
		btnClose.setBorder(BorderFactory.createEmptyBorder());
		btnClose.setContentAreaFilled(false);
		btnClose.setFocusable(false);
		btnClose.setFocusPainted(false);
		btnClose.setOpaque(false);
		btnClose.setBackground(txtSearchGame.getBackground());
		btnClose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (btnClose.getIcon() == icoClose) {
					txtSearchGame.setText(Messages.get(MessageConstants.SEARCH_GAME) + " (Ctrl+F)");
					txtSearchGame.setForeground(colorSearchEmpty);
					fireRequestFocusInWindowEvent();
				}
			}
		});
		btnTags.setOpaque(false);
		btnTags.setBorderPainted(false);
		btnTags.setContentAreaFilled(false);
		btnTags.setToolTipText("Tags");

		btnFilterGroups.setBorderPainted(false);
		btnFilterGroups.setContentAreaFilled(false);
		btnFilterGroups.setToolTipText("Filter Groups");

		pnlSearchField.setBorder(BorderFactory.createEmptyBorder());
		txtSearchGame.setForeground(colorSearchEmpty);

		pnlSearchFieldInner.add(txtSearchGame);
		pnlSearchFieldInner.add(btnClose, BorderLayout.EAST);
		// pnlSearchField.add(pnlSearchFieldInner);

		FormLayout layout = new FormLayout("min:grow",
				"fill:pref");
		CellConstraints cc = new CellConstraints();
		JPanel pnl = new JPanel(layout);
		pnl.setOpaque(false);
		pnl.setBorder(Paddings.DLU2);

		JPanel pnlWrapper = new JPanel(new BorderLayout());
		pnlWrapper.setOpaque(false);
		pnlWrapper.add(btnResizeFilter, BorderLayout.WEST);
		pnlWrapper.add(pnlSearchFieldInner);
		pnlWrapper.add(btnTags, BorderLayout.EAST);

		JSplitPane splFilterPlatformAndGame = new JSplitPane();
		splFilterPlatformAndGame.setResizeWeight(0.625);
		splFilterPlatformAndGame.setBorder(BorderFactory.createEmptyBorder());
		splFilterPlatformAndGame.setContinuousLayout(true);
		splFilterPlatformAndGame.setDividerSize(1);
		splFilterPlatformAndGame.setLeftComponent(cmbPlatforms);
		splFilterPlatformAndGame.setRightComponent(pnlWrapper);
		splFilterPlatformAndGame.setOpaque(false);
		pnl.add(splFilterPlatformAndGame, cc.xy(1, 1));
		add(pnl);

		txtSearchGame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
					txtSearchGame.setText("");
					fireRequestFocusInWindowEvent();
				}
			}
		});

		final DocumentListener documentListener = new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (!isSearchFieldEmpty()) {
					if (!txtSearchGame.getText().isEmpty()) {
						btnClose.setIcon(icoClose);
					} else {
						btnClose.setIcon(icoSearch);
					}
				} else {
					btnClose.setIcon(icoSearch);
				}
				if (!dontFireEvents) {
					int platformId = getSelectedPlatformId();
					fireEvent(new BroFilterEvent(platformId, getCriteria()));
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (!isSearchFieldEmpty()) {
					txtSearchGame.setForeground(UIManager.getColor("Label.foreground"));
					if (!txtSearchGame.getText().isEmpty()) {
						btnClose.setIcon(icoClose);
					} else {
						btnClose.setIcon(icoSearch);
					}
				} else {
					btnClose.setIcon(icoSearch);
				}
				if (!dontFireEvents) {
					int platformId = getSelectedPlatformId();
					fireEvent(new BroFilterEvent(platformId, getCriteria()));
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if (!isSearchFieldEmpty()) {
					txtSearchGame.setForeground(UIManager.getColor("Label.foreground"));
					if (!txtSearchGame.getText().isEmpty()) {
						btnClose.setIcon(icoClose);
					} else {
						btnClose.setIcon(icoSearch);
					}
				} else {
					btnClose.setIcon(icoSearch);
				}
				if (!dontFireEvents) {
					int platformId = getSelectedPlatformId();
					fireEvent(new BroFilterEvent(platformId, getCriteria()));
				}
			}
		};
		txtSearchGame.getDocument().addDocumentListener(documentListener);
		txtSearchGame.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if (txtSearchGame.getText().isEmpty()) {
					txtSearchGame.getDocument().removeDocumentListener(documentListener);
					txtSearchGame.setText(Messages.get(MessageConstants.SEARCH_GAME) + " (Ctrl+F)");
					txtSearchGame.setForeground(colorSearchEmpty);
					txtSearchGame.getDocument().addDocumentListener(documentListener);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				if (isSearchFieldEmpty()) {
					txtSearchGame.setForeground(UIManager.getColor("Label.foreground"));
					txtSearchGame.setText("");
				}
			}
		});
	}

	protected void showAdvancedSearchSettingsPopupMenu(JComponent comp) {
		setVisible(true);
		Component[] comps = mnuTags.getComponents();
		if (comps != null && comps.length != 0) {
			if (comps.length > 2) {
				mnuTags.remove(itmNoTagsAvailable);
			}
		} else {
			mnuTags.add(itmNoTagsAvailable);
		}
		int height = 0;
		if (comp != null) {
			height = comp.getHeight();
		}
		mnuTags.show(comp, 0, height);
	}

	protected void showFilterGroupsSettingsPopupMenu(JComponent comp) {
		setVisible(true);
		Component[] comps = mnuFilterGroups.getComponents();
		if (comps != null && comps.length != 0) {
			if (comps.length > 1) {
				mnuFilterGroups.remove(itmNoFilterGroupsAvailable);
			}
		} else {
			mnuFilterGroups.add(itmNoFilterGroupsAvailable);
		}
		int height = 0;
		if (comp != null) {
			height = comp.getHeight();
		}
		mnuFilterGroups.show(comp, 0, height);
	}

	protected void fireRequestFocusInWindowEvent() {
		requestFocusInWindowListener.requestFocusInWindow();
	}

	public int getSelectedPlatformId() {
		Object selectedItem = cmbPlatforms.getSelectedItem();
		if (selectedItem instanceof Platform) {
			return ((Platform) selectedItem).getId();
		}
		return PlatformConstants.NO_PLATFORM;
	}

	public Criteria getCriteria() {
		List<Tag> selectedTags = getSelectedTags();
		return new BroCriteria((isSearchFieldEmpty() ? "" : txtSearchGame.getText()), selectedTags);
	}

	public void initPlatforms(Collection<Platform> tmpPlatformsWithGames) {
		for (Platform p : tmpPlatformsWithGames) {
			cmbPlatforms.addItem(p);
		}
	}

	public boolean hasPlatform(int platformId) {
		for (int i = 0; i < cmbPlatforms.getItemCount(); i++) {
			if (cmbPlatforms.getItemAt(i).getId() == platformId) {
				return true;
			}
		}
		return false;
	}

	class MyComboRenderer implements ListCellRenderer<Object> {
		@Override
		public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
			JLabel label = new JLabel();
			if (index != -1) {
				Platform platform = (Platform) value;
				if (platform != null) {
					String platformName = platform.getName();
					if (index > 0) {
						label.setText("<html><strong>"+platformName+"</strong></html>");
					} else {
						label.setText(platformName);
					}
				}
			}
			return label;
		}
	}

	private void fireEvent(FilterEvent event) {
		if (fireFilterEvent) {
			for (FilterListener l : filterListeners) {
				l.filterSet(event);
			}
		}
	}

	boolean isSearchFieldEmpty() {
		return txtSearchGame.getText().equals(Messages.get(MessageConstants.SEARCH_GAME) + " (Ctrl+F)");
	}

	public void addFilterListener(FilterListener l) {
		filterListeners.add(l);
	}

	public void addSaveCurrentFiltersListener(ActionListener l) {
		itmSaveCurrentFilters.addActionListener(l);
	}

	public void setRequestFocusInWindowListener(Component l) {
		requestFocusInWindowListener = l;
	}

	public void languageChanged() {
		txtSearchGame.languageChanged();
		fireFilterEvent = false;
		txtSearchGame.setText(Messages.get(MessageConstants.SEARCH_GAME) + " (Ctrl+F)");
		itmNoTagsAvailable.setText(Messages.get(MessageConstants.NO_TAGS_AVAILABLE));
		fireFilterEvent = true;
		cmbPlatforms.repaint();
	}

	@Override
	public void gameAdded(final GameAddedEvent e) {
		int platformId = e.getGame().getPlatformId();
		Platform platform = explorer.getPlatform(platformId);
		if (!hasPlatform(platform.getId())) {
			cmbPlatforms.addItem(platform);
		}
		for (Tag tag : e.getGame().getTags()) {
			if (!hasTag(tag.getName())) {
				addNewTag(tag);
			}
		}
	}

	@Override
	public void gameRemoved(GameRemovedEvent e) {
		int platformId = e.getGame().getPlatformId();
		if (explorer.getGameCountFromPlatform(platformId) == 0) {
			Platform platform;
			if ((platform = getPlatform(platformId)) != null) {
				cmbPlatforms.removeItem(platform);
			}
		}
	}

	private Platform getPlatform(int platformId) {
		for (int i = 0; i < cmbPlatforms.getItemCount(); i++) {
			Platform platform = cmbPlatforms.getItemAt(i);
			if (platform.getId() == platformId) {
				return platform;
			}
		}
		return null;
	}

	private boolean hasTag(String tagName) {
		return tags.containsKey(tagName);
	}

	public void setFocusInTextField() {
		txtSearchGame.selectAll();
		txtSearchGame.requestFocusInWindow();
	}

	public void initTags(List<Tag> tags) {
		this.tags.clear();
		mnuTags.removeAll();
		mnuTags.add(itmClearTagFilter);
		mnuTags.add(new JSeparator());
		if (tags != null) {
			for (Tag tag : tags) {
				addNewTag(tag);
			}
		}
	}

	public void initFilterGroups(List<FilterGroup> filterGroups) {
		mnuFilterGroups.removeAll();
		//		mnuFilterGroups.add(new JMenuItem("New filter group...", icoSaveFilterGroup));
		mnuFilterGroups.add(itmSaveCurrentFilters);
		//		mnuFilterGroups.add(new JMenu("Update filter group"));
		mnuFilterGroups.add(new JSeparator());
		for (FilterGroup group : filterGroups) {
			addFilterGroupItem(group);
		}
	}

	private void addFilterGroupItem(FilterGroup group) {
		JMenuItem itm = new JMenuItem(group.getName(), icoFilterGroups);
		itm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FilterEvent filterEvent = group.getFilterEvent();
				Criteria criteria = filterEvent.getCriteria();
				int platformId = filterEvent.getPlatformId();
				dontFireEvents = true;
				if (criteria.getText().isEmpty()) {
					txtSearchGame.setText(Messages.get(MessageConstants.SEARCH_GAME) + " (Ctrl+F)");
					txtSearchGame.setForeground(Color.WHITE);
				} else {
					txtSearchGame.setText(criteria.getText());
					txtSearchGame.setForeground(UIManager.getColor("Label.foreground"));
				}
				if (platformId == PlatformConstants.NO_PLATFORM) {
					cmbPlatforms.setSelectedIndex(0);
				} else {
					cmbPlatforms.setSelectedItem(getPlatform(platformId));
				}
				selectTags(criteria.getTags());
				fireEvent(filterEvent);
				dontFireEvents = false;
				mnuTags.setVisible(false);
			}
		});
		mnuFilterGroups.add(itm);
	}

	public void showTags(List<Tag> tags) {
		outterLoop:
			for (int i = 2; i < mnuTags.getComponents().length; i++) {
				AbstractButton itm = (AbstractButton) mnuTags.getComponent(i);
				itm.setEnabled(false);
				itm.setForeground(UIManager.getColor("MenuItem.disabledForeground"));

				Font font = itm.getFont();
				Map<TextAttribute, Boolean> attributes = (Map<TextAttribute, Boolean>) font.getAttributes();
				attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
				Font newFont = new Font(attributes);
				itm.setFont(newFont);

				int tmpId = this.tags.get(itm.getText()).getId();
				for (Tag t : tags) {
					if (tmpId == t.getId()) {
						itm.setEnabled(true);
						Color tagColor = Color.decode(t.getHexColor());
						itm.setForeground(tagColor);
						itm.setFont(getFont().deriveFont(Font.PLAIN));
						continue outterLoop;
					}
				}
			}
	}

	public void addNewTag(Tag tag) {
		if (tags.containsKey(tag.getName())) {
			return;
		}
		tags.put(tag.getName(), tag);
		JCheckBoxMenuItem itmTag = new JCheckBoxMenuItem(tag.getName());
		//		itmTag.setIcon(iconTag);

		Color tagColor = Color.decode(tag.getHexColor());
		itmTag.setForeground(tagColor);

		mnuTags.add(itmTag);
		UIUtil.validateAndRepaint(mnuTags);
		itmTag.addActionListener(getTagItemListener(itmTag));
	}

	private ActionListener getTagItemListener(AbstractButton itmTag) {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addTagToFilter(itmTag.isSelected(), itmTag.getText());
			}
		};
	}

	public void addTagToFilter(boolean selected, Tag tag) {
		//		showAdvancedSearchSettingsPopupMenu(btnTags);
		for (int i = 2; i < mnuTags.getComponentCount(); i++) {
			JMenuItem itm = (JMenuItem) mnuTags.getComponent(i);
			if (itm.getText().equals(tag.getName())) {
				itm.setSelected(true);
				break;
			}
		}
		addTagToFilter(selected, tag.getName());
	}

	private void addTagToFilter(boolean selected, String tagName) {
		showAdvancedSearchSettingsPopupMenu(btnTags);

		mnuTags.setVisible(true);
		MenuSelectionManager.defaultManager().setSelectedPath(new MenuElement[] { mnuTags });
		if (selected) {
			btnTags.setText("<html><strong>Tags</strong></html>");
		} else {
			btnTags.setText("Tags");
			for (int i = 2; i < mnuTags.getComponents().length; i++) {
				AbstractButton itm = (AbstractButton) mnuTags.getComponent(i);
				if (itm.isSelected()) {
					btnTags.setText("<html><strong>Tags</strong></html>");
					break;
				}
			}
		}
		int platformId = getSelectedPlatformId();
		fireEvent(new BroFilterEvent(platformId, getCriteria()));
	}

	private void selectTags(List<Tag> list) {
		showAdvancedSearchSettingsPopupMenu(btnTags);
		mnuTags.setVisible(true);
		MenuSelectionManager.defaultManager().setSelectedPath(new MenuElement[] { mnuTags });
		for (int i = 2; i < mnuTags.getComponents().length; i++) {
			AbstractButton itm = (AbstractButton) mnuTags.getComponent(i);
			itm.setSelected(false);
			for (Tag t : list) {
				if (itm.getText().equals(t.getName())) {
					itm.setSelected(true);
					break;
				}
			}
		}
		btnTags.setText(list.size() > 0 ? "<html><strong>Tags</strong></html>" : "Tags");
	}

	private void unselectTags() {
		showAdvancedSearchSettingsPopupMenu(btnTags);
		mnuTags.setVisible(true);
		MenuSelectionManager.defaultManager().setSelectedPath(new MenuElement[] { mnuTags });
		for (int i = 2; i < mnuTags.getComponents().length; i++) {
			AbstractButton itm = (AbstractButton) mnuTags.getComponent(i);
			itm.setSelected(false);
		}
		btnTags.setText("Tags");
	}

	protected List<Tag> getSelectedTags() {
		List<Tag> selectedTags = new ArrayList<>();
		for (int i = 2; i < mnuTags.getComponents().length; i++) {
			AbstractButton itm = (AbstractButton) mnuTags.getComponent(i);
			if (itm.isSelected()) {
				selectedTags.add(tags.get(itm.getText()));
			}
		}
		return selectedTags;
	}

	@Override
	public void tagsInCurrentViewChanged(List<Tag> tags, boolean removeUnusedTags) {
		if (removeUnusedTags) {
			initTags(tags);
		} else {
			showTags(tags);
		}
	}

	public void filterGroupAdded(FilterGroup filterGroup) {
		addFilterGroupItem(filterGroup);
	}



	@Override
	public void platformFromGameAddedToFilter(Platform platform) {
		if (platform == null) {
			cmbPlatforms.setSelectedIndex(0);
			return;
		} else {
			int itemCount = cmbPlatforms.getItemCount();
			for (int i = 1; i < itemCount; i++) {
				Platform p = cmbPlatforms.getItemAt(i);
				if (p.getId() == platform.getId()) {
					cmbPlatforms.setSelectedIndex(i);
					break;
				}
			}
		}
	}

	public void tagAddedToGame(TagEvent e) {
		Tag tag = e.getTag();
		if (!tags.containsKey(tag.getName())) {
			addNewTag(tag);
		}
	}

	// TODO implement logic to remove tag when there are no more games which uses this tag
	public void tagRemovedFromGame(TagEvent e) {
		//		Tag tag = e.getTag();
		//		boolean removed = tags.remove(tag.getName()) != null;
		//		if (removed) {
		//			for (Component cmp : mnuTags.getComponents()) {
		//				if (cmp.getName().equals(tag.getName())) {
		//					mnuTags.remove(cmp);
		//					break;
		//				}
		//			}
		//		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		int w = getWidth();
		int h = getHeight();
		g2d.setColor(IconStore.current().getCurrentTheme().getGameFilterPane().getColor());
		g2d.fillRect(0, 0, w, h);
		BufferedImage background = IconStore.current().getCurrentTheme().getGameFilterPane().getImage();
		if (background != null) {
			g2d.drawImage(background, 0, 0, w, h, this);
		}
		g2d.dispose();
	}
}