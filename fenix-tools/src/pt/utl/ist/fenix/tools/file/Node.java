package pt.utl.ist.fenix.tools.file;

/**
 * 
 * @author naat
 * 
 */
public class Node {
    private String name;

    private String description;

    public Node(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

}
