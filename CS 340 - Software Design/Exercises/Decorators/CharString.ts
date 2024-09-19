import { StringSource } from "./StringSource";

export class CharString implements StringSource{
  next(): string {
    return "qwertyuiopasdfghjklzxcvbnm";
  }
}