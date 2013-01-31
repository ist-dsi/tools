/**
 * 
 */
package pt.linkare.scorm.xmlbeans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.imsproject.xsd.imscpRootv1P1P2.ManifestDocument;

import pt.linkare.scorm.utils.ScormException;
import pt.linkare.scorm.utils.ScormMetaData;
import pt.linkare.scorm.utils.ScormMetaDataHash;
import pt.utl.ist.fenix.tools.file.utils.FileUtils;

/**
 * @author Oscar Ferreira - Linkare TI
 * 
 */
public class ScormHandlerImpl implements ScormHandler {

	//private static final String SCORM_IMPL_PACKAGE_IMSMANIFEST_FILENAME="imsmanifest.xml";
	/**
	 * Auto generated javadoc
	 * 
	 * @see pt.linkare.scorm.xmlBeans.ScormHandler#parseScormPif(java.io.File)
	 */

	@Override
	public ScormData parseScormPifFile(File pifFile) throws ScormException {
		try {
			File scormPackExtracted = FileUtils.unzipFile(pifFile);
			File locationOfCopyOfPif = new File(scormPackExtracted, pifFile.getName());
			ScormData scormDataToReturn = new ScormData(locationOfCopyOfPif);
			ImsManifestReader_1_2 imsR = new ImsManifestReader_1_2(scormPackExtracted);
			scormDataToReturn.setMetaDataFile(imsR.getImsmanifest(scormPackExtracted.getAbsolutePath()));
			//Collection<String> colExternalFiles=imsR.getAllFileNamesRefManifest(scormDataToReturn.getMetaDataFile());
			Collection<ScormMetaData> colItemMetadata =
					imsR.readMetaData(ManifestDocument.Factory.parse(scormDataToReturn.getMetaDataFile()).getManifest()
							.getMetadata());
			scormDataToReturn.setPackageMetaInfo(colItemMetadata);
			scormDataToReturn.setAssets(imsR.getColScormAssets(scormPackExtracted));
			FileUtils.deleteDirectory(scormPackExtracted);
			return scormDataToReturn;
		} catch (Exception e) {
			throw new ScormException(ScormException.IMS_MANIFEST_EXCEPTION, e);
		}
	}

	/**
	 * Auto generated javadoc
	 * 
	 * @throws ScormException
	 * 
	 * @see pt.linkare.scorm.xmlbeans.ScormHandler#createScormPifFile(java.util.HashMap, java.util.Collection)
	 */
	@Override
	public ScormData createScormPifFile(String manifestIdentifier, ScormMetaDataHash scormMetaDataMap,
			Collection<File> originalContentFiles) throws ScormException {
		File tempDir;
		File pifFile;
		try {
			tempDir = FileUtils.createTemporaryDir("ScormPackaging", ".tmp");
		} catch (IOException e) {
			throw new ScormException("Cannot create scorm package temporary dir", e);
		}

		File baseDirForOriginalContentFiles = FileUtils.findBaseDir(originalContentFiles);
		if (baseDirForOriginalContentFiles == null || !baseDirForOriginalContentFiles.exists()) {
			throw new ScormException(
					"Unable to create package from the set of original Files because these files don't share any common root");
		}

		Collection<File> copiedFiles = null;
		try {
			copiedFiles =
					FileUtils.copyFilesToAnotherDirWithRelativePaths(baseDirForOriginalContentFiles, tempDir,
							originalContentFiles);
		} catch (FileNotFoundException e) {
			throw new ScormException("Cannot copy from original file to scorm package temporary dir", e);
		} catch (IOException e) {
			throw new ScormException("Cannot copy from original file to scorm package temporary dir", e);
		}

		FileOutputStream schemaFileOS;
		InputStream schemaFileIS;
		try {
			schemaFileOS = new FileOutputStream(new File(tempDir, "ims_xml.xsd"));
			schemaFileIS = getClass().getClassLoader().getResourceAsStream("schema/ims_xml.xsd");
			FileUtils.copyInputStreamToOutputStream(schemaFileIS, schemaFileOS);
			schemaFileOS.close();

			schemaFileOS = new FileOutputStream(new File(tempDir, "imsmd_rootv1p2p1.xsd"));
			schemaFileIS = getClass().getClassLoader().getResourceAsStream("schema/imsmd_rootv1p2p1.xsd");
			FileUtils.copyInputStreamToOutputStream(schemaFileIS, schemaFileOS);
			schemaFileOS.close();

			schemaFileOS = new FileOutputStream(new File(tempDir, "imscp_rootv1p1p2.xsd"));
			schemaFileIS = getClass().getClassLoader().getResourceAsStream("schema/imscp_rootv1p1p2.xsd");
			FileUtils.copyInputStreamToOutputStream(schemaFileIS, schemaFileOS);
			schemaFileOS.close();

			schemaFileOS = new FileOutputStream(new File(tempDir, "adlcp_rootv1p2.xsd"));
			schemaFileIS = getClass().getClassLoader().getResourceAsStream("schema/adlcp_rootv1p2.xsd");
			FileUtils.copyInputStreamToOutputStream(schemaFileIS, schemaFileOS);
			schemaFileOS.close();
		} catch (FileNotFoundException e) {
			throw new ScormException("Unable to copy schema files to output dir", e);
		} catch (IOException e) {
			throw new ScormException("Unable to copy schema files to output dir", e);
		}

		ImsManifestWriter_1_2 manifestWriter = null;
		try {
			manifestWriter = new ImsManifestWriter_1_2(manifestIdentifier, scormMetaDataMap);
		} catch (Exception e) {
			throw new ScormException("Cannot create the Scorm manifest writer object ", e);
		}
		File metaInfoFile = manifestWriter.createManifest(tempDir.getAbsolutePath());

		for (File resFile : copiedFiles) {
			manifestWriter.addResource2ImsManifest(metaInfoFile, resFile);
		}

		ArrayList<ScormAsset> assets = new ArrayList<ScormAsset>(copiedFiles.size());
		ScormAsset currentAsset = new ScormAsset();
		currentAsset.setContentFiles(copiedFiles);
		assets.add(currentAsset);

		try {
			pifFile = FileUtils.zipDir(tempDir, "scorm_packaged_by_fenix", ".zip");
		} catch (FileNotFoundException e) {
			throw new ScormException("Cannot create the pif zipped file ", e);
		} catch (IOException e) {
			throw new ScormException("Cannot create the pif zipped file ", e);
		}

		ScormData retVal = null;
		try {
			retVal = new ScormData(pifFile, metaInfoFile, assets, scormMetaDataMap.listScormMetaData());
		} catch (Exception e) {
			throw new ScormException("Cannot build ScormData object", e);
		}

		return retVal;
	}

	public static class ScormHandlerTest {

		public static void main(String[] args) {
			ScormMetaDataHash scormMetaHasher = new ScormMetaDataHash();
			scormMetaHasher.put("title", null, "pt", "Conte�do scorm de teste ldap");
			scormMetaHasher.put("title", null, "en", new String[] { "Ldap content test", "tiiitllleee 2", "The title 3" });

			scormMetaHasher.put("subject", null, "pt", new String[] { "protocolo", "ldap", "x509" });
			scormMetaHasher.put("description", null, "en", new String[] {
					"Exemplo de um pacote scorm com conte�do acerca de ldap em formato pdf",
					"Esta � uma descri��o alternativa para o conte�do sobre ldap" });
			scormMetaHasher.put("date", "created", null, new String[] { "2006-07-27-", "Descri��o 1" });
			scormMetaHasher.put("date", "copyright", null, new String[] { "2006-07-28", "Descri��o 2" });
			scormMetaHasher.put("date", "other", "en",
					new String[] { "2006-07-29", "Descri��o 3", "2006-07-30", "Descri��o 3-2" });
			scormMetaHasher.put("contributor", "author", null, "jose criador");
			scormMetaHasher.put("contributor", "editor", null, "joao autor");
			scormMetaHasher.put("contributor", "other", null, "joao produtor");
			scormMetaHasher.put("contributor", "other", null, "joao produtor2");
			scormMetaHasher.put("identifier", "citation", "pt-BR", new String[] { "Oscar J. Ferreira, Rua dos Po�os",
					"joaquin man�l, em casa" });
			scormMetaHasher.put("identifier", "lalala", "pt-BR", "�scar J. Ferreira, Rua dos barcos");
			scormMetaHasher.put("version", null, "pt", new String[] { "v1.0", "v1.2" });
			scormMetaHasher.put("metadatascheme", null, null, new String[] { "metaschema1", "metaschema2", "metaschema3" });
			scormMetaHasher.put("location", "URI", null, new String[] { "http://www.linux.com", "http://www.apache.com",
					"http://www.microsoft.crap" });
			scormMetaHasher.put("rights", "cost", "pt", new String[] { "credit", "transfer" });

			Collection<File> originalContentFiles = Collections.singleton(new File("test/ldap.pdf"));
			ScormHandlerImpl scormHandler = new ScormHandlerImpl();

			try {
				ScormData scData = scormHandler.createScormPifFile("LINKARE_TI_ID", scormMetaHasher, originalContentFiles);
				System.out.println("Generated SCORM PIF file at " + scData.getOriginalFile().getAbsolutePath());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
