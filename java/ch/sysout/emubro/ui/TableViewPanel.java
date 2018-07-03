package ch.sysout.emubro.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import ch.sysout.emubro.api.FilterListener;
import ch.sysout.emubro.api.TagListener;
import ch.sysout.emubro.api.TagsFromGamesListener;
import ch.sysout.emubro.api.event.FilterEvent;
import ch.sysout.emubro.api.event.GameAddedEvent;
import ch.sysout.emubro.api.event.GameRemovedEvent;
import ch.sysout.emubro.api.event.GameRenamedEvent;
import ch.sysout.emubro.api.event.GameSelectionEvent;
import ch.sysout.emubro.api.filter.Criteria;
import ch.sysout.emubro.api.model.Emulator;
import ch.sysout.emubro.api.model.Explorer;
import ch.sysout.emubro.api.model.Game;
import ch.sysout.emubro.api.model.Platform;
import ch.sysout.emubro.api.model.PlatformComparator;
import ch.sysout.emubro.api.model.Tag;
import ch.sysout.emubro.controller.GameSelectionListener;
import ch.sysout.emubro.controller.ViewConstants;
import ch.sysout.emubro.impl.event.BroGameSelectionEvent;
import ch.sysout.emubro.impl.event.NavigationEvent;
import ch.sysout.emubro.impl.model.BroEmulator;
import ch.sysout.emubro.impl.model.BroGame;
import ch.sysout.emubro.impl.model.BroTag;
import ch.sysout.emubro.impl.model.EmulatorConstants;
import ch.sysout.emubro.impl.model.GameConstants;
import ch.sysout.util.ScreenSizeUtil;
import ch.sysout.util.UIUtil;

public class TableViewPanel extends ViewPanel implements ListSelectionListener, GameSelectionListener, FilterListener {
	private static final long serialVersionUID = 1L;

	private TableModel mdlTblAllGames;
	private TableModel mdlTblRecentlyPlayed;
	private TableModel mdlTblFavorites;
	private TableModel mdlTblFiltered;

	private JTable tblGames;
	private TableColumnAdjuster columnAdjuster;
	private List<GameSelectionListener> selectGameListeners = new ArrayList<>();
	private JScrollPane spTblGames;

	// private int[] twok = { 24, 250, 180, 150, 80, 200 };
	// private int[] threek = { 32, 450, 300, 250, 120, 450 };

	private static final int rowHeight = ScreenSizeUtil.adjustValueToResolution(24);
	public static final int FIRST_COLUMN_WIDTH = rowHeight;

	private TableColumnModel columnModel;
	private List<BroGame> games;

	private int lastHorizontalScrollBarValue;

	private boolean hideExtensions = true;

	private boolean touchScreenScrollEnabled;

	private int viewStyle;

	private int currentView;

	private List<UpdateGameCountListener> gameCountListeners = new ArrayList<>();

	private List<Integer> columnWidths = new ArrayList<>();

	protected int mouseX;
	protected int mouseY;

	GameContextMenu popupGame;
	ViewContextMenu popupView;

	private Explorer explorer;

	private boolean doNotFireSelectGameEvent;

	private List<TagsFromGamesListener> tagsFromGamesListeners = new ArrayList<>();

	public TableViewPanel(Explorer explorer, IconStore iconStore, GameContextMenu popupGame, ViewContextMenu popupView) {
		super(new BorderLayout());
		mdlTblAllGames = new GameTableModel(explorer, iconStore);
		mdlTblRecentlyPlayed = new GameTableModel(explorer, iconStore);
		mdlTblFavorites = new GameTableModel(explorer, iconStore);
		mdlTblFiltered = new GameTableModel(explorer, iconStore);
		this.explorer = explorer;
		this.popupGame = popupGame;
		this.popupView = popupView;
		initComponents();
		createUI();

		tblGames.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if (e.getKeyCode() == KeyEvent.VK_CONTEXT_MENU) {
					boolean showFileTreePopup = tblGames.getSelectedRow() != GameConstants.NO_GAME;
					if (showFileTreePopup) {
						showGamePopupMenu(e.getComponent(), mouseX, mouseY);
					}
				}
			}
		});

		tblGames.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});

		tblGames.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				//				dragging = false;
				tblGames.setEnabled(true);
				tblGames.requestFocusInWindow();
				if (SwingUtilities.isRightMouseButton(e)) {
					int index = tblGames.rowAtPoint(e.getPoint());
					if (index != GameConstants.NO_GAME) {
						tblGames.setRowSelectionInterval(index, index);
						showGamePopupMenu(e.getComponent(), e.getX(), e.getY());
					} else {
						tblGames.clearSelection();
						showViewPopupMenu(e.getComponent(), e.getX(), e.getY());
					}
				}
				//	if (sp.getCursor() == cursorDrag) {
				//		sp.setCursor(null);
				//	}
				//	smoothScrollOut(sp, lst);
			}
		});
	}

	protected void showGamePopupMenu(Component relativeTo, int x, int y) {
		popupGame.show(relativeTo, x, y);
		List<Game> currentGame = explorer.getCurrentGames();
		int platformId = currentGame.get(0).getPlatformId();
		Platform platform = explorer.getPlatform(platformId);
		List<BroEmulator> emulators = platform.getEmulators();
		int defaultEmulatorIndex = EmulatorConstants.NO_EMULATOR;
		int notInstalledEmulatorsSkipped = 0;
		for (int i = 0; i < emulators.size(); i++) {
			Emulator emulator = emulators.get(i);
			if (!emulator.isInstalled()) {
				notInstalledEmulatorsSkipped++;
				continue;
			}
			if (emulator.getId() == platform.getDefaultEmulatorId()) {
				defaultEmulatorIndex = i - notInstalledEmulatorsSkipped;
				break;
			}
		}
		popupGame.initEmulators(emulators, defaultEmulatorIndex);
	}

	protected void showViewPopupMenu(Component relativeTo, int x, int y) {
		popupView.show(relativeTo, x, y);
	}

	private void initComponents() {
		tblGames = new JTableDoubleClickOnHeaderFix();
		columnModel = tblGames.getColumnModel();
		columnAdjuster = new TableColumnAdjuster(tblGames);
		// columnModel.getColumn(0).setResizable(false);

		// minWidth = col.getWidth();
		spTblGames = new JScrollPane(tblGames);
		Color color = UIManager.getColor("Table.background");
		spTblGames.getViewport().setBackground(color);

		TableCellRenderer renderer = tblGames.getTableHeader().getDefaultRenderer();
		((JLabel) renderer).setHorizontalAlignment(SwingConstants.LEFT);
		tblGames.getTableHeader().setDefaultRenderer(renderer);
		tblGames.setIntercellSpacing(new Dimension(0, 0));
		tblGames.setShowGrid(false);
		tblGames.getColumnModel().setColumnMargin(0);
		tblGames.setAutoscrolls(false);
		tblGames.getTableHeader().setReorderingAllowed(false);
		tblGames.setFillsViewportHeight(true);
		tblGames.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblGames.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tblGames.setRowHeight(rowHeight);
		tblGames.setPreferredScrollableViewportSize(new Dimension(0, 0));
		tblGames.setAutoCreateRowSorter(true);
		tblGames.getSelectionModel().addListSelectionListener(this);
	}

	public static void scrollToVisible(JTable table, int rowIndex, int vColIndex) {
		if (!(table.getParent() instanceof JViewport)) {
			return;
		}
		JViewport viewport = (JViewport) table.getParent();

		// This rectangle is relative to the table where the
		// northwest corner of cell (0,0) is always (0,0).
		Rectangle rect = table.getCellRect(rowIndex, vColIndex, true);

		// The location of the viewport relative to the table
		Point pt = viewport.getViewPosition();

		// Translate the cell location so that it is relative
		// to the view, assuming the northwest corner of the
		// view is (0,0)
		rect.setLocation(rect.x - pt.x, rect.y - pt.y);

		table.scrollRectToVisible(rect);

		// Scroll the area into view
		// viewport.scrollRectToVisible(rect);
	}

	public void adjustColumns() {
		columnAdjuster.adjustColumns();
		// columnModel.getColumn(0).setWidth(FIRST_COLUMN_WIDTH);
	}

	private void createUI() {
		spTblGames.setBorder(BorderFactory.createEmptyBorder());
		add(spTblGames);
		setPreferredSize(new Dimension(0, 0));
	}

	@Override
	public boolean requestFocusInWindow() {
		return tblGames.requestFocusInWindow();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (doNotFireSelectGameEvent) {
			return;
		}
		// int selectedRow = tblGames.getSelectedRow();
		// Game game = (selectedRow != -1) ? explorer.getGame(selectedRow) :
		// null;
		// GameEvent event = new GameSelectionEvent(game);
		// fireEvent(event);
		if (!e.getValueIsAdjusting()) {
			int[] indices = tblGames.getSelectedRows();
			boolean b = indices.length > 0;
			// mnuRunWith.removeAll();
			List<Game> gamesList = new ArrayList<>();
			if (b) {
				for (Integer index : indices) {
					GameTableModel model = ((GameTableModel) tblGames.getModel());
					Game game = (Game) model.getValueAt(tblGames.convertRowIndexToModel(index), -1);
					if (game != null) {
						gamesList.add(game);
					}
				}
				// mnuRunWith.add(new JMenuItem(""+game.getEmulatorId()));
			}

			// lstGames.setComponentPopupMenu(b ? popup : null);

			GameSelectionEvent event = new BroGameSelectionEvent(gamesList, null);
			fireGameSelectedEvent(event);

			// Game game = b ? mdlLstAllGames.getElementAt(index) : null;
			// BroGameSelectionEvent event = new BroGameSelectionEvent(game);
			// fireEvent(event);
		}
		spTblGames.getHorizontalScrollBar().setValue(lastHorizontalScrollBarValue);
		lastHorizontalScrollBarValue = spTblGames.getHorizontalScrollBar().getValue();
		int selectedRow = tblGames.getSelectedRow();
		tblGames.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
		tblGames.scrollRectToVisible(new Rectangle(tblGames.getCellRect(selectedRow, 0, true)));
	}

	private void fireGameSelectedEvent(GameSelectionEvent event) {
		for (GameSelectionListener l : selectGameListeners) {
			l.gameSelected(event);
		}
	}

	public boolean isInitialized() {
		return games != null;
	}

	@Override
	public void selectGame(int gameId) {
		if (gameId == GameConstants.NO_GAME) {
			tblGames.clearSelection();
		} else {
			int selectedIndex = GameConstants.NO_GAME;
			for (int i = 0; i < tblGames.getModel().getRowCount(); i++) {
				Game game = (Game) tblGames.getModel().getValueAt(i, -1);
				if (game.getId() == gameId) {
					selectedIndex = i;
					tblGames.setRowSelectionInterval(tblGames.convertRowIndexToView(selectedIndex), tblGames.convertRowIndexToView(selectedIndex));
					//				lstGames.ensureIndexIsVisible(selectedIndex);
					break;
				}
			}
		}
	}

	@Override
	public void gameSelected(GameSelectionEvent e) {
	}

	@Override
	public void addSelectGameListener(GameSelectionListener l) {
		selectGameListeners.add(l);
	}

	@Override
	public void addRunGameListener(MouseListener l) {
		tblGames.addMouseListener(l);
	}

	@Override
	public void addRunGameListener(Action l) {
		tblGames.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "actionRunGame");
		tblGames.getActionMap().put("actionRunGame", l);
		//		tblGames.getInputMap().put(KeyStroke.getKeyStroke("pressed ENTER"), "actionRunGame");
		//		tblGames.getActionMap().put("actionRunGame", l);
	}

	public void setSortOrder(int order) {
		tblGames.getRowSorter().toggleSortOrder(order);
	}

	@Override
	public void increaseFontSize() {
		int newRowHeight = getRowHeight() + 4;
		int newColumnWidth = getColumnWidth() + 64;
		setRowHeight(newRowHeight);
		setColumnWidth(newColumnWidth);
		Font font = tblGames.getFont();
		tblGames.setFont(new Font(font.getFontName(), font.getStyle(), font.getSize() + 2));
	}

	@Override
	public void decreaseFontSize() {
		int newRowHeight = getRowHeight() - 4;
		int newColumnWidth = getColumnWidth() - 64;
		if (newRowHeight > 0) {
			setRowHeight(newRowHeight);
			setColumnWidth(newColumnWidth);
			Font font = tblGames.getFont();
			tblGames.setFont(new Font(font.getFontName(), font.getStyle(), font.getSize() - 2));
		}
	}

	@Override
	public int getColumnWidth() {
		if (columnModel.getColumnCount() > 1) {
			TableColumn column = columnModel.getColumn(1);
			int columnWidth = column.getWidth();
			return columnWidth;
		}
		return 128;
	}

	@Override
	public void setColumnWidth(int value) {
		tblGames.getColumnModel().getColumn(1).setWidth(value);
	}

	@Override
	public int getRowHeight() {
		return tblGames.getRowHeight();
	}

	@Override
	public void setRowHeight(int value) {
		if (value < 1) {
			value = 25;
		}
		tblGames.setRowHeight(value);
	}

	@Override
	public void addGameDragDropListener(DropTargetListener l) {
		new DropTarget(spTblGames, l);
	}

	@Override
	public void languageChanged() {
		popupGame.languageChanged();
		popupView.languageChanged();
		((GameTableModel) mdlTblAllGames).languageChanged();
		List<Integer> columnWidths = new ArrayList<>();

		for (int i = 0; i < tblGames.getColumnModel().getColumnCount(); i++) {
			TableColumn nextElement = columnModel.getColumn(i);
			int columnWidth = nextElement.getWidth();
			columnWidths.add(columnWidth);
		}

		List<? extends SortKey> sortKeys = tblGames.getRowSorter().getSortKeys();
		SortKey key = null;
		for (int i = 0; i < sortKeys.size(); i++) {
			key = sortKeys.get(i);
			if (key.getSortOrder() != SortOrder.UNSORTED) {
				break;
			}
		}

		final SortKey keyFinal = key;
		// FIXME this runnable has been done because otherwise language isnt
		// changed yet. dirty.
		Runnable runnableResetColumns = new Runnable() {

			@Override
			public void run() {
				((GameTableModel) tblGames.getModel()).setColumnIdentifiersNow();
				if (keyFinal != null && keyFinal.getColumn() != -1) {
					SortOrder sortOrder = keyFinal.getSortOrder();
					tblGames.getRowSorter().toggleSortOrder(keyFinal.getColumn());
					if (sortOrder == SortOrder.DESCENDING) {
						tblGames.getRowSorter().toggleSortOrder(keyFinal.getColumn());
					}
				}

				for (int i = tblGames.getColumnModel().getColumnCount() - 1; i >= 0; i--) {
					TableColumn nextElement = columnModel.getColumn(i);
					int minWidth = nextElement.getMinWidth();
					nextElement.setMinWidth(columnWidths.get(i));
					nextElement.setMinWidth(minWidth);

					int maxWidth = columnModel.getColumn(i).getMaxWidth();
					columnModel.getColumn(i).setMaxWidth(columnWidths.get(i));
					columnModel.getColumn(i).setMaxWidth(maxWidth);
				}

			}
		};
		SwingUtilities.invokeLater(runnableResetColumns);
	}

	@Override
	public void groupByNone() {
	}

	@Override
	public void groupByPlatform() {
	}

	@Override
	public void groupByTitle() {
	}

	@Override
	public void hideExtensions(boolean selected) {
		hideExtensions = selected;
	}

	public int getfontSize() {
		return tblGames.getFont().getSize();
	}

	@Override
	public void setFontSize(int value) {
		Font font = tblGames.getFont();
		tblGames.setFont(new Font(font.getFontName(), font.getStyle(), value));
	}

	@Override
	public int getGroupBy() {
		return ViewConstants.GROUP_BY_NONE;
		//			return ViewConstants.GROUP_BY_PLATFORM;
		//		throw new IllegalStateException("current viewport not known");
	}

	@Override
	public void gameRated(Game game) {
		if (game.getRate() > 0) {
			if (!((GameTableModel) mdlTblFavorites).contains(game)) {
				((GameTableModel) mdlTblFavorites).addRow(game);
			}
		} else {
			((GameTableModel) mdlTblFavorites).removeGame(game);
		}
		UIUtil.revalidateAndRepaint(spTblGames);
	}

	public void addGame(Game game, ImageIcon gameIcon) {
		((GameTableModel) mdlTblAllGames).addRow(game);
		((GameTableModel) mdlTblAllGames).addGameIcon(game.getId(), gameIcon);
		if (game.isFavorite()) {
			((GameTableModel) mdlTblFavorites).addRow(game);
		}
	}

	@Override
	public void gameAdded(GameAddedEvent e, FilterEvent filterEvent) {
		Game game = e.getGame();
		((GameTableModel) mdlTblAllGames).addRow(game);
		filterSet(filterEvent);
		if (e.isManuallyAdded()) {
			selectGame(game.getId());
		}
	}

	@Override
	public void gameRemoved(GameRemovedEvent e) {
		Game game = e.getGame();
		((GameTableModel) mdlTblAllGames).removeGame(game);
		((GameTableModel) mdlTblRecentlyPlayed).removeGame(game);
		((GameTableModel) mdlTblRecentlyPlayed).removeGame(game);
		((GameTableModel) mdlTblFavorites).removeGame(game);
		selectGame(GameConstants.NO_GAME);
	}

	@Override
	public void initGameList(List<Game> games, int currentNavView) {
		if (mdlTblAllGames.getRowCount() == 0) {
			for (Game game : games) {
				addGame(game, null);
			}
			switch (currentNavView) {
			case NavigationPanel.ALL_GAMES:
				tblGames.setModel(mdlTblAllGames);
				break;
			case NavigationPanel.RECENTLY_PLAYED:
				tblGames.setModel(mdlTblRecentlyPlayed);
				break;
			case NavigationPanel.FAVORITES:
				tblGames.setModel(mdlTblFavorites);
				break;
			default:
				tblGames.setModel(mdlTblAllGames);
				break;
			}
			TableCellRenderer customRenderer = new CustomRenderer();
			columnModel.getColumn(1).setCellRenderer(customRenderer);
			columnModel.getColumn(2).setCellRenderer(customRenderer);
			columnModel.getColumn(3).setCellRenderer(customRenderer);
			columnModel.getColumn(4).setCellRenderer(customRenderer);
		}
	}

	@Override
	public void sortOrder(int sortOrder) {

	}

	@Override
	public void sortBy(int sortBy, PlatformComparator platformComparator) {
		int selectedRow = tblGames.getSelectedRow();
		Game selectedGame = (selectedRow != -1) ? explorer.getGame(selectedRow) : null;

		doNotFireSelectGameEvent = true;
		switch (sortBy) {
		case ViewConstants.SORT_BY_PLATFORM:
			tblGames.getRowSorter().toggleSortOrder(2);
			break;
		case ViewConstants.SORT_BY_TITLE:
			tblGames.getRowSorter().toggleSortOrder(1);
			break;
		}
		if (selectedGame != null) {
			int selectedGameId = selectedGame.getId();
			selectGame(selectedGameId);
		}
		doNotFireSelectGameEvent = false;
	}

	@Override
	public void navigationChanged(NavigationEvent e, FilterEvent filterEvent) {
		int gameCount = 0;
		switch (e.getView()) {
		case NavigationPanel.ALL_GAMES:
			setCurrentView(NavigationPanel.ALL_GAMES);
			gameCount = mdlTblAllGames.getRowCount();
			tblGames.setModel(mdlTblAllGames);
			break;
		case NavigationPanel.RECENTLY_PLAYED:
			setCurrentView(NavigationPanel.RECENTLY_PLAYED);
			tblGames.setModel(mdlTblRecentlyPlayed);
			break;
		case NavigationPanel.FAVORITES:
			setCurrentView(NavigationPanel.FAVORITES);
			tblGames.setModel(mdlTblFavorites);
			break;
		default:
			setCurrentView(NavigationPanel.ALL_GAMES);
			tblGames.setModel(mdlTblAllGames);
			break;
		}
		if (filterEvent.isPlatformFilterSet() || filterEvent.isGameFilterSet()) {
			((GameTableModel) mdlTblFiltered).removeAllElements();
			filterSet(filterEvent);
			gameCount = mdlTblFiltered.getRowCount();
		}
		fireUpdateGameCountEvent(gameCount);
	}

	public int getCurrentView() {
		return currentView;
	}

	private void setCurrentView(int currentView) {
		this.currentView = currentView;
	}

	@Override
	public void pinColumnWidthSliderPanel(JPanel pnlColumnWidthSlider) {
		add(pnlColumnWidthSlider, BorderLayout.SOUTH);
		pnlColumnWidthSlider.setVisible(true);
		UIUtil.revalidateAndRepaint(this);
	}

	@Override
	public void unpinColumnWidthSliderPanel(JPanel pnlColumnWidthSlider) {
		remove(pnlColumnWidthSlider);
		UIUtil.revalidateAndRepaint(this);
	}

	@Override
	public void pinRowHeightSliderPanel(JPanel pnlRowHeightSlider) {
		add(pnlRowHeightSlider, BorderLayout.EAST);
		pnlRowHeightSlider.setVisible(true);
		UIUtil.revalidateAndRepaint(this);
	}

	@Override
	public void unpinRowHeightSliderPanel(JPanel pnlRowHeightSlider) {
		remove(pnlRowHeightSlider);
		UIUtil.revalidateAndRepaint(this);
	}

	@Override
	public void addDecreaseFontListener(Action l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addIncreaseFontListener(Action l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addIncreaseFontListener2(MouseWheelListener l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addOpenGamePropertiesListener(Action l) {
		tblGames.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.ALT_DOWN_MASK),
				"actionOpenGameProperties");
		tblGames.getActionMap().put("actionOpenGameProperties", l);
	}

	@Override
	public void addRemoveGameListener(Action l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addCommentListener(ActionListener l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addOpenGameFolderListener1(MouseListener l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addRateListener(RateListener l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addRenameGameListener(Action l) {
		tblGames.getInputMap().put(KeyStroke.getKeyStroke("F2"), "actionRenameGame");
		tblGames.getActionMap().put("actionRenameGame", l);
	}

	@Override
	public void selectNextGame() {
		//		int nextIndex = lstGames.getSelectedIndex() + 1;
		//		if (nextIndex < lstGames.getModel().getSize()) {
		//			Game game = lstGames.getModel().getElementAt(nextIndex);
		//			selectGame(game.getId());
		//		}
	}

	@Override
	public void selectPreviousGame() {
		//		int previousIndex = lstGames.getSelectedIndex() - 1;
		//		if (previousIndex < lstGames.getModel().getSize()) {
		//			Game game = lstGames.getModel().getElementAt(previousIndex);
		//			selectGame(game.getId());
		//		}
	}

	@Override
	public boolean isTouchScreenScrollEnabled() {
		return touchScreenScrollEnabled;
	}

	@Override
	public void setTouchScreenScrollEnabled(boolean touchScreenScrollEnabled) {
		this.touchScreenScrollEnabled = touchScreenScrollEnabled;
	}

	@Override
	public void setViewStyle(int viewStyle) {
		this.viewStyle = viewStyle;
	}

	@Override
	public void addUpdateGameCountListener(UpdateGameCountListener l) {
		gameCountListeners.add(l);
	}

	private void fireUpdateGameCountEvent(int gameCount) {
		for (UpdateGameCountListener l : gameCountListeners) {
			l.gameCountUpdated(gameCount);
		}
	}

	@Override
	public void gameCoverAdded(int gameId, ImageIcon ico) {

	}

	@Override
	public void addAddGameOrEmulatorFromClipboardListener(Action l) {
		tblGames.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK),
				"actionAddGameOrEmulatorFromClipboard");
		tblGames.getActionMap().put("actionAddGameOrEmulatorFromClipboard", l);
	}

	class CustomRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = -1;
		private Color colorFavorite = new Color(250, 176, 42);

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
					column);
			Game game = (Game) mdlTblAllGames.getValueAt(table.convertRowIndexToModel(row), -1);
			if (game != null) {
				if (isSelected) {
					cellComponent.setForeground(UIManager.getColor("Table.selectionForeground"));
				} else {
					if (game.isFavorite()) {
						cellComponent.setForeground(colorFavorite);
						cellComponent.setFont(cellComponent.getFont().deriveFont(Font.BOLD));

					} else {
						cellComponent.setForeground(null);
						cellComponent.setFont(cellComponent.getFont().deriveFont(Font.PLAIN));
					}
				}
			}
			return cellComponent;
		}
	}

	@Override
	public void filterSet(FilterEvent event) {
		int selectedRow = tblGames.getSelectedRow();
		Game selectedGame = (selectedRow != -1) ? (Game) tblGames.getValueAt(selectedRow, -1) : null;
		int selectedGameId = (selectedGame != null) ? selectedGame.getId() : GameConstants.NO_GAME;
		doNotFireSelectGameEvent = true;
		//		rememberColumnWidths(tblGames);
		tblGames.setAutoCreateColumnsFromModel(false);
		if (!event.isGameFilterSet()) {
			((GameTableModel) mdlTblFiltered).removeAllElements();
			if (!event.isPlatformFilterSet()) {

				// no tag filter set
				if (!event.isTagFilterSet()) {
					if (getCurrentView() == NavigationPanel.ALL_GAMES) {
						tblGames.setModel(mdlTblAllGames);

						List<Tag> tagsFromGames = new ArrayList<>();
						for (Game game : ((GameTableModel) mdlTblAllGames).getAllElements()) {
							tagsFromGames.addAll(game.getTags());
						}
						fireTagFilterEvent(tagsFromGames, true);
					}
					if (getCurrentView() == NavigationPanel.RECENTLY_PLAYED) {
						tblGames.setModel(mdlTblRecentlyPlayed);

						List<Tag> tagsFromGames = new ArrayList<>();
						for (Game game : ((GameTableModel) mdlTblRecentlyPlayed).getAllElements()) {
							tagsFromGames.addAll(game.getTags());
						}
						fireTagFilterEvent(tagsFromGames, true);
					}
					if (getCurrentView() == NavigationPanel.FAVORITES) {
						tblGames.setModel(mdlTblFavorites);

						List<Tag> tagsFromGames = new ArrayList<>();
						for (Game game : ((GameTableModel) mdlTblFavorites).getAllElements()) {
							tagsFromGames.addAll(game.getTags());
						}
						fireTagFilterEvent(tagsFromGames, true);
					}
					// tag filter set but no other filters
				} else {
					if (getCurrentView() == NavigationPanel.ALL_GAMES) {
						checkTagFilter(event, (GameTableModel) mdlTblAllGames);
					}
					if (getCurrentView() == NavigationPanel.RECENTLY_PLAYED) {
						checkTagFilter(event, (GameTableModel) mdlTblRecentlyPlayed);
					}
					if (getCurrentView() == NavigationPanel.FAVORITES) {
						checkTagFilter(event, (GameTableModel) mdlTblFavorites);
					}
				}
			} else {
				if (getCurrentView() == NavigationPanel.ALL_GAMES) {
					checkPlatformFilter(event, (GameTableModel) mdlTblAllGames);
				}
				if (getCurrentView() == NavigationPanel.RECENTLY_PLAYED) {
					checkPlatformFilter(event, (GameTableModel) mdlTblRecentlyPlayed);
				}
				if (getCurrentView() == NavigationPanel.FAVORITES) {
					checkPlatformFilter(event, (GameTableModel) mdlTblFavorites);
				}
			}
		} else {
			List<Game> tmpGames = null;
			if (getCurrentView() == NavigationPanel.ALL_GAMES) {
				tmpGames = ((GameTableModel) mdlTblAllGames).getAllElements();
			}
			if (getCurrentView() == NavigationPanel.RECENTLY_PLAYED) {
				tmpGames = ((GameTableModel) mdlTblRecentlyPlayed).getAllElements();
			}
			if (getCurrentView() == NavigationPanel.FAVORITES) {
				tmpGames = ((GameTableModel) mdlTblFavorites).getAllElements();
			}
			int platformId = event.getPlatformId();
			Criteria criteria = event.getCriteria();
			for (Game game : tmpGames) {
				if (game.getName().toLowerCase().contains(criteria.getText().toLowerCase())) {
					if (!((GameTableModel) mdlTblFiltered).contains(game)) {
						if (getCurrentView() == NavigationPanel.FAVORITES && !game.isFavorite()) {
							continue;
						} else {
							if (!event.isPlatformFilterSet()) {
								((GameTableModel) mdlTblFiltered).addRow(game);
							} else {
								if (game.getPlatformId() == platformId) {
									((GameTableModel) mdlTblFiltered).addRow(game);
								}
							}
						}
					} else {
						if (getCurrentView() == NavigationPanel.FAVORITES) {
							if (!game.isFavorite()) {
								((GameTableModel) mdlTblFiltered).removeGame(game);
							}
						}
						if (event.isPlatformFilterSet()) {
							if (game.getPlatformId() != platformId) {
								((GameTableModel) mdlTblFiltered).removeGame(game);
							}
						}
					}
				} else {
					if (((GameTableModel) mdlTblFiltered).contains(game)) {
						((GameTableModel) mdlTblFiltered).removeGame(game);
					}
				}
			}
			tblGames.setModel(mdlTblFiltered);
			List<Tag> tagsFromGames = new ArrayList<>();
			for (Game game : ((GameTableModel) mdlTblFiltered).getAllElements()) {
				tagsFromGames.addAll(game.getTags());
			}
			fireTagFilterEvent(tagsFromGames, true);
		}
		if (selectedGameId != GameConstants.NO_GAME) {
			selectGame(selectedGameId);
			if (tblGames.getSelectedRow() == GameConstants.NO_GAME) {
				doNotFireSelectGameEvent = false;
				fireGameSelectedEvent(new BroGameSelectionEvent());
			}
		}
		doNotFireSelectGameEvent = false;
		//		fireUpdateGameCountEvent(((GameTableModel) tblGames.getModel()).getRowCount());
		UIUtil.revalidateAndRepaint(tblGames);
		//				setRememberedColumnWidths(tblGames);
	}

	private void setRememberedColumnWidths(JTable tbl) {
		TableColumnModel columnModel = tbl.getColumnModel();
		for (int i = 0; i < columnModel.getColumnCount(); i++) {
			TableColumn column = columnModel.getColumn(i);
			column.setWidth(columnWidths.get(i));
		}
	}

	private void rememberColumnWidths(JTable tbl) {
		columnWidths.clear();
		TableColumnModel columnModel = tbl.getColumnModel();
		for (int i = 0; i < columnModel.getColumnCount(); i++) {
			TableColumn column = columnModel.getColumn(i);
			columnWidths.add(column.getWidth());
		}
	}

	private void checkPlatformFilter(FilterEvent event, GameTableModel mdlTblGames) {
		int platformId = event.getPlatformId();

		for (Game game : mdlTblGames.getAllElements()) {
			if (game.getPlatformId() == platformId) {
				((GameTableModel) mdlTblFiltered).addRow(game);
			}
		}
		tblGames.setModel(mdlTblFiltered);

		List<Tag> tagsFromGames = new ArrayList<>();
		for (Game game : ((GameTableModel) mdlTblFiltered).getAllElements()) {
			tagsFromGames.addAll(game.getTags());
		}
		fireTagFilterEvent(tagsFromGames, true);
	}

	private void checkTagFilter(FilterEvent event, GameTableModel mdlTblAllGames2) {
		List<BroTag> tags = event.getCriteria().getTags();
		List<Tag> tagsFromGames = new ArrayList<>();
		List<Game> games = new ArrayList<>(mdlTblAllGames2.getAllElements());
		outerloop:
			for (Game game : games) {
				for (BroTag tag : tags) {
					int tagId = tag.getId();
					if (!game.hasTag(tagId)) {
						continue outerloop;
					}
				}
				if (!((GameTableModel) mdlTblFiltered).contains(game)) {
					((GameTableModel) mdlTblFiltered).addRow(game);
				}
			}
		GameTableModel mdl = (GameTableModel) mdlTblFiltered;
		boolean removeUnusedTags = false;
		if (mdl.getAllElements().isEmpty()) {
			mdl = (GameTableModel) mdlTblAllGames;
			removeUnusedTags = true;
		}
		tblGames.setModel(mdl);
		for (Game game : mdl.getAllElements()) {
			tagsFromGames.addAll(game.getTags());
		}
		fireTagFilterEvent(tagsFromGames, removeUnusedTags);
	}

	private void fireTagFilterEvent(List<Tag> tags, boolean removeUnusedTags) {
		for (TagsFromGamesListener l : tagsFromGamesListeners) {
			l.tagsInCurrentViewChanged(tags, removeUnusedTags);
		}
	}
	@Override
	public void gameRenamed(GameRenamedEvent event) {
	}

	@Override
	public void addCoverDragDropListener(DropTargetListener l) {
		new DropTarget(tblGames, l);
	}

	@Override
	public Component getDefaultFocusableComponent() {
		return tblGames;
	}

	@Override
	public void addTagListener(TagListener l) {
	}

	@Override
	public void addTagsFromGamesListener(TagsFromGamesListener l) {
		tagsFromGamesListeners.add(l);
	}

	@Override
	public List<Game> getGames() {
		return ((GameTableModel) tblGames.getModel()).getAllElements();
	}
}