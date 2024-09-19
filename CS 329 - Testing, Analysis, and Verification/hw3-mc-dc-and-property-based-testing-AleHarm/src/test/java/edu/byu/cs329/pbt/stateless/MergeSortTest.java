package edu.byu.cs329.pbt.stateless;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test suite for the MergeSort class.
 */
public class MergeSortTest {
  public static Random rand = new Random();

  // Add tests for MergeSort.sort() here

  @ParameterizedTest
  @ValueSource(ints = {1, 15, 125, 1234})
  void testMergeSortAgainstEnsureStatements(int size) {
    int[] input = generateArrayWithRandomInts(size);
    int[] actual = MergeSort.sort(Arrays.copyOf(input, input.length));

    assertEquals(input.length, actual.length);

    //Tests algebraic property sorted as well
    for (int i = 1; i < actual.length; i++) {
      assertTrue(actual[i - 1] <= actual[i]);
    }

    //Tests algebraic property conservation of elements as well
    for (int i = 0; i < actual.length; i++) {
      int currentNum = actual[i];
      assertEquals(count(currentNum, actual), count(currentNum, input));
    }
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 15, 125, 1234})
  void testMergeSortAgainstOracle(int size) {
    int[] input = generateArrayWithRandomInts(size);
    int[] expected = Arrays.copyOf(input, input.length);
    Arrays.sort(expected);
    int[] actual = MergeSort.sort(Arrays.copyOf(input, input.length));
    assertArrayEquals(expected, actual, "Failed with input array " + input);
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 15, 125, 1234})
  void testMergeSortForRuntimeErrors(int size) {
    
    assertDoesNotThrow(() -> {
      int[] input = generateArrayWithRandomInts(size);
      MergeSort.sort(Arrays.copyOf(input, input.length));
    }, "Exception occurred during sorting");
  }

  private int[] generateArrayWithRandomInts(int size) {
    int[] array = new int[size];
    Random random = new Random();

    for (int i = 0; i < size; i++) {
      array[i] = random.nextInt();
    }
    return array;
  }

  /**
   * Counts the number of times x is in arr.
   * 
   * @param x   a value
   * @param arr an array
   * @return count of x in arr
   */
  static int count(int x, int[] arr) {
    int c = 0;
    for (int i : arr) {
      if (x == i) {
        ++c;
      }
    }
    return c;
  }
}
