package serverbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
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
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import serverbot.util.LevelChecker;
import serverbot.util.SpringContextUtils;
import serverbot.util.GetUser;

import java.awt.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class CmdXp implements Command {

    private static void xp(GuildMessageReceivedEvent event, Member xpMember) {
        StatisticsManagement statisticsManagement = SpringContextUtils.getBean(StatisticsManagement.class);
        String strMember;
        strMember = xpMember.getUser().getAsTag();

        NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");

        Statistics statistics = statisticsManagement.findByUserIdAndServerId(event.getMember().getId(),
                event.getGuild().getId()).get();
        Long xp = statistics.getXp();
        Long level = statistics.getLevel();

        event.getChannel()
                .sendMessage(MessageActions.getLocalizedString("xp_msg", "serverbot/user", event.getAuthor().getId())
                        .replace("[USER]", strMember).replace("[LEVEL]", numberFormat.format(level))
                        .replace("[XP]", numberFormat.format(xp))).queue();

    }

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws SQLException {
        try {
            switch (args[0]) {
                case "leaderboard":
                case "ranking":
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
                                    result.size() - 1 - (result.size() - 1 - index + Math.min(10, result.size()) / 2))), Math.max(index-Math.min(10, result.size())+1, 0));

                    StringBuilder sb = new StringBuilder();

                    String name;
                    Long xp;
                    Long level;
                    int k;
                    for (int j = startingIndex; j < startingIndex + Math.min(10, result.size()-startingIndex); j++) {
                        try {
                            name = event.getJDA().getUserById(result.get(j).getUserId()).getAsTag();
                        } catch (Exception e) {
                            name = result.get(j).getUserId();
                        }
                        xp = result.get(j).getXp();
                        level = result.get(j).getLevel();

                        if (name.equals(event.getAuthor().getAsTag()) && j + startingIndex == startingIndex) {
                            sb.append("```css\n");
                        } else if (j + startingIndex == startingIndex*2) {
                            sb.append("```");
                        } else if (name.equals(event.getAuthor().getAsTag())) {
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
                                event.getAuthor().getId()));
                        sb.append(numberFormat.format(level));
                        k = numberFormat.format(level).length();
                        while (k < 10) {
                            sb.append(" ");
                            k++;
                        }
                        sb.append(
                                MessageActions.getLocalizedString("xp_ranking_xp", "user", event.getAuthor().getId()));
                        sb.append(numberFormat.format(xp));
                        k = xp.toString().length();
                        while (k < 10) {
                            sb.append(" ");
                            k++;
                        }
                        sb.append("\n");
                        if (name.equals(event.getAuthor().getAsTag()) &&
                                j + startingIndex != Math.min(10, result.size()) - 1 + startingIndex) {
                            sb.append("\n``````");
                        }

                    }
                    sb.append("```");

                    event.getChannel().sendMessage(sb.toString()).queue();
                    break;
                case "give":
                    if (PermissionChecker.checkPermission(new Permission[]{Permission.ADMINISTRATOR},
                            event.getMember())) {

                        long amount;
                        Member member;
                        MessageChannel channel = event.getChannel();
                        try {
                            ArrayList<String> list = new ArrayList<>(Arrays.asList(args).subList(1, args.length - 1));
                            member = GetUser.getMemberFromInput(list.toArray(new String[0]), event.getAuthor(),
                                    event.getGuild(), event.getChannel());
                        } catch (Exception e) {
                            channel.sendMessage("Gib bitte einen Nutzer an.")
                                    .queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
                            break;
                        }
                        assert member != null;
                        try {
                            amount = Long.parseLong(args[args.length - 1]);
                        } catch (Exception e) {
                            channel.sendMessage("Gib bitte die Anzahl an XP an, die du hinzuf\u00fcgen willst.")
                                    .queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
                            break;
                        }

                        try {
                            statisticsManagement = SpringContextUtils.getBean(StatisticsManagement.class);
                            statisticsManagement.addXpToUser(member.getId(), event.getGuild().getId(), amount);

                            EmbedBuilder embed = new EmbedBuilder();
                            embed.setColor(Color.RED);
                            NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
                            embed.setDescription("**" + event.getAuthor().getAsTag() + "** hat dem Nutzer **" +
                                    member.getUser().getAsTag() + "**" +
                                    " `" + numberFormat.format(amount) + "` XP hinzugef\u00fcgt.");
                            embed.setTimestamp(Instant.now());
                            ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);
                            event.getGuild().getTextChannelById(
                                            channelManagement.findByServerIdAndChannelType(event.getGuild().getId(),
                                                    ChannelType.MODLOG).stream().findFirst().get().getChannelId())
                                    .sendMessageEmbeds(embed.build()).queue();

                            LevelChecker.checker(member, event.getGuild());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        PermissionChecker.noPower(event.getChannel(), Objects.requireNonNull(event.getMember()));
                    }

                    break;
                case "next":
                    try {
                        statisticsManagement = SpringContextUtils.getBean(StatisticsManagement.class);
                        Statistics statistics = statisticsManagement.findByUserIdAndServerId(event.getMember().getId(),
                                event.getGuild().getId()).get();

                        xp = statistics.getXp();
                        level = statistics.getLevel();

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
                        embed.setTitle("Next level / next rank for " + event.getAuthor().getAsTag());
                        NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
                        embed.setDescription(
                                "Next level: " + numberFormat.format(LevelChecker.nextLevel(xp)) + " XP remaining\n" +
                                        "Next rank: " + LevelChecker.nextRank(xp)[1] + " (" +
                                        numberFormat.format(Long.parseLong(LevelChecker.nextRank(xp)[0])) +
                                        " XP remaining)"
                        );
                        event.getChannel().sendMessageEmbeds(embed.build()).queue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    ArrayList<String> args2 = new ArrayList<>();
                    int i = 0;
                    while (i < args.length - 1) {
                        args2.add(args[i]);
                        args2.add(" ");
                        i++;
                    }
                    args2.add(args[i]);
                    String[] args3 = new String[args2.size()];
                    args3 = args2.toArray(args3);
                    Member member = GetUser.getMemberFromInput(args3, event.getAuthor(), event.getGuild(),
                            event.getChannel());
                    assert member != null;
                    xp(event, member);

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            xp(event, Objects.requireNonNull(event.getMember()));
        }
    }
}

//TODO: Update message