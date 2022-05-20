package serverbot.core;

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
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import serverbot.audio.GuildMusicManager;
import serverbot.audio.TrackScheduler;
import serverbot.channel.ChannelType;
import serverbot.commands.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.logging.Level;

public class SlashCommandHandler extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        // Only accept commands from guilds
        if (event.getGuild() == null)
            return;

        try {
            switch (event.getName()) {
                case "2x":
                    Cmd2x.TwoX(event);
                    break;
                case "ban":
                    User user = event.getOption("ban_user").getAsUser();
                    int del_days = (int) event.getOption("ban_del_days").getAsLong();
                    String reason = event.getOption("ban_reason").getAsString();
                    CmdBan.ban(event, user, del_days, reason);
                    break;
                case "botinfo":
                    CmdBotinfo.botinfo(event);
                    break;
                case "kick":
                    CmdKick.kick(event);
                    break;
                case "credits":
                    switch (event.getSubcommandName()) {
                        case "get":
                            CmdCredits.get(event, event.getOption("credits_user").getAsMember());
                            break;
                        case "give":
                            CmdCredits.give(event, event.getOption("credits_give_amount").getAsLong(), event.getOption("credits_give_user").getAsUser());
                            break;
                        case "gift":
                            CmdCredits.gift(event, event.getOption("credits_gift_amount").getAsLong(), event.getOption("credits_gift_user").getAsUser());
                            break;
                    }
                    break;
                case "exil":
                    CmdExil.exile(event, event.getOption("exil_user").getAsMember(), event.getOption("exil_reason"));
                    break;
                case "stats":
                    CmdStats.stats(event, event.getOption("stats_user").getAsMember());
                    break;
                case "xp":
                    switch (event.getSubcommandName()) {
                        case "ranking":
                            CmdXp.ranking(event);
                            break;
                        case "give":
                            CmdXp.give(event, event.getOption("xp_give_amount").getAsLong(), event.getOption("xp_give_user").getAsMember());
                            break;
                        case "next":
                            CmdXp.next(event, event.getOption("xp_next_user").getAsMember());
                            break;
                        case "get":
                            CmdXp.get(event, event.getOption("xp_user").getAsMember());
                            break;
                    }
                case "say":
                    CmdSay.say(event, event.getOption("say_query").getAsString());
                    break;
                case "music":
                    action(event);
                    break;
                case "report":
                    CmdReport.report(event, event.getOption("report_offender").getAsUser(), event.getOption("report_channel").getAsGuildChannel());
                    break;
                case "channel":
                    switch (event.getSubcommandName()) {
                        case "set":
                            serverbot.channel.ChannelType channelType = ChannelType.valueOf(event.getOption("channel_set_type").getAsString());
                            GuildChannel guildChannel = event.getOption("channel_set_channel").getAsGuildChannel();
                            CmdChannel.set(event, channelType, guildChannel);
                            break;
                    }
                    break;
                default:
                    event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
            }
        } catch (Exception ignored) {
        }
    }
    public static final int DEFAULT_VOLUME = 35; //(0 - 100, where 100 is default max volume)

    private final AudioPlayerManager playerManager;
    private final Map<String, GuildMusicManager> musicManagers;

    public SlashCommandHandler() {
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

    public void action(SlashCommandEvent event) throws Exception {
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
        String[] ids_music = {"486085339530788894", "486089278019993611", "486089728437780480", "441962292297596928", "393375474056953856"};
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
                        case "486085339530788894":
                            embed.setFooter("Player: Thorin", null);
                            if (addon_available.get(0).getOnlineStatus().equals(OnlineStatus.OFFLINE)) {
                                //InitMusicAddon.main(SECRETS.TOKENTHORIN, event, embed, userVoiceChannel);
                            } else {
                                if (event.getJDA().getSelfUser().getId().equals("486085339530788894")) {
                                    musicPlayer(event.getTextChannel(), event.getMember(), event.getGuild(), embed, userVoiceChannel, event);
                                }
                            }
                            break;
                        case "486089278019993611":
                            embed.setFooter("Player: Balin", null);
                            if (addon_available.get(0).getOnlineStatus().equals(OnlineStatus.OFFLINE)) {
                                //InitMusicAddon.main(SECRETS.TOKENBALIN, event, embed, userVoiceChannel);
                            } else {
                                if (event.getJDA().getSelfUser().getId().equals("486089278019993611")) {
                                    musicPlayer(event.getTextChannel(), event.getMember(), event.getGuild(), embed, userVoiceChannel, event);
                                }
                            }
                            break;
                        case "486089728437780480":
                            embed.setFooter("Player: Bombur", null);
                            if (addon_available.get(0).getOnlineStatus().equals(OnlineStatus.OFFLINE)) {
                                //InitMusicAddon.main(SECRETS.TOKENBOMBUR, event, embed, userVoiceChannel);
                            } else {
                                if (event.getJDA().getSelfUser().getId().equals("486089728437780480")) {
                                    musicPlayer(event.getTextChannel(), event.getMember(), event.getGuild(), embed, userVoiceChannel, event);
                                }
                            }
                            break;
                        case "441962292297596928":
                            embed.setFooter("Player: BilboT-BETA", null);
                            if (event.getJDA().getSelfUser().getId().equals("441962292297596928")) {
                                musicPlayer(event.getTextChannel(), event.getMember(), event.getGuild(), embed, userVoiceChannel, event);
                            }
                            break;
                        case "393375474056953856":
                            embed.setFooter("Player: BilboT", null);
                            if (event.getJDA().getSelfUser().getId().equals("393375474056953856")) {
                                musicPlayer(event.getTextChannel(), event.getMember(), event.getGuild(), embed, userVoiceChannel, event);
                            }
                            break;
                    }
                }
            }
        }
    }

    public void musicPlayer(TextChannel channel, Member member, Guild guild, EmbedBuilder embed, VoiceChannel userVoiceChannel, SlashCommandEvent event) {
        GuildMusicManager mng = getMusicManager(guild);
        AudioPlayer player = mng.player;
        TrackScheduler scheduler = mng.scheduler;
        switch (event.getSubcommandName()) {
            case "join":
                String id = null;
                id = event.getOption("music_join_channel").getAsString();

                VoiceChannel chan = null;
                try {
                    chan = guild.getVoiceChannelById(id);
                } catch (NumberFormatException ignored) {
                }

                if (chan == null)
                    chan = guild.getVoiceChannelsByName(id, true).stream().findFirst().orElse(null);
                if (chan == null) {
                    embed.setDescription("Could not find VoiceChannel by name: " + id);
                    event.replyEmbeds(embed.build()).queue();
                } else {
                    guild.getAudioManager().setSendingHandler(mng.sendHandler);

                    try {
                        guild.getAudioManager().openAudioConnection(chan);
                    } catch (PermissionException e) {
                        if (e.getPermission() == Permission.VOICE_CONNECT) {
                            embed.setDescription("The bot does not have permission to connect to: " + chan.getName());
                            event.replyEmbeds(embed.build()).queue();
                        }
                    }
                }
                embed.setDescription("Connected to " + chan.getName());
                event.replyEmbeds(embed.build()).queue();
                break;
            case "leave":
                guild.getAudioManager().setSendingHandler(null);
                guild.getAudioManager().closeAudioConnection();
                break;
            case "play":
                String url = null;
                try {
                    url = event.getOption("music_play_url").getAsString();
                } catch (Exception ignored) {}
                if (url == null) //It is only the command to start playback (probably after pause)
                {
                    if (player.isPaused()) {
                        player.setPaused(false);
                        embed.setDescription("Playback as been resumed.");
                        event.replyEmbeds(embed.build()).queue();
                    } else if (player.getPlayingTrack() != null) {
                        embed.setDescription("Player is already playing!");
                        event.replyEmbeds(embed.build()).queue();
                    } else if (scheduler.queue.isEmpty()) {
                        embed.setDescription("The current audio queue is empty! Add something to the queue first!");
                        event.replyEmbeds(embed.build()).queue();
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
                                embed.setDescription("BilboT does not have permission to connect to: " + userVoiceChannel.getName());
                                event.replyEmbeds(embed.build()).queue();
                            }
                        }
                    }
                    loadAndPlay(mng, channel, url, member.getUser(), false, event);
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
                            embed.setDescription("BilboT does not have permission to connect to: " + userVoiceChannel.getName());
                            event.replyEmbeds(embed.build()).queue();
                        }
                    }
                }
                loadAndPlay(mng, channel, event.getOption("music_pplay_url").getAsString(), member.getUser(), true, event);
                break;
            case "skip":
                scheduler.nextTrack();
                embed.setDescription("The current track was skipped.");
                event.replyEmbeds(embed.build()).queue();
                break;
            case "pause":
                if (player.getPlayingTrack() == null) {
                    embed.setDescription("Cannot pause or resume player because no track is loaded for playing.");
                    event.replyEmbeds(embed.build()).queue();
                    return;
                }

                player.setPaused(!player.isPaused());
                if (player.isPaused()) {
                    embed.setDescription("The player has been paused.");
                    event.replyEmbeds(embed.build()).queue();
                } else {
                    embed.setDescription("The player has resumed playing.");
                    event.replyEmbeds(embed.build()).queue();
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
                event.replyEmbeds(embed.build()).queue();
                break;
            case "volume":
                if (PermissionChecker.checkRole(new Role[]{guild.getRolesByName("Vala", true).get(0)}, member) ||
                        PermissionChecker.checkRole(new Role[]{guild.getRolesByName("Leser", true).get(0)}, member)) {
                    int amount = -1;
                    try {
                        amount = (int) event.getOption("music_volume_amount").getAsLong();
                    } catch (Exception ignored) {}
                    if (amount == -1) {
                        embed.setDescription("Current player volume: **" + player.getVolume() + "**");
                        event.replyEmbeds(embed.build()).queue();
                    } else {
                        try {
                            int newVolume = Math.max(10, Math.min(100, amount));
                            int oldVolume = player.getVolume();
                            player.setVolume(newVolume);
                            embed.setDescription("Player volume changed from `" + oldVolume + "` to `" + newVolume + "`");
                            event.replyEmbeds(embed.build()).queue();
                        } catch (NumberFormatException e) {
                            embed.setDescription("`" + amount + "` is not a valid integer. (10 - 100)");
                            event.replyEmbeds(embed.build()).queue();
                        }
                    }
                } else {
                    PermissionChecker.noPower(event);
                }
                break;
            case "restart":
                AudioTrack track = player.getPlayingTrack();
                if (track == null)
                    track = scheduler.lastTrack;

                if (track != null) {
                    embed.setDescription("Restarting track: " + track.getInfo().title);
                    event.replyEmbeds(embed.build()).queue();
                    player.playTrack(track.makeClone());
                } else {
                    embed.setDescription("No track has been previously started, so the player cannot replay a track!");
                    event.replyEmbeds(embed.build()).queue();
                }
                break;
            case "repeat":
                scheduler.setRepeating(!scheduler.isRepeating());
                embed.setDescription("Player was set to: **" + (scheduler.isRepeating() ? "repeat" : "not repeat") + "**");
                event.replyEmbeds(embed.build()).queue();
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
                event.replyEmbeds(embed.build()).queue();
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
                    event.replyEmbeds(embed.build()).queue();
                } else {
                    embed.setDescription("The player is not currently playing anything!");
                    event.replyEmbeds(embed.build()).queue();
                }
                break;
            case "list":
                java.util.Queue<AudioTrack> queue = scheduler.queue;
                synchronized (queue) {
                    if (queue.isEmpty()) {
                        embed.setDescription("The queue is currently empty!");
                        event.replyEmbeds(embed.build()).queue();
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
                        event.replyEmbeds(embed.build()).queue();
                    }
                }
                break;
            case "shuffle":
                if (scheduler.queue.isEmpty()) {
                    embed.setDescription("The queue is currently empty!");
                    event.replyEmbeds(embed.build()).queue();
                    return;
                }

                scheduler.shuffle();
                embed.setDescription("The queue has been shuffled!");
                event.replyEmbeds(embed.build()).queue();
                break;
        }
    }

    private void loadAndPlay(GuildMusicManager mng, final MessageChannel channel, String url, User user, final boolean addPlaylist, SlashCommandEvent event) {
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
                event.replyEmbeds(embed.build()).queue();
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
                    event.replyEmbeds(embed.build()).queue();
                    tracks.forEach(mng.scheduler::queue);
                } else {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(new Color(33, 237, 90));
                    embed.setTitle("Music");
                    embed.setDescription("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")");
                    event.replyEmbeds(embed.build()).queue();
                    mng.scheduler.queue(firstTrack);
                }
            }

            @Override
            public void noMatches() {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(new Color(33, 237, 90));
                embed.setTitle("Music");
                embed.setDescription("Nothing found by " + trackUrl);
                event.replyEmbeds(embed.build()).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(new Color(33, 237, 90));
                embed.setTitle("Music");
                embed.setDescription("Could not play: " + exception.getMessage());
                event.replyEmbeds(embed.build()).queue();
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
}