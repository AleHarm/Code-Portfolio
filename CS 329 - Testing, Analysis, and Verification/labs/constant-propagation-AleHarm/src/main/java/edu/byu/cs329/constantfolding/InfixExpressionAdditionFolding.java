package edu.byu.cs329.constantfolding;

import edu.byu.cs329.utils.ExceptionUtils;
import edu.byu.cs329.utils.TreeModificationUtils;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Folds expressions combineing two number literals into a single number literal.
 */
public class InfixExpressionAdditionFolding implements Folding {

  static final Logger log = LoggerFactory.getLogger(InfixExpressionAdditionFolding.class);

  class Visitor extends ASTVisitor {

    public boolean didFold = false;

    @Override
    public void endVisit(InfixExpression n) {

      InfixExpression.Operator operator = n.getOperator();

      if (operator != InfixExpression.Operator.PLUS) {
        return;
      }

      if (!(n.getLeftOperand() instanceof NumberLiteral)
          || !(n.getRightOperand() instanceof NumberLiteral)) {
        return;
      }

      int leftValue = Integer.parseInt(n.getLeftOperand().toString());
      int rightValue = Integer.parseInt(n.getRightOperand().toString());

      int value = leftValue + rightValue;
      String valueStr = Integer.toString(value);

      NumberLiteral newNode = n.getAST().newNumberLiteral(valueStr);
      TreeModificationUtils.replaceChildInParent(n, newNode);
      didFold = true;
    }
  }

  /**
   * Replaces addition infix expressions with number literals with result of
   * addition.
   * 
   * <p>Visits the root and any reachable nodes from the root to replace
   * any InfixExpression with addition reachable node containing number literals
   * for both operands with the result of the addition.
   *
   * <p>top := all nodes reachable from root such that each node
   * matches the addition infix folding pattern
   * 
   * <p>parents := all nodes such that each one is the parent
   * of some node in top
   * 
   * <p>isFoldable(n) := isInfixAdditionExpression(n)
   * /\ ( isLiteral(expression(n))
   * || isFoldable(expression(n)))
   * 
   * <p>literal(n) := if isLiteral(n) then n else literal(expression(n))
   *
   * @modifies nodes in parents
   * 
   * @requires root != null
   * @requires (root instanceof CompilationUnit) \/ parent(root) != null
   * 
   * @ensures fold(root) == (old(top) != emptyset)
   * @ensures forall n in old(top), exists n' in nodes
   *          fresh(n')
   *          /\ isNumberLiteral(n')
   *          /\ value(n') == eval(+, n.leftOp.value, n.rightOp.value)
   *          /\ parent(n') == parent(n)
   *          /\ children(parent(n')) == (children(parent(n)) setminus {n}) union
   *          {n'}
   * 
   * @param root the root of the tree to traverse.
   * @return true if added literals were replaced in the rooted tree
   */
  @Override
  public boolean fold(ASTNode root) {

    checkRequires(root);
    Visitor visitor = new Visitor();
    root.accept(visitor);
    return visitor.didFold;
  }

  private void checkRequires(final ASTNode root) {
    ExceptionUtils.requiresNonNull(root, "Null root passed to ParenthesizedExpressionFolding.fold");

    if (!(root instanceof CompilationUnit) && root.getParent() == null) {
      ExceptionUtils.throwRuntimeException(
          "Non-CompilationUnit root with no parent passed to ParenthesizedExpressionFolding.fold");
    }
  }
}
