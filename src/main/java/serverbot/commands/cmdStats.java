package serverbot.commands;

import serverbot.core.databaseHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.derby.impl.sql.execute.CurrentDatetime;
import serverbot.util.getUser;

import java.awt.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.ZoneId;
import java.util.ArrayList;

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
        String[] answer1 = null;
        try {
            answer1 = databaseHandler.database(event.getGuild().getId(), "select words, msg, chars, voicetime, first_join from users where id = '" + member.getId() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long words;
        long msg;
        long chars;
        long voicetime;

        try {
            assert answer1 != null;
            words = Long.parseLong(answer1[0]);
        } catch (Exception e) {
            words = 0;
        }
        try {
            msg = Long.parseLong(answer1[1]);
        } catch (Exception e) {
            msg = 0;
        }
        try {
            chars = Long.parseLong(answer1[2]);
        } catch (Exception e) {
            chars = 0;
        }
        try {
            voicetime = Long.parseLong(answer1[3]);
        } catch (Exception e) {
            voicetime = 0;
        }

        long hours = voicetime / 60;
        long minutes = voicetime % 60;
        long days = hours / 24;
        hours = hours % 24;

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(191, 255, 178));
        embed.setTitle("Statistiken f\u00fcr " + member.getUser().getAsTag());
        embed.setFooter("seit dem " + answer1[4], null);
        embed.setTimestamp(new CurrentDatetime().getCurrentTimestamp().toLocalDateTime().atZone(ZoneId.of("Europe/Berlin")));
        NumberFormat numberFormat = new DecimalFormat("###,###,###,###,###");
        embed.setDescription(
                        "Words: " + numberFormat.format(words) +
                        "\nMessages: " + numberFormat.format(msg) +
                        "\nCharacters: " + numberFormat.format(chars) +
                        "\nVoicetime: " + days + " days, " + hours + " hours, " + minutes + " minutes");
        event.getChannel(). sendMessageEmbeds(embed.build()).queue();

    }
}
