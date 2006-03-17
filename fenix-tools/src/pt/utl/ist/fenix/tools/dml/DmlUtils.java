package pt.utl.ist.fenix.tools.dml;

import dml.DmlCompiler;
import dml.DomainModel;

public class DmlUtils {

    public static DomainModel getModel(final String dmlFile) {
        if (dmlFile == null || dmlFile.length() == 0) {
            throw new Error("No DML files specified");
        } else {
            try {
                String[] dmlFilesArray = { dmlFile };
                return DmlCompiler.getDomainModel(dmlFilesArray);
            } catch (antlr.ANTLRException ae) {
                throw new Error("Error parsing the DML files, leaving the domain empty", ae);
            }
        }
    }

}
