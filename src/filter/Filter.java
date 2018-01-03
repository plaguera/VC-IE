package filter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import image.Image;
import image.RGB;
import utils.ImageUtils;

public class Filter {
	
	public static final float[] GRADIENT_X = new float[] { 	-0.5f, 0f, 0.5f };
	
	public static final float[] GRADIENT_Y = new float[] { 	0.5f, 0f, -0.5f };
	
	public static final float[] SOBEL_X = new float[] { 	1f / 4f, 0f / 4f, -1f / 4f,
														2f / 4f, 0f / 4f, -2f / 4f,
														1f / 4f, 0f / 4f, -1f / 4f };

	public static final float[] SOBEL_Y = new float[] { 	1f / 4f, 	2f / 4f, 	1f / 4f,
														0f / 4f,		0f / 4f, 	0f / 4f,
														-1f / 4f, 	-2f / 4f, 	-1f / 4f };
	
	public static final float[] GAUSS_3 = new float[] { 	1f / 16f, 2f / 16f, 1f / 16f,
														2f / 16f, 4f / 16f, 2f / 16f,
														1f / 16f, 2f / 16f, 1f / 16f };
	
	public static final float[] GAUSS_5 = new float[] { 	1f / 256f, 4f / 256f, 6f / 256f, 4f / 256f, 1f / 256f,
														4f / 256f, 16f / 256f, 24f / 256f, 16f / 256f, 4f / 256f,
														6f / 256f, 24f / 256f, 36f / 256f, 24f / 256f, 6f / 256f,
														4f / 256f, 16f / 256f, 24f / 256f, 16f / 256f, 4f / 256f,
														1f / 256f, 2f / 256f, 1f / 256f, 4f / 256f, 1f / 256f };
	
	public static final Kernel KERNEL_GRADIENT_X = new Kernel(3, 1, GRADIENT_X);
	public static final Kernel KERNEL_GRADIENT_Y = new Kernel(1, 3, GRADIENT_Y);
	public static final Kernel KERNEL_SOBEL_X = new Kernel(3, 3, SOBEL_Y);
	public static final Kernel KERNEL_SOBEL_Y = new Kernel(3, 3, SOBEL_Y);
	public static final Kernel KERNEL_GAUSS_3 = new Kernel(3, 3, GAUSS_3);
	public static final Kernel KERNEL_GAUSS_5 = new Kernel(5, 5, GAUSS_5);
	
	public static BufferedImage convolve(Image image, Kernel kernel) {
		BufferedImageOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
	    return op.filter(image.get(), null);
	}
	
	public static Kernel gauss(double theta, boolean horizontal) {
		int size = 5;
		float[] data = new float[size * 2 + 1];
		for(int i = 0; i < data.length; i++) {
			double up = Math.pow(i-size, 2);
			double down = 2 * Math.pow(theta, 2);
			data[i] = (float) (1000 * Math.exp(-(up/down)));
		}
		float sum = 0.0f;
		for(float i : data)
			sum += i;
		for(int i = 0; i < data.length; i++)
			data[i] /= sum;
		if(horizontal)
			return new Kernel(size*2 + 1, 1, data);
		else
			return new Kernel(1, size*2 + 1, data);
	}
	
	public static float[][] doubleArrayDimension(Kernel kernel) {
		int kernelWidth = kernel.getWidth();
		int kernelHeight = kernel.getHeight();
		
		float[] aux = kernel.getKernelData(null);
		float[][] data = new float[kernelHeight][kernelWidth];
		
		for(int i = 0; i < aux.length; i++) {
			int row = i / kernelWidth;
			int col = i % kernelWidth;
			data[row][col] = aux[i];
		}
		for(float[] row : data) {
			for(float i : row)
				System.out.print(i + " ");
			System.out.println();
		}
		return data;
	}

	public static BufferedImage convolveManual(Image image, Kernel kernel) {

		int width = image.getWidth();
		int height = image.getHeight();
		
		BufferedImage output = new BufferedImage(height, width, image.get().getType());
		
		int kernelWidth = kernel.getWidth();
		int kernelHeight = kernel.getHeight();
		
		float[][] data = doubleArrayDimension(kernel);

		for (int y = 1; y < height - 1; y++) {
			for (int x = 1; x < width - 1; x++) {

				// get N-by-M array of colors in neighborhood
				int[][] gray = new int[kernelHeight][kernelWidth];
				for (int i = 0; i < kernelHeight; i++) {
					for (int j = 0; j < kernelWidth; j++) {
						gray[i][j] = new RGB(image.get().getRGB(x - 1 + i, y - 1 + j)).gray();
					}
				}
				// apply filter
				int gray1 = 0, gray2 = 0;
				for (int i = 0; i < kernelHeight; i++) {
					for (int j = 0; j < kernelWidth; j++) {
						gray1 += gray[i][j] * data[i][j];
						gray2 += gray[i][j] * data[i][j];
					}
				}
				// int magnitude = 255 - truncate(Math.abs(gray1) + Math.abs(gray2));
				int magnitude = (int) (255 - ImageUtils.truncate(Math.sqrt(gray1 * gray1 + gray2 * gray2)));
				Color grayscale = new Color(magnitude, magnitude, magnitude);
				output.setRGB(x, y, grayscale.getRGB());
			}
		}
		return output;
	}

}
