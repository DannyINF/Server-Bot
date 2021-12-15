package serverbot.core;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import serverbot.user.User;
import serverbot.user.UserManagement;
import serverbot.util.STATIC;
import serverbot.util.SpringContextUtils;

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
     * @param kind_msg where does the string belong to (serversettings [type "server"] or usersettings [type "data.user])
     * @param id       id of server or data.user
     */
    public static String getLocalizedString(String string, String kind_msg, String id) {
        String localizedString;

        UserManagement userManagement = SpringContextUtils.getBean(UserManagement.class);

        User user = userManagement.findById(id).get();

        Locale currentLocale = user.getLanguage();
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