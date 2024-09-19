package edu.byu.cs329.constantfolding;

import edu.byu.cs329.utils.ExceptionUtils;
import edu.byu.cs329.utils.TreeModificationUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;

/**
 * Folds if statements with boolean conditions.
 */
public class IfStatementBooleanFolding implements Folding {

  class Visitor extends ASTVisitor {

    public boolean didFold = false;

    @Override
    public void endVisit(IfStatement n) {

      Expression exp = n.getExpression();

      if (!(exp instanceof BooleanLiteral)) {
        return;
      }

      if (((BooleanLiteral) exp).booleanValue()) {

        AST ast = n.getAST();
        ASTNode newStatements = ASTNode.copySubtree(ast, n.getThenStatement());
        TreeModificationUtils.replaceChildInParent(n, newStatements);

      } else {

        TreeModificationUtils.removeChildInParent(n);
      }

      didFold = true;
    }
  }

  /**
   * Replaces if statements with boolean literals with the inside code, or nothing
   * 
   * <p>Visits the root and any reachable nodes from the root to replace
   * any boolean expressioned if statment with either the code inside of it, or
   * nothing.
   *
   * <p>top := all nodes reachable from root such that each node
   * matches the if statment boolean folding pattern
   * 
   * <p>parents := all nodes such that each one is the parent
   * of some node in top
   * 
   * <p>isFoldable(n) := isIfStatemetBoolean(n)
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
   *          /\ isBooleanLiteral(n.expression)
   *          /\ value(n') == eval(if, n.expression)
   *          /\ parent(n') == parent(n)
   *          /\ children(parent(n')) == (children(parent(n)) setminus {n}) union
   *          {n'}
   * 
   * @param root the root of the tree to traverse.
   * @return true if the if statment was removed from the tree replaced in the
   *         rooted tree
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
