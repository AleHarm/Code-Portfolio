function onEdit(e){

  /* Looks at the checkboxes column and checks to see that it's changed from false to true */
  if (e.range.getColumn() == 3 && e.oldValue == "false") {
    
    /* gets the sheet and row values */
    var sheet = e.source.getActiveSheet();
    var row = e.range.getRow();

    /* get the proper cell based on row and sheet values */
    let cell = sheet.getRange("D" + String(row));
    
    /* creates date value and updates date cell to reflect today's date */
    let now = new Date();
    cell.setValue(now);

    /* if the sheet updated is Things to Buy */
    if(sheet.getName() == "Things to Buy"){

      /* gets item name and sets the sheet to "On the Way" */
      var itemName = sheet.getRange("A" + String(row)).getValue();
      sheet = SpreadsheetApp.getActiveSpreadsheet().getSheetByName("On the Way");

      /* adds new row to the top of the sheet and puts the item name into the cell */
      sheet.insertRowBefore(3);
      cell = sheet.getRange("A3");
      cell.setValue(itemName);
    }
  }

  if (e.range.getColumn() == 3 && e.oldValue == "false") {
    
    /* Sets "Waiting" and "Do today" checkboxes to false, exluding "On the Way" sheet */
    if(e.source.getActiveSheet().getName() != "Things to Buy"){
      Logger.log("Ran because of " + e.source.getActiveSheet().getName());
      sheet.getRange("E" + String(e.range.getRow())).setValue("false");
      sheet.getRange("F" + String(e.range.getRow())).setValue("false");
    } 
    /* Moves completed items below incomplete items */
    var sheet = e.source.getActiveSheet();
    sheet.sort(4, false).sort(3);
  }
    /* adds an empty row to the top of the spreadsheet whenever an entry is added */
  /* EXCLUDES "On the Way" SHEET BECAUSE "On the Way" IS AUTOPOPULATED */
  if(e.range.getColumn() == 1 && e.range.getRow() == 3 && e.source.getActiveSheet().getName() != "On the Way"){
    
    /* gets the sheet and enters a row at the top */
    var sheet = e.source.getActiveSheet();
    sheet.insertRowBefore(3);
  }
}
