package pt.utl.ist.fenix.tools.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Vector;

import pt.utl.ist.fenix.tools.util.FileUtils;
import sun.awt.image.codec.JPEGImageEncoderImpl;

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
			final File inputDir = new File(args[0]);
            final String outputDirname = args[1];
            final int scaleRatio = Integer.parseInt(args[2]);
            final DWGProcessor processor = new DWGProcessor(scaleRatio);
			for (final File file : inputDir.listFiles()) {
				if (file.isFile()) {
					final String inputFilename = file.getAbsolutePath();
					if (inputFilename.endsWith(".dwg")) {
                        final String outputFilename = constructOutputFilename(file, outputDirname);
                        final OutputStream outputStream = new FileOutputStream(outputFilename);
                        try {
                            processor.generateJPEGImage(inputFilename, outputStream);
                        } catch (Error error) {
                            error.printStackTrace();
                        } finally {
                            outputStream.close();
                        }
					}
				}
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

    protected static String constructOutputFilename(final File inputFile, final String outputDirname) {
        final String simplename = inputFile.getName();
        return outputDirname + "/" + simplename.substring(0, simplename.length() - 3) + "jpg";
    }

    protected class Extremes {
        double minX = 0;
        double maxX = 0;
        double minY = 0;
        double maxY = 0;
        double max = 0;

        public Extremes(final Vector<DwgObject> dwgObjects) {
            for (final DwgObject dwgObject : dwgObjects) {
                if (dwgObject instanceof DwgArc) {
                    final DwgArc dwgArc = (DwgArc) dwgObject;
                } else if (dwgObject instanceof DwgText) {
                    final DwgText dwgText = (DwgText) dwgObject;
                    minX = Math.min(minX, dwgText.getInsertionPoint().getX());
                    minY = Math.min(minY, dwgText.getInsertionPoint().getY());
                    maxX = Math.max(maxX, dwgText.getInsertionPoint().getX());
                    maxY = Math.max(maxY, dwgText.getInsertionPoint().getY());
                } else if (dwgObject instanceof DwgLine) {
                    final DwgLine dwgLine = (DwgLine) dwgObject;
                    minX = Math.min(minX, dwgLine.getP1()[0]);
                    minY = Math.min(minY, dwgLine.getP1()[1]);
                    minX = Math.min(minX, dwgLine.getP2()[0]);
                    minY = Math.min(minY, dwgLine.getP2()[1]);
                    maxX = Math.max(maxX, dwgLine.getP1()[0]);
                    maxY = Math.max(maxY, dwgLine.getP1()[1]);
                    maxX = Math.max(maxX, dwgLine.getP2()[0]);
                    maxY = Math.max(maxY, dwgLine.getP2()[1]);
                } else {
                    //throw new IllegalArgumentException("Unknown DwgObject: " + dwgObject.getClass().getName());
                }
            }

            max = Math.max(maxX, maxY);
        }
    }

    private final int scaleRatio;
    private final int fontSize;
    private final int padding;
    private final int xAxisOffset;
    private final int yAxisOffset;

    public DWGProcessor(final int scaleRatio) {
        this.scaleRatio = scaleRatio;
        fontSize = (int) (scaleRatio * 0.008);
        padding = (int) (scaleRatio * 0.025);
        xAxisOffset = (int) (scaleRatio * 0.075);
        yAxisOffset = (int) (scaleRatio * 0.3);        
    }

    public void generateJPEGImage(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        final File file = FileUtils.copyToTemporaryFile(inputStream);
        generateJPEGImage(file.getAbsolutePath(), outputStream);
    }

    public void generateJPEGImage(final String filename, final OutputStream outputStream) throws IOException {
        final BufferedImage bufferedImage = process(filename, outputStream);
        final JPEGImageEncoder imageEncoder = new JPEGImageEncoderImpl(outputStream);
        imageEncoder.encode(bufferedImage);
        outputStream.close();
    }

    protected BufferedImage process(final String filename, final OutputStream outputStream) throws IOException {
        final DwgFile dwgFile = readDwgFile(filename);

        final Vector<DwgObject> dwgObjects = dwgFile.getDwgObjects();
        final Extremes extremes = new Extremes(dwgObjects);

        final BufferedImage bufferedImage = new BufferedImage(
                convCoord(extremes.maxX, extremes) + padding,
                convCoord(extremes.maxY, extremes) + padding,
                BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setFont(new Font(FONT_NAME, Font.PLAIN, fontSize));
        graphics2D.setBackground(Color.WHITE);
        graphics2D.setColor(Color.BLACK);
        graphics2D.clearRect(0, 0,
                convCoord(extremes.maxX, extremes) + padding,
                convCoord(extremes.maxY, extremes) + padding);

        for (final DwgObject dwgObject : dwgObjects) {
            if (dwgObject instanceof DwgLine) {
                final DwgLine dwgLine = (DwgLine) dwgObject;
                drawLine(extremes, graphics2D, dwgLine);
            } else if (dwgObject instanceof DwgArc) {
                final DwgArc dwgArc = (DwgArc) dwgObject;
                drawArc(extremes, graphics2D, dwgArc);
            } else if (dwgObject instanceof DwgText) {
                final DwgText dwgText = (DwgText) dwgObject;
                drawText(extremes, graphics2D, dwgText);
            }
        }

        graphics2D.dispose();
        return bufferedImage;
    }

    protected void drawLine(final Extremes extremes, final Graphics2D graphics2D, final DwgLine dwgLine) {
        final int x1 = convXCoord(dwgLine.getP1()[0], extremes);
        final int y1 = convYCoord(dwgLine.getP1()[1], extremes);
        final int x2 = convXCoord(dwgLine.getP2()[0], extremes);
        final int y2 = convYCoord(dwgLine.getP2()[1], extremes);

        graphics2D.drawLine(x1, y1, x2, y2);
    }

    protected void drawArc(final Extremes extremes, final Graphics2D graphics2D, final DwgArc dwgArc) {
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

        final int xmax = convXCoord(xc - radius, extremes);
        final int ymax = convYCoord(yc + radius, extremes);

        final int xmin = convXCoord(xc + radius, extremes);
        final int ymin = convYCoord(yc - radius, extremes);

        graphics2D.drawArc(xmax, ymax, Math.abs(xmax - xmin), Math.abs(ymax - ymin), startAngle, endAngle);
    }

    protected void drawText(final Extremes extremes, final Graphics2D graphics2D, final DwgText dwgText) {
        final Point2D point2D = dwgText.getInsertionPoint();
        graphics2D.drawString(dwgText.getText(),
                convXCoord(point2D.getX(), extremes),
                convYCoord(point2D.getY(), extremes));
    }

	protected DwgFile readDwgFile(final String filename) throws IOException {
        final DwgFile dwgFile = new DwgFile(filename);

        final PrintStream outPrintStream = System.out;
        final PrintStream errPrintStream = System.err;

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final PrintStream outputStream = new PrintStream(byteArrayOutputStream);
        System.setOut(outputStream);
        System.setErr(outputStream);            
        try {
            dwgFile.read();
        } finally {
            System.setOut(outPrintStream);
            System.setErr(errPrintStream);
            outputStream.close();
        }
        final String generatedOutput = byteArrayOutputStream.toString();
        final int indexOfError = generatedOutput.indexOf("ERROR: ");
        if (indexOfError != -1) {
            final int indexOfEndOfErrorMessage = generatedOutput.indexOf("\n", indexOfError);
            final String errorMessage = indexOfEndOfErrorMessage > indexOfError ?
                    generatedOutput.substring(indexOfError + 7, indexOfEndOfErrorMessage)
                    : generatedOutput.substring(indexOfError + 7);
            throw new Error(errorMessage);
        }

        return dwgFile;
    }

	protected int calcDegreeAngle(final double radians) {
		return (int) Math.round((radians * 180) / Math.PI);
	}

	protected int convXCoord(final double coordinate, final Extremes extremes) {
		return convCoord(coordinate, extremes) - xAxisOffset;
	}

	protected int convYCoord(final double coordinate, final Extremes extremes) {
		final int convCoord = convCoord(coordinate, extremes);
		return scaleRatio + padding - convCoord - yAxisOffset;
	}

	protected int convCoord(final double coordinate, final Extremes extremes) {
		return (int) Math.round((coordinate * scaleRatio) / extremes.max);
	}

    public static void generateJPEGImage(final InputStream inputStream, final OutputStream outputStream, final int scaleRatio) throws IOException {
        final DWGProcessor processor = new DWGProcessor(scaleRatio);
        processor.generateJPEGImage(inputStream, outputStream);
    }

    public static void generateJPEGImage(final String filename, final OutputStream outputStream, final int scaleRatio) throws IOException {
        final DWGProcessor processor = new DWGProcessor(scaleRatio);
        processor.generateJPEGImage(filename, outputStream);
    }

}
