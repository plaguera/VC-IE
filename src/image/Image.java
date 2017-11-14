package image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import pane.linearTransformation.FunctionSegment;
import utils.ColorUtils;
import utils.DLL;
import utils.ImageUtils;

public class Image extends Observable {

	private BufferedImage original;
	private DLL<BufferedImage> dll;
	private String path;

	public Image(String path) {
		setPath(path);
		setDll(new DLL<BufferedImage>(ImageUtils.readImage(getPath())));
		setOriginal(ImageUtils.readImage(getPath()));
	}

	public Image(Image image) {
		setPath(image.getPath());
		setDll(new DLL<BufferedImage>(get()));
		setOriginal(ImageUtils.copyImage(image.get()));
	}

	public Image(BufferedImage image, String path) {
		setPath(path);
		setDll(new DLL<BufferedImage>(get()));
		setOriginal(ImageUtils.copyImage(image));
	}

	public Dimension getSize() {
		return new Dimension(get().getWidth(), get().getHeight());
	}

	public String getFileName() {
		if (getPath() == null)
			return "";
		return ImageUtils.getNameFromPath(getPath());
	}

	public String getFormat() {
		return ImageUtils.getExtFromName(getFileName());
	}

	public String getResolution() {
		return get().getWidth() + "x" + get().getHeight();
	}

	public void subimage(int x, int y, int w, int h) {
		getDll().add(get().getSubimage(x, y, w, h));
		changed();
	}

	public void commit() {
		getDll().commit();
	}

	public void ctrlZ() {
		getDll().backward();
		changed();
	}

	public void ctrlY() {
		getDll().forward();
		changed();
	}

	public void toGrayScale() {
		BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(),
				BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = image.getGraphics();
		g.drawImage(original, 0, 0, null);
		g.dispose();
		getDll().add(image);
		changed();
	}

	public void reset() {
		getDll().toBeginning();
		changed();
	}

	public int getWidth() {
		return get().getWidth();
	}

	public int getHeight() {
		return get().getHeight();
	}

	public void adjustment(double brightness, double contrast) {
		BufferedImage image = ImageUtils.copyImage(lastCommit());

		double A = contrast / ImageUtils.contrast(lastCommit());
		double B = brightness - ImageUtils.brightness(lastCommit()) * A;
		for (int row = 0; row < get().getHeight(); row++) {
			for (int col = 0; col < get().getWidth(); col++) {
				int[] rgb = ColorUtils.intToRGB(lastCommit().getRGB(col, row));
				rgb[0] = ImageUtils.truncate((int) (rgb[0] * A + B));
				rgb[1] = ImageUtils.truncate((int) (rgb[1] * A + B));
				rgb[2] = ImageUtils.truncate((int) (rgb[2] * A + B));
				image.setRGB(col, row, ColorUtils.rgbToInt(rgb[0], rgb[1], rgb[2]));
			}
		}
		getDll().add(image);
		changed();
	}

	public void gamma(double gamma) {
		BufferedImage image = ImageUtils.copyImage(lastCommit());
		RGB[][] lut = new LUT(lastCommit()).getLut();
		for (int row = 0; row < get().getHeight(); row++)
			for (int col = 0; col < get().getWidth(); col++) {
				RGB i = lut[col][row].gamma(gamma);
				image.setRGB(col, row, i.toInt());
			}
		getDll().add(image);
		changed();
	}

	public void linearTransform(List<FunctionSegment> f) {
		BufferedImage image = ImageUtils.copyImage(lastCommit());
		for (int row = 0; row < get().getHeight(); row++) {
			for (int col = 0; col < get().getWidth(); col++) {
				int[] rgb = ColorUtils.intToRGB(lastCommit().getRGB(col, row));
				for (int i = 0; i < rgb.length; i++) {
					for (FunctionSegment segment : f) {
						if (segment.contains(rgb[i])) {
							rgb[i] = ImageUtils.truncate((int) segment.f(rgb[i]));
							break;
						}
					}
				}
				image.setRGB(col, row, ColorUtils.rgbToInt(rgb[0], rgb[1], rgb[2]));
			}
		}
		getDll().add(image);
		changed();
	}

	public void downsample(int sampleSize) {
		BufferedImage image = ImageUtils.copyImage(lastCommit());

		for (int row = 0; row < image.getHeight(); row += sampleSize) {
			for (int col = 0; col < image.getWidth(); col += sampleSize) {
				RGB[][] sample = null;
				int width = 0, height = 0;
				if (row + sampleSize >= image.getHeight())
					width = Math.abs(sampleSize - (image.getHeight() - row + sampleSize));
				else
					width = sampleSize;
				if (col + sampleSize >= image.getWidth())
					height = Math.abs(sampleSize - (image.getWidth() - col + sampleSize));
				else
					height = sampleSize;
				int side = Math.min(width, height);
				sample = new RGB[side][side];

				for (int i = 0; i < side; i++)
					for (int j = 0; j < side; j++)
						sample[i][j] = new RGB(image.getRGB(col + i, row + j));

				RGB average = RGB.average(sample);

				for (int i = col; i < col + side; i++)
					for (int j = row; j < row + side; j++)
						image.setRGB(i, j, average.toInt());
			}
		}
		getDll().add(image);
		changed();
	}

	public void changeColorDepth(int bits) {
		BufferedImage image = ImageUtils.copyImage(lastCommit());
		int colors = (int) Math.pow(2, bits);
		int length = 256 / colors;
		for (int row = 0; row < lastCommit().getHeight(); row++) {
			for (int col = 0; col < lastCommit().getWidth(); col++) {
				int preColor = lastCommit().getRGB(col, row);
				RGB preRGB = new RGB(preColor);
				preRGB.approxMod(length);
				image.setRGB(col, row, preRGB.toInt());
			}
		}
		getDll().add(image);
		changed();
	}

	public Image difference(BufferedImage newImage) {
		if (getWidth() != newImage.getWidth() || getHeight() != newImage.getHeight())
			return null;
		BufferedImage result = ImageUtils.copyImage(newImage);
		for (int row = 0; row < result.getHeight(); row++) {
			for (int col = 0; col < result.getWidth(); col++) {
				RGB a = new RGB(newImage.getRGB(col, row));
				RGB color = get(col, row).absDifference(a);
				result.setRGB(col, row, color.toInt());
			}
		}
		return new Image(result, getPath());
	}

	public Image colorChangeMap(int threshold) {
		BufferedImage aux = ImageUtils.copyImage(get());
		RGB[][] rgb = new LUT(aux).getLut();
		for (int row = 0; row < get().getHeight(); row++)
			for (int col = 0; col < get().getWidth(); col++)
				if (rgb[col][row].gray() >= threshold)
					aux.setRGB(col, row, Color.RED.getRGB());
		return new Image(aux, getPath());
	}

	public void flipHorizontally() {
		BufferedImage image = ImageUtils.copyImage(get());
		for (int row = 0; row < image.getHeight(); row++) {
			for (int col = 0; col < image.getWidth(); col++) {
				int color = get().getRGB(col, row);
				image.setRGB((image.getWidth() - col - 1), row, color);
			}
		}
		getDll().add(image);
		changed();
	}

	public void flipVertically() {
		BufferedImage image = ImageUtils.copyImage(get());
		for (int row = 0; row < image.getHeight(); row++) {
			for (int col = 0; col < image.getWidth(); col++) {
				int color = get().getRGB(col, row);
				image.setRGB(col, (image.getHeight() - row - 1), color);
			}
		}
		getDll().add(image);
		changed();
	}

	public void transpose() {
		BufferedImage image = new BufferedImage(get().getHeight(), get().getWidth(), get().getType());
		for (int row = 0; row < get().getHeight(); row++) {
			for (int col = 0; col < get().getWidth(); col++) {
				int color = get().getRGB(col, row);
				image.setRGB(row, col, color);
			}
		}
		getDll().add(image);
		changed();
	}

	public void rotate(int angle) {
		if (angle % 90 != 0)
			return;
		int n = angle / 90;
		int i = n % 4;
		switch (i) {
		case 0:
			break;
		case 1:
			transpose();
			flipHorizontally();
			break;
		case 2:
			flipVertically();
			break;
		case 3:
			transpose();
			break;
		}
	}

	public void scale(double percentage) {
		int newW = (int) (percentage * lastCommit().getWidth());
		int newH = (int) (percentage * lastCommit().getHeight());
		java.awt.Image img = lastCommit().getScaledInstance(newW, newH, java.awt.Image.SCALE_DEFAULT);
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		getDll().add(bimage);
		changed();
	}

	public double brightness() {
		int sum = 0;
		int total = get().getWidth() * get().getHeight();
		for (int row = 0; row < get().getHeight(); row++)
			for (int col = 0; col < get().getWidth(); col++)
				sum += ImageUtils.brightness(get().getRGB(col, row));
		return (double) sum / (double) total;
	}

	public double contrast() {
		int[] aux = new int[get().getWidth() * get().getHeight()];
		int k = 0;
		for (int row = 0; row < get().getHeight(); row++)
			for (int col = 0; col < get().getWidth(); col++)
				aux[k++] = ImageUtils.brightness(get().getRGB(col, row));

		double sum = 0.0, standardDeviation = 0.0;
		for (int num : aux)
			sum += num;

		double mean = (double) sum / (double) aux.length;
		for (int num : aux)
			standardDeviation += Math.pow(num - mean, 2);

		return Math.sqrt((double) standardDeviation / (double) aux.length);
	}

	public double shannonEntropy() {
		double[] normalized = new LUT(get()).normalizedCount();
		double entropy = 0.0d;
		for (int i = 0; i < normalized.length; i++)
			entropy -= normalized[i] * log2(normalized[i]);
		return entropy;
	}

	public int dynamicRange() {
		RGB[][] lut = new LUT(get()).getLut();
		List<Integer> aux = new ArrayList<Integer>();
		for (int i = 0; i < get().getWidth(); i++)
			for (int j = 0; j < get().getHeight(); j++)
				if (!aux.contains(lut[i][j].gray()))
					aux.add(lut[i][j].gray());
		return aux.size();
	}

	public Point grayRange() {
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		RGB[][] lut = new LUT(get()).getLut();
		for (int i = 0; i < get().getWidth(); i++)
			for (int j = 0; j < get().getHeight(); j++) {
				int gray = lut[i][j].gray();
				if (gray > max)
					max = gray;
				if (gray < min)
					min = gray;
			}
		return new Point(min, max);
	}

	public RGB get(int x, int y) {
		return new RGB(get().getRGB(x, y));
	}

	public void changed() {
		setChanged();
		notifyObservers();
		System.out.println(getDll());
	}

	public static double log2(double value) {
		if (value == 0)
			return 0;
		return Math.log(value) / Math.log(2);
	}

	public BufferedImage get() {
		return dll.getCurrent().getValue();
	}

	public String getPath() {
		return path;
	}

	public BufferedImage getOriginal() {
		return original;
	}

	public DLL<BufferedImage> getDll() {
		return dll;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setOriginal(BufferedImage original) {
		this.original = original;
	}

	public void setDll(DLL<BufferedImage> dll) {
		this.dll = dll;
	}

	private BufferedImage lastCommit() {
		return getDll().lastCommit().getValue();
	}

}
