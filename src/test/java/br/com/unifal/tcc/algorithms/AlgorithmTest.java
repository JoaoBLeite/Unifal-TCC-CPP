package br.com.unifal.tcc.algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.unifal.tcc.algorithms.interfaces.ShortestPathAlgorithm;
import br.com.unifal.tcc.fixtures.model.graph.GraphFixture;
import br.com.unifal.tcc.model.graph.Graph;
import br.com.unifal.tcc.model.graph.Vertex;
import br.com.unifal.tcc.model.dto.PathResult;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AlgorithmTest {

  private static ShortestPathAlgorithm ssspAlgorithm;
  private static ShortestPathAlgorithm dijkstraPqAlgorithm;
  private static ShortestPathAlgorithm dijkstraListAlgorithm;

  private Graph graph;

  @BeforeAll
  static void setupAll() {
    ssspAlgorithm = new SSSPAlgorithm();
    dijkstraPqAlgorithm = new DijkstraPqAlgorithm();
    dijkstraListAlgorithm = new DijkstraListAlgorithm();
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
          PathResult dijkstraPqResult = dijkstraPqAlgorithm.findShortestPath(graph, source, target);
          PathResult dijkstraListResult =
              dijkstraPqAlgorithm.findShortestPath(graph, source, target);

          Set<PathResult> results = new HashSet<>();
          results.add(ssspResult);
          results.add(dijkstraPqResult);
          results.add(dijkstraListResult);

          assertEquals(
              1,
              results.size(),
              () ->
                  String.format(
                      "Mismatch! Source: %s | Target: %s | SSSP cost: %f | Dijkstra Pq cost: %f | Dijkstra List cost: %f",
                      source.getId(),
                      target.getId(),
                      ssspResult.cost(),
                      dijkstraPqResult.cost(),
                      dijkstraListResult.cost()));
        }
      }
    }
  }

  @Test
  void givenSsspAndDijkstraAlgorithm_whenGetDistanceMap_thenMustReturnSameMap() {
    Set<Vertex> vertexSet = graph.getVerticesSet();

    for (Vertex source : vertexSet) {
      Map<Vertex, Double> ssspMap = ssspAlgorithm.getDistanceMap(graph, source);
      Map<Vertex, Double> dijkstraPqMap = dijkstraPqAlgorithm.getDistanceMap(graph, source);
      Map<Vertex, Double> dijkstraListMap = dijkstraPqAlgorithm.getDistanceMap(graph, source);

      Set<Map<Vertex, Double>> results = new HashSet<>();
      results.add(ssspMap);
      results.add(dijkstraPqMap);
      results.add(dijkstraListMap);

      assertEquals(1, results.size(), () -> String.format("Mismatch! Source: %s ", source.getId()));
    }
  }
}
