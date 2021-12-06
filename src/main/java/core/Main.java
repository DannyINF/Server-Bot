package core;

import audio.PlayerControl;
import commands.*;
import listeners.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import util.*;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.time.*;
import java.util.Objects;
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
class Main {
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

        updateSTATIC(jda);

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Berlin"));
        ZonedDateTime nextActivityRun = now.withHour(3).withMinute(30).withSecond(30);
        if (now.compareTo(nextActivityRun) > 0)
            nextActivityRun = nextActivityRun.plusDays(1);

        Duration durationActivity = Duration.between(now, nextActivityRun);
        long initialDelayActivity = durationActivity.getSeconds();

        ScheduledExecutorService schedulerActivity = Executors.newScheduledThreadPool(1);
        schedulerActivity.scheduleAtFixedRate(() ->
        {
            updateSTATIC(finalJDA);
            try {
                new ActivityChecker().activity(finalJDA);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //new ActivityChecker().kickUnverified();
        },
        initialDelayActivity,
        TimeUnit.DAYS.toSeconds(1),
        TimeUnit.SECONDS);

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

        commandHandler.commands.put("activity", new cmdActivity());

        commandHandler.commands.put("shutdown", new cmdShutdown());

        commandHandler.commands.put("xp", new cmdXp());

        commandHandler.commands.put("2x", new cmd2x());

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
        //builder.addEventListeners(new applicationMinecraftListener());
        builder.addEventListeners(new modReactionListener());
        builder.addEventListeners(new xpListener());
        builder.addEventListeners(new Announcements());
    }

    private static void updateSTATIC(JDA jda) {
        STATIC.setModlog(jda.getTextChannelById(640662809343950860L));
        STATIC.setSpam(jda.getTextChannelById(492410527302156318L));
        STATIC.setRules(jda.getTextChannelById(746003285235728447L));
        STATIC.setRegeln(jda.getTextChannelById(492410235995029506L));
        STATIC.setApply(jda.getTextChannelById(689960471352311825L));

        STATIC.setVerified(new Role[]{Objects.requireNonNull(jda.getRoleById(744515748524851200L))});
        STATIC.setExil(new Role[]{Objects.requireNonNull(jda.getRoleById(744518837763964959L))});
        STATIC.setDeutsch(new Role[]{Objects.requireNonNull(jda.getRoleById(744514162822086668L))});
        STATIC.setEnglish(new Role[]{Objects.requireNonNull(jda.getRoleById(744514369609531503L))});
        STATIC.setStar(new Role[]{Objects.requireNonNull(jda.getRoleById(492382918635683850L))});
        STATIC.setNewcomer(new Role[]{Objects.requireNonNull(jda.getRoleById(492383147170988060L))});

        STATIC.setGuild(jda.getGuildById(492379705748750337L));
    }
}