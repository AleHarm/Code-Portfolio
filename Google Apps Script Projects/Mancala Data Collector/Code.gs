function updateBoard(e) {

  if (e.range.getColumn() != 15 || e.oldValue != undefined) {
    return;
  }

  var sheet = SpreadsheetApp.getActiveSheet();
  var currentRow = e.range.getRow();
  var nextRow = currentRow + 1;

  var needsPerpectiveSwap = sheet.getRange("P" + currentRow).getValue() != sheet.getRange("P" + nextRow).getValue();

  moveRowDown(e, sheet, currentRow, nextRow);
  calculateBoardState(e, sheet, nextRow, currentRow);
  if (needsPerpectiveSwap) {
    Logger.log("Swapping!");
    swapPerspective(e, sheet, nextRow)
  }
}

function moveRowDown(e, sheet, currentRow, nextRow) {

  // Get values from columns A-N in the row above
  var currentRange = sheet.getRange(currentRow, 1, 1, 14); // Columns A to N
  var currentValues = currentRange.getValues();

  // Set those values in the current row for columns A-N
  sheet.getRange(nextRow, 1, 1, 14).setValues(currentValues);
}

function calculateBoardState(e, sheet, newRow, previousRow){

  Logger.log(sheet.getRange("A" + newRow + ":N" + newRow).getValues());

  var moveColumnIndex = sheet.getRange("O" + previousRow).getValue() - 1;
  var row = sheet.getRange("A" + newRow + ":N" + newRow).getValues();
  var numSeeds = row[0][moveColumnIndex]

  var currentIndex = moveColumnIndex;
  row[0][moveColumnIndex] = 0;

  for(var i = 0; i < numSeeds; i++){

    currentIndex = (currentIndex + 1) % 13;
    Logger.log("Current Index: " + currentIndex);

    if(i == numSeeds - 1 
      && currentIndex < 6
      && row[0][currentIndex] == 0 
      && row[0][12 - currentIndex] != 0){

      Logger.log("Doing an empty hole")

      var oppositeHoleValue = row[0][12 - currentIndex];
      row[0][12 - currentIndex] = 0;
      row[0][6] += oppositeHoleValue + 1;
    }else{
      row[0][currentIndex] += 1;
    }
  }
  Logger.log(row);

  sheet.getRange("A" + newRow + ":N" + newRow).setValues(row);
}

function swapPerspective(e, sheet, row) {
  var a2g = sheet.getRange(row, 1, 1, 7).getValues(); // A-G
  var h2n = sheet.getRange(row, 8, 1, 7).getValues(); // H-N

  sheet.getRange(row, 8, 1, 7).setValues(a2g); // Move A-G to H-N
  sheet.getRange(row, 1, 1, 7).setValues(h2n); // Move H-N to A-G
}













































