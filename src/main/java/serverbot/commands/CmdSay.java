package serverbot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import serverbot.core.PermissionChecker;

public class CmdSay {

    public static void say(SlashCommandInteractionEvent event, String query) {
        event.reply("Sending message: " + query).setEphemeral(true).queue(); // Let the user know we received the command before doing anything else
        if (event.getUser().getId().equals("277746420281507841")) {
            event.getChannel().sendMessage(query).queue();
        } else {
            PermissionChecker.noPower(event);
        }
    }


}
