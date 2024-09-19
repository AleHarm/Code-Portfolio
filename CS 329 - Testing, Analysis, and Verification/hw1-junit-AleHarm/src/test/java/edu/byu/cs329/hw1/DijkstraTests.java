package edu.byu.cs329.hw1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests for the Dijkstra class
 */
public class DijkstraTests {

  private static final Logger log = LoggerFactory.getLogger(DijkstraTests.class);
  private Dijkstra classUnderTest;
  int[][] validGraph = {
      { 0, 2, 4, Dijkstra.M, Dijkstra.M, Dijkstra.M },
      { 2, 0, 1, 4, Dijkstra.M, Dijkstra.M },
      { 4, 1, 0, 2, 6, Dijkstra.M },
      { Dijkstra.M, 4, 2, 0, 1, 3 },
      { Dijkstra.M, Dijkstra.M, 6, 1, 0, 5 },
      { Dijkstra.M, Dijkstra.M, Dijkstra.M, 3, 5, 0 } };

  int[][] emptyGraph = {};

  int[][] nullGraph = { null };

  int[][] nonSquareGraph = {
      { 0, 2, 4, Dijkstra.M, Dijkstra.M, Dijkstra.M },
      { 2, 0, 1, 4, Dijkstra.M, Dijkstra.M },
      { 4, 1, 0, 2, 6, Dijkstra.M },
      { Dijkstra.M, 4, 2, 0, 1, 3 },
      { Dijkstra.M, Dijkstra.M, 6, 1, 0, 5 } };

  /**
   * Empty Constructor to shut javadoc up
   */
  public DijkstraTests() {
    // Empty constructor
  }

  /**
   * Sets the Dijkstra instance to null
   */
  @AfterEach
  public void tearDown() {
    classUnderTest = null;
  }

  /**
   * The base case test with all inputs valid
   */
  @Test
  @DisplayName("Everything normal")
  public void testNormalInputsReturnsSuccess() {

    try {

      classUnderTest = new Dijkstra(validGraph);
      assertNotNull(classUnderTest);
      assertEquals(6, classUnderTest.shortestPath(0, 4));
    } catch (Exception e) {
      fail("Using valid graph threw exception: " + e.getMessage());
    }

  }

  @Nested
  @DisplayName("When using bad graph")
  class DijkstraBadGraphs {

    /**
     * Test with empty graph, returns error
     */
    @Test
    @DisplayName("Empty graph")
    public void testEmptyGraphReturnsError() {

      try {

        classUnderTest = new Dijkstra(emptyGraph);
        fail("Using empty graph did not throw exception");
      } catch (Exception e) {
        log.info("Empty graph returned exception with error message: " + e.getMessage());
      }
    }

    /**
     * test with null graph, returns error
     */

    @Test
    @DisplayName("Null graph")
    public void testNullGraphReturnsError() {

      try {

        classUnderTest = new Dijkstra(nullGraph);
        fail("Using null graph did not throw exception");
      } catch (Exception e) {
        log.info("Null graph returned exception with error message: " + e.getMessage());
      }
    }

    /**
     * test with non-square graph, returns error
     */
    @Test
    @DisplayName("Non-square graph")
    public void testNonSquareGraphReturnsError() {

      try {

        classUnderTest = new Dijkstra(nonSquareGraph);
        fail("Using non-square graph did not throw exception");
      } catch (Exception e) {
        log.info("Non-square graph returned exception with error message: " + e.getMessage());
      }
    }
  }

  @Nested
  @DisplayName("When using bad indicies")
  class DijkstraBadIndicies {

    /**
     * sets up the Dijkstra class with valid graph before each test in this section
     */
    @BeforeEach
    public void init() {

      classUnderTest = new Dijkstra(validGraph);
    }

    /**
     * Test negative indicies, returns error
     */
    @Test
    @DisplayName("Negative indicies")
    public void testNegativeIndiciesReturnsError() {

      try {

        classUnderTest.shortestPath(0, -4);
        fail("Using negative indicies did not throw exception");
      } catch (Exception e) {
        log.info("Negative indicies returned exception with error message: " + e.getMessage());
      }
    }

    /**
     * test indicies outside range of graph, returns error
     */
    @Test
    @DisplayName("Indicies outside range")
    public void testOutOfBoundsIndiciesReturnsError() {

      try {

        classUnderTest.shortestPath(0, 999999999);
        fail("Using out of bound indicies did not throw exception");
      } catch (Exception e) {
        log.info("Out of bound indicies returned exception with error message: " + e.getMessage());
      }
    }
  }

  /**
   * a test that doesn't run on Sundays to show the TA's I did the tutorial and
   * paid attention
   */
  @Test
  @DisplayName("This test will not run on Sunday")
  public void testValidNotOnSundayReturnsSuccess() {
    classUnderTest = new Dijkstra(validGraph);
    LocalDateTime ldt = LocalDateTime.now();
    assumeTrue(ldt.getDayOfWeek().getValue() != 7, "Test skipped... even computers shouldn't work on the sabbath!");
    assumeTrue(ldt.getDayOfWeek().getValue() != 7);
    try {

      classUnderTest = new Dijkstra(validGraph);
      assertNotNull(classUnderTest);
      assertEquals(6, classUnderTest.shortestPath(0, 4));
    } catch (Exception e) {
      fail("Using valid graph threw exception: " + e.getMessage());
    }
  }
}
