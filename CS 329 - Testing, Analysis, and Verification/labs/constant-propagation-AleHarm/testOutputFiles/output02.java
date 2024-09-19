package edu.byu.cs329.constantpropagation.resources;
public class should_notPropagate_when_givenAssignmentsAndDeclarationsWithSubtraction {
  public void name(){
    int a=4;
    int b=2;
    int c=4 - 2;
    b=16;
    c=18;
  }
}
