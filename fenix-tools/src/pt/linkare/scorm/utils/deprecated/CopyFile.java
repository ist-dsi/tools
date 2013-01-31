/**
 * 
 */
package pt.linkare.scorm.utils.deprecated;


/**
 * @author oferreira
 *
 */
/*
public class CopyFile {

    public static final int COPYFILE_BUFFER_SIZE=1024;
    
    //Creates a File(fileName) receiving it's data from inputstream in a buffered manner(bufSize)
    public static File createFile(InputStream inputstream,int bufSize,String fileName)
    {
        try
        {   int count=-1;        
            byte[] data=new byte[bufSize];
            File tempFile=File.createTempFile("scorm_",".extract");
            File tempDir=new File(tempFile.getAbsolutePath());
            tempFile.delete();
            tempDir.mkdirs();
            tempDir.deleteOnExit();
            File file=new File(tempDir.getAbsolutePath(),fileName);
            FileOutputStream fos= new FileOutputStream(file);
            BufferedOutputStream bos= new BufferedOutputStream(fos,bufSize);
            while ((count = inputstream.read(data, 0, bufSize)) != -1) 
            {
                bos.write(data, 0, count);
            }
            bos.flush();  
            bos.close();
            return file;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            //new ScormException(ScormException.IMS_MANIFEST_EXCEPTION,e).printStackTrace();            
        }
        return null;
    }
    
//  Creates a File(fileName) with an absolutePath
    //receiving it's data from inputstream in a buffered manner(bufSize)
    public static File createFile(String absolutePath,InputStream inputstream,int bufSize,String fileName)
    {
        try
        {   if(absolutePath==null)
            createFile(inputstream,bufSize,fileName);
            int count=-1;        
            byte[] data=new byte[bufSize];            
            File file=new File(absolutePath,fileName);
            FileOutputStream fos= new FileOutputStream(file);
            BufferedOutputStream bos= new BufferedOutputStream(fos,bufSize);
            while ((count = inputstream.read(data, 0, bufSize)) != -1) 
            {
                bos.write(data, 0, count);
            }
            bos.flush();  
            bos.close();
            return file;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            //new ScormException(ScormException.IMS_MANIFEST_EXCEPTION,e).printStackTrace();
        }
        return null;
    }
    
    //Creates File(fileName),data from an inputstream,with classes default buffer size(1024 bytes)
    public static File createFile(InputStream inputstream,String fileName)
    {
        return CopyFile.createFile(inputstream,CopyFile.COPYFILE_BUFFER_SIZE,fileName);
    }
    
    //Creates a file with fileName from a vector of bytes data to a tempFile location
    public static File createFile(byte[] data,String fileName)
    {
        try
        {      
            File tempFile=File.createTempFile("scorm_",".extract");
            File tempDir=new File(tempFile.getAbsolutePath());
            tempFile.delete();
            tempDir.mkdirs();
            tempDir.deleteOnExit();
            File file=new File(tempDir.getAbsolutePath(),fileName);
            FileOutputStream fos= new FileOutputStream(file);
            BufferedOutputStream bos= new BufferedOutputStream(fos,data.length);
            bos.write(data);
            bos.flush();  
            bos.close();
            return file;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            //new ScormException(ScormException.IMS_MANIFEST_EXCEPTION,e).printStackTrace();
        }
        return null;
    }
    
//  Creates a file with fileName from a vector of bytes data to a absolutePath location
    public static File createFile(String absolutePath,byte[] data,String fileName)
    {
        try
        {      
            if(absolutePath==null)
                createFile(data,fileName);
            File file=new File(absolutePath,fileName);
            FileOutputStream fos= new FileOutputStream(file);
            BufferedOutputStream bos= new BufferedOutputStream(fos,data.length);
            bos.write(data);
            bos.flush();  
            bos.close();
            return file;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            //new ScormException(ScormException.IMS_MANIFEST_EXCEPTION,e).printStackTrace();
        }
        return null;
    }
    
//  Creates a file with fileName from a vector of bytes data to a tempFile location
    //Used for the ScormServices upload Packages
    public static File createUploadFile(byte[] data,String fileName)
    {
        try
        {       
            File tmpFile=File.createTempFile("scorm_",".upload");
            File tmpDir=new File(tmpFile.getAbsolutePath());
            tmpFile.delete();
            tmpDir.mkdirs();
            tmpDir.deleteOnExit();
            File file=new File(tmpDir.getAbsolutePath(),fileName);
            file.deleteOnExit();
            FileOutputStream fos= new FileOutputStream(file);
            BufferedOutputStream bos= new BufferedOutputStream(fos,data.length);
            bos.write(data);
            bos.flush();  
            bos.close();
            return file;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            //new ScormException(ScormException.IMS_MANIFEST_EXCEPTION,e).printStackTrace();
        }
        return null;
    }
//  Creates a file with fileName from a vector of bytes data to a absolutePath location
    public static File createUploadFile(String absolutePath,byte[] data,String fileName)
    {
        try
        {    
            if(absolutePath==null)
                createUploadFile(data,fileName);
            if(!new File(absolutePath+"/resources").mkdir())
                throw new ScormException(ScormException.IMS_MANIFEST_FILE_DIRECTORY_FAILED_CREATE);
            File file=new File(absolutePath+"/resources",fileName);            
            FileOutputStream fos= new FileOutputStream(file);
            BufferedOutputStream bos= new BufferedOutputStream(fos,data.length);
            bos.write(data);
            bos.flush();  
            bos.close();
            return file;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            //new ScormException(ScormException.IMS_MANIFEST_EXCEPTION,e).printStackTrace();
        }
        return null;
    }
    
    //Using InputStream(input) in a buffered mode sends data to an OutputStream(output),using bufSize
    public static void copyFile(InputStream input,OutputStream output,int bufSize)
    {
        try
        {
            int count=-1;
            BufferedInputStream inputBuffed=null;
            BufferedOutputStream outputBuffed=null;
            byte[] data=new byte[bufSize];
            if (input instanceof BufferedInputStream) {
                inputBuffed = (BufferedInputStream) input;
            }
            else
            {
                inputBuffed=new BufferedInputStream(input,bufSize);                         
            }
            if (output instanceof BufferedOutputStream) {
                outputBuffed = (BufferedOutputStream) output;                
            }
            else
            {
                outputBuffed=new BufferedOutputStream(output,bufSize);
            }
            while ((count = inputBuffed.read(data, 0, bufSize)) != -1) 
            {
                outputBuffed.write(data, 0, count);
            }
            outputBuffed.flush();  
            outputBuffed.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            //new ScormException(ScormException.IMS_MANIFEST_EXCEPTION,e).printStackTrace();
        }                 
    }
    
//  Using InputStream(input) in a buffered mode sends data to an OutputStream(output),using default buffer size
    public static void copyFile(InputStream input,OutputStream output)
    {
        CopyFile.copyFile(input,output,CopyFile.COPYFILE_BUFFER_SIZE);
    }
        
}*/
