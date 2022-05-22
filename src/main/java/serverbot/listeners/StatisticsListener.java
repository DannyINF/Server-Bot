package serverbot.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import serverbot.statistics.StatisticsManagement;
import serverbot.util.SpringContextUtils;

public class StatisticsListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        StatisticsManagement statisticsManagement = SpringContextUtils.getBean(StatisticsManagement.class);

        int newWords = event.getMessage().getContentRaw().split(" ").length;
        int newMsg = 1;
        int newChars = event.getMessage().getContentRaw().length();

        statisticsManagement.addWordsToUser(event.getMember().getId(), event.getGuild().getId(), (long) newWords);
        statisticsManagement.addMessagesToUser(event.getMember().getId(), event.getGuild().getId(), (long) newMsg);
        statisticsManagement.addCharsToUser(event.getMember().getId(), event.getGuild().getId(), (long) newChars);

    }
}