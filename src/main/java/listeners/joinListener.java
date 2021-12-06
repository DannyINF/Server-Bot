package listeners;

import core.databaseHandler;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.STATIC;

import java.sql.SQLException;

import static java.lang.Boolean.FALSE;


public class joinListener extends ListenerAdapter {
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

        TextChannel welcome1 = event.getGuild().getDefaultChannel();
        if (event.getMember().getUser().isBot() == FALSE) {
            String[] exil = null;
            try {
                exil = databaseHandler.database(event.getGuild().getId(), "select id from exil");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            assert exil != null;
            boolean isExil = false;
            for (String str : exil) {
                if (str.equals(event.getMember().getId()))
                    isExil = true;
            }
            Role exil_role = STATIC.getExil()[0];
            if (isExil)
                event.getGuild().addRoleToMember(event.getMember(), exil_role).queue();
            else {
                assert welcome1 != null;
                welcome1.sendMessage(":flag_de: " + event.getMember().getAsMention() + ", alatulya meldonya! Bitte lies dir in " + STATIC.getRegeln().getAsMention() + " die Regeln durch und reagiere dort mit :flag_de: oder \uD83C\uDDEC\uD83C\uDDE7, um eine Sprache auszuw\u00e4hlen.\n\n" +
                        "\uD83C\uDDEC\uD83C\uDDE7 " + event.getMember().getAsMention() + ", alatulya meldonya! Please read the rules in " + STATIC.getRules().getAsMention() + " and react on them with :flag_de: or \uD83C\uDDEC\uD83C\uDDE7 to select a language.").queue();
            }
        }
    }
}
