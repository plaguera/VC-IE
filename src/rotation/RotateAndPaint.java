package rotation;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class RotateAndPaint extends Rotation {

	public RotateAndPaint(BufferedImage image) {
		super(image);
	}
	
	@Override
	public BufferedImage rotate(double angle) {
		Dimension newDimensions = calculateNewDimension(angle);
		int width = getWidth();
		int height = getHeight();
		int newWidth = (int) newDimensions.getWidth();
		int newHeight = (int) newDimensions.getHeight();
		BufferedImage dst = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < newWidth; i++)
			for(int j = 0; j < newHeight; j++)
				dst.setRGB(i, j, 0);
		
		Point[][] translation = translateMatrix(angle);
		Point minimums = findMinimums(translation);
		for(int i = 0; i < width; i++)
			for(int j = 0; j < height; j++) {
				Point pos = translation[i][j];
				int x = pos.x + -minimums.x;
				int y = pos.y + -minimums.y;
				//System.out.println("x = " + i + " y = " + j);
				//System.out.println("x' = " + x + " y' = " + y);
				dst.setRGB(x, y, getImage().getRGB(i, j));
			}
		return dst;
	}
	
	private Point[][] translateMatrix(double angle) {
		int width = getWidth();
		int height = getHeight();
		Point[][] aux = new Point[width][height];
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				aux[i][j] = rotatePoint(new Point(i, j), angle);
			}
		}
		return aux;
	}
	
	private Point findMinimums(Point[][] array) {
		int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
		int width = getWidth();
		int height = getHeight();
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				Point aux = array[i][j];
				if(aux.x < minX)
					minX = aux.x;
				if(aux.y < minY)
					minY = aux.y;
			}
		}
		return new Point(minX, minY);
	}

}
