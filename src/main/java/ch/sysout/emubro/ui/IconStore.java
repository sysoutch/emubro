package ch.sysout.emubro.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import ch.sysout.ui.ImageUtil;
import ch.sysout.util.ScreenSizeUtil;

public class IconStore {
	private Map<Integer, ImageIcon> platformIcons = new HashMap<>();

	private Map<Integer, String> emulatorIconPaths = new HashMap<>();
	private Map<Integer, ImageIcon> emulatorIcons = new HashMap<>();

	private Map<Integer, String> gameIconPaths = new HashMap<>();
	private Map<Integer, String> gameCoverPaths = new HashMap<>();

	private Map<Integer, ImageIcon> gameIcons = new HashMap<>();
	private Map<Integer, ImageIcon> gameCovers = new HashMap<>();

	private Map<Integer, ImageIcon> platformCovers = new HashMap<>();

	private Map<Integer, Map<Integer, ImageIcon>> scaledPlatformCovers = new HashMap<>();
	private Map<Integer, Map<Integer, ImageIcon>> scaledGameCovers = new HashMap<>();

	private List<GameCoverListener> gameCoverListeners = new ArrayList<>();

	private String currentPlatformLogosDirectory = System.getProperty("user.dir")+"/emubro-resources/platforms/images/logos";
	private String currentPlatformCoversDirectory = System.getProperty("user.dir")+"/emubro-resources/platforms/images/covers";

	public void addPlatformCover(int platformId, String coverFileName) {
		String coverFilePath = currentPlatformCoversDirectory + "/" + coverFileName;
		if (!platformCovers.containsKey(platformId)) {
			int size = ScreenSizeUtil.adjustValueToResolution(200);
			ImageIcon ii = ImageUtil.getImageIconFrom(coverFilePath, true);
			int scaleOption = CoverConstants.SCALE_AUTO_OPTION;
			ImageIcon cover = null;
			if (ii != null) {
				if (ii.getIconWidth() > size && ii.getIconWidth() >= ii.getIconHeight()) {
					scaleOption = CoverConstants.SCALE_WIDTH_OPTION;
				} else if (ii.getIconHeight() > size && ii.getIconWidth() < ii.getIconHeight()) {
					scaleOption = CoverConstants.SCALE_HEIGHT_OPTION;
				} else {
					cover = ii;
					platformCovers.put(platformId, cover);
					return;
				}
				cover = ImageUtil.scaleCover(ii, size, scaleOption);
			}
			platformCovers.put(platformId, cover);
		}
	}

	void removePlatformCover(int platformId) {
		platformCovers.remove(platformId);
	}

	public ImageIcon getPlatformIcon(int platformId) {
		return platformIcons.get(platformId);
	}

	public ImageIcon getPlatformCover(int platformId) {
		return platformCovers.get(platformId);
	}

	public ImageIcon getScaledPlatformCover(int platformId, int currentCoverSize) {
		ImageIcon icon = getPlatformCover(platformId);
		Map<Integer, ImageIcon> scaledIconMap = scaledPlatformCovers.get(platformId);
		if (scaledIconMap != null) {
			if (scaledIconMap.containsKey(currentCoverSize)) {
				icon = scaledIconMap.get(currentCoverSize);
			}
			else {
				int scaleOption = CoverConstants.SCALE_AUTO_OPTION;
				if (icon.getIconWidth() >= icon.getIconHeight()) {
					scaleOption = CoverConstants.SCALE_WIDTH_OPTION;
				} else if (icon.getIconWidth() < icon.getIconHeight()) {
					scaleOption = CoverConstants.SCALE_HEIGHT_OPTION;
				}
				icon = ImageUtil.scaleCover(icon, currentCoverSize, scaleOption);
				scaledIconMap.put(currentCoverSize, icon);
			}
		} else {
			if (icon == null) {
				return null;
			}
			Map<Integer, ImageIcon> newMap = new HashMap<>();
			int scaleOption = CoverConstants.SCALE_AUTO_OPTION;
			if (icon.getIconWidth() >= icon.getIconHeight()) {
				scaleOption = CoverConstants.SCALE_WIDTH_OPTION;
			} else if (icon.getIconWidth() < icon.getIconHeight()) {
				scaleOption = CoverConstants.SCALE_HEIGHT_OPTION;
			}
			icon = ImageUtil.scaleCover(icon, currentCoverSize, scaleOption);
			newMap.put(currentCoverSize, icon);
			scaledPlatformCovers.put(platformId, newMap);
		}
		return icon;
	}

	public void addPlatformIcon(int platformId, String iconFileName) {
		if (!platformIcons.containsKey(platformId)) {
			int size = ScreenSizeUtil.adjustValueToResolution(16);
			String iconFilePath = currentPlatformLogosDirectory + "/"+iconFileName;
			ImageIcon ii = ImageUtil.getImageIconFrom(iconFilePath, true);
			ImageIcon ico = ImageUtil.scaleCover(ii, size, CoverConstants.SCALE_WIDTH_OPTION);
			platformIcons.put(platformId, ico);
		}
	}

	void removePlatformIcon(int platformId) {
		platformIcons.remove(platformId);
	}

	public void addEmulatorIconPath(int emulatorId, String iconPath) {
		if (!emulatorIconPaths.containsKey(emulatorId)) {
			emulatorIconPaths.put(emulatorId, iconPath);
		}
	}

	void removeEmulatorIcon(int emulatorId) {
		emulatorIcons.remove(emulatorId);
	}

	public ImageIcon getEmulatorIcon(int emulatorId) {
		if (emulatorIconPaths.containsKey(emulatorId) && !emulatorIcons.containsKey(emulatorId)) {
			String iconFilePath = emulatorIconPaths.get(emulatorId);
			ImageIcon ico = ImageUtil.getImageIconFrom(iconFilePath, true);
			emulatorIcons.put(emulatorId, ico);
		}
		return emulatorIcons.get(emulatorId);
	}

	public void addGameIconPath(int gameId, String iconPath) {
		if (iconPath == null || iconPath.trim().isEmpty()) {
			return;
		}
		if (!gameIconPaths.containsKey(gameId)) {
			gameIconPaths.put(gameId, iconPath);
			String iconFilePath = gameIconPaths.get(gameId);
			ImageIcon ico = ImageUtil.getImageIconFrom(iconFilePath, true);
			gameIcons.put(gameId, ico);
		}
	}

	public void removeGameIconPath(int gameId) {
		gameIcons.remove(gameId);
	}

	public ImageIcon getGameIcon(int gameId) {
		return gameIcons.get(gameId);
	}

	public ImageIcon getGameIcons(int gameId) {
		return gameIcons.get(gameId);
	}

	public void addGameCoverPath(int gameId, String coverPath) {
		if (coverPath == null || coverPath.trim().isEmpty()) {
			return;
		}
		if (!gameCoverPaths.containsKey(gameId)) {
			gameCoverPaths.put(gameId, coverPath);
		}
	}

	public ImageIcon getGameCover(int gameId) {
		if (!gameCovers.containsKey(gameId)) {
			if (!gameCoverPaths.containsKey(gameId)) {
				return null;
			}
			String coverFilePath = gameCoverPaths.get(gameId);
			ImageIcon ico = ImageUtil.getImageIconFrom(coverFilePath, true);
			gameCovers.put(gameId, ico);
		}
		return gameCovers.get(gameId);
	}

	public ImageIcon getScaledGameCover(int gameId, int currentCoverSize) {
		ImageIcon icon = getGameCover(gameId);
		Map<Integer, ImageIcon> scaledIconMap = scaledGameCovers.get(gameId);
		if (scaledIconMap != null) {
			if (scaledIconMap.containsKey(currentCoverSize)) {
				icon = scaledIconMap.get(currentCoverSize);
			}
			else {
				int scaleOption = CoverConstants.SCALE_AUTO_OPTION;
				if (icon.getIconWidth() >= icon.getIconHeight()) {
					scaleOption = CoverConstants.SCALE_WIDTH_OPTION;
				} else if (icon.getIconWidth() < icon.getIconHeight()) {
					scaleOption = CoverConstants.SCALE_HEIGHT_OPTION;
				}
				icon = ImageUtil.scaleCover(icon, currentCoverSize, scaleOption);
				scaledIconMap.put(currentCoverSize, icon);
			}
		} else {
			if (icon == null) {
				return null;
			}
			Map<Integer, ImageIcon> newMap = new HashMap<>();
			int scaleOption = CoverConstants.SCALE_AUTO_OPTION;
			if (icon.getIconWidth() >= icon.getIconHeight()) {
				scaleOption = CoverConstants.SCALE_WIDTH_OPTION;
			} else if (icon.getIconWidth() < icon.getIconHeight()) {
				scaleOption = CoverConstants.SCALE_HEIGHT_OPTION;
			}
			icon = ImageUtil.scaleCover(icon, currentCoverSize, scaleOption);
			newMap.put(currentCoverSize, icon);
			scaledGameCovers.put(gameId, newMap);
		}
		return icon;
	}

	public void addGameCoverListener(GameCoverListener l) {
		gameCoverListeners.add(l);
	}
}
