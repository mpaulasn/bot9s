package me.cbitler.raidbot.handlers;

import me.cbitler.raidbot.RaidBot;
import me.cbitler.raidbot.commands.Command;
import me.cbitler.raidbot.commands.CommandRegistry;
import me.cbitler.raidbot.creation.CreationStep;
import me.cbitler.raidbot.creation.RunNameStep;
import me.cbitler.raidbot.logs.LogParser;
import me.cbitler.raidbot.raids.Raid;
import me.cbitler.raidbot.raids.PendingRaid;
import me.cbitler.raidbot.raids.RaidManager;
import me.cbitler.raidbot.utility.PermissionsUtil;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Handle channel message-related events sent to the bot
 * @author Christopher Bitler
 */
public class ChannelMessageHandler extends ListenerAdapter {

    /**
     * Handle receiving a message. This checks to see if it matches the !createRaid or !removeFromRaid commands
     * and acts on them accordingly.
     *
     * @param e The event
     */
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        RaidBot bot = RaidBot.getInstance();
        if (e.getAuthor().isBot()) {
           return;
        }

        if(e.getMessage().getRawContent().startsWith("!")) {
            String[] messageParts = e.getMessage().getRawContent().split(" ");
            String[] arguments = CommandRegistry.getArguments(messageParts);
            Command command = CommandRegistry.getCommand(messageParts[0].replace("!",""));
            if(command != null) {
                command.handleCommand(messageParts[0], arguments, e.getChannel(), e.getAuthor());

                try {
                    e.getMessage().delete().queue();
                } catch (Exception exception) {}
            }
        }

        if (PermissionsUtil.hasRaidLeaderRole(e.getMember())) {
            if (e.getMessage().getRawContent().equalsIgnoreCase("!createRaid")) {
                // Verify there is no pending
                PendingRaid raid = bot.getPendingRaids().get(e.getAuthor().getId());
                if (raid == null) {
                    CreationStep runNameStep = new RunNameStep(e.getMessage().getGuild().getId());
                    e.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(runNameStep.getStepText()).queue());
                    bot.getCreationMap().put(e.getAuthor().getId(), runNameStep);
                    e.getMessage().delete().queue();
                } else {
                    e.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Impossible de créer un raid, tu en as un en cours de création...\n\n Si tu t'en souviens plus : Tapes 'cancel' !").queue());
                }
            } else if (e.getMessage().getRawContent().toLowerCase().startsWith("!removefromraid")) {
                String[] split = e.getMessage().getRawContent().split(" ");
                if(split.length < 3) {
                    e.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Format for !removeFromRaid: !removeFromRaid [raid id] [name]").queue());
                } else {
                    String messageId = split[1];
                    String name = split[2];

                    Raid raid = RaidManager.getRaid(messageId);

                    if (raid != null && raid.getServerId().equalsIgnoreCase(e.getGuild().getId())) {
                        if(raid.getUserByName(name) != null) {
                            raid.removeUserByName(name);
                        }
                    } else {
                        e.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Non-existant raid").queue());
                    }
                }
                try {
                    e.getMessage().delete().queue();
                } catch (Exception exception) {}
            }
        }

        if (e.getMember().getPermissions().contains(Permission.MANAGE_SERVER)) {
            if(e.getMessage().getRawContent().toLowerCase().startsWith("!setraidleaderrole")) {
                String[] commandParts = e.getMessage().getRawContent().split(" ");
                String raidLeaderRole = combineArguments(commandParts,1);
                RaidBot.getInstance().setRaidLeaderRole(e.getMember().getGuild().getId(), raidLeaderRole);
                e.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("OK, maintenant si t'es pas **" + raidLeaderRole +"** avant 50 ans t'as raté ta vie !").queue());
                e.getMessage().delete().queue();
            }
        }
    }

    @Override
    public void onGuildMessageDelete(GuildMessageDeleteEvent e) {
        if (RaidManager.getRaid(e.getMessageId()) != null) {
            RaidManager.deleteRaid(e.getMessageId());
        }
    }

    /**
     * Combine the strings in an array of strings
     * @param parts The array of strings
     * @param offset The offset in the array to start at
     * @return The combined string
     */
    private String combineArguments(String[] parts, int offset) {
        String text = "";
        for (int i = offset; i < parts.length; i++) {
            text += (parts[i] + " ");
        }

        return text.trim();
    }
}
