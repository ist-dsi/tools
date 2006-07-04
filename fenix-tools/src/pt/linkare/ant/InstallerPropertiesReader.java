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
import java.util.Properties;
import java.util.Map.Entry;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * This class is the main responsible for parsing the
 * properties spec file and asking for input to the user... 
 * It controls the main process  -  It is not dependent of ant
 * @author jpereira - Linkare TI
 */

public class InstallerPropertiesReader {
	//The input property file
	private File propFileInput = null;
	//The output property file
	private File propFileOutput = null;
	//were the properties read from the ciphered file?
	private boolean defaultInLastPropertiesFile=false;
	
	
	private static InstallerPropertiesReader instance=null;
	
	/**
	 * Helper constructer
	 * @param propFileInput The input file to read the properties spec
	 * @param propFileOutput The generated output properties file
	 */
	private InstallerPropertiesReader(File propFileInput, File propFileOutput) {
		super();
		this.setPropFileInput(propFileInput);
		this.setPropFileOutput(propFileOutput);
	}

	/**
	 * Default constructor - for completeness
	 *
	 */
	private InstallerPropertiesReader() {
		super();
	}
	
	public synchronized static InstallerPropertiesReader getInstance() {
		if(instance==null)
			instance=new InstallerPropertiesReader();
		
		return instance;
			
	}
	
	public synchronized static InstallerPropertiesReader getInstance(File inputFile,File outputFile) {
		if(instance==null)
			instance=new InstallerPropertiesReader(inputFile,outputFile);
		
		return instance;
			
	}
	/**
	 * JavaBeans property accessor
	 * @return Returns the File of input properties spec
	 */
	public File getPropFileInput() {
		return propFileInput;
	}

	/**
	 * JavaBeans property accessor
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
	 */
	public void setPropFileOutput(File propFileOutput) {
		this.propFileOutput = propFileOutput;
		readOutputPropertiesFile();
		defaultInLastPropertiesFile=readLastPropertiesFile();
	}
	
	public static Properties readProperties(File fInput,File fOutput) throws IOException, InvalidPropertySpecException, NoPropertyReaderException
	{
		
		InstallerPropertiesReader propReader=InstallerPropertiesReader.getInstance(fInput,fOutput);
		
		InputPropertyMap properties=propReader.parse();
		
		ArrayList<InputProperty> generatedProperties=new ArrayList<InputProperty>();
		
		for(InputProperty prop:properties)
		{
			Collection<InputProperty> retVal=prop.readNow(propReader.defaultInLastPropertiesFile);
			if(retVal!=null)
			{
				generatedProperties.addAll(retVal);	
			}
		}
		
		try
		{
			for(InputProperty prop:generatedProperties)
		
		{
			prop.readNow(propReader.defaultInLastPropertiesFile);
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		properties.putAll(generatedProperties);
		return PropertiesSerializer.outputPropertiesFile(propReader.getPropFileOutput(), properties,propReader.defaultInLastPropertiesFile);
	}
	

	public InputPropertyMap parse() throws IOException {
		InputPropertyMap propertiesToRead = new InputPropertyMap();
		
		BufferedReader br=new BufferedReader(new FileReader(getPropFileInput()));
		StringBuffer propMetaInfo=new StringBuffer();
		String propName=null;
		String propDefaultValue=null;
		String line=null;
		
		while((line=br.readLine())!=null)
		{
			if(line.trim().length()==0)
				continue;
			
			if(line.trim().startsWith("#"))
			{
				if(propMetaInfo.length()!=0)
					propMetaInfo.append(CRLF);
				propMetaInfo.append(line.substring(1));
			}
			else
			{//line is not empty and does not start with a comment... it is a property def
				int positionEquals=line.indexOf('=');
				if(positionEquals>-1)
				{
					propName=line.substring(0,positionEquals);
					propDefaultValue=line.substring(positionEquals+1);
				}
				else
					propName=line;
				
				propertiesToRead.put(parseInputPropertyMetaInfo(propertiesToRead,propName, propMetaInfo.toString(), propDefaultValue));
				propMetaInfo=new StringBuffer();
				propName=null;
				propDefaultValue=null;
			}
		}
		
		for(InputProperty prop:propertiesToRead)
		{
			List<PropertyDependency> dependencies=prop.getDependencies();
			List<PropertyDependency> dependencyRemove=new ArrayList<PropertyDependency>();
			for(PropertyDependency propDep:dependencies)
			{
				InputProperty parentProperty=propertiesToRead.get(propDep.getParentPropertyName());
				
				if(parentProperty==null)
					dependencyRemove.add(propDep);
				else
					propDep.setParentProperty(parentProperty);
			}
			dependencies.removeAll(dependencyRemove);
		}
		
		return propertiesToRead;
	}

	
	
	public InputProperty parseInputPropertyMetaInfo(InputPropertyMap map,java.lang.String propName, java.lang.String metadata, java.lang.String defaultPropValue) {
		InputProperty retVal=new InputProperty(map);
		
		retVal.setPropertyName(propName);
		
		String defaultValueForProperty=lastPropertiesValues.getProperty(propName);
		if(defaultValueForProperty==null)
			defaultValueForProperty=outputPropertiesValues.getProperty(propName);
		if(defaultValueForProperty==null)
			defaultValueForProperty=defaultPropValue;
		
		retVal.setPropertyDefaultValue(defaultValueForProperty);
		
		StringReader sr=new StringReader(metadata);
		BufferedReader br=new BufferedReader(sr);
		HashMap<java.lang.String,java.lang.String> metadatas=new HashMap<java.lang.String,java.lang.String>();
		
		ArrayList<PropertyDependency> dependencies=new ArrayList<PropertyDependency>();
		
		try {
			//read properties meta info in format
			//@message=.....
			// .....
			// .....
			// 
			//@type=String
			//@required=true
			//@persist=false
			//@metadataName=teste
			//@dependency=property.name=value - depends on property.name having value
			//@dependency=property2.name=value2 - also depends on property2.name having value2
			//@dependency=property3.name=* - also depends on property3.name being defined - no matter the value
			java.lang.String previousKey=null;
			StringBuffer contentPreviousKey=new StringBuffer();
			
			java.lang.String line=null;
			
			while((line=br.readLine())!=null)
			{
				line=line.trim();
				if(line.startsWith("@"))
				{
					if(previousKey!=null)
					{
						if(previousKey.equalsIgnoreCase("dependency"))
							dependencies.add(new PropertyDependency(contentPreviousKey.toString()));
						else
							metadatas.put(previousKey, contentPreviousKey.toString());
					}
					previousKey=line.substring(line.indexOf('@')+1,line.indexOf('=')).trim();
					contentPreviousKey=new StringBuffer(line.substring(line.indexOf('=')+1).trim());
				}
				else
				{
					contentPreviousKey.append(CRLF).append(line.trim());
				}
			}
			
			if(previousKey!=null)
			{
				if(previousKey.equalsIgnoreCase("dependency"))
					dependencies.add(new PropertyDependency(contentPreviousKey.toString()));
				else
					metadatas.put(previousKey, contentPreviousKey.toString());
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		retVal.setDependencies(dependencies);
		retVal.setPropertyMessage(metadatas.remove("message"));
		retVal.setPropertyType(metadatas.remove("type"));
		retVal.setPropertyRequired(parseBoolean(metadatas.remove("required"),true));
		retVal.setPropertyPersist(parseBoolean(metadatas.remove("persist"),true));
		
		for(Entry<java.lang.String,java.lang.String> entry:metadatas.entrySet())
			retVal.setMetaData(entry.getKey(), entry.getValue());
		
		
		return retVal;
	}

	private boolean parseBoolean(java.lang.String requiredStr, boolean b) {
		if(requiredStr==null)
			return b;
		
		if(requiredStr.equalsIgnoreCase("yes") || requiredStr.equalsIgnoreCase("y") || requiredStr.equalsIgnoreCase("1") || requiredStr.equalsIgnoreCase("true") || requiredStr.equalsIgnoreCase("on"))
			return true;
		else
			return false;
		
	}

	private Properties outputPropertiesValues = new Properties();
	public static final java.lang.String CRLF=System.getProperty("line.separator");

	
	
	private void readOutputPropertiesFile() {
		if (getPropFileOutput() != null 
				&& getPropFileOutput().exists() 
				&& getPropFileOutput().isFile()
				&& getPropFileOutput().canRead()) {
			try {

				outputPropertiesValues.load(new FileInputStream(getPropFileOutput()));
			}
			catch (FileNotFoundException e) {
			}
			catch (IOException e) {
			}
		}
	}
	
	private Properties lastPropertiesValues=new Properties();
	private boolean readLastPropertiesFile() {
		if(getPropFileOutput() == null ) return false;
		File f=buildLastPropertiesFile(getPropFileOutput());
		
		if (f != null 
				&& f.exists() 
				&& f.isFile()
				&& f.canRead()) {
				
				//Ask the user for a password to open the encrypted file...
				if(StdIn.getInstance().readBooleanOption("Found a configuration file from a previous run of the configuration... Do you want to use it?", "y","n"))
				{
					String passCrypt=StdIn.getInstance().readString("Please enter the password to open the configuration file found!",1);
					try {
						final byte[] salt = { 
								(byte)0xaa, (byte)0xbb, (byte)0xcc, (byte)0xdd,
								(byte)0x22, (byte)0x44, (byte)0xab, (byte)0x12 };
						final int iterations = 10;
						final String cipherName = "PBEWithMD5AndDES";
						
						PBEParameterSpec paramSpec=new PBEParameterSpec(salt,iterations);
						KeySpec specKey= new PBEKeySpec(passCrypt.toCharArray());
						Key secKey=SecretKeyFactory.getInstance(cipherName).generateSecret(specKey);

						Cipher cipher=Cipher.getInstance(cipherName);
						cipher.init(Cipher.DECRYPT_MODE,secKey,paramSpec);
						
						CipherInputStream cis=new CipherInputStream(new FileInputStream(f),cipher);
						BufferedReader br=new BufferedReader(new InputStreamReader(cis));
						if(!("#"+passCrypt).equals(br.readLine()))
							throw new InvalidKeyException("password does not match...");
						
						ByteArrayOutputStream bos=new ByteArrayOutputStream();
						
						String line=null;
						while((line=br.readLine())!=null)
						{
							bos.write(line.getBytes());
							bos.write(CRLF.getBytes());
						}
						ByteArrayInputStream bis=new ByteArrayInputStream(bos.toByteArray());
						
						lastPropertiesValues.load(bis);
						bos.close();
						bis.close();
						cis.close();
						
						return StdIn.getInstance().readBooleanOption("Do you want to use the defaults in this file as defaults to all the properties?", "y","n");
						
					}
					catch (InvalidKeyException e) {
						System.out.println("Unable to open encrypted configuration file... password is invalid!");
					}
					catch (InvalidKeySpecException e) {
						System.out.println("Unable to open encrypted configuration file... password algorithm is not available!");
					}
					catch (NoSuchAlgorithmException e) {
						System.out.println("Unable to open encrypted configuration file... password algorithm is not available!");
					}
					catch (FileNotFoundException e) {
						System.out.println("Unable to open encrypted configuration file... file not found!");
					}
					catch (IOException e) {
						System.out.println("Unable to open encrypted configuration file... generic I/O error!");
					}
					catch (NoSuchPaddingException e) {
						System.out.println("Unable to open encrypted configuration file... password algorithm padding is not available!");
					}			
					catch (InvalidAlgorithmParameterException e) {
						System.out.println("Unable to open encrypted configuration file... invalid algorithm parameter !");
					}
			}
		}
		return false;
	}

	
	public static File buildLastPropertiesFile(File outputPropertiesFile)
	{
		return new File(outputPropertiesFile.getParentFile(),"."+outputPropertiesFile.getName()+".crypt");
	}

	public String getDefaultValue(InputProperty property) {
		String propertyName=property.getPropertyName();
		if(lastPropertiesValues!=null && lastPropertiesValues.containsKey(propertyName))
			return lastPropertiesValues.getProperty(propertyName);
		else if(outputPropertiesValues!=null && outputPropertiesValues.containsKey(propertyName))
			return outputPropertiesValues.getProperty(propertyName);
		else
			return property.getPropertyDefaultValue();
	}
	

}
