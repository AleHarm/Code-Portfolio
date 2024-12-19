function main() {
  const spreadsheet = SpreadsheetApp.openByUrl("https://docs.google.com/spreadsheets/d/1QgAZbV5vhLevzEpdGFLU3eszSc8o5zfPUgS9DDJpNyk/edit?gid=663376538#gid=663376538");

  const sheetControls = spreadsheet.getSheetByName("Sheet Controls");
  const newTitle = sheetControls.getRange("A2").getValue();
  if(!newTitle){
    SpreadsheetApp.getActiveSpreadsheet().toast("Please enter a valid sheet name", "Error", 5);
    return;
  }

  copyAndRenameSheet(spreadsheet);
  resetMainSheet(spreadsheet);
  resetControlsSheet(spreadsheet);
}

function copyAndRenameSheet(spreadsheet) {
  // Get the "Checked In" sheet
  const checkedInSheet = spreadsheet.getSheetByName("Checked In");
  
  const copiedSheet = checkedInSheet.copyTo(spreadsheet);
  
  const sheetControls = spreadsheet.getSheetByName("Sheet Controls");
  const newTitle = sheetControls.getRange("A2").getValue();
  
  copiedSheet.setName(newTitle);
  
  const lastRow = copiedSheet.getLastRow();
  copiedSheet.deleteRow(lastRow);
}


function resetMainSheet(spreadsheet){

  // Get the "Checked In" sheet
  const checkedInSheet = spreadsheet.getSheetByName("Checked In");
  
  // Get the total number of rows in the "Checked In" sheet
  const lastRow = checkedInSheet.getLastRow();

  // Check if there are more than 2 rows to avoid errors
  if (lastRow > 2) {
    // Delete all rows except the first and last
    checkedInSheet.deleteRows(2, lastRow - 2);
  }
}

function resetControlsSheet(spreadsheet){

  // Get the "Sheet Controls" sheet
  const sheetControls = spreadsheet.getSheetByName("Sheet Controls");
  
  // Clear the value in cell A2
  sheetControls.getRange("A2").clearContent();
}
