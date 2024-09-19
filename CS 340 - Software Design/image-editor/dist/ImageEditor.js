"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || function (mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (k !== "default" && Object.prototype.hasOwnProperty.call(mod, k)) __createBinding(result, mod, k);
    __setModuleDefault(result, mod);
    return result;
};
Object.defineProperty(exports, "__esModule", { value: true });
const fs = __importStar(require("fs"));
class Color {
    red;
    green;
    blue;
    constructor() {
        this.red = 0;
        this.green = 0;
        this.blue = 0;
    }
}
class ImageFile {
    pixels;
    constructor(width, height) {
        this.pixels = new Array(width);
        for (let i = 0; i < width; i++) {
            this.pixels[i] = new Array(height);
        }
    }
    getWidth() {
        return this.pixels.length;
    }
    getHeight() {
        return this.pixels[0].length;
    }
    setColor(x, y, color) {
        this.pixels[x][y] = color;
    }
    getColor(x, y) {
        return this.pixels[x][y];
    }
}
function run() {
    const args = processCommandLineArguments();
    if (args.length < 3) {
        usage();
        return;
    }
    let inputFile = args[0];
    let outputFile = args[1];
    let filter = args[2];
    //READ FILE
    let image = readAndParse(inputFile);
    //CHECK FILTER TYPE
    if (filter == "grayscale" || filter == "greyscale") {
        if (args.length != 3) {
            usage();
            return;
        }
        grayscale(image);
    }
    else if (filter == "invert") {
        if (args.length != 3) {
            usage();
            return;
        }
        invert(image);
    }
    else if (filter == "emboss") {
        if (args.length != 3) {
            usage();
            return;
        }
        emboss(image);
    }
    else if (filter == "motionblur") {
        if (args.length != 4) {
            usage();
            return;
        }
        motionblur(image, parseInt(args[3]));
    }
    else {
        usage();
    }
    //WRITE IMAGE OUT
    write(image, outputFile);
    console.log("Everything good chief o7");
}
function processCommandLineArguments() {
    const args = process.argv.slice(2);
    return args;
}
function usage() {
    console.log("USAGE: node ImageEditor.ts <in-file> <out-file> <grayscale|invert|emboss|motionblur> {motion-blur-length}");
}
function readAndParse(filePath) {
    try {
        const fileContent = fs.readFileSync(filePath, 'utf-8');
        let P3Cut = fileContent.slice(fileContent.indexOf("\n"));
        let width = parseInt(P3Cut.substring(1, P3Cut.indexOf(" ")), 10);
        let widthCut = P3Cut.slice(P3Cut.indexOf(" "));
        let height = parseInt(widthCut.substring(1, widthCut.indexOf("\n")), 10);
        let imageString255 = widthCut.slice(widthCut.indexOf("\n"));
        let imageString = imageString255.slice(imageString255.indexOf(" ") + 1);
        const stringArray = imageString.split(/[^0-9]+/);
        const numberArray = stringArray.map(Number);
        console.log('Width: ', width);
        console.log('Height: ', height);
        console.log('Image String: ', imageString);
        console.log("Numbers: ", numberArray);
        //CONSTRUCT IMAGE
        let image = new ImageFile(width, height);
        for (let y = 0; y < height; y++) {
            for (let x = 0; x < width * 3; x += 3) {
                let redVal = numberArray[x + y * width * 3];
                let greenVal = numberArray[x + y * width * 3 + 1];
                let blueVal = numberArray[x + y * width * 3 + 2];
                let color = { red: redVal, green: greenVal, blue: blueVal };
                image.setColor(x / 3, y, color);
            }
        }
        return image;
    }
    catch (error) {
        console.log("There was an error");
    }
}
function write(image, filePath) {
    var imageString = "P3\n" + image.getWidth() + " " + image.getHeight() + "\n255\n";
    for (var y = 0; y < image.getHeight(); y++) {
        for (var x = 0; x < image.getWidth(); x++) {
            var color = image.getColor(x, y);
            if (x != 0) {
                imageString += " ";
            }
            imageString += color.red + " " + color.green + " " + color.blue;
        }
        imageString += "\n";
    }
    fs.writeFile(filePath, imageString, (err) => {
        if (err) {
            console.error('Error writing to file:', err);
        }
        else {
            console.log('File written successfully.');
        }
    });
}
function invert(image) {
    for (let x = 0; x < image.getWidth(); ++x) {
        for (let y = 0; y < image.getHeight(); ++y) {
            const color = image.getColor(x, y);
            color.red = 255 - color.red;
            color.green = 255 - color.green;
            color.blue = 255 - color.blue;
        }
    }
}
function grayscale(image) {
    for (var x = 0; x < image.getWidth(); x++) {
        for (var y = 0; y < image.getHeight(); y++) {
            var color = image.getColor(x, y);
            var grayLevel = (color.red + color.green + color.blue) / 3;
            grayLevel = Math.floor(Math.max(0, Math.min(grayLevel, 255)));
            color.red = grayLevel;
            color.green = grayLevel;
            color.blue = grayLevel;
        }
    }
}
function emboss(image) {
    for (var x = image.getWidth() - 1; x >= 0; x--) {
        for (var y = image.getHeight() - 1; y >= 0; y--) {
            var color = image.getColor(x, y);
            var diff = 0;
            if (x > 0 && y > 0) {
                var upLeftColor = image.getColor(x - 1, y - 1);
                if (Math.abs(color.red - upLeftColor.red) > Math.abs(diff)) {
                    diff = color.red - upLeftColor.red;
                }
                if (Math.abs(color.green - upLeftColor.green) > Math.abs(diff)) {
                    diff = color.green - upLeftColor.green;
                }
                if (Math.abs(color.blue - upLeftColor.blue) > Math.abs(diff)) {
                    diff = color.blue - upLeftColor.blue;
                }
            }
            var grayLevel = (128 + diff);
            grayLevel = Math.max(0, Math.min(grayLevel, 255));
            color.red = grayLevel;
            color.green = grayLevel;
            color.blue = grayLevel;
        }
    }
}
function motionblur(image, length) {
    if (length < 1) {
        return;
    }
    for (var x = 0; x < image.getWidth(); ++x) {
        for (var y = 0; y < image.getHeight(); ++y) {
            var color = image.getColor(x, y);
            var maxX = Math.min(image.getWidth() - 1, x + length - 1);
            for (var i = x + 1; i <= maxX; ++i) {
                var tmpColor = image.getColor(i, y);
                color.red += tmpColor.red;
                color.green += tmpColor.green;
                color.blue += tmpColor.blue;
            }
            var delta = (maxX - x + 1);
            color.red /= delta;
            color.green /= delta;
            color.blue /= delta;
        }
    }
}
run();
//# sourceMappingURL=ImageEditor.js.map