// Import the Array2DImple class
import { LazyProxy } from "./LazyProxy";

// Create an instance of Array2DImple
const arrayProxy = new LazyProxy(3, 3);

console.log("Before calling method, array is null is " + arrayProxy.isArrayNull());

// Populate the array with sample values
arrayProxy.set(0, 0, 1);
arrayProxy.set(0, 1, 2);
arrayProxy.set(0, 2, 3);
arrayProxy.set(1, 0, 4);
arrayProxy.set(1, 1, 5);
arrayProxy.set(1, 2, 6);
arrayProxy.set(2, 0, 7);
arrayProxy.set(2, 1, 8);
arrayProxy.set(2, 2, 9);

console.log("After calling method, array is null is " + arrayProxy.isArrayNull());

console.log("Populated Array:");
for (let i = 0; i < 3; i++) {
  for (let j = 0; j < 3; j++) {
    console.log(`Value at (${i}, ${j}): ${arrayProxy.get(i, j)}`);
  }
}

arrayProxy.save("test.txt");



const arrayProxy2 = new LazyProxy(3,3);

console.log("Before calling method, array2 is null is " + arrayProxy2.isArrayNull());

arrayProxy2.load("test.txt");

console.log("After calling method, array2 is null is " + arrayProxy2.isArrayNull());