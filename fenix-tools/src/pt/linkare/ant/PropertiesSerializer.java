package pt.linkare.ant;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.List;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class PropertiesSerializer {

	private List<InputProperty> properties=null;
	private File outputPropertyFile=null;
	private boolean defaultCipherSourced=false;
	
	

	public PropertiesSerializer() {
		super();
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
		return new BufferedWriter(new FileWriter(getOutputPropertyFile()));
	}
	
	private BufferedWriter getCipherOut() throws IOException
	{
		if(isDefaultCipherSourced())
			return null;
		
		File outCipherFile=InstallerPropertiesReader.buildLastPropertiesFile(getOutputPropertyFile());
		//Ask the user for a password to open the encrypted file...
		if(StdIn.getInstance().readBooleanOption("Do you want to write all the configuration properties to an encrypted file at "+outCipherFile.getPath()+"?", "y","n"))
		{
			String passCrypt=StdIn.getInstance().readString("Please enter the password to protect the configuration file!",1);
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
				
				BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(cos));
				bw.write("#"+passCrypt);
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
	
	
	
	public Properties generatePropertiesFile() throws IOException
	{
		Properties retVal=new Properties();
		BufferedWriter out=getOut();
		BufferedWriter outCipher=getCipherOut();
		for(InputProperty prop:getProperties())
		{
			String key=prop.getPropertyName();
			String value=prop.getPropertyValue();
			if(key==null && value==null) continue;
				
			if(prop.isPropertyPersist() && value!=null)
			{//only if it is a persistent property
				if(prop.getPropertyMessage()!=null)
				{
					out.write("# "+prop.getPropertyMessage());
					out.newLine();
				}
				out.write(key+"="+value);
				out.newLine();
			}
			if(outCipher!=null)
			{//no matter if it is serializable or not
				outCipher.write(key+"="+(value==null?"":value));
				outCipher.newLine();
			}
			
			if(value!=null)
				retVal.put(key, value);
		}
		out.flush();
		out.close();
		if(outCipher!=null)
		{
			outCipher.flush();
			outCipher.close();
		}
		return retVal;
	}
	
	public Properties generatePropertiesFile(File out,List<InputProperty> props,boolean defaultCipherSourced) throws IOException
	{
		this.setProperties(props);
		this.setOutputPropertyFile(out);
		this.setDefaultCipherSourced(defaultCipherSourced);
		return this.generatePropertiesFile();
	}
	
	public static Properties outputPropertiesFile(File out,List<InputProperty> props,boolean defaultCipherSourced) throws IOException
	{
		PropertiesSerializer serialThis=new PropertiesSerializer();
		return serialThis.generatePropertiesFile(out, props,defaultCipherSourced);
	}

	/**
	 * @return Returns the properties.
	 */
	public List<InputProperty> getProperties() {
		return properties;
	}

	/**
	 * @param properties The properties to set.
	 */
	public void setProperties(List<InputProperty> properties) {
		this.properties = properties;
	}
	
}
