method reverse(a: array?<int>) returns (res: array?<int>)
  requires a != null
  ensures res != null 
  && res.Length == a.Length 
  && forall i: int :: 0 <= i < a.Length ==> res[i] == a[(a.Length - 1) - i]
  && a.Length == 0 ==> a == res
{
  var i := a.Length - 1;
  var revArr := new int[a.Length];

  while i >= 0
    invariant -1 <= i <= a.Length - 1
    decreases i
  {
    revArr[i] := a[(a.Length - 1) - i];
    i := i - 1;
  }

  return revArr;
}