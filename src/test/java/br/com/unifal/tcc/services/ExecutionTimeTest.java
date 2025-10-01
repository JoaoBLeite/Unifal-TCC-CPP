package br.com.unifal.tcc.services;

import static br.com.unifal.tcc.services.ExecutionTime.measureTimeWithResult;
import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.unifal.tcc.algorithms.DijkstraListAlgorithm;
import br.com.unifal.tcc.algorithms.DijkstraPqAlgorithm;
import br.com.unifal.tcc.algorithms.SSSPAlgorithm;
import br.com.unifal.tcc.algorithms.interfaces.ShortestPathAlgorithm;
import br.com.unifal.tcc.fixtures.model.graph.GraphFixture;
import br.com.unifal.tcc.model.graph.Graph;
import br.com.unifal.tcc.model.graph.Vertex;
import br.com.unifal.tcc.services.dto.TimedResult;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExecutionTimeTest {

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
  void givenAllShortestPathAlgorithms_whenCalculateExecutionTime_thenValidateDuration() {
    Set<Vertex> vertexSet = graph.getVerticesSet();

    for (Vertex source : vertexSet) {

      var timedSsspMap = measureTimeWithResult(() -> ssspAlgorithm.getDistanceMap(graph, source));
      var timedDijkstraPqMap =
          measureTimeWithResult(() -> dijkstraPqAlgorithm.getDistanceMap(graph, source));
      var timedDijkstraListMap =
          measureTimeWithResult(() -> dijkstraListAlgorithm.getDistanceMap(graph, source));

      // Guarantee same result
      Set<Map<Vertex, Double>> results = new HashSet<>();
      results.add(timedSsspMap.result());
      results.add(timedDijkstraPqMap.result());
      results.add(timedDijkstraListMap.result());

      assertEquals(1, results.size(), () -> String.format("Mismatch! Source: %s ", source.getId()));

      // Check duration order
      var algorithmDuration =
          Map.of(
              ssspAlgorithm,
              timedSsspMap,
              dijkstraPqAlgorithm,
              timedDijkstraPqMap,
              dijkstraListAlgorithm,
              timedDijkstraListMap);

      // Print result
      algorithmDuration.entrySet().stream()
          .sorted(Map.Entry.comparingByValue(Comparator.comparing(TimedResult::duration)))
          .toList()
          .forEach(
              entry ->
                  System.out.printf(
                      "%s, duration: %d ns\n",
                      entry.getKey().getName(), entry.getValue().duration().toNanos()));
    }
  }
}
