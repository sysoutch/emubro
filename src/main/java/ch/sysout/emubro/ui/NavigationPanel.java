package ch.sysout.emubro.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GrayFilter;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import ch.sysout.emubro.api.GameViewListener;
import ch.sysout.emubro.impl.event.NavigationEvent;
import ch.sysout.emubro.util.MessageConstants;
import ch.sysout.ui.ImageUtil;
import ch.sysout.util.Icons;
import ch.sysout.util.Messages;
import ch.sysout.util.ScreenSizeUtil;
import ch.sysout.util.UIUtil;

public class NavigationPanel extends JPanel implements ActionListener, GameViewListener {
	public static final long serialVersionUID = 1L;
	public static final int ALL_GAMES = 0;
	public static final int RECENTLY_PLAYED = 1;
	public static final int FAVORITES = 2;
	public static final String MINIMIZED = "min";
	public static final String CENTERED = "centered";
	public static final String MAXIMIZED = "maximized";

	private JToggleButton btnAllGames = new JToggleButton(Messages.get(MessageConstants.ALL_GAMES));
	private JToggleButton btnRecentlyPlayed = new JToggleButton(Messages.get(MessageConstants.RECENTLY_PLAYED));
	private JToggleButton btnFavorites = new JToggleButton(Messages.get(MessageConstants.FAVORITES));
	private List<JToggleButton> platformButtons = new ArrayList<>();
	private AbstractButton[] buttons = new AbstractButton[] { btnAllGames, btnRecentlyPlayed, btnFavorites };

	private int oldWidth;
	private boolean minimizing;
	private JScrollPane spNavigationButtons;
	private JPanel pnlPlatforms;
	private FormLayout layoutPopup;
	private CellConstraints ccPopup;
	private JDialog dlgPopup = new JDialog();
	private AbstractButton btnSelectAll = new JButton(Messages.get(MessageConstants.SELECT_ALL));
	private AbstractButton btnSelectNone = new JButton(Messages.get(MessageConstants.UNSELECT_ALL));
	private AbstractButton btnSelectInvert = new JButton(Messages.get(MessageConstants.SELECT_INVERT));
	private JPanel pnlPopup;
	private int currentNavView = ALL_GAMES;

	public NavigationPanel() {
		super(new BorderLayout());
		initComponents();
		createUI();
		System.out.println(getInsets().left);
	}

	private void initComponents() {
		spNavigationButtons = new JScrollPane();
		int size = ScreenSizeUtil.is3k() ? 10 : 5;
		Insets insets = new Insets(size, size, size, size);
		btnAllGames.setFont(ScreenSizeUtil.defaultFont());
		btnRecentlyPlayed.setFont(ScreenSizeUtil.defaultFont());
		btnFavorites.setFont(ScreenSizeUtil.defaultFont());

		btnAllGames.setHorizontalAlignment(SwingConstants.LEFT);
		btnAllGames.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnAllGames.setVerticalTextPosition(SwingConstants.CENTER);
		btnAllGames.setFocusable(false);
		btnAllGames.setBorder(new EmptyBorder(insets));

		btnRecentlyPlayed.setHorizontalAlignment(SwingConstants.LEFT);
		btnRecentlyPlayed.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnRecentlyPlayed.setVerticalTextPosition(SwingConstants.CENTER);
		btnRecentlyPlayed.setFocusable(false);
		btnRecentlyPlayed.setBorder(new EmptyBorder(insets));

		btnFavorites.setHorizontalAlignment(SwingConstants.LEFT);
		btnFavorites.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnFavorites.setVerticalTextPosition(SwingConstants.CENTER);
		btnFavorites.setFocusable(false);
		btnFavorites.setBorder(new EmptyBorder(insets));

		UIUtil.doHover(false, btnAllGames, btnRecentlyPlayed, btnFavorites);
		dlgPopup.setLayout(new BorderLayout());
		dlgPopup.setUndecorated(true);
		layoutPopup = new FormLayout("min:grow");
		pnlPopup = new JPanel(layoutPopup);
		pnlPopup.setBorder(BorderFactory.createEtchedBorder());
		ccPopup = new CellConstraints();
		// initCmbPlatformsNow(null);
		setToolTipTexts();
		setIcons();
		addToButtonGroup(new ButtonGroup(), btnAllGames, btnRecentlyPlayed, btnFavorites);
		addListeners();
	}

	private void setToolTipTexts() {
		btnAllGames.setToolTipText(Messages.get(MessageConstants.ALL_GAMES));
		btnRecentlyPlayed.setToolTipText(Messages.get(MessageConstants.RECENTLY_PLAYED));
		btnFavorites.setToolTipText(Messages.get(MessageConstants.FAVORITES));
	}

	private void createUI() {
		//		setBorder(BorderFactory.createEtchedBorder());
		FormLayout layout = new FormLayout("default:grow", "fill:default, min, fill:default, min, fill:default");
		JPanel pnl = new JPanel(layout);
		CellConstraints cc = new CellConstraints();
		pnl.add(btnAllGames, cc.xy(1, 1));
		pnl.add(btnRecentlyPlayed, cc.xy(1, 3));
		pnl.add(btnFavorites, cc.xy(1, 5));

		pnlPlatforms = new JPanel();
		// pnlPlatforms.setBackground(UIManager.getColor("List.background"));
		FormLayout layout2 = new FormLayout("default:grow", "");
		pnlPlatforms.setLayout(layout2);
		new CellConstraints();

		// JScrollPane spPlatforms = new JScrollPane(pnlPlatforms);
		// spPlatforms.getVerticalScrollBar().setUnitIncrement(16);
		// spPlatforms.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// spPlatforms.setBorder(BorderFactory.createEmptyBorder());
		// spPlatforms.setMinimumSize(new Dimension(0, 0));

		spNavigationButtons.setViewportView(pnl);
		spNavigationButtons.validate();
		spNavigationButtons.repaint();
		spNavigationButtons.setBorder(BorderFactory.createEmptyBorder());
		spNavigationButtons.getVerticalScrollBar().setUnitIncrement(16);
		spNavigationButtons.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		spNavigationButtons.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		new JPanel(new BorderLayout());

		// pnl3.add(new JSeparator(), BorderLayout.NORTH);
		// pnl3.add(btnPlatformFilter, BorderLayout.NORTH);
		// pnl3.add(spPlatforms);
		// splPlatformFilter.setContinuousLayout(true);
		// int minimumDividerLocation =
		// splPlatformFilter.getMinimumDividerLocation();
		// int yea = minimumDividerLocation
		// + btnAllGames.getHeight()
		// + btnRecentlyPlayed.getHeight()
		// + btnFavorites.getHeight();
		// int maximumDividerLocation =
		// splPlatformFilter.getMaximumDividerLocation();
		// splPlatformFilter.setDividerLocation(
		// (splPlatformFilter.getDividerLocation() <= yea ?
		// splPlatformFilter.getDividerLocation()-1 : yea));
		// maximumDividerLocation-btnPlatformFilter.getHeight());
		// splPlatformFilter.setResizeWeight(1);
		// splPlatformFilter.setDividerSize(0);
		// splPlatformFilter.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY,
		// new PropertyChangeListener() {
		// @Override
		// public void propertyChange(PropertyChangeEvent e) {
		// System.err.println(splPlatformFilter.getDividerLocation() +"=="+
		// splPlatformFilter.getMaximumDividerLocation());
		// if (splPlatformFilter.getDividerLocation() >=
		// splPlatformFilter.getMaximumDividerLocation()-btnPlatformFilter.getHeight())
		// {
		// splPlatformFilter.setDividerLocation(splPlatformFilter.getMaximumDividerLocation()-btnPlatformFilter.getHeight());
		// // btnPlatformFilter.setSelected(false);
		// // doIt();
		// } else {
		// if (!btnPlatformFilter.isSelected()) {
		// // btnPlatformFilter.setSelected(true);
		// // doIt();
		// }
		// }
		// }
		// });
		add(spNavigationButtons, BorderLayout.CENTER);
	}

	// public void initCmbPlatforms(ViewPanel currentViewPanel) {
	// initCmbPlatformsNow(currentViewPanel);
	// }

	// private void initCmbPlatformsNow(ViewPanel currentViewPanel) {
	// if (currentViewPanel != null) {
	// int col = 1;
	// for (Platform p : platforms) {
	// final JCheckBoxMenuItem chk = new JCheckBoxMenuItem(p.getName(), true);
	// ImageIcon platformIcon = currentViewPanel.getPlatformIcon(p.getId());
	// chk.setIcon(platformIcon);
	// chk.setOpaque(true);
	// chk.setBackground(UIManager.getColor("List.background"));
	// chk.addActionListener(new ActionListener() {
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// doIt(chk, platformIcon);
	// // popup.show(btnPlatformFilter, btnPlatformFilter.getWidth(),
	// (-popup.getHeight()) + btnPlatformFilter.getHeight());
	// Insets screenInsets =
	// Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
	// int taskBarHeight = screenInsets.bottom;
	// System.out.println(taskBarHeight);
	// Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	// int yy = btnPlatformFilter.getLocationOnScreen().y;
	// int hh = dlgPopup.getHeight();
	// int result = yy + hh;
	// int doThis = 0;
	// if (result > screenSize.height-taskBarHeight) {
	// int difference = (result - (screenSize.height-taskBarHeight));
	// doThis -= difference;
	// }
	// }
	// });
	// layoutPopup.appendRow(RowSpec.decode("fill:pref:grow"));
	// int row = layoutPopup.getRowCount();
	// pnlPopup.add(chk, ccPopup.xy(col, row));
	// }
	// }
	// // JPanel pnlOutter = new JPanel(new BorderLayout());
	// // pnlOutter.add(new JSeparator(), BorderLayout.NORTH);
	// JPanel pnl = new JPanel(new FlowLayout());
	// pnl.add(btnSelectAll);
	// pnl.add(btnSelectNone);
	// pnl.add(btnSelectInvert);
	// layoutPopup.appendRow(RowSpec.decode("$lgap"));
	// layoutPopup.appendRow(RowSpec.decode("fill:pref"));
	// pnlPopup.add(pnl, ccPopup.xy(1, layoutPopup.getRowCount()));
	//
	// dlgPopup.add(pnlPopup);
	// dlgPopup.pack();
	//
	// // popup.setLocation(btnPlatformFilter.getX() +
	// btnPlatformFilter.getWidth(),
	// // btnPlatformFilter.getY());
	// // popup.setVisible(true);
	// // btnPlatformFilter.setComponentPopupMenu(popup);
	// //
	// btnPlatforms.setPrototypeDisplayValue(Messages.get("filterPlatforms"));
	// // btnPlatforms.addPopupMenuListener(new PopupMenuListener() {
	// //
	// // private Runnable runnableIf;
	// // private Runnable runnableElse;
	// //
	// // @Override
	// // public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
	// // if (!popup.isVisible()) {
	// // if (runnableIf == null) {
	// // runnableIf = new Runnable() {
	// //
	// // @Override
	// // public void run() {
	// // btnPlatforms.hidePopup();
	// // popup.show(btnPlatforms, 0, btnPlatforms.getHeight());
	// // popup.pack();
	// // }
	// // };
	// // }
	// // SwingUtilities.invokeLater(runnableIf);
	// // } else {
	// // if (runnableElse == null) {
	// // runnableElse = new Runnable() {
	// //
	// // @Override
	// // public void run() {
	// // btnPlatforms.hidePopup();
	// // }
	// // };
	// // }
	// // SwingUtilities.invokeLater(runnableElse);
	// // }
	// // }
	// //
	// // @Override
	// // public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
	// // btnPlatforms.hidePopup();
	// // }
	// //
	// // @Override
	// // public void popupMenuCanceled(PopupMenuEvent e) {
	// // btnPlatforms.hidePopup();
	// // }
	// // });
	//
	// btnSelectAll.addActionListener(this);
	// btnSelectNone.addActionListener(this);
	// btnSelectInvert.addActionListener(this);
	// }

	private void doIt(AbstractButton chk, Icon platformIcon) {
		if (chk.isSelected()) {
			chk.setIcon(platformIcon);
			chk.setForeground(UIManager.getColor("PopupMenu.foreground"));
		} else {
			Image grayedImage = GrayFilter.createDisabledImage(((ImageIcon) platformIcon).getImage());
			chk.setIcon(new ImageIcon(grayedImage));
			chk.setForeground(Color.GRAY);
		}
	}

	// private void addToggleButton(FormLayout layout2, CellConstraints cc2,
	// String string, String string2) {
	// JToggleButton btn = new JToggleButton("");
	// btn.setHorizontalAlignment(SwingConstants.CENTER);
	// btn.setHorizontalTextPosition(SwingConstants.CENTER);
	// btn.setVerticalTextPosition(SwingConstants.BOTTOM);
	// btn.setBorderPainted(false);
	// btn.setContentAreaFilled(false);
	// btn.setFocusable(false);
	// btn.setIcon(ImageUtil.getImageIconFrom(string));
	// btn.setToolTipText(string2);
	// // btn.setBackground(UIManager.getColor("List.background"));
	// btn.setOpaque(true);
	// platformButtons.add(btn);
	// layout2.appendRow(RowSpec.decode("fill:default"));
	// layout2.appendRow(RowSpec.decode("min"));
	// pnlPlatforms.add(btn, cc2.xy(1, layout2.getRowCount()));
	//
	// btn.addActionListener(new ActionListener() {
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// boolean selected = btn.isSelected();
	// btn.setBorderPainted(selected);
	// btn.setContentAreaFilled(selected);
	// if (selected) {
	// for (AbstractButton btnCheck : platformButtons) {
	// if (btnCheck != btn) {
	// btnCheck.setSelected(false);
	// btnCheck.setBorderPainted(false);
	// btnCheck.setContentAreaFilled(false);
	// }
	// }
	// } else {
	// // doIt();
	// splPlatformFilter.setDividerLocation(
	// splPlatformFilter.getMaximumDividerLocation() -
	// btnPlatformFilter.getHeight());
	// }
	// }
	// });
	//
	// btn.addMouseListener(new MouseAdapter() {
	// @Override
	// public void mouseEntered(MouseEvent e) {
	// btn.setBorderPainted(true);
	// btn.setContentAreaFilled(true);
	// }
	//
	// @Override
	// public void mouseExited(MouseEvent e) {
	// if (!btn.isSelected()) {
	// btn.setBorderPainted(false);
	// btn.setContentAreaFilled(false);
	// }
	// }
	// });
	//
	// btn.addFocusListener(new FocusAdapter() {
	// @Override
	// public void focusGained(FocusEvent e) {
	// btn.setBorderPainted(true);
	// btn.setContentAreaFilled(true);
	// }
	//
	// @Override
	// public void focusLost(FocusEvent e) {
	// if (!btn.isSelected()) {
	// btn.setBorderPainted(false);
	// btn.setContentAreaFilled(false);
	// }
	// }
	// });
	// }

	private void setIcons() {
		int size = ScreenSizeUtil.adjustValueToResolution(32);
		ImageIcon iconAllGames = ImageUtil.getImageIconFrom(Icons.get("allGames", size, size));
		ImageIcon iconRecentlyPlayed = ImageUtil.getImageIconFrom(Icons.get("recentlyPlayed", size, size));
		ImageIcon iconFavoriters = ImageUtil.getImageIconFrom(Icons.get("favorites", size, size));
		// ImageIcon iconPlatformFilter =
		// ImageUtil.getImageIconFrom(Icons.get("setFilter", size, size));
		btnAllGames.setIcon(iconAllGames);
		btnRecentlyPlayed.setIcon(iconRecentlyPlayed);
		btnFavorites.setIcon(iconFavoriters);
		// btnPlatformFilter.setIcon(iconPlatformFilter);
	}

	private void addToButtonGroup(ButtonGroup grp, AbstractButton... buttons) {
		for (AbstractButton btn : buttons) {
			grp.add(btn);
		}
	}

	private void addListeners() {
		btnAllGames.addMouseListener(UIUtil.getMouseAdapterKeepHoverWhenSelected());
		btnRecentlyPlayed.addMouseListener(UIUtil.getMouseAdapterKeepHoverWhenSelected());
		btnFavorites.addMouseListener(UIUtil.getMouseAdapterKeepHoverWhenSelected());
		// addActionListeners(btnPlatformFilter);
		// addComponentListener(new ComponentAdapter() {
		// @Override
		// public void componentResized(ComponentEvent e) {
		// super.componentResized(e);
		// handleContentAlignmentIfNeeded();
		// }
		// });
	}

	public void addPlatformFilterListener(ActionListener l) {
		// btnPlatformFilter.addActionListener(l);
	}

	private void handleContentAlignmentIfNeeded() {
		if (getWidth() < getTotalContentWidth()) {
			if (!minimizing) {
				oldWidth = getWidth();
			}
			minimizing = true;
			oldWidth = getWidth();
			// minimizeContentWidth();
		} else if (getWidth() > oldWidth) {
			if (getWidth() >= getTotalContentWidth()) {
				// maximizeContentWidth();
				minimizing = false;
			}
		}
	}

	public void minimizeContentWidth() {
		for (AbstractButton btn : buttons) {
			if (!btn.getText().isEmpty()) {
				btn.setText("");
			}
			btn.setHorizontalAlignment(SwingConstants.CENTER);
			btn.setHorizontalTextPosition(SwingConstants.CENTER);
			btn.setVerticalTextPosition(SwingConstants.BOTTOM);
		}
		// fixPlatformDividerMinimumLocation();
	}

	public boolean isMinimized() {
		return btnAllGames.getText().isEmpty();
	}

	public void centerContentWidth() {
		for (AbstractButton btn : buttons) {
			setButtonText(btn);
			// if (btn == btnPlatformFilter) {
			// btnPlatformFilter.setText(Messages.get("setPlatformFilter"));
			// }
			btn.setHorizontalTextPosition(SwingConstants.CENTER);
			btn.setHorizontalAlignment(SwingConstants.CENTER);
			btn.setVerticalTextPosition(SwingConstants.BOTTOM);
		}
		// fixPlatformDividerMinimumLocation();
	}

	public boolean isCentered() {
		return !btnAllGames.getText().isEmpty() && btnAllGames.getHorizontalTextPosition() == SwingConstants.CENTER;
	}

	private void setButtonText(AbstractButton btn) {
		if (btn == btnAllGames) {
			String baseText = Messages.get(MessageConstants.ALL_GAMES);
			String text = (btnAllGames.isSelected()) ? "<html><strong>"+baseText+"</strong></html>" : baseText;
			btnAllGames.setText(text);
		}
		if (btn == btnRecentlyPlayed) {
			String baseText = Messages.get(MessageConstants.RECENTLY_PLAYED);
			String text = (btnRecentlyPlayed.isSelected()) ? "<html><strong>"+baseText+"</strong></html>" : baseText;
			btnRecentlyPlayed.setText(text);
		}
		if (btn == btnFavorites) {
			String baseText = Messages.get(MessageConstants.FAVORITES);
			String text = (btnFavorites.isSelected()) ? "<html><strong>"+baseText+"</strong></html>" : baseText;
			btnFavorites.setText(text);
		}
	}

	public void maximizeContentWidth() {
		for (AbstractButton btn : buttons) {
			setButtonText(btn);
			btn.setHorizontalTextPosition(SwingConstants.RIGHT);
			btn.setHorizontalAlignment(SwingConstants.LEFT);
			btn.setVerticalTextPosition(SwingConstants.CENTER);
		}
		// fixPlatformDividerMinimumLocation();
	}

	public boolean isMaximized() {
		return !btnAllGames.getText().isEmpty() && btnAllGames.getHorizontalTextPosition() == SwingConstants.RIGHT;
	}

	// private void fixPlatformDividerMinimumLocation() {
	// SwingUtilities.invokeLater(new Runnable() {
	//
	// @Override
	// public void run() {
	// if (btnPlatformFilter.isVisible()) {
	// if (splPlatformFilter.getDividerLocation() !=
	// splPlatformFilter.getMaximumDividerLocation()-btnPlatformFilter.getHeight())
	// {
	// splPlatformFilter.setDividerLocation(splPlatformFilter.getMaximumDividerLocation()-btnPlatformFilter.getHeight());
	// }
	// } else {
	// if (splPlatformFilter.getDividerLocation() >=
	// splPlatformFilter.getMaximumDividerLocation()-btnPlatformFilter.getHeight())
	// {
	// splPlatformFilter.setDividerLocation(splPlatformFilter.getMaximumDividerLocation()-btnPlatformFilter.getHeight());
	// }
	// }
	// }
	// });
	// }

	private int getTotalContentWidth() {
		int sbWidth = (!spNavigationButtons.getVerticalScrollBar().isVisible()) ? 0
				: spNavigationButtons.getVerticalScrollBar().getWidth();
		int width = btnRecentlyPlayed.getWidth() + sbWidth;
		return width;
	}

	public int getTotalContentHeight() {
		int height = 0;
		for (AbstractButton btn : buttons) {
			height += btn.getHeight();
		}
		return height;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		// if (source == btnPlatformFilter) {
		// // popup.show(btnPlatformFilter, btnPlatformFilter.getWidth(),
		// (-popup.getHeight()) + btnPlatformFilter.getHeight());
		// // popup.show(btnPlatformFilter, btnPlatformFilter.getWidth(), 0);
		//
		// Insets screenInsets =
		// Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
		// int taskBarHeight = screenInsets.bottom;
		// System.out.println(taskBarHeight);
		// Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// int yy = btnPlatformFilter.getLocationOnScreen().y;
		// int hh = dlgPopup.getHeight();
		// int result = yy + hh;
		// int doThis = 0;
		// if (result > screenSize.height-taskBarHeight) {
		// int difference = (result - (screenSize.height-taskBarHeight));
		// doThis -= difference;
		// }
		// Point locationOnScreen = btnPlatformFilter.getLocationOnScreen();
		// dlgPopup.setLocation(locationOnScreen.x +
		// btnPlatformFilter.getWidth(),
		// locationOnScreen.y);
		// dlgPopup.setVisible(true);
		// } else
		if (source == btnSelectAll) {
			for (Component c : pnlPopup.getComponents()) {
				if (c instanceof AbstractButton) {
					AbstractButton btn = ((AbstractButton) c);
					btn.setSelected(true);
					doIt(btn, btn.getIcon());
				}
			}
		} else if (source == btnSelectNone) {
			for (Component c : pnlPopup.getComponents()) {
				if (c instanceof AbstractButton) {
					AbstractButton btn = ((AbstractButton) c);
					btn.setSelected(false);
					doIt(btn, btn.getIcon());
				}
			}
		} else if (source == btnSelectInvert) {
			for (Component c : pnlPopup.getComponents()) {
				if (c instanceof AbstractButton) {
					AbstractButton btn = ((AbstractButton) c);
					btn.setSelected(!btn.isSelected());
					doIt(btn, btn.getIcon());
				}
			}
		}
	}

	// boolean doIt() {
	// boolean b = btnPlatformFilter.isSelected();
	// boolean filterSet = false;
	// for (AbstractButton btn : platformButtons) {
	// if (btn.isSelected()) {
	// filterSet = true;
	// }
	// btn.setVisible(b);
	// }
	// pnlPlatforms.setVisible(b);
	// splPlatformFilter.setResizeWeight(b ? 0 : 1);
	// btnPlatformFilter.setVisible(!b);
	// String prefix = "<html><strong>";
	// String postfix = "</strong></html>";
	// if (filterSet) {
	// btnPlatformFilter.setText(prefix+btnPlatformFilter.getText()+postfix);
	// } else {
	// btnPlatformFilter.setText(btnPlatformFilter.getText().replace(prefix,
	// "").replace(postfix, ""));
	// }
	// return b;
	// }

	@Override
	public void navigationChanged(NavigationEvent e) {
		changeNav(e.getView());
	}

	private void changeNav(int view) {
		String prefix = "<html><strong>";
		String postfix = "</strong></html>";
		switch (view) {
		case NavigationPanel.ALL_GAMES:
			if (!btnAllGames.getText().isEmpty()) {
				btnAllGames.setText(prefix + Messages.get(MessageConstants.ALL_GAMES) + postfix);
			}
			if (!btnRecentlyPlayed.getText().isEmpty()) {
				btnRecentlyPlayed.setText(Messages.get(MessageConstants.RECENTLY_PLAYED));
			}
			if (!btnFavorites.getText().isEmpty()) {
				btnFavorites.setText(Messages.get(MessageConstants.FAVORITES));
			}
			btnAllGames.setSelected(true);
			UIUtil.doHover(true, btnAllGames);
			UIUtil.doHover(false, btnRecentlyPlayed, btnFavorites);
			spNavigationButtons.getVerticalScrollBar().setValue(spNavigationButtons.getVerticalScrollBar()
					.getMinimum());
			break;
		case NavigationPanel.RECENTLY_PLAYED:
			if (!btnAllGames.getText().isEmpty()) {
				btnAllGames.setText(Messages.get(MessageConstants.ALL_GAMES));
			}
			if (!btnRecentlyPlayed.getText().isEmpty()) {
				btnRecentlyPlayed.setText(prefix + Messages.get(MessageConstants.RECENTLY_PLAYED) + postfix);
			}
			if (!btnFavorites.getText().isEmpty()) {
				btnFavorites.setText(Messages.get(MessageConstants.FAVORITES));
			}
			btnRecentlyPlayed.setSelected(true);
			UIUtil.doHover(false, btnAllGames, btnFavorites);
			UIUtil.doHover(true, btnRecentlyPlayed);
			Rectangle bounds = spNavigationButtons.getViewport().getViewRect();
			Dimension size = spNavigationButtons.getViewport().getViewSize();
			int x = (size.width - bounds.width) / 2;
			int y = (size.height - bounds.height) / 2;
			spNavigationButtons.getViewport().setViewPosition(new Point(x, y));
			break;
		case NavigationPanel.FAVORITES:
			if (!btnAllGames.getText().isEmpty()) {
				btnAllGames.setText(Messages.get(MessageConstants.ALL_GAMES));
			}
			if (!btnRecentlyPlayed.getText().isEmpty()) {
				btnRecentlyPlayed.setText(Messages.get(MessageConstants.RECENTLY_PLAYED));
			}
			if (!btnFavorites.getText().isEmpty()) {
				btnFavorites.setText(prefix + Messages.get(MessageConstants.FAVORITES) + postfix);
			}
			btnFavorites.setSelected(true);
			UIUtil.doHover(false, btnAllGames, btnRecentlyPlayed);
			UIUtil.doHover(true, btnFavorites);
			spNavigationButtons.getVerticalScrollBar().setValue(spNavigationButtons.getVerticalScrollBar()
					.getMaximum());
			break;
		}
		currentNavView = view;
		handleContentAlignmentIfNeeded();
	}

	public void addChangeToAllGamesListener(ActionListener l) {
		btnAllGames.addActionListener(l);
	}

	public void addChangeToRecentlyPlayedListener(ActionListener l) {
		btnRecentlyPlayed.addActionListener(l);
	}

	public void addChangeToFavoritesListener(ActionListener l) {
		btnFavorites.addActionListener(l);
	}

	public boolean isScrollbarVisible() {
		return spNavigationButtons.getVerticalScrollBar().isVisible();
	}

	public void languageChanged() {
		changeNav(currentNavView);
		setToolTipTexts();
	}

	public int getMinimumButtonWidth() {
		return btnAllGames.getIcon().getIconWidth();
	}

	public int getButtonWidth() {
		int width = btnAllGames.getWidth();
		return width;
	}

	public AbstractButton[] getButtons() {
		return buttons;
	}

	public JScrollPane getSpNavigationButtons() {
		return spNavigationButtons;
	}

	public String getNavigationPaneState() {
		String state = NavigationPanel.MINIMIZED;
		if (isMinimized()) {
			state = NavigationPanel.MINIMIZED;
		}
		if (isCentered()) {
			state = NavigationPanel.CENTERED;
		}
		if (isMaximized()) {
			state = NavigationPanel.MAXIMIZED;
		}
		return state;
	}

	public void setNavigationPaneState(String state) {
		if (state.equals(MINIMIZED)) {
			minimizeContentWidth();
		}
		if (state.equals(CENTERED)) {
			centerContentWidth();
		}
		if (state.equals(MAXIMIZED)) {
			maximizeContentWidth();
		}
	}

	public int getSelectedNavigationItem() {
		if (btnAllGames.isSelected()) {
			return ALL_GAMES;
		}
		if (btnRecentlyPlayed.isSelected()) {
			return RECENTLY_PLAYED;
		}
		if (btnFavorites.isSelected()) {
			return FAVORITES;
		}
		return ALL_GAMES;
	}

	public String getLongestLabel() {
		String buttonText = Messages.get(MessageConstants.ALL_GAMES);
		String buttonText1 = Messages.get(MessageConstants.RECENTLY_PLAYED);
		String buttonText2 = Messages.get(MessageConstants.FAVORITES);
		return UIUtil.getLongestLabel(buttonText, buttonText1, buttonText2);
	}

	public int getButtonInsets() {
		return btnAllGames.getInsets().left + btnAllGames.getInsets().right;
	}
}