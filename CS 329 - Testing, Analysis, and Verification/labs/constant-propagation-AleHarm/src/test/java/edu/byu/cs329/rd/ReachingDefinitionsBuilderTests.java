package edu.byu.cs329.rd;

// import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
// import org.mockito.BDDMockito;

import edu.byu.cs329.cfg.ControlFlowGraph;
import edu.byu.cs329.cfg.ControlFlowGraphBuilder;
import edu.byu.cs329.rd.ReachingDefinitions.Definition;
import edu.byu.cs329.utils.JavaSourceUtils;

@DisplayName("Tests for ReachingDefinitionsBuilder")
public class ReachingDefinitionsBuilderTests {

  ReachingDefinitionsBuilder unitUnderTest = null;
  ControlFlowGraphBuilder cfgb = null;
  ReachingDefinitions reachingDefinitions = null;


  @BeforeEach
  void beforeEach() {
    unitUnderTest = new ReachingDefinitionsBuilder();
  }

  // @Test
  // @Tag("Parameters")
  // @DisplayName("Should have a definition for each parameter at start when the method declaration has parameters.")
  // void should_HaveDefinitionForEachParameterAtStart_when_MethodDeclarationHasParameters() {
  //   ControlFlowGraph controlFlowGraph = MockUtils.newMockForEmptyMethodWithTwoParameters("a", "b");
  //   ReachingDefinitions reachingDefinitions = getReachingDefinitions(controlFlowGraph);
  //   Statement start = controlFlowGraph.getStart();
  //   Set<Definition> definitions = reachingDefinitions.getReachingDefinitions(start);
  //   assertEquals(2, definitions.size());
  //   assertAll("Parameters Defined at Start", 
  //       () -> assertTrue(doesDefine("a", definitions)),
  //       () -> assertTrue(doesDefine("b", definitions))
  //   );
  // }

  Boolean compareDefinitionName(Statement statement, int definitionIndex, String expectedName){

    List<Definition> definitions = getDefinitions(statement);
    return definitions.get(definitionIndex).name.getIdentifier().equals(expectedName);
  }

  boolean compareDefinitionValue(Statement statement, int definitionIndex, int expectedValue){

    int expressionValue;
    List<Definition> definitions = getDefinitions(statement);
    Definition definition = definitions.get(definitionIndex);
    Statement defStatement = definition.getStatement();

    if(defStatement instanceof VariableDeclarationStatement){
      VariableDeclarationStatement vdStatement = (VariableDeclarationStatement)defStatement;
      @SuppressWarnings("unchecked")
      List<VariableDeclarationFragment> frags = vdStatement.fragments();
      VariableDeclarationFragment frag = frags.get(0);
      NumberLiteral number = (NumberLiteral)frag.getInitializer();
      String token = number.getToken();
      expressionValue = Integer.parseInt(token);
    }else{

      ExpressionStatement expStatement = (ExpressionStatement)defStatement;
      Assignment assignment = (Assignment)expStatement.getExpression();
      NumberLiteral number = (NumberLiteral)assignment.getRightHandSide();
      String token = number.getToken();
      expressionValue = Integer.parseInt(token);
    }

    return (expressionValue == expectedValue);
  }

  List<Definition> getDefinitions(Statement statement){

    Set<Definition> definitionSet = reachingDefinitions.getReachingDefinitions(statement);
    return new ArrayList<>(definitionSet);
  }

  // private boolean doesDefine(String name, final Set<Definition> definitions) {
  //   for (Definition definition : definitions) {
  //     if (definition.name.getIdentifier().equals(name) && definition.statement == null) {
  //       return true;
  //     }
  //   }
  //   return false;
  // }

  private ReachingDefinitions getReachingDefinitions(ControlFlowGraph controlFlowGraph) {
    List<ControlFlowGraph> list = new ArrayList<ControlFlowGraph>();
    list.add(controlFlowGraph);
    List<ReachingDefinitions> reachingDefinitionsList = unitUnderTest.build(list);
    assertEquals(1, reachingDefinitionsList.size());
    return reachingDefinitionsList.get(0);
  } 

  // @Test
  // @Tag("IfStatement")
  // @DisplayName("Should have a definition inside and outside if statement when if statement has one parameter.")
  // void should_HaveDefinitionInsideAndOutsideIfStatement_when_IfStatementHasOneParameterMOCKING() {
  //   ControlFlowGraph controlFlowGraph = MockUtils.newMockForIfStatementWithTwoParameters("a", "b");
  //   ReachingDefinitions reachingDefinitions = getReachingDefinitions(controlFlowGraph);
  //   Statement start = controlFlowGraph.getStart();
  //   Set<Definition> definitions = reachingDefinitions.getReachingDefinitions(start);
  //   List<Definition> defs = new ArrayList<>(definitions);
  //   BDDMockito.given(defs.get(0).name.getIdentifier()).willReturn("a");
  //   BDDMockito.given(defs.get(1).name.getIdentifier()).willReturn("b");
  // }

  // @Test
  // @Tag("IfStatement")
  // @DisplayName("Should have a definition before, inside, and after if statement when if statement has one declaration.")
  // void should_HaveDefinitionBeforeInsideAndAfterIfStatement_when_IfStatementHasOneDeclarationNoMocks() {

  //   cfgb = new ControlFlowGraphBuilder();
  //   URI uri = JavaSourceUtils.getUri(this, "rdInputs/should_HaveDefinitionBeforeInsideAndAfterIfStatement_when_IfStatementHasOneDeclarationNoMocks.java");
  //   ASTNode root = JavaSourceUtils.getCompilationUnit(uri);
  //   ControlFlowGraph cfg = cfgb.build(root).get(0);
  //   reachingDefinitions = getReachingDefinitions(cfg);
  //   Statement start = cfg.getStart();
  //   Statement ifStatement = new ArrayList<>(cfg.getSuccs(start)).get(0);
  //   assertTrue(compareDefinitionName(ifStatement, 0, "a"));
  //   Statement intBStatement = new ArrayList<>(cfg.getSuccs(ifStatement)).get(1);
  //   Statement intCStatement = new ArrayList<>(cfg.getSuccs(ifStatement)).get(0);
  //   assertTrue(compareDefinitionName(intCStatement,1, "a"));
  //   assertTrue(compareDefinitionName(intBStatement, 0, "a"));
  // }

  // @Test
  // @Tag("IfStatement")
  // @DisplayName("Should have a definition before, inside, and after if statement when if statement has one assignment.")
  // void should_HaveDefinitionBeforeInsideAndAfterIfStatement_when_IfStatementHasOneAssignmentNoMocks() {

  //   //File setup
  //   cfgb = new ControlFlowGraphBuilder();
  //   URI uri = JavaSourceUtils.getUri(this, "rdInputs/should_HaveDefinitionBeforeInsideAndAfterIfStatement_when_IfStatementHasOneAssignmentNoMocks.java");
  //   ASTNode root = JavaSourceUtils.getCompilationUnit(uri);
  //   ControlFlowGraph cfg = cfgb.build(root).get(0);
  //   reachingDefinitions = getReachingDefinitions(cfg);
  //   Statement start = cfg.getStart();

  //   //Get if statement and check for declaration of a
  //   Statement ifStatement = new ArrayList<>(cfg.getSuccs(start)).get(0);
  //   assertTrue(compareDefinitionName(ifStatement, 0, "a"));

  //   //Get statements and check definitions
  //   Statement intAAssignmentStatement = new ArrayList<>(cfg.getSuccs(ifStatement)).get(1);
  //   Statement intCStatement = new ArrayList<>(cfg.getSuccs(ifStatement)).get(0);
  //   assertTrue(compareDefinitionName(intCStatement, 0, "a"));
  //   assertTrue(compareDefinitionName(intCStatement, 1, "a"));
  //   assertTrue(compareDefinitionValue(intCStatement, 1, 10));
  //   assertTrue(compareDefinitionValue(intCStatement, 0, 5));
  //   assertTrue(compareDefinitionName(intAAssignmentStatement, 0, "a"));
  // }
}
