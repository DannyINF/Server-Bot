package serverbot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import serverbot.audio.PlayerControl;
import serverbot.commands.*;
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
import java.sql.SQLException;
import java.time.*;
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

    public static void main(String[] args) throws InterruptedException {
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

        JDA jda = null;

        try {
            jda = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        final JDA finalJDA = jda;

        assert jda != null;
        jda.awaitReady();


        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Berlin"));
        ZonedDateTime nextActivityRun = now.withHour(3).withMinute(30).withSecond(30);
        if (now.compareTo(nextActivityRun) > 0)
            nextActivityRun = nextActivityRun.plusDays(1);

        Duration durationActivity = Duration.between(now, nextActivityRun);

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
    @ComponentScan(basePackages = "serverbot")
    static class WebSecurityConfiguration  {

        /**
         * Session registry session registry.
         *
         * @return the session registry
         */
        @Bean
        public SessionRegistry sessionRegistry() {
            System.out.println("session");
            return new SessionRegistryImpl();
        }
    }

    private static void addCommands() {

        serverbot.core.commandHandler.commands.put("help", new cmdHelp());

        serverbot.core.commandHandler.commands.put("botinfo", new cmdBotinfo());

        serverbot.core.commandHandler.commands.put("rules", new cmdRules());

        serverbot.core.commandHandler.commands.put("chatclear", new cmdClear());
        serverbot.core.commandHandler.commands.put("clear", new cmdClear());

        serverbot.core.commandHandler.commands.put("talk", new cmdTalk());

        serverbot.core.commandHandler.commands.put("report", new cmdReport());

        serverbot.core.commandHandler.commands.put("search", new cmdSearch());

        serverbot.core.commandHandler.commands.put("music", new PlayerControl());

        serverbot.core.commandHandler.commands.put("intro", new cmdIntro());

        serverbot.core.commandHandler.commands.put("ban", new cmdBan());

        serverbot.core.commandHandler.commands.put("kick", new cmdKick());

        serverbot.core.commandHandler.commands.put("statistik", new cmdStats());
        serverbot.core.commandHandler.commands.put("statistic", new cmdStats());
        serverbot.core.commandHandler.commands.put("statistics", new cmdStats());

        serverbot.core.commandHandler.commands.put("stats", new cmdStats());

        serverbot.core.commandHandler.commands.put("exil", new cmdExil());
        serverbot.core.commandHandler.commands.put("exile", new cmdExil());

        serverbot.core.commandHandler.commands.put("edit", new cmdEdit());

        serverbot.core.commandHandler.commands.put("activity", new cmdActivity());

        serverbot.core.commandHandler.commands.put("shutdown", new cmdShutdown());

        serverbot.core.commandHandler.commands.put("xp", new cmdXp());

        serverbot.core.commandHandler.commands.put("2x", new cmd2x());

        //commandHandler.commands.put("apply", new cmdMinecraftApply());
        //commandHandler.commands.put("bewerben", new cmdMinecraftApply());
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
        builder.addEventListeners(new verificationListener());
        builder.addEventListeners(new activityListener());
        builder.addEventListeners(new modReactionListener());
        builder.addEventListeners(new xpListener());
        builder.addEventListeners(new Announcements());
    }
}