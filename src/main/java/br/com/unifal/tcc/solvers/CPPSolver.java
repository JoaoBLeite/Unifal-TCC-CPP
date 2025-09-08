package br.com.unifal.tcc.solvers;

import br.com.unifal.tcc.algorithms.interfaces.ShortestPathAlgorithm;
import br.com.unifal.tcc.model.dto.BalancingEdge;
import br.com.unifal.tcc.model.graph.Edge;
import br.com.unifal.tcc.model.graph.Graph;
import br.com.unifal.tcc.model.graph.Vertex;
import br.com.unifal.tcc.model.graph.VirtualEdge;
import br.com.unifal.tcc.model.results.CPPSolution;
import br.com.unifal.tcc.model.results.PathResult;
import br.com.unifal.tcc.services.MatchingService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CPPSolver {

  private Graph graph;
  private ShortestPathAlgorithm pathAlgorithm;

  public CPPSolution solve(Vertex origin) {
    if (!graph.isEulerian()) {
      makeGraphEulerian();
    }

    List<Vertex> eulerianPath = findEulerianCircuit(origin);
    checkEulerianPath(eulerianPath);
    double totalCost = calculatePathCost(eulerianPath);

    return new CPPSolution(pathAlgorithm, eulerianPath, totalCost);
  }

  private void makeGraphEulerian() {
    Set<Vertex> unbalancedVertices = graph.getUnbalancedVertices();
    if (unbalancedVertices.isEmpty()) {
      return; // Already Eulerian
    }

    List<BalancingEdge> matching =
        (graph.isDirected())
            ? MatchingService.findMinimumCostMatchingDirGraph(graph, pathAlgorithm)
            : MatchingService.findMinimumCostMatchingNotDirGraph(graph, pathAlgorithm);

    for (BalancingEdge balancingEdge : matching) {
      addVirtualEdge(balancingEdge.pathResult());
    }
  }

  private void addVirtualEdge(PathResult path) {
    Edge virtualEdge = new VirtualEdge(path);
    graph.addEdge(virtualEdge);
  }

  private List<Vertex> findEulerianCircuit(Vertex origin) {
    // Create a copy of edges for modification during traversal
    Set<Edge> remainingEdges = new HashSet<>(graph.getEdges());

    List<Vertex> circuit = new ArrayList<>();
    Stack<Vertex> stack = new Stack<>();
    stack.push(origin);

    while (!stack.isEmpty()) {
      Vertex current = stack.peek();
      Optional<Edge> optNextEdge = findUnusedEdge(current, remainingEdges);

      if (optNextEdge.isPresent()) {
        Edge nextEdge = optNextEdge.get();
        remainingEdges.remove(nextEdge);
        Vertex next =
            nextEdge.getSource().equals(current) ? nextEdge.getTarget() : nextEdge.getSource();
        stack.push(next);
      } else {
        // No more edges from the current vertex, add to circuit
        circuit.add(stack.pop());
      }
    }

    // Reverse to get correct order
    Collections.reverse(circuit);
    return circuit;
  }

  private List<Vertex> expandVirtualEdges(List<Vertex> circuit) {
    List<Vertex> expandedCircuit = new ArrayList<>();
    Set<VirtualEdge> remainingVirtualEdges = graph.getVirtualEdges();

    for (int i = 0; i < circuit.size() - 1; i++) {
      Vertex from = circuit.get(i);
      expandedCircuit.add(from);
      Vertex to = circuit.get(i + 1);

      Optional<VirtualEdge> optVirtualEdge = findVirtualEdge(from, to, remainingVirtualEdges);
      if (optVirtualEdge.isPresent()) {
        VirtualEdge virtualEdge = optVirtualEdge.get();
        List<Vertex> hiddenPath = virtualEdge.getHiddenPath();
        boolean forward = hiddenPath.get(0).equals(from);

        // Add intermediate vertices (skip first and last)
        if (forward) {
          for (int j = 1; j < hiddenPath.size() - 1; j++) {
            expandedCircuit.add(hiddenPath.get(j));
          }
        } else {
          int ops = -1;
        }
      }
    }
  }

  private Optional<VirtualEdge> findVirtualEdge(
      Vertex from, Vertex to, Set<VirtualEdge> remainingEdges) {
    return remainingEdges.stream()
        .filter(
            ve ->
                (ve.getSource().equals(from) && ve.getTarget().equals(to))
                    || (!graph.isDirected()
                        && ve.getSource().equals(to)
                        && ve.getTarget().equals(from)))
        .findFirst();
  }

  private void checkEulerianPath(List<Vertex> eulerianPath) {
    Vertex first = eulerianPath.get(0);
    Vertex last = eulerianPath.get(eulerianPath.size() - 1);

    if (!first.equals(last)) {
      throw new RuntimeException(
          "Invalid eulerian path. Path must start and end with origin vertex!");
    }

    int expectedMinPathSize = graph.getEdges().size() + 1;
    if (eulerianPath.size() < expectedMinPathSize) {
      throw new RuntimeException("Invalid eulerian path. Path is missing edge(s)!");
    }
  }

  private double calculatePathCost(List<Vertex> path) {
    double totalCost = 0.0;
    if (path.size() < 2) {
      return totalCost;
    }

    // Create a copy of edges for modification
    Set<Edge> remainingEdges = new HashSet<>(graph.getEdges());

    for (int i = 0; i < path.size() - 1; i++) {
      Vertex from = path.get(i);
      Vertex to = path.get(i + 1);

      Optional<Edge> optEdge = findUnusedEdge(from, to, remainingEdges);
      if (optEdge.isPresent()) {
        Edge edge = optEdge.get();
        remainingEdges.remove(edge);
        totalCost += edge.getWeight();
      }
    }

    return totalCost;
  }

  private Optional<Edge> findUnusedEdge(Vertex vertex, Set<Edge> remainingEdges) {
    for (Edge edge : remainingEdges) {
      if (edge.getSource().equals(vertex)
          || (!graph.isDirected() && edge.getTarget().equals(vertex))) {
        return Optional.of(edge);
      }
    }
    return Optional.empty();
  }

  private Optional<Edge> findUnusedEdge(Vertex from, Vertex to, Set<Edge> remainingEdges) {
    for (Edge edge : remainingEdges) {
      if ((edge.getSource().equals(from) && edge.getTarget().equals(to))
          || (!graph.isDirected()
              && edge.getTarget().equals(from)
              && edge.getSource().equals(to))) {
        return Optional.of(edge);
      }
    }
    return Optional.empty();
  }
}
