package utils;

import java.awt.image.BufferedImage;

public class Color {

	public static final double NTSC_RED = 0.299;
	public static final double NTSC_GREEN = 0.587;
	public static final double NTSC_BLUE = 0.114;

	public static final int RED = 1;
	public static final int GREEN = 2;
	public static final int BLUE = 3;
	public static final int GRAY = 4;

	public static final int N_COLORS = 256;
	
	public static int[][] matrix(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] matrix = new int[width][height];

		for (int j = 0; j < width; j++)
			for (int i = 0; i < height; i++)
				matrix[j][i] = image.getRGB(i, j);
		return matrix;
	}

	public static int[][] matrix(BufferedImage image, int color) {
		switch (color) {
		case RED:
			return redMatrix(image);
		case GREEN:
			return greenMatrix(image);
		case BLUE:
			return blueMatrix(image);
		case GRAY:
			return grayMatrix(image);
		default:
			return null;
		}
	}

	public static int[][] redMatrix(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] result = new int[width][height];
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				if (Color.alpha(image.getRGB(i, j)) == 0)
					result[i][j] = -1;
				else
					result[i][j] = Color.red(image.getRGB(i, j));
			}
		return result;
	}

	public static int[][] greenMatrix(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] result = new int[width][height];
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				if (Color.alpha(image.getRGB(i, j)) == 0)
					result[i][j] = -1;
				else
					result[i][j] = Color.green(image.getRGB(i, j));
			}
		return result;
	}

	public static int[][] blueMatrix(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] result = new int[width][height];
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				if (Color.alpha(image.getRGB(i, j)) == 0)
					result[i][j] = -1;
				else
					result[i][j] = Color.blue(image.getRGB(i, j));
			}
		return result;
	}

	public static int[][] grayMatrix(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] result = new int[width][height];
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				if (Color.alpha(image.getRGB(i, j)) == 0)
					result[i][j] = -1;
				else
					result[i][j] = Color.gray(image.getRGB(i, j));
			}
		return result;
	}

	// COUNT FUNCTIONS
	public static int[] count(BufferedImage image, int color) {
		switch (color) {
		case RED:
			return countRed(image);
		case GREEN:
			return countGreen(image);
		case BLUE:
			return countBlue(image);
		case GRAY:
			return countGray(image);
		default:
			return null;
		}
	}

	public static int[] countRed(BufferedImage image) {
		int[][] data = Color.redMatrix(image);
		int[] values = new int[N_COLORS];
		for (int i = 0; i < values.length; i++)
			values[i] = 0;

		for (int[] row : data)
			for (int i : row)
				if (i != -1)
					values[i] = values[i] + 1;
		return values;
	}

	public static int[] countGreen(BufferedImage image) {
		int[][] data = Color.greenMatrix(image);
		int[] values = new int[N_COLORS];
		for (int i = 0; i < values.length; i++)
			values[i] = 0;

		for (int[] row : data)
			for (int i : row)
				if (i != -1)
					values[i] = values[i] + 1;
		return values;
	}

	public static int[] countBlue(BufferedImage image) {
		int[][] data = Color.blueMatrix(image);
		int[] values = new int[N_COLORS];
		for (int i = 0; i < values.length; i++)
			values[i] = 0;

		for (int[] row : data)
			for (int i : row)
				if (i != -1)
					values[i] = values[i] + 1;
		return values;
	}

	public static int[] countGray(BufferedImage image) {
		int[][] data = Color.redMatrix(image);
		int[] values = new int[N_COLORS];
		for (int i = 0; i < values.length; i++)
			values[i] = 0;

		for (int[] row : data)
			for (int i : row)
				if (i != -1)
					values[i] = values[i] + 1;
		return values;
	}

	// CUMULATIVE FUNCTIONS
	public static int[] cumulative(BufferedImage image, int color) {
		switch (color) {
		case RED:
			return cumulativeRed(image);
		case GREEN:
			return cumulativeGreen(image);
		case BLUE:
			return cumulativeBlue(image);
		case GRAY:
			return cumulativeGray(image);
		default:
			return null;
		}
	}

	public static int[] cumulativeRed(BufferedImage image) {
		int[] values = Color.countRed(image);
		for (int i = 1; i < N_COLORS; i++)
			values[i] += values[i - 1];
		return values;
	}

	public static int[] cumulativeGreen(BufferedImage image) {
		int[] values = Color.countGreen(image);
		for (int i = 1; i < N_COLORS; i++)
			values[i] += values[i - 1];
		return values;
	}

	public static int[] cumulativeBlue(BufferedImage image) {
		int[] values = Color.countBlue(image);
		for (int i = 1; i < N_COLORS; i++)
			values[i] += values[i - 1];
		return values;
	}

	public static int[] cumulativeGray(BufferedImage image) {
		int[] values = Color.countGray(image);
		for (int i = 1; i < N_COLORS; i++)
			values[i] += values[i - 1];
		return values;
	}

	// NORMALIZED FUNCTIONS
	public static double[] normalized(BufferedImage image, int color) {
		switch (color) {
		case RED:
			return normalizedRed(image);
		case GREEN:
			return normalizedGreen(image);
		case BLUE:
			return normalizedBlue(image);
		case GRAY:
			return normalizedGray(image);
		default:
			return null;
		}
	}

	public static double[] normalizedRed(BufferedImage image) {
		double sum = 0d;
		double[] values = Color.toDouble(Color.countRed(image));
		for (double i : values)
			sum += i;
		for (int i = 0; i < values.length; i++) {
			double norm = values[i] / sum;
			values[i] = (int) norm;
		}
		return values;
	}

	public static double[] normalizedGreen(BufferedImage image) {
		double sum = 0d;
		double[] values = Color.toDouble(Color.countGreen(image));
		for (double i : values)
			sum += i;
		for (int i = 0; i < values.length; i++) {
			double norm = values[i] / sum;
			values[i] = (int) norm;
		}
		return values;
	}

	public static double[] normalizedBlue(BufferedImage image) {
		double sum = 0d;
		double[] values = Color.toDouble(Color.countBlue(image));
		for (double i : values)
			sum += i;
		for (int i = 0; i < values.length; i++) {
			double norm = values[i] / sum;
			values[i] = (int) norm;
		}
		return values;
	}

	public static double[] normalizedGray(BufferedImage image) {
		double sum = 0d;
		double[] values = Color.toDouble(Color.countGray(image));
		for (double i : values)
			sum += i;
		for (int i = 0; i < values.length; i++) {
			double norm = values[i] / sum;
			values[i] = (int) norm;
		}
		return values;
	}

	// NORMALIZED FUNCTIONS
	public static double[] cumulativeNormalized(BufferedImage image, int color) {
		switch (color) {
		case RED:
			return cumulativeNormalizedRed(image);
		case GREEN:
			return cumulativeNormalizedGreen(image);
		case BLUE:
			return cumulativeNormalizedBlue(image);
		case GRAY:
			return cumulativeNormalizedGray(image);
		default:
			return null;
		}
	}

	public static double[] cumulativeNormalizedRed(BufferedImage image) {
		int[] aux = Color.cumulativeRed(image);
		double[] auxDouble = new double[aux.length];
		int total = image.getWidth() * image.getHeight();
		for (int i = 0; i < aux.length; i++)
			auxDouble[i] = (double) aux[i] / (double) total;
		return auxDouble;
	}
	
	public static double[] cumulativeNormalizedGreen(BufferedImage image) {
		int[] aux = Color.cumulativeGreen(image);
		double[] auxDouble = new double[aux.length];
		int total = image.getWidth() * image.getHeight();
		for (int i = 0; i < aux.length; i++)
			auxDouble[i] = (double) aux[i] / (double) total;
		return auxDouble;
	}
	
	public static double[] cumulativeNormalizedBlue(BufferedImage image) {
		int[] aux = Color.cumulativeBlue(image);
		double[] auxDouble = new double[aux.length];
		int total = image.getWidth() * image.getHeight();
		for (int i = 0; i < aux.length; i++)
			auxDouble[i] = (double) aux[i] / (double) total;
		return auxDouble;
	}
	
	public static double[] cumulativeNormalizedGray(BufferedImage image) {
		int[] aux = Color.cumulativeGray(image);
		double[] auxDouble = new double[aux.length];
		int total = image.getWidth() * image.getHeight();
		for (int i = 0; i < aux.length; i++)
			auxDouble[i] = (double) aux[i] / (double) total;
		return auxDouble;
	}

	public static int average(int[][] values) {
		if (values.length == 0 || values == null)
			return -1;
		int sumRed = 0, sumGreen = 0, sumBlue = 0;
		int total = values.length * values[0].length;
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				sumRed += utils.Color.red(values[i][j]);
				sumGreen += utils.Color.green(values[i][j]);
				sumBlue += utils.Color.blue(values[i][j]);
			}
		}
		return utils.Color.rgbToInt(sumRed / total, sumGreen / total, sumBlue / total);
	}

	public static int absDifference(int color1, int color2) {
		int R = Math.abs(Color.red(color1) - Color.red(color2));
		int G = Math.abs(Color.green(color1) - Color.green(color2));
		int B = Math.abs(Color.blue(color1) - Color.blue(color2));
		return Color.rgbToInt(R, G, B);
	}

	public static int approx(int color, int length) {
		return Color.rgbToInt(Color.round(Color.red(color), length), Color.round(Color.green(color), length),
				Color.round(Color.blue(color), length));
	}

	public static int round(int value, int length) {
		double val = (double) ((double) value / (double) length);
		return (int) (length * Math.floor(val));
	}

	public static int alpha(int color) {
		return (color >> 24) & 0xff;
	}

	public static int red(int color) {
		return (color >> 16) & 0xff;
	}

	public static int green(int color) {
		return (color >> 8) & 0xff;
	}

	public static int blue(int color) {
		return (color) & 0xff;
	}

	public static int gray(int color) {
		int R = Color.red(color);
		int G = Color.green(color);
		int B = Color.blue(color);
		return (int) (R * NTSC_RED + G * NTSC_GREEN + B * NTSC_BLUE);
	}

	public static int rgbToInt(int R, int G, int B) {
		return (255 & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
	}

	public static int rgbToInt(int R, int G, int B, int A) {
		return (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
	}

	public static int[] intToRGB(int color) {
		int[] rgba = new int[3];
		rgba[0] = Color.red(color);
		rgba[1] = Color.green(color);
		rgba[2] = Color.blue(color);
		return rgba;
	}

	public static double[] toDouble(int[] values) {
		double[] result = new double[values.length];
		for (int i = 0; i < values.length; i++)
			result[i] = (double) values[i];
		return result;
	}

	public static int gamma(int color, double gamma) {
		double aR = (double) Color.red(color) / 255.0d;
		double aG = (double) Color.green(color) / 255.0d;
		double aB = (double) Color.blue(color) / 255.0d;
		double bR = Math.pow(aR, gamma);
		double bG = Math.pow(aG, gamma);
		double bB = Math.pow(aB, gamma);
		return Color.rgbToInt((int) (bR * 255), (int) (bG * 255), (int) (bB * 255), Color.alpha(color));
	}

	public static int[] specify(double[] pO, double[] pR) {
		int M = pO.length;
		int[] T = new int[M];
		int j = -1;
		for (int a = 0; a < M; a++) {
			j = M - 1;
			do {
				T[a] = j;
				j--;
			} while (j >= 0 && pO[a] <= pR[j]);
		}
		return T;
	}

}
