package serverbot.listeners;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import serverbot.statistics.StatisticsManagement;
import serverbot.util.SpringContextUtils;

public class statisticsListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        StatisticsManagement statisticsManagement = SpringContextUtils.getBean(StatisticsManagement.class);

        int newWords = event.getMessage().getContentRaw().split(" ").length;
        int newMsg = 1;
        int newChars = event.getMessage().getContentRaw().length();

        statisticsManagement.addWordsToUser(event.getMember().getId(), event.getGuild().getId(), Long.valueOf(newWords));
        statisticsManagement.addMessagesToUser(event.getMember().getId(), event.getGuild().getId(), Long.valueOf(newMsg));
        statisticsManagement.addCharsToUser(event.getMember().getId(), event.getGuild().getId(), Long.valueOf(newChars));

    }
}