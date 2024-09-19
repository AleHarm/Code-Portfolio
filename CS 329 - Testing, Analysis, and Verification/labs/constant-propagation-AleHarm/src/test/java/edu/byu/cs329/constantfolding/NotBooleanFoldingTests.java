package edu.byu.cs329.constantfolding;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;

import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.byu.cs329.TestUtils;

@DisplayName("Tests for folding not boolean expressions")
public class NotBooleanFoldingTests {

  Folding folderUnderTest = null;

  @BeforeEach
  void beforeEach() {

    folderUnderTest = new NotBooleanFolding();
  }

  @Test
  @DisplayName("Should throw RuntimeException when root is null")
  void should_ThrowRuntimeException_when_RootIsNull() {

    assertThrows(RuntimeException.class, () -> {

      folderUnderTest.fold(null);
    });
  }

  @Test
  @DisplayName("Should throw RuntimeException when root is not a CompilationUnit and has no parent")
  void should_ThrowRuntimeException_when_RootIsNotACompilationUnitAndHasNoParent() {
    assertThrows(RuntimeException.class, () -> {
      URI uri = TestUtils.getUri(this, "");
      ASTNode compilationUnit = TestUtils.getCompilationUnit(uri);
      ASTNode root = compilationUnit.getAST().newNullLiteral();
      folderUnderTest.fold(root);
    });
  }

  @Test
  @DisplayName("Should not fold when given statement with no not prefix")
  void should_notFold_when_GivenStatementWithNoNotPrefix() {
    String rootName = "foldingInputs/notBoolean/should_notFold_when_GivenStatementWithNoNotPrefix.java";
    String expectedName = "foldingInputs//notBoolean//should_notFold_when_GivenStatementWithNoNotPrefix-KEY.java";
    TestUtils.assertDidNotFold(this, rootName, expectedName, folderUnderTest);
  }

  @Test
  @DisplayName("Should fold when given true statement with not prefix")
  void should_fold_when_GivenTrueStatementWithNotPrefix() {
    String rootName = "foldingInputs/notBoolean/should_fold_when_GivenTrueStatementWithNotPrefix.java";
    String expectedName = "foldingInputs//notBoolean//should_fold_when_GivenTrueStatementWithNotPrefix-KEY.java";
    TestUtils.assertDidFold(this, rootName, expectedName, folderUnderTest);
  }

  @Test
  @DisplayName("Should fold when given false statement with not prefix")
  void should_fold_when_GivenFalseStatementWithNotPrefix() {
    String rootName = "foldingInputs/notBoolean/should_fold_when_GivenFalseStatementWithNotPrefix.java";
    String expectedName = "foldingInputs//notBoolean//should_fold_when_GivenFalseStatementWithNotPrefix-KEY.java";
    TestUtils.assertDidFold(this, rootName, expectedName, folderUnderTest);
  }
}
