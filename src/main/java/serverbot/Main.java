package serverbot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
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
        builder.setActivity(Activity.of(Activity.ActivityType.PLAYING, "/help | " + STATIC.VERSION));

        addListeners();

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
        builder.addEventListeners(new ModReactionListener());
        builder.addEventListeners(new XpListener());
        builder.addEventListeners(new Announcements());
        builder.addEventListeners(new SlashCommandHandler());
    }

    private static void addSlashCommands(JDA jda) {
        Guild beta = jda.getGuildById(712664262102745159L);
        CommandListUpdateAction commands = beta.updateCommands();

        jda.updateCommands()
                .addCommands(Commands.slash("2x", "Aktiviert oder deaktiviert das Doppel-XP-Event.")
                        .addOption(INTEGER, "2x_amount", "Multiplikator (wenn nicht gegeben, dann 2)"))
                .addCommands(Commands.slash("ban", "Bannt einen Nutzer.")
                        .addOption(USER, "ban_user", "Nutzer, der gebannt werden soll.", true)
                        .addOption(INTEGER, "ban_del_days", "L\u00F6scht die Nachrichten der letzten Tage.")
                        .addOption(STRING, "ban_reason", "Grund f\u00FCr den Ban."))
                .addCommands(Commands.slash("botinfo", "Gibt Informationen zum Bot aus."))
                .addCommands(Commands.slash("channel", "Setzt Channel.")
                        .addSubcommands(new SubcommandData("set", "Setzt einen Channeltyp.")
                                .addOptions(new OptionData(STRING, "channel_set_type", "Channeltyp", true)
                                        .addChoice("Log", "log").addChoice("Modlog", "modlog").addChoice("Spam", "spam")
                                        .addChoice("Voicelog", "voicelog").addChoice("CMDlog", "cmdlog"))
                                .addOption(CHANNEL, "channel_set_channel", "Channel", true))
                        .addSubcommands(new SubcommandData("change_xp_multiplier", "Ändert den Xp-Multiplier des Channels.")
                                .addOption(NUMBER, "channel_xp_multiplier", "Der neue Xp-Multiplier des Channels.", true)
                                .addOption(CHANNEL, "channel_xp_channel", "Der Channel, dessen Xp-Multiplier geändert werden soll.", true)))
                /*
                commands.addCommands(Commands.slash("clear", "L\u00F6scht Nachrichten aus diesem Channel.")
                                .addOption(INTEGER, "clear_amount", "Wie viele Nachrichten gel\u00F6scht werden sollen. (Standard: 100)"))
                );
                */
                .addCommands(Commands.slash("credits", "Gibt die Credits eines Nutzers aus.")
                        .addSubcommands(
                                new SubcommandData("give", "F\u00FCge einem Nutzer Credits hinzu.")
                                        .addOption(INTEGER, "credits_give_amount", "Anzahl an Credits", true)
                                        .addOption(USER, "credits_give_user", "Nutzer, der die Credits erhalten soll.", true))
                        .addSubcommands(
                                new SubcommandData("gift", "Schenke einem Nutzer einen Teil deiner eigenen Credits.")
                                        .addOptions(new OptionData(INTEGER, "credits_gift_amount", "Anzahl an Credits").setMinValue(1L).setRequired(true))
                                        .addOption(USER, "credits_gift_user", "Nutzer, der die Credits erhalten soll.", true))
                        .addSubcommands(
                                new SubcommandData("get", "Gibt die Credits aus.")
                                        .addOption(USER, "credits_user", "Credits des Nutzers", true)))
                .addCommands(Commands.slash("exil", "Exiliert oder deexiliert einen Nutzer.")
                        .addOption(USER, "exil_user", "De/exiliert diesen Nutzer.", true)
                        .addOption(STRING, "exil_reason", "Gibt den Grund an."))
                /*
                commands.addCommands(Commands.slash("help", "Gibt die Hilfe f\u00FCr verschiedene Themen aus.")
                                .addOption(STRING, "help_topic", "Zeigt Hilfe zu diesem Suchbegriff an."))

                );
                */
                .addCommands(Commands.slash("kick", "Kickt einen Nutzer.")
                        .addOption(USER, "kick_user", "Nutzer, der gekickt werden soll.", true)
                        .addOption(STRING, "kick_reason", "Grund f\u00FCr den Kick."))
                .addCommands(Commands.slash("mute", "Ent/mutet einen Voicechannel, optional mit Ausnahmen.")
                        .addOption(CHANNEL, "mute_channel", "Channel, in dem ge(ent)muted werden soll.", true)
                        .addOption(ROLE, "mute_role", "Rolle, die nicht gemuted werden soll.")
                        .addOption(USER, "mute_user", "Nutzer, der nicht gemutet werden soll."))
                .addCommands(Commands.slash("report", "Erstellt einen Report")
                        .addOption(USER, "report_offender", "Nutzer, den du melden möchtest.", true)
                        .addOption(CHANNEL, "report_channel", "Kanal, in welchem die Situation vorfiel.", true))
                /*
                commands.addCommands(Commands.slash("role", "Rollenmanagement")
                                .addSubcommands(new SubcommandData("add", "Vergibt eine Rolle")
                                        .addOption(STRING, "role_add_role", "Name der Rolle")
                                                .setRequired(true)))
                                .addSubcommands(new SubcommandData("remove", "Entfernt eine Rolle")
                                        .addOption(STRING, "role_remove_role", "Name der Rolle")
                                                .setRequired(true)))
                                .addSubcommands(new SubcommandData("create", "Erstellt eine Rolle")
                                        .addOption(STRING, "role_create_role", "Name der Rolle")
                                                .setRequired(true)))
                                .addSubcommands(new SubcommandData("delete", "L\u00F6scht eine Rolle")
                                        .addOption(STRING, "role_delete_role", "Name der Rolle")
                                                .setRequired(true)))
                                .addSubcommands(new SubcommandData("list", "Listet alle Rollen auf"))
                );
                */
                .addCommands(Commands.slash("stats", "Gibt Statistiken aus.")
                        .addOption(USER, "stats_user", "Statistiken dieses Nutzers", true))
                .addCommands(Commands.slash("say", "L\u00E4sst den Bot reden.")
                        .addOption(STRING, "say_query", "Was der Bot sagen soll.", true))
                .addCommands(Commands.slash("xp", "Gibt deine XP aus.")
                        .addSubcommands(
                                new SubcommandData("ranking", "Gibt eine XP-Rangliste aus. (Standard: eigene Platzierung)")
                                        .addOption(INTEGER, "xp_rank", "Rangliste ab dieser Platzierung."))
                        .addSubcommands(
                                new SubcommandData("give", "Vergibt XP an einen Nutzer")
                                        .addOption(USER, "xp_give_user", "Nutzer, der XP erhalten soll.", true)
                                        .addOption(INTEGER, "xp_give_amount", "Anzahl an vergebenen XP", true))
                        .addSubcommands(
                                new SubcommandData("next", "Zeigt an, wie viele XP zum n\u00E4chsten Level und zum n\u00E4chsten Rang ben\u00F6tigt werden.")
                                        .addOption(USER, "xp_next_user", "F\u00FCr diesen Nutzer.", true))
                        .addSubcommands(
                                new SubcommandData("get", "Gibt die XP aus.")
                                        .addOption(USER, "xp_user", "XP des Nutzers", true)))
                .addCommands(Commands.slash("music", "Musikbefehl")
                        .addSubcommands(
                                new SubcommandData("join", "Tritt einem Voicechannel bei.")
                                        .addOption(CHANNEL, "music_join_channel", "Channel oder ID", true))
                        .addSubcommands(
                                new SubcommandData("leave", "Verl\u00E4sst einen Voicechannel."))
                        .addSubcommands(
                                new SubcommandData("play", "Spielt einen Track ab.")
                                        .addOption(STRING, "music_play_url", "Link"))
                        .addSubcommands(
                                new SubcommandData("pplay", "F\u00FCgt eine Playlist hinzu.")
                                        .addOption(STRING, "music_pplay_url", "Link", true))
                        .addSubcommands(
                                new SubcommandData("skip", "Skipt einen Track."))
                        .addSubcommands(
                                new SubcommandData("pause", "Pausiert einen Track oder beendet eine Pausierung."))
                        .addSubcommands(
                                new SubcommandData("stop", "Stoppt den Abspieler und entleert die Liste."))
                        .addSubcommands(
                                new SubcommandData("volume", "Gibt die Lautst\u00E4rke zur\u00FCck oder setzt diese auf einen Wert.")
                                        .addOption(INTEGER, "music_volume_amount", "Lautst\u00E4rke von 10 - 100"))
                        .addSubcommands(
                                new SubcommandData("restart", "Startet den spielenden Track neu."))
                        .addSubcommands(
                                new SubcommandData("repeat", "Setzt den Abspieler in einer Schleife oder beendet diese."))
                        .addSubcommands(
                                new SubcommandData("reset", "Setzt den Abspieler komplett zur\u00FCck."))
                        .addSubcommands(
                                new SubcommandData("info", "Gibt Informationen zum gerade spielenden Track aus."))
                        .addSubcommands(
                                new SubcommandData("list", "Gibt die Wiedergabeliste aus."))
                        .addSubcommands(
                                new SubcommandData("shuffle", "Mischt die Wiedergabeliste aus.")))
        .queue();

        CommandListUpdateAction global = jda.updateCommands();
        global.queue();
        commands.queue();
    }
}