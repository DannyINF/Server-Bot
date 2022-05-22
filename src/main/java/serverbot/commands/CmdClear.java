package serverbot.commands;

import serverbot.core.MessageActions;
import serverbot.core.PermissionChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class CmdClear implements Command {

    @Override
    public boolean called() {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
            // only members with the permission "ADMINISTRATOR" are able to perform this command
            if (PermissionChecker.checkPermission(new Permission[]{Permission.ADMINISTRATOR}, event.getMember())) {
                //getting number of msgs that shall be deleted
                int num = Integer.parseInt(args[0]);
                try {
                    // getting all msgs of the textchannel
                    MessageHistory history = new MessageHistory(event.getChannel());

                    event.getMessage().delete().queue();

                    history.retrievePast(num).queue(list -> event.getChannel().purgeMessages(list));

                    // preparing and sending msg (information: how many msgs were deleted)
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setTitle(MessageActions.getLocalizedString("clear_deleted_title",
                            "user", event.getAuthor().getId()));
                    embed.setColor(Color.red);
                    embed.setDescription(MessageActions.getLocalizedString("clear_deleted_msg",
                                    "user", event.getAuthor().getId())
                            .replace("[COUNT]", args[0]));
                    event.getChannel(). sendMessageEmbeds(embed.build()).queue(msg -> msg.delete().queueAfter(3, TimeUnit.SECONDS));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                PermissionChecker.noPower(event.getChannel(), Objects.requireNonNull(event.getMember()));
            }
    }
}
