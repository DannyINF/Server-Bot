package serverbot.listeners;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.CombinedAudio;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Collections;
import java.util.Objects;

import static java.awt.SystemColor.text;

public class VoiceListener extends ListenerAdapter implements AudioReceiveHandler {
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        if (event.getChannelJoined().getType().equals(ChannelType.VOICE)) {
            if (event.getGuild().getRolesByName(event.getChannelJoined().getName(), true).isEmpty()) {
                event.getGuild().createRole().setColor(Color.LIGHT_GRAY).setName(event.getChannelJoined().getName())
                        .setMentionable(true).setHoisted(false).queue();
            }
            Permission[] voicepermission = new Permission[]{Permission.MESSAGE_HISTORY, Permission.MESSAGE_SEND, Permission.VIEW_CHANNEL};

            if (event.getGuild().getTextChannelsByName(event.getChannelJoined().getName().replace(" ", "-"), true).isEmpty()) {
                event.getGuild().getCategoriesByName(event.getGuild().getVoiceChannelById(event.getChannelJoined().getId()).getParentCategory().getName(), true).get(0)
                        .createTextChannel(event.getChannelJoined().getName()).setTopic("Chat zu " + event.getChannelJoined().getName()).queue(textChannel -> {
                    textChannel.upsertPermissionOverride(event.getGuild().getPublicRole())
                            .setDenied(Permission.ALL_TEXT_PERMISSIONS).queue();
                    textChannel.upsertPermissionOverride(event.getGuild().getRolesByName(event.getChannelJoined().getName(), true).get(0))
                            .setAllowed(voicepermission).queue();
                });

            }
            event.getGuild().addRoleToMember(event.getMember(), event.getGuild()
                    .getRolesByName(event.getChannelJoined().getName(), true).get(0)).queue();
        }

    }

    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        if (event.getGuild().getAudioManager().isConnected()) {
            if (event.getChannelLeft().equals(event.getGuild().getAudioManager().getConnectedChannel())) {
                boolean onlyBots = true;
                for (Member m : event.getChannelLeft().getMembers()) {
                    if (!m.getUser().isBot()) {
                        onlyBots = false;
                    }
                }
                if (onlyBots) {
                    event.getGuild().getAudioManager().setSendingHandler(null);
                    event.getGuild().getAudioManager().closeAudioConnection();
                }
            }
        }
        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRolesByName(event.getChannelLeft().getName(), true).get(0)).queue();
    }

    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {
        event.getGuild().addRoleToMember(event.getMember(), event.getGuild()
                .getRolesByName(event.getChannelJoined().getName(), true).get(0)).queue();
        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRolesByName(event.getChannelLeft().getName(), true).get(0)).queue();
    }


    @Override
    public boolean canReceiveCombined() {
        return false;
    }

    @Override
    public boolean canReceiveUser() {
        return false;
    }

    @Override
    public void handleCombinedAudio(@NotNull CombinedAudio combinedAudio) { }

    @Override
    public void handleUserAudio(@NotNull UserAudio userAudio) { }
}
