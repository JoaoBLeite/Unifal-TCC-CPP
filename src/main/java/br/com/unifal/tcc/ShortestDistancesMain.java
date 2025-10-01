//package br.com.unifal.tcc;
//
//import static br.com.unifal.tcc.services.ExecutionTime.measureTime;
//
//import br.com.unifal.tcc.algorithms.DijkstraListAlgorithm;
//import br.com.unifal.tcc.algorithms.DijkstraPqAlgorithm;
//import br.com.unifal.tcc.algorithms.SSSPAlgorithm;
//import br.com.unifal.tcc.algorithms.interfaces.ShortestPathAlgorithm;
//import br.com.unifal.tcc.model.graph.Graph;
//import br.com.unifal.tcc.model.graph.Vertex;
//import br.com.unifal.tcc.services.InputService;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//public class ShortestDistancesMain {
//
//  public static void main(String[] args) {
//    // Load graph from parameters
//    String graphFile = args[0];
//    Graph graph = InputService.loadGraphFromFile(graphFile);
//
//    // Initiate algorithms
//    ShortestPathAlgorithm dijkstraPq = new DijkstraPqAlgorithm();
//    ShortestPathAlgorithm dijkstraList = new DijkstraListAlgorithm();
//    ShortestPathAlgorithm sssp = new SSSPAlgorithm();
//
//    // Algorithms score
//    Map<ShortestPathAlgorithm, Long> tierA = new HashMap<>();
//    tierA.put(dijkstraList, 0L);
//    tierA.put(dijkstraPq, 0L);
//    tierA.put(sssp, 0L);
//
//    Map<ShortestPathAlgorithm, Long> tierB = new HashMap<>();
//    tierB.put(dijkstraList, 0L);
//    tierB.put(dijkstraPq, 0L);
//    tierB.put(sssp, 0L);
//
//    Map<ShortestPathAlgorithm, Long> tierC = new HashMap<>();
//    tierC.put(dijkstraList, 0L);
//    tierC.put(dijkstraPq, 0L);
//    tierC.put(sssp, 0L);
//
//    // Run algorithms
//    Set<Vertex> verticesSet = graph.getVerticesSet();
//    long verticesAmount = verticesSet.size();
//    long vertexCount = 0l;
//
//    for (Vertex source : verticesSet) {
//
//      var dijkstraPqDuration = measureTime(() -> dijkstraPq.getDistanceMap(graph, source));
//      var dijkstraListDuration = measureTime(() -> dijkstraList.getDistanceMap(graph, source));
//      var ssspDuration = measureTime(() -> sssp.getDistanceMap(graph, source));
//
//      // Update score
//      var algorithmsDuration =
//          Map.of(
//              dijkstraPq,
//              dijkstraPqDuration,
//              dijkstraList,
//              dijkstraListDuration,
//              sssp,
//              ssspDuration);
//      var sortedDurations =
//          algorithmsDuration.entrySet().stream().sorted(Map.Entry.comparingByValue()).toList();
//
//      var fastestAlgorithm = sortedDurations.get(0);
//      tierA.put(fastestAlgorithm.getKey(), tierA.get(fastestAlgorithm.getKey()) + 1);
//      var regularAlgorithm = sortedDurations.get(1);
//      tierB.put(regularAlgorithm.getKey(), tierB.get(regularAlgorithm.getKey()) + 1);
//      var slowestAlgorithm = sortedDurations.get(2);
//      tierC.put(slowestAlgorithm.getKey(), tierC.get(slowestAlgorithm.getKey()) + 1);
//
//      // Calculate and print progress percentage
//      double progress = (++vertexCount * 100.0) / verticesAmount;
//      System.out.printf("Progress: %.2f%%\r", progress);
//    } // end for
//
//    for (ShortestPathAlgorithm alg : List.of(dijkstraList, dijkstraPq, sssp)) {
//      System.out.println(alg.getName());
//      System.out.println("  Fastest: " + tierA.get(alg));
//      System.out.println("  Regular: " + tierB.get(alg));
//      System.out.println("  Slowest: " + tierC.get(alg));
//      System.out.println();
//    }
//  }
//}
