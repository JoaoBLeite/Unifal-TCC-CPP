package br.com.unifal.tcc.algorithms;

import br.com.unifal.tcc.algorithms.interfaces.ShortestPathAlgorithm;
import br.com.unifal.tcc.model.graph.Graph;
import br.com.unifal.tcc.model.graph.RealEdge;
import br.com.unifal.tcc.model.graph.Vertex;
import br.com.unifal.tcc.model.results.PathResult;
import java.util.Set;

public class AlgorithmTest {
  private static final ShortestPathAlgorithm ssspAlgorithm = new SSSPAlgorithm();
  private static final ShortestPathAlgorithm dijkstraPqAlgorithm = new DijkstraPqAlgorithm();

  public static void main(String[] args) {
    Graph graph = createGraph();
    Set<Vertex> vertexSet = graph.getVerticesSet();

    // Total number of comparisons: all pairs of distinct vertices
    int totalVertices = vertexSet.size();
    int totalComparisons = totalVertices * (totalVertices - 1);
    int comparisonCount = 0;

    for (Vertex source : vertexSet) {
      for (Vertex target : vertexSet) {
        if (!source.equals(target)) {
          comparisonCount++;

          PathResult ssspResult = ssspAlgorithm.findShortestPath(graph, source, target);
          PathResult dijkstraResult = dijkstraPqAlgorithm.findShortestPath(graph, source, target);

          // Calculate and print progress percentage
          double progress = (comparisonCount * 100.0) / totalComparisons;
          System.out.printf("Progress: %.2f%%\r", progress);

          if (!ssspResult.equals(dijkstraResult)) {
            System.out.println(
                String.format(
                    "Source ID: %s | Target ID: %s | SSSP cost: %f | Dijkstra cost: %f",
                    source.getId(), target.getId(), ssspResult.cost(), dijkstraResult.cost()));
          }
        }
      }
    }

    System.out.println("\nAll comparisons completed.");
  }

  private static Graph createGraph() {
    Graph graph = new Graph(true);
    graph.addEdge(new RealEdge(new Vertex("A"), new Vertex("B"), 1d));
    graph.addEdge(new RealEdge(new Vertex("B"), new Vertex("C"), 2d));
    graph.addEdge(new RealEdge(new Vertex("C"), new Vertex("D"), 3d));
    graph.addEdge(new RealEdge(new Vertex("D"), new Vertex("E"), 4d));
    graph.addEdge(new RealEdge(new Vertex("E"), new Vertex("F"), 5d));
    graph.addEdge(new RealEdge(new Vertex("F"), new Vertex("G"), 6d));
    graph.addEdge(new RealEdge(new Vertex("G"), new Vertex("A"), 13d));
    graph.addEdge(new RealEdge(new Vertex("B"), new Vertex("G"), 7d));
    graph.addEdge(new RealEdge(new Vertex("I"), new Vertex("B"), 8d));
    graph.addEdge(new RealEdge(new Vertex("H"), new Vertex("I"), 9d));
    graph.addEdge(new RealEdge(new Vertex("C"), new Vertex("H"), 10d));
    graph.addEdge(new RealEdge(new Vertex("I"), new Vertex("C"), 11d));
    graph.addEdge(new RealEdge(new Vertex("E"), new Vertex("C"), 12d));
    return graph;
  }
}
