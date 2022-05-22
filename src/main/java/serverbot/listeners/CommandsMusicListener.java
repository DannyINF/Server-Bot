package serverbot.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import serverbot.server.ServerManagement;
import serverbot.util.SpringContextUtils;

public class CommandsMusicListener extends ListenerAdapter {

    public void onGuildMessageReceived(@NotNull MessageReceivedEvent event) {
        ServerManagement serverManagement = SpringContextUtils.getBean(ServerManagement.class);

        if (event.getMessage().getContentRaw().startsWith(serverManagement.findById(event.getGuild().getId()).get().getPrefix())) {

            /*try {
                CommandHandlerMusic.handleCommand(CommandParser.parser(event.getMessage().getContentRaw(), event));
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
    }
}
