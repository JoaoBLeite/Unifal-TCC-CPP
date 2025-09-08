package br.com.unifal.tcc.solvers.factories;

import br.com.unifal.tcc.algorithms.DijkstraPqAlgorithm;
import br.com.unifal.tcc.algorithms.SSSPAlgorithm;
import br.com.unifal.tcc.algorithms.interfaces.ShortestPathAlgorithm;

public enum PathAlgorithmFactory {
  DIJKSTRA {
    @Override
    public ShortestPathAlgorithm createAlgorithm() {
      return new DijkstraPqAlgorithm();
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
