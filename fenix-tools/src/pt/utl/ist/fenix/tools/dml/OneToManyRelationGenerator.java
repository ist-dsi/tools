package pt.utl.ist.fenix.tools.dml;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import dml.DomainClass;
import dml.DomainEntity;
import dml.DomainModel;


public class OneToManyRelationGenerator {

    public static void main(String[] args) {
        try {
            final String dmlFile = args[0];
            final String oneDomainObjectClassname = args[1];
            final String outputDmlFile = args[2];
            final boolean append = args.length == 4 && args[3].equalsIgnoreCase("append");

            final FileWriter fileWriter = new FileWriter(outputDmlFile, append);
            fileWriter.write("/* \n * Relations definitions of relations to RootDomainObject \n * \n */\n");

            final Set<String> usedNames = new HashSet<String>();

            final DomainModel domainModel = DmlUtils.getModel(dmlFile);
            final DomainClass oneDomainClass = domainModel.findClass(oneDomainObjectClassname);
            final String oneDomainClassName = oneDomainClass.getName().substring(0, 1).toLowerCase()
                    + oneDomainClass.getName().substring(1, oneDomainClass.getName().length());
            for (final Iterator<DomainClass> iterator = domainModel.getClasses() ; iterator.hasNext(); ) {
                final DomainClass domainClass = iterator.next();
                final String classname = domainClass.getFullName();
                final Class clazz = Class.forName(classname);

                if (clazz.getSuperclass().getSuperclass().getName().equals("net.sourceforge.fenixedu.domain.DomainObject")) {
                	final String tmpName = determineClassnameToUse(usedNames, domainClass);
                	final String name = tmpName.substring(0, 1).toLowerCase() + tmpName.substring(1, tmpName.length());

                	fileWriter.write("\n\nrelation ");
                	fileWriter.write(oneDomainClass.getName());
                	fileWriter.write(tmpName);
                	fileWriter.write(" {\n\t");
                	writeRelationPart(fileWriter, oneDomainClass, oneDomainClassName);
                	fileWriter.write(";\n\t");
                	writeRelationPart(fileWriter, domainClass, name);
                	fileWriter.write("s {\n\t\tmultiplicity *;\n\t}\n}");
                } else {
                	System.out.println("Ignoring " + domainClass.getFullName());
                }
            }
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }

    private static String determineClassnameToUse(final Set<String> usedNames, final DomainClass domainClass) {
        final String originalName = domainClass.getName();
        if (usedNames.contains(originalName)) {
            final DomainEntity superclass = domainClass.getSuperclass();
            final String superclassName = superclass.getName();
            return superclassName + originalName;
        } else {
            usedNames.add(originalName);
            return originalName;
        }
    }

    private static void writeRelationPart(final FileWriter fileWriter, final DomainClass domainClass, final String name) throws IOException {
        fileWriter.write(domainClass.getFullName());
        fileWriter.write(" playsRole ");
        fileWriter.write(name);
    }

}
