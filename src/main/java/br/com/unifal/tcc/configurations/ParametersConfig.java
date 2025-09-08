package br.com.unifal.tcc.configurations;

import br.com.unifal.tcc.solvers.factories.PathAlgorithmFactory;
import lombok.Getter;

@Getter
public class ParametersConfig {

  private final String graphFile;
  private final PathAlgorithmFactory algorithmType;

  public ParametersConfig(String[] args) {
    if (args.length < 2) {
      throw new IllegalArgumentException("Usage: java Main <graphFile> <algorithmType>");
    }

    this.graphFile = args[0];
    this.algorithmType = PathAlgorithmFactory.valueOf(args[1].toUpperCase());
  }
}
