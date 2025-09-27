package br.com.unifal.tcc.solvers;

import br.com.unifal.tcc.algorithms.DijkstraPqAlgorithm;
import br.com.unifal.tcc.algorithms.SSSPAlgorithm;
import br.com.unifal.tcc.algorithms.interfaces.ShortestPathAlgorithm;
import br.com.unifal.tcc.fixtures.model.graph.GraphFixture;
import br.com.unifal.tcc.model.graph.Graph;
import br.com.unifal.tcc.model.graph.Vertex;
import br.com.unifal.tcc.model.results.CPPSolution;
import java.util.Set;

public class CPPSolverTest {

  public static void main(String[] args) {
    Graph graph = GraphFixture.getGraphFixture();

    ShortestPathAlgorithm ssspAlgorithm = new SSSPAlgorithm();
    ShortestPathAlgorithm dijkstraPqAlgorithm = new DijkstraPqAlgorithm();

    CPPSolver cppSolverSssp = new CPPSolver(graph, ssspAlgorithm);
    CPPSolver cppSolverDijkstra = new CPPSolver(graph, dijkstraPqAlgorithm);

    Set<Vertex> vertexSet = graph.getVerticesSet();
    int totalTests = vertexSet.size();
    int testCount = 0;

    System.out.printf("Starting %d tests:\n", totalTests);

    for (Vertex source : vertexSet) {
      testCount++;

      CPPSolution cppSolutionSssp = cppSolverSssp.solve(source);
      CPPSolution cppSolutionDijkstra = cppSolverDijkstra.solve(source);

      // Calculate and print progress percentage
      double progress = (testCount * 100.0) / totalTests;
      System.out.printf("Progress: %.2f%%\r", progress);

      if (!cppSolutionSssp.equals(cppSolutionDijkstra)) {
        System.out.printf(
            "Source ID: %s | SSSP cost: %f | Dijkstra cost: %f\n",
            source.getId(), cppSolutionSssp.totalCost(), cppSolutionDijkstra.totalCost());
      }
    }

    System.out.println("\nAll tests completed.");
  }
}
