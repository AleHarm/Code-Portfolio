let attendanceColumn = 2;
let attendanceRow = 1;
let dateFound = false;
let checkInSheetFound = false;
let attendanceSheetFound = false;
let today = Utilities.formatDate(new Date(), "GMT-6", "MM/dd/yyyy");
 
let checkInSheets;
let attendanceSheets = SpreadsheetApp.openByUrl('-').getSheets();
let checkInDataSheet;
let attendanceDataSheet;
 
function TrimName(name){
 
  let trimmedName = "";
  let nameString = name.getValue().toString();
  let spaceFound = false;
  
  for(let i = 0; i < nameString.length; i++){
 
    if(nameString.charAt(i) == " " && !spaceFound){
 
      spaceFound = true;
    }else if(nameString.charAt(i) == " " && spaceFound){
 
      break;
    }
 
    trimmedName = trimmedName + nameString.charAt(i);
  }
 
  return trimmedName;  
}

//Attendance_01 through Attendance_06 use the DecideSheetSummer() function and reference the summer check-in sheet
 
function Attendance_01() {
  
  DecideColumn();
  DecideSheetSummer();
  Search_01();
}
 
function Attendance_02() {
  
  DecideColumn();
  DecideSheetSummer();
  Search_02();
}
 
function Attendance_03() {
  
  DecideColumn();
  DecideSheetSummer();
  Search_03();
}
 
function Attendance_04() {
  
  DecideColumn();
  DecideSheetSummer();
  Search_04();
}

function Attendance_05() {
  
  DecideColumn();
  DecideSheetSummer();
  Search_05();
}

function Attendance_06() {
  
  DecideColumn();
  DecideSheetSummer();
  Search_06();
}

//Attendance_07 through Attendance_12 use the DecideSheetFall() function and reference the fall check-in sheet

function Attendance_07() {
  
  DecideColumn();
  DecideSheetFall();
  Search_01();
}
 
function Attendance_08() {
  
  DecideColumn();
  DecideSheetFall();
  Search_02();
}
 
function Attendance_09() {
  
  DecideColumn();
  DecideSheetFall();
  Search_03();
}
 
function Attendance_10() {
  
  DecideColumn();
  DecideSheetFall();
  Search_04();
}

function Attendance_11() {
  
  DecideColumn();
  DecideSheetFall();
  Search_05();
}

function Attendance_12() {
  
  DecideColumn();
  DecideSheetFall();
  Search_06();
}
 
function DecideColumn(){

  for(let i = 0; attendanceSheetFound != true; i++){
    
    attendanceDataSheet = attendanceSheets[i];
    attendanceColumn = 2;

    let currentCell = attendanceDataSheet.getRange(1,2);

    for(attendanceColumn; dateFound != true; attendanceColumn++){
 
      currentCell = attendanceDataSheet.getRange(attendanceRow,attendanceColumn);


      if(currentCell.isBlank()){

        break;
      }
        
      let currentCellValue = Utilities.formatDate(currentCell.getValue(), "GMT-6", "MM/dd/yyyy");
  
      if(currentCellValue == today){
 
        dateFound = true;
        attendanceColumn -= 1;
      }
    }
    
    if(dateFound){

      attendanceSheetFound = true;
      break;
    }
  }
}
 
function DecideSheetSummer(){
 
 checkInSheets = SpreadsheetApp.openByUrl('-').getSheets();
 
  for(let i = 0; checkInSheetFound != true; i++){
 
    if(Utilities.formatDate(checkInSheets[i].getRange(1,2).getValue(), "GMT-6", "MM/dd/yyyy") == today){
 
      checkInDataSheet = checkInSheets[i];
      checkInSheetFound = true;
    }
  }
}
function DecideSheetFall(){
 
 checkInSheets = SpreadsheetApp.openByUrl('-').getSheets();
 
  for(let i = 0; checkInSheetFound != true; i++){
 
    if(Utilities.formatDate(checkInSheets[i].getRange(1,2).getValue(), "GMT-6", "MM/dd/yyyy") == today){
 
      checkInDataSheet = checkInSheets[i];
      checkInSheetFound = true;
    }
  }
}
 
function Search_01(){
 
  let currentName = attendanceDataSheet.getRange(3,1);
 
  for(let i = 3; i < 100; i++){                        //Enumerates through all students' names on attendanceDataSheet sheet
 
    currentName = attendanceDataSheet.getRange(i,1);
    let currentNameString = TrimName(currentName);
    let comparisonName = checkInDataSheet.getRange(3,2);
    let numAttendances = 0;
    let trial = false;
    let makeup = false;
    let openGym = false;

    Logger.log(currentNameString);

    let r = 3;

    if(currentName.isBlank()){

      break;
    }
  
    for(let c = 2; !comparisonName.isBlank(); c += 3){
 
      for(r = 3; !comparisonName.isBlank(); r++){
 
        comparisonName = checkInDataSheet.getRange(r,c);
        let comparisonNameString = TrimName(comparisonName);
 
        if(currentNameString == comparisonNameString){
 
          let attendanceData = checkInDataSheet.getRange(r,c-1);
 
          if(!attendanceData.isBlank()){
 
            numAttendances += attendanceData.getValue();
            comparisonName.setBackground("#fff2cc");
 
            if(comparisonName.getFontColorObject().asRgbColor().asHexString() == "#ff0000"){
 
              trial = true;
            }
            if(comparisonName.getFontColorObject().asRgbColor().asHexString() == "#0000ff")  {
 
              makeup = true;
            }
            if(comparisonName.getFontColorObject().asRgbColor().asHexString() == "#e69138")  {
 
              openGym = true;
            }
          }        
        }
      }
 
      comparisonName = checkInDataSheet.getRange(r - 2, c);
    }
 
    if(numAttendances != 0){
 
      attendanceDataSheet.getRange(i,attendanceColumn).setValue(numAttendances);
    }else{

      attendanceDataSheet.getRange(i,attendanceColumn).setValue(null);
    }
    if(trial && makeup){
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("purple");
    }else if(trial){
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("red");
    }else if(makeup){
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("blue");
    }else if(openGym){
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("#e69138");
    }else{
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("black");
    }
 
  }
}
 
function Search_02(){
 
  let currentName = attendanceDataSheet.getRange(3,1);
 
  for(let i = 100; i < 200; i++){                        //Enumerates through all students' names on attendanceDataSheet sheet
 
    currentName = attendanceDataSheet.getRange(i,1);
    let currentNameString = TrimName(currentName);
    let comparisonName = checkInDataSheet.getRange(3,2);
    let numAttendances = 0;
    let trial = false;
    let makeup = false;
    let openGym = false;

    Logger.log(currentNameString);
 
    let r = 3;

    if(currentName.isBlank()){

      break;
    }
 
    for(let c = 2; !comparisonName.isBlank(); c += 3){
 
      for(r = 3; !comparisonName.isBlank(); r++){
 
        comparisonName = checkInDataSheet.getRange(r,c);
        let comparisonNameString = TrimName(comparisonName);
 
        if(currentNameString == comparisonNameString){
 
          let attendanceData = checkInDataSheet.getRange(r,c-1);
 
          if(!attendanceData.isBlank()){
 
            numAttendances += attendanceData.getValue();
            comparisonName.setBackground("#fff2cc");
 
            if(comparisonName.getFontColorObject().asRgbColor().asHexString() == "#ff0000"){
 
              trial = true;
            }
            if(comparisonName.getFontColorObject().asRgbColor().asHexString() == "#0000ff")  {
 
              makeup = true;
            }
            if(comparisonName.getFontColorObject().asRgbColor().asHexString() == "#e69138")  {
 
              openGym = true;
            }
          }        
        }
      }
 
      comparisonName = checkInDataSheet.getRange(r - 2, c);
    }
 
    if(numAttendances != 0){
 
      attendanceDataSheet.getRange(i,attendanceColumn).setValue(numAttendances);
    }else{

      attendanceDataSheet.getRange(i,attendanceColumn).setValue(null);
    }
    if(trial && makeup){
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("purple");
    }else if(trial){
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("red");
    }else if(makeup){
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("blue");
    }else if(openGym){
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("#e69138");
    }else{
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("black");
    }
 
  }
}
 
function Search_03(){
 
  let currentName = attendanceDataSheet.getRange(3,1);
 
  for(let i = 200; i < 300; i++){                        //Enumerates through all students' names on attendanceDataSheet sheet
 
    currentName = attendanceDataSheet.getRange(i,1);
    let currentNameString = TrimName(currentName);
    let comparisonName = checkInDataSheet.getRange(3,2);
    let numAttendances = 0;
    let trial = false;
    let makeup = false;
    let openGym = false;
 
    let r = 3;

    if(currentName.isBlank()){

      break;
    }
 
    for(let c = 2; !comparisonName.isBlank(); c += 3){
 
      for(r = 3; !comparisonName.isBlank(); r++){
 
        comparisonName = checkInDataSheet.getRange(r,c);
        let comparisonNameString = TrimName(comparisonName);
 
        if(currentNameString == comparisonNameString){
 
          let attendanceData = checkInDataSheet.getRange(r,c-1);
 
          if(!attendanceData.isBlank()){
 
            numAttendances += attendanceData.getValue();
            comparisonName.setBackground("#fff2cc");
 
            if(comparisonName.getFontColorObject().asRgbColor().asHexString() == "#ff0000"){
 
              trial = true;
            }
            if(comparisonName.getFontColorObject().asRgbColor().asHexString() == "#0000ff")  {
 
              makeup = true;
            }
            if(comparisonName.getFontColorObject().asRgbColor().asHexString() == "#e69138")  {
 
              openGym = true;
            }
          }        
        }
      }
 
      comparisonName = checkInDataSheet.getRange(r - 2, c);
    }
 
    if(numAttendances != 0){
 
      attendanceDataSheet.getRange(i,attendanceColumn).setValue(numAttendances);
    }else{

      attendanceDataSheet.getRange(i,attendanceColumn).setValue(null);
    }

    if(trial){
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("red");
    }else if(makeup){
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("blue");
    }else if(openGym){
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("#e69138");
    }else{
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("black");
    }
 
  }
}
 
function Search_04(){
 
  let currentName = attendanceDataSheet.getRange(3,1);
 
  for(let i = 300; i < 400; i++){                        //Enumerates through all students' names on attendanceDataSheet sheet
 
    currentName = attendanceDataSheet.getRange(i,1);
    let currentNameString = TrimName(currentName);
    let comparisonName = checkInDataSheet.getRange(3,2);
    let numAttendances = 0;
    let trial = false;
    let makeup = false;
    let openGym = false;
 
    let r = 3;

    if(currentName.isBlank()){

      break;
    }
 
    for(let c = 2; !comparisonName.isBlank(); c += 3){
 
      for(r = 3; !comparisonName.isBlank(); r++){
 
        comparisonName = checkInDataSheet.getRange(r,c);
        let comparisonNameString = TrimName(comparisonName);
 
        if(currentNameString == comparisonNameString){
 
          let attendanceData = checkInDataSheet.getRange(r,c-1);
 
          if(!attendanceData.isBlank()){
 
            numAttendances += attendanceData.getValue();
            comparisonName.setBackground("#fff2cc");
 
            if(comparisonName.getFontColorObject().asRgbColor().asHexString() == "#ff0000"){
 
              trial = true;
            }
            if(comparisonName.getFontColorObject().asRgbColor().asHexString() == "#0000ff")  {
 
              makeup = true;
            }
            if(comparisonName.getFontColorObject().asRgbColor().asHexString() == "#e69138")  {
 
              openGym = true;
            }
          }        
        }
      }
 
      comparisonName = checkInDataSheet.getRange(r - 2, c);
    }
 
    if(numAttendances != 0){
 
      attendanceDataSheet.getRange(i,attendanceColumn).setValue(numAttendances);
    }else{

      attendanceDataSheet.getRange(i,attendanceColumn).setValue(null);
    }
    if(trial && makeup){
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("purple");
    }else if(trial){
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("red");
    }else if(makeup){
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("blue");
    }else if(openGym){
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("#e69138");
    }else{
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("black");
    }
 
  }
}

function Search_05(){
 
  let currentName = attendanceDataSheet.getRange(3,1);
 
  for(let i = 400; i < 500; i++){                        //Enumerates through all students' names on attendanceDataSheet sheet
 
    currentName = attendanceDataSheet.getRange(i,1);
    let currentNameString = TrimName(currentName);
    let comparisonName = checkInDataSheet.getRange(3,2);
    let numAttendances = 0;
    let trial = false;
    let makeup = false;
    let openGym = false;
 
    let r = 3;

    if(currentName.isBlank()){

      break;
    }
 
    for(let c = 2; !comparisonName.isBlank(); c += 3){
 
      for(r = 3; !comparisonName.isBlank(); r++){
 
        comparisonName = checkInDataSheet.getRange(r,c);
        let comparisonNameString = TrimName(comparisonName);
 
        if(currentNameString == comparisonNameString){
 
          let attendanceData = checkInDataSheet.getRange(r,c-1);
 
          if(!attendanceData.isBlank()){
 
            numAttendances += attendanceData.getValue();
            comparisonName.setBackground("#fff2cc");
 
            if(comparisonName.getFontColorObject().asRgbColor().asHexString() == "#ff0000"){
 
              trial = true;
            }
            if(comparisonName.getFontColorObject().asRgbColor().asHexString() == "#0000ff")  {
 
              makeup = true;
            }
            if(comparisonName.getFontColorObject().asRgbColor().asHexString() == "#e69138")  {
 
              openGym = true;
            }
          }        
        }
      }
 
      comparisonName = checkInDataSheet.getRange(r - 2, c);
    }
 
    if(numAttendances != 0){
 
      attendanceDataSheet.getRange(i,attendanceColumn).setValue(numAttendances);
    }else{

      attendanceDataSheet.getRange(i,attendanceColumn).setValue(null);
    }
    if(trial && makeup){
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("purple");
    }else if(trial){
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("red");
    }else if(makeup){
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("blue");
    }else if(openGym){
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("#e69138");
    }else{
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("black");
    }
 
  }
}
 
 function Search_06(){
 
  let currentName = attendanceDataSheet.getRange(3,1);
 
  for(let i = 500; i < 600; i++){                        //Enumerates through all students' names on attendanceDataSheet sheet
 
    currentName = attendanceDataSheet.getRange(i,1);
    let currentNameString = TrimName(currentName);
    let comparisonName = checkInDataSheet.getRange(3,2);
    let numAttendances = 0;
    let trial = false;
    let makeup = false;
    let openGym = false;
 
    let r = 3;

    if(currentName.isBlank()){

      break;
    }
 
    for(let c = 2; !comparisonName.isBlank(); c += 3){
 
      for(r = 3; !comparisonName.isBlank(); r++){
 
        comparisonName = checkInDataSheet.getRange(r,c);
        let comparisonNameString = TrimName(comparisonName);
 
        if(currentNameString == comparisonNameString){
 
          let attendanceData = checkInDataSheet.getRange(r,c-1);
 
          if(!attendanceData.isBlank()){
 
            numAttendances += attendanceData.getValue();
            comparisonName.setBackground("#fff2cc");
 
            if(comparisonName.getFontColorObject().asRgbColor().asHexString() == "#ff0000"){
 
              trial = true;
            }
            if(comparisonName.getFontColorObject().asRgbColor().asHexString() == "#0000ff")  {
 
              makeup = true;
            }
            if(comparisonName.getFontColorObject().asRgbColor().asHexString() == "#e69138")  {
 
              openGym = true;
            }
          }        
        }
      }
 
      comparisonName = checkInDataSheet.getRange(r - 2, c);
    }
 
    if(numAttendances != 0){
 
      attendanceDataSheet.getRange(i,attendanceColumn).setValue(numAttendances);
    }else{

      attendanceDataSheet.getRange(i,attendanceColumn).setValue(null);
    }
    if(trial && makeup){
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("purple");
    }else if(trial){
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("red");
    }else if(makeup){
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("blue");
    }else if(openGym){
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("#e69138");
    }else{
 
      attendanceDataSheet.getRange(i, attendanceColumn).setFontColor("black");
    }
 
  }
}