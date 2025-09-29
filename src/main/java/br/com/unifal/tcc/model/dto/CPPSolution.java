package br.com.unifal.tcc.model.results;

import br.com.unifal.tcc.algorithms.interfaces.ShortestPathAlgorithm;
import br.com.unifal.tcc.model.graph.Vertex;
import java.util.List;
import java.util.Objects;

public record CPPSolution(
    ShortestPathAlgorithm pathAlgorithm, List<Vertex> path, double totalCost) {

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CPPSolution that)) return false;
    return Double.compare(totalCost(), that.totalCost()) == 0
        && Objects.equals(path(), that.path());
  }

  @Override
  public int hashCode() {
    return Objects.hash(path(), totalCost());
  }
}
