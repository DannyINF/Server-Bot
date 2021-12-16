package serverbot.listeners;

import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import serverbot.channel.Channel;
import serverbot.channel.ChannelManagement;
import serverbot.channel.ChannelType;
import serverbot.core.messageActions;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import serverbot.util.SpringContextUtils;


public class channelListener extends ListenerAdapter {

    @Override
    public void onVoiceChannelUpdateName(@NotNull VoiceChannelUpdateNameEvent event) {
        event.getGuild().getTextChannelsByName(event.getOldName().replace(" ", "-"), true).get(0)
                .getManager().setName(event.getNewName()).setTopic(
                        messageActions.getLocalizedString("channel_chat_for", "server", event.getGuild().getId())
                .replace("[CHANNELNAME]", event.getChannel().getName())).queue();
    }

    @Override
    public void onTextChannelCreate(@NotNull TextChannelCreateEvent event) {
        ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);
        channelManagement.save(new Channel(event.getChannel().getId(), event.getGuild().getId(), ChannelType.DEFAULT_TEXT, 1F));
    }

    @Override
    public void onVoiceChannelCreate(@NotNull VoiceChannelCreateEvent event) {
        ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);
        channelManagement.save(new Channel(event.getChannel().getId(), event.getGuild().getId(), ChannelType.DEFAULT_VOICE, 1F));
    }

    @Override
    public void onTextChannelDelete(@NotNull TextChannelDeleteEvent event) {
        ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);
        channelManagement.delete(channelManagement.findByChannelIdAndServerId(event.getChannel().getId(), event.getGuild().getId()).get());
    }

    @Override
    public void onVoiceChannelDelete(@NotNull VoiceChannelDeleteEvent event) {
        ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);
        channelManagement.delete(channelManagement.findByChannelIdAndServerId(event.getChannel().getId(), event.getGuild().getId()).get());
    }
}