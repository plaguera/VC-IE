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
	
	public static int[][] matrix(BufferedImage image, int color) {
		switch(color) {
			case RED: return redMatrix(image);
			case GREEN: return greenMatrix(image);
			case BLUE: return blueMatrix(image);
			case GRAY: return grayMatrix(image);
			default: break;
		}
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] result = new int[width][height];
		for(int i = 0; i < width; i++)
			for(int j = 0; j < height; j++)
				result[i][j] = Color.red(image.getRGB(i, j));
		return result;
	}
	
	public static int[][] redMatrix(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] result = new int[width][height];
		for(int i = 0; i < width; i++)
			for(int j = 0; j < height; j++)
				result[i][j] = Color.red(image.getRGB(i, j));
		return result;
	}
	
	public static int[][] greenMatrix(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] result = new int[width][height];
		for(int i = 0; i < width; i++)
			for(int j = 0; j < height; j++)
				result[i][j] = Color.green(image.getRGB(i, j));
		return result;
	}
	
	public static int[][] blueMatrix(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] result = new int[width][height];
		for(int i = 0; i < width; i++)
			for(int j = 0; j < height; j++)
				result[i][j] = Color.blue(image.getRGB(i, j));
		return result;
	}
	
	public static int[][] grayMatrix(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] result = new int[width][height];
		for(int i = 0; i < width; i++)
			for(int j = 0; j < height; j++)
				result[i][j] = Color.gray(image.getRGB(i, j));
		return result;
	}
	
	public static int red(int color) {
		 return (color >> 16) & 0xff;
	}
	
	public static int green(int color) {
		 return (color >>  8) & 0xff;
	}
	
	public static int blue(int color) {
		 return (color      ) & 0xff;
	}
	
	public static int gray(int color) {
		 int R = (color >> 16) & 0xff;
		 int G = (color >>  8) & 0xff;
		 int B = (color      ) & 0xff;
		 return (int) (R * NTSC_RED + G * NTSC_GREEN + B * NTSC_BLUE);
	}

}
