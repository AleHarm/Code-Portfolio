package misc;

/**
 * A queue class.
 */

public class Queue {
  Object[] arr;
  int size;
  int first;
  int last;

  /**
   * Creates a queue with a bounded size.
   *
   * @requires
   *           max != null
   *           max.type() = int
   *           max >= 0
   *
   * @ensures
   *          arr, (forall i<- arr.elements :: i), size, first, last != null
   *          size(Q), first, last = 0
   *          Q != null
   *
   * @param max the size of the queue
   */
  public Queue(int max) {
    arr = new Object[max];
    size = 0;
    first = 0;
    last = 0;
  }

  /**
   * gets size of arr.
   * 
   * @requires
   *           Q != null
   *           size != null
   * 
   * @ensures
   *          old(Q) = Q
   * 
   * @return size of Q
   */

  public int size() {
    return size;
  }

  /**
   * gets current max value in arr.
   * 
   * @requires
   *           Q != null
   *           arr != null
   * 
   * @ensures
   *          old(Q) = Q
   * 
   * @return size of Q
   */

  public int max() {
    return arr.length;
  }

  /**
   * Adds a new object into the queue.
   *
   * @requires
   *           x != null
   *           arr != null
   *           size < max
   * 
   * @ensures
   *          size = old(Q).size++
   *          size <= max
   *          last = old(Q).last++
   *          arr[last] != null
   * 
   * @param x the object to add to the queue
   */
  public void enqueue(Object x) {
    arr[last] = x;
    last++;
    if (last == arr.length) {
      last = 0;
    }
    size++;
  }

  /**
   * Removes an object from the queue.
   *
   * @requires
   *           arr != null
   *           Q != null
   * 
   * @ensures
   *          size >= 0
   *          size != null
   *          old(Q).size >= size
   *          Q != null
   *          first >= old(Q).first
   * 
   * @return the removed object
   */
  public Object dequeue() {
    if (size == 0) {
      return null;
    }
    final Object x = arr[first];
    first++;
    if (first == arr.length) {
      first = 0;
    }
    size--;
    return x;
  }
}
