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

    int totalVertices = vertexSet.size();
    int totalComparisons = totalVertices * (totalVertices - 1);
    int comparisonCount = 0;

    System.out.println(String.format("Starting %d tests:", totalComparisons));

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

    System.out.println("\nAll tests completed.");
  }

  private static Graph createGraph() {
    Graph graph = new Graph(true);
    // Original edges
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
    graph.addEdge(new RealEdge(new Vertex("H"), new Vertex("F"), 14d));
    graph.addEdge(new RealEdge(new Vertex("G"), new Vertex("F"), 15d));

    // New vertices J, K, L, M, N, O, P, Q, R, S with edges to ensure strong connectivity
    graph.addEdge(new RealEdge(new Vertex("A"), new Vertex("J"), 16d));
    graph.addEdge(new RealEdge(new Vertex("J"), new Vertex("K"), 17d));
    graph.addEdge(new RealEdge(new Vertex("K"), new Vertex("L"), 18d));
    graph.addEdge(new RealEdge(new Vertex("L"), new Vertex("M"), 19d));
    graph.addEdge(new RealEdge(new Vertex("M"), new Vertex("N"), 20d));
    graph.addEdge(new RealEdge(new Vertex("N"), new Vertex("O"), 21d));
    graph.addEdge(new RealEdge(new Vertex("O"), new Vertex("P"), 22d));
    graph.addEdge(new RealEdge(new Vertex("P"), new Vertex("Q"), 23d));
    graph.addEdge(new RealEdge(new Vertex("Q"), new Vertex("R"), 24d));
    graph.addEdge(new RealEdge(new Vertex("R"), new Vertex("S"), 25d));

    // Connecting new vertices back to original vertices to ensure strong connectivity
    graph.addEdge(new RealEdge(new Vertex("S"), new Vertex("A"), 26d)); // Close main cycle
    graph.addEdge(new RealEdge(new Vertex("K"), new Vertex("D"), 27d)); // Connect K to D
    graph.addEdge(new RealEdge(new Vertex("F"), new Vertex("L"), 28d)); // Connect F to L
    graph.addEdge(new RealEdge(new Vertex("M"), new Vertex("H"), 29d)); // Connect M to H
    graph.addEdge(new RealEdge(new Vertex("I"), new Vertex("N"), 30d)); // Connect I to N
    graph.addEdge(new RealEdge(new Vertex("O"), new Vertex("E"), 31d)); // Connect O to E
    graph.addEdge(new RealEdge(new Vertex("G"), new Vertex("P"), 32d)); // Connect G to P
    graph.addEdge(new RealEdge(new Vertex("Q"), new Vertex("C"), 33d)); // Connect Q to C
    graph.addEdge(new RealEdge(new Vertex("B"), new Vertex("R"), 34d)); // Connect B to R
    graph.addEdge(new RealEdge(new Vertex("S"), new Vertex("J"), 35d)); // Additional connection

    // Additional cross-connections to strengthen connectivity
    graph.addEdge(new RealEdge(new Vertex("D"), new Vertex("K"), 36d)); // Reverse of K->D
    graph.addEdge(new RealEdge(new Vertex("L"), new Vertex("F"), 37d)); // Reverse of F->L
    graph.addEdge(new RealEdge(new Vertex("H"), new Vertex("M"), 38d)); // Reverse of M->H
    graph.addEdge(new RealEdge(new Vertex("N"), new Vertex("I"), 39d)); // Reverse of I->N
    graph.addEdge(new RealEdge(new Vertex("E"), new Vertex("O"), 40d)); // Reverse of O->E
    graph.addEdge(new RealEdge(new Vertex("P"), new Vertex("G"), 41d)); // Reverse of G->P
    graph.addEdge(new RealEdge(new Vertex("C"), new Vertex("Q"), 42d)); // Reverse of Q->C
    graph.addEdge(new RealEdge(new Vertex("R"), new Vertex("B"), 43d)); // Reverse of B->R

    // Additional interconnections for robustness
    graph.addEdge(new RealEdge(new Vertex("J"), new Vertex("S"), 44d)); // J to S
    graph.addEdge(new RealEdge(new Vertex("L"), new Vertex("P"), 45d)); // L to P
    graph.addEdge(new RealEdge(new Vertex("N"), new Vertex("R"), 46d)); // N to R
    graph.addEdge(new RealEdge(new Vertex("K"), new Vertex("O"), 47d)); // K to O
    graph.addEdge(new RealEdge(new Vertex("M"), new Vertex("Q"), 48d)); // M to Q
    return graph;
  }
}
