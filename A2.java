package a2;

import java.awt.Color;
import java.io.File;
import java.net.URI;
import java.net.URL;

public class A2 {
	/**
	 * The original image
	 */
	private static Picture orig;
	
	/**
	 * The image viewer class
	 */
	private static A2Viewer viewer;
	
	/**
	 * Returns a 300x200 image containing the Queen's flag (without the crown).
	 * 
	 * @return an image containing the Queen's flag
	 */
	public static Picture flag() {
		Picture img = new Picture(300, 200);
		int w = img.width();
		int h = img.height();

		// set the pixels in the blue stripe
		Color blue = new Color(0, 48, 95);
		for (int col = 0; col < w / 3; col++) {
		    for (int row = 0; row < h - 1; row++) {
		        img.set(col, row, blue);
		    }
		}

		// set the pixels in the yellow stripe
		Color yellow = new Color(255, 189, 17);
		for (int col = w / 3; col < 2 * w / 3; col++) {
		    for (int row = 0; row < h - 1; row++) {
		        img.set(col, row, yellow);
		    }
		}

		// set the pixels in the red stripe
		Color red = new Color(185, 17, 55);
		for (int col = 2 * w / 3; col < w; col++) {
		    for (int row = 0; row < h - 1; row++) {
		        img.set(col, row, red);
		    }
		}
		return img;
	}

	public static Picture copy(Picture p) {
		Picture result = new Picture(p.width(), p.height());
		for (int col = 0; col < p.width(); col ++) {
			for (int row = 0; row < p.height(); row ++) {
				result.set(col, row, p.get(col, row));
			}
		} 
		return result;
	}
	
	public static Picture border(Picture p, int thick) {
		Picture result = copy(p);
		
		//Place blue border on top and bottom rows
		for (int col = 0; col < p.width(); col ++) {
			for (int row = 0; row < thick; row ++) {
				result.set(col, row, Color.BLUE);
			}
			for (int row = p.height()-thick; row < p.height(); row ++) {
				result.set(col, row, Color.BLUE);
			}
		}
		
		//Place blue border on left and right columns
		for (int row = thick; row < p.height() - thick; row ++) {
			for (int col = 0; col < thick; col ++) {
				result.set(col, row, Color.BLUE);
			}
			for (int col = p.width() - thick; col < p.width(); col ++) {
				result.set(col, row, Color.BLUE);
			}
		}
		return result;
	}
	
	public static Picture gray(Picture p) {
		Picture result = copy(p);
		
		for (int col = 0; col < p.width(); col ++) {
			for (int row = 0; row < p.height(); row ++) {
				Color pixel = p.get(col, row);
				double grayscale = pixel.getRed()*0.2989 + pixel.getGreen()*0.587 + pixel.getBlue()*0.114;
				int gray = (int) Math.round(grayscale);
				Color val = new Color(gray, gray, gray);
				result.set(col, row, val);
			}
		}
		return result;
	}
	
	public static Picture binary(Picture p, Color c1, Color c2) {
		Picture result = gray(copy(p));
		for (int col = 0; col < p.width(); col ++) {
			for (int row = 0; row < p.height(); row ++) {
				if (p.get(col, row).getRed() < 128) {
					result.set(col, row, c1);
				}
				else {
					result.set(col, row, c2);
				}
			}
		}
		return result;
	}
	
	public static Picture flipVertical(Picture p) {
		System.out.println();
		Picture result = copy(p);
		int h = p.height();
		for (int col = 0; col < p.width(); col ++) {
			for (int row = 0; row < p.height() / 2; row ++) {
				result.set(col, row, p.get(col, h - row - 1));
				result.set(col, h - row - 1, p.get(col, row));
			}
		}
		return result;
	}
	
	public static Picture rotateRight(Picture p) {
		Picture result = new Picture(p.height(), p.width());
		for (int col = 0; col < result.width(); col ++) {
			for (int row = 0; row < result.height(); row ++) {
				Color temp = p.get(row, p.height() - col - 1);
				result.set(col, row, temp);
			}
		}
		return result;
	}
	
	public static Picture redEye(Picture p) {
		//Check if the value of red is sufficiently greater (69 chosen arbitrarily)
		//Than the green and blue values in each pixel, and if so, make the entire pixel black.
		Picture result = copy(p);
		for (int col = 0; col < result.width(); col ++) {
			for (int row = 0; row < result.height(); row ++) {
				Color px = p.get(col, row);
				if ((px.getRed() - px.getBlue() > 69) && (px.getRed() - px.getGreen() > 69)) {
					result.set(col, row, Color.BLACK);
				}
			}
		}
		return result;
	}
	
	
	
	public static Picture blur(Picture p, int radius) {
		Picture result = new Picture(p.width(), p.height());
		for (int col = 0; col < result.width(); col ++) {
			for (int row = 0; row < result.height(); row ++) {
				
				int avgRed = 0;
				int avgGreen = 0;
				int avgBlue = 0;
				int pixels = 0;
				
				for (int x = col - radius; x < col + radius + 1; x ++) {
					for (int y = row - radius; y < row + radius + 1; y ++) {
						if ((-1 < x) && (x < p.width()) && (-1 < y) && (y < p.height())) {
							
							Color temp = p.get(x, y);
							avgRed += temp.getRed();
							avgGreen += temp.getGreen();
							avgBlue += temp.getBlue();
							pixels ++;
						}
					}
				}
				Color fin = new Color(avgRed / pixels, avgGreen / pixels, avgBlue / pixels);
				result.set(col, row, fin);
			}
		}
		return result;
	}
	
	
	/**
	 * A2Viewer class calls this method when a menu item is selected.
	 * This method computes a new image and then asks the viewer to
	 * display the computed image.
	 * 
	 * @param op the operation selected in the viewer
	 */
	public static void processImage(String op) {
		
		switch (op) {
		case A2Viewer.FLAG:
			System.out.println("check");
			Picture p = A2.flag();
			A2.viewer.setComputed(p);
			break;
			
		case A2Viewer.COPY:
			System.out.println("hello");
			p = A2.copy(A2.orig);
			A2.viewer.setComputed(p);
			break;
			
		case A2Viewer.BORDER_1:
			p = A2.border(A2.orig, 1);
			A2.viewer.setComputed(p);
			break;
			
		case A2Viewer.BORDER_5:
			p = A2.border(A2.orig, 5);
			A2.viewer.setComputed(p);
			break;
			
		case A2Viewer.BORDER_10:
			p = A2.border(A2.orig, 10);
			A2.viewer.setComputed(p);
			break;
			
		case A2Viewer.TO_GRAY:
			p = A2.gray(A2.orig);
			A2.viewer.setComputed(p);		
			break;
			
		case A2Viewer.TO_BINARY:
			p = A2.binary(A2.orig, Color.BLACK, Color.WHITE);
			A2.viewer.setComputed(p);
			break;
			
		case A2Viewer.FLIP_VERTICAL:
			p = A2.flipVertical(A2.orig);
			A2.viewer.setComputed(p);
			break;
			
		case A2Viewer.ROTATE_RIGHT:
			p = A2.rotateRight(A2.orig);
			A2.viewer.setComputed(p);		
			break;
			
		case A2Viewer.RED_EYE:
			p = A2.redEye(A2.orig);
			A2.viewer.setComputed(p);	
			break;
			
		case A2Viewer.BLUR_1:
			p = A2.blur(A2.orig, 1);
			A2.viewer.setComputed(p);
			break;
			
		case A2Viewer.BLUR_3:
			p = A2.blur(A2.orig, 3);
			A2.viewer.setComputed(p);
			break;
			
		case A2Viewer.BLUR_5:
			p = A2.blur(A2.orig, 5);
			A2.viewer.setComputed(p);
			break;
			
		default:
			// do nothing
		}
	}
	
	/**
	 * Starting point of the program. Students can comment/uncomment which image
	 * to use when testing their program.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		A2.viewer = new A2Viewer();
		A2.viewer.setVisible(true);
		
		
		URL img;
		// uncomment one of the next two lines to choose which test image to use (person or cat)
		img = A2.class.getResource("redeye-400x300.jpg");   
		// img = A2.class.getResource("cat.jpg");
		
		try {
			URI uri = new URI(img.toString());
			A2.orig = new Picture(new File(uri.getPath()));
			A2.viewer.setOriginal(A2.orig);
			processImage("BLUR_5");
		}
		catch (Exception x) {
			
		}
	}

}
