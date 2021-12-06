package listeners;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class statisticsListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String[] answer1 = null;
        try {
            answer1 = core.databaseHandler.database(event.getGuild().getId(), "select words, msg, chars from users where id = '" + event.getAuthor().getId() + "'");
        } catch (SQLException ignored) {
        }
        int words;
        int msg;
        int chars;

        try {
            assert answer1 != null;
            words = Integer.parseInt(answer1[0]);
        } catch (Exception e) {
            words = 0;
        }
        try {
            msg = Integer.parseInt(answer1[1]);
        } catch (Exception e) {
            msg = 0;
        }
        try {
            chars = Integer.parseInt(answer1[2]);
        } catch (Exception e) {
            chars = 0;
        }

        int newWords = words + event.getMessage().getContentRaw().split(" ").length;
        int newMsg = msg + 1;
        int newChars = chars + event.getMessage().getContentRaw().length();

        try {
            core.databaseHandler.database(event.getGuild().getId(), "update users set words = " + newWords + ", msg = " + newMsg + ", chars = " + newChars + " where id = '" + event.getAuthor().getId() + "'");
        } catch (SQLException ignored) {
        }
    }
}