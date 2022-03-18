package serverbot.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import serverbot.server.ServerManagement;
import serverbot.util.SpringContextUtils;

public class Cmd2x {

    public static void TwoX(SlashCommandEvent event) {
        if (event.getMember().getId().equals("277746420281507841")) {
            long number = event.getOption("2x_amount").getAsLong();
            event.reply(">>> Das **" + (number != 1 ? number + "-fach-" : "") + "XP-Event** wurde " + (number != 1 ? "gestartet." : "beendet."))
                    .queue();
            ServerManagement serverManagement = SpringContextUtils.getBean(ServerManagement.class);
            serverManagement.changeXpMultiplierTo(event.getGuild().getId(), number);
        }
    }

    public void action(String[] args, GuildMessageReceivedEvent event) {
        if (event.getAuthor().getId().equals("277746420281507841")) {
            int number = Integer.parseInt(args[0]);
            event.getChannel().sendMessage(
                            ">>> Das **" + (number != 1 ? number + "-fach-" : "") + "XP-Event** wurde " + (number != 1 ? "gestartet." : "beendet."))
                    .queue();
            ServerManagement serverManagement = SpringContextUtils.getBean(ServerManagement.class);
            serverManagement.changeXpMultiplierTo(event.getGuild().getId(), number);
        }
    }
}