package listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class emptyChannelListener extends ListenerAdapter {

    //TODO: iclude this into music function

    private static void shouldLeaveFunction(Guild g, VoiceChannel c) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (Objects.requireNonNull(g.getSelfMember().getVoiceState()).inVoiceChannel() && Objects.equals(g.getSelfMember().getVoiceState().getChannel(), c)) {
                    boolean shouldLeave = true;
                    for (Member member : c.getMembers()) {
                        if (!member.getUser().isBot()) {
                            shouldLeave = false;
                        }
                    }
                    if (shouldLeave) {
                        g.getAudioManager().setSendingHandler(null);
                        g.getAudioManager().closeAudioConnection();
                    }
                }
            }
        }, 300000);
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        if (Objects.requireNonNull(event.getGuild().getSelfMember().getVoiceState()).inVoiceChannel() && Objects.equals(event.getGuild().getSelfMember().getVoiceState().getChannel(), event.getChannelLeft())) {
            boolean shouldLeave = true;
            for (Member member : event.getChannelLeft().getMembers()) {
                if (!member.getUser().isBot()) {
                    shouldLeave = false;
                }
            }
            if (shouldLeave) {
                shouldLeaveFunction(event.getGuild(), event.getChannelLeft());
            }
        }
    }

    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {
        if (Objects.requireNonNull(event.getGuild().getSelfMember().getVoiceState()).inVoiceChannel() && Objects.equals(event.getGuild().getSelfMember().getVoiceState().getChannel(), event.getChannelLeft())) {
            boolean shouldLeave = true;
            for (Member member : event.getChannelLeft().getMembers()) {
                if (!member.getUser().isBot()) {
                    shouldLeave = false;
                }
            }
            if (shouldLeave) {
                shouldLeaveFunction(event.getGuild(), event.getChannelLeft());
            }
        }
    }
}
