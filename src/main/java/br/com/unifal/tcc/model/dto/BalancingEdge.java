package br.com.unifal.tcc.model.dto;

import br.com.unifal.tcc.model.graph.Vertex;

public record BalancingEdge(Vertex from, Vertex to, PathResult pathResult) {}
