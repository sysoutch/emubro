package ch.sysout.emubro.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.codehaus.plexus.util.StringUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import com.jgoodies.forms.factories.Paddings;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.view.ValidationComponentUtils;

import au.com.bytecode.opencsv.CSVWriter;
import ch.sysout.emubro.Main;
import ch.sysout.emubro.api.EmulatorListener;
import ch.sysout.emubro.api.FilterListener;
import ch.sysout.emubro.api.PlatformListener;
import ch.sysout.emubro.api.RunGameWithListener;
import ch.sysout.emubro.api.TagListener;
import ch.sysout.emubro.api.dao.ExplorerDAO;
import ch.sysout.emubro.api.event.EmulatorEvent;
import ch.sysout.emubro.api.event.FilterEvent;
import ch.sysout.emubro.api.event.GameSelectionEvent;
import ch.sysout.emubro.api.event.PlatformEvent;
import ch.sysout.emubro.api.event.TagEvent;
import ch.sysout.emubro.api.model.Emulator;
import ch.sysout.emubro.api.model.Explorer;
import ch.sysout.emubro.api.model.Game;
import ch.sysout.emubro.api.model.Platform;
import ch.sysout.emubro.api.model.PlatformComparator;
import ch.sysout.emubro.api.model.Tag;
import ch.sysout.emubro.impl.BroEmulatorDeletedException;
import ch.sysout.emubro.impl.BroGameAlreadyExistsException;
import ch.sysout.emubro.impl.BroGameDeletedException;
import ch.sysout.emubro.impl.event.BroEmulatorAddedEvent;
import ch.sysout.emubro.impl.event.BroEmulatorRemovedEvent;
import ch.sysout.emubro.impl.event.BroGameAddedEvent;
import ch.sysout.emubro.impl.event.BroGameRemovedEvent;
import ch.sysout.emubro.impl.event.BroGameRenamedEvent;
import ch.sysout.emubro.impl.event.BroPlatformAddedEvent;
import ch.sysout.emubro.impl.event.BroTagAddedEvent;
import ch.sysout.emubro.impl.event.NavigationEvent;
import ch.sysout.emubro.impl.model.BroEmulator;
import ch.sysout.emubro.impl.model.BroGame;
import ch.sysout.emubro.impl.model.BroPlatform;
import ch.sysout.emubro.impl.model.BroTag;
import ch.sysout.emubro.impl.model.EmulatorConstants;
import ch.sysout.emubro.impl.model.FileStructure;
import ch.sysout.emubro.impl.model.GameConstants;
import ch.sysout.emubro.impl.model.PlatformConstants;
import ch.sysout.emubro.ui.AboutDialog;
import ch.sysout.emubro.ui.CoverConstants;
import ch.sysout.emubro.ui.EmulationOverlayFrame;
import ch.sysout.emubro.ui.FileTypeConstants;
import ch.sysout.emubro.ui.GamePropertiesDialog;
import ch.sysout.emubro.ui.GameViewConstants;
import ch.sysout.emubro.ui.HelpDialog;
import ch.sysout.emubro.ui.JExtendedComboBox;
import ch.sysout.emubro.ui.JExtendedTextField;
import ch.sysout.emubro.ui.JLinkButton;
import ch.sysout.emubro.ui.LanguageListener;
import ch.sysout.emubro.ui.MainFrame;
import ch.sysout.emubro.ui.NavigationPanel;
import ch.sysout.emubro.ui.NotificationElement;
import ch.sysout.emubro.ui.RateEvent;
import ch.sysout.emubro.ui.RateListener;
import ch.sysout.emubro.ui.RatingBarPanel;
import ch.sysout.emubro.ui.SortedListModel;
import ch.sysout.emubro.ui.SplashScreenWindow;
import ch.sysout.emubro.ui.UpdateDialog;
import ch.sysout.emubro.ui.ViewPanel;
import ch.sysout.emubro.ui.ViewPanelManager;
import ch.sysout.emubro.ui.properties.DefaultEmulatorListener;
import ch.sysout.emubro.ui.properties.PropertiesFrame;
import ch.sysout.emubro.util.MessageConstants;
import ch.sysout.ui.ImageUtil;
import ch.sysout.util.FileUtil;
import ch.sysout.util.Icons;
import ch.sysout.util.LnkParser;
import ch.sysout.util.Messages;
import ch.sysout.util.ScreenSizeUtil;
import ch.sysout.util.UIUtil;
import ch.sysout.util.ValidationUtil;

public class BroController implements ActionListener, PlatformListener, EmulatorListener, TagListener,
GameSelectionListener, BrowseComputerListener {
	Explorer explorer;
	MainFrame view;
	private PropertiesFrame frameProperties;
	private HelpDialog dlgHelp;
	private AboutDialog dlgAbout;
	private UpdateDialog dlgUpdates;

	ExplorerDAO explorerDAO;
	private List<String> alreadyCheckedDirectories = new ArrayList<>();
	private Properties properties;

	private Map<Game, Map<Process, Integer>> processes = new HashMap<>();

	private String applicationVersion = "";
	private String platformDetectionVersion = "";
	private String downloadLink = "";
	private final String currentApplicationVersion = "0.0.9";
	private final String currentPlatformDetectionVersion = "20180605.0";

	private int navigationPaneDividerLocation;
	private String navigationPaneState;
	private int previewPanelWidth;
	private int gameDetailsPanelHeight;
	private int splGameFilterDividerLocation;
	private int detailsPaneNotificationTab;
	private String language;

	private List<TimerTask> taskListRunningGames = new ArrayList<>();
	private List<Timer> timerListRunningGames = new ArrayList<>();
	private EmulationOverlayFrame frameEmulationOverlay;

	private static final String[] propertyKeys = {
			"x",
			"y",
			"width",
			"height",
			"maximized",
			"show_menubar",						 	// 5
			"show_navigationpane",
			"show_previewpane",
			"show_detailspane",
			"BLANK",
			"view",									// 10
			"platform",
			"show_wizard",
			"navigationpane_dividerlocation",
			"previewpane_width",
			"gamedetailspane_height",				// 15
			"view_panel",
			"gamefilterpane_dividerlocation",
			"detailspane_notificationtab",
			"language",
			"detailspane_unpinned",					// 20
			"columnWidth",
			"rowHeight",
			"fontSize",
			"gamefilter_visible",
			"sortOrder",							// 25
			"groupOrder",
			"sortBy",
			"groupBy",
			"lastFrameDetailsPaneX",
			"lastFrameDetailsPaneY",					// 30
			"lastPnlDetailsPreferredWidth",
			"lastPnlDetailsPreferredHeight",
			"navigationPaneState"
	};

	private SortedListModel<Platform> mdlPropertiesLstPlatforms = new SortedListModel<>();
	private Map<String, ImageIcon> platformIcons = new HashMap<>();
	private Map<String, ImageIcon> emulatorIcons = new HashMap<>();
	private Map<String, Icon> emulatorFileIcons = new HashMap<>();
	private List<String> encryptedFiles = new ArrayList<>();
	BrowseComputerWorker workerBrowseComputer;
	List<PlatformListener> platformListeners = new ArrayList<>();
	List<EmulatorListener> emulatorListeners = new ArrayList<>();
	List<TagListener> tagListeners = new ArrayList<>();
	private List<LanguageListener> languageListeners = new ArrayList<>();
	private List<String> zipFiles = new ArrayList<>();
	private List<String> rarFiles = new ArrayList<>();
	private List<String> isoFiles = new ArrayList<>();
	private RenameGameListener renameGameListener;
	private Comparator<Game> platformComparator;
	private boolean detailsPaneVisible;
	private boolean previewPaneVisible;
	private boolean navigationPaneVisible;
	private boolean menuBarVisible;
	private boolean detailsPaneUnpinned;
	private int lastDetailsPaneX;
	private int  lastDetailsPaneY;
	private int lastDetailsPreferredWidth;
	private int lastDetailsPreferredHeight;
	private SplashScreenWindow dlgSplashScreen;
	private int preferredWidthAtFirstStart;
	private MessageDigest digest;
	private Platform lastSelectedPlatformFromAddGameChooser;
	private GamePropertiesDialog dlgGameProperties;
	public int dragRectWidth = -1;
	public int dragRectHeight = -1;
	private Map<Platform, NodeList> gameTagListFiles = new HashMap<>();

	public BroController(ExplorerDAO explorerDAO, Explorer model, MainFrame view) {
		this.explorerDAO = explorerDAO;
		explorer = model;
		this.view = view;
		explorer.setSearchProcessComplete(explorerDAO.isSearchProcessComplete());
		platformComparator = new PlatformComparator(explorer);
		// pnlMain.initializeViewPanel();
		// pnlMain.retrieveNewestAppVersion();
	}

	public void rateGame(Game game) {
		try {
			explorerDAO.setRate(game.getId(), game.getRate());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		view.gameRated(game);
	}

	private void commentGames(List<Game> list) {
		JOptionPane.showInputDialog("");
	}

	public void createView() throws Exception {
		view.adjustSplitPaneDividerSizes();
		Map<String, Action> actionKeysGreeting = new HashMap<>();
		actionKeysGreeting.put("notifications_thanks", null);
		NotificationElement notficationElement = new NotificationElement(
				new String[] { "greeting", "applicationTitle" }, actionKeysGreeting,
				NotificationElement.INFORMATION, null);
		view.showInformation(notficationElement);

		if (!explorer.isSearchProcessComplete()) {
			Map<String, Action> actionKeys = new HashMap<>();
			Action action = new Action() {

				@Override
				public void actionPerformed(ActionEvent e) {
					searchForPlatforms();
					view.switchDetailsTabTo(1);
				}

				@Override
				public void setEnabled(boolean b) {
				}

				@Override
				public void removePropertyChangeListener(PropertyChangeListener listener) {
					// TODO Auto-generated method stub

				}

				@Override
				public void putValue(String key, Object value) {
					// TODO Auto-generated method stub

				}

				@Override
				public boolean isEnabled() {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public Object getValue(String key) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public void addPropertyChangeListener(PropertyChangeListener listener) {
					// TODO Auto-generated method stub

				}
			};
			actionKeys.put("browseComputer", action);
			actionKeys.put("hideMessage", null);

			NotificationElement element = new NotificationElement(new String[] { "browseComputerForGamesAndEmulators" },
					actionKeys, NotificationElement.INFORMATION_MANDATORY, null);
			view.showInformation(element);
		}
		detailsPaneUnpinned = Boolean.parseBoolean(properties.getProperty(propertyKeys[20]));
		String lastDetailsPaneXString = properties.getProperty(propertyKeys[29]);
		String lastDetailsPaneYString = properties.getProperty(propertyKeys[30]);
		String lastDetailsPreferredWidthString = properties.getProperty(propertyKeys[31]);
		String lastDetailsPreferredHeightString = properties.getProperty(propertyKeys[32]);

		lastDetailsPaneX = (lastDetailsPaneXString != null && !lastDetailsPaneXString.isEmpty() ?
				Integer.parseInt(lastDetailsPaneXString) : -1);
		lastDetailsPaneY = (lastDetailsPaneYString != null && !lastDetailsPaneYString.isEmpty() ?
				Integer.parseInt(lastDetailsPaneYString) : -1);
		lastDetailsPreferredWidth = (lastDetailsPreferredWidthString != null && !lastDetailsPreferredWidthString.isEmpty() ?
				Integer.parseInt(lastDetailsPreferredWidthString) : -1);
		lastDetailsPreferredHeight = (lastDetailsPreferredHeightString != null && !lastDetailsPreferredHeightString.isEmpty() ?
				Integer.parseInt(lastDetailsPreferredHeightString) : -1);
		String columnWidth = properties.getProperty(propertyKeys[21]);
		if (columnWidth != null) {
			view.setColumnWidth(Integer.valueOf(columnWidth));
		}
		String rowHeight = properties.getProperty(propertyKeys[22]);
		if (rowHeight != null) {
			view.setRowHeight(Integer.valueOf(rowHeight));
		}
		String fontSize = properties.getProperty(propertyKeys[23]);
		if (fontSize != null) {
			view.setFontSize(Integer.valueOf(fontSize));
		}
	}

	private void setLastViewState() {
		String propertyView = properties.getProperty(propertyKeys[16]);
		int viewPanel = (propertyView != null && !propertyView.isEmpty()) ? Integer.parseInt(propertyView)
				: ViewPanel.LIST_VIEW;
		int viewType = Integer.valueOf(properties.getProperty(propertyKeys[10]));
		view.changeToViewPanel(viewPanel, explorer.getGames());
		view.navigationChanged(new NavigationEvent(viewType));
	}

	public void addListeners() {
		ViewPanelManager viewManager = view.getViewManager();
		addPlatformListener(this);
		addEmulatorListener(this);
		addTagListener(this);
		view.addListeners();
		view.addAutoSearchListener(new AutoSearchListener());
		view.addQuickSearchListener(new QuickSearchListener());
		view.addCustomSearchListener(new CustomSearchListener());
		view.addLastSearchListener(new LastSearchListener());
		view.addGameDragDropListener(new GameDragDropListener());
		view.addCoverDragDropListener(new CoverDragDropListener());
		view.addCoverToLibraryDragDropListener(new CoverToLibraryDragDropListener());
		view.addShowUncategorizedFilesDialogListener(new ShowUncategorizedFilesDialogListener());
		view.addOpenPropertiesListener(new OpenPropertiesListener());
		view.addExportGameListToTxtListener(new ExportGameListToTxtListener());
		view.addExportGameListToCsvListener(new ExportGameListToCsvListener());
		view.addExportGameListToXmlListener(new ExportGameListToXmlListener());
		view.addChangeToWelcomeViewListener(new ChangeToWelcomeViewListener());
		view.addCoverSizeListener(new ChangeCoverSizeListener());
		view.addChangeToListViewListener(new ChangeToListViewListener());
		view.addChangeToElementViewListener(new ChangeToElementViewListener());
		view.addChangeToTableViewListener(new ChangeToTableViewListener());
		view.addChangeToContentViewListener(new ChangeToContentViewListener());
		view.addChangeToCoverViewListener(new ChangeToCoverViewListener());
		view.addLanguageGermanListener(new LanguageGermanListener());
		view.addLanguageEnglishListener(new LanguageEnglishListener());
		view.addLanguageFrenchListener(new LanguageFrenchListener());
		view.addChangeToAllGamesListener(new ChangeToAllGamesListener());
		view.addChangeToRecentlyPlayedListener(new ChangeToRecentlyPlayedListener());
		view.addChangeToFavoritesListener(new ChangeToFavoritesListener());
		view.addFullScreenListener(new FullScreenListener());
		view.addFullScreenListener2(new FullScreenListener());
		view.addSortGameAscendingListListener(new SortGameListAscendingListener());
		view.addSortGameDescendingListListener(new SortGameListDescendingListener());
		view.addSortByTitleListener(new SortByTitleListener());
		view.addSortByPlatformListener(new SortByPlatformListener());
		view.addGroupByNoneListener(new GroupByNoneListener());
		view.addGroupByPlatformListener(new GroupByPlatformListener());
		view.addGroupByTitleListener(new GroupByTitleListener());
		view.addFilterListener(new BroFilterListener());
		viewManager.addSelectGameListener(this);
		viewManager.addSelectGameListener(view);
		RunGameListener runGameListener = new RunGameListener();
		view.addRunGameListener(runGameListener);
		view.addRunGameListener1(runGameListener);
		view.addRunGameListener2(runGameListener);
		view.addRunGameWithListener(new RunGameWithListener() {

			@Override
			public void runGameWith(int emulatorId) {
				for (Game g : explorer.getCurrentGames()) {
					g.setEmulator(emulatorId);
					try {
						explorerDAO.setDefaultEmulatorId(g, emulatorId);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				runGame();
			}
		});
		view.addConfigureEmulatorListener(new ConfigureEmulatorListener());
		view.addCoverFromComputerListener(new CoverFromComputerListener());
		view.addTagFromWebListener(new TagFromWebListener());
		view.addAutoSearchTagsAllListener(new AutoSearchTagsAllListener());
		view.addAutoSearchTagsListener(new AutoSearchTagsListener());
		view.addCoverFromWebListener(new CoverFromWebListener());
		view.addTrailerFromWebListener(new TrailerFromWebListener());
		view.addSearchNetworkListener(new SearchNetworkListener());
		view.addRenameGameListener(renameGameListener = new RenameGameListener());
		view.addTagsFromGamesListener();
		view.addAddGameListener(new AddGameListener());
		view.addRemoveGameListener(new RemoveGameListener());
		view.addAddPlatformListener(new AddPlatformListener());
		view.addRemovePlatformListener(new RemovePlatformListener());
		view.addAddEmulatorListener(new AddEmulatorListener());
		view.addRemoveEmulatorListener(new RemoveEmulatorListener());
		view.addLoadDiscListener(new LoadDiscListener());
		view.addShowNavigationPaneListener(new ShowNavigationPaneListener());
		view.addShowPreviewPaneListener(new ShowPreviewPaneListener());
		view.addShowGameDetailsListener(new ShowGameDetailsListener());
		view.addOpenGamePropertiesListener(new OpenGamePropertiesListener());
		view.addOpenGamePropertiesListener1(new OpenGamePropertiesListener());
		view.addAddFilesListener(new AddFilesListener());
		view.addAddFoldersListener(new AddFoldersListener());
		view.addAddGameOrEmulatorFromClipboardListener(new AddGameOrEmulatorFromClipboardListener());
		viewManager.addIncreaseFontListener(new IncreaseFontListener());
		viewManager.addIncreaseFontListener2(new IncreaseFontListener());
		viewManager.addDecreaseFontListener(new DecreaseFontListener());

		ActionListener openGameFolderActionListener = new OpenGameFolderListener();
		view.addOpenGameFolderListener(openGameFolderActionListener);

		MouseListener openGameFolderMouseListener = new OpenGameFolderListener();
		view.addOpenGameFolderListener1(openGameFolderMouseListener);
		viewManager.addOpenGameFolderListener1(openGameFolderMouseListener);

		view.addShowOrganizeContextMenuListener(new ShowOrganizeContextMenuListener());
		view.addShowContextMenuListener(new ShowContextMenuListener());
		//		view.addSetFilterListener(new AddFilterListener());
		view.addHideExtensionsListener(new HideExtensionsListener());
		view.addTouchScreenOptimizedScrollListener(new TouchScreenOptimizedScrollListener());
		view.addOpenHelpListener(new OpenHelpListener());
		view.addOpenAboutListener(new OpenAboutListener());
		view.addOpenUpdateListener(new OpenCheckForUpdatesListener());
		view.addInterruptSearchProcessListener(new InterruptSearchProcessListener());
		view.addExitListener(new ExitListener());
		view.addColumnWidthSliderListener(new ColumnWidthSliderListener());
		view.addRowHeightSliderListener(new RowHeightSliderListener());
		view.addBroComponentListener(new BroComponentListener());
		view.addRateListener(new BroRateListener());
		view.addTagListener(new BroTagListener());
		view.addCommentListener(new BroCommentListener());
		view.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				checkAndExit();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				super.windowClosed(e);
			}
		});
	}

	public void showOrHideResizeArea() {
		view.showOrHideResizeArea();
	}

	public boolean loadAppDataFromLastSession() {
		properties = new Properties();
		String homePath = System.getProperty("user.home");
		String path = homePath += homePath.endsWith(File.separator) ? ""
				: File.separator + "." + Messages.get(MessageConstants.APPLICATION_TITLE).toLowerCase();
		new File(path).mkdir();
		File file = new File(path + File.separator + "window" + ".properties");
		if (file.exists()) {
			Reader reader = null;
			boolean b = false;
			try {
				reader = new BufferedReader(new FileReader(file));
				properties.load(reader);
				b = true;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (Exception e) { }
			}
			return b;
		}
		return false;
	}

	public boolean isApplicationUpdateAvailable() {
		int versionCompare = versionCompare(currentApplicationVersion, applicationVersion);
		return versionCompare == -1;
	}

	public boolean isPlatformDetectionUpdateAvailable() {
		int versionCompare = versionCompare(currentPlatformDetectionVersion, platformDetectionVersion);
		return versionCompare == -1;
	}

	public UpdateObject retrieveLatestRevisionInformations() throws MalformedURLException, IOException {
		String urlPath = Messages.get(MessageConstants.UPDATE_SERVER);
		urlPath += (!urlPath.endsWith("/") ? "/" : "") + Messages.get(MessageConstants.UPDATE_INFO_FILE);
		URL url = null;
		url = new URL(urlPath);
		BufferedReader in;
		HttpURLConnection con = (HttpURLConnection)
				url.openConnection();
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);
		InputStream is = con.getInputStream();
		Reader reader = new InputStreamReader(is);
		in = new BufferedReader(reader);
		String inputLine;
		boolean applicationUpdateAvailable = false;
		boolean signatureUpdateAvailable = false;
		while ((inputLine = in.readLine()) != null) {
			if (inputLine.startsWith("app_version")) {
				applicationVersion = inputLine.split("=")[1].trim();
				if (applicationVersion != null && !applicationVersion.isEmpty()) {
					applicationUpdateAvailable = isApplicationUpdateAvailable();
				}
			}
			if (inputLine.startsWith("platform_detection_version")) {
				platformDetectionVersion = inputLine.split("=")[1].trim();
				if (platformDetectionVersion != null && !platformDetectionVersion.isEmpty()) {
					if (isPlatformDetectionUpdateAvailable()) {
						signatureUpdateAvailable = true;
					}
				}
			}
			if (inputLine.startsWith("download_link")) {
				downloadLink = inputLine.split("=")[1].trim();
				if (downloadLink != null && !downloadLink.isEmpty()) {

				}
			}
		}
		in.close();
		UpdateObject uo = new UpdateObject(applicationUpdateAvailable, signatureUpdateAvailable,
				applicationVersion, platformDetectionVersion, downloadLink);
		return uo;
	}

	private String retrieveChangelog() throws MalformedURLException, IOException {
		String urlPath = Messages.get(MessageConstants.UPDATE_SERVER);
		urlPath += (!urlPath.endsWith("/") ? "/" : "") + Messages.get(MessageConstants.CHANGELOG_FILE);
		URL url = null;
		url = new URL(urlPath);
		BufferedReader in;
		HttpURLConnection con = (HttpURLConnection)
				url.openConnection();
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);
		InputStream is = con.getInputStream();
		Reader reader = new InputStreamReader(is);
		in = new BufferedReader(reader);
		StringBuffer sb = new StringBuffer();
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			sb.append(inputLine + "\r\n");
		}
		in.close();
		return sb.toString();
	}

	public void installUpdate() {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				String urlPath = Messages.get(MessageConstants.WEBSITE);
				urlPath += (!urlPath.endsWith("/") ? "/" : "") + Messages.get(MessageConstants.UPDATE_INFO_FILE);
				try {
					URL url = new URL(urlPath);
					URLConnection con;
					try {
						con = url.openConnection();
						con.setReadTimeout(20000);
						String userHome = System.getProperty("user.home");
						File applicationFile = new File(userHome + "/" + Messages.get(MessageConstants.APPLICATION_TITLE) + ".jar");
						try {
							FileUtils.copyURLToFile(url, applicationFile);
							System.err.println("update has been successfully installed");
							// view.showInformation("Update ready to install",
							// "restart "+Messages.get("applicationTitle"),
							// NotificationElement.INFORMATION, null);
							//
							// view.showInformation("Update has been
							// successfully installed",
							// Messages.get("hideMessage"),
							// NotificationElement.INFORMATION, null);
						} catch (IOException e) {
							// view.showInformation("Cannot access the update
							// file",
							// "retry update", NotificationElement.ERROR, new
							// UpdateApplicationListener());
						}
					} catch (IOException e1) {
						// view.showInformation("Cannot not establish connection
						// to the update server",
						// "check for updates", NotificationElement.WARNING,
						// null);
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		});
		t.setDaemon(true);
		t.start();
	}

	/**
	 * Compares two version strings.
	 *
	 * Use this instead of String.compareTo() for a non-lexicographical
	 * comparison that works for version strings. e.g. "1.10".compareTo("1.6").
	 *
	 * @note It does not work if "1.10" is supposed to be equal to "1.10.0".
	 *
	 * @param str1
	 *            a string of ordinal numbers separated by decimal points.
	 * @param str2
	 *            a string of ordinal numbers separated by decimal points.
	 * @return The result is a negative integer if str1 is _numerically_ less
	 *         than str2. The result is a positive integer if str1 is
	 *         _numerically_ greater than str2. The result is zero if the
	 *         strings are _numerically_ equal.
	 */
	public Integer versionCompare(String str1, String str2) {
		if (str1 != null && str2 != null && !str1.trim().isEmpty() && !str2.trim().isEmpty()) {
			String[] vals1 = str1.split("\\.");
			String[] vals2 = str2.split("\\.");
			int i = 0;
			// set index to first non-equal ordinal or length of shortest
			// version
			// string
			while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
				i++;
			}
			// compare first non-equal ordinal number
			if (i < vals1.length && i < vals2.length) {
				int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
				return Integer.signum(diff);
			}
			// the strings are equal or one string is a substring of the other
			// e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
			else {
				return Integer.signum(vals1.length - vals2.length);
			}
		} else {
			return 0;
		}
	}

	public void searchForPlatforms() {
		File[] arr = File.listRoots();
		List<File> lst = new ArrayList<>(Arrays.asList(arr));
		searchForPlatforms(lst);
	}

	public void searchForPlatformsString(List<String> filesString) {
		List<File> files = new ArrayList<>();
		for (String f : filesString) {
			files.add(new File(f));
		}
		searchForPlatforms(files);
	}

	public void searchForPlatforms(List<File> files) {
		if (workerBrowseComputer != null && !workerBrowseComputer.isDone()) {
			JOptionPane.showMessageDialog(view, Messages.get(MessageConstants.ALREADY_BROWSING_COMPUTER), "Suche", JOptionPane.ERROR_MESSAGE);
			return;
		}
		boolean searchForPlatforms = true;
		//		try {
		//			searchForPlatforms = initializePlatforms();
		//		} catch (FileNotFoundException e) {
		//			// view.showInformation("[EMUBRO-01] Initializing error: default
		//			// platform file cannot be found", "idk", NotificationElement.ERROR,
		//			// null);
		//		}

		if (searchForPlatforms) {
			view.searchProcessInitialized();
			workerBrowseComputer = new BrowseComputerWorker(view, explorer, explorerDAO, files);
			workerBrowseComputer.addBrowseComputerListener(this);
			workerBrowseComputer.addPropertyChangeListener(new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					System.out.println("propertychange on browscomputer" + evt.getPropertyName() + " - " + evt.getNewValue());
				}
			});
			workerBrowseComputer.execute();
		}
	}

	@Override
	public void searchForPlatform(File filePath) {
		List<Platform> platforms = explorer.getPlatforms();
		//		boolean useDefaultPlatforms = defaultPlatforms != null
		//				&& defaultPlatforms.size() > 0;
		try {
			searchForGameOrEmulator(filePath);
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BroGameDeletedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// searchForEmulators(filePath, useDefaultPlatforms);
	}

	@Override
	public void searchProcessComplete() {
		Map<String, Action> actionKeys = new HashMap<>();
		actionKeys.put("hideMessage", null);
		NotificationElement element = new NotificationElement(new String[] { "searchProcessCompleted" },
				actionKeys, NotificationElement.INFORMATION, null);
		view.showInformation(element);
		try {
			explorerDAO.searchProcessComplete();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// private void searchForEmulators(String filePath, boolean
	// useDefaultPlatforms) {
	// // List<Platform> platforms = (List<Platform>)
	// (useDefaultPlatforms ? explorer.getDefaultPlatforms() :
	// explorer.getPlatforms());
	// List<BroPlatform> platforms = explorer.getDefaultPlatforms();
	//
	// for (Platform p : platforms) {
	//
	// }
	// }

	private void searchForGameOrEmulator(File file)
			throws ZipException, RarException, IOException, BroGameDeletedException {
		if (file.length() == 0) {
			return;
		}
		String filePath = file.getAbsolutePath();
		Platform p0;
		try {
			p0 = isGameOrEmulator(filePath);
			if (p0 != null) {
				if (explorer.hasFile(filePath)) {
					return;
				}
				addGame(p0, file);
				return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BroEmulatorDeletedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//	private boolean initializePlatforms() throws FileNotFoundException {
	//		List<BroPlatform> bla = explorer.getDefaultPlatforms();
	//		for (BroPlatform p : bla) {
	//			p.setId(PlatformConstants.NO_PLATFORM);
	//			p.setDefaultEmulatorId(EmulatorConstants.NO_EMULATOR);
	//		}
	//		return (bla != null && bla.size() > 0);
	//	}

	public void addPlatformListener(PlatformListener l) {
		platformListeners.add(l);
	}

	public void addEmulatorListener(EmulatorListener l) {
		emulatorListeners.add(l);
	}

	public void addTagListener(TagListener l) {
		tagListeners.add(l);
	}

	public void setDefaultTags(List<BroTag> tmpTags) {
		List<BroTag> tags = new ArrayList<>();
		for (BroTag t : tmpTags) {
			tags.add((BroTag) addOrGetTag(t));
		}
		view.initTags(tags);
	}

	public void setDefaultPlatforms(List<BroPlatform> platforms) {
		for (Platform p : platforms) {
			p.setDefaultEmulatorId(EmulatorConstants.NO_EMULATOR);
			Platform p2 = addOrGetPlatform(p);
			for (Emulator emulator : p.getEmulators()) {
				if (emulator == null) {
					// should not happen normally. maybe false configuration in platforms.json file (e.g. }, at last line)
					System.err.println("platform" + p.getName() + " has configured a null emulator in platforms.json file");
					continue;
				}
				String emulatorName = emulator.getName();
				if (!explorer.hasEmulatorByName(p.getName(), emulatorName)) {
					try {
						int platformId = p2.getId();
						explorerDAO.addEmulator(platformId, emulator);
						emulator.setId(explorerDAO.getLastAddedEmulatorId());
						p2.addEmulator((BroEmulator) emulator);
					} catch (BroEmulatorDeletedException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * @return File.separator masked by two more backslashes when running on
	 *         windows
	 */
	String getSeparatorBackslashed() {
		// this has been done to fix exception on windows
		// java.util.regex.PatternSyntaxException: Unexpected internal error
		// near index 1
		return (File.separator.equals("\\")) ? "\\\\" : File.separator;
	}

	/**
	 * TODO check valid zip
	 *
	 * @param filePath
	 * @param list
	 * @return
	 * @throws IOException
	 */
	private String zipFileContainsGame(String filePath, List<String> list) throws ZipException, IOException {
		ZipFile zip = null;
		try {
			zip = new ZipFile(filePath);
		} catch (ZipException e) {
			throw e;
		}
		if (zip != null) {
			Enumeration<? extends ZipEntry> files = zip.entries();
			while (files.hasMoreElements()) {
				try {
					ZipEntry entry = files.nextElement();
					String entryName = entry.getName().toLowerCase();
					for (String s : list) {
						if (entryName.matches(s)) {
							return entry.getName();
						}
					}
				} catch (IllegalArgumentException e) {
					System.err.println(e.getMessage() + " " + filePath);
				}
			}
			zip.close();
			// System.gc();
		}
		return null;
	}

	private String rarFileContainsGame(String filePath, List<String> list) throws RarException, IOException {
		File file = new File(filePath);
		try {
			Archive myRAR = new Archive(file); // TODO catch ioexception
			if (!myRAR.isEncrypted()) {
				encryptedFiles.add(filePath);
			}
			List<FileHeader> files = myRAR.getFileHeaders();

			// InputStream ins;
			for (FileHeader hd : files) {
				for (String s : list) {
					if (hd.getFileNameW().toLowerCase().matches(s)) {
						System.err.println(hd.getFileNameW());
						// ins = myRAR.getInputStream(hd);
						myRAR.close();
						return hd.getFileNameW();
					}
				}
			}
			myRAR.close();
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	public void showView(boolean applyData) throws FileNotFoundException, SQLException {
		/*
		 * this invokeLater has been done, because of an unexplainable (thread
		 * problems?) NullPointerException in ListViewPanel when calling
		 * super.locationToIndex(location); (location is not null, super?!)
		 *
		 * also start up is smoother this way
		 */
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				if (properties != null) {
					showView2();
				}
				view.addComponentListener(new ComponentAdapter() {
					@Override
					public void componentShown(ComponentEvent e) {
						super.componentShown(e);
						showOrHideResizeArea();
					}
				});

				view.addWindowStateListener(new WindowAdapter() {
					@Override
					public void windowStateChanged(WindowEvent e) {
						super.windowStateChanged(e);
						showOrHideResizeArea();
					}
				});
				view.setVisible(true);
				// invoke later has been done here, because otherwise different things
				// doesnt update like
				// vertical scrollbar and navigationpane
				// SwingUtilities.invokeLater(new Runnable() {
				//
				// @Override
				// public void run() {

				addListeners();
				if (applyData) {
					showOrHideMenuBarAndPanels();
					setLastViewState();
					view.toFront();
				} else {
					int minWidth = ScreenSizeUtil.adjustValueToResolution(256);
					view.showPreviewPane(true, minWidth);
					view.showGameDetailsPane(true);
					view.showNavigationPane(true);
					view.navigationChanged(new NavigationEvent(NavigationPanel.ALL_GAMES));
					//			view.changeToViewPanel(GameViewConstants.LIST_VIEW, explorer.getGames());
				}
				showConfigurationWizardIfNeeded();
			}
		});
	}

	private void showView2() {
		boolean gameFilterPanelVisible = getGameFilterPanelVisibleFromProperties();
		view.showFilterPanel(gameFilterPanelVisible);

		int sortOrder = getSortOrderFromProperties();
		sortGameList(sortOrder);

		int sortBy = getSortByFromProperties();
		switch (sortBy) {
		case ViewConstants.SORT_BY_PLATFORM:
			sortBy(sortBy, (PlatformComparator) platformComparator);
			break;
		case ViewConstants.SORT_BY_TITLE:
			sortBy(sortBy, null);
			break;
		}

		int groupOrder = getGroupOrderFromProperties();
		int groupBy = getGroupByFromProperties();
		groupBy(groupBy);
	}

	private boolean getGameFilterPanelVisibleFromProperties() {
		return Boolean.parseBoolean(properties.getProperty(propertyKeys[24]));
	}

	private int getSortOrderFromProperties() {
		String sortOrderProperty = properties.getProperty(propertyKeys[25]);
		try {
			return Integer.parseInt(sortOrderProperty);
		} catch (NumberFormatException e) {
			return ViewConstants.SORT_ASCENDING;
		}
	}

	private int getGroupByFromProperties() {
		try {
			return Integer.parseInt(properties.getProperty(propertyKeys[28]));
		} catch (NumberFormatException e) {
			return ViewConstants.GROUP_BY_NONE;
		}
	}

	private int getSortByFromProperties() {
		try {
			return Integer.parseInt(properties.getProperty(propertyKeys[27]));
		} catch (NumberFormatException e) {
			return ViewConstants.SORT_BY_TITLE;
		}
	}

	private int getGroupOrderFromProperties() {
		try {
			return Integer.parseInt(properties.getProperty(propertyKeys[26]));
		} catch (NumberFormatException e) {
			return ViewConstants.GROUP_ASCENDING;
		}
	}

	private void showConfigurationWizardIfNeeded() {
		try {
			if (!explorerDAO.isConfigWizardHiddenAtStartup()) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						view.showConfigWizardDialog();
					}
				});
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	private void showOrHideMenuBarAndPanels() {
		menuBarVisible = Boolean.parseBoolean(properties.getProperty(propertyKeys[5]));
		navigationPaneVisible = Boolean.parseBoolean(properties.getProperty(propertyKeys[6]));
		previewPaneVisible = Boolean.parseBoolean(properties.getProperty(propertyKeys[7]));
		detailsPaneVisible = Boolean.parseBoolean(properties.getProperty(propertyKeys[8]));
		view.showMenuBar(menuBarVisible);
		view.showNavigationPane(navigationPaneVisible, navigationPaneDividerLocation, navigationPaneState);
		view.showDetailsPane(detailsPaneVisible, gameDetailsPanelHeight,
				detailsPaneUnpinned, lastDetailsPaneX, lastDetailsPaneY, lastDetailsPreferredWidth, lastDetailsPreferredHeight);
		view.showPreviewPane(previewPaneVisible, previewPanelWidth);
		// dont remove invokelater here. otherwise locations may not set
		// correctly when opening frame in maximized state
		//			SwingUtilities.invokeLater(new Runnable() {
		//
		//				@Override
		//				public void run() {
		//					if (view.getExtendedState() == Frame.MAXIMIZED_BOTH) {
		//						view.showPreviewPane(previewPaneVisible, previewPanelWidth);
		//						view.showGameDetailsPane(detailsPaneVisible, gameDetailsPanelHeight);
		//					}
		//				}
		//			});
	}

	public void initGameList() throws SQLException {
		List<Game> games = explorerDAO.getGames();
		explorer.setGames(games);
		Map<Integer, String> checksums = explorerDAO.getChecksums();
		explorer.setChecksums(checksums);
		for (Game g : games) {
			List<String> files = explorerDAO.getFilesForGame(g.getId());
			List<Tag> tags = explorerDAO.getTagsForGame(g.getId());
			explorer.setFilesForGame(g.getId(), files);
			explorer.setTagsForGame(g.getId(), tags);
			for (Tag t : tags) {
				g.addTag(t);
			}
		}
		List<Platform> platforms = explorerDAO.getPlatforms();
		explorer.setPlatforms(platforms);
		if (games != null && !games.isEmpty()) {
			view.updateGameCount(games.size());
			view.initGames(games);
		}
		view.initPlatforms(platforms);
		boolean emulatorsFound = false;
		for (Platform p : platforms) {
			for (Emulator emu : p.getEmulators()) {
				if (emu.isInstalled()) {
					emulatorsFound = true;
					break;
				}
			}
		}
		boolean gamesOrPlatformsFound = games.size() > 0 || emulatorsFound;
		view.activateQuickSearchButton(gamesOrPlatformsFound);
		Main.hideSplashScreen();
	}

	private void saveWindowInformations() {
		try {
			String homePath = System.getProperty("user.home");
			String path = homePath + (homePath.endsWith(File.separator) ? ""
					: File.separator + "." + Messages.get(MessageConstants.APPLICATION_TITLE).toLowerCase());
			new File(path).mkdir();

			String fullPath = path += File.separator + "window" + ".properties";
			File file = new File(fullPath);
			file.createNewFile();

			boolean maximized = view.getExtendedState() == Frame.MAXIMIZED_BOTH;
			FileWriter fw = new FileWriter(file, false);
			fw.append("# window properties output by " + Messages.get(MessageConstants.APPLICATION_TITLE) + "\r\n" + "# " + new Date()
					+ "\r\n\r\n");
			fw.append(propertyKeys[0] + "=" + view.getLocation().x + "\r\n"); // x
			fw.append(propertyKeys[1] + "=" + view.getLocation().y + "\r\n"); // y
			fw.append(propertyKeys[2] + "=" + view.getWidth() + "\r\n"); // width
			fw.append(propertyKeys[3] + "=" + view.getHeight() + "\r\n"); // height
			fw.append(propertyKeys[4] + "=" + maximized + "\r\n"); // maximized
			fw.append(propertyKeys[5] + "=" + view.isMenuBarVisible() + "\r\n"); // show_menubar
			fw.append(propertyKeys[6] + "=" + true + "\r\n"); // show_navigationpane
			fw.append(propertyKeys[7] + "=" + view.isPreviewPaneVisible() + "\r\n"); // show_previewpane
			fw.append(propertyKeys[8] + "=" + view.isDetailsPaneVisible() + "\r\n"); // show_detailspane
			fw.append(propertyKeys[9] + "=" + true + "\r\n"); // BLANK
			fw.append(propertyKeys[10] + "=" + view.getSelectedNavigationItem() + "\r\n"); // view
			fw.append(propertyKeys[11] + "=" + "Playstation 2" + "\r\n"); // platform
			fw.append(propertyKeys[12] + "=" + explorer.isConfigWizardHiddenAtStartup() + "\r\n"); // show_wizard
			fw.append(propertyKeys[13] + "=" + view.getSplNavigationPane().getDividerLocation() + "\r\n"); // navigationpane_dividerlocation
			fw.append(propertyKeys[14] + "=" + (view.getSplPreviewPaneWidth()) + "\r\n"); // previewpane_width
			fw.append(propertyKeys[15] + "=" + (view.getSplDetailsPaneHeight()) + "\r\n"); // gamedetailspane_height
			fw.append(propertyKeys[16] + "=" + view.getCurrentViewPanelType() + "\r\n"); // view panel
			fw.append(propertyKeys[17] + "=" + 0 + "\r\n"); // gamefilterpane_dividerlocation
			fw.append(propertyKeys[18] + "=" + view.getDetailsPaneNotificationTab() + "\r\n"); // detailspane_notificationtab
			fw.append(propertyKeys[19] + "=" + Messages.getDefault().getLanguage() + "\r\n"); // language
			fw.append(propertyKeys[20] + "=" + view.isDetailsPaneUnpinned() + "\r\n"); // game details pane unpinned
			fw.append(propertyKeys[21] + "=" + view.getColumnWidth() + "\r\n"); // column width
			fw.append(propertyKeys[22] + "=" + view.getRowHeight() + "\r\n"); // row height
			fw.append(propertyKeys[23] + "=" + view.getFontSize() + "\r\n"); // font size
			fw.append(propertyKeys[24] + "=" + view.isGameFilterPanelVisible() + "\r\n"); // gamefilter visible
			fw.append(propertyKeys[25] + "=" + view.getSortOrder() + "\r\n"); // sort order
			fw.append(propertyKeys[26] + "=" + view.getGroupOrder() + "\r\n"); // group order
			fw.append(propertyKeys[27] + "=" + view.getSortBy() + "\r\n"); // sort by
			fw.append(propertyKeys[28] + "=" + view.getGroupBy() + "\r\n"); // group by
			Point detailsLocation = view.getLastFrameDetailsPaneLocation();
			int lastDetailsX = -1;
			int lastDetailsY= -1;
			if (detailsLocation != null) {
				lastDetailsX = detailsLocation.x;
				lastDetailsY = detailsLocation.y;
			}
			fw.append(propertyKeys[29] + "=" + lastDetailsX + "\r\n"); // last frame details pane x
			fw.append(propertyKeys[30] + "=" + lastDetailsY + "\r\n"); // last frame details pane y
			Dimension detailsSize = view.getLastPnlDetailsPreferredSize();
			int lastDetailsWidth = -1;
			int lastDetailsHeight = -1;
			if (detailsSize != null) {
				lastDetailsWidth = (int) detailsSize.getWidth();
				lastDetailsHeight = (int) detailsSize.getHeight();
			}
			fw.append(propertyKeys[31] + "=" + lastDetailsWidth + "\r\n"); // last details preferred wiidth
			fw.append(propertyKeys[32] + "=" + lastDetailsHeight + "\r\n"); // last details preferred height
			fw.append(propertyKeys[33] + "=" + view.getNavigationPaneState() + "\r\n"); // group by
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void applyAppDataFromLastSession() throws Exception {
		if (properties != null && properties.size() > 0) {
			try {
				int x = Integer.parseInt(properties.getProperty(propertyKeys[0]));
				int y = Integer.parseInt(properties.getProperty(propertyKeys[1]));
				int width = Integer.parseInt(properties.getProperty(propertyKeys[2]));
				int height = Integer.parseInt(properties.getProperty(propertyKeys[3]));
				boolean maximized = Boolean.parseBoolean(properties.getProperty(propertyKeys[4]));
				navigationPaneDividerLocation = Integer.parseInt(properties.getProperty(propertyKeys[13]));
				navigationPaneState = properties.getProperty(propertyKeys[33]);
				previewPanelWidth = Integer.parseInt(properties.getProperty(propertyKeys[14]));
				gameDetailsPanelHeight = Integer.parseInt(properties.getProperty(propertyKeys[15]));
				splGameFilterDividerLocation = Integer.parseInt(properties.getProperty(propertyKeys[17]));
				detailsPaneNotificationTab = Integer.parseInt(properties.getProperty(propertyKeys[18]));
				language = properties.getProperty(propertyKeys[19]);
				changeLanguage(new Locale(language));

				Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(view.getGraphicsConfiguration());
				int taskBarHeight = screenInsets.bottom;
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				if (width > screenSize.width) {
					width = screenSize.width;
				}
				if (height > screenSize.height - taskBarHeight) {
					height = screenSize.height - taskBarHeight;
				}
				if (x + width > screenSize.width) {
					x = screenSize.width - width;
				}
				if (y + height > screenSize.height - taskBarHeight) {
					y = screenSize.height - taskBarHeight - height;
				}
				if (x < 0) {
					x = 0;
				}
				if (y < 0) {
					y = 0;
				}
				preferredWidthAtFirstStart = view.getWidth();
				if (maximized) {
					/**
					 * setSize has been done here to set initial window size to "nice".
					 * TODO maybe change this sometime to set size to last user defined size like it was before going to fullscreen
					 *
					 * - hint -
					 * button bar button should all be visible and maximized at this point for "correct" sizing
					 */
					view.setSize(new Dimension(preferredWidthAtFirstStart, (int) (preferredWidthAtFirstStart / 1.25)));
					view.setLocationRelativeTo(null);
					// view.setSize(ScreenSizeUtil.screenSize()); // maximize
					// frame showup fix
					view.setExtendedState(view.getExtendedState() | Frame.MAXIMIZED_BOTH);
				} else {
					view.setLocation(x, y);
					view.setSize(width, height);
				}
			} catch (Exception e) {
				throw e;
			}
		} else {
			throw new IllegalArgumentException("unexpected tokens");
		}
	}

	public void changeLanguage(Locale locale) {
		Messages.setDefault(locale);
		Messages.clearCache();
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				view.languageChanged();
				if (dlgAbout != null) {
					dlgAbout.languageChanged();
				}
				if (dlgHelp != null) {
					dlgHelp.languageChanged();
				}
				if (dlgUpdates != null) {
					dlgUpdates.languageChanged();
				}
				if (dlgGameProperties != null) {
					dlgGameProperties.languageChanged();
				}
				if (renameGameListener != null) {
					renameGameListener.languageChanged();
				}
			}
		});
	}

	private File exportGameListTo(int fileType) throws IOException, SQLException {
		boolean filterSet = view.isFilterFavoriteActive() || view.isFilterRecentlyPlayedActive() || view.isGameFilterSet() || view.isPlatformFilterSet() || view.isTagFilterSet();
		int request = JOptionPane.NO_OPTION;
		if (filterSet) {
			String[] options = { "Nur aktuelle Ansicht exportieren", "Gesamte Spielebibliothek exportieren" };
			request = JOptionPane.showOptionDialog(null,
					"Es ist noch ein Filter gesetzt.\n\n"
							+ "Möchten Sie nur die aktuelle Ansicht exportieren oder die gesamte \nSpielebibliothek?",
							"Spieleliste exportieren", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
							options, options[0]);
			if (request == JOptionPane.CLOSED_OPTION || request == JOptionPane.CANCEL_OPTION) {
				return null;
			}
		}
		if (fileType == FileTypeConstants.TXT_FILE) {
			List<Game> games = (request == JOptionPane.YES_OPTION) ? view.getGamesFromCurrentView() : explorer.getGames();
			return exportGameListToTxtFile(games);
		} else if (fileType == FileTypeConstants.CSV_FILE) {
			List<Game> games = (request == JOptionPane.YES_OPTION) ? view.getGamesFromCurrentView() : explorer.getGames();
			return exportGameListToCsvFile(games);
		} else if (fileType == FileTypeConstants.XML_FILE) {
			List<Game> games = (request == JOptionPane.YES_OPTION) ? view.getGamesFromCurrentView() : explorer.getGames();
			return exportGameListToXmlFile(games);
		} else {
			throw new IllegalArgumentException("option must be one of " + "FileTypeConstants.TXT_FILE, "
					+ "FileTypeConstants.CSV_FILE, " + "FileTypeConstants.XML_FILE");
		}
	}

	private File exportGameListToTxtFile(List<Game> games) throws IOException, SQLException {
		File fileTxt;
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fileTxt = new File("gamelist.txt");
			fileTxt.delete();
			fw = new FileWriter(fileTxt, true);
			bw = new BufferedWriter(fw);
			for (Game game : games) {
				bw.append(game.getName() + "\r\n");
			}
			return fileTxt;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				bw.close();
			} catch (Exception ignore) {
			}
			try {
				fw.close();
			} catch (Exception ignore) {
			}
		}
	}

	private File exportGameListToCsvFile(List<Game> games) throws IOException, SQLException {
		List<String[]> allLines = new ArrayList<>();
		for (Game g : games) {
			String[] data = { g.getName(), g.getPlatformId() + "", g.getDefaultEmulatorId() + "", g.getRate() + "",
					explorer.getFiles(g).get(0), g.getCoverPath(), g.getLastPlayed() + "", g.getPlayCount() + "" };
			allLines.add(data);
		}
		File file = new File("gamelist.csv");
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);

		CSVWriter writer = new CSVWriter(bw, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
		writer.writeAll(allLines);
		writer.close();
		return file;
	}

	/**
	 * @throws IOException
	 * @throws SQLException
	 * @throws DOMException
	 */
	private File exportGameListToXmlFile(List<Game> games) throws IOException, DOMException, SQLException {
		File file;
		Document doc;
		Element el;

		file = new File("gamelist.xml");
		file.createNewFile();

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			doc = builder.newDocument();
			doc.setXmlStandalone(true);

			el = doc.createElement("games");
			doc.appendChild(el);

			for (Game g : games) {
				Element game = doc.createElement("game");

				el.appendChild(game);

				Element rate = doc.createElement("rate");
				Element tags = doc.createElement("tags");
				Element path = doc.createElement("path");
				Element coverPath = doc.createElement("coverPath");
				Element lastPlayed = doc.createElement("lastPlayed");
				Element playCount = doc.createElement("playCount");

				//				title.appendChild(doc.createTextNode(g.getName()));
				//				platform.appendChild(doc.createTextNode("" + explorer.getPlatform(g.getPlatformId()).getName()));
				rate.appendChild(doc.createTextNode("" + g.getRate()));

				for (Tag t : g.getTags()) {
					Element tag = doc.createElement("tag");
					tag.appendChild(doc.createTextNode("" + t.getName()));
					tags.appendChild(tag);
				}
				path.appendChild(doc.createTextNode(explorer.getFiles(g).get(0)));
				coverPath.appendChild(doc.createTextNode(g.getCoverPath()));
				lastPlayed.appendChild(doc.createTextNode("" + g.getLastPlayed()));
				playCount.appendChild(doc.createTextNode("" + g.getPlayCount()));

				game.setAttribute("name", g.getName());
				game.setAttribute("platform", explorer.getPlatform(g.getPlatformId()).getName());

				game.appendChild(rate);
				game.appendChild(tags);
				game.appendChild(path);
				game.appendChild(coverPath);
				game.appendChild(lastPlayed);
				game.appendChild(playCount);
			}

			try {
				TransformerFactory transFactory = TransformerFactory.newInstance();
				Transformer transformer = transFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				// transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(file);

				try {
					transformer.transform(source, result);
				} catch (TransformerException e) {
					e.printStackTrace();
				}

			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return file;
	}

	private void runGame() {
		if (explorer.hasCurrentGame()) {
			List<Game> games = explorer.getCurrentGames();
			for (Game game : games) {
				if (processes.containsKey(game)) {
					boolean gameAlreadyRunning = isGameAlreadyRunning(game);
					if (gameAlreadyRunning) {
						int request = JOptionPane.showConfirmDialog(view, Messages.get(MessageConstants.GAME_ALREADY_RUNNING),
								Messages.get(MessageConstants.GAME_ALREADY_RUNNING_TITLE), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						if (request != JOptionPane.YES_OPTION) {
							return;
						}
					}
				}
				Platform platform = explorer.getPlatform(game.getPlatformId());
				try {
					runGame1(game, platform);
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
		}
	}

	private void runGame1(Game game, Platform platform) throws SQLException {
		Emulator emulator = null;
		if (!game.hasEmulator()) {
			List<BroEmulator> emulators = platform.getEmulators();
			if (platform.getEmulators() != null && emulators.size() > 0) {
				emulator = platform.getDefaultEmulator();
				if (emulator == null) {
					boolean noInstalledEmulators = true;
					for (BroEmulator emu : emulators) {
						if (emu.isInstalled()) {
							noInstalledEmulators = false;
							break;
						}
					}
					if (noInstalledEmulators) {
						JOptionPane.showMessageDialog(view,
								"Für dieses Spiel sind keine Emulatoren verfügbar.\n\n"
										+ "<html>Du kannst in den Einstellungen geeignete Emulatoren für dieses Spiel finden.</html>",
										Messages.get(MessageConstants.ERR_STARTING_GAME), JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
						showPropertiesFrame(explorer.getCurrentGames().get(0));
					} else {
						JOptionPane.showMessageDialog(view, "Platform has no default emulator",
								Messages.get(MessageConstants.ERR_STARTING_GAME), JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
					}
					return;
				}
			} else {
				JOptionPane.showMessageDialog(view,
						"Für dieses Spiel sind keine Emulatoren verfügbar.\n\n"
								+ "<html><a href=''>Hier klicken</a> um geeignete Emulatoren für dieses Spiel zu finden.</html>",
								Messages.get(MessageConstants.ERR_STARTING_GAME), JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
				return;
			}
		} else {
			int gameId = game.getId();
			emulator = explorer.getEmulatorFromGame(gameId);
			if (emulator == null) {
				JOptionPane.showMessageDialog(view,
						"There is something wrong with the emulator associated with this game.\n"
								+ "Maybe you set it before as default for this game and deleted it after.\n\n"
								+ "Try to set a new default emulator for the game. We will fix this situation soon.",
								Messages.get(MessageConstants.ERR_STARTING_GAME), JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
				return;
			}
		}
		String emulatorPath = emulator.getPath();
		if (ValidationUtil.isWindows()) {
			emulatorPath = emulatorPath.replace("%windir%", System.getenv("WINDIR"));
		}
		String emulatorStartParameters = emulator.getStartParameters();

		List<String> gamePaths = explorer.getFiles(game);
		String gamePath2 = null;
		if (gamePaths.size() > 1) {
			JComboBox<String> cmbGamePaths = new JComboBox<>();
			for (String s : gamePaths) {
				cmbGamePaths.addItem(s);
			}
			Object[] message = {
					"Multiple files are associated to this game.",
					" ",
					"Choose the file you want to use to start the game from the box below",
					cmbGamePaths,
					" ",
					"Do you want to start the game now using the selected file?"
			};
			cmbGamePaths.addAncestorListener(new RequestFocusListener());
			cmbGamePaths.getEditor().selectAll();

			int resp = JOptionPane.showConfirmDialog(view, message, "", JOptionPane.YES_NO_OPTION);
			if (resp == JOptionPane.OK_OPTION) {
				gamePath2 = cmbGamePaths.getSelectedItem().toString();
			} else {
				return;
			}
		} else if (gamePaths.size() == 1) {
			gamePath2 = gamePaths.get(0);
		} else {
			return;
		}

		if (dlgSplashScreen == null) {
			dlgSplashScreen = new SplashScreenWindow("Game has been started..");
		}
		dlgSplashScreen.setLocationRelativeTo(view);
		dlgSplashScreen.setVisible(true);

		final String emulatorPathFinal = emulatorPath;
		final String gamePathFinal = gamePath2;
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				File gameFile = new File(gamePathFinal);
				File emulatorFile = new File(emulatorPathFinal);
				if (!checkEmulatorFile(emulatorFile)) {
					dlgSplashScreen.dispose();
					return;
				} else if (!checkGameFile(gameFile)) {
					dlgSplashScreen.dispose();
					return;
				}
				//		int confirmRun = JOptionPane.showConfirmDialog(view,
				//				"If you have never started a game of that platform before, maybe the controller input settings are missing.\n\n"
				//						+ "Do yo want to run the game anyway?",
				//						"title", JOptionPane.WARNING_MESSAGE);
				//		if (confirmRun != JOptionPane.YES_OPTION) {
				//			return;
				//		}
				String[] startParameters = (emulatorStartParameters).split(" ");
				String parentFile = emulatorFile.getParent();
				// String emuFilename = emulatorFile.getName();
				//				String gamePathToLower = gamePath.toLowerCase();
				//				if (gamePathToLower.endsWith(".exe")
				//						|| gamePathToLower.endsWith(".bat")
				//						|| gamePathToLower.endsWith(".cmd")
				//						|| gamePathToLower.endsWith(".js")) {
				//					try {
				//						String damnu = gamePath;
				//						Runtime.getRuntime().exec("\""+damnu+"\"", null, gameFile.getParentFile());
				//					} catch (IOException e) {
				//						// TODO Auto-generated catch block
				//						e.printStackTrace();
				//					}
				//				}
				List<String> startParametersList = new ArrayList<>();
				if (emulatorPathFinal.endsWith(".exe")) {
					if (ValidationUtil.isWindows()) {
						startParametersList.add("cmd.exe");
						startParametersList.add("/c");
					} else if (ValidationUtil.isUnix()) {
						startParametersList.add("/usr/bin/wine");
						startParametersList.add("cmd.exe");
						startParametersList.add("/c");
					}
				}
				startParametersList.add("cd");
				startParametersList.add("/d");
				startParametersList.add("\"" + parentFile + "\"");
				startParametersList.add("&&");
				if (emulatorPathFinal.toLowerCase().contains("project64 2.")) {
					startParametersList.add("\"" + emulatorPathFinal + "\"");
					startParametersList.add("\"" + gamePathFinal + "\"");
				} else {
					for (int i = 0; i < startParameters.length; i++) {
						if (startParameters[i].contains("%emupath%") || startParameters[i].contains("%emudir%")
								|| startParameters[i].contains("%emufilename%") || startParameters[i].contains("%gamepath%")
								|| startParameters[i].contains("%gamedir%") || startParameters[i].contains("%gamefilename%")
								|| startParameters[i].contains("%0%")) {
							Path path = Paths.get(gamePathFinal);
							String gameFolder = path.getParent().toString();
							String[] fileNameWithoutExtension = gamePathFinal.split(getSeparatorBackslashed());
							String last = FilenameUtils
									.removeExtension(fileNameWithoutExtension[fileNameWithoutExtension.length - 1]);
							String pathFinal = startParameters[i].replace("%emupath%", "\"" + emulatorPathFinal + "\"")
									.replace("%emudir%", "\"" + Paths.get(emulatorPathFinal).getParent().toString() + "\"")
									.replace("%emufilename%", emulatorFile.getName().toString())
									.replace("%gamepath%", "\"" + gamePathFinal + "\"")
									.replace("%gamedir%", "\"" + gameFolder + "\"")
									.replace("%gamefilename%", "\"" + path.getFileName().toString() + "\"")
									.replace("%0%", last);
							startParametersList.add(pathFinal);
						} else {
							startParametersList.add(startParameters[i]);
						}
					}
				}

				try {
					dlgSplashScreen.showSuccess("Everything ok. Game starts now..");
					frameEmulationOverlay = new EmulationOverlayFrame(game, platform);
					frameEmulationOverlay.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
					frameEmulationOverlay.addShowApplicationListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							view.setState(Frame.NORMAL);
							view.toFront();
						}
					});
					view.setState(Frame.ICONIFIED);
					frameEmulationOverlay.setLocation(ScreenSizeUtil.getWidth() - frameEmulationOverlay.getWidth(), 0);
					frameEmulationOverlay.setVisible(true);
					dlgSplashScreen.dispose();
					runGame2(game, startParametersList);
				} catch (IOException e) {
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							frameEmulationOverlay.dispose();
							view.setState(Frame.NORMAL);
							view.toFront();
							view.repaint();
							JOptionPane op = new GameOptionsPane();
							op.setMessage(Messages.get(MessageConstants.ERR_STARTING_GAME_CONFIG_ERROR) + e.getMessage());
							op.setMessageType(JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
							JDialog dlg = op.createDialog(view, Messages.get(MessageConstants.ERR_STARTING_GAME));
							dlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
							dlg.setVisible(true);
						}
					});
				}
			}
		});
	}

	private boolean isGameAlreadyRunning(Game game) {
		Map<Process, Integer> lb = processes.get(game);
		for (Entry<Process, Integer> entry2 : lb.entrySet()) {
			Process pc = entry2.getKey();
			Integer pId = entry2.getValue();
			if (pc.isAlive()) {
				return true;
			}
		}
		return false;
	}

	private boolean checkEmulatorFile(File emulatorFile) {
		if (!emulatorFile.exists()) {
			String emulatorPath = emulatorFile.getPath();
			if (emulatorPath == null || emulatorPath.trim().isEmpty()) {
				dlgSplashScreen.dispose();
				JOptionPane.showMessageDialog(view, Messages.get(MessageConstants.EMULATOR_NO_PATH), Messages.get(MessageConstants.ERR_STARTING_GAME),
						JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
				return false;
			}
			if (emulatorPath.toLowerCase().matches("^[A-Za-z]\\:\\\\.*$")) {
				boolean rootNotAvailable = true;
				for (File f : File.listRoots()) {
					if (f.getAbsolutePath().startsWith(emulatorPath.substring(0, 3))) {
						rootNotAvailable = false;
						break;
					}
				}
				if (rootNotAvailable) {
					dlgSplashScreen.dispose();
					JOptionPane.showMessageDialog(view, Messages.get(MessageConstants.EMULATOR_NOT_FOUND) + "\n" + Messages.get(MessageConstants.EMULATOR_NOT_FOUND_POST_FIX),
							Messages.get(MessageConstants.ERR_STARTING_GAME), JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
					return false;
				}
			}
			dlgSplashScreen.dispose();
			JOptionPane.showMessageDialog(view, Messages.get(MessageConstants.EMULATOR_NOT_FOUND), Messages.get(MessageConstants.ERR_STARTING_GAME),
					JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
			return false;
		}
		return true;
	}

	private boolean checkGameFile(File gameFile) {
		if (!gameFile.exists()) {
			if (ValidationUtil.isWindows()) {
				for (File f : File.listRoots()) {
					String root = f.getAbsolutePath().toLowerCase();
					String gamePath = gameFile.getAbsolutePath();
					if (gamePath.toLowerCase().startsWith(root)) {
						dlgSplashScreen.dispose();
						JOptionPane.showMessageDialog(view, Messages.get(MessageConstants.GAME_NOT_FOUND) + "\n\n" + gameFile.getAbsolutePath(),
								Messages.get(MessageConstants.ERR_STARTING_GAME), JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
						return false;
					}
				}
				String unmountedDriveLetter = gameFile.getAbsolutePath().substring(0, 1);
				if (unmountedDriveLetter.equals("\\\\")) {
					dlgSplashScreen.dispose();
					JOptionPane.showMessageDialog(view,
							"cannot access network share",
							Messages.get(MessageConstants.ERR_STARTING_GAME), JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
				} else {
					dlgSplashScreen.dispose();
					//					Map<String, Action> actionKeysFixedDrive = new HashMap<>();
					//					actionKeysFixedDrive.put("hideMessage", null);
					//					NotificationElement element = new NotificationElement(new String[] { "fixedDriveNotAvailable", "L:" },
					//							actionKeysFixedDrive, NotificationElement.SUCCESS, null);
					//					view.showInformation(element);

					Map<String, Action> actionKeysDriveLetter = new HashMap<>();
					actionKeysDriveLetter.put("checkAgain", null);
					actionKeysDriveLetter.put("fixDriveLetters", null);
					actionKeysDriveLetter.put("hideMessage", null);
					NotificationElement element2 = new NotificationElement(new String[] { "driveNotAvailable", unmountedDriveLetter+":" }, actionKeysDriveLetter,
							NotificationElement.ERROR, null);
					view.showInformation(element2);

					JOptionPane.showMessageDialog(view,
							Messages.get(MessageConstants.DRIVE_NOT_MOUNTED, unmountedDriveLetter+":"),
							Messages.get(MessageConstants.ERR_STARTING_GAME), JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
					view.getViewManager().addUnmountedDriveLetter(unmountedDriveLetter);
				}
				return false;
			} else {
				dlgSplashScreen.dispose();
				JOptionPane.showMessageDialog(view, Messages.get(MessageConstants.GAME_NOT_FOUND) + "\n" + Messages.get(MessageConstants.GAME_NOT_FOUND_POST_FIX),
						MessageConstants.ERR_STARTING_GAME, JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
				return false;
			}
		}
		return true;
	}

	private void runGame2(Game game, List<String> startParametersList) throws IOException {
		int emulatorId = game.getDefaultEmulatorId();
		int platformId = game.getPlatformId();
		Emulator emulator;
		if (emulatorId == EmulatorConstants.NO_EMULATOR) {
			emulator = explorer.getEmulatorFromPlatform(platformId);
		} else {
			int gameId = game.getId();
			emulator = explorer.getEmulatorFromGame(gameId);
		}
		String taskName = emulator.getPath();
		getTaskList(taskName);

		ProcessBuilder builder = new ProcessBuilder(startParametersList);
		Process p = builder.redirectErrorStream(true).start();
		frameEmulationOverlay.setProcess(p);
		if (p != null) {
			TimerTask taskRunGame = new TimerTask() {

				@Override
				public void run() {
					if (!p.isAlive()) {
						p.destroy();
						int exitValue = p.exitValue();
						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								System.err.println("emulation stopped");
								frameEmulationOverlay.dispose();
								view.setState(Frame.NORMAL);
								view.toFront();
								view.repaint();
								if (exitValue != 0 && exitValue != 1) {
									game.setPlayCount(game.getPlayCount() - 1);
									try {
										explorerDAO.updatePlayCount(game);
									} catch (SQLException e) {
										e.printStackTrace();
									}
									JOptionPane.showMessageDialog(view,
											"Game has been started but emulation stopped with code " + exitValue
											+ "\r\nStart the emulator by hand to show detailed error message",
											"Emulation stopped", JOptionPane.ERROR_MESSAGE);
								}
							}
						});
						cancel();
						taskListRunningGames.remove(this);
					}
				}
			};
			taskListRunningGames.add(taskRunGame);
			Timer timer = new Timer();
			timer.schedule(taskRunGame, 0, 10);
			timerListRunningGames.add(timer);

			game.setPlayCount(game.getPlayCount() + 1);
			game.setLastPlayed(LocalDateTime.now());
			view.updatePlayCountForCurrentGame();

			try {
				explorerDAO.updatePlayCount(game);
				explorerDAO.updateLastPlayed(game);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					List<Integer> pidsNew;
					try {
						pidsNew = getTaskList(taskName);
						if (pidsNew.size() > 0) {
							Integer newPID = pidsNew.get(pidsNew.size()-1);
							if (processes.containsKey(game)) {
								processes.get(game).put(p, newPID);
							} else {
								Map<Process, Integer> pMap = new HashMap<>();
								pMap.put(p, newPID);
								processes.put(game, pMap);
							}
							frameEmulationOverlay.setPID(newPID);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			executorService.schedule(runnable, 3, TimeUnit.SECONDS);
		}
	}

	private List<Integer> getTaskList(String emulatorPath) throws IOException {
		if (ValidationUtil.isWindows()) {
			Path path = Paths.get(emulatorPath);
			String fileName = path.getFileName().toString();

			// ProcessBuilder pb = new ProcessBuilder("tasklist", "/FI",
			// "\"IMAGENAME eq "+fileName+"\"");
			String[] command = { "wmic", "process", "where", "\"name='" + fileName + "'\"", "get", "ProcessID,",
					"ExecutablePath", "/FORMAT:LIST" };
			ProcessBuilder pb = new ProcessBuilder(command);

			pb.redirectErrorStream(true);
			Process process = pb.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			List<Integer> pids = new ArrayList<>();
			String exec = "ExecutablePath=";
			String pidKey = "ProcessId=";

			boolean addPid = false;
			while ((line = reader.readLine()) != null) {
				if (line.toLowerCase().startsWith(exec.toLowerCase())) {
					String processPath = line.replace(exec, "").trim();
					if (processPath.equalsIgnoreCase(emulatorPath)) {
						addPid = true;
					}
				} else if (line.toLowerCase().startsWith(pidKey.toLowerCase())) {
					if (addPid) {
						String pid = line.replace(pidKey, "").trim();
						try {
							pids.add(Integer.valueOf(pid));
						} catch (NumberFormatException e) {
							// ignore
						}
					}
				}
			}
			try {
				process.waitFor();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return pids;
		} else if (ValidationUtil.isUnix()) {
			ProcessBuilder pb = new ProcessBuilder("ps", "ax");
			pb.redirectErrorStream(true);
			Process process = pb.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			List<Integer> pids = new ArrayList<>();
			while ((line = reader.readLine()) != null) {
				String[] arrLine = line.trim().split("\\s+");
				if (arrLine.length >= 5) {
					String processName = arrLine[4];
					if (processName.equalsIgnoreCase(emulatorPath)) {
						String pid = arrLine[0];
						try {
							pids.add(Integer.valueOf(pid));
						} catch (NumberFormatException e) {
							// ignore
						}
					}
				}
			}
			try {
				process.waitFor();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return pids;
		}
		return null;
	}

	private void openGamePropertiesFrame() {
		List<Game> games = explorer.getCurrentGames();
		if (!games.isEmpty()) {
			if (dlgGameProperties == null) {
				dlgGameProperties = new GamePropertiesDialog(explorer);
				dlgGameProperties.setLocationRelativeTo(view);
			}
			dlgGameProperties.scrollGameNameTextFieldToTop();
			dlgGameProperties.setGames(games);
			dlgGameProperties.setVisible(true);
		}
	}

	private void increaseFontSize() {
		view.increaseFontSize();
	}

	private void decreaseFontSize() {
		view.decreaseFontSize();
	}

	public void checkAndExit() {
		// if (!explorer.isSearchProgressComplete()) {
		// JOptionPane.showConfirmDialog(view, "Browsing for platforms is
		// currently in progress.\n"
		// + "Do you really want to exit?\n\n"
		// + "You can manually start the search process at any time");
		// }
		if (workerBrowseComputer != null && !workerBrowseComputer.isDone()) {
			String msg = Messages.get(MessageConstants.EXIT_REQUEST_SEARCH_IN_PROGRESS);
			String title = Messages.get(MessageConstants.EXIT_REQUEST);
			int request = JOptionPane.showConfirmDialog(view, msg, title, JOptionPane.YES_NO_OPTION);
			if (request == JOptionPane.YES_OPTION) {
				try {
					interruptSearchProcess();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				return;
			}
		}
		try {
			explorerDAO.setConfigWizardHiddenAtStartup(explorer.isConfigWizardHiddenAtStartup());
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if (isPropertiesFrameOpen()) {
			frameProperties.dispose();
		}

		if (frameEmulationOverlay != null && frameEmulationOverlay.isVisible()) {
			frameEmulationOverlay.dispose();
		}

		// try {
		// saveGameExplorer();
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		saveWindowInformations();
		view.logOut();
		view.setVisible(false);
		for (Entry<Game, Map<Process, Integer>> entry : processes.entrySet()) {
			Map<Process, Integer> pc2 = entry.getValue();
			for (Entry<Process, Integer> entry2 : pc2.entrySet()) {
				Process pc = entry2.getKey();
				Integer pId = entry2.getValue();
				if (pc.isAlive()) {
					int request = JOptionPane.showConfirmDialog(view, "Do you want to also close the currently running games?", "",
							JOptionPane.YES_NO_CANCEL_OPTION);
					if (request == JOptionPane.OK_OPTION) {
						if (ValidationUtil.isWindows()) {
							try {
								Runtime.getRuntime().exec("cmd.exe /c taskkill -IM " + pId);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} else if (ValidationUtil.isUnix()) {
							try {
								Runtime.getRuntime().exec("kill " + pId);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
			}
		}
		// boolean b = false;
		// if (processes.size() > 0) {
		// for (Process p : processes) {
		// if (p.isAlive()) {
		// p.destroy();
		// b = true;
		// }
		// }
		// }
		// for (TimerTask t : taskListRunningGames) {
		// t.cancel();
		// }
		// for (Timer t : timerListRunningGames) {
		// t.cancel();
		// }

		Game game = (explorer != null && explorer.hasCurrentGame()) ? explorer.getCurrentGames().get(0) : null;
		int gameId = (game != null) ? game.getId() : GameConstants.NO_GAME;
		try {
			explorerDAO.setSelectedGameId(gameId);
		} catch (SQLException e1) {
			try {
				explorerDAO.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				System.exit(0);
			}
		}
		try {
			if (quitNow()) {
				System.exit(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private boolean quitNow() throws SQLException {
		explorerDAO.closeConnection();
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		checkAndExit();
	}

	public void viewChanged() {
		// pnlMain.changeViewPanelTo();
	}

	private boolean isPropertiesFrameOpen() {
		return frameProperties != null && frameProperties.isVisible();
	}

	private void checkAddGame(File file) throws ZipException, SQLException, RarException, IOException {
		String filePath = file.getAbsolutePath();
		if (ValidationUtil.isWindows() && file.getAbsolutePath().toLowerCase().endsWith(".lnk")) {
			LnkParser lnkParser = new LnkParser(file);
			checkAddGame(new File(lnkParser.getRealFilename()));
			return;
		}
		if (explorer.hasFile(filePath)) {
			Game game = explorer.getGameForFile(filePath);
			if (view.getViewManager().isFilterFavoriteActive()) {
				if (!game.isFavorite()) {
					game.setRate(RatingBarPanel.MAXIMUM_RATE);
					rateGame(game);
				} else {
					String message = "<html><h3>This game already exists.</h3>"
							+ "The game does already exist in your list.</html>";
					String title = "Game already exists";
					JOptionPane.showMessageDialog(view, message, title, JOptionPane.INFORMATION_MESSAGE);
				}
			} else {
				String message = "<html><h3>This game already exists.</h3>"
						+ "The game does already exist in your list.</html>";
				String title = "Game already exists";
				JOptionPane.showMessageDialog(view, message, title, JOptionPane.INFORMATION_MESSAGE);
			}
			view.getViewManager().selectGame(game.getId());
			return;
		}
		if (file.length() == 0L) {
			UIUtil.showErrorMessage(view, "This file seems to be empty (0 bytes).\n\n"
					+ filePath + "\n\n"
					+ "Sorry but this is not supported.\n"
					+ "Make sure that the file is not corrupted and you did fully downloaded, moved or copied it to this location.\n\n"
					+ "Add the game again if you fixed the problem.",
					"Cannot add empty files");
			return;
		}
		Platform p0;
		try {
			p0 = isGameOrEmulator(filePath);
			if (p0 != null) {
				boolean doAddGame = true;
				for (Platform p : explorer.getPlatforms()) {
					if (explorer.hasEmulator(p.getName(), filePath)) {
						String message = "<html><h3>Emulator detected.</h3>" + file.getAbsolutePath() + "<br><br>"
								+ "This file has been recognized and added as an emulator.</html>";
						String title = "Emulator detected";
						JOptionPane.showMessageDialog(view, message, title, JOptionPane.INFORMATION_MESSAGE);
						doAddGame = false;
						break;
					}
				}
				if (doAddGame) {
					//					if (askBeforeAddGame) {
					//						String message = "<html><h3>Platform " + p0.getName() + " detected.</h3>" + file.getAbsolutePath()
					//						+ "<br><br>" + "Do you want to add this game now?<br><br>"
					//						+ "<a href=''>False platform detection</a></html>";
					//						String title = "Platform detected";
					//
					//						JCheckBox chkDontAsk = new JCheckBox("Add further games without asking me");
					//						Object[] objectArr = { message, " ", chkDontAsk };
					//						int result = JOptionPane.showConfirmDialog(view, objectArr, title, JOptionPane.YES_NO_OPTION);
					//						if (result == JOptionPane.YES_OPTION) {
					//							askBeforeAddGame = !chkDontAsk.isSelected();
					//							try {
					//								addGame(p0, file);
					//							} catch (BroGameDeletedException e) {
					//								JOptionPane.showConfirmDialog(view, Messages.get("gameDeleted"),
					//										Messages.get("gameDeletedTitle"), JOptionPane.YES_NO_OPTION);
					//							}
					//						}
					//					} else {
					addGame(p0, file, true, view.getViewManager().isFilterFavoriteActive());
					//					}
				}
			} else {
				if (filePath.toLowerCase().endsWith(".zip")) {
					String message = "<html><h3>This is a ZIP-Compressed archive.</h3>" + filePath
							+ "<br><br>" + "Do you want to auto detect the platform for the containing game?<br><br>"
							+ "When you press \"No\", you have to categorize it for yourself.</html>";
					String title = "ZIP-Archive";
					int result = JOptionPane.showConfirmDialog(view, message, title, JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.YES_OPTION) {
						String b = zipFileContainsGame(filePath, explorer.getExtensions());
						if (b != null && !b.isEmpty()) {
							Platform p = isGameInArchive(b);
							try {
								addGame(p, file);
							} catch (BroGameDeletedException e) {
								JOptionPane.showConfirmDialog(view, "deleted");
							}
						}
					} else if (result == JOptionPane.NO_OPTION) {
						message = "<html><h3>This is a ZIP-Compressed archive.</h3>"
								+ "Different platforms may use this file.<br><br>"
								+ "Select a platform from the list below to categorize the game.</html>";
						Platform[] objectsArr = getObjectsForPlatformChooserDialog(filePath);
						Platform defaultt = getDefaultPlatformFromChooser(filePath, objectsArr);
						Platform selected = (Platform) JOptionPane.showInputDialog(view, message, title,
								JOptionPane.WARNING_MESSAGE, null, objectsArr, defaultt);
						lastSelectedPlatformFromAddGameChooser = selected;
						Platform p2 = addOrGetPlatform(selected);
						if (p2 != null) {
							addGame(p2, file, true, view.getViewManager().isFilterFavoriteActive());
						}
					}
				} else if (filePath.toLowerCase().endsWith(".rar")) {
					String message = "<html><h3>This is a RAR-Compressed archive.</h3>" + filePath
							+ "<br><br>" + "Do you want to auto detect the platform for the containing game?<br><br>"
							+ "When you press \"No\", you have to categorize it for yourself.</html>";
					String title = "RAR-Archiv";
					int result = JOptionPane.showConfirmDialog(view, message, title, JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.YES_OPTION) {
						String b = rarFileContainsGame(filePath, explorer.getExtensions());
						if (b != null && !b.isEmpty()) {
							Platform p = isGameInArchive(b);
							addGame(p, file, true, view.getViewManager().isFilterFavoriteActive());
						} else {
							String message1 = Messages.get(MessageConstants.PLATFORM_NOT_RECOGNIZED) + "\n\n"
									+ filePath + "\n\n"
									+ "Is it a game or an emulator?"
									+ "Choose a platform from the list below to categorize the file:";
							String title1 = Messages.get(MessageConstants.PLATFORM_NOT_RECOGNIZED_TITLE);
							Platform[] objectsArr = getObjectsForPlatformChooserDialog(filePath);
							Platform defaultt = getDefaultPlatformFromChooser(filePath, objectsArr);

							Object[] messageEnlarged = {
									Messages.get(MessageConstants.PLATFORM_NOT_RECOGNIZED) + "\n\n",
									filePath,
									"\n",
									"Is it a game or an emulator?",
									new JRadioButton("Game"),
									new JRadioButton("Emulator"),
									"\n",
									"Choose a platform from the list below to categorize the file:",
									objectsArr
							};

							Platform selected = (Platform) JOptionPane.showInputDialog(view, messageEnlarged, title1,
									JOptionPane.WARNING_MESSAGE, null, objectsArr, defaultt);
						}
					}
				} else if (filePath.toLowerCase().endsWith(".iso") || filePath.toLowerCase().endsWith(".cso")
						|| filePath.toLowerCase().endsWith(".bin") || filePath.toLowerCase().endsWith(".img")) {
					String message = "<html><h3>This is an image file.</h3>"
							+ "Different platforms may use this file.<br><br>"
							+ "Select a platform from the list below to categorize the game.</html>";
					String title = "Disc image";
					Platform[] objectsArr = getObjectsForPlatformChooserDialog(filePath);
					Platform defaultt = getDefaultPlatformFromChooser(filePath, objectsArr);
					Platform selected = (Platform) JOptionPane.showInputDialog(view, message, title,
							JOptionPane.WARNING_MESSAGE, null, objectsArr, defaultt);
					lastSelectedPlatformFromAddGameChooser = selected;
					Platform p2 = addOrGetPlatform(selected);
					if (p2 != null) {
						addGame(p2, file, true, view.getViewManager().isFilterFavoriteActive());
						//					if (p2.getUseGameRegionCodes()) {
						if (filePath.toLowerCase().endsWith(".bin")
								|| filePath.toLowerCase().endsWith(".img")
								|| filePath.toLowerCase().endsWith(".iso")) {
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									LineIterator it;
									try {
										it = FileUtils.lineIterator(file);
										outterLoop:
											while (it.hasNext()) {
												String current = it.next();
												String arr[] = {
														"scus-",
														"slus-",
														"sces-",
														"sles-",
														"scps-",
														"scpm-",
														"slps-",
														"slpm-"
												};
												for (String s : arr) {
													if (current.toLowerCase().contains(s)) {
														System.out.println(current);
														Pattern MY_PATTERN = Pattern.compile("(?i)("+s+"\\d\\d\\d\\d\\d)");
														Matcher m = MY_PATTERN.matcher(current);
														while (m.find()) {
															String s2 = m.group(1);
															System.err.println(s2);
														}
														break outterLoop;
													}
												}
											}
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});

						}
						//					}
					}
				} else if (filePath.toLowerCase().endsWith(".cue")) {
					String message = "This is an addition file to an image file. Different platforms may use this file.\n\n"
							+ "Select a platform from the list below to categorize the game.";
					String title = "Disc image";
					Platform[] objectsArr = getObjectsForPlatformChooserDialog(filePath);
					Platform defaultt = getDefaultPlatformFromChooser(filePath, objectsArr);
					Platform selected = (Platform) JOptionPane.showInputDialog(view, message, title,
							JOptionPane.WARNING_MESSAGE, null, objectsArr, defaultt);
					lastSelectedPlatformFromAddGameChooser = selected;
					Platform p2 = addOrGetPlatform(selected);
					if (p2 != null) {
						addGame(p2, file, true, view.getViewManager().isFilterFavoriteActive());
					}
				} else {
					String message = Messages.get(MessageConstants.PLATFORM_NOT_RECOGNIZED) + "\n\n"
							+ filePath + "\n\n"
							+ "Choose a platform from the list below:";
					String title = Messages.get(MessageConstants.PLATFORM_NOT_RECOGNIZED_TITLE);
					List<Platform> platforms = explorer.getPlatforms();
					Platform[] objectsArr = platforms.toArray(new Platform[platforms.size()]);
					JComboBox<Platform> cmbPlatforms = new JComboBox<>(objectsArr);
					cmbPlatforms.setSelectedItem(null);
					JRadioButton rdbGame = new JRadioButton("Game");
					JRadioButton rdbEmulator = new JRadioButton("Emulator");
					rdbGame.setSelected(true);
					ButtonGroup grp = new ButtonGroup();
					grp.add(rdbGame);
					grp.add(rdbEmulator);
					Object[] messageEnlarged = {
							Messages.get(MessageConstants.PLATFORM_NOT_RECOGNIZED) + "\n\n",
							filePath,
							"\n",
							"Is it a game or an emulator?",
							rdbGame,
							rdbEmulator,
							"\n",
							"Choose a platform from the list below to categorize the file:",
							cmbPlatforms,
							"\n",
							new JLinkButton("Your platform doesn't show up? Create a new platform instead.")
					};
					int request = JOptionPane.CANCEL_OPTION;
					do {
						request = JOptionPane.showConfirmDialog(view, messageEnlarged, title,
								JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
					} while (request == JOptionPane.OK_OPTION && cmbPlatforms.getSelectedItem() == null);
					if (request == JOptionPane.OK_OPTION) {
						String fileExtension = FilenameUtils.getExtension(filePath);
						Platform selectedPlatform = (Platform) cmbPlatforms.getSelectedItem();
						String gameOrEmulator = rdbGame.isSelected() ? "game" : "emulator";
						if (fileExtension == null || fileExtension.trim().isEmpty()) {
							System.out.println("This should add new " + gameOrEmulator + " file "+ filePath + " to platform " + selectedPlatform.getName());
						} else {
							System.out.println("This should add new " + gameOrEmulator + " extension " + fileExtension + " to platform " + selectedPlatform.getName());
							String newSearchFor = "^(.+)\\."+fileExtension+"$";
							selectedPlatform.addSearchFor(newSearchFor);
							explorerDAO.addSearchFor(selectedPlatform.getId(), newSearchFor);
							System.out.println(selectedPlatform.getSearchFor());
						}
					}
				}
			}
		} catch (BroEmulatorDeletedException e1) {
			String emulatorName = "<html><strong>"+e1.getEmulator().getName()+"</strong></html>";
			//			String platformName = explorer.getPlatform(e1.getEmulator().getPlatformId()).getName();
			int request = JOptionPane.showConfirmDialog(view, Messages.get(MessageConstants.EMULATOR_DELETED, emulatorName, "platformName"),
					Messages.get(MessageConstants.EMULATOR_DELETED_TITLE), JOptionPane.YES_NO_OPTION);
			if (request == JOptionPane.YES_OPTION) {
				explorerDAO.restoreEmulator(e1.getEmulator());
			} else {
				return;
			}
		}
	}

	private void checkAddGames(List<File> files) {
		List<File> gamesToCheck = new ArrayList<>();
		JDialog dlgCheckFolder = new JDialog();
		dlgCheckFolder.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		JList<String> lstFolderFiles = new JList<>();
		DefaultListModel<String> mdlLstFolderFiles = new DefaultListModel<>();
		lstFolderFiles.setModel(mdlLstFolderFiles);
		for (File file : files) {
			if (file.isDirectory()) {
				mdlLstFolderFiles.addElement(file.getAbsolutePath());
				gamesToCheck.add(file);
				//								File[] subFolderFiles = file.listFiles();
				//								for (File f : subFolderFiles) {
				//									if (f.isFile()) {
				//										gamesToCheck.add(f);
				//									}
				//								}
			} else {
				mdlLstFolderFiles.addElement(file.getAbsolutePath());
				gamesToCheck.add(file);
			}
		}
		dlgCheckFolder.add(lstFolderFiles);
		dlgCheckFolder.pack();
		dlgCheckFolder.setVisible(true);
		//		checkAddGames(gamesToCheck);
	}

	private Platform[] getObjectsForPlatformChooserDialog(String filePath) {
		List<Platform> objects = new ArrayList<>();
		for (Platform p : getPlatformMatches(FilenameUtils.getExtension(filePath.toLowerCase()))) {
			objects.add(p);
		}
		return objects.toArray(new Platform[objects.size()]);
	}

	private Platform getDefaultPlatformFromChooser(String filePath, Platform[] objectsArr) {
		Platform defaultt = null;
		List<Platform> matchedPlatforms = explorer.getPlatformsFromCommonDirectory(filePath);
		if (!matchedPlatforms.isEmpty()) {
			for (Platform mp : matchedPlatforms) {
				for (Platform p : objectsArr) {
					if (p.getName().equals(mp.getName())) {
						if (matchedPlatforms.size() > 1) {
							for (Platform p9 : matchedPlatforms) {
								if (lastSelectedPlatformFromAddGameChooser == null) {
									defaultt = matchedPlatforms.get(0);
									break;
								}
								if (p9.getName().equals(lastSelectedPlatformFromAddGameChooser.getName())) {
									defaultt = lastSelectedPlatformFromAddGameChooser;
									break;
								}
							}
						} else {
							defaultt = p;
						}
						if (defaultt != null) {
							break;
						}
					}
				}
				if (defaultt != null) {
					break;
				}
			}
		}
		return defaultt;
	}

	private List<Platform> getPlatformMatches(String extension) {
		String prefix = ".";
		String finalExtension = extension.startsWith(prefix) ? extension : (prefix + extension);
		List<Platform> platforms = new ArrayList<>();
		for (Platform p : explorer.getPlatforms()) {
			if (p.hasGameSearchMode(GameConstants.ARCHIVE_FILE_NAME_MATCH)) {
				for (String imageType : p.getSupportedArchiveTypes()) {
					if (imageType.equalsIgnoreCase(finalExtension)) {
						platforms.add(p);
					}
				}
			}
			if (p.hasGameSearchMode(GameConstants.IMAGE_FILE_NAME_MATCH)) {
				for (String imageType : p.getSupportedImageTypes()) {
					if (imageType.equalsIgnoreCase(finalExtension)) {
						platforms.add(p);
					}
				}
			}
		}
		return platforms;
	}

	class AutoSearchListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			searchForPlatforms();
		}
	}

	class CustomSearchListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			List<File> dirs = view.getSelectedDirectoriesToBrowse();
			searchForPlatforms(dirs);
		}
	}

	class QuickSearchListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			quickSearch();
		}
	}

	class LastSearchListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

		}
	}

	class CoverDragDropListener implements DropTargetListener {

		@Override
		public void drop(DropTargetDropEvent event) {
			event.acceptDrop(DnDConstants.ACTION_MOVE);
			Transferable transferable = event.getTransferable();
			DataFlavor[] flavors = transferable.getTransferDataFlavors();
			for (DataFlavor flavor : flavors) {
				try {
					if (flavor.isFlavorJavaFileListType()) {
						@SuppressWarnings("unchecked")
						List<File> files = (List<File>) transferable.getTransferData(flavor);
						for (File file : files) {
							if (file.isFile()) {
								ConvertImageWorker worker = new ConvertImageWorker(file);
								worker.execute();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			event.dropComplete(true);
		}

		@Override
		public void dragEnter(DropTargetDragEvent e) {
		}

		@Override
		public void dragExit(DropTargetEvent e) {
		}

		@Override
		public void dragOver(DropTargetDragEvent e) {
		}

		@Override
		public void dropActionChanged(DropTargetDragEvent e) {
		}
	}

	class CoverToLibraryDragDropListener implements DropTargetListener {
		@Override
		public void drop(DropTargetDropEvent event) {
			event.acceptDrop(DnDConstants.ACTION_MOVE);
			Transferable transferable = event.getTransferable();
			DataFlavor[] flavors = transferable.getTransferDataFlavors();
			for (DataFlavor flavor : flavors) {
				try {
					if (flavor.isFlavorJavaFileListType()) {
						@SuppressWarnings("unchecked")
						List<File> files = (List<File>) transferable.getTransferData(flavor);
						Object message = "Do you want to clear the list of previously added image files, before adding new images?";
						String title = "Clear images list";
						int request = JOptionPane.showConfirmDialog(view, message, title, JOptionPane.YES_NO_CANCEL_OPTION);
						if (request != JOptionPane.CANCEL_OPTION && request != JOptionPane.CLOSED_OPTION) {
							if (request == JOptionPane.YES_OPTION) {
								view.removeAllPictures();
							}
							for (File file : files) {
								BrowseCoversOnComputerWorker worker = new BrowseCoversOnComputerWorker(file);
								worker.execute();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			event.dropComplete(true);
		}

		@Override
		public void dragEnter(DropTargetDragEvent e) {
		}

		@Override
		public void dragExit(DropTargetEvent e) {
		}

		@Override
		public void dragOver(DropTargetDragEvent e) {
		}

		@Override
		public void dropActionChanged(DropTargetDragEvent e) {
		}
	}

	class ShowUncategorizedFilesDialogListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JDialog dlg = new JDialog();
			dlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			DefaultListModel<String> mdlLst = new DefaultListModel<>();
			JList<String> lst = new JList<>(mdlLst);
			List<String> arrList = new ArrayList<>();
			arrList.addAll(zipFiles);
			arrList.addAll(rarFiles);
			arrList.addAll(isoFiles);
			Collections.sort(arrList);
			for (String s : arrList) {
				mdlLst.addElement(s);
			}
			dlg.add(new JScrollPane(lst));
			dlg.pack();
			dlg.setVisible(true);
		}
	}

	class GameDragDropListener implements DropTargetListener {

		@Override
		public void drop(DropTargetDropEvent event) {
			event.acceptDrop(DnDConstants.ACTION_MOVE);
			Transferable transferable = event.getTransferable();
			DataFlavor[] flavors = transferable.getTransferDataFlavors();
			for (DataFlavor flavor : flavors) {
				try {
					if (flavor.isFlavorJavaFileListType()) {
						@SuppressWarnings("unchecked")
						List<File> files = (List<File>) transferable.getTransferData(flavor);
						if (files.size() == 1) {
							File file = files.get(0);
							if (file.isDirectory()) {
								checkAddGames(files);
							} else {
								checkAddGame(file);
							}
						}
						if (files.size() > 1) {
							checkAddGames(files);
							//							String message = "You are about to drop " + files.size() + " files.\n"
							//									+ "Do you want to";
							//							String title = "Add multiple files";
							//							int result = JOptionPane.showConfirmDialog(view, message, title, JOptionPane.YES_NO_OPTION);
							//							if (result == JOptionPane.YES_OPTION) {
							//								askUserBeforeAddGame = false;
							//							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			event.dropComplete(true);
		}

		@Override
		public void dragEnter(DropTargetDragEvent e) {
		}

		@Override
		public void dragExit(DropTargetEvent e) {
		}

		@Override
		public void dragOver(DropTargetDragEvent e) {
		}

		@Override
		public void dropActionChanged(DropTargetDragEvent e) {
		}
	}

	class ConvertImageWorker extends SwingWorker<Integer, Image> {
		private File file;
		private ButtonGroup group = new ButtonGroup();
		private JFrame frameImageEdit;
		private ImageEditPanel pnlImageEdit;
		private JToggleButton btnCut = new JToggleButton("Bild zuschneiden");
		private JTextField txtCutBorder = new JTextField("5");
		private JButton btnRotate = new JButton("Bild drehen");
		private JCheckBox chkScale = new JCheckBox("Bild skalieren auf: ");
		private JButton btnSetAsCover = new JButton("Als Cover festlegen");
		private int width;
		private int height;

		public ConvertImageWorker(File file) {
			this.file = file;
		}

		@Override
		protected Integer doInBackground() throws Exception {
			String extension = file.getName().toLowerCase();
			boolean b = extension.endsWith(".jpg") || extension.endsWith(".jpeg") || extension.endsWith(".png")
					|| extension.endsWith(".gif") || extension.endsWith(".bmp");
			if (b) {
				try {
					// ImageIcon ii = new ImageIcon(file.getAbsolutePath());
					BufferedImage bi = ImageIO.read(file);
					width = bi.getWidth();
					height = bi.getHeight();

					showImageEditDialog(file, bi);

					//					publish(resized);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// if (icon.getIconWidth() > icon.getIconHeight()) {
			// scaledIcon = ImageUtil.scaleCover(icon, 128,
			// CoverConstants.SCALE_HEIGHT_OPTION);
			// } else {
			// scaledIcon = ImageUtil.scaleCover(icon, 128,
			// CoverConstants.SCALE_WIDTH_OPTION);
			// }
			return 1;
		}

		private void showImageEditDialog(File file, BufferedImage bi) {
			if (frameImageEdit == null) {
				frameImageEdit = new JFrame();
				frameImageEdit.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				frameImageEdit.setLayout(new BorderLayout());

				JPanel pnlButtons = new JPanel();
				pnlButtons.add(btnCut);
				pnlButtons.add(txtCutBorder);
				pnlButtons.add(btnRotate);
				pnlButtons.add(chkScale);
				pnlButtons.add(btnSetAsCover);
				JTextField txtHeight = new JTextField("200");
				pnlButtons.add(txtHeight);
				JButton btnScaleNow = new JButton("Jetzt skalieren");
				pnlButtons.add(btnScaleNow);
				pnlImageEdit = new ImageEditPanel();
				btnRotate.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						pnlImageEdit.rotateRight();
					}
				});

				chkScale.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						pnlImageEdit.setScaleEnabled(chkScale.isSelected());
					}
				});

				btnSetAsCover.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Image resized = pnlImageEdit.getCuttedImage(pnlImageEdit.getCurrentCoverWidth(), pnlImageEdit.getCurrentCoverHeight());
						List<Game> currentGame = explorer.getCurrentGames();
						String emuBroCoverHome = System.getProperty("user.home") + File.separator + ".emubro"
								+ File.separator + "covers";
						String coverPath = emuBroCoverHome + File.separator + explorer.getChecksum(currentGame.get(0).getChecksumId())
						+ File.separator + System.currentTimeMillis() + ".png";
						File coverHomeFile = new File(coverPath);
						if (!coverHomeFile.exists()) {
							coverHomeFile.mkdirs();
						}
						try {
							ImageIO.write((RenderedImage) resized, "png", new File(coverPath));
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						if (!currentGame.get(0).getCoverPath().equals(coverPath)) {
							currentGame.get(0).setCoverPath(coverPath);
							try {
								explorerDAO.setGameCoverPath(currentGame.get(0).getId(), coverPath);
							} catch (SQLException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
						}
						publish(resized);
					}
				});

				btnCut.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						pnlImageEdit.setCutEnabled(btnCut.isSelected());
						pnlImageEdit.setCutBorder(Double.valueOf(txtCutBorder.getText()));
					}
				});

				btnScaleNow.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						pnlImageEdit.setCoverHeight(Integer.valueOf(txtHeight.getText()));
					}
				});

				frameImageEdit.add(pnlButtons, BorderLayout.NORTH);
				JScrollPane spImageEdit;
				frameImageEdit.add(spImageEdit = new JScrollPane(pnlImageEdit));
				spImageEdit.setPreferredSize(new Dimension(0, 0));
				frameImageEdit.pack();
				frameImageEdit.setSize(800, 800);
			}
			frameImageEdit.setLocationRelativeTo(view);
			frameImageEdit.setVisible(true);
			pnlImageEdit.setImage(bi);
			pnlImageEdit.setFile(file);
		}

		@Override
		protected void process(List<Image> chunks) {
			for (Image i : chunks) {
				//				view.gameCoverChanged(explorer.getCurrentGames(), i);
				// explorerDAO.setCover(model.getCurrentGame(), new
				// ImageIcon(i));
			}
		}

		@Override
		protected void done() {
		}
	}

	class BrowseCoversOnComputerWorker extends SwingWorker<Integer, ImageIcon> {
		private File urls;
		private ButtonGroup group = new ButtonGroup();

		public BrowseCoversOnComputerWorker(File urls) {
			this.urls = urls;
		}

		@Override
		protected Integer doInBackground() throws Exception {
			IOFileFilter coverFileFilter = new IOFileFilter() {
				@Override
				public boolean accept(File arg0, String arg1) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean accept(File file) {
					String extension = file.getName().toLowerCase();
					boolean b = extension.endsWith(".jpg") || extension.endsWith(".jpeg") || extension.endsWith(".png")
							|| extension.endsWith(".gif");
					return b;
				}
			};
			if (urls.isDirectory()) {
				Collection<File> files = FileUtils.listFiles(urls, coverFileFilter, null);
				if (files.size() > 75) {
					int request = JOptionPane.showConfirmDialog(view,
							"Wow you have a lot of picture files in there.\r\n" + "Elements: " + files.size() + "\r\n\r\n"
									+ "Are you sure you want to add them all?",
									"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (request != JOptionPane.YES_OPTION) {
						return -1;
					}
				}
				for (File f : files) {
					Image img = ImageIO.read(f);
					ImageIcon icon = new ImageIcon(img);

					ImageIcon scaledIcon;
					if (icon.getIconWidth() > icon.getIconHeight()) {
						scaledIcon = ImageUtil.scaleCover(icon, 96, CoverConstants.SCALE_HEIGHT_OPTION);
					} else {
						scaledIcon = ImageUtil.scaleCover(icon, 96, CoverConstants.SCALE_WIDTH_OPTION);
					}
					img.flush();
					publish(scaledIcon);
				}
				return 1;
			} else {
				Image img = ImageIO.read(urls);
				ImageIcon icon = new ImageIcon(img);

				ImageIcon scaledIcon;
				if (icon.getIconWidth() > icon.getIconHeight()) {
					scaledIcon = ImageUtil.scaleCover(icon, 96, CoverConstants.SCALE_HEIGHT_OPTION);
				} else {
					scaledIcon = ImageUtil.scaleCover(icon, 96, CoverConstants.SCALE_WIDTH_OPTION);
				}
				img.flush();
				publish(scaledIcon);
				return 1;
			}
		}

		@Override
		protected void process(List<ImageIcon> chunks) {
			for (ImageIcon scaledIcon : chunks) {
				view.addPictureFromComputer(scaledIcon);
			}
		}

		@Override
		protected void done() {
		}
	}

	class GameOptionsPane extends JOptionPane {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMaxCharactersPerLineCount() {
			return 80;
		}
	}

	public class SortGameListAscendingListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			//			Game currentGame = explorer.getCurrentGames();
			sortGameList(ViewConstants.SORT_ASCENDING);
			//			if (currentGame != null) {
			//				view.selectGameNoListeners(currentGame.getId());
			//			}
		}
	}

	public class SortGameListDescendingListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			sortGameList(ViewConstants.SORT_DESCENDING);
		}
	}

	public class SortByTitleListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			sortBy(ViewConstants.SORT_BY_TITLE, null);
		}
	}

	public class SortByPlatformListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			sortBy(ViewConstants.SORT_BY_PLATFORM, (PlatformComparator) platformComparator);
		}
	}

	public class GroupByNoneListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			groupBy(ViewConstants.GROUP_BY_NONE);
		}
	}

	public class GroupByPlatformListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			groupBy(ViewConstants.GROUP_BY_PLATFORM);
		}
	}

	public class GroupByTitleListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			groupBy(ViewConstants.GROUP_BY_TITLE);
		}
	}

	public class BroFilterListener implements FilterListener {

		@Override
		public void filterSet(FilterEvent e) {
			view.filterSet(e);
		}
	}

	class RunGameListener implements ActionListener, MouseListener, Action {
		@Override
		public void actionPerformed(ActionEvent e) {
			runGame();
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// lastSelectedIndex = lstGames.getSelectedIndex();

			boolean rightMouseButton = (e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK;
			if (e.getSource() instanceof JList) {
				@SuppressWarnings("unchecked")
				JList<Game> lstGames = (JList<Game>) e.getSource();
				if (!rightMouseButton && e.getClickCount() == 2) {
					if (e.getModifiersEx() == InputEvent.ALT_DOWN_MASK) {
						openGamePropertiesFrame();
						return;
					}
					lstGames.locationToIndex(e.getPoint());
					runGame();
				}
			}
			if (e.getSource() instanceof JTable) {
				e.getSource();
				if (!rightMouseButton && e.getClickCount() == 2) {
					if (e.getModifiersEx() == InputEvent.ALT_DOWN_MASK) {
						openGamePropertiesFrame();
						return;
					}
					// int index = lstGames.locationToIndex(e.getPoint());
					runGame();
				}
			}
			if (e.getSource() instanceof JToggleButton) {
				if (!rightMouseButton && e.getClickCount() == 2) {
					if (e.getModifiersEx() == InputEvent.ALT_DOWN_MASK) {
						openGamePropertiesFrame();
						return;
					}
					// int index = lstGames.locationToIndex(e.getPoint());
					runGame();
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// super.mousePressed(e);
			// lastMouseY = e.getYOnScreen();
			// if (SwingUtilities.isRightMouseButton(e)) {
			// int row = lstGames.locationToIndex(e.getPoint());
			// lstGames.setSelectedIndex(row);
			// }
			// bla(e.getPoint());
			// lstGames.setSelectedIndex(lastSelectedIndex);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// super.mouseReleased(e);
			// setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			//
			// Timer timer = new Timer();
			// TimerTask task = new TimerTask() {
			//
			// @Override
			// public void run() {
			// if (lastScrollDistance != 0) {
			// if (lastScrollDistance > 0) {
			// if ((lastScrollDistance % 2) == 0) {
			// lastScrollDistance -= 2;
			// } else {
			// lastScrollDistance--;
			// }
			// } else {
			// if ((lastScrollDistance % 2) == 0) {
			// lastScrollDistance += 2;
			// } else {
			// lastScrollDistance++;
			// }
			// }
			//
			// lstGames.scrollRectToVisible(new Rectangle(0,
			// lstGames.getVisibleRect().y
			// + lastScrollDistance, lstGames
			// .getVisibleRect().width, lstGames
			// .getVisibleRect().height));
			//
			// getRootPane().revalidate();
			// getRootPane().repaint();
			// } else {
			// cancel();
			// }
			// }
			// };
			//
			// timer.schedule(task, 0, 10);
			// }
		}

		private Map<String, Object> map = new HashMap<>();

		@Override
		public Object getValue(String key) {
			return map.get(key);
		}

		@Override
		public boolean isEnabled() {
			runGame();
			return false;
		}

		@Override
		public void putValue(String key, Object value) {
			map.put(key, value);
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
		}

		@Override
		public void setEnabled(boolean b) {
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			// TODO Auto-generated method stub

		}
	}

	class LoadDiscListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			FileSystemView fsv = FileSystemView.getFileSystemView();
			File[] roots = fsv.getRoots();
			if (roots.length == 1) {
				roots = roots[0].listFiles()[0].listFiles();
				List<File> foundDrives = new ArrayList<>();
				for (int i = 0; i < roots.length; i++) {
					if (fsv.isDrive(roots[i])) {
						if (fsv.getSystemTypeDescription(roots[i]).indexOf("CD") != -1) {
							foundDrives.add(roots[i]);
							System.out.println(roots[i]);
						}
					}
				}
			}
			else {
				//				System.out.println("I guess you're not on Windows");
			}
			//			return foundDrives;
		}
	}

	class ConfigureEmulatorListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			showPropertiesFrame(explorer.getCurrentGames().get(0));
			System.err.println("open properties for current game: "+explorer.getCurrentGames());
		}
	}

	class CoverFromComputerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
		}
	}

	class TagFromWebListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			List<Game> currentGame = explorer.getCurrentGames();
			List<String> dontSearchAgain = new ArrayList<>();
			for (Game game : currentGame) {
				Platform platform = explorer.getPlatform(game.getPlatformId());
				String platformShortName = platform.getShortName();
				if (dontSearchAgain.contains(platformShortName)) {
					continue;
				}
				try {
					getFileFromUrl(platformShortName);
				} catch (IOException e1) {
					dontSearchAgain.add(platformShortName);
					UIUtil.showErrorMessage(view, "No gamelist found at this source for platform " + platformShortName, "no gamelist found");
				}
			}
		}
	}

	private File getFileFromUrl(String platformShortName) throws IOException {
		String searchString = platformShortName;
		//				String defPlatformName = (platformShortName != null && !platformShortName.trim().isEmpty())
		//						? platformShortName : platform.getName();

		try {
			URL url = new URL("http://www.emubro.net/games/"+searchString.replace(" ", "%20")+".xml");
			File emuBroGameHome = new File(System.getProperty("user.home") + File.separator + ".emubro"
					+ File.separator + "games/"+searchString+".xml");
			URLConnection con = url.openConnection();
			con.setReadTimeout(20000);
			FileUtils.copyURLToFile(url, emuBroGameHome);
			UIUtil.showInformationMessage(view, "Download completed and file installed/updated:\n"
					+ emuBroGameHome.getAbsolutePath(), "Success");
			return emuBroGameHome;
		} catch (MalformedURLException e2) {
			UIUtil.showErrorMessage(view, "Cannot open url", "Error opening url");
		}
		return null;
	}

	class AutoSearchTagsListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			List<Game> currentGames = explorer.getCurrentGames();
			autoSearchTags(currentGames, true);
		}
	}

	class AutoSearchTagsAllListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			List<Game> allGames = explorer.getGames();
			autoSearchTags(allGames, false);
		}
	}

	class CoverFromWebListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			List<Game> currentGame = explorer.getCurrentGames();
			for (Game game : currentGame) {
				String gameName = game.getName();
				Platform platform = explorer.getPlatform(game.getPlatformId());
				String platformShortName = platform.getShortName();
				String defPlatformName = (platformShortName != null && !platformShortName.trim().isEmpty())
						? platformShortName : platform.getName();
				String coverOrIcon = "cover OR icon";
				String site = "https://www.gametdb.com/";
				boolean useSpecificSite = true;
				String searchString = (useSpecificSite ? "site:"+site + " "  : "") + gameName + " " + defPlatformName + " " + coverOrIcon;
				String url = "https://www.google.com/search?q="+searchString.replace(" ", "+").replace("&", "%26")+"&tbm=isch";
				try {
					openWebsite(url);
				} catch (IOException e1) {
					UIUtil.showWarningMessage(view, "Maybe there is a conflict with your default web browser and you have to set it again."
							+ "\n\nThe default program page in control panel will be opened now..", "default web browser");
					try {
						Runtime.getRuntime().exec("control.exe /name Microsoft.DefaultPrograms /page pageDefaultProgram");
					} catch (IOException e2) {
						UIUtil.showErrorMessage(view, "The default program page couldn't be opened.", "oops");
					}
				} catch (URISyntaxException e1) {
					UIUtil.showErrorMessage(view, "The url couldn't be opened.", "oops");
				}
			}
		}
	}

	class TrailerFromWebListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			List<Game> currentGame = explorer.getCurrentGames();
			for (Game game : currentGame) {
				String gameName = game.getName();
				Platform platform = explorer.getPlatform(game.getPlatformId());
				String platformShortName = platform.getShortName();
				String defPlatformName = (platformShortName != null && !platformShortName.trim().isEmpty())
						? platformShortName : platform.getName();
				String searchString = gameName + " " + defPlatformName + " gameplay OR trailer";
				String url = "https://www.youtube.com/results?search_query="+searchString.replace(" ", "+").replace("&", "%26") + "&tbm=vid";
				try {
					openWebsite(url);
				} catch (IOException e1) {
					UIUtil.showWarningMessage(view, "Maybe there is a conflict with your default web browser and you have to set it again."
							+ "\n\nThe default program page in control panel will be opened now..", "default web browser");
					try {
						Runtime.getRuntime().exec("control.exe /name Microsoft.DefaultPrograms /page pageDefaultProgram");
					} catch (IOException e2) {
						UIUtil.showErrorMessage(view, "The default program page couldn't be opened.", "oops");
					}
				} catch (URISyntaxException e1) {
					UIUtil.showErrorMessage(view, "The url couldn't be opened.", "oops");
				}
			}
		}
	}

	class SearchNetworkListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showInputDialog("Enter network share:");
		}
	}

	class RenameGameListener implements Action {
		private JButton btnAutoSetLetterCase = new JButton(Messages.get(MessageConstants.CAPITAL_SMALL_LETTERS));
		private JLabel lblSpaces = new JLabel(Messages.get(MessageConstants.REPLACE));
		private JLabel lblBrackets = new JLabel(Messages.get(MessageConstants.REMOVE_BRACKETS));
		private JLabel lblOr = new JLabel(Messages.get(MessageConstants.OR));
		private JLabel lblOr2 = new JLabel(Messages.get(MessageConstants.OR));
		private JButton btnSpacesDots = new JButton(Messages.get(MessageConstants.DOTS));
		private JButton btnSpacesUnderlines = new JButton(Messages.get(MessageConstants.UNDERLINES));
		private JButton btnSpacesHyphens = new JButton(Messages.get(MessageConstants.HYPHENS));
		private JButton btnSpacesCamelCase = new JButton(Messages.get(MessageConstants.SPLIT_CAMEL_CASE));
		//		private JButton btnBracket1 = new JButton("(PAL), (Europe), ...");
		private JButton btnBracket1 = new JButton("(  )");
		//		private JButton btnBracket2 = new JButton("[SCES-12345], [!], ...");
		private JButton btnBracket2 = new JButton("[  ]");
		private JComboBox<Object> cmbParentFolders;
		private JList<Game> lstMatches;
		private JList<String> lstPreviews;
		protected boolean dontChangeMatchesIndex;
		protected boolean dontChangePreviewIndex;
		private ListSelectionListener listener;
		private ListSelectionListener listener2;
		private AdjustmentListener listener3;
		private AdjustmentListener listener4;
		private JCheckBox chkRenameFile = new JCheckBox(Messages.get(MessageConstants.RENAME_FILE_ON_DISK));
		private JExtendedTextField txtRenameFile = new JExtendedTextField("");
		private JLabel lblBracketsExample = new JLabel(Messages.get(MessageConstants.BRACKETS_EXAMPLE));
		private JLabel lblWithSpaces = new JLabel(Messages.get(MessageConstants.WITH_SPACES));
		private JCheckBox chkDots = new JCheckBox(Messages.get(MessageConstants.REMOVE_DOTS));
		private JCheckBox chkUnderlines = new JCheckBox(Messages.get(MessageConstants.REMOVE_UNDERLINES));
		protected boolean showMoreOptions;

		{
			btnAutoSetLetterCase.addActionListener(this);
			btnSpacesDots.addActionListener(this);
			btnSpacesUnderlines.addActionListener(this);
			btnSpacesHyphens.addActionListener(this);
			btnSpacesCamelCase.addActionListener(this);
			btnBracket1.addActionListener(this);
			btnBracket2.addActionListener(this);
		}

		public void languageChanged() {
			txtRenameFile.languageChanged();
			btnAutoSetLetterCase = new JButton(Messages.get(MessageConstants.CAPITAL_SMALL_LETTERS));
			lblSpaces.setText(Messages.get(MessageConstants.REPLACE));
			lblBrackets.setText(Messages.get(MessageConstants.REMOVE_BRACKETS));
			lblOr.setText(Messages.get(MessageConstants.OR));
			lblOr2.setText(Messages.get(MessageConstants.OR));
			btnSpacesDots.setText(Messages.get(MessageConstants.DOTS));
			btnSpacesUnderlines.setText(Messages.get(MessageConstants.UNDERLINES));
			btnSpacesHyphens.setText(Messages.get(MessageConstants.HYPHENS));
			btnSpacesCamelCase.setText(Messages.get(MessageConstants.SPLIT_CAMEL_CASE));
			chkRenameFile.setText(Messages.get(MessageConstants.RENAME_FILE_ON_DISK));
			lblBracketsExample.setText(Messages.get(MessageConstants.BRACKETS_EXAMPLE));
			lblWithSpaces.setText(Messages.get(MessageConstants.WITH_SPACES));
			chkDots.setText(Messages.get(MessageConstants.REMOVE_DOTS));
			chkUnderlines.setText(Messages.get(MessageConstants.REMOVE_UNDERLINES));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnSpacesDots) {
				String item = cmbParentFolders.getEditor().getItem().toString();
				cmbParentFolders.getEditor().setItem(removeUnnecessarySpaces(item.replace(".", " ")));
			} else if (e.getSource() == btnSpacesUnderlines) {
				String item = cmbParentFolders.getEditor().getItem().toString();
				cmbParentFolders.getEditor().setItem(removeUnnecessarySpaces(item.replace("_", " ")));
			} else if (e.getSource() == btnSpacesHyphens) {
				String item = cmbParentFolders.getEditor().getItem().toString();
				cmbParentFolders.getEditor().setItem(removeUnnecessarySpaces(item.replace("-", " ")));
			} else if (e.getSource() == btnSpacesCamelCase) {
				String item = cmbParentFolders.getEditor().getItem().toString();
				String undoCamelCase = "";
				for (String w : item.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
					undoCamelCase += w + " ";
				}
				cmbParentFolders.getEditor().setItem(removeUnnecessarySpaces(undoCamelCase));
			} else if (e.getSource() == btnAutoSetLetterCase) {
				String source = cmbParentFolders.getEditor().getItem().toString();
				StringBuffer res = new StringBuffer();
				String[] strArr = source.split(" ");
				for (String str : strArr) {
					char[] stringArray = str.trim().toCharArray();
					if (stringArray.length > 0) {
						stringArray[0] = Character.toUpperCase(stringArray[0]);
						for (int i = 1; i < stringArray.length; i++) {
							stringArray[i] = Character.toLowerCase(stringArray[i]);
						}
						str = new String(stringArray);
						res.append(str).append(" ");
					}
				}
				cmbParentFolders.getEditor().setItem(res.toString().trim());
			} else if (e.getSource() == btnBracket1) {
				boolean hasBrackets = false;
				do {
					hasBrackets = removeBrackets('(',')');
				} while (hasBrackets);
			} else if (e.getSource() == btnBracket2) {
				boolean hasBrackets = false;
				do {
					hasBrackets = removeBrackets('[',']');
				} while (hasBrackets);
			} else {
				renameGame();
			}
		}

		private String removeUnnecessarySpaces(String item) {
			String tmp = item;
			while (tmp.contains("  ")) {
				tmp = tmp.replace("  ", " ");
			}
			return tmp;
		}

		private boolean removeBrackets(char bracketType1, char bracketType2) {
			String source = cmbParentFolders.getEditor().getItem().toString();
			String withoutBrackets = source.replaceAll("^.*(\\"+bracketType1+".*\\"+bracketType2+").*$", "$1");
			boolean hasBrackets = withoutBrackets.contains(""+bracketType1) && withoutBrackets.contains(""+bracketType2);
			if (hasBrackets) {
				cmbParentFolders.getEditor().setItem(source.replace(withoutBrackets, "").trim().replaceAll("\\s+"," "));
			}
			return hasBrackets;
		}

		private void renameGame() {
			List<Game> currentGames = explorer.getCurrentGames();
			if (currentGames != null && !currentGames.isEmpty()) {
				Game game = explorer.getCurrentGames().get(0);
				if (game == null) {
					return;
				}
				String oldName = game.getName();
				String pathWithoutFileName = FilenameUtils.getPath(explorer.getFiles(game).get(0));
				String[] folderNames = pathWithoutFileName.split(getSeparatorBackslashed());
				List<String> reverseList = new ArrayList<>();
				reverseList.add(oldName);
				for (int i = folderNames.length-1; i >= 0; i--) {
					if (!folderNames[i].trim().isEmpty()) {
						reverseList.add(folderNames[i]);
					}
				}
				String lblEnterNewName = Messages.get(MessageConstants.ENTER_NEW_NAME);
				String[] arrReverseList = reverseList.toArray(new String[reverseList.size()]);
				cmbParentFolders = new JExtendedComboBox<Object>(arrReverseList);
				txtRenameFile.setEnabled(false);
				chkRenameFile.setOpaque(false);
				chkRenameFile.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						txtRenameFile.setEnabled(chkRenameFile.isSelected());
						UIUtil.revalidateAndRepaint(txtRenameFile.getParent());
					}
				});
				String toolTipParentFolders = Messages.get(MessageConstants.CHOOSE_NAME_FROM_PARENT_FOLDER);
				cmbParentFolders.setToolTipText(toolTipParentFolders);
				cmbParentFolders.setEditable(true);
				FormLayout layoutWrapper = new FormLayout("pref, $ugap, pref, min:grow, min",
						"min, $rgap, min, $rgap, min, $rgap, min");
				layoutWrapper.setRowGroup(1, 3, 5, 7);
				//			layoutWrapper.setRowGroup(1, 3, 5);
				CellConstraints cc = new CellConstraints();
				JPanel pnlWrapWrapper = new JPanel(new BorderLayout());
				TitledBorder titledBorder = new TitledBorder(null, Messages.get(MessageConstants.RENAMING_OPTIONS), 0, TitledBorder.TOP);
				JButton btn = new JButton();
				btn.setFocusPainted(false);
				btn.setContentAreaFilled(false);
				btn.setBorder(titledBorder);
				btn.add(pnlWrapWrapper);
				btn.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						super.mouseEntered(e);
						btn.setContentAreaFilled(true);
					}
					@Override
					public void mouseExited(MouseEvent e) {
						super.mouseExited(e);
						btn.setContentAreaFilled(false);
					}
				});
				btn.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						showMoreOptions = false;
					}
				});

				JPanel pnlWrapper = new JPanel(layoutWrapper);
				pnlWrapper.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						super.mouseEntered(e);
						btn.setContentAreaFilled(false);
					}
					@Override
					public void mouseExited(MouseEvent e) {
						super.mouseExited(e);
						btn.setContentAreaFilled(false);
					}
				});
				//			pnlWrapper.setBackground(ValidationComponentUtils.getMandatoryBackground());
				pnlWrapper.setBorder(Paddings.TABBED_DIALOG);
				JPanel pnlBrackets = new JPanel(new FlowLayout(FlowLayout.LEFT));
				pnlBrackets.add(lblBrackets);
				pnlBrackets.add(btnBracket1);
				pnlBrackets.add(lblOr);
				pnlBrackets.add(btnBracket2);
				pnlBrackets.add(lblBracketsExample);
				pnlWrapper.add(pnlBrackets, cc.xyw(1, 1, layoutWrapper.getColumnCount()-1));

				JPanel pnlSpaces = new JPanel(new FlowLayout(FlowLayout.LEFT));
				pnlSpaces.add(lblSpaces);
				pnlSpaces.add(btnSpacesDots);
				pnlSpaces.add(new JLabel(", "));
				pnlSpaces.add(btnSpacesUnderlines);
				pnlSpaces.add(lblOr2);
				pnlSpaces.add(btnSpacesHyphens);
				pnlSpaces.add(lblWithSpaces);
				pnlWrapper.add(pnlSpaces, cc.xyw(1, 3, layoutWrapper.getColumnCount()-1));

				JPanel pnlAutoCase = new JPanel(new FlowLayout(FlowLayout.LEFT));
				JPanel pnlCamelCase = new JPanel(new FlowLayout(FlowLayout.LEFT));
				//			pnlAutoCase.setBackground(ValidationComponentUtils.getMandatoryBackground());
				//			pnlCamelCase.setBackground(ValidationComponentUtils.getMandatoryBackground());

				pnlAutoCase.add(btnAutoSetLetterCase);
				pnlCamelCase.add(btnSpacesCamelCase);
				pnlWrapper.add(pnlAutoCase, cc.xyw(1, 5, layoutWrapper.getColumnCount()));
				pnlWrapper.add(pnlCamelCase, cc.xyw(1, 7, layoutWrapper.getColumnCount()));

				pnlWrapWrapper.add(pnlWrapper);

				//			btnBracket1.setBackground(Color.RED);
				//			btnBracket2.setBackground(Color.RED);
				//			btnSpacesDots.setBackground(Color.ORANGE);
				//			btnSpacesUnderlines.setBackground(Color.ORANGE);
				pnlBrackets.setBackground(ValidationComponentUtils.getErrorBackground());
				pnlSpaces.setBackground(ValidationComponentUtils.getWarningBackground());
				//			pnlAutoCase.setBackground(ValidationComponentUtils.getMandatoryBackground());
				//			pnlCamelCase.setBackground(ValidationComponentUtils.getMandatoryBackground());

				JButton btnMoreRenamingOptions = new JButton(Messages.get(MessageConstants.RENAMING_OPTIONS));
				int size = ScreenSizeUtil.is3k() ? 24 : 16;
				btnMoreRenamingOptions.setIcon(ImageUtil.getImageIconFrom(Icons.get("arrowDown", size, size)));
				btnMoreRenamingOptions.setHorizontalAlignment(SwingConstants.LEFT);
				UIUtil.doHover(false, btnMoreRenamingOptions);
				btnMoreRenamingOptions.addMouseListener(UIUtil.getMouseAdapter());
				btnMoreRenamingOptions.addFocusListener(UIUtil.getFocusAdapterKeepHoverWhenSelected());
				btnMoreRenamingOptions.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent evt) {
						Window w = SwingUtilities.getWindowAncestor(btnMoreRenamingOptions);
						if (w != null) {
							showMoreOptions = true;
							w.dispose();
						}
					}
				});
				Object[] message = {
						lblEnterNewName + "\n",
						cmbParentFolders,
						toolTipParentFolders,
						"\n",
						btnMoreRenamingOptions
				};
				Object[] messageEnlarged = {
						lblEnterNewName + "\n",
						cmbParentFolders,
						toolTipParentFolders,
						"\n",
						btn/*,
						"\n",
						chkRenameFile,
						txtRenameFile*/
				};
				cmbParentFolders.addAncestorListener(new RequestFocusListener());
				cmbParentFolders.getEditor().selectAll();

				int resp = JOptionPane.CANCEL_OPTION;
				if (!showMoreOptions) {
					resp = JOptionPane.showConfirmDialog(view, message, Messages.get(MessageConstants.RENAME_GAME),
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (resp == JOptionPane.CANCEL_OPTION) {
						return;
					}
				}
				if (resp != JOptionPane.OK_OPTION) {
					if (showMoreOptions) {
						resp = JOptionPane.showConfirmDialog(view, messageEnlarged, Messages.get(MessageConstants.RENAME_GAME),
								JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
					}
				}
				if (resp == JOptionPane.OK_OPTION) {
					String newName = cmbParentFolders.getEditor().getItem().toString();
					if (!oldName.equals(newName)) {
						explorer.renameGame(game.getId(), newName);
						try {
							explorerDAO.renameGame(game.getId(), newName);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						view.gameRenamed(new BroGameRenamedEvent(game, newName));
						// it makes no sense make use of the advanced renaming feature
						// when there are no other games in the list
						if (explorer.getGameCount() > 1) {
							final String oldNameDef = oldName;
							final String newNameDef = newName;
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									boolean brackets1 = false;
									boolean brackets2 = false;
									boolean dots = false;
									boolean underlines = false;
									String regexBracket1 = "^(.*)\\(.*\\)(.*)$";
									String regexBracket2 = "^(.*)\\[.*\\](.*)$";
									String regexDots = "^.*(\\.+).*$";
									String regexUnderlines = "^.*(\\_+).*$";
									String tempOldName = oldNameDef;
									String source;
									List<String> bracketsList1 = new ArrayList<>();
									List<String> bracketsList2 = new ArrayList<>();

									do {
										source = getBrackets(tempOldName, '(', ')');
										if (source != null && !source.isEmpty()) {
											tempOldName = tempOldName.replace(source, "").trim();
											bracketsList1.add(source);
										}
									} while (source != null && !source.isEmpty());

									do {
										source = getBrackets(tempOldName, '[', ']');
										if (source != null && !source.isEmpty()) {
											tempOldName = tempOldName.replace(source, "").trim();
											bracketsList2.add(source);
										}
									} while (source != null && !source.isEmpty());

									if (oldNameDef.matches(regexBracket1)) {
										if (!newNameDef.matches(regexBracket1)) {
											brackets1 = true;
										} else {
											int countOld = StringUtils.countMatches(oldNameDef, "(");
											int countNew = StringUtils.countMatches(newNameDef, "(");
											if (countOld > countNew) {
												brackets1 = true;
											}
										}
									}
									if (oldNameDef.matches(regexBracket2)) {
										if (!newNameDef.matches(regexBracket2)) {
											brackets2 = true;
										} else {
											int countOld = StringUtils.countMatches(oldNameDef, "[");
											int countNew = StringUtils.countMatches(newNameDef, "[");
											if (countOld > countNew) {
												brackets2 = true;
											}
										}
									}
									if (oldNameDef.matches(regexDots) && !newNameDef.matches(regexDots)) {
										dots = true;
									}
									if (oldNameDef.matches(regexUnderlines) && !newNameDef.matches(regexUnderlines)) {
										underlines = true;
									}
									if (brackets1 || brackets2 || dots || underlines) {
										chkDots.setVisible(dots);
										chkUnderlines.setVisible(underlines);
										chkDots.setSelected(dots);
										chkUnderlines.setSelected(underlines);
										JCheckBox chkNeverShowThisAgain = new JCheckBox(Messages.get(MessageConstants.RENAME_WITHOUT_ASK));
										String msg = Messages.get(MessageConstants.RENAME_OTHER_GAMES)+"\n";
										List<Object> messageList = new ArrayList<>();
										messageList.add(msg);
										List<JCheckBox> dynamicCheckBoxes = new ArrayList<>();
										JCheckBox chkBrackets = new JCheckBox(Messages.get(MessageConstants.REMOVE_BRACKETS));
										chkBrackets.setSelected(true);
										messageList.add(chkBrackets);
										for (String brack : bracketsList1) {
											JCheckBox chk = new JCheckBox(brack);
											dynamicCheckBoxes.add(chk);
											chk.setSelected(true);
											messageList.add(chk);
										}
										for (String brack : bracketsList2) {
											JCheckBox chk = new JCheckBox(brack);
											dynamicCheckBoxes.add(chk);
											chk.setSelected(true);
											messageList.add(chk);
										}
										// this has been done for putting a line wrap only when the brackets checkboxes were added
										//									if (messageList.size() > 1) {
										//										if (dots || underlines) {
										//											JLabel lineWrap = new JLabel(" ");
										//											messageList.add(lineWrap);
										//										}
										//									}
										messageList.add(chkDots);
										messageList.add(chkUnderlines);
										messageList.add(new JLabel(" "));
										messageList.add(chkNeverShowThisAgain);
										Object[] stockArr = new Object[messageList.size()];
										stockArr = messageList.toArray(stockArr);
										String title = Messages.get(MessageConstants.SHOW_RENAME_GAMES_DIALOG);
										int request = JOptionPane.showConfirmDialog(view, stockArr, title, JOptionPane.YES_NO_OPTION);
										if (request == JOptionPane.YES_OPTION) {
											dots = chkDots.isSelected();
											underlines = chkUnderlines.isSelected();
											showRenameGamesDialog(dynamicCheckBoxes, dots, underlines);
										}
									}
								}
							});
						}
					}
				}
			}
		}

		private String getBrackets(String string, char bracketType1, char bracketType2) {
			String withoutBrackets = string.replaceAll("^.*(\\"+bracketType1+".*\\"+bracketType2+").*$", "$1");
			boolean hasBrackets = withoutBrackets.contains(""+bracketType1) && withoutBrackets.contains(""+bracketType2);
			if (hasBrackets) {
				return withoutBrackets;
			}
			return null;
		}

		protected void showRenameGamesDialog(List<JCheckBox> dynamicCheckBoxes, boolean dots, boolean underlines) {
			JDialog dlg = new JDialog();
			dlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dlg.setModalityType(ModalityType.APPLICATION_MODAL);
			dlg.setTitle("Rename games");
			FormLayout layout = new FormLayout("min:grow, $rgap, min:grow",
					"fill:default, $rgap, fill:default:grow, $rgap, fill:default");
			CellConstraints cc = new CellConstraints();
			JPanel pnl = new JPanel();
			pnl.setLayout(layout);
			pnl.setBorder(Paddings.DIALOG);
			lstMatches = new JList<>();
			lstPreviews = new JList<>();
			listener = new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (listener2 != null) {
						lstPreviews.removeListSelectionListener(listener2);
					}
					lstPreviews.setSelectedIndex(lstMatches.getSelectedIndex());
					if (listener2 != null) {
						lstPreviews.addListSelectionListener(listener2);
					}
					lstPreviews.repaint();
				}
			};
			listener2 = new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (listener != null) {
						lstMatches.removeListSelectionListener(listener);
					}
					lstMatches.setSelectedIndex(lstPreviews.getSelectedIndex());
					if (listener != null) {
						lstMatches.addListSelectionListener(listener);
					}
					lstMatches.repaint();
				}
			};
			lstMatches.addListSelectionListener(listener);
			lstPreviews.addListSelectionListener(listener2);


			JPanel pnlOptions = new JPanel();
			FormLayout layoutWrapper = new FormLayout("pref, $ugap, pref, min:grow, min",
					"min, $rgap, min, $rgap, min, $rgap, min");
			layoutWrapper.setRowGroup(1, 3, 5, 7);
			//			layoutWrapper.setRowGroup(1, 3, 5);
			CellConstraints cc2 = new CellConstraints();
			JPanel pnlWrapWrapper = new JPanel(new BorderLayout());
			JPanel pnlWrapper = new JPanel(layoutWrapper);
			JPanel pnlBrackets = new JPanel(new FlowLayout(FlowLayout.LEFT));
			pnlBrackets.add(lblBrackets);
			pnlBrackets.add(btnBracket1);
			pnlBrackets.add(lblOr);
			pnlBrackets.add(btnBracket2);
			pnlBrackets.add(lblBracketsExample);
			pnlWrapper.add(pnlBrackets, cc2.xyw(1, 1, layoutWrapper.getColumnCount()-1));

			JPanel pnlSpaces = new JPanel(new FlowLayout(FlowLayout.LEFT));
			pnlSpaces.add(lblSpaces);
			pnlSpaces.add(btnSpacesDots);
			pnlSpaces.add(new JLabel(", "));
			pnlSpaces.add(btnSpacesUnderlines);
			pnlSpaces.add(lblOr2);
			pnlSpaces.add(btnSpacesHyphens);
			pnlSpaces.add(lblWithSpaces);
			pnlWrapper.add(pnlSpaces, cc2.xyw(1, 3, layoutWrapper.getColumnCount()-1));

			JPanel pnlAutoCase = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JPanel pnlCamelCase = new JPanel(new FlowLayout(FlowLayout.LEFT));
			//			pnlAutoCase.setBackground(ValidationComponentUtils.getMandatoryBackground());
			//			pnlCamelCase.setBackground(ValidationComponentUtils.getMandatoryBackground());

			pnlAutoCase.add(btnAutoSetLetterCase);
			pnlCamelCase.add(btnSpacesCamelCase);
			pnlWrapper.add(pnlAutoCase, cc2.xyw(1, 5, layoutWrapper.getColumnCount()));
			pnlWrapper.add(pnlCamelCase, cc2.xyw(1, 7, layoutWrapper.getColumnCount()));

			pnlWrapWrapper.add(pnlWrapper);
			pnlOptions.add(pnlWrapWrapper);


			JScrollPane spMatches = new JScrollPane(lstMatches);
			JScrollPane spPreview = new JScrollPane(lstPreviews);
			spMatches.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

				@Override
				public void adjustmentValueChanged(AdjustmentEvent e) {
					spPreview.getVerticalScrollBar().setValue(e.getValue());
				}
			});
			spPreview.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

				@Override
				public void adjustmentValueChanged(AdjustmentEvent e) {
					spMatches.getVerticalScrollBar().setValue(e.getValue());
				}
			});
			listener3 = new AdjustmentListener() {

				@Override
				public void adjustmentValueChanged(AdjustmentEvent e) {
					spMatches.getHorizontalScrollBar().removeAdjustmentListener(listener4);
					spPreview.getHorizontalScrollBar().setValue(e.getValue());
					spPreview.getHorizontalScrollBar().addAdjustmentListener(listener4);
				}
			};
			spMatches.getHorizontalScrollBar().addAdjustmentListener(listener3);
			listener4 = new AdjustmentListener() {

				@Override
				public void adjustmentValueChanged(AdjustmentEvent e) {
					spMatches.getHorizontalScrollBar().removeAdjustmentListener(listener3);
					spMatches.getHorizontalScrollBar().setValue(e.getValue());
					spMatches.getHorizontalScrollBar().addAdjustmentListener(listener3);
				}
			};
			spPreview.getHorizontalScrollBar().addAdjustmentListener(listener4);
			JButton btnRenameGames = new JButton("rename now");
			btnRenameGames.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					for (int i = 0; i < lstMatches.getModel().getSize(); i++) {
						Game g = lstMatches.getModel().getElementAt(i);
						String newName = lstPreviews.getModel().getElementAt(i);
						explorer.renameGame(g.getId(), newName);
						try {
							explorerDAO.renameGame(g.getId(), newName);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					dlg.dispose();
				}
			});
			pnl.add(pnlOptions, cc.xyw(1, 1, layout.getColumnCount()));
			pnl.add(spMatches, cc.xy(1, 3));
			pnl.add(spPreview, cc.xy(3, 3));
			pnl.add(btnRenameGames, cc.xyw(1, 5, layout.getColumnCount()));
			dlg.add(pnl);
			checkForRenamingGames(dynamicCheckBoxes, dots, underlines);
			dlg.pack();
			dlg.setLocationRelativeTo(view);
			dlg.setVisible(true);
		}

		private void checkForRenamingGames(List<JCheckBox> dynamicCheckBoxes, boolean dots, boolean underlines) {
			DefaultListModel<Game> mdlLstMatches = new DefaultListModel<>();
			DefaultListModel<String> mdlLstPreviews = new DefaultListModel<>();
			for (Game g : explorer.getGames()) {
				String gameName = g.getName();
				for (JCheckBox chk : dynamicCheckBoxes) {
					if (chk.isSelected()) {
						if (g.getName().toLowerCase().contains(chk.getText().trim().replaceAll("\\s+"," ").toLowerCase())) {
							if (!mdlLstMatches.contains(g)) {
								mdlLstMatches.addElement(g);
							}
							gameName = gameName.replace(chk.getText(), "").trim().replaceAll("\\s+"," ");
						}
					}
				}
				if (dots && gameName.contains(".")) {
					if (!mdlLstMatches.contains(g)) {
						mdlLstMatches.addElement(g);
					}
					gameName = gameName.replace(".", " ").trim().replaceAll("\\s+"," ");
				}
				if (underlines && gameName.contains("_")) {
					if (!mdlLstMatches.contains(g)) {
						mdlLstMatches.addElement(g);
					}
					gameName = gameName.replace("_", " ").trim().replaceAll("\\s+"," ");
				}
				if (mdlLstMatches.contains(g)) {
					mdlLstPreviews.addElement(gameName);
				}
			}
			lstMatches.setModel(mdlLstMatches);
			lstPreviews.setModel(mdlLstPreviews);
		}

		@Override
		public void setEnabled(boolean b) {
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public Object getValue(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void putValue(String arg0, Object arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isEnabled() {
			renameGame();
			return false;
		}
	}

	class RemoveGameListener implements Action {
		@Override
		public boolean isEnabled() {
			removeSelectedGames();
			return false;
		}

		private void removeSelectedGames() {
			List<Game> currentGames = explorer.getCurrentGames();
			int removeAll = JOptionPane.CANCEL_OPTION;
			if (currentGames.size() > 1) {
				if (view.getViewManager().isFilterFavoriteActive()) {
					String[] buttons = { Messages.get(MessageConstants.REMOVE_GAMES), Messages.get(MessageConstants.REMOVE_GAMES_FROM_FAVORITES) };
					String message = Messages.get(MessageConstants.REMOVE_OR_UNFAVORITE_GAMES, currentGames.size());
					String title = "Remove or unfavorite games";
					removeAll = JOptionPane.showOptionDialog(view, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, buttons, buttons[1]);
					if (removeAll != JOptionPane.YES_OPTION && removeAll != JOptionPane.NO_OPTION) {
						return;
					}
				} else {
					removeAll = JOptionPane.showConfirmDialog(view, "Are you sure you want to remove the selected " + currentGames.size() + " games?",
							Messages.get(MessageConstants.REMOVE_GAMES), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (removeAll != JOptionPane.YES_OPTION) {
						return;
					}
				}
			}
			for (Game game : currentGames) {
				String gameName = "<html><strong>"+game.getName()+"</strong></html>";
				boolean doRemoveGame = false;
				if (view.getViewManager().isFilterFavoriteActive()) {
					int request2;
					if (removeAll == JOptionPane.CANCEL_OPTION) {
						String[] buttons = { Messages.get(MessageConstants.REMOVE_GAME), Messages.get(MessageConstants.REMOVE_GAME_FROM_FAVORITES) };
						String message = Messages.get(MessageConstants.CONFIRM_REMOVE_OR_UNFAVORITE_GAME, gameName,
								explorer.getPlatform(game.getPlatformId()).getName());
						String title = "Remove game or unfavorite";
						request2 = JOptionPane.showOptionDialog(view, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, buttons, buttons[1]);
					} else {
						request2 = removeAll;
					}
					if (request2 == JOptionPane.NO_OPTION) {
						game.setRate(0);
						rateGame(game);
						continue;
					}
					doRemoveGame = (request2 == JOptionPane.YES_OPTION);
				} else {
					if (removeAll == JOptionPane.CANCEL_OPTION) {
						int request = JOptionPane.showConfirmDialog(view,
								Messages.get(MessageConstants.CONFIRM_REMOVE_GAME, gameName,
										explorer.getPlatform(game.getPlatformId()).getName()),
								Messages.get(MessageConstants.REMOVE_GAME), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						doRemoveGame = (request == JOptionPane.YES_OPTION);
					} else {
						doRemoveGame = true;
					}
				}
				if (doRemoveGame) {
					int gameId = game.getId();
					explorer.removeGame(game);
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							view.gameRemoved(new BroGameRemovedEvent(game, explorer.getGameCount()));
						}
					});
					try {
						explorerDAO.removeGame(gameId);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		}

		@Override
		public void setEnabled(boolean b) {
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			removeSelectedGames();
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener l) {
		}

		@Override
		public Object getValue(String s) {
			return null;
		}

		@Override
		public void putValue(String s, Object o) {
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener l) {
		}
	}

	class AddPlatformListener implements Action {
		@Override
		public boolean isEnabled() {
			// firePlatformAddedEvent(platform);
			return false;
		}

		@Override
		public void setEnabled(boolean b) {
		}

		@Override
		public void actionPerformed(ActionEvent e) {
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener l) {
		}

		@Override
		public Object getValue(String s) {
			return null;
		}

		@Override
		public void putValue(String s, Object o) {
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener l) {
		}
	}

	class RemovePlatformListener implements Action {
		@Override
		public boolean isEnabled() {
			// firePlatformRemovedEvent(element);
			return false;
		}

		@Override
		public void setEnabled(boolean b) {
		}

		@Override
		public void actionPerformed(ActionEvent e) {
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener l) {
		}

		@Override
		public Object getValue(String s) {
			return null;
		}

		@Override
		public void putValue(String s, Object o) {
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener l) {

		}
	}

	class AddEmulatorListener implements Action {
		@Override
		public boolean isEnabled() {
			// fireEmulatorAddedEvent(emulator);
			return false;
		}

		@Override
		public void setEnabled(boolean b) {
		}

		@Override
		public void actionPerformed(ActionEvent e) {
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener l) {
		}

		@Override
		public Object getValue(String s) {
			return null;
		}

		@Override
		public void putValue(String s, Object o) {
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener l) {
		}
	}

	class RemoveEmulatorListener implements Action, ActionListener {
		@Override
		public boolean isEnabled() {
			Emulator selectedEmulator = frameProperties.getSelectedEmulator();
			if (selectedEmulator != null) {
				removeEmulator(frameProperties.getSelectedPlatform(), selectedEmulator);
			}
			return false;
		}

		@Override
		public void setEnabled(boolean b) {
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			removeEmulator(frameProperties.getSelectedPlatform(), frameProperties.getSelectedEmulator());
		}

		private void removeEmulator(Platform platform, Emulator emulator) {
			int request = JOptionPane.showConfirmDialog(frameProperties,
					Messages.get(MessageConstants.CONFIRM_REMOVE_EMULATOR, emulator.getName(),
							""),
					Messages.get(MessageConstants.REMOVE_EMULATOR), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (request == JOptionPane.YES_OPTION) {
				System.out.println("remove emulator with id: "+emulator.getId());
				try {
					explorerDAO.removeEmulator(emulator.getId());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fireEmulatorRemovedEvent(platform, emulator);
			}
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener l) {
		}

		@Override
		public Object getValue(String s) {
			return null;
		}

		@Override
		public void putValue(String s, Object o) {
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener l) {
		}
	}

	class OpenEmulatorPanelListener implements ActionListener, MouseListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			frameProperties.showEmulatorPropertiesPanel(true);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// lastSelectedIndex = lstGames.getSelectedIndex();

			if (e.getSource() instanceof JTable) {
				e.getSource();
				if (e.getClickCount() == 2) {
					frameProperties.showEmulatorPropertiesPanel(true);
				}
			}
			//			if (e.getSource() instanceof JToggleButton) {
			//				if (e.getClickCount() == 2) {
			//					if (e.getModifiersEx() == InputEvent.ALT_DOWN_MASK) {
			//						openGamePropertiesFrame();
			//						return;
			//					}
			//					// int index = lstGames.locationToIndex(e.getPoint());
			//					runGame();
			//				}
			//			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}
	}

	class ShowNavigationPaneListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals(GameViewConstants.SHOW_NAVIGATION_PANE)) {
				view.showNavigationPane(true);
			} else if (e.getActionCommand().equals(GameViewConstants.HIDE_NAVIGATION_PANE)) {
				view.showNavigationPane(false);
			}
		}
	}

	class ShowPreviewPaneListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals(GameViewConstants.SHOW_PREVIEW_PANE)) {
				view.showPreviewPane(true);
			} else if (e.getActionCommand().equals(GameViewConstants.HIDE_PREVIEW_PANE)) {
				view.showPreviewPane(false);
			}
		}
	}

	class ShowGameDetailsListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			view.showGameDetailsPane(!view.isDetailsPaneVisible());
		}
	}

	class SaveAndExitConfigurationListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			frameProperties.dispose();
		}
	}

	public void autoSearchTags(List<Game> games, boolean showFeedback) {
		for (Game game : games) {
			Platform platform = explorer.getPlatform(game.getPlatformId());
			String gameName = game.getName();

			NodeList nList = getNodeList(platform, false);
			if (nList == null) {
				if (showFeedback) {
					UIUtil.showErrorMessage(view, "You have currently no taglist installed for platform: "+ platform.getName(), "no tags found");
				}
				continue;
			}
			Map<String, List<String>> mapTagsToAdd = new HashMap<>();
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String gameNameToCheck = eElement.getAttribute("name");
					if (gameName.trim().toLowerCase().contains(gameNameToCheck.trim().toLowerCase())) {
						NodeList node = eElement.getElementsByTagName("tag");
						List<String> tagsToAdd = new ArrayList<>();
						for (int i = 0; i < node.getLength(); i++) {
							Node nodeItem = node.item(i);
							tagsToAdd.add(nodeItem.getTextContent());
						}
						if (!tagsToAdd.isEmpty()) {
							mapTagsToAdd.put(gameNameToCheck, tagsToAdd);
						}
					}
				}
			}
			if (mapTagsToAdd.isEmpty()) {
				if (showFeedback) {
					UIUtil.showErrorMessage(view, "No tags found to add for this game", "no tags found");
				}
			} else {
				List<String> tagsAdded = new ArrayList<>();
				Set<String> keySet = mapTagsToAdd.keySet();
				String longestString = "";
				Iterator<String> it = keySet.iterator();
				while (it.hasNext()) {
					String tagName = it.next();
					if (tagName.length() > longestString.length()) {
						longestString = tagName;
					}
				}
				for (String tagName : mapTagsToAdd.get(longestString)) {
					Tag tag = addOrGetTag(new BroTag(-1, tagName, "#4286f4"));
					if (!game.hasTag(tag.getId())) {
						tagsAdded.add(tag.getName());
						explorer.addTagForGame(game.getId(), tag);
						game.addTag(tag);
						if (explorer.getCurrentGames().contains(game)) {
							TagEvent tagTagAddedEvent = new BroTagAddedEvent(tag);
							view.tagAdded(tagTagAddedEvent);
						}
						try {
							explorerDAO.addTag(game.getId(), tag);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				if (!tagsAdded.isEmpty()) {
					String tagsString = "";
					for (String s : tagsAdded) {
						tagsString += "\n- "+s;
					}
					if (showFeedback) {
						Object[] message = {
								"The following tags have been added from game " + longestString + ":"+ tagsString
						};
						JOptionPane.showMessageDialog(view, message, "Tags added", JOptionPane.INFORMATION_MESSAGE);
					}
				} else {
					if (showFeedback) {
						UIUtil.showInformationMessage(view, "You already have set all the tags from this source to the game", "no tags added");
					}
				}
			}
		}
		gameTagListFiles.clear();
		view.updateFilter();
	}

	private NodeList getNodeList(Platform platform, boolean updateFromFile) {
		if (!updateFromFile) {
			if (gameTagListFiles.containsKey(platform)) {
				return gameTagListFiles.get(platform);
			}
		}
		String platformShortName = platform.getShortName();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			File fXmlFile = new File(System.getProperty("user.home") + File.separator + ".emubro"
					+ File.separator + "games/"+platformShortName+".xml");
			if (fXmlFile != null && fXmlFile.exists()) {
				Document doc;
				try {
					doc = dBuilder.parse(fXmlFile);
					doc.getDocumentElement().normalize();
					NodeList nList = doc.getElementsByTagName("game");
					gameTagListFiles.put(platform, nList);
					return nList;
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				return null;
			}
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	public void openWebsite(String url) throws IOException, URISyntaxException {
		Desktop.getDesktop().browse(new URI(url));
	}

	public void quickSearch() {
		List<String> gameDirectories = new ArrayList<>();
		//		List<String> emulatorDirectories = new ArrayList<>();
		test(gameDirectories);
		//		test2(emulatorDirectories);
		List<String> allCommonDirectories = new ArrayList<>();
		test3(allCommonDirectories);
		Collections.sort(allCommonDirectories);
		Collections.reverse(allCommonDirectories);
		System.out.println(allCommonDirectories);
		searchForPlatformsString(allCommonDirectories);
	}

	private void test(List<String> gameDirectories) {
		for (Game g : explorer.getGames()) {
			String fullPath = FilenameUtils.getFullPath(explorer.getFiles(g).get(0));
			if (!gameDirectories.contains(fullPath)
					&& explorer.getPlatform(g.getPlatformId()).isAutoSearchEnabled()) {
				if (fullPath.startsWith("D:")) {
					gameDirectories.add(fullPath);
				}
			}
		}
	}

	private void test2(List<String> emulatorDirectories) {
		for (Platform p : explorer.getPlatforms()) {
			for (Emulator emu : p.getEmulators()) {
				if (emu.isInstalled()) {
					String fullPath = FilenameUtils.getFullPath(emu.getPath());
					emulatorDirectories.add(fullPath);
				}
			}
		}
	}

	private void test3(List<String> allCommonDirectories) {
		for (Platform p : explorer.getPlatforms()) {
			List<String> commonDirs;
			System.out.println(p.getName() + " " + (commonDirs = getCommonDirectories(p.getId())));
			for (String dir : commonDirs) {
				if (!allCommonDirectories.contains(dir)) {
					allCommonDirectories.add(dir);
				}
			}
		}
	}

	private List<String> getCommonDirectories(int platformId) {
		List<String> directories = explorer.getGameDirectoriesFromPlatform(platformId);
		System.err.println(explorer.getPlatform(platformId).getName() + " " + directories);
		List<String> commonDirectories = new ArrayList<>();
		for (String dir : directories) {
			if (commonDirectories.isEmpty()) {
				commonDirectories.add(dir);
				continue;
			}
			boolean removed = false;
			for (int i = commonDirectories.size()-1; i >= 0; i--) {
				boolean rootFolder = dir.split(getSeparatorBackslashed()).length <= 1;
				if (!rootFolder) {
					String parentDirs = dir;
					do {
						if (commonDirectories.get(i).startsWith(parentDirs)) {
							commonDirectories.set(i, parentDirs);
							parentDirs = "";
							removed = true;
						}
					}
					while (!(parentDirs = getParentFolderFromString(parentDirs)).isEmpty()
							&& parentDirs.split(getSeparatorBackslashed()).length > 1);
				}
			}
			if (!removed) {
				if (!commonDirectories.contains(dir)) {
					commonDirectories.add(dir);
				}
			}
		}
		return commonDirectories;
	}

	private String getParentFolderFromString(String dir) {
		return FileUtil.getParentDirPath(dir);
		//		String[] dirArr = dir.split(getSeparatorBackslashed());
		//		String bla = "";
		//		for (int i = 0; i < dirArr.length-1; i++) {
		//			bla += dirArr[i] + File.separator;
		//		}
		//		return bla;
	}

	public void sortGameList(int sortOrder) {
		view.sortOrder(sortOrder);
	}

	public void sortBy(int sortBy, PlatformComparator platformComparator) {
		view.sortBy(sortBy, platformComparator);
	}

	public void groupBy(int groupBy) {
		switch (groupBy) {
		case ViewConstants.GROUP_BY_PLATFORM:
			view.groupByPlatform();
			break;
		case ViewConstants.GROUP_BY_TITLE:
			view.groupByTitle();
			break;
		case ViewConstants.GROUP_BY_NONE:
			view.groupByNone();
			break;
		}
	}

	public Platform addOrGetPlatform(Platform platform) {
		Platform p2 = null;
		if (platform != null) {
			if (!explorer.hasPlatform(platform.getName())) {
				try {
					platform.setDefaultEmulatorId(EmulatorConstants.NO_EMULATOR);
					explorerDAO.addPlatform(platform);
					p2 = explorerDAO.getPlatform(explorerDAO.getLastAddedPlatformId());
					p2.setId(explorerDAO.getLastAddedPlatformId());
					explorer.addPlatform(p2);
					firePlatformAddedEvent(p2);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				p2 = explorer.getPlatform(platform.getName());
			}
		}
		return p2;
	}

	public Tag addOrGetTag(Tag tag) {
		Tag tag2 = null;
		if (tag != null) {
			if (!explorer.hasTag(tag.getName())) {
				try {
					explorerDAO.addTag(tag);
					tag2 = explorerDAO.getTag(explorerDAO.getLastAddedTagId());
					tag2.setId(explorerDAO.getLastAddedTagId());
					explorer.addTag(tag2);
					fireTagAddedEvent(tag2);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				tag2 = explorer.getTag(tag.getName());
			}
		}
		return tag2;
	}

	private void fireTagAddedEvent(Tag tag) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				TagEvent event = new BroTagAddedEvent(tag);
				for (TagListener l : tagListeners) {
					l.tagAdded(event);
				}
			}
		});
	}

	private void discardConfigurationChanges() {
	}

	class OpenGamePropertiesListener implements ActionListener, Action {
		private Map<String, Object> map = new HashMap<>();

		@Override
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					openGamePropertiesFrame();
				}
			});
		}

		@Override
		public Object getValue(String key) {
			return map.get(key);
		}

		@Override
		public boolean isEnabled() {
			openGamePropertiesFrame();
			return false;
		}

		@Override
		public void putValue(String key, Object value) {
			map.put(key, value);
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
		}

		@Override
		public void setEnabled(boolean b) {
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			// TODO Auto-generated method stub

		}
	}
	class AddFilesListener implements ActionListener {
		private JFileChooser fc = new JFileChooser();

		@Override
		public void actionPerformed(ActionEvent e) {
			showFileChooser(JFileChooser.FILES_AND_DIRECTORIES);
		}

		private void showFileChooser(int filesAndDirectories) {
			showFileChooser(filesAndDirectories, new File(System.getProperty("user.dir")));
		}

		private void showFileChooser(int filesAndDirectories, File dir) {
			fc.setCurrentDirectory(dir);
			fc.setDialogType(JFileChooser.OPEN_DIALOG);
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			fc.setMultiSelectionEnabled(true);
			int returnVal = fc.showOpenDialog(view);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File potentialGame = fc.getSelectedFile();
				if (!potentialGame.exists()) {
					showFileChooser(filesAndDirectories, fc.getCurrentDirectory());
					return;
				}
				if (potentialGame.isDirectory()) {
					showFileChooser(filesAndDirectories, potentialGame);
					return;
				}
				System.out.println("You want to open this file: " +
						potentialGame.getAbsolutePath());
				try {
					checkAddGame(potentialGame);
				} catch (ZipException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (RarException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	class AddFoldersListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogType(JFileChooser.OPEN_DIALOG);
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.setMultiSelectionEnabled(true);
			int returnVal = fc.showOpenDialog(view);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File potentialGameFolder = fc.getSelectedFile();
				System.out.println("You want to open this folder: " +
						potentialGameFolder.getAbsolutePath());
				List<File> tmpList = new ArrayList<>();
				tmpList.add(potentialGameFolder);
				searchForPlatforms(tmpList);
			}
		}
	}

	class AddGameOrEmulatorFromClipboardListener implements ActionListener, Action {

		@Override
		public Object getValue(String key) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void putValue(String key, Object value) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setEnabled(boolean b) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isEnabled() {
			pasteGameFromClipboard();
			return false;
		}

		private void pasteGameFromClipboard() {
			try {
				List<File> data = (List<File>) Toolkit.getDefaultToolkit()
						.getSystemClipboard().getData(DataFlavor.javaFileListFlavor);
				System.err.println("clipboard data: " + data);
				int request = JOptionPane.YES_OPTION;
				if (data.size() > 1) {
					request = JOptionPane.showConfirmDialog(view, Messages.get(MessageConstants.CLIPBOARD_ADD_MULTIPLE_FILES, Messages.get(MessageConstants.APPLICATION_TITLE), data.size()),
							"", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (request == JOptionPane.YES_OPTION) {
						checkAddGames(data);
					}
				} else if (data.size() == 1) {
					try {
						File file = data.get(0);
						if (file.isDirectory()) {
							checkAddGames(data);
						} else {
							checkAddGame(file);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (RarException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			} catch (UnsupportedFlavorException e1) {
				JOptionPane.showMessageDialog(view, Messages.get(MessageConstants.ERR_CLIPBOARD,
						Messages.get(MessageConstants.APPLICATION_TITLE)));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			// TODO Auto-generated method stub

		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
			// TODO Auto-generated method stub

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			pasteGameFromClipboard();
		}
	}

	class IncreaseFontListener implements Action, KeyListener, MouseWheelListener {
		private Map<String, Object> map = new HashMap<>();

		@Override
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					increaseFontSize();
				}
			});
		}

		@Override
		public Object getValue(String key) {
			return map.get(key);
		}

		@Override
		public boolean isEnabled() {
			increaseFontSize();
			return false;
		}

		@Override
		public void putValue(String key, Object value) {
			map.put(key, value);
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
		}

		@Override
		public void setEnabled(boolean b) {
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			if (keyCode == KeyEvent.VK_CONTROL) {
				System.err.println("control pressed");
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (e.isControlDown()) {
				if (e.getWheelRotation() < 0) {
					increaseFontSize();
				} else {
					decreaseFontSize();
				}
			}
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			// TODO Auto-generated method stub

		}
	}

	class DecreaseFontListener implements Action {
		private Map<String, Object> map = new HashMap<>();

		@Override
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					decreaseFontSize();
				}
			});
		}

		@Override
		public Object getValue(String key) {
			return map.get(key);
		}

		@Override
		public boolean isEnabled() {
			decreaseFontSize();
			return false;
		}

		@Override
		public void putValue(String key, Object value) {
			map.put(key, value);
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
		}

		@Override
		public void setEnabled(boolean b) {
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			// TODO Auto-generated method stub

		}
	}

	class OpenGameFolderListener implements ActionListener, MouseListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			doAction();
		}

		private void doAction() {
			List<Game> currentGames = explorer.getCurrentGames();
			for (Game game : currentGames) {
				String path = explorer.getFiles(game).get(0);
				System.err.println(path);
				path = path.replace("\\", "\\\\");
				String[] path2 = path.split(
						File.separator.equals("\\") ? "\\\\": "/"); // FIXME Exception in thread "AWT-EventQueue-0" java.util.regex.PatternSyntaxException: Unexpected internal error			near index 1

				String path3 = "";
				for (int i = 0; i < path2.length-1; i++) {
					path3 += path2[i] + "" + File.separator;
				}

				try {
					if (ValidationUtil.isWindows()) {
						new ProcessBuilder("explorer.exe", "/select,",
								path.replace("\\\\", "\\")).start();
					} else if (ValidationUtil.isUnix()) {
						ProcessBuilder builder = new ProcessBuilder("xdg-open", path3);
						builder.start();
					} else if (ValidationUtil.isMac()) {

					} else if (ValidationUtil.isSolaris()) {

					}
				} catch (IOException e1) {
					if (Desktop.isDesktopSupported()) {
						try {
							Desktop.getDesktop().open(new File(path3));
						} catch (IOException e2) {
							e2.printStackTrace();
						}
					}
					e1.printStackTrace();
				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			doAction();
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
	}

	class ShowOrganizeContextMenuListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			view.showOrganizePopupMenu(e);
		}
	}

	class ShowContextMenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			List<BroEmulator> emulators = null;
			List<Game> currentGame = explorer.getCurrentGames();
			int platformId = currentGame.get(0).getPlatformId();
			Platform platform = explorer.getPlatform(platformId);
			emulators = platform.getEmulators();
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
			view.showGameSettingsPopupMenu(emulators, defaultEmulatorIndex);
		}
	}

	class ExitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			checkAndExit();
		}
	}

	class BroComponentListener extends ComponentAdapter {

		@Override
		public void componentResized(ComponentEvent e) {
			view.showHidePanels();
		}
	}

	class OpenPropertiesListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			showPropertiesFrame();
		}
	}

	public void initializePlatforms(List<Platform> list) {
		for (Platform p : list) {
			mdlPropertiesLstPlatforms.add(p);
			if (!platformIcons.containsKey(p.getIconFileName())) {
				String iconFilename = p.getIconFileName();
				if (iconFilename != null && !iconFilename.trim().isEmpty()) {
					ImageIcon icon = ImageUtil.getImageIconFrom("/images/platforms/logos/" + iconFilename);
					if (icon != null) {
						int size = ScreenSizeUtil.adjustValueToResolution(24);
						icon = ImageUtil.scaleCover(icon, size, CoverConstants.SCALE_WIDTH_OPTION);
					}
					platformIcons.put(p.getIconFileName(), icon);
				}
			}
			for (Emulator emu : p.getEmulators()) {
				if (!emulatorIcons.containsKey(emu.getIconFilename())) {
					ImageIcon icon = ImageUtil.getImageIconFrom("/images/emulators/"
							+ emu.getIconFilename());
					if (icon != null) {
						int size = ScreenSizeUtil.adjustValueToResolution(24);
						icon = ImageUtil.scaleCover(icon, size, CoverConstants.SCALE_WIDTH_OPTION);
					}
					emulatorIcons.put(emu.getIconFilename(), icon);
				}
			}
		}
	}

	public void showPropertiesFrame() {
		showPropertiesFrame(null);
	}

	public void showPropertiesFrame(Game game) {
		if (frameProperties == null) {
			frameProperties = new PropertiesFrame(explorer);
			frameProperties.setLocationRelativeTo(view);
			frameProperties.addPlatformSelectedListener(new PlatformSelectedListener());
			frameProperties.addRemovePlatformListener(new RemovePlatformListener());
			frameProperties.addRemoveEmulatorListener(new RemoveEmulatorListener());
			frameProperties.addRemoveEmulatorListener2(new RemoveEmulatorListener());
			frameProperties.addOpenEmulatorPropertiesPanelListener(new OpenEmulatorPanelListener());
			frameProperties.addOpenEmulatorPropertiesPanelListener2(new OpenEmulatorPanelListener());
			frameProperties.adjustSplitPaneDividerSizes();
			frameProperties.adjustSplitPaneDividerLocations();
			frameProperties.setPlatformListModel(mdlPropertiesLstPlatforms);
			frameProperties.setSaveAndExitConfigurationListener(new SaveAndExitConfigurationListener());
			addPlatformListener(frameProperties);
			addEmulatorListener(frameProperties);
			addTagListener(frameProperties);
			initializePlatforms(explorer.getPlatforms());
			frameProperties.setPlatformListCellRenderer(new PlatformListCellRenderer());
			frameProperties.setEmulatorListCellRenderer(new EmulatorListCellRenderer());
			frameProperties.addDefaultEmulatorListener(new DefaultEmulatorListener() {

				@Override
				public void defaultEmulatorSet(Platform platform, int emulatorId) {
					try {
						explorerDAO.setDefaultEmulatorId(platform, emulatorId);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			frameProperties.addSearchForEmulatorListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					List<File> downloadFolders = new ArrayList<>();
					String userHome = System.getProperty("user.home");
					File downloadFolder = new File(userHome + "/Downloads");
					downloadFolders.add(downloadFolder);
					searchForPlatforms(downloadFolders);
				}
			});
		}
		if (game != null) {
			Platform platform = explorer.getPlatform(game.getPlatformId());
			Emulator emulator = explorer.getEmulatorFromPlatform(platform.getId());
			frameProperties.configureEmulator(platform, emulator);
		}
		if (frameProperties.isVisible()) {
			frameProperties.setState(Frame.NORMAL);
			frameProperties.toFront();
		} else {
			frameProperties.setVisible(true);
		}
	}

	class ExportGameListToTxtListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				File file = exportGameListTo(FileTypeConstants.TXT_FILE);
				if (file != null) {
					Desktop.getDesktop().open(file);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	class ExportGameListToCsvListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				File file = exportGameListTo(FileTypeConstants.CSV_FILE);
				if (file != null) {
					Desktop.getDesktop().open(file);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	class ExportGameListToXmlListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			File file;
			try {
				file = exportGameListTo(FileTypeConstants.XML_FILE);
				if (file != null) {
					try {
						Desktop.getDesktop().open(file);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
	}

	class ChangeToWelcomeViewListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int divLocation = view.getSplPreviewPane().getDividerLocation();
			view.changeToViewPanel(GameViewConstants.BLANK_VIEW, null);
			view.getSplPreviewPane().setDividerLocation(divLocation); // this
			// has
			// been
			// done,
			// cause
			// otherwise
			// preview
			// panel
			// magically
			// changes
			// size
			// (cause
			// of
			// other
			// panels
			// preferred
			// sizes
			// idk)
		}
	}

	class ChangeCoverSizeListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider) e.getSource();
			view.setCoverSize(source.getValue());
		}
	}

	class ChangeToCoversBiggestListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			view.setCoverSize(CoverConstants.HUGE_COVERS);
		}
	}

	class ChangeToCoversBigListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			view.setCoverSize(CoverConstants.LARGE_COVERS);
		}
	}

	class ChangeToCoversNormalListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			view.setCoverSize(CoverConstants.MEDIUM_COVERS);
		}
	}

	class ChangeToCoversSmallListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			view.setCoverSize(CoverConstants.SMALL_COVERS);
		}
	}

	class ChangeToCoversSmallestListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			view.setCoverSize(CoverConstants.TINY_COVERS);
		}
	}

	class ChangeToListViewListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int divLocation = view.getSplPreviewPane().getDividerLocation();
			view.changeToViewPanel(GameViewConstants.LIST_VIEW, explorer.getGames());
			view.getSplPreviewPane().setDividerLocation(divLocation); // this
			// has
			// been
			// done,
			// cause
			// otherwise
			// preview
			// panel
			// magically
			// changes
			// size
			// (cause
			// of
			// other
			// panels
			// preferred
			// sizes
			// idk)
		}
	}

	class ChangeToElementViewListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int divLocation = view.getSplPreviewPane().getDividerLocation();
			view.changeToViewPanel(GameViewConstants.ELEMENT_VIEW, explorer.getGames());
			view.getSplPreviewPane().setDividerLocation(divLocation); // this
			// has
			// been
			// done,
			// cause
			// otherwise
			// preview
			// panel
			// magically
			// changes
			// size
			// (cause
			// of
			// other
			// panels
			// preferred
			// sizes
			// idk)
		}
	}

	class ChangeToTableViewListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int divLocation = view.getSplPreviewPane().getDividerLocation();
			view.changeToViewPanel(GameViewConstants.TABLE_VIEW, explorer.getGames());
			view.getSplPreviewPane().setDividerLocation(divLocation); // this
			// has
			// been
			// done,
			// cause
			// otherwise
			// preview
			// panel
			// magically
			// changes
			// size
			// (cause
			// of
			// other
			// panels
			// preferred
			// sizes
			// idk)
		}
	}

	class ChangeToContentViewListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int divLocation = view.getSplPreviewPane().getDividerLocation();
			view.changeToViewPanel(GameViewConstants.CONTENT_VIEW, explorer.getGames());
			view.getSplPreviewPane().setDividerLocation(divLocation); // this
			// has
			// been
			// done,
			// cause
			// otherwise
			// preview
			// panel
			// magically
			// changes
			// size
			// (cause
			// of
			// other
			// panels
			// preferred
			// sizes
			// idk)
		}
	}

	class ChangeToCoverViewListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int divLocation = view.getSplPreviewPane().getDividerLocation();
			if (view.isViewPanelInitialized(GameViewConstants.COVER_VIEW)) {
				view.changeToViewPanel(GameViewConstants.COVER_VIEW, null);
			} else {
				view.changeToViewPanel(GameViewConstants.COVER_VIEW, explorer.getGames());
			}
			//			view.getSplGameDetailsPane().setDividerLocation(divLocationDetailsPane);
			view.getSplPreviewPane().setDividerLocation(divLocation); // this
			// has
			// been
			// done,
			// cause
			// otherwise
			// preview
			// panel
			// magically
			// changes
			// size
			// (cause
			// of
			// other
			// panels
			// preferred
			// sizes
			// idk)
		}
	}

	class ChangeToAllGamesListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					view.navigationChanged(new NavigationEvent(NavigationPanel.ALL_GAMES));
				}
			});
		}
	}

	class ChangeToRecentlyPlayedListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					view.navigationChanged(new NavigationEvent(NavigationPanel.RECENTLY_PLAYED));
				}
			});
		}
	}

	class ChangeToFavoritesListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					view.navigationChanged(new NavigationEvent(NavigationPanel.FAVORITES));
				}
			});
		}
	}

	class FullScreenListener implements ActionListener, MouseListener {
		protected Dimension lastWindowSize;
		protected Point lastWindowLocation;
		protected boolean fullScreen;

		private void goFullScreen() {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					//					boolean goFullScreen = !view.isUndecorated();
					//					boolean dontChangeState = goFullScreen && view.getExtendedState() == Frame.MAXIMIZED_BOTH;
					//					view.dispose();
					//					view.setUndecorated(goFullScreen);
					//					view.pack();
					//					int state = goFullScreen ? view.getExtendedState() | Frame.MAXIMIZED_BOTH : Frame.NORMAL;
					//					if (!dontChangeState) {
					//						view.setExtendedState(state);
					//					}
					//					view.setVisible(true);
					//					view.validate();
					//					view.repaint();

					boolean goFullScreen = !view.isUndecorated();
					if (goFullScreen) {
						fullScreen = view.getExtendedState() == Frame.MAXIMIZED_BOTH;
						lastWindowSize = view.getSize();
						lastWindowLocation = view.getLocationOnScreen();
					}
					view.setVisible(false);
					// this has been done because of an issue when already fullscreen not going properly fullscreen
					view.setExtendedState(Frame.NORMAL); // .. dont delete this line
					view.dispose();
					view.setUndecorated(goFullScreen);
					view.pack();
					if (goFullScreen) {
						view.setExtendedState(Frame.MAXIMIZED_BOTH);
					} else {
						if (lastWindowSize != null) {
							if (fullScreen) {
								view.setSize(new Dimension(preferredWidthAtFirstStart, (int) (preferredWidthAtFirstStart / 1.25)));
								view.setLocation(lastWindowLocation.x, lastWindowLocation.y);
								view.setExtendedState(Frame.MAXIMIZED_BOTH);
							} else {
								view.setSize(lastWindowSize);
								view.setLocation(lastWindowLocation.x, lastWindowLocation.y);
							}
						}
						//						view.setLocationRelativeTo(null);
					}
					view.setVisible(true);
					view.validate();
					view.repaint();

				}
			});
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			goFullScreen();
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getClickCount() == 2) {
				goFullScreen();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	}

	class PlatformSelectedListener implements ListSelectionListener {
		// private DefaultTableModel mdlLstSupportedEmulators = new
		// DefaultTableModel();

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()) {
				Platform selectedPlatform = frameProperties.getSelectedPlatform();
				frameProperties.platformSelected(selectedPlatform);
				// frameProperties.setEmulatorsTableModel(mdlLstSupportedEmulators);
			}
		}
	}

	class OpenHelpListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (dlgHelp == null) {
				dlgHelp = new HelpDialog();
			}
			dlgHelp.setLocationRelativeTo(view);
			dlgHelp.setVisible(true);
		}
	}

	class OpenAboutListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (dlgAbout == null) {
				dlgAbout = new AboutDialog();
				dlgAbout.addOpenContactSiteListener(new OpenContactSiteListener());
			}
			dlgAbout.setLocationRelativeTo(view);
			dlgAbout.setVisible(true);
		}
	}

	class OpenContactSiteListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (Desktop.isDesktopSupported()) {
				try {
					Desktop.getDesktop().browse(new URI(e.getActionCommand()));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	class OpenCheckForUpdatesListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (dlgUpdates == null) {
				dlgUpdates = new UpdateDialog(currentApplicationVersion, currentPlatformDetectionVersion);
				dlgUpdates.addSearchForUpdatesListener(new CheckForUpdatesListener());
			}
			dlgUpdates.setLocationRelativeTo(view);
			dlgUpdates.setVisible(true);
		}
	}

	class CheckForUpdatesListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			UpdateObject uo;
			try {
				uo = retrieveLatestRevisionInformations();
				System.out.println(uo.getDownloadLink());
				if (uo.isApplicationUpdateAvailable()) {
					Map<String, Action> actionKeys = new HashMap<>();
					actionKeys.put("updateNow", null);
					actionKeys.put("updateLater", null);
					NotificationElement element = new NotificationElement(new String[] { "applicationUpdateAvailable" },
							actionKeys, NotificationElement.INFORMATION_MANDATORY, null);
					view.showInformation(element);
					view.applicationUpdateAvailable();
				}
				if (uo.isSignatureUpdateAvailable()) {
					Map<String, Action> actionKeys = new HashMap<>();
					actionKeys.put("updateNow", null);
					actionKeys.put("updateLater", null);
					NotificationElement element = new NotificationElement(new String[] { "signatureUpdateAvailable" },
							actionKeys, NotificationElement.INFORMATION_MANDATORY, null);
					view.showInformation(element);
					view.signatureUpdateAvailable();
				}
				if (dlgUpdates.isVisible()) {
					dlgUpdates.setVersionInformations(uo);
				}
			} catch (MalformedURLException e1) {
				if (dlgUpdates.isVisible()) {
					dlgUpdates.setVersionInformations(null);
				}
			} catch (IOException e1) {
				if (dlgUpdates.isVisible()) {
					dlgUpdates.setVersionInformations(null);
				}
			}
			try {
				String changelog = retrieveChangelog();
				if (dlgUpdates.isVisible()) {
					dlgUpdates.setChangelog(changelog);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	class UpdateApplicationListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			installUpdate();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}
	}

	class InterruptSearchProcessListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (workerBrowseComputer != null && !workerBrowseComputer.isDone()) {
				String msg = Messages.get(MessageConstants.REQUEST_INTERRUPT_SEARCH_PROCESS);
				String title = Messages.get(MessageConstants.REQUEST_INTERRUPT_SEARCH_PROCESS_TITLE);
				int request = JOptionPane.showConfirmDialog(view, msg, title, JOptionPane.YES_NO_OPTION);
				if (request == JOptionPane.YES_OPTION) {
					try {
						interruptSearchProcess();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
	}

	public class ColumnWidthSliderListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider) e.getSource();
			view.setColumnWidth(source.getValue());
		}
	}

	public class RowHeightSliderListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider) e.getSource();
			view.setRowHeight(source.getValue());
		}
	}

	public void interruptSearchProcess() throws SQLException {
		workerBrowseComputer.searchProcessInterrupted();
		workerBrowseComputer.cancel(true);
		view.searchProcessEnded();
	}

	@Override
	public void gameSelected(GameSelectionEvent e) {
		explorer.setCurrentGame(e.getGames());
	}

	@Override
	public void platformAdded(PlatformEvent e) {
		Platform p = e.getPlatform();
		view.getViewManager().getIconStore().addPlatformIcon(p.getId(), p.getIconFileName());
	}

	@Override
	public void platformRemoved(PlatformEvent e) {
		Platform p = e.getPlatform();
		explorer.removePlatform(p);
	}

	@Override
	public void emulatorAdded(EmulatorEvent e) {
		view.emulatorAdded(e);

		File emuFile = new File(e.getEmulator().getPath());
		ImageIcon ii = (ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(emuFile);
		int width = ii.getIconWidth();
		int height = ii.getIconHeight();

		double size = 32;
		double factor2 = (height / size);
		if (height > size) {
			height = (int) (height / factor2);
			width = (int) (width / factor2);
		}
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bi.createGraphics();
		g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
		g2d.drawImage(ii.getImage(), 0, 0, width, height, null);
		String emuBroIconHome = System.getProperty("user.home") + File.separator + ".emubro" + File.separator
				+ "emulators";
		String iconPathString = emuBroIconHome + File.separator + e.getEmulator().getId() + ".png";
		File iconHomeFile = new File(iconPathString);
		if (!iconHomeFile.exists()) {
			iconHomeFile.mkdirs();
		}
		try {
			ImageIO.write(bi, "png", new File(iconPathString));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void emulatorRemoved(EmulatorEvent e) {
		BroPlatform platform = (BroPlatform) e.getPlatform();
		BroEmulator emulator = (BroEmulator) e.getEmulator();
		boolean favorite = (platform.getDefaultEmulator() == null) ? false : platform.getDefaultEmulator().getId() == emulator.getId();
		platform.removeEmulator(emulator);
		int emulatorId = e.getEmulator().getId();
		try {
			explorerDAO.removeEmulator(emulatorId);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		view.emulatorRemoved(e);
		if (favorite) {
			List<BroEmulator> emulators = platform.getEmulators();
			if (emulators != null) {
				for (Emulator emu : emulators) {
					if (emu.isInstalled()) {
						platform.setDefaultEmulatorId(emu.getId());
						try {
							explorerDAO.setDefaultEmulatorId(platform, emu.getId());
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		}
		for (Game g : explorer.getGames()) {
			if (g.getDefaultEmulatorId() == emulatorId) {
				g.setEmulator(EmulatorConstants.NO_EMULATOR);
				try {
					explorerDAO.setDefaultEmulatorId(g, EmulatorConstants.NO_EMULATOR);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public Platform isGameInArchive(String fileName) {
		List<Platform> platforms = explorer.getPlatforms();
		for (Platform p : platforms) {
			String searchFor = p.getSearchFor();
			if (p.hasGameSearchMode(GameConstants.FILE_FILE_NAME_MATCH)) {
				if (fileName.toLowerCase().matches(searchFor)) {
					Platform p2 = null;
					if (!explorer.hasPlatform(p.getName())) {
						try {
							explorerDAO.addPlatform(p);
							p2 = p;
							p2.setId(explorerDAO.getLastAddedPlatformId());
							explorer.addPlatform(p2);
							firePlatformAddedEvent(p2);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {
						return explorer.getPlatform(p.getName());
					}
					return p2;
				}
			}
		}
		return null;
	}

	Platform isGameOrEmulator(String filePath)
			throws SQLException, ZipException, RarException, IOException, BroEmulatorDeletedException {
		String[] arr = filePath.split(getSeparatorBackslashed());
		String fileName = arr[arr.length - 1];
		List<Platform> platforms = explorer.getPlatforms();
		for (Platform pDefault : platforms) {
			// check for emulators
			List<BroEmulator> emus = pDefault.getEmulators();
			for (BroEmulator e : emus) {
				String[] arr2 = filePath.split(getSeparatorBackslashed());
				String fileName2 = arr2[arr2.length - 1].toLowerCase();
				String searchString = e.getSearchString();
				if (fileName2.matches(searchString.toLowerCase())) {
					String name = e.getName();
					String filePath2 = filePath;
					String iconFilename = e.getIconFilename();
					String configFilePath = e.getConfigFilePath();
					String website = e.getWebsite();
					String startParameters = e.getStartParameters();
					List<String> supportedFileTypes = e.getSupportedFileTypes();
					boolean autoSearchEnabled = e.isAutoSearchEnabled();
					Emulator emulator = null;
					pDefault = explorer.getPlatform(pDefault.getName());
					if (explorer.hasEmulator(pDefault.getName(), filePath2)) {
						continue;
					}
					emulator = new BroEmulator(EmulatorConstants.NO_EMULATOR, name, filePath2, iconFilename,
							configFilePath, website, startParameters, supportedFileTypes, e.getSearchString(),
							e.getSetupFileMatch(), autoSearchEnabled);
					pDefault.addEmulator((BroEmulator) emulator);

					try {
						int platformId = pDefault.getId();
						if (platformId == PlatformConstants.NO_PLATFORM) {
							for (Platform p3 : explorer.getPlatforms()) {
								System.out.println(p3.getName() + " " + p3.getId());
							}
						} else {
							explorerDAO.addEmulator(platformId, emulator);
							emulator.setId(explorerDAO.getLastAddedEmulatorId());

							if (!pDefault.hasDefaultEmulator()) {
								pDefault.setDefaultEmulatorId(emulator.getId());
							}
							pDefault.addEmulator((BroEmulator) emulator);
							fireEmulatorAddedEvent(pDefault, emulator);
							break;
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					return pDefault;
				}
			}
			String searchFor = pDefault.getSearchFor();
			if (pDefault.hasGameSearchMode("FILE_STRUCTURE_MATCH")) {
				if (fileName.toLowerCase().matches(searchFor)) {
					for (FileStructure fs : pDefault.getFileStructure()) {
						Path path = Paths.get(filePath);
						String parent = path.getParent().toString();

						File file = new File(
								parent + (parent.endsWith(File.separator) ? "" : File.separator + fs.getFolderName()));
						if (file.exists()) {
							if (!explorer.hasPlatform(pDefault.getName())) {
								try {
									explorerDAO.addPlatform(pDefault);
									pDefault.setId(explorerDAO.getLastAddedPlatformId());
									if (!pDefault.hasDefaultEmulator()) {
										System.err.println("no default emulator");
									}
									explorer.addPlatform(pDefault);
									firePlatformAddedEvent(pDefault);
								} catch (SQLException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
							Platform p3 = explorer.getPlatform(pDefault.getName());
							return p3;
						} else {
							return null;
						}
					}
				}
			}

			if (pDefault.hasGameSearchMode(GameConstants.FILE_FILE_NAME_MATCH)) {
				if (fileName.toLowerCase().matches(searchFor)) {
					if (!pDefault.hasDefaultEmulator() && pDefault.getEmulators().size() > 0) {
						for (Emulator emu : emus) {
							if (emu.isInstalled()) {
								pDefault.setDefaultEmulatorId(emu.getId());
							}
						}
					}
					return pDefault;
				}
			}
			if (pDefault.hasGameSearchMode(GameConstants.ARCHIVE_FILE_NAME_MATCH)) {
				if (pDefault.isSupportedArchiveType(fileName)) {
					// if (fileName.toLowerCase().trim().endsWith(".rar")) {
					// if (rarFileContainsGame(filePath, searchFor)) {
					// return p;
					// }
					// } else {
					// if (zipFileContainsGame(filePath, searchFor)) {
					// return p;
					// }
					// }
				}
			}
			if (pDefault.hasGameSearchMode(GameConstants.IMAGE_FILE_NAME_MATCH)) {
				if (pDefault.isSupportedImageType(fileName)) {
					// if (isoFileContainsGame(filePath, searchFor)) {
					// return p;
					// }
				}
			}
		}
		return null;
	}

	void firePlatformAddedEvent(Platform platform) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				PlatformEvent event = new BroPlatformAddedEvent(platform);
				for (PlatformListener l : platformListeners) {
					l.platformAdded(event);
				}
			}
		});
	}

	void fireEmulatorAddedEvent(Platform platform, Emulator emulator) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				EmulatorEvent event = new BroEmulatorAddedEvent(platform, emulator);
				for (EmulatorListener l : emulatorListeners) {
					l.emulatorAdded(event);
				}
			}
		});
	}

	void fireEmulatorRemovedEvent(Platform platform, Emulator emulator) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				EmulatorEvent event = new BroEmulatorRemovedEvent(platform, emulator);
				for (EmulatorListener l : emulatorListeners) {
					l.emulatorRemoved(event);
				}
			}
		});
	}

	public void addGame(Platform p0, File file) throws BroGameDeletedException {
		addGame(p0, file, false, false);
	}

	public void addGame(Platform p0, File file, boolean manuallyAdded, boolean favorite) {
		String checksum = null;
		if (digest == null) {
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			}
		}
		if (digest != null) {
			try {
				checksum = getFileChecksum(digest, file);
				try {
					explorerDAO.addChecksum(checksum);
					explorer.addChecksum(explorerDAO.getLastAddedChecksumId(), checksum);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
				if (manuallyAdded) {
					UIUtil.showErrorMessage(view, "Couldn't get the checksum for this file.", "Error adding file");
				}
				return;
			}
		}
		String filePath = file.getAbsolutePath();
		String[] arr = filePath.split(getSeparatorBackslashed());
		String fileName = arr[arr.length - 1];
		if (explorer.isKnownExtension(FilenameUtils.getExtension(fileName))) {
			fileName = FilenameUtils.removeExtension(fileName);
		}
		LocalDateTime dateAdded = LocalDateTime.now();
		int platformId = p0.getId();
		String platformIconFileName = p0.getIconFileName();
		int defaultFileId = -1;
		Game element = new BroGame(GameConstants.NO_GAME, fileName, defaultFileId, explorerDAO.getChecksumId(checksum), null, null, 0, dateAdded, null, 0,
				EmulatorConstants.NO_EMULATOR, platformId, platformIconFileName);
		String defaultGameCover = p0.getDefaultGameCover();
		view.getViewManager().getIconStore().addPlatformCover(platformId, defaultGameCover);
		if (favorite) {
			element.setRate(RatingBarPanel.MAXIMUM_RATE);
		}
		try {
			try {
				explorerDAO.addGame(element, filePath);
				int gameId = explorerDAO.getLastAddedGameId();
				element.setId(gameId);
			} catch (BroGameDeletedException e) {
				if (manuallyAdded) {
					String gameName = "<html><strong>"+e.getGame().getName()+"</strong></html>";
					String platformName = explorer.getPlatform(e.getGame().getPlatformId()).getName();
					int request = JOptionPane.showConfirmDialog(view, Messages.get(MessageConstants.GAME_DELETED, gameName, platformName),
							Messages.get(MessageConstants.GAME_DELETED_TITLE), JOptionPane.YES_NO_OPTION);
					if (request == JOptionPane.YES_OPTION) {
						explorerDAO.restoreGame(e.getGame());
						element = explorerDAO.getGameByChecksumId(e.getGame().getChecksumId());
						for (Tag tag : explorerDAO.getTagsForGame(element.getId())) {
							element.addTag(tag);
						}
					} else {
						return;
					}
				} else {
					return;
				}
			}
			if (filePath.toLowerCase().endsWith(".exe")) {
				ImageIcon ii = (ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(file);
				int width = ii.getIconWidth();
				int height = ii.getIconHeight();
				double size = 32;
				double factor2 = (height / size);
				if (height > size) {
					height = (int) (height / factor2);
					width = (int) (width / factor2);
				}
				BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2d = bi.createGraphics();
				g2d.addRenderingHints(
						new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
				g2d.drawImage(ii.getImage(), 0, 0, width, height, null);
				String emuBroIconHome = System.getProperty("user.home") + File.separator + ".emubro" + File.separator
						+ "icons";
				String iconPathString = emuBroIconHome + File.separator + explorer.getChecksum(element.getChecksumId()) + File.separator + System.currentTimeMillis() + ".png";
				File iconHomeFile = new File(iconPathString);
				if (!iconHomeFile.exists()) {
					iconHomeFile.mkdirs();
				}
				ImageIO.write(bi, "png", new File(iconPathString));
				element.setIconPath(iconPathString);
				explorerDAO.setGameIconPath(element.getId(), iconPathString);
			}
			explorer.addGame(element, filePath);
			final Game gameFinal = element;
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					view.gameAdded(new BroGameAddedEvent(gameFinal, p0, explorer.getGameCount(), manuallyAdded));
					if (manuallyAdded) {
						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								view.getViewManager().selectGame(gameFinal.getId());
							}
						});
					}
				}
			});
		} catch (BroGameAlreadyExistsException e) {
			//			String message = "This game does already exist.";
			//			String title = "Game already exists";
			//			JOptionPane.showMessageDialog(view, message, title, JOptionPane.ERROR_MESSAGE);
			explorer.addFile(e.getGameId(), filePath);
			if (manuallyAdded) {
				JOptionPane.showMessageDialog(view, "game does already exists");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getFileChecksum(MessageDigest digest, File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		byte[] byteArray = new byte[1024];
		int bytesCount = 0;
		while ((bytesCount = fis.read(byteArray)) != -1) {
			digest.update(byteArray, 0, bytesCount);
		};
		fis.close();
		byte[] bytes = digest.digest();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

	@Override
	public void rememberZipFile(String filePath) {
		if (!zipFiles.contains(filePath)) {
			zipFiles.add(filePath);
			explorerDAO.rememberZipFile(filePath);
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					view.rememberZipFile(filePath);
				}
			});
		}
	}

	@Override
	public void rememberRarFile(String filePath) {
		if (!rarFiles.contains(filePath)) {
			rarFiles.add(filePath);
			explorerDAO.rememberRarFile(filePath);
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					view.rememberRarFile(filePath);
				}
			});
		}
	}

	@Override
	public void rememberIsoFile(String filePath) {
		if (!isoFiles.contains(filePath)) {
			isoFiles.add(filePath);
			explorerDAO.rememberIsoFile(filePath);
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					view.rememberIsoFile(filePath);
				}
			});
		}
	}

	public class HideExtensionsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			view.hideExtensions(((AbstractButton) e.getSource()).isSelected());
		}
	}

	public class TouchScreenOptimizedScrollListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			AbstractButton btn = (AbstractButton) e.getSource();
			view.setTouchScreenOpimizedScrollEnabled(btn.isSelected());
		}
	}

	public class BroRateListener implements RateListener {
		@Override
		public void rateChanged(RateEvent e) {
			rateGame(e.getGame());
		}
	}

	public class BroTagListener implements TagListener {

		@Override
		public void tagAdded(TagEvent e) {
			List<Game> tmpCurrentGames = explorer.getCurrentGames();
			for (Game currentGame : tmpCurrentGames) {
				int gameId = currentGame.getId();
				if (!currentGame.hasTag(e.getTag().getId())) {
					explorer.addTagForGame(gameId, e.getTag());
					currentGame.addTag(e.getTag());
					view.tagAdded(e);
					try {
						explorerDAO.addTag(gameId, e.getTag());
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				view.updateFilter();
			}
		}

		@Override
		public void tagRemoved(TagEvent e) {
			Game currentGame = explorer.getCurrentGames().get(0);
			int gameId = currentGame.getId();
			if (currentGame.hasTag(e.getTag().getId())) {
				explorer.removeTagFromGame(gameId, e.getTag().getId());
				currentGame.removeTag(e.getTag().getId());
				view.tagRemoved(e);
				try {
					explorerDAO.removeTag(gameId, e.getTag().getId());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	public class BroCommentListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			commentGames(explorer.getCurrentGames());
		}
	}

	public class LanguageGermanListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			changeLanguage(Locale.GERMAN);
		}
	}

	public class LanguageEnglishListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			changeLanguage(Locale.ENGLISH);
		}
	}

	public class LanguageFrenchListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			changeLanguage(Locale.FRENCH);
		}
	}

	public class PlatformListCellRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			BroPlatform platform = ((BroPlatform) value);
			boolean hasDefaultEmulator = platform.hasDefaultEmulator();
			boolean hasNoGamesAndEmulators = explorer.getGameCountFromPlatform(platform.getId()) == 0
					&& !hasDefaultEmulator;
			//			label.setForeground((hasDefaultEmulator) ? Color.BLUE : UIManager.getColor("Label.foregroundColor"));
			label.setText((hasDefaultEmulator) ? "<html><strong>"+platform.getName()+"</strong></html>" : platform.getName());
			label.setForeground((hasNoGamesAndEmulators) ? UIManager.getColor("Label.disabledForeground") : UIManager.getColor("Label.foreground"));
			ImageIcon icon = platformIcons.get(platform.getIconFileName());
			label.setIcon(icon);
			label.setDisabledIcon(icon);
			return label;
		}
	}

	public class EmulatorListCellRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			// String iconPath = value ==
			// pnlPlatforms.lstPlatforms.getSelectedValue().getDefaultEmulatorId()
			// ? "/images/"+resolution+"/dialog-ok-apply-5.png"
			// : "/images/"+resolution+"/empty.png";

			// File svgFile = new
			// File("D:/files/workspace/JGameExplorer/res/images/dialog-ok-apply-5.svg");
			// ImageIcon icon = ImageUtil.getImageIconFrom(svgFile);
			// label.setIcon(icon);
			BroEmulator emulator = ((BroEmulator) value);
			Icon icon = emulatorIcons.get(emulator.getIconFilename());
			label.setIcon(icon);
			return label;
		}
	}

	@Override
	public void steamFolderDetected(String absolutePath) {
		System.err.println("steam game folder detected: "+absolutePath);
	}

	class ImageEditPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		private JLabel lblImage = new JLabel();

		private BufferedImage bi;

		private double coverHeight = 200;

		private double angle = 0;

		private boolean scaleEnabled;

		private boolean cutEnabled;

		private double cutBorder;

		private int x;
		private int y;

		protected Point mousePos = new Point();
		protected Point mouseClick = new Point();

		private Rectangle currentRect;

		private File file;

		private Color color0 = new Color(0, 0, 0, 192);
		private Color color3 = new Color(255, 255, 255, 192);

		protected int dragRectX;

		protected int dragRectY;

		private int currentCoverHeight;

		private int currentCoverWidth;

		public ImageEditPanel() {
			add(lblImage);
			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					super.mousePressed(e);
					mouseClick = e.getPoint();
					repaint();
				}
			});

			addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseMoved(MouseEvent e) {
					super.mouseMoved(e);
					mousePos = e.getPoint();
					repaint();
				}

				@Override
				public void mouseDragged(MouseEvent e) {
					super.mouseDragged(e);
					dragRectX = e.getX();
					dragRectY = e.getY();
					repaint();
				}
			});
		}

		public int getCurrentCoverWidth() {
			return currentCoverWidth;
		}

		public void setCurrentCoverWidth(int width) {
			currentCoverWidth = width;
		}

		public int getCurrentCoverHeight() {
			return currentCoverHeight;
		}

		public void setCurrentCoverHeight(int height) {
			currentCoverHeight = height;
		}

		public void setFile(File file) {
			this.file = file;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (bi != null) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				setCurrentCoverWidth(bi.getWidth(null));
				setCurrentCoverHeight(bi.getHeight(null));
				if (scaleEnabled) {
					double factor = (getCurrentCoverHeight() / coverHeight);
					if (getCurrentCoverHeight() > coverHeight) {
						setCurrentCoverHeight((int) (getCurrentCoverHeight() / factor));
						setCurrentCoverWidth((int) (getCurrentCoverWidth() / factor));
					}
				}
				checkRotate(g2d, currentCoverWidth, currentCoverHeight);
				//			g2d.drawImage(bi, 0, 0, width / 2, height, bi.getWidth(null) / 2, 0, bi.getWidth(null), bi.getHeight(null), this);
				g2d.drawImage(bi, 0, 0, currentCoverWidth, currentCoverHeight, this);

				checkDrawCuttingBorder(g2d, currentCoverWidth, currentCoverHeight);
				g2d.dispose();
			}
		}

		private void checkRotate(Graphics2D g2d, int width, int height) {
			double sin = Math.abs(Math.sin(Math.toRadians(angle))),
					cos = Math.abs(Math.cos(Math.toRadians(angle)));
			int newWidth = (int) Math.floor(width * cos + height * sin);
			int newHeight = (int) Math.floor(height * cos + width * sin);
			g2d.translate((newWidth - width) / 2, (newHeight - height) / 2);
			g2d.rotate(Math.toRadians(angle), width / 2, height / 2);
		}

		private void checkDrawCuttingBorder(Graphics2D g2d, int width, int height) {
			if (cutEnabled) {
				double tmpCutBorder = width / 100 * cutBorder;
				int leftCoverWidth = (int) ((width / 2) - (tmpCutBorder / 2));
				int rightCoverWidth = (int) ((width / 2) - (tmpCutBorder / 2));
				int leftCoverX = 0;
				int rightCoverX = (int) ((width / 2) + (tmpCutBorder / 2));
				int borderX = (int) ((width / 2) - (tmpCutBorder / 2));
				int alpha = 64; // 25% transparent
				//			int alpha = 127; // 50% transparent
				//			int alpha = 192; // 75% transparent
				Color color1 = new Color(0, 0, 0, 127);
				if (dragRectWidth == -1) {
					dragRectWidth = width;
				}
				if (dragRectHeight == -1) {
					dragRectHeight = height;
				}
				Rectangle rect0 = new Rectangle(dragRectX, dragRectY, dragRectWidth, dragRectHeight);
				Rectangle rect = new Rectangle(leftCoverX, 0, leftCoverWidth, height);
				Rectangle rect2 = new Rectangle(rightCoverX, 0, rightCoverWidth, height);
				if (mouseClick != null && rect0.contains(mouseClick)) {
					currentRect = rect0;
				} else if (mouseClick != null && rect.contains(mouseClick)) {
					g2d.setColor(color1);
					g2d.fillRect(rightCoverX, 0, rightCoverWidth, height);
					g2d.fillRect(borderX, 0, (int) tmpCutBorder, height);
					currentRect = rect;
				} else if (mouseClick != null && rect2.contains(mouseClick)) {
					g2d.setColor(color1);
					g2d.fillRect(leftCoverX, 0, rightCoverWidth, height);
					g2d.fillRect(borderX, 0, (int) tmpCutBorder, height);
					currentRect = rect2;
				} else {
					g2d.setColor(color1);
					g2d.fillRect(leftCoverX, 0, rightCoverWidth, height);
					g2d.fillRect(borderX, 0, (int) tmpCutBorder, height);
					g2d.fillRect(rightCoverX, 0, rightCoverWidth, height);
					currentRect = null;
				}
				drawStylishRect(g2d, rect0);
				if (1 == 0) {
					drawStylishRect(g2d, rect);
					drawStylishRect(g2d, rect2);
				}
				//				g2d.drawRect(borderX, 0, (int) tmpCutBorder, height);
				//				g2d.drawRect(borderX+2, 2, (int) tmpCutBorder-3, height-3);
				//				g2d.drawRect(borderX+1, 1, (int) tmpCutBorder-2, height-2);
			}
		}

		private void drawStylishRect(Graphics g2d, Rectangle rect) {
			int coverX = rect.x;
			int coverY = rect.y;
			int coverWidth = rect.width;
			int height = rect.height;
			g2d.setColor(color0);
			g2d.drawRect(coverX, coverY, coverWidth, height);
			int dragBorderTop = 2;
			int dragBorderBottom = 2;
			int dragBorderLeft = 2;
			int dragBorderRight = 2;
			if (rect.contains(mousePos)) {
				if (mousePos.getY() <= (rect.y+20)) {
					dragBorderTop = 20;
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					g2d.drawRect(coverX + 2, coverY + 2,
							coverWidth - 4, dragBorderTop - 4);
					g2d.setColor(color3);
					int tmpY = coverY + 2 + dragBorderTop - 4+1;
					g2d.drawLine(coverX + 2, tmpY, coverWidth - 4, tmpY);
				}
				if (mousePos.getX() <= (rect.x+20)) {
					dragBorderLeft = 20;
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
				if (mousePos.getY() >= (rect.height-20)) {
					dragBorderBottom = 20;
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					g2d.setColor(color0);
					g2d.drawRect(coverX + 2, height - dragBorderBottom + 2,
							coverWidth - 4, dragBorderBottom - 4);
					g2d.setColor(color3);
					int tmpY = height - dragBorderBottom + 2 -1;
					g2d.drawLine(coverX + 2, tmpY, coverWidth - 4, tmpY);
				}
				if (mousePos.getX() >= (rect.width-20)) {
					dragBorderRight = 20;
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
			} else {
				setCursor(null);
			}
			g2d.setColor(color0);
			g2d.drawRect(coverX+dragBorderLeft,
					coverY+dragBorderTop,
					coverWidth - dragBorderLeft - dragBorderRight,
					height - dragBorderTop - dragBorderBottom);
			g2d.setColor(color3);
			g2d.drawRect(coverX+1, coverY+1, coverWidth-2, height-2);
		}

		public void rotateRight() {
			if (angle == 270) {
				rotate(0);
			} else {
				rotate(angle + 90);
			}
		}

		public void rotateLeft() {
			if (angle == 0) {
				rotate(270);
			} else {
				rotate(angle - 90);
			}
		}

		public void rotate(double angle) {
			this.angle = angle;
			repaint();
		}

		public void setImage(BufferedImage bi) {
			this.bi = bi;
		}

		public void setCoverHeight(int coverHeight) {
			mousePos = null;
			mouseClick = null;
			this.coverHeight = coverHeight;
			repaint();
		}

		public void setScaleEnabled(boolean scaleEnabled) {
			this.scaleEnabled = scaleEnabled;
		}

		public void setCutEnabled(boolean cutEnabled) {
			this.cutEnabled = cutEnabled;
			repaint();
		}

		public void setCutBorder(double cutBorder) {
			this.cutBorder = cutBorder;
		}

		public BufferedImage getCuttedImage(int width, int height) {
			if (currentRect == null) {
				throw new NullPointerException("no selection");
			}
			Image bi2 = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			/*
			 * Exception in thread "AWT-EventQueue-0" java.awt.image.RasterFormatException: (x + width) is outside raster
			 */
			return ImageUtil.toBufferedImage(bi2).getSubimage(currentRect.x, currentRect.y, currentRect.width, currentRect.height);
		}
	}

	@Override
	public void tagAdded(TagEvent e) {
		explorer.addTag(e.getTag());
	}

	@Override
	public void tagRemoved(TagEvent e) {
		Tag tag = e.getTag();
		explorer.removeTag(tag);
	}
}