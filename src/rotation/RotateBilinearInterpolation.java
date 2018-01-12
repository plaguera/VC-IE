package rotation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import image.Rotation;

public class RotateBilinearInterpolation extends Rotation {

	public RotateBilinearInterpolation(BufferedImage image) {
		super(image);
	}

	@Override
	public BufferedImage rotate(double angle) {
		int width = getWidth();
		int height = getHeight();

		Dimension newDim = calculateNewDimension(angle);
		int newWidth = (int) newDim.getWidth();
		int newHeight = (int) newDim.getHeight();

		BufferedImage dst = new BufferedImage(newWidth, newHeight, getImage().getType());
		
		double oldIradius = (double) ( height  - 1 ) / 2;
        double oldJradius = (double) ( width - 1 ) / 2;
		
		// get destination image size
        double newIradius = (double) ( newHeight  - 1 ) / 2;
        double newJradius = (double) ( newWidth - 1 ) / 2;

        // angle's sine and cosine
        double angleRad = -angle * Math.PI / 180;
        double angleCos = Math.cos( angleRad );
        double angleSin = Math.sin( angleRad );
        
        // destination pixel's coordinate relative to image center
        double ci, cj;
        
        // coordinates of source points
        double  oi, oj, ti, tj, di1, dj1, di2, dj2;
        int     oi1, oj1, oi2, oj2;
        
        // width and height decreased by 1
        int imax = height - 1;
        int jmax = width - 1;
        
        ci = -newIradius;
        Color fill = new Color(0, 0, 0);
        for (int i = 0; i < newHeight; i++) {
            
            // do some pre-calculations of source points' coordinates
            // (calculate the part which depends on y-loop, but does not
            // depend on x-loop)
            ti = angleCos * ci + oldIradius;
            tj = -angleSin * ci + oldJradius;
            
            cj = -newJradius;
            for (int j = 0; j < newWidth; j++) {
                
                // coordinates of source point
                oi = ti + angleSin * cj;
                oj = tj + angleCos * cj;
                
                // top-left coordinate
                oi1 = (int) oi;
                oj1 = (int) oj;
                
                // validate source pixel's coordinates
                if ( ( oi1 < 0 ) || ( oj1 < 0 ) || ( oi1 >= height ) || ( oj1 >= width ) ){
                    // fill destination image with filler
                    dst.setRGB(j, i, fill.getRGB());
                }
                else{
                    // bottom-right coordinate
                    oi2 = ( oi1 == imax ) ? oi1 : oi1 + 1;
                    oj2 = ( oj1 == jmax ) ? oj1 : oj1 + 1;

                    if ( ( di1 = oi - (double) oi1 ) < 0 )
                        di1 = 0;
                    di2 = 1.0 - di1;

                    if ( ( dj1 = oj - (double) oj1 ) < 0 )
                        dj1 = 0;
                    dj2 = 1.0 - dj1;
                    
                    // get four points Red
                    int p1 = new Color(getImage().getRGB(oj1, oi1)).getRed();
                    int p2 = new Color(getImage().getRGB(oj1, oi2)).getRed();
                    int p3 = new Color(getImage().getRGB(oj2, oi1)).getRed();
                    int p4 = new Color(getImage().getRGB(oj2, oi2)).getRed();
                    
                    int r = interpolateBilinear(di1, dj1, di2, dj2, p1, p2, p3, p4);
                    
                    // get four points Green
                    p1 = new Color(getImage().getRGB(oj1, oi1)).getGreen();
                    p2 = new Color(getImage().getRGB(oj1, oi2)).getGreen();
                    p3 = new Color(getImage().getRGB(oj2, oi1)).getGreen();
                    p4 = new Color(getImage().getRGB(oj2, oi2)).getGreen();
                    
                    int g = interpolateBilinear(di1, dj1, di2, dj2, p1, p2, p3, p4);

                    // get four points Blue
                    p1 = new Color(getImage().getRGB(oj1, oi1)).getBlue();
                    p2 = new Color(getImage().getRGB(oj1, oi2)).getBlue();
                    p3 = new Color(getImage().getRGB(oj2, oi1)).getBlue();
                    p4 = new Color(getImage().getRGB(oj2, oi2)).getBlue();
                    
                    int b = interpolateBilinear(di1, dj1, di2, dj2, p1, p2, p3, p4);
                    
                    dst.setRGB(j, i, new Color(r, g, b).getRGB());
                }
                cj++;
            }
            ci++;
        }
        return dst;
	}
	
	private int interpolateBilinear(double di1, double dj1, double di2, double dj2, int p1, int p2, int p3, int p4) {
		return (int) ((p1 * di2 * dj2) + (p2 * di1 * dj2) + (p3 * dj1 * di2) + (p4 * di1 * dj1));
	}

}
