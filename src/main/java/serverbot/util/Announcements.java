package serverbot.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import serverbot.server.Server;
import serverbot.server.ServerManagement;
import serverbot.statistics.StatisticsManagement;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

public class Announcements extends ListenerAdapter {

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        ServerManagement serverManagement = SpringContextUtils.getBean(ServerManagement.class);
        Server server = serverManagement.findById(event.getGuild().getId()).get();
        int i = server.getAnnouncementsCounter();
        if (i >= 250) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTimestamp(Instant.now());
            embed.setColor(Color.decode("#4809bd"));
            int random = ThreadLocalRandom.current().nextInt(0, 2);
            switch (random) {
                case 0:
                    embed.setTitle("Kanalmitgliedschaft");
                    embed.setDescription("Ihr wollt unseren Kanal unterst\u00fctzen und exklusive Vorteile erhalten?\n" +
                            "Dann checkt doch mal die Kanalmitgliedschaft ab! " +
                            "https://www.youtube.com/channel/UC0sD7NjEczsUeeqA74-aV6w/join");
                    break;
                case 1:
                    String finn = event.getGuild().getMemberById("450218791972896769").getAsMention();
                    String manager = event.getGuild().getRoleById("874019771962777732").getAsMention();
                    embed.setTitle("Minecraft-Projekt");
                    embed.setDescription("Wir bauen im Moment ein Minecraft-Roleplay-Projekt auf, welches in Mittelerde " +
                            "spielen soll. Wenn ihr bauen, programmieren oder irgendwie anders mithelfen wollt, " +
                            "dann wendet euch an " + finn + ", unseren " + manager + ".");
                    break;
            }
            if (!event.getChannel().getName().toLowerCase().contains("offtopic")) {
                serverManagement.changeAnnouncementsCounterBy(event.getGuild().getId(), 1);
            } else {
                serverManagement.changeAnnouncementsCounterBy(event.getGuild().getId(), -i);
                event.getChannel().sendMessageEmbeds(embed.build()).queue();
            }

        } else {
            serverManagement.changeAnnouncementsCounterBy(event.getGuild().getId(), 1);
        }
    }
}
