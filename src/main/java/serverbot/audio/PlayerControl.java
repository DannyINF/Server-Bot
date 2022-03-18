package serverbot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import serverbot.commands.Command;
import serverbot.core.PermissionChecker;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.logging.Level;

public class PlayerControl implements Command {
    public static final int DEFAULT_VOLUME = 35; //(0 - 100, where 100 is default max volume)

    private final AudioPlayerManager playerManager;
    private final Map<String, GuildMusicManager> musicManagers;

    public PlayerControl() {
        java.util.logging.Logger.getLogger("org.apache.http.client.protocol.ResponseProcessCookies").setLevel(Level.OFF);

        this.playerManager = new DefaultAudioPlayerManager();
        playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        playerManager.registerSourceManager(new BandcampAudioSourceManager());
        playerManager.registerSourceManager(new VimeoAudioSourceManager());
        playerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
        playerManager.registerSourceManager(new HttpAudioSourceManager());
        playerManager.registerSourceManager(new LocalAudioSourceManager());

        musicManagers = new HashMap<>();
    }

    private static String getTimestamp(long milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

        if (hours > 0)
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        else
            return String.format("%02d:%02d", minutes, seconds);
    }

    //Prefix for all commands: /music
    //Example:  /play
    //Current commands
    // join [name]  - Joins a voice channel that has the provided name
    // join [id]    - Joins a voice channel based on the provided id.
    // leave        - Leaves the voice channel that the bot is currently in.
    // play         - Plays songs from the current queue. Starts playing again if it was previously paused
    // play [url]   - Adds a new song to the queue and starts playing if it wasn't playing already
    // pplay        - Adds a playlist to the queue and starts playing if not already playing
    // pause        - Pauses audio playback
    // stop         - Completely stops audio playback, skipping the current song.
    // skip         - Skips the current song, automatically starting the next
    // nowplaying   - Prints information about the currently playing song (title, current time)
    // list         - Lists the songs in the queue
    // volume [val] - Sets the volume of the MusicPlayer [10 - 100]
    // restart      - Restarts the current song or restarts the previous song if there is no current song playing.
    // repeat       - Makes the player repeat the currently playing song
    // reset        - Completely resets the player, fixing all errors and clearing the queue.
    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws Exception {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(33, 237, 90));
        embed.setTitle("Music");

        VoiceChannel userVoiceChannel = null;
        try {
            userVoiceChannel = Objects.requireNonNull(event.getMember().getVoiceState()).getChannel();
        } catch (Exception ignored) {
        }
        ArrayList<Member> music_addons = new ArrayList<>();
        ArrayList<Member> addon_available = new ArrayList<>();
        String[] ids_music = {"690321216258834525"};
        for (String id : ids_music) {
            try {
                Member m = event.getGuild().getMemberById(id);
                if (m != null) {
                    music_addons.add(m);
                }
            } catch (Exception ignored) {
            }
        }
        if (userVoiceChannel != null) {
            if (!music_addons.isEmpty()) {
                VoiceChannel botchannel = null;
                boolean inAction = false;
                try {
                    botchannel = Objects.requireNonNull(event.getGuild().getMemberById(event.getJDA().getSelfUser().getId()).getVoiceState()).getChannel();
                } catch (Exception ignored) {
                }
                if (botchannel != null) {
                    if (userVoiceChannel.equals(botchannel)) {
                        addon_available.add(event.getGuild().getMemberById(event.getJDA().getSelfUser().getId()));
                        inAction = true;
                    }
                }
                if (!inAction) {
                    for (Member m : music_addons) {
                        if (m.getOnlineStatus().equals(OnlineStatus.OFFLINE)) {
                            addon_available.add(addon_available.size(), m);
                        } else if (m.getOnlineStatus().equals(OnlineStatus.ONLINE)) {
                            if (Objects.requireNonNull(m.getVoiceState()).inVoiceChannel()) {
                                if (Objects.requireNonNull(m.getVoiceState().getChannel()).equals(userVoiceChannel)) {
                                    addon_available.add(0, m);
                                    inAction = true;
                                }
                            } else {
                                if (inAction) {
                                    addon_available.add(1, m);
                                } else {
                                    addon_available.add(0, m);
                                }
                            }
                        }
                    }
                }
                if (!addon_available.isEmpty()) {
                    switch (addon_available.get(0).getUser().getId()) {

                        /*case "441962292297596928":
                            embed.setFooter("Player: Server-Bot-BETA", null);
                            if (event.getJDA().getSelfUser().getId().equals("441962292297596928")) {
                                musicPlayer(event.getChannel(), event.getMember(), event.getMessage(), event.getGuild(), args, embed, userVoiceChannel);
                            }
                            break;*/
                        case "690321216258834525":
                            embed.setFooter("Player: Server-Bot", null);
                            if (event.getJDA().getSelfUser().getId().equals("690321216258834525")) {
                                musicPlayer(event.getChannel(), event.getMember(), event.getMessage(), event.getGuild(), args, embed, userVoiceChannel);
                            }
                            break;
                    }
                }
            }
        }
    }

    void musicPlayer(TextChannel channel, Member member, Message msg, Guild guild, String[] args, EmbedBuilder embed, VoiceChannel userVoiceChannel) {
        GuildMusicManager mng = getMusicManager(guild);
        AudioPlayer player = mng.player;
        TrackScheduler scheduler = mng.scheduler;
        switch (args[0].toLowerCase()) {
            case "join":
                if (args.length == 1) //No channel name was provided to search for.
                {
                    try {
                        guild.getAudioManager().openAudioConnection(Objects.requireNonNull(member.getVoiceState()).getChannel());
                    } catch (NullPointerException e) {
                        embed.setDescription("No channel name was provided to search with to join.");
                        channel. sendMessageEmbeds(embed.build()).queue();

                    }
                } else {
                    VoiceChannel chan = null;
                    try {
                        chan = guild.getVoiceChannelById(args[1]);
                    } catch (NumberFormatException ignored) {
                    }

                    if (chan == null)
                        chan = guild.getVoiceChannelsByName(args[1], true).stream().findFirst().orElse(null);
                    if (chan == null) {
                        embed.setDescription("Could not find VoiceChannel by name: " + args[1]);
                        channel. sendMessageEmbeds(embed.build()).queue();
                    } else {
                        guild.getAudioManager().setSendingHandler(mng.sendHandler);

                        try {
                            guild.getAudioManager().openAudioConnection(chan);
                        } catch (PermissionException e) {
                            if (e.getPermission() == Permission.VOICE_CONNECT) {
                                embed.setDescription("The bot does not have permission to connect to: " + chan.getName());
                                channel. sendMessageEmbeds(embed.build()).queue();
                            }
                        }
                    }
                }
                break;
            case "leave":
                guild.getAudioManager().setSendingHandler(null);
                guild.getAudioManager().closeAudioConnection();
                break;
            case "play":
                if (args.length == 1) //It is only the command to start playback (probably after pause)
                {
                    if (player.isPaused()) {
                        player.setPaused(false);
                        embed.setDescription("Playback as been resumed.");
                        channel. sendMessageEmbeds(embed.build()).queue();
                    } else if (player.getPlayingTrack() != null) {
                        embed.setDescription("Player is already playing!");
                        channel. sendMessageEmbeds(embed.build()).queue();
                    } else if (scheduler.queue.isEmpty()) {
                        embed.setDescription("The current audio queue is empty! Add something to the queue first!");
                        channel. sendMessageEmbeds(embed.build()).queue();
                    }
                } else    //Commands has 2 parts, play and url.
                {
                    if (Objects.requireNonNull(member.getVoiceState()).inVoiceChannel()) {
                        guild.getAudioManager().setSendingHandler(mng.sendHandler);
                        try {
                            guild.getAudioManager().openAudioConnection(userVoiceChannel);
                        } catch (PermissionException e) {
                            if (e.getPermission() == Permission.VOICE_CONNECT) {
                                assert userVoiceChannel != null;
                                embed.setDescription("Server-Bot does not have permission to connect to: " + userVoiceChannel.getName());
                                channel. sendMessageEmbeds(embed.build()).queue();
                            }
                        }
                    }
                    loadAndPlay(mng, channel, args[1], member.getUser(), false);
                }
                break;
            case "pplay":
                if (Objects.requireNonNull(member.getVoiceState()).inVoiceChannel()) {
                    guild.getAudioManager().setSendingHandler(mng.sendHandler);
                    try {
                        guild.getAudioManager().openAudioConnection(userVoiceChannel);
                    } catch (PermissionException e) {
                        if (e.getPermission() == Permission.VOICE_CONNECT) {
                            assert userVoiceChannel != null;
                            embed.setDescription("Server-Bot does not have permission to connect to: " + userVoiceChannel.getName());
                            channel. sendMessageEmbeds(embed.build()).queue();
                        }
                    }
                }
                if (args.length == 2) {
                    loadAndPlay(mng, channel, args[1], member.getUser(), true);
                } else {
                    embed.setDescription("A second argument is needed.");
                    channel. sendMessageEmbeds(embed.build()).queue();
                }
                break;
            case "skip":
                scheduler.nextTrack();
                embed.setDescription("The current track was skipped.");
                channel. sendMessageEmbeds(embed.build()).queue();
                break;
            case "pause":
                if (player.getPlayingTrack() == null) {
                    embed.setDescription("Cannot pause or resume player because no track is loaded for playing.");
                    channel. sendMessageEmbeds(embed.build()).queue();
                    return;
                }

                player.setPaused(!player.isPaused());
                if (player.isPaused()) {
                    embed.setDescription("The player has been paused.");
                    channel. sendMessageEmbeds(embed.build()).queue();
                } else {
                    embed.setDescription("The player has resumed playing.");
                    channel. sendMessageEmbeds(embed.build()).queue();
                }
                break;
            case "stop":
                Objects.requireNonNull(member.getVoiceState()).getChannel();
                Objects.requireNonNull(Objects.requireNonNull(guild.getMemberById(
                        guild.getJDA().getSelfUser().getId())).getVoiceState()).getChannel();
                scheduler.queue.clear();
                player.stopTrack();
                player.setPaused(false);
                embed.setDescription("Playback has been completely stopped and the queue has been cleared.");
                channel. sendMessageEmbeds(embed.build()).queue();
                break;
            case "volume":
                if (PermissionChecker.checkRole(new Role[]{guild.getRolesByName("YT-Team", true).get(0)}, member)) {
                    if (args.length == 1) {
                        embed.setDescription("Current player volume: **" + player.getVolume() + "**");
                        channel. sendMessageEmbeds(embed.build()).queue();
                    } else {
                        try {
                            int newVolume = Math.max(10, Math.min(100, Integer.parseInt(args[1])));
                            int oldVolume = player.getVolume();
                            player.setVolume(newVolume);
                            embed.setDescription("Player volume changed from `" + oldVolume + "` to `" + newVolume + "`");
                            channel. sendMessageEmbeds(embed.build()).queue();
                        } catch (NumberFormatException e) {
                            embed.setDescription("`" + args[1] + "` is not a valid integer. (10 - 100)");
                            channel. sendMessageEmbeds(embed.build()).queue();
                        }
                    }
                } else {
                    PermissionChecker.noPower(channel, member);
                }
                break;
            case "restart":
                AudioTrack track = player.getPlayingTrack();
                if (track == null)
                    track = scheduler.lastTrack;

                if (track != null) {
                    embed.setDescription("Restarting track: " + track.getInfo().title);
                    channel. sendMessageEmbeds(embed.build()).queue();
                    player.playTrack(track.makeClone());
                } else {
                    embed.setDescription("No track has been previously started, so the player cannot replay a track!");
                    channel. sendMessageEmbeds(embed.build()).queue();
                }
                break;
            case "repeat":
                scheduler.setRepeating(!scheduler.isRepeating());
                embed.setDescription("Player was set to: **" + (scheduler.isRepeating() ? "repeat" : "not repeat") + "**");
                channel. sendMessageEmbeds(embed.build()).queue();
                break;
            case "reset":
                synchronized (musicManagers) {
                    scheduler.queue.clear();
                    player.destroy();
                    guild.getAudioManager().setSendingHandler(null);
                    musicManagers.remove(guild.getId());
                }

                mng = getMusicManager(guild);
                guild.getAudioManager().setSendingHandler(mng.sendHandler);
                embed.setDescription("The player has been completely reset!");
                channel. sendMessageEmbeds(embed.build()).queue();
                break;
            case "info":
            case "nowplaying":
            case "track":
            case "current":
                AudioTrack currentTrack = player.getPlayingTrack();
                if (currentTrack != null) {
                    String title = currentTrack.getInfo().title;
                    String position = getTimestamp(currentTrack.getPosition());
                    String duration = getTimestamp(currentTrack.getDuration());

                    String nowplaying = String.format("**Playing:** %s\n**Time:** [%s / %s] added by **" + currentTrack.getUserData() + "**",
                            title, position, duration);
                    embed.setDescription(nowplaying);
                    channel. sendMessageEmbeds(embed.build()).queue();
                } else {
                    embed.setDescription("The player is not currently playing anything!");
                    channel. sendMessageEmbeds(embed.build()).queue();
                }
                break;
            case "list":
                Queue<AudioTrack> queue = scheduler.queue;
                synchronized (queue) {
                    if (queue.isEmpty()) {
                        embed.setDescription("The queue is currently empty!");
                        channel. sendMessageEmbeds(embed.build()).queue();
                    } else {
                        int trackCount = 0;
                        long queueLength = 0;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Current Queue: Entries: ").append(queue.size()).append("\n");
                        for (AudioTrack audioTrack : queue) {
                            queueLength += audioTrack.getDuration();
                            if (trackCount < 10) {
                                sb.append("`[").append(getTimestamp(audioTrack.getDuration())).append("]` ");
                                sb.append(audioTrack.getInfo().title).append("\n");
                                trackCount++;
                            }
                        }
                        sb.append("\n").append("Total Queue Time Length: ").append(getTimestamp(queueLength));

                        embed.setDescription(sb.toString());
                        channel. sendMessageEmbeds(embed.build()).queue();
                    }
                }
                break;
            case "shuffle":
                if (scheduler.queue.isEmpty()) {
                    embed.setDescription("The queue is currently empty!");
                    channel. sendMessageEmbeds(embed.build()).queue();
                    return;
                }

                scheduler.shuffle();
                embed.setDescription("The queue has been shuffled!");
                channel. sendMessageEmbeds(embed.build()).queue();
                break;
        }
        try {
            msg.delete().queue();
        } catch (Exception ignored) {
        }

    }

    private void loadAndPlay(GuildMusicManager mng, final MessageChannel channel, String url, User user, final boolean addPlaylist) {
        final String trackUrl;

        //Strip <>'s that prevent discord from embedding link resources
        if (url.startsWith("<") && url.endsWith(">"))
            trackUrl = url.substring(1, url.length() - 1);
        else
            trackUrl = url;

        playerManager.loadItemOrdered(mng, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                track.setUserData(user.getAsTag());
                String msg = "Adding to queue: " + track.getInfo().title + "\nAdded by **" + track.getUserData() + "**";
                if (mng.player.getPlayingTrack() == null)
                    msg += "\nand the Player has started playing;";

                mng.scheduler.queue(track);
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(new Color(33, 237, 90));
                embed.setTitle("Music");
                embed.setDescription(msg);
                channel. sendMessageEmbeds(embed.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();
                List<AudioTrack> tracks = playlist.getTracks();


                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                if (addPlaylist) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(new Color(33, 237, 90));
                    embed.setTitle("Music");
                    embed.setDescription("Adding **" + playlist.getTracks().size() + "** tracks to queue from playlist: " + playlist.getName());
                    channel. sendMessageEmbeds(embed.build()).queue();
                    tracks.forEach(mng.scheduler::queue);
                } else {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(new Color(33, 237, 90));
                    embed.setTitle("Music");
                    embed.setDescription("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")");
                    channel. sendMessageEmbeds(embed.build()).queue();
                    mng.scheduler.queue(firstTrack);
                }
            }

            @Override
            public void noMatches() {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(new Color(33, 237, 90));
                embed.setTitle("Music");
                embed.setDescription("Nothing found by " + trackUrl);
                channel. sendMessageEmbeds(embed.build()).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(new Color(33, 237, 90));
                embed.setTitle("Music");
                embed.setDescription("Could not play: " + exception.getMessage());
                channel. sendMessageEmbeds(embed.build()).queue();
            }
        });
    }

    private GuildMusicManager getMusicManager(Guild guild) {
        String guildId = guild.getId();
        GuildMusicManager mng = musicManagers.get(guildId);
        if (mng == null) {
            synchronized (musicManagers) {
                mng = musicManagers.get(guildId);
                if (mng == null) {
                    mng = new GuildMusicManager(playerManager);
                    mng.player.setVolume(DEFAULT_VOLUME);
                    musicManagers.put(guildId, mng);
                }
            }
        }
        return mng;
    }

    @Override
    public boolean called() {
        return false;
    }
}
