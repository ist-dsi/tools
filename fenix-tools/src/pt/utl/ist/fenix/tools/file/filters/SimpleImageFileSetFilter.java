/**
 * 
 */
package pt.utl.ist.fenix.tools.file.filters;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.imageio.ImageIO;

import pt.utl.ist.fenix.tools.file.FileSet;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;

/**
 * An example class to extract meta info from Gif FileSets
 * @author José Pedro Pereira - Linkare TI
 */
public class SimpleImageFileSetFilter implements FileSetFilter {

	/**
	 * 
	 */
	public SimpleImageFileSetFilter() {
		super();
	}

	/* (non-Javadoc)
	 * @see pt.utl.ist.fenix.tools.file.filters.FileSetFilter#handleFileSet(pt.utl.ist.fenix.tools.file.FileSet)
	 */
	public void handleFileSet(FileSet fs) throws FileSetFilterException {
		Collection<File> supposedGifFiles=fs.getContentFiles();
		if(supposedGifFiles!=null && supposedGifFiles.size()!=0)
		{
			File supposedGifFile=supposedGifFiles.toArray(new File[0])[0];
			if(supposedGifFile.exists() && supposedGifFile.canRead() && 
                    (          supposedGifFile.getName().endsWith(".gif") 
                            || supposedGifFile.getName().endsWith(".jpg") 
                            || supposedGifFile.getName().endsWith(".png"))
              )
			{
				BufferedImage bfImage;
				try {
					bfImage = ImageIO.read(supposedGifFile);
				}
				catch (IOException e) {
					throw new IOFileSetFilterException(e);
				}
				fs.addMetaInfo(FileSetMetaData.createWidthMeta(bfImage.getWidth()));
				fs.addMetaInfo(FileSetMetaData.createHeightMeta(bfImage.getHeight()));
				//TODO - Some more meta info may be extracted from this files,
				//as its pixel size, number of colors, etc...
			}
			
		}
	}

}
