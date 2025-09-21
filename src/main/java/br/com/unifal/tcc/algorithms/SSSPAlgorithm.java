package br.com.unifal.tcc.algorithms;

import br.com.unifal.tcc.algorithms.interfaces.ShortestPathAlgorithm;
import br.com.unifal.tcc.exceptions.UnreachableVertexException;
import br.com.unifal.tcc.model.graph.Graph;
import br.com.unifal.tcc.model.graph.Vertex;
import br.com.unifal.tcc.model.results.PathResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SSSPAlgorithm implements ShortestPathAlgorithm {

  @Override
  public String getName() {
    return "SSSP-Algorithm";
  }

  @Override
  public PathResult findShortestPath(Graph graph, Vertex start, Vertex end) {
    int vertexCount = graph.getVertexCount();
    ShortestPathResult result = new ShortestPathResult(vertexCount);

    // Initialize source
    result.setDistance(start, 0.0);
    result.setPredecessor(start, null);

    // Set of finished vertices
    Set<Vertex> finished = new HashSet<>();
    finished.add(start);

    // Initial frontier
    Set<Vertex> frontier = new HashSet<>();
    frontier.add(start);

    // Calculate K based on graph (adaptive parameter)
    int k = 2;

    // Main loop
    while (!frontier.isEmpty()) {
      Set<Vertex> newlyFinishedVertices =
          performBoundedRelaxation(graph, frontier, result, k, finished);

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
          performRecursiveExploration(graph, frontier, result, 2, finished, remaining);
        }
      }

      int debug = 1 + 2;
    } // end main loop

    return new PathResult(result.getPath(end), result.getCostTo(end));
  }

  private Set<Vertex> performBoundedRelaxation(
      Graph graph, Set<Vertex> frontier, ShortestPathResult result, int k, Set<Vertex> finished) {

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

    // Perform k rounds of relaxation
    for (int round = 0; round < k; round++) {

      // Relax edges from all vertices discovered so far
      Set<Vertex> activeVerticesForRelaxation = new HashSet<>(frontier);
      for (Vertex vertex : reachableDistance.keySet()) {
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

  /** Computes new frontier: finished vertices that have edges to unfinished vertices */
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

  /** Recursive exploration for remaining unfinished vertices */
  private void performRecursiveExploration(
      Graph graph,
      Set<Vertex> frontier,
      ShortestPathResult result,
      int k,
      Set<Vertex> finished,
      Set<Vertex> remaining) {

    if (remaining.isEmpty() || k <= 0) {
      return;
    }

    Set<Vertex> newFinished = performBoundedRelaxation(graph, frontier, result, k, finished);
    finished.addAll(newFinished);
    remaining.removeAll(newFinished);

    if (!remaining.isEmpty()) {
      Set<Vertex> newFrontier = computeNewFrontier(graph, finished);
      if (!newFrontier.isEmpty()) {
        performRecursiveExploration(graph, newFrontier, result, k, finished, remaining);
      }
    }
  }

  private class ShortestPathResult {
    private HashMap<Vertex, Double> distances;
    private HashMap<Vertex, Vertex> predecessors;

    public ShortestPathResult(int n) {
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

    public double getCostTo(Vertex target) {
      if (getDistance(target) == Double.POSITIVE_INFINITY) {
        throw new UnreachableVertexException(
            String.format(
                "Target vertex with id: %s is not reachable from start vertex!", target.getId()));
      }
      return distances.get(target);
    }
  }
}
