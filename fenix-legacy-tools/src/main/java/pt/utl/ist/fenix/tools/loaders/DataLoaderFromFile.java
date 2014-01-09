/**
 * 
 */
package pt.utl.ist.fenix.tools.loaders;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * This class reads a file and loads it data to a collection of the
 * correspondent DTO. That DTO must implement the interface IFileLine
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class DataLoaderFromFile<T extends IFileLine> {

    protected final static Logger logger = LoggerFactory.getLogger(DataLoaderFromFile.class);

    public Collection<T> load(Class<T> clazz, String fileFullPathAndname) {
        return loadToMap(clazz, readFile(fileFullPathAndname)).values();
    }

    public Map<String, T> loadToMap(Class<T> clazz, String fileFullPathAndname) {
        return loadToMap(clazz, readFile(fileFullPathAndname));
    }

    public Collection<T> load(Class<T> clazz, InputStream stream, int size) {
        return loadToMap(clazz, readStream(stream, size)).values();
    }

    public Map<String, T> loadToMap(Class<T> clazz, InputStream stream, int size) {
        return loadToMap(clazz, readStream(stream, size));
    }

    public Collection<T> load(Class<T> clazz, byte[] contents) {
        return loadToMap(clazz, readContent(contents)).values();
    }

    public Map<String, T> loadToMap(Class<T> clazz, byte[] contents) {
        return loadToMap(clazz, readContent(contents));
    }

    private Map<String, T> loadToMap(Class<T> clazz, String[] data) {
        Map<String, T> result = new HashMap<String, T>(data.length);

        for (String dataLine : data) {
            T t = instanciateType(clazz);
            boolean shouldAdd = t.fillWithFileLineData(dataLine);

            if (shouldAdd) {
                result.put(((IFileLine) t).getUniqueKey(), t);
            }
        }

        return result;
    }

    private T instanciateType(Class<T> clazz) {
        T t = null;
        try {
            t = clazz.newInstance();

        } catch (InstantiationException e) {
            logger.error("", e);
            throw new RuntimeException(e);

        } catch (IllegalAccessException e) {
            logger.error("", e);
            throw new RuntimeException(e);

        }
        return t;
    }

    public static String[] readFile(String fileFullPathAndname) {
        logger.info("Reading file: " + fileFullPathAndname);

        InputStream stream = null;
        int size = (int) new File(fileFullPathAndname).length();

        try {
            stream = null;
            new BufferedInputStream(new FileInputStream(fileFullPathAndname));
        } catch (IOException e) {
            logger.error("Error loading file: " + fileFullPathAndname);
            e.printStackTrace();
            throw new RuntimeException();
        }

        return readStream(stream, size);
    }

    public static String[] readStream(InputStream inputStream, int size) {
        try {
            char[] buffer = new char[size];
            Reader reader = new InputStreamReader(inputStream, "UTF-8");

            reader.read(buffer);
            return new String(buffer).split("\n");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static String[] readContent(byte[] contents) {
        try {
            String fileContents = new String(contents, "UTF-8");
            return fileContents.split("\n");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
