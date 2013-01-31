package pt.utl.ist.fenix.tools.codeGenerator;

import java.io.IOException;

import pt.ist.fenixframework.pstm.DML;
import pt.ist.fenixframework.pstm.dml.FenixDomainModelWithOCC;
import pt.utl.ist.fenix.tools.util.DomainModelDirFileLister;
import dml.DomainModel;

public abstract class DomainObjectGenerator {

	protected DomainModel domainModel;

	protected String dmlDir;

	protected String outputFolder;

	protected String sourceSuffix = "_Base.java";

	protected DomainModel getModel() {
		if (domainModel == null) {
			if (dmlDir.length() == 0) {
				throw new Error("No DML files directory specified");
			} else {
				try {
					String[] dmlFilesArray = DomainModelDirFileLister.listDomainModelFiles(dmlDir);
					domainModel = DML.getDomainModel(FenixDomainModelWithOCC.class, dmlFilesArray);
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
			domainObjectGenerator.dmlDir = args[1];
			domainObjectGenerator.appendMethodsInTheRootDomainObject();
		} catch (IOException e) {
			throw new Error(e);
		}
	}

	public abstract void appendMethodsInTheRootDomainObject() throws IOException;

}
