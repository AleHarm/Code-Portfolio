package rdInputs;

public class should_HaveDefinitionBeforeInsideAndAfterIfStatement_when_IfStatementHasOneAssignmentNoMocks {
  public void name(){
    int a = 5;

    if(true){

      a = 10;
    }else{

      int b = 15;
    }

    int c = 11;
  }
}
