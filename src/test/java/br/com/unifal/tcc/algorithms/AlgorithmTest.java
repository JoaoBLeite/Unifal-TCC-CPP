package br.com.unifal.tcc.algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.unifal.tcc.algorithms.interfaces.ShortestPathAlgorithm;
import br.com.unifal.tcc.fixtures.model.graph.GraphFixture;
import br.com.unifal.tcc.model.graph.Graph;
import br.com.unifal.tcc.model.graph.Vertex;
import br.com.unifal.tcc.model.results.PathResult;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AlgorithmTest {

  private static ShortestPathAlgorithm ssspAlgorithm;
  private static ShortestPathAlgorithm dijkstraPqAlgorithm;

  private Graph graph;

  @BeforeAll
  static void setupAll() {
    ssspAlgorithm = new SSSPAlgorithm();
    dijkstraPqAlgorithm = new DijkstraPqAlgorithm();
  }

  @BeforeEach
  void setupEach() {
    graph = GraphFixture.getGraphFixture();
  }

  @Test
  void givenSsspAndDijkstraAlgorithm_whenSearchShortestPath_thenMustReturnSamePath() {
    Set<Vertex> vertexSet = graph.getVerticesSet();

    for (Vertex source : vertexSet) {
      for (Vertex target : vertexSet) {
        if (!source.equals(target)) {
          PathResult ssspResult = ssspAlgorithm.findShortestPath(graph, source, target);
          PathResult dijkstraResult = dijkstraPqAlgorithm.findShortestPath(graph, source, target);

          assertEquals(
              dijkstraResult,
              ssspResult,
              () ->
                  String.format(
                      "Mismatch! Source: %s | Target: %s | SSSP cost: %f | Dijkstra cost: %f",
                      source.getId(), target.getId(), ssspResult.cost(), dijkstraResult.cost()));
        }
      }
    }
  }
}
