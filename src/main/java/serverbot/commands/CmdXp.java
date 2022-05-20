package serverbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.springframework.data.util.Streamable;
import serverbot.channel.ChannelManagement;
import serverbot.channel.ChannelType;
import serverbot.core.MessageActions;
import serverbot.core.PermissionChecker;
import serverbot.statistics.Statistics;
import serverbot.statistics.StatisticsManagement;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import serverbot.util.LevelChecker;
import serverbot.util.SpringContextUtils;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class CmdXp {

    public static void ranking(SlashCommandEvent event) {
        event.deferReply(true);
        StatisticsManagement statisticsManagement = SpringContextUtils.getBean(StatisticsManagement.class);
        Streamable<Statistics> statisticsStreamable = statisticsManagement.findAll();

        List<Statistics> result = statisticsStreamable.stream()
                .filter(statistics -> statistics.getServerId().equals(event.getGuild().getId())).sorted(
                        Comparator.comparing(Statistics::getXp)).collect(Collectors.toList());

        //remove bot from xp ranking
        result.remove(result.get(result.stream().map(Statistics::getUserId).collect(Collectors.toList())
                .lastIndexOf(event.getJDA().getSelfUser().getId())));

        Collections.reverse(result);

        int index = result.stream().map(Statistics::getUserId).collect(Collectors.toList())
                .lastIndexOf(event.getMember().getId());

        int startingIndex = Math.min(Math.min(result.size() - 1,
                Math.max(0,
                        result.size() - 1 - (result.size() - 1 - index + Math.min(10, result.size()) / 2))), Math.max(index - Math.min(10, result.size()) + 1, 0));

        StringBuilder sb = new StringBuilder();

        String name;
        Long xp;
        Long level;
        int k;
        for (int j = startingIndex; j < startingIndex + Math.min(10, result.size() - startingIndex); j++) {
            try {
                name = event.getJDA().getUserById(result.get(j).getUserId()).getAsTag();
            } catch (Exception e) {
                name = result.get(j).getUserId();
            }
            xp = result.get(j).getXp();
            level = result.get(j).getLevel();

            if (name.equals(event.getUser().getAsTag()) && j + startingIndex == startingIndex) {
                sb.append("```css\n");
            } else if (j + startingIndex == startingIndex * 2) {
                sb.append("```");
            } else if (name.equals(event.getUser().getAsTag())) {
                sb.append("\n``````css\n");
            }

            sb.append(j + 1);
            sb.append(". ");
            sb.append(name);
            k = name.length();
            while (k < 35) {
                sb.append(" ");
                k++;
            }
            NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
            sb.append(MessageActions.getLocalizedString("xp_ranking_level", "user",
                    event.getUser().getId()));
            sb.append(numberFormat.format(level));
            k = numberFormat.format(level).length();
            while (k < 10) {
                sb.append(" ");
                k++;
            }
            sb.append(
                    MessageActions.getLocalizedString("xp_ranking_xp", "user", event.getUser().getId()));
            sb.append(numberFormat.format(xp));
            k = xp.toString().length();
            while (k < 10) {
                sb.append(" ");
                k++;
            }
            sb.append("\n");
            if (name.equals(event.getUser().getAsTag()) &&
                    j + startingIndex != Math.min(10, result.size()) - 1 + startingIndex) {
                sb.append("\n``````");
            }

        }
        sb.append("```");

        event.reply(sb.toString()).queue();
    }

    public static void give(SlashCommandEvent event, long amount, Member member) {
        event.deferReply(true);
        if (PermissionChecker.checkPermission(new Permission[]{Permission.ADMINISTRATOR},
                event.getMember())) {

            MessageChannel channel = event.getChannel();

            StatisticsManagement statisticsManagement = SpringContextUtils.getBean(StatisticsManagement.class);
            statisticsManagement.addXpToUser(member.getId(), event.getGuild().getId(), amount);

            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.RED);
            NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
            embed.setDescription("**" + event.getUser().getAsTag() + "** hat dem Nutzer **" +
                    member.getUser().getAsTag() + "**" +
                    " `" + numberFormat.format(amount) + "` XP hinzugef\u00fcgt.");
            embed.setTimestamp(Instant.now());
            ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);
            event.getGuild().getTextChannelById(
                            channelManagement.findByServerIdAndChannelType(event.getGuild().getId(),
                                    ChannelType.MODLOG).stream().findFirst().get().getChannelId())
                    .sendMessageEmbeds(embed.build()).queue();
            event.replyEmbeds(embed.build()).queue();

            LevelChecker.checker(member, event.getGuild());
        } else {
            PermissionChecker.noPower(event);
        }
    }

    public static void next(SlashCommandEvent event, Member member) {
        event.deferReply(true);
        StatisticsManagement statisticsManagement = SpringContextUtils.getBean(StatisticsManagement.class);
        Statistics statistics = statisticsManagement.findByUserIdAndServerId(member.getId(),
                event.getGuild().getId()).get();

        long xp = statistics.getXp();
        long level = statistics.getLevel();

        Color color;
        if (level > 1049) {
            color = Color.decode("#ca2a1a");
        } else if (level > 749)
            color = Color.decode("#bf3636");
        else if (level > 499)
            color = Color.decode("#f25511");
        else if (level > 299)
            color = Color.decode("#f3730d");
        else if (level > 149)
            color = Color.decode("#e68f0a");
        else if (level > 49)
            color = Color.decode("#f7bf16");
        else if (level > 9)
            color = Color.decode("#fff53d");
        else
            color = Color.decode("#fff9ba");

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(color);
        embed.setTitle("Next level / next rank for " + member.getUser().getAsTag());
        NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
        embed.setDescription(
                "Next level: " + numberFormat.format(LevelChecker.nextLevel(xp)) + " XP remaining\n" +
                        "Next rank: " + LevelChecker.nextRank(xp)[1] + " (" +
                        numberFormat.format(Long.parseLong(LevelChecker.nextRank(xp)[0])) +
                        " XP remaining)"
        );
        event.replyEmbeds(embed.build()).queue();
    }

    public static void get(SlashCommandEvent event, Member member) {
        xp(event, member);
    }

    private static void xp(SlashCommandEvent event, Member xpMember) {
        StatisticsManagement statisticsManagement = SpringContextUtils.getBean(StatisticsManagement.class);
        String strMember = xpMember.getUser().getAsTag();

        NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");

        Statistics statistics = statisticsManagement.findByUserIdAndServerId(xpMember.getId(),
                event.getGuild().getId()).get();
        Long xp = statistics.getXp();
        Long level = statistics.getLevel();

        event.reply(MessageActions.getLocalizedString("xp_msg", "serverbot/user", xpMember.getId())
                .replace("[USER]", strMember).replace("[LEVEL]", numberFormat.format(level))
                .replace("[XP]", numberFormat.format(xp))).queue();
    }
}