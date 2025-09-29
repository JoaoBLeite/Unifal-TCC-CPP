package br.com.unifal.tcc.fixtures.model.graph;

import br.com.unifal.tcc.model.graph.Graph;
import br.com.unifal.tcc.model.graph.RealEdge;
import br.com.unifal.tcc.model.graph.Vertex;

public class GraphFixture {

  private GraphFixture() {}

  public static Graph getGraphFixture() {
    Graph graph = new Graph();
    graph.addEdge(new RealEdge(new Vertex("A"), new Vertex("B"), 1d));
    graph.addEdge(new RealEdge(new Vertex("B"), new Vertex("C"), 2d));
    graph.addEdge(new RealEdge(new Vertex("C"), new Vertex("D"), 3d));
    graph.addEdge(new RealEdge(new Vertex("D"), new Vertex("E"), 4d));
    graph.addEdge(new RealEdge(new Vertex("E"), new Vertex("F"), 5d));
    graph.addEdge(new RealEdge(new Vertex("F"), new Vertex("G"), 6d));
    graph.addEdge(new RealEdge(new Vertex("G"), new Vertex("A"), 13d));
    graph.addEdge(new RealEdge(new Vertex("B"), new Vertex("G"), 7d));
    graph.addEdge(new RealEdge(new Vertex("I"), new Vertex("B"), 8d));
    graph.addEdge(new RealEdge(new Vertex("H"), new Vertex("I"), 9d));
    graph.addEdge(new RealEdge(new Vertex("C"), new Vertex("H"), 10d));
    graph.addEdge(new RealEdge(new Vertex("I"), new Vertex("C"), 11d));
    graph.addEdge(new RealEdge(new Vertex("E"), new Vertex("C"), 12d));
    graph.addEdge(new RealEdge(new Vertex("H"), new Vertex("F"), 14d));
    graph.addEdge(new RealEdge(new Vertex("G"), new Vertex("F"), 15d));
    graph.addEdge(new RealEdge(new Vertex("A"), new Vertex("J"), 16d));
    graph.addEdge(new RealEdge(new Vertex("J"), new Vertex("K"), 17d));
    graph.addEdge(new RealEdge(new Vertex("K"), new Vertex("L"), 18d));
    graph.addEdge(new RealEdge(new Vertex("L"), new Vertex("M"), 19d));
    graph.addEdge(new RealEdge(new Vertex("M"), new Vertex("N"), 20d));
    graph.addEdge(new RealEdge(new Vertex("N"), new Vertex("O"), 21d));
    graph.addEdge(new RealEdge(new Vertex("O"), new Vertex("P"), 22d));
    graph.addEdge(new RealEdge(new Vertex("P"), new Vertex("Q"), 23d));
    graph.addEdge(new RealEdge(new Vertex("Q"), new Vertex("R"), 24d));
    graph.addEdge(new RealEdge(new Vertex("R"), new Vertex("S"), 25d));
    graph.addEdge(new RealEdge(new Vertex("S"), new Vertex("A"), 26d)); 
    graph.addEdge(new RealEdge(new Vertex("K"), new Vertex("D"), 27d)); 
    graph.addEdge(new RealEdge(new Vertex("F"), new Vertex("L"), 28d));
    graph.addEdge(new RealEdge(new Vertex("M"), new Vertex("H"), 29d)); 
    graph.addEdge(new RealEdge(new Vertex("I"), new Vertex("N"), 30d)); 
    graph.addEdge(new RealEdge(new Vertex("O"), new Vertex("E"), 31d));
    graph.addEdge(new RealEdge(new Vertex("G"), new Vertex("P"), 32d)); 
    graph.addEdge(new RealEdge(new Vertex("Q"), new Vertex("C"), 33d)); 
    graph.addEdge(new RealEdge(new Vertex("B"), new Vertex("R"), 34d)); 
    graph.addEdge(new RealEdge(new Vertex("S"), new Vertex("J"), 35d)); 
    graph.addEdge(new RealEdge(new Vertex("D"), new Vertex("K"), 36d)); 
    graph.addEdge(new RealEdge(new Vertex("L"), new Vertex("F"), 37d)); 
    graph.addEdge(new RealEdge(new Vertex("H"), new Vertex("M"), 38d)); 
    graph.addEdge(new RealEdge(new Vertex("N"), new Vertex("I"), 39d)); 
    graph.addEdge(new RealEdge(new Vertex("E"), new Vertex("O"), 40d)); 
    graph.addEdge(new RealEdge(new Vertex("P"), new Vertex("G"), 41d)); 
    graph.addEdge(new RealEdge(new Vertex("C"), new Vertex("Q"), 42d)); 
    graph.addEdge(new RealEdge(new Vertex("R"), new Vertex("B"), 43d)); 
    graph.addEdge(new RealEdge(new Vertex("J"), new Vertex("S"), 44d)); 
    graph.addEdge(new RealEdge(new Vertex("L"), new Vertex("P"), 45d)); 
    graph.addEdge(new RealEdge(new Vertex("N"), new Vertex("R"), 46d)); 
    graph.addEdge(new RealEdge(new Vertex("K"), new Vertex("O"), 47d)); 
    graph.addEdge(new RealEdge(new Vertex("M"), new Vertex("Q"), 48d)); 
    return graph;
  }
}
