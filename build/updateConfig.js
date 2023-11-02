const fs = require('fs');
const fileName = 'wails.json';
const file = require("../" + fileName);
    
console.log("changing version to " + process.argv[2] + ", isNightly to " + process.argv[3] + " and environment to prod")

const versionNum = `${process.argv[2]}`.replace("v", "")
file.version = versionNum;
file.info.productVersion = versionNum;
file.isNightly = process.argv[3] === 'true';
file.environment = "prod"

if(process.argv[2] === undefined || process.argv[2] === "" || process.argv[3] === undefined || process.argv[3] === "") {
  throw new Error("version and isNightly must be set")
}
    
fs.writeFile(fileName, JSON.stringify(file), function writeJSON(err) {
  if (err) return console.log(err);
  console.log(JSON.stringify(file, null, 2));
});
