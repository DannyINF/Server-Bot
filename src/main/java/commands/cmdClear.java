package commands;

import core.messageActions;
import core.permissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class cmdClear implements Command {

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
            // only members with the permission "ADMINISTRATOR" are able to perform this command
            if (permissionChecker.checkPermission(new Permission[]{Permission.ADMINISTRATOR}, event.getMember())) {
                //getting number of msgs that shall be deleted
                int num = Integer.parseInt(args[0]);
                try {
                    // getting all msgs of the textchannel
                    MessageHistory history = new MessageHistory(event.getChannel());

                    event.getMessage().delete().queue();

                    history.retrievePast(num).queue(list -> event.getChannel().purgeMessages(list));

                    // preparing and sending msg (information: how many msgs were deleted)
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setTitle(messageActions.getLocalizedString("clear_deleted_title", "user", event.getAuthor().getId()));
                    embed.setColor(Color.red);
                    embed.setDescription(messageActions.getLocalizedString("clear_deleted_msg", "user", event.getAuthor().getId())
                            .replace("[COUNT]", args[0]));
                    event.getChannel(). sendMessageEmbeds(embed.build()).queue(msg -> msg.delete().queueAfter(3, TimeUnit.SECONDS));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                permissionChecker.noPower(event.getChannel(), Objects.requireNonNull(event.getMember()));
            }
    }
}
