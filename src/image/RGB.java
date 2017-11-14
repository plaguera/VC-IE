package image;

import java.awt.Color;

public class RGB {

	public static final double NTSC_RED = 0.299;
	public static final double NTSC_GREEN = 0.587;
	public static final double NTSC_BLUE = 0.114;

	int red, green, blue;

	public RGB(int red, int green, int blue) {
		setRed(red);
		setGreen(green);
		setBlue(blue);
	}

	public RGB(int color) {
		Color aux = new Color(color);
		setRed(aux.getRed());
		setGreen(aux.getGreen());
		setBlue(aux.getBlue());
	}

	public RGB() {
		this(0, 0, 0);
	}

	public int gray() {
		return (int) (getRed() * NTSC_RED + getGreen() * NTSC_GREEN + getBlue() * NTSC_BLUE);
	}

	public RGB gammaMod(double gamma) {
		double gammaCorrection = 1 / gamma;
		int r = (int) (255 * Math.pow((double) (getRed() / 255d), gammaCorrection));
		int g = (int) (255 * Math.pow((double) (getGreen() / 255d), gammaCorrection));
		int b = (int) (255 * Math.pow((double) (getBlue() / 255d), gammaCorrection));
		return new RGB(r, g, b);
	}

	public RGB gamma(double gamma) {
		double aR = (double) getRed() / 255.0d;
		double aG = (double) getGreen() / 255.0d;
		double aB = (double) getBlue() / 255.0d;
		double bR = Math.pow(aR, gamma);
		double bG = Math.pow(aG, gamma);
		double bB = Math.pow(aB, gamma);
		return new RGB((int) (bR * 255), (int) (bG * 255), (int) (bB * 255));
	}

	public boolean isGrayscale() {
		if (getRed() == getGreen() && getGreen() == getBlue())
			return true;
		return false;
	}

	public int toInt() {
		return RGB.toInt(getRed(), getGreen(), getBlue());
	}

	/**
	 * @return the red
	 */
	public int getRed() {
		return red;
	}

	/**
	 * @return the green
	 */
	public int getGreen() {
		return green;
	}

	/**
	 * @return the blue
	 */
	public int getBlue() {
		return blue;
	}

	/**
	 * @param red
	 *            the red to set
	 */
	public void setRed(int red) {
		this.red = red;
	}

	/**
	 * @param green
	 *            the green to set
	 */
	public void setGreen(int green) {
		this.green = green;
	}

	/**
	 * @param blue
	 *            the blue to set
	 */
	public void setBlue(int blue) {
		this.blue = blue;
	}

	public RGB sum(RGB other) {
		return new RGB(getRed() + other.getRed(), getGreen() + other.getGreen(), getBlue() + other.getBlue());
	}

	public RGB difference(RGB other) {
		return new RGB(getRed() - other.getRed(), getGreen() - other.getGreen(), getBlue() - other.getBlue());
	}

	public RGB absDifference(RGB other) {
		return new RGB(Math.abs(getRed() - other.getRed()), Math.abs(getGreen() - other.getGreen()),
				Math.abs(getBlue() - other.getBlue()));
	}

	public static RGB average(RGB[][] values) {
		if (values.length == 0 || values == null)
			return null;
		int sumRed = 0, sumGreen = 0, sumBlue = 0;
		int total = values.length * values[0].length;
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				sumRed += values[i][j].getRed();
				sumGreen += values[i][j].getGreen();
				sumBlue += values[i][j].getBlue();
			}
		}
		return new RGB(sumRed / total, sumGreen / total, sumBlue / total);
	}

	public void approxMod(int length) {
		setRed(round(getRed(), length));
		setGreen(round(getGreen(), length));
		setBlue(round(getBlue(), length));
	}

	public RGB approx(int length) {
		// System.out.println(round(getRed(), length));
		return new RGB(round(getRed(), length), round(getGreen(), length), round(getBlue(), length));
	}

	public static int round(int value, int length) {

		double val = (double) ((double) value / (double) length);
		return (int) (length * Math.floor(val));
	}

	public static int toInt(int red, int green, int blue) {
		int rgb = red;
		rgb = rgb << 8;
		rgb |= green;
		rgb = rgb << 8;
		rgb |= blue;
		return rgb;
	}

	public String toString() {
		return "RGB@" + this.hashCode() + " : " + "[" + getRed() + ", " + getGreen() + ", " + getBlue() + "]";
	}

}
