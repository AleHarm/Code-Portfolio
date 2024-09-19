package misc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FTests {

  static int MAX = 45;

  static Object[] fullArray = new Object[MAX];
  static Object[] validArray = new Object[MAX];
  Object[] nullArray = null;
  Object validX = 100;
  Object nullX = null;

  @BeforeAll
  @DisplayName("Setup")
  static void Setup() {

    // Fill fullArray
    for (int i = 0; i < MAX; i++) {

      fullArray[i] = i;
    }

    // Fill halfFullArray halfway
    for (int i = 0; i < Math.floor(MAX / 2); i++) {

      validArray[i] = i;
    }
  }

  @Test
  @DisplayName("Normal inputs")
  void NormalInputsSuceeds() {
    Misc.ff(validX, validArray);
    int numXInArray = 0;
    for (int i = 0; i < validArray.length; i++) {

      if (validArray[i] == validX) {

        numXInArray++;
      }
    }

    assertEquals(numXInArray, 1);
  }

  @Test
  @DisplayName("Try to add X twice, array only contains 1 x")
  void DoubleAddHasSingleX() {
    Misc.ff(validX, validArray);
    Misc.ff(validX, validArray);
    int numXInArray = 0;
    for (int i = 0; i < validArray.length; i++) {

      if (validArray[i] == validX) {

        numXInArray++;
      }
    }

    assertEquals(numXInArray, 1);
  }

  @Test
  @DisplayName("Try to add X to a full array")
  void FullArrayThrowsError() {
    try {
      Misc.ff(validX, fullArray);
      fail();
    } catch (Exception e) {

    }
  }

  @Test
  @DisplayName("Try to add X to a null array")
  void NullArrayThrowsError() {
    try {
      Misc.ff(validX, nullArray);
      fail();
    } catch (Exception e) {

    }
  }

  @Test
  @DisplayName("Try to add null X to a valid array")
  void NullXThrowsError() {
    try {
      Misc.ff(nullX, validArray);
      fail();
    } catch (Exception e) {

    }
  }
}
