package br.com.unifal.tcc.algorithms;

import br.com.unifal.tcc.algorithms.dto.DistancePredecessorMap;
import br.com.unifal.tcc.algorithms.interfaces.ShortestPathAlgorithm;
import br.com.unifal.tcc.model.dto.PathResult;
import br.com.unifal.tcc.model.graph.Graph;
import br.com.unifal.tcc.model.graph.Vertex;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SSSPAlgorithm implements ShortestPathAlgorithm {

    private static final int K = 4;

    @Override
    public String getName() {
        return "SSSP-Algorithm";
    }

    @Override
    public PathResult findShortestPath(Graph graph, Vertex start, Vertex end) {
        DistancePredecessorMap result = computeDistancePredecessorMap(graph, start);
        return new PathResult(result.getPath(end), result.getPathCostTo(end));
    }

    @Override
    public Map<Vertex, Double> getDistanceMap(Graph graph, Vertex source) {
        return computeDistancePredecessorMap(graph, source).getDistances();
    }

    /**
     * Runs SSSP's algorithm and builds a {@link DistancePredecessorMap} from the given source.
     */
    private DistancePredecessorMap computeDistancePredecessorMap(Graph graph, Vertex source) {
        DistancePredecessorMap result = new DistancePredecessorMap();

        // Initialize source
        result.setDistance(source, 0.0);
        result.setPredecessor(source, null);

        // Set of finished vertices
        Set<Vertex> finished = new HashSet<>();
        finished.add(source);

        // Initial frontier
        Set<Vertex> frontier = new HashSet<>();
        frontier.add(source);

        // Main loop
        while (!frontier.isEmpty()) {
            Set<Vertex> newlyFinishedVertices = performBoundedRelaxation(graph, frontier, result);

            if (newlyFinishedVertices.isEmpty()) {
                break; // No more progress possible
            }

            finished.addAll(newlyFinishedVertices);

            // Update frontier: vertices that border unfinished regions
            frontier = computeNewFrontier(graph, finished);

            // Recursive call for remaining unfinished vertices if needed
            if (!frontier.isEmpty()) {
                Set<Vertex> remaining = new HashSet<>();
                for (Vertex vertex : graph.getVerticesSet()) {
                    if (!finished.contains(vertex)
                            && result.getDistance(vertex) == Double.POSITIVE_INFINITY) {
                        remaining.add(vertex);
                    }
                }

                if (!remaining.isEmpty()) {
                    // Perform recursive exploration with smaller k
                    performRecursiveExploration(graph, frontier, result, finished, remaining);
                }
            }
        } // end main loop

        return result;
    }

    /**
     * Performs up to {@code K} rounds of edge relaxation from the given frontier,
     * updating tentative distances and predecessors in the result map.
     */
    private Set<Vertex> performBoundedRelaxation(
            Graph graph, Set<Vertex> frontier, DistancePredecessorMap result) {

        Set<Vertex> newlyFinishedVertices = new HashSet<>();

        Map<Vertex, Double> reachableDistance = new HashMap<>();
        Map<Vertex, Vertex> reachablePredecessor = new HashMap<>();

        // Initialize tentative distances with current known distances
        for (Vertex vertex : graph.getVerticesSet()) {
            if (result.getDistance(vertex) < Double.POSITIVE_INFINITY) {
                reachableDistance.put(vertex, result.getDistance(vertex));
                reachablePredecessor.put(vertex, result.getPredecessor(vertex));
            }
        }

        // Relax edges from all vertices discovered so far
        Set<Vertex> activeVerticesForRelaxation = new HashSet<>(frontier);
        for (Vertex vertex : reachableDistance.keySet()) {
            activeVerticesForRelaxation.add(vertex);
        }

        // Perform k rounds of relaxation
        for (int round = 0; round < K; round++) {

            // Relax edges from all vertices discovered so far
            for (Vertex vertex: newlyFinishedVertices) {
                activeVerticesForRelaxation.add(vertex);
            }

            for (Vertex currentVertex : activeVerticesForRelaxation) {
                if (reachableDistance.containsKey(currentVertex)) {
                    double currentVertexDistance = reachableDistance.get(currentVertex);

                    Map<Vertex, Double> neighbors = graph.getNeighbors(currentVertex);
                    for (Map.Entry<Vertex, Double> neighborEntry : neighbors.entrySet()) {

                        Vertex neighbor = neighborEntry.getKey();

                        double newNeighborDistance = currentVertexDistance + neighborEntry.getValue();
                        double currentNeighborDistance =
                                reachableDistance.getOrDefault(neighbor, Double.POSITIVE_INFINITY);

                        if (newNeighborDistance < currentNeighborDistance) {
                            newlyFinishedVertices.add(neighbor);
                            reachableDistance.put(neighbor, newNeighborDistance);
                            reachablePredecessor.put(neighbor, currentVertex);
                        }
                    }
                }
            }
        } // end K rounds of relaxation

        // Apply relaxation
        for (Vertex vertex : newlyFinishedVertices) {
            result.setDistance(vertex, reachableDistance.get(vertex));
            result.setPredecessor(vertex, reachablePredecessor.get(vertex));
        }

        return newlyFinishedVertices;
    }

    /**
     * Computes new frontier: finished vertices that have edges to unfinished vertices
     */
    private Set<Vertex> computeNewFrontier(Graph graph, Set<Vertex> finished) {
        Set<Vertex> newFrontierVertices = new HashSet<>();

        for (Vertex finishedVertex : finished) {
            for (Map.Entry<Vertex, Double> finishedNeighborEntry :
                    graph.getNeighbors(finishedVertex).entrySet()) {

                Vertex finishedNeighbor = finishedNeighborEntry.getKey();
                if (!finished.contains(finishedNeighbor)) {
                    newFrontierVertices.add(finishedVertex);
                    break;
                }
            }
        }

        return newFrontierVertices;
    }

    /**
     * Recursive exploration for remaining unfinished vertices
     */
    private void performRecursiveExploration(
            Graph graph,
            Set<Vertex> frontier,
            DistancePredecessorMap result,
            Set<Vertex> finished,
            Set<Vertex> remaining) {

        if (remaining.isEmpty()) {
            return;
        }

        Set<Vertex> newFinished = performBoundedRelaxation(graph, frontier, result);
        finished.addAll(newFinished);
        remaining.removeAll(newFinished);

        if (!remaining.isEmpty()) {
            Set<Vertex> newFrontier = computeNewFrontier(graph, finished);
            if (!newFrontier.isEmpty()) {
                performRecursiveExploration(graph, newFrontier, result, finished, remaining);
            }
        }
    }
}
