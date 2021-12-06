package core;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import util.STATIC;

import java.sql.SQLException;
import java.util.*;

public class messageActions {
    public static void selfDestroyMSG(Message msg, int time_in_millis, GuildMessageReceivedEvent event) {
        Message send = event.getChannel().sendMessage(msg).complete();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                send.delete().queue();
            }
        }, time_in_millis);
    }

    /**
     * @param string   string you want to insert (e.g. test_test)
     * @param kind_msg where does the string belong to (serversettings [type "server"] or usersettings [type "user])
     * @param id       id of server or user
     */
    public static String getLocalizedString(String string, String kind_msg, String id) {
        String localizedString;
        String lang;
        String country;
        String table_name;
        String db_name;
        String[] answer = null;


        if ("user".equals(kind_msg)) {
            db_name = STATIC.getGuild().getId();
            table_name = "users";
            try {
                answer = databaseHandler.database(db_name, "select language, country from " + table_name + " where id = '" + id + "'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            assert answer != null;
            lang = answer[0];
            country = answer[1];
        } else {
            lang = "de";
            country = "de";
        }


        Locale currentLocale = new Locale(lang, country);
        localizedString = ResourceBundle.getBundle("MessageBundle", currentLocale).getString(string);
        localizedString = localizedString.replace("ö", "\u00f6");
        localizedString = localizedString.replace("Ö", "\u00d6");
        localizedString = localizedString.replace("ä", "\u00e4");
        localizedString = localizedString.replace("Ä", "\u00c4");
        localizedString = localizedString.replace("ü", "\u00fc");
        localizedString = localizedString.replace("Ü", "\u00dc");
        localizedString = localizedString.replace("ß", "\u00df");
        localizedString = localizedString.replace("ẞ", "\u1e9e");
        return localizedString;
    }
}