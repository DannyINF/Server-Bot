package serverbot.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.DriverManager;

public class cmdShutdown implements Command {
    @Override
    public boolean called() {
        return false;
    }


    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        if (event.getAuthor().getId().equals("277746420281507841")) {
            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (Exception e) {
                event.getJDA().shutdown();
            }
        }
    }
}
