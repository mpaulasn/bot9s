const Discord = require('discord.js');

const client = new Discord.Client();

 

client.on('ready', () => {

    console.log('Ready to deploy!');

});

 

client.on('message', message => {

    if (message.content === 'ping') {

       message.reply('pong');

       }

});

 



client.login(process.env.Yyma19mKqaec57RRIkxk-kuJNoQOHhyW);
