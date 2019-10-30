package me.cbitler.raidbot.creation;

import me.cbitler.raidbot.RaidBot;
import me.cbitler.raidbot.raids.PendingRaid;
import me.cbitler.raidbot.raids.RaidRole;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

/**
 * Role setup step for the raid.
 * This one should take multiple inputs and as a result it doesn't finish until the user
 * types 'done'.
 * @author Christopher Bitler
 */
public class RunRoleSetupStep implements CreationStep {

    /**
     * Handle user input - should be in the format [number]:[role] unless it is 'done'.
     * @param e The direct message event
     * @return True if the user entered 'done', false otherwise
     */
    public boolean handleDM(PrivateMessageReceivedEvent e) {
        RaidBot bot = RaidBot.getInstance();
        PendingRaid raid = bot.getPendingRaids().get(e.getAuthor().getId());

        if(e.getMessage().getRawContent().trim().equalsIgnoreCase("done")) {
            if(raid.getRolesWithNumbers().size() > 0) {
                return true;
            } else {
                e.getChannel().sendMessage("T'es gentil mais il faut au moins un type de participant ...").queue();
                return false;
            }
        } else {
            if ( e.getMessage().getRawContent().trim().equalsIgnoreCase("D") ) {
                raid.getRolesWithNumbers().add(new RaidRole(1, "Tank"));
                raid.getRolesWithNumbers().add(new RaidRole(1, "Heal"));
                raid.getRolesWithNumbers().add(new RaidRole(2, "DPS"));
                e.getChannel().sendMessage("BAM ! Une équipe de type Donjon a été ajouté !").queue();
                e.getChannel().sendMessage("Si tu as terminé, tape *done*").queue();
            } else if ( e.getMessage().getRawContent().trim().equalsIgnoreCase("R") ) {
                raid.getRolesWithNumbers().add(new RaidRole(2, "Tank"));
                raid.getRolesWithNumbers().add(new RaidRole(2, "Heal"));
                raid.getRolesWithNumbers().add(new RaidRole(4, "DPS"));
                e.getChannel().sendMessage("BIM ! Une équipe de type Raid a été ajouté !").queue();
                e.getChannel().sendMessage("Si tu as terminé, tape *done*").queue();
            } else {
                String[] parts = e.getMessage().getRawContent().split(":");
                if(parts.length < 2) {
                    e.getChannel().sendMessage(
                        "Tu peux rajouter des types de participants en précisant chaque grands rôles au format : [nombre max]:[Rôle] ou alors utilises directement des rôles préformatés : \n"
                        + "  - *D* pour Donjons (4 joueurs, 1 Tank, 1 Heal, 2 DPS)\n"
                        + "  - *R* pour Raids (8 joueurs, 2 Tank, 2 Heal, 4 DPS)\n"
                    ).queue();
                } else {
                    try {
                        int amnt = Integer.parseInt(parts[0].trim());
                        String roleName = parts[1].trim();
                        // Standardize for role : DPS Heal Tank
                        if ( roleName.equalsIgnoreCase("DPS") ) { roleName = "DPS"; }
                        else if ( roleName.equalsIgnoreCase("Heal") ) { roleName = "Heal"; }
                        else if ( roleName.equalsIgnoreCase("Tank") ) { roleName = "Tank"; }

                        raid.getRolesWithNumbers().add(new RaidRole(amnt, roleName));
                        e.getChannel().sendMessage("j'ai donc ajouté " + amnt + "  " + roleName + "dans ton raid !").queue();
                        e.getChannel().sendMessage("Si tu as terminé, tape *done*").queue();
                    } catch (Exception ex) {
                        e.getChannel().sendMessage("Putain tu tapes n'importe quoi la...").queue();
                    }
                }
            }
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getStepText() {
        return
        "Rajoutes des types de participants en précisant chaque grands rôles au format : [nombre max]:[Rôle] ou alors utilises directement des rôles préformatés : \n"
        + "  - *D* pour Donjons (4 joueurs, 1 Tank, 1 Heal, 2 DPS)\n"
        + "  - *R* pour Raids (8 joueurs, 2 Tank, 2 Heal, 4 DPS)\n"
        + "  - pour avoir 8 DPS, tapes *8:dps*\n"
        + "Quand t'as terminé, signale le moi en tapant *done*";
    }

    /**
     * {@inheritDoc}
     */
    public CreationStep getNextStep() {
        return new RunQueuedRaidStep();
    }
}
