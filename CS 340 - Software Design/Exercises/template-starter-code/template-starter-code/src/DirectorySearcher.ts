import * as fs from "fs";
import * as path from "path";

export abstract class DirectorySearcher{

  protected _dirName: string;
  protected _fileRegExp: RegExp;
  protected _recurse: boolean;
  protected _totalCount: number = 0;

  protected constructor(dirName: string, filePattern: string, recurse: boolean = false){

    this._dirName = dirName;
    this._fileRegExp = RegExp(filePattern);
    this._recurse = recurse;
  }
  protected async run(){

    await this.searchDirectory(this._dirName);
    console.log(`TOTAL: ${this._totalCount}`);
  }

  protected async searchDirectory(filePath: string) {
    if (!this.isDirectory(filePath)) {
      this.nonDirectory(filePath);
      return;
    }

    if (!this.isReadable(filePath)) {
      this.unreadableDirectory(filePath);
      return;
    }

    const files = fs.readdirSync(filePath);

    for (let file of files) {
      const fullPath = path.join(filePath, file);
      if (this.isFile(fullPath)) {
        if (this.isReadable(fullPath)) {
          await this.evaluate(fullPath);
        } else {
          this.unreadableFile(fullPath);
        }
      }
    }

    if (this._recurse) {
      for (let file of files) {
        const fullPath = path.join(filePath, file);
        if (this.isDirectory(fullPath)) {
          await this.searchDirectory(fullPath);
        }
      }
    }
  }

  protected isDirectory(path: string): boolean {
    try {
      return fs.statSync(path).isDirectory();
    } catch (error) {
      return false;
    }
  }

  protected isFile(path: string): boolean {
    try {
      return fs.statSync(path).isFile();
    } catch (error) {
      return false;
    }
  }

  protected isReadable(path: string): boolean {
    try {
      fs.accessSync(path, fs.constants.R_OK);
      return true;
    } catch (error) {
      return false;
    }
  }

  protected nonDirectory(dirName: string): void {
    console.log(`${dirName} is not a directory`);
  }

  protected unreadableDirectory(dirName: string): void {
    console.log(`Directory ${dirName} is unreadable`);
  }

  protected unreadableFile(fileName: string): void {
    console.log(`File ${fileName} is unreadable`);
  }

  protected abstract evaluate(fullPath: string): void;

}