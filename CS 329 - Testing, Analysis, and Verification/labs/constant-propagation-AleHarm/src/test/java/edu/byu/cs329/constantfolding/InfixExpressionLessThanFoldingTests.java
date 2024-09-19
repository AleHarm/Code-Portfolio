package edu.byu.cs329.constantfolding;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;

import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.byu.cs329.TestUtils;

@DisplayName("Tests for the less than infix expression folder with number literal operands")
public class InfixExpressionLessThanFoldingTests {

  Folding folderUnderTest = null;

  @BeforeEach
  void beforeEach() {

    folderUnderTest = new InfixExpressionLessThanFolding();
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
    String rootName = "foldingInputs/lessThanInfix/should_notFold_when_givenNoNumberLiteralExpression.java";
    String expectedName = "foldingInputs/lessThanInfix/should_notFold_when_givenNoNumberLiteralExpression-KEY.java";
    TestUtils.assertDidNotFold(this, rootName, expectedName, folderUnderTest);
  }

  @Test
  @DisplayName("Should not fold anything when infix expression is not less than")
  void should_NotFoldAnything_when_InfixExpressionIsNotLessThan() {
    String rootName = "foldingInputs/lessThanInfix/should_NotFoldAnything_when_InfixExpressionIsNotLessThan.java";
    String expectedName = "foldingInputs/lessThanInfix/should_NotFoldAnything_when_InfixExpressionIsNotLessThan-KEY.java";
    TestUtils.assertDidNotFold(this, rootName, expectedName, folderUnderTest);
  }

  @Test
  @DisplayName("Should fold and reduce to false when less than infix with 6 and 4")
  void should_foldAndReduceToFalse_when_lessThanInfixWith6And4() {

    String rootName = "foldingInputs/lessThanInfix/should_foldAndReduceToFalse_when_lessThanInfixWith6And4.java";
    String expectedName = "foldingInputs/lessThanInfix/should_foldAndReduceToFalse_when_lessThanInfixWith6And4-KEY.java";
    TestUtils.assertDidFold(this, rootName, expectedName, folderUnderTest);
  }

  @Test
  @DisplayName("Should fold and reduce to true when less than infix with 4 and 6")
  void should_foldAndReduceToTrue_when_lessThanInfixWith4And6() {

    String rootName = "foldingInputs/lessThanInfix/should_foldAndReduceToTrue_when_lessThanInfixWith4And6.java";
    String expectedName = "foldingInputs/lessThanInfix/should_foldAndReduceToTrue_when_lessThanInfixWith4And6-KEY.java";
    TestUtils.assertDidFold(this, rootName, expectedName, folderUnderTest);
  }
}