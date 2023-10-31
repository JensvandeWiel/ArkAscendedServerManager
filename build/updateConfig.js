const fs = require('fs');
const fileName = './wails.json';
const file = require(fileName);
    
console.log("changing version to " + process.argv[2] + ", isNightly to " + process.argv[3] + " and environment to prod")

file.version = process.argv[2];
file.isNightly = process.argv[3] === 'true';
file.environment = "prod"
    
fs.writeFile(fileName, JSON.stringify(file), function writeJSON(err) {
  if (err) return console.log(err);
  console.log(JSON.stringify(file));
  console.log('Wrote to ' + fileName + "!");
});