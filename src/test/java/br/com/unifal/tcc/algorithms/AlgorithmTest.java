package br.com.unifal.tcc.algorithms;

import br.com.unifal.tcc.algorithms.interfaces.ShortestPathAlgorithm;
import br.com.unifal.tcc.fixtures.model.graph.GraphFixture;
import br.com.unifal.tcc.model.graph.Graph;
import br.com.unifal.tcc.model.graph.Vertex;
import br.com.unifal.tcc.model.results.PathResult;
import java.util.Set;

public class AlgorithmTest {
  private static final ShortestPathAlgorithm ssspAlgorithm = new SSSPAlgorithm();
  private static final ShortestPathAlgorithm dijkstraPqAlgorithm = new DijkstraPqAlgorithm();

  public static void main(String[] args) {
    Graph graph = GraphFixture.getGraphFixture();
    Set<Vertex> vertexSet = graph.getVerticesSet();

    int totalVertices = vertexSet.size();
    int totalComparisons = totalVertices * (totalVertices - 1);
    int comparisonCount = 0;

    System.out.printf("Starting %d tests:\n", totalComparisons);

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
            System.out.printf(
                "Source ID: %s | Target ID: %s | SSSP cost: %f | Dijkstra cost: %f\n",
                source.getId(), target.getId(), ssspResult.cost(), dijkstraResult.cost());
          }
        }
      }
    }

    System.out.println("\nAll tests completed.");
  }
}
