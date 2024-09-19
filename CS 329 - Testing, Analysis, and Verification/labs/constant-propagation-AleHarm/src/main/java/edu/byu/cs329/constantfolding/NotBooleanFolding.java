package edu.byu.cs329.constantfolding;

import edu.byu.cs329.utils.ExceptionUtils;
import edu.byu.cs329.utils.TreeModificationUtils;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Folds expressions containing a boolean with a logical not operator to a single boolean literal.
 */
public class NotBooleanFolding implements Folding {

  static final Logger log = LoggerFactory.getLogger(NotBooleanFolding.class);

  class Visitor extends ASTVisitor {

    public boolean didFold = false;

    @Override
    public void endVisit(PrefixExpression n) {

      if (n == null) {
        return;
      }

      if (((BooleanLiteral) n.getOperand()).booleanValue() == true) {

        BooleanLiteral newNode = n.getAST().newBooleanLiteral(false);
        TreeModificationUtils.replaceChildInParent(n, newNode);
      } else {
        BooleanLiteral newNode = n.getAST().newBooleanLiteral(true);
        TreeModificationUtils.replaceChildInParent(n, newNode);
      }

      didFold = true;
    }
  }

  /**
   * Replaces booleans prefixed with a logical not with the boolean of the
   * opposite value
   * 
   * <p>Visits the root and any reachable nodes from the root to replace
   * any not prefixed boolean literal reachable node n with the boolean
   * of opposite value.
   *
   * <p>top := all nodes reachable from root such that each node
   * matches the prefix pattern
   * 
   * <p>parents := all nodes such that each one is the parent
   * of some node in top
   * 
   * <p>isFoldable(n) := isLogicalNotBooleanExpression(n)
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
   *          /\ isBooleanLiteral
   *          /\ value(n') == eval(!, n)
   *          /\ parent(n') == parent(n)
   *          /\ children(parent(n')) == (children(parent(n)) setminus {n}) union
   *          {n'}
   * 
   * @param root the root of the tree to traverse.
   * @return true if boolean value has been changed
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
