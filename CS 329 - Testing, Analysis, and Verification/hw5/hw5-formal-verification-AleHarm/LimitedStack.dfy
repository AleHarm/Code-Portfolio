class LimitedStack {

  var arr : array<int>    // contents
  var capacity : int   // max number of elements in stack.
  var top : int       // The index of the top of the stack, or -1 if the stack is empty.

  ghost predicate Valid()
    reads this
  {
    && arr.Length == capacity
    && -1 <= top < arr.Length
  }

  ghost predicate Empty()
    reads this
  {
    && top == -1
  }

  ghost predicate Full()
    reads this
  {
    && top == arr.Length - 1
  }

  method Init(c : int)
    requires 
      && c > 0
    ensures 
      && Valid()
      && fresh(arr)
      && arr.Length == capacity
      && capacity == c
      && top == -1
    modifies this
  {
      capacity := c;
      arr := new int[c];
      top := -1;
  }

  method Push(val: int)
    requires 
      && Valid()
    ensures 
      && Valid()
      && (!old(Full()) ==> 
        && top == old(top) + 1 
        && arr[top] == val
        && (forall i : int :: 0 <= i < old(top) + 1 ==> arr[i] == old(arr[i])))
    modifies this`top, arr
  {
      if (top < capacity - 1) {
          top := top + 1; // Increment top
          arr[top] := val; // Store val at the top of the stack
      }
  }

  method Push2(val: int)
    requires
      && Valid()
    ensures
      && Valid()
      && !Empty()
      && (!old(Full()) ==> 
        && -1 <= old(top) < arr.Length
        && top == old(top) + 1 
        && arr[top] == val
        && (forall i : int :: 0 <= i <= old(top) ==> arr[i] == old(arr[i])))
      && (old(Full()) ==> 
        && top == old(top)
        && top == (arr.Length - 1)
        && arr[top] == val
        && (forall i : int :: 0 <= i < capacity - 1 ==> arr[i] == old(arr[i + 1])))
    modifies this`top, this.arr
  {

    if(top != -1 && top == arr.Length - 1){

      Shift();
    }

    top := top + 1;
    arr[top] := val;
  }

  method Pop() returns (val: int)
    requires 
      && Valid()
    ensures
      && Valid()
      && (!old(Empty()) ==> 
        && (top == old(top) - 1
        && val == arr[old(top)]
        && !Full()
        && (forall i : int :: 0 <= i < (old(top) - 1) ==> arr[i] == old(arr[i]))))
      
    modifies this`top
    {
      if(top != -1){
        var intToReturn := arr[top];
        top := top - 1;
        return intToReturn;
      }
      return -1;
    }

  method Peek() returns (val: int)
    requires 
      && Valid()
    ensures
      && Valid()
      && arr == old(arr)
      && top == old(top)
      && (Empty() ==> val == -1)
      && (!Empty() ==> val == arr[top])
  {

    if(top == -1){
      return -1;
    }else{
      return arr[top];
    }
  }

  method Shift()
    requires 
      && Valid() 
      && !Empty()
    ensures 
      && Valid()
      && (forall i : int :: 0 <= i < capacity - 1 ==> arr[i] == old(arr[i + 1]))
      && (top == old(top) - 1)
    modifies this.arr, this`top
    {
      var i : int := 0;
      while (i < capacity - 1 )
      invariant 0 <= i < capacity
      invariant top == old(top)
      invariant forall j : int :: 0 <= j < i ==> arr[j] == old(arr[j + 1])
      invariant forall j : int :: i <= j < capacity ==> arr[j] == old(arr[j])
      {
          arr[i] := arr[i + 1];
          i := i + 1;
      }
      top := top - 1;
    }

// Feel free to add extra ones as well.
  method Main(){
      var s := new LimitedStack;
      s.Init(3);

      assert s.Empty() && !s.Full();

      s.Push(27);
      assert !s.Empty();

      var e := s.Pop();
      assert e == 27;

      s.Push(5);
      s.Push(32);
      s.Push(9);
      assert s.Full();

      var e2 := s.Pop();
      assert e2 == 9 && !s.Full();
      assert s.arr[0] == 5;

      s.Push(e2);
      s.Push2(99);

      var e3 := s.Peek();
      assert e3 == 99;
      assert s.arr[0] == 32;
  }
}