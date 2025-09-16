package br.com.unifal.tcc.algorithms.interfaces;

import br.com.unifal.tcc.exceptions.UnreachableVertexException;
import br.com.unifal.tcc.model.graph.Graph;
import br.com.unifal.tcc.model.graph.Vertex;
import br.com.unifal.tcc.model.results.PathResult;

public interface ShortestPathAlgorithm {
  /**
   * Returns the name of the shortest path algorithm.
   *
   * <p>This can be used for identification in reports, logs, or user interfaces.
   *
   * @return the algorithm name (e.g.: "Dijkstra")
   */
  String getName();

  /**
   * Computes the shortest path between two vertices in a graph.
   *
   * @param graph the graph in which the path should be searched
   * @param start the source vertex
   * @param end the target vertex
   * @return a {@link PathResult} object containing details of the shortest path, such as the
   *     sequence of vertices and total cost
   * @throws UnreachableVertexException if no path exists between {@code start} and {@code end}
   */
  PathResult findShortestPath(Graph graph, Vertex start, Vertex end);
}
