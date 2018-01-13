package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;

import image.RGB;

public class ColorUtils {

	public static final double NTSC_RED = 0.299;
	public static final double NTSC_GREEN = 0.587;
	public static final double NTSC_BLUE = 0.114;

	public static boolean isRGB(BufferedImage image) {
		int i = image.getType();
		if (i == BufferedImage.TYPE_3BYTE_BGR)
			return true;
		else if (i == BufferedImage.TYPE_4BYTE_ABGR)
			return true;
		else if (i == BufferedImage.TYPE_4BYTE_ABGR_PRE)
			return true;
		else if (i == BufferedImage.TYPE_INT_ARGB)
			return true;
		else if (i == BufferedImage.TYPE_INT_ARGB_PRE)
			return true;
		else if (i == BufferedImage.TYPE_INT_BGR)
			return true;
		else if (i == BufferedImage.TYPE_INT_RGB)
			return true;
		else if (i == BufferedImage.TYPE_USHORT_555_RGB)
			return true;
		else if (i == BufferedImage.TYPE_USHORT_565_RGB)
			return true;
		return false;
	}

	public static boolean isGrayscale(BufferedImage image) {
		int i = image.getType();
		if (i == BufferedImage.TYPE_BYTE_GRAY)
			return true;
		return false;
	}

	public static boolean isGrayscale(RGB[][] lut) {
		for (int i = 0; i < lut.length; i++)
			for (int j = 0; j < lut[i].length; j++)
				if (!lut[i][j].isGrayscale())
					return false;
		return true;
	}

	public static int[] intToRGB(int color) {
		Color aux = new Color(color);
		int[] rgba = new int[3];
		rgba[0] = aux.getRed();
		rgba[1] = aux.getGreen();
		rgba[2] = aux.getBlue();
		return rgba;
	}

	public static int rgbToInt(int red, int green, int blue) {
		return new Color(red, green, blue).getRGB();
	}

	public static RGB[][] getLUT(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		RGB[][] lut = new RGB[width][height];

		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				lut[j][i] = new RGB(image.getRGB(j, i));
		return lut;
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
