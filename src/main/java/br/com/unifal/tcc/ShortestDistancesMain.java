package br.com.unifal.tcc;

import static br.com.unifal.tcc.services.ExecutionTime.measureTimeWithResult;

import br.com.unifal.tcc.algorithms.DijkstraListAlgorithm;
import br.com.unifal.tcc.algorithms.DijkstraPqAlgorithm;
import br.com.unifal.tcc.algorithms.SSSPAlgorithm;
import br.com.unifal.tcc.algorithms.interfaces.ShortestPathAlgorithm;
import br.com.unifal.tcc.model.graph.Graph;
import br.com.unifal.tcc.model.graph.Vertex;
import br.com.unifal.tcc.services.InputService;
import br.com.unifal.tcc.services.dto.TimedResult;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ShortestDistancesMain {

  public static void main(String[] args) {
    // Load graph from parameters
    String graphFile = args[0];
    Graph graph = InputService.loadGraphFromFile(graphFile);

    // Initiate algorithms
    ShortestPathAlgorithm dijkstraPq = new DijkstraPqAlgorithm();
    ShortestPathAlgorithm dijkstraList = new DijkstraListAlgorithm();
    ShortestPathAlgorithm sssp = new SSSPAlgorithm();

    // Algorithms score
    Map<ShortestPathAlgorithm, Long> tierA = new HashMap<>();
    tierA.put(dijkstraList, 0L);
    tierA.put(dijkstraPq, 0L);
    tierA.put(sssp, 0L);

    Map<ShortestPathAlgorithm, Long> tierB = new HashMap<>();
    tierB.put(dijkstraList, 0L);
    tierB.put(dijkstraPq, 0L);
    tierB.put(sssp, 0L);

    Map<ShortestPathAlgorithm, Long> tierC = new HashMap<>();
    tierC.put(dijkstraList, 0L);
    tierC.put(dijkstraPq, 0L);
    tierC.put(sssp, 0L);

    // Run algorithms
    Set<Vertex> verticesSet = graph.getVerticesSet();
    long verticesAmount = verticesSet.size();
    long vertexCount = 0l;

    Instant startInstant = Instant.now();

    for (Vertex source : verticesSet) {

      CompletableFuture<TimedResult<Map<Vertex, Double>>> dijkstraPqFuture =
          CompletableFuture.supplyAsync(
              () -> measureTimeWithResult(() -> dijkstraPq.getDistanceMap(graph, source)));
      CompletableFuture<TimedResult<Map<Vertex, Double>>> dijkstraListFuture =
          CompletableFuture.supplyAsync(
              () -> measureTimeWithResult(() -> dijkstraList.getDistanceMap(graph, source)));
      CompletableFuture<TimedResult<Map<Vertex, Double>>> ssspFuture =
          CompletableFuture.supplyAsync(
              () -> measureTimeWithResult(() -> sssp.getDistanceMap(graph, source)));

      // Wait for all to complete
      CompletableFuture.allOf(dijkstraPqFuture, dijkstraListFuture, ssspFuture).join();

      // Get results
      TimedResult<Map<Vertex, Double>> dijkstraPqTimedResult = dijkstraPqFuture.join();
      TimedResult<Map<Vertex, Double>> dijkstraListTimedResult = dijkstraListFuture.join();
      TimedResult<Map<Vertex, Double>> ssspTimedResult = ssspFuture.join();

      assertEquals(
          dijkstraPqTimedResult.result(),
          dijkstraListTimedResult.result(),
          ssspTimedResult.result());

      // Update score
      Map<ShortestPathAlgorithm, Duration> algorithmsDuration =
          Map.of(
              dijkstraPq,
              dijkstraPqTimedResult.duration(),
              dijkstraList,
              dijkstraListTimedResult.duration(),
              sssp,
              ssspTimedResult.duration());
      var sortedDurations =
          algorithmsDuration.entrySet().stream().sorted(Map.Entry.comparingByValue()).toList();

      Map.Entry<ShortestPathAlgorithm, Duration> fastestAlgorithm = sortedDurations.get(0);
      tierA.put(fastestAlgorithm.getKey(), tierA.get(fastestAlgorithm.getKey()) + 1);

      Map.Entry<ShortestPathAlgorithm, Duration> regularAlgorithm = sortedDurations.get(1);
      tierB.put(regularAlgorithm.getKey(), tierB.get(regularAlgorithm.getKey()) + 1);

      Map.Entry<ShortestPathAlgorithm, Duration> slowestAlgorithm = sortedDurations.get(2);
      tierC.put(slowestAlgorithm.getKey(), tierC.get(slowestAlgorithm.getKey()) + 1);

      // Calculate and print progress percentage
      double progress = (++vertexCount * 100.0) / verticesAmount;
      System.out.printf("Progress: %.2f%%\r", progress);
    } // end for

    System.out.println("Time: " + Duration.between(startInstant, Instant.now()).toSeconds() + "s");

    for (ShortestPathAlgorithm alg : List.of(dijkstraList, dijkstraPq, sssp)) {
      System.out.println(alg.getName());
      System.out.println("  Fastest: " + tierA.get(alg));
      System.out.println("  Regular: " + tierB.get(alg));
      System.out.println("  Slowest: " + tierC.get(alg));
      System.out.println();
    }
  }

  private static void assertEquals(
      Map<Vertex, Double> result1, Map<Vertex, Double> result2, Map<Vertex, Double> result3) {
    Set<Map<Vertex, Double>> results = new HashSet<>();
    results.add(result1);
    results.add(result2);
    results.add(result3);

    if (results.size() > 1) {
      throw new RuntimeException("Mismatch!");
    }
  }
}
