# FFXIV Raid Bot [![Discord Bots](https://discordbots.org/api/widget/status/494597392897933381.png)](https://discordbots.org/bot/494597392897933381) [![Build Status](https://travis-ci.org/Zeuh/FFXIV-Raid-Bot.svg?branch=master)](https://travis-ci.org/Zeuh/FFXIV-Raid-Bot)
This bot is meant to be used to help organize raid teams in discord servers for FFXIV.
It attempts to create a streamlined user experience so that creating and joining raids are easy.

## How does it work?
Once the bot is running, there needs to be a role called 'Raid Leader' (this can be updated to be any role name in the RaidBot.java class).
Any user who should be able to create a raid needs this role.

To create a raid, use the !createRaid command. This will prompt the bot to ask you some questions about your raid, and once you are done, it will create the embedded message
announcing the raid in the channel that you specified.
For people to join the raid, they simply need to click a reaction on the embedded message and then answer the bot on what role they want to play.
After this do this, they can click another specialization reaction to set themselves as a flex role (other roles they can play if necessary).

Raid leaders can also remove people from raids via !removeFromRaid \[raid id\] \[name\]

## How to install the bot
First, the bot requires that Java 8 or higher be installed. Then, the best way to install the bot is to pull this repository
and compile it with maven.

Then, you need to put the resulting jar file where-ever you want to run the bot from.

Next, you need to unzip the icon pack included in this repository and put all of the icons in discord
with the default names discord gives them. Each icon **must** be named after the specialization it represents.

Finally, you need to create a bot via the discord developer application creation page and copy the bot token and put it in a file named 'token'
in the same directory as the jar file.

Finally, you can run the bot and invite it to your discord server and start using it for raids!

Compilation & install on Debian 9 (include systemd service) :
```
# Run this as root user
adduser --system discordbots
cd ~discordbots
mkdir ~discordbots/src
mkdir -p ~discordbots/bots/FFXIV-Raid-Bot
apt install maven openjdk-8-jre openjdk-8-jdk
cd ~discordbots/src
git clone https://github.com/Zeuh/FFXIV-Raid-Bot.git
cd FFXIV-Raid-Bot
mvn package
cp ~discordbots/src/FFXIV-Raid-Bot/target/FFXIV-Raid-Bot-1.0-SNAPSHOT.jar ~discordbots/bots/FFXIV-Raid-Bot/FFXIV-Raid-Bot.jar
cp -r ~discordbots/src/FFXIV-Raid-Bot/Reactions ~discordbots/bots/FFXIV-Raid-Bot/

DISCORDBOT_HOME=$(realpath ~discordbots)
echo "
[Unit]
Description=Discord FFXIV Raid Bot
After=network.target

[Service]
WorkingDirectory=$DISCORDBOT_HOME/bots/FFXIV-Raid-Bot
SyslogIdentifier=discordbots
ExecStart=/bin/sh -c "exec java -jar $DISCORDBOT_HOME/bots/FFXIV-Raid-Bot/FFXIV-Raid-Bot.jar"
User=discordbots
Type=simple

[Install]
WantedBy=multi-user.target" > ~discordbots/bots/FFXIV-Raid-Bot/discord-ffxiv-raid-bot.service

chown -R discordbots.nogroup ~discordbots
ln -s ~discordbots/bots/FFXIV-Raid-Bot/discord-ffxiv-raid-bot.service /etc/systemd/system/discord-ffxiv-raid-bot.service
systemctl enable discord-ffxiv-raid-bot.service
# To start the service run as root : systemctl start discord-ffxiv-raid-bot.service
```
Register your bot on : https://discordapp.com/developers/applications/

After this install, you need to put your bot secret token into : ~discordbots/bots/FFXIV-Raid-Bot/token

The invite link with good permission mask (replace YOUR_BOT_ID_HERE by your bot client ID) :
- https://discordapp.com/oauth2/authorize?client_id=YOUR_BOT_ID_HERE&permissions=490560&scope=bot

## Credits

### FFXIV adaptation
- Yann 'Ze' Richard : code adaptation and features for FFXIV, complete example for compile and install on Debian 9.

### GW2 (original version)
- Christopher Bitler: Bot development for the original GW2 version !
- Tyler "Inverness": Bot idea for GW2 version

## License
GPLv3
