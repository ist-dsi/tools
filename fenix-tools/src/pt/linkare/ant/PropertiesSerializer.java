package pt.linkare.ant;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import pt.linkare.ant.propreaders.PropertyReaderManager;

public class PropertiesSerializer {

	private InputPropertyMap properties=null;
	private File outputPropertyFile=null;
	private boolean defaultCipherSourced=false;
	private String encoding=null;
	

	public PropertiesSerializer(String encoding) {
		super();
		setEncoding(encoding);
	}

	
	public String getEncoding() {
		return encoding;
	}


	private void setEncoding(String encoding) {
		this.encoding=encoding;
	}

	/**
	 * @return Returns the defaultCipherSourced.
	 */
	public boolean isDefaultCipherSourced() {
		return defaultCipherSourced;
	}

	/**
	 * @param defaultCipherSourced The defaultCipherSourced to set.
	 */
	public void setDefaultCipherSourced(boolean defaultCipherSourced) {
		this.defaultCipherSourced = defaultCipherSourced;
	}
	
	
	/**
	 * @return Returns the outputPropertyFilePath.
	 */
	public File getOutputPropertyFile() {
		return outputPropertyFile;
	}

	/**
	 * @param outputPropertyFile The outputPropertyFile to set.
	 */
	public void setOutputPropertyFile(File outputPropertyFile) {
		this.outputPropertyFile = outputPropertyFile;
	}

	private BufferedWriter getOut() throws IOException
	{
		return new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(getOutputPropertyFile()),getEncoding())
				);
	}
	
	private BufferedWriter getCipherOut() throws IOException
	{
		if(isDefaultCipherSourced())
			return null;
		
		File outCipherFile=InstallerPropertiesReader.buildLastPropertiesFile(getOutputPropertyFile());
		
		String passCrypt=PropertyReaderManager.getInstance(getEncoding()).getPropertyCryptPassword();
		boolean automateBatch=passCrypt!=null && passCrypt.length()!=0;
		
		//Ask the user for a password to open the encrypted file...
		if(automateBatch || StdIn.getInstance(getEncoding()).readBooleanOption("Do you want to write all the configuration properties to an encrypted file at "+outCipherFile.getPath()+"?", "y","n"))
		{
			if(!automateBatch)
				passCrypt=StdIn.getInstance(getEncoding()).readString("Please enter the password to protect the configuration file!",1);
			
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
				cipher.init(Cipher.ENCRYPT_MODE,secKey,paramSpec);

				outCipherFile.createNewFile();
				CipherOutputStream cos=new CipherOutputStream(new FileOutputStream(outCipherFile),cipher);
				
				BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(cos,getEncoding()));
				bw.write("##"+passCrypt+"##");
				bw.newLine();
				return bw;
			}
			catch (InvalidKeyException e) {
				e.printStackTrace();
				System.out.println("Unable to open encrypted configuration file... password is invalid!");
			}
			catch (InvalidKeySpecException e) {
				e.printStackTrace();
				System.out.println("Unable to open encrypted configuration file... password algorithm is not available!");
			}
			catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				System.out.println("Unable to open encrypted configuration file... password algorithm is not available!");
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("Unable to open encrypted configuration file... file not found!");
			}
			catch (NoSuchPaddingException e) {
				e.printStackTrace();
				System.out.println("Unable to open encrypted configuration file... password algorithm padding is not available!");
			}
			catch (InvalidAlgorithmParameterException e) {
				e.printStackTrace();
				System.out.println("Unable to open encrypted configuration file... invalid algorithm parameter !");
			}
		}
		return null;
	}
	
	
	private static final String CRLF=System.getProperty("line.separator");
	
	public Properties generatePropertiesFile() throws IOException
	{
		Properties retVal=new Properties();
		BufferedWriter out=getOut();
		BufferedWriter outCipher=getCipherOut();
		for(InputProperty prop:getProperties())
		{
			String key=prop.getPropertyName();
			String value=prop.getPropertyValue();
			
			//if the value is an empty string then it is considered null
			if(value!=null && value.trim().equals(""))
				value=null;
			
			if(key==null && value==null) continue;
				
			if(prop.isPropertyPersist() && (value!=null || prop.isPropertyPersistNull()))
			{//only if it is a persistent property
				String message=prop.getPropertyMessage().replaceAll(CRLF,CRLF+"#  ");
				out.write("#   "+message);
				out.newLine();
				out.write(key+"="+(value==null?"":value));
				out.newLine();
				out.newLine();
			}
			
			if(outCipher!=null)
			{//no matter if it is serializable or not
				outCipher.write(key+"="+(value==null?"":value));
				outCipher.newLine();
			}
			
			if(key!=null && (value!=null || prop.isPropertyPersistNull()))
				retVal.put(key, value==null?"":value);
		}
		out.flush();
		out.close();
		if(outCipher!=null)
		{
			outCipher.flush();
			outCipher.close();
		}
		retVal.put(getOutputPropertyFile().getName()+".configured", "true");
		return retVal;
	}
	
	public Properties generatePropertiesFile(File out,InputPropertyMap props,boolean defaultCipherSourced) throws IOException
	{
		this.setProperties(props);
		this.setOutputPropertyFile(out);
		this.setDefaultCipherSourced(defaultCipherSourced);
		return this.generatePropertiesFile();
	}
	
	public static Properties outputPropertiesFile(File out,InputPropertyMap props,boolean defaultCipherSourced,String encoding) throws IOException
	{
		PropertiesSerializer serialThis=new PropertiesSerializer(encoding);
		return serialThis.generatePropertiesFile(out, props,defaultCipherSourced);
	}

	/**
	 * @return Returns the properties.
	 */
	public InputPropertyMap getProperties() {
		return properties;
	}

	/**
	 * @param properties The properties to set.
	 */
	public void setProperties(InputPropertyMap properties) {
		this.properties = properties;
	}
	
}
