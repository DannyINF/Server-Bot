package serverbot.listeners;

//import serverbot.core.databaseHandler;
import serverbot.core.messageActions;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;


public class banListener extends ListenerAdapter {


    public void onGuildBan(@NotNull GuildBanEvent event) {
        /*try {
            databaseHandler.database(event.getGuild().getId(), "update users set verifystatus = FALSE where id = '" + event.getUser().getId() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

        TextChannel welcome = event.getGuild().getDefaultChannel();
        assert welcome != null;
        welcome.sendMessage(event.getUser().getAsMention() + " klebt jetzt am Banhammer!").queue();

    }

    public void onGuildUnban(@NotNull GuildUnbanEvent event) {
        event.getGuild().getTextChannelsByName("logchannel", true).get(0).sendMessage(
                messageActions.getLocalizedString("log_unban", "server", event.getGuild().getId())
                        .replace("[USER]", event.getUser().getName() + "#" + event.getUser().getDiscriminator())).queue();
    }

    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        if (event.getRoles().contains(event.getGuild().getRolesByName("MYTHEN AUS MITTELERDE", true).get(0)) || event.getRoles().contains(event.getGuild().getRolesByName("MODERATOR", true).get(0))) {
            event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRolesByName((event.getMember().getRoles().contains(event.getGuild().getRolesByName("MYTHEN AUS MITTELERDE", true).get(0)) ? "MODERATOR" : "MYTHEN AUS MITTELERDE"), true).get(0)).queue();
        }
    }
}
