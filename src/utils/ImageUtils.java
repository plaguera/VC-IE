package utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
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
		BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = out.createGraphics();
		g.drawImage(in, 0, 0, in.getWidth(), in.getHeight(), null);
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

	public static Dimension scaleImage(Dimension image) {
		double scale = 1.0;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension bounds = new Dimension((int) (screen.getWidth() * 0.75), (int) (screen.getHeight() * 0.75));
		while (image.getWidth() > bounds.getWidth() || image.getHeight() > bounds.getHeight()) {
			scale -= 0.05;
			int width = (int) (image.getWidth() * scale);
			int height = (int) (image.getHeight() * scale);
			image = new Dimension(width, height);
		}
		return image;
	}

	public static String getNameFromPath(String path) {
		File file = new File(path);
		return file.getName();
	}

	public static String getExtFromName(String filename) {
		try {
			return filename.substring(filename.lastIndexOf(".") + 1);
		} catch (Exception e) {
			return "";
		}
	}

	public static void rgbToGrayscale(BufferedImage image) {
		for (int i = 0; i < image.getWidth(); i++)
			for (int j = 0; j < image.getHeight(); j++) {
				Color color = new Color(image.getRGB(i, j));
				int red = (int) (color.getRed() * 0.299);
				int green = (int) (color.getGreen() * 0.587);
				int blue = (int) (color.getBlue() * 0.114);
				Color newColor = new Color(red + green + blue, red + green + blue, red + green + blue);
				image.setRGB(i, j, newColor.getRGB());
			}
	}

	public static BufferedImage rgbToGrayscaleCopy(BufferedImage original) {
		BufferedImage image = copyImage(original);
		for (int i = 0; i < image.getWidth(); i++)
			for (int j = 0; j < image.getHeight(); j++) {
				Color color = new Color(image.getRGB(i, j));
				int red = (int) (color.getRed() * 0.299);
				int green = (int) (color.getGreen() * 0.587);
				int blue = (int) (color.getBlue() * 0.114);
				Color newColor = new Color(red + green + blue, red + green + blue, red + green + blue);
				image.setRGB(i, j, newColor.getRGB());
			}
		return image;
	}

	public static BufferedImage copyImage(BufferedImage original) {
		BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g.drawImage(original, 0, 0, null);
		g.dispose();
		return image;
	}

	public static BufferedImage rgbToGrayscaleCopyAuto(BufferedImage original) {
		BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(),
				BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = image.getGraphics();
		g.drawImage(original, 0, 0, null);
		g.dispose();
		return image;
	}

	public static BufferedImage linearTransform(BufferedImage original, List<FunctionSegment> f) {
		BufferedImage image = rgbToGrayscaleCopyAuto(original);
		for (int row = 0; row < image.getHeight(); row++) {
			for (int col = 0; col < image.getWidth(); col++) {
				int value = ColorUtils.intToRGB(image.getRGB(col, row))[0];
				for (FunctionSegment segment : f) {
					if (segment.getP1().getX() <= value) {
						value = ImageUtils.truncate((int) segment.f(value));
						break;
					}
				}

				image.setRGB(col, row, ColorUtils.rgbToInt(value, value, value));
			}
		}
		return image;
	}

	public static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	public static boolean isGrayscale(int[][] data) {
		int r = 0, g = 0, b = 0;
		while (r < 256 && g < 256 && b < 256) {
			if (data[0][r] != data[1][g] || data[1][g] != data[2][b] || data[0][r] != data[2][b])
				return false;
			r++;
			g++;
			b++;
		}
		return true;
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
		int[] c = ColorUtils.intToRGB(color);
		int r = (int) (c[0] * NTSC_RED);
		int g = (int) (c[1] * NTSC_GREEN);
		int b = (int) (c[2] * NTSC_BLUE);
		return (int) (r+g+b);
	}

	public static int gamma(int color, double gamma) {
		int[] rgb = ColorUtils.intToRGB(color);
		double gammaCorrection = 1 / gamma;
		int r = (int) (255 * Math.pow((double) (rgb[0] / 255d), gammaCorrection));
		int g = (int) (255 * Math.pow((double) (rgb[1] / 255d), gammaCorrection));
		int b = (int) (255 * Math.pow((double) (rgb[2] / 255d), gammaCorrection));
		return ColorUtils.rgbToInt(r, g, b);
	}

	public static int brighten(int color, int offset) {
		int[] rgb = ColorUtils.intToRGB(color);
		rgb[0] = truncate(rgb[0] + offset);
		rgb[1] = truncate(rgb[1] + offset);
		rgb[2] = truncate(rgb[2] + offset);
		return ColorUtils.rgbToInt(rgb[0], rgb[1], rgb[2]);
	}

	public static int contrast(int color, float contrast) {
		float factor = (259 * (contrast + 255)) / (255 * (259 - contrast));
		int[] rgb = ColorUtils.intToRGB(color);
		rgb[0] = (int) truncate(factor * (rgb[0] - 128) + 128);
		rgb[1] = (int) truncate(factor * (rgb[1] - 128) + 128);
		rgb[2] = (int) truncate(factor * (rgb[2] - 128) + 128);
		return ColorUtils.rgbToInt(rgb[0], rgb[1], rgb[2]);
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
		// frame.setResizable(false);
		// Fullscreen.enableOSXFullscreen(frame);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		// frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		// Fullscreen.requestToggleFullScreen(frame);
	}

	public static BufferedImage subImage(BufferedImage image, int x, int y, int w, int h) {
		return image.getSubimage(x, y, w, h);
	}

	public static ImageFrame getImageFrame(Component c) {
		JFrame frame = (JFrame) SwingUtilities.getRoot(c);
		if (frame instanceof ImageFrame)
			return (ImageFrame) frame;
		return null;
	}

}
