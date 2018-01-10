package filter;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import image.Image;

public class Filter {

	public static final float[] GRADIENT_X = new float[] { -0.5f, 0f, 0.5f };

	public static final float[] GRADIENT_Y = new float[] { 0.5f, 0f, -0.5f };

	public static final float[] SOBEL_X = new float[] { 1f / 4f, 0f / 4f, -1f / 4f, 2f / 4f, 0f / 4f, -2f / 4f, 1f / 4f,
			0f / 4f, -1f / 4f };

	public static final float[] SOBEL_Y = new float[] { 1f / 4f, 2f / 4f, 1f / 4f, 0f / 4f, 0f / 4f, 0f / 4f, -1f / 4f,
			-2f / 4f, -1f / 4f };

	public static final float[] GAUSS_3 = new float[] { 1f / 16f, 2f / 16f, 1f / 16f, 2f / 16f, 4f / 16f, 2f / 16f,
			1f / 16f, 2f / 16f, 1f / 16f };

	public static final float[] GAUSS_5 = new float[] { 1f / 256f, 4f / 256f, 6f / 256f, 4f / 256f, 1f / 256f,
			4f / 256f, 16f / 256f, 24f / 256f, 16f / 256f, 4f / 256f, 6f / 256f, 24f / 256f, 36f / 256f, 24f / 256f,
			6f / 256f, 4f / 256f, 16f / 256f, 24f / 256f, 16f / 256f, 4f / 256f, 1f / 256f, 2f / 256f, 1f / 256f,
			4f / 256f, 1f / 256f };

	public static final Kernel KERNEL_GRADIENT_X = new Kernel(3, 1, GRADIENT_X);
	public static final Kernel KERNEL_GRADIENT_Y = new Kernel(1, 3, GRADIENT_Y);
	public static final Kernel KERNEL_SOBEL_X = new Kernel(3, 3, SOBEL_X);
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
		for (int i = 0; i < data.length; i++) {
			double up = Math.pow(i - size, 2);
			double down = 2 * Math.pow(theta, 2);
			data[i] = (float) (1000 * Math.exp(-(up / down)));
		}
		float sum = 0.0f;
		for (float i : data)
			sum += i;
		for (int i = 0; i < data.length; i++)
			data[i] /= sum;
		if (horizontal)
			return new Kernel(size * 2 + 1, 1, data);
		else
			return new Kernel(1, size * 2 + 1, data);
	}

}
