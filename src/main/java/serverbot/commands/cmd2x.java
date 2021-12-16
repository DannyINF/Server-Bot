package serverbot.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import serverbot.server.ServerManagement;
import serverbot.util.STATIC;
import serverbot.util.SpringContextUtils;

public class cmd2x implements Command {
    @Override
    public boolean called() {
        return false;
    }

    @Override
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