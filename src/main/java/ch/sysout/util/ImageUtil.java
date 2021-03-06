package ch.sysout.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import ch.sysout.emubro.ui.CoverConstants;

public class ImageUtil {

	/**
	 * @param filepath
	 * @return
	 */
	public static ImageIcon getImageIconFrom(String filepath) {
		return getImageIconFrom(filepath, false);
	}

	/**
	 * @param filepath
	 *            absolute or relative filepath
	 * @return
	 */
	public static ImageIcon getImageIconFrom(String filepath, boolean absolutePath) {
		URL url = ImageUtil.class.getResource(filepath);
		if (!absolutePath && url == null) {
			// this happens when the requested url is not in the class path
			throw new NullPointerException("picture file not found in jar: " + filepath);
		}
		ImageIcon icon = (absolutePath) ? new ImageIcon(filepath)
				: new ImageIcon(url);
		return icon;
	}

	/**
	 * @param filepath
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage getBufferedImageFrom(String filepath) throws Exception {
		return getBufferedImageFrom(filepath.startsWith("/") ? filepath : ("/"+filepath), false);
	}

	/**
	 * @param filepath
	 * @param absolutePath
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage getBufferedImageFrom(String filepath, boolean absolutePath) throws Exception {
		URL url = ImageUtil.class.getResource(filepath);
		return (absolutePath) ? ImageIO.read(new File(filepath)) : ImageIO.read(url);
	}

	/**
	 * @param coverPath
	 * @param coverSize
	 * @param option
	 *            an integer specifying the scaling options to use:
	 *            <code>CoverConstants.SCALE_AUTO_OPTION</code>,
	 *            <code>CoverConstants.SCALE_WIDTH_OPTION</code>,
	 *            <code>CoverConstants.SCALE_HEIGHT_OPTION</code>,
	 *            <code>CoverConstants.SCALE_BOTH_OPTION</code>
	 * @exception IllegalArgumentException
	 *                if <code>newType</code> is not one of the legal values
	 *                listed above
	 * @return
	 */
	public static ImageIcon scaleCover(String coverPath, boolean absolutePath, int coverSize, int option) {
		URL url = ImageUtil.class.getResource(coverPath);
		ImageIcon icon = (absolutePath) ? new ImageIcon(coverPath) : new ImageIcon(url);
		return scaleCover(icon, coverSize, option);
	}

	/**
	 * @param icon
	 * @param coverSize
	 * @param option
	 *            an integer specifying the scaling options to use:
	 *            <code>CoverConstants.SCALE_AUTO_OPTION</code>,
	 *            <code>CoverConstants.SCALE_WIDTH_OPTION</code>,
	 *            <code>CoverConstants.SCALE_HEIGHT_OPTION</code>,
	 *            <code>CoverConstants.SCALE_BOTH_OPTION</code>
	 * @exception IllegalArgumentException
	 *                if <code>newType</code> is not one of the legal values
	 *                listed above
	 * @return
	 */
	public static ImageIcon scaleCover(ImageIcon icon, int coverSize, int option) {
		int width = icon.getImage().getWidth(null);
		int height = icon.getImage().getHeight(null);

		float scaledWidth = width;
		float scaledHeight = height;

		if (option == CoverConstants.SCALE_BOTH_OPTION) {
			scaledWidth = coverSize;
			scaledHeight = coverSize;
		} else {
			float scaleFactor = 0;

			if (option == CoverConstants.SCALE_AUTO_OPTION) {
				scaleFactor = (width < height) ? (float) width / (float) coverSize : (float) height / (float) coverSize;
			} else if (option == CoverConstants.SCALE_WIDTH_OPTION) {
				scaleFactor = (float) width / (float) coverSize;
			} else if (option == CoverConstants.SCALE_HEIGHT_OPTION) {
				scaleFactor = (float) height / (float) coverSize;
			} else {
				throw new IllegalArgumentException("option must be one of " + "CoverConstants.SCALE_AUTO_OPTION, "
						+ "CoverConstants.SCALE_WIDTH_OPTION, " + "CoverConstants.SCALE_HEIGHT_OPTION, "
						+ "CoverConstants.SCALE_BOTH_OPTION");
			}
			scaledWidth = width / scaleFactor;
			scaledHeight = height / scaleFactor;
		}

		if (scaledWidth != width || scaledHeight != height) {
			Image image = icon.getImage().getScaledInstance((int) scaledWidth, (int) scaledHeight, Image.SCALE_SMOOTH);
			icon = new ImageIcon(image);
		}

		return icon;
	}

	public static Image scaleCover(Image image, int coverSize, int option) {
		int width = image.getWidth(null);
		int height = image.getHeight(null);

		float scaledWidth = width;
		float scaledHeight = height;

		if (option == CoverConstants.SCALE_BOTH_OPTION) {
			scaledWidth = coverSize;
			scaledHeight = coverSize;
		} else {
			float scaleFactor = 0;

			if (option == CoverConstants.SCALE_AUTO_OPTION) {
				scaleFactor = (width < height) ? (float) width / (float) coverSize : (float) height / (float) coverSize;
			} else if (option == CoverConstants.SCALE_WIDTH_OPTION) {
				scaleFactor = (float) width / (float) coverSize;
			} else if (option == CoverConstants.SCALE_HEIGHT_OPTION) {
				scaleFactor = (float) height / (float) coverSize;
			} else {
				throw new IllegalArgumentException("option must be one of " + "CoverConstants.SCALE_AUTO_OPTION, "
						+ "CoverConstants.SCALE_WIDTH_OPTION, " + "CoverConstants.SCALE_HEIGHT_OPTION, "
						+ "CoverConstants.SCALE_BOTH_OPTION");
			}
			scaledWidth = width / scaleFactor;
			scaledHeight = height / scaleFactor;
		}

		if (scaledWidth != width || scaledHeight != height) {
			image = image.getScaledInstance((int) scaledWidth, (int) scaledHeight, Image.SCALE_SMOOTH);
		}

		return image;
	}

	/**
	 * Converts a given Image into a BufferedImage
	 *
	 * @param img The Image to be converted
	 * @return The converted BufferedImage
	 */
	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}

	public static Image getBufferedImageFrom(InputStream is) throws IOException {
		return ImageIO.read(is);
	}

	public static Image getImageFromClipboard() {
		return getImageFromClipboard(Toolkit.getDefaultToolkit().getSystemClipboard());
	}

	public static Image getImageFromClipboard(Clipboard source) {
		Transferable transferable = source.getContents(null);
		if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
			Image img;
			try {
				img = (Image) transferable.getTransferData(DataFlavor.imageFlavor);
				return img;
			} catch (UnsupportedFlavorException | IOException e) {
				return null;
			}
		}
		return null;
	}

	public static boolean hasClipboardImageChanged(BufferedImage oldImage, BufferedImage newImage) {
		if (oldImage == null && newImage != null) {
			return true;
		}
		if (newImage == null) {
			throw new IllegalArgumentException("newImage must not be null");
		}
		// The images must be the same size.
		if (oldImage.getWidth() != newImage.getWidth() || oldImage.getHeight() != newImage.getHeight()) {
			return true;
		}
		int width = oldImage.getWidth();
		int height = oldImage.getHeight();
		// Loop over every pixel.
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// Compare the pixels for equality.
				if (oldImage.getRGB(x, y) != newImage.getRGB(x, y)) {
					return true;
				}
			}
		}
		return false;
	}
}