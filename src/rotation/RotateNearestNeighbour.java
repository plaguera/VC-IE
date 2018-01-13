package rotation;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

public class RotateNearestNeighbour extends Rotation {

	public RotateNearestNeighbour(BufferedImage image) {
		super(image);
	}

	@Override
	public BufferedImage rotate(double angle) {
		int width = getWidth();
		int height = getHeight();

		Dimension newDim = calculateNewDimension(angle);
		int newWidth = (int) newDim.getWidth();
		int newHeight = (int) newDim.getHeight();

		BufferedImage dst = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		
		double oldIradius = (double) (height - 1) / 2;
		double oldJradius = (double) (width - 1) / 2;

		// get destination image size
		double newIradius = (double) (newHeight - 1) / 2;
		double newJradius = (double) (newWidth - 1) / 2;

		// angle's sine and cosine
		double angleRad = -angle * Math.PI / 180;
		double angleCos = Math.cos(angleRad);
		double angleSin = Math.sin(angleRad);

		// destination pixel's coordinate relative to image center
		double ci, cj;

		// source pixel's coordinates
		int oi, oj;

		ci = -newIradius;
		for (int i = 0; i < newHeight; i++) {
			cj = -newJradius;
			for (int j = 0; j < newWidth; j++) {

				oi = (int) (angleCos * ci + angleSin * cj + oldIradius);
				oj = (int) (-angleSin * ci + angleCos * cj + oldJradius);

				// validate source pixel's coordinates
				if ((oi < 0) || (oj < 0) || (oi >= height) || (oj >= width)) {
					// fill destination image with filler
					dst.setRGB(j, i, 0);
					//System.out.println(Integer.toHexString(dst.getRGB(j, i)));
				} else {
					dst.setRGB(j, i, getImage().getRGB(oj, oi));
				}
				cj++;
			}
			ci++;
		}
		return dst;
	}

}
