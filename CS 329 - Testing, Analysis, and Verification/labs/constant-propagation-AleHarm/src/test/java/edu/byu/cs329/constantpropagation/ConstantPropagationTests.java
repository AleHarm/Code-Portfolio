package edu.byu.cs329.constantpropagation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


@DisplayName("Tests for ConstantPropagation")
public class ConstantPropagationTests {

  private boolean compareFiles(String expected, String actual) {
    File expectedFile = new File(expected);
    File actualFile = new File(actual);
    Path expectedPath = Paths.get(expectedFile.toURI());
    Path actualPath = Paths.get(actualFile.toURI());

    try{
    String expectedContent = new String(Files.readAllBytes(expectedPath)).replaceAll("\\s", "");
    String actualContent = new String(Files.readAllBytes(actualPath)).replaceAll("\\s", "");

    return expectedContent.equals(actualContent);
    }catch (IOException e) {
      return false;
    }
  }

  @Nested
  class BlackBoxTests{
  
    @Test
    @DisplayName("Should propagate when given assignments and declarations with addition")
    public void should_propagate_when_givenAssignmentsAndDeclarationsWithAddition(){
      String testFileString = "src/test/resources/cpInputs/should_propagate_when_givenAssignmentsAndDeclarationsWithAddition.java";
      String testFileKeyString = "src/test/resources/cpInputs/should_propagate_when_givenAssignmentsAndDeclarationsWithAddition_KEY.java";
      String outputFile = "testOutputFiles/output00.java";
      String[] args = {testFileString, outputFile};
      ConstantPropagation.main(args);
      assertTrue(compareFiles(testFileKeyString, outputFile));
    }
  
    @Test
    @DisplayName("Should propagate when given assignments and declarations with addition and an If statement")
    public void should_propagate_when_givenAssignmentsAndDeclarationsWithAdditionAndAnIfStatement(){
      String testFileString = "src/test/resources/cpInputs/should_propagate_when_givenAssignmentsAndDeclarationsWithAdditionAndAnIfStatement.java";
      String testFileKeyString = "src/test/resources/cpInputs/should_propagate_when_givenAssignmentsAndDeclarationsWithAdditionAndAnIfStatement_KEY.java";
      String outputFile = "testOutputFiles/output01.java";
      String[] args = {testFileString, outputFile};
      ConstantPropagation.main(args);
      assertTrue(compareFiles(testFileKeyString, outputFile));
    }
  
    @Test
    @DisplayName("Should not propagate when given assignments and declarations with subtraction")
    public void should_notPropagate_when_givenAssignmentsAndDeclarationsWithSubtraction(){
      String testFileString = "src/test/resources/cpInputs/should_notPropagate_when_givenAssignmentsAndDeclarationsWithSubtraction.java";
      String testFileKeyString = "src/test/resources/cpInputs/should_notPropagate_when_givenAssignmentsAndDeclarationsWithSubtraction_KEY.java";
      String outputFile = "testOutputFiles/output02.java";
      String[] args = {testFileString, outputFile};
      ConstantPropagation.main(args);
      assertFalse(compareFiles(testFileKeyString, outputFile));
    }
  }

  @Nested
  class CoverageTests{
    @Test
    @DisplayName("Good inputs coverage")
    public void goodInputsCoverage(){

      String testFileString = "src/test/resources/cpInputs/goodInputsCoverage.java";
      String testFileKeyString = "src/test/resources/cpInputs/goodInputsCoverage_KEY.java";
      String outputFile = "testOutputFiles/output03.java";
      String[] args = {testFileString, outputFile};
      ConstantPropagation.main(args);
      assertTrue(compareFiles(testFileKeyString, outputFile));
    }

    @Test
    @DisplayName("Bad args")
    public void badArgs(){

      String[] badArgs = {""};
      ConstantPropagation.main(badArgs);
    }
  }
}
