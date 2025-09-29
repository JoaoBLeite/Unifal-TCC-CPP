package br.com.unifal.tcc.algorithms;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import br.com.unifal.tcc.algorithms.interfaces.ShortestPathAlgorithm;
import br.com.unifal.tcc.algorithms.dto.DistancePredecessorMap;
import br.com.unifal.tcc.model.graph.Graph;
import br.com.unifal.tcc.model.graph.Vertex;
import br.com.unifal.tcc.model.dto.PathResult;

public class DijkstraPqAlgorithm implements ShortestPathAlgorithm {

    @Override
    public String getName() {
        return "Dijkstra-PriorityQueue-Algorithm";
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
     * Runs Dijkstra's algorithm and builds a {@link DistancePredecessorMap} from the given source.
     */
    private DistancePredecessorMap computeDistancePredecessorMap(Graph graph, Vertex source) {
        DistancePredecessorMap result = new DistancePredecessorMap();

        PriorityQueue<VertexDistance> pq =
                new PriorityQueue<>(Comparator.comparingDouble(VertexDistance::distance));
        Set<Vertex> visited = new HashSet<>();

        // Initialize source
        result.setDistance(source, 0.0);
        result.setPredecessor(source, null);

        // Initialize distances
        pq.offer(new VertexDistance(source, result.getDistance(source)));

        while (!pq.isEmpty()) {
            VertexDistance current = pq.poll();
            Vertex currentVertex = current.vertex;

            if (visited.contains(currentVertex)) {
                continue;
            }

            visited.add(currentVertex);

            // Explore neighbors
            Map<Vertex, Double> neighbors = graph.getNeighbors(currentVertex);
            neighbors.forEach((neighbor, cost) -> {
                if (visited.contains(neighbor)) {
                    return;
                }

                double newDistance = result.getDistance(currentVertex) + cost;

                if (newDistance < result.getDistance(neighbor)) {
                    result.setDistance(neighbor, newDistance);
                    result.setPredecessor(neighbor, currentVertex);
                    pq.offer(new VertexDistance(neighbor, newDistance));
                }
            });
        }

        return result;
    }

    /**
     * Helper record to store vertex-distance pairs for the priority queue.
     */
    private record VertexDistance(Vertex vertex, double distance) {
    }
}
