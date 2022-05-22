package serverbot.commands;

import serverbot.core.PermissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Objects;

public class CmdRules implements Command {
    @Override
    public boolean called() {
        return false;
    }

    //TODO: Update to database-usage
    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (PermissionChecker.checkPermission(new Permission[]{Permission.ADMINISTRATOR}, event.getMember())) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.red);
                String paragraf1 =
                        "1. Dieses Regelwerk gilt auf dem gesamten Discord-Server und ist immer und \u00FCberall g\u00FCltig.\n" +
                                "2. F\u00FCr einzelne Kan\u00E4le gelten besondere Regeln, diese sind dann im jeweiligen Kanalthema zu finden. Im Zweifelsfall gelten immer die normalen Regeln.\n" +
                                "3. Unwissenheit \u00FCber Regeln sch\u00FCtzt in keinem Fall vor Strafen, wenn es Unklarheiten gibt sind die Moderatoren oder Administratoren zu befragen.\n";
                String paragraf2 =
                        "1. Strafen werden von Moderatoren oder Administratoren ausgesprochen und sind in jedem Fall zu respektieren!\n" +
                                "2. Verwarnungen werden bei normalen Strafen immer vergegeben. Durch diese wird klar warum man eine Strafe erhalten hat. Eine Verwarnung kann auch ohne eine weitere Strafe ausgesprochen werden, ob und wie eine Verwarnung mit einer anderen Strafe kombiniert wird liegt am jeweiligen Vergehen, der Notwendigkeit und schlussendlich im Ermessen des zust\u00E4ndigen Moderators.\n";
                String paragraf3 =
                        "1. F\u00FCr jegliche Gespr\u00E4che ist immer m\u00F6glichst der passende Kanal zu w\u00E4hlen.\n" +
                                "2. Beleidigungen sind, egal ob sie ernst gemeint sind oder nicht, verboten und werden je nach H\u00E4rte bestraft.\n" +
                                "3. Spam von Buchstaben, Emoji oder Reactions ist genauso wie der \u00FCberm\u00E4\u00DFige Gebrauch von Capslock zu vermeiden.\n" +
                                "4. Provokationen sind nicht erlaubt.\n" +
                                "5. Jegliche Rassistische, Sexistische oder andere hetzerische Aussagen gegen einzelne Menschen oder Menschengruppen sind strengstens verboten. (Au\u00DFnahmen gelten im NSFW-Channel und nach Absprache mit einem Moderator/Administrator!)\n" +
                                "6. Werbung f\u00FCr andere Discords, Youtube-Kan\u00E4le, etc. ist verboten. Links zu Videos d\u00FCrfen gerne gesendet werden, aber nur im Video-Channel.\n" +
                                "7. Pornographische Links, Bilder, Videos etc. sind allerstrengstens verboten. F\u00FCr den YouTube-Feed k\u00F6nnt ihr euch bei der Administration bewerben!\n" +
                                "8. Das Senden von unsicheren Links wie Scam-Seiten, Viren oder \u00E4hnlichem ist strengstens untersagt.\n" +
                                "9. Unn\u00F6tige und vor allem unsachliche Diskussionen sind zu unterlassen.\n" +
                                "10. Das ver\u00F6ffentlichen von privaten und sensiblen Daten ohne Einverst\u00E4ndnis der jeweiligen Person ist allerstrengstens verboten.\n" +
                                "11. Das absichtliche Verbreiten von falschen Informationen um Leute zu sch\u00E4digen ist untersagt.\n" +
                                "12. Unn\u00F6tiges und sehr oftes Markieren von anderen Usern wird nicht geduldet.\n" +
                                "13. Das absichtliche und offensichtliche Nerven von anderen Nutzern ist ebenfalls verboten.\n" +
                                "14. Es ist stehts auf eine gute und freundliche Wortwahl zu achten, unn\u00F6tiges verwenden von unpassenden W\u00F6rtern ist somit verboten.\n";
                String paragraf4 =
                        "1. Beleidigungen, Provokationen, Rassistische so wie andere abwertende \u00E4u\u00DFerungen anderen gegen\u00FCber sind untersagt.\n" +
                                "2. Das Abspielen von Musik ist nur mit der Zustimmung aller im Kanal anwesenden Personen erlaubt, gleiches gilt f\u00FCr Soundboards.\n" +
                                "3. Auf eine angenehme und ertr\u00E4gliche Audioqualit\u00E4t ist zu achten.\n" +
                                "4. Das Abspielen oder Erzeugen von Lauten oder unangenehmen Ger\u00E4uschen ist verboten.\n" +
                                "5. Wiederholtes Betreten und Verlassen der Kan\u00E4le ist ebenso untersagt.";
                String paragraf5 =
                        "1. Den Moderatoren und Moderatoren ist in jedem Fall  Folge zu leisten, bei Problemen mit der Leitung gilt es sich an die Head-Moderatoren (Gil-galad und Moorhuhn) zu wenden.\n" +
                                "2. Wenn Hilfe gebraucht wird, so meldet man sich bitte mit @Moderator bzw. @Il\u00FBvatar und schreibt unbedingt direkt dahinter was man braucht. Die Sinnhaftigkeit jeder Verwendung der Markierung ist in jedem Fall zu bedenken, da man damit immer das gesamte Team markiert. Missbrauch sowie falsches Markieren ist zu vermeiden und kann gegebenenfalls auch zu Strafen f\u00FChren.\n" +
                                "3. Das Botten von XP durch so genannte Selfbots ist nicht erlaubt.";

                embed.setTitle("\u00A71 G\u00FCltigkeit des Regelwerks\n");
                embed.setDescription(paragraf1);
                event.getChannel(). sendMessageEmbeds(embed.build()).queue();
                embed.setTitle("\u00A72 Durchsetzung des Regelwerks\n");
                embed.setDescription(paragraf2);
                event.getChannel(). sendMessageEmbeds(embed.build()).queue();
                embed.setTitle("\u00A73 Verhalten in Text Channels\n");
                embed.setDescription(paragraf3);
                event.getChannel(). sendMessageEmbeds(embed.build()).queue();
                embed.setTitle("\u00A74 Verhalten in Voice Channels\n");
                embed.setDescription(paragraf4);
                event.getChannel(). sendMessageEmbeds(embed.build()).queue();
                embed.setTitle("\u00A75 Sonstiges\n");
                embed.setDescription(paragraf5);
                event.getChannel(). sendMessageEmbeds(embed.build()).queue();


        } else {
            PermissionChecker.noPower(event.getChannel(), Objects.requireNonNull(event.getMember()));
        }


    }


}
