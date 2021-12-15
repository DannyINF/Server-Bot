package serverbot.listeners;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import serverbot.core.commandHandler;
import serverbot.core.commandParser;
import serverbot.core.permissionChecker;
import serverbot.util.STATIC;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class commandsListener extends ListenerAdapter {

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        if (message.startsWith(STATIC.PREFIX)) {

            try {
                commandHandler.handleCommand(commandParser.parser(message, event));
            } catch (Exception e) {
                e.printStackTrace();
            }



            if (!event.getChannel().getName().toLowerCase().contains("spam") && !event.getAuthor().equals(event.getJDA().getSelfUser())) {
                if (STATIC.addCommandSpammer(event.getAuthor().getId()) == 0)
                    event.getChannel().sendMessage(Objects.requireNonNull(event.getMember()).getAsMention() + ", bitte f\u00fchre Befehle nur im Channel " + Objects.requireNonNull(event.getGuild().getTextChannelById("492410527302156318")).getAsMention() + " aus!").queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
            }

            try {
                if (!message.startsWith(STATIC.PREFIX + "music"))
                    event.getMessage().delete().queue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}