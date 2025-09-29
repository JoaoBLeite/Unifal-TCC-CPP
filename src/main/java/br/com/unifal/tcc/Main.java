package br.com.unifal.tcc;

import br.com.unifal.tcc.algorithms.interfaces.ShortestPathAlgorithm;
import br.com.unifal.tcc.configurations.ParametersConfig;
import br.com.unifal.tcc.model.graph.Graph;
import br.com.unifal.tcc.model.graph.Vertex;
import br.com.unifal.tcc.model.dto.CPPSolution;
import br.com.unifal.tcc.services.InputService;
import br.com.unifal.tcc.services.OutputService;
import br.com.unifal.tcc.solvers.CPPSolver;
import java.time.Duration;
import java.time.Instant;

public class Main {
  public static void main(String[] args) {
    ParametersConfig params = new ParametersConfig(args);

    // Load from parameters
    Graph graph = InputService.loadGraphFromFile(params.getGraphFile());
    ShortestPathAlgorithm pathAlgorithm = params.getAlgorithmType().createAlgorithm();

    // Solve CPP
    CPPSolver solver = new CPPSolver(graph, pathAlgorithm);
    Instant startInstant = Instant.now();
    CPPSolution solution = solver.solve(new Vertex("A")); // TODO
    Instant endInstant = Instant.now();

    // Export output
    Duration duration = Duration.between(startInstant, endInstant);
    OutputService.exportResultFile(params.getGraphFile(), solution, duration);
  }
}
