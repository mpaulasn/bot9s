package me.cbitler.raidbot.creation;

import me.cbitler.raidbot.RaidBot;
import me.cbitler.raidbot.raids.PendingRaid;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

/**
 * Set the time for the raid
 * @author Christopher Bitler
 */
public class RunQueuedRaidStep implements CreationStep {

    /**
     * Handle setting the time for the raid
     * @param e The direct message event
     * @return True if the time is set, false otherwise
     */
    public boolean handleDM(PrivateMessageReceivedEvent e) {
        RaidBot bot = RaidBot.getInstance();
        PendingRaid raid = bot.getPendingRaids().get(e.getAuthor().getId());
        if (raid == null) {
            return false;
        }
        String decision = e.getMessage().getRawContent().trim().toLowerCase();
        String yn = "";

        switch(decision)
        {
            case "yes":
            case "y":
            case "oui":
            case "o":
                yn = "1";
                break;
            case "no":
            case "n":
            case "non":
                yn = "0";
                break;
            default:
                e.getChannel().sendMessage("Répondre **oui** ou **non** c'est trop compliqué ?!").queue();
                return false;
        }

        raid.setQueued(yn);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    public String getStepText() {
        return "Veux-tu une liste d'attente pour ce raid ? [Oui/Non]";
    }

    /**
     * {@inheritDoc}
     */
    public CreationStep getNextStep() {
        return null;
    }
}
