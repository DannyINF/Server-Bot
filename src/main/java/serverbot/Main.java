package serverbot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import serverbot.audio.PlayerControl;
import serverbot.commands.*;
import serverbot.core.CommandHandler;
import serverbot.core.SlashCommandHandler;
import serverbot.listeners.*;
import serverbot.util.Announcements;
import serverbot.util.SECRETS;
import serverbot.util.STATIC;
import serverbot.util.VoiceActivity;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

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

        addSlashCommands(jda);

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            try {
                VoiceActivity.giveVoiceActivity(finalJDA);
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

        CommandHandler.commands.put("help", new CmdHelp());

        CommandHandler.commands.put("rules", new CmdRules());

        CommandHandler.commands.put("chatclear", new CmdClear());
        CommandHandler.commands.put("clear", new CmdClear());

        CommandHandler.commands.put("report", new CmdReport());

        CommandHandler.commands.put("search", new CmdSearch());

        CommandHandler.commands.put("music", new PlayerControl());

        CommandHandler.commands.put("intro", new CmdIntro());

        CommandHandler.commands.put("edit", new CmdEdit());

        CommandHandler.commands.put("role", new CmdRole());

        CommandHandler.commands.put("channel", new CmdChannel());

        // commands.put("apply", new cmdMinecraftApply());
        // commands.put("bewerben", new cmdMinecraftApply());
    }

    private static void addListeners() {
        builder.addEventListeners(new ReadyListener());
        builder.addEventListeners(new VoiceListener());
        builder.addEventListeners(new CommandsListener());
        builder.addEventListeners(new JoinListener());
        builder.addEventListeners(new LeaveListener());
        builder.addEventListeners(new ReportListener());
        builder.addEventListeners(new BanListener());
        builder.addEventListeners(new ChannelListener());
        builder.addEventListeners(new ChatfilterListener());
        builder.addEventListeners(new IntroListener());
        builder.addEventListeners(new AfkListener());
        builder.addEventListeners(new SpamListener());
        builder.addEventListeners(new EmptyChannelListener());
        builder.addEventListeners(new StatisticsListener());
        builder.addEventListeners(new ActivityListener());
        builder.addEventListeners(new ModReactionListener());
        builder.addEventListeners(new XpListener());
        builder.addEventListeners(new Announcements());
        builder.addEventListeners(new SlashCommandHandler());
    }

    private static void addSlashCommands(JDA jda) {
        Guild beta = jda.getGuildById(712664262102745159L);
        CommandListUpdateAction commands = beta.updateCommands();

        commands.addCommands(
                new CommandData("2x", "Aktiviert oder deaktiviert das Doppel-XP-Event.")
                        .addOptions(new OptionData(INTEGER, "2x_amount", "Multiplikator (wenn nicht gegeben, dann 2)"))
        );

        commands.addCommands(
                new CommandData("ban", "Bannt einen Nutzer.")
                        .addOptions(new OptionData(USER, "ban_user", "Nutzer, der gebannt werden soll.")
                                .setRequired(true))
                        .addOptions(new OptionData(INTEGER, "ban_del_days", "L\u00F6scht die Nachrichten der letzten Tage."))
                        .addOptions(new OptionData(STRING, "ban_reason", "Grund f\u00FCr den Ban."))
        );

        commands.addCommands(
                new CommandData("botinfo", "Gibt Informationen zum Bot aus.")
        );
        /*
        commands.addCommands(
                new CommandData("channel", "Setzt Channel.")
                        .addSubcommands(new SubcommandData("set", "Setzt einen Channeltyp.")
                                .addOptions(new OptionData(STRING, "channel_set_type", "Channeltyp")
                                        .addChoice("Log", "log").addChoice("Modlog", "modlog").addChoice("Spam", "spam")
                                        .addChoice("Voicelog", "voicelog").addChoice("CMDlog", "cmdlog").setRequired(true))
                                .addOptions(new OptionData(CHANNEL, "channel_channel", "Channel").setRequired(true)))
        );

        commands.addCommands(
                new CommandData("clear", "L\u00F6scht Nachrichten aus diesem Channel.")
                        .addOptions(new OptionData(INTEGER, "clear_amount", "Wie viele Nachrichten gel\u00F6scht werden sollen. (Standard: 100)"))
        );
        */
        commands.addCommands(
                new CommandData("credits", "Gibt die Credits eines Nutzers aus.")
                        .addSubcommands(
                                new SubcommandData("give", "F\u00FCge einem Nutzer Credits hinzu.")
                                        .addOptions(new OptionData(INTEGER, "credits_give_amount", "Anzahl an Credits").setRequired(true))
                                        .addOptions(new OptionData(USER, "credits_give_user", "Nutzer, der die Credits erhalten soll.").setRequired(true))
                        )
                        .addSubcommands(
                                new SubcommandData("gift", "Schenke einem Nutzer einen Teil deiner eigenen Credits.")
                                        .addOptions(new OptionData(INTEGER, "credits_gift_amount", "Anzahl an Credits").setMinValue(1L).setRequired(true))
                                        .addOptions(new OptionData(USER, "credits_gift_user", "Nutzer, der die Credits erhalten soll.").setRequired(true))
                        )
                        .addSubcommands(
                                new SubcommandData("get", "Gibt die Credits aus.")
                                        .addOptions(new OptionData(USER, "credits_user", "Credits des Nutzers").setRequired(true))
                        )
        );

        commands.addCommands(
                new CommandData("exil", "Exiliert oder deexiliert einen Nutzer.")
                        .addOptions(new OptionData(USER, "exil_user", "De/exiliert diesen Nutzer.").setRequired(true))
                        .addOptions(new OptionData(STRING, "exil_reason", "Gibt den Grund an."))
        );
/*
        commands.addCommands(
                new CommandData("help", "Gibt die Hilfe f\u00FCr verschiedene Themen aus.")
                        .addOptions(new OptionData(STRING, "help_topic", "Zeigt Hilfe zu diesem Suchbegriff an."))

        );
*/
        commands.addCommands(
                new CommandData("kick", "Kickt einen Nutzer.")
                        .addOptions(new OptionData(USER, "kick_user", "Nutzer, der gekickt werden soll.")
                                .setRequired(true))
                        .addOptions(new OptionData(STRING, "kick_reason", "Grund f\u00FCr den Kick."))
        );
/*
        commands.addCommands(
                new CommandData("report", "Erstellt einen Report")
        );

        commands.addCommands(
                new CommandData("role", "Rollenmanagement")
                        .addSubcommands(new SubcommandData("add", "Vergibt eine Rolle")
                                .addOptions(new OptionData(STRING, "role_add_role", "Name der Rolle")
                                        .setRequired(true)))
                        .addSubcommands(new SubcommandData("remove", "Entfernt eine Rolle")
                                .addOptions(new OptionData(STRING, "role_remove_role", "Name der Rolle")
                                        .setRequired(true)))
                        .addSubcommands(new SubcommandData("create", "Erstellt eine Rolle")
                                .addOptions(new OptionData(STRING, "role_create_role", "Name der Rolle")
                                        .setRequired(true)))
                        .addSubcommands(new SubcommandData("delete", "L\u00F6scht eine Rolle")
                                .addOptions(new OptionData(STRING, "role_delete_role", "Name der Rolle")
                                        .setRequired(true)))
                        .addSubcommands(new SubcommandData("list", "Listet alle Rollen auf"))
        );
        */
        commands.addCommands(
                new CommandData("stats", "Gibt Statistiken aus.")
                        .addOptions(new OptionData(USER, "stats_user", "Statistiken dieses Nutzers").setRequired(true))
        );

        commands.addCommands(
                new CommandData("say", "L\u00E4sst den Bot reden.")
                        .addOptions(new OptionData(STRING, "say_query", "Was der Bot sagen soll.")
                                .setRequired(true))
        );

        commands.addCommands(
                new CommandData("xp", "Gibt deine XP aus.")
                        .addSubcommands(new SubcommandData("ranking", "Gibt eine XP-Rangliste aus. (Standard: eigene Platzierung)")
                                .addOptions(new OptionData(INTEGER, "xp_rank", "Rangliste ab dieser Platzierung.")))
                        .addSubcommands(new SubcommandData("give", "Vergibt XP an einen Nutzer")
                                .addOptions(new OptionData(USER, "xp_give_user", "Nutzer, der XP erhalten soll.").setRequired(true))
                                .addOptions(new OptionData(INTEGER, "xp_give_amount", "Anzahl an vergebenen XP").setRequired(true)))
                        .addSubcommands(new SubcommandData("next", "Zeigt an, wie viele XP zum n\u00E4chsten Level und zum n\u00E4chsten Rang ben\u00F6tigt werden.")
                                .addOptions(new OptionData(USER, "xp_next_user", "F\u00FCr diesen Nutzer.").setRequired(true)))
                        .addSubcommands(new SubcommandData("get", "Gibt die XP aus.")
                                .addOptions(new OptionData(USER, "xp_user", "XP des Nutzers").setRequired(true)))
        );
/*
        commands.addCommands(
                new CommandData("music", "Musikbefehl")
                        .addSubcommands(new SubcommandData("join", "Tritt einem Voicechannel bei.").addOptions(new OptionData(STRING, "music_join_channel", "Channel oder ID")))
                        .addSubcommands(new SubcommandData("leave", "Verl\u00E4sst einen Voicechannel."))
                        .addSubcommands(new SubcommandData("play", "Spielt einen Track ab.").addOptions(new OptionData(STRING, "music_play_url", "Link")))
                        .addSubcommands(new SubcommandData("pplay", "F\u00FCgt eine Playlist hinzu.").addOptions(new OptionData(STRING, "music_pplay_url", "Link").setRequired(true)))
                        .addSubcommands(new SubcommandData("skip", "Skipt einen Track."))
                        .addSubcommands(new SubcommandData("pause", "Pausiert einen Track oder beendet eine Pausierung."))
                        .addSubcommands(new SubcommandData("stop", "Stoppt den Abspieler und entleert die Liste."))
                        .addSubcommands(new SubcommandData("volume", "Gibt die Lautst\u00E4rke zur\u00FCck oder setzt diese auf einen Wert.").addOptions(new OptionData(INTEGER, "music_volume_amount", "Lautst\u00E4rke von 10 - 100")))
                        .addSubcommands(new SubcommandData("restart", "Startet den spielenden Track neu."))
                        .addSubcommands(new SubcommandData("repeat", "Setzt den Abspieler in einer Schleife oder beendet diese."))
                        .addSubcommands(new SubcommandData("reset", "Setzt den Abspieler komplett zur\u00FCck."))
                        .addSubcommands(new SubcommandData("info", "Gibt Informationen zum gerade spielenden Track aus."))
                        .addSubcommands(new SubcommandData("list", "Gibt die Wiedergabeliste aus."))
                        .addSubcommands(new SubcommandData("shuffle", "Mischt die Wiedergabeliste aus."))
        );*/
        CommandListUpdateAction global = jda.updateCommands();
        global.queue();
        commands.queue();
    }
}