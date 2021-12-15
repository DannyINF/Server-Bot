package serverbot.listeners;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import serverbot.channel.Channel;
import serverbot.channel.ChannelManagement;
import serverbot.statistics.Statistics;
import serverbot.statistics.StatisticsManagement;
import serverbot.user.User;
import serverbot.user.UserManagement;
import serverbot.util.SpringContextUtils;

import java.time.LocalDateTime;
import java.util.Locale;

public class readyListener extends ListenerAdapter {

    private long members;

    public void onReady(@NotNull ReadyEvent event) {

        ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);
        UserManagement userManagement = SpringContextUtils.getBean(UserManagement.class);
        StatisticsManagement statisticsManagement = SpringContextUtils.getBean(StatisticsManagement.class);

        StringBuilder out = new StringBuilder("\nThis bot is running on following servers: \n");

        for (Guild g : event.getJDA().getGuilds()) {
            members += g.getMembers().size();
            out.append(g.getName()).append(" (").append(g.getId()).append(")  ").append("Nutzeranzahl: ")
                    .append(g.getMembers().size()).append("\n");
            for (TextChannel textChannel : g.getTextChannels()) {
                if (!channelManagement.findByChannelIdAndServerId(textChannel.getId(), g.getId()).isPresent()) {
                    channelManagement.save(new Channel(textChannel.getId(), g.getId(), 1F));
                }
            }
            for (VoiceChannel voiceChannel : g.getVoiceChannels()) {
                if (!channelManagement.findByChannelIdAndServerId(voiceChannel.getId(), g.getId()).isPresent()) {
                    channelManagement.save(new Channel(voiceChannel.getId(), g.getId(), 1F));
                }
            }
            for (Member member : g.getMembers()) {
                if (!userManagement.findById(member.getId()).isPresent()) {
                    userManagement.save(new User(member.getId(), Locale.GERMAN));
                }
                if (!statisticsManagement.findByUserIdAndServerId(member.getId(), g.getId()).isPresent()) {
                    statisticsManagement.save(
                            new Statistics(member.getId(), g.getId(), 0L, 0L, 0L, 0L, LocalDateTime.now(),
                                    LocalDateTime.now(), null, 0L, 0L, 0L));
                }
            }
        }

        System.out.println(out);
        System.out.println("\nInsgesamte Nutzeranzahl: " + members);
    }
}