package edu.byu.cs329.cfg;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import edu.byu.cs329.TestUtils;

@DisplayName("Tests for ControlFlowBuilder")
public class ControlFlowBuilderTests {
  ControlFlowGraphBuilder unitUnderTest = null;
  ControlFlowGraph controlFlowGraph = null;
  StatementTracker statementTracker = null;

  @BeforeEach
  void beforeEach() {
    unitUnderTest = new ControlFlowGraphBuilder();
  }

  void init(String fileName) {
    ASTNode node = TestUtils.getASTNodeFor(this, fileName);
    List<ControlFlowGraph> cfgList = unitUnderTest.build(node);
    assertEquals(1, cfgList.size());
    controlFlowGraph = cfgList.get(0);
    statementTracker = new StatementTracker(node);
  }

  @Test
  @Tag("MethodDeclaration")
  @DisplayName("Should set start and end same when empty method declaration")
  void should_SetStartAndEndSame_when_EmptyMethodDeclaration() {
    String fileName = "cfgInputs/should_SetStartAndEndSame_when_EmptyMethodDeclaration.java";
    init(fileName);
    assertAll("Method declaration with empty block",
        () -> assertNotNull(controlFlowGraph.getMethodDeclaration()),
        () -> assertEquals(controlFlowGraph.getStart(), controlFlowGraph.getEnd())
    );
  }

  @Test
  @Tag("MethodDeclaration")
  @DisplayName("Should set start to first statement and end different when non-empty method declaration")
  void should_SetStartToFirstStatementAndEndDifferent_when_NonEmptyMethodDeclaration() {
    String fileName = "cfgInputs/should_SetStartToFirstStatementAndEndDifferent_when_NonEmptyMethodDeclaration.java";
    init(fileName);
    Statement start = controlFlowGraph.getStart();
    Statement end = controlFlowGraph.getEnd();
    Statement variableDeclStatement = statementTracker.getVariableDeclarationStatement(0);
    assertAll("Method declaration with non-empty block",
        () -> assertNotNull(controlFlowGraph.getMethodDeclaration()), 
        () -> assertNotEquals(start, end),
        () -> assertTrue(start == variableDeclStatement),
        () -> assertTrue(hasEdge(variableDeclStatement, end))
    );    
  }

  @Test
  @Tag("Block")
  @DisplayName("Should link all when block has no return")
  void should_LinkAll_when_BlockHasNoReturn() {
    String fileName = "cfgInputs/should_LinkAll_when_BlockHasNoReturn.java";
    init(fileName);
    Statement variableDeclaration = statementTracker.getVariableDeclarationStatement(0);
    Statement expressionStatement = statementTracker.getExpressionStatement(0);
    assertTrue(hasEdge(variableDeclaration, expressionStatement));
  }

  @Test
  @Tag("Block")
  @DisplayName("Should link to return when block has return") 
  void should_LinkToReturn_when_BlockHasReturn() {
    String fileName = "cfgInputs/should_LinkToReturn_when_BlockHasReturn.java";
    init(fileName);
    Statement variableDeclaration = statementTracker.getVariableDeclarationStatement(0);
    Statement expressionStatement = statementTracker.getExpressionStatement(0);
    Statement returnStatement = statementTracker.getReturnStatement(0);
    assertAll(
        () -> assertTrue(hasEdge(variableDeclaration, returnStatement)),
        () -> assertFalse(hasEdge(returnStatement, expressionStatement))
    );
  }

  private boolean hasEdge(Statement source, Statement dest) {
    Set<Statement> successors = controlFlowGraph.getSuccs(source);
    Set<Statement> predecessors = controlFlowGraph.getPreds(dest);
    return successors != null && successors.contains(dest) 
        && predecessors != null && predecessors.contains(source);
  }

  @Test
  @Tag("IfStatement")
  @DisplayName("Should have two edges when if statement has statement inside")
  void should_HaveTwoEdges_when_IfStatementHasStatementInside() {
    String fileName = "cfgInputs/should_HaveTwoEdges_when_IfStatementHasStatementInside.java";
    init(fileName);
    Statement start = controlFlowGraph.getStart();
    IfStatement ifStatement = (IfStatement)statementTracker.ifList.get(0);
    Statement jDeclaration = statementTracker.getVariableDeclarationStatement(0);
    Statement iDeclaration = statementTracker.getVariableDeclarationStatement(1);
    assertAll("If statement with non empty block",
        () -> assertNotNull(ifStatement), 
        () -> assertEquals(start, ifStatement),
        () -> assertTrue(hasEdge(ifStatement, jDeclaration)),
        () -> assertTrue(hasEdge(ifStatement, iDeclaration)),
        () -> assertTrue(hasEdge(jDeclaration, iDeclaration))
    );    
  }

  @Test
  @Tag("IfStatement")
  @DisplayName("Should have one edge when if statement has nothing inside")
  void should_HaveOneEdge_when_IfStatementHasNothingInside() {
    String fileName = "cfgInputs/should_HaveOneEdge_when_IfStatementHasNothingInside.java";
    init(fileName);
    Statement start = controlFlowGraph.getStart();
    IfStatement ifStatement = (IfStatement)statementTracker.ifList.get(0);
    Statement iDeclaration = statementTracker.getVariableDeclarationStatement(0);
    assertAll("If statement with empty block",
        () -> assertNotNull(ifStatement), 
        () -> assertEquals(start, ifStatement),
        () -> assertTrue(hasEdge(ifStatement, iDeclaration))
    );    
  }

  @Test
  @Tag("IfStatement")
  @DisplayName("Should have multiple edges when if statement and else statements are not empty")
  void should_HaveMultipleEdges_when_IfStatementAndElseStatementNotEmpty() {
    String fileName = "cfgInputs/should_HaveMultipleEdges_when_IfStatementAndElseStatementNotEmpty.java";
    init(fileName);
    Statement start = controlFlowGraph.getStart();
    IfStatement ifStatement = (IfStatement)statementTracker.ifList.get(0);
    Statement iDeclaration = statementTracker.getVariableDeclarationStatement(0);
    Statement jDeclaration = statementTracker.getVariableDeclarationStatement(1);
    Statement kDeclaration = statementTracker.getVariableDeclarationStatement(2);
    assertAll("If statement and else statement both non-empty",
        () -> assertNotNull(ifStatement), 
        () -> assertEquals(start, ifStatement),
        () -> assertTrue(hasEdge(ifStatement, iDeclaration)),
        () -> assertTrue(hasEdge(ifStatement, jDeclaration)),
        () -> assertTrue(hasEdge(iDeclaration, kDeclaration)),
        () -> assertTrue(hasEdge(jDeclaration, kDeclaration))
    );    
  }
  @Test
  @Tag("IfStatement")
  @DisplayName("Should have one edge when if statement and else statements are empty")
  void should_HaveOneEdge_when_IfStatementAndElseStatementEmpty() {
    String fileName = "cfgInputs/should_HaveOneEdge_when_IfStatementAndElseStatementEmpty.java";
    init(fileName);
    Statement start = controlFlowGraph.getStart();
    IfStatement ifStatement = (IfStatement)statementTracker.ifList.get(0);
    Statement iDeclaration = statementTracker.getVariableDeclarationStatement(0);
    assertAll("If statement and else statement both non-empty",
        () -> assertNotNull(ifStatement), 
        () -> assertEquals(start, ifStatement),
        () -> assertTrue(hasEdge(ifStatement, iDeclaration)),
        () -> assertFalse(hasEdge(ifStatement.getElseStatement(), iDeclaration))
    );    
  }
}
