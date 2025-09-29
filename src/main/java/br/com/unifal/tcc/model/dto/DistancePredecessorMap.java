package br.com.unifal.tcc.model.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import br.com.unifal.tcc.exceptions.UnreachableVertexException;
import br.com.unifal.tcc.model.graph.Vertex;
import lombok.Getter;

public class DistancePredecessorMap {
    @Getter
    private HashMap<Vertex, Double> distances;
    private HashMap<Vertex, Vertex> predecessors;

    public DistancePredecessorMap() {
        this.distances = new HashMap<>();
        this.predecessors = new HashMap<>();
    }

    public double getDistance(Vertex vertex) {
        return distances.getOrDefault(vertex, Double.POSITIVE_INFINITY);
    }

    public void setDistance(Vertex vertex, double cost) {
        distances.put(vertex, cost);
    }

    public Vertex getPredecessor(Vertex vertex) {
        return predecessors.get(vertex);
    }

    public void setPredecessor(Vertex vertex, Vertex predecessor) {
        predecessors.put(vertex, predecessor);
    }

    public List<Vertex> getPath(Vertex target) {
        if (getDistance(target) == Double.POSITIVE_INFINITY) {
            throw new UnreachableVertexException(
                    String.format(
                            "Target vertex with id: %s is not reachable from start vertex!", target.getId()));
        }

        List<Vertex> path = new ArrayList<>();
        Vertex current = target;

        while (Objects.nonNull(current)) {
            path.add(current);
            current = predecessors.get(current);
        }

        Collections.reverse(path);
        return path;
    }

    public double getPathCostTo(Vertex target) {
        if (getDistance(target) == Double.POSITIVE_INFINITY) {
            throw new UnreachableVertexException(
                    String.format(
                            "Target vertex with id: %s is not reachable from start vertex!", target.getId()));
        }
        return distances.get(target);
    }
}
