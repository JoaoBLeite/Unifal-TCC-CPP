package br.com.unifal.tcc.model.graph;

import java.util.Objects;
import java.util.UUID;
import lombok.Getter;

public class RealEdge implements Edge {

  @Getter private final UUID id;
  private final Vertex source;
  private final Vertex target;
  private final Double weight;

  public RealEdge(Vertex source, Vertex target, double weight) {
    id = UUID.randomUUID();
    this.source = source;
    this.target = target;
    this.weight = weight;
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
