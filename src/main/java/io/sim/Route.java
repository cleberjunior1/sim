package io.sim;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

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

    public void setEdges(String edges) {
        // Divida a string em substrings representando as arestas
        List<String> edgeList = Arrays.asList(edges.split(" "));

        // Limpe a lista de arestas atual
        this.edges.clear();

        // Adicione as arestas à lista
        this.edges.addAll(edgeList);
    }
}
