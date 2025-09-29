package br.com.unifal.tcc;

import static br.com.unifal.tcc.services.ExecutionTime.measureTimeWithResult;

import br.com.unifal.tcc.algorithms.interfaces.ShortestPathAlgorithm;
import br.com.unifal.tcc.configurations.ParametersConfig;
import br.com.unifal.tcc.model.dto.CPPSolution;
import br.com.unifal.tcc.model.graph.Graph;
import br.com.unifal.tcc.model.graph.Vertex;
import br.com.unifal.tcc.services.InputService;
import br.com.unifal.tcc.services.OutputService;
import br.com.unifal.tcc.services.dto.TimedResult;
import br.com.unifal.tcc.solvers.CPPSolver;

public class Main {
  public static void main(String[] args) {
    ParametersConfig params = new ParametersConfig(args);

    // Load from parameters
    Graph graph = InputService.loadGraphFromFile(params.getGraphFile());
    ShortestPathAlgorithm pathAlgorithm = params.getAlgorithmType().createAlgorithm();

    // Solve CPP
    CPPSolver solver = new CPPSolver(graph, pathAlgorithm);

    TimedResult<CPPSolution> timedSolution =
        measureTimeWithResult(
            () -> {
              return solver.solve(new Vertex("A"));
            }); // TODO

    // Export output
    OutputService.exportResultFile(
        params.getGraphFile(), timedSolution.result(), timedSolution.duration());
  }
}
