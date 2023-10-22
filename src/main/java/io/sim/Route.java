package io.sim;
import java.util.ArrayList;
import java.util.List;

public class Route {
    private List<String> edges;

    public Route() {
        this.edges = new ArrayList<>();
    }

    public void addEdge(String edge) {
        this.edges.add(edge);
    }

    public List<String> getEdges() {
        return edges;
    }
}
