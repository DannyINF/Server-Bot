package serverbot.commands;

//import serverbot.core.databaseHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import serverbot.statistics.Statistics;
import serverbot.statistics.StatisticsManagement;
import serverbot.util.SpringContextUtils;
import serverbot.util.getUser;

import java.awt.*;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Locale;

public class cmdStats implements Command {
    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        Member member;
        TextChannel channel = event.getChannel();
        if (args.length > 0) {
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
            member = getUser.getMemberFromInput(args3, event.getAuthor(), event.getGuild(), channel);
        } else {
            member = event.getMember();
        }
        assert member != null;
        long words;
        long msg;
        long chars;
        long voiceTime;

        StatisticsManagement statisticsManagement = SpringContextUtils.getBean(StatisticsManagement.class);
        Statistics statistics = statisticsManagement.findByUserIdAndServerId(event.getMember().getId(), event.getGuild().getId()).get();
        words = statistics.getWords();
        msg = statistics.getMessages();
        chars = statistics.getChars();
        voiceTime = statistics.getVoiceTime();

        long hours = voiceTime / 60;
        long minutes = voiceTime % 60;
        long days = hours / 24;
        hours = hours % 24;

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(191, 255, 178));
        embed.setTitle("Statistiken f\u00fcr " + member.getUser().getAsTag());
        embed.setFooter("seit dem " + statistics.getFirstJoin(), null);

        embed.setTimestamp(Instant.now());
        NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
        embed.setDescription(
                        "Words: " + numberFormat.format(words) +
                        "\nMessages: " + numberFormat.format(msg) +
                        "\nCharacters: " + numberFormat.format(chars) +
                        "\nVoicetime: " + days + " days, " + hours + " hours, " + minutes + " minutes");
        event.getChannel(). sendMessageEmbeds(embed.build()).queue();

    }
}
