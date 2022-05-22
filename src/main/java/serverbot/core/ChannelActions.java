package serverbot.core;

import net.dv8tion.jda.api.entities.BaseGuildMessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ChannelActions {
    public static BaseGuildMessageChannel getChannel(MessageReceivedEvent event, String name) {
        /*try {
            String id = Objects.requireNonNull(databaseHandler.database("serversettings", "select " + name + " from channels where id = '" + event.getGuild().getId() + "'"))[0];
            return event.getGuild().getTextChannelById(id);
        } catch (Exception e) {
            try {
                for (TextChannel tx : event.getGuild().getTextChannels()) {
                    if (tx.getName().contains("spam")) {
                        return tx;
                    }
                }
            } catch (Exception ignored) {
            }
        }*/
        return event.getGuild().getDefaultChannel();
    }

    public static BaseGuildMessageChannel getChannel(GuildMemberJoinEvent event, String name) {
        /*try {
            String id = Objects.requireNonNull(databaseHandler.database("serversettings", "select " + name + " from channels where id = '" + event.getGuild().getId() + "'"))[0];
            return event.getGuild().getTextChannelById(id);
        } catch (Exception e) {
            try {
                for (TextChannel tx : event.getGuild().getTextChannels()) {
                    if (tx.getName().contains("spam")) {
                        return tx;
                    }
                }
            } catch (Exception ignored) {
            }
        }*/
        return event.getGuild().getDefaultChannel();
    }
}
