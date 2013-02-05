package pt.linkare.ant;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public interface TextBlockParser {
    public static String CRLF = System.getProperty("line.separator");

    public List<TextBlock> readBlocks(Reader in) throws IOException;

    public abstract static class TextBlockParserManager {
        public static TextBlockParser getBlockParserByFileName(String fileName) {
            if (fileName.endsWith(".css")) {
                return new CSSTextBlockParser();
            } else if (fileName.endsWith(".properties")) {
                return new PropertyTextBlockParser();
            }

            throw new RuntimeException("No parser available for file " + fileName
                    + " (YET!!! - you're more than welcome!!! ;) )!");
        }
    }

}
