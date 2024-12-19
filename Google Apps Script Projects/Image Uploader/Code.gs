
var UPLOADS_FOLDER_ID = "-"; // Replace with proper ID of uploads folder
var CACHE_KEY_PREFIX = 'userFolderCache-';

function doGet() {
  return HtmlService.createHtmlOutputFromFile('index')
      .setTitle('Image Uploader')
      .setXFrameOptionsMode(HtmlService.XFrameOptionsMode.ALLOWALL);
}

function uploadFile(base64Data, fileName) {
  try {

    var userEmail = Session.getActiveUser().getEmail();
    
    if (!userEmail || !userEmail.includes('@')) {
      throw new Error("Invalid user email: " + userEmail);
    }

    var userName = getUserName(userEmail);

    var uploadsFolder = DriveApp.getFolderById(UPLOADS_FOLDER_ID);
    if (!uploadsFolder) {
      throw new Error("Unable to locate uploads folder. Please verify the folder ID.");
    }

    var cache = CacheService.getScriptCache();
    var userFolderCacheKey = CACHE_KEY_PREFIX + userName;
    var userFolder = cache.get(userFolderCacheKey);

    if (!userFolder) {
      userFolder = createOrGetFolder(userName, uploadsFolder);
      cache.put(userFolderCacheKey, userFolder.getId(), 60); // Cache the folder ID for 1 minute
    } else {
      userFolder = DriveApp.getFolderById(userFolder);
    }

    var today = new Date();
    var dateFolderName = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate();
    var dateFolder = createOrGetFolder(dateFolderName, userFolder);

    var mimeType = getMimeType(fileName);
    var blob = Utilities.newBlob(Utilities.base64Decode(base64Data), mimeType, fileName);
    var file = dateFolder.createFile(blob);

  } catch (error) {
    Logger.log(error);
  }
}

function getMimeType(fileName) {
  var extension = fileName.split('.').pop().toLowerCase();
  switch (extension) {
    case 'jpg':
    case 'jpeg':
      return MimeType.JPEG;
    case 'png':
      return MimeType.PNG;
    case 'gif':
      return MimeType.GIF;
    case 'mp4':
      return 'video/mp4';
    case 'mov':
      return 'video/quicktime';
    case 'avi':
      return 'video/x-msvideo';
    case 'mkv':
      return 'video/x-matroska';
    default:
      return MimeType.BINARY;
  }
}

function createOrGetFolder(folderName, parentFolder) {
  if (!parentFolder) {
    Logger.log("Parent folder is undefined or invalid.");
    throw new Error("Parent folder is undefined. Please check the folder ID and permissions.");
  }

  if (!folderName || folderName.trim() === "") {
    Logger.log("Folder name is undefined or invalid: " + folderName);
    throw new Error("Folder name is undefined or invalid.");
  }

  Logger.log("Attempting to access or create folder: " + folderName);

  try {
    var folders = parentFolder.getFoldersByName(folderName);
    if (folders.hasNext()) {
      var folder = folders.next();
      Logger.log("Folder found: " + folder.getName());
      return folder;
    } else {
      Logger.log("Folder not found. Creating new folder: " + folderName);
      var newFolder = parentFolder.createFolder(folderName);
      Logger.log("Folder created successfully: " + newFolder.getName());
      return newFolder;
    }
  } catch (error) {
    Logger.log("Error in createOrGetFolder: " + error);
    throw new Error("Error while accessing or creating the folder: " + error.message);
  }
}

function getUserName(userEmail) {
  try {
    var peopleService = People.People.get('people/me', {
      personFields: 'names',
    });
    
    if (peopleService.names && peopleService.names.length > 0) {
      var name = peopleService.names[0];
      return name.givenName + ' ' + name.familyName;
    } else {
      return userEmail; // Fallback to email if name is not available
    }
  } catch (e) {
    Logger.log("Error fetching user name: " + e.message);
    return userEmail; // Fallback to email in case of error
  }
}