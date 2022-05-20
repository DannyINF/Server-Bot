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
        event.reply("Der Channel **" + guildChannel.getAsMention() + "** hat nun den Typ **" + channelType + "**.").queue(); // Let the user know we received the command before doing anything else

        ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);
        Channel channel = channelManagement.findByChannelId(guildChannel.getId()).get();
        channel.setChannelType(channelType);
        channelManagement.save(channel);
    }
}