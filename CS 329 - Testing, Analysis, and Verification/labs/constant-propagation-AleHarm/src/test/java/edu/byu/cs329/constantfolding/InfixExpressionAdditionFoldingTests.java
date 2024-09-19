package edu.byu.cs329.constantfolding;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;

import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.byu.cs329.TestUtils;

@DisplayName("Tests for the addition infix expression folder with number literal operands")
public class InfixExpressionAdditionFoldingTests {

  Folding folderUnderTest = null;

  @BeforeEach
  void beforeEach() {

    folderUnderTest = new InfixExpressionAdditionFolding();
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
  @DisplayName("Should not fold anything when there are no number literals")
  void should_NotFoldAnything_when_ThereAreNoNumberLiterals() {
    String rootName = "foldingInputs/additionInfix/should_notFold_when_givenNoNumberLiteralExpression.java";
    String expectedName = "foldingInputs/additionInfix/should_notFold_when_givenNoNumberLiteralExpression-KEY.java";
    TestUtils.assertDidNotFold(this, rootName, expectedName, folderUnderTest);
  }

  @Test
  @DisplayName("Should not fold anything when infix expression is not addition")
  void should_NotFoldAnything_when_InfixExpressionIsNotAddition() {
    String rootName = "foldingInputs/additionInfix/should_NotFoldAnything_when_InfixExpressionIsNotAddition.java";
    String expectedName = "foldingInputs/additionInfix/should_NotFoldAnything_when_InfixExpressionIsNotAddition-KEY.java";
    TestUtils.assertDidNotFold(this, rootName, expectedName, folderUnderTest);
  }

  @Test
  @DisplayName("Should fold and reduce to 10 when addition infix with 6 and 4")
  void should_foldAndReduceTo10_when_additionInfixWith6And4() {

    String rootName = "foldingInputs/additionInfix/should_foldAndReduceTo10_when_additionInfixWith6And4.java";
    String expectedName = "foldingInputs/additionInfix/should_foldAndReduceTo10_when_additionInfixWith6And4-KEY.java";
    TestUtils.assertDidFold(this, rootName, expectedName, folderUnderTest);
  }
}