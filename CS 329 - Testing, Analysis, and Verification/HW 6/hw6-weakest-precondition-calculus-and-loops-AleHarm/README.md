# Objectives

   * Prove partial correctness in programs with weakest pre-condition calculus
   * Prove total correctness in programs with weakest pre-condition calculus
   * Prove programs in Dafny using weakest pre-condition calculus as a guide

# Reading

   * [weakest-precondition.md](https://bitbucket.org/byucs329/byu-cs-329-lecture-notes/src/master/weakest-preconditions.md): all about weakest pre-condition calculus
   * [wp-for-loops.md](https://bitbucket.org/byucs329/byu-cs-329-lecture-notes/src/master/wp-for-loops.md)

# Acknowledgement

This homework is largely the same as [this lab exercise](http://www.cse.chalmers.se/edu/year/2016/course/course/TDA567_Testing_debugging_and_verification/Lab3.html) from Chalmers.

# Guidelines

Please follow these general guidelines for the paper-pencil proofs:  

   1. State an expression for the weakest precondition of the program.
   2. State the goal of the proof: `P ==> wp(S,Q)` 
   2. Proceed to calculate the weakest precondition of the program by applying the appropriate inference rules.

Also, be mindful of the following

   * If the rule application resulted in a new statement, simply continue with the proof in the same way.
   * If it resulted in a logical formula, explain why the formula is valid.
   * If it resulted in several new subgoals or conjuncts, you may pick one of them and continue proving that one. When the first subgoal has been proved true, don't forget that you have to go back and prove the other ones, too.     Number the intermediate results if there is any danger of confusion.
   * If you used the loop invariant rule, clearly state which formula you are using as invariant.

The proofs can be rather lengthy. You can copy and paste to avoid excessive typing. When you are dealing with large formulas in the pre- or post-condition you can introduce names to abbreviate them. Just keep in mind that when you are applying an update to a formula you have to unfold the abbreviation first before you apply the update (i.e., you need to substitute through the entire formula).

Whenever you are asked to implement something using Dafny you should:

   1. Write a file with a *dfy* extension corresponding to the program
   2. Make sure Dafny can prove your program correct according to specification


# Problems

## 1. Paper-pencil Proof (20 points)

Below is the familiar Dafny program for computing an absolute value of a number

```java
method Abs(x : int) returns (y : int) 
  ensures 0 <= y;
  ensures 0 <= x ==> y == x;
  ensures x < 0 ==> y == -x;
{
  if (x < 0)
   {y := -x;}
  else
   {y := x;}
}
```

Use the weakest precondition calculus to prove that the program is correct with respect to the specification. Show the entire proof.

## 2. Complete the Specification (20 points)

Below is a small Dafny program

```java
method Q2(x : int, y : int) returns (big : int, small : int) 
  ensures big > small;
{
  if (x > y)
   {big, small := x, y;}
  else
   {big, small := y, x;}
}
```
**Part A**: Dafny will not be able to prove the above program correct because its specification is incomplete. Figure out exactly why the proof fails, and from there derive the missing part of the specification.  **Changing the postcondition or method body is not allowed**; rather, use weakest precondition calculus to find a suitable precondition. Show each step of the derivation. **The precondition must come from the proof using the weakest precondition calculus.**

**Part B**: 
Show that your answer from **Part A** enables Dafny to prove the program correct.

## 3. Total Correctness with Loops (30 points)

The following method multiplies two integers.
```java
method Q3(n0 : int, m0 : int) returns (res : int)
{
  var n, m : int;
  res := 0;
  if (n0 >= 0) 
       {n,m := n0, m0;} 
  else 
       {n,m := -n0, -m0;}
  while (0 < n) 
  { 
    res := res + m; 
    n := n - 1; 
  }
}
```

`n0` and `m0` are the two integer inputs to the program copied into the local variables `n` and `m` respectively because Dafny does not allow the re-assignment of formal parameters. `res` is the integer result. 

**Part A**: Write a specification for the above method and show that Dafny is able to prove total correctness relative to the specification.  Use weakest-precondition calculus as needed to create the specification.


**Part B**: Construct a paper-pencil proof of the method showing all 5 obligations required to compute the weakest-precondition for the loop for total correctness.
![alt text](image.png)

## 4. Total Correctness Relative to Recursive Definition (30 points)

**OPTIONAL -- NOT REQUIRED**

The factorial function, `n!` computes the product of all numbers from 1 up to n (e.g., `3! = 1 * 2 * 3 = 6`). The following Dafny program iteratively calculates the factorial of a number.

```java
method ComputeFact(n : nat) returns (res : nat)
requires n > 0;
ensures res == fact(n);
{
  res := 1;
  var i := 2;
  while (i <= n) 
  {
    res := res * i;
    i := i + 1;
  }
}
 ```

The variables are natural numbers. Prove that the program is correct with respect to the following recursive definition:

```
  fact(1) = 1
  fact(m) = m * fact(m - 1)
```

**Part A**: Complete the specification so that Dafny is able to prove `ComputeFact` correct relative to the specification.

**Part B**: Construct a paper-pencil proof of total correctness of the loop.

# Submission

Submit to canvas the URL for the pull request with the Dafny models and paper-pencil proofs. The pull request should include a file **EXPLANATIONS.md** explaining your solution to each problem and how to load each into Dafny to see if it is able to prove the models correct. It should also make clear where to find the proofs. Please be organized and make it easy to find and grade each problem.