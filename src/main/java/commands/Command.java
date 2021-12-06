package commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface Command {

    boolean called();

    default void action(String[] args, GuildMessageReceivedEvent event) throws Exception {

    }
}