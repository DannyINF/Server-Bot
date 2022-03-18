package serverbot.listeners;

//import serverbot.core.databaseHandler;
import serverbot.core.MessageActions;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import serverbot.statistics.Statistics;
import serverbot.statistics.StatisticsManagement;
import serverbot.util.LevelChecker;
import serverbot.util.SpringContextUtils;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class XpListener extends ListenerAdapter {
    /**
     * @param event GuildMessageReceivedEvent
     */
    private static void checkLevel(GuildMessageReceivedEvent event) {
        StatisticsManagement statisticsManagement = SpringContextUtils.getBean(StatisticsManagement.class);
        Statistics statistics = statisticsManagement.findByUserIdAndServerId(event.getMember().getId(), event.getGuild().getId()).get();

        Long currentLevel = statistics.getLevel();

        Long newLevel = LevelChecker.checker(event.getMember(), event.getGuild());

        // if your current xp are bigger than the xp needed for the next level you receive a level up
        if (!Objects.equals(newLevel, currentLevel)) {
            statisticsManagement.setLevelOfUser(event.getMember().getId(), event.getGuild().getId(), newLevel);

            //adding the coins received through level-up to the total coins-count
            // creating level-up msg
            if (!event.getAuthor().isBot()) {

                event.getChannel().sendMessage(
                        MessageActions.getLocalizedString("xp_level_up", "user", event.getAuthor().getId())
                        .replace("[USER]", event.getAuthor().getAsMention()).replace("[LEVEL]", String.valueOf(newLevel))).queue(msg -> {
                            if (!(newLevel == 50 || newLevel == 100 || newLevel == 200 || newLevel == 350 || newLevel == 550)) {
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        msg.delete().queue();
                                    }
                                }, 15000);
                    }
                });
            }
        }
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        StatisticsManagement statisticsManagement = SpringContextUtils.getBean(StatisticsManagement.class);
        statisticsManagement.giveXP(event);
        checkLevel(event);
    }
}