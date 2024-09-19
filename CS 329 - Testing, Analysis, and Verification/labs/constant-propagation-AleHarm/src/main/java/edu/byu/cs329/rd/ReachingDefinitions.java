package edu.byu.cs329.rd;

import java.util.Set;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;

/**
 * Interface for reaching definitions.
 */
public interface ReachingDefinitions {
  
  /**
   * Definition entity.
   */
  public static class Definition { 
    public SimpleName name;
    public Statement statement;

    public Statement getStatement() {

      return statement;
    }

    public SimpleName getName() {

      return name;
    }
  }
  
  public Set<Definition> getReachingDefinitions(final Statement s);
}

