package serverbot.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import serverbot.channel.Channel;
import serverbot.channel.ChannelManagement;
import serverbot.channel.ChannelType;
import serverbot.role.Role;
import serverbot.role.RoleType;
import serverbot.util.SpringContextUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CmdChannel implements Command {
    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws Exception {
        String action = args[0];
        String id = args[1];
        ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);
        //TODO: add "show"
        switch (action) {
            case "create":
            case "add":
                channelManagement.save(new Channel(id, event.getGuild().getId(), ChannelType.valueOf(args[2]), 1F));
                break;
            case "delete":
            case "remove":
                channelManagement.delete(channelManagement.findById(id).get());
                break;
            case "edit":
                Channel editChannel = channelManagement.findById(id).get();
                editChannel.setChannelType(ChannelType.valueOf(args[2]));
                channelManagement.save(editChannel);
                break;
            case "show":
                StringBuilder stringBuilder = new StringBuilder();
                if (Stream.of(ChannelType.values())
                        .map(Enum::name)
                        .collect(Collectors.toList()).contains(id)) {
                    for (Channel channel : channelManagement.findByServerIdAndChannelType(event.getGuild().getId(), ChannelType.valueOf(id))) {
                        stringBuilder.append(event.getGuild().getTextChannelById(channel.getChannelId()).getName());
                        stringBuilder.append(" ");
                    }
                } else {
                    stringBuilder.append(channelManagement.findById(id).get().getChannelType());
                }
                event.getChannel().sendMessage(stringBuilder.toString()).queue();
                break;
        }
    }
}
