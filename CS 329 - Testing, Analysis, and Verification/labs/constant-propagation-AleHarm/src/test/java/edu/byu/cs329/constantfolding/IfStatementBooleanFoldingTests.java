package edu.byu.cs329.constantfolding;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;

import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.byu.cs329.TestUtils;

@DisplayName("Tests for folding if statements with boolean literal expressions")
public class IfStatementBooleanFoldingTests {

  Folding folderUnderTest = null;

  @BeforeEach
  void beforeEach() {

    folderUnderTest = new IfStatementBooleanFolding();
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
  @DisplayName("Should not fold anything when given no if statement")
  void should_NotFoldAnything_when_GivenNoIfStatement() {
    String rootName = "foldingInputs/ifStatementBoolean/should_NotFoldAnything_when_GivenNoIfStatement.java";
    String expectedName = "foldingInputs/ifStatementBoolean/should_NotFoldAnything_when_GivenNoIfStatement-KEY.java";
    TestUtils.assertDidNotFold(this, rootName, expectedName, folderUnderTest);
  }

  @Test
  @DisplayName("Should not fold anything when expression is not boolean literal")
  void should_NotFoldAnything_when_ExpressionIsNotBooleanLiteral() {
    String rootName = "foldingInputs/ifStatementBoolean/should_NotFoldAnything_when_ExpressionIsNotBooleanLiteral.java";
    String expectedName = "foldingInputs/ifStatementBoolean/should_NotFoldAnything_when_ExpressionIsNotBooleanLiteral-KEY.java";
    TestUtils.assertDidNotFold(this, rootName, expectedName, folderUnderTest);
  }

  @Test
  @DisplayName("Should fold when given false boolean if statement")
  void should_fold_when_GivenFalseBooleanIfStatement() {
    String rootName = "foldingInputs/ifStatementBoolean/should_fold_when_GivenFalseBooleanIfStatement.java";
    String expectedName = "foldingInputs/ifStatementBoolean/should_fold_when_GivenFalseBooleanIfStatement-KEY.java";
    TestUtils.assertDidFold(this, rootName, expectedName, folderUnderTest);
  }
}
