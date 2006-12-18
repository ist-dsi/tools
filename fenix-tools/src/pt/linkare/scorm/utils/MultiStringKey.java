/**
 * 
 */
package pt.linkare.scorm.utils;

import java.util.Arrays;

/**
 * @author Óscar Ferreira - LINKARE TI
 *
 */
public class MultiStringKey {
    
    private String[] strKeys;
    
    public MultiStringKey(String... strKeys)
    {
        this.strKeys=strKeys;
    }

    @Override
    public int hashCode()
    {
    	return Arrays.hashCode(strKeys);
    }
    
    @Override
    public boolean equals(Object other) 
    {
        if(other instanceof MultiStringKey)
        {
            MultiStringKey theOther=(MultiStringKey) other;
            return Arrays.equals(strKeys, theOther.getKeys());
        }
        return false;
    }

    public String[] getKeys()
    {
        if(strKeys==null) return null;
        String[] copyOfKeys=new String[strKeys.length];
        System.arraycopy(strKeys,0,copyOfKeys,0,strKeys.length);
        return copyOfKeys;
    }
    
    public String getKey(int index)
    {
        return getKeys()[index];
    }
    
}

    