package rdInputs;

public class should_HaveDefinitionBeforeInsideAndAfterIfStatement_when_IfStatementHasOneDeclarationNoMocks {
  public void name(){
    int a = 5;

    if(true){

      int b = 10;
    }

    int c = 11;
  }
}
