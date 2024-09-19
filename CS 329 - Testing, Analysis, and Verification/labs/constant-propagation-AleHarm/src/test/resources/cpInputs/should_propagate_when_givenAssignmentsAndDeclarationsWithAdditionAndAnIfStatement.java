package edu.byu.cs329.constantpropagation.resources;

public class should_propagate_when_givenAssignmentsAndDeclarationsWithAdditionAndAnIfStatement {
  public void name(){

    int a = 4;
    int b = 2;
    int c = a + b;
    if(7 < c){
      b = 12 + a;
    }
    c = b + 2;
  }
}
