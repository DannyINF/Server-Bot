package serverbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.springframework.data.util.Streamable;
import serverbot.channel.Channel;
import serverbot.channel.ChannelManagement;
import serverbot.channel.ChannelType;
import serverbot.core.MessageActions;
import serverbot.core.PermissionChecker;
import serverbot.statistics.Statistics;
import serverbot.statistics.StatisticsManagement;
import serverbot.util.SpringContextUtils;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;

public class CmdCredits {

    public static void get(SlashCommandEvent event, Member member) {
        credits(event, member);
    }

    public static void give(SlashCommandEvent event, long amount, User user) {
        if (PermissionChecker.checkPermission(new Permission[]{Permission.ADMINISTRATOR}, event.getMember())) {

            ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);
            Streamable<Channel> streamableModlog = channelManagement.findByServerIdAndChannelType(event.getGuild().getId(), ChannelType.MODLOG);
            if (streamableModlog.isEmpty()) {
                MessageActions.needChannel(event, ChannelType.MODLOG);
            } else {
                TextChannel modlog = event.getGuild().getTextChannelById(streamableModlog.stream().findFirst().get().getChannelId());
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.RED);
                NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
                embed.setDescription("**" + event.getUser().getAsTag() + "** hat dem Nutzer **" + user.getAsTag() + "**" +
                        " `" + numberFormat.format(amount) + "` Credits hinzugef\u00fcgt.");
                embed.setTimestamp(Instant.now());
                assert modlog != null;
                modlog.sendMessageEmbeds(embed.build()).queue();

                StatisticsManagement statisticsManagement = SpringContextUtils.getBean(StatisticsManagement.class);
                statisticsManagement.addCreditsToUser(user.getId(), event.getGuild().getId(), amount);

                event.reply("Hat dem Nutzer **" + user.getAsTag() + "** `" + amount + "` hinzugef\u00FCgt.").queue();
            }
        } else {
            PermissionChecker.noPower(event);
        }
    }

    public static void gift(SlashCommandEvent event, long amount, User user) {
        StatisticsManagement statisticsManagement = SpringContextUtils.getBean(StatisticsManagement.class);
        Statistics statistics = statisticsManagement.findByUserIdAndServerId(event.getMember().getId(),
                event.getGuild().getId()).get();
        Long credits = statistics.getCurrency();
        Long level = statistics.getLevel();
        if (level < 50L) {
            event.reply("Du musst mindestens Level 50 erreicht haben, um Credits zu verschenken.").queue();
        } else if (credits < amount) {
            event.reply("Du kannst nicht mehr Credits verschenken, als du besitzt!").queue();
        } else {
            statisticsManagement.addCreditsToUser(event.getUser().getId(), event.getGuild().getId(), -amount);
            statisticsManagement.addCreditsToUser(user.getId(), event.getGuild().getId(), amount);

            event.reply("**" + event.getUser().getAsTag() + "** hat an **" + user.getAsTag() + "** `" + amount + "` Credits verschenkt.").queue();
        }
    }

    private static void credits(SlashCommandEvent event, Member member) {
        StatisticsManagement statisticsManagement = SpringContextUtils.getBean(StatisticsManagement.class);
        String strMember;
        strMember = member.getUser().getAsTag();

        NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");

        Statistics statistics = statisticsManagement.findByUserIdAndServerId(member.getId(),
                event.getGuild().getId()).get();
        Long credits = statistics.getCurrency();

        event.reply(MessageActions.getLocalizedString("coins_msg", "user", member.getId())
                .replace("[USER]", strMember).replace("[COINS]", numberFormat.format(credits))).queue();

    }
}
