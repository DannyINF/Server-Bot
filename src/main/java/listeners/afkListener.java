package listeners;

import audio.AudioPlayerSendHandler;
import audio.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.STATIC;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class afkListener extends ListenerAdapter {
    private static final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();
    private static final Map<Guild, Map.Entry<AudioPlayer, TrackScheduler>> PLAYERS = new HashMap<>();
    private static Guild guild;

    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {
        if (event.getChannelJoined().equals(event.getGuild().getAfkChannel()) && event.getMember().getUser().getId().equals(STATIC.BOT_ID)) {
            guild = event.getGuild();
            skip(guild);
            guild.getAudioManager().closeAudioConnection();
        }

    }

    private void skip(Guild g) {
        getPlayer(g).stopTrack();
    }

    private AudioPlayer getPlayer(Guild g) {
        if (hasPlayer(g))
            return PLAYERS.get(g).getKey();
        else
            return createPlayer(g);
    }

    private boolean hasPlayer(Guild g) {
        return PLAYERS.containsKey(g);
    }

    private AudioPlayer createPlayer(Guild g) {
        AudioPlayer p = MANAGER.createPlayer();
        TrackScheduler m = new TrackScheduler(p);
        p.addListener(m);

        guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(p));

        PLAYERS.put(g, new AbstractMap.SimpleEntry<>(p, m));

        return p;
    }
}
