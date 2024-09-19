import { Array2D } from "./Array2D";
import * as fs from 'fs';

export class Array2DImple implements Array2D{

  values: number[][];

  set(row: number, col: number, value: number): void{

    this.values[row][col] = value;
  }

  get(row: number, col: number): number{

    return this.values[row][col];
  }

  public constructor(numRows?:number, numCols?:number, fileName?:string){

    if(numRows && numCols){

      this.values =  new Array(numRows);
      for (let i = 0; i < numRows; i++) {
        this.values[i] = new Array(numCols);
      }
    }else{
      this.values =  new Array(10);
      for (let i = 0; i < 10; i++) {
        this.values[i] = new Array(10);
      }
    }

    if(fileName){

      this.load(fileName);
    }
  }

  public save(fileName:string): void{

    const data = JSON.stringify(this.values);
    fs.writeFileSync(fileName, data);
  }

  public load(fileName:string){

    try {
      const data = fs.readFileSync(fileName, 'utf8');
  
      const jsonData: number[][] = JSON.parse(data);
  
      this.values = jsonData;
    } catch (err) {
        console.error('Error reading or parsing file:', err);
    }
  }
}

