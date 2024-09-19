package edu.byu.cs329.constantfolding;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import edu.byu.cs329.utils.JavaSourceUtils;

@DisplayName("System test for constant folding")
public class ConstantFoldingTests {

  ConstantFolding folderUnderTest = null;

  @Test
  @DisplayName("Should fold when given one of each folding type")
  void should_fold_when_givenOneOfEachFoldingType() {
    String rootName = "src/test/resources/foldingInputs/constantFolding/should_fold_when_givenOneOfEachFoldingType.java";
    String constantFoldingOutput = "ConstantFoldingOutput.java";
    File expectedName = new File(
        "src/test/resources/foldingInputs/constantFolding/should_fold_when_givenOneOfEachFoldingType-KEY.java");

    folderUnderTest = new ConstantFolding();
    String[] args = {rootName, "ConstantFoldingOutput.java"};

    ConstantFolding.main(args);
    
    File constantFoldingOutputFile = new File(constantFoldingOutput);
    try {
      assertTrue(compareFiles(expectedName, constantFoldingOutputFile));
    } catch (IOException e) {
      fail("Failed to compare files: " + e.getMessage());
    }
  }

  @Test
  @DisplayName("Should fail when given bad arguments")
  void should_fail_when_givenBadArgs() {
      String[] args = {""};

      try {
        ConstantFolding.main(args);
        fail();
      } catch (Exception e) {
      }
  }

  private boolean compareFiles(File expected, File actual) throws IOException {
    Path expectedPath = Paths.get(expected.toURI());
    Path actualPath = Paths.get(actual.toURI());

    String expectedContent = new String(Files.readAllBytes(expectedPath)).replaceAll("\\s", "");
    String actualContent = new String(Files.readAllBytes(actualPath)).replaceAll("\\s", "");

    return expectedContent.equals(actualContent);
  }
}
