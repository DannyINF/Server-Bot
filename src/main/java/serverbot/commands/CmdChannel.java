package serverbot.commands;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import serverbot.channel.Channel;
import serverbot.channel.ChannelManagement;
import serverbot.channel.ChannelType;
import serverbot.util.SpringContextUtils;

public class CmdChannel {

    public static void set(SlashCommandInteractionEvent event, ChannelType channelType, GuildChannel guildChannel) {
        event.reply("Der Channel **" + guildChannel.getAsMention() + "** hat nun den Typ **" + channelType + "**.").queue(); // Let the user know we received the command before doing anything else

        ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);
        Channel channel = channelManagement.findByChannelId(guildChannel.getId()).get();
        channel.setChannelType(channelType);
        channelManagement.save(channel);
    }

    public static void changeXpMultiplier(SlashCommandInteractionEvent event, Double xpMultiplier, GuildChannel guildChannel) {
        event.reply(String.format("Der Xp-Multiplier des Channels **%s** beträgt nun **%.2f**.", guildChannel.getName(), xpMultiplier)).queue();

        ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);
        Channel channel = channelManagement.findByChannelId(guildChannel.getId()).get();
        channel.setXpMultiplier(xpMultiplier);
        channelManagement.save(channel);
    }
}