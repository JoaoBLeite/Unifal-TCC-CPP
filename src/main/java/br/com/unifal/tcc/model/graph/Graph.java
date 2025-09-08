package br.com.unifal.tcc.model.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class Graph {

  private final Map<String, Vertex> vertices;
  private final Set<Edge> edges;
  private final boolean isDirected;

  public Graph(boolean isDirected) {
    this.isDirected = isDirected;
    this.vertices = new HashMap<>();
    this.edges = new HashSet<>();
  }

  /**
   * Adds an edge to the graph.
   *
   * <p>If the source or target vertices do not exist in the graph, they are added. Updates the
   * degree of the vertices according to the edge and graph type (directed or undirected).
   *
   * @param edge the edge to add
   * @complexity O(1) amortized for vertex lookup and O(1) to add to the edges set → overall O(1)
   *     amortized
   */
  public void addEdge(Edge edge) {
    addVertex(edge.getSource(), isDirected ? -1 : 1);
    addVertex(edge.getTarget(), 1);
    edges.add(edge);
  }

  // TODO: Javadoc with complexity big o annotation
  public Set<VirtualEdge> getVirtualEdges() {
    return edges.stream()
        .filter(edge -> edge instanceof VirtualEdge)
        .map(edge -> (VirtualEdge) edge)
        .collect(Collectors.toSet());
  }

  /**
   * Adds a vertex to the graph if it does not exist.
   *
   * <p>If a vertex with the same ID already exists, updates it's degree by {@code degreeChange}.
   * Otherwise, add the new vertex with the given initial degree change.
   *
   * @param vertex the vertex to add
   * @param degreeChange the change in degree to apply
   * @complexity O(1) amortized using HashMap lookup
   */
  private void addVertex(Vertex vertex, int degreeChange) {
    Vertex existing = vertices.get(vertex.getId());
    if (Objects.nonNull(existing)) {
      existing.updateDegree(degreeChange);
    } else {
      vertex.updateDegree(degreeChange);
      vertices.put(vertex.getId(), vertex);
    }
  }

  public Map<Vertex, Double> getNeighbors(Vertex vertex) {
    Map<Vertex, Double> neighbors = new HashMap<>();
    edges.forEach(
        edge -> {
          if (vertex.equals(edge.getSource())) {
            if (!neighbors.containsKey(edge.getTarget())) {
              // Add
              neighbors.put(edge.getTarget(), edge.getWeight());
            }
            if (neighbors.containsKey(edge.getTarget())
                && edge.getWeight() < neighbors.get(edge.getTarget())) {
              // Update
              neighbors.put(edge.getTarget(), edge.getWeight());
            }
          }
          if (!isDirected && vertex.equals(edge.getTarget())) {
            if (!neighbors.containsKey(edge.getSource())) {
              // Add
              neighbors.put(edge.getSource(), edge.getWeight());
            }
            if (neighbors.containsKey(edge.getSource())
                && edge.getWeight() < neighbors.get(edge.getSource())) {
              // Update
              neighbors.put(edge.getSource(), edge.getWeight());
            }
          }
        });
    return neighbors;
  }

  /**
   * Returns all vertices that are unbalanced.
   *
   * <p>A vertex is unbalanced if its degree is odd (for undirected graphs) or does not satisfy the
   * Eulerian condition (for directed graphs) according to {@link Vertex#isNotBalanced(boolean)}.
   *
   * @return a set of unbalanced vertices
   * @complexity O(v) – iterates through all vertices
   */
  public Set<Vertex> getUnbalancedVertices() {
    return vertices.values().stream()
        .filter(vertex -> vertex.isNotBalanced(isDirected))
        .collect(Collectors.toSet());
  }

  /**
   * Checks if the graph is Eulerian.
   *
   * <p>A graph is Eulerian if it has no unbalanced vertices:
   *
   * <ul>
   *   <li>Undirected: all vertices have even degree
   *   <li>Directed: in-degree equals out-degree for all vertices
   * </ul>
   *
   * @return true if the graph is Eulerian, false otherwise
   * @complexity O(e), same as {@link Graph#getUnbalancedVertices()}
   */
  public boolean isEulerian() {
    return getUnbalancedVertices().isEmpty();
  }

  /**
   * Retrieves the minimum weight of edges between two vertices.
   *
   * <p>If the graph is undirected, edges in both directions (from→to and to→from) are considered.
   * If no edge exists between the vertices, an empty {@link Optional} is returned.
   *
   * @param from the source vertex
   * @param to the target vertex
   * @return an {@link Optional} containing the minimum edge-weight if at least one edge exists, or
   *     an empty {@link Optional} if no edge is found
   */
  public Optional<Double> getEdgeWeight(Vertex from, Vertex to) {
    List<Double> weights = new ArrayList<>();
    for (Edge edge : getEdges()) {
      if (edge.getSource().equals(from) && edge.getTarget().equals(to)) {
        weights.add(edge.getWeight());
      }
      if (!isDirected() && edge.getTarget().equals(from) && edge.getSource().equals(to)) {
        weights.add(edge.getWeight());
      }
    }
    return weights.isEmpty() ? Optional.empty() : Optional.of(Collections.min(weights));
  }
}
