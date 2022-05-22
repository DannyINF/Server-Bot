package serverbot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Command {

    boolean called();

    default void action(String[] args, MessageReceivedEvent event) throws Exception {

    }
}