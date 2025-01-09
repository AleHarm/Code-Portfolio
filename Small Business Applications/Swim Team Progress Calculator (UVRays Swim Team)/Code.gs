//columns of each athlete attribute in Main Data
var firstNameCol = "A";
var lastNameCol = "B";
var birthdayCol = "C";
var groupCol = "D";
var sexCol = "E";
var ageGroupCol = "G";

//Points columns in each group page
var groupAttendancePointsCol = "C";
var groupTestSetsPointsCol = "D";
var groupMeetPointsCol = "E";
var groupRacePointsCol = "F";

/* Map to keep track of events compared to columns in Main Data */
var events = new Map([
  ["25 Free", "H"],	
  ["50 Free", "I"],
  ["100 Free", "J"],
  ["200 Free", "K"],
  ["400 Free", "L"],
  ["500 Free", "L"],
  ["800 Free", "M"],
  ["1000 Free", "M"],
  ["1500 Free", "N"],
  ["1650 Free", "N"],
  ["25 Back", "O"],
  ["50 Back", "P"],
  ["100 Back", "Q"],
  ["200 Back", "R"],
  ["25 Breast", "S"],
  ["50 Breast", "T"],
  ["100 Breast", "U"],
  ["200 Breast", "V"],
  ["25 Fly", "W"],
  ["50 Fly", "X"],
  ["100 Fly", "Y"],
  ["200 Fly", "Z"],
  ["100 IM", "AA"],
  ["200 IM", "AB"],
  ["400 IM", "AC"]													
]);

function migrateUVRG() {

  /* contains the sheet information for UVRays Groups sheet */
  var uvrg = SpreadsheetApp.openByUrl("https://docs.google.com/spreadsheets/d/1uhFdZdRQTzgzMj48XYAehys9f6gRxLsNLWYDiBF3peo/edit?gid=0#gid=0");
  var uvrgSheets = uvrg.getSheets();

  /* gets the sheet info for the Move Ups sheet */
  var moveUpSheet = SpreadsheetApp.openByUrl("https://docs.google.com/spreadsheets/d/1A8BuDOOvmFAcd_DWUPU8NBDp4X09Z-BGOs0DYjLfRg0/edit?gid=0#gid=0");
  var mainData = moveUpSheet.getSheetByName("Main Data");
  var moveUpsRow = 2;
  mainData.getRange("H2:AC").clearContent();

  /* iterates over each sheet in UVRays Groups */
  for (var i = 0; i < uvrgSheets.length ; i++ ) {
        var sheet = uvrgSheets[i];
        var group = sheet.getName();
        var uvrgRow = 2;

        /* iterates over each non-blank row in the current sheet */
        while(!sheet.getRange("B" + uvrgRow).isBlank()){
          var firstName = sheet.getRange("C" + uvrgRow).getValue();
          var lastName = sheet.getRange("B" + uvrgRow).getValue();
          var birthday = sheet.getRange("E" + uvrgRow).getValue();
          var sex = sheet.getRange("F" + uvrgRow).getValue();

          /* inserts the name, birthday, and group of each athlete into the Main Data sheet */
          mainData.getRange(firstNameCol + moveUpsRow).setValue(firstName);
          mainData.getRange(lastNameCol + moveUpsRow).setValue(lastName);
          mainData.getRange(birthdayCol + moveUpsRow).setValue(birthday);
          mainData.getRange(groupCol + moveUpsRow).setValue(group);
          mainData.getRange(sexCol + moveUpsRow).setValue(sex);

          /* increments the input and output rows */
          moveUpsRow++;
          uvrgRow++;
        }      
    }

  /* Sorts by last name, then by first name */
  mainData.sort(1).sort(2);

  //moveAthletesToGroups();
}

function moveAthletesToGroups(){

  /* gets the sheet info for the Move Ups sheet */
  var sheet = SpreadsheetApp.openByUrl("https://docs.google.com/spreadsheets/d/1A8BuDOOvmFAcd_DWUPU8NBDp4X09Z-BGOs0DYjLfRg0/edit?gid=0#gid=0");
  var mainData = sheet.getSheetByName("Main Data");
  var mdRow = 2;

  while(mainData.getRange(firstNameCol + mdRow).getValue() != ""){
  
    var infoRange = mainData.getRange(firstNameCol + mdRow + ":" + groupCol + mdRow).getValues();
    var firstName = infoRange[0][0];
    var lastName = infoRange[0][1];
    var group = infoRange[0][3];
    var groupSheet = sheet.getSheetByName(group);

    /* Gets the row beneath the first name in the specific group sheet. 
    The first if statement is there because the getNextDataCell function 
    will get the last row in the sheet if getNextDataCell starts on the last filled in row
    which is the header in the case of the sheet being empty */

    if(groupSheet.getRange("A2").isBlank()){
      var groupSheetRow = 2;
    }else{
      var groupSheetRow = (groupSheet.getRange("A1").getNextDataCell(SpreadsheetApp.Direction.DOWN).getRow()) + 1;
    }

    if(groupSheetRow > groupSheet.getMaxRows()){
      groupSheet.appendRow(["", "", "", "", "","", "=SUM(C" + groupSheetRow + ":F" + groupSheetRow + ")"]);
    }

    groupSheet.getRange("A" + groupSheetRow).setValue(firstName);
    groupSheet.getRange("B" + groupSheetRow).setValue(lastName);

    mdRow++;
  }
}

function migrateAthleteTimes(boy){

  /* gets the sheet info for the Move Ups sheet */
  var sheet = SpreadsheetApp.openByUrl("https://docs.google.com/spreadsheets/d/1A8BuDOOvmFAcd_DWUPU8NBDp4X09Z-BGOs0DYjLfRg0/edit?gid=0#gid=0");
  var mainData = sheet.getSheetByName("Main Data");
  var athleteTimes = sheet.getSheetByName("Top Times By Athletes");


  if(!boy){
    mainData.getRange("H2:AC").clearContent();
    var atRow = 2;
  }else{
    var atRow = findFirstBoy();
    
  }

  var atCol = "A";
  var mdRow = 2;

  /* iterates over each row in the Top Times By Athletes sheet */

  for(atRow; atRow < athleteTimes.getMaxRows(); atRow++){
    
    var checkValue = athleteTimes.getRange(atCol + atRow).getValue();

    if(checkValue != "" && checkValue != "1"){
      var value = athleteTimes.getRange(atCol + atRow).getValue();

      var atNameArray = value.split(/,| /);

      if(atNameArray[1] == ""){
        var firstName = atNameArray[2];
        var lastName = atNameArray[0];
      }else{
        var firstName = atNameArray[3];
        var lastName = atNameArray[0] + " " + atNameArray[1];
      }

      var firstLetter = lastName[0];
      var startSearch = mdRow;
      
      while(mdRow <= mainData.getMaxRows()){

        var nameArray = mainData.getRange(firstNameCol + mdRow + ":" + lastNameCol + mdRow).getValues();
        var mdLastName = nameArray[0][1];

        if(lastName == mdLastName){

          var mdFirstName = nameArray[0][0];

          if(firstName == mdFirstName){


            atRow++;
            while(!athleteTimes.getRange("B" + atRow).isBlank()){
              /* Gets rid of the Y at the end of each athlete time */

              var timeEventArray = athleteTimes.getRange("B" + atRow + ":C" + atRow).getValues();

              /* Deletes the Ys and Ls from times. If any other characters begin to appear, 
              just add a .replace function to the end of the time declaration below */
              var time = timeEventArray[0][1].replace("Y", "").replace("L", "");
              var event = timeEventArray[0][0];

              try{
                mainData.getRange(events.get(event) + mdRow).setValue(time);
              }
              catch(err){
                Logger.log("Not a real race");
              }

              atRow++;

              var tempATRow = skipBlanksInTopTimesByAthletes(athleteTimes, atRow);

              while(tempATRow > atRow){

                atRow = tempATRow;
                skipBlanksInTopTimesByAthletes(athleteTimes, atRow);
              }
            }
            atRow--;
            break;
          }  
        }else if(firstLetter < mdLastName[0]){
          
          /* Only triggers if the name isn't found in the proper letter group 
          (i.e. A search for the name "Anna" would end once the first letter of the comparison name began with a "B") */
          mdRow = startSearch;
          break;
        }
        
        mdRow++;
      }
    }
  }
}

function migrateBoyTimes(){
  migrateAthleteTimes(true);
}

function migrateGirlTimes(){
  migrateAthleteTimes(false);
}

function createHashValue(firstName, lastName){
  var hashValue = firstName.toString().toLowerCase() + lastName.toString().toLowerCase();
  return hashValue;
}

function calculatePoints(){
  var sheet = SpreadsheetApp.openByUrl("https://docs.google.com/spreadsheets/d/1A8BuDOOvmFAcd_DWUPU8NBDp4X09Z-BGOs0DYjLfRg0/edit?gid=0#gid=0");
  var mainData = sheet.getSheetByName("Main Data");

  /* Gets the sheet that has the button that was pressed to activate this script */
  var groupSheet = sheet.getSheetByName(SpreadsheetApp.getActiveSheet().getName());
  //var groupSheet = sheet.getSheetByName("13-14 Champs"); //FOR TESTING PURPOSES


  var gsInfoRange = groupSheet.getRange("A2:O").getValues();
  var mdInfoRange = mainData.getRange(firstNameCol + "2:" + ageGroupCol).getValues();
  var mdHashMap = {};
  for (var currRow = 0; currRow < mdInfoRange.length; currRow++){
    var firstName = mdInfoRange[currRow][0];
    var lastName = mdInfoRange[currRow][1];
    if (firstName != "" && lastName != "") {
      var currHash = createHashValue(firstName, lastName);
      mdHashMap[currHash] = currRow;
    }
  }
  
  for (var currRow = 0; currRow < gsInfoRange.length; currRow++){
    var gsRow = currRow + 2;
    var firstName = gsInfoRange[currRow][0];
    var lastName = gsInfoRange[currRow][1];
    if (firstName == "" || lastName == ""){
      if (firstName == "" && lastName == ""){
        break;
      }
      else {
        groupSheet.getRange(groupAttendancePointsCol + gsRow).setValue("Athlete \"" + firstName  + " " + lastName + "\" not found on Main Data sheet");
      }
    }

    /* Calculates each point column */
    if (1) { // new, fast method
      var currRowPointData = ["", "", "", ""];
      // Calculate score for attendance (Column "C")
      var attendancePercentage = calcAttendancePoints(gsInfoRange[currRow][8]); // Column 8 is "I"
      currRowPointData[0] = attendancePercentage;

      // Calculate score for test sets (Column "D")
      var totalSetPoints = calcTestSetPoints(gsInfoRange[currRow][9], gsInfoRange[currRow][10]); // Column 9/10 are "J"/"K"
      currRowPointData[1] = totalSetPoints;

      // Calculate score for meet points (Column "E")
      var meetSetPoints = calcMeetPoints(gsInfoRange[currRow].slice(11,15));
      currRowPointData[2] = meetSetPoints;

      // Calculate score for races (Column "F")
      var currHash = createHashValue(firstName, lastName);
      var mdHashRow = mdHashMap[currHash];
      if (typeof mdHashRow !== "undefined"){
        var mdRow = mdHashRow + 2;
        var ageGroup = mdInfoRange[mdHashRow][6];
        var sex = mdInfoRange[mdHashRow][4];
        
        var racePoints = calcRacePoints(mainData, ageGroup, sex, mdRow);
        currRowPointData[3] = racePoints;
      }
      else{
        currRowPointData[3] = "Cannot calculate: athlete name not found in 'Main Data' sheet";
      }

      // Write the data back to the spreadsheet
      groupSheet.getRange("C" + gsRow + ':' + "F" + gsRow).setValues([currRowPointData]);
    }
    else { // old, slow method
      calcAttendancePoints_old(groupSheet, gsRow);
      calcTestSetPoints_old(groupSheet, gsRow);
      calcMeetPoints_old(groupSheet, gsRow);

      var currHash = createHashValue(firstName, lastName);
      var mdHashRow = mdHashMap[currHash];
      if (typeof mdHashRow !== "undefined"){
        var mdRow = mdHashRow + 2;
        var ageGroup = mdInfoRange[mdHashRow][6];
        var sex = mdInfoRange[mdHashRow][4];
        calcRacePoints_old(mainData, groupSheet, ageGroup, sex, gsRow, mdRow);
      }
      else{
        groupSheet.getRange(groupRacePointsCol + gsRow).setValue("Cannot calculate: athlete name not found on 'Main Data' sheet");
      }
    }
  }
  return;
}

function calcAttendancePoints_old(groupSheet, gsRow){
  var attendancePercentage = groupSheet.getRange("I" + gsRow).getValue();

    /* Calculate attendance points. Used a switch to increase speed as opposed to a set of if/elses.
    As a result, all percentages must be whole numbers */
  switch(attendancePercentage){

    case 100:
      groupSheet.getRange(groupAttendancePointsCol + gsRow).setValue(100);
      break;
    
    case 99:
    case 98:
    case 97:
    case 96:
    case 95:
      groupSheet.getRange(groupAttendancePointsCol + gsRow).setValue(95);
      break;
    
    case 94:
    case 93:
    case 92:
    case 91:
    case 90:
      groupSheet.getRange(groupAttendancePointsCol + gsRow).setValue(90);
      break;
    
    case 89:
    case 88:
    case 87:
    case 86:
    case 85:
      groupSheet.getRange(groupAttendancePointsCol + gsRow).setValue(85);
      break;

    case 84:
    case 83:
    case 82:
    case 81:
    case 80:
      groupSheet.getRange(groupAttendancePointsCol + gsRow).setValue(80);
      break;

    case 79:
    case 78:
    case 77:
    case 76:
    case 75:
      groupSheet.getRange(groupAttendancePointsCol + gsRow).setValue(75);
      break;
    
    case 74:
    case 73:
    case 72:
    case 71:
    case 70:
      groupSheet.getRange(groupAttendancePointsCol + gsRow).setValue(70);
      break;
    
    case 69:
    case 68:
    case 67:
    case 66:
    case 65:
      groupSheet.getRange(groupAttendancePointsCol + gsRow).setValue(65);
      break;
    
    case 64:
    case 63:
    case 62:
    case 61:
    case 60:
      groupSheet.getRange(groupAttendancePointsCol + gsRow).setValue(60);
      break;
    
    case 59:
    case 58:
    case 57:
    case 56:
    case 55:
      groupSheet.getRange(groupAttendancePointsCol + gsRow).setValue(55);
      break;
    
    case 54:
    case 53:
    case 52:
    case 51:
    case 50:
      groupSheet.getRange(groupAttendancePointsCol + gsRow).setValue(50);
      break;
    
    case 49:
    case 48:
    case 47:
    case 46:
    case 45:
      groupSheet.getRange(groupAttendancePointsCol + gsRow).setValue(40);
      break;
    
    case 44:
    case 43:
    case 42:
    case 41:
    case 40:
      groupSheet.getRange(groupAttendancePointsCol + gsRow).setValue(30);
      break;
    
    case 39:
    case 38:
    case 37:
    case 36:
    case 35:
      groupSheet.getRange(groupAttendancePointsCol + gsRow).setValue(20);
      break;
    
    case 34:
    case 33:
    case 32:
    case 31:
    case 30:
      groupSheet.getRange(groupAttendancePointsCol + gsRow).setValue(15);
      break;
    
    case 29:
    case 28:
    case 27:
    case 26:
    case 25:
      groupSheet.getRange(groupAttendancePointsCol + gsRow).setValue(10);
      break;
    
    case 24:
    case 23:
    case 22:
    case 21:
    case 20:
      groupSheet.getRange(groupAttendancePointsCol + gsRow).setValue(5);
      break;
    
    case 19:
    case 18:
    case 17:
    case 16:
    case 15:
    case 14:
    case 13:
    case 12:
    case 11:
    case 10:
    case 9:
    case 8:
    case 7:
    case 6:
    case 5:
    case 4:
    case 3:
    case 2:
    case 1:
    case 0:
      groupSheet.getRange(groupAttendancePointsCol + gsRow).setValue(0);
      break;
    
    case(""):
      groupSheet.getRange(groupAttendancePointsCol + gsRow).setValue("0");
      break;

    default:
      throw new Error("Attendance value must be a whole number. Fix " + groupSheet.getRange("A" + gsRow).getValue() + " " + groupSheet.getRange("B" + gsRow).getValue() + "'s attendance value");
  }
}
function calcAttendancePoints(attendancePercentage){
  //var attendancePercentage = groupSheet.getRange("I" + gsRow).getValue();

  if (attendancePercentage >= 50){
    return Math.floor(attendancePercentage / 5) * 5;
  } else if (attendancePercentage >= 45) {
    return 40;
  } else if (attendancePercentage >= 40) {
    return 30;
  } else if (attendancePercentage >= 35) {
    return 20;
  } else if (attendancePercentage >= 30) {
    return 15;
  } else if (attendancePercentage >= 25) {
    return 10;
  } else if (attendancePercentage >= 20) {
    return 5;
  } else {
    return 0;
  }
}

function calcTestSetPoints_old(groupSheet, gsRow){

  var set1Interval = parseTimeToSeconds(groupSheet.getRange("J" + gsRow).getValue());
  var set2Interval = parseTimeToSeconds(groupSheet.getRange("K" + gsRow).getValue());
  var set1Points = 0;
  var set2Points = 0;
  var totalSetPoints = 0;

  /* Calculate test set 1 points */
  if(set1Interval >= 156 || set1Interval == ""){
    set1Points = 0;
  }else if(set1Interval >= 151){
    set1Points = 5;
  }else if(set1Interval >= 146){
    set1Points = 10;
  }else if(set1Interval >= 141){
    set1Points = 15;
  }else if(set1Interval >= 136){
    set1Points = 20;
  }else if(set1Interval >= 131){
    set1Points = 25;
  }else if(set1Interval >= 126){
    set1Points = 30;
  }else if(set1Interval >= 121){
    set1Points = 35;
  }else if(set1Interval >= 116){
    set1Points = 40;
  }else if(set1Interval >= 111){
    set1Points = 45;
  }else if(set1Interval >= 106){
    set1Points = 50;
  }else if(set1Interval >= 101){
    set1Points = 55;
  }else if(set1Interval >= 96){
    set1Points = 60;
  }else if(set1Interval >= 91){
    set1Points = 65;
  }else if(set1Interval >= 86){
    set1Points = 70;
  }else if(set1Interval >= 81){
    set1Points = 75;
  }else if(set1Interval >= 76){
    set1Points = 80;
  }else if(set1Interval >= 71){
    set1Points = 85;
  }else if(set1Interval >= 66){
    set1Points = 90;
  }else if(set1Interval >= 61){
    set1Points = 95;
  }else if(set1Interval < 60){
    set1Points = 100;
  }

  /* Calculate test set 2 points */
  if(set2Interval >= 131 || set2Interval == ""){
    set2Points = 0;
  }else if(set2Interval >= 126){
    set2Points = 5;
  }else if(set2Interval >= 121){
    set2Points = 10;
  }else if(set2Interval >= 116){
    set2Points = 15;
  }else if(set2Interval >= 111){
    set2Points = 20;
  }else if(set2Interval >= 106){
    set2Points = 25;
  }else if(set2Interval >= 101){
    set2Points = 30;
  }else if(set2Interval >= 96){
    set2Points = 35;
  }else if(set2Interval >= 91){
    set2Points = 40;
  }else if(set2Interval >= 86){
    set2Points = 45;
  }else if(set2Interval >= 81){
    set2Points = 50;
  }else if(set2Interval >= 76){
    set2Points = 55;
  }else if(set2Interval >= 71){
    set2Points = 60;
  }else if(set2Interval >= 66){
    set2Points = 65;
  }else if(set2Interval >= 61){
    set2Points = 70;
  }else if(set2Interval >= 56){
    set2Points = 75;
  }else if(set2Interval >= 51){
    set2Points = 80;
  }else if(set2Interval >= 46){
    set2Points = 85;
  }else if(set2Interval >= 41){
    set2Points = 90;
  }else if(set2Interval >= 36){
    set2Points = 95;
  }else if(set2Interval < 35){
    set2Points = 100;
  }

  /* Input test set points */
  totalSetPoints = set1Points + set2Points;
  groupSheet.getRange(groupTestSetsPointsCol + gsRow).setValue(totalSetPoints);
}
function calcTestSetPoints(set1_input, set2_input){

  // var set1Interval = parseTimeToSeconds(groupSheet.getRange("J" + gsRow).getValue());
  // var set2Interval = parseTimeToSeconds(groupSheet.getRange("K" + gsRow).getValue());
  var set1Interval = parseTimeToSeconds(set1_input);
  var set2Interval = parseTimeToSeconds(set2_input);
  var set1Points = 0;
  var set2Points = 0;
  var totalSetPoints = 0;

  /* Calculate test set 1 points */
  if(set1Interval >= 156 || set1Interval == ""){
    set1Points = 0;
  }else if(set1Interval >= 151){
    set1Points = 5;
  }else if(set1Interval >= 146){
    set1Points = 10;
  }else if(set1Interval >= 141){
    set1Points = 15;
  }else if(set1Interval >= 136){
    set1Points = 20;
  }else if(set1Interval >= 131){
    set1Points = 25;
  }else if(set1Interval >= 126){
    set1Points = 30;
  }else if(set1Interval >= 121){
    set1Points = 35;
  }else if(set1Interval >= 116){
    set1Points = 40;
  }else if(set1Interval >= 111){
    set1Points = 45;
  }else if(set1Interval >= 106){
    set1Points = 50;
  }else if(set1Interval >= 101){
    set1Points = 55;
  }else if(set1Interval >= 96){
    set1Points = 60;
  }else if(set1Interval >= 91){
    set1Points = 65;
  }else if(set1Interval >= 86){
    set1Points = 70;
  }else if(set1Interval >= 81){
    set1Points = 75;
  }else if(set1Interval >= 76){
    set1Points = 80;
  }else if(set1Interval >= 71){
    set1Points = 85;
  }else if(set1Interval >= 66){
    set1Points = 90;
  }else if(set1Interval >= 61){
    set1Points = 95;
  }else if(set1Interval < 60){
    set1Points = 100;
  }

  /* Calculate test set 2 points */
  if(set2Interval >= 131 || set2Interval == ""){
    set2Points = 0;
  }else if(set2Interval >= 126){
    set2Points = 5;
  }else if(set2Interval >= 121){
    set2Points = 10;
  }else if(set2Interval >= 116){
    set2Points = 15;
  }else if(set2Interval >= 111){
    set2Points = 20;
  }else if(set2Interval >= 106){
    set2Points = 25;
  }else if(set2Interval >= 101){
    set2Points = 30;
  }else if(set2Interval >= 96){
    set2Points = 35;
  }else if(set2Interval >= 91){
    set2Points = 40;
  }else if(set2Interval >= 86){
    set2Points = 45;
  }else if(set2Interval >= 81){
    set2Points = 50;
  }else if(set2Interval >= 76){
    set2Points = 55;
  }else if(set2Interval >= 71){
    set2Points = 60;
  }else if(set2Interval >= 66){
    set2Points = 65;
  }else if(set2Interval >= 61){
    set2Points = 70;
  }else if(set2Interval >= 56){
    set2Points = 75;
  }else if(set2Interval >= 51){
    set2Points = 80;
  }else if(set2Interval >= 46){
    set2Points = 85;
  }else if(set2Interval >= 41){
    set2Points = 90;
  }else if(set2Interval >= 36){
    set2Points = 95;
  }else if(set2Interval < 35){
    set2Points = 100;
  }

  /* Input test set points */
  totalSetPoints = set1Points + set2Points;
  return totalSetPoints
  // groupSheet.getRange(groupTestSetsPointsCol + gsRow).setValue(totalSetPoints);
}

function calcMeetPoints_old(groupSheet, gsRow){

  var infoRange = groupSheet.getRange("L" + gsRow + ":O" + gsRow).getValues();

  if(infoRange[0][3]){
    groupSheet.getRange(groupMeetPointsCol + gsRow).setValue(35);
  }else if(infoRange[0][2]){
    groupSheet.getRange(groupMeetPointsCol + gsRow).setValue(30);
  }else if(infoRange[0][1]){
    groupSheet.getRange(groupMeetPointsCol + gsRow).setValue(25);
  }else if(infoRange[0][0]){
    groupSheet.getRange(groupMeetPointsCol + gsRow).setValue(10);
  }else{
    groupSheet.getRange(groupMeetPointsCol + gsRow).setValue(0);
  }
}
function calcMeetPoints(meet_data){

  if(meet_data[3]){
    return 35;
  }else if(meet_data[2]){
    return 30;
  }else if(meet_data[1]){
    return 25;
  }else if(meet_data[0]){
    return 10;
  }else{
    return 0;
  }
}

function calcRacePoints_old(mainData, groupSheet, ageGroup, sex, gsRow, mdRow){

  var today = Utilities.formatDate(new Date(), "GMT+1", "MM/dd")

  /* Throws "error" if no sex or ageGroup is listed for the athlete, as these are necessary for time point calculation */
  if(sex == ""){

    groupSheet.getRange(groupRacePointsCol + gsRow).setValue("Cannot calculate: althete sex not found on 'Main Data' sheet");
  }else if(ageGroup == ""){
    
        groupSheet.getRange(groupRacePointsCol + gsRow).setValue("Cannot calculate: athlete 'Age Group' not found on 'Main Data' sheet");
  }else{

    /* These dates determine when races times switch from yards to meters.
        Each spreadsheet with motivational times must be named with the convention
        ageGroup, then " Motivational Times ", followed by a Y or M, depending on if the times are in yards or meters*/
    if(today > "04/01" && today < "09/01"){ //CHANGE DATES LATER
      var timeSheet = SpreadsheetApp.openByUrl("https://docs.google.com/spreadsheets/d/1A8BuDOOvmFAcd_DWUPU8NBDp4X09Z-BGOs0DYjLfRg0/edit?gid=0#gid=0").getSheetByName(ageGroup + " Motivational Times M");
    }else{
      var timeSheet = SpreadsheetApp.openByUrl("https://docs.google.com/spreadsheets/d/1A8BuDOOvmFAcd_DWUPU8NBDp4X09Z-BGOs0DYjLfRg0/edit?gid=0#gid=0").getSheetByName(ageGroup + " Motivational Times Y");
    }

    var totalPoints = 0;
    var tsRow = 3;
    var motivationalEvents = timeSheet.getRange("F" + tsRow + ":" + "F" + timeSheet.getRange("F" + tsRow).getNextDataCell(SpreadsheetApp.Direction.DOWN).getRow()).getValues();


    for(var tsRow; (tsRow - 3) < motivationalEvents.length; tsRow++){

      var mdCol = events.get(motivationalEvents[tsRow - 3][0].replace(" Y", "").replace(" M", ""));
      var athleteTimeRAW = mainData.getRange(mdCol + mdRow).getValue();

      if(athleteTimeRAW == ""){

        continue;
      }

      var athleteTime = parseTimeToSeconds(athleteTimeRAW);

      if(sex == "M"){

        var timeCategories = timeSheet.getRange("G" + tsRow + ":" + "K" + tsRow).getValues();
        timeCategories = timeCategories.map(row=>row.reverse()).reverse();
      }else{

        var timeCategories = timeSheet.getRange("A" + tsRow + ":" + "E" + tsRow).getValues();
      }

    if(athleteTime < parseTimeToSeconds(timeCategories[0][4])){

        totalPoints += 6;
      }else if(athleteTime < parseTimeToSeconds(timeCategories[0][3])){

        totalPoints += 5;
      }else if(athleteTime < parseTimeToSeconds(timeCategories[0][2])){

        totalPoints += 4;
      }else if(athleteTime < parseTimeToSeconds(timeCategories[0][1])){

        totalPoints += 3;
      }else if(athleteTime < parseTimeToSeconds(timeCategories[0][0])){

        totalPoints += 2;
      }else{

        totalPoints += 1;
      }
    }

    groupSheet.getRange(groupRacePointsCol + gsRow).setValue(totalPoints);
  }
}
function calcRacePoints(mainData, ageGroup, sex, mdRow){

  var today = Utilities.formatDate(new Date(), "GMT+1", "MM/dd")

  /* Throws "error" if no sex or ageGroup is listed for the athlete, as these are necessary for time point calculation */
  if(sex == ""){
    return "Cannot calculate: althete sex not found on 'Main Data' sheet";
  }else if(ageGroup == ""){
        return "Cannot calculate: athlete 'Age Group' not found on 'Main Data' sheet";
  }else{

    /* These dates determine when races times switch from yards to meters.
        Each spreadsheet with motivational times must be named with the convention
        ageGroup, then " Motivational Times ", followed by a Y or M, depending on if the times are in yards or meters*/
    if(today > "04/01" && today < "09/01"){ //CHANGE DATES LATER
      var timeSheet = SpreadsheetApp.openByUrl("https://docs.google.com/spreadsheets/d/1A8BuDOOvmFAcd_DWUPU8NBDp4X09Z-BGOs0DYjLfRg0/edit?gid=0#gid=0").getSheetByName(ageGroup + " Motivational Times M");
    }else{
      var timeSheet = SpreadsheetApp.openByUrl("https://docs.google.com/spreadsheets/d/1A8BuDOOvmFAcd_DWUPU8NBDp4X09Z-BGOs0DYjLfRg0/edit?gid=0#gid=0").getSheetByName(ageGroup + " Motivational Times Y");
    }

    var totalPoints = 0;
    var tsRow = 3;
    var motivationalEvents = timeSheet.getRange("F" + tsRow + ":" + "F" + timeSheet.getRange("F" + tsRow).getNextDataCell(SpreadsheetApp.Direction.DOWN).getRow()).getValues();


    for(var tsRow; (tsRow - 3) < motivationalEvents.length; tsRow++){

      var mdCol = events.get(motivationalEvents[tsRow - 3][0].replace(" Y", "").replace(" M", ""));
      var athleteTimeRAW = mainData.getRange(mdCol + mdRow).getValue();

      if(athleteTimeRAW == ""){

        continue;
      }

      var athleteTime = parseTimeToSeconds(athleteTimeRAW);

      if(sex == "M"){

        var timeCategories = timeSheet.getRange("G" + tsRow + ":" + "K" + tsRow).getValues();
        timeCategories = timeCategories.map(row=>row.reverse()).reverse();
      }else{

        var timeCategories = timeSheet.getRange("A" + tsRow + ":" + "E" + tsRow).getValues();
      }

    if(athleteTime < parseTimeToSeconds(timeCategories[0][4])){

        totalPoints += 6;
      }else if(athleteTime < parseTimeToSeconds(timeCategories[0][3])){

        totalPoints += 5;
      }else if(athleteTime < parseTimeToSeconds(timeCategories[0][2])){

        totalPoints += 4;
      }else if(athleteTime < parseTimeToSeconds(timeCategories[0][1])){

        totalPoints += 3;
      }else if(athleteTime < parseTimeToSeconds(timeCategories[0][0])){

        totalPoints += 2;
      }else{

        totalPoints += 1;
      }
    }

    //groupSheet.getRange(groupRacePointsCol + gsRow).setValue(totalPoints);
    return totalPoints
  }
}



//
//
/* Utility Functions */
//
//

function clearGroupSheet(){
  var sheet = SpreadsheetApp.getActiveSheet();
  sheet.getRange("A2:F").clearContent();
}

/* Converts strings that have the format 00:00.00 to numerical values with the format 0.00 */
function parseTimeToSeconds(timeRAW){
  
  if(timeRAW == ""){

    return "";
  }

  if(/:/.test(timeRAW)){

    if(timeRAW[1] == ":"){

      var firstDigits = parseFloat(timeRAW[0]);
    }else{
      var firstDigits = parseFloat(timeRAW[0] + timeRAW[1]);
    }
    try {
      timeRAW = parseFloat(timeRAW.replace(firstDigits.toString(), "").replace(":", ""));
      var parsedTime = parseFloat(((firstDigits * 60) + timeRAW).toFixed(2));
    }
    catch (error) {
      throw new Error( "Times must be in the format 00:00.00" );
    }

  }else if(/./.test(timeRAW) || typeof(timeRAW) == number){

    var parsedTime = timeRAW;
  }

  return parsedTime;
}

/* finds the row of the first name that has "Boy" after it in the Top Times By Athletes sheet */
function findFirstBoy(){
  var sheet = SpreadsheetApp.openByUrl("https://docs.google.com/spreadsheets/d/1A8BuDOOvmFAcd_DWUPU8NBDp4X09Z-BGOs0DYjLfRg0/edit?gid=0#gid=0");
  var athleteTimes = sheet.getSheetByName("Top Times By Athletes");
  var atCol = "A";
  var atRow = 2;

  for(atRow = 2; atRow < athleteTimes.getMaxRows(); atRow++){
    var checkValue = athleteTimes.getRange(atCol + atRow).getValue();

    if(checkValue != "" && checkValue != "1"){
      var value = athleteTimes.getRange(atCol + atRow).getValue();
      var nameArray = value.split(/,| /);
      if(nameArray[3] == "(Boy"){
        
        return atRow;
      }
    }
  }
}

function skipBlanksInTopTimesByAthletes(athleteTimes, atRow){

  if(athleteTimes.getRange("B" + atRow).isBlank()){
    if(athleteTimes.getRange("A" + atRow).isBlank()){
      atRow++;
    }
  }

  return atRow;
}

































