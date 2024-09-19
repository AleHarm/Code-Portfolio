import { NumberString } from "./NumberString"
import { MakeExclamatory } from "./MakeExclamatory"
import { MakeInquisitive } from "./MakeInquisitive"

class Main{

  static main() {
    var ss = new NumberString();
    console.log(ss);
    ss = new MakeExclamatory(ss);
    console.log(ss);
    ss = new MakeInquisitive(ss);
    console.log(ss);
  }
}

Main.main()