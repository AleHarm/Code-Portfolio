import { Array2D } from "./Array2D";
import * as fs from 'fs';
import { Array2DImple } from "./Array2DImple";

export class LazyProxy implements Array2D{

  values: Array2DImple | null = null;
  rows: number | undefined;
  cols: number | undefined;
  fileName: string | undefined;

  public isArrayNull(){

    if(this.values == null){
      return true;
    }else{
      return false;
    }
  }

  

  public constructor(numRows?:number, numCols?:number, fileName?:string){

    this.rows = numRows;
    this.cols = numCols;
    this.fileName = fileName;
  }

  public set(row: number, col: number, value: number): void{

    if(this.values == null){

      this.values = new Array2DImple(this.rows, this.cols, this.fileName);
    }

    this.values.set(row, col, value);
  }

  public get(row: number, col: number): number{

    if(this.values == null){

      this.values = new Array2DImple(this.rows, this.cols, this.fileName);
    }

    return this.values.get(row, col);
  }

  public save(fileName:string): void{

    if(this.values == null){

      this.values = new Array2DImple(this.rows, this.cols, this.fileName);
    }

    this.values.save(fileName);
  }

  public load(fileName:string){

    if(this.values == null){

      this.values = new Array2DImple(this.rows, this.cols, this.fileName);
    }

    this.values.load(fileName);
  }
}

