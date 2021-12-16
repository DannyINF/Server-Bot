package serverbot.commands;

import serverbot.channel.ChannelManagement;
import serverbot.channel.ChannelType;
import serverbot.core.messageActions;
import serverbot.core.permissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import serverbot.util.SpringContextUtils;
import serverbot.util.getUser;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class cmdKick implements Command {
    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        if (permissionChecker.checkPermission(new Permission[]{Permission.KICK_MEMBERS}, event.getMember())) {
            if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.KICK_MEMBERS)) {
                ArrayList<String> args2 = new ArrayList<>();
                args2.add(args[0]);
                String[] args3 = new String[args2.size()];
                args3 = args2.toArray(args3);
                Member member = getUser.getMemberFromInput(args3, event.getAuthor(), event.getGuild(), event.getChannel());
                assert member != null;
                User user = member.getUser();
                StringBuilder sb = new StringBuilder();
                int i = 1;
                while (i < args.length) {
                    sb.append(args[i]);
                    sb.append(" ");
                    i++;
                }
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.red);
                embed.setTitle(messageActions.getLocalizedString("kick_title", "user", event.getAuthor().getId()));
                embed.setDescription("**" + messageActions.getLocalizedString("kick_msg_priv",
                        "user", event.getAuthor().getId()) + "**\n" + sb.toString());
                user.openPrivateChannel().queue(channel ->
                        channel. sendMessageEmbeds(embed.build()).queue()
                );
                EmbedBuilder embed1 = new EmbedBuilder();
                embed1.setColor(Color.RED);
                embed1.setTitle(messageActions.getLocalizedString("kick_title", "user", event.getAuthor().getId()));
                embed1.setDescription(messageActions.getLocalizedString("kick_msg", "server", event.getGuild().getId())
                        .replace("[USER]", user.getAsMention()).replace("[REASON]", sb.toString()));

                ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);
                event.getGuild().getTextChannelById(channelManagement.findByServerIdAndChannelType(event.getGuild().getId(), ChannelType.MODLOG).stream().findFirst().get().getChannelId()).sendMessageEmbeds(embed1.build()).queue(msg -> event.getGuild().kick(user.getId(), sb.toString()).queue());
            }
        } else {
            permissionChecker.noPower(event.getChannel(), Objects.requireNonNull(event.getMember()));
        }
    }
}