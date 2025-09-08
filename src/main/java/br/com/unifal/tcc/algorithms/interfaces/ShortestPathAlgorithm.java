package br.com.unifal.tcc.algorithms.interfaces;

import br.com.unifal.tcc.model.graph.Graph;
import br.com.unifal.tcc.model.graph.Vertex;
import br.com.unifal.tcc.model.results.PathResult;

public interface ShortestPathAlgorithm {
  String getName();

  PathResult findShortestPath(Graph graph, Vertex start, Vertex end);
}
