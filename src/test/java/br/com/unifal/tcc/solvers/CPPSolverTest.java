package br.com.unifal.tcc.solvers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.unifal.tcc.algorithms.DijkstraListAlgorithm;
import br.com.unifal.tcc.algorithms.DijkstraPqAlgorithm;
import br.com.unifal.tcc.algorithms.SSSPAlgorithm;
import br.com.unifal.tcc.fixtures.model.graph.GraphFixture;
import br.com.unifal.tcc.model.graph.Graph;
import br.com.unifal.tcc.model.graph.Vertex;
import br.com.unifal.tcc.model.dto.CPPSolution;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CPPSolverTest {

  private CPPSolver cppSolverSsspAlgorithm;
  private CPPSolver cppSolverDijkstraPqAlgorithm;
  private CPPSolver cppSolverDijkstraListAlgorithm;
  private Graph graph;

  @BeforeEach
  void setupEach() {
    graph = GraphFixture.getGraphFixture();
    cppSolverSsspAlgorithm = new CPPSolver(graph, new SSSPAlgorithm());
    cppSolverDijkstraPqAlgorithm = new CPPSolver(graph, new DijkstraPqAlgorithm());
    cppSolverDijkstraListAlgorithm = new CPPSolver(graph, new DijkstraListAlgorithm());
  }

  @Test
  void givenCppSolverWithSsspAndAnotherWithDijkstra_whenFindCppSolution_thenMustReturnSameResult() {
    Set<Vertex> vertexSet = graph.getVerticesSet();
    for (Vertex source : vertexSet) {
      CPPSolution cppSsspSolution = cppSolverSsspAlgorithm.solve(source);
      CPPSolution cppDijkstraPqSolution = cppSolverDijkstraPqAlgorithm.solve(source);
      CPPSolution cppDijkstraListSolution = cppSolverDijkstraListAlgorithm.solve(source);

      Set<CPPSolution> results = new HashSet<>();
      results.add(cppSsspSolution);
      results.add(cppDijkstraPqSolution);
      results.add(cppDijkstraListSolution);

      assertEquals(
          1,
          results.size(),
          () ->
              String.format(
                  "Mismatch! Source: %s | SSSP cost: %f | Dijkstra Pq cost: %f | Dijkstra List cost: %f",
                  source.getId(),
                  cppSsspSolution.totalCost(),
                  cppDijkstraPqSolution.totalCost(),
                  cppDijkstraListSolution.totalCost()));
    }
  }
}
