package br.com.unifal.tcc.services;

import br.com.unifal.tcc.model.graph.Edge;
import br.com.unifal.tcc.model.graph.Graph;
import br.com.unifal.tcc.model.graph.RealEdge;
import br.com.unifal.tcc.model.graph.Vertex;
import com.google.gson.Gson;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

public class InputService {

  private InputService() {}

  public static Graph loadGraphFromFile(String graphFile) {
    Gson gson = new Gson();

    try (FileReader reader = new FileReader(graphFile)) {
      GraphInp graphInp = gson.fromJson(reader, GraphInp.class);
      Graph graph = new Graph(graphInp.directed());
      graphInp
          .links()
          .forEach(
              linkInp -> {
                Vertex source = new Vertex(linkInp.source());
                Vertex target = new Vertex(linkInp.target());
                Edge edge = new RealEdge(source, target, linkInp.weight());
                graph.addEdge(edge);
              });
      return graph;
    } catch (Exception e) {
      throw new RuntimeException("Error reading graph json file", e);
    }
  }

  private record GraphInp(
      boolean directed,
      boolean multigraph,
      Map<String, Object> graph,
      List<NodeInp> nodes,
      List<LinkInp> links) {}

  private record NodeInp(String id) {}

  private record LinkInp(String source, String target, Double weight) {}
}
