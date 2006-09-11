package pt.utl.ist.fenix.tools.codeGenerator;

import java.io.IOException;

import dml.DmlCompiler;
import dml.DomainModel;

public abstract class DomainObjectGenerator {

    protected DomainModel domainModel;

    protected String dmlFile;

    protected String outputFolder;

    protected String sourceSuffix = "_Base.java";

    protected DomainModel getModel() {
        if (domainModel == null) {
            if (dmlFile.length() == 0) {
                throw new Error("No DML files specified");
            } else {
                try {
                    String[] dmlFilesArray = { dmlFile };
                    domainModel = DmlCompiler.getFenixDomainModel(dmlFilesArray);
                } catch (antlr.ANTLRException ae) {
                    System.err.println("Error parsing the DML files, leaving the domain empty");
                }
            }
        }
        return domainModel;
    }

    public static void process(final String[] args, final DomainObjectGenerator domainObjectGenerator) {
        if (args.length < 2) {
            throw new Error("Invalid Number of Arguments");
        }

        try {
            domainObjectGenerator.outputFolder = args[0];
            domainObjectGenerator.dmlFile = args[1];
            domainObjectGenerator.appendMethodsInTheRootDomainObject();
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    public abstract void appendMethodsInTheRootDomainObject() throws IOException;

}
