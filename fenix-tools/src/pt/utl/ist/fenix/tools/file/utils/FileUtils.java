package pt.utl.ist.fenix.tools.file.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileUtils {

    public static final int DEFAULT_COPY_BUFFER_SIZE = 2048;

    public static File createTemporaryDir(String prefix, String suffix)
            throws IOException {
        java.io.File f = java.io.File.createTempFile(prefix, suffix);
        if (f.exists() && f.canWrite() || !f.exists()) {
            String absolutePath = f.getAbsolutePath();
            if (f.exists())
                f.delete();

            f = new File(absolutePath);
            f.mkdirs();
            f.deleteOnExit();
            return f;
        } else
            throw new IOException("Unable to create temporary dir at path "
                    + f.getAbsolutePath());
    }

    public static void copyInputStreamToOutputStream(InputStream src,
            OutputStream dest) throws IOException {
        if (!(dest instanceof BufferedOutputStream))
            dest = new BufferedOutputStream(dest);

        if (!(src instanceof BufferedInputStream))
            src = new BufferedInputStream(src);

        int countBytesRead = -1;
        byte[] bufferCopy = new byte[DEFAULT_COPY_BUFFER_SIZE];
        while ((countBytesRead = src.read(bufferCopy)) != -1)
            dest.write(bufferCopy, 0, countBytesRead);

        dest.flush();
    }

   public static void adaptativeCopyInputStreamToOutputStream(InputStream src,
            OutputStream dest,int bytesMinSize,int bytesMaxSize,int bytesBlockIncrement) throws IOException {
        
    	
    	if(bytesMaxSize<=0 || bytesMinSize<=0)
    		throw new IOException("Cannot use a buffer size lower than zero!");
    	if(bytesMaxSize<bytesMinSize)
    	{   //just exchange the min and max as the user probably made a
    		//mistake specifying it
    		int temp=bytesMaxSize;
    		bytesMaxSize=bytesMinSize;
    		bytesMinSize=temp;
    	}
    	if(bytesBlockIncrement>=(bytesMaxSize-bytesMinSize))
    	{
    		//the block increment is bigger than the 
    		//the difference between max-min, so choose an appropriate
    		//block size, as this one does not enable any optimization
    		//use a block increment size of 10% of the total difference
    		bytesBlockIncrement=(int)Math.ceil((((double)(bytesMaxSize-bytesMinSize))/10.));
    	}
    	
    	double timePerByte=Double.MIN_VALUE;
    	double oldTimePerByte=Double.MIN_VALUE;
    	int directionIncrement=1;
    	int currentBufferSize=bytesMaxSize;

    	int countBytesRead = -1;
        byte[] bufferCopy = new byte[bytesMaxSize];
        
        long timeStartRead=System.currentTimeMillis();
        while ((countBytesRead = src.read(bufferCopy,0,currentBufferSize)) != -1)
        {
            dest.write(bufferCopy, 0, countBytesRead);
            timePerByte=((double)(System.currentTimeMillis()-timeStartRead))/(double)currentBufferSize;
            if(timePerByte<=oldTimePerByte)
            {//we are improving... try again the same change
            	//even if we are getting the same speed
            	//we may try in the same direction... 
            	currentBufferSize=currentBufferSize+directionIncrement*bytesBlockIncrement;
            }
            else
            {//we are not improving... change the incrementDirection
            	directionIncrement=-1*directionIncrement;
            	currentBufferSize=currentBufferSize+directionIncrement*bytesBlockIncrement;
            }
            //never go beyound the max or min values
            if(currentBufferSize<bytesMinSize)
            	currentBufferSize=bytesMinSize;
            else if(currentBufferSize>bytesMaxSize)
            	currentBufferSize=bytesMaxSize;
            
            oldTimePerByte=timePerByte;
            timeStartRead=System.currentTimeMillis();
        }
        
        dest.flush();
    }

    public static Collection<File> listOnlyFiles(File dir) {
        if (dir == null || !dir.isDirectory() || !dir.canRead())
            return null;
        File[] innerFiles = dir.listFiles(new OnlyFilesFileFilter());
        if (innerFiles != null)
            return Arrays.asList(innerFiles);
        else
            return null;

    }

    public static class OnlyFilesFileFilter implements FileFilter {
        public boolean accept(File file) {
            return file != null && file.isFile();
        }
    }

    /*
     * public static File createTemporaryFile(String string, String string2,
     * InputStream fileInputStream) {
     * 
     * return null; }
     */

    public static Collection<File> listFilesByExtension(File dir,
            String extension) {
        if (dir == null || !dir.isDirectory() || !dir.canRead())
            return null;
        File[] innerFiles = dir.listFiles(new ExtensionFileFilter(extension));
        if (innerFiles == null)
            return null;
        else
            return Arrays.asList(innerFiles);
    }

    public static class ExtensionFileFilter implements FileFilter {
        private String extension;

        public ExtensionFileFilter(String extension) {
            this.extension = extension;
        }

        public boolean accept(File file) {
            if (file != null && file.isFile())
                if (extension != null)
                    return file.getName().endsWith(extension);
                else
                    return true;

            return false;
        }
    }

    public static Collection<File> listFilesByName(File dir, String name,
            boolean ignoreCase) {
        if (dir == null || !dir.isDirectory() || !dir.canRead())
            return null;
        File[] innerFiles = dir.listFiles(new ExactNameFileFilter(name,
                ignoreCase));
        if (innerFiles == null)
            return null;
        else
            return Arrays.asList(innerFiles);
    }

    public static Collection<File> listFilesByName(File dir, String name) {
        return listFilesByName(dir, name, false);
    }

    public static class ExactNameFileFilter implements FileFilter {
        private String filename;

        private boolean ignoreCase = false;

        public ExactNameFileFilter(String filename, boolean ignoreCase) {
            this.filename = filename;
            this.ignoreCase = ignoreCase;
        }

        public boolean accept(File file) {
            if (file != null && file.isFile()) {
                if (filename != null) {
                    return ignoreCase ? file.getName().equalsIgnoreCase(
                            filename) : file.getName().equals(filename);
                } else
                    return true;
            }
            return false;
        }
    }

    public static Collection<File> listSubDirs(File dir) {
        if (dir == null || !dir.isDirectory() || !dir.canRead())
            return null;
        File[] innerDirs = dir.listFiles(new DirectoryFileFilter());
        if (innerDirs == null)
            return null;
        else
            return Arrays.asList(innerDirs);
    }

    public static class DirectoryFileFilter implements FileFilter {
        public boolean accept(File file) {
            return file != null && file.isDirectory();
        }
    }

    public static Collection<File> recursiveListOnlyFiles(File dir) {
        ArrayList<File> retVal = new ArrayList<File>();
        Collection<File> subDirs = listSubDirs(dir);
        Collection<File> innerFiles = listOnlyFiles(dir);

        if (innerFiles != null)
            retVal.addAll(innerFiles);

        if (subDirs != null)
            for (File subDir : subDirs)
                retVal.addAll(recursiveListOnlyFiles(subDir));

        return retVal;
    }

    public static File unzipFile(File file) throws IOException {
        File tempDir = FileUtils.createTemporaryDir("fenix_unzip", "temp");
        
        file.renameTo(new File(tempDir,file.getName()));
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(
                file));

        ZipEntry zipEntry = zipInputStream.getNextEntry();
        File zipContentFile = null;
        File zipContentFileParentDir = null;
        while (zipEntry != null) {
            zipEntry.getName();
            zipContentFile = new File(tempDir, zipEntry.getName());
            zipContentFileParentDir = zipContentFile.getParentFile();
            zipContentFileParentDir.mkdirs();

            if (!zipEntry.isDirectory())
                zipContentFile.createNewFile();
            else
                zipContentFile.mkdirs();

            zipContentFile.deleteOnExit();

            if (!zipEntry.isDirectory() && zipContentFile.exists()
                    && zipContentFile.canWrite()) {
                OutputStream zipOs = new FileOutputStream(zipContentFile);
                FileUtils.copyInputStreamToOutputStream(zipInputStream, zipOs);
                zipOs.close();
            }

            zipInputStream.closeEntry();
            zipEntry = zipInputStream.getNextEntry();
        }

        return tempDir;
    }

    public static File zipDir(File dirToZip, String prefix,String suffix)
            throws IOException {
        // create a new File object based on the directory we have to zip
        // get a listing of the directory content
        Collection<File> dirList = recursiveListOnlyFiles(dirToZip);
        File zipFile=File.createTempFile(prefix, suffix,dirToZip);
        ZipOutputStream zos=new ZipOutputStream(new FileOutputStream(zipFile));
        // loop through dirList, and zip the files
        for (File f : dirList) {
            if (f.isDirectory()) {
                // Just create the entry
                zos.putNextEntry(new ZipEntry(FileUtils.makeRelativePath(
                        dirToZip.getAbsolutePath(), f.getAbsolutePath())));
                continue;
            }
            // if we reached here, the File object f was not a directory
            // create a FileInputStream on top of f
            FileInputStream fis = new FileInputStream(f);
            // create a new zip entry //XXX era getPath() antes
            ZipEntry anEntry = new ZipEntry(FileUtils.makeRelativePath(dirToZip
                    .getAbsolutePath(), f.getAbsolutePath()));
            // place the zip entry in the ZipOutputStream object
            zos.putNextEntry(anEntry);
            copyInputStreamToOutputStream(fis, zos);
            // close the input Stream
            fis.close();
        }
        zos.finish();
        zos.flush();
        zos.close();
        return zipFile;
    }

    // Creates a File(fileName) with an absolutePath
    // receiving it's data from inputstream in a buffered manner(bufSize)
    public static File saveToFile(String absolutePath, InputStream inputStream,
            String fileName) throws IOException {
        File destinationFile = null;
        if (absolutePath == null)
            destinationFile = new File(createTemporaryDir("scorm_", ".extract")
                    .getAbsolutePath(), fileName);
        else
            destinationFile = new File(absolutePath, fileName);

        FileOutputStream fos = new FileOutputStream(destinationFile);
        copyInputStreamToOutputStream(inputStream, fos);
        return destinationFile;
    }

    // Creates a File(fileName) receiving it's data from inputstream in a
    // buffered manner(bufSize)
    public static File saveToFile(InputStream inputStream, String fileName)
            throws IOException {
        return saveToFile(null, inputStream, fileName);
    }

    public static byte[] readByteArray(File contentFile) throws IOException,
            FileNotFoundException {
        FileInputStream fis;
        byte[] bufferRetVal = new byte[0];
        byte[] bufferRead = new byte[1024];
        byte[] temp = null;
        int countBytesRead = 0;

        fis = new FileInputStream(contentFile);
        while ((countBytesRead = fis.read(bufferRead)) != -1) {
            temp = new byte[bufferRetVal.length + countBytesRead];
            System.arraycopy(bufferRetVal, 0, temp, 0, bufferRetVal.length);
            System.arraycopy(bufferRead, 0, temp, bufferRetVal.length,
                    countBytesRead);
            bufferRetVal = temp;
            temp = null;
        }

        return bufferRetVal;
    }

    public static String makeRelativePath(String absoluteParentPath,String originalAbsoluteFilePath,String uniqueId) {
    	if(originalAbsoluteFilePath!=null && absoluteParentPath!=null && originalAbsoluteFilePath.length()>absoluteParentPath.length()) {
    		return originalAbsoluteFilePath.substring(absoluteParentPath.length()+1);
    	}
    	else {
    		return uniqueId;
    	}	
    }
    
    public static String makeRelativePath(String absoluteParentPath,
            String originalAbsoluteFilePath) {
    	return makeRelativePath(absoluteParentPath, originalAbsoluteFilePath,"");
    }

    /**
     * @param originalContentFiles
     * @return
     */
    public static File findBaseDir(Collection<File> originalFiles) {
        if(originalFiles==null || originalFiles.size()<=0) return null;
        
        File lowestBasePathFound=originalFiles.iterator().next().getAbsoluteFile().getParentFile();

        for(File f:originalFiles)
        {
            lowestBasePathFound=findCommonAncestor(lowestBasePathFound,f);
        }
        return lowestBasePathFound;
    }

    /**
     * @param lowestBasePathFound
     * @param f
     * @return
     */
    private static File findCommonAncestor(File file1, File file2) {
        
    	ArrayList<File> listComponents1=listFilePathComponents(file1);
    	
    	ArrayList<File> listComponents2=listFilePathComponents(file2);
        
    	File retVal=null;
    	
    	for(int i=0;i<listComponents1.size() && i<listComponents2.size();i++)
        {
            if(listComponents1.get(i).equals(listComponents2.get(i)))
                retVal=listComponents1.get(i);
            else
                break;
        }
        
        return retVal;
    }

    /**
     * @param f
     * @return
     */
    private static ArrayList<File> listFilePathComponents(File f) {
    	ArrayList<File> listToReturn=new ArrayList<File>();
        File parent=f.getAbsoluteFile();
        while(parent!=null)
        {
        	listToReturn.add(0,parent);
        	parent=parent.getParentFile();
        }	
        return listToReturn;
    }

    /**
     * @param baseDirForOriginalContentFiles
     * @param tempDir
     * @param originalContentFiles
     * @return
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public static Collection<File> copyFilesToAnotherDirWithRelativePaths(File srcDir, File destDir, Collection<File> originalFiles) throws FileNotFoundException, IOException {
        ArrayList<File> newFiles=new ArrayList<File>(originalFiles.size());
        for(File f:originalFiles)
            newFiles.add(copyFileToAnotherDirWithRelativePaths(srcDir,destDir,f));
        return newFiles;
    }
    
    public static File copyFileToAnotherDirWithRelativePaths(File srcDir, File destDir, File originalFile) throws FileNotFoundException,IOException{
        String relativePath=makeRelativePath(srcDir.getAbsolutePath(),originalFile.getAbsolutePath());
        File newFile=new File(destDir,relativePath);
        FileInputStream fis=new FileInputStream(originalFile);
        FileOutputStream fos=new FileOutputStream(newFile);
        copyInputStreamToOutputStream(fis,fos);
        fis.close();
        fos.close();
        return newFile;
    }
    public static String locateFilePath(String abstractFilePath) {
		if(abstractFilePath==null) return null;
		if(abstractFilePath.startsWith("classpath://"))
		{
			String path=abstractFilePath.substring("classpath://".length());
			URL filePath=FileUtils.class.getClassLoader().getResource(path);
			if(filePath==null)
				return null;
			if(filePath.toString().startsWith("file:"))
				return filePath.toString().substring("file:".length());
			else
				return null;
		}
		else if(abstractFilePath.contains("://"))
		{
			URL location;
			try {
				location = new URL(abstractFilePath);
			}
			catch (MalformedURLException e) {
				return null;
			}
			if(location==null)
				return null;
			if(location.toString().startsWith("file:"))
				return location.toString().substring("file:".length());
			else
				return null;
		}
		else
		{
			File f=new File(abstractFilePath);
			if(f.exists())
				return f.getAbsolutePath();
			else
				return null;
		}
	}
    

}
