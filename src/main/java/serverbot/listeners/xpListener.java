package serverbot.listeners;

import serverbot.core.databaseHandler;
import serverbot.core.messageActions;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import serverbot.statistics.StatisticsManagement;
import serverbot.util.LevelChecker;
import serverbot.util.SpringContextUtils;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class xpListener extends ListenerAdapter {
    /**
     * @param event GuildMessageReceivedEvent
     */
    private static void checkLevel(GuildMessageReceivedEvent event) throws SQLException {
        long currentlevel;

        // getting level and xp of the data.user
        String[] level = databaseHandler.database(event.getGuild().getId(), "select level from users where id = '" + event.getAuthor().getId() + "'");

        try {
            currentlevel = Long.parseLong(level[0]);
        } catch (Exception e) {
            currentlevel = 0;
        }

        long newlevel = LevelChecker.checker(Objects.requireNonNull(event.getMember()), event.getGuild());

        databaseHandler.database(event.getGuild().getId(), "update users set level = " + newlevel + " where id = '" + event.getAuthor().getId() + "'");

        // if your current xp are bigger than the xp needed for the next level you receive a level up
        if (newlevel != currentlevel) {

            //adding the coins received through level-up to the total coins-count
            // creating level-up msg
            if (!event.getAuthor().isBot()) {

                Message msg = event.getChannel().sendMessage(
                        messageActions.getLocalizedString("xp_level_up", "serverbot/user", event.getAuthor().getId())
                        .replace("[USER]", event.getAuthor().getAsMention()).replace("[LEVEL]", String.valueOf(newlevel))).complete();
                // msg deletes itself after 15000 milliseconds
                if (!(newlevel == 50 || newlevel == 100 || newlevel == 200 || newlevel == 350 || newlevel == 550)) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            msg.delete().queue();
                        }
                    }, 15000);
                }
            }
        }

        databaseHandler.database(event.getGuild().getId(), "update users set level = " + newlevel + " where id = '" + event.getAuthor().getId() + "'");

    }

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        StatisticsManagement statisticsManagement = SpringContextUtils.getBean(StatisticsManagement.class);
        try {
            statisticsManagement.giveXP(event);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            checkLevel(event);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}