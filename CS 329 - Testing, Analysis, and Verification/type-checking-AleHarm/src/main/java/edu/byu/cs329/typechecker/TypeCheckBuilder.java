package edu.byu.cs329.typechecker;

import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.byu.cs329.utils.AstNodePropertiesUtils;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builder for the type proof.
 */
public class TypeCheckBuilder {
  static final Logger log = LoggerFactory.getLogger(TypeCheckBuilder.class);

  class Visitor extends ASTVisitor {
    SymbolTable symbolTable = null;
    String className = null;
    Deque<List<DynamicNode>> typeCheckStack = null;
    Deque<String> typeStack = null;
    int blockCounter = 0;
    int statementCounter = 0;

    public Visitor(SymbolTable symbolTable) {
      this.symbolTable = symbolTable;
      typeCheckStack = new ArrayDeque<>();
      pushTypeCheck(new ArrayList<>());
      typeStack = new ArrayDeque<>();
    }

    @Override
    public boolean visit(CompilationUnit node) {
      pushTypeCheck(new ArrayList<>());

      List<String> types = new ArrayList<String>();
      for (Object declaration : node.types()) {
        ((TypeDeclaration) declaration).accept(this);
        types.add(popType());
      }

      DynamicTest test = generateAllVoidTestAndPushResultingType(types);
      peekTypeCheck().add(test);
      return false;
    }

    @Override
    public boolean visit(TypeDeclaration node) {
      pushTypeCheck(new ArrayList<>());
      className = AstNodePropertiesUtils.getName(node);

      List<String> types = new ArrayList<String>();
      for (MethodDeclaration method : Arrays.asList(node.getMethods())) {
        method.accept(Visitor.this);
        types.add(popType());
      }

      DynamicTest test = generateAllVoidTestAndPushResultingType(types);
      peekTypeCheck().add(test);
      return false;
    }

    @Override
    public boolean visit(MethodDeclaration node) {
      resetCounters();
      pushTypeCheck(new ArrayList<>());

      String name = TypeCheckUtils.buildName(className, AstNodePropertiesUtils.getName(node));

      List<SimpleImmutableEntry<String, String>> typeList = symbolTable.getParameterTypeList(name);
      symbolTable.pushScope();
      for (SimpleImmutableEntry<String, String> entry : typeList) {
        symbolTable.addLocal(entry.getKey(), entry.getValue());
      }

      symbolTable.addLocal("this", className);
      String type = symbolTable.getType(name);
      symbolTable.addLocal("return", type);

      node.getBody().accept(this);

      type = popType();
      DynamicTest test = generateAllVoidTestAndPushResultingType(Arrays.asList(type));
      peekTypeCheck().add(test);
      return false;
    }

    @Override
    public boolean visit(Block node) {
      pushTypeCheck(new ArrayList<>());
      symbolTable.pushScope();

      List<String> typeList = new ArrayList<String>();
      for (Object statement : node.statements()) {
        ((Statement) statement).accept(this);
        typeList.add(popType());
      }

      DynamicTest test = generateAllVoidTestAndPushResultingType(typeList);
      peekTypeCheck().add(test);
      return false;
    }

    @Override
    public boolean visit(VariableDeclarationStatement node) {
      pushTypeCheck(new ArrayList<>());
      String name = AstNodePropertiesUtils.getName(node);
      String type = TypeCheckUtils.getType(node);
      symbolTable.addLocal(name, type);
      AstNodePropertiesUtils.getSimpleName(node).accept(this);
      type = popType();

      Expression initializer = AstNodePropertiesUtils.getInitializer(node);
      if (initializer != null) {
        initializer.accept(this);
        String rightType = popType();
        DynamicTest test = generateTypeCompatibleTestAndPushResultingType(type, rightType);
        peekTypeCheck().add(test);
        type = popType();
      }

      if (!TypeCheckTypes.isError(type)) {
        type = TypeCheckTypes.VOID;
      }

      pushType(type);
      return false;
    }

    public boolean visit(ExpressionStatement nodeExp) {
      
      if (!(nodeExp.getExpression() instanceof Assignment)) {
        return false;
      }

      Assignment node = (Assignment) nodeExp.getExpression();
      
      pushTypeCheck(new ArrayList<>());

      String leftName = ((SimpleName) node.getLeftHandSide()).getIdentifier();
      String leftType = symbolTable.getType(leftName);
      generateLookupTestAndAddToObligations(leftName, leftType);

      node.getRightHandSide().accept(this);
      String rightType = popType();
      DynamicTest test = generateTypeCompatibleTestAndPushResultingType(leftType, rightType);
      peekTypeCheck().add(test);
      String type = popType();

      if (!TypeCheckTypes.isError(type)) {
        type = TypeCheckTypes.VOID;
      }

      pushType(type);
      return false;
    }

    public boolean visit(IfStatement node) {
      
      pushTypeCheck(new ArrayList<>());

      node.getExpression().accept(this);
      String expressionType = popType();
      DynamicTest test = 
          generateTypeCompatibleTestAndPushResultingType(TypeCheckTypes.BOOL, expressionType);
      peekTypeCheck().add(test);


      node.getThenStatement().accept(this);
      String rightType = popType();
      test = generateAllVoidTestAndPushResultingType(Arrays.asList(rightType));
      peekTypeCheck().add(test);
      String type = popType();

      if (!TypeCheckTypes.isError(type)) {
        type = TypeCheckTypes.VOID;
      }

      pushType(type);
      return false;
    }

    public boolean visit(WhileStatement node) {
      
      pushTypeCheck(new ArrayList<>());

      node.getExpression().accept(this);
      String expressionType = popType();
      DynamicTest test = 
          generateTypeCompatibleTestAndPushResultingType(TypeCheckTypes.BOOL, expressionType);
      peekTypeCheck().add(test);


      node.getBody().accept(this);
      String rightType = popType();
      test = generateAllVoidTestAndPushResultingType(Arrays.asList(rightType));
      peekTypeCheck().add(test);
      String type = popType();

      if (!TypeCheckTypes.isError(type)) {
        type = TypeCheckTypes.VOID;
      }

      pushType(type);
      return false;
    }

    public boolean visit(PrefixExpression node) {
      pushTypeCheck(new ArrayList<>());
      String type = TypeCheckTypes.BOOL;
      node.getOperand().accept(this);
      type = popType();
      
      if (!TypeCheckTypes.isError(type)) {
        type = TypeCheckTypes.BOOL;
      }

      pushType(type);
      return false;
    }

    public boolean visit(InfixExpression node) {
      pushTypeCheck(new ArrayList<>());
      String type;
      String operator = node.getOperator().toString();
      if (operator.equals("+") || operator.equals("*") || operator.equals("-")) {
        type = TypeCheckTypes.INT;
      } else {
        type = TypeCheckTypes.BOOL;
      }


      node.getLeftOperand().accept(this);
      String leftType = popType();
      DynamicTest leftTest = generateTypeCompatibleTestAndPushResultingType(type, leftType);
      peekTypeCheck().add(leftTest);
      node.getRightOperand().accept(this);
      String rightType = popType();
      DynamicTest rightTest = generateTypeCompatibleTestAndPushResultingType(type, rightType);
      peekTypeCheck().add(rightTest);

      pushType(type);
      return false;
    }

    public boolean visit(ReturnStatement node) {
      pushTypeCheck(new ArrayList<>());
      String type = symbolTable.getType("return");
      node.getExpression().accept(this);
      String expType = popType();
      DynamicTest test = generateTypeCompatibleTestAndPushResultingType(type, expType);
      peekTypeCheck().add(test);
      type = popType();

      if (!TypeCheckTypes.isError(type)) {
        type = TypeCheckTypes.VOID;
      }

      pushType(type);
      return false;
    }

    public boolean visit(MethodInvocation node) {
      pushTypeCheck(new ArrayList<>());
      String name = node.getName().getIdentifier();
      String nameOfThis = symbolTable.getType("this");
      String type = symbolTable.getType(nameOfThis + "." + name);
      generateLookupTestAndAddToObligations(name, type);
      pushType(type);
      return false;
    }

    public boolean visit(FieldAccess node) {
      pushTypeCheck(new ArrayList<>());
      String name = node.getName().getIdentifier();
      String nameOfThis = symbolTable.getType("this");
      String type = symbolTable.getType(nameOfThis + "." + name);
      generateLookupTestAndAddToObligations(name, type);
      pushType(type);
      return false;
    }

    public boolean visit(QualifiedName node) {
      pushTypeCheck(new ArrayList<>());
      String name = node.getName().getIdentifier();
      String nameOfThis = symbolTable.getType("this");
      String type = symbolTable.getType(nameOfThis + "." + name);
      generateLookupTestAndAddToObligations(name, type);
      pushType(type);
      return false;
    }

    @Override
    public boolean visit(SimpleName node) {
      pushTypeCheck(new ArrayList<>());
      String name = AstNodePropertiesUtils.getName(node);
      String type = symbolTable.getType(name);
      generateLookupTestAndAddToObligations(name, type);
      pushType(type);
      return false;
    }

    @Override
    public boolean visit(BooleanLiteral node) {
      pushTypeCheck(new ArrayList<>());
      String name = node.toString();
      String type = TypeCheckTypes.BOOL;
      generateLookupTestAndAddToObligations(name, type);
      pushType(type);
      return false;
    }

    @Override
    public boolean visit(NumberLiteral node) {
      pushTypeCheck(new ArrayList<>());
      String name = node.getToken();
      String type = TypeCheckTypes.INT;
      generateLookupTestAndAddToObligations(name, type);
      pushType(type);
      return false;
    }

    @Override
    public boolean visit(NullLiteral node) {
      pushTypeCheck(new ArrayList<>());
      String name = node.toString();
      String type = TypeCheckTypes.NULL;
      generateLookupTestAndAddToObligations(name, type);
      pushType(type);
      return false;
    }

    @Override
    public void endVisit(CompilationUnit node) {
      String name = "CompilationUnit ";
      generateProofAndAddToObligations(name);
    }

    @Override
    public void endVisit(TypeDeclaration node) {
      String name = "class " + className;
      generateProofAndAddToObligations(name);
    }

    @Override
    public void endVisit(MethodDeclaration node) {
      symbolTable.popScope();
      String name =
          TypeCheckUtils.buildName("method " + className, AstNodePropertiesUtils.getName(node));
      generateProofAndAddToObligations(name);
    }

    @Override
    public void endVisit(Block node) {
      symbolTable.popScope();
      String name = generateBlockName();
      generateProofAndAddToObligations(name);
    }

    @Override
    public void endVisit(VariableDeclarationStatement node) {
      String name = generateStatementName();
      generateProofAndAddToObligations(name);
    }

    public void endVisit(FieldAccess node) {
      String name = node.toString();
      generateProofAndAddToObligations(name);
    }

    public void endVisit(QualifiedName node) {
      String name = node.toString();
      generateProofAndAddToObligations(name);
    }

    public void endVisit(MethodInvocation node) {
      String name = node.toString();
      generateProofAndAddToObligations(name);
    }

    public void endVisit(IfStatement node) {
      String name = generateBlockName();
      generateProofAndAddToObligations(name);
    }

    public void endVisit(WhileStatement node) {
      String name = generateBlockName();
      generateProofAndAddToObligations(name);
    }
    
    public void endVisit(ExpressionStatement node) {
      String name = generateStatementName();
      generateProofAndAddToObligations(name);
    }

    public void endVisit(ReturnStatement node) {
      String name = generateStatementName();
      generateProofAndAddToObligations(name);
    }

    public void endVisit(PrefixExpression node) {
      String name = node.toString();
      generateProofAndAddToObligations(name);
    }

    public void endVisit(InfixExpression node) {
      String name = node.toString();
      generateProofAndAddToObligations(name);
    }

    @Override
    public void endVisit(SimpleName node) {
      String name = AstNodePropertiesUtils.getName(node);
      generateProofAndAddToObligations(name);
    }

    @Override
    public void endVisit(BooleanLiteral node) {
      String name = node.toString();
      generateProofAndAddToObligations(name);
    }

    @Override
    public void endVisit(NumberLiteral node) {
      String name = node.getToken();
      generateProofAndAddToObligations(name);
    }

    @Override
    public void endVisit(NullLiteral node) {
      String name = node.toString();
      generateProofAndAddToObligations(name);
    }

    private void resetCounters() {
      statementCounter = 0;
      blockCounter = 0;
    }

    private void generateLookupTestAndAddToObligations(String name, String type) {
      String displayName = generateLookupDisplayName(name, type);
      DynamicTest test = DynamicTest.dynamicTest(displayName,
          () -> Assertions.assertNotEquals(TypeCheckTypes.ERROR, type));
      peekTypeCheck().add(test);
    }

    private void generateProofAndAddToObligations(String name) {
      String type = peekType();
      String displayName = generateProvesDisplayName(name, type);
      List<DynamicNode> proofs = popTypeCheck();
      addNoObligationIfEmpty(proofs);
      DynamicContainer proof = DynamicContainer.dynamicContainer(displayName, proofs.stream());
      List<DynamicNode> obligations = peekTypeCheck();
      obligations.add(proof);
    }

    private DynamicTest generateTypeCompatibleTestAndPushResultingType(String leftType,
        String rightType) {
      String displayName = leftType + " := " + rightType;

      boolean isAssignmentCompatible = TypeCheckTypes.isAssignmentCompatible(leftType, rightType);

      DynamicTest test =
          DynamicTest.dynamicTest(displayName, () -> assertTrue(isAssignmentCompatible));

      String type = TypeCheckTypes.VOID;
      if (!isAssignmentCompatible) {
        type = TypeCheckTypes.ERROR;
      }
      pushType(type);
      return test;
    }

    private DynamicTest generateAllVoidTestAndPushResultingType(List<String> types) {
      DynamicTest test = null;
      String type = TypeCheckTypes.VOID;

      if (types.isEmpty()) {
        test = generateNoObligation();
        pushType(type);
        return test;
      }

      String displayName = String.join(",", types) + " = " + TypeCheckTypes.VOID;
      boolean testValue = types.stream().allMatch(t -> t.equals(TypeCheckTypes.VOID));
      test = DynamicTest.dynamicTest(displayName, () -> assertTrue(testValue));
      if (!testValue) {
        type = TypeCheckTypes.ERROR;
      }
      pushType(type);
      return test;
    }


    private void addNoObligationIfEmpty(List<DynamicNode> proofs) {
      if (proofs.size() > 0) {
        return;
      }
      proofs.add(generateNoObligation());
    }

    private DynamicTest generateNoObligation() {
      return DynamicTest.dynamicTest("true", () -> assertTrue(true));
    }

    private String generateProvesDisplayName(String name, String type) {
      return name + ":" + type;
    }

    private String generateLookupDisplayName(String name, String type) {
      return "E(" + name + ") = " + type;
    }

    private List<DynamicNode> popTypeCheck() {
      return typeCheckStack.pop();
    }

    private void pushTypeCheck(List<DynamicNode> proof) {
      typeCheckStack.push(proof);
    }

    private List<DynamicNode> peekTypeCheck() {
      return typeCheckStack.peek();
    }

    private String popType() {
      return typeStack.pop();
    }

    private void pushType(String type) {
      typeStack.push(type);
    }

    private String peekType() {
      return typeStack.peek();
    }

    private String generateBlockName() {
      return "B" + blockCounter++;
    }

    private String generateStatementName() {
      return "S" + statementCounter++;
    }

  }


  public TypeCheckBuilder() {

  }

  /**
   * Returns true if static type safe with the checks.
   *
   * @param symbolTable the environment for the type checks
   * @param node the ASTNode for the compilation unit
   * @param tests a container to hold the tests
   * @return true iff the compilation is static type safe
   */
  public boolean getTypeChecker(SymbolTable symbolTable, ASTNode node, List<DynamicNode> tests) {
    Visitor visitor = new Visitor(symbolTable);
    node.accept(visitor);
    tests.addAll(visitor.popTypeCheck());
    return visitor.popType().equals(TypeCheckTypes.VOID);
  }
}
