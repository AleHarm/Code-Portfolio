package edu.byu.cs329.pbt.stateless;

/**
 * Defines a static binary search method.
 */
public class BinarySearch {

  /**
   * Private constructor. Methods should only be called statically.
   * Example:
   * {@code boolean ans = BinarySearch.search(array, value)}
   */
  private BinarySearch() {
  }

  /**
   * Runs a binary search on an array.
   *
   * @requires array != null
   * @requires array is sorted by increasing value:
   *           {@literal forall i, 0 < i < array.length - 1 ==> array[i] < array[i+1] }
   * 
   * @ensures {@literal out = true <==> exists i in arr (i = value) }
   * 
   * @return (out: boolean) whether if value was found in the array
   **/
  public static boolean search(int[] array, int value) {
    int left = 0;
    int right = array.length - 1;
    while (left <= right) {
      int index = (right + left) / 2;
      if (array[index] == value) {
        return true;
      }
      if (array[index] < value) {
        right = index - 1;
      } else {
        left = index + 1;
      }
    }
    return false;

  }

}
