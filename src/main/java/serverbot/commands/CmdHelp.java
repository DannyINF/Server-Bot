package serverbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class CmdHelp implements Command {

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        String Title;
        String Footer;
        String Description;

        String argument1;
        String argument2 = "";

        try {
            argument1 = args[0].replace("/", "").toLowerCase();
        } catch (Exception ignored) {
            argument1 = "help";
        }
        try {
            argument2 = args[1].replace("/", "").toLowerCase();
        } catch (Exception ignored) {
        }

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.LIGHT_GRAY);


        switch (argument1) {

            case "serverbot/commands":
            case "command":
                Title = "Liste aller Befehle";
                Description =
                        "`/ban`\n" +
                                "`/botinfo`\n" +
                                "`/clear`\n" +
                                "`/help`\n" +
                                "`/intro`\n" +
                                "`/kick`\n" +
                                "`/music`\n" +
                                "`/report`\n" +
                                "`/search`\n" +
                                "`/stats`\n";
                Footer = "Gib `/help <command>` ein, um die Hilfestellung f\u00fcr einen Befehl auszugeben.";
                break;
            case "features":
            case "feature":
                switch (argument2) {
                    case "voice":
                        Title = "Feature: Voice";
                        Description = "Beim Joinen in einen Voice bekommt man durch einen Rang Zugriff auf einen besonderen Chat, der nur f\u00fcr User im selben Voicechat zug\u00e4nglich ist.";
                        Footer = "Module: XP, Voice";
                        break;
                    case "welcome":
                        Title = "Feature: Welcome";
                        Description = "Wenn ein Nutzer auf den Server joint, wird eine Nachricht im Willkommenschat ausgegeben.";
                        Footer = "Module: Welcome";
                        break;
                    case "leaving":
                        Title = "Feature: Leaving";
                        Description = "Verl\u00e4sst der Nutzer den Server, so wird eine Nachricht ausgegeben. Dabei gibt es Unterscheidungen, ob der Nutzer freiwillig verl\u00e4sst, oder " +
                                "ob er gekickt oder gebannt wird.";
                        Footer = "Module: Leaving";
                        break;
                    default:
                        Title = "/help feature <feature>";
                        Description = "Es wird eine Hilfe zum angegebenen Feature <feature> ausgegeben. Als Feature stehen zur Auswahl:\n" +
                                "XP, Level, Voice, Login, Welcome, Leaving, Logs";
                        Footer = "Module: Help";
                        break;
                }
                break;

            case "ban":
                Title = "/ban <mention> <intervall> <reason>";
                Description = "Der markierte User <mention> wird mit dem Grund <reason> gebannt. Dabei werden die Nachrichten der letzten <intervall> Tage gel\u00f6scht.";
                Footer = "Module: Administration";
                break;
            case "kick":
                Title = "/kick <mention> <reason>";
                Description = "Der markierte User <mention> wird mit dem Grund <reason> gekickt.";
                Footer = "Module: Administration";
                break;

            case "help":
                Title = "/help <commands/features" +
                        //"/modules" +
                        ">";
                Description = "`/help commands` - Zeigt eine Liste aller Befehle an.\n`/help features` - Zeigt eine Liste aller Features an.\n" +
                        //"'/help modules` - Zeigt eine Lister aller Module an.\n\n" +
                        "\n`/help <command>` - Gibt die Hilfe zu einem bestimmten Befehl oder Unterbefehl <command> an.\n" +
                        "`/help feature <feature>` - Gibt eine Hilfestellung f\u00fcr ein bestimmtes Feature aus.\n" +
                        //"`/help module <module>` - Gibt eine Hilfestellung f\u00fcr ein bestimmtes Module aus.\n" +
                        "\n M\u00f6gliche Argumente werden dabei in geschweiften Klammern dargestellt {argument}, w\u00e4hrend zwingend notwendige Argumente in Guillemets angegeben werden <argument>.";
                Footer = "Module: Help";
                break;

            case "search":
                Title = "/search <site> <keywords>";
                Description = "Sucht auf einer Seite <site> nach einem Thema oder bestimmten Wortgruppen bzw. W\u00f6rtern <keywords>. Bisher sind die Seiten Wikipedia, lotr.fandom und Ardapedia " +
                        "verf\u00fcgbar.";
                Footer = "Module: Search";
                break;

            case "botinfo":
                Title = "/botinfo";
                Description = "Gibt Informationen zum Server-Bot aus.";
                Footer = "Module: Administration";
                break;

            case "clear":
                Title = "/clear <count>";
                Description = "Entfernt die letzten <count> Nachrichten aus einem Channel.";
                Footer = "Module: Administration";
                break;

            case "report":
                Title = "/report";
                Description = "Der Server-Bot kontaktiert dich, um ein Reportformular auszuf\u00fcllen.";
                Footer = "Module: Administration";
                break;

            case "stats":
                Title = "/stats {data.user}";
                Description = "Gibt alle nutzerrelevanten Statistiken an.";
                Footer = "Module: XP, Voice";
                break;

            case "music":
                switch (argument2) {
                    case "join":
                        Title = "/music join {channelname}";
                        Description = "Joint dem Channel {channelname}.";
                        Footer = "Module: Music";
                        break;
                    case "leave":
                        Title = "/music leave";
                        Description = "Verl\u00e4sst den Channel.";
                        Footer = "Module: Music";
                        break;
                    case "play":
                        Title = "/music play {url}";
                        Description = "Spielt den Song mit dem Link {url} ab. Ist kein Link angegeben, dann wird der pausierte Song fortgesetzt.";
                        Footer = "Module: Music";
                        break;
                    case "pplay":
                        Title = "/music pplay <url>";
                        Description = "Spielt eine Playlist mit dem Link <url> ab.";
                        Footer = "Module: Music";
                        break;
                    case "pause":
                        Title = "/music pause";
                        Description = "Pausiert den Track.";
                        Footer = "Module: Music";
                        break;
                    case "stop":
                        Title = "/music stop";
                        Description = "Stoppt das Abspielen von Musik.";
                        Footer = "Module: Music";
                        break;
                    case "skip":
                        Title = "/music skip";
                        Description = "Skipt einen Track.";
                        Footer = "Module: Music";
                        break;
                    case "now":
                    case "info":
                    case "nowplaying":
                        Title = "/music info";
                        Description = "Gibt Informationen zum aktuellen Track ab.";
                        Footer = "Module: Music";
                        break;
                    case "list":
                        Title = "/music list";
                        Description = "Gibt eine Liste aller folgenden Tracks aus.";
                        Footer = "Module: Music";
                        break;
                    case "volume":
                        Title = "/music volume {volume}";
                        Description = "Setzt die Lautst\u00e4rke auf {volume} Prozent (Zahl zwischen 10 und 100}. Ist {volume} nicht angegeben, wird die aktuelle Lautst\u00e4rke ausgegeben.";
                        Footer = "Module: Music";
                        break;
                    case "restart":
                        Title = "/music restart";
                        Description = "Startet den Track neu.";
                        Footer = "Module: Music";
                        break;
                    case "repeat":
                        Title = "/music repeat";
                        Description = "Wiederholt einen Track immer wieder oder hebt eine Wiederholung auf.";
                        Footer = "Module: Music";
                        break;
                    case "reset":
                        Title = "/music reset";
                        Description = "Setzt den Bot komplett zur\u00fcck.";
                        Footer = "Module: Music";
                        break;
                    default:
                        Title = "/music";
                        Description =
                                "`/music join {channelname}` - Joint dem Channel {channelname}.\n" +
                                        "`/music leave` - Verl\u00e4sst den Channel.\n" +
                                        "`/music play {url}` - Spielt den Song mit dem Link {url} ab. Ist kein Link angegeben, dann wird der pausierte Song fortgesetzt.\n" +
                                        "`/music pplay <url>` - Spielt eine Playlist mit dem Link <url> ab.\n" +
                                        "`/music pause` - Pausiert den Track.\n" +
                                        "`/music stop` - Stoppt das Abspielen von Musik.\n" +
                                        "`/music skip` - Skipt einen Track.\n" +
                                        "`/music info` - Gibt Informationen zum aktuellen Track ab.\n" +
                                        "`/music list` - Gibt eine Liste aller folgenden Tracks aus.\n" +
                                        "`/music volume {volume}` - Setzt die Lautst\u00e4rke auf {volume} Prozent (Zahl zwischen 10 und 100}. Ist {volume} nicht angegeben, wird die aktuelle Lautst\u00e4rke ausgegeben.\n" +
                                        "`/music restart` - Startet den Track neu.\n" +
                                        "`/music repeat` - Wiederholt einen Track immer wieder oder hebt eine Wiederholung auf.\n" +
                                        "`/music reset` - Setzt den Bot komplett zur\u00fcck.";
                        Footer = "Module: Music";
                        break;
                }
                break;

            case "intro":
                switch (argument2) {
                    case "set":
                        Title = "/intro set <intro>";
                        Description = "Das Intro <intro> wird ausgew\u00e4hlt und beim Joinen in einen Voicechat abgespielt.";
                        Footer = "Module: Intro";
                        break;
                    case "list":
                        Title = "/intro list";
                        Description = "Gibt eine Liste aller verf\u00fcgbaren Voiceintros aus.";
                        Footer = "Module: Intro";
                        break;
                    case "incentory":
                    case "cache":
                        Title = "/intro cache";
                        Description = "Gibt eine Liste aller Voiceintros aus, die sich in deinem Besitzt befinden.";
                        Footer = "Module: Intro";
                        break;
                    case "price":
                        Title = "/intro price";
                        Description = "Vermittelt Informationen zum Preis der Voiceintros.";
                        Footer = "Module: Intro";
                        break;
                    case "buy":
                        Title = "/intro buy <intro>";
                        Description = "Das Intro <intro> wird gekauft und dem Inventar hinzugef\u00fcgt. Gibt man eine Gruppe (common, rare, epic, legendary) an, so wird der normale Preis berechnet." +
                                "Beim Kauf eines bestimmten Intros (z.B. legendary-14) wird daf\u00fcr der **dreifache** Preis der Preisklasse abgezogen.";
                        Footer = "Module: Intro";
                        break;
                    default:
                    case "help":
                        Title = "/intro help";
                        Description = "Mit dem /intro Befehl kann man sich Voiceintros kaufen und diese verwalten, welche beim Joinen in einen Voicechat abgespielt werden.\n" +
                                "`/intro set <intro>` - Das Intro <intro> wird ausgew\u00e4hlt und beim Joinen in einen Voicechat abgespielt.\n" +
                                "`/intro list` - Gibt eine Liste aller verf\u00fcgbaren Voiceintros aus.\n" +
                                "`/intro cache` - Gibt eine Liste aller Voiceintros aus, die sich in deinem Besitzt befinden.\n" +
                                "`/intro price` - Vermittelt Informationen zum Preis der Voiceintros.\n" +
                                "`/intro buy <intro>` - Das Intro <intro> wird gekauft und dem Inventar hinzugef\u00fcgt. Gibt man eine Gruppe (common, rare, epic, legendary) an, " +
                                "so wird der normale Preis berechnet. Beim Kauf eines bestimmten Intros (z.B. legendary-14) wird daf\u00fcr der **dreifache** Preis der Preisklasse abgezogen.";
                        Footer = "Module: Intro";
                        break;
                }
                break;

            default:
                Title = argument1;
                Description = "Dieser Befehl existiert nicht oder es gibt keine Hilfestellung. Gib `/help help` an, um mehr \u00fcber diesen Befehl zu erfahren.";
                Footer = "Befehl nicht gefunden!";
                break;
        }

        embed.setTitle(Title);
        embed.setFooter(Footer, null);
        embed.setDescription(Description);

        event.getChannel(). sendMessageEmbeds(embed.build()).queue();
    }
}
