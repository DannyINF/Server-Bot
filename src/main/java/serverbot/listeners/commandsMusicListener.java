package serverbot.listeners;

import serverbot.core.commandHandlerMusic;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import serverbot.core.commandParser;
import serverbot.server.ServerManagement;
import serverbot.util.SpringContextUtils;

public class commandsMusicListener extends ListenerAdapter {

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        ServerManagement serverManagement = SpringContextUtils.getBean(ServerManagement.class);

        if (event.getMessage().getContentRaw().startsWith(serverManagement.findById(event.getGuild().getId()).get().getPrefix())) {

            try {
                commandHandlerMusic.handleCommand(commandParser.parser(event.getMessage().getContentRaw(), event));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
