package utils;

import java.awt.image.BufferedImage;

import image.RGB;

public class HistogramUtils {

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
		for(int a = 0; a < M; a++) {
			j = M-1;
			do{
				T[a] = j;
				j--;
			} while(j >= 0 && pO[a] <= pR[j]);
		}
		return T;
	}

}
