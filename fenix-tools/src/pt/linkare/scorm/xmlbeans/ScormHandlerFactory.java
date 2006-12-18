/**
 * 
 */
package pt.linkare.scorm.xmlbeans;

import java.io.File;

/**
 * @author Oscar Ferreira - Linkare TI
 *
 */
public class ScormHandlerFactory {

    private static ScormHandler scormHandlerImplementation=new ScormHandlerImpl();
    
    public static ScormHandler getScormHandler()
    {
        return scormHandlerImplementation;
    }
    
    
    public static class ScormTest
    {
        public static void main(String[] args) 
        {
            try
            {            
                ScormData scormData=ScormHandlerFactory.getScormHandler().parseScormPifFile(new File(args[0]));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        
    }

    
    
}
