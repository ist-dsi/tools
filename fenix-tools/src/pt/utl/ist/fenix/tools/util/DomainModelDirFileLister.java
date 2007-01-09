package pt.utl.ist.fenix.tools.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class DomainModelDirFileLister {

	private static class DomainModelFileFilter implements FileFilter
    {

		public boolean accept(File file) {
			if(file.isDirectory()) return false;
			System.out.println("File "+file.getAbsolutePath()+" is a domain model file ? "+file.getAbsolutePath().endsWith(".dml"));
	    	return file.getAbsolutePath().endsWith(".dml");
		}
    	
    }

    private static class DomainModelFileComparator implements Comparator<File>
    {

		public int compare(File o1, File o2) {
			return o1.getPath().compareTo(o2.getPath());
		}
    	
    }

    public static String[] listDomainModelFiles(String domainModelDirPath) {
        
	File fDomainDirOrFile=new File(domainModelDirPath);
	if(fDomainDirOrFile.isFile())
	    return new String[]{domainModelDirPath};
	
	
    	File[] domainModelFiles=(new File(domainModelDirPath)).listFiles(new DomainModelFileFilter());
        Set<File> sortedDomainModelFiles=new TreeSet<File>(new DomainModelFileComparator());
        sortedDomainModelFiles.addAll(Arrays.asList(domainModelFiles));
        domainModelFiles=sortedDomainModelFiles.toArray(new File[0]);
        String[] domainModelPaths=new String[domainModelFiles.length];
        int i=0;
        for(File f:domainModelFiles)
        	domainModelPaths[i++]=f.getAbsolutePath();

		return domainModelPaths;
	}


}
