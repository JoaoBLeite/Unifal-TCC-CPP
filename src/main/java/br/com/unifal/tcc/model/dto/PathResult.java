package br.com.unifal.tcc.model.dto;

import br.com.unifal.tcc.model.graph.Vertex;
import java.util.List;
import java.util.Objects;

public record PathResult(List<Vertex> path, double cost) {
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PathResult that = (PathResult) o;
    return Double.compare(that.cost, cost) == 0 && path.equals(that.path);
  }

  @Override
  public int hashCode() {
    return Objects.hash(path, cost);
  }
}
