package serverbot.commands;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import serverbot.channel.Channel;
import serverbot.channel.ChannelManagement;
import serverbot.channel.ChannelType;
import serverbot.util.SpringContextUtils;

import javax.swing.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
public class CmdChannel {

    public static void set(SlashCommandEvent event, ChannelType channelType, GuildChannel guildChannel) {
        event.deferReply(true).queue(); // Let the user know we received the command before doing anything else

        ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);
        channelManagement.save(new Channel(guildChannel.getId(), event.getGuild().getId(), channelType, 1.0f));
    }
}