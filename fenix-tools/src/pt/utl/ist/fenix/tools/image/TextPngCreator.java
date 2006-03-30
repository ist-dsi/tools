package pt.utl.ist.fenix.tools.image;

import java.awt.FontFormatException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.chimpen.txt2png.Txt2PngFactory;

public class TextPngCreator {

    public static byte[] createPng(final String fontFace, final String fontSize, final String fontColor,
            final String text) throws IOException, FontFormatException {
        Txt2PngFactory tpf = new Txt2PngFactory();

        // Set font face and size
        tpf.setFontFace(fontFace);
        tpf.setFontSize(Integer.parseInt(fontSize));

        // Convert rrggbb string to hex ints
        int r = Integer.parseInt(fontColor.substring(0, 2), 16);
        int g = Integer.parseInt(fontColor.substring(2, 4), 16);
        int b = Integer.parseInt(fontColor.substring(4), 16);

        // Set colour
        tpf.setTextRGB(r, g, b);

        // Set text to render
        tpf.setText(text);

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        tpf.createPngFile(byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

}
