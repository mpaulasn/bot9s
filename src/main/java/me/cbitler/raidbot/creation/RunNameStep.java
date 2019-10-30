package me.cbitler.raidbot.creation;

import me.cbitler.raidbot.RaidBot;
import me.cbitler.raidbot.raids.PendingRaid;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

/**
 * Set the name for the raid
 * @author Christopher Bitler
 */
public class RunNameStep implements CreationStep {

    String serverId;

    /**
     * Set the serverId for this step. This is needed for setting the serverId in the PendingRaid
     * @param serverId Server ID for the raid
     */
    public RunNameStep(String serverId) {
        this.serverId = serverId;
    }

    /**
     * Set the name of the raid and the server ID. Also create the raid if it doesn't exist
     * @param e The direct message event
     * @return True always
     */
    public boolean handleDM(PrivateMessageReceivedEvent e) {
        RaidBot bot = RaidBot.getInstance();
        PendingRaid raid = bot.getPendingRaids().get(e.getAuthor().getId());
        if(raid == null) {
            raid = new PendingRaid();
            raid.setLeaderName(e.getAuthor().getName());
            bot.getPendingRaids().put(e.getAuthor().getId(), raid);
        }

        raid.setName(e.getMessage().getRawContent());
        raid.setServerId(serverId);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    public String getStepText() {
        return " Allez c’est parti pour la création d'un petit raid des familles !!\n"
            + "Pendant toute notre petite discussion tu peux taper *cancel* pour annuler, Je ne te cache pas que je suis sensible et que cela me briserai le cœur…\n\n"
            + "Allez, un petit nom pour cette aventure :";
    }

    /**
     * {@inheritDoc}
     */
    public CreationStep getNextStep() {
        return new RunDescriptionStep();
    }
}
