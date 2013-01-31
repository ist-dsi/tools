/**
 * 
 */
package pt.linkare.scorm.utils.deprecated;


/**
 * @author oferreira
 *
 */
/*
public class DeleteDirectory {

  public static void main(String[] args)
  {
      deleteInsideDirectory(new File("/home/oferreira/Desktop/SCORMUploadTemp"));
  }
  
  //Called to delete the interior of a Directory(path) without deleting the Directory
  static public boolean deleteInsideDirectory(File path) 
  {
    try{          
        if( path.exists() ) {
          File[] files = path.listFiles();
          for(int i=0; i<files.length; i++) {
             if(files[i].isDirectory()) {
               deleteDirectory(files[i]);
             }
             else {
               files[i].delete();
             }
          }
        }
        return true;
    }
    catch(Exception e)
    {
        //new ScormException(ScormException.IMS_MANIFEST_EXCEPTION,e).printStackTrace();
        e.printStackTrace();
        return false;
    }
  }
  
  //Deletes Directory(path)
  static public boolean deleteDirectory(File path) {
      if( path.exists() ) {
        File[] files = path.listFiles();
        for(int i=0; i<files.length; i++) {
           if(files[i].isDirectory()) {
             deleteDirectory(files[i]);
           }
           else {
             files[i].delete();
           }
        }
      }
      return( path.delete() );
    }
  
}*/