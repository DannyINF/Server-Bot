package serverbot.listeners;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import serverbot.channel.Channel;
import serverbot.channel.ChannelManagement;
import serverbot.channel.ChannelType;
import serverbot.member.MemberId;
import serverbot.member.MemberManagement;
import serverbot.role.RoleManagement;
import serverbot.role.RoleType;
import serverbot.server.Server;
import serverbot.server.ServerManagement;
import serverbot.statistics.Statistics;
import serverbot.statistics.StatisticsManagement;
import serverbot.user.User;
import serverbot.user.UserManagement;
import serverbot.util.SpringContextUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Collectors;

public class ReadyListener extends ListenerAdapter {

    private long members;

    public void onReady(@NotNull ReadyEvent event) {

        ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);
        UserManagement userManagement = SpringContextUtils.getBean(UserManagement.class);
        StatisticsManagement statisticsManagement = SpringContextUtils.getBean(StatisticsManagement.class);
        ServerManagement serverManagement = SpringContextUtils.getBean(ServerManagement.class);
        MemberManagement memberManagement = SpringContextUtils.getBean(MemberManagement.class);
        RoleManagement roleManagement = SpringContextUtils.getBean(RoleManagement.class);

        StringBuilder out = new StringBuilder("\nThis bot is running on following servers: \n");

        for (Guild g : event.getJDA().getGuilds()) {
            if (!serverManagement.findById(g.getId()).isPresent()) {
                serverManagement.save(new Server(g.getId(), 1, "/", 0));
                System.out.println("add server " + g.getName());
            } else {
                System.out.println("has server " + g.getName());
            }
            members += g.getMembers().size();
            out.append(g.getName()).append(" (").append(g.getId()).append(")  ").append("Nutzeranzahl: ")
                    .append(g.getMembers().size()).append("\n");
            for (Role role : g.getRoles()) {
                if (!roleManagement.findByRoleId(role.getId()).isPresent()) {
                    roleManagement.save(new serverbot.role.Role(role.getId(), g.getId(), RoleType.DEFAULT));
                    System.out.println("add role " + role.getName());
                } else {
                    System.out.println("has role " + role.getName());
                }
            }
            for (TextChannel textChannel : g.getTextChannels()) {
                if (!channelManagement.findByChannelIdAndServerId(textChannel.getId(), g.getId()).isPresent()) {
                    channelManagement.save(new Channel(textChannel.getId(), g.getId(), ChannelType.DEFAULT_TEXT, 1F));
                    System.out.println("add text channel " + textChannel.getName());
                } else {
                    System.out.println("has text channel " + textChannel.getName());
                }
            }
            for (VoiceChannel voiceChannel : g.getVoiceChannels()) {
                if (!channelManagement.findByChannelIdAndServerId(voiceChannel.getId(), g.getId()).isPresent()) {
                    channelManagement.save(new Channel(voiceChannel.getId(), g.getId(), ChannelType.DEFAULT_VOICE, 1F));
                    System.out.println("add voice channel " + voiceChannel.getName());
                } else {
                    System.out.println("has voice channel " + voiceChannel.getName());
                }
            }

            for (Category category : g.getCategories()) {
                if (!channelManagement.findByChannelIdAndServerId(category.getId(), g.getId()).isPresent()) {
                    channelManagement.save(new Channel(category.getId(), g.getId(), ChannelType.DEFAULT_CATEGORY, 1F));
                    System.out.println("add category channel " + category.getName());
                } else {
                    System.out.println("has category channel " + category.getName());
                }
            }
            for (Member member : g.getMembers()) {
                if (!userManagement.findById(member.getId()).isPresent()) {
                    userManagement.save(new User(member.getId(), Locale.GERMAN, false));
                    System.out.println("add user " + member.getEffectiveName());
                } else {
                    System.out.println("has user " + member.getEffectiveName());
                }
                if (!memberManagement.findById(new MemberId(member.getId(), g.getId())).isPresent()) {
                    memberManagement.save(new serverbot.member.Member(member.getId(), g.getId(), false, false, false,
                            member.getRoles().stream().map(role -> roleManagement.findByRoleId(role.getId()).get()).collect(
                                    Collectors.toList())));
                    System.out.println("add member " + member.getEffectiveName());
                } else {
                    System.out.println("has member " + member.getEffectiveName());
                }
                System.out.println(statisticsManagement.findByUserIdAndServerId(member.getId(), g.getId()));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                if (!statisticsManagement.findByUserIdAndServerId(member.getId(), g.getId()).isPresent()) {
                    statisticsManagement.save(
                            new Statistics(member.getId(), g.getId(), 0L, 0L, 0L, 0L, LocalDateTime.now(),
                                    LocalDateTime.now(), null, 0L, 0L, 0L));
                    System.out.println("add statistic for " + member.getEffectiveName());
                } else {
                    System.out.println("has statistic for " + member.getEffectiveName());
                }
            }
        }

        System.out.println(out);
        System.out.println("\nInsgesamte Nutzeranzahl: " + members);
    }
}