package image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import pane.linearTransformation.FunctionSegment;
import utils.ColorUtils;
import utils.DLL;
import utils.HistogramUtils;
import utils.ImageUtils;

public class Image extends Observable {

	private BufferedImage original;
	private DLL<BufferedImage> dll;
	private String path;

	public int blacks = 0;

	public Image(String path) {
		setPath(path);
		setDll(new DLL<BufferedImage>(ImageUtils.readImage(getPath())));
		setOriginal(ImageUtils.readImage(getPath()));
	}

	public Image(Image image) {
		setPath(image.getPath());
		setDll(new DLL<BufferedImage>(image.get()));
		setOriginal(ImageUtils.copyImage(image.get()));
	}

	public Image(BufferedImage image, String path) {
		setPath(path);
		setDll(new DLL<BufferedImage>(image));
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

	public void equalize() {
		BufferedImage image = ImageUtils.copyImage(get());
		LUT lut = new LUT(image);
		int[][] gray = lut.getGrayMatrix();
		int[] grayAcc = lut.cumulativeCount();
		int[][] result = equalizeMatrix(grayAcc, gray);
		for (int i = 0; i < result.length; i++)
			for (int j = 0; j < result[i].length; j++)
				image.setRGB(i, j, new Color(result[i][j], result[i][j], result[i][j]).getRGB());
		getDll().add(image);
		changed();
	}

	public void equalizeRGB() {
		BufferedImage image = ImageUtils.copyImage(get());
		LUT lut = new LUT(image);
		int[][] red = lut.getRedMatrix();
		int[][] green = lut.getGreenMatrix();
		int[][] blue = lut.getBlueMatrix();
		int[] redAcc = lut.cumulativeRedCount();
		int[] greenAcc = lut.cumulativeGreenCount();
		int[] blueAcc = lut.cumulativeBlueCount();
		int[][] resultR = equalizeMatrix(redAcc, red);
		int[][] resultG = equalizeMatrix(greenAcc, green);
		int[][] resultB = equalizeMatrix(blueAcc, blue);
		for (int i = 0; i < resultR.length; i++)
			for (int j = 0; j < resultR[i].length; j++)
				image.setRGB(i, j, new Color(resultR[i][j], resultG[i][j], resultB[i][j]).getRGB());
		getDll().add(image);
		changed();
	}

	public void specify(String file) {
		Image desiredImage = new Image(file);
		LUT current = new LUT(get());
		LUT desired = new LUT(desiredImage.get());

		int[] r = HistogramUtils.specify(current.redCumulativeNormalizedCount(),
				desired.redCumulativeNormalizedCount());
		int[] g = HistogramUtils.specify(current.greenCumulativeNormalizedCount(),
				desired.greenCumulativeNormalizedCount());
		int[] b = HistogramUtils.specify(current.blueCumulativeNormalizedCount(),
				desired.blueCumulativeNormalizedCount());

		BufferedImage image = ImageUtils.copyImage(get());
		for (int row = 0; row < image.getHeight(); row++)
			for (int col = 0; col < image.getWidth(); col++) {
				RGB value = new RGB(image.getRGB(col, row));
				int newColor = new RGB(r[value.getRed()], g[value.getGreen()], b[value.getBlue()]).toInt();
				image.setRGB(col, row, newColor);
			}
		getDll().add(image);
		changed();
	}

	public static int[][] equalizeMatrix(int[] cumulative, int[][] color) {
		int L = cumulative.length;
		int a[] = new int[L];
		for (int i = 0; i < L; i++)
			a[i] = (int) Math.floor(((L - 1) * cumulative[i]) / (color[0].length * color.length));

		for (int i = 0; i < color.length; i++)
			for (int j = 0; j < color[i].length; j++)
				color[i][j] = a[color[i][j]];
		return color;
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

	public void scale(int width, int height, int hint) {
		int imageWidth = getWidth();
		int imageHeight = getHeight();

		double scaleX = (double) width / imageWidth;
		double scaleY = (double) height / imageHeight;
		AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
		AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, hint);

		getDll().add(bilinearScaleOp.filter(get(), new BufferedImage(width, height, get().getType())));
		changed();
	}

	public void rotateN(int degrees) {
		javaxt.io.Image image = new javaxt.io.Image(lastCommit());
		image.rotate(degrees);

		getDll().add(image.getBufferedImage());
		int aux = blacks;
		changed();
		blacks = aux;
		/*
		 * BufferedImage sourceBI = new BufferedImage(lastCommit().getWidth(null),
		 * lastCommit().getHeight(null),BufferedImage.TYPE_INT_ARGB);
		 * sourceBI.getGraphics().drawImage(lastCommit(), 0, 0, null); AffineTransform
		 * at = new AffineTransform(); at.rotate(degrees * Math.PI/180,
		 * sourceBI.getWidth()/2, sourceBI.getHeight()/2); BufferedImageOp bio = new
		 * AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		 * getDll().add(bio.filter(sourceBI, null));
		 */
	}

	public void convolute(Kernel kernel) {
		float[][] k = ImageUtils.doubleArrayDimension(kernel);
		LUT lut = new LUT(this);
		int[][] red = convoluteChannel(lut.getRedMatrix(), k);
		int[][] green = convoluteChannel(lut.getGreenMatrix(), k);
		int[][] blue = convoluteChannel(lut.getBlueMatrix(), k);
		getDll().add(ImageUtils.formImage(red, green, blue));
		changed();
	}
	
	private int[][] convoluteChannel(int[][] array, float[][] kernel) {
		int inputWidth = array.length;
		int inputHeight = array[0].length;
		int kernelHeight = kernel.length;
		int kernelWidth = kernel[0].length;
		int kernelWidthRadius = kernelWidth >>> 1;
		int kernelHeightRadius = kernelHeight >>> 1;
		int kernelDivisor = 1;
		
		if ((kernelWidth <= 0) || ((kernelWidth & 1) != 1))
			throw new IllegalArgumentException("Kernel must have odd width");
		if ((kernelHeight <= 0) || ((kernelHeight & 1) != 1))
			throw new IllegalArgumentException("Kernel must have odd height");
		
		int[][] result = new int[inputWidth][inputHeight];
		for (int i = inputWidth - 1; i >= 0; i--) {
			for (int j = inputHeight - 1; j >= 0; j--) {
				double newValue = 0.0;
				for (int kw = kernelWidth - 1; kw >= 0; kw--)
					for (int kh = kernelHeight - 1; kh >= 0; kh--)
						newValue += kernel[kh][kw] * array[bound(i + kw - kernelWidthRadius, inputWidth)][
								bound(j + kh - kernelHeightRadius, inputHeight)];
				result[i][j] = ImageUtils.truncate((int) Math.round(newValue / kernelDivisor));
			}
		}
		return result;
	}
	
	public void convolve(Kernel kernel) {
		BufferedImageOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		getDll().add(op.filter(lastCommit(), null));
		changed();
	}

	private static int bound(int value, int endIndex) {
		if (value < 0)
			return 0;
		if (value < endIndex)
			return value;
		return endIndex - 1;
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
		blacks = blacks();
	}

	public int blacks() {
		int count = 0;
		for (int row = 0; row < get().getHeight(); row++)
			for (int col = 0; col < get().getWidth(); col++)
				if (get().getRGB(col, row) == 0)
					count++;
		return count;
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
		this.blacks = blacks();
	}

	public BufferedImage lastCommit() {
		return getDll().lastCommit().getValue();
	}

}
