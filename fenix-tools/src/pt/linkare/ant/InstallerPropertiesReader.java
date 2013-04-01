package pt.linkare.ant;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import pt.linkare.ant.propreaders.PropertyReaderManager;

/**
 * This class is the main responsible for parsing the properties spec file and asking for input to the user... It
 * controls the main process - It is not dependent of ant
 * 
 * @author jpereira - Linkare TI
 */

public class InstallerPropertiesReader {
    // The input property file
    private File propFileInput = null;

    // The output property file
    private File propFileOutput = null;

    // were the properties read from the ciphered file?
    private boolean defaultInLastPropertiesFile = false;

    private boolean debug = false;

    private static InstallerPropertiesReader instance = null;

    private InputPropertyMap allProperties = new InputPropertyMap();

    private String encoding;

    /**
     * Helper constructer
     * 
     * @param propFileInput
     *            The input file to read the properties spec
     * @param propFileOutput
     *            The generated output properties file
     * @param encoding
     * @throws UnsupportedEncodingException
     */
    private InstallerPropertiesReader(File propFileInput, File propFileOutput, boolean debug, String encoding)
            throws UnsupportedEncodingException {
        super();
        this.debug = debug;
        this.setEncoding(encoding);
        this.setPropFileInput(propFileInput);
        this.setPropFileOutput(propFileOutput);
    }

    private void setEncoding(String encoding) {
        this.encoding = encoding;
        debug("Encoding is set to " + encoding);
    }

    /**
     * Default constructor - for completeness
     */
    private InstallerPropertiesReader() {
        super();
    }

    public synchronized static InstallerPropertiesReader getInstance() {
        if (instance == null) {
            instance = new InstallerPropertiesReader();
        }

        return instance;

    }

    public synchronized static InstallerPropertiesReader getInstance(File inputFile, File outputFile, boolean debug,
            String encoding) throws UnsupportedEncodingException {
        instance = new InstallerPropertiesReader(inputFile, outputFile, debug, encoding);
        return instance;
    }

    /**
     * JavaBeans property accessor
     * 
     * @return Returns the File of input properties spec
     */
    public File getPropFileInput() {
        return propFileInput;
    }

    /**
     * JavaBeans property accessor
     * 
     * @param propFileInput
     *            The propFileInput to set.
     */
    public void setPropFileInput(File propFileInput) {
        this.propFileInput = propFileInput;
    }

    /**
     * @return Returns the propFileOutput.
     */
    public File getPropFileOutput() {
        return propFileOutput;
    }

    /**
     * @param propFileOutput
     *            The propFileOutput to set.
     * @throws UnsupportedEncodingException
     */
    public void setPropFileOutput(File propFileOutput) throws UnsupportedEncodingException {
        this.propFileOutput = propFileOutput;
        readOutputPropertiesFile();
        defaultInLastPropertiesFile = readLastPropertiesFile();
    }

    public static Properties readProperties(File fInput, File fOutput, boolean debug, String encoding) throws IOException,
            InvalidPropertySpecException, NoPropertyReaderException {

        InstallerPropertiesReader propReader = InstallerPropertiesReader.getInstance(fInput, fOutput, debug, encoding);

        propReader.parse();

        ArrayList<InputProperty> generatedProperties = new ArrayList<InputProperty>();

        for (InputProperty prop : propReader.allProperties) {
            Collection<InputProperty> retVal = prop.readNow(propReader.defaultInLastPropertiesFile);
            if (retVal != null) {
                propReader.debug("While reading property " + prop.getPropertyName() + " additional " + retVal.size()
                        + " generated properties were returned!");
                generatedProperties.addAll(retVal);
            }
        }

        try {
            for (InputProperty prop : generatedProperties) {
                propReader.debug("now reading generated property " + prop.getPropertyName());
                prop.readNow(propReader.defaultInLastPropertiesFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        propReader.allProperties.putAll(generatedProperties);
        return PropertiesSerializer.outputPropertiesFile(propReader.getPropFileOutput(), propReader.allProperties,
                propReader.defaultInLastPropertiesFile, encoding);
    }

    public void parse() throws IOException {

        debug("Trying to read from file " + getPropFileInput().getName());
        BufferedReader br = new BufferedReader(new FileReader(getPropFileInput()));
        StringBuilder propMetaInfo = new StringBuilder();
        String propName = null;
        String propDefaultValue = null;
        String line = null;

        while ((line = br.readLine()) != null) {
            if (line.trim().length() == 0) {
                debug("Empty line found in file");
                continue;
            }
            if (line.trim().startsWith("#")) {
                debug("Comment line found! Appendding to property metainfo!");
                if (propMetaInfo.length() != 0) {
                    propMetaInfo.append(CRLF);
                }
                propMetaInfo.append(line.substring(1));
            } else {// line is not empty and does not start with a comment... it is a property def
                debug("Line is not empty and it does not start with a comment... It is a property then!");
                int positionEquals = line.indexOf('=');
                debug("Found an equals (=) sign at position " + positionEquals + " in line!");
                if (positionEquals > -1) {
                    propName = line.substring(0, positionEquals);
                    propDefaultValue = line.substring(positionEquals + 1);
                } else {
                    propName = line;
                }

                debug("Read property name " + propName + " and property default value " + propDefaultValue);
                allProperties.put(parseInputPropertyMetaInfo(allProperties, propName, propMetaInfo.toString(), propDefaultValue));
                propMetaInfo = new StringBuilder();
                propName = null;
                propDefaultValue = null;
            }
        }

        debug("Cycling dependencies to find (and remove) dependencies on not specified properties or to find the parent properties reference based on name!");
        for (InputProperty prop : this.allProperties) {
            List<PropertyDependency> dependencies = prop.getDependencies();
            List<PropertyDependency> dependencyRemove = new ArrayList<PropertyDependency>();
            for (PropertyDependency propDep : dependencies) {
                InputProperty parentProperty = allProperties.get(propDep.getParentPropertyName());

                if (parentProperty == null) {
                    debug("Dependency parent property " + propDep.getParentPropertyName() + " was not found for property "
                            + prop.getPropertyName() + "! Removing dependency!");
                    dependencyRemove.add(propDep);
                } else {
                    debug("Found dependency parent property " + propDep.getParentPropertyName() + " for property "
                            + prop.getPropertyName());
                    propDep.setParentProperty(parentProperty);
                }
            }
            dependencies.removeAll(dependencyRemove);
        }

    }

    public InputProperty parseInputPropertyMetaInfo(InputPropertyMap map, java.lang.String propName, java.lang.String metadata,
            java.lang.String defaultPropValue) {
        debug("Parsing metainfo for property " + propName);
        InputProperty retVal = new InputProperty(map, getEncoding());

        retVal.setPropertyName(propName);

        retVal.setPropertyDefaultValue(getDefaultValue(propName, defaultPropValue));

        StringReader sr = new StringReader(metadata);
        BufferedReader br = new BufferedReader(sr);
        HashMap<java.lang.String, java.lang.String> metadatas = new HashMap<java.lang.String, java.lang.String>();

        ArrayList<PropertyDependency> dependencies = new ArrayList<PropertyDependency>();

        try {
            // read properties meta info in format
            // @message=.....
            // .....
            // .....
            // 
            // @type=String
            // @required=true
            // @persist=false
            // @metadataName=teste
            // @dependency=property.name=value - depends on property.name having value
            // @dependency=property2.name=value2 - also depends on property2.name having value2
            // @dependency=property3.name=* - also depends on property3.name being defined - no matter the value
            java.lang.String previousKey = null;
            StringBuilder contentPreviousKey = new StringBuilder();

            java.lang.String line = null;

            debug("Reading property " + propName + " metainfo");

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("@")) {
                    if (previousKey != null) {
                        if (previousKey.equalsIgnoreCase("dependency")) {
                            debug("Property " + propName + " metainfo tag is a dependency on :" + contentPreviousKey.toString());
                            dependencies.add(new PropertyDependency(contentPreviousKey.toString()));
                        } else {
                            debug("Property " + propName + " metainfo tag is a " + previousKey + " value :"
                                    + contentPreviousKey.toString());
                            metadatas.put(previousKey, contentPreviousKey.toString());
                        }
                    }
                    previousKey = line.substring(line.indexOf('@') + 1, line.indexOf('=')).trim();
                    contentPreviousKey = new StringBuilder(line.substring(line.indexOf('=') + 1).trim());
                    debug("Property " + propName + " metainfo tag was started " + previousKey);
                } else if (line.startsWith("#")) {
                    debug("Line is a comment inside comment... ignoring...");
                    continue;
                } else {
                    debug("Line is a continuation of the previous metainfo tag! Appending value!");
                    contentPreviousKey.append(CRLF).append(line.trim());
                }
            }

            if (previousKey.equalsIgnoreCase("dependency")) {
                debug("Property " + propName + " metainfo tag is a dependency on :" + contentPreviousKey.toString());
                dependencies.add(new PropertyDependency(contentPreviousKey.toString()));
            } else {
                debug("Property " + propName + " metainfo tag is a " + previousKey + " value :" + contentPreviousKey.toString());
                metadatas.put(previousKey, contentPreviousKey.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        retVal.setDependencies(dependencies);
        retVal.setPropertyMessage(metadatas.remove("message"));
        retVal.setPropertyType(metadatas.remove("type"));
        retVal.setPropertyRequired(parseBoolean(metadatas.remove("required"), true));
        retVal.setPropertyPersist(parseBoolean(metadatas.remove("persist"), true));
        retVal.setPropertyPersistNull(parseBoolean(metadatas.remove("persistNull"), true));

        for (Entry<java.lang.String, java.lang.String> entry : metadatas.entrySet()) {
            retVal.setMetaData(entry.getKey(), entry.getValue());
        }

        debug("Returning input property " + retVal);

        debug("Cycling dependencies to find (and remove) dependencies on not specified properties or to find the parent properties reference based on name!");
        List<PropertyDependency> dependencyRemove = new ArrayList<PropertyDependency>();
        for (PropertyDependency propDep : dependencies) {
            InputProperty parentProperty = allProperties.get(propDep.getParentPropertyName());

            if (parentProperty == null) {
                debug("Dependency parent property " + propDep.getParentPropertyName() + " was not found for property "
                        + retVal.getPropertyName() + "! Removing dependency!");
                dependencyRemove.add(propDep);
            } else {
                debug("Found dependency parent property " + propDep.getParentPropertyName() + " for property "
                        + retVal.getPropertyName());
                propDep.setParentProperty(parentProperty);
            }
        }
        retVal.getDependencies().removeAll(dependencyRemove);

        return retVal;
    }

    private boolean parseBoolean(java.lang.String requiredStr, boolean b) {
        if (requiredStr == null) {
            return b;
        }

        if (requiredStr.equalsIgnoreCase("yes") || requiredStr.equalsIgnoreCase("y") || requiredStr.equalsIgnoreCase("1")
                || requiredStr.equalsIgnoreCase("true") || requiredStr.equalsIgnoreCase("on")) {
            return true;
        } else {
            return false;
        }

    }

    private Properties outputPropertiesValues = new Properties();

    public static final java.lang.String CRLF = System.getProperty("line.separator");

    private void readOutputPropertiesFile() {
        if (getPropFileOutput() != null && getPropFileOutput().exists() && getPropFileOutput().isFile()
                && getPropFileOutput().canRead()) {
            try {

                outputPropertiesValues.load(new FileInputStream(getPropFileOutput()));
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        }
    }

    private Properties lastPropertiesValues = new Properties();

    private boolean readLastPropertiesFile() throws UnsupportedEncodingException {
        if (getPropFileOutput() == null) {
            return false;
        }
        File f = buildLastPropertiesFile(getPropFileOutput());

        if (f != null && f.exists() && f.isFile() && f.canRead()) {

            String passCrypt = PropertyReaderManager.getInstance(getEncoding()).getPropertyCryptPassword();
            boolean automateBatch = passCrypt != null && passCrypt.length() != 0;

            // Ask the user for a password to open the encrypted file...
            if (automateBatch
                    || StdIn.getInstance(getEncoding()).readBooleanOption(
                            "Found a configuration file from a previous run of the configuration... Do you want to use it?", "y",
                            "n")) {
                if (!automateBatch) {
                    passCrypt =
                            StdIn.getInstance(getEncoding()).readString(
                                    "Please enter the password to open the configuration file found!", 1);
                }
                try {
                    final byte[] salt =
                            { (byte) 0xaa, (byte) 0xbb, (byte) 0xcc, (byte) 0xdd, (byte) 0x22, (byte) 0x44, (byte) 0xab,
                                    (byte) 0x12 };
                    final int iterations = 10;
                    final String cipherName = "PBEWithMD5AndDES";

                    PBEParameterSpec paramSpec = new PBEParameterSpec(salt, iterations);
                    KeySpec specKey = new PBEKeySpec(passCrypt.toCharArray());
                    Key secKey = SecretKeyFactory.getInstance(cipherName).generateSecret(specKey);

                    Cipher cipher = Cipher.getInstance(cipherName);
                    cipher.init(Cipher.DECRYPT_MODE, secKey, paramSpec);

                    CipherInputStream cis = new CipherInputStream(new FileInputStream(f), cipher);
                    BufferedReader br = new BufferedReader(new InputStreamReader(cis));
                    if (!("##" + passCrypt + "##").equals(br.readLine())) {
                        throw new InvalidKeyException("password does not match...");
                    }

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();

                    String line = null;
                    while ((line = br.readLine()) != null) {
                        bos.write(line.getBytes(getEncoding()));
                        bos.write(CRLF.getBytes(getEncoding()));
                    }
                    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());

                    lastPropertiesValues.load(bis);
                    bos.close();
                    bis.close();
                    cis.close();

                    if (automateBatch) {
                        return true;
                    }

                    return StdIn.getInstance(getEncoding()).readBooleanOption(
                            "Do you want to use the defaults in this file as defaults to all the properties?", "y", "n");

                } catch (InvalidKeyException e) {
                    System.out.println("Unable to open encrypted configuration file... password is invalid!");
                } catch (InvalidKeySpecException e) {
                    System.out.println("Unable to open encrypted configuration file... password algorithm is not available!");
                } catch (NoSuchAlgorithmException e) {
                    System.out.println("Unable to open encrypted configuration file... password algorithm is not available!");
                } catch (FileNotFoundException e) {
                    System.out.println("Unable to open encrypted configuration file... file not found!");
                } catch (IOException e) {
                    System.out.println("Unable to open encrypted configuration file... generic I/O error!");
                } catch (NoSuchPaddingException e) {
                    System.out
                            .println("Unable to open encrypted configuration file... password algorithm padding is not available!");
                } catch (InvalidAlgorithmParameterException e) {
                    System.out.println("Unable to open encrypted configuration file... invalid algorithm parameter !");
                }
            }
        }
        return false;
    }

    public static File buildLastPropertiesFile(File outputPropertiesFile) {
        return new File(outputPropertiesFile.getParentFile(), "." + outputPropertiesFile.getName() + ".crypt");
    }

    public String getDefaultValue(InputProperty property) {
        return getDefaultValue(property.getPropertyName(), property.getPropertyDefaultValue());
    }

    public String getDefaultValue(String propertyName, String defaultValue) {
        if (outputPropertiesValues != null && outputPropertiesValues.containsKey(propertyName)) {
            return outputPropertiesValues.getProperty(propertyName);
        } else if (lastPropertiesValues != null && lastPropertiesValues.containsKey(propertyName)) {
            return lastPropertiesValues.getProperty(propertyName);
        } else {
            return defaultValue;
        }
    }

    public void debug(String message) {
        if (debug) {
            System.out.println(getClass().getName() + ":" + message);
        }
    }

    public String getEncoding() {
        return encoding;
    }

}
