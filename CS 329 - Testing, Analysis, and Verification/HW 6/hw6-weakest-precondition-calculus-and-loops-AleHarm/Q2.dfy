method Q2(x : int, y : int) returns (big : int, small : int) 
  requires x != y // This requires statement added
  ensures big > small;
{
  if (x > y)
   {big, small := x, y;}
  else
   {big, small := y, x;}
}