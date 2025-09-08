package br.com.unifal.tcc.model.graph;

public interface Edge {

  Vertex getSource();

  Vertex getTarget();

  double getWeight();
}
