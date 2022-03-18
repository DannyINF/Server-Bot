package serverbot.core;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import serverbot.commands.*;

public class SlashCommandHandler extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        // Only accept commands from guilds
        if (event.getGuild() == null)
            return;

        try {
            switch (event.getName()) {
                case "2x":
                    Cmd2x.TwoX(event);
                    break;
                case "ban":
                    User user = event.getOption("ban_user").getAsUser();
                    int del_days = (int) event.getOption("ban_del_days").getAsLong();
                    String reason = event.getOption("ban_reason").getAsString();
                    CmdBan.ban(event, user, del_days, reason);
                    break;
                case "botinfo":
                    CmdBotinfo.botinfo(event);
                    break;
                    /*
                    TODO: implement music
                case "music":
                    action(event);
                    */
                default:
                    event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
            }
        } catch (Exception ignored) {
        }

    }
}