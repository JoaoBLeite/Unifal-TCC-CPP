package br.com.unifal.tcc.algorithms;

import br.com.unifal.tcc.algorithms.interfaces.ShortestPathAlgorithm;
import br.com.unifal.tcc.algorithms.dto.DistancePredecessorMap;
import br.com.unifal.tcc.model.graph.Graph;
import br.com.unifal.tcc.model.graph.Vertex;
import br.com.unifal.tcc.model.dto.PathResult;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DijkstraListAlgorithm implements ShortestPathAlgorithm {

  @Override
  public String getName() {
    return "Dijkstra-List-Algorithm";
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
   * Runs Dijkstra's algorithm using a regular list (linear search for minimum). Complexity: O(V^2 +
   * E).
   */
  private DistancePredecessorMap computeDistancePredecessorMap(Graph graph, Vertex source) {
    DistancePredecessorMap result = new DistancePredecessorMap();
    Set<Vertex> visited = new HashSet<>();

    // Initialize source
    result.setDistance(source, 0.0);
    result.setPredecessor(source, null);

    while (visited.size() < graph.getVerticesSet().size()) {
      Vertex currentVertex = getClosestUnvisitedVertex(result, visited, graph);

      if (currentVertex == null) {
        break; // Remaining vertices are unreachable
      }

      if (visited.contains(currentVertex)) {
        continue;
      }

      visited.add(currentVertex);

      // Explore neighbors
      Map<Vertex, Double> neighbors = graph.getNeighbors(currentVertex);
      neighbors.forEach(
          (neighbor, cost) -> {
            if (visited.contains(neighbor)) {
              return;
            }

            double newDistance = result.getDistance(currentVertex) + cost;

            if (newDistance < result.getDistance(neighbor)) {
              result.setDistance(neighbor, newDistance);
              result.setPredecessor(neighbor, currentVertex);
            }
          });
    }

    return result;
  }

  /** Returns the unvisited vertex with the smallest tentative distance. */
  private Vertex getClosestUnvisitedVertex(
      DistancePredecessorMap result, Set<Vertex> visited, Graph graph) {

    Vertex closest = null;
    double minDistance = Double.POSITIVE_INFINITY;

    for (Vertex vertex : graph.getVerticesSet()) {
      if (!visited.contains(vertex)) {
        double distance = result.getDistance(vertex);
        if (distance < minDistance) {
          minDistance = distance;
          closest = vertex;
        }
      }
    }

    return closest;
  }
}
