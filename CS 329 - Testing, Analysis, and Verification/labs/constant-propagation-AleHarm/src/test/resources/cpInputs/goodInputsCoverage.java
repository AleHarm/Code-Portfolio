package edu.byu.cs329.constantpropagation.resources;

public class goodInputsCoverage {
  public void name(){

    int a = 4;
    int b = 2;
    int c = a + b;
    int d = 1;
    d = c + b;
    int e = d;
    if(c < 7){
      if(7 < c){
      b = 12 + a;
      }
    }
    a = 4;
    int f = d + a;
    int g = a + 2;
    int h = 2 + a;
    if(0 < a){
      if(a < 0){

        a = 12 + a;
      }
    }
    c = b + 2;
  }
}
