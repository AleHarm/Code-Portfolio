package edu.byu.cs329.typechecker;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.byu.cs329.utils.JavaSourceUtils;

@DisplayName("Tests for TypeCheckerBuilder")
public class TypeCheckBuilderTests {
  static final Logger log = LoggerFactory.getLogger(TypeCheckBuilderTests.class);

  private boolean getTypeChecker(final String fileName, List<DynamicNode> tests) {
    ASTNode compilationUnit = JavaSourceUtils.getAstNodeFor(this, fileName);
    SymbolTableBuilder symbolTableBuilder = new SymbolTableBuilder();
    SymbolTable symbolTable = symbolTableBuilder.getSymbolTable(compilationUnit);
    TypeCheckBuilder typeCheckerBuilder = new TypeCheckBuilder();
    return typeCheckerBuilder.getTypeChecker(symbolTable, compilationUnit, tests);
  }
 
  @TestFactory
  @DisplayName("Should prove type safe when given empty class")
  Stream<DynamicNode> should_proveTypeSafe_when_givenEmptyClass() {
    String fileName = "typeChecker/should_proveTypeSafe_when_givenEmptyClass.java";
    List<DynamicNode> tests = new ArrayList<>();
    boolean isTypeSafe = getTypeChecker(fileName, tests);
    DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
    tests.add((DynamicNode)test);
    return tests.stream();
  }

  @TestFactory
  @DisplayName("Should prove type safe when given empty method")
  Stream<DynamicNode> should_proveTypeSafe_when_givenEmptyMethod() {
    String fileName = "typeChecker/should_proveTypeSafe_when_givenEmptyMethod.java";
    List<DynamicNode> tests = new ArrayList<>();
    boolean isTypeSafe = getTypeChecker(fileName, tests);
    DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
    tests.add((DynamicNode)test);
    return tests.stream();
  }

  @TestFactory
  @DisplayName("Should prove type safe when given empty block")
  Stream<DynamicNode> should_proveTypeSafe_when_givenEmptyBlock() {
    String fileName = "typeChecker/should_proveTypeSafe_when_givenEmptyBlock.java";
    List<DynamicNode> tests = new ArrayList<>();
    boolean isTypeSafe = getTypeChecker(fileName, tests);
    DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
    tests.add((DynamicNode)test);
    return tests.stream();
  }

  @TestFactory
  @DisplayName("Should prove type safe when given variable declarations no inits")
  Stream<DynamicNode> should_proveTypeSafe_when_givenVariableDeclrationsNoInits() {
    String fileName = "typeChecker/should_proveTypeSafe_when_givenVariableDeclrationsNoInits.java";
    List<DynamicNode> tests = new ArrayList<>();
    boolean isTypeSafe = getTypeChecker(fileName, tests);
    DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
    tests.add((DynamicNode)test);
    return tests.stream();
  }

  @TestFactory
  @DisplayName("Should prove type safe when given variable declarations with compatible inits")
  Stream<DynamicNode> should_proveTypeSafe_when_givenVariableDeclrationsWithCompatibleInits() {
    String fileName = "typeChecker/should_proveTypeSafe_when_givenVariableDeclrationsWithCompatibleInits.java";
    List<DynamicNode> tests = new ArrayList<>();
    boolean isTypeSafe = getTypeChecker(fileName, tests);
    DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
    tests.add((DynamicNode)test);
    return tests.stream();
  }

  @TestFactory
  @DisplayName("Should not prove type safe when given bad inits")
  Stream<DynamicNode> should_NotProveTypeSafe_when_givenBadInits() {
    String fileName = "typeChecker/should_NotProveTypeSafe_when_givenBadInits.java";
    List<DynamicNode> tests = new ArrayList<>();
    boolean isTypeSafe = getTypeChecker(fileName, tests);

    // Toggle as desired
    // 
    // Option 1: mvn exec:java shows the details of the typeproof for visual inspection
    // return tests.stream();
    //
    // Option 2: test only isNotTypeSafe and show no details
    DynamicTest test = DynamicTest.dynamicTest("isNotTypeSafe", () -> assertFalse(isTypeSafe));
    return Arrays.asList((DynamicNode)test).stream();
  }
  
  @TestFactory
  @DisplayName("Should prove TypeSafe when given boolean with bang prefix")
  Stream<DynamicNode> should_proveTypeSafe_when_givenBooleanWithBangPrefix() {
    String fileName = "typeChecker/should_proveTypeSafe_when_givenBooleanWithBangPrefix.java";
    List<DynamicNode> tests = new ArrayList<>();
    boolean isTypeSafe = getTypeChecker(fileName, tests);
    DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
    tests.add((DynamicNode)test);
    return tests.stream();
  }

  @TestFactory
  @DisplayName("Should prove TypeSafe when given infix with boolean operands")
  Stream<DynamicNode> should_proveTypeSafe_when_givenInfixWithBooleanOperands() {
    String fileName = "typeChecker/should_proveTypeSafe_when_givenInfixWithBooleanOperands.java";
    List<DynamicNode> tests = new ArrayList<>();
    boolean isTypeSafe = getTypeChecker(fileName, tests);
    DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
    tests.add((DynamicNode)test);
    return tests.stream();
  }

  @TestFactory
  @DisplayName("Should prove TypeSafe when given infix with integer operands")
  Stream<DynamicNode> should_proveTypeSafe_when_givenInfixWithIntegerOperands() {
    String fileName = "typeChecker/should_proveTypeSafe_when_givenInfixWithIntegerOperands.java";
    List<DynamicNode> tests = new ArrayList<>();
    boolean isTypeSafe = getTypeChecker(fileName, tests);
    DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
    tests.add((DynamicNode)test);
    return tests.stream();
  }

  @TestFactory
  @DisplayName("Should prove TypeSafe when given boolean return statement")
  Stream<DynamicNode> should_proveTypeSafe_when_givenBooleanReturnStatement() {
    String fileName = "typeChecker/should_proveTypeSafe_when_givenBooleanReturnStatement.java";
    List<DynamicNode> tests = new ArrayList<>();
    boolean isTypeSafe = getTypeChecker(fileName, tests);
    DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
    tests.add((DynamicNode)test);
    return tests.stream();
  }

  @TestFactory
  @DisplayName("Should prove TypeSafe when given boolean expression statement for assignment")
  Stream<DynamicNode> should_proveTypeSafe_when_givenBooleanExpressionStatementForAssignment() {
    String fileName = "typeChecker/should_proveTypeSafe_when_givenBooleanExpressionStatementForAssignment.java";
    List<DynamicNode> tests = new ArrayList<>();
    boolean isTypeSafe = getTypeChecker(fileName, tests);
    DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
    tests.add((DynamicNode)test);
    return tests.stream();
  }

  @TestFactory
  @DisplayName("Should prove TypeSafe when given integer expression statement for assignment")
  Stream<DynamicNode> should_proveTypeSafe_when_givenIntgerExpressionStatementForAssignment() {
    String fileName = "typeChecker/should_proveTypeSafe_when_givenIntegerExpressionStatementForAssignment.java";
    List<DynamicNode> tests = new ArrayList<>();
    boolean isTypeSafe = getTypeChecker(fileName, tests);
    DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
    tests.add((DynamicNode)test);
    return tests.stream();
  }

  @TestFactory
  @DisplayName("Should prove TypeSafe when given if statement with safe insides")
  Stream<DynamicNode> should_proveTypeSafe_when_givenIfStatementWithSafeInsides() {
    String fileName = "typeChecker/should_proveTypeSafe_when_givenIfStatementWithSafeInsides.java";
    List<DynamicNode> tests = new ArrayList<>();
    boolean isTypeSafe = getTypeChecker(fileName, tests);
    DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
    tests.add((DynamicNode)test);
    return tests.stream();
  }

  @TestFactory
  @DisplayName("Should prove TypeSafe when given if statement with empty insides")
  Stream<DynamicNode> should_proveTypeSafe_when_givenIfStatementWithEmptyInsides() {
    String fileName = "typeChecker/should_proveTypeSafe_when_givenIfStatementWithEmptyInsides.java";
    List<DynamicNode> tests = new ArrayList<>();
    boolean isTypeSafe = getTypeChecker(fileName, tests);
    DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
    tests.add((DynamicNode)test);
    return tests.stream();
  }

  @TestFactory
  @DisplayName("Should prove TypeSafe when given while statement with safe insides")
  Stream<DynamicNode> should_proveTypeSafe_when_givenWhileStatementWithSafeInsides() {
    String fileName = "typeChecker/should_proveTypeSafe_when_givenWhileStatementWithSafeInsides.java";
    List<DynamicNode> tests = new ArrayList<>();
    boolean isTypeSafe = getTypeChecker(fileName, tests);
    DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
    tests.add((DynamicNode)test);
    return tests.stream();
  }

  @TestFactory
  @DisplayName("Should prove TypeSafe when given while statement with empty insides")
  Stream<DynamicNode> should_proveTypeSafe_when_givenWhileStatementWithEmptyInsides() {
    String fileName = "typeChecker/should_proveTypeSafe_when_givenWhileStatementWithEmptyInsides.java";
    List<DynamicNode> tests = new ArrayList<>();
    boolean isTypeSafe = getTypeChecker(fileName, tests);
    DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
    tests.add((DynamicNode)test);
    return tests.stream();
  }

  @TestFactory
  @DisplayName("Should prove TypeSafe when given field access assignment")
  Stream<DynamicNode> should_proveTypeSafe_when_givenFieldAccessAssignment() {
    String fileName = "typeChecker/should_proveTypeSafe_when_givenFieldAccessAssignment.java";
    List<DynamicNode> tests = new ArrayList<>();
    boolean isTypeSafe = getTypeChecker(fileName, tests);
    DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
    tests.add((DynamicNode)test);
    return tests.stream();
  }

  @TestFactory
  @DisplayName("Should not prove TypeSafe when given invalid field access assignment")
  Stream<DynamicNode> should_NotProveTypeSafe_when_givenInvalidFieldAccessAssignment() {
    String fileName = "typeChecker/should_NotProveTypeSafe_when_givenInvalidFieldAccessAssignment.java";
    List<DynamicNode> tests = new ArrayList<>();
    boolean isTypeSafe = getTypeChecker(fileName, tests);
    DynamicTest test = DynamicTest.dynamicTest("isNotTypeSafe", () -> assertFalse(isTypeSafe));
    tests.add((DynamicNode)test);
    return Arrays.asList((DynamicNode)test).stream();
  }

  @TestFactory
  @DisplayName("Should prove TypeSafe when given qualified name assignment")
  Stream<DynamicNode> should_proveTypeSafe_when_givenQualifiedNameAssignment() {
    String fileName = "typeChecker/should_proveTypeSafe_when_givenQualifiedNameAssignment.java";
    List<DynamicNode> tests = new ArrayList<>();
    boolean isTypeSafe = getTypeChecker(fileName, tests);
    DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
    tests.add((DynamicNode)test);
    return tests.stream();
  }

  @TestFactory
  @DisplayName("Should not prove TypeSafe when given invalid qualified name assignment")
  Stream<DynamicNode> should_NotProveTypeSafe_when_givenInvalidQualifiedNameAssignment() {
    String fileName = "typeChecker/should_NotProveTypeSafe_when_givenInvalidQualifiedNameAssignment.java";
    List<DynamicNode> tests = new ArrayList<>();
    boolean isTypeSafe = getTypeChecker(fileName, tests);
    DynamicTest test = DynamicTest.dynamicTest("isNotTypeSafe", () -> assertFalse(isTypeSafe));
    tests.add((DynamicNode)test);
    return Arrays.asList((DynamicNode)test).stream();
  }

  @TestFactory
  @DisplayName("Should prove TypeSafe when given method invocation assignment")
  Stream<DynamicNode> should_proveTypeSafe_when_givenMethodInvocationAssignment() {
    String fileName = "typeChecker/should_proveTypeSafe_when_givenMethodInvocationAssignment.java";
    List<DynamicNode> tests = new ArrayList<>();
    boolean isTypeSafe = getTypeChecker(fileName, tests);
    DynamicTest test = DynamicTest.dynamicTest("isTypeSafe", () -> assertTrue(isTypeSafe));
    tests.add((DynamicNode)test);
    return tests.stream();
  }

  @TestFactory
  @DisplayName("Should not prove TypeSafe when given invalid method invocation assignment")
  Stream<DynamicNode> should_NotProveTypeSafe_when_givenInvalidMethodInvocationAssignment() {
    String fileName = "typeChecker/should_NotProveTypeSafe_when_givenInvalidMethodInvocationAssignment.java";
    List<DynamicNode> tests = new ArrayList<>();
    boolean isTypeSafe = getTypeChecker(fileName, tests);
    DynamicTest test = DynamicTest.dynamicTest("isNotTypeSafe", () -> assertFalse(isTypeSafe));
    tests.add((DynamicNode)test);
    return Arrays.asList((DynamicNode)test).stream();
  }

  @Nested
  @DisplayName("Regression tests")
  class RegressionTests{

    private long getContainerCount(Stream<? extends DynamicNode> tests) {
      return tests.filter(t -> t instanceof DynamicContainer)
          .mapToLong(v -> 1 + getContainerCount(((DynamicContainer) v).getChildren())).sum();
    }

    private long getTestCount(Stream<? extends DynamicNode> tests) {
    
      return tests.mapToLong(node -> {
        if (node instanceof DynamicTest) {
          return 1;
        }
        return getTestCount(((DynamicContainer) node).getChildren());
      }).sum();
    }

    int countStatements(Stream<? extends DynamicNode> tests){

      return tests.mapToInt(node ->{
        if(node instanceof DynamicTest){
          return 0;
        }
        DynamicContainer container = (DynamicContainer)node;
        int numberStatements = countStatements(container.getChildren());
        var displayName = container.getDisplayName();
        var isStatement = displayName.matches("S\\d+(.*)");
        return numberStatements + (isStatement? 1: 0);
      }).sum();
    }

    @Nested
    @DisplayName("Tests Empty Class")
    class EmptyClassTests {

      String fileName = "typeChecker/emptyClass.java";

      @Test
      @DisplayName("Should create two containers when given an empty class")
      void Should_createTwoContainers_When_EmptyClass() {
        List<DynamicNode> tests = new ArrayList<>();
        getTypeChecker(fileName, tests);
        long containerCount = getContainerCount(tests.stream());
        Assertions.assertEquals(2, containerCount);
      }

      @Test
      @DisplayName("Should create two tests when given an empty class")
      void Should_createTwoTests_When_EmptyClass() {
        List<DynamicNode> tests = new ArrayList<>();
        getTypeChecker(fileName, tests);
        long testCount = getTestCount(tests.stream());
        Assertions.assertEquals(2, testCount);
      }
    }

    @Nested
    @DisplayName("Tests Method Invocation")
    class MethodInvocationTests {

      String fileName = "typeChecker/should_proveTypeSafe_when_givenMethodInvocationAssignment.java";

      @Test
      @DisplayName("Should prove TypeSafe and check one statement when given valid method invocation assignment")
      void should_proveTypeSafe_andCheckOneStatement_when_givenMethodInvocationAssignment() {
        List<DynamicNode> tests = new ArrayList<>();
        boolean isTypeSafe = getTypeChecker(fileName, tests);
        Assertions.assertTrue(isTypeSafe);
        Assertions.assertEquals(2, countStatements(tests.stream()));
      }

      @Test
      @DisplayName("Should create eleven tests when given valid method invocation assignment")
      void Should_createElevenTests_When_givenMethodInvocationAssignment() {
        List<DynamicNode> tests = new ArrayList<>();
        getTypeChecker(fileName, tests);
        long testCount = getTestCount(tests.stream());
        Assertions.assertEquals(11, testCount);
      }

      @Test
      @DisplayName("Should create two containers when given valid method invocation assignment")
      void Should_createTwoContainers_When_givenValidMethodInvocationAssignment() {
        List<DynamicNode> tests = new ArrayList<>();
        getTypeChecker(fileName, tests);
        long containerCount = getContainerCount(tests.stream());
        Assertions.assertEquals(11, containerCount);
      }
    }

    @Nested
    @DisplayName("Tests If Statements")
    class IfStatementTests {

      String fileName = "typeChecker/should_proveTypeSafe_when_givenIfStatementWithSafeInsides.java";

      @Test
      @DisplayName("Should prove TypeSafe and check two statements when given if statement with safe insides")
      void should_proveTypeSafe_andCheckTwoStatements_when_givenIfStatementWithSafeInsides() {
        List<DynamicNode> tests = new ArrayList<>();
        boolean isTypeSafe = getTypeChecker(fileName, tests);
        Assertions.assertTrue(isTypeSafe);
        Assertions.assertEquals(2, countStatements(tests.stream()));
      }

      @Test
      @DisplayName("Should create fourteen tests when given if statement with safe insides")
      void Should_createFourteenTests_When_givenIfStatementWithSafeInsides() {
        List<DynamicNode> tests = new ArrayList<>();
        getTypeChecker(fileName, tests);
        long testCount = getTestCount(tests.stream());
        Assertions.assertEquals(14, testCount);
      }

      @Test
      @DisplayName("Should create twelve containers when given if statement with safe insides")
      void Should_createTwelveContainers_When_givenIfStatementWithSafeInsides() {
        List<DynamicNode> tests = new ArrayList<>();
        getTypeChecker(fileName, tests);
        long containerCount = getContainerCount(tests.stream());
        Assertions.assertEquals(12, containerCount);
      }
    }

    @Nested
    @DisplayName("Tests While Statements")
    class WhileStatementTests {

      String fileName = "typeChecker/should_proveTypeSafe_when_givenWhileStatementWithSafeInsides.java";

      @Test
      @DisplayName("Should prove TypeSafe and check two statements when given while statement with safe insides")
      void should_proveTypeSafe_andCheckTwoStatements_when_givenWhileStatementWithSafeInsides() {
        List<DynamicNode> tests = new ArrayList<>();
        boolean isTypeSafe = getTypeChecker(fileName, tests);
        Assertions.assertTrue(isTypeSafe);
        Assertions.assertEquals(2, countStatements(tests.stream()));
      }

      @Test
      @DisplayName("Should create fourteen tests when given while statement with safe insides")
      void Should_createFourteenTests_When_givenWhileStatementWithSafeInsides() {
        List<DynamicNode> tests = new ArrayList<>();
        getTypeChecker(fileName, tests);
        long testCount = getTestCount(tests.stream());
        Assertions.assertEquals(14, testCount);
      }

      @Test
      @DisplayName("Should create twelve containers when given while statement with safe insides")
      void Should_createTwelveContainers_When_givenWhileStatementWithSafeInsides() {
        List<DynamicNode> tests = new ArrayList<>();
        getTypeChecker(fileName, tests);
        long containerCount = getContainerCount(tests.stream());
        Assertions.assertEquals(12, containerCount);
      }
    }

    @Nested
    @DisplayName("Tests Integer Expression Statement For Assignment")
    class IntegerExpressionStatementForAssignmentTests {

      String fileName = "typeChecker/should_proveTypeSafe_when_givenIntegerExpressionStatementForAssignment.java";

      @Test
      @DisplayName("Should prove TypeSafe and check two statements when given integer expression statement for assignment")
      void should_proveTypeSafe_andCheckTwoStatements_when_givenIntegerExpressionStatementForAssignment() {
        List<DynamicNode> tests = new ArrayList<>();
        boolean isTypeSafe = getTypeChecker(fileName, tests);
        Assertions.assertTrue(isTypeSafe);
        Assertions.assertEquals(2, countStatements(tests.stream()));
      }

      @Test
      @DisplayName("Should create fourteen tests when given integer expression statement for assignment")
      void Should_createEightTests_When_givenIntegerExpressionStatementForAssignment() {
        List<DynamicNode> tests = new ArrayList<>();
        getTypeChecker(fileName, tests);
        long testCount = getTestCount(tests.stream());
        Assertions.assertEquals(8, testCount);
      }

      @Test
      @DisplayName("Should create twelve containers when given integer expression statement for assignment")
      void Should_createEightContainers_When_givenIntegerExpressionStatementForAssignment() {
        List<DynamicNode> tests = new ArrayList<>();
        getTypeChecker(fileName, tests);
        long containerCount = getContainerCount(tests.stream());
        Assertions.assertEquals(8, containerCount);
      }
    }

    @Nested
    @DisplayName("Tests Integer Infix Operands")
    class InfixIntegerTests {

      String fileName = "typeChecker/should_proveTypeSafe_when_givenInfixWithIntegerOperands.java";

      @Test
      @DisplayName("Should prove TypeSafe and check one statement when given infix with integer operands")
      void should_proveTypeSafe_andCheckOneStatement_when_givenInfixWithIntegerOperands() {
        List<DynamicNode> tests = new ArrayList<>();
        boolean isTypeSafe = getTypeChecker(fileName, tests);
        Assertions.assertTrue(isTypeSafe);
        Assertions.assertEquals(1, countStatements(tests.stream()));
      }

      @Test
      @DisplayName("Should create ten tests when given infix with integer operands")
      void Should_createTenTests_When_givenInfixWithIntegerOperands() {
        List<DynamicNode> tests = new ArrayList<>();
        getTypeChecker(fileName, tests);
        long testCount = getTestCount(tests.stream());
        Assertions.assertEquals(10, testCount);
      }

      @Test
      @DisplayName("Should create nine containers when given infix with integer operands")
      void Should_createNineContainers_When_givenInfixWithIntegerOperands() {
        List<DynamicNode> tests = new ArrayList<>();
        getTypeChecker(fileName, tests);
        long containerCount = getContainerCount(tests.stream());
        Assertions.assertEquals(9, containerCount);
      }
    }

    @Nested
    @DisplayName("Tests Field Access Assignment")
    class FieldAccessTests {

      String fileName = "typeChecker/should_proveTypeSafe_when_givenFieldAccessAssignment.java";

      @Test
      @DisplayName("Should prove TypeSafe and check one statement when given field access assignment")
      void should_proveTypeSafe_andCheckOneStatement_when_givenFieldAccessAssignment() {
        List<DynamicNode> tests = new ArrayList<>();
        boolean isTypeSafe = getTypeChecker(fileName, tests);
        Assertions.assertTrue(isTypeSafe);
        Assertions.assertEquals(1, countStatements(tests.stream()));
      }

      @Test
      @DisplayName("Should create seven tests when given field access assignment")
      void Should_createSevenTests_When_givenFieldAccessAssignment() {
        List<DynamicNode> tests = new ArrayList<>();
        getTypeChecker(fileName, tests);
        long testCount = getTestCount(tests.stream());
        Assertions.assertEquals(7, testCount);
      }

      @Test
      @DisplayName("Should create seven containers when given field access assignment")
      void Should_createSevenContainers_When_givenFieldAccessAssignment() {
        List<DynamicNode> tests = new ArrayList<>();
        getTypeChecker(fileName, tests);
        long containerCount = getContainerCount(tests.stream());
        Assertions.assertEquals(7, containerCount);
      }
    }

    @Nested
    @DisplayName("Tests Qualified Name Assignment")
    class QualifiedNameAssignmentTests {

      String fileName = "typeChecker/should_proveTypeSafe_when_givenQualifiedNameAssignment.java";

      @Test
      @DisplayName("Should prove TypeSafe and check one statement when given qualified name assignment")
      void should_proveTypeSafe_andCheckOneStatement_when_givenQualifiedNameAssignment() {
        List<DynamicNode> tests = new ArrayList<>();
        boolean isTypeSafe = getTypeChecker(fileName, tests);
        Assertions.assertTrue(isTypeSafe);
        Assertions.assertEquals(1, countStatements(tests.stream()));
      }

      @Test
      @DisplayName("Should create seven tests when given qualified name assignment")
      void Should_createSevenTests_When_givenQualifiedNameAssignment() {
        List<DynamicNode> tests = new ArrayList<>();
        getTypeChecker(fileName, tests);
        long testCount = getTestCount(tests.stream());
        Assertions.assertEquals(7, testCount);
      }

      @Test
      @DisplayName("Should create seven containers when given qualified name assignment")
      void Should_createSevenContainers_When_givenQualifiedNameAssignment() {
        List<DynamicNode> tests = new ArrayList<>();
        getTypeChecker(fileName, tests);
        long containerCount = getContainerCount(tests.stream());
        Assertions.assertEquals(7, containerCount);
      }
    }

    @Nested
    @DisplayName("Tests Boolean Return Statement")
    class BooleanReturnTests {

      String fileName = "typeChecker/should_proveTypeSafe_when_givenBooleanReturnStatement.java";

      @Test
      @DisplayName("Should prove TypeSafe and check one statement when given boolean return statement")
      void should_proveTypeSafe_andCheckOneStatement_when_givenBooleanReturnStatement() {
        List<DynamicNode> tests = new ArrayList<>();
        boolean isTypeSafe = getTypeChecker(fileName, tests);
        Assertions.assertTrue(isTypeSafe);
        Assertions.assertEquals(1, countStatements(tests.stream()));
      }

      @Test
      @DisplayName("Should create six tests when given boolean return statement")
      void Should_createSixTests_When_givenBooleanReturnStatement() {
        List<DynamicNode> tests = new ArrayList<>();
        getTypeChecker(fileName, tests);
        long testCount = getTestCount(tests.stream());
        Assertions.assertEquals(6, testCount);
      }

      @Test
      @DisplayName("Should create six containers when given boolean return statement")
      void Should_createSixContainers_When_givenBooleanReturnStatement() {
        List<DynamicNode> tests = new ArrayList<>();
        getTypeChecker(fileName, tests);
        long containerCount = getContainerCount(tests.stream());
        Assertions.assertEquals(6, containerCount);
      }
    }

    @Nested
    @DisplayName("Tests Bang Prefix")
    class BangPrefixTests {

      String fileName = "typeChecker/should_proveTypeSafe_when_givenBooleanWithBangPrefix.java";

      @Test
      @DisplayName("Should prove TypeSafe and check one statement when given boolean with bang prefix")
      void should_proveTypeSafe_andCheckOneStatement_when_givenBooleanWithBangPrefix() {
        List<DynamicNode> tests = new ArrayList<>();
        boolean isTypeSafe = getTypeChecker(fileName, tests);
        Assertions.assertTrue(isTypeSafe);
        Assertions.assertEquals(1, countStatements(tests.stream()));
      }

      @Test
      @DisplayName("Should create seven tests when given boolean with bang prefix")
      void Should_createSevenTests_When_givenBooleanWithBangPrefix() {
        List<DynamicNode> tests = new ArrayList<>();
        getTypeChecker(fileName, tests);
        long testCount = getTestCount(tests.stream());
        Assertions.assertEquals(7, testCount);
      }

      @Test
      @DisplayName("Should create eight containers when given boolean with bang prefix")
      void Should_createEightContainers_When_givenBooleanWithBangPrefix() {
        List<DynamicNode> tests = new ArrayList<>();
        getTypeChecker(fileName, tests);
        long containerCount = getContainerCount(tests.stream());
        Assertions.assertEquals(8, containerCount);
      }
    }
  }
}








