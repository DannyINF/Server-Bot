package serverbot.commands;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import serverbot.channel.Channel;
import serverbot.channel.ChannelManagement;
import serverbot.channel.ChannelType;
import serverbot.core.MessageActions;
import serverbot.core.PermissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class CmdBan {

    @Autowired
    static ChannelManagement channelManagement;

    public static void ban(SlashCommandEvent event, User user, int del_days, String reason) {
        event.deferReply(true).queue();
        // only members with the ban permission are able to ban using this command
        if (PermissionChecker.checkPermission(new Permission[]{Permission.BAN_MEMBERS}, event.getMember())) {
            Streamable<Channel> streamableModlog = channelManagement.findByServerIdAndChannelType(event.getGuild().getId(), ChannelType.MODLOG);
            if (streamableModlog.isEmpty()) {
                MessageActions.needChannel(event, ChannelType.MODLOG);
            } else {
                TextChannel modlog = event.getGuild().getTextChannelById(streamableModlog.stream().findFirst().get().getChannelId());

                // setting up and sending the msg for the user
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.red);
                embed.setTitle(MessageActions.getLocalizedString("banned_msg_title", "user", event.getUser().getId()));
                embed.setDescription("**" + MessageActions.getLocalizedString("banned_msg_content", "user", event.getUser().getId()) + "**\n" + reason);
                user.openPrivateChannel().queue(channel ->
                        channel.sendMessageEmbeds(embed.build()).queue()
                );
                // setting up and sending the msg for the #modlog
                EmbedBuilder embed1 = new EmbedBuilder();
                embed1.setColor(Color.red);
                embed1.setTitle(MessageActions.getLocalizedString("banned_msg_title", "user", event.getUser().getId()));
                embed1.setDescription(MessageActions.getLocalizedString("log_mod_ban", "server", event.getGuild().getId())
                        .replace("[USER]", user.getAsTag()).replace("[REASON]", reason));
                assert modlog != null;
                modlog.sendMessageEmbeds(embed1.build()).queue(msg -> event.getGuild().ban(user, del_days, reason).queue());
            }
        } else {
            PermissionChecker.noPower(event);
        }
    }
}