package serverbot.listeners;

import serverbot.core.commandHandlerMusic;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import serverbot.core.commandParser;
import serverbot.util.STATIC;

public class commandsMusicListener extends ListenerAdapter {

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        if (event.getMessage().getContentRaw().startsWith(STATIC.PREFIX)) {

            try {
                commandHandlerMusic.handleCommand(commandParser.parser(event.getMessage().getContentRaw(), event));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
