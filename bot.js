const Discord = require('discord.js');
const client = new Discord.Client();

client.on('ready', () => {
    console.log('I am ready!');
});

client.on('message', message => {
    if (message.content === 'ping') {
    	message.reply('Its collo time, @243509481231613963>!');
  	}
});

if(input === "!collotime")
{
    bot.sendMessage(message, "Its collo time! Wakey, wakey <@243509481231613963>! (๑˃̵　ᴗ　˂̵)و")
}

// THIS  MUST  BE  THIS  WAY
client.login(process.env.BOT_TOKEN);
