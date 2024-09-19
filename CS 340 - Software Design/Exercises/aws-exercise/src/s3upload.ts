import fs from "fs";
import path from "path";

import {
  S3Client,
  PutObjectCommand,
} from "@aws-sdk/client-s3";

s3upload();

async function s3upload() {
  if(process.argv.length !== 3) {
    console.log("Specify the file to upload on the command line");
    process.exit();
  }

  try {
    const client = new S3Client();
    const fileContent = fs.readFileSync(process.argv[2]);

    const params = {
      "Body": fileContent,
      "Bucket": "testbucketah962",
      "Key": path.basename(process.argv[2])
    }

    const command =  new PutObjectCommand(params);
    const response = await client.send(command);

    console.log("File upload successful with ", response.$metadata.httpStatusCode);
  } catch (error) {
    console.log(error);
  }
}
