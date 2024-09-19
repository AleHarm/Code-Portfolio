package edu.byu.cs329.constantpropagation;

import edu.byu.cs329.cfg.ControlFlowGraph;
import edu.byu.cs329.cfg.ControlFlowGraphBuilder;
import edu.byu.cs329.constantfolding.ConstantFolding;
import edu.byu.cs329.rd.ReachingDefinitions;
import edu.byu.cs329.rd.ReachingDefinitions.Definition;
import edu.byu.cs329.rd.ReachingDefinitionsBuilder;
import edu.byu.cs329.utils.JavaSourceUtils;
import edu.byu.cs329.utils.TreeModificationUtils;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Prepare to see the nastiest most disgusting code of your life. 
 * I give you: Constant Propagation.
 *
 * @author Eric Mercer
 * @author Alex Harmon
 */
public class ConstantPropagation {

  static final Logger log = LoggerFactory.getLogger(ConstantPropagation.class);

  /**
   * Performs constant propagation.
   *
   * @param node the root node for constant propagation.
   */
  public static boolean propagate(ASTNode node) {

    boolean hasChanged = false;

    ControlFlowGraphBuilder cfgb = new ControlFlowGraphBuilder();
    List<ControlFlowGraph> cfgs = cfgb.build(node);
    ControlFlowGraph cfg = cfgs.get(0);
    ReachingDefinitionsBuilder rdb = new ReachingDefinitionsBuilder();
    List<ReachingDefinitions> rds = rdb.build(cfgs);
    ReachingDefinitions rd = rds.get(0);

    Queue<Statement> worklist = new LinkedList<>();
    worklist.add(cfgs.get(0).getStart());

    while (!worklist.isEmpty()) {
      Statement currentStatement = worklist.poll();
      List<Definition> definitions;
      if (rd.getReachingDefinitions(currentStatement) != null) {
        definitions = new ArrayList<Definition>(rd.getReachingDefinitions(currentStatement));
        if (cfg.getSuccs(currentStatement) != null) {
          worklist.addAll(cfg.getSuccs(currentStatement));
        }
        if (currentStatement instanceof IfStatement) {
          boolean didChange = handleIfStatement(currentStatement, cfg, definitions);
          if (!hasChanged) {
            hasChanged = didChange;
          }
        }
        if (currentStatement instanceof ExpressionStatement) {

          boolean didChange = handleExpStat(currentStatement, cfg, definitions);
          if (!hasChanged) {
            hasChanged = didChange;
          }
        }
        if (currentStatement instanceof VariableDeclarationStatement) {
          boolean didChange = handVarDeclStat(currentStatement, cfg, definitions);
          if (!hasChanged) {
            hasChanged = didChange;
          }
        }
      }
    }

    return hasChanged;
  }

  static boolean handleIfStatement(
      Statement statement, 
      ControlFlowGraph cfg, 
      List<Definition> defs) {
    boolean hasChanged = false;
    Expression exp = ((IfStatement) statement).getExpression();
    if (exp instanceof InfixExpression) {
      InfixExpression infixExp = (InfixExpression) exp;
      if (infixExp.getLeftOperand() instanceof SimpleName) {
        SimpleName name = (SimpleName) infixExp.getLeftOperand();
        for (Definition definition : defs) {
          if (definition.name.getIdentifier().equals(name.getIdentifier())) {

            Statement defStatement = definition.getStatement();
            NumberLiteral value;

            if (defStatement instanceof VariableDeclarationStatement) {
              VariableDeclarationStatement varDecl;
              varDecl = (VariableDeclarationStatement) defStatement;
              VariableDeclarationFragment varDeclFrag;
              varDeclFrag = (VariableDeclarationFragment) varDecl.fragments().get(0);
              if (varDeclFrag.getInitializer() instanceof NumberLiteral) {
                value = (NumberLiteral) (varDeclFrag.getInitializer());
              } else {
                break;
              }
            } else {
              ExpressionStatement expStat = (ExpressionStatement) defStatement;
              Assignment ass = (Assignment) expStat.getExpression();
              if (ass.getRightHandSide() instanceof NumberLiteral) {
                value = (NumberLiteral) ass.getRightHandSide();
              } else {
                break;
              }
            }

            AST ast = infixExp.getAST();
            InfixExpression newInfixExp = ast.newInfixExpression();
            newInfixExp.setOperator(infixExp.getOperator());
            NumberLiteral rightOp = ast.newNumberLiteral(infixExp.getRightOperand().toString());
            newInfixExp.setRightOperand(rightOp);
            NumberLiteral leftOperand = ast.newNumberLiteral(value.toString());
            newInfixExp.setLeftOperand(leftOperand);
            TreeModificationUtils.replaceChildInParent(exp, newInfixExp);
            hasChanged = true;
          }
        }
      } else if (infixExp.getRightOperand() instanceof SimpleName) {
        SimpleName name = (SimpleName) infixExp.getRightOperand();
        for (Definition definition : defs) {
          if (definition.name.getIdentifier().equals(name.getIdentifier())) {
            Statement defStatement = definition.getStatement();
            NumberLiteral value;

            if (defStatement instanceof VariableDeclarationStatement) {
              VariableDeclarationStatement varDecl;
              varDecl = (VariableDeclarationStatement) defStatement;
              VariableDeclarationFragment varDeclFrag;
              varDeclFrag = (VariableDeclarationFragment) varDecl.fragments().get(0);
              if (varDeclFrag.getInitializer() instanceof NumberLiteral) {
                value = (NumberLiteral) (varDeclFrag.getInitializer());
              } else {
                break;
              }
            } else {
              ExpressionStatement expStat = (ExpressionStatement) defStatement;
              Assignment ass = (Assignment) expStat.getExpression();
              if (ass.getRightHandSide() instanceof NumberLiteral) {
                value = (NumberLiteral) ass.getRightHandSide();
              } else {
                break;
              }
            }

            AST ast = infixExp.getAST();
            InfixExpression newInfixExp = ast.newInfixExpression();
            newInfixExp.setOperator(infixExp.getOperator());
            NumberLiteral rightOp = ast.newNumberLiteral(value.toString());
            newInfixExp.setRightOperand(rightOp);
            NumberLiteral leftOperand = ast.newNumberLiteral(infixExp.getLeftOperand().toString());
            newInfixExp.setLeftOperand(leftOperand);
            TreeModificationUtils.replaceChildInParent(exp, newInfixExp);
            hasChanged = true;
          }
        }
      }
    }

    return hasChanged;
  }

  static boolean handleExpStat(Statement statement, ControlFlowGraph cfg, List<Definition> defs) {
    boolean hasChanged = false;
    Assignment assignment = (Assignment) ((ExpressionStatement) statement).getExpression();
    if (assignment.getRightHandSide() instanceof InfixExpression) {
      InfixExpression infixExp = (InfixExpression) assignment.getRightHandSide();
      if (infixExp.getLeftOperand() instanceof SimpleName 
          && infixExp.getRightOperand() instanceof SimpleName) {
        SimpleName leftName = (SimpleName) infixExp.getLeftOperand();
        SimpleName rightName = (SimpleName) infixExp.getRightOperand();
        Statement leftDefStatement;
        Statement rightDefStatement;
        NumberLiteral leftValue = null;
        NumberLiteral rightValue = null;
        for (Definition definition : defs) {
          if (definition.name.getIdentifier().equals(leftName.getIdentifier())) {
            leftDefStatement = definition.getStatement();

            if (leftDefStatement instanceof VariableDeclarationStatement) {
              VariableDeclarationStatement varDecl;
              varDecl = (VariableDeclarationStatement) leftDefStatement;
              VariableDeclarationFragment varDeclFrag;
              varDeclFrag = (VariableDeclarationFragment) varDecl.fragments().get(0);
              if (varDeclFrag.getInitializer() instanceof NumberLiteral) {
                leftValue = (NumberLiteral) (varDeclFrag.getInitializer());
              } else {
                break;
              }
            } else {
              ExpressionStatement expStat = (ExpressionStatement) leftDefStatement;
              Assignment ass = (Assignment) expStat.getExpression();
              if (ass.getRightHandSide() instanceof NumberLiteral) {
                leftValue = (NumberLiteral) ass.getRightHandSide();
              } else {
                break;
              }
            }
          }
        }
        for (Definition definition : defs) {
          if (definition.name.getIdentifier().equals(rightName.getIdentifier())) {
            rightDefStatement = definition.getStatement();

            if (rightDefStatement instanceof VariableDeclarationStatement) {
              VariableDeclarationStatement varDecl;
              varDecl = (VariableDeclarationStatement) rightDefStatement;
              VariableDeclarationFragment varDeclFrag;
              varDeclFrag = (VariableDeclarationFragment) varDecl.fragments().get(0);
              if (varDeclFrag.getInitializer() instanceof NumberLiteral) {
                rightValue = (NumberLiteral) (varDeclFrag.getInitializer());
              } else {
                break;
              }
            } else {
              ExpressionStatement expStat = (ExpressionStatement) rightDefStatement;
              Assignment ass = (Assignment) expStat.getExpression();
              if (ass.getRightHandSide() instanceof NumberLiteral) {
                rightValue = (NumberLiteral) ass.getRightHandSide();
              } else {
                break;
              }
            }
          }
        }

        AST ast = infixExp.getAST();
        InfixExpression newInfixExp = ast.newInfixExpression();
        newInfixExp.setOperator(infixExp.getOperator());

        if (leftValue != null && rightValue != null) {
          NumberLiteral rightOp = ast.newNumberLiteral(rightValue.toString());
          newInfixExp.setRightOperand(rightOp);
          NumberLiteral leftOperand = ast.newNumberLiteral(leftValue.toString());
          newInfixExp.setLeftOperand(leftOperand);
          TreeModificationUtils.replaceChildInParent(infixExp, newInfixExp);
          hasChanged = true;
        }
        
      } else if (infixExp.getLeftOperand() instanceof SimpleName) {
        SimpleName name = (SimpleName) infixExp.getLeftOperand();
        for (Definition definition : defs) {
          if (definition.name.getIdentifier().equals(name.getIdentifier())) {

            Statement defStatement = definition.getStatement();
            NumberLiteral value;

            if (defStatement instanceof VariableDeclarationStatement) {
              VariableDeclarationStatement varDecl;
              varDecl = (VariableDeclarationStatement) defStatement;
              VariableDeclarationFragment varDeclFrag;
              varDeclFrag = (VariableDeclarationFragment) varDecl.fragments().get(0);
              if (varDeclFrag.getInitializer() instanceof NumberLiteral) {
                value = (NumberLiteral) (varDeclFrag.getInitializer());
              } else {
                break;
              }
            } else {
              ExpressionStatement expStat = (ExpressionStatement) defStatement;
              Assignment ass = (Assignment) expStat.getExpression();
              if (ass.getRightHandSide() instanceof NumberLiteral) {
                value = (NumberLiteral) ass.getRightHandSide();
              } else {
                break;
              }
            }

            AST ast = infixExp.getAST();
            InfixExpression newInfixExp = ast.newInfixExpression();
            newInfixExp.setOperator(infixExp.getOperator());
            NumberLiteral rightOp = ast.newNumberLiteral(infixExp.getRightOperand().toString());
            newInfixExp.setRightOperand(rightOp);
            NumberLiteral leftOperand = ast.newNumberLiteral(value.toString());
            newInfixExp.setLeftOperand(leftOperand);
            if (infixExp.getParent() != null) {
              TreeModificationUtils.replaceChildInParent(infixExp, newInfixExp);
            }
            hasChanged = true;
          }
        }
      } else if (infixExp.getRightOperand() instanceof SimpleName) {
        SimpleName name = (SimpleName) infixExp.getRightOperand();
        for (Definition definition : defs) {
          if (definition.name.getIdentifier().equals(name.getIdentifier())) {

            Statement defStatement = definition.getStatement();
            NumberLiteral value;

            if (defStatement instanceof VariableDeclarationStatement) {
              VariableDeclarationStatement varDecl;
              varDecl = (VariableDeclarationStatement) defStatement;
              VariableDeclarationFragment varDeclFrag;
              varDeclFrag = (VariableDeclarationFragment) varDecl.fragments().get(0);
              if (varDeclFrag.getInitializer() instanceof NumberLiteral) {
                value = (NumberLiteral) (varDeclFrag.getInitializer());
              } else {
                break;
              }
            } else {
              ExpressionStatement expStat = (ExpressionStatement) defStatement;
              Assignment ass = (Assignment) expStat.getExpression();
              if (ass.getRightHandSide() instanceof NumberLiteral) {
                value = (NumberLiteral) ass.getRightHandSide();
              } else {
                break;
              }
            }

            AST ast = infixExp.getAST();
            InfixExpression newInfixExp = ast.newInfixExpression();
            newInfixExp.setOperator(infixExp.getOperator());
            NumberLiteral rightOperand = ast.newNumberLiteral(infixExp.getLeftOperand().toString());
            newInfixExp.setRightOperand(rightOperand);
            NumberLiteral leftOperand = ast.newNumberLiteral(value.toString());
            newInfixExp.setLeftOperand(leftOperand);
            TreeModificationUtils.replaceChildInParent(infixExp, newInfixExp);
            hasChanged = true;
          }
        }
      }
    }
    return hasChanged;
  }

  @SuppressWarnings("unchecked")
  static boolean handVarDeclStat(Statement statement, ControlFlowGraph cfg, List<Definition> defs) {
    boolean hasChanged = false;

    VariableDeclarationStatement varDeclStat;
    varDeclStat = (VariableDeclarationStatement) statement;
    VariableDeclarationFragment frag;
    frag = (VariableDeclarationFragment) varDeclStat.fragments().get(0);
    SimpleName name = (SimpleName) frag.getName();
    if (frag.getInitializer() instanceof SimpleName) {
      SimpleName varName = (SimpleName) frag.getInitializer();
      for (Definition definition : defs) {
        if (definition.name.getIdentifier().equals(varName.getIdentifier())) {
  
          Statement defStatement = definition.getStatement();
          NumberLiteral value;
  
          if (defStatement instanceof VariableDeclarationStatement) {
            VariableDeclarationStatement varDecl;
            varDecl = (VariableDeclarationStatement) defStatement;
            VariableDeclarationFragment varDeclFrag;
            varDeclFrag = (VariableDeclarationFragment) varDecl.fragments().get(0);
            if (varDeclFrag.getInitializer() instanceof NumberLiteral) {
              value = (NumberLiteral) (varDeclFrag.getInitializer());
            } else {
              break;
            }
          } else {
            ExpressionStatement expStat = (ExpressionStatement) defStatement;
            Assignment ass = (Assignment) expStat.getExpression();
            if (ass.getRightHandSide() instanceof NumberLiteral) {
              value = (NumberLiteral) ass.getRightHandSide();
            } else {
              break;
            }
          }
  
          AST ast = frag.getAST();
          VariableDeclarationFragment newFrag = ast.newVariableDeclarationFragment();
          SimpleName newName = ast.newSimpleName(name.toString());
          newFrag.setName(newName);
          NumberLiteral initializer = ast.newNumberLiteral(value.toString());
          newFrag.setInitializer(initializer);
          TreeModificationUtils.replaceChildInParent(frag, newFrag);
          hasChanged = true;
        }
      }
    } else if (frag.getInitializer() instanceof InfixExpression) {
      InfixExpression infixExp = (InfixExpression) frag.getInitializer();
      if (infixExp.getLeftOperand() instanceof SimpleName 
          && infixExp.getRightOperand() instanceof SimpleName) {
        SimpleName leftName = (SimpleName) infixExp.getLeftOperand();
        SimpleName rightName = (SimpleName) infixExp.getRightOperand();
        Statement leftDefStatement;
        Statement rightDefStatement;
        NumberLiteral leftValue = null;
        NumberLiteral rightValue = null;
        for (Definition definition : defs) {
          if (definition.name.getIdentifier().equals(leftName.getIdentifier())) {
            leftDefStatement = definition.getStatement();

            if (leftDefStatement instanceof VariableDeclarationStatement) {
              VariableDeclarationStatement varDecl;
              varDecl = (VariableDeclarationStatement) leftDefStatement;
              VariableDeclarationFragment varDeclFrag;
              varDeclFrag = (VariableDeclarationFragment) varDecl.fragments().get(0);
              if (varDeclFrag.getInitializer() instanceof NumberLiteral) {
                leftValue = (NumberLiteral) (varDeclFrag.getInitializer());
              } else {
                break;
              }
            } else {
              ExpressionStatement expStat = (ExpressionStatement) leftDefStatement;
              Assignment ass = (Assignment) expStat.getExpression();
              if (ass.getRightHandSide() instanceof NumberLiteral) {
                leftValue = (NumberLiteral) ass.getRightHandSide();
              } else {
                break;
              }
            }
          }
        }
        for (Definition definition : defs) {
          if (definition.name.getIdentifier().equals(rightName.getIdentifier())) {
            rightDefStatement = definition.getStatement();

            if (rightDefStatement instanceof VariableDeclarationStatement) {
              VariableDeclarationStatement varDecl;
              varDecl = (VariableDeclarationStatement) rightDefStatement;
              VariableDeclarationFragment varDeclFrag;
              varDeclFrag = (VariableDeclarationFragment) varDecl.fragments().get(0);
              if (varDeclFrag.getInitializer() instanceof NumberLiteral) {
                rightValue = (NumberLiteral) (varDeclFrag.getInitializer());
              } else {
                break;
              }
            } else {
              ExpressionStatement expStat = (ExpressionStatement) rightDefStatement;
              Assignment ass = (Assignment) expStat.getExpression();
              if (ass.getRightHandSide() instanceof NumberLiteral) {
                rightValue = (NumberLiteral) ass.getRightHandSide();
              } else {
                break;
              }
            }
          }
        }

        AST ast = infixExp.getAST();
        InfixExpression newInfixExp = ast.newInfixExpression();
        newInfixExp.setOperator(infixExp.getOperator());
        NumberLiteral rightOp = ast.newNumberLiteral(rightValue.toString());
        newInfixExp.setRightOperand(rightOp);
        NumberLiteral leftOperand = ast.newNumberLiteral(leftValue.toString());
        newInfixExp.setLeftOperand(leftOperand);
        TreeModificationUtils.replaceChildInParent(infixExp, newInfixExp);
        hasChanged = true;
      } else if (infixExp.getLeftOperand() instanceof SimpleName) {
        SimpleName varName = (SimpleName) infixExp.getLeftOperand();
        for (Definition definition : defs) {
          if (definition.name.getIdentifier().equals(varName.getIdentifier())) {

            Statement defStatement = definition.getStatement();
            NumberLiteral value;

            if (defStatement instanceof VariableDeclarationStatement) {
              VariableDeclarationStatement varDecl;
              varDecl = (VariableDeclarationStatement) defStatement;
              VariableDeclarationFragment varDeclFrag;
              varDeclFrag = (VariableDeclarationFragment) varDecl.fragments().get(0);
              if (varDeclFrag.getInitializer() instanceof NumberLiteral) {
                value = (NumberLiteral) (varDeclFrag.getInitializer());
              } else {
                break;
              }
            } else {
              ExpressionStatement expStat = (ExpressionStatement) defStatement;
              Assignment ass = (Assignment) expStat.getExpression();
              if (ass.getRightHandSide() instanceof NumberLiteral) {
                value = (NumberLiteral) ass.getRightHandSide();
              } else {
                break;
              }
            }

            AST ast = infixExp.getAST();
            InfixExpression newInfixExp = ast.newInfixExpression();
            newInfixExp.setOperator(infixExp.getOperator());
            NumberLiteral rightOp = ast.newNumberLiteral(infixExp.getRightOperand().toString());
            newInfixExp.setRightOperand(rightOp);
            NumberLiteral leftOperand = ast.newNumberLiteral(value.toString());
            newInfixExp.setLeftOperand(leftOperand);
            TreeModificationUtils.replaceChildInParent(infixExp, newInfixExp);
            hasChanged = true;
          }
        }
      } else if (infixExp.getRightOperand() instanceof SimpleName) {
        SimpleName varName = (SimpleName) infixExp.getRightOperand();
        for (Definition definition : defs) {
          if (definition.name.getIdentifier().equals(varName.getIdentifier())) {

            Statement defStatement = definition.getStatement();
            NumberLiteral value;

            if (defStatement instanceof VariableDeclarationStatement) {
              VariableDeclarationStatement varDecl;
              varDecl = (VariableDeclarationStatement) defStatement;
              VariableDeclarationFragment varDeclFrag;
              varDeclFrag = (VariableDeclarationFragment) varDecl.fragments().get(0);
              if (varDeclFrag.getInitializer() instanceof NumberLiteral) {
                value = (NumberLiteral) (varDeclFrag.getInitializer());
              } else {
                break;
              }
            } else {
              ExpressionStatement expStat = (ExpressionStatement) defStatement;
              Assignment ass = (Assignment) expStat.getExpression();
              if (ass.getRightHandSide() instanceof NumberLiteral) {
                value = (NumberLiteral) ass.getRightHandSide();
              } else {
                break;
              }
            }

            AST ast = infixExp.getAST();
            InfixExpression newInfixExp = ast.newInfixExpression();
            newInfixExp.setOperator(infixExp.getOperator());
            NumberLiteral rightOperand = ast.newNumberLiteral(infixExp.getLeftOperand().toString());
            newInfixExp.setRightOperand(rightOperand);
            NumberLiteral leftOperand = ast.newNumberLiteral(value.toString());
            newInfixExp.setLeftOperand(leftOperand);
            TreeModificationUtils.replaceChildInParent(infixExp, newInfixExp);
            hasChanged = true;
          }
        }
      }
    }
    
    return hasChanged;
  }

  /**
   * Performs constant folding an a Java file.
   *
   * @param args args[0] is the file to fold and args[1] is where to write the
   *      output
   */
  public static void main(String[] args) {
    if (args.length != 2) {
      log.error("Missing Java input file or output file on command line");
      System.out.println("usage: java DomViewer <java file to parse> <html file to write>");
      return;
    }

    File inputFile = new File(args[0]);
    ASTNode node = JavaSourceUtils.getCompilationUnit(inputFile.toURI());

    
    
    boolean hasChanged = true;

    while (hasChanged) {
      node = ConstantFolding.fold(node);
      hasChanged = ConstantPropagation.propagate(node);
    }

    try {
      PrintWriter writer = new PrintWriter(args[1], "UTF-8");
      writer.print(node.toString());
      writer.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
