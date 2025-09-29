package br.com.unifal.tcc.solvers.factories;

import br.com.unifal.tcc.algorithms.DijkstraListAlgorithm;
import br.com.unifal.tcc.algorithms.DijkstraPqAlgorithm;
import br.com.unifal.tcc.algorithms.SSSPAlgorithm;
import br.com.unifal.tcc.algorithms.interfaces.ShortestPathAlgorithm;

public enum PathAlgorithmFactory {
  DIJKSTRA_PQ {
    @Override
    public ShortestPathAlgorithm createAlgorithm() {
      return new DijkstraPqAlgorithm();
    }
  },
  DIJKSTRA_LIST {
    @Override
    public ShortestPathAlgorithm createAlgorithm() {
      return new DijkstraListAlgorithm();
    }
  },
  SSSP {
    @Override
    public ShortestPathAlgorithm createAlgorithm() {
      return new SSSPAlgorithm();
    }
  };

  public abstract ShortestPathAlgorithm createAlgorithm();
}
