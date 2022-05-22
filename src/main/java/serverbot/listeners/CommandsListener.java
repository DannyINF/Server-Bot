package serverbot.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import serverbot.channel.ChannelManagement;
import serverbot.channel.ChannelType;
import serverbot.server.ServerManagement;
import serverbot.util.STATIC;
import serverbot.util.SpringContextUtils;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CommandsListener extends ListenerAdapter {

    public void onGuildMessageReceived(@NotNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        ServerManagement serverManagement = SpringContextUtils.getBean(ServerManagement.class);
        ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);

        if (message.startsWith(serverManagement.findById(event.getGuild().getId()).get().getPrefix())) {

            if (!channelManagement.findByChannelId(event.getChannel().getId()).get().getChannelType().equals(ChannelType.SPAM) && !event.getAuthor().equals(event.getJDA().getSelfUser())) {
                if (STATIC.addCommandSpammer(event.getAuthor().getId()) == 0)
                    event.getChannel().sendMessage(Objects.requireNonNull(event.getMember()).getAsMention() + ", bitte f\u00fchre Befehle nur im Channel " + event.getGuild().getTextChannelById(channelManagement.findByServerIdAndChannelType(event.getGuild().getId(), ChannelType.SPAM).get().findFirst().get().getChannelId()).getAsMention() + " aus!").queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
            }

            try {
                if (!message.startsWith(serverManagement.findById(event.getGuild().getId()).get().getPrefix() + "music"))
                    event.getMessage().delete().queue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}