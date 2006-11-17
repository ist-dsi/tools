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
import java.util.Properties;
import java.util.Vector;

import pt.utl.ist.fenix.tools.util.FileUtils;
import pt.utl.ist.fenix.tools.util.PropertiesManager;
import sun.awt.image.codec.JPEGImageEncoderImpl;

import com.iver.cit.jdwglib.dwg.DwgFile;
import com.iver.cit.jdwglib.dwg.DwgObject;
import com.iver.cit.jdwglib.dwg.objects.DwgArc;
import com.iver.cit.jdwglib.dwg.objects.DwgLine;
import com.iver.cit.jdwglib.dwg.objects.DwgText;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class DWGProcessor {

    private static final String FONT_NAME = "Helvetica";

    protected final int scaleRatio;

    protected final int fontSize;

    protected final int padding;

    protected final int xAxisOffset;

    protected final int yAxisOffset;

    public DWGProcessor() throws IOException {

	Properties properties = PropertiesManager.loadProperties("/configuration.properties");
	String scaleRatioString = properties.getProperty("scaleRatio");
	String fontSizeString = properties.getProperty("fontSize");
	String paddingString = properties.getProperty("padding");
	String xAxisOffsetString = properties.getProperty("xAxisOffset");
	String yAxisOffsetString = properties.getProperty("yAxisOffset");

	scaleRatio = (int) Double.valueOf(scaleRatioString).doubleValue();
	fontSize = (int) (scaleRatio * Double.valueOf(fontSizeString));
	padding = (int) (scaleRatio * Double.valueOf(paddingString));
	xAxisOffset = (int) (scaleRatio * Double.valueOf(xAxisOffsetString));
	yAxisOffset = (int) (scaleRatio * Double.valueOf(yAxisOffsetString));
    }

    protected static String constructOutputFilename(final File inputFile, final String outputDirname) {
	final String simplename = inputFile.getName();
	return outputDirname + "/" + simplename.substring(0, simplename.length() - 3) + "jpg";
    }

    public void generateJPEGImage(final InputStream inputStream, final OutputStream outputStream)
	    throws IOException {

	final File file = FileUtils.copyToTemporaryFile(inputStream);
	generateJPEGImage(file.getAbsolutePath(), outputStream);
    }

    public void generateJPEGImage(final String filename, final OutputStream outputStream)
	    throws IOException {

	final BufferedImage bufferedImage = process(filename, outputStream);
	final JPEGImageEncoder imageEncoder = new JPEGImageEncoderImpl(outputStream);
	imageEncoder.encode(bufferedImage);
	outputStream.close();
    }

    protected BufferedImage process(final String filename, final OutputStream outputStream)
	    throws IOException {
	final DwgFile dwgFile = readDwgFile(filename);

	final Vector<DwgObject> dwgObjects = dwgFile.getDwgObjects();
	final ReferenceConverter referenceConverter = new ReferenceConverter(dwgObjects, scaleRatio);

	final BufferedImage bufferedImage = new BufferedImage((int) referenceConverter
		.convX(referenceConverter.maxX),
		(int) referenceConverter.convY(referenceConverter.minY), BufferedImage.TYPE_INT_RGB);

	final Graphics2D graphics2D = bufferedImage.createGraphics();
	graphics2D.setFont(new Font(FONT_NAME, Font.PLAIN, fontSize));
	graphics2D.setBackground(Color.WHITE);
	graphics2D.setColor(Color.BLACK);
	graphics2D.clearRect(0, 0, (int) referenceConverter.convX(referenceConverter.maxX),
		(int) referenceConverter.convY(referenceConverter.minY));

	for (final DwgObject dwgObject : dwgObjects) {
	    drawObject(referenceConverter, graphics2D, dwgObject);
	}

	graphics2D.dispose();
	return bufferedImage;
    }

    private void drawObject(final ReferenceConverter referenceConverter, final Graphics2D graphics2D,
	    final DwgObject dwgObject) {

	if (dwgObject.getColor() != 0) {
	    if (dwgObject instanceof DwgLine) {
		final DwgLine dwgLine = (DwgLine) dwgObject;
		drawLine(referenceConverter, graphics2D, dwgLine);
	    } else if (dwgObject instanceof DwgArc) {
		final DwgArc dwgArc = (DwgArc) dwgObject;
		drawArc(referenceConverter, graphics2D, dwgArc);
	    } else if (dwgObject instanceof DwgText) {
		final DwgText dwgText = (DwgText) dwgObject;
		drawText(referenceConverter, graphics2D, dwgText);
	    }
	}
    }

    protected void drawLine(final ReferenceConverter referenceConverter, final Graphics2D graphics2D,
	    final DwgLine dwgLine) {

	final int x1 = convXCoord(dwgLine.getP1()[0], referenceConverter);
	final int y1 = convYCoord(dwgLine.getP1()[1], referenceConverter);
	final int x2 = convXCoord(dwgLine.getP2()[0], referenceConverter);
	final int y2 = convYCoord(dwgLine.getP2()[1], referenceConverter);

	graphics2D.drawLine(x1, y1, x2, y2);
    }

    protected void drawArc(final ReferenceConverter referenceConverter, final Graphics2D graphics2D,
	    final DwgArc dwgArc) {

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
	    endAngle = -1 * calcDegreeAngle(Math.abs(Math.abs(ti) - Math.abs(tf + 2 * Math.PI)));
	}

	final int xmax = convXCoord(xc - radius, referenceConverter);
	final int ymax = convYCoord(yc + radius, referenceConverter);

	final int xmin = convXCoord(xc + radius, referenceConverter);
	final int ymin = convYCoord(yc - radius, referenceConverter);

	graphics2D.drawArc(xmax, ymax, Math.abs(xmax - xmin), Math.abs(ymax - ymin), startAngle,
		endAngle);
    }

    protected void drawText(final ReferenceConverter referenceConverter, final Graphics2D graphics2D,
	    final DwgText dwgText) {
	final Point2D point2D = dwgText.getInsertionPoint();
	graphics2D.drawString(dwgText.getText(), convXCoord(point2D.getX(), referenceConverter),
		convYCoord(point2D.getY(), referenceConverter));
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
	    final String errorMessage = indexOfEndOfErrorMessage > indexOfError ? generatedOutput
		    .substring(indexOfError + 7, indexOfEndOfErrorMessage) : generatedOutput
		    .substring(indexOfError + 7);
	    throw new Error(errorMessage);
	}

	return dwgFile;
    }

    protected int calcDegreeAngle(final double radians) {
	return (int) Math.round((radians * 180) / Math.PI);
    }

    protected int convXCoord(final double coordinate, final ReferenceConverter referenceConverter) {
	return (int) referenceConverter.convX(coordinate);
    }

    protected int convYCoord(final double coordinate, final ReferenceConverter referenceConverter) {
	return (int) referenceConverter.convY(coordinate);
    }

    public static class ReferenceConverter {
	double minX = Double.MAX_VALUE;

	double maxX = Double.MAX_VALUE * -1.0;

	double minY = Double.MAX_VALUE;

	double maxY = Double.MAX_VALUE * -1.0;

	int scaleRatio = 0;

	public ReferenceConverter(final Vector<DwgObject> dwgObjects, int scaleRatio) {
	    this.scaleRatio = scaleRatio;
	    for (final DwgObject dwgObject : dwgObjects) {
		if (dwgObject.getColor() == 0) {
		    continue;

		} else if (dwgObject instanceof DwgText) {
		    final DwgText dwgText = (DwgText) dwgObject;

		    minX = Math.min(minX, dwgText.getInsertionPoint().getX());
		    minY = Math.min(minY, dwgText.getInsertionPoint().getY());

		    maxX = Math.max(maxX, dwgText.getInsertionPoint().getX());
		    maxY = Math.max(maxY, dwgText.getInsertionPoint().getY());

		} else if (dwgObject instanceof DwgArc) {
		    final DwgArc dwgArc = (DwgArc) dwgObject;
		    final double ti = dwgArc.getInitAngle();
		    final double tf = dwgArc.getEndAngle();		    

		    if (ti != tf) {
						
			final double r = dwgArc.getRadius();
			final double xc = dwgArc.getCenter()[0];
			final double yc = dwgArc.getCenter()[1];
			
			final double xi = r * Math.cos(ti) + xc;
			final double yi = r * Math.sin(ti) + yc;
			
			final double xf = r * Math.cos(tf) + xc;
			final double yf = r * Math.sin(tf) + yc;

			minX = Math.min(minX, xi);
			minX = Math.min(minX, xf);					
			minY = Math.min(minY, yi);
			minY = Math.min(minY, yf);

			maxX = Math.max(maxX, xi);
			maxX = Math.max(maxX, xf);
			maxY = Math.max(maxY, yi);			
			maxY = Math.max(maxY, yf);
		    }

		} else if (dwgObject instanceof DwgLine) {
		    final DwgLine dwgLine = (DwgLine) dwgObject;

		    minX = Math.min(minX, dwgLine.getP1()[0]);
		    minY = Math.min(minY, dwgLine.getP1()[1]);
		    maxX = Math.max(maxX, dwgLine.getP1()[0]);
		    maxY = Math.max(maxY, dwgLine.getP1()[1]);

		    minX = Math.min(minX, dwgLine.getP2()[0]);
		    minY = Math.min(minY, dwgLine.getP2()[1]);
		    maxX = Math.max(maxX, dwgLine.getP2()[0]);
		    maxY = Math.max(maxY, dwgLine.getP2()[1]);

		    // } else if (dwgObject instanceof DwgLwPolyline) {
		    // final DwgLwPolyline dwgLwPolyline = (DwgLwPolyline)
		    // dwgObject;
		    // } else if (dwgObject instanceof DwgBlockHeader) {
		    // final DwgBlockHeader dwgBlockHeader =
		    // (DwgBlockHeader) dwgObject;
		    // } else if (dwgObject instanceof DwgLayer) {
		    // final DwgLayer dwgLayer = (DwgLayer) dwgObject;
		    // } else if (dwgObject instanceof DwgSolid) {
		    // final DwgSolid dwgSolid = (DwgSolid) dwgObject;

		} else {
		    // System.out.println("otherObject: " +
		    // dwgObject.getClass().getName());
		    // throw new IllegalArgumentException("Unknown
		    // DwgObject: " + dwgObject.getClass().getName());
		}
	    }
	}

	public double convX(final double x) {
	    return (x - minX) * scaleRatio / maxX;
	}

	public double convY(final double y) {
	    return (maxY - y) * scaleRatio / maxX;
	}
    }

    public static void main(String[] args) {
	try {
	    final File inputDir = new File(args[0]);
	    final String outputDirname = args[1];
	    final DWGProcessor processor = new DWGProcessor();
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
}
