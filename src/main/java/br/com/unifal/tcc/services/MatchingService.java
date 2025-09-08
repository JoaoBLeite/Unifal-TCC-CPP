package br.com.unifal.tcc.services;

import br.com.unifal.tcc.algorithms.interfaces.ShortestPathAlgorithm;
import br.com.unifal.tcc.model.dto.BalancingEdge;
import br.com.unifal.tcc.model.graph.Graph;
import br.com.unifal.tcc.model.graph.Vertex;
import br.com.unifal.tcc.model.results.PathResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MatchingService {

  private MatchingService() {}

  public static List<BalancingEdge> findMinimumCostMatchingDirGraph(
      Graph graph, ShortestPathAlgorithm pathAlgorithm) {
    Set<Vertex> unbalancedSet = graph.getUnbalancedVertices();
    List<BalancingEdge> balancingEdges = new ArrayList<>();

    List<Vertex> positiveDegree = unbalancedSet.stream().filter(v -> v.getDegree() > 0).toList();
    List<Vertex> negativeDegree = unbalancedSet.stream().filter(v -> v.getDegree() < 0).toList();

    // Track remaining degree imbalances
    Map<Vertex, Integer> positiveRemaining = new HashMap<>();
    for (Vertex v : positiveDegree) {
      positiveRemaining.put(v, v.getDegree());
    }

    Map<Vertex, Integer> negativeRemaining = new HashMap<>();
    for (Vertex v : negativeDegree) {
      negativeRemaining.put(v, Math.abs(v.getDegree()));
    }

    // Greedy approach: always match the closest unmatched pair
    // TODO: Refactoring: Maybe use something better than greedy
    while (!positiveRemaining.isEmpty() && !negativeRemaining.isEmpty()) {
      PathResult pathResult = new PathResult(List.of(), Double.POSITIVE_INFINITY);
      Vertex bestPositive = null, bestNegative = null;

      // Find the closest positive-negative pair
      for (Vertex positive : positiveRemaining.keySet()) {
        for (Vertex negative : negativeRemaining.keySet()) {
          PathResult path = pathAlgorithm.findShortestPath(graph, positive, negative);

          if (path.path().isEmpty()) {
            throw new RuntimeException(
                "No path found between unbalanced vertices "
                    + positive.getId()
                    + " and "
                    + negative.getId());
          }

          if (path.cost() < pathResult.cost()) {
            pathResult = path;
            bestPositive = positive;
            bestNegative = negative;
          }
        }
      }

      if (Objects.nonNull(bestPositive) && Objects.nonNull(bestNegative)) {
        balancingEdges.add(new BalancingEdge(bestPositive, bestNegative, pathResult));

        int positiveBalance = positiveRemaining.get(bestPositive) - 1;
        int negativeBalance = negativeRemaining.get(bestNegative) - 1;

        // Update or remove vertices based on remaining imbalance
        if (positiveBalance > 0) {
          positiveRemaining.put(bestPositive, positiveBalance);
        } else {
          positiveRemaining.remove(bestPositive);
        }

        if (negativeBalance > 0) {
          negativeRemaining.put(bestNegative, negativeBalance);
        } else {
          negativeRemaining.remove(bestNegative);
        }
      } else {
        // No valid pairs found
        break;
      }
    }

    return balancingEdges;
  }

  public static List<BalancingEdge> findMinimumCostMatchingNotDirGraph(
      Graph graph, ShortestPathAlgorithm pathAlgorithm) {
    List<Vertex> unbalancedList = new ArrayList<>(graph.getUnbalancedVertices());

    List<BalancingEdge> matching = new ArrayList<>();
    Set<Vertex> matched = new HashSet<>();

    // Greedy approach: always match the closest unmatched pair
    // TODO: Refactoring: Maybe use something better than greedy
    while (matched.size() < unbalancedList.size()) {
      PathResult pathResult = new PathResult(List.of(), Double.POSITIVE_INFINITY);
      Vertex bestV1 = null, bestV2 = null;

      // Find the closest unmatched pair
      for (int i = 0; i < unbalancedList.size(); i++) {
        for (int j = i + 1; j < unbalancedList.size(); j++) {
          Vertex v1 = unbalancedList.get(i);
          Vertex v2 = unbalancedList.get(j);

          if (matched.contains(v1) || matched.contains(v2)) {
            continue;
          }

          PathResult path = pathAlgorithm.findShortestPath(graph, v1, v2);

          if (path.path().isEmpty()) {
            throw new RuntimeException(
                "No path found between unbalanced vertices " + v1.getId() + " and " + v2.getId());
          }

          if (path.cost() < pathResult.cost()) {
            pathResult = path;
            bestV1 = v1;
            bestV2 = v2;
          }
        }
      }

      if (Objects.nonNull(bestV1) && Objects.nonNull(bestV2)) {
        matching.add(new BalancingEdge(bestV1, bestV1, pathResult));
        matched.add(bestV1);
        matched.add(bestV2);
      } else {
        // No more pairs to match
        break;
      }
    }

    return matching;
  }
}
