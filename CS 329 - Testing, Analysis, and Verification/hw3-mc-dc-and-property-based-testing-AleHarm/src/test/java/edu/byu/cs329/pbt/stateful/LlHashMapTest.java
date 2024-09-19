package edu.byu.cs329.pbt.stateful;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Test suite for the hash map.
 */
public class LlHashMapTest {

  @Test
  public void testRandomActions() {
    int numberOfActions = 100;
    Random random = new Random();

    Map<Integer, Integer> map1 = new HashMap<>();
    LlHashMap llMap = new LlHashMap(numberOfActions);

    for (int i = 0; i < numberOfActions; i++) {
      int key = random.nextInt(100);
      int value = random.nextInt(100);

      performAction(map1, key, value);
      performActionToLLHash(llMap, key, value);
    }

    assertTrue(llMap.equals(map1));
  }

  private void performAction(Map<Integer, Integer> map, int key, int value) {
    Random random = new Random();
    int action = random.nextInt(3);

    switch (action) {
      case 0:
        map.put(key, value);
        break;
      case 1:
        map.remove(key);
        break;
      case 2:
        map.putIfAbsent(key, value);
        map.get(key);
        break;
    }
  }
  private void performActionToLLHash(LlHashMap map, int key, int value) {
    Random random = new Random();
    int action = random.nextInt(3);

    switch (action) {
    case 0:
      map.put(key, value);
      break;
    case 1:
      map.remove(key);
      break;
    case 2:
      if(!map.contains(key)){
        map.put(key, value);
      }
      map.get(key);
      break;
    }
  }
}
