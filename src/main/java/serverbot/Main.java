package serverbot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import serverbot.audio.PlayerControl;
import serverbot.commands.*;
import serverbot.core.commandHandler;
import serverbot.listeners.*;
import serverbot.util.Announcements;
import serverbot.util.SECRETS;
import serverbot.util.STATIC;
import serverbot.util.voiceActivity;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.boot.SpringApplication;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.*;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//TODO: comment fucking everything!

//TODO: welcome and leave messages (!)
//TODO: Verification + language (!)
//TODO: help (!)
//TODO: intro
//TODO: stats (!)
//TODO: exil-channel (!)

@SpringBootApplication
public class Main {
    private static JDABuilder builder;

    public static JDA jda;

    public static void main(String[] args) throws InterruptedException, IOException {
        SpringApplication.run(Main.class, args);

        builder = JDABuilder.create(SECRETS.TOKEN,
                GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                GatewayIntent.DIRECT_MESSAGE_TYPING,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.GUILD_BANS,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_INVITES,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGE_TYPING,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_MESSAGES);
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.of(Activity.ActivityType.DEFAULT, "/help | " + STATIC.VERSION));

        addListeners();
        addCommands();

        jda = null;

        try {
            jda = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        final JDA finalJDA = jda;

        assert jda != null;
        jda.awaitReady();

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            try {
                voiceActivity.giveVoiceActivity(finalJDA);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(ZonedDateTime.now());
        }, 0, 1, TimeUnit.MINUTES);

    }

    @Configuration
    @PropertySource("classpath:application.properties")
    @ComponentScan(basePackages = "serverbot")
    static class WebSecurityConfiguration  {

        /**
         * Session registry session registry.
         *
         * @return the session registry
         */
        @Bean
        public SessionRegistry sessionRegistry() {
            return new SessionRegistryImpl();
        }
    }

    private static void addCommands() {

        commandHandler.commands.put("help", new cmdHelp());

        commandHandler.commands.put("botinfo", new cmdBotinfo());

        commandHandler.commands.put("rules", new cmdRules());

        commandHandler.commands.put("chatclear", new cmdClear());
        commandHandler.commands.put("clear", new cmdClear());

        commandHandler.commands.put("talk", new cmdTalk());

        commandHandler.commands.put("report", new cmdReport());

        commandHandler.commands.put("search", new cmdSearch());

        commandHandler.commands.put("music", new PlayerControl());

        commandHandler.commands.put("intro", new cmdIntro());

        commandHandler.commands.put("ban", new cmdBan());

        commandHandler.commands.put("kick", new cmdKick());

        commandHandler.commands.put("statistik", new cmdStats());
        commandHandler.commands.put("statistic", new cmdStats());
        commandHandler.commands.put("statistics", new cmdStats());

        commandHandler.commands.put("stats", new cmdStats());

        commandHandler.commands.put("exil", new cmdExil());
        commandHandler.commands.put("exile", new cmdExil());

        commandHandler.commands.put("edit", new cmdEdit());

        commandHandler.commands.put("xp", new CmdXp());

        commandHandler.commands.put("2x", new cmd2x());

        commandHandler.commands.put("role", new CmdRole());

        commandHandler.commands.put("channel", new CmdChannel());

        // commands.put("apply", new cmdMinecraftApply());
        // commands.put("bewerben", new cmdMinecraftApply());
    }

    private static void addListeners() {
        builder.addEventListeners(new readyListener());
        builder.addEventListeners(new voiceListener());
        builder.addEventListeners(new commandsListener());
        builder.addEventListeners(new joinListener());
        builder.addEventListeners(new leaveListener());
        builder.addEventListeners(new reportListener());
        builder.addEventListeners(new banListener());
        builder.addEventListeners(new channelListener());
        builder.addEventListeners(new chatfilterListener());
        builder.addEventListeners(new introListener());
        builder.addEventListeners(new afkListener());
        builder.addEventListeners(new spamListener());
        builder.addEventListeners(new emptyChannelListener());
        builder.addEventListeners(new statisticsListener());
        builder.addEventListeners(new activityListener());
        builder.addEventListeners(new modReactionListener());
        builder.addEventListeners(new xpListener());
        builder.addEventListeners(new Announcements());
    }
}