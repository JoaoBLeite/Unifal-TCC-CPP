package br.com.unifal.tcc.algorithms.interfaces;

import java.util.Map;
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
     * @param end   the target vertex
     * @return a {@link PathResult} object containing details of the shortest path, such as the
     * sequence of vertices and total cost
     * @throws UnreachableVertexException if no path exists between {@code start} and {@code end}
     */
    PathResult findShortestPath(Graph graph, Vertex start, Vertex end);

    /**
     * Computes the shortest distance from a given source vertex to all other reachable vertices in the graph.
     *
     * <p>The returned map contains each vertex as a key, and the value is the minimum cost (distance)
     * to reach that vertex from the specified source. The source vertex itself should have a distance
     * of {@code 0.0}. Vertices that are unreachable from the source may be absent from the map or
     * associated with {@link Double#POSITIVE_INFINITY}, depending on the implementation.
     *
     * @param graph  the graph in which the distances should be computed
     * @param source the vertex from which distances to all other vertices should be calculated
     * @return a map of vertices to their minimum distance from {@code source}
     */
    Map<Vertex, Double> getDistanceMap(Graph graph, Vertex source);
}
