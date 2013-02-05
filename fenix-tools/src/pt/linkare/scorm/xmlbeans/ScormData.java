/**
 * 
 */
package pt.linkare.scorm.xmlbeans;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;

import pt.linkare.scorm.utils.ScormException;
import pt.linkare.scorm.utils.ScormMetaData;

/**
 * @author Oscar Ferreira - Linkare TI
 * 
 */
// Top Hierarical Object to represent the Scorm Package
public class ScormData {

    // File Object representing Zip of Scorm Package
    private File originalFile = null; // not null

    // File Object representing the ImsManifest.xml file
    private File metaDataFile = null; // not null

    // Collection of Objects that represent Asset Files with common MetaData
    private Collection<ScormAsset> assets = null; // not empty

    // All the Packages ItemMetada in a Collection
    private Collection<ScormMetaData> packageMetaInfo = null; // not null

    public ScormData(File originalPifFile) throws Exception {
        super();
        try {
            if (originalPifFile != null) {
                this.originalFile = originalPifFile;
            } else {
                throw new ScormException("PIF File object is null.");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public ScormData(File originalPifFile, File metaDataFile, Collection<ScormAsset> assets,
            Collection<ScormMetaData> packageMetaInfo) throws Exception {
        super();
        try {
            if (originalPifFile != null) {
                this.originalFile = originalPifFile;
            } else {
                throw new ScormException("PIF File object is null.");
            }
            if (metaDataFile != null) {
                this.metaDataFile = metaDataFile;
            } else {
                throw new ScormException("MetaData File is null.");
            }
            if (assets != null && assets.size() != 0) {
                this.assets = assets;
            } else {
                throw new ScormException("Size of Asset Collection equal to zero(0).");
            }
            if (packageMetaInfo != null) {
                this.packageMetaInfo = packageMetaInfo;
            } else {
                throw new ScormException("Collection of ItemMeta is null.");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public Collection<ScormAsset> getAssets() {
        return assets;
    }

    public void setAssets(Collection<ScormAsset> assets) {
        this.assets = assets;
    }

    public File getMetaDataFile() {
        return metaDataFile;
    }

    public void setMetaDataFile(File metaDataFile) {
        this.metaDataFile = metaDataFile;
    }

    public File getOriginalFile() {
        return originalFile;
    }

    public void setOriginalFile(File originalPifFile) {
        this.originalFile = originalPifFile;
    }

    public Collection<ScormMetaData> getPackageMetaInfo() {
        return packageMetaInfo;
    }

    public void setPackageMetaInfo(Collection<ScormMetaData> packageMetaInfo) {
        this.packageMetaInfo = packageMetaInfo;
    }

    public Collection<ScormMetaData> buildFullPackageMetaInfo() {
        ArrayList<ScormMetaData> retVal = new ArrayList<ScormMetaData>();
        if (getPackageMetaInfo() != null) {
            retVal.addAll(getPackageMetaInfo());
        }

        for (ScormAsset sas : getAssets()) {
            if (sas.getContentMetadataInfo() != null) {
                retVal.addAll(sas.getContentMetadataInfo());
            }
        }
        return retVal;
    }

    @Override
    public String toString() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        print(pw);

        pw.flush();
        String retVal = sw.getBuffer().toString();
        pw.close();

        return retVal;
    }

    private void print(PrintWriter out) {
        out.println("Original Pif File: " + getOriginalFile().getAbsolutePath());
        out.println("ImsManifest.xml file: " + getMetaDataFile());
        out.println("External Asset Files: ");
        this.printScormAssetsCollection(getAssets(), out);
        out.println("Package metadata: ");
        ScormMetaData.print(out, getPackageMetaInfo());
    }

    public void printScormAssetsCollection(Collection<ScormAsset> sas, PrintWriter out) {
        for (ScormAsset sa : sas) {
            out.println(sa);
        }
    }

}
