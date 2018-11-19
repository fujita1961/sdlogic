package edu.hawaii.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Canvas Display
 * @author satoru
 */
public class Canvas2 {
	/**
	 * Panel for Canvas
	 */
	private DrawingPanel panel;

	/**
	 * Base Frame
	 */
	private JFrame frame = null;

	/**
	 * Width
	 */
	private int width = 500;

	/**
	 * Height
	 */
	private int height = 500;

	/**
	 * Image buffer
	 */
	private BufferedImage image;

	/**
	 * Graphics instance
	 */
	private Graphics g;

	/**
	 * X coordinate pointed
	 */
	private int pointedX;

	/**
	 * Y coordinate pointed
	 */
	private int pointedY;

	/**
	 * flag for automatic repaint
	 */
	private boolean repaintFlag = true;

	/**
	 *
	 */
	private MouseListener listener = null;

	/**
	 * Does canvas physically have a window?
	 */
	private boolean visible = false;

	/**
	 * initialize
	 */
	public Canvas2(boolean visible) {
		this.visible = visible;
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		panel = new DrawingPanel(image);
		if(visible) {
			frame = new JFrame("Canvas");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().add(panel);
			frame.setSize(width, height);
		}
		g = image.getGraphics();
		g.clearRect(0, 0, width, height);
		g.setColor(new Color(255, 255,255));
		g.fillRect(0, 0, width, height);
	}

	/**
	 * Display Canvas
	 * @param w width
	 * @param h height
	 */
	public void show(int w, int h) {
		setWindowSize(w, h);
		show();
	}

	/*
	public void show(int w, int h, boolean visible) {
		setWindowSize(w, h);
		show(visible);
	}
	*/

	/**
	 * Display Canvas
	 */
	public void show() {
		if(visible) {
			frame.setVisible(visible);
			int pw = panel.getWidth();
			int ph = panel.getHeight();
			int fwidth = frame.getWidth() + width - pw;
			int fheight = frame.getHeight() + height - ph;
			frame.setSize(fwidth, fheight);

			panel.addMouseListener(new MouseListener() {
				public void mouseClicked(MouseEvent e) {
					if(listener != null) {
						listener.mouseClicked(e);
					}
				}

				public void mouseEntered(MouseEvent e) {
					if(listener != null) {
						listener.mouseEntered(e);
					}
				}

				public void mouseExited(MouseEvent e) {
					if(listener != null) {
						listener.mouseExited(e);
					}
				}

				public void mousePressed(MouseEvent e) {
					if(listener != null) {
						listener.mousePressed(e);
					}
					pointedX = e.getX();
					pointedY = e.getY();

					synchronized(panel) {
						panel.notifyAll();
					}
				}

				public void mouseReleased(MouseEvent e) {
					if(listener != null) {
						listener.mouseReleased(e);
					}
				}
			});
		}
	}

	public void addMouseListener(MouseListener listener) {
		this.listener = listener;
	}

	/**
	 * Canvas clear
	 */
	public void clear() {
		g.clearRect(0, 0, width, height);
		g.setColor(new Color(255, 255,255));
		g.fillRect(0, 0, width, height);
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * Draw string
	 * @param x
	 * @param y
	 * @param text
	 */
	public void drawString(double x, double y, Object text) {
		g.drawString(text.toString(), round(x), round(y));
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * Draw string at center
	 * @param x
	 * @param y
	 * @param text
	 */
	public void drawStringCenter(double x, double y, Object text) {
		FontMetrics metrics = g.getFontMetrics();
		int w = metrics.stringWidth(text.toString());
		//int h = metrics.getHeight();

		//g.drawString(text.toString(), round(x) - w / 2, round(y) + h / 2);
		g.drawString(text.toString(), round(x) - w / 2, round(y));
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * Draw string to right
	 * @param x
	 * @param y
	 * @param text
	 */
	public void drawStringRight(double x, double y, Object text) {
		FontMetrics metrics = g.getFontMetrics();
		int w = metrics.stringWidth(text.toString());
		//int h = metrics.getHeight();

		//g.drawString(text.toString(), round(x) - w, round(y) + h / 2);
		g.drawString(text.toString(), round(x) - w, round(y));
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * Draw string to left
	 * @param x
	 * @param y
	 * @param text
	 */
	public void drawStringLeft(double x, double y, Object text) {
		//FontMetrics metrics = g.getFontMetrics();
		//int h = metrics.getHeight();

		//g.drawString(text.toString(), round(x), round(y) + h / 2);
		g.drawString(text.toString(), round(x), round(y));
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * Draw line
	 * @param x0 x of start point
	 * @param y0 y of start point
	 * @param x1 x of end point
	 * @param y1 y of end point
	 */
	public void drawLine(double x0, double y0, double x1, double y1) {
		g.drawLine(round(x0), round(y0), round(x1), round(y1));
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * Draw point
	 * @param x
	 * @param y
	 */
	public void drawPoint(double x, double y) {
		g.drawRect(round(x), round(y), 1, 1);
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * Draw rectangle
	 * @param x x of left-top
	 * @param y y of left-top
	 * @param w width
	 * @param h height
	 */
	public void drawRect(double x, double y, double w, double h) {
		g.drawRect(round(x), round(y), round(w), round(h));
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * Fill rectangle
	 * @param x x of left-top
	 * @param y y of left-top
	 * @param w width
	 * @param h height
	 */
	public void fillRect(double x, double y, double w, double h) {
		g.fillRect(round(x), round(y), round(w), round(h));
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * Draw circle
	 * @param x x of center
	 * @param y y of center
	 * @param r radius
	 */
	public void drawCircle(double x, double y, double r) {
		int r2 = round(r * 2);
		g.drawOval(round(x - r), round(y - r), r2, r2);
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * Fill circle
	 * @param x x of center
	 * @param y y of center
	 * @param r radius
	 */
	public void fillCircle(double x, double y, double r) {
		int r2 = round(r * 2);
		g.fillOval(round(x - r), round(y - r), r2, r2);
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * Draw oval
	 * @param x x of left-top
	 * @param y y of left-top
	 * @param w width
	 * @param h height
	 */
	public void drawOval(double x, double y, double w, double h) {
		g.drawOval(round(x), round(y), round(w), round(h));
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * Fill oval
	 * @param x x of left-top
	 * @param y y of left-top
	 * @param w width
	 * @param h height
	 */
	public void fillOval(double x, double y, double w, double h) {
		g.fillOval(round(x), round(y), round(w), round(h));
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * Draw triangle
	 * @param x0 x of the first point
	 * @param y0 y of the first point
	 * @param x1 x of the second point
	 * @param y1 y of the second point
	 * @param x2 x of the third point
	 * @param y2 y of the third point
	 */
	public void drawTriangle(double x0, double y0, double x1, double y1, double x2, double y2) {
		int[] xPoints = new int[] {
				round(x0), round(x1), round(x2)
		};

		int[] yPoints = new int[] {
				round(y0), round(y1), round(y2)
		};

		g.drawPolygon(xPoints, yPoints, xPoints.length);
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * Fill triangle
	 * @param x0 x of the first point
	 * @param y0 y of the first point
	 * @param x1 x of the second point
	 * @param y1 y of the second point
	 * @param x2 x of the third point
	 * @param y2 y of the third point
	 */
	public void fillTriangle(double x0, double y0, double x1, double y1, double x2, double y2) {
		int[] xPoints = new int[] {
				round(x0), round(x1), round(x2)
		};

		int[] yPoints = new int[] {
				round(y0), round(y1), round(y2)
		};

		g.fillPolygon(xPoints, yPoints, xPoints.length);
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * Draw polygon
	 * @param xPoints array of x
	 * @param yPoints array of y
	 */
	public void drawPolygon(int[] xPoints, int[] yPoints) {
		g.drawPolygon(xPoints, yPoints, xPoints.length);
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * Fill polygon
	 * @param xPoints array of x
	 * @param yPoints array of y
	 */
	public void fillPolygon(int[] xPoints, int[] yPoints) {
		g.fillPolygon(xPoints, yPoints, xPoints.length);
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * Draw image file
	 * @param x x of left-top
	 * @param y y of left-top
	 * @param fileName image file name
	 */
	public void drawImage(double x, double y, String fileName) {
		File file = new File(fileName);
		drawImage(round(x), round(y), file);
	}

	/**
	 * Draw image file
	 * @param x x of left-top
	 * @param y y of left-top
	 * @param file File object of image file
	 */
	public void drawImage(double x, double y, File file) {
		try {
			BufferedImage im = ImageIO.read(file);
			g.drawImage(im, round(x), round(y), null);
			if(repaintFlag) {
				panel.repaint();
			}
		} catch (Exception e) {
			System.err.println(file.getName() + ": cannot open.");
		}
	}

	/**
	 * Draw image
	 * @param x x of left-top
	 * @param y y of left-top
	 * @param image Image object
	 */
	public void drawImage(double x, double y, Image image) {
		try {
			g.drawImage(image, round(x), round(y), null);
			if(repaintFlag) {
				panel.repaint();
			}
		} catch (Exception e) {
			System.err.println("image: cannot be displayed.");
		}
	}

	/**
	 * Set line width
	 * @param width
	 */
	public void setWidth(int width) {
		BasicStroke wideStroke = new BasicStroke(width);
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(wideStroke);
	}

	/**
	 * Set color
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void setColor(int red, int green, int blue) {
		g.setColor(new Color(red, green, blue));
	}

	/**
	 * Set color
	 * @param red
	 * @param green
	 * @param blue
	 * @param alpha
	 */
	public void setColor(int red, int green, int blue, int alpha) {
		g.setColor(new Color(red, green, blue, alpha));
	}

	/**
	 * Set canvas size
	 * @param w width
	 * @param h height
	 */
	public void setSize(int w, int h) {
		setSize(w, h, true);
	}

	public void setSize(int w, int h, boolean visible) {
		setWindowSize(w, h);
		// frame.setVisible(false);

		if(visible) {
			frame.setVisible(visible);
			int pw = panel.getWidth();
			int ph = panel.getHeight();
			int fwidth = frame.getWidth() + width - pw;
			int fheight = frame.getHeight() + height - ph;
			frame.setSize(fwidth, fheight);
		}

		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * Set window wize
	 *  This method is callded from setSize() and show().
	 * @param w width
	 * @param h height
	 */
	private void setWindowSize(int w, int h) {
		// バックグランドにある BufferedImage もサイズ変更する
		BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		// 古い内容のコピー
		panel.setSize(w, h);
		panel.setImage(newImage);
		if(visible) {
			width = w;
			height = h;
			frame.setSize(w, h);
		}
		g = newImage.getGraphics();
		g.clearRect(0, 0, width, height);
		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, width, height);
		g.drawImage(image, 0, 0, null);
		image = newImage;
	}

	/**
	 * Set font size
	 * @param size font size
	 */
	public void setFontSize(double size) {
		Font font = g.getFont();
		Font newFont = font.deriveFont((float)size);
		g.setFont(newFont);
	}

	/**
	 * Wait for mouse input
	 * @return
	 */
	public void waitForPoint() {
		synchronized(panel) {
			try {
				panel.wait();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Wait for mouse input with message
	 * @return
	 */
	public void waitForPoint(String message) {
		JOptionPane.showMessageDialog(null, message);
		waitForPoint();
	}

	/**
	 * Wait for count down
	 * @param time mille seconds
	 */
	public void waitForCountdown(long time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
		}
	}

	/**
	 * Disable auto repaint
	 */
	public void disableAutoRepaint() {
		repaintFlag = false;
	}

	/**
	 * Enable auto repaint
	 */
	public void enableAutoRepaint() {
		repaintFlag = true;
	}

	/**
	 * Set auto repaint
	 * @param flag true or false
	 */
	public void setAutoRepaint(boolean flag) {
		repaintFlag = flag;
	}

	/**
	 * Force repaint
	 */
	public void forceRepaint() {
		panel.repaint();
	}

	/**
	 * Get x coordinate pointed
	 * @return
	 */
	public int getPointedX(){
		return pointedX;
	}

	/**
	 * Get y coordinate pointed
	 * @return
	 */
	public int getPointedY() {
		return pointedY;
	}

	/**
	 * Save image to file
	 * @param file File object for saving
	 */
	public void save(File file) {
		try {
			ImageIO.write(image, "png", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Save image to file
	 * @param file file name for saving
	 */
	public void save(String fileName) {
		File file = new File(fileName);
		try {
			ImageIO.write(image, "png", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load image file
	 * @param file file name for loading
	 */
	public void load(String fileName) {
		drawImage(0, 0, fileName);
	}

	/**
	 * Load image file
	 * @param file File object for loading
	 */
	public void load(File file) {
		drawImage(0, 0, file);
	}

	private int round(double x) {
		return (int)(x + 0.5);
	}

	/**
	 * Show or hide title bar
	 * @param title true (show) or false (hide)
	 */
	public void setTitle(boolean title) {
		if(visible) {
			frame.setUndecorated(!title);
		}
	}

	/**
	 * image writer for animation GIF
	 */
	public ImageWriter iw = null;

	/**
	 * prepare animation GIF
	 * @param fileName
	 */
	public void prepareAnimationGIF(String fileName) {
		Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName("gif");
		iw = it.hasNext() ? (ImageWriter) it.next() : null;
		File outfile = new File(fileName);

		try {
			ImageOutputStream ios = ImageIO.createImageOutputStream(outfile);
			iw.setOutput(ios);
			iw.prepareWriteSequence(null);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * write one scene to animation GIF
	 */
	public void writeAnimationGIF() {
		try {
			if(iw != null) {
				iw.writeToSequence(new IIOImage(image, null, null), null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * close animation GIF
	 */
	public void finishAnimationGIF() {
		try {
			if(iw != null) {
				iw.endWriteSequence();
			}
			iw = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Panel for drawing
	 * @author satoru
	 *
	 */
	class DrawingPanel extends JPanel {
		/**
		 * image buffer
		 */
		private BufferedImage image;
		final long serialVersionUID = 0L;

		/**
		 * Constructor
		 * @param image
		 */
		DrawingPanel(BufferedImage image) {
			this.image = image;
		}

		/**
		 * Paint
		 */
		@Override
		public void paintComponent(Graphics g) {
	        g.drawImage(image, 0, 0, null);
		}

		/**
		 * Set image buffer
		 * @param image
		 */
		void setImage(BufferedImage image) {
			this.image = image;
		}
	}
}
