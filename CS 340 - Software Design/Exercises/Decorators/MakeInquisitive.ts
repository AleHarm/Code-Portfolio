import { StringSource } from "./StringSource";

export class MakeInquisitive implements StringSource{

  private source: StringSource;

  constructor(ss: StringSource){

    this.source = ss;
  }

  next(): string {
    return this.source + "????";
  }
}