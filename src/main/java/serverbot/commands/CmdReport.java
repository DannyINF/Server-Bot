package serverbot.commands;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import serverbot.core.SlashCommandHandler;
import serverbot.report.Report;
import serverbot.report.ReportManagement;
import serverbot.report.RulingType;
import serverbot.util.SpringContextUtils;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class CmdReport {

    public static void report(SlashCommandEvent event, User offender, GuildChannel guildChannel) {
        event.reply("Ich habe dir privat ein paar Fragen gesendet. Bitte beantworte diese, um deinen Report abzuschließen.").queue();
        ReportManagement reportManagement = SpringContextUtils.getBean(ReportManagement.class);
        reportManagement.save(new Report(LocalDateTime.now(), event.getUser().getId(), event.getGuild().getId(), offender.getId(), guildChannel.getId(), "", "", RulingType.NEED_CAUSE, "", 0L));

        event.getUser().openPrivateChannel().queue(channel -> {
            channel.sendMessage(">>> Hey **" + event.getUser().getAsTag() + "**,\n" +
                    "um einen Report zu erstellen, musst du mir noch einige Fragen beantworten:").queue();
            channel.sendMessage(">>> Wie w\u00fcrdest du den Vorfall einfach kategorisieren? (beispielsweise \"Beleidigung\", \"Hetze\" oder \"Spam\")\n " +
                    "Halte diese Angabe so allgemein und unpers\u00f6nlich wie m\u00f6glich!").queue();
        });
    }
}