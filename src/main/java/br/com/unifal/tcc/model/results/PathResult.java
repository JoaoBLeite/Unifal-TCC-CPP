package br.com.unifal.tcc.model.results;

import java.util.List;

import br.com.unifal.tcc.model.graph.Vertex;

public record PathResult(List<Vertex> path, double cost) {}
