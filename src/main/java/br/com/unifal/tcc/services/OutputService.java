package br.com.unifal.tcc.services;

import br.com.unifal.tcc.algorithms.interfaces.ShortestPathAlgorithm;
import br.com.unifal.tcc.model.graph.Vertex;
import br.com.unifal.tcc.model.results.CPPSolution;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class OutputService {

  private OutputService() {}

  public static void exportResultFile(String graphFile, CPPSolution solution, Duration duration) {
    String outputFileName = generateOutputFileName(graphFile, solution.pathAlgorithm());
    generateOutputFile(solution, duration, outputFileName);
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private static String generateOutputFileName(
      String graphFile, ShortestPathAlgorithm pathAlgorithm) {
    int lastSlashIdx = graphFile.lastIndexOf('/');
    String graphFileName = (lastSlashIdx == -1) ? graphFile : graphFile.substring(lastSlashIdx + 1);
    String baseName = graphFileName.split("\\.")[0];

    String outputDir = "output" + File.separator + pathAlgorithm.getName().toLowerCase();
    File dir = new File(outputDir);
    if (!dir.exists()) {
      dir.mkdirs();
    }

    return outputDir + File.separator + baseName + "-output.txt";
  }

  private static void generateOutputFile(
      CPPSolution solution, Duration duration, String outputFile) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
      // Title
      writer.write("Chinese Postman Problem Solution:\n");

      // Metadata
      writer.write("---------------------------------\n");
      writer.write("Algorithm: " + solution.pathAlgorithm().getName() + "\n");
      writer.write("Total Cost: " + solution.totalCost() + "\n");
      writer.write("Execution Time: " + duration.toMillis() + " ms\n");
      writer.write("---------------------------------\n");

      // Solution
      List<Vertex> path = solution.path();
      for (int i = 0; i < path.size(); i++) {
        writer.write(path.get(i).getId());
        if (i < path.size() - 1) writer.write(" -> ");
      }
    } catch (IOException e) {
      System.err.println("Error writing solution to output file. Error: " + e.getMessage());
    }
  }
}
