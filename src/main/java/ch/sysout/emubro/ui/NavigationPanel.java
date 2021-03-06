package ch.sysout.emubro.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import ch.sysout.emubro.api.GameViewListener;
import ch.sysout.emubro.impl.event.NavigationEvent;
import ch.sysout.emubro.util.MessageConstants;
import ch.sysout.ui.util.JCustomToggleButton;
import ch.sysout.util.Icons;
import ch.sysout.util.ImageUtil;
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

	private JToggleButton btnAllGames = new JCustomToggleButton(Messages.get(MessageConstants.ALL_GAMES));
	private JToggleButton btnRecentlyPlayed = new JCustomToggleButton(Messages.get(MessageConstants.RECENTLY_PLAYED));
	private JToggleButton btnFavorites = new JCustomToggleButton(Messages.get(MessageConstants.FAVORITES));
	private List<JToggleButton> platformButtons = new ArrayList<>();
	private AbstractButton[] buttons = new AbstractButton[] { btnAllGames, btnRecentlyPlayed, btnFavorites };

	private int oldWidth;
	private boolean minimizing;
	private JScrollPane spNavigationButtons;
	private JPanel pnlPlatforms;
	private FormLayout layoutPopup;
	private CellConstraints ccPopup;
	private JDialog dlgPopup = new JDialog();
	private JPanel pnlPopup;
	private int currentNavView = ALL_GAMES;

	public NavigationPanel() {
		super(new BorderLayout());
		initComponents();
		createUI();
	}

	private void initComponents() {
		int size = ScreenSizeUtil.is3k() ? 10 : 5;
		Insets insets = new Insets(size, size, size, size);

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
	}

	private void setToolTipTexts() {
		btnAllGames.setToolTipText(Messages.get(MessageConstants.ALL_GAMES));
		btnRecentlyPlayed.setToolTipText(Messages.get(MessageConstants.RECENTLY_PLAYED));
		btnFavorites.setToolTipText(Messages.get(MessageConstants.FAVORITES));
	}

	private void createUI() {
		//		setBorder(BorderFactory.createEtchedBorder());
		FormLayout layout = new FormLayout("default:grow", "fill:default, min, fill:default, min, fill:default, min");
		JPanel pnl = new JPanel(layout) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g.create();
				int panelWidth = getWidth();
				int panelHeight = getHeight();
				Theme currentTheme = IconStore.current().getCurrentTheme();
				ThemeBackground currentBackground = currentTheme.getNavigationPane();
				if (currentBackground.hasGradientPaint()) {
					GradientPaint p = currentBackground.getGradientPaint();
					g2d.setPaint(p);
				} else if (currentBackground.hasColor()) {
					g2d.setColor(currentBackground.getColor());
				}
				g2d.fillRect(0, 0, panelWidth, panelHeight);

				BufferedImage background = currentBackground.getImage();
				if (background != null) {
					g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
					g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					int imgWidth = background.getWidth();
					int imgHeight = background.getHeight();
					int x = 0;
					int y = 0;
					boolean shouldScale = currentBackground.isImageScaleEnabled();
					if (shouldScale) {
						int new_width = imgWidth;
						int new_height = imgHeight;
						boolean scaleProportionally = currentBackground.isScaleProportionallyEnabled();
						if (scaleProportionally) {
							// first check if we need to scale width
							if (imgWidth > panelWidth) {
								//scale width to fit
								new_width = panelWidth;
								//scale height to maintain aspect ratio
								new_height = (new_width * imgHeight) / imgWidth;
							}

							// then check if we need to scale even with the new height
							if (new_height > panelHeight) {
								//scale height to fit instead
								new_height = panelHeight;
								//scale width to maintain aspect ratio
								new_width = (new_height * imgWidth) / imgHeight;
							}
							if (new_width < panelWidth) {
								x += (panelWidth-new_width) / 2;
							}
							if (new_height < panelHeight) {
								y += (panelHeight-new_height) / 2; // image centered
								//					y = panelHeight-new_height; // image bottom
							}
						} else {
							new_width = panelWidth;
							new_height = panelHeight;
						}
						g2d.drawImage(background, x, y, new_width, new_height, this);
						//						boolean addTransparencyPane = true;
						//						if (addTransparencyPane) {
						//							g2d.setColor(getTransparencyColor());
						//							g2d.fillRect(x, y, new_width, new_height);
						//						}
					} else {
						boolean shouldVerticalCenterImage = currentBackground.isVerticalCenterImageEnabled();
						boolean shouldHorizontalCenterImage = currentBackground.isHorizontalCenterImageEnabled();
						if (shouldVerticalCenterImage) {
							if (imgWidth > panelWidth) {
								x -= (imgWidth-panelWidth) / 2;
							}
						}
						if (shouldHorizontalCenterImage) {
							if (imgHeight > panelHeight) {
								y -= (imgHeight-panelHeight) / 2;
							}
						}
						g2d.drawImage(background, x, y, imgWidth, imgHeight, this);
						//						boolean addTransparencyPane = true;
						//						if (addTransparencyPane) {
						//							g2d.setColor(getTransparencyColor());
						//							g2d.fillRect(x, y, imgWidth, imgHeight);
						//						}
					}
					boolean addTransparencyPane = currentBackground.isAddTransparencyPaneEnabled();
					if (addTransparencyPane) {
						g2d.setColor(currentBackground.getTransparencyColor());
						g2d.fillRect(0, 0, panelWidth, panelHeight);
					}
					BufferedImage imgTransparentOverlay = currentTheme.getTransparentBackgroundOverlayImage();
					if (imgTransparentOverlay != null) {
						int width = imgTransparentOverlay.getWidth();
						int height = imgTransparentOverlay.getHeight();

						double factor = background.getWidth() / panelWidth;
						if (factor != 0) {
							int scaledWidth = (int) (width/factor);
							int scaledHeight = (int) (height/factor);
							width = scaledWidth;
							height = scaledHeight;
						}
						x = panelWidth-width;
						y = panelHeight-height;
						g2d.drawImage(imgTransparentOverlay, x, y, width, height, this);
					}
				}
				g2d.dispose();
			}
		};
		spNavigationButtons = new JCustomScrollPane(pnl);

		CellConstraints cc = new CellConstraints();
		pnl.add(btnAllGames, cc.xy(1, 1));
		pnl.add(btnFavorites, cc.xy(1, 3));
		pnl.add(btnRecentlyPlayed, cc.xy(1, 5));

		pnlPlatforms = new JPanel();
		FormLayout layout2 = new FormLayout("default:grow", "");
		pnlPlatforms.setLayout(layout2);
		new CellConstraints();
		//		spNavigationButtons.setViewportView(pnl);
		spNavigationButtons.validate();
		spNavigationButtons.repaint();
		spNavigationButtons.setBorder(BorderFactory.createEmptyBorder());
		spNavigationButtons.getVerticalScrollBar().setUnitIncrement(16);
		spNavigationButtons.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		spNavigationButtons.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(spNavigationButtons, BorderLayout.CENTER);
	}

	private void setIcons() {
		int size = ScreenSizeUtil.adjustValueToResolution(32);
		ImageIcon iconAllGames = ImageUtil.getImageIconFrom(Icons.get("allGames", size, size));
		ImageIcon iconRecentlyPlayed = ImageUtil.getImageIconFrom(Icons.get("recentlyPlayed", size, size));
		ImageIcon iconFavoriters = ImageUtil.getImageIconFrom(Icons.get("favorites", size, size));
		btnAllGames.setIcon(iconAllGames);
		btnRecentlyPlayed.setIcon(iconRecentlyPlayed);
		btnFavorites.setIcon(iconFavoriters);
	}

	private void addToButtonGroup(ButtonGroup grp, AbstractButton... buttons) {
		for (AbstractButton btn : buttons) {
			grp.add(btn);
		}
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

	public void addChangeToTagsListener(ActionListener l) {
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
		String buttonText3 = Messages.get(MessageConstants.TAGS);
		return UIUtil.getLongestLabel(buttonText, buttonText1, buttonText2, buttonText3);
	}

	public int getButtonBorderSize() {
		return btnAllGames.getInsets().left + btnAllGames.getInsets().right;
	}

	public Insets getButtonInsets() {
		return btnAllGames.getInsets();
	}
}