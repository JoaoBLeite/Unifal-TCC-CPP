package br.com.unifal.tcc.model.graph;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Vertex {

  private final String id;
  private int degree;

  public Vertex(String id) {
    this.id = id;
    this.degree = 0;
  }

  public void updateDegree(int value) {
    this.degree += value;
  }

  public boolean isNotBalanced() {
    return degree != 0;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Vertex vertex)) return false;
    return Objects.equals(id, vertex.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
