package br.com.unifal.tcc.algorithms.dto;

import br.com.unifal.tcc.model.graph.Vertex;

/** Helper record to store vertex-distance pairs for the priority queue. */
public record VertexDistance(Vertex vertex, double distance) {}
