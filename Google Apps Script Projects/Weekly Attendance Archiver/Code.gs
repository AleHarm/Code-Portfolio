function main() {
  const spreadsheet = SpreadsheetApp.openByUrl("-");

  const responsesSheet = spreadsheet.getSheetByName("Responses");


  // Check if there are more than 1 rows to avoid errors
  if (responsesSheet.getMaxRows() > 2) {
    copyAndRenameSheet(spreadsheet, responsesSheet);
    resetMainSheet(spreadsheet, responsesSheet);
  }else{
    Logger.log("No reponses!");
  }
}

function copyAndRenameSheet(spreadsheet, responsesSheet) {
  
  const copiedSheet = responsesSheet.copyTo(spreadsheet);

  const formattedDate = Utilities.formatDate(new Date(), Session.getScriptTimeZone(), "MM/dd/yy");
  const newTitle = "SS " + formattedDate;
  
  copiedSheet.setName(newTitle);

  const lastRow = copiedSheet.getLastRow();
  copiedSheet.deleteRow(lastRow);
}


function resetMainSheet(spreadsheet, responsesSheet){
  
  // Get the total number of rows in the "Responses" sheet
  const lastRow = responsesSheet.getLastRow();

  // Check if there are more than 1 rows to avoid errors
  if (lastRow > 2) {
    // Delete all rows except the first and last
    responsesSheet.deleteRows(2, lastRow - 2);
  }
}
