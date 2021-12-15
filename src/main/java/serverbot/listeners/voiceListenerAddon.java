package serverbot.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Objects;

public class voiceListenerAddon extends ListenerAdapter {
    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        if (event.getGuild().getAudioManager().isConnected()) {
            if (event.getChannelLeft().equals(event.getGuild().getAudioManager().getConnectedChannel())) {
                boolean onlyBotsHere = true;
                boolean onlyBotsEverywhere = true;
                for (Guild g : event.getJDA().getGuilds()) {
                    if (g.getAudioManager().isConnected()) {
                        for (Member m : Objects.requireNonNull(g.getAudioManager().getConnectedChannel()).getMembers()) {
                            if (!m.getUser().isBot()) {
                                onlyBotsEverywhere = false;
                                if (g.equals(event.getGuild())) {
                                    onlyBotsHere = false;
                                }
                            }
                        }
                    }
                }

                if (onlyBotsEverywhere) {
                    event.getJDA().shutdown();
                } else if (onlyBotsHere) {
                    event.getGuild().getAudioManager().setSendingHandler(null);
                    event.getGuild().getAudioManager().closeAudioConnection();
                }
            }
        }
    }

    @Override
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent event) {
        if (event.getGuild().getAudioManager().isConnected()) {
            if (event.getChannelLeft().equals(event.getGuild().getAudioManager().getConnectedChannel())) {
                boolean onlyBotsHere = true;
                boolean onlyBotsEverywhere = true;
                for (Guild g : event.getJDA().getGuilds()) {
                    if (g.getAudioManager().isConnected()) {
                        for (Member m : Objects.requireNonNull(g.getAudioManager().getConnectedChannel()).getMembers()) {
                            if (!m.getUser().isBot()) {
                                onlyBotsEverywhere = false;
                                if (g.equals(event.getGuild())) {
                                    onlyBotsHere = false;
                                }
                            }
                        }
                    }
                }

                if (onlyBotsEverywhere) {
                    event.getJDA().shutdown();
                } else if (onlyBotsHere) {
                    event.getGuild().getAudioManager().setSendingHandler(null);
                    event.getGuild().getAudioManager().closeAudioConnection();
                }
            } else if (event.getChannelJoined().equals(event.getGuild().getAudioManager().getConnectedChannel())){
                boolean onlyBotsHere = true;
                boolean onlyBotsEverywhere = true;
                for (Guild g : event.getJDA().getGuilds()) {
                    if (g.getAudioManager().isConnected()) {
                        for (Member m : Objects.requireNonNull(g.getAudioManager().getConnectedChannel()).getMembers()) {
                            if (!m.getUser().isBot()) {
                                onlyBotsEverywhere = false;
                                if (g.equals(event.getGuild())) {
                                    onlyBotsHere = false;
                                }
                            }
                        }
                    }
                }

                if (onlyBotsEverywhere) {
                    event.getJDA().shutdown();
                } else if (onlyBotsHere) {
                    event.getGuild().getAudioManager().setSendingHandler(null);
                    event.getGuild().getAudioManager().closeAudioConnection();
                }
            }
        }
    }
}
