package br.com.unifal.tcc.algorithms;

import br.com.unifal.tcc.algorithms.interfaces.ShortestPathAlgorithm;
import br.com.unifal.tcc.model.graph.Graph;
import br.com.unifal.tcc.model.graph.Vertex;
import br.com.unifal.tcc.model.results.PathResult;

public class SSSPAlgorithm implements ShortestPathAlgorithm {

  @Override
  public String getName() {
    return "SSSP-Algorithm";
  }

  @Override
  public PathResult findShortestPath(Graph graph, Vertex start, Vertex end) {
    return null; // TODO
  }
}
