package serverbot.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.util.Streamable;
import serverbot.channel.ChannelManagement;
import serverbot.channel.ChannelType;
import serverbot.report.Report;
import serverbot.report.ReportManagement;
import serverbot.report.RulingType;
import serverbot.util.SpringContextUtils;

import java.awt.*;
import java.util.Objects;

public class ReportListener extends ListenerAdapter {
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        ReportManagement reportManagement = SpringContextUtils.getBean(ReportManagement.class);
        Streamable<Report> reportStreamable = reportManagement.findUnfinishedReportsByUserId(event.getAuthor().getId(), RulingType.NEED_CAUSE, RulingType.NEED_INFO);
        if (!reportStreamable.isEmpty()) {
            Report report = reportStreamable.stream().findFirst().get();
            switch (report.getRulingType()) {
                case NEED_CAUSE:
                    if (event.getMessage().getContentRaw().length() > 200) {
                        event.getChannel().sendMessage("Deine Angabe ist zu groß. (**" + event.getMessage().getContentRaw().length() + "**/200)\n\n" +
                                "Wie w\u00fcrdest du den Vorfall einfach kategorisieren? (beispielsweise \"Beleidigung\", \"Hetze\" oder \"Spam\")\n " +
                                "Halte diese Angabe so allgemein und unpers\u00f6nlich wie m\u00f6glich!").queue();
                        break;
                    }
                    event.getChannel().sendMessage(">>> Gibt es noch weitere Details oder Beschreibungen, die du angeben m\u00f6chtest? " +
                            "Je besser die Admins \u00fcber die Situation in Kenntnis gesetzt werden, desto genauer und fairer werden m\u00f6gliche Konsequenzen ausfallen!").queue();
                    report.setRulingType(RulingType.NEED_INFO);
                    report.setCause(event.getMessage().getContentRaw());
                    reportManagement.save(report);
                    break;
                case NEED_INFO:
                    if (event.getMessage().getContentRaw().length() > 1024) {
                        event.getChannel().sendMessage("Deine Angabe ist zu groß. (**" + event.getMessage().getContentRaw().length() + "**/1.024)\n\n" +
                                "Gibt es noch weitere Details oder Beschreibungen, die du angeben m\u00f6chtest? " +
                                "Je besser die Admins \u00fcber die Situation in Kenntnis gesetzt werden, desto genauer und fairer werden m\u00f6gliche Konsequenzen ausfallen!").queue();
                        break;
                    }
                    event.getChannel().sendMessage(">>> Wenn du den Report absenden m\u00f6chtest, dann klicke hier auf den Haken!").queue(msg -> msg.addReaction("\u2705").queue());
                    report.setRulingType(RulingType.NEED_VERIFICATION);
                    report.setInfo(event.getMessage().getContentRaw());
                    reportManagement.save(report);
                    break;
            }
        }
    }

    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        ReportManagement reportManagement = SpringContextUtils.getBean(ReportManagement.class);
        Streamable<Report> reportStreamable = reportManagement.findByUserIdAndRulingType(event.getUserId(), RulingType.NEED_VERIFICATION);
        if (!reportStreamable.isEmpty() && event.isFromType(net.dv8tion.jda.api.entities.ChannelType.PRIVATE)) {
            Report report = reportStreamable.stream().findFirst().get();
            EmbedBuilder embed = new EmbedBuilder();
            Guild guild = event.getJDA().getGuildById(report.getServerId());
            embed.setColor(Color.RED);
            embed.setTitle("REPORT");
            embed.setDescription("Der Nutzer **" + Objects.requireNonNull(guild.getMemberById(report.getUserId())).getUser().getAsTag() + "** hat den Nutzer **" + guild.getMemberById(report.getOffenderId()).getUser().getAsTag() + "** reportet.");
            embed.addField("Grund:", report.getCause(), false);
            embed.addField("Channel:", guild.getGuildChannelById(report.getChannelId()).getAsMention(), false);
            embed.addField("Beschreibung:", report.getInfo(), false);
            ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);
            guild.getTextChannelById(channelManagement.findByServerIdAndChannelType(guild.getId(), ChannelType.MODLOG).stream().findFirst().get().getChannelId()).sendMessageEmbeds(embed.build()).queue(msg -> {
                msg.addReaction("\u21A9").queue();
                msg.addReaction("\u2705").queue();
                msg.addReaction("\uD83C\uDFAD").queue();
                msg.addReaction("\u2B55").queue();
                msg.addReaction("\u26D4").queue();
                msg.addReaction("\uD83D\uDD28").queue();
                report.setRulingType(RulingType.WAITING);
                report.setMessageId(msg.getId());
                reportManagement.save(report);
            });
            event.getChannel().sendMessage(">>> Dein Report wurde abgesendet und wird schnellstm\u00f6glichst bearbeitet. Vielen Dank!").queue();
        }
    }
}