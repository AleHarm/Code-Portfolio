package edu.byu.cs329.rd;

import edu.byu.cs329.cfg.ControlFlowGraph;
import edu.byu.cs329.rd.ReachingDefinitions.Definition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

/**
 * Builder for reaching definitions on a control flow graph.
 */
public class ReachingDefinitionsBuilder {
  private List<ReachingDefinitions> rdList = null;
  private Map<Statement, Set<Definition>> entrySetMap = null;

  /**
   * Computes the reaching definitions for each control flow graph.
   *
   * @param cfgList the list of control flow graphs.
   * @return the coresponding reaching definitions for each graph.
   */
  public List<ReachingDefinitions> build(List<ControlFlowGraph> cfgList) {

    rdList = new ArrayList<ReachingDefinitions>();

    for (ControlFlowGraph cfg : cfgList) {

      ReachingDefinitions rd = computeReachingDefinitions(cfg);
      rdList.add(rd);
    }
    return rdList;
  }

  private ReachingDefinitions computeReachingDefinitions(ControlFlowGraph cfg) {
    entrySetMap = new HashMap<Statement, Set<Definition>>();
    Map<Statement, Set<Definition>> exitSetMap = new HashMap<>();
    
    Statement start = cfg.getStart();
    
    //Create worklist
    Queue<Statement> worklist = new LinkedList<>();

    //add everything to worklist initially
    worklist.add(start);

    //Go through each item on worklist
    while (!worklist.isEmpty()) {

      //Setup
      Statement currentStatement = worklist.poll();
      List<Statement> parentStatements;
      Set<Definition> entryStatements = new HashSet<>();
      Set<Definition> exitStatements = new HashSet<>();
      boolean needAddSuccs = false;

      //Setup entry and exit statements
      if (cfg.getPreds(currentStatement) != null) {

        parentStatements = new ArrayList<>(cfg.getPreds(currentStatement));

        for (Statement statement : parentStatements) {
          
          if (exitSetMap.get(statement) != null && exitSetMap.get(statement).size() > 0) {

            Set<Definition> defs = exitSetMap.get(statement);
            for (Definition def : defs) {

              entryStatements.add(def);
            }
          }
        }

        exitStatements.addAll(entryStatements);
      }

      //Add declaration generation to exit statements
      if (currentStatement instanceof VariableDeclarationStatement 
          && entrySetMap.get(currentStatement) == null) {

        VariableDeclarationStatement vdStatement = (VariableDeclarationStatement) currentStatement;
        VariableDeclaration varDecl = (VariableDeclaration) vdStatement.fragments().get(0);
        SimpleName name = varDecl.getName();
        Definition newDefinition = createDefinition(name, currentStatement);
        exitStatements.add(newDefinition);
        needAddSuccs = true;
      }

      //Add assignment generation to exit statements
      if (currentStatement instanceof ExpressionStatement 
          && entrySetMap.get(currentStatement) == null) {

        ExpressionStatement expStatement = (ExpressionStatement) currentStatement;
        Assignment assignment = (Assignment) expStatement.getExpression();
        SimpleName name = (SimpleName) assignment.getLeftHandSide();
        Definition newDefinition = createDefinition(name, currentStatement);
        
        for (Definition def : entryStatements) {

          if (def.getName().getIdentifier().equals(name.getIdentifier())) {

            exitStatements.remove(def);
            break;
          }
        }
        exitStatements.add(newDefinition);
        needAddSuccs = true;
      }

      //Add children of if statement if they generate definitions
      if (currentStatement instanceof IfStatement) {

        IfStatement ifStatement = (IfStatement) currentStatement;
        @SuppressWarnings("unchecked")
        List<Statement> thenStatements = ((Block) (ifStatement.getThenStatement())).statements();

        if (entrySetMap.get(currentStatement) == null && thenStatements.size() > 0) {

          for (Statement statement : thenStatements) {

            if (statement instanceof VariableDeclarationStatement 
                || statement instanceof ExpressionStatement) {

              needAddSuccs = true;
            }
          }
        }
      }
      
      //If the item's exit set is different from the entry set, add successors to worklist

      if (needAddSuccs && cfg.getSuccs(currentStatement) != null) {

        Set<Statement> successors = cfg.getSuccs(currentStatement);
        List<Statement> succs = new ArrayList<>(successors);

        for (Statement statement : succs) {
          
          worklist.add(statement);
        }
      }

      //Add entry and exit statements to exit and entry map
      entrySetMap.put(currentStatement, entryStatements);
      exitSetMap.put(currentStatement, exitStatements);
    }
    
    // End of implementation
    
    return new ReachingDefinitions() {

      final Map<Statement, Set<Definition>> reachingDefinitions = 
          Collections.unmodifiableMap(entrySetMap);

      @Override 
      public Set<Definition> getReachingDefinitions(final Statement s) {

        Set<Definition> returnValue = null;
        if (reachingDefinitions.containsKey(s)) {
          returnValue = reachingDefinitions.get(s);
        }
        return returnValue;
      }
    };
  }

  private Definition createDefinition(SimpleName name, Statement statement) {

    Definition definition = new Definition();
    definition.name = name;
    definition.statement = statement;
    return definition;
  }
}
