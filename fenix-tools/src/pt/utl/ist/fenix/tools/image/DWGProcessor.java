package pt.utl.ist.fenix.tools.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
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

	private static final String FONT_NAME = "Helvetica";

	public static void main(String[] args) {
		try {
			final File dir = new File(args[0]);
			for (final File file : dir.listFiles()) {
				if (file.isFile()) {
					final String filename = file.getAbsolutePath();
					if (filename.endsWith(".dwg")) {
						run(filename, Integer.parseInt(args[1]));
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	private static void run(final String filename, final int scaleRatio) throws IOException, DriverException {
		final int fontSize = (int) (scaleRatio * 0.008);
		final int padding = (int) (scaleRatio * 0.025);
		final int xAxisOffset = (int) (scaleRatio * 0.075);
		final int yAxisOffset = (int) (scaleRatio * 0.3);

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

		final BufferedImage bufferedImage = new BufferedImage(
				convCoord(maxX, max, scaleRatio) + padding,
				convCoord(maxY, max, scaleRatio) + padding, BufferedImage.TYPE_INT_RGB);
		final Graphics2D graphics2D = bufferedImage.createGraphics();
		graphics2D.setFont(new Font(FONT_NAME, Font.PLAIN, fontSize));
		graphics2D.setBackground(Color.WHITE);
		graphics2D.setColor(Color.BLACK);
		graphics2D.clearRect(0, 0, convCoord(maxX, max, scaleRatio) + padding, convCoord(maxY, max, scaleRatio) + padding);

		maxX = max;
		maxY = max;

		for (final Object object : vector) {
			if (object instanceof DwgLine) {
				final DwgLine dwgLine = (DwgLine) object;
				final int x1 = convXCoord(dwgLine.getP1()[0], maxX, minX, xAxisOffset, scaleRatio);
				final int y1 = convYCoord(dwgLine.getP1()[1], maxY, minY, yAxisOffset, scaleRatio, padding);
				final int x2 = convXCoord(dwgLine.getP2()[0], maxX, minX, xAxisOffset, scaleRatio);
				final int y2 = convYCoord(dwgLine.getP2()[1], maxY, minY, yAxisOffset, scaleRatio, padding);
				graphics2D.drawLine(x1, y1, x2, y2);
			} else if (object instanceof DwgArc) {
				final DwgArc dwgArc = (DwgArc) object;
                final double radius = dwgArc.getRadius();
                final double xc = dwgArc.getCenter()[0];
				final double yc = dwgArc.getCenter()[1];
                final double ti = dwgArc.getInitAngle();
                final double tf = dwgArc.getEndAngle();
                final int startAngle;
                final int endAngle;
                if (tf > ti) {
                    startAngle = calcDegreeAngle(ti);
                    endAngle = calcDegreeAngle(Math.abs(Math.abs(tf) - Math.abs(ti)));
                } else {
                    startAngle = calcDegreeAngle(tf);
                    endAngle = -1 * calcDegreeAngle(Math.abs(Math.abs(ti) - Math.abs(tf + 2*Math.PI)));
                }

              final int xmax = convXCoord(xc - radius, maxX, minX, xAxisOffset, scaleRatio);
              final int ymax = convYCoord(yc + radius, maxY, minY, yAxisOffset, scaleRatio, padding);

              final int xmin = convXCoord(xc + radius, maxX, minX, xAxisOffset, scaleRatio);
              final int ymin = convYCoord(yc - radius, maxY, minY, yAxisOffset, scaleRatio, padding);

              graphics2D.drawArc(xmax, ymax, Math.abs(xmax - xmin), Math.abs(ymax - ymin), startAngle, endAngle);
			} else if (object instanceof DwgText) {
				final DwgText dwgText = (DwgText) object;
				final Point2D point2D = dwgText.getInsertionPoint();
				graphics2D.drawString(
						dwgText.getText(), convXCoord(point2D.getX(), maxX, minX, xAxisOffset, scaleRatio),
						convYCoord(point2D.getY(), maxY, minY, yAxisOffset, scaleRatio, padding));
			}
		}

		graphics2D.dispose();

		final String outputFilename = constructOutputFilename(filename);
		final FileOutputStream fileOutputStream = new FileOutputStream(outputFilename);
		final JPEGImageEncoder imageEncoder = new JPEGImageEncoderImpl(fileOutputStream);
		imageEncoder.encode(bufferedImage);
		fileOutputStream.close();
	}

	private static int calcDegreeAngle(final double radians) {
		return (int) Math.round((radians * 180) / Math.PI);
	}

	private static int convXCoord(final double coordinate, final double max, final double min, final int xAxisOffset, final int scaleRatio) {
		return convCoord(coordinate, max, scaleRatio) - xAxisOffset;
	}

	private static int convYCoord(final double coordinate, final double max, final double min,
			final int yAxisOffset, final int scaleRatio, final int padding) {
		final int convCoord = convCoord(coordinate, max, scaleRatio);
		return scaleRatio + padding - convCoord - yAxisOffset;
	}

	private static int convCoord(final double coordinate, final double max, final int scaleRatio) {
		return (int) Math.round((coordinate * scaleRatio) / max);
	}

	private static String constructOutputFilename(final String filename) {
		final File file = new File(filename);
		final String simplename = file.getName();
		final int indexOfLastDot = simplename.lastIndexOf('.');
		final int endOfName = indexOfLastDot > 0 ? indexOfLastDot : simplename.length();
		return "/tmp/" + simplename.substring(0, endOfName) + ".jpg";
	}

}
