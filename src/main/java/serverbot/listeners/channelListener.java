package serverbot.listeners;

import serverbot.core.messageActions;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;


public class channelListener extends ListenerAdapter {

    @Override
    public void onVoiceChannelUpdateName(@NotNull VoiceChannelUpdateNameEvent event) {
        event.getGuild().getTextChannelsByName(event.getOldName().replace(" ", "-"), true).get(0)
                .getManager().setName(event.getNewName()).setTopic(
                        messageActions.getLocalizedString("channel_chat_for", "server", event.getGuild().getId())
                .replace("[CHANNELNAME]", event.getChannel().getName())).queue();
    }
}