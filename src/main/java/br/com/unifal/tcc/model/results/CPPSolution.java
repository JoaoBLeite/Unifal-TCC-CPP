package br.com.unifal.tcc.model.results;

import br.com.unifal.tcc.algorithms.interfaces.ShortestPathAlgorithm;
import br.com.unifal.tcc.model.graph.Vertex;
import java.util.List;

public record CPPSolution(
    ShortestPathAlgorithm pathAlgorithm, List<Vertex> path, double totalCost) {}
