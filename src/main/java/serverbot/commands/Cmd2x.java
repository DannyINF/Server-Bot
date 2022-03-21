package serverbot.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import serverbot.server.ServerManagement;
import serverbot.util.SpringContextUtils;

public class Cmd2x {

    public static void TwoX(SlashCommandEvent event) {
        if (event.getMember().getId().equals("277746420281507841")) {
            OptionMapping option = event.getOption("2x_amount");
            long number;
            if (option == null) {
                number = 2;
            } else {
                number = option.getAsLong();
            }
            event.reply(">>> Das **" + (number != 1 ? number + "-fach-" : "") + "XP-Event** wurde " + (number != 1 ? "gestartet." : "beendet."))
                    .queue();
            ServerManagement serverManagement = SpringContextUtils.getBean(ServerManagement.class);
            serverManagement.changeXpMultiplierTo(event.getGuild().getId(), number);
        } else {
            event.reply(">>> Du besitzt nicht die benötigten Berechtigungen.").queue();
        }
    }
}