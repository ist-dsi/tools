package pt.utl.ist.fenix.tools.image;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import sun.awt.image.codec.JPEGImageEncoderImpl;

import com.hardcode.gdbms.engine.data.driver.DriverException;
import com.iver.cit.jdwglib.dwg.DwgFile;
import com.iver.cit.jdwglib.dwg.DwgObject;
import com.iver.cit.jdwglib.dwg.objects.DwgArc;
import com.iver.cit.jdwglib.dwg.objects.DwgLine;
import com.iver.cit.jdwglib.dwg.objects.DwgText;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class DWGProcessor {

	private static final int SCALE_RATIO = 1000;
	private static final String FONT_NAME = "Helvetica";

	private static final int FONT_SIZE = (int) (SCALE_RATIO * 0.008);
	private static final int PADDING = (int) (SCALE_RATIO * 0.025);
	private static final int X_AXIS_OFFSET = (int) (SCALE_RATIO * 0.075);
	private static final int Y_AXIS_OFFSET = (int) (SCALE_RATIO * 0.3);

	public static void main(String[] args) {
		try {
			run("/home/marvin/i/Civilp02.dwg");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	private static void run(final String filename) throws IOException, DriverException {
		DwgFile dwgFile = new DwgFile(filename);
		dwgFile.read();

		System.out.println("Read: " + dwgFile.getFileName());
		System.out.println("dwgFile.isDwg3DFile(): " + dwgFile.isDwg3DFile());
		Vector vector = dwgFile.getDwgObjects();
		System.out.println("vector.size(): " + vector.size());
		double minX = 0;
		double minY = 0;
		double maxX = 0;
		double maxY = 0;
		double max = 0;
		for (Object object : vector) {
			final DwgObject dwgObject = (DwgObject) object;
			if (object instanceof DwgArc) {
				DwgArc dwgArc = (DwgArc) object;
			} else if (object instanceof DwgText) {
				DwgText dwgText = (DwgText) object;
				minX = Math.min(minX, dwgText.getInsertionPoint().getX());
				minY = Math.min(minY, dwgText.getInsertionPoint().getY());
				maxX = Math.max(maxX, dwgText.getInsertionPoint().getX());
				maxY = Math.max(maxY, dwgText.getInsertionPoint().getY());
			} else if (object instanceof DwgLine) {
				final DwgLine dwgLine = (DwgLine) object;
				minX = Math.min(minX, dwgLine.getP1()[0]);
				minY = Math.min(minY, dwgLine.getP1()[1]);
				minX = Math.min(minX, dwgLine.getP2()[0]);
				minY = Math.min(minY, dwgLine.getP2()[1]);
				maxX = Math.max(maxX, dwgLine.getP1()[0]);
				maxY = Math.max(maxY, dwgLine.getP1()[1]);
				maxX = Math.max(maxX, dwgLine.getP2()[0]);
				maxY = Math.max(maxY, dwgLine.getP2()[1]);
			} else {
//				System.out.println("object.getClass().getName(): " + object.getClass().getName());
			}
		}

		System.out.println("Max x: " + maxX);
		System.out.println("Max y: " + maxY);
		max = Math.max(maxX, maxY);

		final BufferedImage bufferedImage = new BufferedImage(convCoord(maxX, max) + PADDING, convCoord(maxY, max) + PADDING, BufferedImage.TYPE_INT_RGB);
		final Graphics2D graphics2D = bufferedImage.createGraphics();
		graphics2D.setFont(new Font(FONT_NAME, Font.PLAIN, FONT_SIZE));

		maxX = max;
		maxY = max;

		for (final Object object : vector) {
			if (object instanceof DwgLine) {
				final DwgLine dwgLine = (DwgLine) object;
				final int x1 = convXCoord(dwgLine.getP1()[0], maxX, minX);
				final int y1 = convYCoord(dwgLine.getP1()[1], maxY, minY);
				final int x2 = convXCoord(dwgLine.getP2()[0], maxX, minX);
				final int y2 = convYCoord(dwgLine.getP2()[1], maxY, minY);
				graphics2D.drawLine(x1, y1, x2, y2);
//			} else if (object instanceof DwgArc) {
//				final DwgArc dwgArc = (DwgArc) object;
//				final double xc = dwgArc.getCenter()[0];
//				final double yc = dwgArc.getCenter()[1];
//				final double radius = dwgArc.getRadius();
//				final double xi = radius * Math.cos(dwgArc.getInitAngle()) + xc;
//				final double yi = radius * Math.sin(dwgArc.getInitAngle()) + yc;
//				final double xf = radius * Math.cos(dwgArc.getEndAngle()) + xc;
//				final double yf = radius * Math.sin(dwgArc.getEndAngle()) + yc;
//
//				System.out.println("xi: " + xi);
//				System.out.println("yi: " + yi);
//				System.out.println("xf: " + xf);
//				System.out.println("yf: " + yf);
//
//				final int x1 = convXCoord(xi, maxX);
//				final int y1 = convYCoord(yi, maxY);
//				final int x2 = convXCoord(xf, maxX);
//				final int y2 = convYCoord(yf, maxY);
//
//				System.out.println("x1: " + x1);
//				System.out.println("y1: " + y1);
//				System.out.println("x2: " + x2);
//				System.out.println("y2: " + y2);
//
//				graphics2D.drawLine(x1, y1, x2, y2);
//
//				final int x;
//				final int y;
//				final int w = Math.abs(Math.abs(x2) - Math.abs(x1));
//				final int h = Math.abs(Math.abs(y2) - Math.abs(y1));
//				final int startAngle;
//				final int arcAngle;
//				if (y1 < y2) {
//					y = y1;
//					x = x1;
//					startAngle = calcDegreeAngle(dwgArc.getInitAngle());
//					arcAngle = calcDegreeAngle(dwgArc.getEndAngle());
//				} else {
//					y = y2;
//					x = x2;
//					startAngle = calcDegreeAngle(dwgArc.getEndAngle());
//					arcAngle = calcDegreeAngle(dwgArc.getInitAngle());
//				}
//
//				System.out.println("calcDegreeAngle(dwgArc.getInitAngle()): " + calcDegreeAngle(dwgArc.getInitAngle()));
//				System.out.println("calcDegreeAngle(dwgArc.getEndAngle()): " + calcDegreeAngle(dwgArc.getEndAngle()));
//
//				graphics2D.drawArc(x, y, w, h, startAngle, arcAngle);
			} else if (object instanceof DwgText) {
				final DwgText dwgText = (DwgText) object;
				final Point2D point2D = dwgText.getInsertionPoint();
				graphics2D.drawString(dwgText.getText(), convXCoord(point2D.getX(), maxX, minX), convYCoord(point2D.getY(), maxY, minY));
			}
		}

		graphics2D.dispose();

		final FileOutputStream fileOutputStream = new FileOutputStream("/tmp/xpto.jpeg");
		final JPEGImageEncoder imageEncoder = new JPEGImageEncoderImpl(fileOutputStream);
		imageEncoder.encode(bufferedImage);
		fileOutputStream.close();
	}

//	private static int calcDegreeAngle(final double radians) {
//		return -1 * (int) Math.round((radians * 180) / Math.PI);
//	}

	private static int convXCoord(final double coordinate, final double max, final double min) {
		return convCoord(coordinate, max) - X_AXIS_OFFSET;
	}

	private static int convYCoord(final double coordinate, final double max, final double min) {
		final int convCoord = convCoord(coordinate, max);
		return SCALE_RATIO + PADDING - convCoord - Y_AXIS_OFFSET;
	}

	private static int convCoord(final double coordinate, final double max) {
		return (int) Math.round((coordinate * SCALE_RATIO) / max);
	}

}
