package edu.brown.cs.student.Sprint0.main;

import edu.brown.cs.student.CSV.CSVParser;
import edu.brown.cs.student.CSV.FactoryFailureException;
import edu.brown.cs.student.Sprint0.kdtree.DistanceSorter;
import edu.brown.cs.student.Sprint0.kdtree.KdTree;
import edu.brown.cs.student.Sprint0.stars.GalaxyGenerator;
import edu.brown.cs.student.Sprint0.stars.Star;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

/** The Main class of our project. This is where execution begins. */
public final class Main {
  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // runs utility program taking in csv filename as the first argument
    // new CSVUtility(args[0]);
    try {
      new CSVParser(new FileReader(args[0]), row -> row);
    } catch (IOException e) {
      System.err.println("Error: Exception when parsing");
    } catch (FactoryFailureException e) {
      System.err.println("Error: Factory Failure when parsing");
    }

    // generates galaxy of stars, computes nearest neighbor for all
    if (args.length == 2 && args[0].equals("generate_galaxy")) {
      int numStars = 0;
      try {
        numStars = Integer.parseInt(args[1]);
      } catch (Exception ignored) {
        System.err.println("ERROR: Could not parse number of stars to generate.");
      }
      List<Star> galaxy = GalaxyGenerator.generate(numStars);
      KdTree<Star> starKdTree = new KdTree<>(galaxy, 0);
      for (Star star : galaxy) {
        PriorityQueue<Star> pq =
            starKdTree.kdTreeSearch(
                "neighbors", 1, star, new DistanceSorter(star), new HashSet<>());
        System.out.println(pq.peek());
      }
    }
  }
}
