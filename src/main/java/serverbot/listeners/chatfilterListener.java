package serverbot.listeners;

import net.dv8tion.jda.api.entities.TextChannel;
import serverbot.channel.ChannelManagement;
import serverbot.channel.ChannelType;
import serverbot.core.messageActions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import serverbot.util.SpringContextUtils;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;


public class chatfilterListener extends ListenerAdapter {
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (!event.getAuthor().equals(event.getJDA().getSelfUser())) {
            ChannelManagement channelManagement = SpringContextUtils.getBean(ChannelManagement.class);
            TextChannel modlog = event.getGuild().getTextChannelById(channelManagement.findByServerIdAndChannelType(event.getGuild().getId(), ChannelType.MODLOG).stream().findFirst().get().getChannelId());
            String[] ban = {"pisser", "pissa", "hure", "fick", "fotze", "brezelsalzabpopler", "inzest", "bastard", "spast",
                    "wichser", "wixxer", "\u5350", "npd", "nsdap", "hitler", "hodenkobold", "arschloch", "Milf", "slut",
                    "Hurensohn", "Huhrensohn", "Mongo", "gay", "Schwuchtel", "Neger", "Nigga"};
            String[] pardon = {"secret"};
            StringBuilder sb = new StringBuilder();
            boolean writeReport = false;
            for (String filter : ban) {
                if (event.getMessage().getContentRaw().toLowerCase().replace(" ", "").replace("\n", "").contains(filter.toLowerCase())) {
                    sb.append(filter).append(", ");
                    writeReport = true;
                }
            }
            for (String filter : pardon) {
                if (event.getMessage().getContentRaw().toLowerCase().replace(" ", "").replace("\n", "").contains(filter.toLowerCase()))
                    writeReport = false;
            }
            if (writeReport) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.RED);
                embed.setTitle(messageActions.getLocalizedString("report_auto_title", "server", event.getGuild().getId()));
                URL jump = null;
                try {
                    jump = new URL("https://discord.com/channels/" + event.getGuild().getId() + "/" + event.getChannel().getId() + "/" + event.getMessageId());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                embed.setDescription(messageActions.getLocalizedString("report_auto_msg", "server", event.getGuild().getId())
                        .replace("[CHANNEL]", event.getChannel().getAsMention())
                        .replace("[MESSAGE]", "`" + event.getMessage().getContentRaw() + "`")
                        .replace("[USER]", "**" + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator() + "**") +
                        "\n" + sb.toString().substring(0, sb.toString().length() - 2) +
                        "\n[jump](" + jump + ")");
                assert modlog != null;
                modlog. sendMessageEmbeds(embed.build()).queue();
            }
            /*if (event.getMessage().getContentRaw().toLowerCase().contains("#feanordidnothingwrong") &&
                    (event.getChannel().getId().equals("388970700372705280") ||
                    event.getChannel().getId().equals("453541314706014228") ||
                    event.getChannel().getId().equals("473261177397444620"))) {
                event.getAuthor().openPrivateChannel().queue(channel -> {
                            channel.sendMessage(">>> Deine Nachricht `" + event.getMessage().getContentRaw() + "` wurde aus dem Channel **" + event.getChannel().getName() + "** entfernt!").queue();
                            event.getMessage().delete().queue();
                        });
            }*/
        }
    }
}

//}
