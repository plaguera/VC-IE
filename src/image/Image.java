package image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.List;
import java.util.Observable;

import pane.linearTransformation.FunctionSegment;
import rotation.RotateAndPaint;
import rotation.RotateBilinearInterpolation;
import rotation.RotateNearestNeighbour;
import rotation.Rotation;
import util.DLL;
import util.ImageUtils;
import util.MathUtil;

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
		getDll().add(ImageUtils.changeImageType(get(), BufferedImage.TYPE_BYTE_GRAY));
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

		double A = contrast / util.Color.contrast(lastCommit());
		double B = brightness - util.Color.brightness(lastCommit()) * A;
		for (int row = 0; row < get().getHeight(); row++) {
			for (int col = 0; col < get().getWidth(); col++) {
				int[] rgb = util.Color.intToRGB(lastCommit().getRGB(col, row));
				rgb[0] = MathUtil.truncate((int) (rgb[0] * A + B));
				rgb[1] = MathUtil.truncate((int) (rgb[1] * A + B));
				rgb[2] = MathUtil.truncate((int) (rgb[2] * A + B));
				image.setRGB(col, row, util.Color.rgbToInt(rgb[0], rgb[1], rgb[2]));
			}
		}
		getDll().add(image);
		changed();
	}

	public void gamma(double gamma) {
		BufferedImage image = ImageUtils.copyImage(lastCommit());
		for (int col = 0; col < get().getWidth(); col++) {
			for (int row = 0; row < get().getHeight(); row++) {
				int value = util.Color.gamma(image.getRGB(col, row), gamma);
				image.setRGB(col, row, value);
			}
		}
		getDll().add(image);
		changed();
	}

	public void linearTransform(List<FunctionSegment> f) {
		BufferedImage image = ImageUtils.copyImage(lastCommit());
		for (int row = 0; row < get().getHeight(); row++) {
			for (int col = 0; col < get().getWidth(); col++) {
				int[] rgb = util.Color.intToRGB(lastCommit().getRGB(col, row));
				for (int i = 0; i < rgb.length; i++) {
					for (FunctionSegment segment : f) {
						if (segment.contains(rgb[i])) {
							rgb[i] = MathUtil.truncate((int) segment.f(rgb[i]));
							break;
						}
					}
				}
				image.setRGB(col, row, util.Color.rgbToInt(rgb[0], rgb[1], rgb[2]));
			}
		}
		getDll().add(image);
		changed();
	}

	public void equalize() {
		BufferedImage image = ImageUtils.copyImage(get());
		int[][] result = equalizeMatrix(util.Color.cumulativeGray(image), util.Color.grayMatrix(image));
		for (int i = 0; i < result.length; i++)
			for (int j = 0; j < result[i].length; j++)
				image.setRGB(i, j, new Color(result[i][j], result[i][j], result[i][j]).getRGB());
		getDll().add(image);
		changed();
	}

	public void equalizeRGB() {
		BufferedImage image = ImageUtils.copyImage(get());
		int[][] red = util.Color.redMatrix(image);
		int[][] green = util.Color.greenMatrix(image);
		int[][] blue = util.Color.blueMatrix(image);
		int[] redAcc = util.Color.cumulativeRed(image);
		int[] greenAcc = util.Color.cumulativeGreen(image);
		int[] blueAcc = util.Color.cumulativeBlue(image);
		int[][] resultR = equalizeMatrix(redAcc, red);
		int[][] resultG = equalizeMatrix(greenAcc, green);
		int[][] resultB = equalizeMatrix(blueAcc, blue);
		for (int i = 0; i < resultR.length; i++)
			for (int j = 0; j < resultR[i].length; j++)
				image.setRGB(i, j, util.Color.rgbToInt(resultR[i][j], resultG[i][j], resultB[i][j]));
		getDll().add(image);
		changed();
	}

	public void specify(String file) {
		Image desiredImage = new Image(file);
		
		int[] r = util.Color.specify(	util.Color.cumulativeNormalizedRed(get()),
										util.Color.cumulativeNormalizedRed(desiredImage.get()));
		int[] g = util.Color.specify(	util.Color.cumulativeNormalizedGreen(get()),
										util.Color.cumulativeNormalizedGreen(desiredImage.get()));
		int[] b = util.Color.specify(	util.Color.cumulativeNormalizedBlue(get()),
										util.Color.cumulativeNormalizedBlue(desiredImage.get()));

		BufferedImage image = ImageUtils.copyImage(get());
		for (int col = 0; col < image.getWidth(); col++) {
			for (int row = 0; row < image.getHeight(); row++) {
				int color = image.getRGB(col, row);
				color = util.Color.rgbToInt(	r[util.Color.red(color)],
												g[util.Color.green(color)],
												b[util.Color.blue(color)]);
				image.setRGB(col, row, color);
			}
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
				int[][] sample = null;
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
				sample = new int[side][side];

				for (int i = 0; i < side; i++)
					for (int j = 0; j < side; j++)
						sample[i][j] = image.getRGB(col + i, row + j);

				int average = util.Color.average(sample);

				for (int i = col; i < col + side; i++)
					for (int j = row; j < row + side; j++)
						image.setRGB(i, j, average);
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
				int color = util.Color.approx(preColor, length);
				image.setRGB(col, row, color);
			}
		}
		getDll().add(image);
		changed();
	}

	public Image difference(BufferedImage newImage) {
		if (getWidth() != newImage.getWidth() || getHeight() != newImage.getHeight())
			return null;
		BufferedImage result = ImageUtils.copyImage(newImage);
		for (int col = 0; col < result.getWidth(); col++) {
			for (int row = 0; row < result.getHeight(); row++) {
				int diff = util.Color.absDifference(newImage.getRGB(col, row), get().getRGB(col, row));
				result.setRGB(col, row, diff);
			}
		}
		return new Image(result, getPath());
	}

	public Image colorChangeMap(int threshold) {
		BufferedImage aux = ImageUtils.copyImage(get());
		int[][] gray = util.Color.grayMatrix(get());
		int diffColor = Color.RED.getRGB();
		for (int col = 0; col < get().getWidth(); col++)
			for (int row = 0; row < get().getHeight(); row++)
				if (gray[col][row] >= threshold)
					aux.setRGB(col, row, diffColor);
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
		changed();
	}
	
	public void rotate(double angle, int algorithm) {
		Rotation operation = null;
		switch(algorithm) {
			case Rotation.ROTATE_PAINT: operation = new RotateAndPaint(lastCommit()); break;
			case Rotation.NEAREST_NEIGHBOUR: operation = new RotateNearestNeighbour(lastCommit()); break;
			case Rotation.BILINEAR_INTERPOLATION: operation = new RotateBilinearInterpolation(lastCommit()); break;
			default: break;
		}
		getDll().add(operation.rotate(angle));
		changed();
	}

	public void convolute(Kernel kernel) {
		float[][] k = MathUtil.doubleArrayDimension(kernel);
		int[][] red = convoluteChannel(util.Color.redMatrix(get()), k);
		int[][] green = convoluteChannel(util.Color.greenMatrix(get()), k);
		int[][] blue = convoluteChannel(util.Color.blueMatrix(get()), k);
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
						newValue += kernel[kh][kw] * array[bound(i + kw - kernelWidthRadius, inputWidth)][bound(
								j + kh - kernelHeightRadius, inputHeight)];
				result[i][j] = MathUtil.truncate((int) Math.round(newValue / kernelDivisor));
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

	public void changed() {
		setChanged();
		notifyObservers();
		System.out.println(getDll());
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

	public BufferedImage lastCommit() {
		return getDll().lastCommit().getValue();
	}

}
