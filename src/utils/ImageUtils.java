package utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Kernel;
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

	public static final double NTSC_RED = 0.2126;
	public static final double NTSC_GREEN = 0.7152;
	public static final double NTSC_BLUE = 0.0722;

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

	public static double contrast(BufferedImage image) {
		int[] aux = new int[image.getWidth() * image.getHeight()];
		int k = 0;
		for (int row = 0; row < image.getHeight(); row++)
			for (int col = 0; col < image.getWidth(); col++)
				aux[k++] = ImageUtils.brightness(image.getRGB(col, row));
		double sum = 0.0, standardDeviation = 0.0;
		for (int num : aux)
			sum += num;
		double mean = sum / aux.length;

		for (int num : aux)
			standardDeviation += Math.pow(num - mean, 2);
		return Math.sqrt(standardDeviation / aux.length);
	}

	public static double brightness(BufferedImage image) {
		int sum = 0;
		int total = image.getWidth() * image.getHeight();
		for (int row = 0; row < image.getHeight(); row++) {
			for (int col = 0; col < image.getWidth(); col++) {
				sum += ImageUtils.brightness(image.getRGB(col, row));
			}
		}
		return sum / total;
	}

	public static int brightness(Color c) {
		int r = (int) (c.getRed() * c.getRed() * NTSC_RED);
		int g = (int) (c.getGreen() * c.getGreen() * NTSC_GREEN);
		int b = (int) (c.getBlue() * c.getBlue() * NTSC_BLUE);
		return (int) Math.sqrt(r + g + b);
	}

	public static int brightness(int color) {
		int[] c = utils.Color.intToRGB(color);
		int r = (int) (c[0] * NTSC_RED);
		int g = (int) (c[1] * NTSC_GREEN);
		int b = (int) (c[2] * NTSC_BLUE);
		return (int) (r+g+b);
	}

	public static int gamma(int color, double gamma) {
		int[] rgb = utils.Color.intToRGB(color);
		double gammaCorrection = 1 / gamma;
		int r = (int) (255 * Math.pow((double) (rgb[0] / 255d), gammaCorrection));
		int g = (int) (255 * Math.pow((double) (rgb[1] / 255d), gammaCorrection));
		int b = (int) (255 * Math.pow((double) (rgb[2] / 255d), gammaCorrection));
		return utils.Color.rgbToInt(r, g, b);
	}

	public static int brighten(int color, int offset) {
		int[] rgb = utils.Color.intToRGB(color);
		rgb[0] = truncate(rgb[0] + offset);
		rgb[1] = truncate(rgb[1] + offset);
		rgb[2] = truncate(rgb[2] + offset);
		return utils.Color.rgbToInt(rgb[0], rgb[1], rgb[2]);
	}

	public static int contrast(int color, float contrast) {
		float factor = (259 * (contrast + 255)) / (255 * (259 - contrast));
		int[] rgb = utils.Color.intToRGB(color);
		rgb[0] = (int) truncate(factor * (rgb[0] - 128) + 128);
		rgb[1] = (int) truncate(factor * (rgb[1] - 128) + 128);
		rgb[2] = (int) truncate(factor * (rgb[2] - 128) + 128);
		return utils.Color.rgbToInt(rgb[0], rgb[1], rgb[2]);
	}

	public static int truncate(int value) {
		if (value < 0)
			value = 0;
		else if (value > 255)
			value = 255;
		return value;
	}

	public static double truncate(double value) {
		if (value < 0)
			value = 0;
		else if (value > 255)
			value = 255;
		return value;
	}
	
	public static float[][] doubleArrayDimension(Kernel kernel) {
		int kernelWidth = kernel.getWidth();
		int kernelHeight = kernel.getHeight();

		float[] aux = kernel.getKernelData(null);
		float[][] data = new float[kernelHeight][kernelWidth];

		for (int i = 0; i < aux.length; i++) {
			int row = i / kernelWidth;
			int col = i % kernelWidth;
			data[row][col] = aux[i];
		}/*
		for(int i = 0; i < kernelHeight; i++) {
			for(int j = 0; j < kernelWidth; j++)
				System.out.print(data[i][j] + " ");
			System.out.println();
		}
		for (float[] row : data) {
			for (float i : row)
				System.out.print(i + " ");
			System.out.println();
		}*/
		return data;
	}
	
	public static BufferedImage formImage(int[][] red, int[][] green, int[][] blue) {
		int width = red.length;
		int height = red[0].length;
		BufferedImage dst = new BufferedImage(red.length, height, BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				dst.setRGB(i, j, new Color(red[i][j], green[i][j], blue[i][j]).getRGB());
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
