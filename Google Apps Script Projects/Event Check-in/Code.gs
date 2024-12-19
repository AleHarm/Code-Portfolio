// Function to display the web form
function doGet() {
  return HtmlService.createHtmlOutputFromFile('Index')
      .setSandboxMode(HtmlService.SandboxMode.IFRAME);
}

// Function to handle form submission
function submitData(firstName, lastName, branch, plusOne) {
  var sheet = SpreadsheetApp.openByUrl('https://docs.google.com/spreadsheets/d/1QgAZbV5vhLevzEpdGFLU3eszSc8o5zfPUgS9DDJpNyk/edit?gid=663376538#gid=663376538');
  var bannedSheet = sheet.getSheetByName('Banned People');
  var bannedNames = bannedSheet.getRange('A2:B').getValues(); // Get all first and last names as a 2D array

  var ws = sheet.getSheetByName('Checked In');
  ws.insertRowBefore(2);
  ws.getRange(2, 1).setValue(firstName);
  ws.getRange(2, 2).setValue(lastName);
  ws.getRange(2, 3).setValue(branch);
  ws.getRange(2, 4).setValue(plusOne ? "TRUE" : "FALSE");
  ws.getRange(2, 5).setValue("=IF(ISNA(VLOOKUP(CONCATENATE(A2, \" \", B2), 'Banned People'!A2:B, 1, FALSE)), FALSE, TRUE)");

  // Check if the full name exists in the Banned People sheet
  var fullName = firstName + ' ' + lastName;
  for (var i = 0; i < bannedNames.length; i++) {
    if (bannedNames[i][0] === fullName) {
      throw new Error('User is banned');
    }
  }

  return 'Success';
}
