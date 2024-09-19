package edu.byu.cs329.hw1;

import java.util.HashSet;
import java.util.Set;

/**
 * This is a class that defines a Dijkstra object.
 *
 * @author Not Me
 * @version 0.0.1
 */

public class Dijkstra {
  static final int M = Integer.MAX_VALUE;
  private int[][] graph;
  private int dimension;
  private int[][] length = null;

  /**
   * Constructor for the Dijkstra class.
   *
   * @param g 2d array of ints
   */
  public Dijkstra(int[][] g) {
    if (g == null) {
      throw new IllegalArgumentException("The graph must be non-null");
    }
    dimension = g.length;
    if (dimension == 0) {
      throw new IllegalArgumentException("The graph must be non-empty");
    }
    for (int i = 0; i < dimension; ++i) {
      if (g[i].length != dimension) {
        throw new IllegalArgumentException("The graph must be square");
      }
    }
    graph = g;
  }

  private void allShortestPathLengths() {
    int[][] lengths = new int[graph.length][graph.length];
    initializeL(graph, lengths);
    for (int a = 0; a < lengths.length; ++a) {
      int[] thisL = lengths[a];
      Set<Integer> s = new HashSet<Integer>();
      while (s.size() < lengths.length) {
        int u = getMinimumIndex(thisL, s);
        s.add(u);
        for (int v = 0; v < thisL.length; ++v) {
          if (s.contains(v)) {
            continue;
          }
          int newDistance = thisL[u] + graph[u][v];
          if (newDistance > 0 && newDistance < thisL[v]) {
            thisL[v] = newDistance;
          }
        }
      }
    }

    length = lengths;
  }

  /**
   * Finds the shortest path between the two points.
   *
   * @param from an int starting point
   * @param to   an int ending points
   * @return the value of the shortest path as an int
   */

  public int shortestPath(int from, int to) {
    if (from < 0 || to < 0) {
      throw new IllegalArgumentException("Indices must be nonnegative!");
    }
    if (from >= graph.length || to >= graph.length) {
      throw new IllegalArgumentException(
          "Indices must be within the graph's dimension!");
    }
    if (length == null) {
      allShortestPathLengths();
    }
    return length[from][to];
  }

  static int getMinimumIndex(final int[] thisL, final Set<Integer> s) {
    int u = M;
    final int length = thisL.length;
    for (int i = 0; i < length; ++i) {
      if (s.contains(i)) {
        continue;
      }
      if (u == M || thisL[i] < thisL[u]) {
        u = i;
      }
    }
    return u;
  }

  private void initializeL(int[][] graph, int[][] l) {
    for (int i = 0; i < graph.length; ++i) {
      for (int j = 0; j < graph.length; ++j) {
        if (i == j) {
          l[i][j] = 0;
        } else {
          l[i][j] = M;
        }
      }
    }
  }

  /**
   * Converts a 2d array of ints to a string.
   *
   * @param l a 2d array of ints that you want converted to a string
   * @return a string of the 2d array passed in
   */

  public static String tableToString(int[][] l) {
    StringBuilder sb = new StringBuilder();
    String eol = System.getProperty("line.separator");
    int length = l.length;
    for (int i = 0; i < length; ++i) {
      int[] tl = l[i];
      for (int j = 0; j < length; ++j) {
        int value = tl[j];
        if (value == M) {
          sb.append("-");
        } else {
          sb.append(tl[j]);
        }
        if (j < length - 1) {
          sb.append(" ");
        }
      }
      if (i < length - 1) {
        sb.append(eol);
      }
    }

    return sb.toString();
  }

}
