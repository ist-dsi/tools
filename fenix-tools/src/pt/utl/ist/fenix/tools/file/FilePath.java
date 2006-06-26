package pt.utl.ist.fenix.tools.file;

import java.util.ArrayList;
import java.util.List;

public class FilePath {

    private List<Node> nodes;

    public FilePath() {
        this.nodes = new ArrayList<Node>();
    }

    public FilePath addNode(int index, Node node) {
        this.nodes.add(index, node);
        return this;
    }

    public FilePath addNode(Node node) {
        this.nodes.add(node);
        return this;
    }

    public List<Node> getNodes() {
        return this.nodes;
    }

}
