package rdInputs;

public class should_HaveDefinitionBeforeInsideAndAfterIfStatement_when_IfStatementHasOneAssignmentNoMocks {
  public void name(){
    int a = 5;

    if(true){

      a = 10;
    }

    int c = 11;
  }
}
