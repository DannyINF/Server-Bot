package serverbot.commands;

import serverbot.channel.ChannelManagement;
import serverbot.channel.ChannelType;
import serverbot.core.MessageActions;
import serverbot.core.PermissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import serverbot.moderation.Moderation;
import serverbot.moderation.ModerationManagement;
import serverbot.moderation.ModerationType;
import serverbot.util.SpringContextUtils;
import serverbot.util.GetUser;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class CmdKick implements Command {
    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        if (PermissionChecker.checkPermission(new Permission[]{Permission.KICK_MEMBERS}, event.getMember())) {
            if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.KICK_MEMBERS)) {
                ArrayList<String> args2 = new ArrayList<>();
                args2.add(args[0]);
                String[] args3 = new String[args2.size()];
                args3 = args2.toArray(args3);
                Member member = GetUser.getMemberFromInput(args3, event.getAuthor(), event.getGuild(), event.getChannel());
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
                embed.setTitle(MessageActions.getLocalizedString("kick_title", "user", user.getId()));
                embed.setDescription("**" + MessageActions.getLocalizedString("kick_msg_priv",
                        "user", user.getId()) + "**\n" + sb.toString());
                user.openPrivateChannel().queue(channel ->
                        channel.sendMessageEmbeds(embed.build()).queue()
                );
                EmbedBuilder embed1 = new EmbedBuilder();
                embed1.setColor(Color.RED);
                embed1.setTitle(MessageActions.getLocalizedString("kick_title", "user", event.getAuthor().getId()));
                embed1.setDescription(MessageActions.getLocalizedString("kick_msg", "server", event.getGuild().getId())
                        .replace("[USER]", user.getAsMention()).replace("[REASON]", sb.toString()));

                ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);
                event.getGuild().getTextChannelById(channelManagement.findByServerIdAndChannelType(event.getGuild().getId(), ChannelType.MODLOG).stream().findFirst().get().getChannelId()).sendMessageEmbeds(embed1.build()).queue(msg -> event.getGuild().kick(user.getId(), sb.toString()).queue());

                ModerationManagement moderationManagement = SpringContextUtils.getBean(ModerationManagement.class);
                moderationManagement.save(new Moderation(LocalDateTime.now(), user.getId(), event.getGuild().getId(), event.getAuthor().getId(),
                        ModerationType.KICK, 0L, false, sb.toString()));
            }
        } else {
            PermissionChecker.noPower(event.getChannel(), Objects.requireNonNull(event.getMember()));
        }
    }
}