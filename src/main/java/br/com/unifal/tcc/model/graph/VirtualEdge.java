package br.com.unifal.tcc.model.graph;

import br.com.unifal.tcc.model.results.PathResult;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;

public class VirtualEdge implements Edge {

  @Getter private final UUID id;
  private final Vertex source;
  private final Vertex target;
  @Getter private final List<Vertex> hiddenPath;
  private final Double weight;

  public VirtualEdge(PathResult pathResult) {
    id = UUID.randomUUID();
    this.hiddenPath = pathResult.path();
    this.source = hiddenPath.get(0);
    this.target = hiddenPath.get(hiddenPath.size() - 1);
    this.weight = pathResult.cost();
  }

  @Override
  public Vertex getSource() {
    return source;
  }

  @Override
  public Vertex getTarget() {
    return target;
  }

  @Override
  public double getWeight() {
    return weight;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof RealEdge realEdge)) return false;
    return Objects.equals(id, realEdge.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
