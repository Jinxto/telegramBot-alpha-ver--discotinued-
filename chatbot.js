const cleverbot = require('cleverbot-free');
var yes = process.argv.slice(2);
var text = ""
for(i = 0; i<yes.length;i++){
text += yes[i]+" ";
}
cleverbot(text).then((cleverbot) => {
 var yes = console.log(cleverbot);
})


