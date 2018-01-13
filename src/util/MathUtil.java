package util;

import java.awt.image.Kernel;

public class MathUtil {
	
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
	
	public static double log2(double value) {
		if (value == 0)
			return 0;
		return Math.log(value) / Math.log(2);
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
		}
		return data;
	}

}
