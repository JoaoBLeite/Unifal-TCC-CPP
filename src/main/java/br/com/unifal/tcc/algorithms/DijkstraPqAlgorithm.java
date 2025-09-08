package br.com.unifal.tcc.algorithms;

import br.com.unifal.tcc.algorithms.interfaces.ShortestPathAlgorithm;
import br.com.unifal.tcc.model.graph.Graph;
import br.com.unifal.tcc.model.graph.Vertex;
import br.com.unifal.tcc.model.results.PathResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

public class DijkstraPqAlgorithm implements ShortestPathAlgorithm {

  @Override
  public String getName() {
    return "Dijkstra-PriorityQueue-Algorithm";
  }

  @Override
  public PathResult findShortestPath(Graph graph, Vertex start, Vertex end) {
    Map<Vertex, Double> distances = new HashMap<>();
    Map<Vertex, Vertex> previous = new HashMap<>();
    PriorityQueue<VertexDistance> pq =
        new PriorityQueue<>(Comparator.comparingDouble(VertexDistance::distance));
    Set<Vertex> visited = new HashSet<>();

    // Initialize distances to infinity for all vertices except start
    for (Vertex vertex : graph.getVertices().values()) {
      distances.put(vertex, vertex.equals(start) ? 0.0 : Double.POSITIVE_INFINITY);
    }
    pq.offer(new VertexDistance(start, 0.0));

    while (!pq.isEmpty()) {
      VertexDistance current = pq.poll();
      Vertex currentVertex = current.vertex;

      // Skip if we've processed this vertex
      if (visited.contains(currentVertex)) {
        continue;
      }

      // Check if we've reached the target vertex
      if (currentVertex.equals(end)) {
        break;
      }

      visited.add(currentVertex);

      // Check all neighbors
      Map<Vertex, Double> neighborsDistances = graph.getNeighbors(currentVertex);
      neighborsDistances.forEach(
          (neighbor, cost) -> {
            if (visited.contains(neighbor)) {
              return;
            }

            double newDistance = distances.get(currentVertex) + cost;

            // If we found a shorter path, update it
            if (newDistance < distances.get(neighbor)) {
              distances.put(neighbor, newDistance);
              previous.put(neighbor, currentVertex);
              pq.offer(new VertexDistance(neighbor, newDistance));
            }
          });
    }

    // Check if end vertex is reachable
    if (distances.get(end) == Double.POSITIVE_INFINITY) {
      throw new RuntimeException(
          String.format("Target vertex with id: %s cannot be reachable!", end.getId()));
    }

    // Reconstruct the path from end to start
    List<Vertex> path = reconstructPath(previous, start, end);
    double totalCost = distances.get(end);

    return new PathResult(path, totalCost);
  }

  /** Reconstructs the shortest path from start to end using the previous vertex map. */
  private List<Vertex> reconstructPath(Map<Vertex, Vertex> previous, Vertex start, Vertex end) {
    List<Vertex> path = new ArrayList<>();
    Vertex current = end;

    // Build the path backwards from end to start
    while (Objects.nonNull(current)) {
      path.add(current);
      current = previous.get(current);
    }

    // Reverse to get the path from start to end
    Collections.reverse(path);

    // Verify that the path actually starts with the start vertex
    if (!path.isEmpty() && path.get(0).equals(start)) {
      return path;
    } else {
      // No valid path found
      return Collections.emptyList();
    }
  }

  /** Helper record to store vertex-distance pairs for the priority queue. */
  private record VertexDistance(Vertex vertex, double distance) {}
}
