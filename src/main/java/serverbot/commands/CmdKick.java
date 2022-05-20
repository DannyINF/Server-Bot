package serverbot.commands;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.springframework.data.util.Streamable;
import serverbot.channel.Channel;
import serverbot.channel.ChannelManagement;
import serverbot.channel.ChannelType;
import serverbot.core.MessageActions;
import serverbot.core.PermissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import serverbot.moderation.Moderation;
import serverbot.moderation.ModerationManagement;
import serverbot.moderation.ModerationType;
import serverbot.util.SpringContextUtils;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Objects;

public class CmdKick {

    public static void kick(SlashCommandEvent event) {

        ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);

        if (PermissionChecker.checkPermission(new Permission[]{Permission.KICK_MEMBERS}, event.getMember())) {
            Streamable<Channel> streamableModlog = channelManagement.findByServerIdAndChannelType(event.getGuild().getId(), ChannelType.MODLOG);
            if (streamableModlog.isEmpty()) {
                MessageActions.needChannel(event, ChannelType.MODLOG);
            } else {
                TextChannel modlog = event.getGuild().getTextChannelById(streamableModlog.stream().findFirst().get().getChannelId());
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.KICK_MEMBERS)) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(Color.red);
                    embed.setTitle(MessageActions.getLocalizedString("kick_title", "user", event.getUser().getId()));
                    embed.setDescription("**" + MessageActions.getLocalizedString("kick_msg_priv", "user", event.getUser().getId()) + "**\n" + event.getOption("kick_reason").getAsString());
                    event.getOption("kick_user").getAsUser().openPrivateChannel().queue(channel ->
                            channel.sendMessageEmbeds(embed.build()).queue()
                    );
                    EmbedBuilder embed1 = new EmbedBuilder();
                    embed1.setColor(Color.RED);
                    embed1.setTitle(MessageActions.getLocalizedString("kick_title", "user", event.getUser().getId()));
                    embed1.setDescription(MessageActions.getLocalizedString("kick_msg", "server", event.getGuild().getId())
                            .replace("[USER]", event.getOption("kick_user").getAsUser().getAsMention()).replace("[REASON]", event.getOption("kick_reason").getAsString()));

                    User user = event.getOption("kick_user").getAsUser();
                    String reason = "";
                    if (event.getOption("kick_reason") != null) {
                        reason = event.getOption("kick_reason").getAsString();
                    }

                    String finalReason = reason;
                    event.getGuild().getTextChannelById(channelManagement.findByServerIdAndChannelType(event.getGuild().getId(), ChannelType.MODLOG).stream().findFirst().get().getChannelId()).sendMessageEmbeds(embed1.build()).queue(msg -> event.getGuild().kick(user.getId(), finalReason).queue());

                    ModerationManagement moderationManagement = SpringContextUtils.getBean(ModerationManagement.class);
                    moderationManagement.save(new Moderation(LocalDateTime.now(), user.getId(), event.getGuild().getId(), event.getUser().getId(),
                            ModerationType.KICK, 0L, false, reason));
                }
            }
        } else {
            PermissionChecker.noPower(event);
        }
    }
}