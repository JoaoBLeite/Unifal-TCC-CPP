package br.com.unifal.tcc.model.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class Graph {

  private final Map<String, Vertex> vertices; // TODO: Check need for Map instead of Set
  private final Set<Edge> edges;

  public Graph() {
    this.vertices = new HashMap<>();
    this.edges = new HashSet<>();
  }

  /**
   * Adds an edge to the graph.
   *
   * <p>If the source or target vertices do not exist in the graph, they are added. Updates the
   * degree of the vertices.
   *
   * @param edge the edge to add
   * @complexity O(1) amortized for vertex lookup and O(1) to add to the edges set → overall O(1)
   *     amortized
   */
  public void addEdge(Edge edge) {
    addVertex(edge.getSource(), -1);
    addVertex(edge.getTarget(), 1);
    edges.add(edge);
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

  /**
   * Returns a set containing all vertices in the graph.
   *
   * <p>This method creates a new {@link HashSet} containing all vertex objects stored in the graph.
   * Modifications to the returned set do not affect the original graph.
   *
   * @return a new {@link Set} containing all vertices in the graph
   * @complexity O(V), where V is the number of vertices in the graph, since each vertex is copied
   *     into the new set
   */
  public Set<Vertex> getVerticesSet() {
    return new HashSet<>(vertices.values());
  }

  /**
   * Retrieves the neighboring vertices of a given vertex along with the minimum edge weight for
   * each connection.
   *
   * <p>If multiple edges exist between the same pair of vertices, only the edge with the smallest
   * weight is included in the result.
   *
   * @param vertex the vertex whose neighbors are to be retrieved
   * @return a map where the keys are the neighboring vertices and the values are the corresponding
   *     minimum edge weights
   */
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
        });
    return neighbors;
  }

  /**
   * Returns all vertices that are unbalanced.
   *
   * <p>A vertex is unbalanced if it does not satisfy the Eulerian condition according to {@link
   * Vertex#isNotBalanced()}.
   *
   * @return a set of unbalanced vertices
   * @complexity O(v) – iterates through all vertices
   */
  public Set<Vertex> getUnbalancedVertices() {
    return vertices.values().stream().filter(Vertex::isNotBalanced).collect(Collectors.toSet());
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
}
