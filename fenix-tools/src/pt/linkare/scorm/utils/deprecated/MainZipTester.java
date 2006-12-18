/**
 * 
 */
package pt.linkare.scorm.utils.deprecated;


;

/**
 * @author oferreira
 *
 */
/*
public class MainZipTester {

    private static final int BUFFER_SIZE=1024;
    
    //Returns an Object that represents the file wanted from inside a ZipFile,
    //if file not found returns null
    public static ZipEntry getFileEntry(String zipfileName,String fileName) throws IOException,FileNotFoundException
    {
        try
        {
            FileInputStream fis = new FileInputStream(zipfileName);
            if(fis!=null)
            {
                ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
                if(zis!=null)
                {
                    ZipEntry ze;
                    while((ze=zis.getNextEntry())!=null)
                    {
                        if(!ze.isDirectory())
                        {
                            if(ze.getName().compareToIgnoreCase(fileName)==0)
                                return ze;
                        }
                    }
                }
            }  
        }catch(FileNotFoundException e)
        {
            throw e;
        }
        catch(IOException e)
        {
            throw e;
        }
        return null;
    }
    
    //Returns an ArrayList<ZipEntry> of all the ZipEntries in the ZipFile,even the ones
    //in directories
    public static Collection<ZipEntry> getAllFilesEntry(String zipfileName) throws IOException,FileNotFoundException
    {
        try
        {
            FileInputStream fis = new FileInputStream(zipfileName);
            if(fis!=null)
            {
                ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
                if(zis!=null)
                {
                    ZipEntry ze;
                    Collection<ZipEntry> zipEntryCol = new ArrayList<ZipEntry>();
                    while((ze=zis.getNextEntry())!=null)
                    {
                        if(!ze.isDirectory())
                        {
                            zipEntryCol.add(ze);
                        }
                    }
                    return zipEntryCol;
                }
            }
        }
        catch(FileNotFoundException e)
        {
            throw e;
        }
        catch(IOException e)
        {
            throw e;
        }
        return null;
    }
    
    //Returns an ArrayList<ZipEntry> of all the ZipEntries in the ZipFile,with the right 
    //extention, even the ones in directories
    public static Collection<ZipEntry> getAllFilesEntry(String zipfileName,String extention) throws IOException,FileNotFoundException
    {
        try
        {
            FileInputStream fis = new FileInputStream(zipfileName);
            if(fis!=null)
            {
                ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
                if(zis!=null)
                {
                    ZipEntry ze;
                    Collection<ZipEntry> zipEntryCol = new ArrayList<ZipEntry>();
                    while((ze=zis.getNextEntry())!=null)
                    {
                        if(!ze.isDirectory())
                        {
                            if(extention.charAt(0)!='.')
                                extention=".".concat(extention);
                            if(ze.getName().endsWith(extention))
                                zipEntryCol.add(ze);                      
                        }
                    }
                    return zipEntryCol;
                }
            }
        }
        catch(FileNotFoundException e)
        {
            throw e;
        }
        catch(IOException e)
        {
            throw e;
        }
        return null;
    }
    

//  Returns a file wanted from inside a ZipFile,
    //if file not found returns null
    public static File getFile(String zipfileName,String fileName) throws IOException,FileNotFoundException
    {
        int count;
        byte[] data=new byte[MainZipTester.BUFFER_SIZE];
        try
        {
            FileInputStream fis = new FileInputStream(zipfileName);
            if(fis!=null)
            {
                ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
                if(zis!=null)
                {
                    ZipEntry ze;
                    while((ze=zis.getNextEntry())!=null)
                    {   
                        if(!ze.isDirectory())
                        {
                            if(ze.getName().compareToIgnoreCase(fileName)==0)
                            {
                                File file = File.createTempFile(fileName,"");
                                
                                FileOutputStream fos= new FileOutputStream(file);
                                BufferedOutputStream bos= new BufferedOutputStream(fos,MainZipTester.BUFFER_SIZE);
                                while ((count = zis.read(data, 0, MainZipTester.BUFFER_SIZE)) != -1)
                                {                                    
                                    bos.write(data, 0, count);
                                }
                                bos.flush();
                                bos.close();
                                return file;
                            }
                        }
                    }
                }
                zis.close();
            }  
        }catch(FileNotFoundException e)
        {
            throw e;
        }
        catch(IOException e)
        {
            throw e;
        }
        return null;
    }
            
//  Returns a boolean to inform if file exists in ZipFile
    public static boolean getFileBoolean(String zipfileName,String fileName) throws IOException,FileNotFoundException
    {
        try
        {
            FileInputStream fis = new FileInputStream(zipfileName);
            if(fis!=null)
            {
                ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
                if(zis!=null)
                {
                    ZipEntry ze;
                    while((ze=zis.getNextEntry())!=null)
                    {   
                        if(!ze.isDirectory())
                        {
                            if(ze.getName().compareToIgnoreCase(fileName)==0)
                            {
                                return true;
                            }
                        }
                    }
                }
                zis.close();
            }  
        }catch(FileNotFoundException e)
        {
            throw e;
        }
        catch(IOException e)
        {
            throw e;
        }
        return false;
    }
    
    @SuppressWarnings("unused")
    private static boolean removeDir(File directory)
    {
        if(directory.isDirectory())
        {
            if(directory.exists())
            {
                File[] fileArray=directory.listFiles();
                for(File fileOrDir:fileArray)
                {
                    if(fileOrDir.isDirectory())
                        MainZipTester.removeDir(fileOrDir);
                    else
                        fileOrDir.delete();
                }
                //here the root directory should be empty
                directory.delete();
                return true;
            }
        }
        else
            return false;
        return false;
    }
    
    /*
     *  //XXX Versão José Pedro que agora está no FileUtils.unzipFile(File)
     * //Dando o endereço do ficheiro .zip e o nome da Directoria onde o seu conteudo será
    //descarregado.
    @SuppressWarnings("unused")
    public static File extractZipFile(File zipFile) throws Exception
    {
        File tempFile=File.createTempFile("scorm_",".extract");
        File tempDir=new File(tempFile.getAbsolutePath());
        tempFile.delete();
        tempDir.mkdirs();
        tempDir.deleteOnExit();        
        try{
            FileInputStream fis = new FileInputStream(zipFile);
            if(fis!=null)
            {
                ZipInputStream zis = new ZipInputStream(fis);
                if(zis!=null)
                {
                    System.out.println("Extracting zipfile '"+zipFile.getPath()+"' in Directory '"+tempDir.getPath()+"'.");
                    FileOutputStream fos;
                    ZipEntry zEntry;
                    System.out.println("Getting Files from Zip File and Extracting them...");
                    while((zEntry=zis.getNextEntry())!=null)
                    {
                        int counter=0 ,index=0;
                        File newFile=new File(tempDir,zEntry.getName());
                        if(!zEntry.isDirectory())
                        {
                            byte[] data = new byte[MainZipTester.BUFFER_SIZE];
                            int count = -1;
                            String innerDirAux=null;
                            fos= new FileOutputStream(newFile);
                            BufferedOutputStream bos= new BufferedOutputStream(fos,MainZipTester.BUFFER_SIZE);
                            while ((count = zis.read(data, 0, MainZipTester.BUFFER_SIZE)) != -1) 
                            {
                                bos.write(data, 0, count);
                            }
                            bos.flush();  
                            bos.close();
                        }
                        else
                        {
                            System.out.println("Directory "+newFile.getPath()+" Created...");
                            newFile.mkdirs();
                        }
                    }
                    zis.close();
                }                
            }
            return tempDir;
        }
        catch(Exception e)
        {
            throw e;
        }
    }*/
    
/*
    //XXX Versão Óscar Ferreira
//  Dando o endereço do ficheiro .zip e o nome da Directoria onde o seu conteudo será
    //descarregado.
    public static File extractZipFile(File zipfileName) throws Exception
    {
        return FileUtils.unzipFile(zipfileName);
        /*try{
            File tempFile=File.createTempFile("scorm_",".extract");
            File tempDir=new File(tempFile.getAbsolutePath());
            tempFile.delete();
            tempDir.mkdirs();
            tempDir.deleteOnExit(); 
            FileInputStream fis = new FileInputStream(zipfileName);
            if(fis!=null)
            {
                ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
                if(zis!=null)
                {
                    System.out.println("Extracting zipfile '"+zipfileName.getAbsolutePath()+"' in Directory '"+tempDir.getAbsolutePath()+"'.");                    
                    FileOutputStream fos;
                    ZipEntry zEntry;
                    System.out.println("Getting Files from Zip File and Extracting them...");
                    while((zEntry=zis.getNextEntry())!=null)
                    {
                        int counter=0 ,index=0;
                        String entryName = zEntry.getName();
                        entryName=tempDir.getAbsolutePath().concat("/").concat(entryName);
                        if(!zEntry.isDirectory())
                        {
                            byte[] data = new byte[MainZipTester.BUFFER_SIZE];
                            int count = -1;
                            String innerDirAux=null;
                            while((index=entryName.indexOf('/',index+1))!=-1)
                            {
                                if(counter!=0)
                                {
                                    innerDirAux=entryName.substring(0,index+1);                                   
                                    new File(innerDirAux).mkdir();
                                }
                                counter++;
                            }  
                            fos= new FileOutputStream(entryName);
                            BufferedOutputStream bos= new BufferedOutputStream(fos,MainZipTester.BUFFER_SIZE);
                            while ((count = zis.read(data, 0, MainZipTester.BUFFER_SIZE)) != -1) 
                            {
                                bos.write(data, 0, count);
                            }
                            bos.flush();  
                            bos.close();
                        }
                        else
                        {
                            System.out.println("Directory "+entryName+" Created...");
                            new File(entryName).mkdir();
                        }
                    }
                    zis.close();
                }                
            }
            return tempDir;
        }
        catch(Exception e)
        {
            throw e;
        }
    }
    
    //Dá-nos os ficheiros todos duma File(Directoria)
    public static Collection<File> colAllFiles(File file)
    {
        File[] fileArray=file.listFiles();
        Collection<File> col =new ArrayList<File>(fileArray.length);
        col.addAll(Arrays.asList(fileArray));
        for(File f:fileArray)
        {
            if(f.isDirectory())
                col.addAll(MainZipTester.colAllFiles(f));
        }
        return col;
    }
    
    //Dando um File(representa uma Directoria) e uma string, é devolvido
    //uma colecção de ficheiros que contêm o myFilter no nome.
    public static Collection<File> colAllFilteredFiles(File file,String myFilter)
    {
        File[] fileArray=file.listFiles(new MyFileFilter(myFilter));
        Collection<File> col =new ArrayList<File>(fileArray.length);
        col.addAll(Arrays.asList(fileArray));
        for(File f:fileArray)
        {
            if(f.isDirectory())
                col.addAll(MainZipTester.colAllFilteredFiles(f,myFilter));
        }
        return col;
    }
    */
    /**
     * @param args
     */
    /*
    public static void main(String[] args) {
        try
        {  
           Collection<File> fileCol=null;
           File zipDirectory=extractZipFile(new File(args[0]));           
           //fileArray=zipDirectory.listFiles(new MyFileFilter(".xml"));
           //fileArray=zipDirectory.listFiles(new MyFileFilter(".pdf"));
           System.out.println(zipDirectory.getAbsolutePath());
           fileCol = MainZipTester.colAllFilteredFiles(zipDirectory,"imsmanifest.xml");
           for(File f:fileCol)
           {
               System.out.println("A XML Files: "+f.getAbsolutePath());
           }
           if(fileCol==null || fileCol.size()<=0)
               throw new ScormException("Didn't find a imsmanifest.xml FILE!");
           ImsManifestReader_1_2 imsMR=new ImsManifestReader_1_2(zipDirectory);
           Collection<ScormMetaData> imCol=imsMR.readManifestData(fileCol.iterator().next(),null);
           for(ScormMetaData im:imCol)
           {
               System.out.println("Element: "+im.getElement());
               System.out.println("Qualifier: "+im.getQualifier());
               System.out.println("Language: "+im.getLang());
               if(im.getValues()!=null)
               {                   
                   for(String str:im.getValues())
                   {
                       System.out.println("   Values: "+str);
                   }
               }
               else
                   System.out.println("   Values: "+im.getValues());
           }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
/*
    public static void zipDir(File dir2zip, ZipOutputStream zos,String tempDir) 
    { 
        try 
           { 
                //create a new File object based on the directory we have to zip 
                //get a listing of the directory content                
                String[] dirList = dir2zip.list(); 
                byte[] readBuffer = new byte[2156]; 
                int bytesIn = 0; 
                //loop through dirList, and zip the files 
                for(int i=0; i<dirList.length; i++) 
                { 
                    File f = new File(dir2zip, dirList[i]); 
                    if(f.isDirectory()) 
                    { 
                            //if the File object is a directory, call this 
                            //function again to add its content recursively
                        tempDir=tempDir.concat("/").concat(f.getName());
                        ZipEntry anEntry = new ZipEntry(tempDir);
                        zos.putNextEntry(anEntry);
                        zipDir(f, zos,""); 
                            //loop again 
                        continue; 
                    } 
                    //if we reached here, the File object f was not a directory 
                    //create a FileInputStream on top of f 
                    FileInputStream fis = new FileInputStream(f); 
                    //create a new zip entry //XXX era getPath() antes
                    ZipEntry anEntry = new ZipEntry(tempDir+"/"+dir2zip.getName()+"/"+f.getName()); 
                    //place the zip entry in the ZipOutputStream object 
                    zos.putNextEntry(anEntry); 
                    //now write the content of the file to the ZipOutputStream 
                    while((bytesIn = fis.read(readBuffer)) != -1) 
                    { 
                        zos.write(readBuffer, 0, bytesIn); 
                    } 
                   //close the Stream 
                   fis.close(); 
            } 
        } 
        catch(Exception e) 
        { 
            //handle exception 
        } 
    }
    
    @SuppressWarnings("unused")
    public static void zipDir(File dir2zip, ZipOutputStream zos) 
    { 
        try 
           { 
                //create a new File object based on the directory we have to zip 
                //get a listing of the directory content                
                String[] dirList = dir2zip.list(); 
                byte[] readBuffer = new byte[2156]; 
                int bytesIn = 0; 
                //loop through dirList, and zip the files 
                for(int i=0; i<dirList.length; i++) 
                { //XXX added!
                    //System.out.println("dirList pos-> "+i+" value: "+dirList[i]);
                    File f = new File(dir2zip, dirList[i]); 
                    if(f.isDirectory()) 
                    { 
                            //if the File object is a directory, call this 
                            //function again to add its content recursively
                        String tempDir=dir2zip.getName();
                        //XXX add this here
                        ZipEntry anEntry = new ZipEntry(f.getName()+"/");
                        zos.putNextEntry(anEntry);
                        //XXX end
                        //zipDir(f, zos,"");
                        zipDir(f, zos,"");
                            //loop again 
                        continue; 
                    } 
                    //if we reached here, the File object f was not a directory 
                    //create a FileInputStream on top of f 
                    FileInputStream fis = new FileInputStream(f); 
                    //create a new zip entry //XXX era getPath() antes
                    //ZipEntry anEntry = new ZipEntry(dir2zip.getName()+"/"+f.getName());
                    ZipEntry anEntry = new ZipEntry(f.getName());
                    //place the zip entry in the ZipOutputStream object 
                    zos.putNextEntry(anEntry); 
                    //now write the content of the file to the ZipOutputStream 
                    while((bytesIn = fis.read(readBuffer)) != -1) 
                    { 
                        zos.write(readBuffer, 0, bytesIn); 
                    } 
                   //close the Stream 
                   fis.close(); 
            } 
        } 
        catch(Exception e) 
        { 
            //handle exception 
        } 
    }
*/
    /*
}
*/
