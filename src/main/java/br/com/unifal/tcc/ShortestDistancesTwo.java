package br.com.unifal.tcc;

import br.com.unifal.tcc.algorithms.DijkstraListAlgorithm;
import br.com.unifal.tcc.algorithms.DijkstraPqAlgorithm;
import br.com.unifal.tcc.algorithms.SSSPAlgorithm;
import br.com.unifal.tcc.algorithms.interfaces.ShortestPathAlgorithm;
import br.com.unifal.tcc.model.graph.Edge;
import br.com.unifal.tcc.model.graph.Graph;
import br.com.unifal.tcc.model.graph.Vertex;
import br.com.unifal.tcc.services.InputService;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Set;

public class ShortestDistancesTwo {
    public static void main(String[] args) throws IOException {
        if(args.length < 1) {
            System.err.println("Usage: java ShortestDistanceTwo <input-file-name>");
            return;
        }

        String graphFile = args[0];
        Graph graph = InputService.loadGraphFromFile(graphFile);

        List<ShortestPathAlgorithm> algorithmList = List.of(
                new DijkstraPqAlgorithm(),
                new DijkstraListAlgorithm(),
                new SSSPAlgorithm()
        );

        String outputCsvFile = "data_graph2.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputCsvFile))) {
            writer.write("graph_name, num_vertice, num_edge, algorithm_usaded,source_vertex_id,execution_time_ns\n");

            Set<Vertex> verticesSet = graph.getVerticesSet();
            long verticesAmount = verticesSet.size();
            long vertexCount = 0;

            String graphName = new java.io.File(graphFile).getName();
            Set<Vertex> numVertices = graph.getVerticesSet();
            Set<Edge> numEdges = graph.getEdges();

            int vertexCountToInt = verticesSet.size();
            int edgeCountToInt =  numEdges.size();

            for (Vertex source : verticesSet) {
                for (ShortestPathAlgorithm algorithm : algorithmList) {

                    Duration duration = measureTime(() -> algorithm.getDistanceMap(graph, source));
                    long timeInNanos = duration.toNanos();

                    String csvLine = String.format("%s,%d,%d,%s,%s,%d\n",
                            graphName,
                            vertexCountToInt,
                            edgeCountToInt,
                            algorithm.getName(),
                            source.getId(),
                            timeInNanos);

                    writer.write(csvLine);
                }

                double progress = (++vertexCount * 100.0) / verticesAmount;
                System.out.printf("Progress: %.2f%%\r", progress);
            }
        }

        System.out.println("\nBenchmark concluído. Resultados salvos em: " + outputCsvFile);
    }

    private static Duration measureTime(Runnable method) {
        long start = System.nanoTime();
        method.run();
        long end = System.nanoTime();
        return Duration.ofNanos(end - start);
        }
    }
