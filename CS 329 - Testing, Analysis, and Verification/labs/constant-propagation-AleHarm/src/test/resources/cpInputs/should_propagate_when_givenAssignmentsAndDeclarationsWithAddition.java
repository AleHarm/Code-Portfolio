package edu.byu.cs329.constantpropagation.resources;

public class should_propagate_when_givenAssignmentsAndDeclarationsWithAddition {
  public void name(){

    int a = 4;
    int b = a;
    int c = a + b;
    int d = a + 3;
    int e = 6 + c;
    b = 12 + a;
    c = b + 2;
    e = b + c;
  }
}
