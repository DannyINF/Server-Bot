package listeners;

import core.messageActions;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.SET_CHANNEL;


public class channelListener extends ListenerAdapter {

    @Override
    public void onVoiceChannelUpdateName(@NotNull VoiceChannelUpdateNameEvent event) {
        event.getGuild().getTextChannelsByName(event.getOldName().replace(" ", "-"), true).get(0)
                .getManager().setName(event.getNewName()).setTopic(messageActions.getLocalizedString("channel_chat_for", "server", event.getGuild().getId())
                .replace("[CHANNELNAME]", event.getChannel().getName())).queue();
    }
}