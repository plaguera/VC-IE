package util;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import image.Frame;
import imageFrame.ImageFrame;
import pane.linearTransformation.FunctionSegment;
import pane.linearTransformation.Node;

public class ImageUtils {

	public static BufferedImage readImage(String file) {
		BufferedImage in = null;
		try {
			in = ImageIO.read(new File(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ImageUtils.changeImageType(in, BufferedImage.TYPE_INT_ARGB);
	}
	
	public static BufferedImage changeImageType(BufferedImage image, int type) {
		BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), type);
		Graphics2D g = out.createGraphics();
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
		g.dispose();
		return out;
	}
	
	public static BufferedImage emptyImage(BufferedImage image) {
		return new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
	}

	public static File openImage() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Select an image");
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG and BMP images", "png", "bmp", "tif", "jpg");
		jfc.addChoosableFileFilter(filter);
		int returnValue = jfc.showOpenDialog(null);
		File file = null;
		if (returnValue == JFileChooser.APPROVE_OPTION)
			file = jfc.getSelectedFile();
		return file;
	}

	public static String getNameFromPath(String path) {
		File file = new File(path);
		return file.getName();
	}

	public static String getExtFromName(String filename) {
		try {
			return filename.substring(filename.lastIndexOf(".") + 1);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static BufferedImage copyImage(BufferedImage original) {
		BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g.drawImage(original, 0, 0, null);
		g.dispose();
		return image;
	}

	public static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	public static List<FunctionSegment> returnSegments(List<Node> nodes) {
		Collections.sort(nodes);
		List<FunctionSegment> list = new ArrayList<FunctionSegment>();
		for (int i = 1; i < nodes.size(); i++) {
			Node node1 = nodes.get(i - 1);
			Node node2 = nodes.get(i);
			list.add(new FunctionSegment(node1.getCoordinates(), node2.getCoordinates()));
		}
		Collections.sort(list);
		return list;
	}

	public static BufferedImage changeBrightness(BufferedImage original, float val) {
		RescaleOp brighterOp = new RescaleOp(val, 0, null);
		BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
		Graphics g = image.getGraphics();
		g.drawImage(original, 0, 0, null);
		g.dispose();
		return brighterOp.filter(image, null);
	}

	public static BufferedImage changeContrastBrightness(BufferedImage image, int brightness, float contrast) {
		RescaleOp rescale = new RescaleOp(contrast, brightness, null);
		return rescale.filter(image, null);
	}

	public static BufferedImage formImage(int[][] red, int[][] green, int[][] blue) {
		int width = red.length;
		int height = red[0].length;
		BufferedImage dst = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < width; i++)
			for(int j = 0; j < height; j++)
				dst.setRGB(i, j, util.Color.rgbToInt(red[i][j], green[i][j], blue[i][j]));
		return dst;
	}

	public static void launchFrame(Frame frame) {
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	public static ImageFrame getImageFrame(Component c) {
		JFrame frame = (JFrame) SwingUtilities.getRoot(c);
		if (frame instanceof ImageFrame)
			return (ImageFrame) frame;
		return null;
	}

}
