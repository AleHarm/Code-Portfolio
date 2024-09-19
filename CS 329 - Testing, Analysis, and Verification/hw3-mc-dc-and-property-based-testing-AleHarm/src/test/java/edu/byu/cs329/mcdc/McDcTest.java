package edu.byu.cs329.mcdc;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Test suite for the McDc static methods.
 */
public class McDcTest {

  @Nested
  @DisplayName("Problem 1 tests")
  class Problem1Tests{

    //I only need 5 tests, because with only 5 tests I can isolate each of the
    //booleans and test their true and false values without changing the others
    //This combined with the graph I drew, indicates that all paths to both true
    //and false results are tested. See my "Images" folder in my repo to view
    //my drawings for further justification and verification

    @Test
    @DisplayName("Returns false when A: false, B: true, C: false, D: false")
    void returnsFalseWhenAfalseBtrueCfalseDfalse(){

      boolean a = false;
      boolean b = true;
      boolean c = false;
      boolean d = false;

      assertFalse(McDc.problem1(a, b, c, d));
    }

    @Test
    @DisplayName("Returns true when A: true, B: true, C: false, D: false")
    void returnsTrueWhenAtrueBtrueCfalseDfalse(){

      boolean a = true;
      boolean b = true;
      boolean c = false;
      boolean d = false;

      assertTrue(McDc.problem1(a, b, c, d));
    }

    @Test
    @DisplayName("Returns false when A: true, B: false, C: false, D: false")
    void returnsFalseWhenAtrueBfalseCfalseDfalse(){

      boolean a = true;
      boolean b = false;
      boolean c = false;
      boolean d = false;

      assertFalse(McDc.problem1(a, b, c, d));
    }

    @Test
    @DisplayName("Returns false when A: false, B: false, C: false, D: true")
    void returnsFalseWhenAfalseBfalseCfalseDtrue(){

      boolean a = false;
      boolean b = false;
      boolean c = false;
      boolean d = true;

      assertFalse(McDc.problem1(a, b, c, d));
    }

    @Test
    @DisplayName("Returns true when A: false, B: false, C: true, D: true")
    void returnsTrueWhenAfalseBfalseCtrueDtrue(){

      boolean a = false;
      boolean b = false;
      boolean c = true;
      boolean d = true;

      assertTrue(McDc.problem1(a, b, c, d));
    }
  }

  //Since this functino is recursive, it's possible to test the If statement
  //multiple times with different inputs in the same test. For this reason
  //even though according to my table I needed 4 tests, we only actually need 
  //2 test inputs to test all 4 branches. See my "Images" folder in my repo to view
  //my drawings for further justification and verification

  @Nested
  @DisplayName("Problem 2 tests")
  class Problem2Tests{

    //Covers both true false false, and true true false
    @Test
    @DisplayName("Sorts array when given array {5, 3}")
    void sortsWhenGivenArray5Comma3(){
      
      int[] array = {5, 3};

      assertArrayEquals(new int[]{3, 5}, McDc.problem2(array, 0,1));
    }
    //Covers both true false true, and false false false
    @Test
    @DisplayName("Sorts array when given array {3, 5}")
    void sortsWhenGivenArray3Comma5(){
      
      int[] array = {3,5};

      assertArrayEquals(new int[]{3, 5}, McDc.problem2(array, 0,1));
    }
  }
}
