package com.makotojava.learn.hellojunit5;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JUnit 5 (with JUnitPlatform.class)
 * 
 * Class-level Exercises:
 * <ol>
 * <li>Enable Eclipse JUnit support for this test class using the
 * {@link org.junit.runner.RunWith @RunWith} and
 * {@link org.junit.platform.runner.JUnitPlatform
 * JUnitPlatform} class.</li>
 * <li>Give the class a cool
 * {@link org.junit.jupiter.api.DisplayName @DisplayName} that shows up in the
 * JUnit test
 * report.</li>
 * </ol>
 *
 */

@RunWith(JUnitPlatform.class)
@DisplayName("Dank Tests")
public class JUnit5AppTest {

  // Create a JDK Logger here
  private static final Logger log = LoggerFactory.getLogger(JUnit5AppTest.class);

  // Create a fixture for the class under test
  private App classUnderTest;

  // Do something before ANY test is run in this class
  @BeforeAll
  public static void init() {
    log.info("Doing something before everything else");
  }

  // Do something after ALL tests in this class are run
  @AfterAll
  public static void done() {
    log.info("The last thing to be done");
  }

  // Create an instance of the test class before each @Test method is executed
  @BeforeEach
  public void setUp() throws Exception {
    log.info("Setting stuff up");
    classUnderTest = new App();
  }

  // Destroy reference to the instance of the test class after each @Test method
  // is executed
  @AfterEach
  public void destruction() throws Exception {
    log.info("Hulk smash, no more class");
    classUnderTest = null;
  }

  // Disabled test
  @Test
  @Disabled
  @DisplayName("It don't work")
  void testNotRun() {
    log.info("It done been disabled");
  }

  /**
   * testAdd() - Exercises:
   * <ol>
   * <li>Tell JUnit this method is a test method.</li>
   * <li>Give it a cool display name for the test report.</li>
   * <li>The reference to the class under test cannot be null. If it is, the test
   * should fail.</li>
   * <li>Create a group of three tests of the add methods with the following
   * arrays of positive numbers:
   * <ol>
   * <li>1, 2, 3, 4</li>
   * <li>20, 934, 110</li>
   * <li>2, 4, 6</li>
   * </ol>
   * Ensure the actual sum matches the expected sum for each group of numbers.
   * Make sure that all groups of numbers are
   * tested (i.e., if one fails, it should not fail the test method). Hint: use
   * {@link org.junit.jupiter.api.Assertions#assertAll(org.junit.jupiter.api.function.Executable...)
   * assertAll()}
   * </ol>
   */

  @Test
  @DisplayName("Positive numbers")
  public void testAdd() {

    assertNotNull(classUnderTest);

    assertAll(
        () -> {
          long[] numbersToSum = { 1, 2, 3, 4 };
          long expectedSum = 10;
          long actualSum = classUnderTest.add(numbersToSum);
          assertEquals(expectedSum, actualSum);
        },
        () -> {
          long[] numbersToSum = new long[] { 20, 934, 110 };
          long expectedSum = 1064;
          long actualSum = classUnderTest.add(numbersToSum);
          assertEquals(expectedSum, actualSum);
        },
        () -> {
          long[] numbersToSum = new long[] { 2, 4, 6 };
          long expectedSum = 12;
          long actualSum = classUnderTest.add(numbersToSum);
          assertEquals(expectedSum, actualSum);
        });
  }

  /**
   * Class-level Exercises:
   * <ol>
   * <li>Make this class a nested test class (hint: use
   * {@link org.junit.jupiter.api.Nested @Nested}).
   * <li>Give the class a cool
   * {@link org.junit.jupiter.api.DisplayName @DisplayName} that shows up in the
   * JUnit test
   * report.</li>
   * <li>Create an instance of the {@link com.makotojava.learn.hellojunit5.App
   * App} class specifically for this nested
   * class:
   * <ul>
   * <li>Set the <code>classUnderTest</code> variable in a method called
   * <code>setUp()</code> that runs before the test
   * method does (hint: use
   * {@link org.junit.jupiter.api.BeforeEach @BeforeEach})</li>
   * </ul>
   * <li>Set the <code>classUnderTest</code> variable to null in a method called
   * <code>tearDown()</code> that runs after
   * the
   * test method does (hint: use
   * {@link org.junit.jupiter.api.BeforeEach @AfterEach})</li>
   * </ol>
   * 
   */

  @Nested
  @DisplayName("Negative numbers")
  class NegativeNumbersTest {

    private App classUnderTest;

    @BeforeEach
    public void setUp() throws Exception {
      classUnderTest = new App();
    }

    @AfterEach
    public void tearDown() throws Exception {
      classUnderTest = null;
    }

    @Test
    @DisplayName("Negative numbers tests")
    public void testAdd() {
      assertNotNull(classUnderTest);
      assertAll(
          () -> {
            long[] numbersToSum = { -1, -2, -3, -4 };
            long expectedSum = -10;
            long actualSum = classUnderTest.add(numbersToSum);
            assertEquals(expectedSum, actualSum);
          },
          () -> {
            long[] numbersToSum = { -20, -934, -110 };
            long expectedSum = -1064;
            long actualSum = classUnderTest.add(numbersToSum);
            assertEquals(expectedSum, actualSum);
          },
          () -> {
            long[] numbersToSum = new long[] { -2, -4, -6 };
            long expectedSum = -12;
            long actualSum = classUnderTest.add(numbersToSum);
            assertEquals(expectedSum, actualSum);
          });
    }
  }

  /**
   * Class-level Exercises:
   * <ol>
   * <li>Make this class a nested test class (hint: use
   * {@link org.junit.jupiter.api.Nested @Nested}).
   * <li>Give the class a cool
   * {@link org.junit.jupiter.api.DisplayName @DisplayName} that shows up in the
   * JUnit test
   * report.</li>
   * </ol>
   * 
   */
  @Nested
  @DisplayName("Positive and negative numbers")
  class PositiveAndNegativeNumbersTest {

    @Test
    @DisplayName("Positive and negative numbers tests")
    public void testAdd() {
      assertNotNull(classUnderTest);
      assertAll(
          () -> {
            long[] numbersToSum = { -1, 2, -3, 4 };
            long expectedSum = 2;
            long actualSum = classUnderTest.add(numbersToSum);
            assertEquals(expectedSum, actualSum);
          },
          () -> {
            long[] numbersToSum = { -20, 934, -110 };
            long expectedSum = 804;
            long actualSum = classUnderTest.add(numbersToSum);
            assertEquals(expectedSum, actualSum);
          },
          () -> {
            long[] numbersToSum = new long[] { -2, -4, 6 };
            long expectedSum = 0;
            long actualSum = classUnderTest.add(numbersToSum);
            assertEquals(expectedSum, actualSum);
          });
    }

    @Test
    @DisplayName("Only runs Fridays")
    public void testAdd_OnlyOnFriday() {
      assertNotNull(classUnderTest);
      LocalDateTime ldt = LocalDateTime.now();
      assumeTrue(ldt.getDayOfWeek().getValue() == 5);
      assumeTrue(ldt.getDayOfWeek().getValue() == 5);
      long[] operands = { 1, 2, 3, 4, 5 };
      long expectedSum = 15;
      long actualSum = classUnderTest.add(operands);
      assertEquals(expectedSum, actualSum);
    }

    @Test
    @DisplayName("Heck if I know what Lambda is")
    public void testAdd_OnlyOnFriday_WithLambda() {
      assertNotNull(classUnderTest);
      LocalDateTime ldt = LocalDateTime.now();
      assumingThat(ldt.getDayOfWeek().getValue() == 5,
          () -> {
            long[] operands = { 1, 2, 3, 4, 5 };
            long expectedSum = 15;
            long actualSum = classUnderTest.add(operands);
            assertEquals(expectedSum, actualSum);
          });
    }

  }

  /**
   * Class-level Exercises:
   * <ol>
   * <li>Make this class a nested test class (hint: use
   * {@link org.junit.jupiter.api.Nested @Nested}).
   * <li>Give the class a cool
   * {@link org.junit.jupiter.api.DisplayName @DisplayName} that shows up in the
   * JUnit test
   * report.</li>
   * </ol>
   * 
   */
  @Nested
  @DisplayName("One operand")
  class JUnit5AppSingleOperandTest {

    @Test
    @DisplayName("Positive")
    public void testAdd_NumbersGt0() {
      assertNotNull(classUnderTest);
      assertAll(
          () -> {
            long[] numbersToSum = { 1 };
            long expectedSum = 1;
            long actualSum = classUnderTest.add(numbersToSum);
            assertEquals(expectedSum, actualSum);
          },
          () -> {
            long[] numbersToSum = { 0 };
            long expectedSum = 0;
            long actualSum = classUnderTest.add(numbersToSum);
            assertEquals(expectedSum, actualSum);
          });
    }

    @Test
    @DisplayName("Negative")
    public void testAdd_NumbersLt0() {
      assertNotNull(classUnderTest);
      assertAll(
          () -> {
            long[] numbersToSum = { -1 };
            long expectedSum = -1;
            long actualSum = classUnderTest.add(numbersToSum);
            assertEquals(expectedSum, actualSum);
          },
          () -> {
            long[] numbersToSum = { -10 };
            long expectedSum = -10;
            long actualSum = classUnderTest.add(numbersToSum);
            assertEquals(expectedSum, actualSum);
          });
    }
  }

  /**
   * Class-level Exercises:
   * <ol>
   * <li>Make this class a nested test class (hint: use
   * {@link org.junit.jupiter.api.Nested @Nested}).
   * <li>Give the class a cool
   * {@link org.junit.jupiter.api.DisplayName @DisplayName} that shows up in the
   * JUnit test
   * report.</li>
   * </ol>
   * 
   */
  @Nested
  @DisplayName("No operands")
  class JUnit5AppZeroOperandsTest {

    @Test()
    @DisplayName("Abosultely nothing")
    public void testAdd_ZeroOperands_EmptyArgument() {
      assertNotNull(classUnderTest);
      long[] numbersToSum = {};
      assertThrows(IllegalArgumentException.class, () -> classUnderTest.add(numbersToSum));
    }

    @Test
    @DisplayName("If it's null")
    public void testAdd_ZeroOperands_NullArgument() {
      assertNotNull(classUnderTest);
      long[] numbersToSum = null;
      Throwable expectedException = assertThrows(IllegalArgumentException.class,
          () -> classUnderTest.add(numbersToSum));
      assertEquals("Bad bad", expectedException.getLocalizedMessage());
    }

  }
}
