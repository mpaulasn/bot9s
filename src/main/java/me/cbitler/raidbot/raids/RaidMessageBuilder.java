package me.cbitler.raidbot.raids;

import me.cbitler.raidbot.RaidBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;

import java.awt.Color;

/**
 *
 * @author Yann 'Ze' Richard
 */
public class RaidMessageBuilder {

    // \u202f \u200b
    // \u2063

    //static String fieldValuePrefix  = "\u202f\u202f\u202f\u202f";
    static String fieldValuePrefix  = "\u00a0\u00a0\u00a0\u00a0";
    static String userInRaidPrefix  = fieldValuePrefix + fieldValuePrefix + ":small_blue_diamond: ";
    static String userInQueuePrefix = fieldValuePrefix + fieldValuePrefix + ":small_orange_diamond: ";

    public static MessageEmbed buildEmbed(PendingRaid raid) {
        return buildEmbed(
            raid.getName(),
            raid.getDescription(),
            raid.getLeaderName(),
            raid.getDate(),
            raid.getTime(),
            buildRolesText(raid),
            "",
            raid.getQueued()
        );
    }

    public static MessageEmbed buildEmbed(Raid raid) {
        return buildEmbed(
            raid.getName(),
            raid.getDescription(),
            raid.getRaidLeaderName(),
            raid.getDate(),
            raid.getTime(),
            buildRolesText(raid),
            raid.messageId,
            raid.getQueued()
        );
    }

    private static MessageEmbed buildEmbed(String name, String description, String leader, String date, String time, String roleText, String messageId, String queued) {
        EmbedBuilder builder = new EmbedBuilder();

        String author_img_url    = "https://ffxiv.gamerescape.com/w/images/9/90/Player32_Icon.png";
        String thumbnail_img_url = "https://tabula-rasa.ovh/discord/The_Feast3_Icon.png";
        String footer_img_url    = "https://ffxiv.gamerescape.com/w/images/f/f2/Mob19_Icon.png";
        String author_url        = "https://tabula-rasa.ovh/";
        String greets            = fieldValuePrefix + fieldValuePrefix + " By Yrline Hil'Wayard @Cerberus ! Envoyez vos dons ;)";

        String greetsLine        = "";
        Boolean legende          = false;
        String legendeLine       = "";

        builder.setThumbnail(thumbnail_img_url);
        builder.setColor(new Color(16729856));

        builder.setAuthor(name, author_url, author_img_url);
        String desc = "```diff\n-> " + description + "\n```\n";
        builder.setDescription(desc);

        if ( queued.equals("1") ) {
            legende = true;
            greetsLine = greets;
            legendeLine = "**" + userInRaidPrefix + "** : tout est ok !\n";
            legendeLine += "**" + userInQueuePrefix + "** : en liste d'attente..\n";
        }

        builder.addField(":gem: Créé par : ", "**" + fieldValuePrefix + leader + "**", true);
        builder.addField(":watch: Date / Heure ", date + " à " + time + "\n", true);
        builder.addField(":pushpin: Roles :", roleText, true);
        if ( legende ) {
            builder.addField(":warning: Légende : ",legendeLine , true);
        }
        builder.setFooter("Raid ID : " + messageId + greetsLine, footer_img_url);

        return builder.build();
    }


    /**
     * Builds the text to go into the roles field in the embedded message
     * @param raid The raid object
     * @return The role text
     */
    private static String buildRolesText(PendingRaid raid) {
        String text = "";

        for(RaidRole role : raid.getRolesWithNumbers()) {
            text += ("\n" + "**" + fieldValuePrefix + role.name + " (0 / " + role.amount + ") :** \n");
        }
        return text;
    }

    /**
     * Build the role text, which shows the roles users are playing in the raids
     * @return The role text
     */
    private static String buildRolesText(Raid raid) {
        String text = "";
        for(RaidRole role : raid.getRoles()) {
            int cpt = 0;
            int max = role.getAmount();
            String players = "";
            String prefix = userInRaidPrefix;
            for(RaidUser user : raid.getUsersInRole(role.name)) {
                if ( cpt >= max ) { prefix = userInQueuePrefix; }
                players += prefix + user.name + " (" + user.spec + ")\n";
                cpt++;
            }
            //players += "\";
            text += ("\n" + "**" + fieldValuePrefix + role.name + " (" + cpt + " / " + role.amount + "):** \n") + players;
        }
        return text + "\n";
    }
}
