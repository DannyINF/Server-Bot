package serverbot.listeners;

import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateNameEvent;
import serverbot.channel.Channel;
import serverbot.channel.ChannelManagement;
import serverbot.channel.ChannelType;
import serverbot.core.MessageActions;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import serverbot.util.SpringContextUtils;


public class ChannelListener extends ListenerAdapter {

    @Override
    public void onChannelUpdateName(@NotNull ChannelUpdateNameEvent event) {
        if (event.getChannel().getType().isAudio()) {
            event.getGuild().getTextChannelsByName(event.getOldValue().replace(" ", "-"), true).get(0)
                    .getManager().setName(event.getNewValue()).setTopic(
                    MessageActions.getLocalizedString("channel_chat_for", "server", event.getGuild().getId())
                            .replace("[CHANNELNAME]", event.getChannel().getName())).queue();
            event.getGuild().getRolesByName(event.getOldValue(), true).get(0).getManager().setName(event.getNewValue()).queue();
        }
    }

    @Override
    public void onChannelCreate(@NotNull ChannelCreateEvent event) {
        ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);
        switch (event.getChannel().getType()) {
            case TEXT:
            case NEWS:
                channelManagement.save(new Channel(event.getChannel().getId(), event.getGuild().getId(), ChannelType.DEFAULT_TEXT, 1D));
                break;
            case VOICE:
            case STAGE:
                channelManagement.save(new Channel(event.getChannel().getId(), event.getGuild().getId(), ChannelType.DEFAULT_VOICE, 1D));
                break;
            case CATEGORY:
                channelManagement.save(new Channel(event.getChannel().getId(), event.getGuild().getId(), ChannelType.DEFAULT_CATEGORY, 1D));
                break;
        }
    }

    @Override
    public void onChannelDelete(@NotNull ChannelDeleteEvent event) {
        ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);
        channelManagement.delete(channelManagement.findByChannelIdAndServerId(event.getChannel().getId(), event.getGuild().getId()).get());

        if (event.getChannel().getType().isAudio()) {
            event.getGuild().getRolesByName(event.getChannel().getName(), true).get(0).delete().queue();
            try {
                event.getGuild().getTextChannelsByName(event.getChannel().getName(), true).get(0).delete().queue();
            } catch (Exception ignored) {}
        }
    }
}