package pt.linkare.ant.propreaders;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;

import pt.linkare.ant.InputProperty;
import pt.linkare.ant.InvalidPropertySpecException;
import pt.linkare.ant.NoPropertyReaderException;

public class PropertyReaderManager {

    private boolean debug = false;

    private String propertyCryptPassword = null;

    private String encoding = "iso-8859-1";

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    private PropertyReaderManager(String encoding) {
        super();
        setEncoding(encoding);
    }

    private static PropertyReaderManager instance = null;
    private String additionalPackageForPropertyReaders = null;

    public static PropertyReaderManager getInstance(boolean debug, String encoding) {
        if (instance == null) {
            instance = new PropertyReaderManager(encoding);
        }
        instance.setDebug(debug);
        return instance;
    }

    public static PropertyReaderManager getInstance(String encoding) {
        if (instance == null) {
            instance = new PropertyReaderManager(encoding);
        }
        return instance;
    }

    private HashMap<String, PropertyReader> registeredPropertyReaders = new HashMap<String, PropertyReader>();

    private static Class getClassForPropertyTypeFromSystemProperty(String type) {
        String className = System.getProperty("property.reader." + type);
        if (className == null) {
            return null;
        }
        try {
            Class cPropReader = Class.forName(className);
            if (PropertyReader.class.isAssignableFrom(cPropReader)) {
                return cPropReader;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private static Class getClassForPropertyTypeFromClassPath(String type) {
        String className = PropertyReader.class.getPackage().getName() + "." + capitalize(type) + "PropertyReader";
        try {
            Class cPropReader = Class.forName(className);
            if (PropertyReader.class.isAssignableFrom(cPropReader)) {
                return cPropReader;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

    }

    private static Class getClassForPropertyTypeFromAdditionalPackage(String type, String encoding) {
        String packagePrefix = getInstance(encoding).getAdditionalPackageForPropertyReaders();
        if (packagePrefix == null) {
            return null;
        }
        String className = packagePrefix + "." + capitalize(type) + "PropertyReader";
        try {
            Class cPropReader = Class.forName(className);
            if (PropertyReader.class.isAssignableFrom(cPropReader)) {
                return cPropReader;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private static Class getClassForPropertyType(String type, String encoding) {
        Class c = getClassForPropertyTypeFromSystemProperty(type);
        if (c == null) {
            c = getClassForPropertyTypeFromClassPath(type);
        }
        if (c == null) {
            c = getClassForPropertyTypeFromAdditionalPackage(type, encoding);
        }
        return c;
    }

    private static PropertyReader getPropertyReaderForType(String type, String encoding) {
        if (getInstance(encoding).registeredPropertyReaders.containsKey(type)) {
            return getInstance(encoding).registeredPropertyReaders.get(type);
        }

        Class cPropReader = getClassForPropertyType(type, encoding);
        if (cPropReader != null) {
            PropertyReader reader;
            try {
                reader = (PropertyReader) cPropReader.newInstance();
                getInstance(encoding).registeredPropertyReaders.put(type, reader);
                reader.setDebug(getInstance(encoding).isDebug());
                return reader;
            } catch (InstantiationException e) {
                System.out.println("Unable to instantiate class " + cPropReader.getName());
            } catch (IllegalAccessException e) {
                System.out.println("Illegal access trying to instantiate class " + cPropReader.getName());
            } catch (ClassCastException e) {
                System.out.println("Class " + cPropReader.getName() + " is not of type PropertyReader");
            }

            return null;
        } else {
            return null;
        }

    }

    private static String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public Collection<InputProperty> readProperty(InputProperty prop, boolean fromDefault) throws InvalidPropertySpecException,
            NoPropertyReaderException, UnsupportedEncodingException {
        if (prop != null) {
            PropertyReader propReader = getPropertyReaderForType(prop.getPropertyType(), encoding);
            if (propReader == null) {
                throw new NoPropertyReaderException("No property reader available for property of type " + prop.getPropertyType());
            }
            propReader.setProperty(prop);
            propReader.setEncoding(getEncoding());
            return propReader.readPropertyValue(fromDefault);

        } else {
            throw new RuntimeException("Trying to read null property");
        }
    }

    /**
     * @return Returns the additionalPackageForPropertyReaders.
     */
    public String getAdditionalPackageForPropertyReaders() {
        return additionalPackageForPropertyReaders;
    }

    /**
     * @param additionalPackageForPropertyReaders The additionalPackageForPropertyReaders to set.
     */
    public void setAdditionalPackageForPropertyReaders(String additionalPackageForPropertyReaders) {
        this.additionalPackageForPropertyReaders = additionalPackageForPropertyReaders;
    }

    /**
     * @return Returns the debug.
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * @param debug The debug to set.
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getPropertyCryptPassword() {
        return propertyCryptPassword;
    }

    public void setPropertyCryptPassword(String propertyCryptPassword) {
        this.propertyCryptPassword = propertyCryptPassword;
    }

}
