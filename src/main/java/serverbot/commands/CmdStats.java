package serverbot.commands;

//import serverbot.core.databaseHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import serverbot.statistics.Statistics;
import serverbot.statistics.StatisticsManagement;
import serverbot.util.SpringContextUtils;
import serverbot.util.GetUser;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CmdStats {

    public static void stats(SlashCommandEvent event, Member member) {
        event.deferReply(true);
        MessageChannel channel = event.getChannel();

        long words;
        long msg;
        long chars;
        long voiceTime;

        StatisticsManagement statisticsManagement = SpringContextUtils.getBean(StatisticsManagement.class);
        Statistics statistics = statisticsManagement.findByUserIdAndServerId(member.getId(), event.getGuild().getId()).get();
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
        embed.setFooter("seit dem " + statistics.getFirstJoin().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), null);

        embed.setTimestamp(Instant.now());
        NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
        embed.setDescription(
                "Words: " + numberFormat.format(words) +
                        "\nMessages: " + numberFormat.format(msg) +
                        "\nCharacters: " + numberFormat.format(chars) +
                        "\nVoicetime: " + days + " days, " + hours + " hours, " + minutes + " minutes");
        event.replyEmbeds(embed.build()).queue();
    }
}
