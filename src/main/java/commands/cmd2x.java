package commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import util.STATIC;

public class cmd2x implements Command {
    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        if (event.getAuthor().getId().equals("277746420281507841"))
            event.getChannel().sendMessage(">>> Das **Double-XP-Event** wurde " + (STATIC.toggle2x() ? "gestartet." : "beendet.")).queue();
    }
}
