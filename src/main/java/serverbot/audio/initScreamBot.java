package serverbot.audio;

import serverbot.listeners.VoiceListenerAddon;
import com.neovisionaries.ws.client.WebSocketFactory;
import serverbot.listeners.IntroListener;
import serverbot.listeners.ReadyListener;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class initScreamBot extends ListenerAdapter {
    private static JDABuilder builder;
    public static boolean isStarting;
    public static JDA screamJda;

    public static void main(String token, GuildVoiceJoinEvent event, VoiceChannel userVoiceChannel, String intro) throws InterruptedException {
        WebSocketFactory ws = new WebSocketFactory();
        ws.setVerifyHostname(false);

        builder = JDABuilder.create(token,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_PRESENCES);
        builder.setWebsocketFactory(ws);
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.of(Activity.ActivityType.PLAYING, " RAAAAAAAAAAAAAAAAAAAAAH"));

        addListeners();

        try {
            screamJda = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        assert screamJda != null;
        screamJda.awaitReady();

        Guild guild = null;
        VoiceChannel voiceChannel = null;

        try {
            guild = screamJda.getGuildById(event.getGuild().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            voiceChannel = screamJda.getVoiceChannelById(userVoiceChannel.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        IntroListener il = new IntroListener();
        il.playIntro(intro, guild, voiceChannel);
        isStarting = false;
    }

    private static void addListeners() {
        builder.addEventListeners(new ReadyListener());
        builder.addEventListeners(new IntroListener());
        builder.addEventListeners(new VoiceListenerAddon());
    }
}