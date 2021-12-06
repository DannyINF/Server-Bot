package listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.STATIC;

import java.awt.*;
import java.sql.SQLException;
import java.util.Objects;

public class reportListener extends ListenerAdapter {
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        String[] answer = null;
        try {
            answer = core.databaseHandler.database(STATIC.getGuild().getId(), "select * from reports where victim_id = '" + event.getAuthor().getId() + "' and (report_id = '1' or report_id = '2' or report_id = '3' or report_id = '4')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert answer != null;
        if (answer.length != 0 && answer[0].length() == 1) {
            switch (answer[0]) {
                case "1":
                    String user;
                    try {
                        if (event.getMessage().getContentRaw().contains("#"))
                            user = event.getJDA().getUserByTag(event.getMessage().getContentRaw()).getId();
                        else
                            user = event.getJDA().getUserById(event.getMessage().getContentRaw()).getId();
                    } catch (Exception e) {
                        event.getChannel().sendMessage(">>> Deine Angabe ist fehlerhaft. Bitte gib entweder die ID oder den Tag (Name#1234) an!").queue();
                        return;
                    }
                    event.getChannel().sendMessage(">>> Wo hat sich der Vorfall abgespielt? Gib hier bestenfalls die Channel-ID oder den Namen des Chats (egal ob Text oder Voice) an.").queue();
                    try {
                        core.databaseHandler.database(STATIC.getGuild().getId(), "update reports set report_id = '2', offender_id = '" + user + "' where victim_id = '" + event.getAuthor().getId() + "' and report_id = '1'");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "2":
                    if (event.getMessage().getContentRaw().length() > 100) {
                        event.getChannel().sendMessage("Deine Angabe ist zu groß. (**" + event.getMessage().getContentRaw().length() + "**/100)\n\n" +
                                "Wo hat sich der Vorfall abgespielt? Gib hier bestenfalls die Channel-ID oder den Namen des Chats (egal ob Text oder Voice) an.").queue();
                        break;
                    }
                    event.getChannel().sendMessage(">>> Wie w\u00fcrdest du den Vorfall einfach kategorisieren? (beispielsweise \"Beleidigung\", \"Hetze\" oder \"Spam\")\n " +
                            "Halte diese Angabe so allgemein und unpers\u00f6nlich wie m\u00f6glich!").queue();
                    String channel;
                    try {
                        channel = Objects.requireNonNull(STATIC.getGuild().getTextChannelById(event.getMessage().getContentRaw())).getAsMention();
                    } catch (Exception e) {
                        try {
                            channel = Objects.requireNonNull(STATIC.getGuild().getTextChannelsByName(event.getMessage().getContentRaw(), true).get(0)).getAsMention();
                        } catch (Exception ex) {
                            try {
                                channel = Objects.requireNonNull(STATIC.getGuild().getGuildChannelById(event.getMessage().getContentRaw())).getName();
                            } catch (Exception exc) {
                                channel = event.getMessage().getContentRaw().replace("'", "\"");
                            }
                        }
                    }
                    try {
                        core.databaseHandler.database(STATIC.getGuild().getId(), "update reports set report_id = '3', channel = '" + channel + "' where victim_id = '" + event.getAuthor().getId() + "' and report_id = '2'");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "3":
                    if (event.getMessage().getContentRaw().length() > 200) {
                        event.getChannel().sendMessage("Deine Angabe ist zu groß. (**" + event.getMessage().getContentRaw().length() + "**/200)\n\n" +
                                "Wie w\u00fcrdest du den Vorfall einfach kategorisieren? (beispielsweise \"Beleidigung\", \"Hetze\" oder \"Spam\")\n " +
                                "Halte diese Angabe so allgemein und unpers\u00f6nlich wie m\u00f6glich!").queue();
                        break;
                    }
                    event.getChannel().sendMessage(">>> Gibt es noch weitere Details oder Beschreibungen, die du angeben m\u00f6chtest? " +
                            "Je besser die Admins \u00fcber die Situation in Kenntnis gesetzt werden, desto genauer und fairer werden m\u00f6gliche Konsequenzen ausfallen!").queue();
                    try {
                        core.databaseHandler.database(STATIC.getGuild().getId(), "update reports set report_id = '4', cause = '" + event.getMessage().getContentRaw().replace("'", "\"") + "' where victim_id = '" + event.getAuthor().getId() + "' and report_id = '3'");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "4":
                    if (event.getMessage().getContentRaw().length() > 1024) {
                        event.getChannel().sendMessage("Deine Angabe ist zu groß. (**" + event.getMessage().getContentRaw().length() + "**/1.024)\n\n" +
                                "Gibt es noch weitere Details oder Beschreibungen, die du angeben m\u00f6chtest? " +
                                "Je besser die Admins \u00fcber die Situation in Kenntnis gesetzt werden, desto genauer und fairer werden m\u00f6gliche Konsequenzen ausfallen!").queue();
                        break;
                    }
                    event.getChannel().sendMessage(">>> Wenn du den Report absenden m\u00f6chtest, dann klicke hier auf den Haken!").queue(msg -> msg.addReaction("\u2705").queue());
                    try {
                        core.databaseHandler.database(STATIC.getGuild().getId(), "update reports set report_id = '5', info = '" + event.getMessage().getContentRaw().replace("'", "\"") + "' where victim_id = '" + event.getAuthor().getId() + "' and report_id = '4'");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    public void onPrivateMessageReactionAdd(PrivateMessageReactionAddEvent event) {
        String[] answer = null;
        try {
            answer = core.databaseHandler.database(STATIC.getGuild().getId(), "select * from reports where victim_id = '" + event.getUserId() + "' and report_id = '5'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert answer != null;
        if (answer.length != 0 && answer[0].length() == 1) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.RED);
            embed.setTitle("REPORT");
            embed.setDescription("Der Nutzer **" + Objects.requireNonNull(STATIC.getGuild().getMemberById(answer[1])).getUser().getAsTag() + "** hat den Nutzer **" + Objects.requireNonNull(STATIC.getGuild().getMemberById(answer[2])).getUser().getAsTag() + "** reportet.");
            embed.addField("Grund:", answer[4], false);
            embed.addField("Channel:", answer[3], false);
            embed.addField("Beschreibung:", answer[5], false);
            STATIC.getModlog(). sendMessageEmbeds(embed.build()).queue(msg -> {
                msg.addReaction("\u21A9").queue();
                msg.addReaction("\u2705").queue();
                msg.addReaction("\uD83C\uDFAD").queue();
                msg.addReaction("\u2B55").queue();
                msg.addReaction("\u26D4").queue();
                msg.addReaction("\uD83D\uDD28").queue();
                try {
                    core.databaseHandler.database(STATIC.getGuild().getId(), "update reports set report_id = '" + msg.getId() + "' where victim_id = '" + event.getUserId() + "' and report_id = '5'");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            event.getChannel().sendMessage(">>> Dein Report wurde abgesendet und wird schnellstm\u00f6glichst bearbeitet. Vielen Dank!").queue();

        }
    }
}